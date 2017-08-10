# dubbo-monitor
## 开发技术栈
- ~~mybatils~~ 
   - mybatils generator
   - pagehelper
- spring boot
- spring mvc
- spring event
- shiro
- dubbox
- thymeleaf 3
- adminLTE (plugin...)
- echarts
- ~~quartz2.x~~

## feature list
## 基础平台

### websocket  push(2017-7-31)

增加了websocket 实时推送，当后台接收到信息变更时，通知客户端变更信息 

### thymeleaf 的局部变更(2017-7-31)

优化了页面，使用局部加载方式加载Main-content

### mybatils generator
-    新增自动生成services插件

 **注意：**  因为其内部使用了mbg的protected继承，所以直接修改了源代码

```
<plugin type="org.mybatis.generator.plugin.MybatisServicePlugin">
			<property name="targetPackage" value="${servicePackage}" />
			<property name="targetProject" value="${targetProject}" />
</plugin>

```

-   格式化mapper xml services生成包路径

 **注意：**  因为其内部使用了mbg的protected继承，所以直接修改了源代码

```
<plugin type="org.mybatis.generator.codegen.mybatis3.RenameModuleNamePlugin">

```

 t_[model]_name 会按照Model进行分包便于管理和查看


-  :fa-times: ~~MGB重新生成后,保留原有更新内容~~

### spring boot 与dubbx集成
   
- 通过配置的方式完成dubbo配置，不再需要xml文件，可以集成至YML配置

```
dubbo: 
   registryAddress: zookeeper://192.168.31.243:2181
   registryCheck: false
   registryTimeout: 5000
   applicationName: njwd-dubbo-monitor
    //dubbo services 通过扫描包完成注册
   annotationPackage: com.njwd.rpc.monitor.core.dubbo
   protocolName: dubbo
   protocolPort: 20881
```

### ~~后台任务调度~~
- ~~可视化任务管理~~
- ~~可视化任务运行~~
- ~~可视化任务监控~~

###    shiro spring boot集成，完成RBAC权限管理
-   与redis集成后支持session共享 
-   权限、角色的灵活分配.与数据库中的权限完成动态注册到shiro权限中
-   当客户端（有可能是后台接口）不支持cookie的情况下，支持token与session的关联
-   shiro 的thyemleaf方言 

###   Swagger 集成
  可以生成spring rest 接口调用平台和调用文档
## 实时的dubbo运行服务管理
#### 配置管理  

-   按照服务的维护查看当前服务的consumer、provider、config、route
-    服务的配置的精细化操作。 当熟悉dubbo元数据后可以通过后台的配置完成dubbo的精细化操作。比如 按条件路由、集群权重分配、集群负载均衡

    -   完成动态路由的新增和删除（2017-7-31）

#### 服务设置
  按照comsumer provder的维度提供服务展示

#### 调用关系
  通过节点的形式查看节点与节点之间的调用关系生成的关系图


## 运行统计
### 全量运行统计   
  以服务的维度查看当前维度的调用统计

###  服务整体运行统计
###  服务分comsumer provdier 运行统计
-   后端处理
-   按照以分钟的维度查看某一个服务comsuer/provider的服务统计
-    按照以小时的维度查看某一个服务comsuer/provider的服务统计

###  服务方法运行统计
-   后端处理
-    按照以分钟的维度查看某一个服务comsuer/provider 方法的服务统计
-    按照以小时的维度查看某一个服务comsuer/provider 方法的服务统计

##    监控管理
####    监控规则管理(2017-7-31)
####    报警列表
####    处理管理


##   系统设置
###   权限设置
###   用户设置
###   菜单设置



