package cn.iocoder.yudao.module.erp.service.process;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.process.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 工序 Service 接口
 *
 * @author 芋道源码
 */
public interface ProcessService {

    /**
     * 创建ERP 工序
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProcess(@Valid ProcessSaveReqVO createReqVO);

    /**
     * 更新ERP 工序
     *
     * @param updateReqVO 更新信息
     */
    void updateProcess(@Valid ProcessSaveReqVO updateReqVO);

    /**
     * 删除ERP 工序
     *
     * @param id 编号
     */
    void deleteProcess(Long id);

    /**
    * 批量删除ERP 工序
    *
    * @param ids 编号
    */
    void deleteProcessListByIds(List<Long> ids);

    /**
     * 获得ERP 工序
     *
     * @param id 编号
     * @return ERP 工序
     */
    ProcessDO getProcess(Long id);

    /**
     * 获得ERP 工序分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 工序分页
     */
    PageResult<ProcessDO> getProcessPage(ProcessPageReqVO pageReqVO);

    /**
     * 根据工序编号获取工序
     *
     * @param processNo 工序编号
     * @return 工序
     */
    ProcessDO getProcessByProcessNo(String processNo);

    /**
     * 获取工序列表（简单列表，用于下拉选择）
     *
     * @return 工序列表
     */
    List<ProcessDO> getProcessList();

    /**
     * 生成默认工序编号
     *
     * @return 工序编号
     */
    String generateProcessNo();

}



