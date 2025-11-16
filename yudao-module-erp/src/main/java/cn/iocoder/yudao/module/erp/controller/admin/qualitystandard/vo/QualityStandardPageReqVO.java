package cn.iocoder.yudao.module.erp.controller.admin.qualitystandard.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 质检标准分页 Request VO")
@Data
public class QualityStandardPageReqVO extends PageParam {

    @Schema(description = "标准编号")
    private String standardNo;

    @Schema(description = "标准名称", example = "芋艿")
    private String standardName;

    @Schema(description = "产品ID", example = "29166")
    private Long productId;

    @Schema(description = "工序ID", example = "24223")
    private Long processId;

    @Schema(description = "检验类型：1-进料检验，2-过程检验，3-成品检验", example = "1")
    private Integer inspectionType;

    @Schema(description = "标准版本")
    private String standardVersion;

    @Schema(description = "AQL水平")
    private String aqlLevel;

    @Schema(description = "抽样方法")
    private String samplingMethod;

    @Schema(description = "状态：1-草稿，2-生效，3-失效", example = "2")
    private Integer status;

    @Schema(description = "标准描述", example = "随便")
    private String description;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}