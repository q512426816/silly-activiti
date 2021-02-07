# 傻瓜工作流 -  SILLY ACTIVITI

20200915 - 1.0.1-RELEASE 本工具目前正处于孵化阶段，正式版本及demo敬请期待
20210207 - 1.0.1 集成简易DEMO，未完成

#### 介绍
傻瓜式操作工作流业务数据
1. 流程控制，我不管（用现成的各种流程引擎框架）
2. 业务数据，我管理（傻瓜工作流，简单上手）

仅需要实现 
    后台：【一个流程引擎服务对象 + 具体业务对象实体属性（每种业务三个） + 业务ReadService + 业务WriterService】
    前端：按业务节点，画页面，有模板
    数据库： 每种业务 基本三张数据表
即可完成整套业务的 【流程数据 + 业务数据 + 状态控制 + 版本控制】

#### 软件架构
软件架构说明 
1.  流程引擎一般交由第三方如 Activiti 实现，本工具未实现自身的流程引擎，引用 silly-activiti 包及可集成activiti 
2.  业务数据拆分为三块 【1.Master 主表】【2.Node 节点表】 【3.Variable 变量表】 通过三者的组合管理业务数据


#### 使用教程

项目中 引用
```        
  <!-- 实际用法 demo 正在开发中 -->      
  <dependency>
      <groupId>com.iqiny.silly</groupId>
      <artifactId>silly-all</artifactId>
      <version>1.0.1-RELEASE</version>
  </dependency>
    <!-- PS: MAVEN 私钥弄丢了，密码忘记了怎么办 -->  
```
实现 AbstractSillyReadService 及 AbstractSillyWriteService

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


