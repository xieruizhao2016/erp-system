package cn.iocoder.yudao.module.erp.dal.mysql.processroute;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.processroute.ProcessRouteDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.processroute.vo.*;

/**
 * ERP 工艺路线主 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProcessRouteMapper extends BaseMapperX<ProcessRouteDO> {

    default PageResult<ProcessRouteDO> selectPage(ProcessRoutePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProcessRouteDO>()
                .eqIfPresent(ProcessRouteDO::getRouteNo, reqVO.getRouteNo())
                .likeIfPresent(ProcessRouteDO::getRouteName, reqVO.getRouteName())
                .eqIfPresent(ProcessRouteDO::getProductId, reqVO.getProductId())
                .eqIfPresent(ProcessRouteDO::getVersion, reqVO.getVersion())
                .betweenIfPresent(ProcessRouteDO::getStandardCycleTime, reqVO.getStandardCycleTime())
                .eqIfPresent(ProcessRouteDO::getStandardLaborCost, reqVO.getStandardLaborCost())
                .eqIfPresent(ProcessRouteDO::getStandardOverheadCost, reqVO.getStandardOverheadCost())
                .eqIfPresent(ProcessRouteDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ProcessRouteDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProcessRouteDO::getId));
    }

    default ProcessRouteDO selectByRouteNo(String routeNo) {
        return selectOne(ProcessRouteDO::getRouteNo, routeNo);
    }

    /**
     * 根据产品ID查询生效的工艺路线（状态为2-生效）
     *
     * @param productId 产品ID
     * @return 工艺路线
     */
    default ProcessRouteDO selectActiveByProductId(Long productId) {
        return selectOne(new LambdaQueryWrapperX<ProcessRouteDO>()
                .eq(ProcessRouteDO::getProductId, productId)
                .eq(ProcessRouteDO::getStatus, 2) // 2-生效
                .orderByDesc(ProcessRouteDO::getId)
                .last("LIMIT 1"));
    }

}