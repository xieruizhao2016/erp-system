-- ========================================
-- ERP 产品包装管理模块
-- 创建时间: 2025-11-21
-- 说明: 为产品管理新增产品包装子模块
-- ========================================

-- ----------------------------
-- Table structure for erp_product_package
-- ----------------------------
DROP TABLE IF EXISTS `erp_product_package`;
CREATE TABLE `erp_product_package` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '包装编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `package_code` varchar(50) NOT NULL COMMENT '包装编码',
  `package_name` varchar(100) NOT NULL COMMENT '包装名称',
  `description` varchar(500) NULL DEFAULT '' COMMENT '包装描述',
  `status` int NOT NULL DEFAULT 0 COMMENT '状态：0-开启，1-关闭',
  `quantity_per_box` int NULL DEFAULT 1 COMMENT '装箱数量（件/箱）',
  `gross_weight` decimal(10,2) NULL COMMENT '单箱毛重（KG）',
  `net_weight` decimal(10,2) NULL COMMENT '单箱净重（KG）',
  `inner_box_size` varchar(50) NULL DEFAULT '' COMMENT '内箱尺寸（CM），格式：长x宽x高',
  `outer_box_size` varchar(50) NULL DEFAULT '' COMMENT '外箱尺寸（CM），格式：长x宽x高',
  `box_volume` decimal(10,4) NULL COMMENT '外箱体积（立方米）',
  `pallet_quantity` int NULL COMMENT '托盘装箱数（箱/托盘）',
  `material` varchar(100) NULL DEFAULT '' COMMENT '包装材料',
  `package_type` varchar(50) NULL DEFAULT '' COMMENT '包装类型（如：纸箱、木箱、托盘等）',
  `barcode` varchar(50) NULL DEFAULT '' COMMENT '包装条码',
  `image_url` varchar(500) NULL DEFAULT '' COMMENT '包装图片URL',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_package_code` (`tenant_id`, `package_code`, `deleted`) USING BTREE COMMENT '包装编码唯一索引',
  INDEX `idx_tenant_status` (`tenant_id`, `status`) USING BTREE,
  INDEX `idx_package_name` (`package_name`) USING BTREE,
  INDEX `idx_create_time` (`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 产品包装 DO';

-- ----------------------------
-- 产品表扩展：添加package_id字段
-- ----------------------------
SET @dbname = DATABASE();
SET @tablename = 'erp_product';
SET @columnname = 'package_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''产品包装编号'' AFTER `sku_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 添加产品表package_id的索引
SET @indexname = 'idx_package';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (INDEX_NAME = @indexname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD INDEX `', @indexname, '` (`package_id`) USING BTREE')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- ----------------------------
-- 示例数据（可选）
-- ----------------------------
-- INSERT INTO `erp_product_package` (`package_code`, `package_name`, `description`, `status`, `quantity_per_box`, `gross_weight`, `net_weight`, `inner_box_size`, `outer_box_size`, `material`, `package_type`, `sort`, `remark`) VALUES
-- ('PKG-001', '标准纸箱包装', '标准纸箱，适用于大多数产品', 1, 10, 5.50, 5.00, '30x20x15', '32x22x17', '瓦楞纸', '纸箱', 1, '常用包装'),
-- ('PKG-002', '加强型纸箱', '加强型纸箱，适用于重型产品', 1, 5, 8.00, 7.00, '40x30x20', '42x32x22', '五层瓦楞纸', '纸箱', 2, '重型产品专用');

