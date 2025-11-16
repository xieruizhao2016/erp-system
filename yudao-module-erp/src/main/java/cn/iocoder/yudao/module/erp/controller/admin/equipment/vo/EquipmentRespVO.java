package cn.iocoder.yudao.module.erp.controller.admin.equipment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 设备台账 Response VO")
@Data
@ExcelIgnoreUnannotated
public class EquipmentRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "6378")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "设备编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("设备编号")
    private String equipmentNo;

    @Schema(description = "设备名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("设备名称")
    private String equipmentName;

    @Schema(description = "设备类型", example = "1")
    @ExcelProperty("设备类型")
    private String equipmentType;

    @Schema(description = "设备型号")
    @ExcelProperty("设备型号")
    private String model;

    @Schema(description = "制造商")
    @ExcelProperty("制造商")
    private String manufacturer;

    @Schema(description = "序列号")
    @ExcelProperty("序列号")
    private String serialNumber;

    @Schema(description = "购置日期")
    @ExcelProperty("购置日期")
    private LocalDate purchaseDate;

    @Schema(description = "购置价格", example = "15916")
    @ExcelProperty("购置价格")
    private BigDecimal purchasePrice;

    @Schema(description = "设计寿命（年）")
    @ExcelProperty("设计寿命（年）")
    private Integer serviceLife;

    @Schema(description = "工作中心ID", example = "235")
    @ExcelProperty("工作中心ID")
    private Long workCenterId;

    @Schema(description = "设备位置")
    @ExcelProperty("设备位置")
    private String location;

    @Schema(description = "产能（小时/天）")
    @ExcelProperty("产能（小时/天）")
    private BigDecimal capacity;

    @Schema(description = "效率系数")
    @ExcelProperty("效率系数")
    private BigDecimal efficiencyRate;

    @Schema(description = "状态：1-正常，2-维修中，3-故障，4-报废", example = "2")
    @ExcelProperty("状态：1-正常，2-维修中，3-故障，4-报废")
    private Integer status;

    @Schema(description = "责任人")
    @ExcelProperty("责任人")
    private String responsiblePerson;

    @Schema(description = "技术规格")
    @ExcelProperty("技术规格")
    private String specification;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}