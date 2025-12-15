package cn.iocoder.yudao.module.erp.dal.dataobject.stock.internalin;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 内部入库单 DO
 *
 * @author 开发团队
 */
@TableName("erp_stock_internal_in")
@KeySequence("erp_stock_internal_in_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpStockInternalInDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 入库单号
     */
    private String no;
    /**
     * 员工编号（关联 system_users.id）
     */
    private Long employeeId;
    /**
     * 部门编号（关联 system_dept.id）
     */
    private Long deptId;
    /**
     * 内部类型（1-部门调拨，2-员工领用，3-其他）
     */
    private Integer internalType;
    /**
     * 入库时间
     */
    private LocalDateTime inTime;
    /**
     * 合计数量
     */
    private BigDecimal totalCount;
    /**
     * 合计金额，单位：元
     */
    private BigDecimal totalPrice;
    /**
     * 状态（10-未审核，20-已审核）
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 附件 URL
     */
    private String fileUrl;


}