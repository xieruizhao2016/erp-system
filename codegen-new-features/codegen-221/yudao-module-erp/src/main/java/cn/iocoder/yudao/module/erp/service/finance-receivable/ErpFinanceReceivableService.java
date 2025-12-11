package cn.iocoder.yudao.module.erp.service.finance-receivable;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance-receivable.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance-receivable.ErpFinanceReceivableDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 应收账款 Service 接口
 *
 * @author 开发团队
 */
public interface ErpFinanceReceivableService {

    /**
     * 创建应收账款
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    ${primaryColumn.javaType} createFinanceReceivable(@Valid ErpFinanceReceivableSaveReqVO createReqVO);

    /**
     * 更新应收账款
     *
     * @param updateReqVO 更新信息
     */
    void updateFinanceReceivable(@Valid ErpFinanceReceivableSaveReqVO updateReqVO);

    /**
     * 删除应收账款
     *
     * @param id 编号
     */
    void deleteFinanceReceivable(${primaryColumn.javaType} id);

    /**
    * 批量删除应收账款
    *
    * @param ids 编号
    */
    void deleteFinanceReceivableListByIds(List<${primaryColumn.javaType}> ids);

    /**
     * 获得应收账款
     *
     * @param id 编号
     * @return 应收账款
     */
    ErpFinanceReceivableDO getFinanceReceivable(${primaryColumn.javaType} id);

    /**
     * 获得应收账款分页
     *
     * @param pageReqVO 分页查询
     * @return 应收账款分页
     */
    PageResult<ErpFinanceReceivableDO> getFinanceReceivablePage(ErpFinanceReceivablePageReqVO pageReqVO);

}