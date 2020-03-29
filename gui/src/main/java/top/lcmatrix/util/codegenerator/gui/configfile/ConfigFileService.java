package top.lcmatrix.util.codegenerator.gui.configfile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lcmatrix.util.codegenerator.Constants;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigFileService {

    public static final String DEFAULT_CONFIG_FILE = "default.json";
    public static final String LAST_CONFIG_FILE = ".last";

    private static Logger logger = LoggerFactory.getLogger(ConfigFileService.class);

    private File rootDir;

    public ConfigFileService(File rootDir){
        this.rootDir = rootDir;
    }

    public void saveConfigurations(Object config, String fileName) {
        String ibJson = JSON.toJSONString(config);
        File configFile = new File(rootDir.getAbsolutePath() + File.separator + fileName);
        try {
            FileUtils.write(configFile, ibJson, Constants.DEFAULT_CHARSET, false);
            setLastConfig(fileName);
        } catch (IOException e) {
            logger.error("save configuration error.", e);
        }
    }

    public void setLastConfig(String configFileName){
        try {
            FileUtils.write(new File(rootDir.getAbsolutePath() + File.separator + LAST_CONFIG_FILE),
                    configFileName, Constants.DEFAULT_CHARSET, false);
        } catch (IOException e) {
            logger.error("save last config file name error.", e);
        }
    }

    public JSONObject readConfigurations(String configName) {
        JSONObject jsonObject = null;
        try {
            String ibJson = FileUtils.readFileToString(new File(rootDir.getAbsolutePath() + File.separator + configName), Constants.DEFAULT_CHARSET);
            jsonObject = JSON.parseObject(ibJson);
            setLastConfig(configName);
        } catch (FileNotFoundException e){
        } catch (IOException | JSONException e) {
            logger.error("read config file fail.", e);
        }
        return jsonObject;
    }

    public String getLastConfigFile() {
        try {
            return FileUtils.readFileToString(new File(rootDir.getAbsolutePath() + File.separator + LAST_CONFIG_FILE),
                    Constants.DEFAULT_CHARSET);
        } catch (FileNotFoundException e){
        } catch (IOException e) {
            logger.warn("Read last config file name failed", e);
        }
        return DEFAULT_CONFIG_FILE;
    }

    public File[] getConfigFiles(){
        File[] configFiles = rootDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().endsWith(".json");
            }
        });
        return configFiles;
    }
}
