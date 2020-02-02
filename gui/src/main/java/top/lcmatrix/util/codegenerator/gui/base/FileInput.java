package top.lcmatrix.util.codegenerator.gui.base;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileInput extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JFileChooser fileChooser = new JFileChooser();
	private JTextField pathField = new JTextField();
	private JButton chooseButton = new JButton("Choose");
	
	public FileInput(){
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(pathField);
		this.add(chooseButton);
		chooseButton.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pathField.grabFocus();
			}
		});
		chooseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.showOpenDialog(FileInput.this);
				if(fileChooser.isMultiSelectionEnabled()) {
					File[] selectedFiles = fileChooser.getSelectedFiles();
					if(selectedFiles != null && selectedFiles.length > 0) {
						String pathes = "";
						for(File f : selectedFiles) {
							pathes += f.getAbsolutePath() + ";";
						}
						pathes = pathes.substring(0, pathes.length() - 1);
						pathField.setText(pathes);
					}
				}else {
					File selectedFile = fileChooser.getSelectedFile();
					if(selectedFile != null) {
						pathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
					}
				}
			}
		});
	}
	
	/**
	 * @param mode the type of files to be displayed:
     * <ul>
     * <li>JFileChooser.FILES_ONLY
     * <li>JFileChooser.DIRECTORIES_ONLY
     * <li>JFileChooser.FILES_AND_DIRECTORIES
     * </ul>
	 * @return
	 */
	public FileInput setFileSelectionMode(int mode) {
		fileChooser.setFileSelectionMode(mode);
		return this;
	}
	
	public FileInput setMultiSelectionEnabled(boolean b) {
		fileChooser.setMultiSelectionEnabled(b);
		return this;
	}
	
	public FileInput setFileFilter(FileFilter ff) {
		fileChooser.setFileFilter(ff);
		return this;
	}
	
	public FileInput setCurrentDirectory(File dir) {
		fileChooser.setCurrentDirectory(dir);
		return this;
	}
	
	public String getValue() {
		return pathField.getText();
	}
	
	public void setValue(String text) {
		pathField.setText(text);
	}
	
	public JTextField getPathField() {
		return pathField;
	}
}
