package cn.iocoder.yudao.module.erp.controller.admin.finance;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import cn.iocoder.yudao.module.erp.controller.admin.finance.vo.receipt.ErpFinanceReceiptPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.finance.vo.receipt.ErpFinanceReceiptRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.finance.vo.receipt.ErpFinanceReceiptSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpAccountDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpFinanceReceiptDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpFinanceReceiptItemDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpCustomerDO;
import cn.iocoder.yudao.module.erp.service.finance.ErpAccountService;
import cn.iocoder.yudao.module.erp.service.finance.ErpFinanceReceiptService;
import cn.iocoder.yudao.module.erp.service.sale.ErpCustomerService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;

@Tag(name = "管理后台 - ERP 收款单")
@RestController
@RequestMapping("/erp/finance-receipt")
@Validated
@Slf4j
public class ErpFinanceReceiptController {

    @Resource
    private ErpFinanceReceiptService financeReceiptService;
    @Resource
    private ErpCustomerService customerService;
    @Resource
    private ErpAccountService accountService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建收款单")
    @PreAuthorize("@ss.hasPermission('erp:finance-receipt:create')")
    public CommonResult<Long> createFinanceReceipt(@Valid @RequestBody ErpFinanceReceiptSaveReqVO createReqVO) {
        return success(financeReceiptService.createFinanceReceipt(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新收款单")
    @PreAuthorize("@ss.hasPermission('erp:finance-receipt:update')")
    public CommonResult<Boolean> updateFinanceReceipt(@Valid @RequestBody ErpFinanceReceiptSaveReqVO updateReqVO) {
        financeReceiptService.updateFinanceReceipt(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新收款单的状态")
    @PreAuthorize("@ss.hasPermission('erp:finance-receipt:update-status')")
    public CommonResult<Boolean> updateFinanceReceiptStatus(@RequestParam("id") Long id,
                                                           @RequestParam("status") Integer status) {
        financeReceiptService.updateFinanceReceiptStatus(id, status);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除收款单")
    @Parameter(name = "ids", description = "编号数组", required = true)
    @PreAuthorize("@ss.hasPermission('erp:finance-receipt:delete')")
    public CommonResult<Boolean> deleteFinanceReceipt(@RequestParam("ids") List<Long> ids) {
        financeReceiptService.deleteFinanceReceipt(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得收款单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:finance-receipt:query')")
    public CommonResult<ErpFinanceReceiptRespVO> getFinanceReceipt(@RequestParam("id") Long id) {
        ErpFinanceReceiptDO receipt = financeReceiptService.getFinanceReceipt(id);
        if (receipt == null) {
            return success(null);
        }
        List<ErpFinanceReceiptItemDO> receiptItemList = financeReceiptService.getFinanceReceiptItemListByReceiptId(id);
        return success(BeanUtils.toBean(receipt, ErpFinanceReceiptRespVO.class, financeReceiptVO ->
                financeReceiptVO.setItems(BeanUtils.toBean(receiptItemList, ErpFinanceReceiptRespVO.Item.class))));
    }

    @GetMapping("/page")
    @Operation(summary = "获得收款单分页")
    @PreAuthorize("@ss.hasPermission('erp:finance-receipt:query')")
    public CommonResult<PageResult<ErpFinanceReceiptRespVO>> getFinanceReceiptPage(@Valid ErpFinanceReceiptPageReqVO pageReqVO) {
        try {
            PageResult<ErpFinanceReceiptDO> pageResult = financeReceiptService.getFinanceReceiptPage(pageReqVO);
            return success(buildFinanceReceiptVOPageResult(pageResult));
        } catch (Exception e) {
            log.error("获取收款单分页失败", e);
            throw e;
        }
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出收款单 Excel")
    @PreAuthorize("@ss.hasPermission('erp:finance-receipt:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFinanceReceiptExcel(@Valid ErpFinanceReceiptPageReqVO pageReqVO,
                                         HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpFinanceReceiptRespVO> list = buildFinanceReceiptVOPageResult(financeReceiptService.getFinanceReceiptPage(pageReqVO)).getList();
        // 导出 Excel
        ExcelUtils.write(response, "收款单.xls", "数据", ErpFinanceReceiptRespVO.class, list);
    }

    private PageResult<ErpFinanceReceiptRespVO> buildFinanceReceiptVOPageResult(PageResult<ErpFinanceReceiptDO> pageResult) {
        try {
            if (CollUtil.isEmpty(pageResult.getList())) {
                return PageResult.empty(pageResult.getTotal());
            }
            
            // 1.1 收款项
            Set<Long> receiptIds = convertSet(pageResult.getList(), ErpFinanceReceiptDO::getId, id -> id != null);
            List<ErpFinanceReceiptItemDO> receiptItemList = CollUtil.isEmpty(receiptIds) 
                ? Collections.emptyList() 
                : financeReceiptService.getFinanceReceiptItemListByReceiptIds(receiptIds);
            
            // 过滤掉 receiptId 为 null 的项
            receiptItemList = receiptItemList != null 
                ? receiptItemList.stream().filter(item -> item.getReceiptId() != null).collect(Collectors.toList())
                : Collections.emptyList();
            
            Map<Long, List<ErpFinanceReceiptItemDO>> financeReceiptItemMap = convertMultiMap(receiptItemList, ErpFinanceReceiptItemDO::getReceiptId);
            
            // 1.2 客户信息
            Set<Long> customerIds = convertSet(pageResult.getList(), ErpFinanceReceiptDO::getCustomerId, id -> id != null);
            Map<Long, ErpCustomerDO> customerMap = CollUtil.isEmpty(customerIds) 
                ? Collections.emptyMap() 
                : customerService.getCustomerMap(customerIds);
            
            // 1.3 结算账户信息
            Set<Long> accountIds = convertSet(pageResult.getList(), ErpFinanceReceiptDO::getAccountId, id -> id != null);
            Map<Long, ErpAccountDO> accountMap = CollUtil.isEmpty(accountIds) 
                ? Collections.emptyMap() 
                : accountService.getAccountMap(accountIds);
            
            // 1.4 管理员信息
            java.util.Set<Long> userIds = new java.util.HashSet<>();
            for (ErpFinanceReceiptDO receipt : pageResult.getList()) {
                // 处理 creator
                if (receipt.getCreator() != null && !receipt.getCreator().isEmpty()) {
                    try {
                        Long creatorId = NumberUtils.parseLong(receipt.getCreator());
                        if (creatorId != null) {
                            userIds.add(creatorId);
                        }
                    } catch (Exception e) {
                        log.warn("解析 creator 失败: {}", receipt.getCreator(), e);
                    }
                }
                // 处理 financeUserId
                if (receipt.getFinanceUserId() != null) {
                    userIds.add(receipt.getFinanceUserId());
                }
            }
            Map<Long, AdminUserRespDTO> userMap = CollUtil.isEmpty(userIds) 
                ? Collections.emptyMap() 
                : adminUserApi.getUserMap(userIds);
            
            // 2. 开始拼接
            return BeanUtils.toBean(pageResult, ErpFinanceReceiptRespVO.class, receipt -> {
                // 设置收款项
                List<ErpFinanceReceiptItemDO> items = financeReceiptItemMap.get(receipt.getId());
                if (items != null) {
                    receipt.setItems(BeanUtils.toBean(items, ErpFinanceReceiptRespVO.Item.class));
                } else {
                    receipt.setItems(Collections.emptyList());
                }
                
                // 设置客户名称
                if (receipt.getCustomerId() != null) {
                    MapUtils.findAndThen(customerMap, receipt.getCustomerId(), customer -> {
                        if (customer != null && customer.getName() != null) {
                            receipt.setCustomerName(customer.getName());
                        }
                    });
                }
                
                // 设置账户名称
                if (receipt.getAccountId() != null) {
                    MapUtils.findAndThen(accountMap, receipt.getAccountId(), account -> {
                        if (account != null && account.getName() != null) {
                            receipt.setAccountName(account.getName());
                        }
                    });
                }
                
                // 设置创建人名称
                if (receipt.getCreator() != null && !receipt.getCreator().isEmpty()) {
                    try {
                        Long creatorId = NumberUtils.parseLong(receipt.getCreator());
                        if (creatorId != null) {
                            MapUtils.findAndThen(userMap, creatorId, user -> {
                                if (user != null && user.getNickname() != null) {
                                    receipt.setCreatorName(user.getNickname());
                                }
                            });
                        }
                    } catch (Exception e) {
                        log.warn("解析 creator 失败: {}", receipt.getCreator(), e);
                    }
                }
                
                // 设置财务人员名称
                if (receipt.getFinanceUserId() != null) {
                    MapUtils.findAndThen(userMap, receipt.getFinanceUserId(), user -> {
                        if (user != null && user.getNickname() != null) {
                            receipt.setFinanceUserName(user.getNickname());
                        }
                    });
                }
            });
        } catch (Exception e) {
            log.error("构建收款单 VO 分页结果失败", e);
            return PageResult.empty(pageResult != null ? pageResult.getTotal() : 0);
        }
    }

}
