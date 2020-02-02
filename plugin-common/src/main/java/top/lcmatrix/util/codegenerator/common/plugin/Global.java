package top.lcmatrix.util.codegenerator.common.plugin;

import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;

public class Global extends HashMap<String, Object>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String EXP_SPLIT_TOKEN = ";";
	public static final String EXP_ASSIGN_TOKEN = "=";

	public static Global fromSettingsExp(String exp) {
		Global g = new Global();
		if(StringUtils.isNotBlank(exp)) {
			String[] assignExps = exp.split(EXP_SPLIT_TOKEN);
			for(String assignExp : assignExps) {
				try {
					String[] nameValue = assignExp.split(EXP_ASSIGN_TOKEN);
					g.put(nameValue[0], nameValue[1]);
				} catch (ArrayIndexOutOfBoundsException e) {
					throw new RuntimeException("Incorrect global settings!");
				}
			}
		}
		return g;
	}
}
