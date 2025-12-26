package cn.iocoder.yudao.module.erp.controller.admin.purchase.vo.order;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购订单 Excel 导入 VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpPurchaseOrderImportExcelVO {

    @ExcelProperty("订单号")
    private String orderNo;

    @ExcelProperty("供应商名称")
    private String supplierName;

    @ExcelProperty("订单时间")
    private LocalDateTime orderTime;

    @ExcelProperty("结算账户")
    private String accountName;

    @ExcelProperty("优惠率")
    private BigDecimal discountPercent;

    @ExcelProperty("定金金额")
    private BigDecimal depositPrice;

    @ExcelProperty("备注")
    private String remark;

    // 订单项字段
    @ExcelProperty("产品名称")
    private String productName;

    @ExcelProperty("产品单位")
    private String productUnitName;

    @ExcelProperty("产品数量")
    private BigDecimal count;

    @ExcelProperty("产品单价")
    private BigDecimal productPrice;

    @ExcelProperty("税率")
    private BigDecimal taxPercent;

    @ExcelProperty("订单项备注")
    private String itemRemark;

}

