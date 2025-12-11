package cn.iocoder.yudao.module.erp.dal.redis.no;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.iocoder.yudao.module.erp.dal.redis.RedisKeyConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalDate;


/**
 * Erp 订单序号的 Redis DAO
 *
 * @author HUIHUI
 */
@Repository
public class ErpNoRedisDAO {

    /**
     * 其它入库 {@link cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpStockInDO}
     */
    public static final String STOCK_IN_NO_PREFIX = "QTRK";
    /**
     * 其它出库 {@link cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpStockOutDO}
     */
    public static final String STOCK_OUT_NO_PREFIX = "QCKD";

    /**
     * 库存调拨 {@link cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpStockMoveDO}
     */
    public static final String STOCK_MOVE_NO_PREFIX = "QCDB";

    /**
     * 库存盘点 {@link cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpStockCheckDO}
     */
    public static final String STOCK_CHECK_NO_PREFIX = "QCPD";

    /**
     * 销售订单 {@link cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderDO}
     */
    public static final String SALE_ORDER_NO_PREFIX = "XSDD";
    /**
     * 销售出库 {@link cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOutDO}
     */
    public static final String SALE_OUT_NO_PREFIX = "XSCK";
    /**
     * 销售退货 {@link cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleReturnDO}
     */
    public static final String SALE_RETURN_NO_PREFIX = "XSTH";

    /**
     * 采购订单 {@link cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderDO}
     */
    public static final String PURCHASE_ORDER_NO_PREFIX = "CGDD";
    /**
     * 采购入库 {@link cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseInDO}
     */
    public static final String PURCHASE_IN_NO_PREFIX = "CGRK";
    /**
     * 采购退货 {@link cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseReturnDO}
     */
    public static final String PURCHASE_RETURN_NO_PREFIX = "CGTH";

    /**
     * 生产订单 {@link cn.iocoder.yudao.module.erp.dal.dataobject.productionorder.ProductionOrderDO}
     */
    public static final String PRODUCTION_ORDER_NO_PREFIX = "SCDD";
    /**
     * 工单 {@link cn.iocoder.yudao.module.erp.dal.dataobject.workorder.WorkOrderDO}
     */
    public static final String WORK_ORDER_NO_PREFIX = "GD";
    /**
     * 成本单 {@link cn.iocoder.yudao.module.erp.dal.dataobject.costactual.CostActualDO}
     */
    public static final String COST_ACTUAL_NO_PREFIX = "CBD";
    /**
     * 排程单 {@link cn.iocoder.yudao.module.erp.dal.dataobject.productionschedule.ProductionScheduleDO}
     */
    public static final String PRODUCTION_SCHEDULE_NO_PREFIX = "PCD";
    /**
     * 质检单 {@link cn.iocoder.yudao.module.erp.dal.dataobject.qualityinspection.QualityInspectionDO}
     */
    public static final String QUALITY_INSPECTION_NO_PREFIX = "ZJD";

    /**
     * 工序 {@link cn.iocoder.yudao.module.erp.dal.dataobject.process.ProcessDO}
     */
    public static final String PROCESS_NO_PREFIX = "GX";

    /**
     * 工艺路线 {@link cn.iocoder.yudao.module.erp.dal.dataobject.processroute.ProcessRouteDO}
     */
    public static final String PROCESS_ROUTE_NO_PREFIX = "GYLX";

    /**
     * 付款单 {@link cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpFinancePaymentDO}
     */
    public static final String FINANCE_PAYMENT_NO_PREFIX = "FKD";
    /**
     * 收款单 {@link cn.iocoder.yudao.module.erp.dal.dataobject.finance.ErpFinanceReceiptDO}
     */
    public static final String FINANCE_RECEIPT_NO_PREFIX = "SKD";
    /**
     * 应收账款 {@link cn.iocoder.yudao.module.erp.dal.dataobject.finance.receivable.ErpFinanceReceivableDO}
     */
    public static final String FINANCE_RECEIVABLE_NO_PREFIX = "YSZK";
    /**
     * 应付账款 {@link cn.iocoder.yudao.module.erp.dal.dataobject.finance.payable.ErpFinancePayableDO}
     */
    public static final String FINANCE_PAYABLE_NO_PREFIX = "YFZK";
    /**
     * 内部入库 {@link cn.iocoder.yudao.module.erp.dal.dataobject.stock.internalin.ErpStockInternalInDO}
     */
    public static final String STOCK_INTERNAL_IN_NO_PREFIX = "NBRK";
    /**
     * 内部出库 {@link cn.iocoder.yudao.module.erp.dal.dataobject.stock.internalout.ErpStockInternalOutDO}
     */
    public static final String STOCK_INTERNAL_OUT_NO_PREFIX = "NBCK";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成序号，使用当前日期，格式为 {PREFIX} + yyyyMMdd + 6 位自增
     * 例如说：QTRK 202109 000001 （没有中间空格）
     *
     * @param prefix 前缀
     * @return 序号
     */
    public String generate(String prefix) {
        // 递增序号
        String noPrefix = prefix + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN);
        String key = RedisKeyConstants.NO + noPrefix;
        Long no = stringRedisTemplate.opsForValue().increment(key);
        // 设置过期时间
        stringRedisTemplate.expire(key, Duration.ofDays(1L));
        return noPrefix + String.format("%06d", no);
    }

}
