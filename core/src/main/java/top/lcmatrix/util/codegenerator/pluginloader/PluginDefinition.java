package top.lcmatrix.util.codegenerator.pluginloader;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class PluginDefinition {
    private String jarPath;

    private String name;
    private String version;
    @JSONField(name = "class")
    private String classCanonicalName;
    private Class<?> pluginClass;

    public PluginDefinition(){}

    public PluginDefinition(PluginDefinition source){
        this.jarPath = source.jarPath;
        this.name = source.name;
        this.version = source.getVersion();
        this.classCanonicalName = source.classCanonicalName;
        this.pluginClass = source.pluginClass;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getClassCanonicalName() {
        return classCanonicalName;
    }

    public void setClassCanonicalName(String classCanonicalName) {
        this.classCanonicalName = classCanonicalName;
    }

    public Class<?> getPluginClass() {
        return pluginClass;
    }

    public void setPluginClass(Class<?> pluginClass) {
        this.pluginClass = pluginClass;
    }

    @Override
    public String toString() {
        return name + (StringUtils.isBlank(version) ? "" : (" ( " + version + " )"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginDefinition that = (PluginDefinition) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(version, that.version) &&
                Objects.equals(classCanonicalName, that.classCanonicalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version, classCanonicalName);
    }

}
