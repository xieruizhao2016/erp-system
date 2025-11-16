package cn.iocoder.yudao.module.erp.service.productbom;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.productbom.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productbom.ProductBomDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP BOM主 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductBomService {

    /**
     * 创建ERP BOM主
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductBom(@Valid ProductBomSaveReqVO createReqVO);

    /**
     * 更新ERP BOM主
     *
     * @param updateReqVO 更新信息
     */
    void updateProductBom(@Valid ProductBomSaveReqVO updateReqVO);

    /**
     * 删除ERP BOM主
     *
     * @param id 编号
     */
    void deleteProductBom(Long id);

    /**
    * 批量删除ERP BOM主
    *
    * @param ids 编号
    */
    void deleteProductBomListByIds(List<Long> ids);

    /**
     * 获得ERP BOM主
     *
     * @param id 编号
     * @return ERP BOM主
     */
    ProductBomDO getProductBom(Long id);

    /**
     * 获得ERP BOM主分页
     *
     * @param pageReqVO 分页查询
     * @return ERP BOM主分页
     */
    PageResult<ProductBomDO> getProductBomPage(ProductBomPageReqVO pageReqVO);

}