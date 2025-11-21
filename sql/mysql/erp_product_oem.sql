-- ========================================
-- ERP 产品OEM管理模块
-- 创建时间: 2025-11-21
-- 说明: 为产品管理新增产品OEM子模块
-- ========================================

-- ----------------------------
-- Table structure for erp_product_oem
-- ----------------------------
DROP TABLE IF EXISTS `erp_product_oem`;
CREATE TABLE `erp_product_oem` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'OEM编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `oem_code` varchar(50) NOT NULL COMMENT '产品OEM编码',
  `oem_name` varchar(100) NOT NULL COMMENT 'OEM名称',
  `factory_name` varchar(100) NOT NULL COMMENT '产品工厂名称',
  `factory_code` varchar(50) NOT NULL COMMENT '产品工厂编码',
  `factory_address` varchar(255) NULL DEFAULT '' COMMENT '工厂地址',
  `factory_contact` varchar(50) NULL DEFAULT '' COMMENT '工厂联系人',
  `factory_phone` varchar(20) NULL DEFAULT '' COMMENT '工厂联系电话',
  `factory_email` varchar(100) NULL DEFAULT '' COMMENT '工厂邮箱',
  `production_capacity` varchar(100) NULL DEFAULT '' COMMENT '生产能力',
  `certification` varchar(255) NULL DEFAULT '' COMMENT '认证资质（如：ISO9001、ISO14001等）',
  `cooperation_start_date` datetime NULL COMMENT '合作开始日期',
  `cooperation_end_date` datetime NULL COMMENT '合作结束日期',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `quality_level` varchar(20) NULL DEFAULT '' COMMENT '质量等级（如：A级、B级、C级）',
  `payment_terms` varchar(100) NULL DEFAULT '' COMMENT '付款条款',
  `delivery_terms` varchar(100) NULL DEFAULT '' COMMENT '交货条款',
  `logo_url` varchar(500) NULL DEFAULT '' COMMENT 'OEM LOGO URL',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_oem_code` (`tenant_id`, `oem_code`, `deleted`) USING BTREE COMMENT 'OEM编码唯一索引',
  INDEX `idx_tenant_status` (`tenant_id`, `status`) USING BTREE,
  INDEX `idx_factory_code` (`factory_code`) USING BTREE,
  INDEX `idx_factory_name` (`factory_name`) USING BTREE,
  INDEX `idx_create_time` (`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 产品OEM DO';

-- ----------------------------
-- 产品表扩展：添加oem_id字段
-- ----------------------------
SET @dbname = DATABASE();
SET @tablename = 'erp_product';
SET @columnname = 'oem_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''产品OEM编号'' AFTER `package_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 添加产品表oem_id的索引
SET @indexname = 'idx_oem';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (INDEX_NAME = @indexname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD INDEX `', @indexname, '` (`oem_id`) USING BTREE')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- ----------------------------
-- 示例数据（可选）
-- ----------------------------
-- INSERT INTO `erp_product_oem` (`oem_code`, `oem_name`, `factory_name`, `factory_code`, `factory_address`, `factory_contact`, `factory_phone`, `status`, `quality_level`, `sort`, `remark`) VALUES
-- ('OEM-001', '富士康OEM', '富士康科技集团', 'FOXCONN-001', '广东省深圳市龙华区', '张经理', '0755-88888888', 1, 'A级', 1, '主要OEM供应商'),
-- ('OEM-002', '比亚迪OEM', '比亚迪股份有限公司', 'BYD-001', '广东省深圳市坪山区', '李经理', '0755-99999999', 1, 'A级', 2, '新能源领域合作伙伴');

