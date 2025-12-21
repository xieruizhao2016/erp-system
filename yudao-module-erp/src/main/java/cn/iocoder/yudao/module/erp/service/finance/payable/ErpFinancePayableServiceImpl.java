package cn.iocoder.yudao.module.erp.service.finance.payable;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance.payable.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.payable.ErpFinancePayableDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderDO;
import cn.iocoder.yudao.module.erp.dal.mysql.finance.payable.ErpFinancePayableMapper;
import cn.iocoder.yudao.module.erp.dal.redis.no.ErpNoRedisDAO;
import cn.iocoder.yudao.module.erp.enums.ErpAuditStatus;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * 应付账款 Service 实现类
 *
 * @author 开发团队
 */
@Service
@Validated
public class ErpFinancePayableServiceImpl implements ErpFinancePayableService {

    @Resource
    private ErpFinancePayableMapper financePayableMapper;

    @Resource
    private ErpNoRedisDAO noRedisDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFinancePayable(ErpFinancePayableSaveReqVO createReqVO) {
        // 1. 生成单据号（如果没有提供）
        if (createReqVO.getNo() == null || createReqVO.getNo().isEmpty()) {
            String no = noRedisDAO.generate(ErpNoRedisDAO.FINANCE_PAYABLE_NO_PREFIX);
            createReqVO.setNo(no);
        }

        // 2. 设置默认已付金额（如果没有提供）
        if (createReqVO.getPaidAmount() == null) {
            createReqVO.setPaidAmount(BigDecimal.ZERO);
        }

        // 3. 计算余额（如果没有提供）
        if (createReqVO.getBalance() == null) {
            BigDecimal amount = createReqVO.getAmount() != null ? createReqVO.getAmount() : BigDecimal.ZERO;
            BigDecimal paidAmount = createReqVO.getPaidAmount() != null 
                ? createReqVO.getPaidAmount() : BigDecimal.ZERO;
            createReqVO.setBalance(amount.subtract(paidAmount));
        }

        // 4. 设置默认状态（如果没有提供）
        if (createReqVO.getStatus() == null) {
            createReqVO.setStatus(ErpAuditStatus.PROCESS.getStatus());
        }

        // 5. 插入
        ErpFinancePayableDO financePayable = BeanUtils.toBean(createReqVO, ErpFinancePayableDO.class);
        financePayableMapper.insert(financePayable);

        // 返回
        return financePayable.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFinancePayable(ErpFinancePayableSaveReqVO updateReqVO) {
        // 校验存在
        validateFinancePayableExists(updateReqVO.getId());
        
        // 如果提供了金额和已付金额，但没有提供余额，则自动计算余额
        if (updateReqVO.getBalance() == null 
            && updateReqVO.getAmount() != null 
            && updateReqVO.getPaidAmount() != null) {
            BigDecimal balance = updateReqVO.getAmount().subtract(updateReqVO.getPaidAmount());
            updateReqVO.setBalance(balance);
        }
        
        // 更新
        ErpFinancePayableDO updateObj = BeanUtils.toBean(updateReqVO, ErpFinancePayableDO.class);
        financePayableMapper.updateById(updateObj);
    }

    @Override
    public void deleteFinancePayable(Long id) {
        // 校验存在
        validateFinancePayableExists(id);
        // 删除
        financePayableMapper.deleteById(id);
    }

    @Override
        public void deleteFinancePayableListByIds(List<Long> ids) {
        // 删除
        financePayableMapper.deleteByIds(ids);
        }


    private void validateFinancePayableExists(Long id) {
        if (financePayableMapper.selectById(id) == null) {
            throw exception(FINANCE_PAYABLE_NOT_EXISTS);
        }
    }

    @Override
    public ErpFinancePayableDO getFinancePayable(Long id) {
        return financePayableMapper.selectById(id);
    }

    @Override
    public PageResult<ErpFinancePayableDO> getFinancePayablePage(ErpFinancePayablePageReqVO pageReqVO) {
        return financePayableMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPayableFromPurchaseOrder(ErpPurchaseOrderDO purchaseOrder) {
        // 1. 检查是否已存在
        ErpFinancePayableDO existing = financePayableMapper.selectByOrderId(purchaseOrder.getId());
        if (existing != null) {
            return; // 已存在，不重复创建
        }

        // 2. 生成单据号
        String no = noRedisDAO.generate(ErpNoRedisDAO.FINANCE_PAYABLE_NO_PREFIX);

        // 3. 计算到期日（默认订单日期+30天）
        LocalDate dueDate = purchaseOrder.getOrderTime() != null
            ? purchaseOrder.getOrderTime().toLocalDate().plusDays(30)
            : LocalDate.now().plusDays(30);

        // 4. 创建应付账款
        ErpFinancePayableDO payable = ErpFinancePayableDO.builder()
            .no(no)
            .supplierId(purchaseOrder.getSupplierId())
            .orderId(purchaseOrder.getId())
            .amount(purchaseOrder.getTotalPrice())
            .paidAmount(BigDecimal.ZERO)
            .balance(purchaseOrder.getTotalPrice())
            .dueDate(dueDate)
            .status(ErpAuditStatus.PROCESS.getStatus())
            .remark("自动生成自采购订单：" + purchaseOrder.getNo())
            .build();

        financePayableMapper.insert(payable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePayableByOrderId(Long orderId) {
        ErpFinancePayableDO payable = financePayableMapper.selectByOrderId(orderId);
        if (payable != null) {
            // 只有未审核的应付账款才能删除
            if (!ErpAuditStatus.APPROVE.getStatus().equals(payable.getStatus())) {
                financePayableMapper.deleteById(payable.getId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void writeOff(Long supplierId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        // 获取该供应商的未核销应付账款（按到期日排序）
        List<ErpFinancePayableDO> payables = financePayableMapper.selectListBySupplierIdAndBalance(
            supplierId, BigDecimal.ZERO);

        BigDecimal remainingAmount = amount;
        for (ErpFinancePayableDO payable : payables) {
            if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal payableBalance = payable.getBalance();
            if (payableBalance == null || payableBalance.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            // 计算本次核销金额
            BigDecimal writeOffAmount = remainingAmount.min(payableBalance);

            // 更新应付账款
            BigDecimal newPaidAmount = (payable.getPaidAmount() != null 
                ? payable.getPaidAmount() : BigDecimal.ZERO).add(writeOffAmount);
            BigDecimal newBalance = payableBalance.subtract(writeOffAmount);

            ErpFinancePayableDO updateObj = new ErpFinancePayableDO();
            updateObj.setId(payable.getId());
            updateObj.setPaidAmount(newPaidAmount);
            updateObj.setBalance(newBalance);
            financePayableMapper.updateById(updateObj);

            remainingAmount = remainingAmount.subtract(writeOffAmount);
        }
    }

}