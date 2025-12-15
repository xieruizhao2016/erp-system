package cn.iocoder.yudao.module.erp.service.finance.profitstatement;

import cn.hutool.core.collection.CollUtil;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import javax.annotation.Resource;
import java.time.LocalDate;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

import java.util.*;
import java.time.LocalDate;
import cn.iocoder.yudao.module.erp.controller.admin.finance.profitstatement.vo.*;
import java.time.LocalDate;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.profitstatement.ErpFinanceProfitStatementDO;
import java.time.LocalDate;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import java.time.LocalDate;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDate;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import java.time.LocalDate;

import cn.iocoder.yudao.module.erp.dal.mysql.finance.profitstatement.ErpFinanceProfitStatementMapper;
import java.time.LocalDate;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import java.time.LocalDate;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import java.time.LocalDate;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import java.time.LocalDate;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;
import java.time.LocalDate;

/**
 * 利润表 Service 实现类
 *
 * @author 开发团队
 */
@Service
@Validated
public class ErpFinanceProfitStatementServiceImpl implements ErpFinanceProfitStatementService {

    @Resource
    private ErpFinanceProfitStatementMapper financeProfitStatementMapper;
    @Resource
    private cn.iocoder.yudao.module.erp.service.sale.ErpSaleOrderService saleOrderService;
    @Resource
    private cn.iocoder.yudao.module.erp.service.purchase.ErpPurchaseOrderService purchaseOrderService;

    @Override
    public Long createFinanceProfitStatement(ErpFinanceProfitStatementSaveReqVO createReqVO) {
        // 插入
        ErpFinanceProfitStatementDO financeProfitStatement = BeanUtils.toBean(createReqVO, ErpFinanceProfitStatementDO.class);
        financeProfitStatementMapper.insert(financeProfitStatement);

        // 返回
        return financeProfitStatement.getId();
    }

    @Override
    public void updateFinanceProfitStatement(ErpFinanceProfitStatementSaveReqVO updateReqVO) {
        // 校验存在
        validateFinanceProfitStatementExists(updateReqVO.getId());
        // 更新
        ErpFinanceProfitStatementDO updateObj = BeanUtils.toBean(updateReqVO, ErpFinanceProfitStatementDO.class);
        financeProfitStatementMapper.updateById(updateObj);
    }

    @Override
    public void deleteFinanceProfitStatement(Long id) {
        // 校验存在
        validateFinanceProfitStatementExists(id);
        // 删除
        financeProfitStatementMapper.deleteById(id);
    }

    @Override
    public void deleteFinanceProfitStatementListByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 删除
        financeProfitStatementMapper.deleteByIds(ids);
    }


    private void validateFinanceProfitStatementExists(Long id) {
        if (financeProfitStatementMapper.selectById(id) == null) {
            throw exception(FINANCE_PROFIT_STATEMENT_NOT_EXISTS);
        }
    }

    @Override
    public ErpFinanceProfitStatementDO getFinanceProfitStatement(Long id) {
        return financeProfitStatementMapper.selectById(id);
    }

    @Override
    public PageResult<ErpFinanceProfitStatementDO> getFinanceProfitStatementPage(ErpFinanceProfitStatementPageReqVO pageReqVO) {
        return financeProfitStatementMapper.selectPage(pageReqVO);
    }

    /**
     * 计算利润表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void calculateProfitStatement(java.time.LocalDate periodDate) {
        // 计算营业收入（汇总已审核的销售订单）
        java.math.BigDecimal revenue = calculateRevenue(periodDate);

        // 计算营业成本（汇总已审核的采购订单 + 生产订单成本）
        java.math.BigDecimal cost = calculateCost(periodDate);

        // 计算毛利润
        java.math.BigDecimal grossProfit = revenue.subtract(cost);

        // 计算净利润（毛利润 - 营业费用）
        java.math.BigDecimal operatingExpense = getOperatingExpense(periodDate);
        java.math.BigDecimal netProfit = grossProfit.subtract(operatingExpense);

        // 保存或更新利润表
        ErpFinanceProfitStatementDO profitStatement = getOrCreate(periodDate);
        profitStatement.setRevenue(revenue);
        profitStatement.setCost(cost);
        profitStatement.setGrossProfit(grossProfit);
        profitStatement.setOperatingExpense(operatingExpense);
        profitStatement.setNetProfit(netProfit);
        financeProfitStatementMapper.updateById(profitStatement);
    }

    /**
     * 获取或创建利润表
     */
    private ErpFinanceProfitStatementDO getOrCreate(java.time.LocalDate periodDate) {
        ErpFinanceProfitStatementPageReqVO pageReqVO = new ErpFinanceProfitStatementPageReqVO();
        pageReqVO.setPeriodDate(periodDate);
        PageResult<ErpFinanceProfitStatementDO> pageResult = financeProfitStatementMapper.selectPage(pageReqVO);

        if (pageResult != null && !pageResult.getList().isEmpty()) {
            return pageResult.getList().get(0);
        }

        // 创建新的利润表
        ErpFinanceProfitStatementDO profitStatement = new ErpFinanceProfitStatementDO();
        profitStatement.setPeriodDate(periodDate);
        profitStatement.setStatus(10); // 未审核
        financeProfitStatementMapper.insert(profitStatement);
        return profitStatement;
    }

    /**
     * 计算营业收入（汇总已审核的销售订单）
     */
    private java.math.BigDecimal calculateRevenue(java.time.LocalDate periodDate) {
        java.math.BigDecimal revenue = java.math.BigDecimal.ZERO;

        try {
            // 计算期间开始和结束时间
            java.time.LocalDateTime startTime = periodDate.atStartOfDay();
            java.time.LocalDateTime endTime = periodDate.plusMonths(1).atStartOfDay().minusSeconds(1);

            // 查询指定期间内已审核的销售订单
            cn.iocoder.yudao.module.erp.controller.admin.sale.vo.order.ErpSaleOrderPageReqVO pageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.sale.vo.order.ErpSaleOrderPageReqVO();
            pageReqVO.setStatus(20); // 20表示已审核
            pageReqVO.setOrderTime(new java.time.LocalDateTime[]{startTime, endTime});
            pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE); // 获取所有数据

            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderDO> saleOrderPage = 
                saleOrderService.getSaleOrderPage(pageReqVO);

            if (saleOrderPage != null && saleOrderPage.getList() != null) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderDO saleOrder : saleOrderPage.getList()) {
                    if (saleOrder.getTotalPrice() != null) {
                        revenue = revenue.add(saleOrder.getTotalPrice());
                    }
                }
            }
        } catch (Exception e) {
            // 如果计算失败，返回0
            return java.math.BigDecimal.ZERO;
        }

        return revenue;
    }

    /**
     * 计算营业成本（汇总已审核的采购订单 + 生产订单成本）
     */
    private java.math.BigDecimal calculateCost(java.time.LocalDate periodDate) {
        java.math.BigDecimal cost = java.math.BigDecimal.ZERO;

        try {
            // 计算期间开始和结束时间
            java.time.LocalDateTime startTime = periodDate.atStartOfDay();
            java.time.LocalDateTime endTime = periodDate.plusMonths(1).atStartOfDay().minusSeconds(1);

            // 1. 汇总已审核的采购订单成本
            cn.iocoder.yudao.module.erp.controller.admin.purchase.vo.order.ErpPurchaseOrderPageReqVO pageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.purchase.vo.order.ErpPurchaseOrderPageReqVO();
            pageReqVO.setStatus(20); // 20表示已审核
            pageReqVO.setOrderTime(new java.time.LocalDateTime[]{startTime, endTime});
            pageReqVO.setPageSize(10000);

            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderDO> purchaseOrderPage = 
                purchaseOrderService.getPurchaseOrderPage(pageReqVO);

            if (purchaseOrderPage != null && purchaseOrderPage.getList() != null) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderDO purchaseOrder : purchaseOrderPage.getList()) {
                    if (purchaseOrder.getTotalPrice() != null) {
                        cost = cost.add(purchaseOrder.getTotalPrice());
                    }
                }
            }

            // 2. TODO: 汇总生产订单成本（如果有生产订单模块）
            // 这里可以查询生产订单表，汇总生产成本

        } catch (Exception e) {
            // 如果计算失败，返回0
            return java.math.BigDecimal.ZERO;
        }

        return cost;
    }

    /**
     * 获取营业费用
     */
    private java.math.BigDecimal getOperatingExpense(java.time.LocalDate periodDate) {
        // TODO: 实现从费用表或其他数据源获取营业费用
        // 可以查询费用表、付款单中的费用类型等
        // 这里先返回0，后续可以根据实际业务需求实现
        return java.math.BigDecimal.ZERO;
    }

}