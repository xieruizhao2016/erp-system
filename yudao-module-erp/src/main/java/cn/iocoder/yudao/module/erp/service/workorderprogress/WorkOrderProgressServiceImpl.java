package cn.iocoder.yudao.module.erp.service.workorderprogress;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.workorderprogress.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.workorderprogress.WorkOrderProgressDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.workorderprogress.WorkOrderProgressMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 工单进度 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class WorkOrderProgressServiceImpl implements WorkOrderProgressService {

    @Resource
    private WorkOrderProgressMapper workOrderProgressMapper;

    @Override
    public Long createWorkOrderProgress(WorkOrderProgressSaveReqVO createReqVO) {
        // 插入
        WorkOrderProgressDO workOrderProgress = BeanUtils.toBean(createReqVO, WorkOrderProgressDO.class);
        workOrderProgressMapper.insert(workOrderProgress);

        // 返回
        return workOrderProgress.getId();
    }

    @Override
    public void updateWorkOrderProgress(WorkOrderProgressSaveReqVO updateReqVO) {
        // 校验存在
        validateWorkOrderProgressExists(updateReqVO.getId());
        // 更新
        WorkOrderProgressDO updateObj = BeanUtils.toBean(updateReqVO, WorkOrderProgressDO.class);
        workOrderProgressMapper.updateById(updateObj);
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


    private void validateWorkOrderProgressExists(Long id) {
        if (workOrderProgressMapper.selectById(id) == null) {
            throw exception(WORK_ORDER_PROGRESS_NOT_EXISTS);
        }
    }

    @Override
    public WorkOrderProgressDO getWorkOrderProgress(Long id) {
        return workOrderProgressMapper.selectById(id);
    }

    @Override
    public PageResult<WorkOrderProgressDO> getWorkOrderProgressPage(WorkOrderProgressPageReqVO pageReqVO) {
        return workOrderProgressMapper.selectPage(pageReqVO);
    }

}