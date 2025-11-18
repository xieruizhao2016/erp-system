package cn.iocoder.yudao.module.erp.service.productionschedule;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.productionschedule.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionschedule.ProductionScheduleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.productionschedule.ProductionScheduleMapper;
import cn.iocoder.yudao.module.erp.dal.redis.no.ErpNoRedisDAO;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 生产排程主 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductionScheduleServiceImpl implements ProductionScheduleService {

    @Resource
    private ProductionScheduleMapper productionScheduleMapper;

    @Resource
    private ErpNoRedisDAO noRedisDAO;

    @Override
    public Long createProductionSchedule(ProductionScheduleSaveReqVO createReqVO) {
        // 生成排程单号，并校验唯一性
        String scheduleNo = noRedisDAO.generate(ErpNoRedisDAO.PRODUCTION_SCHEDULE_NO_PREFIX);
        if (productionScheduleMapper.selectByScheduleNo(scheduleNo) != null) {
            throw exception(PRODUCTION_SCHEDULE_NO_EXISTS);
        }

        // 插入
        ProductionScheduleDO productionSchedule = BeanUtils.toBean(createReqVO, ProductionScheduleDO.class);
        productionSchedule.setScheduleNo(scheduleNo);
        productionScheduleMapper.insert(productionSchedule);

        // 返回
        return productionSchedule.getId();
    }

    @Override
    public void updateProductionSchedule(ProductionScheduleSaveReqVO updateReqVO) {
        // 校验存在
        validateProductionScheduleExists(updateReqVO.getId());
        // 更新
        ProductionScheduleDO updateObj = BeanUtils.toBean(updateReqVO, ProductionScheduleDO.class);
        productionScheduleMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductionSchedule(Long id) {
        // 校验存在
        validateProductionScheduleExists(id);
        // 删除
        productionScheduleMapper.deleteById(id);
    }

    @Override
        public void deleteProductionScheduleListByIds(List<Long> ids) {
        // 删除
        productionScheduleMapper.deleteByIds(ids);
        }


    private void validateProductionScheduleExists(Long id) {
        if (productionScheduleMapper.selectById(id) == null) {
            throw exception(PRODUCTION_SCHEDULE_NOT_EXISTS);
        }
    }

    @Override
    public ProductionScheduleDO getProductionSchedule(Long id) {
        return productionScheduleMapper.selectById(id);
    }

    @Override
    public PageResult<ProductionScheduleDO> getProductionSchedulePage(ProductionSchedulePageReqVO pageReqVO) {
        return productionScheduleMapper.selectPage(pageReqVO);
    }

}