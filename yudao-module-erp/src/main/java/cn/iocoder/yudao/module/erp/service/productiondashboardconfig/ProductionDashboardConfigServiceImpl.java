package cn.iocoder.yudao.module.erp.service.productiondashboardconfig;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.productiondashboardconfig.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productiondashboardconfig.ProductionDashboardConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.productiondashboardconfig.ProductionDashboardConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 看板配置 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductionDashboardConfigServiceImpl implements ProductionDashboardConfigService {

    @Resource
    private ProductionDashboardConfigMapper productionDashboardConfigMapper;

    @Override
    public Long createProductionDashboardConfig(ProductionDashboardConfigSaveReqVO createReqVO) {
        // 插入
        ProductionDashboardConfigDO productionDashboardConfig = BeanUtils.toBean(createReqVO, ProductionDashboardConfigDO.class);
        productionDashboardConfigMapper.insert(productionDashboardConfig);

        // 返回
        return productionDashboardConfig.getId();
    }

    @Override
    public void updateProductionDashboardConfig(ProductionDashboardConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateProductionDashboardConfigExists(updateReqVO.getId());
        // 更新
        ProductionDashboardConfigDO updateObj = BeanUtils.toBean(updateReqVO, ProductionDashboardConfigDO.class);
        productionDashboardConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductionDashboardConfig(Long id) {
        // 校验存在
        validateProductionDashboardConfigExists(id);
        // 删除
        productionDashboardConfigMapper.deleteById(id);
    }

    @Override
        public void deleteProductionDashboardConfigListByIds(List<Long> ids) {
        // 删除
        productionDashboardConfigMapper.deleteByIds(ids);
        }


    private void validateProductionDashboardConfigExists(Long id) {
        if (productionDashboardConfigMapper.selectById(id) == null) {
            throw exception(PRODUCTION_DASHBOARD_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public ProductionDashboardConfigDO getProductionDashboardConfig(Long id) {
        return productionDashboardConfigMapper.selectById(id);
    }

    @Override
    public PageResult<ProductionDashboardConfigDO> getProductionDashboardConfigPage(ProductionDashboardConfigPageReqVO pageReqVO) {
        return productionDashboardConfigMapper.selectPage(pageReqVO);
    }

}