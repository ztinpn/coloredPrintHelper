# 功能

将pdf分成彩色和黑白部分，便于打印，不用再一页一页记录彩色页面位置了！！支持双面和单面打印。

# 注意

受到打印机的限制，如果是双面打印的话，实际上是两页两页进行的，例如第1和第2页，只要有一个是彩色的，就需要都彩打。

# 用法

需要JAVA运行时环境支持，可到 http://rj.baidu.com/soft/detail/10463.html 下载并安装

下载 https://github.com/ztinpn/coloredPrintHelper/archive/master.zip 并解压，把待处理的pdf文件拷到coloredPrintHelper.jar和process.bat的同文件夹下，记事本打开process.bat，把 "pdf文件名.pdf" 修改为对应的pdf文件名，如果是单面打印的话，把“双面打印”改为“单面打印”，保存。

双击process.bat即可，将处理并生成
- pdf文件名.pdf_彩色部分_x面打印.pdf
- pdf文件名.pdf_黑白部分_x面打印.pdf

同时也输出了页码号，复制即可，如果有误也便于修改。。

