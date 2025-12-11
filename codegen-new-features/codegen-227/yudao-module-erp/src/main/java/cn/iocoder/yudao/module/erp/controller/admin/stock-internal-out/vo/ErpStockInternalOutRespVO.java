package cn.iocoder.yudao.module.erp.controller.admin.stock-internal-out.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 内部出库单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ErpStockInternalOutRespVO {

    @Schema(description = "出库单号")
    @ExcelProperty("出库单号")
    private String no;

    @Schema(description = "员工编号（关联 system_users.id）")
    @ExcelProperty("员工编号（关联 system_users.id）")
    private Long employeeId;

    @Schema(description = "部门编号（关联 system_dept.id）")
    @ExcelProperty("部门编号（关联 system_dept.id）")
    private Long deptId;

    @Schema(description = "内部类型（1-部门调拨，2-员工领用，3-其他）")
    @ExcelProperty("内部类型（1-部门调拨，2-员工领用，3-其他）")
    private Integer internalType;

    @Schema(description = "出库时间")
    @ExcelProperty("出库时间")
    private LocalDateTime outTime;

    @Schema(description = "合计数量")
    @ExcelProperty("合计数量")
    private BigDecimal totalCount;

    @Schema(description = "合计金额，单位：元")
    @ExcelProperty("合计金额，单位：元")
    private BigDecimal totalPrice;

    @Schema(description = "状态（10-未审核，20-已审核）")
    @ExcelProperty("状态（10-未审核，20-已审核）")
    private Integer status;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "附件 URL")
    @ExcelProperty("附件 URL")
    private String fileUrl;

}