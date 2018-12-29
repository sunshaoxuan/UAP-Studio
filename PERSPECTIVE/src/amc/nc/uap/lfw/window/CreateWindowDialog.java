/**
 * 
 */
package nc.uap.lfw.window;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.application.LFWApplicationTreeItem;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.common.XmlCommonTool;
import nc.uap.lfw.component.SelectComponentDialog;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.lang.M_window;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.rule.CodeRuleChecker;
import nc.uap.lfw.perspective.webcomponent.LFWComponentTreeItem;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * 新建Window节点对话框
 * @author chouhl
 *
 */
public class CreateWindowDialog extends DialogWithTitle  {

	/**
	 * ID输入框
	 */
	private Text idText;
	/**
	 * 名称输入框
	 */
	private Text nameText;
	/**
	 * 组件ID选择框
	 */
	private Text compidText;
	/**
	 * Controller类全路径输入框
	 */
	private Text controllerText;
	
	private Text preferredWidthText;
	
	private Text preferredHeightText;
	
	/**
	 * WindowID
	 */
	private String id;
	/**
	 * Window名称
	 */
	private String name;
	/**
	 * ComponentID
	 */
	private String compid;
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
	 * 是否流式布局
	 */
	private boolean isFlowlayout = true;
	
	private String preferredWidth = "0"; //$NON-NLS-1$
	
	private String preferredHeight = "0"; //$NON-NLS-1$
	
	private String templateType;
	
	private Map<String, String> extInfo;
	
	public CreateWindowDialog(String templateType) {
		super(null, WEBPersConstants.NEW_WINDOW);
		this.templateType = templateType;
	}
	
	public CreateWindowDialog(Shell parentShell, String templateType) {
		super(parentShell, WEBPersConstants.NEW_WINDOW);
		this.templateType = templateType;
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		//ID
		Label idLabel = new Label(container, SWT.NONE);
		idLabel.setText(M_window.CreateWindowDialog_0);
		idText = new Text(container, SWT.NONE);
		idText.setLayoutData(new GridData(220,20));
		idText.setText(""); //$NON-NLS-1$
		//名称
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText(M_window.CreateWindowDialog_1);
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
		//组件ID
		Label compidLabel = new Label(container, SWT.NONE);
		compidLabel.setText(M_window.CreateWindowDialog_14);
		Composite compContainer = new Composite(container, SWT.NONE);
		compContainer.setLayout(new GridLayout(2, false));
		compContainer.setLayoutData(new GridData(SWT.LEFT));
		compidText = new Text(compContainer, SWT.BORDER);
		compidText.setLayoutData(new GridData(210,16));
		compidText.setEditable(false);
		Button btn_comp = new Button(compContainer, SWT.NONE);
		btn_comp.setText(M_window.CreateWindowDialog_15);
		btn_comp.setEnabled(false);
		TreeItem currentItem = LFWPersTool.getCurrentTreeItem();
		if(currentItem instanceof LFWApplicationTreeItem){
			compidText.setText(LfwUIComponent.ANNOYUICOMPONENT); 
			btn_comp.setEnabled(true);
			btn_comp.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent e) {
					SelectComponentDialog dialog = new SelectComponentDialog(null, M_window.CreateWindowDialog_16);
					if(dialog.open() == IDialogConstants.OK_ID){
						try{
							String compid = dialog.getComponentId();
							if(compid==null){
								compid = dialog.getComponentText();
								Document doc = XmlCommonTool.createDocument();
								Element rootNode = doc.createElement("Component"); //$NON-NLS-1$
								doc.appendChild(rootNode);
								int index = compid.lastIndexOf("."); //$NON-NLS-1$
								rootNode.setAttribute("id", compid.substring(index+1)); //$NON-NLS-1$
								rootNode.setAttribute("name",  compid.substring(index+1)); //$NON-NLS-1$
								rootNode.setAttribute("pack", compid.substring(0,index)); //$NON-NLS-1$
								
								String path = LFWPersTool.getProjectWithBcpPath()+"/web/html/nodes/"+compid.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								File folder = new File(path);
								File file = new File(folder,"component.cp"); //$NON-NLS-1$
								if(!folder.exists()){
									folder.mkdirs();
								}
						    	XmlCommonTool.documentToXml(doc, file);
						    	IProject project = LFWPersTool.getCurrentProject();
						    	project.refreshLocal(2, null);
							}
							compidText.setText(compid);
						
						}
						catch(Exception ex){
							MainPlugin.getDefault().logError(ex.getMessage(),ex);
						}
						
					}
					
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
					
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					
				}
			});
		}else{
			LFWComponentTreeItem compItem = LFWPersTool.getComponentItem(currentItem);
			LfwUIComponent component = compItem.getComponent();
			if(component.getPack().equals("")){ //$NON-NLS-1$
				compidText.setText(component.getId());
			}else{
				compidText.setText(component.getPack()+"."+component.getId()); //$NON-NLS-1$
			}
		}
		
		//源文件夹
		new Label(container, SWT.NONE).setText(M_window.CreateWindowDialog_2);
		sourceFolderCombo = new Combo(container, SWT.READ_ONLY);
		sourceFolderCombo.setLayoutData(new GridData(150, 1));
		sourceFolderCombo.removeAll();

//		List<String> sourceFolderList = LFWTool.getAllRootPackage();
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
		controllerLabel.setText(M_window.CreateWindowDialog_3);
		controllerText = new Text(container, SWT.NONE);
		controllerText.setLayoutData(new GridData(220,20));
		controllerText.setText(""); //$NON-NLS-1$
		controllerText.addFocusListener(new FocusListener(){
			@Override
			public void focusLost(FocusEvent e) {
				
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(idText.getText() != "" && idText.getText() != null){ //$NON-NLS-1$
					if(controllerText.getText() == null || controllerText.getText().trim().length() == 0){
						IProject proj = LFWPersTool.getCurrentProject();
						String prefix = LfwCommonTool.getModuleProperty(proj, CodeRuleChecker.PACK_PREFIX);						
						String bcpId = LFWPersTool.getBcpId(LFWPersTool.getCurrentTreeItem());
//						String winid = idText.getText().indexOf(bcpId+"_")>-1?idText.getText().split("_")[1]:idText.getText();
						String winid = idText.getText();
						String upWinId = winid.replaceFirst(winid.substring(0,1), winid.substring(0,1).toUpperCase());
						controllerText.setText(prefix +"."+bcpId+"."+winid + "."+upWinId+"WinController"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//						controllerText.setText("nc.bs." + idText.getText() + ".ctrl.WinController"); //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
				controllerText.selectAll();
				}
			});
		//流式布局
		new Label(container, SWT.NONE).setText(M_window.CreateWindowDialog_4);
		Composite radioContainer = new Composite(container, SWT.NONE);
		radioContainer.setLayout(new GridLayout(2, false));
		radioContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		flowlayoutRadio = new Button(radioContainer, SWT.RADIO);
		flowlayoutRadio.setSelection(true);
		flowlayoutRadio.setText(M_window.CreateWindowDialog_5);
		Button flowlayoutRadio1 = new Button(radioContainer, SWT.RADIO);
		flowlayoutRadio1.setSelection(false);
		flowlayoutRadio1.setText(M_window.CreateWindowDialog_6);
		
		Label lb = new Label(container, SWT.NONE);
		lb.setText(M_window.CreateWindowDialog_17);
		GridData layout = new GridData();
		layout.horizontalSpan = 2;
		//lb.setForeground(SWT.COLOR_RED);
		lb.setLayoutData(layout);
		
		//最优宽度
		new Label(container, SWT.NONE).setText(M_window.CreateWindowDialog_18);
		preferredWidthText = new Text(container, SWT.NONE);
		preferredWidthText.setLayoutData(new GridData(220,20));
		preferredWidthText.setText(preferredWidth); //$NON-NLS-1$
		
		//最优宽度
		new Label(container, SWT.NONE).setText(M_window.CreateWindowDialog_19);
		preferredHeightText = new Text(container, SWT.NONE);
		preferredHeightText.setLayoutData(new GridData(220,20));
		preferredHeightText.setText(preferredHeight); //$NON-NLS-1$
		
		
		return container;
	}
	
	protected void okPressed() {
		//ID校验
		if(idText.getText() == null || idText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_window.CreateWindowDialog_7, M_window.CreateWindowDialog_8);
			return;
		}
		
		if(!checkNumber(preferredWidthText.getText())){
			MessageDialog.openError(null, M_window.CreateWindowDialog_20, M_window.CreateWindowDialog_21);
			return;
		}
		
		setPreferredWidth(preferredWidthText.getText());
		
		if(!checkNumber(preferredHeightText.getText())){
			MessageDialog.openError(null, M_window.CreateWindowDialog_22, M_window.CreateWindowDialog_23);
			return;
		}
		
		setPreferredHeight(preferredHeightText.getText());
		
		setId(idText.getText().trim());
		try{
			LFWTool.idCheck(id);
			LFWTool.createNodeCheck(id, ILFWTreeNode.WINDOW);
		}catch(Exception e){
			MessageDialog.openError(null, M_window.CreateWindowDialog_7, e.getMessage());
			return;
		}
		//Name校验
		if(nameText.getText() == null || nameText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_window.CreateWindowDialog_7, M_window.CreateWindowDialog_9);
			return;
		}
		setName(nameText.getText().trim());
		setCompid(compidText.getText().trim());
		//源文件夹校验
		if(sourceFolderCombo.getText().trim().length() == 0){
			MessageDialog.openError(null, M_window.CreateWindowDialog_7, M_window.CreateWindowDialog_10);
			return;
		}
		setSourcePackage(sourceFolderCombo.getText().trim());
		//Controller类校验
		if(controllerText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_window.CreateWindowDialog_7, M_window.CreateWindowDialog_11);
			return;
		}
		controllerText.setText(LFWTool.upperClassName(controllerText.getText().trim()));
		setControllerClazz(controllerText.getText().trim());
		try{
			LFWTool.clazzCheck(controllerClazz);
		}catch(Exception e){
			MessageDialog.openError(null, M_window.CreateWindowDialog_7, e.getMessage());
			return;
		}
		try{
			LFWTool.createNodeClassFileCheck(controllerClazz, sourcePackage);
		}catch(Exception e){
			boolean temp = MessageDialog.openConfirm(null, M_window.CreateWindowDialog_12, e.getMessage() + M_window.CreateWindowDialog_13);
			if(!temp){
				return;
			}else{
				controllerClazz = LFWTool.getExistWholeClassName(controllerClazz, sourcePackage);
			}
		}
		
		//流式布局
		setFlowlayout(flowlayoutRadio.getSelection());
		super.okPressed();
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getCompid() {
		return compid;
	}

	public void setCompid(String compid) {
		this.compid = compid;
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

	public boolean isFlowlayout() {
		return isFlowlayout;
	}

	public void setFlowlayout(boolean isFlowlayout) {
		this.isFlowlayout = isFlowlayout;
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
	
	public String getTemplateType() {
		return templateType;
	}
	
	public void setExtInfo(Map<String, String> extInfo){
		this.extInfo = extInfo;
	}
	
	public Map<String, String> getExtInfo() {
		return extInfo;
	}
}
