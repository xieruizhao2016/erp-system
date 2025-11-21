package cn.iocoder.yudao.module.erp.service.productoem;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.productoem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productoem.ProductOemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * ERP 产品OEM Service 接口
 *
 * @author 芋道源码
 */
public interface ProductOemService {

    /**
     * 创建ERP 产品OEM
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductOem(@Valid ProductOemSaveReqVO createReqVO);

    /**
     * 更新ERP 产品OEM
     *
     * @param updateReqVO 更新信息
     */
    void updateProductOem(@Valid ProductOemSaveReqVO updateReqVO);

    /**
     * 删除ERP 产品OEM
     *
     * @param id 编号
     */
    void deleteProductOem(Long id);

    /**
    * 批量删除ERP 产品OEM
    *
    * @param ids 编号列表
    */
    void deleteProductOemListByIds(List<Long> ids);

    /**
     * 获得ERP 产品OEM
     *
     * @param id 编号
     * @return ERP 产品OEM
     */
    ProductOemDO getProductOem(Long id);

    /**
     * 获得ERP 产品OEM分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 产品OEM分页
     */
    PageResult<ProductOemDO> getProductOemPage(ProductOemPageReqVO pageReqVO);

    /**
     * 获得产品OEM精简列表
     * 
     * @return 产品OEM列表
     */
    List<ProductOemDO> getProductOemSimpleList();

    /**
     * 根据ID集合获得产品OEM列表
     *
     * @param ids ID集合
     * @return 产品OEM列表
     */
    List<ProductOemDO> getProductOemList(Collection<Long> ids);

}

