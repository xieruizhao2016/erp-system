package cn.iocoder.yudao.module.erp.dal.dataobject.processroute;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 工艺路线主 DO
 *
 * @author 芋道源码
 */
@TableName("erp_process_route")
@KeySequence("erp_process_route_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessRouteDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 工艺路线编号
     */
    private String routeNo;
    /**
     * 工艺路线名称
     */
    private String routeName;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 版本号
     */
    private String version;
    /**
     * 标准周期时间（分钟）
     */
    private Integer standardCycleTime;
    /**
     * 标准人工成本
     */
    private BigDecimal standardLaborCost;
    /**
     * 标准制造费用
     */
    private BigDecimal standardOverheadCost;
    /**
     * 状态：1-草稿，2-生效，3-失效
     */
    private Integer status;


}