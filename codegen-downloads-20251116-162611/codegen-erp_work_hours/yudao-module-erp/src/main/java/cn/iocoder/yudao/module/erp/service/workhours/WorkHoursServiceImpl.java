package cn.iocoder.yudao.module.erp.service.workhours;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.workhours.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.workhours.WorkHoursDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.workhours.WorkHoursMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 工时统计 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class WorkHoursServiceImpl implements WorkHoursService {

    @Resource
    private WorkHoursMapper workHoursMapper;

    @Override
    public Long createWorkHours(WorkHoursSaveReqVO createReqVO) {
        // 插入
        WorkHoursDO workHours = BeanUtils.toBean(createReqVO, WorkHoursDO.class);
        workHoursMapper.insert(workHours);

        // 返回
        return workHours.getId();
    }

    @Override
    public void updateWorkHours(WorkHoursSaveReqVO updateReqVO) {
        // 校验存在
        validateWorkHoursExists(updateReqVO.getId());
        // 更新
        WorkHoursDO updateObj = BeanUtils.toBean(updateReqVO, WorkHoursDO.class);
        workHoursMapper.updateById(updateObj);
    }

    @Override
    public void deleteWorkHours(Long id) {
        // 校验存在
        validateWorkHoursExists(id);
        // 删除
        workHoursMapper.deleteById(id);
    }

    @Override
        public void deleteWorkHoursListByIds(List<Long> ids) {
        // 删除
        workHoursMapper.deleteByIds(ids);
        }


    private void validateWorkHoursExists(Long id) {
        if (workHoursMapper.selectById(id) == null) {
            throw exception(WORK_HOURS_NOT_EXISTS);
        }
    }

    @Override
    public WorkHoursDO getWorkHours(Long id) {
        return workHoursMapper.selectById(id);
    }

    @Override
    public PageResult<WorkHoursDO> getWorkHoursPage(WorkHoursPageReqVO pageReqVO) {
        return workHoursMapper.selectPage(pageReqVO);
    }

}