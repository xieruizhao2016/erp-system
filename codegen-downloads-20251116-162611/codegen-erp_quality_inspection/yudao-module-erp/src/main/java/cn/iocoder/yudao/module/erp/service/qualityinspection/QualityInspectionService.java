package cn.iocoder.yudao.module.erp.service.qualityinspection;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.qualityinspection.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.qualityinspection.QualityInspectionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 质检记录 Service 接口
 *
 * @author 芋道源码
 */
public interface QualityInspectionService {

    /**
     * 创建ERP 质检记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createQualityInspection(@Valid QualityInspectionSaveReqVO createReqVO);

    /**
     * 更新ERP 质检记录
     *
     * @param updateReqVO 更新信息
     */
    void updateQualityInspection(@Valid QualityInspectionSaveReqVO updateReqVO);

    /**
     * 删除ERP 质检记录
     *
     * @param id 编号
     */
    void deleteQualityInspection(Long id);

    /**
    * 批量删除ERP 质检记录
    *
    * @param ids 编号
    */
    void deleteQualityInspectionListByIds(List<Long> ids);

    /**
     * 获得ERP 质检记录
     *
     * @param id 编号
     * @return ERP 质检记录
     */
    QualityInspectionDO getQualityInspection(Long id);

    /**
     * 获得ERP 质检记录分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 质检记录分页
     */
    PageResult<QualityInspectionDO> getQualityInspectionPage(QualityInspectionPageReqVO pageReqVO);

}