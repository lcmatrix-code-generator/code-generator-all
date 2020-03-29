package top.lcmatrix.util.codegenerator.gui.input;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lcmatrix.util.codegenerator.common.plugin.InputField;
import top.lcmatrix.util.codegenerator.gui.GUIConstants;
import top.lcmatrix.util.codegenerator.gui.base.DM;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public class JCheckBoxDelegate extends InputComponentDelegate{

    private static Logger logger = LoggerFactory.getLogger(JCheckBoxDelegate.class);

    private JCheckBox checkbox;

    @Override
    protected JComponent doInit(Field inputModelField, int width) {
        InputField inputFieldAnnotation = getInputFieldAnnotation();
        checkbox = createBooleanInputComponent(inputModelField, inputFieldAnnotation);
        checkbox.setPreferredSize(new Dimension(width, getHeight()));
        return checkbox;
    }

    private JCheckBox createBooleanInputComponent(Field inputModelField, InputField inputFieldAnnotation) {
        JCheckBox checkbox = new JCheckBox(((inputFieldAnnotation != null && StringUtils.isNotBlank(inputFieldAnnotation.label()))? inputFieldAnnotation.label() : inputModelField.getName()) +
                ((inputFieldAnnotation != null && inputFieldAnnotation.required()) ? " * " : "") + ": ");
        checkbox.setHorizontalTextPosition(SwingConstants.LEFT);
        return checkbox;
    }

    @Override
    protected void reset() {
        checkbox.setSelected(false);
        InputField inputFieldAnnotation = getInputFieldAnnotation();
        if(inputFieldAnnotation != null){
            String defaultValue = inputFieldAnnotation.defaultValue();
            if("true".equalsIgnoreCase(defaultValue)){
                checkbox.setSelected(true);
            }
        }
    }

    @Override
    public Object getValue() {
        return checkbox.isSelected();
    }

    @Override
    public void setValue(Object value) {
        if(value == null){
            reset();
            return;
        }
        Boolean checked = null;
        try {
            checked = (Boolean) value;
            checkbox.setSelected(checked);
        } catch (Exception e) {
            logger.warn("load from json error, field:" + getInputModelField().getName(), e);
        }
    }

    @Override
    public int getHeight() {
        return DM.dm2pix(GUIConstants.DM_SINGLE_ROW);
    }
}
