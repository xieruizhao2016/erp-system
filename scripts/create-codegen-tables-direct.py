#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
直接通过数据库创建代码生成器表定义
用于解决API返回500错误的情况
"""

import pymysql
import sys
from datetime import datetime

# 配置
DB_CONFIG = {
    'host': 'localhost',
    'port': 3306,
    'user': 'root',
    'password': '123456',
    'database': 'ruoyi-vue-pro',
    'charset': 'utf8mb4'
}

# 表配置
TABLE_CONFIGS = [
    {
        'table_name': 'erp_finance_balance_sheet',
        'module_name': 'erp',
        'business_name': 'finance-balance-sheet',
        'class_name': 'ErpFinanceBalanceSheet',
        'class_comment': '资产负债表',
    },
    {
        'table_name': 'erp_finance_receivable',
        'module_name': 'erp',
        'business_name': 'finance-receivable',
        'class_name': 'ErpFinanceReceivable',
        'class_comment': '应收账款',
    },
    {
        'table_name': 'erp_finance_payable',
        'module_name': 'erp',
        'business_name': 'finance-payable',
        'class_name': 'ErpFinancePayable',
        'class_comment': '应付账款',
    },
    {
        'table_name': 'erp_finance_prepayment',
        'module_name': 'erp',
        'business_name': 'finance-prepayment',
        'class_name': 'ErpFinancePrepayment',
        'class_comment': '预付款',
    },
    {
        'table_name': 'erp_finance_prereceipt',
        'module_name': 'erp',
        'business_name': 'finance-prereceipt',
        'class_name': 'ErpFinancePrereceipt',
        'class_comment': '预收款',
    },
    {
        'table_name': 'erp_finance_profit_statement',
        'module_name': 'erp',
        'business_name': 'finance-profit-statement',
        'class_name': 'ErpFinanceProfitStatement',
        'class_comment': '利润表',
    },
    {
        'table_name': 'erp_stock_internal_in',
        'module_name': 'erp',
        'business_name': 'stock-internal-in',
        'class_name': 'ErpStockInternalIn',
        'class_comment': '内部入库单',
    },
    {
        'table_name': 'erp_stock_internal_out',
        'module_name': 'erp',
        'business_name': 'stock-internal-out',
        'class_name': 'ErpStockInternalOut',
        'class_comment': '内部出库单',
    },
]

def get_next_id(cursor):
    """获取下一个ID"""
    cursor.execute("SELECT MAX(id) FROM infra_codegen_table")
    result = cursor.fetchone()
    return (result[0] or 0) + 1

def get_table_comment(cursor, table_name):
    """获取表注释"""
    cursor.execute("""
        SELECT TABLE_COMMENT 
        FROM information_schema.TABLES 
        WHERE TABLE_SCHEMA = %s AND TABLE_NAME = %s
    """, (DB_CONFIG['database'], table_name))
    result = cursor.fetchone()
    return result[0] if result else ''

def create_codegen_table(cursor, table_id, config):
    """创建代码生成器表定义"""
    table_comment = get_table_comment(cursor, config['table_name'])
    
    sql = """
        INSERT INTO infra_codegen_table (
            id, data_source_config_id, scene, table_name, table_comment,
            module_name, business_name, class_name, class_comment, author,
            template_type, front_type, creator, create_time, updater, update_time, deleted
        ) VALUES (
            %s, 1, 1, %s, %s, %s, %s, %s, %s, '开发团队',
            1, 10, 'admin', NOW(), 'admin', NOW(), 0
        )
    """
    
    cursor.execute(sql, (
        table_id,
        config['table_name'],
        table_comment,
        config['module_name'],
        config['business_name'],
        config['class_name'],
        config['class_comment']
    ))
    
    return table_id

def create_codegen_columns(cursor, table_id, table_name):
    """创建代码生成器字段定义"""
    # 获取表字段信息
    cursor.execute("""
        SELECT 
            COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT, IS_NULLABLE, 
            COLUMN_KEY, ORDINAL_POSITION, COLUMN_DEFAULT
        FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = %s AND TABLE_NAME = %s
        ORDER BY ORDINAL_POSITION
    """, (DB_CONFIG['database'], table_name))
    
    columns = cursor.fetchall()
    
    # 获取下一个字段ID
    cursor.execute("SELECT MAX(id) FROM infra_codegen_column")
    result = cursor.fetchone()
    column_id = (result[0] or 0) + 1
    
    # Java类型映射
    java_type_map = {
        'bigint': 'Long',
        'int': 'Integer',
        'tinyint': 'Integer',
        'smallint': 'Integer',
        'decimal': 'BigDecimal',
        'varchar': 'String',
        'text': 'String',
        'datetime': 'LocalDateTime',
        'date': 'LocalDate',
        'bit': 'Boolean',
    }
    
    # HTML类型映射
    html_type_map = {
        'bigint': 'input',
        'int': 'input',
        'tinyint': 'select',
        'smallint': 'select',
        'decimal': 'input',
        'varchar': 'input',
        'text': 'textarea',
        'datetime': 'datetime',
        'date': 'date',
        'bit': 'radio',
    }
    
    for col in columns:
        column_name, data_type, column_comment, is_nullable, column_key, ordinal_position, column_default = col
        
        # 跳过BaseDO字段
        base_do_fields = ['id', 'creator', 'create_time', 'updater', 'update_time', 'deleted', 'tenant_id']
        if column_name in base_do_fields:
            continue
        
        # 确定Java类型
        java_type = java_type_map.get(data_type.lower().split('(')[0], 'String')
        if java_type == 'BigDecimal':
            java_type = 'BigDecimal'
        
        # 确定Java字段名（驼峰命名）
        java_field = ''.join(word.capitalize() if i > 0 else word for i, word in enumerate(column_name.split('_')))
        java_field = java_field[0].lower() + java_field[1:] if java_field else column_name
        
        # 确定HTML类型
        html_type = html_type_map.get(data_type.lower().split('(')[0], 'input')
        
        # 特殊字段处理
        if 'status' in column_name.lower():
            html_type = 'select'
        elif 'type' in column_name.lower() and '_id' not in column_name.lower():
            html_type = 'select'
        elif '_id' in column_name.lower():
            html_type = 'select'
        elif 'date' in column_name.lower() or 'time' in column_name.lower():
            if 'date' in data_type.lower():
                html_type = 'date'
            else:
                html_type = 'datetime'
        
        # 是否主键
        is_primary_key = 1 if column_key == 'PRI' else 0
        
        # 是否允许为空
        nullable = 1 if is_nullable == 'YES' else 0
        
        # 插入字段定义
        sql = """
            INSERT INTO infra_codegen_column (
                id, table_id, column_name, data_type, column_comment, nullable,
                primary_key, ordinal_position, java_type, java_field,
                dict_type, create_operation, update_operation, list_operation,
                list_operation_condition, list_operation_result, html_type,
                creator, create_time, updater, update_time
            ) VALUES (
                %s, %s, %s, %s, %s, %s, %s, %s, %s, %s,
                '', 1, 1, 1, '=', 1, %s,
                'admin', NOW(), 'admin', NOW()
            )
        """
        
        cursor.execute(sql, (
            column_id,
            table_id,
            column_name,
            data_type,
            column_comment or '',
            nullable,
            is_primary_key,
            ordinal_position,
            java_type,
            java_field,
            html_type
        ))
        
        column_id += 1

def main():
    """主函数"""
    try:
        # 连接数据库
        conn = pymysql.connect(**DB_CONFIG)
        cursor = conn.cursor()
        
        print("=" * 60)
        print("开始创建代码生成器表定义")
        print("=" * 60)
        print()
        
        # 获取起始ID
        start_id = get_next_id(cursor)
        current_id = start_id
        
        success_count = 0
        skip_count = 0
        
        for config in TABLE_CONFIGS:
            table_name = config['table_name']
            
            # 检查表是否已存在
            cursor.execute("""
                SELECT id FROM infra_codegen_table 
                WHERE table_name = %s AND deleted = 0
            """, (table_name,))
            
            if cursor.fetchone():
                print(f"⏭️  跳过（已存在）: {table_name}")
                skip_count += 1
                continue
            
            # 检查数据库表是否存在
            cursor.execute("""
                SELECT COUNT(*) FROM information_schema.TABLES 
                WHERE TABLE_SCHEMA = %s AND TABLE_NAME = %s
            """, (DB_CONFIG['database'], table_name))
            
            if cursor.fetchone()[0] == 0:
                print(f"❌ 跳过（数据库表不存在）: {table_name}")
                skip_count += 1
                continue
            
            try:
                # 创建表定义
                table_id = create_codegen_table(cursor, current_id, config)
                
                # 创建字段定义
                create_codegen_columns(cursor, table_id, table_name)
                
                # 提交事务
                conn.commit()
                
                print(f"✅ 创建成功: {table_name} (ID: {table_id})")
                success_count += 1
                current_id += 1
                
            except Exception as e:
                conn.rollback()
                print(f"❌ 创建失败: {table_name} - {str(e)}")
        
        print()
        print("=" * 60)
        print(f"完成！成功: {success_count}, 跳过: {skip_count}")
        print("=" * 60)
        
        # 关闭连接
        cursor.close()
        conn.close()
        
    except Exception as e:
        print(f"❌ 错误: {str(e)}")
        sys.exit(1)

if __name__ == '__main__':
    main()

