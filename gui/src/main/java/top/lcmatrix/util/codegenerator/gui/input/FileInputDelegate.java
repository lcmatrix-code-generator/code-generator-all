package top.lcmatrix.util.codegenerator.gui.input;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import top.lcmatrix.util.codegenerator.common.plugin.InputField;
import top.lcmatrix.util.codegenerator.gui.GUIConstants;
import top.lcmatrix.util.codegenerator.gui.base.DM;
import top.lcmatrix.util.codegenerator.gui.base.FileInput;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FileInputDelegate extends InputComponentDelegate{

    private boolean multiple;
    private FileInput fileInput;

    public FileInputDelegate(boolean multiple){
        this.multiple = multiple;
    }

    @Override
    protected JComponent doInit(Field inputModelField, int width) {
        InputField inputFieldAnnotation = getInputFieldAnnotation();
        fileInput = createFileInputComponent(inputFieldAnnotation);
        return fileInput;
    }

    private FileInput createFileInputComponent(InputField inputFieldAnnotation) {
        FileInput fileInput = new FileInput();
        if(inputFieldAnnotation != null){
            fileInput.setFileSelectionMode(inputFieldAnnotation.fileSelectionMode());
            fileInput.setMultiSelectionEnabled(multiple);
            if(inputFieldAnnotation.allowFileSuffixes() != null){
                fileInput.setFileFilter(new SuffixesFileFilter(inputFieldAnnotation.allowFileSuffixes()));
            }
        }
        return fileInput;
    }

    @Override
    protected void reset() {
        fileInput.setValue("");
        InputField inputFieldAnnotation = getInputFieldAnnotation();
        if(inputFieldAnnotation != null){
            String defaultValue = inputFieldAnnotation.defaultValue();
            if(StringUtils.isNotBlank(defaultValue)){
                fileInput.setValue(defaultValue);
            }
        }
    }

    @Override
    protected void setClearWarningStyleListener(JComponent component) {
        FileInput fileInput = (FileInput) getInputComponent();
        super.setClearWarningStyleListener(fileInput.getPathField());
    }

    @Override
    public Object getValue() {
        String value = fileInput.getValue();
        if(StringUtils.isBlank(value)){
            return null;
        }
        if(List.class.isAssignableFrom(getInputModelField().getType())){
            String[] filePaths = value.split(";");
            List<File> files = new ArrayList<>();
            for (String filePath : filePaths) {
                files.add(new File(filePath));
            }
            return files;
        }
        return new File(value);
    }

    @Override
    public void setValue(Object value) {
        if(value == null){
            reset();
            return;
        }
        if(value instanceof JSONArray){
            String files = "";
            JSONArray fileArray = (JSONArray) value;
            for(int i = 0; i < fileArray.size(); i++){
                files += fileArray.get(i);
                if(i < fileArray.size() - 1){
                    files += ";";
                }
            }
            fileInput.setValue(files);
        }else{
            fileInput.setValue(value.toString());
        }
    }

    @Override
    public int getHeight() {
        return DM.dm2pix(GUIConstants.DM_SINGLE_ROW * 2);
    }

    @Override
    protected void setWarningStyle() {
        fileInput.getPathField().setBackground(Color.PINK);
    }

    private static class SuffixesFileFilter extends FileFilter {
        private String suffixes;
        private String[] suffixArray;

        SuffixesFileFilter(String suffixes){
            this.suffixes = suffixes;
            this.suffixArray = suffixes.split(",");
        }

        @Override
        public boolean accept(File f) {
            if(f.isDirectory()){
                return true;
            }
            for (String s : suffixArray) {
                if(f.getName().endsWith(s)){
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "file with suffixes: " + suffixes;
        }
    }
}
