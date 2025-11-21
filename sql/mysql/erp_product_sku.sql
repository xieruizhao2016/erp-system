-- ========================================
-- ERP 产品SKU管理模块
-- 创建时间: 2025-11-21
-- 说明: 为产品管理新增SKU管理子模块
-- ========================================

-- ----------------------------
-- Table structure for erp_product_sku
-- ----------------------------
DROP TABLE IF EXISTS `erp_product_sku`;
CREATE TABLE `erp_product_sku` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'SKU编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `sku_code` varchar(50) NOT NULL COMMENT 'SKU编码',
  `sku_name` varchar(100) NOT NULL COMMENT 'SKU名称',
  `description` varchar(500) NULL DEFAULT '' COMMENT 'SKU描述',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `category_id` bigint NULL COMMENT '分类编号',
  `bar_code` varchar(50) NULL DEFAULT '' COMMENT 'SKU条码',
  `standard` varchar(255) NULL DEFAULT '' COMMENT 'SKU规格',
  `unit_id` bigint NULL COMMENT '单位编号',
  `weight` decimal(20,2) NULL COMMENT '重量（kg）',
  `volume` decimal(20,2) NULL COMMENT '体积（立方米）',
  `cost_price` decimal(20,2) NULL COMMENT '成本价格，单位：元',
  `purchase_price` decimal(20,2) NULL COMMENT '采购价格，单位：元',
  `sale_price` decimal(20,2) NULL COMMENT '销售价格，单位：元',
  `min_price` decimal(20,2) NULL COMMENT '最低价格，单位：元',
  `color` varchar(50) NULL DEFAULT '' COMMENT '颜色',
  `size` varchar(50) NULL DEFAULT '' COMMENT '尺寸',
  `material` varchar(100) NULL DEFAULT '' COMMENT '材质',
  `image_url` varchar(500) NULL DEFAULT '' COMMENT '图片URL',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_sku_code` (`tenant_id`, `sku_code`, `deleted`) USING BTREE COMMENT 'SKU编码唯一索引',
  INDEX `idx_tenant_status` (`tenant_id`, `status`) USING BTREE,
  INDEX `idx_category` (`category_id`) USING BTREE,
  INDEX `idx_sku_name` (`sku_name`) USING BTREE,
  INDEX `idx_create_time` (`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 产品SKU DO';

-- ----------------------------
-- 产品表扩展：添加sku_id字段
-- ----------------------------
SET @dbname = DATABASE();
SET @tablename = 'erp_product';
SET @columnname = 'sku_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''SKU编号'' AFTER `unit_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 添加产品表sku_id的索引
SET @indexname = 'idx_sku';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (INDEX_NAME = @indexname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD INDEX `', @indexname, '` (`sku_id`) USING BTREE')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- ----------------------------
-- 产品表扩展：添加code字段（产品编码）
-- ----------------------------
SET @columnname = 'code';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(50) NULL DEFAULT '''' COMMENT ''产品编码'' AFTER `name`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- ----------------------------
-- 示例数据（可选）
-- ----------------------------
-- INSERT INTO `erp_product_sku` (`sku_code`, `sku_name`, `description`, `status`, `bar_code`, `standard`, `color`, `size`, `sort`, `remark`) VALUES
-- ('SKU-001', '标准SKU', '这是一个标准SKU示例', 1, '1234567890123', '标准规格', '红色', 'M', 1, '示例备注'),
-- ('SKU-002', '高级SKU', '这是一个高级SKU示例', 1, '1234567890124', '高级规格', '蓝色', 'L', 2, '示例备注');
