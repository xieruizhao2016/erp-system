package cn.iocoder.yudao.module.erp.service.finance.prereceipt;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance.prereceipt.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.prereceipt.ErpFinancePrereceiptDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.finance.prereceipt.ErpFinancePrereceiptMapper;
import cn.iocoder.yudao.module.erp.enums.ErpAuditStatus;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * 预收款 Service 实现类
 *
 * @author 开发团队
 */
@Service
@Validated
public class ErpFinancePrereceiptServiceImpl implements ErpFinancePrereceiptService {

    @Resource
    private ErpFinancePrereceiptMapper financePrereceiptMapper;

    @Override
    public Long createFinancePrereceipt(ErpFinancePrereceiptSaveReqVO createReqVO) {
        // 插入
        ErpFinancePrereceiptDO financePrereceipt = BeanUtils.toBean(createReqVO, ErpFinancePrereceiptDO.class);
        financePrereceiptMapper.insert(financePrereceipt);

        // 返回
        return financePrereceipt.getId();
    }

    @Override
    public void updateFinancePrereceipt(ErpFinancePrereceiptSaveReqVO updateReqVO) {
        // 校验存在
        validateFinancePrereceiptExists(updateReqVO.getId());
        // 更新
        ErpFinancePrereceiptDO updateObj = BeanUtils.toBean(updateReqVO, ErpFinancePrereceiptDO.class);
        financePrereceiptMapper.updateById(updateObj);
    }

    @Override
    public void deleteFinancePrereceipt(Long id) {
        // 校验存在
        validateFinancePrereceiptExists(id);
        // 删除
        financePrereceiptMapper.deleteById(id);
    }

    @Override
        public void deleteFinancePrereceiptListByIds(List<Long> ids) {
        // 删除
        financePrereceiptMapper.deleteByIds(ids);
        }


    private void validateFinancePrereceiptExists(Long id) {
        if (financePrereceiptMapper.selectById(id) == null) {
            throw exception(FINANCE_PRERECEIPT_NOT_EXISTS);
        }
    }

    @Override
    public ErpFinancePrereceiptDO getFinancePrereceipt(Long id) {
        return financePrereceiptMapper.selectById(id);
    }

    @Override
    public PageResult<ErpFinancePrereceiptDO> getFinancePrereceiptPage(ErpFinancePrereceiptPageReqVO pageReqVO) {
        return financePrereceiptMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePrereceiptByOrderId(Long orderId) {
        ErpFinancePrereceiptDO prereceipt = financePrereceiptMapper.selectByOrderId(orderId);
        if (prereceipt != null) {
            // 只有未审核的预收款才能删除
            if (!ErpAuditStatus.APPROVE.getStatus().equals(prereceipt.getStatus())) {
                financePrereceiptMapper.deleteById(prereceipt.getId());
            }
        }
    }

}