# 代码生成器解压报告

## 解压时间
2025-11-16 16:28:13

## 解压统计
- 总 ZIP 文件数: 24
- 成功解压目录数: 24
- 总文件数: 336

## 文件类型统计
- Java 文件: 192
- Vue 文件: 48
- TypeScript 文件: 24
- XML 文件: 24
- SQL 文件: 24

## 解压的模块列表
- cost_actual
- cost_standard
- cost_variance
- equipment
- equipment_status
- mrp_params
- mrp_result
- process_route
- process_route_item
- product_bom
- product_bom_item
- production_dashboard_config
- production_kpi
- production_order
- production_report
- production_schedule
- production_schedule_item
- quality_inspection
- quality_inspection_item
- quality_item
- quality_standard
- work_hours
- work_order
- work_order_progress

## 目录结构
每个模块包含以下目录结构：
- `yudao-module-erp/src/main/java/` - Java 后端代码（Controller, Service, Mapper, DO等）
- `yudao-module-erp/src/main/resources/mapper/` - MyBatis Mapper XML
- `yudao-ui-admin-vue3/src/api/` - TypeScript API 定义
- `yudao-ui-admin-vue3/src/views/` - Vue 前端页面
- `sql/` - SQL 脚本

## 示例文件结构
```
codegen-erp_production_order/
├── yudao-module-erp/
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── cn/iocoder/yudao/module/erp/
│       │   │       ├── controller/admin/productionorder/
│       │   │       ├── service/productionorder/
│       │   │       ├── dal/dataobject/productionorder/
│       │   │       └── dal/mysql/productionorder/
│       │   └── resources/
│       │       └── mapper/productionorder/
│       └── yudao-ui-admin-vue3/
│           └── src/
│               ├── api/erp/productionorder/
│               └── views/erp/productionorder/
└── sql/
    └── sql.sql
```

## 注意事项
部分文件可能存在文件名编码问题（ErrorCodeConstants_手动操作.java），但不影响主要功能代码的使用。

## 下一步操作
1. 检查生成的代码文件
2. 将代码文件复制到项目对应目录
3. 检查并修复导入路径
4. 编译测试
