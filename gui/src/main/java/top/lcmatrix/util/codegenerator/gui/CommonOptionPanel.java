package top.lcmatrix.util.codegenerator.gui;

import top.lcmatrix.util.codegenerator.gui.base.DM;
import top.lcmatrix.util.codegenerator.gui.base.FileInput;
import top.lcmatrix.util.codegenerator.gui.base.FormItemPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CommonOptionPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FileInput cTemplateDir = new FileInput();
	private FileInput cOutputDir = new FileInput();
	private JLabel outputModelLabel;

	public CommonOptionPanel() {
		super();
		this.setLayout(new GridLayout(0, 1, 0, DM.dm2pix(4)));
		cTemplateDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.add(new FormItemPanel("template dir *:", cTemplateDir));
		this.add(createOutputPanel());
	}

	private JComponent createOutputPanel(){
		JPanel outputPanel = new JPanel();
		outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
		labelPanel.add(new JLabel("output dir *:"));
		outputModelLabel = new JLabel("<html>&nbsp;&nbsp;( <u color=\"blue\">preview one of output models</u> )</html>");
		labelPanel.add(outputModelLabel);
		outputPanel.add(labelPanel);
		labelPanel.add(Box.createGlue());

		cOutputDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		cOutputDir.setPreferredSize(new Dimension(0, 100));
		outputPanel.add(cOutputDir);

		return outputPanel;
	}

	public String getTemplateDir() {
		return cTemplateDir.getValue();
	}

	public void setTemplateDir(String templateDir) {
		cTemplateDir.setValue(templateDir);
	}

	public String getOutputDir() {
		return cOutputDir.getValue();
	}

	public void setOutputDir(String outputDir) {
		cOutputDir.setValue(outputDir);
	}

	public void setPreviewOutputModelAction(Runnable action){
		outputModelLabel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				action.run();
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
		});
	}
}
