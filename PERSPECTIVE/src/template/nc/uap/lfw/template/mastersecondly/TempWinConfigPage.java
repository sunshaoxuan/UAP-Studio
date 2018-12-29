package nc.uap.lfw.template.mastersecondly;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.common.XmlCommonTool;
import nc.uap.lfw.component.CreateComponentDialog;
import nc.uap.lfw.component.SelectComponentDialog;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.dev.LfwComponent;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.lang.M_template;
import nc.uap.lfw.perspective.rule.CodeRuleChecker;
import nc.uap.lfw.template.NewTempleteWindowWizard;
import nc.uap.lfw.template.ShowFilesWillBeAddedPage;
import nc.uap.lfw.template.TempNextUsedWizardPage;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author guomq1 2012-7-30
 */
public class TempWinConfigPage extends TempNextUsedWizardPage {

	public static final String POPWIN = "pop"; //$NON-NLS-1$
	public static final String POPWIN_NAME = M_template.TempWinConfigPage_3;
	public static final String LISTWIN = "list"; //$NON-NLS-1$
	public static final String LISTWIN_NAME = M_template.TempWinConfigPage_4;
	private StringDialogField listwindowIdField;
	private StringDialogField listwindowNameField;
	private StringDialogField cardwindowIdField;
	private StringDialogField cardwindowNameField;
	private StringDialogField componentNameField;
	private StringDialogField windowControllerField;
	private ComboDialogField windowLocationField;
	private Combo sourceFolderCombo;
	private Application app = LFWAMCPersTool.getCurrentApplication();
	private String appid = app.getId();
	private String projectPath = LFWAMCPersTool.getProjectPath();
	private IProject project = LFWPersTool.getCurrentProject();
	private int ind = 0;
	private String iprojectPath = null;
	protected String listWinPath;
	protected String popWinPath;
	protected String popWinCtrl;
	protected String listWinCtrl;
	protected String listWinCtrlPath;
	protected String listViewCtrlPath;
	protected String popWinCtrlPath;
	protected String popViewCtrlPath;
	private String listWinCtrlClazz;
	private String listViewCtrlClazz;
	private String popWinCtrlClazz;
	private String popViewCtrlClazz;
	private String listWinCtrlName;
	private String listViewCtrlName;
	private String popWinCtrlName;
	private String popViewCtrlName;
	private String listWinCtrlPack;
	private String listViewCtrlPack;
	private String popWinCtrlPack;
	private String popViewCtrlPack;
	private String prefix;
	private String bcpId;
	private String realAppId;
	private Map<String,LfwComponent> componentMap;

	// String projectname = project.getName();

	@Override
	public void createControl(Composite parent) {
		prefix = LfwCommonTool.getModuleProperty(project, CodeRuleChecker.PACK_PREFIX);
		bcpId = LFWPersTool.getBcpId(LFWPersTool.getCurrentTreeItem());
		realAppId = appid.indexOf(bcpId+"_")>-1?appid.replace(bcpId+"_", ""):appid; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		initializeDialogUnits(parent);
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(new GridLayout(4, true));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		listwindowIdField = new StringDialogField();
		listwindowIdField.setLabelText("ListWindow ID"); //$NON-NLS-1$
		listwindowIdField.doFillIntoGrid(composite, 4);
		listwindowIdField.setText(realAppId+"_listwin");  //$NON-NLS-1$
		listwindowIdField.setEnabled(false);

		listwindowNameField = new StringDialogField();
		listwindowNameField.setLabelText("ListWindow Name"); //$NON-NLS-1$
		listwindowNameField.doFillIntoGrid(composite, 4);
		listwindowNameField.setText(app.getCaption()+M_template.TempWinConfigPage_5); 
		
		cardwindowIdField = new StringDialogField();
		cardwindowIdField.setLabelText("CardWindow ID"); //$NON-NLS-1$
		cardwindowIdField.doFillIntoGrid(composite, 4);
		cardwindowIdField.setText(realAppId+"_cardwin");  //$NON-NLS-1$
		cardwindowIdField.setEnabled(false);

		cardwindowNameField = new StringDialogField();
		cardwindowNameField.setLabelText("CardWindow Name"); //$NON-NLS-1$
		cardwindowNameField.doFillIntoGrid(composite, 4);
		cardwindowNameField.setText(app.getCaption()+M_template.TempWinConfigPage_6); 

		componentNameField = new StringDialogField();
		componentNameField.setLabelText(M_template.TempWinConfigPage_7);
		componentNameField.doFillIntoGrid(composite, 3);
		componentNameField.setText(prefix+"."+bcpId+".uiComps"); //$NON-NLS-1$ //$NON-NLS-2$
//		componentNameField.setText(CreateComponentDialog.COMP_PRE + appid); //$NON-NLS-1$
		Button btn1 = new Button(composite, SWT.NULL);
		btn1.setText(M_template.TempWinConfigPage_8);
		btn1.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				SelectComponentDialog dialog = new SelectComponentDialog(null, M_template.TempWinConfigPage_9);
				if(dialog.open() == IDialogConstants.OK_ID){
					try{
						String compid = dialog.getComponentId();
						if(compid==null){
							compid = dialog.getComponentText();
							LFWPersTool.createComponent(compid);
					    	project.refreshLocal(2, null);
						}
						componentNameField.setText(compid);
					
					}
					catch(Exception ex){
						MainPlugin.getDefault().logError(ex.getMessage(),ex);
					}
					
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		
		});
		
		windowLocationField = new ComboDialogField(0);
		windowLocationField.setLabelText(M_template.TempWinConfigPage_2);
		List<String> sourceFolderList = null;
		if (LfwCommonTool.isBCPProject(project)) {
			String bussiPath = LFWAMCPersTool.getBCPProjectPath();
			String bussiCompnentName = bussiPath.substring(bussiPath.lastIndexOf("/") + 1); //$NON-NLS-1$
			sourceFolderList = LFWTool.getBpcPackage(bussiCompnentName);
			windowLocationField.setItems(sourceFolderList.toArray(new String[0]));
			windowLocationField.doFillIntoGrid(composite, 4);
			windowLocationField.selectItem(bussiCompnentName + "/src/public/"); //$NON-NLS-1$
			if (!(bussiCompnentName + "/src/public/").equals(windowLocationField.getText())) //$NON-NLS-1$
				windowLocationField.selectItem(0);
		} 
		else {
			sourceFolderList = LFWTool.getAllRootPackage();
			windowLocationField.setItems(sourceFolderList.toArray(new String[0]));
			windowLocationField.doFillIntoGrid(composite, 4);
			windowLocationField.selectItem("src/public/"); //$NON-NLS-1$
			if (!"src/public/".equals(windowLocationField.getText())) //$NON-NLS-1$
				windowLocationField.selectItem(0);
		}
		windowControllerField = new StringDialogField();
		windowControllerField.setLabelText(M_template.TempWinConfigPage_10);
		windowControllerField.doFillIntoGrid(composite, 4);
		windowControllerField.setText(prefix + "."+bcpId+"."+realAppId); //$NON-NLS-1$ //$NON-NLS-2$
		ind = projectPath.lastIndexOf("/"); //$NON-NLS-1$
		iprojectPath = projectPath.substring(0, ind);
		setControl(composite);
	}

	public TempWinConfigPage(String pageName) {
		super(pageName);
		setTitle(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_WINDOW_MAINPAGE_TITLE));
		setDescription(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_FORTHPAGE_DESC));
	}

	public String getListWindowId() {
		return listwindowIdField.getText();
	}

	public String getPopWindowId() {
		return cardwindowIdField.getText();
	}
	
	public String getListWindowCaption() {
		return listwindowNameField.getText();
	}
	
	public String getComponentId() {
		return componentNameField.getText();
	}
	
	public String getPopWindowCaption() {
		return cardwindowNameField.getText();
	}
	
	public String getLocation() {
		return windowLocationField.getText();
	}
	
	public String getController() {
		return windowControllerField.getText();
	}
	
	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getIprojectPath() {
		return iprojectPath;
	}

	public void setIprojectPath(String iprojectPath) {
		this.iprojectPath = iprojectPath;
	}

	public IWizardPage getNextPage() {

		if (getWizard() == null) {
			return null;
		}
		
		int index = ((NewTempleteWindowWizard) getWizard()).getIndex();
		ShowFilesWillBeAddedPage showfilepage = ((NewTempleteWindowWizard) getWizard()).getShowFilesWillBeAddedPage();

		
		
		showfilepage.getText().setText(""); //$NON-NLS-1$
		List<String> filemessages = new ArrayList<String>();
		addMessages(filemessages);
		for (int i = 0; i < filemessages.size(); i++) {
			showfilepage.getText().insert(filemessages.get(i) + "\n\r"); //$NON-NLS-1$
		}
		return showfilepage;
	}
	
	protected void addMessages(List<String> filemessages){
		String listWindowId = getListWindowId();
		String popWindowId = getPopWindowId();
		String listFullPath = null;
		String popFullPath = null;
		if(getComponentId().equals(LfwUIComponent.ANNOYUICOMPONENT)||getComponentId().equals("")){ //$NON-NLS-1$
			listFullPath = listWindowId;
			popFullPath = popWindowId;
		}
		else{
			listFullPath = (getComponentId() + "." + listWindowId).replaceAll("\\.", "/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			popFullPath = (getComponentId() + "." + popWindowId).replaceAll("\\.", "/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		
		String location = this.getLocation();
		String controllerPrefix = this.getController().replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		listWinPath = projectPath + "/web/html/nodes/" + listFullPath; //$NON-NLS-1$
		popWinPath = projectPath + "/web/html/nodes/" + popFullPath; //$NON-NLS-1$
		listWinCtrl = iprojectPath + "/" + location + controllerPrefix + "/" + listWindowId; //$NON-NLS-1$ //$NON-NLS-2$
		popWinCtrl = iprojectPath + "/" + location + controllerPrefix + "/" + popWindowId; //$NON-NLS-1$ //$NON-NLS-2$
		
		String mainwinviewwdpath = listWinPath + "/main/widget.wd"; //$NON-NLS-1$ //$NON-NLS-2$
		String mainwinviewumpath = listWinPath + "/main/uimeta.um"; //$NON-NLS-1$ //$NON-NLS-2$
		String editwinviewwdpath = popWinPath + "/main/widget.wd"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String editwinviewumpath = popWinPath + "/main/uimeta.um"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String mainwinpmpath = listWinPath + "/pagemeta.pm"; //$NON-NLS-1$ //$NON-NLS-2$
		String mainwinumpath = listWinPath + "/uimeta.um"; //$NON-NLS-1$ //$NON-NLS-2$
		String mainwinjspath = listWinPath + "/include.js"; //$NON-NLS-1$ //$NON-NLS-2$
		String editwinpmpath = popWinPath + "/pagemeta.pm"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String editwinumpath = popWinPath + "/uimeta.um"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		String controllerPrefixDot = this.getController();
//		String listWinCtrlPackDot = (this.getController() + "." + getListWindowId() + ".ctrl");
//		String listViewCtrlPackDot = (this.getController() + "." + getListWindowId() + ".view.ctrl");
//		String popWinCtrlPackDot = (this.getController() + "." + getPopWindowId() + ".ctrl");
//		String popViewCtrlPackDot = (this.getController() + "." + getPopWindowId() + ".view.ctrl");
		
		listWinCtrlPack = controllerPrefixDot.replaceAll("\\.", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		listViewCtrlPack = controllerPrefixDot.replaceAll("\\.", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		popWinCtrlPack = controllerPrefixDot.replaceAll("\\.", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		popViewCtrlPack = controllerPrefixDot.replaceAll("\\.", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		
//		listWinCtrlPath = listWinCtrlPack + "/"+realAppId + "ListWinController";
//		listViewCtrlPath = listViewCtrlPack + "/" + realAppId + "ListViewController";
//		popWinCtrlPath = popWinCtrlPack + "/" + realAppId + "CardWinController";
//		popViewCtrlPath = popViewCtrlPack + "/" + realAppId + "CardViewController";
		String uprealAppid = realAppId.replaceFirst(realAppId.substring(0,1), realAppId.substring(0,1).toUpperCase());
		
		listWinCtrlName = uprealAppid+"ListWinCtrl"; //$NON-NLS-1$
		listViewCtrlName = uprealAppid+"ListWinMainViewCtrl"; //$NON-NLS-1$
		popWinCtrlName = uprealAppid+"CardWinCtrl"; //$NON-NLS-1$
		popViewCtrlName = uprealAppid+"CardWinMainViewCtrl"; //$NON-NLS-1$
		
		listWinCtrlClazz = controllerPrefixDot + "."+listWinCtrlName; //$NON-NLS-1$
		listViewCtrlClazz = controllerPrefixDot + "."+listViewCtrlName; //$NON-NLS-1$
		popWinCtrlClazz = controllerPrefixDot + "."+popWinCtrlName; //$NON-NLS-1$
		popViewCtrlClazz = controllerPrefixDot+ "."+popViewCtrlName; //$NON-NLS-1$
		
		listWinCtrlPath = listWinCtrlClazz.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		listViewCtrlPath =listViewCtrlClazz.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		popWinCtrlPath = popWinCtrlClazz.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		popViewCtrlPath = popViewCtrlClazz.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$

		filemessages.add(mainwinviewwdpath);
		filemessages.add(mainwinviewumpath);
		filemessages.add(editwinviewwdpath);
		filemessages.add(editwinviewumpath);
		filemessages.add(mainwinpmpath);
		filemessages.add(mainwinumpath);
		filemessages.add(mainwinjspath);
		filemessages.add(editwinpmpath);
		filemessages.add(editwinumpath);
		filemessages.add(iprojectPath + "/" + location +listWinCtrlPath + ".java"); //$NON-NLS-1$ //$NON-NLS-2$
		filemessages.add(iprojectPath + "/" + location +listViewCtrlPath + ".java"); //$NON-NLS-1$ //$NON-NLS-2$
		filemessages.add(iprojectPath + "/" + location +popWinCtrlPath + ".java"); //$NON-NLS-1$ //$NON-NLS-2$
		filemessages.add(iprojectPath + "/" + location +popViewCtrlPath + ".java"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getListWinPath() {
		return listWinPath;
	}

	public void setListWinPath(String listWinPath) {
		this.listWinPath = listWinPath;
	}

	public String getPopWinPath() {
		return popWinPath;
	}

	public void setPopWinPath(String popWinPath) {
		this.popWinPath = popWinPath;
	}

	public String getPopWinCtrl() {
		return popWinCtrl;
	}

	public void setPopWinCtrl(String popWinCtrl) {
		this.popWinCtrl = popWinCtrl;
	}

	public String getListWinCtrl() {
		return listWinCtrl;
	}

	public void setListWinCtrl(String listWinCtrl) {
		this.listWinCtrl = listWinCtrl;
	}

	public String getListWinCtrlPath() {
		return listWinCtrlPath;
	}

	public void setListWinCtrlPath(String listWinCtrlPath) {
		this.listWinCtrlPath = listWinCtrlPath;
	}

	public String getListViewCtrlPath() {
		return listViewCtrlPath;
	}

	public void setListViewCtrlPath(String listViewCtrlPath) {
		this.listViewCtrlPath = listViewCtrlPath;
	}

	public String getPopWinCtrlPath() {
		return popWinCtrlPath;
	}

	public void setPopWinCtrlPath(String popWinCtrlPath) {
		this.popWinCtrlPath = popWinCtrlPath;
	}

	public String getPopViewCtrlPath() {
		return popViewCtrlPath;
	}

	public void setPopViewCtrlPath(String popViewCtrlPath) {
		this.popViewCtrlPath = popViewCtrlPath;
	}

	public String getListWinCtrlClazz() {
		return listWinCtrlClazz;
	}

	public void setListWinCtrlClazz(String listWinCtrlClazz) {
		this.listWinCtrlClazz = listWinCtrlClazz;
	}

	public String getListViewCtrlClazz() {
		return listViewCtrlClazz;
	}

	public void setListViewCtrlClazz(String listViewCtrlClazz) {
		this.listViewCtrlClazz = listViewCtrlClazz;
	}

	public String getPopWinCtrlClazz() {
		return popWinCtrlClazz;
	}

	public void setPopWinCtrlClazz(String popWinCtrlClazz) {
		this.popWinCtrlClazz = popWinCtrlClazz;
	}

	public String getPopViewCtrlClazz() {
		return popViewCtrlClazz;
	}

	public void setPopViewCtrlClazz(String popViewCtrlClazz) {
		this.popViewCtrlClazz = popViewCtrlClazz;
	}

	public String getListWinCtrlPack() {
		return listWinCtrlPack;
	}

	public void setListWinCtrlPack(String listWinCtrlPack) {
		this.listWinCtrlPack = listWinCtrlPack;
	}

	public String getListViewCtrlPack() {
		return listViewCtrlPack;
	}

	public void setListViewCtrlPack(String listViewCtrlPack) {
		this.listViewCtrlPack = listViewCtrlPack;
	}

	public String getPopWinCtrlPack() {
		return popWinCtrlPack;
	}

	public void setPopWinCtrlPack(String popWinCtrlPack) {
		this.popWinCtrlPack = popWinCtrlPack;
	}

	public String getPopViewCtrlPack() {
		return popViewCtrlPack;
	}

	public void setPopViewCtrlPack(String popViewCtrlPack) {
		this.popViewCtrlPack = popViewCtrlPack;
	}
	

	public String getListWinCtrlName() {
		return listWinCtrlName;
	}

	public void setListWinCtrlName(String listWinCtrlName) {
		this.listWinCtrlName = listWinCtrlName;
	}

	public String getListViewCtrlName() {
		return listViewCtrlName;
	}

	public void setListViewCtrlName(String listViewCtrlName) {
		this.listViewCtrlName = listViewCtrlName;
	}

	public String getPopWinCtrlName() {
		return popWinCtrlName;
	}

	public void setPopWinCtrlName(String popWinCtrlName) {
		this.popWinCtrlName = popWinCtrlName;
	}

	public String getPopViewCtrlName() {
		return popViewCtrlName;
	}

	public void setPopViewCtrlName(String popViewCtrlName) {
		this.popViewCtrlName = popViewCtrlName;
	}

	@Override
	protected boolean nextButtonClick() {
		String compid = getComponentId();
		try{
			if(compid!=null){
				if("".equals(compid)){ //$NON-NLS-1$
					compid = LfwUIComponent.ANNOYUICOMPONENT;
				}
				String moduleId = LFWPersTool.getProjectModuleName(project);
				componentMap = LFWAMCConnector.getCacheComponentMap(moduleId, bcpId);
				Iterator<String> iter = componentMap.keySet().iterator();
				boolean exsitComp = false;
				while(iter.hasNext()){
					String id = iter.next();
					if(id.equals(compid)){
						exsitComp = true;
					}
				}
				if(!exsitComp){
					if(MessageDialog.openConfirm(null, M_template.TempWinConfigPage_11, M_template.TempWinConfigPage_12)){
						LFWPersTool.createComponent(compid);
						project.refreshLocal(2, null);
						return true;
					}
					else{
						return false;
					}
				
				}
				else
					return true;
			}
		}
		catch(Exception e){
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
		return false;
	}
	
	@Override
	protected boolean previousButtonClick() {
		return true;
	}
}
