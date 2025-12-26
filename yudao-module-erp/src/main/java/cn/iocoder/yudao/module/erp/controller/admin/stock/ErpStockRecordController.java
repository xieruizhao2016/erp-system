package cn.iocoder.yudao.module.erp.controller.admin.stock;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ErpProductRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.stock.vo.record.ErpStockRecordPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.stock.vo.record.ErpStockRecordRespVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpStockRecordDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpWarehouseDO;
import cn.iocoder.yudao.module.erp.service.product.ErpProductService;
import cn.iocoder.yudao.module.erp.service.stock.ErpStockRecordService;
import cn.iocoder.yudao.module.erp.service.stock.ErpWarehouseService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.erp.dal.mysql.purchase.ErpSupplierMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.sale.ErpCustomerMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.purchase.ErpPurchaseInMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.sale.ErpSaleOutMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.stock.ErpStockInMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.stock.ErpStockOutMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.stock.internalin.ErpStockInternalInMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.stock.internalout.ErpStockInternalOutMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.sale.ErpSaleReturnMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.purchase.ErpPurchaseReturnMapper;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpSupplierDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpCustomerDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseInDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOutDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpStockInDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpStockOutDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.internalin.ErpStockInternalInDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.internalout.ErpStockInternalOutDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleReturnDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseReturnDO;
import cn.iocoder.yudao.module.erp.enums.stock.ErpStockRecordBizTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - ERP 产品库存明细")
@RestController
@RequestMapping("/erp/stock-record")
@Validated
public class ErpStockRecordController {

    @Resource
    private ErpStockRecordService stockRecordService;
    @Resource
    private ErpProductService productService;
    @Resource
    private ErpWarehouseService warehouseService;

    @Resource
    private AdminUserApi adminUserApi;
    
    @Resource
    private ErpSupplierMapper supplierMapper;
    
    @Resource
    private ErpCustomerMapper customerMapper;
    
    @Resource
    private ErpPurchaseInMapper purchaseInMapper;
    
    @Resource
    private ErpSaleOutMapper saleOutMapper;
    
    @Resource
    private ErpStockInMapper stockInMapper;
    
    @Resource
    private ErpStockOutMapper stockOutMapper;
    
    @Resource
    private ErpStockInternalInMapper stockInternalInMapper;
    
    @Resource
    private ErpStockInternalOutMapper stockInternalOutMapper;
    
    @Resource
    private ErpSaleReturnMapper saleReturnMapper;
    
    @Resource
    private ErpPurchaseReturnMapper purchaseReturnMapper;

    @GetMapping("/get")
    @Operation(summary = "获得产品库存明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:stock-record:query')")
    public CommonResult<ErpStockRecordRespVO> getStockRecord(@RequestParam("id") Long id) {
        ErpStockRecordDO stockRecord = stockRecordService.getStockRecord(id);
        return success(BeanUtils.toBean(stockRecord, ErpStockRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品库存明细分页")
    @PreAuthorize("@ss.hasPermission('erp:stock-record:query')")
    public CommonResult<PageResult<ErpStockRecordRespVO>> getStockRecordPage(@Valid ErpStockRecordPageReqVO pageReqVO) {
        PageResult<ErpStockRecordDO> pageResult = stockRecordService.getStockRecordPage(pageReqVO);
        return success(buildStockRecrodVOPageResult(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品库存明细 Excel")
    @PreAuthorize("@ss.hasPermission('erp:stock-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportStockRecordExcel(@Valid ErpStockRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpStockRecordRespVO> list = buildStockRecrodVOPageResult(stockRecordService.getStockRecordPage(pageReqVO)).getList();
        // 导出 Excel
        ExcelUtils.write(response, "产品库存明细.xls", "数据", ErpStockRecordRespVO.class, list);
    }

    private PageResult<ErpStockRecordRespVO> buildStockRecrodVOPageResult(PageResult<ErpStockRecordDO> pageResult) {
        if (CollUtil.isEmpty(pageResult.getList())) {
            return PageResult.empty(pageResult.getTotal());
        }
        Map<Long, ErpProductRespVO> productMap = productService.getProductVOMap(
                convertSet(pageResult.getList(), ErpStockRecordDO::getProductId));
        Map<Long, ErpWarehouseDO> warehouseMap = warehouseService.getWarehouseMap(
                convertSet(pageResult.getList(), ErpStockRecordDO::getWarehouseId));
        // 安全地转换 creator 字符串为 Long，过滤掉空值或无效值
        // convertSet 会自动过滤掉 null 值
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
                convertSet(pageResult.getList(), record -> {
                    String creator = record.getCreator();
                    if (creator == null || creator.trim().isEmpty()) {
                        return null;
                    }
                    try {
                        return Long.parseLong(creator);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }));
        return BeanUtils.toBean(pageResult, ErpStockRecordRespVO.class, stock -> {
            MapUtils.findAndThen(productMap, stock.getProductId(), product -> stock.setProductName(product.getName())
                    .setCategoryName(product.getCategoryName()).setUnitName(product.getUnitName()));
            MapUtils.findAndThen(warehouseMap, stock.getWarehouseId(), warehouse -> stock.setWarehouseName(warehouse.getName()));
            // 安全地转换 creator 字符串为 Long
            String creator = stock.getCreator();
            if (creator != null && !creator.trim().isEmpty()) {
                try {
                    Long creatorId = Long.parseLong(creator);
                    MapUtils.findAndThen(userMap, creatorId, user -> stock.setCreatorName(user.getNickname()));
                } catch (NumberFormatException e) {
                    // 如果转换失败，不设置 creatorName，保持为 null
                }
            }
            // 填充关系人信息
            fillRelatedPersonName(stock);
        });
    }

    /**
     * 填充关系人信息
     * 根据业务类型查询对应的关系人：
     * - 采购入库、其它入库、采购退货出库 → 供应商
     * - 销售出库、其它出库、销售退货入库 → 客户
     * - 内部入库、内部出库 → 员工
     */
    private void fillRelatedPersonName(ErpStockRecordRespVO stock) {
        if (stock.getBizType() == null || stock.getBizId() == null) {
            return;
        }
        
        Integer bizType = stock.getBizType();
        Long bizId = stock.getBizId();
        
        try {
            // 采购入库(70)、其它入库(10)、采购退货出库(80) → 供应商
            if (bizType == ErpStockRecordBizTypeEnum.PURCHASE_IN.getType() 
                    || bizType == ErpStockRecordBizTypeEnum.PURCHASE_IN_CANCEL.getType()) {
                // 采购入库
                ErpPurchaseInDO purchaseIn = purchaseInMapper.selectById(bizId);
                if (purchaseIn != null && purchaseIn.getSupplierId() != null) {
                    ErpSupplierDO supplier = supplierMapper.selectById(purchaseIn.getSupplierId());
                    if (supplier != null) {
                        stock.setRelatedPersonName(supplier.getName());
                    }
                }
            } else if (bizType == ErpStockRecordBizTypeEnum.OTHER_IN.getType() 
                    || bizType == ErpStockRecordBizTypeEnum.OTHER_IN_CANCEL.getType()) {
                // 其它入库
                ErpStockInDO stockIn = stockInMapper.selectById(bizId);
                if (stockIn != null && stockIn.getSupplierId() != null) {
                    ErpSupplierDO supplier = supplierMapper.selectById(stockIn.getSupplierId());
                    if (supplier != null) {
                        stock.setRelatedPersonName(supplier.getName());
                    }
                }
            } else if (bizType == ErpStockRecordBizTypeEnum.PURCHASE_RETURN.getType() 
                    || bizType == ErpStockRecordBizTypeEnum.PURCHASE_RETURN_CANCEL.getType()) {
                // 采购退货出库
                ErpPurchaseReturnDO purchaseReturn = purchaseReturnMapper.selectById(bizId);
                if (purchaseReturn != null && purchaseReturn.getSupplierId() != null) {
                    ErpSupplierDO supplier = supplierMapper.selectById(purchaseReturn.getSupplierId());
                    if (supplier != null) {
                        stock.setRelatedPersonName(supplier.getName());
                    }
                }
            }
            // 销售出库(50)、其它出库(20)、销售退货入库(60) → 客户
            else if (bizType == ErpStockRecordBizTypeEnum.SALE_OUT.getType() 
                    || bizType == ErpStockRecordBizTypeEnum.SALE_OUT_CANCEL.getType()) {
                // 销售出库
                ErpSaleOutDO saleOut = saleOutMapper.selectById(bizId);
                if (saleOut != null && saleOut.getCustomerId() != null) {
                    ErpCustomerDO customer = customerMapper.selectById(saleOut.getCustomerId());
                    if (customer != null) {
                        stock.setRelatedPersonName(customer.getName());
                    }
                }
            } else if (bizType == ErpStockRecordBizTypeEnum.OTHER_OUT.getType() 
                    || bizType == ErpStockRecordBizTypeEnum.OTHER_OUT_CANCEL.getType()) {
                // 其它出库
                ErpStockOutDO stockOut = stockOutMapper.selectById(bizId);
                if (stockOut != null && stockOut.getCustomerId() != null) {
                    ErpCustomerDO customer = customerMapper.selectById(stockOut.getCustomerId());
                    if (customer != null) {
                        stock.setRelatedPersonName(customer.getName());
                    }
                }
            } else if (bizType == ErpStockRecordBizTypeEnum.SALE_RETURN.getType() 
                    || bizType == ErpStockRecordBizTypeEnum.SALE_RETURN_CANCEL.getType()) {
                // 销售退货入库
                ErpSaleReturnDO saleReturn = saleReturnMapper.selectById(bizId);
                if (saleReturn != null && saleReturn.getCustomerId() != null) {
                    ErpCustomerDO customer = customerMapper.selectById(saleReturn.getCustomerId());
                    if (customer != null) {
                        stock.setRelatedPersonName(customer.getName());
                    }
                }
            }
            // 内部入库(90)、内部出库(92) → 员工
            else if (bizType == ErpStockRecordBizTypeEnum.INTERNAL_IN.getType() 
                    || bizType == ErpStockRecordBizTypeEnum.INTERNAL_IN_CANCEL.getType()) {
                // 内部入库
                ErpStockInternalInDO stockInternalIn = stockInternalInMapper.selectById(bizId);
                if (stockInternalIn != null && stockInternalIn.getEmployeeId() != null) {
                    AdminUserRespDTO user = adminUserApi.getUser(stockInternalIn.getEmployeeId());
                    if (user != null) {
                        stock.setRelatedPersonName(user.getNickname());
                    }
                }
            } else if (bizType == ErpStockRecordBizTypeEnum.INTERNAL_OUT.getType() 
                    || bizType == ErpStockRecordBizTypeEnum.INTERNAL_OUT_CANCEL.getType()) {
                // 内部出库
                ErpStockInternalOutDO stockInternalOut = stockInternalOutMapper.selectById(bizId);
                if (stockInternalOut != null && stockInternalOut.getEmployeeId() != null) {
                    AdminUserRespDTO user = adminUserApi.getUser(stockInternalOut.getEmployeeId());
                    if (user != null) {
                        stock.setRelatedPersonName(user.getNickname());
                    }
                }
            }
        } catch (Exception e) {
            // 忽略异常，避免影响主流程
            // 关系人信息填充失败不影响其他信息的显示
        }
    }

}