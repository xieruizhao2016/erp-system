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

        // 3. 计算库存金额（流动资产）
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
        try {
            cn.iocoder.yudao.module.erp.controller.admin.finance.prereceipt.vo.ErpFinancePrereceiptPageReqVO prereceiptPageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.finance.prereceipt.vo.ErpFinancePrereceiptPageReqVO();
            prereceiptPageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.finance.prereceipt.ErpFinancePrereceiptDO> prereceiptPage = 
                prereceiptService.getFinancePrereceiptPage(prereceiptPageReqVO);
            
            if (prereceiptPage != null && prereceiptPage.getList() != null) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.finance.prereceipt.ErpFinancePrereceiptDO prereceipt : prereceiptPage.getList()) {
                    if (prereceipt.getBalance() != null) {
                        liabilityTotal = liabilityTotal.add(prereceipt.getBalance());
                    }
                }
            }
        } catch (Exception e) {
            // 忽略错误
        }

        return liabilityTotal;
    }

}