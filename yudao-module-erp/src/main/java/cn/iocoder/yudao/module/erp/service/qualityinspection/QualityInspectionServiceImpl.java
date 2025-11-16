package cn.iocoder.yudao.module.erp.service.qualityinspection;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.qualityinspection.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.qualityinspection.QualityInspectionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.qualityinspection.QualityInspectionMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 质检记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class QualityInspectionServiceImpl implements QualityInspectionService {

    @Resource
    private QualityInspectionMapper qualityInspectionMapper;

    @Override
    public Long createQualityInspection(QualityInspectionSaveReqVO createReqVO) {
        // 插入
        QualityInspectionDO qualityInspection = BeanUtils.toBean(createReqVO, QualityInspectionDO.class);
        qualityInspectionMapper.insert(qualityInspection);

        // 返回
        return qualityInspection.getId();
    }

    @Override
    public void updateQualityInspection(QualityInspectionSaveReqVO updateReqVO) {
        // 校验存在
        validateQualityInspectionExists(updateReqVO.getId());
        // 更新
        QualityInspectionDO updateObj = BeanUtils.toBean(updateReqVO, QualityInspectionDO.class);
        qualityInspectionMapper.updateById(updateObj);
    }

    @Override
    public void deleteQualityInspection(Long id) {
        // 校验存在
        validateQualityInspectionExists(id);
        // 删除
        qualityInspectionMapper.deleteById(id);
    }

    @Override
        public void deleteQualityInspectionListByIds(List<Long> ids) {
        // 删除
        qualityInspectionMapper.deleteByIds(ids);
        }


    private void validateQualityInspectionExists(Long id) {
        if (qualityInspectionMapper.selectById(id) == null) {
            throw exception(QUALITY_INSPECTION_NOT_EXISTS);
        }
    }

    @Override
    public QualityInspectionDO getQualityInspection(Long id) {
        return qualityInspectionMapper.selectById(id);
    }

    @Override
    public PageResult<QualityInspectionDO> getQualityInspectionPage(QualityInspectionPageReqVO pageReqVO) {
        return qualityInspectionMapper.selectPage(pageReqVO);
    }

}