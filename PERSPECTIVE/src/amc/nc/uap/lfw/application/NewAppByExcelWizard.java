package nc.uap.lfw.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.cpb.org.vos.CpAppsCategoryVO;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.cpb.org.vos.CpMenuCategoryVO;
import nc.uap.cpb.org.vos.CpMenuItemVO;
import nc.uap.cpb.org.vos.CpModuleVO;
import nc.uap.lfw.application.excel.FuncCateObj;
import nc.uap.lfw.application.excel.FuncNodeObj;
import nc.uap.lfw.application.excel.MenuCateObj;
import nc.uap.lfw.application.excel.MenuItemObj;
import nc.uap.lfw.application.excel.ModuleObj;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_application;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.template.ITemplatePageFactory;
import nc.vo.pub.lang.UFBoolean;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewAppByExcelWizard extends Wizard implements
		IExecutableExtension, INewWizard {

	private List<ModuleObj> moduleList;
	private List<FuncCateObj> funcCateList;
	private List<FuncNodeObj> funcNodeList;
	private List<MenuItemObj> menuItemList;
	private List<MenuCateObj> menuCateList;

	private Map<String, ModuleObj> moduleMap;
	private Map<String, FuncNodeObj> funcNodeMap;
	private Map<String, FuncCateObj> funcCateMap;
	private Map<String, MenuItemObj> menuItemMap;
	private Map<String, MenuCateObj> menuCateMap;
	private Map<String, List<MenuItemObj>> funcMenuMap;
	private Map<String,String> menuCatePkMap;

	private ModuleObj[] modules;
	private FuncNodeObj[] funcNodes;

	private List<String> appsCategoryIdList;
	private List<String> menuIdList;

	protected AppFuncSelPage funcpage;
	protected AppMenuCateSelPage menupage;

	private String menuCateId = null;
	private String pk_menuCate = null;

	private String modulePK = null;
	
	public static boolean OK = true;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	public NewAppByExcelWizard() {
		super();
		init();
	}

	public void init() {
		IProject project = LFWPersTool.getCurrentProject();
		TreeItem item = LFWPersTool.getCurrentTreeItem();
		if (item instanceof LFWDirtoryTreeItem) {
			String id = ((LFWDirtoryTreeItem) item).getId();
			CpAppsNodeVO[] appsNodeVOs = LFWWfmConnector
					.getAppsNodeVOsByCondition("appid = '" + id + "'"); //$NON-NLS-1$ //$NON-NLS-2$
			if (appsNodeVOs != null && appsNodeVOs.length > 0) {
				MessageDialog.openInformation(getShell(), M_application.NewAppByExcelWizard_0,
						M_application.NewAppByExcelWizard_1);
				OK = false;
				
			} else {
				try {
					File folder = new File(project.getLocation().toOSString()
							+ "/doc/design"); //$NON-NLS-1$
					File f = null;
					if(folder.exists()){
						for(File file :folder.listFiles()){
							if(file.getName().endsWith(".xlsx")){ //$NON-NLS-1$
								f = file;
								break;
							}
						}
					}
					if (f!=null&&f.exists()) {
						FileInputStream input = new FileInputStream(f);
						importModuleFromExcel(input);
						input.close();
						input = new FileInputStream(f);
						importFuncNodeFromExcel(input);
						input.close();
						input = new FileInputStream(f);
						importMenuFromExcel(input);
						input.close();
						OK = true;
					} else {
						MessageDialog.openError(getShell(), M_application.NewAppByExcelWizard_2,
								M_application.NewAppByExcelWizard_20);
						OK = false;
					}
				} catch (Exception e) {
					WEBPersPlugin.getDefault().logError(e);
				}
			}
		}
	}

	public void importModuleFromExcel(InputStream input) {
		moduleList = new ArrayList<ModuleObj>();
		moduleMap = new HashMap<String, ModuleObj>();
		try {

			XSSFWorkbook wb = new XSSFWorkbook(input);
			XSSFSheet sheet = wb.getSheetAt(0);
			int rowCount = sheet.getLastRowNum() + 1;
			for (int j = 1; j < rowCount; j++) {
				XSSFRow row = sheet.getRow(j);
				ModuleObj moduleObj = new ModuleObj();
				if (null != row) {
					int colCount = row.getLastCellNum();
					for (int k = 0; k < colCount; k++) {
						XSSFCell cell = row.getCell(k);
						if(cell!=null) cell.setCellType(1);
						switch (k) {						
						case 3:
							moduleObj.setModuleId(cell.getStringCellValue());
							break;
						case 4:
							moduleObj.setModuleTitle(cell.getStringCellValue());
							break;
						case 5:
							moduleObj.setModuleCode(cell.getStringCellValue());
							break;
						}
					}
					moduleList.add(moduleObj);
					moduleMap.put(moduleObj.getModuleId(), moduleObj);
				}
			}
			modules = moduleList.toArray(new ModuleObj[0]);
		} catch (Exception e) {
			WEBPersPlugin.getDefault().logError(e);
		}

	}

	public void importFuncNodeFromExcel(InputStream input) {
		funcNodeList = new ArrayList<FuncNodeObj>();
		funcCateList = new ArrayList<FuncCateObj>();
		funcCateMap = new HashMap<String, FuncCateObj>();
		funcNodeMap = new HashMap<String, FuncNodeObj>();
		try {
			XSSFWorkbook wb = new XSSFWorkbook(input);
			XSSFSheet sheet = wb.getSheetAt(1);
			int rowCount = sheet.getLastRowNum() + 1;
			for (int j = 1; j < rowCount; j++) {
				XSSFRow row = sheet.getRow(j);
				FuncCateObj funcCateObj = new FuncCateObj();
				FuncNodeObj funcNodeObj = new FuncNodeObj();
				if (null != row) {
					int colCount = row.getLastCellNum();
					for (int k = 0; k < colCount; k++) {
						XSSFCell cell = row.getCell(k);
						if(cell!=null) cell.setCellType(1);
						switch (k) {
//						case 0:
//							if (cell != null && row.getCell(1)!=null) {
//								funcCateObj.setBcp(cell.getStringCellValue());
//							}
//							else if(cell != null && row.getCell(2)!=null)
//								funcNodeObj.setBcp(cell.getStringCellValue());
//							break;
						case 0:
							if (cell != null && cell.getStringCellValue() != "") { //$NON-NLS-1$
								funcCateObj.setId(cell.getStringCellValue());
							}
							break;
						case 1:
							if (cell != null && cell.getStringCellValue() != "") { //$NON-NLS-1$
								funcNodeObj.setId(cell.getStringCellValue());
							}
							break;
						case 2:
							if (funcCateObj.getId() != null && cell != null ) {
								funcCateObj.setTitle(cell.getStringCellValue());
							} else if (funcNodeObj.getId() != null && cell != null) {
								funcNodeObj.setTitle(cell.getStringCellValue());
							}
							break;
						case 3:
							if (funcCateObj.getId() != null && cell != null ) {
								funcCateObj.setParentId(cell
										.getStringCellValue());
							} else if (funcNodeObj.getId() != null && cell != null) {
								funcNodeObj.setParentId(cell
										.getStringCellValue());
							}
							break;
						case 5:
							if (funcCateObj.getId() != null&&cell != null && cell.getStringCellValue() != "") { //$NON-NLS-1$
								funcCateObj.setModuleId(cell.getStringCellValue());
							}else if (funcNodeObj.getId() != null&&cell != null && cell.getStringCellValue() != "") {  //$NON-NLS-1$
								funcNodeObj.setModuleId(cell.getStringCellValue());
							}
					
						}
					}

				}
				if (funcCateObj.getId() != null) {
					funcCateList.add(funcCateObj);
					funcCateMap.put(funcCateObj.getId(), funcCateObj);
				} else if (funcNodeObj.getId() != null) {
					funcNodeList.add(funcNodeObj);
					funcNodeMap.put(funcNodeObj.getId(), funcNodeObj);
				}
			}
			funcNodes = funcNodeList.toArray(new FuncNodeObj[0]);
		} catch (Exception e) {
			WEBPersPlugin.getDefault().logError(e);
		}

	}

	public void importMenuFromExcel(InputStream input) {
		menuItemList = new ArrayList<MenuItemObj>();
		menuCateList = new ArrayList<MenuCateObj>();
		menuItemMap = new HashMap<String, MenuItemObj>();
		menuCateMap = new HashMap<String, MenuCateObj>();
		funcMenuMap = new HashMap<String, List<MenuItemObj>>();
		try {
			XSSFWorkbook wb = new XSSFWorkbook(input);
			XSSFSheet sheet = wb.getSheetAt(2);
			int rowCount = sheet.getLastRowNum() + 1;
			for (int j = 1; j < rowCount; j++) {
				XSSFRow row = sheet.getRow(j);
				MenuItemObj menuItemObj = new MenuItemObj();
				MenuCateObj menuCateObj = new MenuCateObj();
				if (null != row) {
					int colCount = row.getLastCellNum();
					for (int k = 0; k < colCount; k++) {
						XSSFCell cell = row.getCell(k);
						if(cell!=null) cell.setCellType(1);
						if(k==0&& cell != null
								&& cell.getStringCellValue() != ""){ //$NON-NLS-1$
							menuCateObj.setMenuCode(cell.getStringCellValue());
						}
						if (k>0&&k < 4 && cell != null
								&& cell.getStringCellValue() != "") { //$NON-NLS-1$
							menuItemObj.setMenuCode(cell.getStringCellValue());
						}
						if (k == 4 && cell != null){
							if(menuItemObj.getMenuCode()!=null){
								menuItemObj.setTitle(cell.getStringCellValue());
							}
							else menuCateObj.setTitle(cell.getStringCellValue());
						}
							
						if (k == 5 && cell != null&& menuItemObj.getMenuCode()!=null)
							menuItemObj.setFuncCode(cell.getStringCellValue());
//						if (k == 6 && cell != null&& menuCateObj.getMenuCode()!=null)
//							 menuCateObj.setBcp(cell.getStringCellValue());
					}

				}
				if (menuItemObj.getMenuCode() != null) {
					menuItemList.add(menuItemObj);
					menuItemMap.put(menuItemObj.getMenuCode(), menuItemObj);
					if (menuItemObj.getFuncCode() != null
							&& menuItemObj.getFuncCode() != "") { //$NON-NLS-1$
						List<MenuItemObj> menuList= funcMenuMap.get(menuItemObj.getFuncCode());
						if(menuList==null) menuList = new ArrayList<MenuItemObj>();
						menuList.add(menuItemObj);
						funcMenuMap.put(menuItemObj.getFuncCode(), menuList);
					}
				}
				else if(menuCateObj.getMenuCode()!=null){
					menuCateList.add(menuCateObj);
					menuCateMap.put(menuCateObj.getMenuCode(), menuCateObj);
				}
			}
		} catch (Exception e) {
			WEBPersPlugin.getDefault().logError(e);
		}

	}

	@Override
	public boolean performFinish() {
		try{
			String functionId = funcpage.getSelectedFuncId();
			String appId = funcpage.getApplicationId();
			menuCateId = null;
			pk_menuCate = null;
	//		Boolean[] tempSel = menupage.getTemplateSel();
			if (functionId == null||appId == null) {
				MessageDialog.openError(getShell(), M_application.NewAppByExcelWizard_4, M_application.NewAppByExcelWizard_5);
				return true;
			}
	//		CpAppsNodeVO exsitedFunc =  LFWWfmConnector.getAppsNodeById(functionId);
			if(appId!=null){
				CpAppsNodeVO[] exsitedFuncs =  LFWWfmConnector.getAppsNodeVOsByCondition("appid = '"+appId+"'"); //$NON-NLS-1$ //$NON-NLS-2$
				if (exsitedFuncs != null && exsitedFuncs.length>0) {
		//			MessageDialog.openError(getShell(), "错误", "此功能已注册");
					if(MessageDialog.openConfirm(getShell(), M_application.NewAppByExcelWizard_6, M_application.NewAppByExcelWizard_7)){
						delFuncAndMenuByAppid(appId);
					}
					else return true;
				}
			}
			CpAppsNodeVO exsitedFunc =  LFWWfmConnector.getAppsNodeById(functionId);
			if(exsitedFunc!=null){
				if(MessageDialog.openConfirm(getShell(), M_application.NewAppByExcelWizard_8, M_application.NewAppByExcelWizard_9)){
					delFuncAndMenuByFunc(exsitedFunc);
				}
				else return true;
			}
			CpAppsCategoryVO[] categoryVOs = LFWWfmConnector.getAppsCategory();
			appsCategoryIdList = new ArrayList<String>();
			for (CpAppsCategoryVO cate : categoryVOs) {
				appsCategoryIdList.add(cate.getId());
			}
			
			ModuleObj module = moduleList.get(0);
			CpModuleVO moduleVO = LFWWfmConnector.getModuleById(module.getModuleId());
	
			if (moduleVO == null) {			
				CpModuleVO cpModuleVO = new CpModuleVO();
				cpModuleVO.setTitle(module.getModuleTitle());
				cpModuleVO.setId(module.getModuleId());
				cpModuleVO.setDevmodulecode(module.getModuleCode());
				modulePK = LFWWfmConnector.saveModule(cpModuleVO);
	
			} else {
				modulePK = moduleVO.getPk_module();
			}
	
			funcRegister(appId, functionId);
	
			menuRegister(functionId);
			
			String projectPath = LFWAMCPersTool.getProjectPath();
			Application app = LFWAMCConnector.getApplication(projectPath+"/web/html/applications/"+appId+"/", "application.app"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if(app!=null){
				IExtensionPoint extPoint = Platform.getExtensionRegistry().getExtensionPoint("com.yonyou.studio.web.perspective.template"); //$NON-NLS-1$
				for(IConfigurationElement confElement:extPoint.getConfigurationElements()){
					String type = app.getExtendAttribute("templateType").getValue().toString(); //$NON-NLS-1$
					if(confElement.getAttribute("id").equals(type)){ //$NON-NLS-1$
						ITemplatePageFactory factory = (ITemplatePageFactory)confElement.createExecutableExtension("class"); //$NON-NLS-1$
						factory.publish(functionId, appId);
						break;
					}
				}
			}
			
//			String bcpPath = LFWPersTool.getBcpPath(LFWPersTool.getCurrentTreeItem());
//			File queryFolder = new File(bcpPath,"/queryt/"+appId);
//		
//			if(queryFolder.exists()){
//				for(File qryFile:queryFolder.listFiles()){
//					if(qryFile.isFile()){
//						String name = qryFile.getName();
//						if(name.endsWith("qt")){
//							String pk = LFWWfmConnector.generatePK();
//							String content = FileUtilities.fetchFileContent(qryFile, "GBK");
//							content = content.replace("${nodecode}", functionId);
//							content = content.replace("${pk_qty}", pk);
//							LFWWfmConnector.executeSql(content);
//							MainPlugin.getDefault().logInfo("execute sql :"+content);
//							File qtcFile = new File(queryFolder,name+"c");
//							if(qtcFile.exists()){
//								content = FileUtilities.fetchFileContent(qtcFile, "GBK");
//								String lines[] = content.split("\n");
//								for(String line:lines){
//									String pk_condition = LFWWfmConnector.generatePK();
//									line = line.replace("${pk_query_con}", pk_condition);
//									line = line.replace("${pk_qty}", pk);
//									LFWWfmConnector.executeSql(line);
//									MainPlugin.getDefault().logInfo("execute sql :"+line);
//								}
//								
//							}
//						}
////						String ext = name.substring(name.lastIndexOf(".")+1,name.length());
////						fileMap.get(ext).add(qryFile);
//					}					
//				}
//
//			}
//			File printFolder = new File(bcpPath,"/printt/"+appId);
//			if(printFolder.exists()){
//				
//			}
		
		}
		catch(Exception e){
			MainPlugin.getDefault().logError(e.getMessage());
		}
//		if(tempSel[0]){
//			CpQueryTemplateVO template = new CpQueryTemplateVO();
//			template.setModelcode(selectedFunc.getId());
//			template.setModelname(selectedFunc.getTitle());
//			template.setNodecode(selectedFunc.getId());
//			String pk = LFWWfmConnector.getQueryTemplateByNodeCode(selectedFunc.getId());
//			if(pk==null) LFWWfmConnector.saveQryTemplate(template);
//			else{
//				MessageDialog.openError(getShell(), "错误", "此功能节点已有查询模板");
//			}
//
//		}
//		if(tempSel[1]){
//			CpPrintTemplateVO template = new CpPrintTemplateVO();
//			template.setModelcode(selectedFunc.getId());
//			template.setModelname(selectedFunc.getTitle());
//			template.setNodecode(selectedFunc.getId());
//			String pk = LFWWfmConnector.getPrintTemplateByNodeCode(selectedFunc.getId());
//			if(pk==null) LFWWfmConnector.savePrintTemplate(template);
//			else{
//				MessageDialog.openError(getShell(), "错误", "此功能节点已有打印模板");
//			}
//			
//		}
		// Set<String> keys = funcCateMap.keySet();
		// while(keys.contains(parentId)){
		// FuncCateObj funcCate = funcCateMap.get(parentId);
		// if(!appsCategoryIdList.contains(parentId){
		//
		// }
		// }
		MessageDialog.openInformation(getShell(), M_application.NewAppByExcelWizard_10, M_application.NewAppByExcelWizard_11);
		return true;
	}

	/**
	 * @param functionId
	 */
	private void menuRegister(String functionId) {
		// 递归进行菜单注册
		List<MenuItemObj> menuList =  funcMenuMap.get(functionId);
		if(menuList==null||menuList.size()==0){
			MessageDialog.openWarning(getShell(), M_application.NewAppByExcelWizard_12, M_application.NewAppByExcelWizard_13);
			return;
		}
		for(MenuItemObj selectedMenu:menuList){
			String code = selectedMenu.getMenuCode();
			String parentCode = code.substring(0, code.length() - 2);
			menuCatePkMap = new HashMap<String, String>();
			Set<String> keys = menuItemMap.keySet();
			CpMenuItemVO menuItemVO = new CpMenuItemVO();
			if (keys.contains(parentCode)) {
				searchParentMenu(menuItemMap.get(parentCode));
				String newPk_parent = LFWWfmConnector.getMenuItemById(parentCode)
						.getPk_menuitem();
				menuItemVO.setPk_parent(newPk_parent);
			}
			menuItemVO.setCode(code);
			menuItemVO.setName(selectedMenu.getTitle());
			String pk_funnode = LFWWfmConnector.getAppsNodeById(
					selectedMenu.getFuncCode()).getPk_appsnode();
			menuItemVO.setPk_funnode(pk_funnode);
			String pk_menucategory = null;
			if(pk_menuCate==null)
				pk_menucategory = LFWWfmConnector.getMenuCategoryById(
					menuCateId).getPk_menucategory();
			else 
				pk_menucategory = pk_menuCate;
	
			menuItemVO.setPk_menucategory(pk_menucategory);
			LFWWfmConnector.saveMenuItem(menuItemVO);
			MainPlugin.getDefault().logInfo("insert menuItem " + selectedMenu.getTitle()); //$NON-NLS-1$
		}
	}

	/**
	 * @param appId
	 * @param functionId
	 */
	private void funcRegister(String appId, String functionId) {
		// 递归进行功能注册
		FuncNodeObj selectedFunc = funcNodeMap.get(functionId);
		String parentId = selectedFunc.getParentId();
		String parentPK = null;
		FuncCateObj funCate = funcCateMap.get(parentId);
		CpAppsCategoryVO parentCateVO = LFWWfmConnector.getAppsCategoryById(parentId);
		if(parentCateVO!=null){
			parentPK = parentCateVO.getPk_appscategory();
		}
		else{
			CpAppsCategoryVO appsCateVO = new CpAppsCategoryVO();
			appsCateVO.setId(parentId);
			appsCateVO.setTitle(funCate.getTitle());
			appsCateVO.setActiveflag(UFBoolean.TRUE);
			appsCateVO.setPk_module(modulePK);
			MainPlugin.getDefault().logInfo("creating appcategory, id:" + parentId); //$NON-NLS-1$
			LFWWfmConnector.saveAppsCategory(appsCateVO);
			parentPK = LFWWfmConnector.getAppsCategoryById(parentId).getPk_appscategory();
			MainPlugin.getDefault().logInfo("creating appcategory, id:" + parentId + ":done"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		CpAppsNodeVO selectedNodeVO = new CpAppsNodeVO();
		selectedNodeVO.setId(selectedFunc.getId());
		selectedNodeVO.setPk_appscategory(parentPK);
		selectedNodeVO.setTitle(selectedFunc.getTitle());
		selectedNodeVO.setActiveflag(UFBoolean.TRUE);
		LFWDirtoryTreeItem funcItem = (LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem();
		selectedNodeVO.setDevcomponent(funcItem.getBcp().getName());
		selectedNodeVO.setType(1);
		String url = null;
		if(appId==null){
			TreeItem item = LFWPersTool.getCurrentTreeItem();
			if (item instanceof LFWDirtoryTreeItem) {
				url = "app/" + ((LFWDirtoryTreeItem) item).getId(); //$NON-NLS-1$
				selectedNodeVO.setAppid(((LFWDirtoryTreeItem) item).getId());
			}
		}
		else{
			selectedNodeVO.setAppid(appId);
			url = "app/" + appId; //$NON-NLS-1$
		}
		selectedNodeVO.setUrl(url);
		LFWWfmConnector.saveAppsNode(selectedNodeVO);
		MainPlugin.getDefault().logInfo("insert the apps node"); //$NON-NLS-1$
	}

	/**
	 * @param exsitedFunc
	 */
	private void delFuncAndMenuByFunc(CpAppsNodeVO exsitedFunc) {
		String pk_funnode = exsitedFunc.getPk_appsnode();		
		CpMenuItemVO[] menuItemVOs = LFWWfmConnector.getMenuItemsByCondition("pk_funnode = '"+pk_funnode+"'"); //$NON-NLS-1$ //$NON-NLS-2$
		if(menuItemVOs!=null && menuItemVOs.length>0){
			String pk_menuitem = menuItemVOs[0].getPk_menuitem();
			LFWWfmConnector.delMenuItem(pk_menuitem);
		}
		LFWWfmConnector.delAppsNode(pk_funnode);
		String appId = exsitedFunc.getAppid();
		String funcCode = exsitedFunc.getId();
		String queryPk = LFWWfmConnector.getQueryTemplateByNodeCode(funcCode);
		String printPk = LFWWfmConnector.getPrintTemplateByNodeCode(funcCode);
		if(queryPk!=null){
			LFWWfmConnector.executeSql("UPDATE cp_query_template  set nodecode = '$tempcode_" + appId + "' WHERE nodecode = '" + funcCode + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//			LFWWfmConnector.delQryTemplate(queryPk);
//			LFWWfmConnector.executeSql("DELETE FROM cp_query_condition WHERE pk_query_template = '"+queryPk+"'");
		}
		if(printPk!=null) {
			LFWWfmConnector.executeSql("UPDATE cp_print_template  set nodecode = '$tempcode_" + appId + "' WHERE nodecode = '" + funcCode + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//			LFWWfmConnector.delPrintTemplate(printPk);
//			LFWWfmConnector.executeSql("DELETE FROM cp_print_condition WHERE pk_print_template = '"+printPk+"'");
		}
		MessageDialog.openInformation(null, M_application.NewAppByExcelWizard_14, M_application.NewAppByExcelWizard_15);
	}

	/**
	 * @param appId
	 */
	private void delFuncAndMenuByAppid(String appId) {
		TreeItem item = LFWPersTool.getCurrentTreeItem();
		if(item instanceof LFWDirtoryTreeItem){
			String id = appId;
			CpAppsNodeVO[] appsNodeVOs = LFWWfmConnector.getAppsNodeVOsByCondition("appid = '"+id+"'"); //$NON-NLS-1$ //$NON-NLS-2$
			if(appsNodeVOs!=null&&appsNodeVOs.length>0){
				String pk_funnode = appsNodeVOs[0].getPk_appsnode();				
				CpMenuItemVO[] menuItemVOs = LFWWfmConnector.getMenuItemsByCondition("pk_funnode = '"+pk_funnode+"'"); //$NON-NLS-1$ //$NON-NLS-2$
				if(menuItemVOs!=null && menuItemVOs.length>0){
					String pk_menuitem = menuItemVOs[0].getPk_menuitem();
					LFWWfmConnector.delMenuItem(pk_menuitem);
				}
				LFWWfmConnector.delAppsNode(pk_funnode);
				String funcCode = appsNodeVOs[0].getId();
				String queryPk = LFWWfmConnector.getQueryTemplateByNodeCode(funcCode);
				String printPk = LFWWfmConnector.getPrintTemplateByNodeCode(funcCode);
				if(queryPk!=null) {
					LFWWfmConnector.executeSql("UPDATE cp_query_template  set nodecode = '$tempcode_" + appId + "' WHERE nodecode = '" + funcCode + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//					LFWWfmConnector.delQryTemplate(queryPk);
//					LFWWfmConnector.executeSql("DELETE FROM cp_query_condition WHERE pk_query_template = '"+queryPk+"'");
				}
				if(printPk!=null) {
					LFWWfmConnector.executeSql("UPDATE cp_print_template  set nodecode = '$tempcode_" + appId + "' WHERE nodecode = '" + funcCode + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//					LFWWfmConnector.delPrintTemplate(printPk);
//					LFWWfmConnector.executeSql("DELETE FROM cp_print_condition WHERE pk_print_template = '"+printPk+"'");
				}
				MessageDialog.openInformation(null, M_application.NewAppByExcelWizard_16, M_application.NewAppByExcelWizard_17);
			}
			else{
				MessageDialog.openWarning(null, M_application.NewAppByExcelWizard_18, M_application.NewAppByExcelWizard_19);
			}
		}
	}

	public void searchParentFunc(FuncCateObj funcCateObj) {
		if (appsCategoryIdList.contains(funcCateObj.getId())) {
			return;
		} else {
			String parentPK = null;
			if (funcCateObj.getParentId() != null
					&& funcCateObj.getParentId() != "") { //$NON-NLS-1$
				searchParentFunc(funcCateMap.get(funcCateObj.getParentId()));
				CpAppsCategoryVO parentCateVO = LFWWfmConnector
						.getAppsCategoryById(funcCateObj.getParentId());
				if (parentCateVO != null) {
					parentPK = parentCateVO.getPk_appscategory();
				}
			}

			CpAppsCategoryVO selectedCateVO = new CpAppsCategoryVO();
			selectedCateVO.setId(funcCateObj.getId());
			selectedCateVO.setPk_parent(parentPK);
			selectedCateVO.setPk_module(modulePK);
			selectedCateVO.setTitle(funcCateObj.getTitle());
			// 保存CpAppsCategoryVO对象
			LFWWfmConnector.saveAppsCategory(selectedCateVO);
			MainPlugin.getDefault().logInfo("insert the apps cate"); //$NON-NLS-1$
		}
	}

	public void searchParentMenu(MenuItemObj menuItemObj) {
		String code = menuItemObj.getMenuCode();
		String parentCode = code.substring(0, code.length() - 2);
		Set<String> keys = menuItemMap.keySet();
		CpMenuItemVO item = LFWWfmConnector.getMenuItemById(code);
		if ( item != null) {
			pk_menuCate = item.getPk_menucategory();
			return;
		} 
		else if(menuItemList.contains(menuItemObj)&& menuCateMap.get(parentCode)!=null){
			CpMenuCategoryVO exsitCategory = LFWWfmConnector.getMenuCategoryById(parentCode);
			String pk_menuCate = null;
			if(exsitCategory==null){
				CpMenuCategoryVO menuCateVO= new CpMenuCategoryVO();
				MenuCateObj cateobj = menuCateMap.get(parentCode);
				menuCateVO.setId(parentCode);
				menuCateVO.setTitle(cateobj.getTitle());
				menuCateVO.setActiveflag(UFBoolean.TRUE);
				pk_menuCate = LFWWfmConnector.saveMenuCategory(menuCateVO);
				menuCatePkMap.put(pk_menuCate, menuCateVO.getId());
			}
			else{
				pk_menuCate = exsitCategory.getPk_menucategory();
				if(menuCatePkMap.get(pk_menuCate)==null) menuCatePkMap.put(pk_menuCate, exsitCategory.getId());
			}
			menuCateId = parentCode;
			CpMenuItemVO menuItemVO = new CpMenuItemVO();
			String pk_menucategory = LFWWfmConnector.getMenuCategoryById(
					menuCateId).getPk_menucategory();
			pk_menuCate = pk_menucategory;
			menuItemVO.setCode(code);
			menuItemVO.setPk_menucategory(pk_menucategory);
			menuItemVO.setName(menuItemObj.getTitle());
			CpAppsNodeVO funcNode = LFWWfmConnector.getAppsNodeById(menuItemObj.getFuncCode());
			if(funcNode!=null){
				String pk_funnode = funcNode.getPk_appsnode();
				menuItemVO.setPk_funnode(pk_funnode);
			}			
			LFWWfmConnector.saveMenuItem(menuItemVO);
			return;
		}
			else {
			CpMenuItemVO menuItemVO = new CpMenuItemVO();
			if (keys.contains(parentCode)) {
				searchParentMenu(menuItemMap.get(parentCode));
				String newPk_parent = LFWWfmConnector.getMenuItemById(
						parentCode).getPk_menuitem();
				menuItemVO.setPk_parent(newPk_parent);
			} else
				menuItemVO.setPk_parent(null);

			String pk_menucategory = null;
			if(pk_menuCate==null)
				pk_menucategory = LFWWfmConnector.getMenuCategoryById(
					menuCateId).getPk_menucategory();
			else pk_menucategory = pk_menuCate;
			menuItemVO.setCode(code);
			menuItemVO.setPk_menucategory(pk_menucategory);
			menuItemVO.setName(menuItemObj.getTitle());
			CpAppsNodeVO funcNode = LFWWfmConnector.getAppsNodeById(menuItemObj.getFuncCode());
			if(funcNode!=null){
				String pk_funnode = funcNode.getPk_appsnode();
				menuItemVO.setPk_funnode(pk_funnode);
			}	
			LFWWfmConnector.saveMenuItem(menuItemVO);
			MainPlugin.getDefault().logInfo("insert menuItem " + menuItemObj.getTitle()); //$NON-NLS-1$
		}
	}

	/**
	 * 创建向导页面
	 */
	@Override
	public void addPages() {
		funcpage = new AppFuncSelPage(
				WEBPersPlugin
						.getResourceString(WEBPersConstants.KEY_EXCEL_MAINPAGE_TITLE));
		funcpage.setFuncNodes(funcNodes);
		addPage(funcpage);
//		menupage = new AppMenuCateSelPage(
//				WEBProjPlugin
//						.getResourceString(WEBProjConstants.KEY_EXCEL_MAINPAGE_TITLE));
//		addPage(menupage);
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		// TODO Auto-generated method stub
	}

}
