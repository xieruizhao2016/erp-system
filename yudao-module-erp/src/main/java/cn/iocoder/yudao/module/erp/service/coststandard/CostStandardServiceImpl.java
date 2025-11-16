package cn.iocoder.yudao.module.erp.service.coststandard;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.coststandard.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.coststandard.CostStandardDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.coststandard.CostStandardMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 标准成本 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CostStandardServiceImpl implements CostStandardService {

    @Resource
    private CostStandardMapper costStandardMapper;

    @Override
    public Long createCostStandard(CostStandardSaveReqVO createReqVO) {
        // 插入
        CostStandardDO costStandard = BeanUtils.toBean(createReqVO, CostStandardDO.class);
        costStandardMapper.insert(costStandard);

        // 返回
        return costStandard.getId();
    }

    @Override
    public void updateCostStandard(CostStandardSaveReqVO updateReqVO) {
        // 校验存在
        validateCostStandardExists(updateReqVO.getId());
        // 更新
        CostStandardDO updateObj = BeanUtils.toBean(updateReqVO, CostStandardDO.class);
        costStandardMapper.updateById(updateObj);
    }

    @Override
    public void deleteCostStandard(Long id) {
        // 校验存在
        validateCostStandardExists(id);
        // 删除
        costStandardMapper.deleteById(id);
    }

    @Override
        public void deleteCostStandardListByIds(List<Long> ids) {
        // 删除
        costStandardMapper.deleteByIds(ids);
        }


    private void validateCostStandardExists(Long id) {
        if (costStandardMapper.selectById(id) == null) {
            throw exception(COST_STANDARD_NOT_EXISTS);
        }
    }

    @Override
    public CostStandardDO getCostStandard(Long id) {
        return costStandardMapper.selectById(id);
    }

    @Override
    public PageResult<CostStandardDO> getCostStandardPage(CostStandardPageReqVO pageReqVO) {
        return costStandardMapper.selectPage(pageReqVO);
    }

}