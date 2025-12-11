package cn.iocoder.yudao.module.erp.service.balancesheet;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance.balancesheet.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.balancesheet.ErpFinanceBalanceSheetDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.finance.balancesheet.ErpFinanceBalanceSheetMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

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

}