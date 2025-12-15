package cn.iocoder.yudao.module.erp.service.finance.prereceipt;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance.prereceipt.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.prereceipt.ErpFinancePrereceiptDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 预收款 Service 接口
 *
 * @author 开发团队
 */
public interface ErpFinancePrereceiptService {

    /**
     * 创建预收款
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFinancePrereceipt(@Valid ErpFinancePrereceiptSaveReqVO createReqVO);

    /**
     * 更新预收款
     *
     * @param updateReqVO 更新信息
     */
    void updateFinancePrereceipt(@Valid ErpFinancePrereceiptSaveReqVO updateReqVO);

    /**
     * 删除预收款
     *
     * @param id 编号
     */
    void deleteFinancePrereceipt(Long id);

    /**
    * 批量删除预收款
    *
    * @param ids 编号
    */
    void deleteFinancePrereceiptListByIds(List<Long> ids);

    /**
     * 获得预收款
     *
     * @param id 编号
     * @return 预收款
     */
    ErpFinancePrereceiptDO getFinancePrereceipt(Long id);

    /**
     * 获得预收款分页
     *
     * @param pageReqVO 分页查询
     * @return 预收款分页
     */
    PageResult<ErpFinancePrereceiptDO> getFinancePrereceiptPage(ErpFinancePrereceiptPageReqVO pageReqVO);

}