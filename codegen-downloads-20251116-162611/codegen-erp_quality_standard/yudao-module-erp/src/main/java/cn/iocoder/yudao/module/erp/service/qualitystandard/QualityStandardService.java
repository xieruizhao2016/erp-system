package cn.iocoder.yudao.module.erp.service.qualitystandard;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.qualitystandard.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.qualitystandard.QualityStandardDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 质检标准 Service 接口
 *
 * @author 芋道源码
 */
public interface QualityStandardService {

    /**
     * 创建ERP 质检标准
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createQualityStandard(@Valid QualityStandardSaveReqVO createReqVO);

    /**
     * 更新ERP 质检标准
     *
     * @param updateReqVO 更新信息
     */
    void updateQualityStandard(@Valid QualityStandardSaveReqVO updateReqVO);

    /**
     * 删除ERP 质检标准
     *
     * @param id 编号
     */
    void deleteQualityStandard(Long id);

    /**
    * 批量删除ERP 质检标准
    *
    * @param ids 编号
    */
    void deleteQualityStandardListByIds(List<Long> ids);

    /**
     * 获得ERP 质检标准
     *
     * @param id 编号
     * @return ERP 质检标准
     */
    QualityStandardDO getQualityStandard(Long id);

    /**
     * 获得ERP 质检标准分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 质检标准分页
     */
    PageResult<QualityStandardDO> getQualityStandardPage(QualityStandardPageReqVO pageReqVO);

}