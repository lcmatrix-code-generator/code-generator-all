package top.lcmatrix.util.codegenerator.gui;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lcmatrix.util.codegenerator.Constants;
import top.lcmatrix.util.codegenerator.common.plugin.Assert;
import top.lcmatrix.util.codegenerator.common.plugin.Global;
import top.lcmatrix.util.codegenerator.common.plugin.IOutputModel;
import top.lcmatrix.util.codegenerator.generate.Generator;
import top.lcmatrix.util.codegenerator.gui.base.DM;
import top.lcmatrix.util.codegenerator.gui.base.JsonDialog;
import top.lcmatrix.util.codegenerator.gui.util.DirUtil;
import top.lcmatrix.util.codegenerator.gui.util.PropertiesUtil;
import top.lcmatrix.util.codegenerator.pluginloader.PluginDefinition;
import top.lcmatrix.util.codegenerator.pluginloader.SourcePluginDefinition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(MainWindow.class);
	
	private static MainWindow mainWindow;
	
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel contentPanel;
	private static int heightPerComponent = DM.dm2pix(GUIConstants.DM_SINGLE_ROW * 2);
	private SourcePluginOptionPanel sourcePluginOptionPanel = new SourcePluginOptionPanel();
	private TemplateEnginePluginOptionPanel templateEnginePluginOptionPanel = new TemplateEnginePluginOptionPanel();
	private SourcePluginInputPanel sourcePluginInputPanel;
	private CommonOptionPanel commonOptionPanel = new CommonOptionPanel();
	private ExtraOptionPanel extraOptionPanel = new ExtraOptionPanel();
	private JButton generateButton = new JButton("GENERATE");
	private Color gBtnNormalBgColor = generateButton.getBackground();
	private String gBtnNormalText = generateButton.getText();

	public static MainWindow getInstance() {
		if(mainWindow == null) {
			mainWindow = new MainWindow();
		}
		return mainWindow;
	}
	
	private MainWindow(){
		super();
		initMyself();
		initComponents();
	}
	
	private void initMyself() {
		this.setTitle("LcMatrix code generator " + PropertiesUtil.getApplicationProperty("app-version"));
		this.setSize((int)(screenSize.width * 0.5), (int)(screenSize.height * 0.7));
		this.setLocation((int)(screenSize.width * 0.25), (int)(screenSize.height * 0.15));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLayout(new FlowLayout());
		contentPanel = new JPanel();
		contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, DM.dm2pix(4)));
		JScrollPane jScrollPane = new JScrollPane(contentPanel);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.setPreferredSize(new Dimension((int)(getWidth() - 20), (int)(getHeight() * 0.83)));
		this.getContentPane().add(jScrollPane);
	}
	
	private void initComponents() {
		initSourcePluginOptionPanel();
		initTemplateEnginePluginOptionPanel();
		initCommonOptionPanel();
		initExtraOptionPanel();
		addOtherComponent();
		initGenerateButton();
		readConfigurations("default");
		initMenu();
		adjustContentHeight();
	}

	private void adjustContentHeight(){
		int vgap = DM.dm2pix(4);
		int contentHeight = 0;
		// source plugin option panel
		contentHeight += heightPerComponent * 1 + vgap;
		// source input panel
		if(sourcePluginInputPanel != null){
			contentHeight += sourcePluginInputPanel.getTotalHeight();
		}
		// template engine option panel
		contentHeight += heightPerComponent * 1 + vgap;
		// common option panel
		contentHeight += (heightPerComponent + vgap) * 2;;
		//extra option panel
		contentHeight += heightPerComponent * 1 + vgap;
		// other
		contentHeight +=  heightPerComponent * 2;

		contentPanel.setPreferredSize(new Dimension((int)(getWidth() * 0.8), contentHeight));
	}

	private void initSourcePluginOptionPanel() {
		int thisHeight = heightPerComponent * 1;
		sourcePluginOptionPanel.setPreferredSize(new Dimension((int)(getWidth() * 0.9), thisHeight));
		contentPanel.add(sourcePluginOptionPanel);
		sourcePluginOptionPanel.setOnPluginChangeListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.DESELECTED){
					if(sourcePluginInputPanel != null){
						contentPanel.remove(sourcePluginInputPanel);
					}
				} else {
					SourcePluginDefinition newSourcePluginDef = (SourcePluginDefinition) e.getItem();
					addSourceInputPanel(newSourcePluginDef);
					adjustContentHeight();
				}
				contentPanel.updateUI();
			}
		});
		SourcePluginDefinition selectedPlugin = sourcePluginOptionPanel.getSelectedPlugin();
		if(selectedPlugin != null){
			addSourceInputPanel(selectedPlugin);
		}
	}

	private void addSourceInputPanel(SourcePluginDefinition sourcePluginDef) {
		double thisWidth = getWidth() * 0.9;
		sourcePluginInputPanel = new SourcePluginInputPanel(sourcePluginDef, thisWidth);
		double thisHeight = sourcePluginInputPanel.getTotalHeight();
		sourcePluginInputPanel.setPreferredSize(new Dimension((int) thisWidth, (int) thisHeight));
		contentPanel.add(sourcePluginInputPanel, 1);
	}

	private void initTemplateEnginePluginOptionPanel() {
		int thisHeight = heightPerComponent * 1;
		templateEnginePluginOptionPanel.setPreferredSize(new Dimension((int)(getWidth() * 0.9), thisHeight));
		contentPanel.add(templateEnginePluginOptionPanel);
	}

	private void initCommonOptionPanel() {
		int thisHeight = heightPerComponent * 2;
		commonOptionPanel.setPreferredSize(new Dimension((int)(getWidth() * 0.9), thisHeight));
		commonOptionPanel.setPreviewOutputModelAction(new Runnable() {
			@Override
			public void run() {
				try {
					Assert.assertTrue("you should select a source", sourcePluginOptionPanel.getSelectedPlugin() != null && sourcePluginInputPanel!= null);
				} catch (Assert.AssertFailException e1) {
					JOptionPane.showMessageDialog(MainWindow.this, e1.getMessage());
					return;
				}
				Object inputModel = null;
				try {
					inputModel = sourcePluginInputPanel.getInputModel();
				} catch (SourcePluginInputPanel.ValidateException ex) {
					JOptionPane.showMessageDialog(MainWindow.this, ex.getMessage());
					return;
				}
				Global global = null;
				try {
					global = Global.fromSettingsExp(extraOptionPanel.getGlobal());
				} catch (Exception ex) {
					logger.error("format global variables error.", ex);
					JOptionPane.showMessageDialog(MainWindow.this, ex.getMessage());
					return;
				}
				MainWindow.this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				List<? extends IOutputModel> outputModels = null;
				try {
					outputModels = Generator.getOutputModels(sourcePluginOptionPanel.getSelectedPlugin(), inputModel, global);
				} catch (Exception e) {
					logger.error("preview output model error.", e);
					MainWindow.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					JOptionPane.showMessageDialog(MainWindow.this, e.getMessage());
					return;
				}
				MainWindow.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				if(outputModels == null || outputModels.isEmpty()){
					JOptionPane.showMessageDialog(MainWindow.this, "No output model found!");
					return;
				}
				JsonDialog outputModelDialog = new JsonDialog(MainWindow.this, "one of output models", outputModels.get(0));
				outputModelDialog.setSize(DM.dm2pix(400), DM.dm2pix(300));
				outputModelDialog.setVisible(true);
			}
		});
		contentPanel.add(commonOptionPanel);
	}

	private void initExtraOptionPanel() {
		int thisHeight = heightPerComponent * 1;
		extraOptionPanel.setPreferredSize(new Dimension((int)(getWidth() * 0.9), thisHeight));
		contentPanel.add(extraOptionPanel);
	}

	private volatile boolean generating = false;
	private void initGenerateButton() {
		generateButton.setPreferredSize(new Dimension((int)(getWidth() * 0.5), DM.dm2pix(GUIConstants.DM_SINGLE_ROW * 1.5)));
		this.getContentPane().add(generateButton);
		generateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(generating) {
					return;
				}
				String templateDir = commonOptionPanel.getTemplateDir();
				String outputDir = commonOptionPanel.getOutputDir();
				try {
					Assert.assertTrue("you should select a source", sourcePluginOptionPanel.getSelectedPlugin() != null && sourcePluginInputPanel != null);
					Assert.assertTrue("you should select a template engine", templateEnginePluginOptionPanel.getSelectedPlugin() != null);
					Assert.assertTrue("template dir can not be blank", StringUtils.isNotBlank(templateDir));
					Assert.assertTrue("output dir can not be blank", StringUtils.isNotBlank(outputDir));
				} catch (Assert.AssertFailException e1) {
					JOptionPane.showMessageDialog(MainWindow.this, e1.getMessage());
					return;
				}

				Object inputModel = null;
				try {
					inputModel = sourcePluginInputPanel.getInputModel();
				} catch (SourcePluginInputPanel.ValidateException ex) {
					JOptionPane.showMessageDialog(MainWindow.this, ex.getMessage());
					return;
				}

				String globalStr = extraOptionPanel.getGlobal();
				Global global = null;
				try {
					global = Global.fromSettingsExp(globalStr);
				} catch (Exception ex) {
					logger.error("format global variables error.", ex);
					JOptionPane.showMessageDialog(MainWindow.this, ex.getMessage());
					return;
				}
				generating = true;
				changeGBtnStatus();
				
				saveConfigurations(sourcePluginOptionPanel.getSelectedPlugin(), inputModel, templateEnginePluginOptionPanel.getSelectedPlugin(),
						templateDir, outputDir, globalStr, "default");
				
				doGenerateInBackground(inputModel, templateDir, outputDir, global);
			}

		});
	}
	
	private void doGenerateInBackground(Object inputModel, String templateDir, String outputDir, Global global) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Generator.generate(sourcePluginOptionPanel.getSelectedPlugin(), inputModel, templateDir, outputDir,
							global, templateEnginePluginOptionPanel.getSelectedPlugin());
					int toOpen = JOptionPane.showConfirmDialog(MainWindow.this, "Generate finish!\nOpen the output dir now?", "", JOptionPane.YES_NO_OPTION);
					if(toOpen == JOptionPane.YES_OPTION) {
						try {
							Desktop.getDesktop().open(new File(outputDir));
						} catch (IOException e) {
							logger.error("Open the output dir error.", e);
						}
					}
				} catch (Exception e) {
					logger.error("generate process error.", e);
					JOptionPane.showMessageDialog(MainWindow.this, "Process error, view the log in \"logs\" directory. " + e.getMessage());
				}
				generating = false;
				changeGBtnStatus();
			}
		}).start();
	}
	
	private void changeGBtnStatus() {
		if(generating) {
			generateButton.setText("Generating...");
			generateButton.setBackground(Color.cyan);
		}else {
			generateButton.setText(gBtnNormalText);
			generateButton.setBackground(gBtnNormalBgColor);
		}
	}

	private void saveConfigurations(SourcePluginDefinition sourcePluginDefinition, Object inputModel, PluginDefinition templateEnginePlugin,
									String templateDir, String outputDir, String globalStr, String name) {
		Map<String, Object> configMap = new HashMap<>();
		configMap.put("sourcePlugin", sourcePluginDefinition);
		configMap.put("inputModel", inputModel);
		configMap.put("templateEnginePlugin", templateEnginePlugin);
		configMap.put("templateDir", templateDir);
		configMap.put("outputDir", outputDir);
		configMap.put("global", globalStr);

		String ibJson = JSON.toJSONString(configMap);
		File jarDir = DirUtil.getJarDir();
		File defaultJson = new File(jarDir.getAbsoluteFile() + File.separator + name + ".json");
		try {
			FileUtils.write(defaultJson, ibJson, Constants.DEFAULT_CHARSET, false);
		} catch (IOException e) {
			logger.error("save configuration error.", e);
		}
	}
	
	private void readConfigurations(String configName) {
		File jarDir = DirUtil.getJarDir();
		try {
			String ibJson = FileUtils.readFileToString(new File(jarDir.getAbsoluteFile() + File.separator + configName + ".json"), Constants.DEFAULT_CHARSET);
			JSONObject jsonObject = JSON.parseObject(ibJson);
			commonOptionPanel.setTemplateDir(jsonObject.getString("templateDir"));
			commonOptionPanel.setOutputDir(jsonObject.getString("outputDir"));
			SourcePluginDefinition sourcePluginDefinition = jsonObject.getObject("sourcePlugin", SourcePluginDefinition.class);
			sourcePluginOptionPanel.selectedPlugin(sourcePluginDefinition);
			templateEnginePluginOptionPanel.selectedPlugin(jsonObject.getObject("templateEnginePlugin", PluginDefinition.class));
			if(sourcePluginInputPanel != null){
				sourcePluginInputPanel.loadFromJson(jsonObject.getString("inputModel"));
			}
			extraOptionPanel.setGlobal(jsonObject.getString("global"));
		} catch (FileNotFoundException e){
		} catch (IOException | JSONException e) {
			logger.error("read config file fail.", e);
		}
		
	}
	
	private void addOtherComponent() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 20));
		double thisHeight = heightPerComponent * 2;
		panel.setPreferredSize(new Dimension((int)(getWidth() * 0.9), (int)(getHeight() * thisHeight)));
		JButton githubBtn = new JButton("Github");
		githubBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URL("https://github.com/lcmatrix-code-generator").toURI());
				} catch (MalformedURLException e1) {
				} catch (IOException e1) {
				} catch (URISyntaxException e1) {
				}
			}
		});
		panel.add(githubBtn);
		contentPanel.add(panel);
	}
	
	private JMenu fileMenu;
	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		fileMenu.add(createSaveConfigMenuItem());
		fileMenu.add(createLoadConfigMenuItem());
		
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
	}
	
	private JMenuItem createSaveConfigMenuItem() {
		JMenuItem saveConfigMenuItem = new JMenuItem("Save Configurations...");
		saveConfigMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String configName = JOptionPane.showInputDialog("Enter the name of configurations:");
				if(configName == null) {
					return;
				}
				Object inputModel = null;
				if(sourcePluginInputPanel != null){
					try {
						inputModel = sourcePluginInputPanel.getInputModel();
					} catch (SourcePluginInputPanel.ValidateException ex) {
						JOptionPane.showMessageDialog(MainWindow.this, ex.getMessage());
						return;
					}
				}
				String templateDir = commonOptionPanel.getTemplateDir();
				String outputDir = commonOptionPanel.getOutputDir();
				String globalStr = extraOptionPanel.getGlobal();
				saveConfigurations(sourcePluginOptionPanel.getSelectedPlugin(), inputModel, templateEnginePluginOptionPanel.getSelectedPlugin(), templateDir, outputDir, globalStr, configName);
				//reload load configuration menu
				fileMenu.remove(1);
				fileMenu.add(createLoadConfigMenuItem());
			}
		});
		return saveConfigMenuItem;
	}
	
	private JMenuItem createLoadConfigMenuItem() {
		JMenu loadConfigMenu = new JMenu("Load Configurations");
		File jarDir = DirUtil.getJarDir();
		File[] configFiles = jarDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile() && pathname.getName().endsWith(".json");
			}
		});
		for(File f : configFiles) {
			JMenuItem configFileMenuItem = new JMenuItem(f.getName());
			configFileMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String fileName = ((JMenuItem)e.getSource()).getText();
					readConfigurations(fileName.substring(0, fileName.lastIndexOf(".json")));
				}
			});
			loadConfigMenu.add(configFileMenuItem);
		}
		return loadConfigMenu;
	}
}
