  

# 在线OJ项目重构

目的：之前的OJ项目主要是增删改查，本次重构主要是为了提升对前端技术的掌握和多了解一些docker的知识

![image-20230729172554586](doc/image-20230729172554586.png)

![image-20230729182457686](doc/image-20230729182457686.png)

主要开发功能设计：

1. 题目模块
   1. 题目的增删改查
   2. 搜索题目
   3. 在线做题
   4. 提交题目
2. 用户
   1. 登录
   2. 注册
3. 判断题目模块
   1. 提交题目（判断结果对错）
   2. 错误处理（内存溢出、安全性、超时）
   3. 实现代码沙箱（隔离环境）
   4. （接口）



### 技术实现

Vue3，Java进程控制，虚拟机，Docker，消息队列

开始开发：

## 2023-7-29

使用组件库：

> https://arco.design/react/docs/start

使用vue-router动态路由

使用vuex全局状态管理

## 2023-7-30

使用vue-router的权限管理

需要在全局页面组件中，绑定一个全局路由监听，每次访问页面的时候，根据用户需要访问的页面的路由信息，先判断用户是否有访问权限

如果有，跳转，如果没有，就拦截或者是跳转到403或者401鉴权或者是登录页面

##### 如何根据权限隐藏菜单

在 routers.ts 中给路由新增一个标志位，用于判断路由是否显隐

不要使用v-if + v-for 去进行条件渲染，会导致性能的浪费

所以先过滤需要展示的页面

需要抽象出一个全局的权限管理



后端项目初始化

然后后端暴露swagger接口，然后使用代码生成工具构建请求函数

> https://github.com/ferdikoomen/openapi-typescript-codegen

```
openapi --input http://localhost:8123/api/v2/api-docs --output ./generated --client axios
```

## 2023-8-7

数据库字段：

使用 judgeConfig 判题配置（json对象）

- 时间限制 timeLimit
- 内存限制 memoryLimit

judge 判题用例（json数组）

每个元素是：一个输入用例和一个输出用例

```json
[
    {
        "input": "1 2",
        "output": "3 4"
    },
    {
        "input": "1 3",
        "output": "2 4"
    }
]
```

存json的前提：

1. 不需要根据json中的某个字段去查询这条数据
2. 你的字段含义相关，属于同一类的值
3. 你的字段存储空间不能占用太大

判题信息：

```json
judgeInfo
{
    "message": "执行信息",
    "time": 1000, // ms
    "memory": 1000 // kb
}
```

判题枚举值：

- Accepted 成功
- Wrong Answer 失败错误
- Compile Error 编译错误
- Memory Limit Exceeded 内存溢出
- Time Limit Exceeded 超时
- Presentation Error 展示错误
- Output Limit Exceeded 输出溢出
- Waiting 等待中
- Dangerous Operation 危险操作
- Runtime Error 运行错误（用户程序问题）
- System Error 系统错误（系统内的问题）

## 2023-8-8

编写独立的实体类entity，DTO，VO等

## 2023-8-14

使用md编辑器：

> [bytedance/bytemd: Hackable Markdown Editor and Viewer (github.com)](https://github.com/bytedance/bytemd)

引入代码编辑器： monaco-editor

> [microsoft/monaco-editor: A browser based code editor (github.com)](https://github.com/microsoft/monaco-editor)
>
> npm install monaco-editor

> 安装：使得代码编辑器和webpack整合
>
> npm install monaco-editor-webpack-plugin

vue-cli项目/ webpack项目整合 Webpack项目

```js
const { defineConfig } = require("@vue/cli-service");
const MonacoWebpackPlugin = require("monaco-editor-webpack-plugin");
module.exports = defineConfig({
  transpileDependencies: true,
  chainWebpack(config) {
    config.plugin(new MonacoWebpackPlugin({}));
  },
});
```

![image-20230815231947462](doc/image-20230815231947462.png)

## 2023-9-7

### 代码沙箱的开发

定义代码沙箱的接口，提高通用性

之后的项目代码只调用接口，不调用具体的实现类，这样在使用其他的代码沙箱实现类的时候，就不用去修改名称了，便于拓展

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
```

使用工厂模式，根据用户传入的字符串参数，来生成对应的代码沙箱实现类

此处使用静态工厂某事，实现比较简单，符合需求

参数配置化。。。

##### 如何进行代码沙箱能力增强：

比如在调用代码沙箱前，输出请求参数日志，在代码沙箱调用后，输出响应结果日志，便于管理员去分析

使用代码模式，提供一个Proxy来增强代码沙箱的能力

使用代理后，不仅不用改变原来的代码沙箱实现类，而且对于调用者来说，调用方式没有改变

### 判题服务完整业务流程开发

1）传入题目提交id，获取到对应的题、提交信息（包含代码、编程语言等）

如果题目的提交状态不为等待中，就不需要再提交了

2）更改题目提交的状态为 判题中，防止重复执行，也能让用户堪当状态

2）调用沙箱，获取到题目执行结果

3）根据沙箱的执行结果，设置题目的判题状态和信息

```
if (status.equals(QuestionSubmitStatusEnum.Failed.getValue())) {
            // 代码执行返回异常
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.Failed.getValue());
            questionSubmitUpdate.setJudgeInfo(gson.toJson(judgeInfo));
            boolean updateById = questionSubmitService.updateById(questionSubmitUpdate);
            ThrowUtils.throwIf(!updateById, ErrorCode.SYSTEM_ERROR, "修改提交状态异常");
            throw new BusinessException(50005, "执行代码异常:" + judgeInfo.getMessage());
        }

        Boolean res = judgeOutputList(outputList, outputListByExecute);
        if (res.equals(true)) {
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
            questionSubmitUpdate.setJudgeInfo(gson.toJson(judgeInfo));
            boolean updateById = questionSubmitService.updateById(questionSubmitUpdate);
            ThrowUtils.throwIf(!updateById, ErrorCode.SYSTEM_ERROR, "修改提交状态异常");
        } else {
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.Failed.getValue());
            questionSubmitUpdate.setJudgeInfo(gson.toJson(judgeInfo));
            boolean updateById = questionSubmitService.updateById(questionSubmitUpdate);
            ThrowUtils.throwIf(!updateById, ErrorCode.SYSTEM_ERROR, "修改提交状态异常");
        }

        //设置返回结果并返回
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        questionSubmitVO.setJudgeInfo(judgeInfo);
        questionSubmitVO.setStatus(status);
```

这里需要使用策略模式，根据不同的情况进行判题



#### 主要流程

##### 1. 获取到代码

通过读取文件流的方式

##### 2. 通过调用命令行的方式进行编译

```java
String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
Process compileProcess = Runtime.getRuntime().exec(compileCmd);
```

##### 3. 执行程序

```
String runCmd = String.format("java -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
```

需要注意编码问题，要指定编码

##### 4. 最后整理到输出结果

得到运行时间：使用StopWatch

##### 5. 还需要进行文件清理

##### 6. 异常错误返回空数据

## 2023-9-10

处理危险代码带来的问题

#### 1. 超时控制

通过创建一个守护线程，超时后中断process实现

```
// 超时控制
FutureTask<Integer> futureTask = new FutureTask<>(() -> {
    try {
        Thread.sleep(TIME_OUT);
        if (process.isAlive()) {
            process.destroy();
            System.out.println("超时了，中断");
            // 有一个中断就不执行后面可能有问题的输入了
            return 1;
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
  return 0;
});
new Thread(futureTask).start();
Integer res = futureTask.get();
if (res == 1) {
    ifLongTime = 1;
    break;
}
```



#### 2. 限制资源的分配

不能够让每个Java进程的执行占用的JVM最大内存空间都和系统的一致，实际上应该小一点，比如说256MB

启动Java时，可以指定JVM的参数，-Xmx256m

```
java -Xmx256m
```

这里使用的JVM的堆内存限制，不等于系统实际占用的最大资源，可能会超出

如果需要更为严格的内存限制，需要到系统层面进行限制

如果时Linux系统，可以使用cgroup来实现对某个进程的cpu、内存等资源的分配

#### 3. 限制代码

定义黑白名单，使用工具库的中的WordTree，提手匹配效率

#### 4. 限制用户的操作权限

限制用户对文件、内存、CPU、网络等资源的操作和访问

使用Java安全管理器来实现更严格的限制

首先是创建一个类继承SecurityManager

```java
package com.yt.ytojcodesandbox.security;

import java.security.Permission;

public class MySecurityManager extends SecurityManager{

    @Override
    public void checkPermission(Permission perm) {
        // 不做任何限制
        //super.checkPermission(perm);
    }

    // 检测程序是否可执行文件
    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("checkExec 权限异常:" + cmd);
    }

    // 检测程序是否可以读文件
    @Override
    public void checkRead(String file) {
        throw new SecurityException("checkRead 权限异常:" + file);
    }

    // 检测程序是否允许写文件
    @Override
    public void checkWrite(String file) {
        throw new SecurityException("checkWrite 权限异常:" + file);
    }

    // 检测程序是否允许删除文件
    @Override
    public void checkDelete(String file) {
        throw new SecurityException("checkDelete 权限异常:" + file);
    }

    // 检测程序是否允许连接网络
    @Override
    public void checkConnect(String host, int port) {
        throw new SecurityException("checkConnect 权限异常:" + host + ":" + port);
    }
}
```

这里尽量限制用户输入代码的权限

想要在用户输入的所有代码中统一加上这个manager限制，需要在运行时的命令行中进行限制

```
java -Xmx256m -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=MySecurityManager Main %s
```

这里需要注意，在windows中路径是使用%s;%s，在linux下是使用%s:%s

然后需要先编译

```
javac -encoding utf-8 ./MySecurityManager.java
```

然后需要将编译后的路径指定到代码中

```java
private static final String SECURITY_MANAGER_PATH = "E:\\gitee\\yt-oj-project\\ytoj-code-sandbox\\src\\main\\resources\\security";

public static final String SECURITY_MANAGER_CLASS_NAME = "MySecurityManager";
```

```java
String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=%s Main %s",
                    userCodeParentPath, SECURITY_MANAGER_PATH, SECURITY_MANAGER_CLASS_NAME, inputArgs);
```



## 2023-9-11

使用代码沙箱进行隔离

之前都是在代码层面进行隔离，所以只要是在代码层面出现漏洞就会出现问题

为了提升系统的安全性，把不同的程序和宿主机进行隔离，使得某个程序的执行不会影响到系统本身

![image-20230911224313097](doc/image-20230911224313097.png)

1）Docker运行在Linux内核上

2）CGroups：实现容器的资源隔离，底层是Linux Cgroup命令，能够控制进程使用的资源

3）Network网络：实现容器的网络隔离，docker容器内部的网络互不影响

4）Namespaces命名空间：可以把进程隔离在不同的命名空间下，每个容器他都可以有自己的命名空间，不同的命名空间下的进程互不影响

5）Storage存储空间，容器内部的文件是相互隔离的，也可以去使用宿主机的文件

地址：https://hub.docker.com/

> docker create xxx 启动实例，得到容器实例containerId

docker ps -a 查看容器状态



## 2023-9-12

要使用Java操作docker https://github.com/docker-java/docker-java

```xml
<!-- https://mvnrepository.com/artifact/com.github.docker-java/docker-java -->
<dependency>
    <groupId>com.github.docker-java</groupId>
    <artifactId>docker-java</artifactId>
    <version>3.3.0</version>
</dependency>
<!-- https://mvnrepository.com/artifact/com.github.docker-java/docker-java-transport-httpclient5 -->
<dependency>
    <groupId>com.github.docker-java</groupId>
    <artifactId>docker-java-transport-httpclient5</artifactId>
    <version>3.3.0</version>
</dependency>
```

DockerClientConfig：用于定义初始化DockerClient 的配置（类比MySQL的连接、线程数配置）

DockerHttpClient：用于向Docker守护进程（操作Docker的接口）发送请求的的客户端，低层封装，不推荐使用，要自己构建请求参数

DockerClient：才是真正和Docker守护进程交互的、最方便的SDK，高层封装，对DockerHttpClient再进行了一层封装
