package top.lcmatrix.util.codegenerator.gui.input;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lcmatrix.util.codegenerator.common.plugin.ISelectableEnum;
import top.lcmatrix.util.codegenerator.common.plugin.InputField;
import top.lcmatrix.util.codegenerator.gui.GUIConstants;
import top.lcmatrix.util.codegenerator.gui.base.DM;
import top.lcmatrix.util.codegenerator.gui.base.SelectableEnumComboBox;

import javax.swing.*;
import java.lang.reflect.Field;

public class JComboBoxDelegate extends InputComponentDelegate{

    private static Logger logger = LoggerFactory.getLogger(JComboBoxDelegate.class);

    private SelectableEnumComboBox jComboBox;

    @Override
    protected JComponent doInit(Field inputModelField, int width) {
        InputField inputFieldAnnotation = getInputFieldAnnotation();
        Class<?> fieldType = getInputModelField().getType();
        jComboBox = createSingleEnumInputComponent(fieldType, inputFieldAnnotation);
        return jComboBox;
    }

    private SelectableEnumComboBox createSingleEnumInputComponent(Class<?> fieldType, InputField inputFieldAnnotation) {
        SelectableEnumComboBox jComboBox = new SelectableEnumComboBox((ISelectableEnum[]) fieldType.getEnumConstants());
        return jComboBox;
    }

    @Override
    protected void reset() {
        jComboBox.setSelectedIndex(0);
        InputField inputFieldAnnotation = getInputFieldAnnotation();
        if(inputFieldAnnotation != null){
            Enum defaultSelectItem = null;
            String defaultValue = inputFieldAnnotation.defaultValue();
            if(StringUtils.isNotBlank(defaultValue)){
                Class<?> fieldType = getInputModelField().getType();
                try {
                    defaultSelectItem = Enum.valueOf((Class<Enum>) fieldType, defaultValue);
                } catch (IllegalArgumentException e) {
                    logger.error("error default value", e);
                }
                if(defaultSelectItem != null){
                    jComboBox.setSelectedItem(defaultSelectItem);
                }
            }
        }
    }

    @Override
    public Object getValue() {
        return jComboBox.getUnwrappedSelectedItem();
    }

    @Override
    public void setValue(Object value) {
        if(value == null){
            reset();
            return;
        }
        Field field = getInputModelField();
        Enum anEnum = Enum.valueOf((Class<Enum>) field.getType(), value.toString());
        jComboBox.setSelectedItem(anEnum);
    }

    @Override
    public int getHeight() {
        return DM.dm2pix(GUIConstants.DM_SINGLE_ROW * 2);
    }
}
