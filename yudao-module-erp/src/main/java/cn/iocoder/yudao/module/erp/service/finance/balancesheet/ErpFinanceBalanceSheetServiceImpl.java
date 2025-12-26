package cn.iocoder.yudao.module.erp.service.finance.balancesheet;

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
import org.springframework.context.annotation.Lazy;

import java.util.*;
import java.time.LocalDate;
import cn.iocoder.yudao.module.erp.controller.admin.finance.balancesheet.vo.*;
import java.time.LocalDate;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.balancesheet.ErpFinanceBalanceSheetDO;
import java.time.LocalDate;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import java.time.LocalDate;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDate;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import java.time.LocalDate;

import cn.iocoder.yudao.module.erp.dal.mysql.finance.balancesheet.ErpFinanceBalanceSheetMapper;
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
 * 资产负债表 Service 实现类
 *
 * @author 开发团队
 */
@Service
@Validated
public class ErpFinanceBalanceSheetServiceImpl implements ErpFinanceBalanceSheetService {

    @Resource
    private ErpFinanceBalanceSheetMapper financeBalanceSheetMapper;
    @Resource
    private cn.iocoder.yudao.module.erp.service.finance.ErpAccountService accountService;
    @Resource
    private cn.iocoder.yudao.module.erp.service.finance.receivable.ErpFinanceReceivableService receivableService;
    @Resource
    private cn.iocoder.yudao.module.erp.service.finance.payable.ErpFinancePayableService payableService;
    @Resource
    private cn.iocoder.yudao.module.erp.service.finance.prereceipt.ErpFinancePrereceiptService prereceiptService;
    @Resource
    private cn.iocoder.yudao.module.erp.service.stock.ErpStockService stockService;
    @Resource
    private cn.iocoder.yudao.module.erp.dal.mysql.stock.ErpStockMapper stockMapper;
    @Resource
    private cn.iocoder.yudao.module.erp.service.product.ErpProductService productService;
    @Resource
    private cn.iocoder.yudao.module.erp.dal.mysql.equipment.EquipmentMapper equipmentMapper;
    @Resource
    @Lazy
    private cn.iocoder.yudao.module.erp.service.sale.ErpSaleOrderService saleOrderService;

    @Override
    public Long createFinanceBalanceSheet(ErpFinanceBalanceSheetSaveReqVO createReqVO) {
        // 插入
        ErpFinanceBalanceSheetDO financeBalanceSheet = BeanUtils.toBean(createReqVO, ErpFinanceBalanceSheetDO.class);
        financeBalanceSheetMapper.insert(financeBalanceSheet);

        // 返回
        return financeBalanceSheet.getId();
    }

    @Override
    public void updateFinanceBalanceSheet(ErpFinanceBalanceSheetSaveReqVO updateReqVO) {
        // 校验存在
        validateFinanceBalanceSheetExists(updateReqVO.getId());
        // 更新
        ErpFinanceBalanceSheetDO updateObj = BeanUtils.toBean(updateReqVO, ErpFinanceBalanceSheetDO.class);
        financeBalanceSheetMapper.updateById(updateObj);
    }

    @Override
    public void deleteFinanceBalanceSheet(Long id) {
        // 校验存在
        validateFinanceBalanceSheetExists(id);
        // 删除
        financeBalanceSheetMapper.deleteById(id);
    }

    @Override
    public void deleteFinanceBalanceSheetListByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 删除
        financeBalanceSheetMapper.deleteByIds(ids);
    }


    private void validateFinanceBalanceSheetExists(Long id) {
        if (financeBalanceSheetMapper.selectById(id) == null) {
            throw exception(FINANCE_BALANCE_SHEET_NOT_EXISTS);
        }
    }

    @Override
    public ErpFinanceBalanceSheetDO getFinanceBalanceSheet(Long id) {
        return financeBalanceSheetMapper.selectById(id);
    }

    @Override
    public PageResult<ErpFinanceBalanceSheetDO> getFinanceBalanceSheetPage(ErpFinanceBalanceSheetPageReqVO pageReqVO) {
        return financeBalanceSheetMapper.selectPage(pageReqVO);
    }

    /**
     * 计算资产负债表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void calculateBalanceSheet(java.time.LocalDate periodDate) {
        // 计算资产总计（简化：从账户余额、库存金额等汇总）
        java.math.BigDecimal assetTotal = calculateAssets(periodDate);

        // 计算负债总计（从应付账款、预收款等汇总）
        java.math.BigDecimal liabilityTotal = calculateLiabilities(periodDate);

        // 计算所有者权益
        java.math.BigDecimal equityTotal = assetTotal.subtract(liabilityTotal);

        // 保存或更新资产负债表
        ErpFinanceBalanceSheetDO balanceSheet = getOrCreate(periodDate);
        balanceSheet.setAssetTotal(assetTotal);
        balanceSheet.setLiabilityTotal(liabilityTotal);
        balanceSheet.setEquityTotal(equityTotal);
        financeBalanceSheetMapper.updateById(balanceSheet);
    }

    /**
     * 获取或创建资产负债表
     */
    private ErpFinanceBalanceSheetDO getOrCreate(java.time.LocalDate periodDate) {
        ErpFinanceBalanceSheetPageReqVO pageReqVO = new ErpFinanceBalanceSheetPageReqVO();
        pageReqVO.setPeriodDate(periodDate);
        PageResult<ErpFinanceBalanceSheetDO> pageResult = financeBalanceSheetMapper.selectPage(pageReqVO);
        
        if (pageResult != null && !pageResult.getList().isEmpty()) {
            return pageResult.getList().get(0);
        }

        // 创建新的资产负债表
        ErpFinanceBalanceSheetDO balanceSheet = new ErpFinanceBalanceSheetDO();
        balanceSheet.setPeriodDate(periodDate);
        balanceSheet.setStatus(10); // 未审核
        financeBalanceSheetMapper.insert(balanceSheet);
        return balanceSheet;
    }

    /**
     * 计算资产总计
     */
    private java.math.BigDecimal calculateAssets(java.time.LocalDate periodDate) {
        java.math.BigDecimal assetTotal = java.math.BigDecimal.ZERO;

        // 1. 计算账户余额（流动资产）
        try {
            cn.iocoder.yudao.module.erp.controller.admin.finance.vo.account.ErpAccountPageReqVO accountPageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.finance.vo.account.ErpAccountPageReqVO();
            accountPageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE); // 获取所有数据
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpAccountDO> accountPage = 
                accountService.getAccountPage(accountPageReqVO);
            
            if (accountPage != null && accountPage.getList() != null) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpAccountDO account : accountPage.getList()) {
                    if (account.getBalance() != null) {
                        assetTotal = assetTotal.add(account.getBalance());
                    }
                }
            }
        } catch (Exception e) {
            // 忽略错误，继续计算其他资产
        }

        // 2. 计算应收账款余额（流动资产）
        try {
            cn.iocoder.yudao.module.erp.controller.admin.finance.receivable.vo.ErpFinanceReceivablePageReqVO receivablePageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.finance.receivable.vo.ErpFinanceReceivablePageReqVO();
            receivablePageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.finance.receivable.ErpFinanceReceivableDO> receivablePage = 
                receivableService.getFinanceReceivablePage(receivablePageReqVO);
            
            if (receivablePage != null && receivablePage.getList() != null) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.finance.receivable.ErpFinanceReceivableDO receivable : receivablePage.getList()) {
                    if (receivable.getBalance() != null) {
                        assetTotal = assetTotal.add(receivable.getBalance());
                    }
                }
            }
        } catch (Exception e) {
            // 忽略错误
        }

        // 3. 计算库存金额（流动资产）- 库存产品
        try {
            // 查询所有库存记录
            List<cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpStockDO> stockList = 
                stockMapper.selectList();
            
            if (CollUtil.isNotEmpty(stockList)) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpStockDO stock : stockList) {
                    if (stock.getProductId() != null && stock.getCount() != null && stock.getCount().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        // 获取产品价格
                        cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO product = 
                            productService.getProduct(stock.getProductId());
                        
                        java.math.BigDecimal productPrice = java.math.BigDecimal.ZERO;
                        if (product != null) {
                            // 优先使用销售价格，如果没有则使用采购价格
                            if (product.getSalePrice() != null && product.getSalePrice().compareTo(java.math.BigDecimal.ZERO) > 0) {
                                productPrice = product.getSalePrice();
                            } else if (product.getPurchasePrice() != null && product.getPurchasePrice().compareTo(java.math.BigDecimal.ZERO) > 0) {
                                productPrice = product.getPurchasePrice();
                            }
                        }
                        
                        // 库存金额 = 库存数量 * 产品价格
                        java.math.BigDecimal stockAmount = stock.getCount().multiply(productPrice);
                        assetTotal = assetTotal.add(stockAmount);
                    }
                }
            }
        } catch (Exception e) {
            // 忽略错误
        }

        // 4. 计算设备价值（固定资产）
        try {
            // 查询所有设备记录
            List<cn.iocoder.yudao.module.erp.dal.dataobject.equipment.EquipmentDO> equipmentList = 
                equipmentMapper.selectList();
            
            if (CollUtil.isNotEmpty(equipmentList)) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.equipment.EquipmentDO equipment : equipmentList) {
                    // 排除报废设备（状态为4）
                    if (equipment.getStatus() != null && equipment.getStatus() == 4) {
                        continue;
                    }
                    
                    // 设备价值 = 购置价格
                    if (equipment.getPurchasePrice() != null && equipment.getPurchasePrice().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        assetTotal = assetTotal.add(equipment.getPurchasePrice());
                    }
                }
            }
        } catch (Exception e) {
            // 忽略错误
        }

        // 5. 计算预收款余额（流动资产）- 预收款收到现金，增加资产
        // 从会计角度：收到预收款时，借：银行存款（资产增加），贷：预收账款（负债增加）
        // 所以预收款应该同时计入资产和负债
        try {
            cn.iocoder.yudao.module.erp.controller.admin.finance.prereceipt.vo.ErpFinancePrereceiptPageReqVO prereceiptPageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.finance.prereceipt.vo.ErpFinancePrereceiptPageReqVO();
            prereceiptPageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.finance.prereceipt.ErpFinancePrereceiptDO> prereceiptPage = 
                prereceiptService.getFinancePrereceiptPage(prereceiptPageReqVO);
            
            if (prereceiptPage != null && prereceiptPage.getList() != null) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.finance.prereceipt.ErpFinancePrereceiptDO prereceipt : prereceiptPage.getList()) {
                    if (prereceipt.getBalance() != null) {
                        assetTotal = assetTotal.add(prereceipt.getBalance());
                    }
                }
            }
        } catch (Exception e) {
            // 忽略错误
        }

        return assetTotal;
    }

    /**
     * 计算负债总计
     */
    private java.math.BigDecimal calculateLiabilities(java.time.LocalDate periodDate) {
        java.math.BigDecimal liabilityTotal = java.math.BigDecimal.ZERO;

        // 1. 计算应付账款余额（流动负债）
        try {
            cn.iocoder.yudao.module.erp.controller.admin.finance.payable.vo.ErpFinancePayablePageReqVO payablePageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.finance.payable.vo.ErpFinancePayablePageReqVO();
            payablePageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.finance.payable.ErpFinancePayableDO> payablePage = 
                payableService.getFinancePayablePage(payablePageReqVO);
            
            if (payablePage != null && payablePage.getList() != null) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.finance.payable.ErpFinancePayableDO payable : payablePage.getList()) {
                    if (payable.getBalance() != null) {
                        liabilityTotal = liabilityTotal.add(payable.getBalance());
                    }
                }
            }
        } catch (Exception e) {
            // 忽略错误，继续计算其他负债
        }

        // 2. 计算预收款余额（流动负债）
        // 只有未出库的销售订单对应的预收款才计入负债
        // 已出库的预收款不再计入负债（因为已经完成交付义务）
        try {
            cn.iocoder.yudao.module.erp.controller.admin.finance.prereceipt.vo.ErpFinancePrereceiptPageReqVO prereceiptPageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.finance.prereceipt.vo.ErpFinancePrereceiptPageReqVO();
            prereceiptPageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.finance.prereceipt.ErpFinancePrereceiptDO> prereceiptPage = 
                prereceiptService.getFinancePrereceiptPage(prereceiptPageReqVO);
            
            if (prereceiptPage != null && prereceiptPage.getList() != null) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.finance.prereceipt.ErpFinancePrereceiptDO prereceipt : prereceiptPage.getList()) {
                    if (prereceipt.getBalance() != null && prereceipt.getBalance().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        // 如果有关联的销售订单，检查是否已出库
                        if (prereceipt.getOrderId() != null) {
                            try {
                                cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderDO saleOrder = 
                                    saleOrderService.getSaleOrder(prereceipt.getOrderId());
                                if (saleOrder != null) {
                                    // 如果已出库（outCount > 0 且等于 totalCount），则不计入负债
                                    java.math.BigDecimal outCount = saleOrder.getOutCount() != null ? saleOrder.getOutCount() : java.math.BigDecimal.ZERO;
                                    java.math.BigDecimal totalCount = saleOrder.getTotalCount() != null ? saleOrder.getTotalCount() : java.math.BigDecimal.ZERO;
                                    // 如果未完全出库，则计入负债
                                    if (outCount.compareTo(totalCount) < 0) {
                                        liabilityTotal = liabilityTotal.add(prereceipt.getBalance());
                                    }
                                } else {
                                    // 如果订单不存在，默认计入负债
                                    liabilityTotal = liabilityTotal.add(prereceipt.getBalance());
                                }
                            } catch (Exception e) {
                                // 如果查询订单失败，默认计入负债
                                liabilityTotal = liabilityTotal.add(prereceipt.getBalance());
                            }
                        } else {
                            // 如果没有关联订单，默认计入负债
                            liabilityTotal = liabilityTotal.add(prereceipt.getBalance());
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 忽略错误
        }

        return liabilityTotal;
    }

    @Override
    public ErpFinanceBalanceSheetStatisticsRespVO getBalanceSheetStatistics() {
        try {
            ErpFinanceBalanceSheetStatisticsRespVO statistics = new ErpFinanceBalanceSheetStatisticsRespVO();
            
            // 实时计算资产和负债总额（与资产构成保持一致）
            java.time.LocalDate currentDate = java.time.LocalDate.now();
            java.math.BigDecimal assetTotal = calculateAssets(currentDate);
            java.math.BigDecimal liabilityTotal = calculateLiabilities(currentDate);
            java.math.BigDecimal equityTotal = assetTotal.subtract(liabilityTotal);
        
        statistics.setAssetTotal(assetTotal);
        statistics.setLiabilityTotal(liabilityTotal);
        statistics.setEquityTotal(equityTotal);
        
        // 计算资产构成
        List<ErpFinanceBalanceSheetStatisticsRespVO.AssetCompositionItem> assetComposition = new ArrayList<>();
        
        // 账户余额
        try {
            cn.iocoder.yudao.module.erp.controller.admin.finance.vo.account.ErpAccountPageReqVO accountPageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.finance.vo.account.ErpAccountPageReqVO();
            accountPageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpAccountDO> accountPage = 
                accountService.getAccountPage(accountPageReqVO);
            
            java.math.BigDecimal accountBalance = java.math.BigDecimal.ZERO;
            if (accountPage != null && accountPage.getList() != null) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpAccountDO account : accountPage.getList()) {
                    if (account.getBalance() != null) {
                        accountBalance = accountBalance.add(account.getBalance());
                    }
                }
            }
            if (accountBalance.compareTo(java.math.BigDecimal.ZERO) > 0) {
                ErpFinanceBalanceSheetStatisticsRespVO.AssetCompositionItem item = new ErpFinanceBalanceSheetStatisticsRespVO.AssetCompositionItem();
                item.setName("账户余额");
                item.setAmount(accountBalance);
                assetComposition.add(item);
            }
        } catch (Exception e) {
            // 忽略错误
        }
        
        // 应收账款
        try {
            cn.iocoder.yudao.module.erp.controller.admin.finance.receivable.vo.ErpFinanceReceivablePageReqVO receivablePageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.finance.receivable.vo.ErpFinanceReceivablePageReqVO();
            receivablePageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.finance.receivable.ErpFinanceReceivableDO> receivablePage = 
                receivableService.getFinanceReceivablePage(receivablePageReqVO);
            
            java.math.BigDecimal receivableBalance = java.math.BigDecimal.ZERO;
            if (receivablePage != null && receivablePage.getList() != null) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.finance.receivable.ErpFinanceReceivableDO receivable : receivablePage.getList()) {
                    if (receivable.getBalance() != null) {
                        receivableBalance = receivableBalance.add(receivable.getBalance());
                    }
                }
            }
            if (receivableBalance.compareTo(java.math.BigDecimal.ZERO) > 0) {
                ErpFinanceBalanceSheetStatisticsRespVO.AssetCompositionItem item = new ErpFinanceBalanceSheetStatisticsRespVO.AssetCompositionItem();
                item.setName("应收账款");
                item.setAmount(receivableBalance);
                assetComposition.add(item);
            }
        } catch (Exception e) {
            // 忽略错误
        }
        
        // 库存金额
        try {
            java.math.BigDecimal stockAmount = java.math.BigDecimal.ZERO;
            List<cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpStockDO> stockList = stockMapper.selectList();
            if (CollUtil.isNotEmpty(stockList)) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpStockDO stock : stockList) {
                    if (stock.getProductId() != null && stock.getCount() != null && stock.getCount().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO product = productService.getProduct(stock.getProductId());
                        java.math.BigDecimal productPrice = java.math.BigDecimal.ZERO;
                        if (product != null) {
                            if (product.getSalePrice() != null && product.getSalePrice().compareTo(java.math.BigDecimal.ZERO) > 0) {
                                productPrice = product.getSalePrice();
                            } else if (product.getPurchasePrice() != null && product.getPurchasePrice().compareTo(java.math.BigDecimal.ZERO) > 0) {
                                productPrice = product.getPurchasePrice();
                            }
                        }
                        stockAmount = stockAmount.add(stock.getCount().multiply(productPrice));
                    }
                }
            }
            if (stockAmount.compareTo(java.math.BigDecimal.ZERO) > 0) {
                ErpFinanceBalanceSheetStatisticsRespVO.AssetCompositionItem item = new ErpFinanceBalanceSheetStatisticsRespVO.AssetCompositionItem();
                item.setName("库存金额");
                item.setAmount(stockAmount);
                assetComposition.add(item);
            }
        } catch (Exception e) {
            // 忽略错误
        }
        
        // 设备价值
        try {
            java.math.BigDecimal equipmentValue = java.math.BigDecimal.ZERO;
            List<cn.iocoder.yudao.module.erp.dal.dataobject.equipment.EquipmentDO> equipmentList = equipmentMapper.selectList();
            if (CollUtil.isNotEmpty(equipmentList)) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.equipment.EquipmentDO equipment : equipmentList) {
                    if (equipment.getStatus() != null && equipment.getStatus() == 4) {
                        continue;
                    }
                    if (equipment.getPurchasePrice() != null && equipment.getPurchasePrice().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        equipmentValue = equipmentValue.add(equipment.getPurchasePrice());
                    }
                }
            }
            if (equipmentValue.compareTo(java.math.BigDecimal.ZERO) > 0) {
                ErpFinanceBalanceSheetStatisticsRespVO.AssetCompositionItem item = new ErpFinanceBalanceSheetStatisticsRespVO.AssetCompositionItem();
                item.setName("设备价值");
                item.setAmount(equipmentValue);
                assetComposition.add(item);
            }
        } catch (Exception e) {
            // 忽略错误
        }
        
        // 预收款（收到现金，计入资产）
        // 所有预收款余额都计入资产，因为收到现金
        try {
            cn.iocoder.yudao.module.erp.controller.admin.finance.prereceipt.vo.ErpFinancePrereceiptPageReqVO prereceiptPageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.finance.prereceipt.vo.ErpFinancePrereceiptPageReqVO();
            prereceiptPageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.finance.prereceipt.ErpFinancePrereceiptDO> prereceiptPage = 
                prereceiptService.getFinancePrereceiptPage(prereceiptPageReqVO);
            
            java.math.BigDecimal prereceiptBalance = java.math.BigDecimal.ZERO;
            if (prereceiptPage != null && prereceiptPage.getList() != null) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.finance.prereceipt.ErpFinancePrereceiptDO prereceipt : prereceiptPage.getList()) {
                    if (prereceipt.getBalance() != null && prereceipt.getBalance().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        prereceiptBalance = prereceiptBalance.add(prereceipt.getBalance());
                    }
                }
            }
            // 无论金额大小，只要有余额就显示（即使为0也显示，确保数据一致性）
            if (prereceiptBalance.compareTo(java.math.BigDecimal.ZERO) >= 0) {
                ErpFinanceBalanceSheetStatisticsRespVO.AssetCompositionItem item = new ErpFinanceBalanceSheetStatisticsRespVO.AssetCompositionItem();
                item.setName("预收款（现金）");
                item.setAmount(prereceiptBalance);
                assetComposition.add(item);
            }
        } catch (Exception e) {
            // 忽略错误
        }
        
        statistics.setAssetComposition(assetComposition);
        
        // 计算负债构成
        List<ErpFinanceBalanceSheetStatisticsRespVO.LiabilityCompositionItem> liabilityComposition = new ArrayList<>();
        
        // 应付账款
        try {
            cn.iocoder.yudao.module.erp.controller.admin.finance.payable.vo.ErpFinancePayablePageReqVO payablePageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.finance.payable.vo.ErpFinancePayablePageReqVO();
            payablePageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.finance.payable.ErpFinancePayableDO> payablePage = 
                payableService.getFinancePayablePage(payablePageReqVO);
            
            java.math.BigDecimal payableBalance = java.math.BigDecimal.ZERO;
            if (payablePage != null && payablePage.getList() != null) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.finance.payable.ErpFinancePayableDO payable : payablePage.getList()) {
                    if (payable.getBalance() != null) {
                        payableBalance = payableBalance.add(payable.getBalance());
                    }
                }
            }
            if (payableBalance.compareTo(java.math.BigDecimal.ZERO) > 0) {
                ErpFinanceBalanceSheetStatisticsRespVO.LiabilityCompositionItem item = new ErpFinanceBalanceSheetStatisticsRespVO.LiabilityCompositionItem();
                item.setName("应付账款");
                item.setAmount(payableBalance);
                liabilityComposition.add(item);
            }
        } catch (Exception e) {
            // 忽略错误
        }
        
        // 预收款（只有未出库的销售订单对应的预收款才计入负债）
        try {
            cn.iocoder.yudao.module.erp.controller.admin.finance.prereceipt.vo.ErpFinancePrereceiptPageReqVO prereceiptPageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.finance.prereceipt.vo.ErpFinancePrereceiptPageReqVO();
            prereceiptPageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.finance.prereceipt.ErpFinancePrereceiptDO> prereceiptPage = 
                prereceiptService.getFinancePrereceiptPage(prereceiptPageReqVO);
            
            java.math.BigDecimal prereceiptLiabilityBalance = java.math.BigDecimal.ZERO;
            if (prereceiptPage != null && prereceiptPage.getList() != null) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.finance.prereceipt.ErpFinancePrereceiptDO prereceipt : prereceiptPage.getList()) {
                    if (prereceipt.getBalance() != null && prereceipt.getBalance().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        // 如果有关联的销售订单，检查是否已出库
                        if (prereceipt.getOrderId() != null) {
                            try {
                                cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderDO saleOrder = 
                                    saleOrderService.getSaleOrder(prereceipt.getOrderId());
                                if (saleOrder != null) {
                                    // 如果已出库（outCount >= totalCount），则不计入负债
                                    java.math.BigDecimal outCount = saleOrder.getOutCount() != null ? saleOrder.getOutCount() : java.math.BigDecimal.ZERO;
                                    java.math.BigDecimal totalCount = saleOrder.getTotalCount() != null ? saleOrder.getTotalCount() : java.math.BigDecimal.ZERO;
                                    // 如果未完全出库，则计入负债
                                    if (outCount.compareTo(totalCount) < 0) {
                                        prereceiptLiabilityBalance = prereceiptLiabilityBalance.add(prereceipt.getBalance());
                                    }
                                } else {
                                    // 如果订单不存在，默认计入负债
                                    prereceiptLiabilityBalance = prereceiptLiabilityBalance.add(prereceipt.getBalance());
                                }
                            } catch (Exception e) {
                                // 如果查询订单失败，默认计入负债
                                prereceiptLiabilityBalance = prereceiptLiabilityBalance.add(prereceipt.getBalance());
                            }
                        } else {
                            // 如果没有关联订单，默认计入负债
                            prereceiptLiabilityBalance = prereceiptLiabilityBalance.add(prereceipt.getBalance());
                        }
                    }
                }
            }
            if (prereceiptLiabilityBalance.compareTo(java.math.BigDecimal.ZERO) > 0) {
                ErpFinanceBalanceSheetStatisticsRespVO.LiabilityCompositionItem item = new ErpFinanceBalanceSheetStatisticsRespVO.LiabilityCompositionItem();
                item.setName("预收款");
                item.setAmount(prereceiptLiabilityBalance);
                liabilityComposition.add(item);
            }
        } catch (Exception e) {
            // 忽略错误
        }
        
        statistics.setLiabilityComposition(liabilityComposition);
        
        // 获取月度趋势数据（最近12个月）
        List<ErpFinanceBalanceSheetStatisticsRespVO.MonthlyTrendItem> monthlyTrend = new ArrayList<>();
        try {
            java.time.LocalDate startDate = currentDate.minusMonths(11).withDayOfMonth(1);
            for (int i = 0; i < 12; i++) {
                java.time.LocalDate monthDate = startDate.plusMonths(i);
                ErpFinanceBalanceSheetPageReqVO trendPageReqVO = new ErpFinanceBalanceSheetPageReqVO();
                trendPageReqVO.setPeriodDate(monthDate);
                trendPageReqVO.setPageSize(1);
                trendPageReqVO.setPageNo(1);
                PageResult<ErpFinanceBalanceSheetDO> trendPageResult = financeBalanceSheetMapper.selectPage(trendPageReqVO);
                
                ErpFinanceBalanceSheetStatisticsRespVO.MonthlyTrendItem trendItem = new ErpFinanceBalanceSheetStatisticsRespVO.MonthlyTrendItem();
                trendItem.setMonth(monthDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM")));
                
                if (trendPageResult != null && !trendPageResult.getList().isEmpty()) {
                    ErpFinanceBalanceSheetDO sheet = trendPageResult.getList().get(0);
                    trendItem.setAssetTotal(sheet.getAssetTotal() != null ? sheet.getAssetTotal() : java.math.BigDecimal.ZERO);
                    trendItem.setLiabilityTotal(sheet.getLiabilityTotal() != null ? sheet.getLiabilityTotal() : java.math.BigDecimal.ZERO);
                } else {
                    trendItem.setAssetTotal(java.math.BigDecimal.ZERO);
                    trendItem.setLiabilityTotal(java.math.BigDecimal.ZERO);
                }
                
                monthlyTrend.add(trendItem);
            }
        } catch (Exception e) {
            // 忽略错误
        }
        
        statistics.setMonthlyTrend(monthlyTrend);
        
        return statistics;
        } catch (Exception e) {
            // 记录异常日志
            org.slf4j.LoggerFactory.getLogger(ErpFinanceBalanceSheetServiceImpl.class).error("获取资产负债表统计数据失败", e);
            // 返回默认值，避免前端报错
            ErpFinanceBalanceSheetStatisticsRespVO statistics = new ErpFinanceBalanceSheetStatisticsRespVO();
            statistics.setAssetTotal(java.math.BigDecimal.ZERO);
            statistics.setLiabilityTotal(java.math.BigDecimal.ZERO);
            statistics.setEquityTotal(java.math.BigDecimal.ZERO);
            statistics.setAssetComposition(new ArrayList<>());
            statistics.setLiabilityComposition(new ArrayList<>());
            statistics.setMonthlyTrend(new ArrayList<>());
            return statistics;
        }
    }

}