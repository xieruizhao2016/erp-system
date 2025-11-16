package cn.iocoder.yudao.module.erp.service.costvariance;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.costvariance.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.costvariance.CostVarianceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.costvariance.CostVarianceMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 成本差异分析 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CostVarianceServiceImpl implements CostVarianceService {

    @Resource
    private CostVarianceMapper costVarianceMapper;

    @Override
    public Long createCostVariance(CostVarianceSaveReqVO createReqVO) {
        // 插入
        CostVarianceDO costVariance = BeanUtils.toBean(createReqVO, CostVarianceDO.class);
        costVarianceMapper.insert(costVariance);

        // 返回
        return costVariance.getId();
    }

    @Override
    public void updateCostVariance(CostVarianceSaveReqVO updateReqVO) {
        // 校验存在
        validateCostVarianceExists(updateReqVO.getId());
        // 更新
        CostVarianceDO updateObj = BeanUtils.toBean(updateReqVO, CostVarianceDO.class);
        costVarianceMapper.updateById(updateObj);
    }

    @Override
    public void deleteCostVariance(Long id) {
        // 校验存在
        validateCostVarianceExists(id);
        // 删除
        costVarianceMapper.deleteById(id);
    }

    @Override
        public void deleteCostVarianceListByIds(List<Long> ids) {
        // 删除
        costVarianceMapper.deleteByIds(ids);
        }


    private void validateCostVarianceExists(Long id) {
        if (costVarianceMapper.selectById(id) == null) {
            throw exception(COST_VARIANCE_NOT_EXISTS);
        }
    }

    @Override
    public CostVarianceDO getCostVariance(Long id) {
        return costVarianceMapper.selectById(id);
    }

    @Override
    public PageResult<CostVarianceDO> getCostVariancePage(CostVariancePageReqVO pageReqVO) {
        return costVarianceMapper.selectPage(pageReqVO);
    }

}