package top.lcmatrix.util.codegenerator.gui.base;

import javax.swing.*;
import java.awt.*;

public class FormItemPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel label;
	private JComponent inputComponent;	

	public FormItemPanel(String labelText, JComponent inputComponent) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setLabel(labelText);
		this.setInputComponent(inputComponent);
	}
	
	public void setLabel(String labelText) {
		if(label == null) {
			JPanel labelPanel = new JPanel();
			labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
			label = new JLabel(labelText);
			labelPanel.add(label);
			labelPanel.add(Box.createGlue());
			this.add(labelPanel);
			this.add(Box.createVerticalStrut(DM.dm2pix(1)));
		}
		label.setText(labelText);
	}

	private void setInputComponent(JComponent inputComponent) {
		if(this.inputComponent != null) {
			this.remove(this.inputComponent);
		}
		inputComponent.setPreferredSize(new Dimension(0, 100));
		this.add(inputComponent);
		this.inputComponent = inputComponent;
	}

}
