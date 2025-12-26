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

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 工单主 Service 实现类
 *
 * @author 芋道源码
 */
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
        workOrderMapper.insert(workOrder);

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