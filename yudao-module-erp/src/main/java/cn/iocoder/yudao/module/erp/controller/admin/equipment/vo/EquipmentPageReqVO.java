package cn.iocoder.yudao.module.erp.controller.admin.equipment.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 设备台账分页 Request VO")
@Data
public class EquipmentPageReqVO extends PageParam {

    @Schema(description = "设备编号")
    private String equipmentNo;

    @Schema(description = "设备名称", example = "王五")
    private String equipmentName;

    @Schema(description = "设备类型", example = "1")
    private String equipmentType;

    @Schema(description = "设备型号")
    private String model;

    @Schema(description = "制造商")
    private String manufacturer;

    @Schema(description = "序列号")
    private String serialNumber;

    @Schema(description = "购置日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] purchaseDate;

    @Schema(description = "购置价格", example = "15916")
    private BigDecimal purchasePrice;

    @Schema(description = "设计寿命（年）")
    private Integer serviceLife;

    @Schema(description = "工作中心ID", example = "235")
    private Long workCenterId;

    @Schema(description = "设备位置")
    private String location;

    @Schema(description = "产能（小时/天）")
    private BigDecimal capacity;

    @Schema(description = "效率系数")
    private BigDecimal efficiencyRate;

    @Schema(description = "状态：1-正常，2-维修中，3-故障，4-报废", example = "2")
    private Integer status;

    @Schema(description = "责任人")
    private String responsiblePerson;

    @Schema(description = "技术规格")
    private String specification;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}