-- ERP 系统示例数据
-- 用于测试采购入库功能

-- 1. 插入示例供应商
INSERT INTO `erp_supplier` (`name`, `contact`, `mobile`, `telephone`, `email`, `remark`, `sort`, `tax_no`, `tax_percent`, `bank_name`, `bank_account`, `bank_address`, `creator`, `updater`) VALUES
('供应商A', '张经理', '13800138001', '010-88888888', 'supplier_a@example.com', '优质供应商', 1, '91110000123456789X', 13.00, '中国工商银行', '6222021234567890123', '北京市朝阳区', 'admin', 'admin'),
('供应商B', '李总', '13800138002', '010-99999999', 'supplier_b@example.com', '长期合作供应商', 2, '91110000987654321Y', 13.00, '中国建设银行', '6222029876543210987', '北京市海淀区', 'admin', 'admin');

-- 2. 插入示例产品
INSERT INTO `erp_product` (`name`, `bar_code`, `standard`, `remark`, `expiry_day`, `weight`, `purchase_price`, `sale_price`, `min_price`, `creator`, `updater`) VALUES
('笔记本电脑', 'LAPTOP001', '15.6寸/8GB/256GB SSD', '高性能办公笔记本', 1095, 2.5, 3999.00, 4999.00, 3500.00, 'admin', 'admin'),
('无线鼠标', 'MOUSE001', '2.4G/蓝牙双模', '静音无线鼠标', 730, 0.1, 99.00, 129.00, 79.00, 'admin', 'admin'),
('机械键盘', 'KEYBOARD001', '87键/青轴/RGB背光', '游戏机械键盘', 1095, 1.2, 299.00, 399.00, 249.00, 'admin', 'admin'),
('显示器', 'MONITOR001', '27寸/2K/75Hz', 'IPS显示器', 1095, 5.0, 1299.00, 1599.00, 1199.00, 'admin', 'admin'),
('USB数据线', 'CABLE001', 'Type-C to USB 3.0', '1.5米快充数据线', 365, 0.05, 29.00, 39.00, 19.00, 'admin', 'admin');

-- 3. 插入示例产品单位
INSERT INTO `erp_product_unit` (`name`, `status`, `creator`, `updater`) VALUES
('台', 0, 'admin', 'admin'),
('个', 0, 'admin', 'admin'),
('套', 0, 'admin', 'admin'),
('件', 0, 'admin', 'admin');

-- 4. 插入示例仓库
INSERT INTO `erp_warehouse` (`name`, `address`, `sort`, `remark`, `principal`, `warehouse_price`, `truckage_price`, `default_status`, `creator`, `updater`) VALUES
('主仓库', '北京市朝阳区XX路XX号', 1, '主要存储仓库', '王经理', 1000.00, 200.00, 1, 'admin', 'admin'),
('分仓库A', '北京市海淀区XX路XX号', 2, '临时存储仓库', '李主管', 800.00, 150.00, 0, 'admin', 'admin');

-- 5. 插入结算账户
INSERT INTO `erp_account` (`name`, `no`, `remark`, `sort`, `default_status`, `creator`, `updater`) VALUES
('现金账户', 'CASH001', '现金账户', 1, 1, 'admin', 'admin'),
('工商银行', 'ICBC001', '工商银行对公账户', 2, 0, 'admin', 'admin');

-- 6. 插入示例采购订单（状态为已审核，且可以入库）
INSERT INTO `erp_purchase_order` (`no`, `status`, `supplier_id`, `account_id`, `order_time`, `total_count`, `total_price`, `total_product_price`, `total_tax_price`, `discount_percent`, `discount_price`, `deposit_price`, `file_url`, `remark`, `in_count`, `return_count`, `creator`, `updater`) VALUES
('CG20241111001', 20, 1, 2, '2024-11-10 09:00:00', 10.00, 13986.00, 12384.00, 1602.00, 0.00, 0.00, 0.00, '', '办公用品采购', 0.00, 0.00, 'admin', 'admin'),
('CG20241111002', 20, 2, 1, '2024-11-09 14:30:00', 15.00, 5286.00, 4680.00, 606.00, 10.00, 528.60, 1000.00, '', '电脑配件采购', 5.00, 0.00, 'admin', 'admin'),
('CG20241111003', 20, 1, 2, '2024-11-08 16:00:00', 20.00, 7774.00, 6880.00, 894.00, 5.00, 363.51, 0.00, '', '外设采购', 0.00, 0.00, 'admin', 'admin');

-- 7. 插入采购订单明细
-- 订单1明细：可以全部入库
INSERT INTO `erp_purchase_order_items` (`order_id`, `product_id`, `product_price`, `count`, `tax_percent`, `remark`, `in_count`, `return_count`, `creator`, `updater`) VALUES
(1, 1, 3999.00, 2.00, 13.00, '笔记本电脑', 0.00, 0.00, 'admin', 'admin'),
(1, 2, 99.00, 5.00, 13.00, '无线鼠标', 0.00, 0.00, 'admin', 'admin'),
(1, 5, 29.00, 3.00, 13.00, 'USB数据线', 0.00, 0.00, 'admin', 'admin');

-- 订单2明细：部分已入库，还可以入库5个
INSERT INTO `erp_purchase_order_items` (`order_id`, `product_id`, `product_price`, `count`, `tax_percent`, `remark`, `in_count`, `return_count`, `creator`, `updater`) VALUES
(2, 1, 3999.00, 1.00, 13.00, '笔记本电脑', 1.00, 0.00, 'admin', 'admin'),
(2, 3, 299.00, 2.00, 13.00, '机械键盘', 2.00, 0.00, 'admin', 'admin'),
(2, 4, 1299.00, 2.00, 13.00, '显示器', 2.00, 0.00, 'admin', 'admin'),
(2, 5, 29.00, 10.00, 13.00, 'USB数据线', 5.00, 0.00, 'admin', 'admin');

-- 订单3明细：可以全部入库
INSERT INTO `erp_purchase_order_items` (`order_id`, `product_id`, `product_price`, `count`, `tax_percent`, `remark`, `in_count`, `return_count`, `creator`, `updater`) VALUES
(3, 2, 99.00, 8.00, 13.00, '无线鼠标', 0.00, 0.00, 'admin', 'admin'),
(3, 3, 299.00, 4.00, 13.00, '机械键盘', 0.00, 0.00, 'admin', 'admin'),
(3, 4, 1299.00, 2.00, 13.00, '显示器', 0.00, 0.00, 'admin', 'admin'),
(3, 5, 29.00, 6.00, 13.00, 'USB数据线', 0.00, 0.00, 'admin', 'admin');