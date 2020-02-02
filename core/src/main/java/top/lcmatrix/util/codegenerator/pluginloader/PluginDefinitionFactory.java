package top.lcmatrix.util.codegenerator.pluginloader;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lcmatrix.util.codegenerator.common.plugin.AbstractSourcePlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

public class PluginDefinitionFactory {

    private static Logger logger = LoggerFactory.getLogger(PluginDefinitionFactory.class);

    public static PluginDefinition create(Jar pluginJar) {
        return getDefinitionFromJar(pluginJar);
    }

    private static PluginDefinition getDefinitionFromJar(Jar pluginJar) {
        JarEntry jarEntry = pluginJar.getJarEntry("plugin-definition.json");
        if (jarEntry == null || jarEntry.isDirectory()) {
            return null;
        }
        try {
            InputStream inputStream = pluginJar.getInputStream(jarEntry);
            String definitionJson = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
            PluginDefinition pluginDefinition = JSON.parseObject(definitionJson, PluginDefinition.class);
            pluginDefinition.setJarPath(pluginJar.getJarFilePath());
            try {
                pluginDefinition.setPluginClass(new PluginClassLoader(pluginJar.getJarFilePath())
                        .loadClass(pluginDefinition.getClassCanonicalName()));
                return getCertainPluginDefinition(pluginDefinition);
            } catch (ClassNotFoundException e) {
                logger.error("load plugin error, plugin class not found, jar file:" + pluginJar.getJarFilePath(), e);
            }
            return pluginDefinition;
        } catch (IOException e) {
            logger.error("load plugin error, jar file:" + pluginJar.getJarFilePath(), e);
        }
        return null;
    }

    private static PluginDefinition getCertainPluginDefinition(PluginDefinition pluginDefinition) {
        Class<?> pluginClass = pluginDefinition.getPluginClass();
        if (AbstractSourcePlugin.class.isAssignableFrom(pluginClass)) {
            return new SourcePluginDefinition(pluginDefinition);
        } else {
            return pluginDefinition;
        }
    }
}
