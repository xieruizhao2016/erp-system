package cn.iocoder.yudao.module.erp.dal.dataobject.productoem;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 产品OEM DO
 *
 * @author 芋道源码
 */
@TableName("erp_product_oem")
@KeySequence("erp_product_oem_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOemDO extends BaseDO {

    /**
     * OEM编号
     */
    @TableId
    private Long id;
    /**
     * 产品OEM编码
     */
    private String oemCode;
    /**
     * OEM名称
     */
    private String oemName;
    /**
     * 产品工厂名称
     */
    private String factoryName;
    /**
     * 产品工厂编码
     */
    private String factoryCode;
    /**
     * 工厂地址
     */
    private String factoryAddress;
    /**
     * 工厂联系人
     */
    private String factoryContact;
    /**
     * 工厂联系电话
     */
    private String factoryPhone;
    /**
     * 工厂邮箱
     */
    private String factoryEmail;
    /**
     * 生产能力
     */
    private String productionCapacity;
    /**
     * 认证资质
     */
    private String certification;
    /**
     * 合作开始日期
     */
    private LocalDateTime cooperationStartDate;
    /**
     * 合作结束日期
     */
    private LocalDateTime cooperationEndDate;
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    /**
     * 质量等级
     */
    private String qualityLevel;
    /**
     * 付款条款
     */
    private String paymentTerms;
    /**
     * 交货条款
     */
    private String deliveryTerms;
    /**
     * OEM LOGO URL
     */
    private String logoUrl;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;

}

