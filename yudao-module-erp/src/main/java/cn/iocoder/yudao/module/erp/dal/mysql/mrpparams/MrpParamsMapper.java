package cn.iocoder.yudao.module.erp.dal.mysql.mrpparams;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.mrpparams.MrpParamsDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.mrpparams.vo.*;

/**
 * ERP MRP参数 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MrpParamsMapper extends BaseMapperX<MrpParamsDO> {

    default PageResult<MrpParamsDO> selectPage(MrpParamsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MrpParamsDO>()
                .likeIfPresent(MrpParamsDO::getParamName, reqVO.getParamName())
                .eqIfPresent(MrpParamsDO::getParamCode, reqVO.getParamCode())
                .eqIfPresent(MrpParamsDO::getParamValue, reqVO.getParamValue())
                .eqIfPresent(MrpParamsDO::getParamType, reqVO.getParamType())
                .eqIfPresent(MrpParamsDO::getDescription, reqVO.getDescription())
                .eqIfPresent(MrpParamsDO::getIsSystem, reqVO.getIsSystem())
                .betweenIfPresent(MrpParamsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MrpParamsDO::getId));
    }

    default MrpParamsDO selectByParamCode(String paramCode) {
        return selectOne(MrpParamsDO::getParamCode, paramCode);
    }

}