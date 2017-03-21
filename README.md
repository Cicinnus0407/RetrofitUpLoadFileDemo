# RxJava2+Retrofit 单文件上传加进度监听封装
- 注： 工程使用最新的AS2.3版本和gradle plugin 2.3，如果无法正常运行请修改对应的本地版本即可。
- 工程中的UploadFileServer目录是服务器代码，直接用Eclipse for JavaEE编译运行即可。
- 记得修改Android工程中的File文件和服务器地址url，同时保证手机/模拟器和服务器运行在同一网络（同Wifi或者电脑开启Wifi）
- 查看本机ip地址和修改AS工程url方法：Win键+R 输入cmd调出控制台，输入ipconfig ,IPv4地址即为本机地址，（如：192.168.199.232）
  则AS的接口url为：http://192.168.199.232:8080/UploadFileServer/servlet/UploadHandleServlet
