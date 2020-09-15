# jdbc-driver-switcher
Dynamically switch the JDBC driver during runtime. 运行时切换不同版本的JDBC驱动

## 理论知识
* 类的唯一性由类自身和加载它的类加载器实例决定
* DriverManager只允许你访问调用方所属类加载器能加载的Driver
* 一个类的类加载器，会被用来加载这个类所依赖的类
