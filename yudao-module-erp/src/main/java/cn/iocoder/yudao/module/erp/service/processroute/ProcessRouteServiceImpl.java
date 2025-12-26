package cn.iocoder.yudao.module.erp.service.processroute;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.math.BigDecimal;
import cn.iocoder.yudao.module.erp.controller.admin.processroute.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.processroute.ProcessRouteDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.processroute.ProcessRouteMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.processrouteitem.ProcessRouteItemMapper;
import cn.iocoder.yudao.module.erp.dal.dataobject.processrouteitem.ProcessRouteItemDO;
import cn.iocoder.yudao.module.erp.dal.redis.no.ErpNoRedisDAO;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 工艺路线主 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProcessRouteServiceImpl implements ProcessRouteService {

    @Resource
    private ProcessRouteMapper processRouteMapper;

    @Resource
    private ProcessRouteItemMapper processRouteItemMapper;

    @Resource
    private ErpNoRedisDAO noRedisDAO;

    /**
     * 根据工序明细计算工艺路线的标准周期时间、标准人工成本、标准制造费用
     */
    private void calculateRouteMetrics(ProcessRouteDO processRoute, List<ProcessRouteSaveReqVO.Item> items) {
        if (CollUtil.isEmpty(items)) {
            processRoute.setStandardCycleTime(0);
            processRoute.setStandardLaborCost(BigDecimal.ZERO);
            processRoute.setStandardOverheadCost(BigDecimal.ZERO);
            return;
        }

        // 计算标准周期时间 = 所有工序的标准工时之和（包括准备时间）
        int totalCycleTime = 0;
        BigDecimal totalLaborCost = BigDecimal.ZERO;
        BigDecimal totalOverheadCost = BigDecimal.ZERO;

        for (ProcessRouteSaveReqVO.Item item : items) {
            // 标准周期时间 = 标准工时 + 准备时间
            if (item.getStandardTime() != null) {
                totalCycleTime += item.getStandardTime();
            }
            if (item.getSetupTime() != null) {
                totalCycleTime += item.getSetupTime();
            }

            // 标准人工成本 = 标准工时（分钟） × 人工费率（元/小时） / 60
            if (item.getStandardTime() != null && item.getLaborRate() != null) {
                BigDecimal minutes = new BigDecimal(item.getStandardTime());
                BigDecimal hours = minutes.divide(new BigDecimal(60), 4, java.math.RoundingMode.HALF_UP);
                BigDecimal itemLaborCost = hours.multiply(item.getLaborRate());
                totalLaborCost = totalLaborCost.add(itemLaborCost);
            }

            // 标准制造费用 = 标准工时（分钟） × 制造费率（元/小时） / 60
            if (item.getStandardTime() != null && item.getOverheadRate() != null) {
                BigDecimal minutes = new BigDecimal(item.getStandardTime());
                BigDecimal hours = minutes.divide(new BigDecimal(60), 4, java.math.RoundingMode.HALF_UP);
                BigDecimal itemOverheadCost = hours.multiply(item.getOverheadRate());
                totalOverheadCost = totalOverheadCost.add(itemOverheadCost);
            }
        }

        processRoute.setStandardCycleTime(totalCycleTime);
        processRoute.setStandardLaborCost(totalLaborCost.setScale(2, java.math.RoundingMode.HALF_UP));
        processRoute.setStandardOverheadCost(totalOverheadCost.setScale(2, java.math.RoundingMode.HALF_UP));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProcessRoute(ProcessRouteSaveReqVO createReqVO) {
        // 如果工艺路线编号为空，自动生成
        String routeNo = createReqVO.getRouteNo();
        if (routeNo == null || routeNo.trim().isEmpty()) {
            routeNo = noRedisDAO.generate(ErpNoRedisDAO.PROCESS_ROUTE_NO_PREFIX);
        }
        
        // 校验编号唯一性
        if (processRouteMapper.selectByRouteNo(routeNo) != null) {
            throw exception(PROCESS_ROUTE_NO_EXISTS);
        }

        // 插入主表
        ProcessRouteDO processRoute = BeanUtils.toBean(createReqVO, ProcessRouteDO.class);
        processRoute.setRouteNo(routeNo);
        
        // 根据工序明细自动计算标准周期时间、标准人工成本、标准制造费用
        calculateRouteMetrics(processRoute, createReqVO.getItems());
        
        processRouteMapper.insert(processRoute);

        // 插入明细
        if (CollUtil.isNotEmpty(createReqVO.getItems())) {
            List<ProcessRouteItemDO> items = BeanUtils.toBean(createReqVO.getItems(), ProcessRouteItemDO.class);
            items.forEach(item -> {
                item.setRouteId(processRoute.getId());
                item.setId(null); // 确保是新增
            });
            processRouteItemMapper.insertBatch(items);
        }

        // 返回
        return processRoute.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProcessRoute(ProcessRouteSaveReqVO updateReqVO) {
        // 校验存在
        ProcessRouteDO existingRoute = validateProcessRouteExists(updateReqVO.getId());
        // 如果编号发生变化，校验新编号的唯一性
        if (updateReqVO.getRouteNo() != null && !updateReqVO.getRouteNo().equals(existingRoute.getRouteNo())) {
            ProcessRouteDO routeWithSameNo = processRouteMapper.selectByRouteNo(updateReqVO.getRouteNo());
            if (routeWithSameNo != null && !routeWithSameNo.getId().equals(updateReqVO.getId())) {
                throw exception(PROCESS_ROUTE_NO_EXISTS);
            }
        }
        // 更新主表
        ProcessRouteDO updateObj = BeanUtils.toBean(updateReqVO, ProcessRouteDO.class);
        
        // 根据工序明细自动计算标准周期时间、标准人工成本、标准制造费用
        calculateRouteMetrics(updateObj, updateReqVO.getItems());
        
        processRouteMapper.updateById(updateObj);

        // 更新明细
        updateProcessRouteItems(updateReqVO.getId(), updateReqVO.getItems());
    }

    @Override
    public void deleteProcessRoute(Long id) {
        // 校验存在
        ProcessRouteDO processRoute = validateProcessRouteExists(id);
        // 删除
        processRouteMapper.deleteById(id);
    }

    @Override
        public void deleteProcessRouteListByIds(List<Long> ids) {
        // 删除
        processRouteMapper.deleteByIds(ids);
        }


    private ProcessRouteDO validateProcessRouteExists(Long id) {
        ProcessRouteDO processRoute = processRouteMapper.selectById(id);
        if (processRoute == null) {
            throw exception(PROCESS_ROUTE_NOT_EXISTS);
        }
        return processRoute;
    }

    @Override
    public ProcessRouteDO getProcessRoute(Long id) {
        return processRouteMapper.selectById(id);
    }

    @Override
    public PageResult<ProcessRouteDO> getProcessRoutePage(ProcessRoutePageReqVO pageReqVO) {
        return processRouteMapper.selectPage(pageReqVO);
    }

    @Override
    public String generateRouteNo() {
        // 生成工艺路线编号，格式：GYLX + yyyyMMdd + 6位自增
        String routeNo = noRedisDAO.generate(ErpNoRedisDAO.PROCESS_ROUTE_NO_PREFIX);
        // 确保编号唯一性，如果已存在则重新生成
        int maxRetries = 10;
        int retryCount = 0;
        while (processRouteMapper.selectByRouteNo(routeNo) != null && retryCount < maxRetries) {
            routeNo = noRedisDAO.generate(ErpNoRedisDAO.PROCESS_ROUTE_NO_PREFIX);
            retryCount++;
        }
        return routeNo;
    }

    /**
     * 更新工艺路线明细
     *
     * @param routeId 工艺路线ID
     * @param newItems 新明细列表
     */
    private void updateProcessRouteItems(Long routeId, List<ProcessRouteSaveReqVO.Item> newItems) {
        // 1. 查询已有明细
        List<ProcessRouteItemDO> oldItems = processRouteItemMapper.selectListByRouteId(routeId);
        // 2. 如果新明细为空，删除所有旧明细
        if (CollUtil.isEmpty(newItems)) {
            processRouteItemMapper.deleteByRouteId(routeId);
            return;
        }
        // 3. 转换为DO
        List<ProcessRouteItemDO> newItemDOs = BeanUtils.toBean(newItems, ProcessRouteItemDO.class);
        // 4. 计算需要新增、修改、删除的明细
        Map<Long, ProcessRouteItemDO> oldItemMap = convertMap(oldItems, ProcessRouteItemDO::getId);
        List<ProcessRouteItemDO> insertItems = new ArrayList<>();
        List<ProcessRouteItemDO> updateItems = new ArrayList<>();
        for (ProcessRouteItemDO newItem : newItemDOs) {
            newItem.setRouteId(routeId);
            if (newItem.getId() != null && oldItemMap.containsKey(newItem.getId())) {
                // 更新
                updateItems.add(newItem);
            } else {
                // 新增
                newItem.setId(null);
                insertItems.add(newItem);
            }
        }
        // 5. 执行新增、修改
        if (CollUtil.isNotEmpty(insertItems)) {
            processRouteItemMapper.insertBatch(insertItems);
        }
        if (CollUtil.isNotEmpty(updateItems)) {
            updateItems.forEach(processRouteItemMapper::updateById);
        }
        // 6. 计算需要删除的明细
        Set<Long> newItemIds = convertSet(newItemDOs, item -> item.getId() != null ? item.getId() : 0L);
        List<Long> deleteItemIds = new ArrayList<>();
        for (ProcessRouteItemDO oldItem : oldItems) {
            if (!newItemIds.contains(oldItem.getId())) {
                deleteItemIds.add(oldItem.getId());
            }
        }
        if (CollUtil.isNotEmpty(deleteItemIds)) {
            processRouteItemMapper.deleteByIds(deleteItemIds);
        }
    }

}