package cn.iocoder.yudao.module.erp.controller.admin.productoem.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ERP 产品OEM分页 Request VO")
@Data
public class ProductOemPageReqVO extends PageParam {

    @Schema(description = "产品OEM编码", example = "OEM-001")
    private String oemCode;

    @Schema(description = "OEM名称", example = "富士康OEM")
    private String oemName;

    @Schema(description = "产品工厂名称", example = "富士康科技集团")
    private String factoryName;

    @Schema(description = "产品工厂编码", example = "FOXCONN-001")
    private String factoryCode;

    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}

