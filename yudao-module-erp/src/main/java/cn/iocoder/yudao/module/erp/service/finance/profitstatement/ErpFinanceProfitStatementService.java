package cn.iocoder.yudao.module.erp.service.finance.profitstatement;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance.profitstatement.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.profitstatement.ErpFinanceProfitStatementDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 利润表 Service 接口
 *
 * @author 开发团队
 */
public interface ErpFinanceProfitStatementService {

    /**
     * 创建利润表
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFinanceProfitStatement(@Valid ErpFinanceProfitStatementSaveReqVO createReqVO);

    /**
     * 更新利润表
     *
     * @param updateReqVO 更新信息
     */
    void updateFinanceProfitStatement(@Valid ErpFinanceProfitStatementSaveReqVO updateReqVO);

    /**
     * 删除利润表
     *
     * @param id 编号
     */
    void deleteFinanceProfitStatement(Long id);

    /**
    * 批量删除利润表
    *
    * @param ids 编号
    */
    void deleteFinanceProfitStatementListByIds(List<Long> ids);

    /**
     * 获得利润表
     *
     * @param id 编号
     * @return 利润表
     */
    ErpFinanceProfitStatementDO getFinanceProfitStatement(Long id);

    /**
     * 获得利润表分页
     *
     * @param pageReqReqVO 分页查询
     * @return 利润表分页
     */
    PageResult<ErpFinanceProfitStatementDO> getFinanceProfitStatementPage(ErpFinanceProfitStatementPageReqVO pageReqVO);

    /**
     * 计算利润表
     *
     * @param periodDate 期间日期
     */
    void calculateProfitStatement(java.time.LocalDate periodDate);

}