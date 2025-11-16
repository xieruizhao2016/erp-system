package cn.iocoder.yudao.module.erp.dal.mysql.mrpresult;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.mrpresult.MrpResultDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.mrpresult.vo.*;

/**
 * ERP MRP运算结果 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MrpResultMapper extends BaseMapperX<MrpResultDO> {

    default PageResult<MrpResultDO> selectPage(MrpResultPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MrpResultDO>()
                .eqIfPresent(MrpResultDO::getRunNo, reqVO.getRunNo())
                .eqIfPresent(MrpResultDO::getProductId, reqVO.getProductId())
                .eqIfPresent(MrpResultDO::getWarehouseId, reqVO.getWarehouseId())
                .betweenIfPresent(MrpResultDO::getPeriodStartDate, reqVO.getPeriodStartDate())
                .betweenIfPresent(MrpResultDO::getPeriodEndDate, reqVO.getPeriodEndDate())
                .eqIfPresent(MrpResultDO::getGrossRequirement, reqVO.getGrossRequirement())
                .eqIfPresent(MrpResultDO::getScheduledReceipts, reqVO.getScheduledReceipts())
                .eqIfPresent(MrpResultDO::getOnHandInventory, reqVO.getOnHandInventory())
                .eqIfPresent(MrpResultDO::getNetRequirement, reqVO.getNetRequirement())
                .eqIfPresent(MrpResultDO::getPlannedOrderReceipts, reqVO.getPlannedOrderReceipts())
                .eqIfPresent(MrpResultDO::getPlannedOrderReleases, reqVO.getPlannedOrderReleases())
                .eqIfPresent(MrpResultDO::getOrderType, reqVO.getOrderType())
                .eqIfPresent(MrpResultDO::getLotSizingRule, reqVO.getLotSizingRule())
                .betweenIfPresent(MrpResultDO::getLeadTime, reqVO.getLeadTime())
                .eqIfPresent(MrpResultDO::getSafetyStock, reqVO.getSafetyStock())
                .eqIfPresent(MrpResultDO::getOrderStatus, reqVO.getOrderStatus())
                .betweenIfPresent(MrpResultDO::getDueDate, reqVO.getDueDate())
                .betweenIfPresent(MrpResultDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MrpResultDO::getId));
    }

}