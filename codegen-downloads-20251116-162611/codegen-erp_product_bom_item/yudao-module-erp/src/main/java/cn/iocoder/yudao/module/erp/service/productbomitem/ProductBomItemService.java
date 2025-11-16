package cn.iocoder.yudao.module.erp.service.productbomitem;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.productbomitem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productbomitem.ProductBomItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP BOM明细 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductBomItemService {

    /**
     * 创建ERP BOM明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductBomItem(@Valid ProductBomItemSaveReqVO createReqVO);

    /**
     * 更新ERP BOM明细
     *
     * @param updateReqVO 更新信息
     */
    void updateProductBomItem(@Valid ProductBomItemSaveReqVO updateReqVO);

    /**
     * 删除ERP BOM明细
     *
     * @param id 编号
     */
    void deleteProductBomItem(Long id);

    /**
    * 批量删除ERP BOM明细
    *
    * @param ids 编号
    */
    void deleteProductBomItemListByIds(List<Long> ids);

    /**
     * 获得ERP BOM明细
     *
     * @param id 编号
     * @return ERP BOM明细
     */
    ProductBomItemDO getProductBomItem(Long id);

    /**
     * 获得ERP BOM明细分页
     *
     * @param pageReqVO 分页查询
     * @return ERP BOM明细分页
     */
    PageResult<ProductBomItemDO> getProductBomItemPage(ProductBomItemPageReqVO pageReqVO);

}