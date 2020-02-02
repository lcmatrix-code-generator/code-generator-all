package top.lcmatrix.util.codegenerator.template;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import top.lcmatrix.util.codegenerator.base.GenerateException;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TemplateLoader {
	
	public static List<TemplateStruct> loadTemplates(String templateDir){
		List<TemplateStruct> templates = new ArrayList<TemplateStruct>();
		Path templateDirPath = new File(templateDir).toPath();
		for(File templateFile : getTemplateFiles(templateDir)){
			TemplateStruct ts = new TemplateStruct();
			ts.setContentTemplate(templateFile);
			ts.setFileNameTemplate(FilenameUtils.getBaseName(templateFile.getAbsolutePath()));
			String relativeFilePath = templateDirPath.relativize(templateFile.toPath()).toString();
			String relativeDirPath = FilenameUtils.getPath(relativeFilePath);
			ts.setRelativeDirPath(relativeDirPath);

			templates.add(ts);
		}
		return templates;
	}
	
	private static Collection<File> getTemplateFiles(String sTemplateDir){
		File templateDir = new File(sTemplateDir);
		if(!templateDir.exists() || !templateDir.isDirectory()) {
			throw new GenerateException("Incorrect template dir");
		}
		return FileUtils.listFiles(templateDir, null, true);
	}
}
