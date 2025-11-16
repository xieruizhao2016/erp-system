package cn.iocoder.yudao.module.erp.service.qualityitem;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.qualityitem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.qualityitem.QualityItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 质检项目 Service 接口
 *
 * @author 芋道源码
 */
public interface QualityItemService {

    /**
     * 创建ERP 质检项目
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createQualityItem(@Valid QualityItemSaveReqVO createReqVO);

    /**
     * 更新ERP 质检项目
     *
     * @param updateReqVO 更新信息
     */
    void updateQualityItem(@Valid QualityItemSaveReqVO updateReqVO);

    /**
     * 删除ERP 质检项目
     *
     * @param id 编号
     */
    void deleteQualityItem(Long id);

    /**
    * 批量删除ERP 质检项目
    *
    * @param ids 编号
    */
    void deleteQualityItemListByIds(List<Long> ids);

    /**
     * 获得ERP 质检项目
     *
     * @param id 编号
     * @return ERP 质检项目
     */
    QualityItemDO getQualityItem(Long id);

    /**
     * 获得ERP 质检项目分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 质检项目分页
     */
    PageResult<QualityItemDO> getQualityItemPage(QualityItemPageReqVO pageReqVO);

}