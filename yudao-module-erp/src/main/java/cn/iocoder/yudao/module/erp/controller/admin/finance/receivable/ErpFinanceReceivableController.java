package cn.iocoder.yudao.module.erp.controller.admin.finance.receivable;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.erp.controller.admin.finance.receivable.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.receivable.ErpFinanceReceivableDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpCustomerDO;
import cn.iocoder.yudao.module.erp.service.finance.receivable.ErpFinanceReceivableService;
import cn.iocoder.yudao.module.erp.service.sale.ErpCustomerService;
import cn.iocoder.yudao.module.erp.service.sale.ErpSaleOrderService;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderDO;
import cn.iocoder.yudao.module.erp.dal.mysql.sale.ErpSaleOrderMapper;
import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - 应收账款")
@RestController
@RequestMapping("/erp/finance-receivable")
@Validated
public class ErpFinanceReceivableController {

    @Resource
    private ErpFinanceReceivableService financeReceivableService;

    @Resource
    private ErpCustomerService customerService;

    @Resource
    private ErpSaleOrderService saleOrderService;

    @Resource
    private ErpSaleOrderMapper saleOrderMapper;

    @PostMapping("/create")
    @Operation(summary = "创建应收账款", description = "支持手动创建初始应收账款或没有关联订单的借贷记录。如果不提供单据号，将自动生成；如果不提供余额，将自动计算（余额=应收金额-已收金额）")
    @PreAuthorize("@ss.hasPermission('erp:finance-receivable:create')")
    public CommonResult<Long> createFinanceReceivable(@Valid @RequestBody ErpFinanceReceivableSaveReqVO createReqVO) {
        return success(financeReceivableService.createFinanceReceivable(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新应收账款")
    @PreAuthorize("@ss.hasPermission('erp:finance-receivable:update')")
    public CommonResult<Boolean> updateFinanceReceivable(@Valid @RequestBody ErpFinanceReceivableSaveReqVO updateReqVO) {
        financeReceivableService.updateFinanceReceivable(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除应收账款")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:finance-receivable:delete')")
    public CommonResult<Boolean> deleteFinanceReceivable(@RequestParam("id") Long id) {
        financeReceivableService.deleteFinanceReceivable(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除应收账款")
                @PreAuthorize("@ss.hasPermission('erp:finance-receivable:delete')")
    public CommonResult<Boolean> deleteFinanceReceivableList(@RequestParam("ids") List<Long> ids) {
        financeReceivableService.deleteFinanceReceivableListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得应收账款")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:finance-receivable:query')")
    public CommonResult<ErpFinanceReceivableRespVO> getFinanceReceivable(@RequestParam("id") Long id) {
        ErpFinanceReceivableDO financeReceivable = financeReceivableService.getFinanceReceivable(id);
        ErpFinanceReceivableRespVO respVO = BeanUtils.toBean(financeReceivable, ErpFinanceReceivableRespVO.class);
        // 填充客户名称
        if (respVO != null && respVO.getCustomerId() != null) {
            ErpCustomerDO customer = customerService.getCustomer(respVO.getCustomerId());
            if (customer != null) {
                respVO.setCustomerName(customer.getName());
            }
        }
        // 填充订单单号
        if (respVO != null && respVO.getOrderId() != null) {
            ErpSaleOrderDO order = saleOrderService.getSaleOrder(respVO.getOrderId());
            if (order != null) {
                respVO.setOrderNo(order.getNo());
            }
        }
        // 如果订单号为空，尝试从remark字段提取
        if (respVO != null && (respVO.getOrderNo() == null || respVO.getOrderNo().isEmpty())) {
            String orderNo = extractOrderNoFromRemark(respVO.getRemark());
            if (orderNo != null && !orderNo.isEmpty()) {
                respVO.setOrderNo(orderNo);
            }
        }
        // 设置是否自动生成
        if (respVO != null) {
            respVO.setAutoGenerated(isAutoGenerated(respVO.getOrderId(), respVO.getRemark()));
        }
        return success(respVO);
    }

    /**
     * 从remark字段中提取订单号
     * remark格式示例："自动生成自销售订单：XS20260101001" 或 "自动生成自销售订单: XSDD20260104000001"
     */
    private String extractOrderNoFromRemark(String remark) {
        if (remark == null || remark.isEmpty()) {
            return null;
        }
        // 匹配 "自动生成自采购订单：" 或 "自动生成自销售订单：" 后面的订单号
        // 支持中文冒号和英文冒号，以及可能的空格
        String pattern = "自动生成自(?:采购|销售)订单[：:](?:\\s*)([^（\\s]+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(remark);
        if (m.find()) {
            String orderNo = m.group(1);
            // 去除可能的尾随空格
            if (orderNo != null) {
                orderNo = orderNo.trim();
            }
            return orderNo;
        }
        return null;
    }

    /**
     * 判断是否自动生成
     * 如果 orderId 不为空且 remark 包含"自动生成"，则认为是自动生成的
     */
    private Boolean isAutoGenerated(Long orderId, String remark) {
        if (orderId != null && remark != null && remark.contains("自动生成")) {
            return true;
        }
        return false;
    }

    @GetMapping("/page")
    @Operation(summary = "获得应收账款分页")
    @PreAuthorize("@ss.hasPermission('erp:finance-receivable:query')")
    public CommonResult<PageResult<ErpFinanceReceivableRespVO>> getFinanceReceivablePage(@Valid ErpFinanceReceivablePageReqVO pageReqVO) {
        PageResult<ErpFinanceReceivableDO> pageResult = financeReceivableService.getFinanceReceivablePage(pageReqVO);
        // 获取客户信息
        Set<Long> customerIds = convertSet(pageResult.getList(), ErpFinanceReceivableDO::getCustomerId, 
                receivable -> receivable.getCustomerId() != null);
        Map<Long, ErpCustomerDO> customerMap = CollUtil.isEmpty(customerIds) ? Collections.emptyMap() :
                customerService.getCustomerMap(customerIds);
        // 获取订单信息
        Set<Long> orderIds = convertSet(pageResult.getList(), ErpFinanceReceivableDO::getOrderId, 
                receivable -> receivable.getOrderId() != null);
        Map<Long, ErpSaleOrderDO> orderMap = new HashMap<>();
        if (CollUtil.isNotEmpty(orderIds)) {
            // 批量查询订单
            List<ErpSaleOrderDO> orders = saleOrderMapper.selectBatchIds(orderIds);
            if (CollUtil.isNotEmpty(orders)) {
                for (ErpSaleOrderDO order : orders) {
                    if (order != null && order.getId() != null && order.getNo() != null) {
                        orderMap.put(order.getId(), order);
                    }
                }
            }
        }
        // 转换为 VO 并填充客户名称和订单单号
        return success(BeanUtils.toBean(pageResult, ErpFinanceReceivableRespVO.class, receivable -> {
            MapUtils.findAndThen(customerMap, receivable.getCustomerId(), customer -> {
                receivable.setCustomerName(customer.getName());
            });
            // 优先从订单表获取订单号
            MapUtils.findAndThen(orderMap, receivable.getOrderId(), order -> {
                receivable.setOrderNo(order.getNo());
            });
            // 如果订单号为空，尝试从remark字段提取
            if (receivable.getOrderNo() == null || receivable.getOrderNo().isEmpty()) {
                String orderNo = extractOrderNoFromRemark(receivable.getRemark());
                if (orderNo != null && !orderNo.isEmpty()) {
                    receivable.setOrderNo(orderNo);
                }
            }
            // 设置是否自动生成
            receivable.setAutoGenerated(isAutoGenerated(receivable.getOrderId(), receivable.getRemark()));
        }));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出应收账款 Excel")
    @PreAuthorize("@ss.hasPermission('erp:finance-receivable:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFinanceReceivableExcel(@Valid ErpFinanceReceivablePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpFinanceReceivableDO> list = financeReceivableService.getFinanceReceivablePage(pageReqVO).getList();
        // 获取客户信息
        Set<Long> customerIds = convertSet(list, ErpFinanceReceivableDO::getCustomerId, 
                receivable -> receivable.getCustomerId() != null);
        Map<Long, ErpCustomerDO> customerMap = CollUtil.isEmpty(customerIds) ? Collections.emptyMap() :
                customerService.getCustomerMap(customerIds);
        // 获取订单信息
        Set<Long> orderIds = convertSet(list, ErpFinanceReceivableDO::getOrderId, 
                receivable -> receivable.getOrderId() != null);
        Map<Long, ErpSaleOrderDO> orderMap = new HashMap<>();
        if (CollUtil.isNotEmpty(orderIds)) {
            // 批量查询订单
            List<ErpSaleOrderDO> orders = saleOrderMapper.selectBatchIds(orderIds);
            if (CollUtil.isNotEmpty(orders)) {
                for (ErpSaleOrderDO order : orders) {
                    if (order != null && order.getId() != null && order.getNo() != null) {
                        orderMap.put(order.getId(), order);
                    }
                }
            }
        }
        // 转换为 VO 并填充客户名称和订单单号
        List<ErpFinanceReceivableRespVO> respList = BeanUtils.toBean(list, ErpFinanceReceivableRespVO.class, receivable -> {
            MapUtils.findAndThen(customerMap, receivable.getCustomerId(), customer -> {
                receivable.setCustomerName(customer.getName());
            });
            // 优先从订单表获取订单号
            MapUtils.findAndThen(orderMap, receivable.getOrderId(), order -> {
                receivable.setOrderNo(order.getNo());
            });
            // 如果订单号为空，尝试从remark字段提取
            if (receivable.getOrderNo() == null || receivable.getOrderNo().isEmpty()) {
                String orderNo = extractOrderNoFromRemark(receivable.getRemark());
                if (orderNo != null && !orderNo.isEmpty()) {
                    receivable.setOrderNo(orderNo);
                }
            }
            // 设置是否自动生成
            receivable.setAutoGenerated(isAutoGenerated(receivable.getOrderId(), receivable.getRemark()));
        });
        // 导出 Excel
        ExcelUtils.write(response, "应收账款.xls", "数据", ErpFinanceReceivableRespVO.class, respList);
    }

}