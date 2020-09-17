package com.nameof;

import java.net.URL;
import java.net.URLClassLoader;

public class DriverClassLoader {

    Class<?> loadDriverClass(URL jarSource, String driverClass) throws ClassNotFoundException {
        URLClassLoader ucl = new URLClassLoader(new URL[] { jarSource });
        return Class.forName(driverClass, true, ucl);
    }
}
