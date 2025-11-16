package cn.iocoder.yudao.module.erp.service.processroute;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.processroute.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.processroute.ProcessRouteDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.processroute.ProcessRouteMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 工艺路线主 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProcessRouteServiceImpl implements ProcessRouteService {

    @Resource
    private ProcessRouteMapper processRouteMapper;

    @Override
    public Long createProcessRoute(ProcessRouteSaveReqVO createReqVO) {
        // 插入
        ProcessRouteDO processRoute = BeanUtils.toBean(createReqVO, ProcessRouteDO.class);
        processRouteMapper.insert(processRoute);

        // 返回
        return processRoute.getId();
    }

    @Override
    public void updateProcessRoute(ProcessRouteSaveReqVO updateReqVO) {
        // 校验存在
        validateProcessRouteExists(updateReqVO.getId());
        // 更新
        ProcessRouteDO updateObj = BeanUtils.toBean(updateReqVO, ProcessRouteDO.class);
        processRouteMapper.updateById(updateObj);
    }

    @Override
    public void deleteProcessRoute(Long id) {
        // 校验存在
        validateProcessRouteExists(id);
        // 删除
        processRouteMapper.deleteById(id);
    }

    @Override
        public void deleteProcessRouteListByIds(List<Long> ids) {
        // 删除
        processRouteMapper.deleteByIds(ids);
        }


    private void validateProcessRouteExists(Long id) {
        if (processRouteMapper.selectById(id) == null) {
            throw exception(PROCESS_ROUTE_NOT_EXISTS);
        }
    }

    @Override
    public ProcessRouteDO getProcessRoute(Long id) {
        return processRouteMapper.selectById(id);
    }

    @Override
    public PageResult<ProcessRouteDO> getProcessRoutePage(ProcessRoutePageReqVO pageReqVO) {
        return processRouteMapper.selectPage(pageReqVO);
    }

}