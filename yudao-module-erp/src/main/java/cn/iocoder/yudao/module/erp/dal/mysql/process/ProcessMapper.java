package cn.iocoder.yudao.module.erp.dal.mysql.process;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.process.ProcessDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.process.vo.*;

/**
 * ERP 工序 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProcessMapper extends BaseMapperX<ProcessDO> {

    default PageResult<ProcessDO> selectPage(ProcessPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProcessDO>()
                .eqIfPresent(ProcessDO::getProcessNo, reqVO.getProcessNo())
                .likeIfPresent(ProcessDO::getProcessName, reqVO.getProcessName())
                .eqIfPresent(ProcessDO::getProcessType, reqVO.getProcessType())
                .likeIfPresent(ProcessDO::getCategory, reqVO.getCategory())
                .eqIfPresent(ProcessDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ProcessDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProcessDO::getId));
    }

    default ProcessDO selectByProcessNo(String processNo) {
        return selectOne(ProcessDO::getProcessNo, processNo);
    }

}



