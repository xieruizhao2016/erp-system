package cn.iocoder.yudao.module.erp.service.workorderprogress;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.workorderprogress.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.workorderprogress.WorkOrderProgressDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 工单进度 Service 接口
 *
 * @author 芋道源码
 */
public interface WorkOrderProgressService {

    /**
     * 创建ERP 工单进度
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWorkOrderProgress(@Valid WorkOrderProgressSaveReqVO createReqVO);

    /**
     * 更新ERP 工单进度
     *
     * @param updateReqVO 更新信息
     */
    void updateWorkOrderProgress(@Valid WorkOrderProgressSaveReqVO updateReqVO);

    /**
     * 删除ERP 工单进度
     *
     * @param id 编号
     */
    void deleteWorkOrderProgress(Long id);

    /**
    * 批量删除ERP 工单进度
    *
    * @param ids 编号
    */
    void deleteWorkOrderProgressListByIds(List<Long> ids);

    /**
     * 获得ERP 工单进度
     *
     * @param id 编号
     * @return ERP 工单进度
     */
    WorkOrderProgressDO getWorkOrderProgress(Long id);

    /**
     * 获得ERP 工单进度分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 工单进度分页
     */
    PageResult<WorkOrderProgressDO> getWorkOrderProgressPage(WorkOrderProgressPageReqVO pageReqVO);

}