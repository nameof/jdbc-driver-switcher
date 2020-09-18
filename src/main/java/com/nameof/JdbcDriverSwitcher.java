package com.nameof;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class JdbcDriverSwitcher {

    private Map<DatabaseType, Object> locks;
    private Map<DatabaseType, Driver> driverMap;
    private DriverLoader driverLoader = new DriverLoader();

    public JdbcDriverSwitcher() {
        driverMap = new HashMap<>(DatabaseType.values().length);
        createLocks();
    }

    public boolean switchToDriver(DatabaseType type, String jarFilePath, String driverClass) throws Exception {
        Object lock = locks.get(type);
        synchronized (lock) {
            return trySwitchDriver(type, jarFilePath, driverClass);
        }
    }

    private boolean trySwitchDriver(DatabaseType type, String jarFilePath, String driverClass) throws Exception {
        DriverWrapper wrapper = driverLoader.loadDriver(jarFilePath, driverClass);

        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getName().equals(driverClass)) {
                DriverManager.deregisterDriver(driver);
            }
        }

        DriverManager.registerDriver(wrapper);
        DriverManager.deregisterDriver(driverMap.put(type, wrapper));
        return true;
    }

    private void createLocks() {
        locks = new HashMap<>(DatabaseType.values().length);
        for (DatabaseType type : DatabaseType.values()) {
            locks.put(type, new Object());
        }
    }

}