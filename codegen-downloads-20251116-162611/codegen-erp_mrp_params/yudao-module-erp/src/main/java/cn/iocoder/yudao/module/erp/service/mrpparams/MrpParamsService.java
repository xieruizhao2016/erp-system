package cn.iocoder.yudao.module.erp.service.mrpparams;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.mrpparams.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.mrpparams.MrpParamsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP MRP参数 Service 接口
 *
 * @author 芋道源码
 */
public interface MrpParamsService {

    /**
     * 创建ERP MRP参数
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMrpParams(@Valid MrpParamsSaveReqVO createReqVO);

    /**
     * 更新ERP MRP参数
     *
     * @param updateReqVO 更新信息
     */
    void updateMrpParams(@Valid MrpParamsSaveReqVO updateReqVO);

    /**
     * 删除ERP MRP参数
     *
     * @param id 编号
     */
    void deleteMrpParams(Long id);

    /**
    * 批量删除ERP MRP参数
    *
    * @param ids 编号
    */
    void deleteMrpParamsListByIds(List<Long> ids);

    /**
     * 获得ERP MRP参数
     *
     * @param id 编号
     * @return ERP MRP参数
     */
    MrpParamsDO getMrpParams(Long id);

    /**
     * 获得ERP MRP参数分页
     *
     * @param pageReqVO 分页查询
     * @return ERP MRP参数分页
     */
    PageResult<MrpParamsDO> getMrpParamsPage(MrpParamsPageReqVO pageReqVO);

}