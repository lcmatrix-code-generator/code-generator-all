package top.lcmatrix.util.codegenerator.gui.input;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lcmatrix.util.codegenerator.common.plugin.ISelectableEnum;
import top.lcmatrix.util.codegenerator.common.plugin.InputField;
import top.lcmatrix.util.codegenerator.gui.base.DM;
import top.lcmatrix.util.codegenerator.gui.base.MyJScrollPane;
import top.lcmatrix.util.codegenerator.gui.base.SelectableEnumList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JListDelegate extends InputComponentDelegate{

    private static Logger logger = LoggerFactory.getLogger(JListDelegate.class);

    private SelectableEnumList jList;

    @Override
    protected JComponent doInit(Field inputModelField, int width) {
        InputField inputFieldAnnotation = getInputFieldAnnotation();
        Class<?> itemClass = (Class<?>) ((ParameterizedType)inputModelField.getGenericType()).getActualTypeArguments()[0];
        jList = createMultipleEnumInputComponent(inputFieldAnnotation, itemClass);
        return new MyJScrollPane(jList);
    }

    private SelectableEnumList createMultipleEnumInputComponent(InputField inputFieldAnnotation, Class<?> itemClass) {
        SelectableEnumList jList = new SelectableEnumList((ISelectableEnum[]) itemClass.getEnumConstants());
        jList.addMouseListener(JListMouseListenerForClearSelection);
        jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        return jList;
    }

    @Override
    protected void reset() {
        jList.clearSelection();
        InputField inputFieldAnnotation = getInputFieldAnnotation();
        if(inputFieldAnnotation != null){
            List<ISelectableEnum> defaultSelectItems = new ArrayList<>();
            String defaultValue = inputFieldAnnotation.defaultValue();
            if(StringUtils.isNotBlank(defaultValue)){
                String[] defaultValues = defaultValue.split(",");
                Class<?> itemClass = (Class<?>) ((ParameterizedType)getInputModelField().getGenericType()).getActualTypeArguments()[0];
                for (String value : defaultValues) {
                    try {
                        Enum defaultSelectItem = Enum.valueOf((Class<Enum>) itemClass, value);
                        defaultSelectItems.add((ISelectableEnum) defaultSelectItem);
                    } catch (IllegalArgumentException e) {
                        logger.error("error default value", e);
                    }
                }
            }
            jList.setSelectedValue(defaultSelectItems);
        }
    }

    @Override
    public Object getValue() {
        return jList.getSelectedValuesList();
    }

    @Override
    public void setValue(Object value) {
        if(value == null){
            reset();
            return;
        }
        Field field = getInputModelField();
        try {
            JSONArray enums = (JSONArray) value;
            Iterator<Object> iterator = enums.iterator();
            List<ISelectableEnum> selectedEnums = new ArrayList<>();
            while(iterator.hasNext()){
                Enum anEnum = Enum.valueOf((Class<Enum>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0], iterator.next().toString());
                selectedEnums.add((ISelectableEnum) anEnum);
            }
            jList.setSelectedValue(selectedEnums);
        } catch (Exception e) {
            logger.warn("load from json error, field:" + field.getName(), e);
        }
    }

    @Override
    public int getHeight() {
        return DM.dm2pix(40);
    }

    @Override
    protected void setWarningStyle() {
        jList.setBackground(Color.PINK);
    }

    @Override
    protected void setClearWarningStyleListener(JComponent component) {
        super.setClearWarningStyleListener(jList);
    }

    private MouseListener JListMouseListenerForClearSelection = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON3){
                ((JList) e.getSource()).clearSelection();
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
    };
}
