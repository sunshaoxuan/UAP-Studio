package nc.uap.lfw.application;


import java.io.File;
import java.util.ArrayList;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.cpb.org.vos.CpModuleVO;
import nc.uap.lfw.application.excel.FuncNodeObj;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class AppFuncSelPage extends WizardPage{
	
//	private List<FuncNodeObj> funcNodeList;
	private FuncNodeObj[] funcNodes;
	
	private List appIdList;
	private List funcList;
	
	private ArrayList<String> funcidList;
	private ArrayList<String> appFile;

	protected AppFuncSelPage(String pageName) {
		super(pageName);
		setTitle(WEBPersPlugin
				.getResourceString(WEBPersConstants.KEY_EXCEL_MAINPAGE_TITLE));
		setDescription(WEBPersPlugin
				.getResourceString(WEBPersConstants.KEY_EXCEL_FUNC_DESC)); 
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		initializeDialogUnits(parent);
		final Composite composite = new Composite(parent, SWT.NULL);
		
		String bcpPath = LFWPersTool.getBcpPath(LFWPersTool.getCurrentTreeItem());
		if(((LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem()).getType().equals(ILFWTreeNode.APPLICATION)){
			composite.setLayout(new GridLayout(1, true));
		}
		else{
			composite.setLayout(new GridLayout(2, true));
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
			GridLayout layout = new GridLayout();
			final Group appGrp = new Group(composite, SWT.NONE);
			appGrp.setLayoutData(new GridData(GridData.FILL_BOTH));
			appGrp.setSize(400, 200);
			appGrp.setLayout(new GridLayout(1, false));
			appGrp.setText("可选application"); //$NON-NLS-1$	
			appIdList = new List(appGrp,SWT.SINGLE);
			appIdList.setLayoutData(new GridData(GridData.FILL_BOTH));
			
			File appFolder = new File(bcpPath,"/web/html/applications");
			appFile = new ArrayList<String>();
			if(appFolder.exists()){
				for(File app:appFolder.listFiles()){
					appFile.add(app.getName());
				}
			}
			appIdList.setItems(appFile.toArray(new String[0]));
		}
		final Group group = new Group(composite, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setSize(400, 200);
		group.setLayout(new GridLayout(1, false));
		group.setText("可选功能节点"); //$NON-NLS-1$	
		funcList = new List(group,SWT.SINGLE);
		funcList.setLayoutData(new GridData(GridData.FILL_BOTH));
		ArrayList<String> titleList = new ArrayList();
		funcidList = new ArrayList<String>();
//		String bcpPath = LFWPersTool.getBcpPath(LFWPersTool.getCurrentTreeItem());
		String moduleName = LfwCommonTool.getProjectModuleName(LFWPersTool.getCurrentProject());
		String moduleId = null;
		CpModuleVO[] modules =  LFWWfmConnector.getModules();
		for(CpModuleVO module:modules){
			if(moduleName.equals(module.getDevmodulecode())){
				moduleId = module.getId();
				break;
			}
		}
		if(moduleId ==null){
			MessageDialog.openError(new Shell(Display.getCurrent()), "错误", "库中没有此模块信息，请在cp_module表中注册");
			return;
		}
		if(funcNodes!=null) {
			for(FuncNodeObj funcobj:funcNodes){
//				if(funcobj.getBcp()!=null&&bcpPath.endsWith(funcobj.getBcp())){
//					titleList.add(funcobj.getTitle());
//					funcidList.add(funcobj.getId());					
//				}
				if(funcobj.getModuleId().equals(moduleId)){
					titleList.add(funcobj.getTitle());
					funcidList.add(funcobj.getId());		
				}
				
			}
			
			funcList.setItems(titleList.toArray(new String[0]));
		}
//		funcList.addSelectionListener(new SelectionListener(){
//
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
		setControl(composite);
		
	}

	public FuncNodeObj[] getFuncNodes() {
		return funcNodes;
	}

	public void setFuncNodes(FuncNodeObj[] funcNodes) {
		this.funcNodes = funcNodes;
	}
	
	public String getSelectedFuncId(){
		int index = funcList.getSelectionIndex();
		if(index==-1) return null;
		return funcidList.get(index);
	}
	public String getApplicationId(){
		if(appIdList==null) 
			return null;
		else{
			int index = appIdList.getSelectionIndex();
			if(index==-1) 
				return null;
			return appFile.get(index);
		}
	}

//	public List<FuncNodeObj> getFuncNodeList() {
//		return funcNodeList;
//	}
//
//	public void setFuncNodeList(List<FuncNodeObj> funcNodeList) {
//		this.funcNodeList = funcNodeList;
//	}

	


}
