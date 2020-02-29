package top.lcmatrix.util.codegenerator.template;

import org.apache.commons.io.FileUtils;
import top.lcmatrix.util.codegenerator.base.GenerateException;
import top.lcmatrix.util.codegenerator.common.plugin.AbstractTemplateEnginePlugin;
import top.lcmatrix.util.codegenerator.util.PathUtil;

import java.io.File;

public class TemplateProcessor {

	public static void process(AbstractTemplateEnginePlugin templateEngine, TemplateStruct ts, Object model, String storeDir) {
		try {
			byte[] fileNameBytes = templateEngine.apply(ts.getFileNameTemplate(), model);
			if(fileNameBytes == null || fileNameBytes.length == 0){
				return;
			}
			String fileName = new String(fileNameBytes);
			String filePath = PathUtil.createNoRepeatPath(storeDir, fileName);
			File outputFile = new File(filePath);
			FileUtils.writeByteArrayToFile(outputFile, templateEngine.apply(ts.getContentTemplate(), model), false);
		} catch (Exception e) {
			throw new GenerateException(e);
		}
	}
}
