package top.lcmatrix.util.codegenerator.common.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractSourcePlugin<I, O extends IOutputModel> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public abstract List<O> getOutputModels(I inputModel, Global global);

    protected Logger getLogger(){
        return logger;
    }
}
