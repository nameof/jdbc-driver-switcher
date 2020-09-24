# jdbc-driver-switcher
Dynamically switch the JDBC driver during runtime. 运行时切换不同版本的JDBC驱动

## 理论知识
* 类的唯一性由类自身和加载它的类加载器实例决定
* DriverManager只允许你访问调用方所属类加载器实例能加载的Driver，例如普通Java程序使用的System Classloader
* 一个类的类加载器，会被用来加载这个类所依赖的类

## 使用
```
    mvn clean install -DskipTests
```
```
    <dependency>
        <groupId>com.nameof</groupId>
        <artifactId>jdbc-driver-switcher</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
```
```
    JdbcDriverSwitcher switcher = JdbcDriverSwitcher.getInstance(); // 单例，线程安全
    String jarFilePath = ""; // jar文件绝对路径
    String driverClass = "com.mysql.jdbc.Driver";
    switcher.switchToDriver(DatabaseType.MYSQL, jarFilePath, driverClass);
    
    // ... 切换完成，获取连接，读写数据
```