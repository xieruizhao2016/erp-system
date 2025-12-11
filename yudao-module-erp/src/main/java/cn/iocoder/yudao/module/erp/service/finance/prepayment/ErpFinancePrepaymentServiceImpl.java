package cn.iocoder.yudao.module.erp.service.prepayment;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance.prepayment.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.prepayment.ErpFinancePrepaymentDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.finance.prepayment.ErpFinancePrepaymentMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * 预付款 Service 实现类
 *
 * @author 开发团队
 */
@Service
@Validated
public class ErpFinancePrepaymentServiceImpl implements ErpFinancePrepaymentService {

    @Resource
    private ErpFinancePrepaymentMapper financePrepaymentMapper;

    @Override
    public Long createFinancePrepayment(ErpFinancePrepaymentSaveReqVO createReqVO) {
        // 插入
        ErpFinancePrepaymentDO financePrepayment = BeanUtils.toBean(createReqVO, ErpFinancePrepaymentDO.class);
        financePrepaymentMapper.insert(financePrepayment);

        // 返回
        return financePrepayment.getId();
    }

    @Override
    public void updateFinancePrepayment(ErpFinancePrepaymentSaveReqVO updateReqVO) {
        // 校验存在
        validateFinancePrepaymentExists(updateReqVO.getId());
        // 更新
        ErpFinancePrepaymentDO updateObj = BeanUtils.toBean(updateReqVO, ErpFinancePrepaymentDO.class);
        financePrepaymentMapper.updateById(updateObj);
    }

    @Override
    public void deleteFinancePrepayment(Long id) {
        // 校验存在
        validateFinancePrepaymentExists(id);
        // 删除
        financePrepaymentMapper.deleteById(id);
    }

    @Override
        public void deleteFinancePrepaymentListByIds(List<Long> ids) {
        // 删除
        financePrepaymentMapper.deleteByIds(ids);
        }


    private void validateFinancePrepaymentExists(Long id) {
        if (financePrepaymentMapper.selectById(id) == null) {
            throw exception(FINANCE_PREPAYMENT_NOT_EXISTS);
        }
    }

    @Override
    public ErpFinancePrepaymentDO getFinancePrepayment(Long id) {
        return financePrepaymentMapper.selectById(id);
    }

    @Override
    public PageResult<ErpFinancePrepaymentDO> getFinancePrepaymentPage(ErpFinancePrepaymentPageReqVO pageReqVO) {
        return financePrepaymentMapper.selectPage(pageReqVO);
    }

}