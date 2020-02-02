package top.lcmatrix.util.codegenerator.util;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class PathUtil {

	public static String createNoRepeatPath(String baseDir, String subPath){
		String mdir = baseDir + File.separator + subPath;
		String dir = mdir + "";
		int i = 1;
		while(new File(dir).exists()){
			dir = mdir + "_" + i;
			i++;
		}
		return dir;
	}
}
