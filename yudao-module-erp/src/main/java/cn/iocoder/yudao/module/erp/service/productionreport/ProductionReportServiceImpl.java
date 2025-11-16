package cn.iocoder.yudao.module.erp.service.productionreport;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.productionreport.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionreport.ProductionReportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.productionreport.ProductionReportMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 生产报表 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductionReportServiceImpl implements ProductionReportService {

    @Resource
    private ProductionReportMapper productionReportMapper;

    @Override
    public Long createProductionReport(ProductionReportSaveReqVO createReqVO) {
        // 插入
        ProductionReportDO productionReport = BeanUtils.toBean(createReqVO, ProductionReportDO.class);
        productionReportMapper.insert(productionReport);

        // 返回
        return productionReport.getId();
    }

    @Override
    public void updateProductionReport(ProductionReportSaveReqVO updateReqVO) {
        // 校验存在
        validateProductionReportExists(updateReqVO.getId());
        // 更新
        ProductionReportDO updateObj = BeanUtils.toBean(updateReqVO, ProductionReportDO.class);
        productionReportMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductionReport(Long id) {
        // 校验存在
        validateProductionReportExists(id);
        // 删除
        productionReportMapper.deleteById(id);
    }

    @Override
        public void deleteProductionReportListByIds(List<Long> ids) {
        // 删除
        productionReportMapper.deleteByIds(ids);
        }


    private void validateProductionReportExists(Long id) {
        if (productionReportMapper.selectById(id) == null) {
            throw exception(PRODUCTION_REPORT_NOT_EXISTS);
        }
    }

    @Override
    public ProductionReportDO getProductionReport(Long id) {
        return productionReportMapper.selectById(id);
    }

    @Override
    public PageResult<ProductionReportDO> getProductionReportPage(ProductionReportPageReqVO pageReqVO) {
        return productionReportMapper.selectPage(pageReqVO);
    }

}