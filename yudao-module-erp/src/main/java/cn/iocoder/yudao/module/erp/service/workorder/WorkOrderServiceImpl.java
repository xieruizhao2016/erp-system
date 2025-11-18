package cn.iocoder.yudao.module.erp.service.workorder;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.workorder.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.workorder.WorkOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.workorder.WorkOrderMapper;
import cn.iocoder.yudao.module.erp.dal.redis.no.ErpNoRedisDAO;

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

    @Override
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

        // 返回
        return workOrder.getId();
    }

    @Override
    public void updateWorkOrder(WorkOrderSaveReqVO updateReqVO) {
        // 校验存在
        validateWorkOrderExists(updateReqVO.getId());
        // 更新
        WorkOrderDO updateObj = BeanUtils.toBean(updateReqVO, WorkOrderDO.class);
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
        return workOrderMapper.selectPage(pageReqVO);
    }

}