package cn.iocoder.yudao.module.erp.dal.dataobject.productsku;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * ERP 产品和SKU关联 DO
 *
 * @author 芋道源码
 */
@TableName("erp_product_sku_relation")
@KeySequence("erp_product_sku_relation_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSkuRelationDO extends BaseDO {

    /**
     * 关联编号
     */
    @TableId
    private Long id;

    /**
     * 产品编号
     *
     * 关联 {@link cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO#getId()}
     */
    private Long productId;

    /**
     * SKU编号
     *
     * 关联 {@link ProductSkuDO#getId()}
     */
    private Long skuId;

}

