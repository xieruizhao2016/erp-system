-- ========================================
-- 产品和SKU多对多关联表
-- 创建时间: 2025-11-23
-- 说明: 实现产品和SKU的多对多关系，支持一个产品关联多个SKU，一个SKU可以被多个产品关联
-- ========================================

SET @dbname = DATABASE();

-- ========================================
-- 1. 创建产品和SKU关联表
-- ========================================
DROP TABLE IF EXISTS `erp_product_sku_relation`;
CREATE TABLE `erp_product_sku_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `sku_id` bigint NOT NULL COMMENT 'SKU编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_product_sku` (`tenant_id`, `product_id`, `sku_id`, `deleted`) USING BTREE COMMENT '产品和SKU唯一索引',
  INDEX `idx_product_id` (`product_id`) USING BTREE,
  INDEX `idx_sku_id` (`sku_id`) USING BTREE,
  INDEX `idx_create_time` (`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 产品和SKU关联表';

-- ========================================
-- 2. 迁移现有数据：将SKU表的product_id迁移到关联表
-- ========================================
INSERT INTO `erp_product_sku_relation` (`tenant_id`, `product_id`, `sku_id`, `create_time`, `update_time`, `creator`, `updater`)
SELECT 
  sku.tenant_id,
  sku.product_id,
  sku.id,
  sku.create_time,
  sku.update_time,
  sku.creator,
  sku.updater
FROM `erp_product_sku` sku
WHERE sku.product_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM `erp_product_sku_relation` rel
    WHERE rel.product_id = sku.product_id AND rel.sku_id = sku.id AND rel.deleted = 0
  );

-- ========================================
-- 3. 将SKU表的product_id字段改为可选（不删除，保留用于兼容）
-- ========================================
-- 注意：product_id字段保留，但不再强制要求，允许为NULL

