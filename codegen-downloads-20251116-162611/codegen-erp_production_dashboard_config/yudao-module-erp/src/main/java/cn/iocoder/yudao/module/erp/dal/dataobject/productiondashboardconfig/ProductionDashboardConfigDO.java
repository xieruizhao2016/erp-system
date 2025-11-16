package cn.iocoder.yudao.module.erp.dal.dataobject.productiondashboardconfig;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 看板配置 DO
 *
 * @author 芋道源码
 */
@TableName("erp_production_dashboard_config")
@KeySequence("erp_production_dashboard_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionDashboardConfigDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 配置名称
     */
    private String configName;
    /**
     * 配置类型：1-大屏，2-PC端，3-移动端
     */
    private Integer configType;
    /**
     * 屏幕分辨率
     */
    private String screenResolution;
    /**
     * 布局配置（JSON）
     */
    private String layoutConfig;
    /**
     * 组件配置（JSON）
     */
    private String componentConfig;
    /**
     * 数据刷新间隔（秒）
     */
    private Integer dataRefreshInterval;
    /**
     * 是否默认配置
     */
    private Boolean isDefault;
    /**
     * 是否启用
     */
    private Boolean isActive;


}