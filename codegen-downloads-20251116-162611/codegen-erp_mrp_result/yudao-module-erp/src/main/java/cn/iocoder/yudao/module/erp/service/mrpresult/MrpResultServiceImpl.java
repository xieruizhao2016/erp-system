package cn.iocoder.yudao.module.erp.service.mrpresult;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.mrpresult.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.mrpresult.MrpResultDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.mrpresult.MrpResultMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP MRP运算结果 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class MrpResultServiceImpl implements MrpResultService {

    @Resource
    private MrpResultMapper mrpResultMapper;

    @Override
    public Long createMrpResult(MrpResultSaveReqVO createReqVO) {
        // 插入
        MrpResultDO mrpResult = BeanUtils.toBean(createReqVO, MrpResultDO.class);
        mrpResultMapper.insert(mrpResult);

        // 返回
        return mrpResult.getId();
    }

    @Override
    public void updateMrpResult(MrpResultSaveReqVO updateReqVO) {
        // 校验存在
        validateMrpResultExists(updateReqVO.getId());
        // 更新
        MrpResultDO updateObj = BeanUtils.toBean(updateReqVO, MrpResultDO.class);
        mrpResultMapper.updateById(updateObj);
    }

    @Override
    public void deleteMrpResult(Long id) {
        // 校验存在
        validateMrpResultExists(id);
        // 删除
        mrpResultMapper.deleteById(id);
    }

    @Override
        public void deleteMrpResultListByIds(List<Long> ids) {
        // 删除
        mrpResultMapper.deleteByIds(ids);
        }


    private void validateMrpResultExists(Long id) {
        if (mrpResultMapper.selectById(id) == null) {
            throw exception(MRP_RESULT_NOT_EXISTS);
        }
    }

    @Override
    public MrpResultDO getMrpResult(Long id) {
        return mrpResultMapper.selectById(id);
    }

    @Override
    public PageResult<MrpResultDO> getMrpResultPage(MrpResultPageReqVO pageReqVO) {
        return mrpResultMapper.selectPage(pageReqVO);
    }

}