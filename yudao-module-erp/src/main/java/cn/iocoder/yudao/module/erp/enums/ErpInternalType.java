package cn.iocoder.yudao.module.erp.enums;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * ERP 内部流转类型枚举
 *
 * @author 芋道源码
 */
@RequiredArgsConstructor
@Getter
public enum ErpInternalType implements ArrayValuable<Integer> {

    DEPT_TRANSFER(1, "部门调拨"),
    EMPLOYEE_USE(2, "员工领料"),
    PRODUCTION(3, "生产产品"),
    OTHER(4, "其他");

    public static final Integer[] ARRAYS = Arrays.stream(values())
        .map(ErpInternalType::getType).toArray(Integer[]::new);

    private final Integer type;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}

