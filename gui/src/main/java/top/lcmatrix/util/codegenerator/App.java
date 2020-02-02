package top.lcmatrix.util.codegenerator;

import top.lcmatrix.util.codegenerator.gui.MainWindow;
import top.lcmatrix.util.codegenerator.gui.util.DirUtil;
import top.lcmatrix.util.codegenerator.pluginloader.PluginLoader;

import javax.swing.*;
import java.io.File;

public class App {
	
	public static void main( String[] args ) throws Exception
    {
		PluginLoader.load(new File(DirUtil.getJarDir() + File.separator + "plugins"));

		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainWindow.getInstance().setVisible(true);
			}
		});
    }
}
