package cn.iocoder.yudao.module.erp.service.stock-internal-in;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.stock-internal-in.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock-internal-in.ErpStockInternalInDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 内部入库单 Service 接口
 *
 * @author 开发团队
 */
public interface ErpStockInternalInService {

    /**
     * 创建内部入库单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    ${primaryColumn.javaType} createStockInternalIn(@Valid ErpStockInternalInSaveReqVO createReqVO);

    /**
     * 更新内部入库单
     *
     * @param updateReqVO 更新信息
     */
    void updateStockInternalIn(@Valid ErpStockInternalInSaveReqVO updateReqVO);

    /**
     * 删除内部入库单
     *
     * @param id 编号
     */
    void deleteStockInternalIn(${primaryColumn.javaType} id);

    /**
    * 批量删除内部入库单
    *
    * @param ids 编号
    */
    void deleteStockInternalInListByIds(List<${primaryColumn.javaType}> ids);

    /**
     * 获得内部入库单
     *
     * @param id 编号
     * @return 内部入库单
     */
    ErpStockInternalInDO getStockInternalIn(${primaryColumn.javaType} id);

    /**
     * 获得内部入库单分页
     *
     * @param pageReqVO 分页查询
     * @return 内部入库单分页
     */
    PageResult<ErpStockInternalInDO> getStockInternalInPage(ErpStockInternalInPageReqVO pageReqVO);

}