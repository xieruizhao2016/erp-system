package cn.iocoder.yudao.module.erp.service.workorderprogress;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.workorderprogress.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.workorderprogress.WorkOrderProgressDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.workorderprogress.WorkOrderProgressMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.workorder.WorkOrderMapper;
import cn.iocoder.yudao.module.erp.dal.dataobject.workorder.WorkOrderDO;
import cn.iocoder.yudao.module.erp.dal.mysql.productionorder.ProductionOrderMapper;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionorder.ProductionOrderDO;
import org.springframework.context.annotation.Lazy;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 工单进度 Service 实现类
 *
 * @author 芋道源码
 */
@Slf4j
@Service
@Validated
public class WorkOrderProgressServiceImpl implements WorkOrderProgressService {

    @Resource
    private WorkOrderProgressMapper workOrderProgressMapper;
    
    @Resource
    private WorkOrderMapper workOrderMapper;
    
    @Resource
    @Lazy
    private ProductionOrderMapper productionOrderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createWorkOrderProgress(WorkOrderProgressSaveReqVO createReqVO) {
        // 插入
        WorkOrderProgressDO workOrderProgress = BeanUtils.toBean(createReqVO, WorkOrderProgressDO.class);
        
        // 处理创建时的自动时间记录
        LocalDateTime now = LocalDateTime.now();
        Integer status = createReqVO.getStatus();
        
        // 如果创建时状态就是"进行中"（2），且实际开始时间为空，则记录当前时间
        if (status != null && status == 2 && createReqVO.getActualStartTime() == null) {
            workOrderProgress.setActualStartTime(now);
            log.info("创建工单进度时状态为进行中，自动记录实际开始时间：时间={}", now);
        }
        
        // 如果创建时状态就是"已完成"（3），且实际结束时间为空，则记录当前时间并计算实际工时
        if (status != null && status == 3) {
            if (createReqVO.getActualStartTime() == null) {
                workOrderProgress.setActualStartTime(now);
            }
            if (createReqVO.getActualEndTime() == null) {
                workOrderProgress.setActualEndTime(now);
                log.info("创建工单进度时状态为已完成，自动记录实际结束时间：时间={}", now);
            }
            
            // 计算实际工时（分钟）
            LocalDateTime actualStartTime = workOrderProgress.getActualStartTime();
            LocalDateTime actualEndTime = workOrderProgress.getActualEndTime();
            if (actualStartTime != null && actualEndTime != null && createReqVO.getWorkTime() == null) {
                long minutes = ChronoUnit.MINUTES.between(actualStartTime, actualEndTime);
                if (minutes > 0) {
                    workOrderProgress.setWorkTime((int) minutes);
                    log.info("创建工单进度时自动计算实际工时：开始时间={}, 结束时间={}, 工时={}分钟", 
                            actualStartTime, actualEndTime, minutes);
                }
            }
        }
        
        workOrderProgressMapper.insert(workOrderProgress);

        // 同步更新工单状态和时间（如果创建时状态就是进行中或已完成）
        if (workOrderProgress.getWorkOrderId() != null && status != null) {
            syncWorkOrderStatus(workOrderProgress, status, null, now);
        }

        // 返回
        return workOrderProgress.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWorkOrderProgress(WorkOrderProgressSaveReqVO updateReqVO) {
        // 校验存在，并获取现有数据
        WorkOrderProgressDO existing = validateWorkOrderProgressExists(updateReqVO.getId());
        
        // 更新
        WorkOrderProgressDO updateObj = BeanUtils.toBean(updateReqVO, WorkOrderProgressDO.class);
        
        // 处理状态变更时的自动时间记录
        Integer oldStatus = existing.getStatus();
        Integer newStatus = updateReqVO.getStatus();
        
        LocalDateTime now = LocalDateTime.now();
        
        // 实际开始时间、实际结束时间、实际工时由系统自动记录，不允许手动修改
        // 如果状态变更为"进行中"（2），且实际开始时间为空，则记录当前时间
        if (newStatus != null && newStatus == 2 && (oldStatus == null || oldStatus != 2)) {
            // 如果实际开始时间为空，则记录当前时间（精确到秒）
            if (existing.getActualStartTime() == null) {
                updateObj.setActualStartTime(now);
                log.info("工单进度状态变更为进行中，自动记录实际开始时间：工单进度ID={}, 时间={}", updateReqVO.getId(), now);
            } else {
                // 如果已经存在实际开始时间，保持不变
                updateObj.setActualStartTime(existing.getActualStartTime());
            }
        } else {
            // 状态未变更为进行中，保持原有实际开始时间
            updateObj.setActualStartTime(existing.getActualStartTime());
        }
        
        // 如果状态变更为"已完成"（3），且实际结束时间为空，则记录当前时间并计算实际工时
        if (newStatus != null && newStatus == 3 && (oldStatus == null || oldStatus != 3)) {
            // 如果实际结束时间为空，则记录当前时间（精确到秒）
            if (existing.getActualEndTime() == null) {
                updateObj.setActualEndTime(now);
                log.info("工单进度状态变更为已完成，自动记录实际结束时间：工单进度ID={}, 时间={}", updateReqVO.getId(), now);
                
                // 计算实际工时（分钟）
                LocalDateTime actualStartTime = existing.getActualStartTime();
                if (actualStartTime != null) {
                    long minutes = ChronoUnit.MINUTES.between(actualStartTime, now);
                    if (minutes > 0) {
                        updateObj.setWorkTime((int) minutes);
                        log.info("工单进度自动计算实际工时：工单进度ID={}, 开始时间={}, 结束时间={}, 工时={}分钟", 
                                updateReqVO.getId(), actualStartTime, now, minutes);
                    } else {
                        updateObj.setWorkTime(existing.getWorkTime());
                    }
                } else {
                    updateObj.setWorkTime(existing.getWorkTime());
                }
            } else {
                // 如果已经存在实际结束时间，保持不变
                updateObj.setActualEndTime(existing.getActualEndTime());
                updateObj.setWorkTime(existing.getWorkTime());
            }
        } else {
            // 状态未变更为已完成，保持原有实际结束时间和实际工时
            updateObj.setActualEndTime(existing.getActualEndTime());
            updateObj.setWorkTime(existing.getWorkTime());
        }
        
        workOrderProgressMapper.updateById(updateObj);
        
        // 同步更新工单状态和时间
        syncWorkOrderStatus(existing, newStatus, oldStatus, now);
    }
    
    /**
     * 同步更新工单状态和时间
     * 
     * @param currentProgress 当前更新的工单进度记录
     * @param newStatus 新状态
     * @param oldStatus 旧状态
     * @param now 当前时间
     */
    private void syncWorkOrderStatus(WorkOrderProgressDO currentProgress, Integer newStatus, Integer oldStatus, LocalDateTime now) {
        Long workOrderId = currentProgress.getWorkOrderId();
        Integer currentSequence = currentProgress.getSequence();
        if (workOrderId == null) {
            return;
        }
        
        try {
            // 获取工单信息
            WorkOrderDO workOrder = workOrderMapper.selectById(workOrderId);
            if (workOrder == null) {
                log.warn("工单不存在，无法同步状态：工单ID={}", workOrderId);
                return;
            }
            
            // 获取工单的所有进度记录
            List<WorkOrderProgressDO> allProgresses = workOrderProgressMapper.selectList(
                    WorkOrderProgressDO::getWorkOrderId, workOrderId
            );
            
            if (CollUtil.isEmpty(allProgresses)) {
                return;
            }
            
            // 判断是否是第一个工序（sequence 最小）
            Integer minSequence = allProgresses.stream()
                    .map(WorkOrderProgressDO::getSequence)
                    .filter(Objects::nonNull)
                    .min(Integer::compareTo)
                    .orElse(null);
            boolean isFirstProcess = minSequence != null && minSequence.equals(currentSequence);
            
            // 判断是否是最后一个工序（sequence 最大）
            Integer maxSequence = allProgresses.stream()
                    .map(WorkOrderProgressDO::getSequence)
                    .filter(Objects::nonNull)
                    .max(Integer::compareTo)
                    .orElse(null);
            boolean isLastProcess = maxSequence != null && maxSequence.equals(currentSequence);
            
            WorkOrderDO updateObj = new WorkOrderDO();
            updateObj.setId(workOrderId);
            boolean needUpdate = false;
            
            // 如果第一个工序状态变更为"进行中"（2），且工单状态不是"进行中"（3），则更新工单状态为"进行中"并记录实际开始时间
            if (newStatus != null && newStatus == 2 && (oldStatus == null || oldStatus != 2) && isFirstProcess) {
                if (workOrder.getStatus() == null || workOrder.getStatus() != 3) {
                    updateObj.setStatus(3); // 3-进行中
                    if (workOrder.getActualStartTime() == null) {
                        updateObj.setActualStartTime(now);
                        log.info("第一个工序状态变更为进行中，同步更新工单状态为进行中并记录实际开始时间：工单ID={}, 时间={}", workOrderId, now);
                    } else {
                        log.info("第一个工序状态变更为进行中，同步更新工单状态为进行中：工单ID={}", workOrderId);
                    }
                    needUpdate = true;
                }
            }
            
            // 如果最后一个工序状态变更为"已完成"（3），且工单状态不是"已完成"（5），则更新工单状态为"已完成"并记录实际结束时间
            if (newStatus != null && newStatus == 3 && (oldStatus == null || oldStatus != 3) && isLastProcess) {
                // 检查是否所有工序都已完成（需要考虑当前更新的记录）
                boolean allCompleted = allProgresses.stream()
                        .allMatch(p -> {
                            // 如果是当前更新的记录，使用新状态；否则使用数据库中的状态
                            if (p.getId() != null && p.getId().equals(currentProgress.getId())) {
                                return newStatus != null && newStatus == 3;
                            }
                            return p.getStatus() != null && p.getStatus() == 3;
                        });
                
                if (allCompleted && (workOrder.getStatus() == null || workOrder.getStatus() != 5)) {
                    updateObj.setStatus(5); // 5-已完成
                    if (workOrder.getActualEndTime() == null) {
                        updateObj.setActualEndTime(now);
                        log.info("最后一个工序状态变更为已完成，且所有工序都已完成，同步更新工单状态为已完成并记录实际结束时间：工单ID={}, 时间={}", workOrderId, now);
                    } else {
                        log.info("最后一个工序状态变更为已完成，且所有工序都已完成，同步更新工单状态为已完成：工单ID={}", workOrderId);
                    }
                    needUpdate = true;
                    
                    // 如果工单关联了生产订单，且生产订单状态不是"已完成"，则自动更新生产订单状态为"已完成"
                    if (workOrder.getProductionOrderId() != null) {
                        try {
                            ProductionOrderDO productionOrder = productionOrderMapper.selectById(workOrder.getProductionOrderId());
                            if (productionOrder != null && (productionOrder.getStatus() == null || productionOrder.getStatus() != 4)) {
                                // 状态不是"已完成"（状态4），则更新为"已完成"
                                ProductionOrderDO productionOrderUpdateObj = new ProductionOrderDO();
                                productionOrderUpdateObj.setId(workOrder.getProductionOrderId());
                                productionOrderUpdateObj.setStatus(4); // 4-已完成
                                // 如果实际完成时间为空，则记录当前时间
                                if (productionOrder.getActualEndTime() == null) {
                                    productionOrderUpdateObj.setActualEndTime(now);
                                }
                                productionOrderMapper.updateById(productionOrderUpdateObj);
                                log.info("工单完成，自动更新关联的生产订单状态为已完成：工单ID={}, 生产订单ID={}, 生产订单号={}", 
                                        workOrderId, workOrder.getProductionOrderId(), productionOrder.getNo());
                            }
                        } catch (Exception e) {
                            log.error("工单完成时更新生产订单状态失败：工单ID={}, 生产订单ID={}", 
                                    workOrderId, workOrder.getProductionOrderId(), e);
                            // 不抛出异常，避免影响工单状态的更新
                        }
                    }
                }
            }
            
            if (needUpdate) {
                workOrderMapper.updateById(updateObj);
            }
        } catch (Exception e) {
            log.error("同步工单状态失败：工单ID={}", workOrderId, e);
            // 不抛出异常，避免影响工单进度的更新
        }
    }

    @Override
    public void deleteWorkOrderProgress(Long id) {
        // 校验存在
        validateWorkOrderProgressExists(id);
        // 删除
        workOrderProgressMapper.deleteById(id);
    }

    @Override
        public void deleteWorkOrderProgressListByIds(List<Long> ids) {
        // 删除
        workOrderProgressMapper.deleteByIds(ids);
        }


    private WorkOrderProgressDO validateWorkOrderProgressExists(Long id) {
        WorkOrderProgressDO workOrderProgress = workOrderProgressMapper.selectById(id);
        if (workOrderProgress == null) {
            throw exception(WORK_ORDER_PROGRESS_NOT_EXISTS);
        }
        return workOrderProgress;
    }

    @Override
    public WorkOrderProgressDO getWorkOrderProgress(Long id) {
        return workOrderProgressMapper.selectById(id);
    }

    @Override
    public PageResult<WorkOrderProgressDO> getWorkOrderProgressPage(WorkOrderProgressPageReqVO pageReqVO) {
        try {
            log.debug("查询工单进度分页，参数：{}", pageReqVO);
            PageResult<WorkOrderProgressDO> result = workOrderProgressMapper.selectPage(pageReqVO);
            log.debug("查询工单进度分页成功，结果数量：{}", result.getTotal());
            return result;
        } catch (Exception e) {
            log.error("查询工单进度分页失败，参数：{}", pageReqVO, e);
            throw e;
        }
    }

}