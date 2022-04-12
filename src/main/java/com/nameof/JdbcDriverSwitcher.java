package com.nameof;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * thread safe
 */
public class JdbcDriverSwitcher {

    private static final JdbcDriverSwitcher INSTANCE = new JdbcDriverSwitcher();

    private Map<DatabaseType, Object> locks;
    private Map<DatabaseType, Driver> driverMap = new HashMap<>(DatabaseType.values().length);;
    private DriverLoader driverLoader = new DriverLoader();
    private DriverManagerSideCar dm = new DriverManagerSideCar();

    private JdbcDriverSwitcher() {
        createLocks();
    }

    public static JdbcDriverSwitcher getInstance() {
        return INSTANCE;
    }

    public Connection getConnection(DatabaseType type, String jarFilePath, String driverClass
            , String url, String user, String password) throws Exception {
        Object lock = locks.get(type);
        synchronized (lock) {
            try {
                switchToDriver(type, jarFilePath, driverClass);
                return DriverManager.getConnection(url, user, password);
            } finally {
                unloadDriver(type);
            }
        }
    }

    public void switchToDriver(DatabaseType type, String jarFilePath, String driverClass) throws Exception {
        Object lock = locks.get(type);
        synchronized (lock) {
            DriverWrapper wrapper = driverLoader.loadDriver(jarFilePath, driverClass);

            // clear default driver
            dm.deregisterDriverByClassName(driverClass);

            replacePreviousDriver(type, wrapper);
        }
    }

    private void replacePreviousDriver(DatabaseType type, DriverWrapper wrapper) throws SQLException {
        dm.replaceDriver(driverMap.put(type, wrapper), wrapper);
    }

    public void unloadDriver(DatabaseType type) throws Exception {
        Object lock = locks.get(type);
        synchronized (lock) {
            dm.unLoadDriver(driverMap.remove(type));
        }
    }

    public Driver getCurrentDriver(DatabaseType type) {
        return driverMap.get(type);
    }

    public Driver getCurrentRawDriver(DatabaseType type) throws Exception {
        DriverWrapper wrapper = (DriverWrapper) this.getCurrentDriver(type);
        return wrapper.getDriver();
    }

    private void createLocks() {
        locks = new HashMap<>(DatabaseType.values().length);
        for (DatabaseType type : DatabaseType.values()) {
            locks.put(type, new Object());
        }
    }

}