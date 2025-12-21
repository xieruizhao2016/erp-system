package cn.iocoder.yudao.module.erp.service.finance.receivable;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance.receivable.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.receivable.ErpFinanceReceivableDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderDO;
import cn.iocoder.yudao.module.erp.dal.mysql.finance.receivable.ErpFinanceReceivableMapper;
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
 * 应收账款 Service 实现类
 *
 * @author 开发团队
 */
@Service
@Validated
public class ErpFinanceReceivableServiceImpl implements ErpFinanceReceivableService {

    @Resource
    private ErpFinanceReceivableMapper financeReceivableMapper;

    @Resource
    private ErpNoRedisDAO noRedisDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFinanceReceivable(ErpFinanceReceivableSaveReqVO createReqVO) {
        // 1. 生成单据号（如果没有提供）
        if (createReqVO.getNo() == null || createReqVO.getNo().isEmpty()) {
            String no = noRedisDAO.generate(ErpNoRedisDAO.FINANCE_RECEIVABLE_NO_PREFIX);
            createReqVO.setNo(no);
        }

        // 2. 设置默认已收金额（如果没有提供）
        if (createReqVO.getReceivedAmount() == null) {
            createReqVO.setReceivedAmount(BigDecimal.ZERO);
        }

        // 3. 计算余额（如果没有提供）
        if (createReqVO.getBalance() == null) {
            BigDecimal amount = createReqVO.getAmount() != null ? createReqVO.getAmount() : BigDecimal.ZERO;
            BigDecimal receivedAmount = createReqVO.getReceivedAmount() != null 
                ? createReqVO.getReceivedAmount() : BigDecimal.ZERO;
            createReqVO.setBalance(amount.subtract(receivedAmount));
        }

        // 4. 设置默认状态（如果没有提供）
        if (createReqVO.getStatus() == null) {
            createReqVO.setStatus(ErpAuditStatus.PROCESS.getStatus());
        }

        // 5. 插入
        ErpFinanceReceivableDO financeReceivable = BeanUtils.toBean(createReqVO, ErpFinanceReceivableDO.class);
        financeReceivableMapper.insert(financeReceivable);

        // 返回
        return financeReceivable.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFinanceReceivable(ErpFinanceReceivableSaveReqVO updateReqVO) {
        // 校验存在
        validateFinanceReceivableExists(updateReqVO.getId());
        
        // 如果提供了金额和已收金额，但没有提供余额，则自动计算余额
        if (updateReqVO.getBalance() == null 
            && updateReqVO.getAmount() != null 
            && updateReqVO.getReceivedAmount() != null) {
            BigDecimal balance = updateReqVO.getAmount().subtract(updateReqVO.getReceivedAmount());
            updateReqVO.setBalance(balance);
        }
        
        // 更新
        ErpFinanceReceivableDO updateObj = BeanUtils.toBean(updateReqVO, ErpFinanceReceivableDO.class);
        financeReceivableMapper.updateById(updateObj);
    }

    @Override
    public void deleteFinanceReceivable(Long id) {
        // 校验存在
        validateFinanceReceivableExists(id);
        // 删除
        financeReceivableMapper.deleteById(id);
    }

    @Override
        public void deleteFinanceReceivableListByIds(List<Long> ids) {
        // 删除
        financeReceivableMapper.deleteByIds(ids);
        }


    private void validateFinanceReceivableExists(Long id) {
        if (financeReceivableMapper.selectById(id) == null) {
            throw exception(FINANCE_RECEIVABLE_NOT_EXISTS);
        }
    }

    @Override
    public ErpFinanceReceivableDO getFinanceReceivable(Long id) {
        return financeReceivableMapper.selectById(id);
    }

    @Override
    public PageResult<ErpFinanceReceivableDO> getFinanceReceivablePage(ErpFinanceReceivablePageReqVO pageReqVO) {
        return financeReceivableMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createReceivableFromSaleOrder(ErpSaleOrderDO saleOrder) {
        // 1. 检查是否已存在
        ErpFinanceReceivableDO existing = financeReceivableMapper.selectByOrderId(saleOrder.getId());
        if (existing != null) {
            return; // 已存在，不重复创建
        }

        // 2. 生成单据号
        String no = noRedisDAO.generate(ErpNoRedisDAO.FINANCE_RECEIVABLE_NO_PREFIX);

        // 3. 计算到期日（默认订单日期+30天）
        LocalDate dueDate = saleOrder.getOrderTime() != null
            ? saleOrder.getOrderTime().toLocalDate().plusDays(30)
            : LocalDate.now().plusDays(30);

        // 4. 创建应收账款
        ErpFinanceReceivableDO receivable = ErpFinanceReceivableDO.builder()
            .no(no)
            .customerId(saleOrder.getCustomerId())
            .orderId(saleOrder.getId())
            .amount(saleOrder.getTotalPrice())
            .receivedAmount(BigDecimal.ZERO)
            .balance(saleOrder.getTotalPrice())
            .dueDate(dueDate)
            .status(ErpAuditStatus.PROCESS.getStatus())
            .remark("自动生成自销售订单：" + saleOrder.getNo())
            .build();

        financeReceivableMapper.insert(receivable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReceivableByOrderId(Long orderId) {
        ErpFinanceReceivableDO receivable = financeReceivableMapper.selectByOrderId(orderId);
        if (receivable != null) {
            // 只有未审核的应收账款才能删除
            if (!ErpAuditStatus.APPROVE.getStatus().equals(receivable.getStatus())) {
                financeReceivableMapper.deleteById(receivable.getId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void writeOff(Long customerId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        // 获取该客户的未核销应收账款（按到期日排序）
        List<ErpFinanceReceivableDO> receivables = financeReceivableMapper.selectListByCustomerIdAndBalance(
            customerId, BigDecimal.ZERO);

        BigDecimal remainingAmount = amount;
        for (ErpFinanceReceivableDO receivable : receivables) {
            if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal receivableBalance = receivable.getBalance();
            if (receivableBalance == null || receivableBalance.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            // 计算本次核销金额
            BigDecimal writeOffAmount = remainingAmount.min(receivableBalance);

            // 更新应收账款
            BigDecimal newReceivedAmount = (receivable.getReceivedAmount() != null 
                ? receivable.getReceivedAmount() : BigDecimal.ZERO).add(writeOffAmount);
            BigDecimal newBalance = receivableBalance.subtract(writeOffAmount);

            ErpFinanceReceivableDO updateObj = new ErpFinanceReceivableDO();
            updateObj.setId(receivable.getId());
            updateObj.setReceivedAmount(newReceivedAmount);
            updateObj.setBalance(newBalance);
            financeReceivableMapper.updateById(updateObj);

            remainingAmount = remainingAmount.subtract(writeOffAmount);
        }
    }

}