package cn.iocoder.yudao.module.erp.service.stock-internal-out;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.stock-internal-out.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock-internal-out.ErpStockInternalOutDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.stock-internal-out.ErpStockInternalOutMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * 内部出库单 Service 实现类
 *
 * @author 开发团队
 */
@Service
@Validated
public class ErpStockInternalOutServiceImpl implements ErpStockInternalOutService {

    @Resource
    private ErpStockInternalOutMapper stockInternalOutMapper;

    @Override
    public ${primaryColumn.javaType} createStockInternalOut(ErpStockInternalOutSaveReqVO createReqVO) {
        // 插入
        ErpStockInternalOutDO stockInternalOut = BeanUtils.toBean(createReqVO, ErpStockInternalOutDO.class);
        stockInternalOutMapper.insert(stockInternalOut);

        // 返回
        return stockInternalOut.getId();
    }

    @Override
    public void updateStockInternalOut(ErpStockInternalOutSaveReqVO updateReqVO) {
        // 校验存在
        validateStockInternalOutExists(updateReqVO.getId());
        // 更新
        ErpStockInternalOutDO updateObj = BeanUtils.toBean(updateReqVO, ErpStockInternalOutDO.class);
        stockInternalOutMapper.updateById(updateObj);
    }

    @Override
    public void deleteStockInternalOut(${primaryColumn.javaType} id) {
        // 校验存在
        validateStockInternalOutExists(id);
        // 删除
        stockInternalOutMapper.deleteById(id);
    }

    @Override
        public void deleteStockInternalOutListByIds(List<${primaryColumn.javaType}> ids) {
        // 删除
        stockInternalOutMapper.deleteByIds(ids);
        }


    private void validateStockInternalOutExists(${primaryColumn.javaType} id) {
        if (stockInternalOutMapper.selectById(id) == null) {
            throw exception(STOCK_INTERNAL_OUT_NOT_EXISTS);
        }
    }

    @Override
    public ErpStockInternalOutDO getStockInternalOut(${primaryColumn.javaType} id) {
        return stockInternalOutMapper.selectById(id);
    }

    @Override
    public PageResult<ErpStockInternalOutDO> getStockInternalOutPage(ErpStockInternalOutPageReqVO pageReqVO) {
        return stockInternalOutMapper.selectPage(pageReqVO);
    }

}