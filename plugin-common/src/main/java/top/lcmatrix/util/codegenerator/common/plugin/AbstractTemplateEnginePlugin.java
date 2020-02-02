package top.lcmatrix.util.codegenerator.common.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class AbstractTemplateEnginePlugin {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public abstract String apply(String templateContent, Object model);

    public abstract String apply(File templateFile, Object model);

    protected Logger getLogger(){
        return logger;
    }
}
