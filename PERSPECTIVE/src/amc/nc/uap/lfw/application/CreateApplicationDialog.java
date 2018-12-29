/**
 * 
 */
package nc.uap.lfw.application;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.data.LfwParameter;
import nc.uap.lfw.core.log.LfwOperatorLogVO;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.lang.M_application;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.rule.CodeRuleChecker;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 * 新建Application节点对话框类
 * @author chouhl
 *
 */
public class CreateApplicationDialog extends DialogWithTitle {

	public static final String PACK_PRE = "com.prod.app.";
	/**
	 * ID输入框
	 */
	private Text idText;
	/**
	 * 名称输入框
	 */
	private Text nameText;
	/**
	 * Controller类全路径输入框
	 */
	private Text controllerText;
	/**
	 * ApplicationID
	 */
	private String applicationId;
	/**
	 * Application名称
	 */
	private String applicationName;
	/**
	 * Controller类全路径 
	 */
	private String controllerClazz;
	/**
	 * 源文件夹
	 */
	private String sourcePackage;
	/**
	 * 下拉框控件
	 */
	private Combo sourceFolderCombo;
	
	
	
	public CreateApplicationDialog(String title) {
		super(null, title);
	}
	
	public CreateApplicationDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		// ID
		Label idLabel = new Label(container, SWT.NONE);
		idLabel.setText(M_application.CreateApplicationDialog_0);
		idText = new Text(container, SWT.NONE);
		idText.setLayoutData(new GridData(220, 20));
		String bcpPath = LFWPersTool.getBcpPath(LFWPersTool.getCurrentTreeItem());
		String bcpId = bcpPath.substring(bcpPath.lastIndexOf("\\")+1,bcpPath.length());
		idText.setText(bcpId+"_"); //$NON-NLS-1$
		idText.setSelection(bcpId.length()+1);
		// 名称
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText(M_application.CreateApplicationDialog_2);
		nameText = new Text(container, SWT.NONE);
		nameText.setLayoutData(new GridData(220, 20));
		nameText.setText(""); //$NON-NLS-1$
		nameText.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {

			}

			@Override
			public void focusGained(FocusEvent e) {
				if (nameText.getText() == null
						|| nameText.getText().trim().length() == 0) {
					nameText.setText(idText.getText());
				}
				nameText.selectAll();
			}
		});
		// 源文件夹
		new Label(container, SWT.NONE)
				.setText(M_application.CreateApplicationDialog_4);
		sourceFolderCombo = new Combo(container, SWT.READ_ONLY);
		sourceFolderCombo.setLayoutData(new GridData(150, 1));
		sourceFolderCombo.removeAll();

		boolean selected = false;
		IProject project = LFWAMCPersTool.getCurrentProject();
		List<String> sourceFolderList = new ArrayList<String>();
		if (LfwCommonTool.isBCPProject(project)) {
			String bussiPath = LFWAMCPersTool.getBCPProjectPath();
			String bussiCompnentName = bussiPath.substring(bussiPath
					.lastIndexOf("/") + 1); //$NON-NLS-1$
			sourceFolderList = LFWTool.getBpcPackage(bussiCompnentName);
		} else {
			sourceFolderList = LFWTool.getAllRootPackage();
		}
		for (int i = 0; i < sourceFolderList.size(); i++) {
			String sourceFolder = sourceFolderList.get(i);
			sourceFolderCombo.add(sourceFolder);
			sourceFolderCombo.setData(sourceFolder, sourceFolder);
			if (sourceFolder.endsWith("src/public/")) { //$NON-NLS-1$
				sourceFolderCombo.select(i);
				selected = true;
			}
		}
		if (sourceFolderCombo.getItemCount() > 0 && !selected) {
			sourceFolderCombo.select(0);
		}
		// Controller类全路径
		Label controllerLabel = new Label(container, SWT.NONE);
		controllerLabel.setText(M_application.CreateApplicationDialog_7);
		controllerText = new Text(container, SWT.NONE);
		controllerText.setLayoutData(new GridData(220, 20));
		controllerText.setText(""); //$NON-NLS-1$
		controllerText.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {

			}

			@Override
			public void focusGained(FocusEvent e) {
				if (idText.getText() != "" && idText.getText() != null) { //$NON-NLS-1$
					if (controllerText.getText() == null
							|| controllerText.getText().trim().length() == 0) {
						IProject proj = LFWPersTool.getCurrentProject();
						String prefix = LfwCommonTool.getModuleProperty(proj, CodeRuleChecker.PACK_PREFIX);						
						String bcpId = LFWPersTool.getBcpId(LFWPersTool.getCurrentTreeItem());
						String appid = idText.getText().indexOf(bcpId+"_")>-1?idText.getText().split("_")[1]:idText.getText();
						String upAppId = appid.replaceFirst(appid.substring(0,1), appid.substring(0,1).toUpperCase());
						controllerText.setText(prefix +"."+bcpId+"."+appid + "."+upAppId+"AppController"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				controllerText.selectAll();
			}
		});
		return container;
	}
	
	protected void okPressed() {
		//ID校验
		if(idText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_application.CreateApplicationDialog_12, M_application.CreateApplicationDialog_13);
			return;
		}
		if(LFWAMCConnector.getApplicationById(idText.getText())!=null){
			MessageDialog.openError(null, M_application.CreateApplicationDialog_12, "application ID 不能与环境中已存在的app重复");
			return;
		}
		setApplicationId(idText.getText().trim());
		try{
			LFWTool.idCheck(applicationId);
			LFWTool.createNodeCheck(applicationId, ILFWTreeNode.APPLICATION);
		}catch(Exception e){
			MessageDialog.openError(null, M_application.CreateApplicationDialog_14, e.getMessage());
			return;
		}
		//Name校验
		if(nameText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_application.CreateApplicationDialog_15, M_application.CreateApplicationDialog_16);
			return;
		}
		setApplicationName(nameText.getText().trim());
		//源文件夹校验
		if(sourceFolderCombo.getText().trim().length() == 0){
			MessageDialog.openError(null, M_application.CreateApplicationDialog_17, M_application.CreateApplicationDialog_18);
			return;
		}
		setSourcePackage(sourceFolderCombo.getText().trim());
		//Controller类校验
		if(controllerText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_application.CreateApplicationDialog_19, M_application.CreateApplicationDialog_20);
			return;
		}
		controllerText.setText(LFWTool.upperClassName(controllerText.getText().trim()));
		setControllerClazz(controllerText.getText().trim());
		try{
			LFWTool.clazzCheck(controllerClazz);
		}catch(Exception e){
			MessageDialog.openError(null, M_application.CreateApplicationDialog_21, e.getMessage());
			return;
		}
		try{
			LFWTool.createNodeClassFileCheck(controllerClazz, sourcePackage);
		}catch(Exception e){
			MainPlugin.getDefault().logError(e.getMessage(), e);
			boolean temp = MessageDialog.openConfirm(null, M_application.CreateApplicationDialog_22, e.getMessage() + M_application.CreateApplicationDialog_23);
			if(!temp){
				return;
			}else{
				controllerClazz = LFWTool.getExistWholeClassName(controllerClazz, sourcePackage);
			}
		}
		super.okPressed();
	}

	public String getApplicationId() {
		return this.applicationId;
	}

	public String getApplicationName() {
		return this.applicationName;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	public String getSourcePackage() {
		return sourcePackage;
	}

	public void setSourcePackage(String sourcePackage) {
		this.sourcePackage = sourcePackage;
	}

	public String getControllerClazz() {
		return controllerClazz;
	}

	public void setControllerClazz(String controllerClazz) {
		this.controllerClazz = controllerClazz;
	}

}
