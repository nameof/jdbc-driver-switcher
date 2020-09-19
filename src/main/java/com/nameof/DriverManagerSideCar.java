package com.nameof;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class DriverManagerSideCar {
    void deregisterDriverByClassName(String driverClass) throws SQLException {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getName().equals(driverClass)) {
                DriverManager.deregisterDriver(driver);
            }
        }
    }

    void unLoadDriver(Driver driver) throws SQLException {
        DriverManager.deregisterDriver(driver);
    }

    void replaceDriver(Driver old, DriverWrapper wrapper) throws SQLException {
        DriverManager.registerDriver(wrapper);
        DriverManager.deregisterDriver(old);
    }
}
