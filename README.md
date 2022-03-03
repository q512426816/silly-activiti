# SILLY-ACTIVITI 文档

### silly-activiti 是什么？

​		silly-activiti是流程中业务数据管理工具，致力于提供傻瓜式的数据管理服务。



### 快速开始

#### 使用DEMO： https://gitee.com/iqiny/silly-activiti-demo



### 结构说明

```
- silly
	+ actviti 		[集成 activiti 工作流，涉及流程引擎服务]
	+ all			[打包控制]
	+ bom			[打包版本依赖控制]
	+ common		[工具类]
	+ core			[核心包]
	+ mybatisplus	[集成 mybatis-plus, 涉及业务数据读写服务]
	+ spring		[集成 spring，涉及工具容器]
	+ starter		[集成 springboot-starter，无需配置即可使用]
	+ style			[代码 copyright]
	
	针对核心包的说明：
	- core
		+ base			[基础接口]
		+ cache			[缓存接口]
		+ common		[核心工具]
		+ config		[工具配置]
		+ convertor		[数据转换器]
		+ engine		[流程引擎接口]
		+ group			[任务组接口]
		+ read			[读取相关对象]
		+ readhandle	[读取相关处置器]
		+ resume		[流程履历接口]
		+ savehandle	[保存相关处置器]
		+ service		[傻瓜服务接口]
	
```



### 使用这玩意有什么好处

```
1. 可独立的流程引擎服务，可更换不同引擎服务，耦合性低，方便升级引擎。
2. 基于json配置各个节点信息，便于控制及问题排查。
3. json内容动态解析，支持SPEL，支持更多业务逻辑，让代码更简洁。
4. 内置json-scheme，防止错误填写。
5. 内置各种handle方便随时随地切入，进行逻辑处理。

还有很多功能，不一一介绍了，期待你的参与~
```

