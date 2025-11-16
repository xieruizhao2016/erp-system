package cn.iocoder.yudao.module.erp.service.qualitystandard;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.qualitystandard.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.qualitystandard.QualityStandardDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.qualitystandard.QualityStandardMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 质检标准 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class QualityStandardServiceImpl implements QualityStandardService {

    @Resource
    private QualityStandardMapper qualityStandardMapper;

    @Override
    public Long createQualityStandard(QualityStandardSaveReqVO createReqVO) {
        // 插入
        QualityStandardDO qualityStandard = BeanUtils.toBean(createReqVO, QualityStandardDO.class);
        qualityStandardMapper.insert(qualityStandard);

        // 返回
        return qualityStandard.getId();
    }

    @Override
    public void updateQualityStandard(QualityStandardSaveReqVO updateReqVO) {
        // 校验存在
        validateQualityStandardExists(updateReqVO.getId());
        // 更新
        QualityStandardDO updateObj = BeanUtils.toBean(updateReqVO, QualityStandardDO.class);
        qualityStandardMapper.updateById(updateObj);
    }

    @Override
    public void deleteQualityStandard(Long id) {
        // 校验存在
        validateQualityStandardExists(id);
        // 删除
        qualityStandardMapper.deleteById(id);
    }

    @Override
        public void deleteQualityStandardListByIds(List<Long> ids) {
        // 删除
        qualityStandardMapper.deleteByIds(ids);
        }


    private void validateQualityStandardExists(Long id) {
        if (qualityStandardMapper.selectById(id) == null) {
            throw exception(QUALITY_STANDARD_NOT_EXISTS);
        }
    }

    @Override
    public QualityStandardDO getQualityStandard(Long id) {
        return qualityStandardMapper.selectById(id);
    }

    @Override
    public PageResult<QualityStandardDO> getQualityStandardPage(QualityStandardPageReqVO pageReqVO) {
        return qualityStandardMapper.selectPage(pageReqVO);
    }

}