package cn.iocoder.yudao.framework.mybatis.core.handler;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 通用参数填充实现类
 *
 * 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 *
 * @author hexiaowu
 */
public class DefaultDBFieldHandler implements MetaObjectHandler {

    @Override
    @SuppressWarnings("PatternVariableCanBeUsed")
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO) {
            BaseDO baseDO = (BaseDO) metaObject.getOriginalObject();

            LocalDateTime current = LocalDateTime.now();
            // 创建时间为空，则以当前时间为插入时间
            if (Objects.isNull(baseDO.getCreateTime())) {
                baseDO.setCreateTime(current);
            }
            // 更新时间为空，则以当前时间为更新时间
            if (Objects.isNull(baseDO.getUpdateTime())) {
                baseDO.setUpdateTime(current);
            }

            Long userId = SecurityFrameworkUtils.getLoginUserId();
            // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
            if (Objects.nonNull(userId) && Objects.isNull(baseDO.getCreator())) {
                baseDO.setCreator(userId.toString());
            }
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            if (Objects.nonNull(userId) && Objects.isNull(baseDO.getUpdater())) {
                baseDO.setUpdater(userId.toString());
            }
            
            // 填充租户ID（如果DO类中有tenantId字段且值为空）
            // 使用反射访问 TenantContextHolder，避免循环依赖
            Long tenantId = getTenantId();
            if (Objects.nonNull(tenantId) && metaObject.hasGetter("tenantId")) {
                Object currentTenantId = getFieldValByName("tenantId", metaObject);
                if (Objects.isNull(currentTenantId)) {
                    setFieldValByName("tenantId", tenantId, metaObject);
                }
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间为空，则以当前时间为更新时间
        Object modifyTime = getFieldValByName("updateTime", metaObject);
        if (Objects.isNull(modifyTime)) {
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }

        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        Object modifier = getFieldValByName("updater", metaObject);
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        if (Objects.nonNull(userId) && Objects.isNull(modifier)) {
            setFieldValByName("updater", userId.toString(), metaObject);
        }
    }

    /**
     * 获取租户ID（使用反射避免循环依赖）
     * 
     * @return 租户ID，如果获取失败返回null
     */
    private Long getTenantId() {
        try {
            // 使用反射访问 TenantContextHolder，避免直接依赖
            Class<?> tenantContextHolderClass = Class.forName("cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder");
            java.lang.reflect.Method getTenantIdMethod = tenantContextHolderClass.getMethod("getTenantId");
            Object result = getTenantIdMethod.invoke(null);
            return result != null ? (Long) result : null;
        } catch (Exception e) {
            // 如果 TenantContextHolder 不存在或获取失败，返回 null
            // 这通常发生在未引入 yudao-spring-boot-starter-biz-tenant 依赖时
            return null;
        }
    }
}
