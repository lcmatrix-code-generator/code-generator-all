package top.lcmatrix.util.codegenerator.pluginloader;

import top.lcmatrix.util.codegenerator.common.plugin.AbstractSourcePlugin;

import java.lang.reflect.ParameterizedType;

public class SourcePluginDefinition extends PluginDefinition{

    private Class<?> inputModelClass;

    public SourcePluginDefinition(){
        super();
    }

    public SourcePluginDefinition(PluginDefinition pluginDefinition) {
        super(pluginDefinition);
        Class<? extends AbstractSourcePlugin> pluginClass = (Class<? extends AbstractSourcePlugin>) getPluginClass();
        ParameterizedType parameterizedType = (ParameterizedType) pluginClass.getGenericSuperclass();
        inputModelClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

    public Class<?> getInputModelClass() {
        return inputModelClass;
    }

}
