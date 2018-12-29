/**
 * 
 */
package nc.uap.lfw.window.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.lang.M_window;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.rule.CodeRuleChecker;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author chouhl
 *
 */
public class CreateNormalViewDialog extends DialogWithTitle {
	
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
	 * WindowID
	 */
	private String id;
	/**
	 * Window名称
	 */
	private String name;
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
	/**
	 * 流式布局
	 */
	private Button flowlayoutRadio;
	/**
	 * 个性化
	 */
	private Button flowlayoutRadio3;
	/**
	 * 是否流式布局
	 */
	private boolean isFlowlayout = true;
	
	private boolean isNotRefPublicView = true;
	
	private boolean isCreateView = true;
	
	private boolean canFreeDesign = true;
	
	private Text preferredWidthText;
	
	private Text preferredHeightText;
	
	
	private String preferredWidth = "0"; //$NON-NLS-1$
	
	private String preferredHeight = "0"; //$NON-NLS-1$
	
	public CreateNormalViewDialog(String title) {
		super(null, title);
	}
	
	public CreateNormalViewDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		//ID
		Label idLabel = new Label(container, SWT.NONE);
		idLabel.setText("Id:"); //$NON-NLS-1$
		idText = new Text(container, SWT.NONE);
		idText.setLayoutData(new GridData(220,20));
		idText.setText(""); //$NON-NLS-1$
		//名称
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText(M_window.CreateNormalViewDialog_0);
		nameText = new Text(container, SWT.NONE);
		nameText.setLayoutData(new GridData(220,20));
		nameText.setText(""); //$NON-NLS-1$
		nameText.addFocusListener(new FocusListener(){
			@Override
			public void focusLost(FocusEvent e) {
				
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(nameText.getText() == null || nameText.getText().trim().length() == 0){
					nameText.setText(idText.getText());
				}
				nameText.selectAll();
			}
		});
		if(isNotRefPublicView){
			//源文件夹
			new Label(container, SWT.NONE).setText(M_window.CreateNormalViewDialog_1);
			sourceFolderCombo = new Combo(container, SWT.READ_ONLY);
			sourceFolderCombo.setLayoutData(new GridData(150, 1));
			sourceFolderCombo.removeAll();

//			List<String> sourceFolderList = LFWTool.getAllRootPackage();
			boolean selected  = false;
			IProject project = LFWAMCPersTool.getCurrentProject();
			List<String> sourceFolderList = new ArrayList<String>();
			if(LfwCommonTool.isBCPProject(project)){
				String bussiPath = LFWAMCPersTool.getBCPProjectPath();
				String bussiCompnentName = bussiPath.substring(bussiPath.lastIndexOf("/")+1); //$NON-NLS-1$
				sourceFolderList = LFWTool.getBpcPackage(bussiCompnentName);
			}else{
				sourceFolderList = LFWTool.getAllRootPackage();
				}
			for (int i = 0 ; i < sourceFolderList.size(); i++ ) {
				String sourceFolder = sourceFolderList.get(i);
				sourceFolderCombo.add(sourceFolder);
				sourceFolderCombo.setData(sourceFolder, sourceFolder);
				if(sourceFolder.endsWith("src/public/")){ //$NON-NLS-1$
					sourceFolderCombo.select(i);
					selected = true;
				}
			}
			if(sourceFolderCombo.getItemCount() > 0 && !selected){
				sourceFolderCombo.select(0);
			}
			//Controller类全路径
			Label controllerLabel = new Label(container, SWT.NONE);
			controllerLabel.setText(M_window.CreateNormalViewDialog_2);
			controllerText = new Text(container, SWT.NONE);
			controllerText.setLayoutData(new GridData(220,20));
			controllerText.setText(""); //$NON-NLS-1$
			controllerText.addFocusListener(new FocusListener(){
				@Override
				public void focusLost(FocusEvent e) {
					
				}
				@Override
				public void focusGained(FocusEvent e) {
					if(nameText.getText() != "" && nameText.getText() != null){ //$NON-NLS-1$
						if(controllerText.getText() == null || controllerText.getText().trim().length() == 0){
							IProject proj = LFWPersTool.getCurrentProject();
							String prefix = LfwCommonTool.getModuleProperty(proj, CodeRuleChecker.PACK_PREFIX);						
							String bcpId = LFWPersTool.getBcpId(LFWPersTool.getCurrentTreeItem());
							String viewid = idText.getText();
							String upWinId = viewid.replaceFirst(viewid.substring(0,1), viewid.substring(0,1).toUpperCase());					
							String winId = viewid;
							if(LFWPersTool.getCurrentTreeItem() instanceof LFWPageMetaTreeItem){
								winId = ((LFWPageMetaTreeItem)LFWPersTool.getCurrentTreeItem()).getPm().getId();
							}
							controllerText.setText(prefix +"."+bcpId+"."+winId + "."+upWinId+"ViewController"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//							controllerText.setText("nc.bs." + idText.getText() + ".ctrl.ViewController"); //$NON-NLS-1$ //$NON-NLS-2$
							}
						}
					controllerText.selectAll();
					}
				});
			//流式布局
			new Label(container, SWT.NONE).setText(M_window.CreateNormalViewDialog_3);
			Composite radioContainer = new Composite(container, SWT.NONE);
			radioContainer.setLayout(new GridLayout(2, false));
			radioContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
			flowlayoutRadio = new Button(radioContainer, SWT.RADIO);
			flowlayoutRadio.setSelection(true);
			flowlayoutRadio.setText(M_window.CreateNormalViewDialog_4);
			Button flowlayoutRadio1 = new Button(radioContainer, SWT.RADIO);
			flowlayoutRadio1.setSelection(false);
			flowlayoutRadio1.setText(M_window.CreateNormalViewDialog_5);
			//个性化
			new Label(container,SWT.NONE).setText(M_window.CreateNormalViewDialog_6);
			Composite radioContainer2 = new Composite(container, SWT.NONE);
			radioContainer2.setLayout(new GridLayout(2, false));
			radioContainer2.setLayoutData(new GridData(GridData.FILL_BOTH));
			flowlayoutRadio3 = new Button(radioContainer2, SWT.RADIO);
			flowlayoutRadio3.setSelection(true);
			flowlayoutRadio3.setText(M_window.CreateNormalViewDialog_4);
			Button flowlayoutRadio4 = new Button(radioContainer2, SWT.RADIO);
			flowlayoutRadio4.setSelection(false);
			flowlayoutRadio4.setText(M_window.CreateNormalViewDialog_5);
		}
		
		Label lb = new Label(container, SWT.NONE);
		lb.setText(M_window.CreateNormalViewDialog_15);
		GridData layout = new GridData();
		layout.horizontalSpan = 2;
		//lb.setForeground(SWT.COLOR_RED);
		lb.setLayoutData(layout);
		//最优宽度
		new Label(container, SWT.NONE).setText(M_window.CreateNormalViewDialog_16);
		preferredWidthText = new Text(container, SWT.NONE);
		preferredWidthText.setLayoutData(new GridData(220,20));
		preferredWidthText.setText(preferredWidth); //$NON-NLS-1$
		
		//最优宽度
		new Label(container, SWT.NONE).setText(M_window.CreateNormalViewDialog_17);
		preferredHeightText = new Text(container, SWT.NONE);
		preferredHeightText.setLayoutData(new GridData(220,20));
		preferredHeightText.setText(preferredHeight); //$NON-NLS-1$
		
		return container;
	}
	
	protected void okPressed() {
		if(idText.getText() == null || idText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_window.CreateNormalViewDialog_18, M_window.CreateNormalViewDialog_19);
			return;
		}
		setId(idText.getText().trim());
		//Name校验
		if(nameText.getText() == null || nameText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_window.CreateNormalViewDialog_7, M_window.CreateNormalViewDialog_8);
			return;
		}
		setName(nameText.getText().trim());

		if(!checkNumber(preferredWidthText.getText())){
			MessageDialog.openError(null, M_window.CreateNormalViewDialog_20, M_window.CreateNormalViewDialog_21);
			return;
		}
		
		setPreferredWidth(preferredWidthText.getText());
		
		if(!checkNumber(preferredHeightText.getText())){
			MessageDialog.openError(null, M_window.CreateNormalViewDialog_22, M_window.CreateNormalViewDialog_23);
			return;
		}
		
		setPreferredHeight(preferredHeightText.getText());
		if(isCreateView){//创建View
			try{
				LFWTool.createNodeCheck(name, ILFWTreeNode.VIEW);
			}catch(Exception e){
				MainPlugin.getDefault().logError(e.getMessage(), e);
				MessageDialog.openError(null, M_window.CreateNormalViewDialog_7, e.getMessage());
				return;
			}
		}else{//创建PublicView
			Map<String, LfwView> publicViews = LFWAMCPersTool.getPublicViewsByContext();
			if(publicViews != null && publicViews.get(name) != null){
				MessageDialog.openError(null, WEBPersConstants.NEW_PUBLIC_VIEW, M_window.CreateNormalViewDialog_9 + name + M_window.CreateNormalViewDialog_10 + WEBPersConstants.PUBLIC_VIEW_SUB + "!"); //$NON-NLS-3$ //$NON-NLS-1$
				return;
			}
		}
		if(isNotRefPublicView){
			//源文件夹校验
			if(sourceFolderCombo.getText().trim().length() == 0){
				MessageDialog.openError(null, M_window.CreateNormalViewDialog_7, M_window.CreateNormalViewDialog_11);
				return;
			}
			setSourcePackage(sourceFolderCombo.getText().trim());
			//Controller类校验
			if(controllerText.getText().trim().length() == 0){
				MessageDialog.openError(null, M_window.CreateNormalViewDialog_7, M_window.CreateNormalViewDialog_12);
				return;
			}
			controllerText.setText(LFWTool.upperClassName(controllerText.getText().trim()));
			setControllerClazz(controllerText.getText().trim());
			try{
				LFWTool.clazzCheck(controllerClazz);
			}catch(Exception e){
				MessageDialog.openError(null, M_window.CreateNormalViewDialog_7, e.getMessage());
				return;
			}
			try{
				LFWTool.createNodeClassFileCheck(controllerClazz, sourcePackage);
			}catch(Exception e){
				MainPlugin.getDefault().logError(e.getMessage(), e);
				boolean temp = MessageDialog.openConfirm(null, M_window.CreateNormalViewDialog_13, e.getMessage() + M_window.CreateNormalViewDialog_14);
				if(!temp){
					return;
				}else{
					controllerClazz = LFWTool.getExistWholeClassName(controllerClazz, sourcePackage);
				}
			}
			//流式布局
			setFlowlayout(flowlayoutRadio.getSelection());
			setCanFreeDesign(flowlayoutRadio3.getSelection());
		}else{
			setSourcePackage(""); //$NON-NLS-1$
			setControllerClazz(""); //$NON-NLS-1$
		}
		super.okPressed();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getControllerClazz() {
		return controllerClazz;
	}

	public void setControllerClazz(String controllerClazz) {
		this.controllerClazz = controllerClazz;
	}

	public String getSourcePackage() {
		return sourcePackage;
	}

	public void setSourcePackage(String sourcePackage) {
		this.sourcePackage = sourcePackage;
	}

	public void setNotRefPublicView(boolean isNotRefPublicView) {
		this.isNotRefPublicView = isNotRefPublicView;
	}

	public void setCreateView(boolean isCreateView) {
		this.isCreateView = isCreateView;
	}

	public boolean isFlowlayout() {
		return isFlowlayout;
	}

	public void setFlowlayout(boolean isFlowlayout) {
		this.isFlowlayout = isFlowlayout;
	}

	public boolean isCanFreeDesign() {
		return canFreeDesign;
	}

	public void setCanFreeDesign(boolean canFreeDesign) {
		this.canFreeDesign = canFreeDesign;
	}

	public String getPreferredWidth() {
		return preferredWidth;
	}

	public void setPreferredWidth(String preferredWidth) {
		this.preferredWidth = preferredWidth;
	}

	public String getPreferredHeight() {
		return preferredHeight;
	}

	public void setPreferredHeight(String preferredHeight) {
		this.preferredHeight = preferredHeight;
	}
	
	private boolean checkNumber(String text) {
		try{
			Integer value = Integer.parseInt(text);
			if(value < 0)
				return false;
		}
		catch(NumberFormatException exp){
			return false;
		}
		return true;
	}
}
