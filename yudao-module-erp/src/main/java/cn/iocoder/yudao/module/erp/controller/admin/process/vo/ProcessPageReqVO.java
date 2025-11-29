package cn.iocoder.yudao.module.erp.controller.admin.process.vo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ERP 工序分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProcessPageReqVO extends PageParam {

    @Schema(description = "工序编号", example = "P001")
    private String processNo;

    @Schema(description = "工序名称", example = "加工")
    private String processName;

    @Schema(description = "工序类型：1-加工，2-装配，3-检验，4-包装，5-其他", example = "1")
    private Integer processType;

    @Schema(description = "工序分类", example = "机加工")
    private String category;

    @Schema(description = "状态：1-启用，2-停用", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}



