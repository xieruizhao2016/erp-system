package cn.iocoder.yudao.module.erp.service.stock.internalout;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.stock.internalout.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.internalout.ErpStockInternalOutDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.internalout.ErpStockInternalOutItemDO;
import cn.iocoder.yudao.module.erp.dal.mysql.stock.internalout.ErpStockInternalOutMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.stock.internalout.ErpStockInternalOutItemMapper;
import cn.iocoder.yudao.module.erp.dal.redis.no.ErpNoRedisDAO;
import cn.iocoder.yudao.module.erp.enums.ErpAuditStatus;
import cn.iocoder.yudao.module.erp.enums.stock.ErpStockRecordBizTypeEnum;
import cn.iocoder.yudao.module.erp.service.product.ErpProductService;
import cn.iocoder.yudao.module.erp.service.stock.ErpStockRecordService;
import cn.iocoder.yudao.module.erp.service.stock.ErpWarehouseService;
import cn.iocoder.yudao.module.erp.service.stock.bo.ErpStockRecordCreateReqBO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.number.MoneyUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
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
    @Resource
    private ErpStockInternalOutItemMapper stockInternalOutItemMapper;

    @Resource
    private ErpNoRedisDAO noRedisDAO;

    @Resource
    private ErpProductService productService;
    @Resource
    private ErpWarehouseService warehouseService;
    @Resource
    private ErpStockRecordService stockRecordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createStockInternalOut(ErpStockInternalOutSaveReqVO createReqVO) {
        // 1.1 校验出库项的有效性（如果有明细项）
        List<ErpStockInternalOutItemDO> stockInternalOutItems = CollUtil.isNotEmpty(createReqVO.getItems())
                ? validateStockInternalOutItems(createReqVO.getItems())
                : Collections.emptyList();
        // 1.2 生成出库单号
        String no = noRedisDAO.generate(ErpNoRedisDAO.STOCK_INTERNAL_OUT_NO_PREFIX);
        if (stockInternalOutMapper.selectByNo(no) != null) {
            throw exception(STOCK_INTERNAL_OUT_NOT_EXISTS); // TODO: 需要添加对应的错误码
        }

        // 2.1 插入出库单
        ErpStockInternalOutDO stockInternalOut = BeanUtils.toBean(createReqVO, ErpStockInternalOutDO.class, out -> out
                .setNo(no)
                .setStatus(ErpAuditStatus.PROCESS.getStatus())
                .setTotalCount(getSumValue(stockInternalOutItems, ErpStockInternalOutItemDO::getCount, BigDecimal::add, BigDecimal.ZERO))
                .setTotalPrice(getSumValue(stockInternalOutItems, ErpStockInternalOutItemDO::getTotalPrice, BigDecimal::add, BigDecimal.ZERO)));
        stockInternalOutMapper.insert(stockInternalOut);
        // 2.2 插入出库单项
        if (CollUtil.isNotEmpty(stockInternalOutItems)) {
            stockInternalOutItems.forEach(o -> o.setOutId(stockInternalOut.getId()));
            stockInternalOutItemMapper.insertBatch(stockInternalOutItems);
        }

        // 返回
        return stockInternalOut.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStockInternalOut(ErpStockInternalOutSaveReqVO updateReqVO) {
        // 1.1 校验存在
        ErpStockInternalOutDO stockInternalOut = validateStockInternalOutExists(updateReqVO.getId());
        // 1.2 校验状态（已审核的不能修改）
        if (ErpAuditStatus.APPROVE.getStatus().equals(stockInternalOut.getStatus())) {
            throw exception(STOCK_INTERNAL_OUT_NOT_EXISTS); // TODO: 需要添加对应的错误码
        }
        // 1.3 校验出库项的有效性（如果有明细项）
        List<ErpStockInternalOutItemDO> stockInternalOutItems = CollUtil.isNotEmpty(updateReqVO.getItems())
                ? validateStockInternalOutItems(updateReqVO.getItems())
                : Collections.emptyList();

        // 2.1 更新出库单
        ErpStockInternalOutDO updateObj = BeanUtils.toBean(updateReqVO, ErpStockInternalOutDO.class, out -> out
                .setTotalCount(getSumValue(stockInternalOutItems, ErpStockInternalOutItemDO::getCount, BigDecimal::add, BigDecimal.ZERO))
                .setTotalPrice(getSumValue(stockInternalOutItems, ErpStockInternalOutItemDO::getTotalPrice, BigDecimal::add, BigDecimal.ZERO)));
        stockInternalOutMapper.updateById(updateObj);
        // 2.2 更新出库单项
        if (CollUtil.isNotEmpty(stockInternalOutItems) || CollUtil.isNotEmpty(updateReqVO.getItems())) {
            updateStockInternalOutItemList(updateReqVO.getId(), stockInternalOutItems);
        }
    }

    @Override
    public void deleteStockInternalOut(Long id) {
        // 校验存在
        validateStockInternalOutExists(id); // 验证存在
        // 删除
        stockInternalOutMapper.deleteById(id);
    }

    @Override
        public void deleteStockInternalOutListByIds(List<Long> ids) {
        // 删除
        stockInternalOutMapper.deleteByIds(ids);
        }


    @Override
    public ErpStockInternalOutDO getStockInternalOut(Long id) {
        return stockInternalOutMapper.selectById(id);
    }

    @Override
    public PageResult<ErpStockInternalOutDO> getStockInternalOutPage(ErpStockInternalOutPageReqVO pageReqVO) {
        return stockInternalOutMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStockInternalOutStatus(Long id, Integer status) {
        boolean approve = ErpAuditStatus.APPROVE.getStatus().equals(status);
        // 1.1 校验存在
        ErpStockInternalOutDO stockInternalOut = validateStockInternalOutExists(id);
        // 1.2 校验状态
        if (stockInternalOut.getStatus().equals(status)) {
            throw exception(STOCK_INTERNAL_OUT_NOT_EXISTS); // TODO: 需要添加对应的错误码
        }

        // 2. 更新状态
        int updateCount = stockInternalOutMapper.updateByIdAndStatus(id, stockInternalOut.getStatus(),
                new ErpStockInternalOutDO().setStatus(status));
        if (updateCount == 0) {
            throw exception(STOCK_INTERNAL_OUT_NOT_EXISTS); // TODO: 需要添加对应的错误码
        }

        // 3. 变更库存（出库时数量为负数）
        List<ErpStockInternalOutItemDO> stockInternalOutItems = stockInternalOutItemMapper.selectListByOutId(id);
        if (CollUtil.isNotEmpty(stockInternalOutItems)) {
            Integer bizType = approve ? ErpStockRecordBizTypeEnum.INTERNAL_OUT.getType()
                    : ErpStockRecordBizTypeEnum.INTERNAL_OUT_CANCEL.getType();
            stockInternalOutItems.forEach(stockInternalOutItem -> {
                // 出库时数量为负数，反审核时恢复为正数
                BigDecimal count = approve ? stockInternalOutItem.getCount().negate() : stockInternalOutItem.getCount();
                stockRecordService.createStockRecord(new ErpStockRecordCreateReqBO(
                        stockInternalOutItem.getProductId(), stockInternalOutItem.getWarehouseId(), count,
                        bizType, stockInternalOutItem.getOutId(), stockInternalOutItem.getId(), stockInternalOut.getNo()));
            });
        }
    }

    private List<ErpStockInternalOutItemDO> validateStockInternalOutItems(List<ErpStockInternalOutSaveReqVO.Item> list) {
        // 1.1 校验产品存在
        List<cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO> productList = productService.validProductList(
                convertSet(list, ErpStockInternalOutSaveReqVO.Item::getProductId));
        Map<Long, cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO> productMap = convertMap(productList, cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO::getId);
        // 1.2 校验仓库存在
        warehouseService.validWarehouseList(convertSet(
                list, ErpStockInternalOutSaveReqVO.Item::getWarehouseId));
        // 2. 转化为 ErpStockInternalOutItemDO 列表
        return convertList(list, o -> BeanUtils.toBean(o, ErpStockInternalOutItemDO.class, item -> item
                .setProductUnitId(productMap.get(item.getProductId()).getUnitId())
                .setTotalPrice(MoneyUtils.priceMultiply(item.getProductPrice(), item.getCount()))));
    }

    private void updateStockInternalOutItemList(Long id, List<ErpStockInternalOutItemDO> newList) {
        // 第一步，对比新老数据，获得添加、修改、删除的列表
        List<ErpStockInternalOutItemDO> oldList = stockInternalOutItemMapper.selectListByOutId(id);
        List<List<ErpStockInternalOutItemDO>> diffList = diffList(oldList, newList, // id 不同，就认为是不同的记录
                (oldVal, newVal) -> oldVal.getId().equals(newVal.getId()));

        // 第二步，批量添加、修改、删除
        if (CollUtil.isNotEmpty(diffList.get(0))) {
            diffList.get(0).forEach(o -> o.setOutId(id));
            stockInternalOutItemMapper.insertBatch(diffList.get(0));
        }
        if (CollUtil.isNotEmpty(diffList.get(1))) {
            stockInternalOutItemMapper.updateBatch(diffList.get(1));
        }
        if (CollUtil.isNotEmpty(diffList.get(2))) {
            stockInternalOutItemMapper.deleteByIds(convertList(diffList.get(2), ErpStockInternalOutItemDO::getId));
        }
    }

    private ErpStockInternalOutDO validateStockInternalOutExists(Long id) {
        ErpStockInternalOutDO stockInternalOut = stockInternalOutMapper.selectById(id);
        if (stockInternalOut == null) {
            throw exception(STOCK_INTERNAL_OUT_NOT_EXISTS);
        }
        return stockInternalOut;
    }

}