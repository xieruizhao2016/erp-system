package cn.iocoder.yudao.module.erp.controller.admin.qualityitem.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 质检项目分页 Request VO")
@Data
public class QualityItemPageReqVO extends PageParam {

    @Schema(description = "标准ID", example = "19958")
    private Long standardId;

    @Schema(description = "项目编号")
    private String itemNo;

    @Schema(description = "项目名称", example = "张三")
    private String itemName;

    @Schema(description = "项目类型：1-定性，2-定量", example = "1")
    private Integer itemType;

    @Schema(description = "检验方法")
    private String testMethod;

    @Schema(description = "标准值")
    private String standardValue;

    @Schema(description = "公差范围")
    private String tolerance;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "是否关键项")
    private Boolean isKeyItem;

    @Schema(description = "检验序号")
    private Integer sequence;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}