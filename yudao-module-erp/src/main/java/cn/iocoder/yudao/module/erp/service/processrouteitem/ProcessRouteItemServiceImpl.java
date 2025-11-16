package cn.iocoder.yudao.module.erp.service.processrouteitem;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.processrouteitem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.processrouteitem.ProcessRouteItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.processrouteitem.ProcessRouteItemMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 工艺路线明细 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProcessRouteItemServiceImpl implements ProcessRouteItemService {

    @Resource
    private ProcessRouteItemMapper processRouteItemMapper;

    @Override
    public Long createProcessRouteItem(ProcessRouteItemSaveReqVO createReqVO) {
        // 插入
        ProcessRouteItemDO processRouteItem = BeanUtils.toBean(createReqVO, ProcessRouteItemDO.class);
        processRouteItemMapper.insert(processRouteItem);

        // 返回
        return processRouteItem.getId();
    }

    @Override
    public void updateProcessRouteItem(ProcessRouteItemSaveReqVO updateReqVO) {
        // 校验存在
        validateProcessRouteItemExists(updateReqVO.getId());
        // 更新
        ProcessRouteItemDO updateObj = BeanUtils.toBean(updateReqVO, ProcessRouteItemDO.class);
        processRouteItemMapper.updateById(updateObj);
    }

    @Override
    public void deleteProcessRouteItem(Long id) {
        // 校验存在
        validateProcessRouteItemExists(id);
        // 删除
        processRouteItemMapper.deleteById(id);
    }

    @Override
        public void deleteProcessRouteItemListByIds(List<Long> ids) {
        // 删除
        processRouteItemMapper.deleteByIds(ids);
        }


    private void validateProcessRouteItemExists(Long id) {
        if (processRouteItemMapper.selectById(id) == null) {
            throw exception(PROCESS_ROUTE_ITEM_NOT_EXISTS);
        }
    }

    @Override
    public ProcessRouteItemDO getProcessRouteItem(Long id) {
        return processRouteItemMapper.selectById(id);
    }

    @Override
    public PageResult<ProcessRouteItemDO> getProcessRouteItemPage(ProcessRouteItemPageReqVO pageReqVO) {
        return processRouteItemMapper.selectPage(pageReqVO);
    }

}