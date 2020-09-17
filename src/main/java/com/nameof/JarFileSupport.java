package com.nameof;

import java.net.MalformedURLException;
import java.net.URL;

public class JarFileSupport {

    public URL locateJar(String jarFilePath) throws MalformedURLException {
        return new URL("jar:file:" + jarFilePath + "!/");
    }
}
