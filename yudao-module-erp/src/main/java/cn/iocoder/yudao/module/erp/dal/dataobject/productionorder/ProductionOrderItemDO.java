package cn.iocoder.yudao.module.erp.dal.dataobject.productionorder;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * ERP 生产订单项 DO
 *
 * @author 芋道源码
 */
@TableName("erp_production_order_items")
@KeySequence("erp_production_order_items_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionOrderItemDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 生产订单编号
     *
     * 关联 {@link ProductionOrderDO#getId()}
     */
    private Long productionOrderId;
    /**
     * 产品编号
     *
     * 关联 {@link ErpProductDO#getId()}
     */
    private Long productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品规格
     */
    private String productSpec;
    /**
     * 单位编号
     */
    private Long unitId;
    /**
     * 生产数量
     */
    private BigDecimal quantity;
    /**
     * 已完成数量
     */
    private BigDecimal completedQuantity;
    /**
     * 备注
     */
    private String remark;

}

