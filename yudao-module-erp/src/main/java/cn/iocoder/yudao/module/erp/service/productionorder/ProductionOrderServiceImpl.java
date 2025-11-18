package cn.iocoder.yudao.module.erp.service.productionorder;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.productionorder.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionorder.ProductionOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.productionorder.ProductionOrderMapper;
import cn.iocoder.yudao.module.erp.dal.redis.no.ErpNoRedisDAO;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 生产订单 DO
 * Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductionOrderServiceImpl implements ProductionOrderService {

    @Resource
    private ProductionOrderMapper productionOrderMapper;

    @Resource
    private ErpNoRedisDAO noRedisDAO;

    @Override
    public Long createProductionOrder(ProductionOrderSaveReqVO createReqVO) {
        // 生成生产订单号，并校验唯一性
        String no = noRedisDAO.generate(ErpNoRedisDAO.PRODUCTION_ORDER_NO_PREFIX);
        if (productionOrderMapper.selectByNo(no) != null) {
            throw exception(PRODUCTION_ORDER_NO_EXISTS);
        }

        // 插入
        ProductionOrderDO productionOrder = BeanUtils.toBean(createReqVO, ProductionOrderDO.class);
        productionOrder.setNo(no);
        productionOrderMapper.insert(productionOrder);

        // 返回
        return productionOrder.getId();
    }

    @Override
    public void updateProductionOrder(ProductionOrderSaveReqVO updateReqVO) {
        // 校验存在
        validateProductionOrderExists(updateReqVO.getId());
        // 更新
        ProductionOrderDO updateObj = BeanUtils.toBean(updateReqVO, ProductionOrderDO.class);
        productionOrderMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductionOrder(Long id) {
        // 校验存在
        validateProductionOrderExists(id);
        // 删除
        productionOrderMapper.deleteById(id);
    }

    @Override
        public void deleteProductionOrderListByIds(List<Long> ids) {
        // 删除
        productionOrderMapper.deleteByIds(ids);
        }


    private void validateProductionOrderExists(Long id) {
        if (productionOrderMapper.selectById(id) == null) {
            throw exception(PRODUCTION_ORDER_NOT_EXISTS);
        }
    }

    @Override
    public ProductionOrderDO getProductionOrder(Long id) {
        return productionOrderMapper.selectById(id);
    }

    @Override
    public PageResult<ProductionOrderDO> getProductionOrderPage(ProductionOrderPageReqVO pageReqVO) {
        return productionOrderMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateProductionOrderStatus(Long id, Integer status) {
        // 校验存在
        validateProductionOrderExists(id);
        // 更新状态
        ProductionOrderDO updateObj = new ProductionOrderDO();
        updateObj.setId(id);
        updateObj.setStatus(status);
        productionOrderMapper.updateById(updateObj);
    }

}