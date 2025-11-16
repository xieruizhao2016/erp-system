package cn.iocoder.yudao.module.erp.service.processrouteitem;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.processrouteitem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.processrouteitem.ProcessRouteItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 工艺路线明细 Service 接口
 *
 * @author 芋道源码
 */
public interface ProcessRouteItemService {

    /**
     * 创建ERP 工艺路线明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProcessRouteItem(@Valid ProcessRouteItemSaveReqVO createReqVO);

    /**
     * 更新ERP 工艺路线明细
     *
     * @param updateReqVO 更新信息
     */
    void updateProcessRouteItem(@Valid ProcessRouteItemSaveReqVO updateReqVO);

    /**
     * 删除ERP 工艺路线明细
     *
     * @param id 编号
     */
    void deleteProcessRouteItem(Long id);

    /**
    * 批量删除ERP 工艺路线明细
    *
    * @param ids 编号
    */
    void deleteProcessRouteItemListByIds(List<Long> ids);

    /**
     * 获得ERP 工艺路线明细
     *
     * @param id 编号
     * @return ERP 工艺路线明细
     */
    ProcessRouteItemDO getProcessRouteItem(Long id);

    /**
     * 获得ERP 工艺路线明细分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 工艺路线明细分页
     */
    PageResult<ProcessRouteItemDO> getProcessRouteItemPage(ProcessRouteItemPageReqVO pageReqVO);

}