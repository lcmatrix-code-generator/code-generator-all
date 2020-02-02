package top.lcmatrix.util.codegenerator.gui;

import top.lcmatrix.util.codegenerator.gui.base.FormItemPanel;
import top.lcmatrix.util.codegenerator.pluginloader.PluginDefinition;
import top.lcmatrix.util.codegenerator.pluginloader.PluginLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.List;

public class TemplateEnginePluginOptionPanel extends JPanel{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JComboBox<PluginDefinition> templateEnginePluginComboBox = new JComboBox<>();
	private ItemListener onPluginChangeListener;

	public TemplateEnginePluginOptionPanel() {
		super();
		this.setLayout(new GridLayout(0, 1));
		List<PluginDefinition> templateEnginePluginDefinitions = PluginLoader.getTemplateEnginePluginDefinitions();
		for (PluginDefinition templateEnginePlugin : templateEnginePluginDefinitions) {
			templateEnginePluginComboBox.addItem(templateEnginePlugin);
		}
		this.add(new FormItemPanel("select template engine *:", templateEnginePluginComboBox));
	}

	public PluginDefinition getSelectedPlugin(){
		return (PluginDefinition) templateEnginePluginComboBox.getSelectedItem();
	}

	public void selectedPlugin(PluginDefinition templateEnginePlugin){
		templateEnginePluginComboBox.setSelectedItem(templateEnginePlugin);
	}

	public void setOnPluginChangeListener(ItemListener itemListener){
		if(onPluginChangeListener != null){
			templateEnginePluginComboBox.removeItemListener(onPluginChangeListener);
		}
		onPluginChangeListener = itemListener;
		templateEnginePluginComboBox.addItemListener(onPluginChangeListener);
	}
}
