package cn.iocoder.yudao.module.erp.service.stock-internal-out;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.stock-internal-out.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock-internal-out.ErpStockInternalOutDO;
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
    ${primaryColumn.javaType} createStockInternalOut(@Valid ErpStockInternalOutSaveReqVO createReqVO);

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
    void deleteStockInternalOut(${primaryColumn.javaType} id);

    /**
    * 批量删除内部出库单
    *
    * @param ids 编号
    */
    void deleteStockInternalOutListByIds(List<${primaryColumn.javaType}> ids);

    /**
     * 获得内部出库单
     *
     * @param id 编号
     * @return 内部出库单
     */
    ErpStockInternalOutDO getStockInternalOut(${primaryColumn.javaType} id);

    /**
     * 获得内部出库单分页
     *
     * @param pageReqVO 分页查询
     * @return 内部出库单分页
     */
    PageResult<ErpStockInternalOutDO> getStockInternalOutPage(ErpStockInternalOutPageReqVO pageReqVO);

}