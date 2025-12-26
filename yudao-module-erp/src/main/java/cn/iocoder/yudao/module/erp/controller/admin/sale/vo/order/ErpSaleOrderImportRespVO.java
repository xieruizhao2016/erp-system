package cn.iocoder.yudao.module.erp.controller.admin.sale.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 销售订单导入 Response VO")
@Data
@Builder
public class ErpSaleOrderImportRespVO {

    @Schema(description = "创建成功的订单号数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> createOrderNos;

    @Schema(description = "更新成功的订单号数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> updateOrderNos;

    @Schema(description = "导入失败的订单集合，key 为订单号，value 为失败原因", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, String> failureOrderNos;

}

