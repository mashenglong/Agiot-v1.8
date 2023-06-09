**2022 年 1月 9 号 开发**

在之前的基础上添加了日志模块
将之前代码中的硬编码修改为文件配置方式，增加了程序的可扩展性

**2022.10.20**

更新readme文件，分析项目目录
启动：main->queueHelper().start->disruptor构建mysql和mqtt服务
数据处理：SerialPortListener()通过com接收数据根据串口属性解析数据
