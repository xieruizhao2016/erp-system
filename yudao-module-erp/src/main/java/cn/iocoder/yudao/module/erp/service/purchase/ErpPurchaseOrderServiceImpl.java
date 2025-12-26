package cn.iocoder.yudao.module.erp.service.purchase;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.number.MoneyUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.purchase.vo.order.ErpPurchaseOrderImportExcelVO;
import cn.iocoder.yudao.module.erp.controller.admin.purchase.vo.order.ErpPurchaseOrderImportReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.purchase.vo.order.ErpPurchaseOrderImportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.purchase.vo.order.ErpPurchaseOrderPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.purchase.vo.order.ErpPurchaseOrderSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderItemDO;
import cn.iocoder.yudao.module.erp.dal.mysql.purchase.ErpPurchaseOrderItemMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.purchase.ErpPurchaseOrderMapper;
import cn.iocoder.yudao.module.erp.dal.redis.no.ErpNoRedisDAO;
import cn.iocoder.yudao.module.erp.enums.ErpAuditStatus;
import cn.iocoder.yudao.module.erp.service.finance.ErpAccountService;
import cn.iocoder.yudao.module.erp.service.finance.payable.ErpFinancePayableService;
import cn.iocoder.yudao.module.erp.service.product.ErpProductService;
import cn.iocoder.yudao.module.erp.service.product.ErpProductUnitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

// TODO 芋艿：记录操作日志

/**
 * ERP 采购订单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ErpPurchaseOrderServiceImpl implements ErpPurchaseOrderService {

    @Resource
    private ErpPurchaseOrderMapper purchaseOrderMapper;
    @Resource
    private ErpPurchaseOrderItemMapper purchaseOrderItemMapper;

    @Resource
    private ErpNoRedisDAO noRedisDAO;

    @Resource
    private ErpProductService productService;
    @Resource
    private ErpProductUnitService productUnitService;
    @Resource
    private ErpSupplierService supplierService;
    @Resource
    private ErpAccountService accountService;
    @Resource
    private ErpFinancePayableService financePayableService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPurchaseOrder(ErpPurchaseOrderSaveReqVO createReqVO) {
        // 1.1 校验订单项的有效性
        List<ErpPurchaseOrderItemDO> purchaseOrderItems = validatePurchaseOrderItems(createReqVO.getItems());
        // 1.2 校验供应商
        supplierService.validateSupplier(createReqVO.getSupplierId());
        // 1.3 校验结算账户
        if (createReqVO.getAccountId() != null) {
            accountService.validateAccount(createReqVO.getAccountId());
        }
        // 1.4 生成订单号，并校验唯一性
        String no = noRedisDAO.generate(ErpNoRedisDAO.PURCHASE_ORDER_NO_PREFIX);
        if (purchaseOrderMapper.selectByNo(no) != null) {
            throw exception(PURCHASE_ORDER_NO_EXISTS);
        }

        // 2.1 插入订单
        ErpPurchaseOrderDO purchaseOrder = BeanUtils.toBean(createReqVO, ErpPurchaseOrderDO.class, in -> in
                .setNo(no).setStatus(ErpAuditStatus.PROCESS.getStatus()));
        calculateTotalPrice(purchaseOrder, purchaseOrderItems);
        purchaseOrderMapper.insert(purchaseOrder);
        // 2.2 插入订单项
        purchaseOrderItems.forEach(o -> o.setOrderId(purchaseOrder.getId()));
        purchaseOrderItemMapper.insertBatch(purchaseOrderItems);
        return purchaseOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePurchaseOrder(ErpPurchaseOrderSaveReqVO updateReqVO) {
        // 1.1 校验存在
        ErpPurchaseOrderDO purchaseOrder = validatePurchaseOrderExists(updateReqVO.getId());
        if (ErpAuditStatus.APPROVE.getStatus().equals(purchaseOrder.getStatus())) {
            throw exception(PURCHASE_ORDER_UPDATE_FAIL_APPROVE, purchaseOrder.getNo());
        }
        // 1.2 校验供应商
        supplierService.validateSupplier(updateReqVO.getSupplierId());
        // 1.3 校验结算账户
        if (updateReqVO.getAccountId() != null) {
            accountService.validateAccount(updateReqVO.getAccountId());
        }
        // 1.4 校验订单项的有效性
        List<ErpPurchaseOrderItemDO> purchaseOrderItems = validatePurchaseOrderItems(updateReqVO.getItems());

        // 2.1 更新订单
        ErpPurchaseOrderDO updateObj = BeanUtils.toBean(updateReqVO, ErpPurchaseOrderDO.class);
        calculateTotalPrice(updateObj, purchaseOrderItems);
        purchaseOrderMapper.updateById(updateObj);
        // 2.2 更新订单项
        updatePurchaseOrderItemList(updateReqVO.getId(), purchaseOrderItems);
    }

    private void calculateTotalPrice(ErpPurchaseOrderDO purchaseOrder, List<ErpPurchaseOrderItemDO> purchaseOrderItems) {
        purchaseOrder.setTotalCount(getSumValue(purchaseOrderItems, ErpPurchaseOrderItemDO::getCount, BigDecimal::add));
        purchaseOrder.setTotalProductPrice(getSumValue(purchaseOrderItems, ErpPurchaseOrderItemDO::getTotalPrice, BigDecimal::add, BigDecimal.ZERO));
        purchaseOrder.setTotalTaxPrice(getSumValue(purchaseOrderItems, ErpPurchaseOrderItemDO::getTaxPrice, BigDecimal::add, BigDecimal.ZERO));
        purchaseOrder.setTotalPrice(purchaseOrder.getTotalProductPrice().add(purchaseOrder.getTotalTaxPrice()));
        // 计算优惠价格
        if (purchaseOrder.getDiscountPercent() == null) {
            purchaseOrder.setDiscountPercent(BigDecimal.ZERO);
        }
        purchaseOrder.setDiscountPrice(MoneyUtils.priceMultiplyPercent(purchaseOrder.getTotalPrice(), purchaseOrder.getDiscountPercent()));
        purchaseOrder.setTotalPrice(purchaseOrder.getTotalPrice().subtract(purchaseOrder.getDiscountPrice()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePurchaseOrderStatus(Long id, Integer status) {
        boolean approve = ErpAuditStatus.APPROVE.getStatus().equals(status);
        // 1.1 校验存在
        ErpPurchaseOrderDO purchaseOrder = validatePurchaseOrderExists(id);
        // 1.2 校验状态
        if (purchaseOrder.getStatus().equals(status)) {
            throw exception(approve ? PURCHASE_ORDER_APPROVE_FAIL : PURCHASE_ORDER_PROCESS_FAIL);
        }
        // 1.3 存在采购入库单，无法反审核
        BigDecimal inCount = purchaseOrder.getInCount() != null ? purchaseOrder.getInCount() : BigDecimal.ZERO;
        if (!approve && inCount.compareTo(BigDecimal.ZERO) > 0) {
            throw exception(PURCHASE_ORDER_PROCESS_FAIL_EXISTS_IN);
        }
        // 1.4 存在采购退货单，无法反审核
        BigDecimal returnCount = purchaseOrder.getReturnCount() != null ? purchaseOrder.getReturnCount() : BigDecimal.ZERO;
        if (!approve && returnCount.compareTo(BigDecimal.ZERO) > 0) {
            throw exception(PURCHASE_ORDER_PROCESS_FAIL_EXISTS_RETURN);
        }

        // 2. 更新状态
        int updateCount = purchaseOrderMapper.updateByIdAndStatus(id, purchaseOrder.getStatus(),
                new ErpPurchaseOrderDO().setStatus(status));
        if (updateCount == 0) {
            throw exception(approve ? PURCHASE_ORDER_APPROVE_FAIL : PURCHASE_ORDER_PROCESS_FAIL);
        }

        // 3. 审核通过时，自动创建应付账款
        if (approve) {
            financePayableService.createPayableFromPurchaseOrder(purchaseOrder);
        } else {
            // 反审核时，删除应付账款（如果存在且未审核）
            financePayableService.deletePayableByOrderId(id);
        }
    }

    private List<ErpPurchaseOrderItemDO> validatePurchaseOrderItems(List<ErpPurchaseOrderSaveReqVO.Item> list) {
        // 1. 校验产品存在
        List<ErpProductDO> productList = productService.validProductList(
                convertSet(list, ErpPurchaseOrderSaveReqVO.Item::getProductId));
        Map<Long, ErpProductDO> productMap = convertMap(productList, ErpProductDO::getId);
        // 2. 转化为 ErpPurchaseOrderItemDO 列表
        return convertList(list, o -> BeanUtils.toBean(o, ErpPurchaseOrderItemDO.class, item -> {
            item.setProductUnitId(productMap.get(item.getProductId()).getUnitId());
            item.setTotalPrice(MoneyUtils.priceMultiply(item.getProductPrice(), item.getCount()));
            if (item.getTotalPrice() == null) {
                return;
            }
            if (item.getTaxPercent() != null) {
                item.setTaxPrice(MoneyUtils.priceMultiplyPercent(item.getTotalPrice(), item.getTaxPercent()));
            }
        }));
    }

    private void updatePurchaseOrderItemList(Long id, List<ErpPurchaseOrderItemDO> newList) {
        // 第一步，对比新老数据，获得添加、修改、删除的列表
        List<ErpPurchaseOrderItemDO> oldList = purchaseOrderItemMapper.selectListByOrderId(id);
        List<List<ErpPurchaseOrderItemDO>> diffList = diffList(oldList, newList, // id 不同，就认为是不同的记录
                (oldVal, newVal) -> oldVal.getId().equals(newVal.getId()));

        // 第二步，批量添加、修改、删除
        if (CollUtil.isNotEmpty(diffList.get(0))) {
            diffList.get(0).forEach(o -> o.setOrderId(id));
            purchaseOrderItemMapper.insertBatch(diffList.get(0));
        }
        if (CollUtil.isNotEmpty(diffList.get(1))) {
            purchaseOrderItemMapper.updateBatch(diffList.get(1));
        }
        if (CollUtil.isNotEmpty(diffList.get(2))) {
            purchaseOrderItemMapper.deleteByIds(convertList(diffList.get(2), ErpPurchaseOrderItemDO::getId));
        }
    }

    @Override
    public void updatePurchaseOrderInCount(Long id, Map<Long, BigDecimal> inCountMap) {
        List<ErpPurchaseOrderItemDO> orderItems = purchaseOrderItemMapper.selectListByOrderId(id);
        // 1. 更新每个采购订单项
        orderItems.forEach(item -> {
            BigDecimal inCount = inCountMap.getOrDefault(item.getId(), BigDecimal.ZERO);
            BigDecimal currentInCount = item.getInCount() != null ? item.getInCount() : BigDecimal.ZERO;
            if (currentInCount.equals(inCount)) {
                return;
            }
            if (inCount.compareTo(item.getCount()) > 0) {
                throw exception(PURCHASE_ORDER_ITEM_IN_FAIL_PRODUCT_EXCEED,
                        productService.getProduct(item.getProductId()).getName(), item.getCount());
            }
            purchaseOrderItemMapper.updateById(new ErpPurchaseOrderItemDO().setId(item.getId()).setInCount(inCount));
        });
        // 2. 更新采购订单
        BigDecimal totalInCount = getSumValue(inCountMap.values(), value -> value, BigDecimal::add, BigDecimal.ZERO);
        purchaseOrderMapper.updateById(new ErpPurchaseOrderDO().setId(id).setInCount(totalInCount));
    }

    @Override
    public void updatePurchaseOrderReturnCount(Long orderId, Map<Long, BigDecimal> returnCountMap) {
        List<ErpPurchaseOrderItemDO> orderItems = purchaseOrderItemMapper.selectListByOrderId(orderId);
        // 1. 更新每个采购订单项
        orderItems.forEach(item -> {
            BigDecimal returnCount = returnCountMap.getOrDefault(item.getId(), BigDecimal.ZERO);
            BigDecimal currentReturnCount = item.getReturnCount() != null ? item.getReturnCount() : BigDecimal.ZERO;
            if (currentReturnCount.equals(returnCount)) {
                return;
            }
            BigDecimal currentInCount = item.getInCount() != null ? item.getInCount() : BigDecimal.ZERO;
            if (returnCount.compareTo(currentInCount) > 0) {
                throw exception(PURCHASE_ORDER_ITEM_RETURN_FAIL_IN_EXCEED,
                        productService.getProduct(item.getProductId()).getName(), currentInCount);
            }
            purchaseOrderItemMapper.updateById(new ErpPurchaseOrderItemDO().setId(item.getId()).setReturnCount(returnCount));
        });
        // 2. 更新采购订单
        BigDecimal totalReturnCount = getSumValue(returnCountMap.values(), value -> value, BigDecimal::add, BigDecimal.ZERO);
        purchaseOrderMapper.updateById(new ErpPurchaseOrderDO().setId(orderId).setReturnCount(totalReturnCount));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePurchaseOrder(List<Long> ids) {
        // 1. 校验不处于已审批
        List<ErpPurchaseOrderDO> purchaseOrders = purchaseOrderMapper.selectByIds(ids);
        if (CollUtil.isEmpty(purchaseOrders)) {
            return;
        }
        purchaseOrders.forEach(purchaseOrder -> {
            if (ErpAuditStatus.APPROVE.getStatus().equals(purchaseOrder.getStatus())) {
                throw exception(PURCHASE_ORDER_DELETE_FAIL_APPROVE, purchaseOrder.getNo());
            }
        });

        // 2. 遍历删除，并记录操作日志
        purchaseOrders.forEach(purchaseOrder -> {
            // 2.1 删除订单
            purchaseOrderMapper.deleteById(purchaseOrder.getId());
            // 2.2 删除订单项
            purchaseOrderItemMapper.deleteByOrderId(purchaseOrder.getId());
        });
    }

    private ErpPurchaseOrderDO validatePurchaseOrderExists(Long id) {
        ErpPurchaseOrderDO purchaseOrder = purchaseOrderMapper.selectById(id);
        if (purchaseOrder == null) {
            throw exception(PURCHASE_ORDER_NOT_EXISTS);
        }
        return purchaseOrder;
    }

    @Override
    public ErpPurchaseOrderDO getPurchaseOrder(Long id) {
        return purchaseOrderMapper.selectById(id);
    }

    @Override
    public ErpPurchaseOrderDO validatePurchaseOrder(Long id) {
        ErpPurchaseOrderDO purchaseOrder = validatePurchaseOrderExists(id);
        if (ObjectUtil.notEqual(purchaseOrder.getStatus(), ErpAuditStatus.APPROVE.getStatus())) {
            throw exception(PURCHASE_ORDER_NOT_APPROVE);
        }
        return purchaseOrder;
    }

    @Override
    public PageResult<ErpPurchaseOrderDO> getPurchaseOrderPage(ErpPurchaseOrderPageReqVO pageReqVO) {
        return purchaseOrderMapper.selectPage(pageReqVO);
    }

    // ==================== 订单项 ====================

    @Override
    public List<ErpPurchaseOrderItemDO> getPurchaseOrderItemListByOrderId(Long orderId) {
        return purchaseOrderItemMapper.selectListByOrderId(orderId);
    }

    @Override
    public List<ErpPurchaseOrderItemDO> getPurchaseOrderItemListByOrderIds(Collection<Long> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return Collections.emptyList();
        }
        return purchaseOrderItemMapper.selectListByOrderIds(orderIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErpPurchaseOrderImportRespVO importPurchaseOrderList(List<ErpPurchaseOrderImportExcelVO> importOrders,
                                                                ErpPurchaseOrderImportReqVO importReqVO) {
        // 校验非空
        if (CollUtil.isEmpty(importOrders)) {
            throw new ServiceException(1_030_500_000, "导入采购订单数据不能为空！");
        }

        // 预加载所有关联数据，建立名称到ID的映射
        Map<String, Long> supplierNameMap = buildSupplierNameMap();
        Map<String, Long> accountNameMap = buildAccountNameMap();
        Map<String, Long> productNameMap = buildProductNameMap();
        Map<String, Long> unitNameMap = buildUnitNameMap();

        // 按订单号分组，处理多行数据合并
        Map<String, List<ErpPurchaseOrderImportExcelVO>> orderMap = new LinkedHashMap<>();
        String currentOrderNo = null;
        for (ErpPurchaseOrderImportExcelVO row : importOrders) {
            if (StrUtil.isNotBlank(row.getOrderNo())) {
                currentOrderNo = row.getOrderNo();
                orderMap.put(currentOrderNo, new LinkedList<>());
            }
            if (currentOrderNo != null) {
                orderMap.get(currentOrderNo).add(row);
            }
        }

        // 逐条处理
        ErpPurchaseOrderImportRespVO respVO = ErpPurchaseOrderImportRespVO.builder()
                .createOrderNos(new ArrayList<>())
                .updateOrderNos(new ArrayList<>())
                .failureOrderNos(new LinkedHashMap<>())
                .build();

        orderMap.forEach((orderNo, rows) -> {
            try {
                if (CollUtil.isEmpty(rows)) {
                    respVO.getFailureOrderNos().put(orderNo, "订单数据为空");
                    return;
                }

                // 第一行作为订单基本信息
                ErpPurchaseOrderImportExcelVO firstRow = rows.get(0);
                // 后续行作为订单项
                List<ErpPurchaseOrderImportExcelVO> itemRows = rows.size() > 1 ? rows.subList(1, rows.size()) : Collections.emptyList();

                // 验证数据
                validatePurchaseOrderForImport(firstRow, itemRows, supplierNameMap, accountNameMap, productNameMap, unitNameMap);

                // 检查订单是否存在
                ErpPurchaseOrderDO existOrder = purchaseOrderMapper.selectByNo(orderNo);
                if (existOrder == null) {
                    // 创建新订单
                    ErpPurchaseOrderSaveReqVO createReqVO = convertToSaveReqVO(firstRow, itemRows, supplierNameMap, accountNameMap, productNameMap, unitNameMap);
                    createReqVO.setId(null); // 确保是新建
                    // 手动设置订单号
                    createPurchaseOrderWithNo(orderNo, createReqVO);
                    respVO.getCreateOrderNos().add(orderNo);
                } else {
                    // 更新现有订单
                    if (!importReqVO.getUpdateSupport()) {
                        respVO.getFailureOrderNos().put(orderNo, "订单已存在，且未开启更新支持");
                        return;
                    }
                    if (ErpAuditStatus.APPROVE.getStatus().equals(existOrder.getStatus())) {
                        respVO.getFailureOrderNos().put(orderNo, "订单已审核，无法更新");
                        return;
                    }
                    ErpPurchaseOrderSaveReqVO updateReqVO = convertToSaveReqVO(firstRow, itemRows, supplierNameMap, accountNameMap, productNameMap, unitNameMap);
                    updateReqVO.setId(existOrder.getId());
                    updatePurchaseOrder(updateReqVO);
                    respVO.getUpdateOrderNos().add(orderNo);
                }
            } catch (ServiceException ex) {
                respVO.getFailureOrderNos().put(orderNo, ex.getMessage());
            } catch (Exception ex) {
                respVO.getFailureOrderNos().put(orderNo, "导入失败: " + ex.getMessage());
            }
        });

        return respVO;
    }

    /**
     * 建立供应商名称到ID的映射
     */
    private Map<String, Long> buildSupplierNameMap() {
        List<cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpSupplierDO> suppliers =
                supplierService.getSupplierListByStatus(CommonStatusEnum.ENABLE.getStatus());
        return convertMap(suppliers,
                cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpSupplierDO::getName,
                cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpSupplierDO::getId);
    }

    /**
     * 建立账户名称到ID的映射
     */
    private Map<String, Long> buildAccountNameMap() {
        List<cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpAccountDO> accounts =
                accountService.getAccountListByStatus(CommonStatusEnum.ENABLE.getStatus());
        return convertMap(accounts,
                cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpAccountDO::getName,
                cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpAccountDO::getId);
    }

    /**
     * 建立产品名称到ID的映射
     */
    private Map<String, Long> buildProductNameMap() {
        List<ErpProductDO> products = productService.validProductList(
                productService.getProductVOListByStatus(CommonStatusEnum.ENABLE.getStatus())
                        .stream().map(cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ErpProductRespVO::getId)
                        .collect(Collectors.toList()));
        return convertMap(products, ErpProductDO::getName, ErpProductDO::getId);
    }

    /**
     * 建立单位名称到ID的映射
     */
    private Map<String, Long> buildUnitNameMap() {
        List<cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductUnitDO> units =
                productUnitService.getProductUnitListByStatus(CommonStatusEnum.ENABLE.getStatus());
        return convertMap(units,
                cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductUnitDO::getName,
                cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductUnitDO::getId);
    }

    /**
     * 验证导入采购订单数据
     */
    private void validatePurchaseOrderForImport(ErpPurchaseOrderImportExcelVO orderRow,
                                               List<ErpPurchaseOrderImportExcelVO> itemRows,
                                               Map<String, Long> supplierNameMap,
                                               Map<String, Long> accountNameMap,
                                               Map<String, Long> productNameMap,
                                               Map<String, Long> unitNameMap) {
        // 验证订单基本信息
        if (StrUtil.isBlank(orderRow.getSupplierName())) {
            throw new ServiceException(1_030_500_010, "供应商名称不能为空");
        }
        if (!supplierNameMap.containsKey(orderRow.getSupplierName())) {
            throw new ServiceException(1_030_500_011, "供应商不存在: " + orderRow.getSupplierName());
        }
        if (orderRow.getOrderTime() == null) {
            throw new ServiceException(1_030_500_012, "订单时间不能为空");
        }

        // 验证账户（可选）
        if (StrUtil.isNotBlank(orderRow.getAccountName()) &&
                !accountNameMap.containsKey(orderRow.getAccountName())) {
            throw new ServiceException(1_030_500_013, "结算账户不存在: " + orderRow.getAccountName());
        }

        // 验证订单项
        if (CollUtil.isEmpty(itemRows)) {
            throw new ServiceException(1_030_500_014, "订单项不能为空");
        }

        for (ErpPurchaseOrderImportExcelVO itemRow : itemRows) {
            if (StrUtil.isBlank(itemRow.getProductName())) {
                throw new ServiceException(1_030_500_015, "产品名称不能为空");
            }
            if (!productNameMap.containsKey(itemRow.getProductName())) {
                throw new ServiceException(1_030_500_016, "产品不存在: " + itemRow.getProductName());
            }
            if (StrUtil.isBlank(itemRow.getProductUnitName())) {
                throw new ServiceException(1_030_500_017, "产品单位不能为空");
            }
            if (!unitNameMap.containsKey(itemRow.getProductUnitName())) {
                throw new ServiceException(1_030_500_018, "产品单位不存在: " + itemRow.getProductUnitName());
            }
            if (itemRow.getCount() == null || itemRow.getCount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ServiceException(1_030_500_019, "产品数量必须大于0");
            }
        }
    }

    /**
     * 转换为保存请求VO
     */
    private ErpPurchaseOrderSaveReqVO convertToSaveReqVO(ErpPurchaseOrderImportExcelVO orderRow,
                                                         List<ErpPurchaseOrderImportExcelVO> itemRows,
                                                         Map<String, Long> supplierNameMap,
                                                         Map<String, Long> accountNameMap,
                                                         Map<String, Long> productNameMap,
                                                         Map<String, Long> unitNameMap) {
        ErpPurchaseOrderSaveReqVO saveReqVO = new ErpPurchaseOrderSaveReqVO();
        saveReqVO.setSupplierId(supplierNameMap.get(orderRow.getSupplierName()));
        saveReqVO.setOrderTime(orderRow.getOrderTime());
        saveReqVO.setDiscountPercent(orderRow.getDiscountPercent());
        saveReqVO.setDepositPrice(orderRow.getDepositPrice());
        saveReqVO.setRemark(orderRow.getRemark());

        // 处理结算账户
        if (StrUtil.isNotBlank(orderRow.getAccountName())) {
            saveReqVO.setAccountId(accountNameMap.get(orderRow.getAccountName()));
        }

        // 处理订单项
        List<ErpPurchaseOrderSaveReqVO.Item> items = new ArrayList<>();
        for (ErpPurchaseOrderImportExcelVO itemRow : itemRows) {
            ErpPurchaseOrderSaveReqVO.Item item = new ErpPurchaseOrderSaveReqVO.Item();
            item.setProductId(productNameMap.get(itemRow.getProductName()));
            // 使用导入的单位名称查找单位ID
            item.setProductUnitId(unitNameMap.get(itemRow.getProductUnitName()));
            item.setProductPrice(itemRow.getProductPrice());
            item.setCount(itemRow.getCount());
            item.setTaxPercent(itemRow.getTaxPercent());
            item.setRemark(itemRow.getItemRemark());
            items.add(item);
        }
        saveReqVO.setItems(items);

        return saveReqVO;
    }

    /**
     * 创建采购订单（指定订单号）
     */
    private Long createPurchaseOrderWithNo(String no, ErpPurchaseOrderSaveReqVO createReqVO) {
        // 1.1 校验订单项的有效性
        List<ErpPurchaseOrderItemDO> purchaseOrderItems = validatePurchaseOrderItems(createReqVO.getItems());
        // 1.2 校验供应商
        supplierService.validateSupplier(createReqVO.getSupplierId());
        // 1.3 校验结算账户
        if (createReqVO.getAccountId() != null) {
            accountService.validateAccount(createReqVO.getAccountId());
        }
        // 1.4 校验订单号唯一性
        if (purchaseOrderMapper.selectByNo(no) != null) {
            throw exception(PURCHASE_ORDER_NO_EXISTS);
        }

        // 2.1 插入订单
        ErpPurchaseOrderDO purchaseOrder = BeanUtils.toBean(createReqVO, ErpPurchaseOrderDO.class, in -> in
                .setNo(no).setStatus(ErpAuditStatus.PROCESS.getStatus()));
        calculateTotalPrice(purchaseOrder, purchaseOrderItems);
        purchaseOrderMapper.insert(purchaseOrder);
        // 2.2 插入订单项
        purchaseOrderItems.forEach(o -> o.setOrderId(purchaseOrder.getId()));
        purchaseOrderItemMapper.insertBatch(purchaseOrderItems);
        return purchaseOrder.getId();
    }

}
