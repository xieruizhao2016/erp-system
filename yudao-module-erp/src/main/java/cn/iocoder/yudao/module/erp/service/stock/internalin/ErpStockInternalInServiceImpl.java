package cn.iocoder.yudao.module.erp.service.stock.internalin;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.stock.internalin.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.internalin.ErpStockInternalInDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.internalin.ErpStockInternalInItemDO;
import cn.iocoder.yudao.module.erp.dal.mysql.stock.internalin.ErpStockInternalInMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.stock.internalin.ErpStockInternalInItemMapper;
import cn.iocoder.yudao.module.erp.dal.redis.no.ErpNoRedisDAO;
import cn.iocoder.yudao.module.erp.enums.ErpAuditStatus;
import cn.iocoder.yudao.module.erp.enums.stock.ErpStockRecordBizTypeEnum;
import cn.iocoder.yudao.module.erp.service.product.ErpProductService;
import cn.iocoder.yudao.module.erp.service.stock.ErpStockRecordService;
import cn.iocoder.yudao.module.erp.service.stock.ErpWarehouseService;
import cn.iocoder.yudao.module.erp.service.stock.bo.ErpStockRecordCreateReqBO;
import cn.iocoder.yudao.framework.common.util.number.MoneyUtils;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
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
    @Resource
    private ErpStockInternalInItemMapper stockInternalInItemMapper;

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
    public Long createStockInternalIn(ErpStockInternalInSaveReqVO createReqVO) {
        // 1.1 校验入库项的有效性（如果有明细项）
        List<ErpStockInternalInItemDO> stockInternalInItems = CollUtil.isNotEmpty(createReqVO.getItems())
                ? validateStockInternalInItems(createReqVO.getItems())
                : Collections.emptyList();
        // 1.2 生成入库单号
        String no = noRedisDAO.generate(ErpNoRedisDAO.STOCK_INTERNAL_IN_NO_PREFIX);
        if (stockInternalInMapper.selectByNo(no) != null) {
            throw exception(STOCK_INTERNAL_IN_NOT_EXISTS); // TODO: 需要添加对应的错误码
        }

        // 2.1 插入入库单
        ErpStockInternalInDO stockInternalIn = BeanUtils.toBean(createReqVO, ErpStockInternalInDO.class, in -> in
                .setNo(no)
                .setStatus(ErpAuditStatus.PROCESS.getStatus())
                .setTotalCount(getSumValue(stockInternalInItems, ErpStockInternalInItemDO::getCount, BigDecimal::add, BigDecimal.ZERO))
                .setTotalPrice(getSumValue(stockInternalInItems, ErpStockInternalInItemDO::getTotalPrice, BigDecimal::add, BigDecimal.ZERO)));
        stockInternalInMapper.insert(stockInternalIn);
        // 2.2 插入入库单项
        if (CollUtil.isNotEmpty(stockInternalInItems)) {
            stockInternalInItems.forEach(o -> o.setInId(stockInternalIn.getId()));
            stockInternalInItemMapper.insertBatch(stockInternalInItems);
        }

        // 返回
        return stockInternalIn.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStockInternalIn(ErpStockInternalInSaveReqVO updateReqVO) {
        // 1.1 校验存在
        ErpStockInternalInDO stockInternalIn = validateStockInternalInExists(updateReqVO.getId());
        // 1.2 校验状态（已审核的不能修改）
        if (ErpAuditStatus.APPROVE.getStatus().equals(stockInternalIn.getStatus())) {
            throw exception(STOCK_INTERNAL_IN_NOT_EXISTS); // TODO: 需要添加对应的错误码
        }
        // 1.3 校验入库项的有效性（如果有明细项）
        List<ErpStockInternalInItemDO> stockInternalInItems = CollUtil.isNotEmpty(updateReqVO.getItems())
                ? validateStockInternalInItems(updateReqVO.getItems())
                : Collections.emptyList();

        // 2.1 更新入库单
        ErpStockInternalInDO updateObj = BeanUtils.toBean(updateReqVO, ErpStockInternalInDO.class, in -> in
                .setTotalCount(getSumValue(stockInternalInItems, ErpStockInternalInItemDO::getCount, BigDecimal::add, BigDecimal.ZERO))
                .setTotalPrice(getSumValue(stockInternalInItems, ErpStockInternalInItemDO::getTotalPrice, BigDecimal::add, BigDecimal.ZERO)));
        stockInternalInMapper.updateById(updateObj);
        // 2.2 更新入库单项
        if (CollUtil.isNotEmpty(stockInternalInItems) || CollUtil.isNotEmpty(updateReqVO.getItems())) {
            updateStockInternalInItemList(updateReqVO.getId(), stockInternalInItems);
        }
    }

    @Override
    public void deleteStockInternalIn(Long id) {
        // 校验存在
        validateStockInternalInExists(id); // 验证存在
        // 删除
        stockInternalInMapper.deleteById(id);
    }

    @Override
        public void deleteStockInternalInListByIds(List<Long> ids) {
        // 删除
        stockInternalInMapper.deleteByIds(ids);
        }


    @Override
    public ErpStockInternalInDO getStockInternalIn(Long id) {
        return stockInternalInMapper.selectById(id);
    }

    @Override
    public PageResult<ErpStockInternalInDO> getStockInternalInPage(ErpStockInternalInPageReqVO pageReqVO) {
        return stockInternalInMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStockInternalInStatus(Long id, Integer status) {
        boolean approve = ErpAuditStatus.APPROVE.getStatus().equals(status);
        // 1.1 校验存在
        ErpStockInternalInDO stockInternalIn = validateStockInternalInExists(id);
        // 1.2 校验状态
        if (stockInternalIn.getStatus().equals(status)) {
            throw exception(STOCK_INTERNAL_IN_NOT_EXISTS); // TODO: 需要添加对应的错误码
        }

        // 2. 更新状态
        int updateCount = stockInternalInMapper.updateByIdAndStatus(id, stockInternalIn.getStatus(),
                new ErpStockInternalInDO().setStatus(status));
        if (updateCount == 0) {
            throw exception(STOCK_INTERNAL_IN_NOT_EXISTS); // TODO: 需要添加对应的错误码
        }

        // 3. 变更库存
        List<ErpStockInternalInItemDO> stockInternalInItems = stockInternalInItemMapper.selectListByInId(id);
        if (CollUtil.isNotEmpty(stockInternalInItems)) {
            Integer bizType = approve ? ErpStockRecordBizTypeEnum.INTERNAL_IN.getType()
                    : ErpStockRecordBizTypeEnum.INTERNAL_IN_CANCEL.getType();
            stockInternalInItems.forEach(stockInternalInItem -> {
                BigDecimal count = approve ? stockInternalInItem.getCount() : stockInternalInItem.getCount().negate();
                stockRecordService.createStockRecord(new ErpStockRecordCreateReqBO(
                        stockInternalInItem.getProductId(), stockInternalInItem.getWarehouseId(), count,
                        bizType, stockInternalInItem.getInId(), stockInternalInItem.getId(), stockInternalIn.getNo()));
            });
        }
    }

    private List<ErpStockInternalInItemDO> validateStockInternalInItems(List<ErpStockInternalInSaveReqVO.Item> list) {
        // 1.1 校验产品存在
        List<cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO> productList = productService.validProductList(
                convertSet(list, ErpStockInternalInSaveReqVO.Item::getProductId));
        Map<Long, cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO> productMap = convertMap(productList, cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO::getId);
        // 1.2 校验仓库存在
        warehouseService.validWarehouseList(convertSet(
                list, ErpStockInternalInSaveReqVO.Item::getWarehouseId));
        // 2. 转化为 ErpStockInternalInItemDO 列表
        return convertList(list, o -> BeanUtils.toBean(o, ErpStockInternalInItemDO.class, item -> item
                .setProductUnitId(productMap.get(item.getProductId()).getUnitId())
                .setTotalPrice(MoneyUtils.priceMultiply(item.getProductPrice(), item.getCount()))));
    }

    private void updateStockInternalInItemList(Long id, List<ErpStockInternalInItemDO> newList) {
        // 第一步，对比新老数据，获得添加、修改、删除的列表
        List<ErpStockInternalInItemDO> oldList = stockInternalInItemMapper.selectListByInId(id);
        List<List<ErpStockInternalInItemDO>> diffList = diffList(oldList, newList, // id 不同，就认为是不同的记录
                (oldVal, newVal) -> oldVal.getId().equals(newVal.getId()));

        // 第二步，批量添加、修改、删除
        if (CollUtil.isNotEmpty(diffList.get(0))) {
            diffList.get(0).forEach(o -> o.setInId(id));
            stockInternalInItemMapper.insertBatch(diffList.get(0));
        }
        if (CollUtil.isNotEmpty(diffList.get(1))) {
            stockInternalInItemMapper.updateBatch(diffList.get(1));
        }
        if (CollUtil.isNotEmpty(diffList.get(2))) {
            stockInternalInItemMapper.deleteByIds(convertList(diffList.get(2), ErpStockInternalInItemDO::getId));
        }
    }

    private ErpStockInternalInDO validateStockInternalInExists(Long id) {
        ErpStockInternalInDO stockInternalIn = stockInternalInMapper.selectById(id);
        if (stockInternalIn == null) {
            throw exception(STOCK_INTERNAL_IN_NOT_EXISTS);
        }
        return stockInternalIn;
    }

}