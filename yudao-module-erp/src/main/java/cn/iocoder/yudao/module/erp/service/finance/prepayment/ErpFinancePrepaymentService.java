package cn.iocoder.yudao.module.erp.service.prepayment;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance.prepayment.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.prepayment.ErpFinancePrepaymentDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 预付款 Service 接口
 *
 * @author 开发团队
 */
public interface ErpFinancePrepaymentService {

    /**
     * 创建预付款
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFinancePrepayment(@Valid ErpFinancePrepaymentSaveReqVO createReqVO);

    /**
     * 更新预付款
     *
     * @param updateReqVO 更新信息
     */
    void updateFinancePrepayment(@Valid ErpFinancePrepaymentSaveReqVO updateReqVO);

    /**
     * 删除预付款
     *
     * @param id 编号
     */
    void deleteFinancePrepayment(Long id);

    /**
    * 批量删除预付款
    *
    * @param ids 编号
    */
    void deleteFinancePrepaymentListByIds(List<Long> ids);

    /**
     * 获得预付款
     *
     * @param id 编号
     * @return 预付款
     */
    ErpFinancePrepaymentDO getFinancePrepayment(Long id);

    /**
     * 获得预付款分页
     *
     * @param pageReqVO 分页查询
     * @return 预付款分页
     */
    PageResult<ErpFinancePrepaymentDO> getFinancePrepaymentPage(ErpFinancePrepaymentPageReqVO pageReqVO);

}