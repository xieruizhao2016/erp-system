package cn.iocoder.yudao.module.erp.service.workcenter;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.workcenter.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.workcenter.WorkCenterDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 工作中心 Service 接口
 *
 * @author 芋道源码
 */
public interface WorkCenterService {

    /**
     * 创建ERP 工作中心
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWorkCenter(@Valid WorkCenterSaveReqVO createReqVO);

    /**
     * 更新ERP 工作中心
     *
     * @param updateReqVO 更新信息
     */
    void updateWorkCenter(@Valid WorkCenterSaveReqVO updateReqVO);

    /**
     * 删除ERP 工作中心
     *
     * @param id 编号
     */
    void deleteWorkCenter(Long id);

    /**
     * 批量删除ERP 工作中心
     *
     * @param ids 编号
     */
    void deleteWorkCenterListByIds(List<Long> ids);

    /**
     * 获得ERP 工作中心
     *
     * @param id 编号
     * @return ERP 工作中心
     */
    WorkCenterDO getWorkCenter(Long id);

    /**
     * 获得ERP 工作中心分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 工作中心分页
     */
    PageResult<WorkCenterDO> getWorkCenterPage(WorkCenterPageReqVO pageReqVO);

    /**
     * 获得ERP 工作中心列表（简单列表，用于下拉选择）
     *
     * @return ERP 工作中心列表
     */
    List<WorkCenterDO> getWorkCenterList();

    /**
     * 生成工作中心编号
     *
     * @return 工作中心编号
     */
    String generateWorkCenterNo();

}

