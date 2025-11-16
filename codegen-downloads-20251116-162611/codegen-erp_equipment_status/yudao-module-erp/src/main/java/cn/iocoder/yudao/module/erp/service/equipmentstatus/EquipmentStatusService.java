package cn.iocoder.yudao.module.erp.service.equipmentstatus;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.equipmentstatus.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.equipmentstatus.EquipmentStatusDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 设备状态记录 Service 接口
 *
 * @author 芋道源码
 */
public interface EquipmentStatusService {

    /**
     * 创建ERP 设备状态记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createEquipmentStatus(@Valid EquipmentStatusSaveReqVO createReqVO);

    /**
     * 更新ERP 设备状态记录
     *
     * @param updateReqVO 更新信息
     */
    void updateEquipmentStatus(@Valid EquipmentStatusSaveReqVO updateReqVO);

    /**
     * 删除ERP 设备状态记录
     *
     * @param id 编号
     */
    void deleteEquipmentStatus(Long id);

    /**
    * 批量删除ERP 设备状态记录
    *
    * @param ids 编号
    */
    void deleteEquipmentStatusListByIds(List<Long> ids);

    /**
     * 获得ERP 设备状态记录
     *
     * @param id 编号
     * @return ERP 设备状态记录
     */
    EquipmentStatusDO getEquipmentStatus(Long id);

    /**
     * 获得ERP 设备状态记录分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 设备状态记录分页
     */
    PageResult<EquipmentStatusDO> getEquipmentStatusPage(EquipmentStatusPageReqVO pageReqVO);

}