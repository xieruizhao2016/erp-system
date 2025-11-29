package cn.iocoder.yudao.module.erp.service.process;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.process.vo.ProcessPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.process.vo.ProcessSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.erp.dal.mysql.process.ProcessMapper;
import cn.iocoder.yudao.module.erp.dal.redis.no.ErpNoRedisDAO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.PROCESS_NOT_EXISTS;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.PROCESS_NO_DUPLICATE;

/**
 * ERP 工序 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProcessServiceImpl implements ProcessService {

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private ErpNoRedisDAO noRedisDAO;

    @Override
    public Long createProcess(ProcessSaveReqVO createReqVO) {
        // 如果工序编号为空，自动生成
        if (createReqVO.getProcessNo() == null || createReqVO.getProcessNo().trim().isEmpty()) {
            createReqVO.setProcessNo(generateProcessNo());
        }
        // 校验工序编号唯一性
        validateProcessNoUnique(null, createReqVO.getProcessNo());
        // 插入
        ProcessDO process = BeanUtils.toBean(createReqVO, ProcessDO.class);
        processMapper.insert(process);
        // 返回
        return process.getId();
    }

    @Override
    public void updateProcess(ProcessSaveReqVO updateReqVO) {
        // 校验存在
        validateProcessExists(updateReqVO.getId());
        // 校验工序编号唯一性
        validateProcessNoUnique(updateReqVO.getId(), updateReqVO.getProcessNo());
        // 更新
        ProcessDO updateObj = BeanUtils.toBean(updateReqVO, ProcessDO.class);
        processMapper.updateById(updateObj);
    }

    @Override
    public void deleteProcess(Long id) {
        // 校验存在
        validateProcessExists(id);
        // 删除
        processMapper.deleteById(id);
    }

    @Override
    public void deleteProcessListByIds(List<Long> ids) {
        // 删除
        processMapper.deleteBatchIds(ids);
    }

    private void validateProcessExists(Long id) {
        if (processMapper.selectById(id) == null) {
            throw exception(PROCESS_NOT_EXISTS);
        }
    }

    private void validateProcessNoUnique(Long id, String processNo) {
        ProcessDO process = processMapper.selectByProcessNo(processNo);
        if (process == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的工序
        if (id == null) {
            throw exception(PROCESS_NO_DUPLICATE);
        }
        if (!process.getId().equals(id)) {
            throw exception(PROCESS_NO_DUPLICATE);
        }
    }

    @Override
    public ProcessDO getProcess(Long id) {
        return processMapper.selectById(id);
    }

    @Override
    public PageResult<ProcessDO> getProcessPage(ProcessPageReqVO pageReqVO) {
        return processMapper.selectPage(pageReqVO);
    }

    @Override
    public ProcessDO getProcessByProcessNo(String processNo) {
        return processMapper.selectByProcessNo(processNo);
    }

    @Override
    public List<ProcessDO> getProcessList() {
        return processMapper.selectList(ProcessDO::getStatus, 1); // 只返回启用状态的工序
    }

    @Override
    public String generateProcessNo() {
        // 生成工序编号，格式：GX + yyyyMMdd + 6位自增
        String processNo = noRedisDAO.generate(ErpNoRedisDAO.PROCESS_NO_PREFIX);
        // 确保编号唯一性，如果已存在则重新生成
        int maxRetries = 10;
        int retryCount = 0;
        while (processMapper.selectByProcessNo(processNo) != null && retryCount < maxRetries) {
            processNo = noRedisDAO.generate(ErpNoRedisDAO.PROCESS_NO_PREFIX);
            retryCount++;
        }
        return processNo;
    }

}



