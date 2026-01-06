package cn.iocoder.yudao.module.erp.service.workorder;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Lazy;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.workorder.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.workorder.WorkOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.workorder.WorkOrderMapper;
import cn.iocoder.yudao.module.erp.dal.redis.no.ErpNoRedisDAO;
import cn.iocoder.yudao.module.erp.service.productionorder.ProductionOrderService;
import cn.iocoder.yudao.module.erp.dal.mysql.productionorder.ProductionOrderMapper;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionorder.ProductionOrderDO;
import cn.iocoder.yudao.module.erp.dal.mysql.workorderprogress.WorkOrderProgressMapper;
import cn.iocoder.yudao.module.erp.dal.dataobject.workorderprogress.WorkOrderProgressDO;
import cn.iocoder.yudao.module.erp.service.workorderprogress.WorkOrderProgressService;
import cn.iocoder.yudao.module.erp.controller.admin.workorderprogress.vo.WorkOrderProgressSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.mysql.processrouteitem.ProcessRouteItemMapper;
import cn.iocoder.yudao.module.erp.dal.dataobject.processrouteitem.ProcessRouteItemDO;
import lombok.extern.slf4j.Slf4j;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 工单主 Service 实现类
 *
 * @author 芋道源码
 */
@Slf4j
@Service
@Validated
public class WorkOrderServiceImpl implements WorkOrderService {

    @Resource
    private WorkOrderMapper workOrderMapper;

    @Resource
    private ErpNoRedisDAO noRedisDAO;

    @Resource
    @Lazy
    private ProductionOrderService productionOrderService;

    @Resource
    private ProductionOrderMapper productionOrderMapper;
    
    @Resource
    private WorkOrderProgressMapper workOrderProgressMapper;
    
    @Resource
    private WorkOrderProgressService workOrderProgressService;
    
    @Resource
    private ProcessRouteItemMapper processRouteItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createWorkOrder(WorkOrderSaveReqVO createReqVO) {
        // 生成工单号，并校验唯一性
        String workOrderNo = noRedisDAO.generate(ErpNoRedisDAO.WORK_ORDER_NO_PREFIX);
        if (workOrderMapper.selectByWorkOrderNo(workOrderNo) != null) {
            throw exception(WORK_ORDER_NO_EXISTS);
        }

        // 插入
        WorkOrderDO workOrder = BeanUtils.toBean(createReqVO, WorkOrderDO.class);
        workOrder.setWorkOrderNo(workOrderNo);
        log.info("创建工单，工单号: {}, 工艺路线ID: {}, 生产订单ID: {}, 产品ID: {}, 请求VO中的routeId: {}", 
                workOrderNo, workOrder.getRouteId(), workOrder.getProductionOrderId(), workOrder.getProductId(), createReqVO.getRouteId());
        workOrderMapper.insert(workOrder);
        log.info("工单插入成功，工单ID: {}, 工艺路线ID: {}", workOrder.getId(), workOrder.getRouteId());

        // 如果关联了生产订单，且生产订单状态不是"进行中"，则自动更新为"进行中"
        if (workOrder.getProductionOrderId() != null) {
            try {
                // 获取生产订单当前状态
                ProductionOrderDO productionOrder = productionOrderMapper.selectById(workOrder.getProductionOrderId());
                if (productionOrder != null && productionOrder.getStatus() != null && productionOrder.getStatus() != 2) {
                    // 状态不是"进行中"（状态2），则直接使用 Mapper 更新为"进行中"
                    // 注意：这里直接使用 Mapper 更新，避免调用 updateProductionOrderStatus 再次触发自动创建工单的逻辑
                    ProductionOrderDO updateObj = new ProductionOrderDO();
                    updateObj.setId(workOrder.getProductionOrderId());
                    updateObj.setStatus(2); // 2-进行中
                    productionOrderMapper.updateById(updateObj);
                }
            } catch (Exception e) {
                // 忽略错误，不影响工单创建
            }
        }

        // 如果关联了工艺路线，根据工艺路线明细自动创建工单进度记录
        log.info("检查是否需要创建工单进度，工单ID: {}, workOrder.getRouteId(): {}, createReqVO.getRouteId(): {}", 
                workOrder.getId(), workOrder.getRouteId(), createReqVO.getRouteId());
        if (workOrder.getRouteId() != null) {
            try {
                log.info("开始根据工艺路线创建工单进度记录，工单ID: {}, 工艺路线ID: {}", workOrder.getId(), workOrder.getRouteId());
                List<ProcessRouteItemDO> routeItems = processRouteItemMapper.selectListByRouteId(workOrder.getRouteId());
                log.info("查询工艺路线明细结果，工艺路线ID: {}, 工序数量: {}", workOrder.getRouteId(), routeItems != null ? routeItems.size() : 0);
                if (CollUtil.isNotEmpty(routeItems)) {
                    log.info("找到 {} 个工序，开始创建工单进度记录", routeItems.size());
                    for (ProcessRouteItemDO routeItem : routeItems) {
                        WorkOrderProgressSaveReqVO progressReqVO = new WorkOrderProgressSaveReqVO();
                        progressReqVO.setWorkOrderId(workOrder.getId());
                        progressReqVO.setProcessId(routeItem.getProcessId());
                        progressReqVO.setProcessName(routeItem.getOperationName()); // 使用 operationName 作为 processName
                        progressReqVO.setSequence(routeItem.getSequence());
                        progressReqVO.setEquipmentId(routeItem.getEquipmentId());
                        progressReqVO.setStatus(1); // 1-待开始
                        progressReqVO.setRemark(routeItem.getRemark());

                        Long progressId = workOrderProgressService.createWorkOrderProgress(progressReqVO);
                        log.info("创建工单进度记录成功，工单ID: {}, 进度ID: {}, 工序ID: {}, 工序名称: {}", 
                                workOrder.getId(), progressId, routeItem.getProcessId(), routeItem.getOperationName());
                    }
                    log.info("工单进度记录创建完成，工单ID: {}, 共创建 {} 条记录", workOrder.getId(), routeItems.size());
                } else {
                    log.warn("工艺路线没有工序明细，跳过创建工单进度记录，工单ID: {}, 工艺路线ID: {}", workOrder.getId(), workOrder.getRouteId());
                }
            } catch (Exception e) {
                log.error("根据工艺路线创建工单进度记录时发生异常，工单ID: {}, 工艺路线ID: {}", workOrder.getId(), workOrder.getRouteId(), e);
                e.printStackTrace(); // 打印完整堆栈信息
                // 忽略错误，不影响工单创建，但记录错误日志
            }
        } else {
            log.warn("工单未关联工艺路线，跳过创建工单进度记录，工单ID: {}, workOrder.getRouteId()为null", workOrder.getId());
        }

        // 返回
        return workOrder.getId();
    }

    @Override
    public void updateWorkOrder(WorkOrderSaveReqVO updateReqVO) {
        // 校验存在，并获取现有工单（用于保持工单号不变）
        WorkOrderDO existingWorkOrder = workOrderMapper.selectById(updateReqVO.getId());
        if (existingWorkOrder == null) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
        // 更新
        WorkOrderDO updateObj = BeanUtils.toBean(updateReqVO, WorkOrderDO.class);
        // 保持工单号不变（工单号创建后不允许修改）
        updateObj.setWorkOrderNo(existingWorkOrder.getWorkOrderNo());
        workOrderMapper.updateById(updateObj);
    }

    @Override
    public void deleteWorkOrder(Long id) {
        // 校验存在
        validateWorkOrderExists(id);
        // 删除
        workOrderMapper.deleteById(id);
    }

    @Override
        public void deleteWorkOrderListByIds(List<Long> ids) {
        // 删除
        workOrderMapper.deleteByIds(ids);
        }


    private void validateWorkOrderExists(Long id) {
        if (workOrderMapper.selectById(id) == null) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
    }

    @Override
    public WorkOrderDO getWorkOrder(Long id) {
        return workOrderMapper.selectById(id);
    }

    @Override
    public PageResult<WorkOrderDO> getWorkOrderPage(WorkOrderPageReqVO pageReqVO) {
        PageResult<WorkOrderDO> pageResult = workOrderMapper.selectPage(pageReqVO);
        
        // 批量计算并填充每个工单的总工时
        if (CollUtil.isNotEmpty(pageResult.getList())) {
            List<Long> workOrderIds = convertList(pageResult.getList(), WorkOrderDO::getId);
            
            // 批量查询每个工单的所有进度记录
            List<WorkOrderProgressDO> allProgresses = workOrderProgressMapper.selectList(
                    WorkOrderProgressDO::getWorkOrderId, workOrderIds
            );
            
            // 按工单ID分组，计算每个工单的总工时
            Map<Long, Integer> totalWorkTimeMap = new HashMap<>();
            if (CollUtil.isNotEmpty(allProgresses)) {
                for (WorkOrderProgressDO progress : allProgresses) {
                    Long workOrderId = progress.getWorkOrderId();
                    Integer workTime = progress.getWorkTime();
                    if (workTime != null && workTime > 0) {
                        totalWorkTimeMap.put(workOrderId, 
                                totalWorkTimeMap.getOrDefault(workOrderId, 0) + workTime);
                    }
                }
            }
            
            // 填充总工时到工单对象
            for (WorkOrderDO workOrder : pageResult.getList()) {
                Integer totalWorkTime = totalWorkTimeMap.getOrDefault(workOrder.getId(), 0);
                workOrder.setTotalWorkTime(totalWorkTime > 0 ? totalWorkTime : null);
            }
        }
        
        return pageResult;
    }

}