package cn.iocoder.yudao.module.erp.dal.dataobject.productbomitem;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.time.LocalDate;

/**
 * ERP BOM明细 DO
 *
 * @author 芋道源码
 */
@TableName("erp_product_bom_item")
@KeySequence("erp_product_bom_item_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBomItemDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * BOM ID
     */
    private Long bomId;
    /**
     * 父产品ID
     */
    private Long parentProductId;
    /**
     * 子产品ID
     */
    private Long childProductId;
    /**
     * 子产品名称
     */
    private String childProductName;
    /**
     * 子产品规格
     */
    private String childProductSpec;
    /**
     * 单位ID
     */
    private Long unitId;
    /**
     * 用量
     */
    private BigDecimal quantity;
    /**
     * 损耗率
     */
    private BigDecimal lossRate;
    /**
     * 有效用量
     */
    private BigDecimal effectiveQuantity;
    /**
     * 是否关键物料
     */
    private Boolean isKeyMaterial;
    /**
     * 是否替代料
     */
    private Boolean isAlternative;
    /**
     * 替代料组
     */
    private String alternativeGroup;
    /**
     * 位号
     */
    private Integer position;
    /**
     * 工序ID
     */
    private Long processId;
    /**
     * 备注
     */
    private String remark;


}