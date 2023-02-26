# 简介
实现在 matlab 中使用 java 的 ThreadPool。

支持所有 java 中 `Runtime.exec()` 能够支持的指令（即系统指令），
使用 `SystemThreadPool.getTaskNumber()` 获取正在执行以及排队的任务数目，
`SystemThreadPool.waitUntilDone()` 等待直到所有任务执行完毕。

因此可以方便的在 matlab 中并行执行第三方程序，或者提交任务（需要是提交后直接挂起终端的任务，这样 ThreadPool 会自动管理，等有空闲进程的时候自动将排队的任务提交）等等。

相比 matlab 中的 Parallel Computing Toolbox，响应速度更快，占用内存更少，并行数目的设置更加灵活。

增加了一个实用的 `waitPools(pools)` 方法，可以等待传入的线程池全部执行完成，并且期间会绘制进度条。


# 使用方法
从 [Release](https://github.com/CHanzyLazer/Matlab-JavaThreadPool/releases/tag/v1.0) 中下载压缩包，
解压后可以参看其中的 `demo.m` 文件作为一个基本的实用例子。

具体为，首先需要导入 include 目录下的 matlab 函数以及 java 类：
```matlab
addpath('include');
javaaddpath('include');
```

直接创建一个 `SystemThreadPool`，这里指定并行数目为 4：
```matlab
pool = SystemThreadPool(4);
```

向线程池中添加任务，这里高度包装，在 windows 上直接传入需要执行的 cmd 指令即可：
```matlab
for i=1:20
  pool.submitSystem(['powershell Start-Sleep -Seconds ' num2str(randi(10))]); 
end
```
这里的指令意思是随机 sleep 一段时间，一共添加了 20 个这样的任务。

此时 matlab 会直接继续指定后续代码，可以使用 `waitPools` 来等待执行完成并且输出进度条：
```matlab
waitPools(pool);
```
其中 `waitPools` 可以像这样传入单个 pool 或者像 `demo.m` 中一样传入一个 cell 包装的 pool 数组。

最后记得关闭线程池：
```matlab
pool.shutdown();
clear pool;
```


# 代码部分
下载源码后，执行 `include/build.bat` 可以将 include 目录下的 java 源码编译成 class 文件，从而使得 matlab 可以识别。
需要系统拥有 jdk，可以从 [这里](https://mirrors.tuna.tsinghua.edu.cn/Adoptium/) 下载 jdk（建议使用 jdk11，这里本人使用的是 jdk8 不过对于这种简单项目应该不影响）。


# License
This code is licensed under the [MIT License](LICENSE).
