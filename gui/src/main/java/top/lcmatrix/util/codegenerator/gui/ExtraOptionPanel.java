package top.lcmatrix.util.codegenerator.gui;

import top.lcmatrix.util.codegenerator.gui.base.DM;
import top.lcmatrix.util.codegenerator.gui.base.FormItemPanel;

import javax.swing.*;
import java.awt.*;

public class ExtraOptionPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField cGlobal = new JTextField();

	public ExtraOptionPanel() {
		super();
		this.setLayout(new GridLayout(0, 1,0, DM.dm2pix(4)));
		this.add(new FormItemPanel("global variables ( eg: var1=value1;var2=value2. use: global.var1;global.var2 ):", cGlobal));
	}

	public String getGlobal() {
		return cGlobal.getText();
	}

	public void setGlobal(String global) {
		this.cGlobal.setText(global);
	}
}
