package top.lcmatrix.util.codegenerator.generate;

import top.lcmatrix.util.codegenerator.base.GenerateException;
import top.lcmatrix.util.codegenerator.common.plugin.*;
import top.lcmatrix.util.codegenerator.pluginloader.PluginDefinition;
import top.lcmatrix.util.codegenerator.pluginloader.SourcePluginDefinition;
import top.lcmatrix.util.codegenerator.template.TemplateLoader;
import top.lcmatrix.util.codegenerator.template.TemplateProcessor;
import top.lcmatrix.util.codegenerator.template.TemplateStruct;

import java.io.File;
import java.util.List;

public class Generator {

    public static void generate(SourcePluginDefinition sourcePluginDefinition, Object inputModel,
                                String templateDir, String outputDir, Global global, PluginDefinition templateEnginePlugin){
        List<TemplateStruct> templates = TemplateLoader.loadTemplates(templateDir);
        new File(outputDir).mkdirs();

        List<? extends IOutputModel> outputModels = getOutputModels(sourcePluginDefinition, inputModel, global);
        if(outputModels == null || outputModels.isEmpty()){
            return;
        }
        // template plugin must apply in the same thread with source plugin getOutputModels invoke
        AbstractTemplateEnginePlugin templateEngine = createTemplateEngine(templateEnginePlugin);
        for(IOutputModel outputModel : outputModels) {
            for(TemplateStruct ts : templates){
                String fileStoreDir = outputDir + File.separator + ts.getRelativeDirPath();
                new File(fileStoreDir).mkdirs();
                TemplateProcessor.process(templateEngine, ts, outputModel, fileStoreDir);
            }
        }
    }

    public static List<? extends IOutputModel> getOutputModels(SourcePluginDefinition sourcePluginDefinition,
                                                               Object inputModel, Global global){
        Class<? extends AbstractSourcePlugin> pluginClass = (Class<? extends AbstractSourcePlugin>) sourcePluginDefinition.getPluginClass();
        AbstractSourcePlugin sourcePlugin = null;
        try {
            sourcePlugin = pluginClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new GenerateException("Invalid source plugin.", e);
        }
        List<? extends IOutputModel> outputModels = sourcePlugin.getOutputModels(inputModel, global);
        ContextHolder.setGlobal(global);
        ContextHolder.setInput(inputModel);
        return outputModels;
    }

    private static AbstractTemplateEnginePlugin createTemplateEngine(PluginDefinition templateEnginePlugin){
        Class<? extends AbstractTemplateEnginePlugin> pluginClass = (Class<? extends AbstractTemplateEnginePlugin>) templateEnginePlugin.getPluginClass();
        AbstractTemplateEnginePlugin templateEngine = null;
        try {
            templateEngine = pluginClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new GenerateException("Invalid template engine plugin.", e);
        }
        return templateEngine;
    }
}
