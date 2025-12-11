package cn.iocoder.yudao.module.erp.dal.mysql.stock-internal-in;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock-internal-in.ErpStockInternalInDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.stock-internal-in.vo.*;

/**
 * 内部入库单 Mapper
 *
 * @author 开发团队
 */
@Mapper
public interface ErpStockInternalInMapper extends BaseMapperX<ErpStockInternalInDO> {

    default PageResult<ErpStockInternalInDO> selectPage(ErpStockInternalInPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpStockInternalInDO>()
                .eqIfPresent(ErpStockInternalInDO::getNo, reqVO.getNo())
                .eqIfPresent(ErpStockInternalInDO::getEmployeeId, reqVO.getEmployeeId())
                .eqIfPresent(ErpStockInternalInDO::getDeptId, reqVO.getDeptId())
                .eqIfPresent(ErpStockInternalInDO::getInternalType, reqVO.getInternalType())
                .eqIfPresent(ErpStockInternalInDO::getInTime, reqVO.getInTime())
                .eqIfPresent(ErpStockInternalInDO::getTotalCount, reqVO.getTotalCount())
                .eqIfPresent(ErpStockInternalInDO::getTotalPrice, reqVO.getTotalPrice())
                .eqIfPresent(ErpStockInternalInDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ErpStockInternalInDO::getRemark, reqVO.getRemark())
                .eqIfPresent(ErpStockInternalInDO::getFileUrl, reqVO.getFileUrl())
                .orderByDesc(ErpStockInternalInDO::getId));
    }

}