package ru.practicum.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {
    private static Properties prop = new Properties();

    PropertyUtil() {
    }

    static {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appProperty = rootPath + "application.properties";
        try {
            prop.load(new FileInputStream(appProperty));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String propertyName) {
        return prop.getProperty(propertyName);
    }
}
