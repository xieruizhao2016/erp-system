package cn.iocoder.yudao.module.erp.service.stock-internal-in;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.stock-internal-in.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock-internal-in.ErpStockInternalInDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.stock-internal-in.ErpStockInternalInMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * 内部入库单 Service 实现类
 *
 * @author 开发团队
 */
@Service
@Validated
public class ErpStockInternalInServiceImpl implements ErpStockInternalInService {

    @Resource
    private ErpStockInternalInMapper stockInternalInMapper;

    @Override
    public ${primaryColumn.javaType} createStockInternalIn(ErpStockInternalInSaveReqVO createReqVO) {
        // 插入
        ErpStockInternalInDO stockInternalIn = BeanUtils.toBean(createReqVO, ErpStockInternalInDO.class);
        stockInternalInMapper.insert(stockInternalIn);

        // 返回
        return stockInternalIn.getId();
    }

    @Override
    public void updateStockInternalIn(ErpStockInternalInSaveReqVO updateReqVO) {
        // 校验存在
        validateStockInternalInExists(updateReqVO.getId());
        // 更新
        ErpStockInternalInDO updateObj = BeanUtils.toBean(updateReqVO, ErpStockInternalInDO.class);
        stockInternalInMapper.updateById(updateObj);
    }

    @Override
    public void deleteStockInternalIn(${primaryColumn.javaType} id) {
        // 校验存在
        validateStockInternalInExists(id);
        // 删除
        stockInternalInMapper.deleteById(id);
    }

    @Override
        public void deleteStockInternalInListByIds(List<${primaryColumn.javaType}> ids) {
        // 删除
        stockInternalInMapper.deleteByIds(ids);
        }


    private void validateStockInternalInExists(${primaryColumn.javaType} id) {
        if (stockInternalInMapper.selectById(id) == null) {
            throw exception(STOCK_INTERNAL_IN_NOT_EXISTS);
        }
    }

    @Override
    public ErpStockInternalInDO getStockInternalIn(${primaryColumn.javaType} id) {
        return stockInternalInMapper.selectById(id);
    }

    @Override
    public PageResult<ErpStockInternalInDO> getStockInternalInPage(ErpStockInternalInPageReqVO pageReqVO) {
        return stockInternalInMapper.selectPage(pageReqVO);
    }

}