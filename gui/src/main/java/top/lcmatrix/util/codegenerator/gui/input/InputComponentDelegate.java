package top.lcmatrix.util.codegenerator.gui.input;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lcmatrix.util.codegenerator.common.plugin.InputField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Pattern;

public abstract class InputComponentDelegate {

    private static Logger logger = LoggerFactory.getLogger(InputComponentDelegate.class);

    private Field inputModelField;
    private JComponent inputComponent;
    private Color originBackground;

    public InputComponentDelegate init(Field inputModelField, int width){
        this.inputModelField = inputModelField;
        JComponent inputComponent = doInit(inputModelField, width);
        setInputComponent(inputComponent);
        reset();
        setClearWarningStyleListener(inputComponent);
        return this;
    }

    protected abstract JComponent doInit(Field inputModelField, int width);

    protected abstract void reset();

    public abstract Object getValue();

    public abstract void setValue(Object value);

    public abstract int getHeight();

    public void validate() throws ValidateException{
        InputField inputFieldAnnotation = getInputFieldAnnotation();
        if(inputFieldAnnotation == null){
            return;
        }
        Object value = getValue();
        boolean required = inputFieldAnnotation.required();
        if(required){
            if(isNullOrEmpty(value)){
                setWarningStyle();
                throw new ValidateException("\"" + (StringUtils.isNotBlank(inputFieldAnnotation.label()) ? inputFieldAnnotation.label() : getInputModelField().getName())
                        + "\" can not be empty");
            }
        }
        validateByRegexp(value);
    }

    public InputField getInputFieldAnnotation() {
        return inputModelField.getAnnotation(InputField.class);
    }

    protected void setWarningStyle(){
        getInputComponent().setBackground(Color.PINK);
    }

    protected void setClearWarningStyleListener(JComponent component) {
        setOriginBackground(component.getBackground());
        component.addFocusListener(clearStyleListener);
    }

    private FocusListener clearStyleListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            e.getComponent().setBackground(originBackground);
        }
        @Override
        public void focusLost(FocusEvent e) {

        }
    };

    private void validateByRegexp(Object value) throws ValidateException {
        if(value == null){
            return;
        }
        InputField inputFieldAnnotation = getInputFieldAnnotation();
        String validateRegExp = inputFieldAnnotation.validateRegExp();
        Pattern pattern = null;
        if(StringUtils.isNotBlank(validateRegExp)){
            try {
                pattern = Pattern.compile(validateRegExp);
            } catch (Exception e) {
                logger.error("error validate regexp", e);
            }
        }
        if(pattern != null){
            if(!pattern.matcher(value.toString()).matches()){
                setWarningStyle();
                throw new ValidateException("\"" + (StringUtils.isNotBlank(inputFieldAnnotation.label()) ? inputFieldAnnotation.label() : getInputModelField().getName())
                        + "\" is invalid.");
            }
        }
    }

    public Field getInputModelField() {
        return inputModelField;
    }

    public JComponent getInputComponent() {
        return inputComponent;
    }

    private void setInputComponent(JComponent inputComponent) {
        this.inputComponent = inputComponent;
    }

    protected void setOriginBackground(Color originBackground) {
        this.originBackground = originBackground;
    }

    private boolean isNullOrEmpty(Object value) {
        if(value == null){
            return true;
        }
        if(value instanceof String && StringUtils.isBlank((String) value)){
            return true;
        }
        if(value instanceof List && ((List) value).isEmpty()){
            return true;
        }
        return false;
    }

    public static class ValidateException extends Exception {
        public ValidateException() {
        }

        public ValidateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

        public ValidateException(String message, Throwable cause) {
            super(message, cause);
        }

        public ValidateException(String message) {
            super(message);
        }

        public ValidateException(Throwable cause) {
            super(cause);
        }
    }
}
