package cn.iocoder.yudao.module.erp.dal.dataobject.productsku;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 产品SKU DO
 *
 * @author 芋道源码
 */
@TableName("erp_product_sku")
@KeySequence("erp_product_sku_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSkuDO extends BaseDO {

    /**
     * SKU编号
     */
    @TableId
    private Long id;
    /**
     * 产品编号（关联产品）
     */
    private Long productId;
    /**
     * SKU编码
     */
    private String skuCode;
    /**
     * SKU名称
     */
    private String skuName;
    /**
     * SKU描述
     */
    private String description;
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    /**
     * 分类编号
     */
    private Long categoryId;
    /**
     * SKU条码
     */
    private String barCode;
    /**
     * SKU规格
     */
    private String standard;
    /**
     * 单位编号
     */
    private Long unitId;
    /**
     * SKU图片URL
     */
    private String imageUrl;
    /**
     * 规格参数（JSON格式）
     */
    private String specification;
    /**
     * 属性信息（JSON格式，如：颜色、尺寸等）
     */
    private String attributes;
    /**
     * 采购价格，单位：元
     */
    private BigDecimal purchasePrice;
    /**
     * 销售价格，单位：元
     */
    private BigDecimal salePrice;
    /**
     * 最低价格，单位：元
     */
    private BigDecimal minPrice;
    /**
     * 成本价格，单位：元
     */
    private BigDecimal costPrice;
    /**
     * 颜色
     */
    private String color;
    /**
     * 尺寸
     */
    private String size;
    /**
     * 材质
     */
    private String material;
    /**
     * 重量（kg）
     */
    private BigDecimal weight;
    /**
     * 体积（m³）
     */
    private BigDecimal volume;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;


}