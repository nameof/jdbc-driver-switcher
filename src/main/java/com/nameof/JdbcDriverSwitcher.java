package com.nameof;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JdbcDriverSwitcher {

    private Map<DatabaseType, Object> locks;
    private Map<DatabaseType, Driver> driverMap = new HashMap<>(DatabaseType.values().length);;
    private DriverLoader driverLoader = new DriverLoader();
    private DriverManagerSideCar dm = new DriverManagerSideCar();

    public JdbcDriverSwitcher() {
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

        // clear default driver
        dm.deregisterDriverByClassName(driverClass);

        replacePreviousDriver(type, wrapper);
        return true;
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
        return (Driver) ReflectionUtils.getFieldValue(wrapper, "driver");
    }

    private void createLocks() {
        locks = new HashMap<>(DatabaseType.values().length);
        for (DatabaseType type : DatabaseType.values()) {
            locks.put(type, new Object());
        }
    }

}