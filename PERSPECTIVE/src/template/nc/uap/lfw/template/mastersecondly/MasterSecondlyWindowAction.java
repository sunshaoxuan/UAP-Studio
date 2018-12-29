package nc.uap.lfw.template.mastersecondly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.aciton.NCConnector;
import nc.uap.lfw.application.CreateWidgetByTemp;
import nc.uap.lfw.application.ManualAppNodeAction;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.data.RefMdDataset;
import nc.uap.lfw.core.datamodel.MdClassVO;
import nc.uap.lfw.core.datamodel.MdComponnetVO;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.ViewRule;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIGridComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanel;
import nc.uap.lfw.jsp.uimeta.UIPanelPanel;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import nc.uap.lfw.lang.M_template;
import nc.uap.lfw.md.LfwWfmBizItf;
import nc.uap.lfw.template.NewTempleteWindowWizard;
import nc.uap.lfw.window.template.MenubarTemplateProvider;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 主子表模板创建action
 * @author guomq1
 * 2012-8-6
 */
public class MasterSecondlyWindowAction {
	private static final String TAB_TABLAYOUT = "tabLayout"; //$NON-NLS-1$
	private static final String LAYOUT_FLOWVPANELTAB = "flowvpaneltab"; //$NON-NLS-1$
	private static final String LAYOUT_FLOWVLAYOUT = "flowvLayout"; //$NON-NLS-1$
	private static final String MAIN_VIEW = "main"; //$NON-NLS-1$
	private static final String TEMPLATE_CTRL_BASE = "templates/multiTableQuery/controller"; //$NON-NLS-1$
	private static final String TEMPLATE_BASE = "templates/multiTableQuery/web/html/nodes"; //$NON-NLS-1$
	private static final String MASTER_SECONDLY_FACTORY = "nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory"; //$NON-NLS-1$
	private static final String MASTER_SECONDLYFLOW_FACTORY = "nc.uap.lfw.template.mastersecondlyflow.MasterSecondlyFlowFactory"; //$NON-NLS-1$
	public static final String DATA_WFM_PACKAGE = "wfmPackage"; //$NON-NLS-1$
	public static final String DATA_POP_WIN_VIEW_CTR_PACKAGE = "editWinViewCtrPackage"; //$NON-NLS-1$
	public static final String DATA_POP_WIN_CTR_PACKAGE = "editWinCtrPackage"; //$NON-NLS-1$
	public static final String DATA_LIST_WIN_VIEW_CTR_PACKAGE = "mainWinViewCtrPackage"; //$NON-NLS-1$
	public static final String DATA_LIST_WIN_CTR_PACKAGE = "mainWinCtrPackage"; //$NON-NLS-1$
	public static final String DATA_POP_WIN_VIEW_CTR_NAME = "cardWinViewCtrName"; //$NON-NLS-1$
	public static final String DATA_POP_WIN_CTR_NAME = "cardWinCtrName"; //$NON-NLS-1$
	public static final String DATA_LIST_WIN_VIEW_CTR_NAME = "listWinViewCtrName"; //$NON-NLS-1$
	public static final String DATA_LIST_WIN_CTR_NAME = "listWinCtrName"; //$NON-NLS-1$
	public static final String DATA_WIN_ID = "winListId"; //$NON-NLS-1$
	public static final String DATA_POP_ID = "winPopId"; //$NON-NLS-1$
	public static final String DATA_WIN_CAPTION = "winCaption"; //$NON-NLS-1$
	public static final String DATA_GRID_CAPTION = "grid_caption"; //$NON-NLS-1$
	public static final String DATA_CONTROLLER_PREFIX = "controllerPrefix"; //$NON-NLS-1$
	public static final String DATA_LOCATION = "location"; //$NON-NLS-1$
	public static final String DATA_POP_WIN_CONTROLLER = "EditWinController"; //$NON-NLS-1$
	public static final String DATA_LIST_WIN_CONTROLLER = "winController"; //$NON-NLS-1$
	public static final String DATA_POP_VIEW_CONTROLLER = "EditController"; //$NON-NLS-1$
	public static final String DATA_LIST_VIEW_CONTROLL_CLAZZ = "main_controllClazz"; //$NON-NLS-1$
	public static final String DATA_VO_CLASS_NAME = "VOClassName"; //$NON-NLS-1$
	public static final String DATA_MD_VO_META = "md_voMeta"; //$NON-NLS-1$
	public static final String DATA_MD_OBJ_META = "md_objMeta"; //$NON-NLS-1$
	public static final String DATA_MASTER_DS_ID = "masterDsId"; //$NON-NLS-1$
	public static final String DATA_PKORG = "pk_orgAttr"; //$NON-NLS-1$
	public static final String DATA_APPID = "appid"; //$NON-NLS-1$
	private static final String DATA_LIST_WIN_ID = "replaceListWinId"; //$NON-NLS-1$
	private static final String DATA_LIST_WIN_NAME = "replaceListWinName"; //$NON-NLS-1$
	private static final String DATA_POP_WIN_ID = "replacePopWinId"; //$NON-NLS-1$
	private static final String DATA_POP_WIN_NAME = "replacePopWinName"; //$NON-NLS-1$
	//模板向导
	private NewTempleteWindowWizard wizard = null;
	//当前模板类型 
	private String templateType;
	//当前工程路径
	private String projectPath = null;
	//当前app
	private Application app = null;
	//元数据选择页
	private DoubleDsSelectPage doubleDsPage;
	//window配置页面
	private TempWinConfigPage tempWinConfigPage;
	//主数据集
	private MdDataset masterDs = null;
	//子数据集List
	private List<MdDataset> subDatasetList = new ArrayList<MdDataset>();
	//
	private ViewModels viewModels = null;
	
	private CreateWidgetByTemp createAction = null;
	
	private Map<String,String> entity = null;
	
	//准备模板替换需要的参数
	protected HashMap<String, String> data = new HashMap<String, String>();
	
	public MasterSecondlyWindowAction(NewTempleteWindowWizard wizard){
		this.wizard = wizard;
		this.projectPath = LFWAMCPersTool.getProjectPath();
		this.app = LFWAMCPersTool.getCurrentApplication();
	}
	
	protected void config(){
		//设置模板类型
		setTemplateType(MASTER_SECONDLY_FACTORY);
		this.doubleDsPage = (DoubleDsSelectPage)wizard.getPage(MainPlugin.getResourceString(WEBPersConstants.KEY_TEMP_THDDPAGE_DESC));
		this.tempWinConfigPage = (TempWinConfigPage)wizard.getPage(WEBPersConstants.KEY_TEMP_FORTHPAGE_DESC);
		this.createAction = new CreateWidgetByTemp("MasterSecondlyQuery"); //$NON-NLS-1$
	}
	
	public boolean run(){
		try {
			//读取模板页面及配置
			config();
			
			//准备所有数据集
			if (genDataSets() == false)
				return false;
			
			if (genParamData(data) == false)
				return false;
			return doGen();
		} 
		catch (Exception e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "ERROR", M_template.MasterSecondlyWindowAction_0 + e.getMessage()); //$NON-NLS-1$
			MainPlugin.getDefault().logError(e);
		}
		return false;
	}

	protected boolean doGen() throws Exception {
		
		TempWinConfigPage tempWin = getTempWinConfigPage();
		//拷贝模板前台文件
		List[] listArray = new List[12];
		listArray[0] = getCreateAction().createFromTemp(getTemplateBase() + "/" + TempWinConfigPage.LISTWIN + "/main/", tempWin.getListWinPath() +"/main/", "widget.wd", data); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		listArray[1] = getCreateAction().createFromTemp(getTemplateBase() + "/" + TempWinConfigPage.LISTWIN + "/main/", tempWin.getListWinPath() + "/main/", "uimeta.um", data); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		listArray[2] = getCreateAction().createFromTemp(getTemplateBase() + "/" + TempWinConfigPage.POPWIN + "/main/", tempWin.getPopWinPath() + "/main/", "widget.wd", data); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		listArray[3] = getCreateAction().createFromTemp(getTemplateBase() + "/" + TempWinConfigPage.POPWIN + "/main/", tempWin.getPopWinPath() + "/main/", "uimeta.um", data); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		listArray[4] = getCreateAction().createFromTemp(getTemplateBase() + "/" + TempWinConfigPage.LISTWIN + "/", tempWin.getListWinPath() + "/", "pagemeta.pm", data); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		listArray[5] = getCreateAction().createFromTemp(getTemplateBase() + "/" + TempWinConfigPage.LISTWIN + "/", tempWin.getListWinPath() + "/", "uimeta.um", data); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		getCreateAction().createFromTemp(getTemplateBase() + "/" + TempWinConfigPage.LISTWIN + "/", tempWin.getListWinPath() + "/", "include.js", data);
		listArray[6] = getCreateAction().createFromTemp(getTemplateBase() + "/" + TempWinConfigPage.POPWIN + "/", tempWin.getPopWinPath() + "/", "pagemeta.pm", data); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		listArray[7] = getCreateAction().createFromTemp(getTemplateBase() + "/" + TempWinConfigPage.POPWIN + "/", tempWin.getPopWinPath() + "/", "uimeta.um", data); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		
		int index = projectPath.lastIndexOf("/"); //$NON-NLS-1$
		String iprojectPath = projectPath.substring(0, index);
		String prePath = iprojectPath + "/" + data.get(DATA_LOCATION); //$NON-NLS-1$
		//拷贝模板控制文件
		listArray[8] = getCreateAction().createFromTemp(getTemplateCtrlBase() + "/" + TempWinConfigPage.LISTWIN + "/", prePath + "/" + tempWin.getListWinCtrlPack() + "/", "ListWinController.java.template", data); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		listArray[9] = getCreateAction().createFromTemp(getTemplateCtrlBase() + "/" + TempWinConfigPage.LISTWIN + "/", prePath + "/" + tempWin.getListViewCtrlPack() + "/", "ListWinMainViewController.java.template", data); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		listArray[10] = getCreateAction().createFromTemp(getTemplateCtrlBase() + "/" + TempWinConfigPage.POPWIN + "/", prePath + "/" + tempWin.getPopWinCtrlPack() + "/", "CardWinController.java.template", data); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		listArray[11] = getCreateAction().createFromTemp(getTemplateCtrlBase() + "/" + TempWinConfigPage.POPWIN + "/", prePath + "/" + tempWin.getPopViewCtrlPack() + "/", "CardWinMainViewController.java.template", data); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		
		getCreateAction().saveFile(listArray);

		IProject project = LFWPersTool.getCurrentProject();
		try {
			project.refreshLocal(2, null);
		} 
		catch (CoreException e1) {
			MainPlugin.getDefault().logError(e1);
		}

		waitForReady();
		
		//填充模板，生成需要的页面
		if (genListWindow() == false)
			return false;
		if (genEditWindow() == false)
			return false;
		if (genApplication() == false)
			return false;
		//生成查询模板信息
		if (genQueryPrintTemp() == false)
			return false;
//		MessageDialog.openInformation(Display.getDefault().getActiveShell(), "成功", "模式化已完成");
		if(MessageDialog.openConfirm(Display.getDefault().getActiveShell(), M_template.MasterSecondlyWindowAction_1, M_template.MasterSecondlyWindowAction_2)){
			ManualAppNodeAction manualAppNodeAction = new ManualAppNodeAction();
			manualAppNodeAction.run();
		}
		return true;
	}

	private void waitForReady() throws Exception {
		int count = 0;
		do{
			try {
				Thread.sleep(2000);
				String id = null;
				String compId = getTempWinConfigPage().getComponentId();
				String windowId = getTempWinConfigPage().getListWindowId();
				if(compId.equals(LfwUIComponent.ANNOYUICOMPONENT)||compId.equals("")){ //$NON-NLS-1$
					id = windowId;
				}else{
					id = compId + "." + windowId; //$NON-NLS-1$
				}
				LfwWindow listPageMeta = LFWConnector.getPageMeta(id);
				
				LfwView listView= null;
				if(listPageMeta != null){
					listView = listPageMeta.getView(MAIN_VIEW);
				}
				
				String popWinId = getTempWinConfigPage().getPopWindowId();
				if(compId.equals(LfwUIComponent.ANNOYUICOMPONENT)||compId.equals("")){ //$NON-NLS-1$
					id = popWinId;
				}else{
					id = compId + "." + popWinId; //$NON-NLS-1$
				}
				LfwWindow popPageMeta = LFWConnector.getPageMeta(id);
				LfwView popView= null;
				if(popPageMeta != null){
					popView = popPageMeta.getView(MAIN_VIEW);
				}
				
				if(listPageMeta != null&&listView != null&&popPageMeta != null&&popView != null)
					return;
			} 
			catch (InterruptedException e) {
				throw e;
			}
		}
		while(count ++ < 10);
		throw new RuntimeException(M_template.MasterSecondlyWindowAction_3);
	}
	
	protected String getTemplateBase() {
		return TEMPLATE_BASE;
	}

	protected String getTemplateCtrlBase() {
		return TEMPLATE_CTRL_BASE;
	}

	/**
	 * 准备所有数据集
	 * 
	 * @return
	 */
	protected boolean genDataSets(){
		MdComponnetVO componnetVO = getDoubleDsPage().getComponentVo();
		if(componnetVO == null){
			Shell shell = new Shell(Display.getCurrent());
			MessageDialog.openError(shell, M_template.MasterSecondlyWindowAction_4, M_template.MasterSecondlyWindowAction_5);
			return false;
		}
		
//		String aggVoName =  NCConnector.getAggVOByComponent(componnetVO.getName());
//		if(aggVoName==null){
//			Shell shell = new Shell(Display.getCurrent());
//			MessageDialog.openError(shell, "错误", "所选元数据不是主子表,请重新选择");
//			return false;
//		}
		
		LfwView widget = new LfwView();
		MainPlugin.getDefault().logInfo(M_template.MasterSecondlyWindowAction_6 + componnetVO.getId());
		widget = NCConnector.getMdDsFromComponent(widget, componnetVO.getId());
		Dataset[] datasets = widget.getViewModels().getDatasets();
		DatasetRelations relations = widget.getViewModels().getDsrelations();
		this.viewModels = widget.getViewModels();
		String masterDsId = null;
		if(relations != null){
			masterDsId = relations.getMasterDatasetIds()[0];
		}
		else{
			masterDsId = ""; //$NON-NLS-1$
		}
		for(int i = 0; i < datasets.length; i++){
			Dataset dataset = datasets[i];
			if(dataset instanceof RefMdDataset)
				continue;
			if(!masterDsId.equals("")){ //$NON-NLS-1$
				if(dataset.getId().equals(masterDsId))
					masterDs = (MdDataset)dataset;
				else
					subDatasetList.add((MdDataset)dataset);
			}
			else{
				masterDs = (MdDataset)dataset;
				break;
			}
		}
		return true;	
	}
	
	/**
	 * 准备模板替换需要的参数
	 */
	protected boolean genParamData(Map<String, String> dataParam){
		dataParam.put(DATA_APPID, getApp().getId());
		MdComponnetVO componnetVO = getDoubleDsPage().getComponentVo();
		
		if(componnetVO == null){
			Shell shell = new Shell(Display.getCurrent());
			MessageDialog.openError(shell, M_template.MasterSecondlyWindowAction_4, M_template.MasterSecondlyWindowAction_7);
			return false;
		}
	
//		String aggVoName =  NCConnector.getAggVOByComponent(componnetVO.getName());		
//		dataParam.put(DATA_AGGVO, aggVoName);

		Map<String,String> IBDMap = NCConnector.getIBDObjMap(componnetVO.getId());
		if(IBDMap!=null){
			if(IBDMap.get("pk_org")==null) //$NON-NLS-1$
				dataParam.put(DATA_PKORG, "pk_org"); //$NON-NLS-1$
			else
				dataParam.put(DATA_PKORG, IBDMap.get("pk_org"));			 //$NON-NLS-1$
		}
		else dataParam.put(DATA_PKORG, "pk_org");			 //$NON-NLS-1$
		
		String voMeta = null;
		String objMeta = null;
		String voClassName = null;
		
		voMeta = masterDs.getVoMeta();
		objMeta = masterDs.getObjMeta();
		
		if(voMeta != null){
			voClassName = voMeta.substring(voMeta.lastIndexOf(".")+1, voMeta.length());	 //$NON-NLS-1$
		}
		else if(objMeta != null){
//			voClassName = objMeta.substring(objMeta.lastIndexOf(".")+1);
		}
		
		dataParam.put(DATA_MASTER_DS_ID, masterDs.getId());
		dataParam.put(DATA_MD_OBJ_META, masterDs.getObjMeta());
		dataParam.put(DATA_MD_VO_META, voMeta);
		dataParam.put(DATA_VO_CLASS_NAME, voClassName);

//		TempWinConfigPage tempWinConfigPage = (TempWinConfigPage)wizard.getPage(WEBPersConstants.KEY_TEMP_FORTHPAGE_DESC);
		TempWinConfigPage tempWin = getTempWinConfigPage();
		String windowId = tempWin.getListWindowId();
		String popwinId = tempWin.getPopWindowId();
		String windowCaption = tempWin.getListWindowCaption();
		String windowLocation = tempWin.getLocation();
		String windowController = tempWin.getController();
		String compId = tempWin.getComponentId();
		
		dataParam.put(DATA_LIST_VIEW_CONTROLL_CLAZZ, tempWin.getListViewCtrlClazz());
		dataParam.put(DATA_POP_VIEW_CONTROLLER, tempWin.getPopViewCtrlClazz());
		dataParam.put(DATA_LIST_WIN_CONTROLLER, tempWin.getListWinCtrlClazz());
		dataParam.put(DATA_POP_WIN_CONTROLLER, tempWin.getPopWinCtrlClazz());
		
		dataParam.put(DATA_LIST_WIN_CTR_PACKAGE, tempWin.getListWinCtrlPack().replaceAll("/", ".")); //$NON-NLS-1$ //$NON-NLS-2$
		dataParam.put(DATA_POP_WIN_CTR_PACKAGE, tempWin.getPopWinCtrlPack().replaceAll("/", ".")); //$NON-NLS-1$ //$NON-NLS-2$
		dataParam.put(DATA_LIST_WIN_VIEW_CTR_PACKAGE, tempWin.getListViewCtrlPack().replaceAll("/", ".")); //$NON-NLS-1$ //$NON-NLS-2$
		dataParam.put(DATA_POP_WIN_VIEW_CTR_PACKAGE, tempWin.getPopViewCtrlPack().replaceAll("/", ".")); //$NON-NLS-1$ //$NON-NLS-2$

		dataParam.put(DATA_LIST_WIN_CTR_NAME, tempWin.getListWinCtrlName());
		dataParam.put(DATA_POP_WIN_CTR_NAME, tempWin.getPopWinCtrlName());
		dataParam.put(DATA_LIST_WIN_VIEW_CTR_NAME, tempWin.getListViewCtrlName());
		dataParam.put(DATA_POP_WIN_VIEW_CTR_NAME, tempWin.getPopViewCtrlName());
		
		dataParam.put(DATA_LOCATION, windowLocation);
		dataParam.put(DATA_CONTROLLER_PREFIX, windowController.replace(".", "/")); //$NON-NLS-1$ //$NON-NLS-2$
		dataParam.put(DATA_GRID_CAPTION, windowCaption);
		dataParam.put(DATA_WIN_CAPTION, windowCaption);
		dataParam.put(DATA_WIN_ID, windowId);
		dataParam.put(DATA_POP_ID, popwinId);
		if(compId.equals(LfwUIComponent.ANNOYUICOMPONENT)||compId.equals("")){ //$NON-NLS-1$
			dataParam.put(DATA_LIST_WIN_ID, windowId);
			dataParam.put(DATA_POP_WIN_ID, popwinId);
		}else{
			dataParam.put(DATA_LIST_WIN_ID, tempWin.getComponentId() + "." + windowId); //$NON-NLS-1$
			dataParam.put(DATA_POP_WIN_ID, tempWin.getComponentId() + "." + popwinId); //$NON-NLS-1$
		}		
		dataParam.put(DATA_LIST_WIN_NAME, tempWin.getListWindowCaption());		
		dataParam.put(DATA_POP_WIN_NAME, tempWin.getPopWindowCaption());
		dataParam.put(DATA_WFM_PACKAGE, windowController + ".wfm"); //$NON-NLS-1$
		
//		String mataClass = null;
		String businessEntityFullName = null;
		List<MdClassVO> classVos = NCConnector.getAllClassByComId(componnetVO.getId());

		for (MdClassVO classVo : classVos){
			if (classVo.getFullclassname().equals(masterDs.getVoMeta())){
//				mataClass = classVo.getId();
				businessEntityFullName = componnetVO.getNamespace() + "." + classVo.getName(); //$NON-NLS-1$
				break;
			}
		}
		entity = NCConnector.getBusinessEntity(businessEntityFullName);
		if(entity != null){
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_APPROVEDATE, entity.get(LfwWfmBizItf.ATTRIBUTE_APPROVEDATE));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_BILLMAKEDATE, entity.get(LfwWfmBizItf.ATTRIBUTE_BILLMAKEDATE));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_BILLMAKER, entity.get(LfwWfmBizItf.ATTRIBUTE_BILLMAKER));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_BILLNO, entity.get(LfwWfmBizItf.ATTRIBUTE_BILLNO));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_ORG, entity.get(LfwWfmBizItf.ATTRIBUTE_ORG));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_APPROVER, entity.get(LfwWfmBizItf.ATTRIBUTE_APPROVER));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_FORMSTATE, entity.get(LfwWfmBizItf.ATTRIBUTE_FORMSTATE));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_FORMTITLE, entity.get(LfwWfmBizItf.ATTRIBUTE_FORMTITLE));
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_FORMINSPK, entity.get(LfwWfmBizItf.ATTRIBUTE_FORMINSPK));
		}
		else{
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_APPROVEDATE, ""); //$NON-NLS-1$
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_BILLMAKEDATE, ""); //$NON-NLS-1$
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_BILLMAKER, ""); //$NON-NLS-1$
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_BILLNO, ""); //$NON-NLS-1$
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_ORG, ""); //$NON-NLS-1$
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_APPROVER, ""); //$NON-NLS-1$
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_FORMSTATE, ""); //$NON-NLS-1$
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_FORMTITLE, ""); //$NON-NLS-1$
			dataParam.put(LfwWfmBizItf.ATTRIBUTE_FORMINSPK, ""); //$NON-NLS-1$
		}
		
		if (genOtherParamData(dataParam) == false)
			return false;
		
		return true;
	}
	
	
	protected boolean genOtherParamData(Map<String, String> dataParam) {
		return true;
	}

	/**
	 * 生成列表页面
	 * 
	 * @param createAction
	 * @return
	 */
	protected boolean genListWindow(){
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put(WebConstant.PROJECT_PATH_KEY, projectPath);
		String compId = getTempWinConfigPage().getComponentId();
		String windowId = null;
		if(compId == null || "".equals(compId) || LfwUIComponent.ANNOYUICOMPONENT.equals(compId)) //$NON-NLS-1$
			windowId = getTempWinConfigPage().getListWindowId();
		else 
			windowId = compId + "." + getTempWinConfigPage().getListWindowId(); //$NON-NLS-1$
		LfwWindow listPageMeta = null;
		try {
			listPageMeta = LFWConnector.getPageMeta(windowId);
		} 
		catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
			MessageDialog.openError(null, M_template.MasterSecondlyWindowAction_8, e.getMessage());
			return false;
		}
		
		String masterDsId = masterDs.getId();
		//处理widget
		LfwView mainWidget = listPageMeta.getWidget(MAIN_VIEW);
		if(viewModels != null){
			Dataset ds = mainWidget.getViewModels().getDataset(masterDsId);
//			Field field = new Field();
//			field.setId("operate");
//			field.setText("操作");
//			field.setDataType(StringDataTypeConst.STRING);
//			viewModels.getDataset(masterDsId).getFieldSet().getFieldList().add(0, field);
			viewModels.getDataset(masterDsId).setEventConfList(ds.getEventConfList());
			viewModels.getDataset(masterDsId).setLazyLoad(true);
			viewModels.getDataset(masterDsId).setPageSize(15);
			viewModels.getDataset(masterDsId).setControlwidgetopeStatus(true);
			mainWidget.setViewModels(viewModels);
		}
		
		if(getTemplateType().equals(MASTER_SECONDLY_FACTORY)){
			mainWidget.getViewMenus().addMenuBar(MenubarTemplateProvider.createListMenubar());
		}
		else if(getTemplateType().equals(MASTER_SECONDLYFLOW_FACTORY)){
			mainWidget.getViewMenus().addMenuBar(MenubarTemplateProvider.createWfListMenubar());
		}

		
		GridComp grid = (GridComp)mainWidget.getViewComponents().getComponent(masterDsId + "_grid"); //$NON-NLS-1$
		grid.setWidget(mainWidget);
		Dataset mds = mainWidget.getViewModels().getDataset(masterDsId);
		getCreateAction().fillWithData(grid, mds);
		
//		GridColumn operateColumn = (GridColumn)grid.getColumnById("operate");
//		operateColumn.setRenderType("OperateRender");

		UIMeta listMeta = LFWAMCConnector.getUImetaById(windowId);
		
		//获取UIFlowvLayout
		UIMeta widgetUIMeta = UIElementFinder.findUIMeta(listMeta, MAIN_VIEW);
		UIFlowvLayout fl = (UIFlowvLayout) widgetUIMeta.findChildById(LAYOUT_FLOWVLAYOUT);
		try{
			ViewRule rule = new ViewRule();
			rule.setId(MAIN_VIEW);
			for(MdDataset subDs : subDatasetList){
				DatasetRule subDsRule = new DatasetRule();
				subDsRule.setId(subDs.getId());
				subDsRule.setType(DatasetRule.TYPE_ALL_LINE);
				rule.addDsRule(subDsRule);
			}	
			EventConf event = mainWidget.getViewMenus().getMenuBar("menubar").getItem("del").getEventConf("onclick", "onDelete");
			if(event.getSubmitRule()==null){
				event.setSubmitRule(new EventSubmitRule());
			}
			event.getSubmitRule().addViewRule(rule); 
		}
		catch(Exception e){
			MainPlugin.getDefault().logError(e.getMessage());
		}
		if(!subDatasetList.isEmpty()){
			//增加一个flowvpanel
			UIFlowvPanel flowvPanelTab = new UIFlowvPanel();
			flowvPanelTab.setId(LAYOUT_FLOWVPANELTAB);
			flowvPanelTab.setWidgetId(mainWidget.getId());
			fl.addPanel(flowvPanelTab);
			//flowvpanel里增加一个tab布局
			UITabComp tabLayout = new UITabComp();
			tabLayout.setId(TAB_TABLAYOUT);
			tabLayout.setWidgetId(mainWidget.getId());
			flowvPanelTab.setElement(tabLayout);
			
			for(MdDataset mdds: subDatasetList){
				Dataset ds = (Dataset)mdds;
				ds.setLazyLoad(true);
				ds.setPageSize(15);
				
				mainWidget.getViewModels().addDataset(ds);
				GridComp component = new GridComp();
				component.setId(ds.getId() + "_grid"); //$NON-NLS-1$
				component.setDataset(ds.getId());
				mainWidget.getViewComponents().addComponent(component);
				component.setWidget(mainWidget); 
				getCreateAction().fillWithData(component, ds);
				
				//新建tabItem
				UITabItem tabItem = new UITabItem();
				tabItem.setId("tabitem_" + ds.getId()); //$NON-NLS-1$
				tabItem.setText(ds.getCaption());
				tabLayout.addPanel(tabItem);
				
				UIGridComp uiGrid = new UIGridComp();
				uiGrid.setId(ds.getId() + "_grid"); //$NON-NLS-1$
				tabItem.setElement(uiGrid);
			}
		}
		if (genOtherListWindow(mainWidget,listMeta) == false)
			return false;
		String winFolderPath = windowId.replaceAll("\\.", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		NCConnector.saveWidgettoXml(projectPath + "/web/html/nodes/" + winFolderPath + "/main/","widget.wd", projectPath, mainWidget, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NCConnector.saveUIMeta(widgetUIMeta, projectPath + "/web/html/nodes/" + winFolderPath + "/", projectPath + "/web/html/nodes/" + winFolderPath + "/main/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		
		app.setDefaultWindowId(windowId);
		if(listPageMeta !=null)
			listPageMeta.setId(windowId);
			app.addWindow(listPageMeta.createWindowConfig());
		
		String ctxPath = LfwCommonTool.getLfwProjectCtx(LFWAMCPersTool.getCurrentProject());
		if(!ctxPath.startsWith("/")){ //$NON-NLS-1$
			ctxPath = "/" + ctxPath; //$NON-NLS-1$
		}
		return true;
	}
	
	protected boolean genOtherListWindow(LfwView mainWidget, UIMeta listMeta) {
		return true;
	}

	protected boolean genEditWindow(){
		String compId = getTempWinConfigPage().getComponentId();
		String windowId = null;
		if(compId == null || "".equals(compId) || LfwUIComponent.ANNOYUICOMPONENT.equals(compId)) //$NON-NLS-1$
			windowId = getTempWinConfigPage().getPopWindowId();
		else 
			windowId = compId + "." + getTempWinConfigPage().getPopWindowId(); //$NON-NLS-1$
		
		LfwWindow editPageMeta = null;
		try {
			editPageMeta = LFWConnector.getPageMeta(windowId);
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
			MessageDialog.openError(null, M_template.MasterSecondlyWindowAction_8, e.getMessage());
			return false;
		}
		
		//处理widget
		LfwView editWidget = editPageMeta.getWidget(MAIN_VIEW);
		if  (viewModels != null){
			Dataset ds = editWidget.getViewModels().getDataset(masterDs.getId());
			masterDs.getFieldSet().getFieldList().remove(0);
			masterDs.setEventConfList(ds.getEventConfList());
			editWidget.setViewModels(viewModels);
		}
		if(getTemplateType().equals(MASTER_SECONDLY_FACTORY)){
			editWidget.getViewMenus().addMenuBar(MenubarTemplateProvider.createCardMenubar());
		}
		else if(getTemplateType().equals(MASTER_SECONDLYFLOW_FACTORY)){
			editWidget.getViewMenus().addMenuBar(MenubarTemplateProvider.createWfCardMenubar());
		}
		
		MdComponnetVO componnetVO = getDoubleDsPage().getComponentVo();
		Map<String,String> IBDMap = NCConnector.getIBDObjMap(componnetVO.getId());
		
		WebComponent formComp = editWidget.getViewComponents().getComponent(masterDs.getId() + "_form"); //$NON-NLS-1$
		formComp.setWidget(editWidget);
		getCreateAction().setEntity(entity);
		getCreateAction().fillWithData(formComp, masterDs,IBDMap);
		try{
			ViewRule rule = new ViewRule();
			rule.setId(MAIN_VIEW);
			DatasetRule dsRule = new DatasetRule();
			dsRule.setId(masterDs.getId());
			dsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
			rule.addDsRule(dsRule);
			for(MdDataset subDs : subDatasetList){
				DatasetRule subDsRule = new DatasetRule();
				subDsRule.setId(subDs.getId());
				subDsRule.setType(DatasetRule.TYPE_ALL_LINE);
				rule.addDsRule(subDsRule);
			}
			EventConf event = editWidget.getViewMenus().getMenuBar("menubar").getItem("save").getEventConf("onclick", "onSave");
			if(event.getSubmitRule()==null){
				event.setSubmitRule(new EventSubmitRule());
			}
			event.getSubmitRule().addViewRule(rule); 
		}
		catch(Exception e){
			MainPlugin.getDefault().logError(e.getMessage());
		}
		//处理uimeta
		editPageMeta.setFoldPath("html/nodes/"+windowId+"_edit"+"/main"); 
		UIMeta editMeta = LFWAMCConnector.getUImetaById(windowId);
		
		//获取UIFlowvLayout
		UIMeta widgetUIMeta = UIElementFinder.findUIMeta(editMeta, MAIN_VIEW);
		UIFlowvLayout fl = (UIFlowvLayout) (UIFlowvLayout) widgetUIMeta.findChildById("mainFlowvLayout"); //$NON-NLS-1$
		if(!subDatasetList.isEmpty()){
			for(MdDataset mdds: subDatasetList){
				Dataset ds = (Dataset)mdds;
				ds.setLazyLoad(true);
				
				editWidget.getViewModels().addDataset(ds);
				GridComp component = new GridComp();
				component.setId(ds.getId()+"_grid"); //$NON-NLS-1$
				component.setShowImageBtn(true);
				component.setDataset(ds.getId());
				editWidget.getViewComponents().addComponent(component);
				component.setWidget(editWidget); 
				getCreateAction().fillWithData(component, ds);
				
				//新建UIFlowvPanel
				UIFlowvPanel flowvPanel2 = new UIFlowvPanel();
				flowvPanel2.setId(ds.getId() + "_flowvpane2"); //$NON-NLS-1$
				flowvPanel2.setLeftPadding("20"); //$NON-NLS-1$
				flowvPanel2.setRightPadding("20"); //$NON-NLS-1$
				
				//新建UIPanel
				UIPanel panel = new UIPanel();
				panel.setId(ds.getId()+"_panel"); //$NON-NLS-1$
				panel.setTitle(ds.getCaption());
				panel.setClassName("small_panel_div"); //$NON-NLS-1$
				
				//新建UIPanelPanel
				UIPanelPanel panelPanel = new UIPanelPanel();
				panelPanel.setId(ds.getId()+"_panelPanel"); //$NON-NLS-1$

				//新建UIGridComp
				UIGridComp grid = new UIGridComp();
				grid.setId(ds.getId() + "_grid"); //$NON-NLS-1$
				
				fl.addPanel(flowvPanel2);
				flowvPanel2.setElement(panel);
				panel.addPanel(panelPanel);
				panelPanel.setElement(grid);
			}
		}
		if (genOtherEditWindow(editWidget,editMeta) == false)
			return false;
		String winFolderPath = windowId.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		NCConnector.saveWidgettoXml(projectPath + "/web/html/nodes/" + winFolderPath + "/main/","widget.wd", projectPath, editWidget, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		NCConnector.saveUIMeta(widgetUIMeta, projectPath + "/web/html/nodes/" + winFolderPath+"/", projectPath + "/web/html/nodes/" + winFolderPath + "/main/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		
		String ctxPath = LfwCommonTool.getLfwProjectCtx(LFWAMCPersTool.getCurrentProject());
		if(!ctxPath.startsWith("/")){ //$NON-NLS-1$
			ctxPath = "/" + ctxPath; //$NON-NLS-1$
		}
		if(editPageMeta !=null)
			editPageMeta.setId(windowId);
			app.addWindow(editPageMeta.createWindowConfig());
		
		return true;
	}
	
	protected boolean genOtherEditWindow(LfwView editWidget, UIMeta editMeta) {
		return true;
	}

	protected boolean genApplication(){
		ExtAttribute attr = new ExtAttribute();
		attr.setKey("templateType"); //$NON-NLS-1$
		attr.setValue(getTemplateType());
		app.addAttribute(attr);
		for(WindowConfig win:app.getWindowList()){
			win.setIsCanFreeDesign(true);
		}
		Connector conn = new Connector();
		conn.setConnType(Connector.WINDOW_WINDOW);
		conn.setId(app.getId()+"conn"); //$NON-NLS-1$
		conn.setSource(data.get(DATA_POP_WIN_ID));
		conn.setPlugoutId("proxyAfterSavePlugout"); //$NON-NLS-1$
		conn.setTarget(data.get(DATA_LIST_WIN_ID));
		conn.setPluginId("refreshProxy"); //$NON-NLS-1$
		app.addConnector(conn);
		LFWSaveElementTool.saveApplication(app);
		String ctxPath = LfwCommonTool.getLfwProjectCtx(LFWAMCPersTool.getCurrentProject());
		if(!ctxPath.startsWith("/")){ //$NON-NLS-1$
			ctxPath = "/" + ctxPath; //$NON-NLS-1$
		}
		return true;
	}

	/**
	 * 生成查询和打印模板
	 * @return
	 */
	protected boolean genQueryPrintTemp(){
		MainPlugin.getDefault().logInfo(M_template.MasterSecondlyWindowAction_9);
		MdComponnetVO componnetVO = getDoubleDsPage().getComponentVo();
		String windowId = getTempWinConfigPage().getListWindowId();
		String windowCaption = getTempWinConfigPage().getListWindowCaption();
		
		if(componnetVO == null){
			Shell shell = new Shell(Display.getCurrent());
			MessageDialog.openError(shell, M_template.MasterSecondlyWindowAction_4, M_template.MasterSecondlyWindowAction_10);
			return false;
		}
		String modelCode = windowId;
		String modelName = windowCaption;
		String mataClass = null;
		String businessEntityFullName = null;
		List<MdClassVO> classVos = NCConnector.getAllClassByComId(componnetVO.getId());
		for (MdClassVO classVo : classVos){
			if (classVo.getFullclassname().equals(masterDs.getVoMeta())){
				mataClass = classVo.getId();
				businessEntityFullName = componnetVO.getNamespace() + "." + classVo.getName(); //$NON-NLS-1$
				break;
			}
		}
//		LfwTemplateTool.GenQryTemplate(app.getId(), modelCode, modelName, mataClass, businessEntityFullName);
		String nodecode = "$tempcode_" + app.getId();	 //$NON-NLS-1$
		MainPlugin.getDefault().logInfo(M_template.MasterSecondlyWindowAction_11+nodecode);
		LFWWfmConnector.genQryTemplate(nodecode, mataClass, modelCode, modelName, businessEntityFullName);
		LFWWfmConnector.genPrintTemplate(nodecode, mataClass, modelCode, modelName, businessEntityFullName);
		return true;
	}
	

	protected Application getApp() {
		return app;
	}

	protected String getProjectPath() {
		return projectPath;
	}

	protected ViewModels getViewModels() {
		return viewModels;
	}

	protected List<MdDataset> getSubDatasetList() {
		return subDatasetList;
	}

	protected MdDataset getMasterDs() {
		return masterDs;
	}

	protected NewTempleteWindowWizard getWizard() {
		return wizard;
	}
	
	protected String getTemplateType() {
		return templateType;
	}

	protected void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	
	
	protected DoubleDsSelectPage getDoubleDsPage() {
		return doubleDsPage;
	}
	
	protected void setDoubleDsPage(DoubleDsSelectPage doubleDsPage) {
		this.doubleDsPage = doubleDsPage;
	}

	protected TempWinConfigPage getTempWinConfigPage() {
		return tempWinConfigPage;
	}

	protected void setTempWinConfigPage(TempWinConfigPage tempWinConfigPage) {
		this.tempWinConfigPage = tempWinConfigPage;
	}

	protected CreateWidgetByTemp getCreateAction() {
		return createAction;
	}

	protected void setCreateAction(CreateWidgetByTemp createAction) {
		this.createAction = createAction;
	}
	
	
}
