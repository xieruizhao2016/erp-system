package cn.iocoder.yudao.module.erp.dal.dataobject.mrpresult;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.time.LocalDate;

/**
 * ERP MRP运算结果 DO
 *
 * @author 芋道源码
 */
@TableName("erp_mrp_result")
@KeySequence("erp_mrp_result_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MrpResultDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 运算批次号
     */
    private String runNo;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 仓库ID
     */
    private Long warehouseId;
    /**
     * 周期开始日期
     */
    private LocalDate periodStartDate;
    /**
     * 周期结束日期
     */
    private LocalDate periodEndDate;
    /**
     * 毛需求
     */
    private BigDecimal grossRequirement;
    /**
     * 计划接收量
     */
    private BigDecimal scheduledReceipts;
    /**
     * 现有库存
     */
    private BigDecimal onHandInventory;
    /**
     * 净需求
     */
    private BigDecimal netRequirement;
    /**
     * 计划订单接收量
     */
    private BigDecimal plannedOrderReceipts;
    /**
     * 计划订单发放量
     */
    private BigDecimal plannedOrderReleases;
    /**
     * 订单类型：1-生产订单，2-采购订单
     */
    private Integer orderType;
    /**
     * 批量规则：1-固定批量，2-按需，3-最小-最大
     */
    private Integer lotSizingRule;
    /**
     * 提前期（天）
     */
    private Integer leadTime;
    /**
     * 安全库存
     */
    private BigDecimal safetyStock;
    /**
     * 订单状态：1-建议，2-确认，3-下达
     */
    private Integer orderStatus;
    /**
     * 需求日期
     */
    private LocalDate dueDate;


}