package cn.iocoder.yudao.module.erp.service.qualityinspectionitem;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.qualityinspectionitem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.qualityinspectionitem.QualityInspectionItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 质检明细 Service 接口
 *
 * @author 芋道源码
 */
public interface QualityInspectionItemService {

    /**
     * 创建ERP 质检明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createQualityInspectionItem(@Valid QualityInspectionItemSaveReqVO createReqVO);

    /**
     * 更新ERP 质检明细
     *
     * @param updateReqVO 更新信息
     */
    void updateQualityInspectionItem(@Valid QualityInspectionItemSaveReqVO updateReqVO);

    /**
     * 删除ERP 质检明细
     *
     * @param id 编号
     */
    void deleteQualityInspectionItem(Long id);

    /**
    * 批量删除ERP 质检明细
    *
    * @param ids 编号
    */
    void deleteQualityInspectionItemListByIds(List<Long> ids);

    /**
     * 获得ERP 质检明细
     *
     * @param id 编号
     * @return ERP 质检明细
     */
    QualityInspectionItemDO getQualityInspectionItem(Long id);

    /**
     * 获得ERP 质检明细分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 质检明细分页
     */
    PageResult<QualityInspectionItemDO> getQualityInspectionItemPage(QualityInspectionItemPageReqVO pageReqVO);

}