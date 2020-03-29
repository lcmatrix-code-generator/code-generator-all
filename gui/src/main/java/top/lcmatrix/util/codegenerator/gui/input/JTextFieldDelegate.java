package top.lcmatrix.util.codegenerator.gui.input;

import org.apache.commons.lang3.StringUtils;
import top.lcmatrix.util.codegenerator.common.plugin.InputField;
import top.lcmatrix.util.codegenerator.gui.GUIConstants;
import top.lcmatrix.util.codegenerator.gui.base.DM;

import javax.swing.*;
import java.lang.reflect.Field;

public class JTextFieldDelegate extends InputComponentDelegate{

    private JTextField jTextField;

    @Override
    protected JComponent doInit(Field inputModelField, int width) {
        InputField inputFieldAnnotation = getInputFieldAnnotation();
        if(inputFieldAnnotation != null && inputFieldAnnotation.mask()){
            jTextField = new JPasswordField();
        } else {
            jTextField = new JTextField();
        }
        return jTextField;
    }

    @Override
    protected void reset() {
        jTextField.setText("");
        InputField inputFieldAnnotation = getInputFieldAnnotation();
        if(inputFieldAnnotation != null){
            String defaultValue = inputFieldAnnotation.defaultValue();
            if(StringUtils.isNotBlank(defaultValue)){
                jTextField.setText(defaultValue);
            }
        }
    }

    @Override
    public Object getValue() {
        String text = jTextField.getText();
        Field field = getInputModelField();
        if(field.getType().equals(Byte.class) || field.getType().equals(byte.class)){
            return Byte.valueOf(text);
        } else if(field.getType().equals(Short.class) || field.getType().equals(short.class)){
            return Short.valueOf(text);
        } else if(field.getType().equals(Integer.class) || field.getType().equals(int.class)){
            return Integer.valueOf(text);
        } else if(field.getType().equals(Long.class) || field.getType().equals(long.class)){
            return Long.valueOf(text);
        } else if(field.getType().equals(Float.class) || field.getType().equals(float.class)){
            return Float.valueOf(text);
        } else if(field.getType().equals(Double.class) || field.getType().equals(double.class)){
            return Double.valueOf(text);
        } else if(field.getType().equals(Character.class) || field.getType().equals(char.class)){
            return text.charAt(0);
        }
        return text;
    }

    @Override
    public void setValue(Object value) {
        if(value == null){
            reset();
            return;
        }
        jTextField.setText(value.toString());
    }

    @Override
    public int getHeight() {
        return DM.dm2pix(GUIConstants.DM_SINGLE_ROW * 2);
    }
}
