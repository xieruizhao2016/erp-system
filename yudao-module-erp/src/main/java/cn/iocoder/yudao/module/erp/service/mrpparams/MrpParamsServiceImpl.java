package cn.iocoder.yudao.module.erp.service.mrpparams;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.mrpparams.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.mrpparams.MrpParamsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.mrpparams.MrpParamsMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP MRP参数 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class MrpParamsServiceImpl implements MrpParamsService {

    @Resource
    private MrpParamsMapper mrpParamsMapper;

    @Override
    public Long createMrpParams(MrpParamsSaveReqVO createReqVO) {
        // 插入
        MrpParamsDO mrpParams = BeanUtils.toBean(createReqVO, MrpParamsDO.class);
        mrpParamsMapper.insert(mrpParams);

        // 返回
        return mrpParams.getId();
    }

    @Override
    public void updateMrpParams(MrpParamsSaveReqVO updateReqVO) {
        // 校验存在
        validateMrpParamsExists(updateReqVO.getId());
        // 更新
        MrpParamsDO updateObj = BeanUtils.toBean(updateReqVO, MrpParamsDO.class);
        mrpParamsMapper.updateById(updateObj);
    }

    @Override
    public void deleteMrpParams(Long id) {
        // 校验存在
        validateMrpParamsExists(id);
        // 删除
        mrpParamsMapper.deleteById(id);
    }

    @Override
        public void deleteMrpParamsListByIds(List<Long> ids) {
        // 删除
        mrpParamsMapper.deleteByIds(ids);
        }


    private void validateMrpParamsExists(Long id) {
        if (mrpParamsMapper.selectById(id) == null) {
            throw exception(MRP_PARAMS_NOT_EXISTS);
        }
    }

    @Override
    public MrpParamsDO getMrpParams(Long id) {
        return mrpParamsMapper.selectById(id);
    }

    @Override
    public PageResult<MrpParamsDO> getMrpParamsPage(MrpParamsPageReqVO pageReqVO) {
        return mrpParamsMapper.selectPage(pageReqVO);
    }

}