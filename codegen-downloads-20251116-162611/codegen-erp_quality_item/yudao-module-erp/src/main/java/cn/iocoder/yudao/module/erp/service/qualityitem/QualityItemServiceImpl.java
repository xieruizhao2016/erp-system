package cn.iocoder.yudao.module.erp.service.qualityitem;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.qualityitem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.qualityitem.QualityItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.qualityitem.QualityItemMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 质检项目 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class QualityItemServiceImpl implements QualityItemService {

    @Resource
    private QualityItemMapper qualityItemMapper;

    @Override
    public Long createQualityItem(QualityItemSaveReqVO createReqVO) {
        // 插入
        QualityItemDO qualityItem = BeanUtils.toBean(createReqVO, QualityItemDO.class);
        qualityItemMapper.insert(qualityItem);

        // 返回
        return qualityItem.getId();
    }

    @Override
    public void updateQualityItem(QualityItemSaveReqVO updateReqVO) {
        // 校验存在
        validateQualityItemExists(updateReqVO.getId());
        // 更新
        QualityItemDO updateObj = BeanUtils.toBean(updateReqVO, QualityItemDO.class);
        qualityItemMapper.updateById(updateObj);
    }

    @Override
    public void deleteQualityItem(Long id) {
        // 校验存在
        validateQualityItemExists(id);
        // 删除
        qualityItemMapper.deleteById(id);
    }

    @Override
        public void deleteQualityItemListByIds(List<Long> ids) {
        // 删除
        qualityItemMapper.deleteByIds(ids);
        }


    private void validateQualityItemExists(Long id) {
        if (qualityItemMapper.selectById(id) == null) {
            throw exception(QUALITY_ITEM_NOT_EXISTS);
        }
    }

    @Override
    public QualityItemDO getQualityItem(Long id) {
        return qualityItemMapper.selectById(id);
    }

    @Override
    public PageResult<QualityItemDO> getQualityItemPage(QualityItemPageReqVO pageReqVO) {
        return qualityItemMapper.selectPage(pageReqVO);
    }

}