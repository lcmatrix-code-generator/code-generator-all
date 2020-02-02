package top.lcmatrix.util.codegenerator.pluginloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lcmatrix.util.codegenerator.common.plugin.AbstractTemplateEnginePlugin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PluginLoader {

    private static Logger logger = LoggerFactory.getLogger(PluginLoader.class);

    private static List<PluginDefinition> allPluginDefinitions = new ArrayList<>();
    private static List<SourcePluginDefinition> sourcePluginDefinitions = new ArrayList<>();
    private static List<PluginDefinition> templateEnginePluginDefinitions = new ArrayList<>();

    public static void load(File pluginDir) {
        if (pluginDir == null || !pluginDir.isDirectory()) {
            return;
        }
        List<Jar> allPluginJars = getAllPluginJars(pluginDir);
        for (Jar pluginJar : allPluginJars) {
            allPluginDefinitions.add(PluginDefinitionFactory.create(pluginJar));
        }
        dividePluginDefinitions();
    }

    public static List<SourcePluginDefinition> getSourcePluginDefinitions() {
        return sourcePluginDefinitions;
    }

    public static List<PluginDefinition> getTemplateEnginePluginDefinitions() {
        return templateEnginePluginDefinitions;
    }

    public static List<PluginDefinition> getAllPluginDefinitions() {
        return allPluginDefinitions;
    }

    private static void dividePluginDefinitions() {
        for (PluginDefinition pluginDefinition : allPluginDefinitions) {
            if(SourcePluginDefinition.class.isInstance(pluginDefinition)){
                SourcePluginDefinition sourcePluginDefinition = (SourcePluginDefinition) pluginDefinition;
                sourcePluginDefinitions.add(sourcePluginDefinition);
            } else if(AbstractTemplateEnginePlugin.class.isAssignableFrom(pluginDefinition.getPluginClass())){
                templateEnginePluginDefinitions.add(pluginDefinition);
            }
        }
    }

    private static List<Jar> getAllPluginJars(File pluginDir) {
        File[] files = pluginDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });
        List<Jar> jarFiles = new ArrayList<>(files.length);
        for (File file : files) {
            try {
                Jar jar = new Jar(file);
                jar.setJarFilePath(file.getAbsolutePath());
                jarFiles.add(jar);
            } catch (IOException e) {
                logger.error("load plugin error, jar file name:" + file.getName(), e);
            }
        }
        return jarFiles;
    }

}
