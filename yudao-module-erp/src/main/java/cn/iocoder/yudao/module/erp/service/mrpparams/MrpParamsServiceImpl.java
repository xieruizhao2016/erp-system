package cn.iocoder.yudao.module.erp.service.mrpparams;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.mrpparams.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.mrpparams.MrpParamsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.mrpparams.MrpParamsMapper;

import lombok.extern.slf4j.Slf4j;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP MRP参数 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class MrpParamsServiceImpl implements MrpParamsService {

    @Resource
    private MrpParamsMapper mrpParamsMapper;

    @Override
    public Long createMrpParams(MrpParamsSaveReqVO createReqVO) {
        // 插入
        MrpParamsDO mrpParams = BeanUtils.toBean(createReqVO, MrpParamsDO.class);
        mrpParamsMapper.insert(mrpParams);

        // 返回
        return mrpParams.getId();
    }

    @Override
    public void updateMrpParams(MrpParamsSaveReqVO updateReqVO) {
        // 校验存在
        validateMrpParamsExists(updateReqVO.getId());
        // 更新
        MrpParamsDO updateObj = BeanUtils.toBean(updateReqVO, MrpParamsDO.class);
        mrpParamsMapper.updateById(updateObj);
    }

    @Override
    public void deleteMrpParams(Long id) {
        // 校验存在
        validateMrpParamsExists(id);
        // 删除
        mrpParamsMapper.deleteById(id);
    }

    @Override
        public void deleteMrpParamsListByIds(List<Long> ids) {
        // 删除
        mrpParamsMapper.deleteByIds(ids);
        }


    private void validateMrpParamsExists(Long id) {
        if (mrpParamsMapper.selectById(id) == null) {
            throw exception(MRP_PARAMS_NOT_EXISTS);
        }
    }

    @Override
    public MrpParamsDO getMrpParams(Long id) {
        return mrpParamsMapper.selectById(id);
    }

    @Override
    public PageResult<MrpParamsDO> getMrpParamsPage(MrpParamsPageReqVO pageReqVO) {
        return mrpParamsMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer generateDefaultParams() {
        log.info("[MRP参数] 开始生成默认MRP参数");
        
        // 定义默认参数列表
        List<MrpParamsDO> defaultParams = new ArrayList<>();
        
        // 1. 安全库存比例
        defaultParams.add(MrpParamsDO.builder()
                .paramName("安全库存比例")
                .paramCode("SAFETY_STOCK_RATIO")
                .paramValue("0.1")
                .paramType(2) // 数字类型
                .description("保持的安全库存比例，0.1表示10%。用于MRP运算时计算安全库存需求。")
                .isSystem(true)
                .build());
        
        // 2. 默认提前期（天）
        defaultParams.add(MrpParamsDO.builder()
                .paramName("默认提前期")
                .paramCode("DEFAULT_LEAD_TIME")
                .paramValue("7")
                .paramType(2) // 数字类型
                .description("采购或生产的默认提前期（天）。如果产品没有设置提前期，则使用此默认值。")
                .isSystem(true)
                .build());
        
        // 3. 批量规则
        defaultParams.add(MrpParamsDO.builder()
                .paramName("批量规则")
                .paramCode("LOT_SIZING_RULE")
                .paramValue("2")
                .paramType(2) // 数字类型
                .description("订单批量规则：1-固定批量，2-按需，3-最小-最大。默认按需生产/采购。")
                .isSystem(true)
                .build());
        
        // 4. 计划展望期（天）
        defaultParams.add(MrpParamsDO.builder()
                .paramName("计划展望期")
                .paramCode("PLANNING_HORIZON")
                .paramValue("90")
                .paramType(2) // 数字类型
                .description("MRP计划的时间范围（天），90天表示未来3个月。")
                .isSystem(true)
                .build());
        
        // 5. 是否考虑在途数量
        defaultParams.add(MrpParamsDO.builder()
                .paramName("考虑在途数量")
                .paramCode("CONSIDER_IN_TRANSIT")
                .paramValue("true")
                .paramType(4) // 布尔类型
                .description("MRP运算时是否考虑在途的采购和生产数量。true-考虑，false-不考虑。")
                .isSystem(true)
                .build());
        
        // 6. 是否自动展开BOM
        defaultParams.add(MrpParamsDO.builder()
                .paramName("自动展开BOM")
                .paramCode("AUTO_EXPAND_BOM")
                .paramValue("true")
                .paramType(4) // 布尔类型
                .description("MRP运算时是否自动展开BOM，计算子物料需求。true-自动展开，false-仅计算成品。")
                .isSystem(true)
                .build());
        
        // 7. BOM展开层级
        defaultParams.add(MrpParamsDO.builder()
                .paramName("BOM展开层级")
                .paramCode("BOM_EXPAND_LEVEL")
                .paramValue("10")
                .paramType(2) // 数字类型
                .description("BOM最大展开层级。0表示不限制，建议设置为10层以内。")
                .isSystem(true)
                .build());
        
        // 8. 固定批量数量
        defaultParams.add(MrpParamsDO.builder()
                .paramName("固定批量数量")
                .paramCode("FIXED_LOT_SIZE")
                .paramValue("100")
                .paramType(2) // 数字类型
                .description("当批量规则为固定批量时使用的数量。")
                .isSystem(false)
                .build());
        
        // 9. 最小批量
        defaultParams.add(MrpParamsDO.builder()
                .paramName("最小批量")
                .paramCode("MIN_LOT_SIZE")
                .paramValue("10")
                .paramType(2) // 数字类型
                .description("最小采购/生产批量。实际订单数量不会小于此值。")
                .isSystem(false)
                .build());
        
        // 10. 最大批量
        defaultParams.add(MrpParamsDO.builder()
                .paramName("最大批量")
                .paramCode("MAX_LOT_SIZE")
                .paramValue("1000")
                .paramType(2) // 数字类型
                .description("最大采购/生产批量。实际订单数量不会大于此值。")
                .isSystem(false)
                .build());
        
        // 批量插入前，先检查是否已存在相同编码的参数
        int insertCount = 0;
        for (MrpParamsDO param : defaultParams) {
            // 检查参数编码是否已存在
            List<MrpParamsDO> existingParams = mrpParamsMapper.selectList("param_code", param.getParamCode());
            if (CollUtil.isEmpty(existingParams)) {
                mrpParamsMapper.insert(param);
                insertCount++;
                log.info("[MRP参数] 生成参数：{} - {}", param.getParamCode(), param.getParamName());
            } else {
                log.info("[MRP参数] 参数已存在，跳过：{} - {}", param.getParamCode(), param.getParamName());
            }
        }
        
        log.info("[MRP参数] 默认参数生成完成，成功生成{}个参数", insertCount);
        return insertCount;
    }

}