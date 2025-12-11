package cn.iocoder.yudao.module.erp.dal.mysql.stock.internalout;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.internalout.ErpStockInternalOutDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.stock.internalout.vo.*;

/**
 * 内部出库单 Mapper
 *
 * @author 开发团队
 */
@Mapper
public interface ErpStockInternalOutMapper extends BaseMapperX<ErpStockInternalOutDO> {

    default PageResult<ErpStockInternalOutDO> selectPage(ErpStockInternalOutPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpStockInternalOutDO>()
                .eqIfPresent(ErpStockInternalOutDO::getNo, reqVO.getNo())
                .eqIfPresent(ErpStockInternalOutDO::getEmployeeId, reqVO.getEmployeeId())
                .eqIfPresent(ErpStockInternalOutDO::getDeptId, reqVO.getDeptId())
                .eqIfPresent(ErpStockInternalOutDO::getInternalType, reqVO.getInternalType())
                .eqIfPresent(ErpStockInternalOutDO::getOutTime, reqVO.getOutTime())
                .eqIfPresent(ErpStockInternalOutDO::getTotalCount, reqVO.getTotalCount())
                .eqIfPresent(ErpStockInternalOutDO::getTotalPrice, reqVO.getTotalPrice())
                .eqIfPresent(ErpStockInternalOutDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ErpStockInternalOutDO::getRemark, reqVO.getRemark())
                .eqIfPresent(ErpStockInternalOutDO::getFileUrl, reqVO.getFileUrl())
                .orderByDesc(ErpStockInternalOutDO::getId));
    }

    default ErpStockInternalOutDO selectByNo(String no) {
        return selectOne(ErpStockInternalOutDO::getNo, no);
    }

    default int updateByIdAndStatus(Long id, Integer status, ErpStockInternalOutDO updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<ErpStockInternalOutDO>()
                .eq(ErpStockInternalOutDO::getId, id).eq(ErpStockInternalOutDO::getStatus, status));
    }

}