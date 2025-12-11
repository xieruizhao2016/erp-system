package cn.iocoder.yudao.module.erp.service.finance-payable;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance-payable.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance-payable.ErpFinancePayableDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.finance-payable.ErpFinancePayableMapper;

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

    @Override
    public ${primaryColumn.javaType} createFinancePayable(ErpFinancePayableSaveReqVO createReqVO) {
        // 插入
        ErpFinancePayableDO financePayable = BeanUtils.toBean(createReqVO, ErpFinancePayableDO.class);
        financePayableMapper.insert(financePayable);

        // 返回
        return financePayable.getId();
    }

    @Override
    public void updateFinancePayable(ErpFinancePayableSaveReqVO updateReqVO) {
        // 校验存在
        validateFinancePayableExists(updateReqVO.getId());
        // 更新
        ErpFinancePayableDO updateObj = BeanUtils.toBean(updateReqVO, ErpFinancePayableDO.class);
        financePayableMapper.updateById(updateObj);
    }

    @Override
    public void deleteFinancePayable(${primaryColumn.javaType} id) {
        // 校验存在
        validateFinancePayableExists(id);
        // 删除
        financePayableMapper.deleteById(id);
    }

    @Override
        public void deleteFinancePayableListByIds(List<${primaryColumn.javaType}> ids) {
        // 删除
        financePayableMapper.deleteByIds(ids);
        }


    private void validateFinancePayableExists(${primaryColumn.javaType} id) {
        if (financePayableMapper.selectById(id) == null) {
            throw exception(FINANCE_PAYABLE_NOT_EXISTS);
        }
    }

    @Override
    public ErpFinancePayableDO getFinancePayable(${primaryColumn.javaType} id) {
        return financePayableMapper.selectById(id);
    }

    @Override
    public PageResult<ErpFinancePayableDO> getFinancePayablePage(ErpFinancePayablePageReqVO pageReqVO) {
        return financePayableMapper.selectPage(pageReqVO);
    }

}