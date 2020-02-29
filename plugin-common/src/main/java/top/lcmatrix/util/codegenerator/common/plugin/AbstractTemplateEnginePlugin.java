package top.lcmatrix.util.codegenerator.common.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class AbstractTemplateEnginePlugin {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public abstract byte[] apply(String templateContent, Object model);

    public abstract byte[] apply(File templateFile, Object model);

    protected Logger getLogger(){
        return logger;
    }
}
