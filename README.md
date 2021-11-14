# SILLY-ACTIVITI 文档

```java
/**
 * @since 2021-11-14
 * @version 1.0.6
 * @author qiny
 */
```

### Silly-activiti 是什么？

silly-activiti是流程中业务数据管理工具，致力于提供可靠可控可拓展的数据管理服务。silly本身不提供流程引擎服务，但提供了流程引擎的相关接口，通过实现接口来接入已有的流程引擎中。

### 快速开始

#### 用例 (基于 内部系统，无法提供)

员工差旅费报销流程。整个业务分两个层面

1. 流程图，由流程引擎控制，本服务不参与。
2. 流程信息配置，由json配置。本服务管控。

#### 绘制流程图

使用activiti在线编辑器绘制流程图，需要注意设置任务节点ID，后面的JSON配置会用到。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.activiti.org/processdef">
  <process id="silly_reimbursement_v1" name="silly_reimbursement_v1" isExecutable="true">
    <startEvent id="start" name="启动"></startEvent>
    <userTask id="T10010" name="报销单申请"></userTask>
    <sequenceFlow id="sid-7AAC26AF-97E4-4504-BB67-EEA4837F0C68" sourceRef="start" targetRef="T10010"></sequenceFlow>
    <userTask id="T10020" name="主管审核"></userTask>
    <exclusiveGateway id="sid-75C7B6F8-FD90-41A3-A089-694728891E8F"></exclusiveGateway>
    <userTask id="T10030" name="经理审批"></userTask>
    <endEvent id="结束" name="end"></endEvent>
    <sequenceFlow id="sid-6A624E43-3486-42FA-9813-40D8D06D205D" name="小于等于1000元" sourceRef="sid-75C7B6F8-FD90-41A3-A089-694728891E8F" targetRef="结束">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${money <= 1000}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="sid-ECEE2D3D-9EA8-4E39-A228-7ECA7849CAA5" name="大于1000元" sourceRef="sid-75C7B6F8-FD90-41A3-A089-694728891E8F" targetRef="T10030">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${money > 1000}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="sid-B22C6853-454E-47AD-A338-952127990ECC" name="不同意" sourceRef="T10030" targetRef="T10010">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${approveFlag eq '0'}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="sid-704540CE-883C-4C67-93F3-B5C9DB12A6E0" name="同意" sourceRef="T10030" targetRef="结束">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${approveFlag eq '1'}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="sid-649C4718-44DF-4022-AF9A-EAD4F1D7341C" name="同意" sourceRef="T10020" targetRef="sid-75C7B6F8-FD90-41A3-A089-694728891E8F">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${auditFlag eq '1'}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="sid-DA816420-1EFA-459C-846D-7E9156A1975C" name="不同意" sourceRef="T10020" targetRef="T10010">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${auditFlag eq '0'}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="sid-E76A43C3-1F9A-4085-9EAA-6CDD4063353E" sourceRef="T10010" targetRef="T10020"></sequenceFlow>
  </process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_silly_reimbursement_v1">
    <bpmndi:BPMNPlane bpmnElement="silly_reimbursement_v1" id="BPMNPlane_silly_reimbursement_v1">
      <bpmndi:BPMNShape bpmnElement="start" id="BPMNShape_start">
        <omgdc:Bounds height="30.0" width="30.0" x="100.0" y="163.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="T10010" id="BPMNShape_T10010">
        <omgdc:Bounds height="80.0" width="100.0" x="195.0" y="138.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="T10020" id="BPMNShape_T10020">
        <omgdc:Bounds height="80.0" width="100.0" x="359.5333333333333" y="138.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="sid-75C7B6F8-FD90-41A3-A089-694728891E8F" id="BPMNShape_sid-75C7B6F8-FD90-41A3-A089-694728891E8F">
        <omgdc:Bounds height="40.0" width="40.0" x="510.0" y="158.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="T10030" id="BPMNShape_T10030">
        <omgdc:Bounds height="80.0" width="100.0" x="613.0" y="138.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="结束" id="BPMNShape_结束">
        <omgdc:Bounds height="28.0" width="28.0" x="773.0" y="164.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge bpmnElement="sid-7AAC26AF-97E4-4504-BB67-EEA4837F0C68" id="BPMNEdge_sid-7AAC26AF-97E4-4504-BB67-EEA4837F0C68">
        <omgdi:waypoint x="130.0" y="178.0"></omgdi:waypoint>
        <omgdi:waypoint x="195.0" y="178.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-B22C6853-454E-47AD-A338-952127990ECC" id="BPMNEdge_sid-B22C6853-454E-47AD-A338-952127990ECC">
        <omgdi:waypoint x="663.0" y="138.0"></omgdi:waypoint>
        <omgdi:waypoint x="663.0" y="86.0"></omgdi:waypoint>
        <omgdi:waypoint x="245.0" y="86.0"></omgdi:waypoint>
        <omgdi:waypoint x="245.0" y="138.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-649C4718-44DF-4022-AF9A-EAD4F1D7341C" id="BPMNEdge_sid-649C4718-44DF-4022-AF9A-EAD4F1D7341C">
        <omgdi:waypoint x="459.5333333333333" y="178.20666850372004"></omgdi:waypoint>
        <omgdi:waypoint x="510.4169894853348" y="178.4169894853348"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-ECEE2D3D-9EA8-4E39-A228-7ECA7849CAA5" id="BPMNEdge_sid-ECEE2D3D-9EA8-4E39-A228-7ECA7849CAA5">
        <omgdi:waypoint x="549.5719696969697" y="178.4280303030303"></omgdi:waypoint>
        <omgdi:waypoint x="613.0" y="178.18867924528303"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-E76A43C3-1F9A-4085-9EAA-6CDD4063353E" id="BPMNEdge_sid-E76A43C3-1F9A-4085-9EAA-6CDD4063353E">
        <omgdi:waypoint x="295.0" y="178.0"></omgdi:waypoint>
        <omgdi:waypoint x="359.5333333333333" y="178.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-6A624E43-3486-42FA-9813-40D8D06D205D" id="BPMNEdge_sid-6A624E43-3486-42FA-9813-40D8D06D205D">
        <omgdi:waypoint x="530.5" y="197.5"></omgdi:waypoint>
        <omgdi:waypoint x="530.5" y="273.0"></omgdi:waypoint>
        <omgdi:waypoint x="787.0" y="273.0"></omgdi:waypoint>
        <omgdi:waypoint x="787.0" y="192.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-704540CE-883C-4C67-93F3-B5C9DB12A6E0" id="BPMNEdge_sid-704540CE-883C-4C67-93F3-B5C9DB12A6E0">
        <omgdi:waypoint x="713.0" y="178.0"></omgdi:waypoint>
        <omgdi:waypoint x="773.0" y="178.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="sid-DA816420-1EFA-459C-846D-7E9156A1975C" id="BPMNEdge_sid-DA816420-1EFA-459C-846D-7E9156A1975C">
        <omgdi:waypoint x="409.5333333333333" y="138.0"></omgdi:waypoint>
        <omgdi:waypoint x="409.5333333333333" y="90.0"></omgdi:waypoint>
        <omgdi:waypoint x="245.0" y="90.0"></omgdi:waypoint>
        <omgdi:waypoint x="245.0" y="138.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</definitions>
```



#### 配置流程JSON

```json
{
  "category": "silly_reimbursement",
  "processDesc": "员工差旅费用报销流程",
  "lastProcessKey": "silly_reimbursement_v1",
  "firstNodeKey": "T10010",
  "master": {
    "silly_reimbursement_v1": {
      "processVersion": "v1",
      "node": {
        "T10010": {
          "nodeName": "报销单填写",
          "variable": {
            "code": {
              "desc": "报销单编号",
              "belong": "master",
              "defaultText": "${@generatorReimbursementCodeHandle.code(#code)}"
            },
            "money": {
              "desc": "报销金额",
              "belong": "master",
              "activitiHandler": "string"
            },
            "fileGroupId": {
              "desc": "报销文件组ID",
              "belong": "master",
              "variableType": "filegroup"
            },
            "remarks": {
              "desc": "备注说明",
              "belong": "master",
              "request": false
            },
            "auditUserId": {
              "desc": "审核人",
              "variableType": "user",
              "activitiHandler": "string"
            },
            "approveUserId": {
              "desc": "审批人",
              "variableType": "user",
              "activitiHandler": "string",
              "requestEl": "${#money > 1000}"
            }
          }
        },
        "T10020": {
          "nodeName": "主管审核",
          "variable": {
            "auditFlag": {
              "desc": "审核标识 1：通过 0：不通过",
              "activitiHandler": "string"
            },
            "auditRemarks": {
              "desc": "审核备注",
              "defaultText": "${#auditFlag eq '1'?'同意':null}"
            },
            "handleType": {
              "desc": "处置类型",
              "belong": "node",
              "saveHandleNames": [
                "skip"
              ],
              "defaultText": "${#auditFlag eq '0'? 'back':'next'}"
            },
            "nodeInfo": {
              "desc": "履历信息",
              "belong": "node",
              "saveHandleNames": [
                "skip"
              ],
              "defaultText": "${'主管审核意见：' + #auditRemarks}"
            }
          }
        },
        "T10030": {
          "nodeName": "经理审批",
          "variable": {
            "approveFlag": {
              "desc": "审核标识 1：通过 0：不通过",
              "activitiHandler": "string"
            },
            "approveRemarks": {
              "desc": "审核备注",
              "defaultText": "${#approveFlag eq '1'?'同意':null}"
            },
            "handleType": {
              "desc": "处置类型",
              "belong": "node",
              "saveHandleNames": [
                "skip"
              ],
              "defaultText": "${#approveFlag eq '0'? 'back':'next'}"
            },
            "nodeInfo": {
              "desc": "履历信息",
              "belong": "node",
              "saveHandleNames": [
                "skip"
              ],
              "defaultText": "${'经理审批意见：' + #approveRemarks}"
            }
          }
        }
      }
    }
  }
}
```



#### 数据库表设计（主表业务字段设置）

先自己设置主表业务字段信息，主表工具信息和节点表及变量表silly工具提供标准结构。

```java
 // 生成表创建SQL(执行后会生成个SQL文件)
 SillyGenerateService.generatorTable("silly_reimbursement");
```

生成的sql文件内容如下，生成的是必须的字段，放入oracle 中执行创建表结构

```sql
-- Create table
create table silly_reimbursement
(
  id                VARCHAR2(64) not null,
  create_user_id    VARCHAR2(64),
  create_date       DATE,
  update_user_id    VARCHAR2(64),
  update_date       DATE,
  del_flag          CHAR(1) default '0' not null,
  status            VARCHAR2(20),
  process_id        VARCHAR2(64),
  start_date        DATE,
  start_user_id     VARCHAR2(64),
  close_date        DATE,
  close_user_id     VARCHAR2(64),
  process_key       VARCHAR2(128),
  process_version   VARCHAR2(10),
  handle_user_name  VARCHAR2(512),
  task_name         VARCHAR2(512)
);
-- Add comments to the table
comment on table silly_reimbursement
  is '主表MASTER';
-- Add comments to the columns
comment on column silly_reimbursement.create_user_id
  is '创建人ID';
comment on column silly_reimbursement.create_date
  is '创建时间';
comment on column silly_reimbursement.update_user_id
  is '更新人ID';
comment on column silly_reimbursement.update_date
  is '更新时间';
comment on column silly_reimbursement.del_flag
  is '删除标识';
comment on column silly_reimbursement.status
  is '状态 （10: 编辑中 20：流程处置中   90：已完成 ）';
comment on column silly_reimbursement.process_id
  is '流程实例ID';
comment on column silly_reimbursement.start_date
  is '启动日期';
comment on column silly_reimbursement.start_user_id
  is '启动人';
comment on column silly_reimbursement.close_date
  is '关闭日期';
comment on column silly_reimbursement.close_user_id
  is '关闭人';
comment on column silly_reimbursement.process_key
  is '流程KEY';
comment on column silly_reimbursement.process_version
  is '流程版本';
comment on column silly_reimbursement.handle_user_name
  is '当前处置人名称';
comment on column silly_reimbursement.task_name
  is '当前任务名称';

-- Create table
create table silly_reimbursement_NODE
(
  id             VARCHAR2(64) not null,
  master_id      VARCHAR2(64) not null,
  seq            NUMBER not null,
  node_key       VARCHAR2(64) not null,
  node_name      VARCHAR2(128),
  task_id        VARCHAR2(64),
  parallel_flag  CHAR(1) default '0' not null,
  node_date      DATE not null,
  node_user_id   VARCHAR2(64) not null,
  status         VARCHAR2(10),
  node_info      VARCHAR2(512),
  del_flag       CHAR(1) default '0' not null,
  create_date    DATE,
  create_user_id VARCHAR2(64),
  update_date    DATE,
  update_user_id VARCHAR2(64)
);
-- Add comments to the table
comment on table silly_reimbursement_NODE
  is 'NODE';
-- Add comments to the columns
comment on column silly_reimbursement_NODE.master_id
  is '主表ID';
comment on column silly_reimbursement_NODE.seq
  is '处置排序';
comment on column silly_reimbursement_NODE.node_key
  is '节点Key';
comment on column silly_reimbursement_NODE.node_name
  is '节点名称';
comment on column silly_reimbursement_NODE.task_id
  is '任务ID';
comment on column silly_reimbursement_NODE.parallel_flag
  is '并行标识 ‘1’ 是  ‘0’ 不是';
comment on column silly_reimbursement_NODE.node_date
  is '节点处置时间';
comment on column silly_reimbursement_NODE.node_user_id
  is '节点处置人ID';
comment on column silly_reimbursement_NODE.status
  is '状态';
comment on column silly_reimbursement_NODE.node_info
  is '节点信息';


-- Create table
create table silly_reimbursement_VARIABLE
(
  id               VARCHAR2(64) not null,
  node_id          VARCHAR2(64) not null,
  master_id        VARCHAR2(64) not null,
  task_id          VARCHAR2(64),
  node_key         VARCHAR2(64),
  status           VARCHAR2(64) not null,
  activiti_handler VARCHAR2(64),
  variable_type    VARCHAR2(64) not null,
  variable_name    VARCHAR2(64) not null,
  variable_text    VARCHAR2(2000),
  belong           VARCHAR2(64) not null,
  del_flag         CHAR(1) default '0' not null,
  create_date      DATE,
  create_user_id   VARCHAR2(64),
  update_date      DATE,
  update_user_id   VARCHAR2(64)
);
-- Add comments to the table
comment on table silly_reimbursement_VARIABLE
  is 'VARIABLE';
-- Add comments to the columns
comment on column silly_reimbursement_VARIABLE.node_id
  is '节点ID';
comment on column silly_reimbursement_VARIABLE.master_id
  is '主表ID';
comment on column silly_reimbursement_VARIABLE.task_id
  is '任务ID';
comment on column silly_reimbursement_VARIABLE.node_key
  is '节点Key';
comment on column silly_reimbursement_VARIABLE.status
  is '状态';
comment on column silly_reimbursement_VARIABLE.activiti_handler
  is '流程变量类型';
comment on column silly_reimbursement_VARIABLE.variable_type
  is '变量类型';
comment on column silly_reimbursement_VARIABLE.variable_name
  is '变量名称';
comment on column silly_reimbursement_VARIABLE.variable_text
  is '变量内容';
comment on column silly_reimbursement_VARIABLE.belong
  is '归属对象 ‘master’/''node''/''variable''';
```

根据自己的业务需求在表中添加所需字段

```sql
-- Add/modify columns 
alter table SILLY_REIMBURSEMENT add code varchar2(64);
alter table SILLY_REIMBURSEMENT add money number;
alter table SILLY_REIMBURSEMENT add file_group_id varchar2(64);
alter table SILLY_REIMBURSEMENT add remarks varchar2(512);
-- Add comments to the columns 
comment on column SILLY_REIMBURSEMENT.code
  is '报销单编号';
comment on column SILLY_REIMBURSEMENT.money
  is '报销金额';
comment on column SILLY_REIMBURSEMENT.file_group_id
  is '报销文件组ID';
comment on column SILLY_REIMBURSEMENT.remarks
  is '备注说明';

```

如需添加复杂数据，比如列表，参考saveHandle使用篇章

#### 代码自动生成

后端代码生成：

```java
SillyGenerateService service = SpringSillyContent.getBean(SillyGenerateService.class);
// 参数为 主表表名
service.generatorCode("silly_reimbursement");
```

前端代码生成（可在json配置文件中设置html内容）：

```java
SillyGenerateService service = SpringSillyContent.getBean(SillyGenerateService.class);
// 参数为 业务分类， 流程主键
service.generatorJsp("silly_reimbursement","silly_reimbursement_v1");
```



#### 运行测试

启动服务，部署流程图，进入模块页面中进行测试。



### 参数配置

#### yml 文件配置

```yaml
silly:
  # 启用start自动注入
  enabled: true
  # 实体类扫描的包匹配符
  entity-scan-package: com.crrcdt.**.entity
  # 流程JSON文件扫描路径匹配符
  process-pattern: classpath:silly/*.json
  # 流程JSON解析参数对象Clazz
  process-property-clazz:  com.iqiny.silly.core.config.property.impl.DefaultProcessProperty
```



#### json文件配置

具体参考 SillyJsonScheme 下说明

#### SillyJsonScheme

```json
{
  "type": "object",
  "properties": {
    "category": {
      "type": "string",
      "description": "业务分类，需要保证不重复"
    },
    "processDesc": {
      "type": "string",
      "description": "流程描述信息"
    },
    "lastProcessKey": {
      "type": "string",
      "description": "最新版本流程ID[支持SPEL]"
    },
    "lastProcessVersion": {
      "type": "string",
      "description": "最新版本流程版本号[支持SPEL]"
    },
    "master": {
      "type": "object",
      "description": "可配置多个不同流程， 流程ID：{流程信息}",
      "additionalProperties": {
        "$ref": "#/properties/master"
      },
      "properties": {
        "processVersion": {
          "type": "string",
          "description": "当前流程版本号"
        },
        "node": {
          "type": "object",
          "description": "配置节点信息，节点ID：{节点信息}",
          "additionalProperties": {
            "$ref": "#/properties/master/properties/node"
          },
          "default": {},
          "properties": {
            "nodeKey": {
              "type": "string",
              "description": "节点ID，不配置默认为当前KEY"
            },
            "nodeName": {
              "type": "string",
              "description": "节点名称"
            },
            "allowOtherVariable": {
              "type": "boolean",
              "description": "是否允许其他未定义的变量进行操作(是否可保存未定义的参数) 默认false"
            },
            "otherVariableThrowException": {
              "type": "boolean",
              "description": "存在未配置的变量是否抛出异常（数据校验）"
            },
            "isParallel": {
              "type": "boolean",
              "description": "是否为并行节点，默认false"
            },
            "ignoreFieldNames": {
              "type": "array",
              "description": "忽略的参数名称（不进行处置和保存）"
            },
            "variable": {
              "type": "object",
              "description": "配置当前节点的变量信息，变量名称：{变量信息}",
              "additionalProperties": {
                "$ref": "#/properties/master/properties/node/properties/variable"
              },
              "default": {},
              "properties": {
                "requestEl": {
                  "type": "string",
                  "description": "支持SPEL表达式，'1'为需要，'0'为不需要, 表达式需要以 “${”开头，“}”结尾"
                },
                "request": {
                  "type": "boolean",
                  "description": "是否必须项"
                },
                "desc": {
                  "type": "string",
                  "description": "描述字段"
                },
                "updatePropertyHandleValue": {
                  "type": "boolean",
                  "description": "是否更新此字段信息到处理器上下文中，默认true"
                },
                "belong": {
                  "type": "string",
                  "description": "此变量归属对象，但都会存到variable中",
                  "enum": [
                    "master",
                    "node",
                    "variable"
                  ]
                },
                "variableType": {
                  "type": "string",
                  "description": "变量类型，基础（string,list）其余的都是根据业务衍生出来的，通过实现SillyVariableConvertor接口可拓展[支持SPEL]",
                  "enum": [
                    "string",
                    "list",
                    "filegroup",
                    "filegroups",
                    "dict",
                    "dicts",
                    "user",
                    "users",
                    "office",
                    "offices",
                    "project",
                    "projects",
                    "station",
                    "stations",
                    "supplier",
                    "suppliers"
                  ]
                },
                "activitiHandler": {
                  "type": "string",
                  "description": "工作流变量类型，基础（string,list）其余的都是根据业务衍生出来的，通过实现SillyVariableConvertor接口可拓展[支持SPEL]",
                  "enum": [
                    "string",
                    "list"
                  ]
                },
                "defaultText": {
                  "type": "string",
                  "description": "变量的默认值，支持spel表达式，表达式需要以 “${”开头，“}”结尾"
                },
                "saveHandleNames": {
                  "type": "array",
                  "description": "变量保存处置器名称，可配置多个按配置的顺序执行，保存处置器可通过实现SillyVariableSaveHandle接口拓展",
                  "items": {
                    "type": "string",
                    "enum": [
                      "default",
                      "dataJoin",
                      "overwrite",
                      "save",
                      "skip",
                      "notifyUser"
                    ]
                  }
                },
                "htmlType": {
                  "type": "string",
                  "description": "HTML生成使用的参数",
                  "enum": [
                    "el-input",
                    "my-select",
                    "my-upload"
                  ]
                },
                "htmlConfig": {
                  "type": "object",
                  "description": "HTML生成使用的参数",
                  "properties": {
                    "tagName": {
                      "type": "string"
                    },
                    "fieldName": {
                      "type": "string"
                    },
                    "label": {
                      "type": "string"
                    },
                    "request": {
                      "type": "boolean"
                    },
                    "desc": {
                      "type": "string"
                    },
                    "requestDesc": {
                      "type": "string"
                    },
                    "params": {
                      "type": "object",
                      "additionalProperties": {
                        "$ref": "#/properties/master/properties/node/properties/variable/properties/htmlConfig/properties/params"
                      },
                      "properties": {
                        "dataList": {
                          "type": "array"
                        },
                        "buttonName": {
                          "type": "string"
                        }
                      }
                    },
                    "attrConfigs": {
                      "type": "object",
                      "additionalProperties": {
                        "$ref": "#/properties/master/properties/node/properties/variable/properties/htmlConfig/properties/attrConfigs/properties/attrConfigsName"
                      },
                      "properties": {
                        "attrConfigsName": {
                          "type": "string"
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  },
  "default": true
}
```

