package com.nameof;

import org.junit.Assert;
import org.junit.Test;

public class JdbcDriverSwitcherTest {
    @Test
    public void testSwitch() {
        JdbcDriverSwitcher switcher = new JdbcDriverSwitcher();
        String jarFilePath = "";
        String driverClass = "";
        Assert.assertTrue(switcher.switchToDriver(DatabaseType.ORACLE, jarFilePath, driverClass));
    }
}
