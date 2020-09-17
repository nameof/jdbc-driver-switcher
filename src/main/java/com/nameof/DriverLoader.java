package com.nameof;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Driver;

public class DriverLoader {

    private JarFileSupport jarFileSupport = new JarFileSupport();
    private DriverClassLoader driverClassLoader = new DriverClassLoader();

    DriverWrapper loadDriver(String jarFilePath, String driverClass) throws ClassNotFoundException, IllegalAccessException, InstantiationException, MalformedURLException {
        URL url = jarFileSupport.locateJar(jarFilePath);
        Class<?> klass = driverClassLoader.loadDriverClass(url, driverClass);
        Driver d = (Driver) klass.newInstance();
        return new DriverWrapper(d);
    }
}