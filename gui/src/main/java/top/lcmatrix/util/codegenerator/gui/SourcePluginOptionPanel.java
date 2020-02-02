package top.lcmatrix.util.codegenerator.gui;

import top.lcmatrix.util.codegenerator.gui.base.FormItemPanel;
import top.lcmatrix.util.codegenerator.pluginloader.PluginLoader;
import top.lcmatrix.util.codegenerator.pluginloader.SourcePluginDefinition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.List;

public class SourcePluginOptionPanel extends JPanel{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JComboBox<SourcePluginDefinition> sourcePluginComboBox = new JComboBox<>();
	private ItemListener onPluginChangeListener;

	public SourcePluginOptionPanel() {
		super();
		this.setLayout(new GridLayout(0, 1));
		List<SourcePluginDefinition> sourcePluginDefinitions = PluginLoader.getSourcePluginDefinitions();
		for (SourcePluginDefinition sourcePluginDefinition : sourcePluginDefinitions) {
			sourcePluginComboBox.addItem(sourcePluginDefinition);
		}
		this.add(new FormItemPanel("select source *:", sourcePluginComboBox));
	}

	public SourcePluginDefinition getSelectedPlugin(){
		return (SourcePluginDefinition) sourcePluginComboBox.getSelectedItem();
	}

	public void selectedPlugin(SourcePluginDefinition sourcePluginDefinition){
		sourcePluginComboBox.setSelectedItem(sourcePluginDefinition);
	}

	public void setOnPluginChangeListener(ItemListener itemListener){
		if(onPluginChangeListener != null){
			sourcePluginComboBox.removeItemListener(onPluginChangeListener);
		}
		onPluginChangeListener = itemListener;
		sourcePluginComboBox.addItemListener(onPluginChangeListener);
	}
}
