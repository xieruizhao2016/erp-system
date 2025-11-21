package cn.iocoder.yudao.module.erp.dal.dataobject.productpackage;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 产品包装 DO
 *
 * @author 芋道源码
 */
@TableName("erp_product_package")
@KeySequence("erp_product_package_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPackageDO extends BaseDO {

    /**
     * 包装编号
     */
    @TableId
    private Long id;
    /**
     * 包装编码
     */
    private String packageCode;
    /**
     * 包装名称
     */
    private String packageName;
    /**
     * 包装描述
     */
    private String description;
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    /**
     * 装箱数量（件/箱）
     */
    private Integer quantityPerBox;
    /**
     * 单箱毛重（KG）
     */
    private BigDecimal grossWeight;
    /**
     * 单箱净重（KG）
     */
    private BigDecimal netWeight;
    /**
     * 内箱尺寸（CM），格式：长x宽x高
     */
    private String innerBoxSize;
    /**
     * 外箱尺寸（CM），格式：长x宽x高
     */
    private String outerBoxSize;
    /**
     * 外箱体积（立方米）
     */
    private BigDecimal boxVolume;
    /**
     * 托盘装箱数（箱/托盘）
     */
    private Integer palletQuantity;
    /**
     * 包装材料
     */
    private String material;
    /**
     * 包装类型（如：纸箱、木箱、托盘等）
     */
    private String packageType;
    /**
     * 包装条码
     */
    private String barcode;
    /**
     * 包装图片URL
     */
    private String imageUrl;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;

}

