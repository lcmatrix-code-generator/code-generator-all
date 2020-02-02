package top.lcmatrix.util.codegenerator.gui.util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    private static Properties applicationProperties;
    static {
        try {
            applicationProperties = new Properties();
            applicationProperties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getApplicationProperty(String key){
        return applicationProperties.getProperty(key);
    }

}
