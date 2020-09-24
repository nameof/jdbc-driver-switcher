package com.nameof;


import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * DriverManager存在隔离机制，只允许你访问调用方所属类加载器实例能加载的Driver（即驱动类和调用者类由同一类加载器实例加载），例如普通Java程序使用的System Classloader
 * 所以，使用URLClassLoader去手动加载Driver类，并不能直接注册到DriverManager使用
 * 需要DriverWrapper这样一个普通类（这个类由调用者的System Classloader等加载器加载）包装，交给DriverManager使用
 *
 * @see java.sql.DriverManager#isDriverAllowed
 *
 * https://stackoverflow.com/questions/14478870/dynamically-load-the-jdbc-driver
 * https://www.kfu.com/~nsayer/Java/dyn-jdbc.html
 */
public class DriverWrapper implements Driver {

    private Driver driver;

    public DriverWrapper(Driver d) {
        this.driver = d;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return this.driver.connect(url, info);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return this.driver.acceptsURL(url);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return this.driver.getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return this.driver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return this.driver.getMinorVersion();
    }

    @Override
    public boolean jdbcCompliant() {
        return this.driver.jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.driver.getParentLogger();
    }

    public Driver getDriver() {
        return driver;
    }
}
