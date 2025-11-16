package cn.iocoder.yudao.module.erp.service.productionkpi;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.productionkpi.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionkpi.ProductionKpiDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.productionkpi.ProductionKpiMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 生产KPI Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductionKpiServiceImpl implements ProductionKpiService {

    @Resource
    private ProductionKpiMapper productionKpiMapper;

    @Override
    public Long createProductionKpi(ProductionKpiSaveReqVO createReqVO) {
        // 插入
        ProductionKpiDO productionKpi = BeanUtils.toBean(createReqVO, ProductionKpiDO.class);
        productionKpiMapper.insert(productionKpi);

        // 返回
        return productionKpi.getId();
    }

    @Override
    public void updateProductionKpi(ProductionKpiSaveReqVO updateReqVO) {
        // 校验存在
        validateProductionKpiExists(updateReqVO.getId());
        // 更新
        ProductionKpiDO updateObj = BeanUtils.toBean(updateReqVO, ProductionKpiDO.class);
        productionKpiMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductionKpi(Long id) {
        // 校验存在
        validateProductionKpiExists(id);
        // 删除
        productionKpiMapper.deleteById(id);
    }

    @Override
        public void deleteProductionKpiListByIds(List<Long> ids) {
        // 删除
        productionKpiMapper.deleteByIds(ids);
        }


    private void validateProductionKpiExists(Long id) {
        if (productionKpiMapper.selectById(id) == null) {
            throw exception(PRODUCTION_KPI_NOT_EXISTS);
        }
    }

    @Override
    public ProductionKpiDO getProductionKpi(Long id) {
        return productionKpiMapper.selectById(id);
    }

    @Override
    public PageResult<ProductionKpiDO> getProductionKpiPage(ProductionKpiPageReqVO pageReqVO) {
        return productionKpiMapper.selectPage(pageReqVO);
    }

}