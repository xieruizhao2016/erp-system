package cn.iocoder.yudao.module.erp.service.equipment;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.equipment.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.equipment.EquipmentDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 设备台账 Service 接口
 *
 * @author 芋道源码
 */
public interface EquipmentService {

    /**
     * 创建ERP 设备台账
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createEquipment(@Valid EquipmentSaveReqVO createReqVO);

    /**
     * 更新ERP 设备台账
     *
     * @param updateReqVO 更新信息
     */
    void updateEquipment(@Valid EquipmentSaveReqVO updateReqVO);

    /**
     * 删除ERP 设备台账
     *
     * @param id 编号
     */
    void deleteEquipment(Long id);

    /**
    * 批量删除ERP 设备台账
    *
    * @param ids 编号
    */
    void deleteEquipmentListByIds(List<Long> ids);

    /**
     * 获得ERP 设备台账
     *
     * @param id 编号
     * @return ERP 设备台账
     */
    EquipmentDO getEquipment(Long id);

    /**
     * 获得ERP 设备台账分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 设备台账分页
     */
    PageResult<EquipmentDO> getEquipmentPage(EquipmentPageReqVO pageReqVO);

}