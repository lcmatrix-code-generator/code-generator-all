package top.lcmatrix.util.codegenerator.pluginloader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 提供Jar隔离的加载机制，会把传入的路径、及其子路径、以及路径中的jar文件加入到class path。
 */
public class PluginClassLoader extends URLClassLoader {
    public PluginClassLoader(String jarPath) {
        this(jarPath, PluginClassLoader.class.getClassLoader());
    }

    public PluginClassLoader(String jarPath, ClassLoader parent) {
        super(getURLs(jarPath), parent);
    }

    private static URL[] getURLs(String jarPath) {
        Validate.isTrue(StringUtils.isNotBlank(jarPath),
                "jar包路径不能为空.");
        File jarFile = new File(jarPath);
        try {
            return new URL[]{jarFile.toURI().toURL()};
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
