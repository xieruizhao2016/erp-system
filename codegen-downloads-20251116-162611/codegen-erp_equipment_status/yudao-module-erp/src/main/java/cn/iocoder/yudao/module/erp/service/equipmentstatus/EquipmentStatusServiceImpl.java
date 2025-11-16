package cn.iocoder.yudao.module.erp.service.equipmentstatus;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.equipmentstatus.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.equipmentstatus.EquipmentStatusDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.equipmentstatus.EquipmentStatusMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 设备状态记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class EquipmentStatusServiceImpl implements EquipmentStatusService {

    @Resource
    private EquipmentStatusMapper equipmentStatusMapper;

    @Override
    public Long createEquipmentStatus(EquipmentStatusSaveReqVO createReqVO) {
        // 插入
        EquipmentStatusDO equipmentStatus = BeanUtils.toBean(createReqVO, EquipmentStatusDO.class);
        equipmentStatusMapper.insert(equipmentStatus);

        // 返回
        return equipmentStatus.getId();
    }

    @Override
    public void updateEquipmentStatus(EquipmentStatusSaveReqVO updateReqVO) {
        // 校验存在
        validateEquipmentStatusExists(updateReqVO.getId());
        // 更新
        EquipmentStatusDO updateObj = BeanUtils.toBean(updateReqVO, EquipmentStatusDO.class);
        equipmentStatusMapper.updateById(updateObj);
    }

    @Override
    public void deleteEquipmentStatus(Long id) {
        // 校验存在
        validateEquipmentStatusExists(id);
        // 删除
        equipmentStatusMapper.deleteById(id);
    }

    @Override
        public void deleteEquipmentStatusListByIds(List<Long> ids) {
        // 删除
        equipmentStatusMapper.deleteByIds(ids);
        }


    private void validateEquipmentStatusExists(Long id) {
        if (equipmentStatusMapper.selectById(id) == null) {
            throw exception(EQUIPMENT_STATUS_NOT_EXISTS);
        }
    }

    @Override
    public EquipmentStatusDO getEquipmentStatus(Long id) {
        return equipmentStatusMapper.selectById(id);
    }

    @Override
    public PageResult<EquipmentStatusDO> getEquipmentStatusPage(EquipmentStatusPageReqVO pageReqVO) {
        return equipmentStatusMapper.selectPage(pageReqVO);
    }

}