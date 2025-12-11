package cn.iocoder.yudao.module.erp.service.balancesheet;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance.balancesheet.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.balancesheet.ErpFinanceBalanceSheetDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 资产负债表 Service 接口
 *
 * @author 开发团队
 */
public interface ErpFinanceBalanceSheetService {

    /**
     * 创建资产负债表
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFinanceBalanceSheet(@Valid ErpFinanceBalanceSheetSaveReqVO createReqVO);

    /**
     * 更新资产负债表
     *
     * @param updateReqVO 更新信息
     */
    void updateFinanceBalanceSheet(@Valid ErpFinanceBalanceSheetSaveReqVO updateReqVO);

    /**
     * 删除资产负债表
     *
     * @param id 编号
     */
    void deleteFinanceBalanceSheet(Long id);

    /**
    * 批量删除资产负债表
    *
    * @param ids 编号
    */
    void deleteFinanceBalanceSheetListByIds(List<Long> ids);

    /**
     * 获得资产负债表
     *
     * @param id 编号
     * @return 资产负债表
     */
    ErpFinanceBalanceSheetDO getFinanceBalanceSheet(Long id);

    /**
     * 获得资产负债表分页
     *
     * @param pageReqVO 分页查询
     * @return 资产负债表分页
     */
    PageResult<ErpFinanceBalanceSheetDO> getFinanceBalanceSheetPage(ErpFinanceBalanceSheetPageReqVO pageReqVO);

}