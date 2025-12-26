package cn.iocoder.yudao.module.erp.service.workcenter;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.workcenter.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.workcenter.WorkCenterDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.workcenter.WorkCenterMapper;
import cn.iocoder.yudao.module.erp.dal.redis.no.ErpNoRedisDAO;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;

/**
 * ERP 工作中心 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class WorkCenterServiceImpl implements WorkCenterService {

    @Resource
    private WorkCenterMapper workCenterMapper;

    @Resource
    private ErpNoRedisDAO noRedisDAO;

    @Override
    public Long createWorkCenter(WorkCenterSaveReqVO createReqVO) {
        // 如果工作中心编号为空，自动生成
        if (createReqVO.getWorkCenterNo() == null || createReqVO.getWorkCenterNo().trim().isEmpty()) {
            createReqVO.setWorkCenterNo(generateWorkCenterNo());
        }
        // 校验工作中心编号唯一性
        validateWorkCenterNoUnique(null, createReqVO.getWorkCenterNo());
        // 插入
        WorkCenterDO workCenter = BeanUtils.toBean(createReqVO, WorkCenterDO.class);
        workCenterMapper.insert(workCenter);
        // 返回
        return workCenter.getId();
    }

    @Override
    public void updateWorkCenter(WorkCenterSaveReqVO updateReqVO) {
        // 校验存在
        validateWorkCenterExists(updateReqVO.getId());
        // 校验工作中心编号唯一性
        validateWorkCenterNoUnique(updateReqVO.getId(), updateReqVO.getWorkCenterNo());
        // 更新
        WorkCenterDO updateObj = BeanUtils.toBean(updateReqVO, WorkCenterDO.class);
        workCenterMapper.updateById(updateObj);
    }

    @Override
    public void deleteWorkCenter(Long id) {
        // 校验存在
        validateWorkCenterExists(id);
        // 删除
        workCenterMapper.deleteById(id);
    }

    @Override
    public void deleteWorkCenterListByIds(List<Long> ids) {
        // 删除
        workCenterMapper.deleteByIds(ids);
    }

    private void validateWorkCenterExists(Long id) {
        if (workCenterMapper.selectById(id) == null) {
            throw new RuntimeException("工作中心不存在");
        }
    }

    private void validateWorkCenterNoUnique(Long id, String workCenterNo) {
        WorkCenterDO workCenter = workCenterMapper.selectByWorkCenterNo(workCenterNo);
        if (workCenter == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的工作中心
        if (id == null) {
            throw new RuntimeException("工作中心编号已存在");
        }
        if (!workCenter.getId().equals(id)) {
            throw new RuntimeException("工作中心编号已存在");
        }
    }

    @Override
    public WorkCenterDO getWorkCenter(Long id) {
        return workCenterMapper.selectById(id);
    }

    @Override
    public PageResult<WorkCenterDO> getWorkCenterPage(WorkCenterPageReqVO pageReqVO) {
        return workCenterMapper.selectPage(pageReqVO);
    }

    @Override
    public List<WorkCenterDO> getWorkCenterList() {
        return workCenterMapper.selectList(WorkCenterDO::getStatus, 1); // 只返回启用状态的工作中心
    }

    /**
     * 生成工作中心编号
     * 格式：GZZX + yyyyMMdd + 6位自增
     *
     * @return 工作中心编号
     */
    public String generateWorkCenterNo() {
        // 生成工作中心编号，格式：GZZX + yyyyMMdd + 6位自增
        String workCenterNo = noRedisDAO.generate(ErpNoRedisDAO.WORK_CENTER_NO_PREFIX);
        // 确保编号唯一性，如果已存在则重新生成
        int maxRetries = 10;
        int retryCount = 0;
        while (workCenterMapper.selectByWorkCenterNo(workCenterNo) != null && retryCount < maxRetries) {
            workCenterNo = noRedisDAO.generate(ErpNoRedisDAO.WORK_CENTER_NO_PREFIX);
            retryCount++;
        }
        return workCenterNo;
    }

}

