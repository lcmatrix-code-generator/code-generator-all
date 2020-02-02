package top.lcmatrix.util.codegenerator.gui.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lcmatrix.util.codegenerator.App;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class DirUtil {

	private static Logger logger = LoggerFactory.getLogger(DirUtil.class);

	public static File getJarDir(){
		String jarFilePath = App.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		try {
			jarFilePath = java.net.URLDecoder.decode(jarFilePath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		jarFilePath = jarFilePath.replace("file:", "");
		jarFilePath = jarFilePath.replaceAll("\\!/.*\\!/", "");
		logger.info("jarFilePath:" + jarFilePath);
		return new File(jarFilePath).getParentFile();
	}
}
