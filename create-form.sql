INSERT INTO bpm_form (name, status, fields, conf, remark, creator, create_time, updater, update_time, deleted, tenant_id) VALUES
('请假申请表单', 0, '[
  {
    "type": "select",
    "field": "type",
    "title": "请假类型",
    "value": "1",
    "options": [
      {"title": "病假", "value": "1"},
      {"title": "事假", "value": "2"},
      {"title": "婚假", "value": "3"}
    ],
    "props": {"placeholder": "请选择请假类型"},
    "validate": [{"required": true, "message": "请选择请假类型"}]
  },
  {
    "type": "datePicker",
    "field": "startTime",
    "title": "开始时间",
    "value": "",
    "props": {"type": "datetime", "placeholder": "请选择开始时间"},
    "validate": [{"required": true, "message": "请选择开始时间"}]
  },
  {
    "type": "datePicker",
    "field": "endTime",
    "title": "结束时间",
    "value": "",
    "props": {"type": "datetime", "placeholder": "请选择结束时间"},
    "validate": [{"required": true, "message": "请选择结束时间"}]
  },
  {
    "type": "inputNumber",
    "field": "day",
    "title": "请假天数",
    "value": 1,
    "props": {"placeholder": "请输入请假天数", "min": 0.5, "step": 0.5},
    "validate": [{"required": true, "message": "请输入请假天数"}]
  },
  {
    "type": "textarea",
    "field": "reason",
    "title": "请假原因",
    "value": "",
    "props": {"placeholder": "请输入请假原因", "maxlength": 500},
    "validate": [{"required": true, "message": "请输入请假原因"}]
  }
]',
'{"labelPosition": "right", "labelWidth": "120px", "size": "default"}',
'员工请假申请表单', 1, NOW(), 1, NOW(), 0, 1);