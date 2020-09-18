package com.nameof;

import org.junit.Assert;
import org.junit.Test;

public class JdbcDriverSwitcherTest {
    @Test
    public void testSwitch() throws Exception {
        JdbcDriverSwitcher switcher = new JdbcDriverSwitcher();
        String jarFilePath = "C:\\Users\\chengpan\\Desktop\\mysql-connector-java-5.1.49.jar";
        String driverClass = "com.mysql.jdbc.Driver";
        Assert.assertTrue(switcher.switchToDriver(DatabaseType.MYSQL, jarFilePath, driverClass));
    }
}
