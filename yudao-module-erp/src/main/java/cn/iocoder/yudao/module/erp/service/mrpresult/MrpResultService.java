package cn.iocoder.yudao.module.erp.service.mrpresult;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.mrpresult.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.mrpresult.MrpResultDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP MRP运算结果 Service 接口
 *
 * @author 芋道源码
 */
public interface MrpResultService {

    /**
     * 创建ERP MRP运算结果
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMrpResult(@Valid MrpResultSaveReqVO createReqVO);

    /**
     * 更新ERP MRP运算结果
     *
     * @param updateReqVO 更新信息
     */
    void updateMrpResult(@Valid MrpResultSaveReqVO updateReqVO);

    /**
     * 删除ERP MRP运算结果
     *
     * @param id 编号
     */
    void deleteMrpResult(Long id);

    /**
    * 批量删除ERP MRP运算结果
    *
    * @param ids 编号
    */
    void deleteMrpResultListByIds(List<Long> ids);

    /**
     * 获得ERP MRP运算结果
     *
     * @param id 编号
     * @return ERP MRP运算结果
     */
    MrpResultDO getMrpResult(Long id);

    /**
     * 获得ERP MRP运算结果分页
     *
     * @param pageReqVO 分页查询
     * @return ERP MRP运算结果分页
     */
    PageResult<MrpResultDO> getMrpResultPage(MrpResultPageReqVO pageReqVO);

    /**
     * 执行MRP运算
     *
     * @param calculationReqVO MRP运算请求参数
     * @return MRP运算结果
     */
    MrpCalculationResultVO executeMrpCalculation(@Valid MrpCalculationReqVO calculationReqVO);

}