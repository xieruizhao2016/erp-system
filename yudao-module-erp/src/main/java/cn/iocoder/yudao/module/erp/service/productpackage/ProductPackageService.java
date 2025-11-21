package cn.iocoder.yudao.module.erp.service.productpackage;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.productpackage.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productpackage.ProductPackageDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * ERP 产品包装 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductPackageService {

    /**
     * 创建ERP 产品包装
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductPackage(@Valid ProductPackageSaveReqVO createReqVO);

    /**
     * 更新ERP 产品包装
     *
     * @param updateReqVO 更新信息
     */
    void updateProductPackage(@Valid ProductPackageSaveReqVO updateReqVO);

    /**
     * 删除ERP 产品包装
     *
     * @param id 编号
     */
    void deleteProductPackage(Long id);

    /**
    * 批量删除ERP 产品包装
    *
    * @param ids 编号列表
    */
    void deleteProductPackageListByIds(List<Long> ids);

    /**
     * 获得ERP 产品包装
     *
     * @param id 编号
     * @return ERP 产品包装
     */
    ProductPackageDO getProductPackage(Long id);

    /**
     * 获得ERP 产品包装分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 产品包装分页
     */
    PageResult<ProductPackageDO> getProductPackagePage(ProductPackagePageReqVO pageReqVO);

    /**
     * 获得产品包装精简列表
     * 
     * @return 产品包装列表
     */
    List<ProductPackageDO> getProductPackageSimpleList();

    /**
     * 根据ID集合获得产品包装列表
     *
     * @param ids ID集合
     * @return 产品包装列表
     */
    List<ProductPackageDO> getProductPackageList(Collection<Long> ids);

}

