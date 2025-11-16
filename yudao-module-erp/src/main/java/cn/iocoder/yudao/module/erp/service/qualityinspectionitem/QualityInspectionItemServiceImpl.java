package cn.iocoder.yudao.module.erp.service.qualityinspectionitem;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.qualityinspectionitem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.qualityinspectionitem.QualityInspectionItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.qualityinspectionitem.QualityInspectionItemMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 质检明细 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class QualityInspectionItemServiceImpl implements QualityInspectionItemService {

    @Resource
    private QualityInspectionItemMapper qualityInspectionItemMapper;

    @Override
    public Long createQualityInspectionItem(QualityInspectionItemSaveReqVO createReqVO) {
        // 插入
        QualityInspectionItemDO qualityInspectionItem = BeanUtils.toBean(createReqVO, QualityInspectionItemDO.class);
        qualityInspectionItemMapper.insert(qualityInspectionItem);

        // 返回
        return qualityInspectionItem.getId();
    }

    @Override
    public void updateQualityInspectionItem(QualityInspectionItemSaveReqVO updateReqVO) {
        // 校验存在
        validateQualityInspectionItemExists(updateReqVO.getId());
        // 更新
        QualityInspectionItemDO updateObj = BeanUtils.toBean(updateReqVO, QualityInspectionItemDO.class);
        qualityInspectionItemMapper.updateById(updateObj);
    }

    @Override
    public void deleteQualityInspectionItem(Long id) {
        // 校验存在
        validateQualityInspectionItemExists(id);
        // 删除
        qualityInspectionItemMapper.deleteById(id);
    }

    @Override
        public void deleteQualityInspectionItemListByIds(List<Long> ids) {
        // 删除
        qualityInspectionItemMapper.deleteByIds(ids);
        }


    private void validateQualityInspectionItemExists(Long id) {
        if (qualityInspectionItemMapper.selectById(id) == null) {
            throw exception(QUALITY_INSPECTION_ITEM_NOT_EXISTS);
        }
    }

    @Override
    public QualityInspectionItemDO getQualityInspectionItem(Long id) {
        return qualityInspectionItemMapper.selectById(id);
    }

    @Override
    public PageResult<QualityInspectionItemDO> getQualityInspectionItemPage(QualityInspectionItemPageReqVO pageReqVO) {
        return qualityInspectionItemMapper.selectPage(pageReqVO);
    }

}