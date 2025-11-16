package cn.iocoder.yudao.module.erp.service.costactual;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.costactual.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.costactual.CostActualDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.costactual.CostActualMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 实际成本 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CostActualServiceImpl implements CostActualService {

    @Resource
    private CostActualMapper costActualMapper;

    @Override
    public Long createCostActual(CostActualSaveReqVO createReqVO) {
        // 插入
        CostActualDO costActual = BeanUtils.toBean(createReqVO, CostActualDO.class);
        costActualMapper.insert(costActual);

        // 返回
        return costActual.getId();
    }

    @Override
    public void updateCostActual(CostActualSaveReqVO updateReqVO) {
        // 校验存在
        validateCostActualExists(updateReqVO.getId());
        // 更新
        CostActualDO updateObj = BeanUtils.toBean(updateReqVO, CostActualDO.class);
        costActualMapper.updateById(updateObj);
    }

    @Override
    public void deleteCostActual(Long id) {
        // 校验存在
        validateCostActualExists(id);
        // 删除
        costActualMapper.deleteById(id);
    }

    @Override
        public void deleteCostActualListByIds(List<Long> ids) {
        // 删除
        costActualMapper.deleteByIds(ids);
        }


    private void validateCostActualExists(Long id) {
        if (costActualMapper.selectById(id) == null) {
            throw exception(COST_ACTUAL_NOT_EXISTS);
        }
    }

    @Override
    public CostActualDO getCostActual(Long id) {
        return costActualMapper.selectById(id);
    }

    @Override
    public PageResult<CostActualDO> getCostActualPage(CostActualPageReqVO pageReqVO) {
        return costActualMapper.selectPage(pageReqVO);
    }

}