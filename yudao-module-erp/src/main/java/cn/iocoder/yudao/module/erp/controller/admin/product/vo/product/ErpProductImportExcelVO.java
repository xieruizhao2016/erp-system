package cn.iocoder.yudao.module.erp.controller.admin.product.vo.product;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 产品 Excel 导入 VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpProductImportExcelVO {

    @ExcelProperty("产品名称")
    private String name;

    @ExcelProperty("产品条码")
    private String barCode;

    @ExcelProperty("产品分类名称")
    private String categoryName;

    @ExcelProperty("单位名称")
    private String unitName;

    @ExcelProperty("包装名称")
    private String packageName;

    @ExcelProperty("OEM名称")
    private String oemName;

    @ExcelProperty("状态")
    private Integer status;

    @ExcelProperty("规格")
    private String standard;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("保质期天数")
    private Integer expiryDay;

    @ExcelProperty("基础重量（kg）")
    private BigDecimal weight;

    @ExcelProperty("采购价格")
    private BigDecimal purchasePrice;

    @ExcelProperty("销售价格")
    private BigDecimal salePrice;

    @ExcelProperty("最低价格")
    private BigDecimal minPrice;

    @ExcelProperty("SKU编码列表")
    private String skuCodes;

}

