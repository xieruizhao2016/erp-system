package cn.iocoder.yudao.module.erp.dal.mysql.productiondashboardconfig;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.productiondashboardconfig.ProductionDashboardConfigDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.productiondashboardconfig.vo.*;

/**
 * ERP 看板配置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductionDashboardConfigMapper extends BaseMapperX<ProductionDashboardConfigDO> {

    default PageResult<ProductionDashboardConfigDO> selectPage(ProductionDashboardConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductionDashboardConfigDO>()
                .likeIfPresent(ProductionDashboardConfigDO::getConfigName, reqVO.getConfigName())
                .eqIfPresent(ProductionDashboardConfigDO::getConfigType, reqVO.getConfigType())
                .eqIfPresent(ProductionDashboardConfigDO::getScreenResolution, reqVO.getScreenResolution())
                .eqIfPresent(ProductionDashboardConfigDO::getLayoutConfig, reqVO.getLayoutConfig())
                .eqIfPresent(ProductionDashboardConfigDO::getComponentConfig, reqVO.getComponentConfig())
                .eqIfPresent(ProductionDashboardConfigDO::getDataRefreshInterval, reqVO.getDataRefreshInterval())
                .eqIfPresent(ProductionDashboardConfigDO::getIsDefault, reqVO.getIsDefault())
                .eqIfPresent(ProductionDashboardConfigDO::getIsActive, reqVO.getIsActive())
                .betweenIfPresent(ProductionDashboardConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductionDashboardConfigDO::getId));
    }

}