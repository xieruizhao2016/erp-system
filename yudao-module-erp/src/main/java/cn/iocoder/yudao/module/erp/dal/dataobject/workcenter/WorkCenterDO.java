package cn.iocoder.yudao.module.erp.dal.dataobject.workcenter;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 工作中心 DO
 *
 * @author 芋道源码
 */
@TableName("erp_work_center")
@KeySequence("erp_work_center_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkCenterDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 工作中心编号
     */
    private String workCenterNo;
    /**
     * 工作中心名称
     */
    private String workCenterName;
    /**
     * 描述
     */
    private String description;
    /**
     * 位置
     */
    private String location;
    /**
     * 负责人
     */
    private String responsiblePerson;
    /**
     * 状态：1-启用，2-停用
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}

