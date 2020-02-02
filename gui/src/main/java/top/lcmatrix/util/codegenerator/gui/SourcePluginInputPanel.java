package top.lcmatrix.util.codegenerator.gui;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lcmatrix.util.codegenerator.base.GenerateException;
import top.lcmatrix.util.codegenerator.common.plugin.ISelectableEnum;
import top.lcmatrix.util.codegenerator.common.plugin.InputField;
import top.lcmatrix.util.codegenerator.gui.base.*;
import top.lcmatrix.util.codegenerator.pluginloader.SourcePluginDefinition;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class SourcePluginInputPanel extends JPanel{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(SourcePluginInputPanel.class);

	private double width;
	private Map<String, JComponent> componentMap = new HashMap<>();
	private Map<String, InputField> validateRuleMap = new HashMap<>();
	private SourcePluginDefinition sourcePluginDefinition;
	private int totalHeight = 0;
	private static int heightPerComponent = DM.dm2pix(GUIConstants.DM_SINGLE_ROW * 2);
	private static int vgap = DM.dm2pix(4);

	public SourcePluginInputPanel(SourcePluginDefinition sourcePluginDefinition, double width) {
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
		if(inputFieldAnnotation != null){
			validateRuleMap.put(inputModelField.getName(), inputFieldAnnotation);
		}
		JComponent containComponent = null;
		JComponent inputComponent = null;
		int thisComponentHeight = heightPerComponent;
		if(fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)){
			JCheckBox checkbox = createBooleanInputComponent(inputModelField, inputFieldAnnotation);
			thisComponentHeight = heightPerComponent / 2;
			checkbox.setPreferredSize(new Dimension((int) width, thisComponentHeight));
			containComponent = checkbox;
			inputComponent = checkbox;
		} else if(ISelectableEnum.class.isAssignableFrom(fieldType) && fieldType.isEnum()){
			JComboBox jComboBox = createSingleEnumInputComponent(fieldType, inputFieldAnnotation);
			containComponent = jComboBox;
			inputComponent = jComboBox;
		} else if(List.class.isAssignableFrom(fieldType)) {
			Class<?> itemClass = (Class<?>) ((ParameterizedType)inputModelField.getGenericType()).getActualTypeArguments()[0];
			if(ISelectableEnum.class.isAssignableFrom(itemClass) && itemClass.isEnum()){
				JList jList = createMultipleEnumInputComponent(inputFieldAnnotation, itemClass);
				containComponent = new JScrollPane(jList);
				inputComponent = jList;
				thisComponentHeight = DM.dm2pix(40);
			} else if(File.class.isAssignableFrom(itemClass)){
				FileInput fileInput = createFileInputComponent(inputFieldAnnotation, true);
				containComponent = fileInput;
				inputComponent = fileInput;
			} else {
				logger.error("Just \"ITextValueEnum\" or \"File\" in \"List\" is supported for input model.");
			}
		} else if(File.class.isAssignableFrom(fieldType)){
			FileInput fileInput = createFileInputComponent(inputFieldAnnotation, false);
			containComponent = fileInput;
			inputComponent = fileInput;
		} else {
			JTextField jTextField = null;
			if(inputFieldAnnotation != null && inputFieldAnnotation.mask()){
				jTextField = new JPasswordField();
			} else {
				jTextField = new JTextField();
			}
			if(inputFieldAnnotation != null){
				String defaultValue = inputFieldAnnotation.defaultValue();
				if(StringUtils.isNotBlank(defaultValue)){
					jTextField.setText(defaultValue);
				}
			}
			containComponent = jTextField;
			inputComponent = jTextField;
		}
		if(inputComponent instanceof FileInput){
			setClearWarningStyleListener(((FileInput)inputComponent).getPathField());
		}else{
			setClearWarningStyleListener(inputComponent);
		}
		componentMap.put(inputModelField.getName(), inputComponent);
		totalHeight += thisComponentHeight;
		if(inputComponent instanceof JCheckBox){
			return inputComponent;
		}
		FormItemPanel formItemPanel = new FormItemPanel(((inputFieldAnnotation != null && StringUtils.isNotBlank(inputFieldAnnotation.label()))? inputFieldAnnotation.label() : inputModelField.getName())
				+ ((inputFieldAnnotation != null && inputFieldAnnotation.required()) ? " * " : "")
				+ ":", containComponent);
		formItemPanel.setPreferredSize(new Dimension((int) width, thisComponentHeight));
		return formItemPanel;
	}

	private JList createMultipleEnumInputComponent(InputField inputFieldAnnotation, Class<?> itemClass) {
		SelectableEnumList jList = new SelectableEnumList((ISelectableEnum[]) itemClass.getEnumConstants());
		jList.addMouseListener(JListMouseListenerForClearSelection);
		jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		if(inputFieldAnnotation != null){
			List<ISelectableEnum> defaultSelectItems = new ArrayList<>();
			String defaultValue = inputFieldAnnotation.defaultValue();
			if(StringUtils.isNotBlank(defaultValue)){
				String[] defaultValues = defaultValue.split(",");
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
		return jList;
	}

	private FileInput createFileInputComponent(InputField inputFieldAnnotation, boolean multiple) {
		FileInput fileInput = new FileInput();
		if(inputFieldAnnotation != null){
			fileInput.setFileSelectionMode(inputFieldAnnotation.fileSelectionMode());
			fileInput.setMultiSelectionEnabled(multiple);
			if(inputFieldAnnotation.allowFileSuffixes() != null){
				fileInput.setFileFilter(new SuffixesFileFilter(inputFieldAnnotation.allowFileSuffixes()));
			}
			String defaultValue = inputFieldAnnotation.defaultValue();
			if(StringUtils.isNotBlank(defaultValue)){
				fileInput.setValue(defaultValue);
			}
		}
		return fileInput;
	}

	private JComboBox createSingleEnumInputComponent(Class<?> fieldType, InputField inputFieldAnnotation) {
		SelectableEnumComboBox jComboBox = new SelectableEnumComboBox((ISelectableEnum[]) fieldType.getEnumConstants());
		if(inputFieldAnnotation != null){
			Enum defaultSelectItem = null;
			String defaultValue = inputFieldAnnotation.defaultValue();
			if(StringUtils.isNotBlank(defaultValue)){
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
		return jComboBox;
	}

	private JCheckBox createBooleanInputComponent(Field inputModelField, InputField inputFieldAnnotation) {
		JCheckBox checkbox = new JCheckBox(((inputFieldAnnotation != null && StringUtils.isNotBlank(inputFieldAnnotation.label()))? inputFieldAnnotation.label() : inputModelField.getName()) +
				((inputFieldAnnotation != null && inputFieldAnnotation.required()) ? " * " : "") + ": ");
		checkbox.setHorizontalTextPosition(SwingConstants.LEFT);
		if(inputFieldAnnotation != null){
			String defaultValue = inputFieldAnnotation.defaultValue();
			if("true".equalsIgnoreCase(defaultValue)){
				checkbox.setSelected(true);
			}
		}
		return checkbox;
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

	public Object getInputModel() throws ValidateException {
		Object inputModel;
		try {
			inputModel = sourcePluginDefinition.getInputModelClass().newInstance();
		} catch (InstantiationException |IllegalAccessException e) {
			throw new GenerateException("Instantiate input model fail, may be the plugin is illegal.");
		}
		for (Map.Entry<String, JComponent> entry : componentMap.entrySet()) {
			try {
				Field field = sourcePluginDefinition.getInputModelClass().getDeclaredField(entry.getKey());
				Object value = getAndParseValue(field, entry.getValue());
				validate(field, value);
				field.setAccessible(true);
				field.set(inputModel, value);
			} catch (NoSuchFieldException | IllegalAccessException e) {
				logger.warn("get input model error, field:" + entry.getKey());
				continue;
			}

		}
		return inputModel;
	}

	private void validate(Field field, Object value) throws ValidateException{
		InputField inputFieldAnnotation = validateRuleMap.get(field.getName());
		if(inputFieldAnnotation == null){
			return;
		}
		boolean required = inputFieldAnnotation.required();
		if(required){
			if(isNullOrEmpty(value)){
				setWarningStyle(field);
				throw new ValidateException("\"" + (StringUtils.isNotBlank(inputFieldAnnotation.label()) ? inputFieldAnnotation.label() : field.getName())
						+ "\" can not be empty");
			}
		}
		validateByRegexp(field, value);
	}

	private void validateByRegexp(Field field, Object value) throws ValidateException {
		if(value == null){
			return;
		}
		InputField inputFieldAnnotation = validateRuleMap.get(field.getName());
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
				setWarningStyle(field);
				throw new ValidateException("\"" + (StringUtils.isNotBlank(inputFieldAnnotation.label()) ? inputFieldAnnotation.label() : field.getName())
						+ "\" is invalid.");
			}
		}
	}

	private void setWarningStyle(Field field) {
		JComponent component = componentMap.get(field.getName());
		if(component instanceof FileInput){
			((FileInput) component).getPathField().setBackground(Color.PINK);
		}else{
			component.setBackground(Color.PINK);
		}
	}

	private Map<Component, Color> originBackgrounds = new HashMap<>();
	private FocusListener clearStyleListener = new FocusListener() {
		@Override
		public void focusGained(FocusEvent e) {
			e.getComponent().setBackground(originBackgrounds.get(e.getComponent()));
		}
		@Override
		public void focusLost(FocusEvent e) {

		}
	};
	private void setClearWarningStyleListener(Component component) {
		Color originBack = component.getBackground();
		originBackgrounds.put(component, originBack);
		component.addFocusListener(clearStyleListener);
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

	private Object getAndParseValue(Field field, JComponent component) {
		if(component instanceof JTextField){
			String text = ((JTextField) component).getText();
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
		} else if(component instanceof FileInput){
			String value = ((FileInput) component).getValue();
			if(StringUtils.isBlank(value)){
				return null;
			}
			if(List.class.isAssignableFrom(field.getType())){
				String[] filePaths = value.split(";");
				List<File> files = new ArrayList<>();
				for (String filePath : filePaths) {
					files.add(new File(filePath));
				}
				return files;
			}
			return new File(value);
		} else if(component instanceof SelectableEnumComboBox){
			return ((SelectableEnumComboBox) component).getSelectedItem();
		} else if(component instanceof JCheckBox){
			return ((JCheckBox) component).isSelected();
		} else if(component instanceof SelectableEnumList){
			return ((SelectableEnumList) component).getSelectedValuesList();
		}
		throw new GenerateException("Build input model fail, may be the plugin is illegal.");
	}

    public void loadFromJson(String inputModelJson) {
		JSONObject jsonObject = JSON.parseObject(inputModelJson);
		for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
			JComponent component = componentMap.get(entry.getKey());
			if(component == null || entry.getValue() == null){
				return;
			}
			if(component instanceof JTextField) {
				((JTextField) component).setText(entry.getValue().toString());
			} else if(component instanceof FileInput){
				Object value = entry.getValue();
				if(value instanceof JSONArray){
					String files = "";
					JSONArray fileArray = (JSONArray) value;
					for(int i = 0; i < fileArray.size(); i++){
						files += fileArray.get(i);
						if(i < fileArray.size() - 1){
							files += ";";
						}
					}
					((FileInput) component).setValue(files);
				}else{
					((FileInput) component).setValue(entry.getValue().toString());
				}
			} else if(component instanceof SelectableEnumComboBox){
				try {
					Field field = sourcePluginDefinition.getInputModelClass().getDeclaredField(entry.getKey());
					Enum anEnum = Enum.valueOf((Class<Enum>) field.getType(), entry.getValue().toString());
					((SelectableEnumComboBox) component).setSelectedItem(anEnum);
				} catch (NoSuchFieldException e) {
					logger.warn("load from json error, field:" + entry.getKey(), e);
				}
			} else if (component instanceof SelectableEnumList){
				try {
					Field field = sourcePluginDefinition.getInputModelClass().getDeclaredField(entry.getKey());
					JSONArray enums = (JSONArray) entry.getValue();
					Iterator<Object> iterator = enums.iterator();
					List<ISelectableEnum> selectedEnums = new ArrayList<>();
					while(iterator.hasNext()){
						Enum anEnum = Enum.valueOf((Class<Enum>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0], iterator.next().toString());
						selectedEnums.add((ISelectableEnum) anEnum);
					}
					((SelectableEnumList) component).setSelectedValue(selectedEnums);
				} catch (Exception e) {
					logger.warn("load from json error, field:" + entry.getKey(), e);
				}
			} else if(component instanceof JCheckBox){
				Boolean checked = null;
				try {
					checked = (Boolean) entry.getValue();
					((JCheckBox) component).setSelected(checked);
				} catch (Exception e) {
					logger.warn("load from json error, field:" + entry.getKey(), e);
				}
			}
		}
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
