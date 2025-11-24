package cn.iocoder.yudao.module.erp.service.productsku;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.productsku.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 产品SKU Service 接口
 *
 * @author 芋道源码
 */
public interface ProductSkuService {

    /**
     * 创建ERP 产品SKU
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductSku(@Valid ProductSkuSaveReqVO createReqVO);

    /**
     * 更新ERP 产品SKU
     *
     * @param updateReqVO 更新信息
     */
    void updateProductSku(@Valid ProductSkuSaveReqVO updateReqVO);

    /**
     * 删除ERP 产品SKU
     *
     * @param id 编号
     */
    void deleteProductSku(Long id);

    /**
    * 批量删除ERP 产品SKU
    *
    * @param ids 编号
    */
    void deleteProductSkuListByIds(List<Long> ids);

    /**
     * 获得ERP 产品SKU
     *
     * @param id 编号
     * @return ERP 产品SKU
     */
    ProductSkuDO getProductSku(Long id);

    /**
     * 获得ERP 产品SKU分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 产品SKU分页
     */
    PageResult<ProductSkuDO> getProductSkuPage(ProductSkuPageReqVO pageReqVO);

    /**
     * 获得产品SKU精简列表
     * 
     * @return 产品SKU列表
     */
    List<ProductSkuDO> getProductSkuSimpleList();

    /**
     * 根据ID集合获得产品SKU列表
     *
     * @param ids ID集合
     * @return 产品SKU列表
     */
    List<ProductSkuDO> getProductSkuList(Collection<Long> ids);

    /**
     * 根据产品ID获得产品SKU列表（只返回启用的SKU）
     *
     * @param productId 产品ID
     * @return 产品SKU列表（只包含启用的）
     */
    List<ProductSkuDO> getProductSkuListByProductId(Long productId);

    /**
     * 根据产品ID获得产品SKU列表（包含所有状态的SKU，用于编辑产品时显示）
     *
     * @param productId 产品ID
     * @return 产品SKU列表（包含所有状态）
     */
    List<ProductSkuDO> getProductSkuListByProductIdAll(Long productId);

}