# 傻瓜工作流 -  SILLY ACTIVITI

##### 20200915 - 1.0.1-RELEASE 本工具目前正处于孵化阶段，正式版本及demo敬请期待

##### 20210207 - 1.0.1 集成简易DEMO，未完成

##### 20210316 :smile:  -  **1.0.2-RELEASE 后端服务JAR正式发布可用** ！！！

#### 介绍

##### 缺点：性能不高、占用大量数据空间

##### 优点：急速开发、无需表设计，拓展性强、支持复杂的各种数据结构、支持多版本同时执行

##### Future：1.前端工具包（保存、显示、表单组件）、2.历史数据仓库功能、3.代码自动生成

非常适合性能要求不高，但是需求变化多，流程多版本兼容跑，数据结构复杂多变的系统引入【制造业、政府行业等】

傻瓜式操作工作流业务数据

1. 流程控制，我不管（用现成的各种流程引擎框架）

2. 业务数据，我管理（傻瓜工作流，简单上手）

3. 核心对象简介：

   1.   **master** : 业务主对象，表示业务主体，可存放关键数据

   2.   **node** : 操作节点对象，类似一个人的一步操作，操作信息对象

   3.   **variable** : 操作节点的具体数据对象（可根据转换器适配多种业务场景，如：转换变量的id与name，大数据内容转base64另存）

   4. ​    **sillyConfig** : 傻瓜配置对象，一般一个服务使用一个配置对象，也可根据业务分类 Category 一一适配

      

#### 仅需要实现 

​    后台：【一个流程引擎服务对象 + 具体业务对象实体属性（每种业务三个） + 业务ReadService + 业务WriterService】
​    前端：按业务节点，画页面，有模板
​    数据库： 每种业务 基本三张数据表
即可完成整套业务的 【流程数据 + 业务数据 + 状态控制 + 版本控制】

#### 软件架构

软件架构说明 

1. 流程引擎一般交由第三方如 Activiti 实现，本工具未实现自身的流程引擎，引用 silly-activiti 包及可集成activiti 
2. 业务数据拆分为三块 【1.Master 主表】【2.Node 节点表】 【3.Variable 变量表】 通过三者的组合管理业务数据

#### 使用教程 具体可参考本项目下示例代码silly-activiti（前端功能未完善）

```
  <!-- 实际用法 直接引入全部依赖 正在开发中 -->      
  <dependency>
      <groupId>com.iqiny.silly</groupId>
      <artifactId>silly-all</artifactId>
      <version>1.0.2-RELEASE</version>
  </dependency>
```

##### 按上述方式引入会依赖: 

​		1: activiti 7.0 （若不是7.0，请重写下 SillyActivitiEngineServiceImpl， 因为7.0 查询返回接口对象（TaskQuery/Query）与之前版本不一致） 
​        2: spring 
​        3: mybatis-plus

项目中 引用，若想分开引入可到maven 仓库搜索引入 （1.0.1-RELEASE 及之前的版本为测试版本，请 **不要引用** ）

实现 AbstractSillyReadService 及 AbstractSillyWriteService，具体可参考本项目下示例代码silly-activiti（前端功能未完善）

#### 参与贡献（欢迎各位大佬指导）

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request