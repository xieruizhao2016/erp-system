package cn.iocoder.yudao.module.erp.controller.admin.equipment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 设备台账新增/修改 Request VO")
@Data
public class EquipmentSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "6378")
    private Long id;

    @Schema(description = "设备编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "设备编号不能为空")
    private String equipmentNo;

    @Schema(description = "设备名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "设备名称不能为空")
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
    private LocalDate purchaseDate;

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

}