package top.lcmatrix.util.codegenerator.template;

import java.io.File;

public class TemplateStruct {

	private File contentTemplate;
	private String fileNameTemplate;
	private String relativeDirPath;
	
	public File getContentTemplate() {
		return contentTemplate;
	}
	public void setContentTemplate(File contentTemplate) {
		this.contentTemplate = contentTemplate;
	}
	public String getFileNameTemplate() {
		return fileNameTemplate;
	}
	public void setFileNameTemplate(String fileNameTemplate) {
		this.fileNameTemplate = fileNameTemplate;
	}

	public String getRelativeDirPath() {
		return relativeDirPath;
	}

	public void setRelativeDirPath(String relativeDirPath) {
		this.relativeDirPath = relativeDirPath;
	}
}
