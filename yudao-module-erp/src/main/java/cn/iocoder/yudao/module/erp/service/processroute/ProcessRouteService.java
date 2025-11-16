package cn.iocoder.yudao.module.erp.service.processroute;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.processroute.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.processroute.ProcessRouteDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 工艺路线主 Service 接口
 *
 * @author 芋道源码
 */
public interface ProcessRouteService {

    /**
     * 创建ERP 工艺路线主
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProcessRoute(@Valid ProcessRouteSaveReqVO createReqVO);

    /**
     * 更新ERP 工艺路线主
     *
     * @param updateReqVO 更新信息
     */
    void updateProcessRoute(@Valid ProcessRouteSaveReqVO updateReqVO);

    /**
     * 删除ERP 工艺路线主
     *
     * @param id 编号
     */
    void deleteProcessRoute(Long id);

    /**
    * 批量删除ERP 工艺路线主
    *
    * @param ids 编号
    */
    void deleteProcessRouteListByIds(List<Long> ids);

    /**
     * 获得ERP 工艺路线主
     *
     * @param id 编号
     * @return ERP 工艺路线主
     */
    ProcessRouteDO getProcessRoute(Long id);

    /**
     * 获得ERP 工艺路线主分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 工艺路线主分页
     */
    PageResult<ProcessRouteDO> getProcessRoutePage(ProcessRoutePageReqVO pageReqVO);

}