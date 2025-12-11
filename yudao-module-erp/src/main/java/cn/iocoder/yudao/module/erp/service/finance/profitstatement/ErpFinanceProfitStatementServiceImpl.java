package cn.iocoder.yudao.module.erp.service.profitstatement;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance.profitstatement.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.profitstatement.ErpFinanceProfitStatementDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.finance.profitstatement.ErpFinanceProfitStatementMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

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

}