package com.nameof;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.sql.*;
import java.util.Enumeration;
import java.util.StringJoiner;

public class JdbcDriverSwitcherTest {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mysql?serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String JAR_FILEDIR = "C:\\Users\\chengpan\\Desktop";
    private static final String MYSQL5149 = "mysql-connector-java-5.1.49.jar";
    private static final String MYSQL5145 = "mysql-connector-java-5.1.45-bin.jar";
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

    @Test
    public void testSwitch() throws Exception {
        switch5145To5149();

        Enumeration<Driver> drivers = DriverManager.getDrivers();
        int count = 0;
        while (drivers.hasMoreElements()) {
            drivers.nextElement();
            count += 1;
        }
        Assert.assertEquals(1, count);

        JdbcDriverSwitcher.getInstance().unloadDriver(DatabaseType.MYSQL);
    }

    @Test
    public void testReadData() throws Exception {
        switch5145To5149();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("show tables");
            StringJoiner tables = new StringJoiner(",");
            while(rs.next()) {
                tables.add(String.valueOf(rs.getObject(1)));
            }
            Assert.assertTrue(tables.toString().contains("user"));
        }

        JdbcDriverSwitcher.getInstance().unloadDriver(DatabaseType.MYSQL);
    }

    @Test
    public void testEnsureDriverLocation() throws Exception {
        JdbcDriverSwitcher switcher = JdbcDriverSwitcher.getInstance();

        File jar5145File = new File(JAR_FILEDIR, MYSQL5145);
        switcher.switchToDriver(DatabaseType.MYSQL, jar5145File.getAbsolutePath(), DRIVER_CLASS);
        assertDriverLocation(MYSQL5145);

        File jar5149File = new File(JAR_FILEDIR, MYSQL5149);
        switcher.switchToDriver(DatabaseType.MYSQL, jar5149File.getAbsolutePath(), DRIVER_CLASS);
        assertDriverLocation(MYSQL5149);

        switcher.unloadDriver(DatabaseType.MYSQL);
    }

    private void switch5145To5149() throws Exception {
        JdbcDriverSwitcher switcher = JdbcDriverSwitcher.getInstance();

        File jar5145File = new File(JAR_FILEDIR, MYSQL5145);
        switcher.switchToDriver(DatabaseType.MYSQL, jar5145File.getAbsolutePath(), DRIVER_CLASS);

        File jar5149File = new File(JAR_FILEDIR, MYSQL5149);
        switcher.switchToDriver(DatabaseType.MYSQL, jar5149File.getAbsolutePath(), DRIVER_CLASS);
    }

    /**
     * 断言当前Driver的实现类是否加载于期望的jar文件，即切换Driver是否生效
     */
    private void assertDriverLocation(String exceptedJarName) throws Exception {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            Class<? extends Statement> statementClass = conn.createStatement().getClass();
            Assert.assertTrue(statementClass.getProtectionDomain()
                    .getCodeSource().getLocation().toString()
                    .contains(exceptedJarName));
        }
    }
}
