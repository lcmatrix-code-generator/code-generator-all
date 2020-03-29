package top.lcmatrix.util.codegenerator.gui;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lcmatrix.util.codegenerator.base.GenerateException;
import top.lcmatrix.util.codegenerator.common.plugin.ISelectableEnum;
import top.lcmatrix.util.codegenerator.common.plugin.InputField;
import top.lcmatrix.util.codegenerator.gui.base.DM;
import top.lcmatrix.util.codegenerator.gui.base.FormItemPanel;
import top.lcmatrix.util.codegenerator.gui.input.*;
import top.lcmatrix.util.codegenerator.pluginloader.SourcePluginDefinition;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourcePluginInputPanel extends JPanel{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(SourcePluginInputPanel.class);

	private int width;
	private Map<String, InputComponentDelegate> componentDelegateMap = new HashMap<>();
	private SourcePluginDefinition sourcePluginDefinition;
	private int totalHeight = 0;
	private static int vgap = DM.dm2pix(4);

	public SourcePluginInputPanel(SourcePluginDefinition sourcePluginDefinition, int width) {
		super();
		this.sourcePluginDefinition = sourcePluginDefinition;
		this.width = width;
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, vgap));
		Field[] inputModelFields = sourcePluginDefinition.getInputModelClass().getDeclaredFields();
		for (Field inputModelField : inputModelFields) {
			this.add(createFormItemFromField(inputModelField));
			totalHeight += vgap;
		}
	}

	public double getTotalHeight(){
		return totalHeight;
	}

	private JComponent createFormItemFromField(Field inputModelField) {
		Class<?> fieldType = inputModelField.getType();
		InputField inputFieldAnnotation = inputModelField.getAnnotation(InputField.class);
		InputComponentDelegate inputComponentDelegate = null;
		if(fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)){
			inputComponentDelegate = new JCheckBoxDelegate();
		} else if(ISelectableEnum.class.isAssignableFrom(fieldType) && fieldType.isEnum()){
			inputComponentDelegate = new JComboBoxDelegate();
		} else if(List.class.isAssignableFrom(fieldType)) {
			Class<?> itemClass = (Class<?>) ((ParameterizedType)inputModelField.getGenericType()).getActualTypeArguments()[0];
			if(ISelectableEnum.class.isAssignableFrom(itemClass) && itemClass.isEnum()){
				inputComponentDelegate = new JListDelegate();
			} else if(File.class.isAssignableFrom(itemClass)){
				inputComponentDelegate = new FileInputDelegate(true);
			} else {
				logger.error("Just \"ITextValueEnum\" or \"File\" in \"List\" is supported for input model.");
			}

		} else if(File.class.isAssignableFrom(fieldType)){
			inputComponentDelegate = new FileInputDelegate(false);
		} else if(inputFieldAnnotation != null && inputFieldAnnotation.multiLine()){
			inputComponentDelegate = new JTextAreaDelegate();
		} else {
			inputComponentDelegate = new JTextFieldDelegate();
		}
		inputComponentDelegate.init(inputModelField, width);
		componentDelegateMap.put(inputModelField.getName(), inputComponentDelegate);
		totalHeight += inputComponentDelegate.getHeight();
		if(inputComponentDelegate instanceof JCheckBoxDelegate){
			return inputComponentDelegate.getInputComponent();
		}
		FormItemPanel formItemPanel = new FormItemPanel(((inputFieldAnnotation != null && StringUtils.isNotBlank(inputFieldAnnotation.label()))? inputFieldAnnotation.label() : inputModelField.getName())
				+ ((inputFieldAnnotation != null && inputFieldAnnotation.required()) ? " * " : "")
				+ ":", inputComponentDelegate.getInputComponent());
		formItemPanel.setPreferredSize(new Dimension((int) width, inputComponentDelegate.getHeight()));
		return formItemPanel;
	}

	public Object getInputModel() throws InputComponentDelegate.ValidateException {
		Object inputModel;
		try {
			inputModel = sourcePluginDefinition.getInputModelClass().newInstance();
		} catch (InstantiationException |IllegalAccessException e) {
			throw new GenerateException("Instantiate input model fail, may be the plugin is illegal.");
		}
		for (Map.Entry<String, InputComponentDelegate> entry : componentDelegateMap.entrySet()) {
			try {
				Field field = sourcePluginDefinition.getInputModelClass().getDeclaredField(entry.getKey());
				Object value = entry.getValue().getValue();
				entry.getValue().validate();
				field.setAccessible(true);
				field.set(inputModel, value);
			} catch (NoSuchFieldException | IllegalAccessException e) {
				logger.warn("get input model error, field:" + entry.getKey());
				continue;
			}

		}
		return inputModel;
	}

    public void loadFromJson(String inputModelJson) {
		if(StringUtils.isBlank(inputModelJson)){
			return;
		}
		JSONObject jsonObject = JSON.parseObject(inputModelJson);
		for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
			InputComponentDelegate componentDelegate = componentDelegateMap.get(entry.getKey());
			if(componentDelegate == null){
				return;
			}
			componentDelegate.setValue(entry.getValue());
		}
	}

}
