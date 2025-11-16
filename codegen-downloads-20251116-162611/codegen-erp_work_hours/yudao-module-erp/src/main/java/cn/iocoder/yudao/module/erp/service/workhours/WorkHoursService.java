package cn.iocoder.yudao.module.erp.service.workhours;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.workhours.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.workhours.WorkHoursDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 工时统计 Service 接口
 *
 * @author 芋道源码
 */
public interface WorkHoursService {

    /**
     * 创建ERP 工时统计
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWorkHours(@Valid WorkHoursSaveReqVO createReqVO);

    /**
     * 更新ERP 工时统计
     *
     * @param updateReqVO 更新信息
     */
    void updateWorkHours(@Valid WorkHoursSaveReqVO updateReqVO);

    /**
     * 删除ERP 工时统计
     *
     * @param id 编号
     */
    void deleteWorkHours(Long id);

    /**
    * 批量删除ERP 工时统计
    *
    * @param ids 编号
    */
    void deleteWorkHoursListByIds(List<Long> ids);

    /**
     * 获得ERP 工时统计
     *
     * @param id 编号
     * @return ERP 工时统计
     */
    WorkHoursDO getWorkHours(Long id);

    /**
     * 获得ERP 工时统计分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 工时统计分页
     */
    PageResult<WorkHoursDO> getWorkHoursPage(WorkHoursPageReqVO pageReqVO);

}