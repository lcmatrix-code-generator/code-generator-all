package top.lcmatrix.util.codegenerator.template;

import org.apache.commons.io.FileUtils;
import top.lcmatrix.util.codegenerator.Constants;
import top.lcmatrix.util.codegenerator.base.GenerateException;
import top.lcmatrix.util.codegenerator.common.plugin.AbstractTemplateEnginePlugin;
import top.lcmatrix.util.codegenerator.util.PathUtil;

import java.io.File;

public class TemplateProcessor {

	public static void process(AbstractTemplateEnginePlugin templateEngine, TemplateStruct ts, Object model, String storeDir) {
		try {
			String fileName = templateEngine.apply(ts.getFileNameTemplate(), model);
			if(fileName == null || "".equals(fileName)){
				return;
			}
			String content = templateEngine.apply(ts.getContentTemplate(), model);
			String filePath = PathUtil.createNoRepeatPath(storeDir, fileName);
			File outputFile = new File(filePath);
			FileUtils.write(outputFile, content, Constants.DEFAULT_CHARSET, false);
		} catch (Exception e) {
			throw new GenerateException(e);
		}
	}
}
