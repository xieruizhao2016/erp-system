package cn.iocoder.yudao.module.erp.service.finance-receivable;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance-receivable.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance-receivable.ErpFinanceReceivableDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.finance-receivable.ErpFinanceReceivableMapper;

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

    @Override
    public ${primaryColumn.javaType} createFinanceReceivable(ErpFinanceReceivableSaveReqVO createReqVO) {
        // 插入
        ErpFinanceReceivableDO financeReceivable = BeanUtils.toBean(createReqVO, ErpFinanceReceivableDO.class);
        financeReceivableMapper.insert(financeReceivable);

        // 返回
        return financeReceivable.getId();
    }

    @Override
    public void updateFinanceReceivable(ErpFinanceReceivableSaveReqVO updateReqVO) {
        // 校验存在
        validateFinanceReceivableExists(updateReqVO.getId());
        // 更新
        ErpFinanceReceivableDO updateObj = BeanUtils.toBean(updateReqVO, ErpFinanceReceivableDO.class);
        financeReceivableMapper.updateById(updateObj);
    }

    @Override
    public void deleteFinanceReceivable(${primaryColumn.javaType} id) {
        // 校验存在
        validateFinanceReceivableExists(id);
        // 删除
        financeReceivableMapper.deleteById(id);
    }

    @Override
        public void deleteFinanceReceivableListByIds(List<${primaryColumn.javaType}> ids) {
        // 删除
        financeReceivableMapper.deleteByIds(ids);
        }


    private void validateFinanceReceivableExists(${primaryColumn.javaType} id) {
        if (financeReceivableMapper.selectById(id) == null) {
            throw exception(FINANCE_RECEIVABLE_NOT_EXISTS);
        }
    }

    @Override
    public ErpFinanceReceivableDO getFinanceReceivable(${primaryColumn.javaType} id) {
        return financeReceivableMapper.selectById(id);
    }

    @Override
    public PageResult<ErpFinanceReceivableDO> getFinanceReceivablePage(ErpFinanceReceivablePageReqVO pageReqVO) {
        return financeReceivableMapper.selectPage(pageReqVO);
    }

}