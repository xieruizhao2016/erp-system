package cn.iocoder.yudao.module.erp.service.stock.internalout;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.stock.internalout.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.internalout.ErpStockInternalOutDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 内部出库单 Service 接口
 *
 * @author 开发团队
 */
public interface ErpStockInternalOutService {

    /**
     * 创建内部出库单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createStockInternalOut(@Valid ErpStockInternalOutSaveReqVO createReqVO);

    /**
     * 更新内部出库单
     *
     * @param updateReqVO 更新信息
     */
    void updateStockInternalOut(@Valid ErpStockInternalOutSaveReqVO updateReqVO);

    /**
     * 删除内部出库单
     *
     * @param id 编号
     */
    void deleteStockInternalOut(Long id);

    /**
    * 批量删除内部出库单
    *
    * @param ids 编号
    */
    void deleteStockInternalOutListByIds(List<Long> ids);

    /**
     * 获得内部出库单
     *
     * @param id 编号
     * @return 内部出库单
     */
    ErpStockInternalOutDO getStockInternalOut(Long id);

    /**
     * 获得内部出库单分页
     *
     * @param pageReqVO 分页查询
     * @return 内部出库单分页
     */
    PageResult<ErpStockInternalOutDO> getStockInternalOutPage(ErpStockInternalOutPageReqVO pageReqVO);

    /**
     * 更新内部出库单的状态
     *
     * @param id 编号
     * @param status 状态
     */
    void updateStockInternalOutStatus(Long id, Integer status);

}