package nc.uap.lfw.perspective.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.cpb.org.vos.CpMenuCategoryVO;
import nc.uap.cpb.org.vos.CpMenuItemVO;
import nc.uap.cpb.org.vos.CpModuleVO;
import nc.uap.lfw.application.excel.FuncCateObj;
import nc.uap.lfw.application.excel.FuncNodeObj;
import nc.uap.lfw.application.excel.MenuCateObj;
import nc.uap.lfw.application.excel.MenuItemObj;
import nc.uap.lfw.application.excel.ModuleObj;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.internal.bcp.BCPManifest;
import nc.uap.lfw.internal.bcp.BCPSourceFolder;
import nc.uap.lfw.internal.bcp.BusinessComponent;
import nc.uap.lfw.internal.util.BCPUtils;
import nc.uap.lfw.internal.util.ProjCoreUtility;
import nc.uap.lfw.lang.M_perspective;
import nc.vo.pub.lang.UFBoolean;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.dialogs.MessageDialog;

public class CreateByExcelAction extends NodeAction {
	
	private List<ModuleObj> moduleList;
	private List<FuncCateObj> funcCateList;
	private List<FuncNodeObj> funcNodeList;
	private List<MenuItemObj> menuItemList;
	private List<MenuCateObj> menuCateList;
	private Map<String,String> menuCatePkMap;

	private Map<String, ModuleObj> moduleMap;
	private Map<String, FuncNodeObj> funcNodeMap;
	private Map<String, FuncCateObj> funcCateMap;
	private Map<String, MenuItemObj> menuItemMap;
	private Map<String, MenuCateObj> menuCateMap;
	private Map<String, List<MenuItemObj>> funcMenuMap;

	private ModuleObj[] modules;
	private FuncNodeObj[] funcNodes;

	private List<String> appsCategoryIdList;
	private List<String> menuIdList;


	private String menuCateId = null;
	private String pk_menuCate = null;

	private String modulePK = null;
	
	private File f = null;
	private IProject project = null;

	public CreateByExcelAction() {
		super(M_perspective.CreateByExcelAction_0);
	}

	public void run() {
		project = LFWPersTool.getCurrentProject();
		File folder = new File(project.getLocation().toOSString()
			+ "/doc/design"); //$NON-NLS-1$
		if(folder.exists()){
		for(File file :folder.listFiles()){
			if(file.getName().endsWith(".xlsx")){ //$NON-NLS-1$
				f = file;
				break;
			}
			}
		}
		if (f==null||!f.exists()) {
			MessageDialog.openError(null, M_perspective.CreateByExcelAction_1, M_perspective.CreateByExcelAction_2);
			return;
		}
		Job job = new Job(M_perspective.CreateByExcelAction_3) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					monitor.beginTask(M_perspective.CreateByExcelAction_4, 3);

					if (f!=null&&f.exists()) {
						MainPlugin.getDefault().logInfo("creating bcp component"); //$NON-NLS-1$
						createBcpCompenent(project,f);
						monitor.worked(1);
//						MainPlugin.getDefault().logInfo("creating applications");
//						createApplication(project,f);
//						monitor.worked(1);
						MainPlugin.getDefault().logInfo("creating functions and menus"); //$NON-NLS-1$
						functionAndMenu(project,f);
						monitor.worked(1);
						MainPlugin.getDefault().logInfo("creating portal config"); //$NON-NLS-1$
						portalConfig(project);	
					}
					else{
						MainPlugin.getDefault().logError(M_perspective.CreateByExcelAction_5);
					}
				} catch (Exception e) {
					MainPlugin.getDefault().logError(e.getMessage());
				}
				finally{
					if(funcNodeList!=null)
						funcNodeList.clear();
					if(menuItemList!=null)
						menuItemList.clear();					
					if(funcNodeMap!=null)
						funcNodeMap.clear();
					if(funcCateMap!=null)
						funcCateMap.clear();
					if(funcNodeMap!=null)
						funcNodeMap.clear();
					if(menuItemMap!=null)
						menuItemMap.clear();
					if(menuCateMap!=null)
						menuCateMap.clear();
					if(funcMenuMap!=null)
						funcMenuMap.clear();
				}
				monitor.done();
				return Status.OK_STATUS;
			}

		};
		job.schedule();
	}

	public void createBcpCompenent(IProject project,File f) throws Exception {
		FileInputStream input = null;
		try{
			input = new FileInputStream(f);
			XSSFWorkbook wb = new XSSFWorkbook(input);		
			XSSFSheet sheet = wb.getSheetAt(1);
			int rowCount = sheet.getLastRowNum() + 1;
			for (int j = 1; j < rowCount; j++) {
				XSSFRow row = sheet.getRow(j);
				if (null != row) {
					int colCount = row.getLastCellNum();
					String displayName = null;
					String bcpName = null;
					for (int k = 0; k < colCount; k++) {
						XSSFCell cell = row.getCell(k);
						if(cell!=null) cell.setCellType(1);
						switch (k) {
							case 1:
								displayName = cell.getStringCellValue();
								break;
							case 2:
								bcpName = cell.getStringCellValue();
								break;
						}
	
					}
					BusinessComponent businessComponent = new BusinessComponent();
					businessComponent.setComponentDisplayName(displayName);
					businessComponent.setComponentName(bcpName);
					IFile manifestFile = project.getFile("manifest.xml"); //$NON-NLS-1$
					BCPManifest bcpmanifest = (BCPManifest) BCPUtils
							.read(manifestFile);
					if (bcpmanifest == null)
						bcpmanifest = new BCPManifest();
					bcpmanifest.addBusinessComponent(businessComponent);
					BCPUtils.writeObjectXml2File(manifestFile, bcpmanifest,
							JavaPlugin.getActiveWorkbenchShell(), null);
					BCPUtils.createDefaultSourceableFolers(businessComponent);
					List<BCPSourceFolder> allSourceFolderList = new ArrayList<BCPSourceFolder>();
					allSourceFolderList.addAll(BCPUtils.checkBCFolder(project,
							null, businessComponent));
					if (!LfwCommonTool.getModuleProperty(project, "module.name") //$NON-NLS-1$
							.equals(LfwCommonTool.getModuleProperty(project,
									"module.webContext"))) { //$NON-NLS-1$
						String path = project.getLocation().toString() + "/" //$NON-NLS-1$
								+ businessComponent.getComponentName()
								+ "/web/WEB-INF/"; //$NON-NLS-1$
						File folder = new File(path);
						if (!folder.exists())
							folder.mkdirs();
						writePart(path, bcpName,
								LfwCommonTool.getModuleProperty(project,
										"module.webContext")); //$NON-NLS-1$
						String metadatapath =project.getLocation().toString() + "/" //$NON-NLS-1$
						+ businessComponent.getComponentName()+"/METADATA/"; //$NON-NLS-1$
						File metadatafolder = new File(metadatapath);
						if(!metadatafolder.exists()){
							metadatafolder.mkdirs();
						}
						project.refreshLocal(2, null);
						BCPUtils.rebuildBCPProject(project, allSourceFolderList, null);
						if (!project.hasNature(WEBPersConstants.BCPMODULE_NATURE))
							ProjCoreUtility.addNatureToProject(project,
									WEBPersConstants.BCPMODULE_NATURE, null);
//						if(!project.hasNature(WEBPersConstants.BCP_MDE_MODULE_NATURE))
//							ProjCoreUtility.addNatureToProject(project,
//									WEBPersConstants.BCP_MDE_MODULE_NATURE, null);
					}
				}
			}
		}
		catch(Exception e){
			MainPlugin.getDefault().logError(e.getMessage());
		}
		finally{
			if(input != null)
				input.close();
		}
	}

	public void createApplication(IProject project,File f) throws Exception {
		FileInputStream input = null;
		try{
			input = new FileInputStream(f);
			if(funcNodeList==null) 
				importFuncNodeFromExcel(input);
			for(FuncNodeObj funcNode : funcNodeList){
				Application appConf = new Application();
				String appName = funcNode.getTitle();
				String appId = funcNode.getId();
				MainPlugin.getDefault().logInfo("creating application, id:" + appId); //$NON-NLS-1$
				appConf.setId(appId);
				appConf.setCaption(appName);
				appConf.setControllerClazz(appId+".AppController"); //$NON-NLS-1$
				appConf.setSourcePackage(funcNode.getBcp()+"/src/public/"); //$NON-NLS-1$
				appConf.setRealPath(appId);
				int index = appConf.getControllerClazz().lastIndexOf("."); //$NON-NLS-1$
				String packageName = null;
				if(index > 0){
					packageName = appConf.getControllerClazz().substring(0, index);
				}
				else{
					packageName = ""; //$NON-NLS-1$
				}
				String projectPath = LFWAMCPersTool.getLFWProjectPath();
				String className = appConf.getControllerClazz().substring(index + 1);
				String classFilePath = projectPath + File.separator + appConf.getSourcePackage() + packageName.replaceAll("\\.", "/"); //$NON-NLS-1$ //$NON-NLS-2$
				String classFileName = className + ".java"; //$NON-NLS-1$
				String filePath = projectPath + File.separator+ funcNode.getBcp()+"/web/html/applications/" + appConf.getId(); //$NON-NLS-1$
				LFWAMCConnector.createApplicationWithoutCheck(packageName, className, classFilePath, classFileName, filePath, WEBPersConstants.AMC_APPLICATION_FILENAME, projectPath, appConf);
				MainPlugin.getDefault().logInfo("creating application, id:" + appId + ":done"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			MainPlugin.getDefault().logInfo("refreshing current project"); //$NON-NLS-1$
			LFWAMCPersTool.refreshCurrentPorject();
			MainPlugin.getDefault().logInfo("refreshing current project: done"); //$NON-NLS-1$
		}
		catch(Exception e){
			MainPlugin.getDefault().logError(e.getMessage());
		}
		finally{
			if(input != null)
				input.close();
		}
	}

	public void functionAndMenu(IProject project,File f) throws Exception {
		FileInputStream funcinput = new FileInputStream(f);
		FileInputStream menuinput = new FileInputStream(f);
		FileInputStream moduleinput = new FileInputStream(f);
		try{
			if(funcNodeList == null) 
				importFuncNodeFromExcel(funcinput);
			if(menuItemList == null) 
				importMenuFromExcel(menuinput);
			//注册模块
			XSSFWorkbook wb = new XSSFWorkbook(moduleinput);
			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row = sheet.getRow(1);
			ModuleObj moduleObj = new ModuleObj();			
			moduleObj.setModuleCode(row.getCell(1).getStringCellValue());
			XSSFCell cell = row.getCell(2);
			if(cell!=null) 
				cell.setCellType(1);
			moduleObj.setModuleId(cell.getStringCellValue());
			moduleObj.setModuleTitle(row.getCell(3).getStringCellValue());
			CpModuleVO moduleVO = LFWWfmConnector.getModuleById(moduleObj.getModuleId());
			if (moduleVO == null) {				
				CpModuleVO cpModuleVO = new CpModuleVO();
				cpModuleVO.setTitle(moduleObj.getModuleTitle());
				cpModuleVO.setId(moduleObj.getModuleId());
				cpModuleVO.setDevmodulecode(moduleObj.getModuleCode());
				cpModuleVO.setActiveflag(UFBoolean.TRUE);
				MainPlugin.getDefault().logInfo("creating module, id:" + moduleObj.getModuleId()); //$NON-NLS-1$
				modulePK = LFWWfmConnector.saveModule(cpModuleVO);
				MainPlugin.getDefault().logInfo("creating module, id:" + moduleObj.getModuleId() + ":done"); //$NON-NLS-1$ //$NON-NLS-2$
			} 
			else {
				modulePK = moduleVO.getPk_module();
			}
			//循环注册功能
//			for(FuncNodeObj funcNode:funcNodeList){
//				String parentId = funcNode.getParentId();
//				String parentPK = null;
//				FuncCateObj funCate = funcCateMap.get(parentId);
//				CpAppsCategoryVO parentCateVO = LFWWfmConnector.getAppsCategoryById(parentId);
//				if(parentCateVO!=null){
//					parentPK = parentCateVO.getPk_appscategory();
//				}
//				else{
//					CpAppsCategoryVO appsCateVO = new CpAppsCategoryVO();
//					appsCateVO.setId(parentId);
//					appsCateVO.setTitle(funCate.getTitle());
//					appsCateVO.setActiveflag(new UFBoolean(true));
//					appsCateVO.setPk_module(modulePK);
//					MainPlugin.getDefault().logInfo("creating appcategory, id:" + parentId);
//					LFWWfmConnector.saveAppsCategory(appsCateVO);
//					parentPK = LFWWfmConnector.getAppsCategoryById(parentId).getPk_appscategory();
//					MainPlugin.getDefault().logInfo("creating appcategory, id:" + parentId + ":done");
//				}
//				CpAppsNodeVO selectedNodeVO = new CpAppsNodeVO();
//				selectedNodeVO.setId(funcNode.getId());
//				selectedNodeVO.setPk_appscategory(parentPK);
//				selectedNodeVO.setTitle(funcNode.getTitle());
//				selectedNodeVO.setActiveflag(new UFBoolean(true));
//				selectedNodeVO.setDevcomponent(funcNode.getBcp());
//				selectedNodeVO.setType(1);
//				String url = null;
//				String appid = funcNode.getId();
//				selectedNodeVO.setAppid(appid);
//				url = "app/" + appid;
//				selectedNodeVO.setUrl(url);
//				MainPlugin.getDefault().logInfo("creating appnode, id:" + funcNode.getId());
//				LFWWfmConnector.saveAppsNode(selectedNodeVO);
//				MainPlugin.getDefault().logInfo("creating appnode, id:" + funcNode.getId() + ":done");
//			}
			//递归注册菜单
			menuCatePkMap = new HashMap<String, String>();
			for(FuncNodeObj funcNode:funcNodeList){
				menuCateId = null;
				pk_menuCate = null;
				String functionId = funcNode.getId();
				List<MenuItemObj> menuList = funcMenuMap.get(functionId);
				if(menuList==null||menuList.size()==0){
					continue;
				}
				for(MenuItemObj selectedMenu:menuList){
					if(selectedMenu!=null){
						String code = selectedMenu.getMenuCode();
						String parentCode = code.substring(0, code.length() - 2);
						Set<String> keys = menuItemMap.keySet();
						CpMenuItemVO menuItemVO = new CpMenuItemVO();
						if (keys.contains(parentCode)) {
							searchParentMenu(menuItemMap.get(parentCode));
		//					String newPk_parent = LFWWfmConnector.getMenuItemById(parentCode)
		//							.getPk_menuitem();
		//					menuItemVO.setPk_parent(newPk_parent);
						}
						else if(menuCateMap.get(parentCode)!=null && LFWWfmConnector.getMenuCategoryById(parentCode)== null){
							CpMenuCategoryVO menuCateVO= new CpMenuCategoryVO();
							MenuCateObj cateobj = menuCateMap.get(parentCode);
							menuCateVO.setId(parentCode);
							menuCateVO.setTitle(cateobj.getTitle());
							menuCateVO.setActiveflag(UFBoolean.TRUE);
							pk_menuCate = LFWWfmConnector.saveMenuCategory(menuCateVO);
							menuCatePkMap.put(pk_menuCate, menuCateVO.getId());
						}
		//				menuItemVO.setCode(code);
		//				menuItemVO.setName(selectedMenu.getTitle());
		//				String pk_funnode = LFWWfmConnector.getAppsNodeById(
		//						selectedMenu.getFuncCode()).getPk_appsnode();
		//				menuItemVO.setPk_funnode(pk_funnode);
		//				String pk_menucategory = null;
		//				if(pk_menuCate==null)
		//					pk_menucategory = LFWWfmConnector.getMenuCategoryById(
		//						menuCateId).getPk_menucategory();
		//				else 
		//					pk_menucategory = pk_menuCate;
		//				menuItemVO.setPk_menucategory(pk_menucategory);
		//				MainPlugin.getDefault().logInfo("creating menuitem, id:" + code);
		//				LFWWfmConnector.saveMenuItem(menuItemVO);
		//				MainPlugin.getDefault().logInfo("creating menuitem, id:" + code + ":done");
					}
				}
			}
		}
		catch(Exception e){
			MainPlugin.getDefault().logError(e.getMessage());
		}
		finally{
			moduleinput.close();
			funcinput.close();
			menuinput.close();
		}
	}
	
	public void portalConfig(IProject project) throws Exception {
		InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream("templates/portalpage/admin.pml"); //$NON-NLS-1$
		byte[] b = new byte[1638400];
		int bytesReader = 0;
		String tempcontent = ""; //$NON-NLS-1$
		StringBuffer content = new StringBuffer();
		try{
			while(true){
				bytesReader = ins.read(b,0,1638400);
				if(bytesReader==-1){
					break;
				}
				String lineContent =""; //$NON-NLS-1$
				lineContent = new String(b,"UTF-8"); //$NON-NLS-1$

//				content.append(new String(b,0,bytesReader));
				content.append(lineContent);
				
			}
			tempcontent = content.toString().trim();
			
			for(String pk_menuCate:menuCatePkMap.keySet()){
				String sourcecontent = tempcontent;
				String menuCateId =  menuCatePkMap.get(pk_menuCate);
				MenuCateObj menuCateObj = menuCateMap.get(menuCateId);
				sourcecontent = replaceTemp(sourcecontent,"level","1"); //$NON-NLS-1$ //$NON-NLS-2$
				sourcecontent = replaceTemp(sourcecontent,"menuCategory",pk_menuCate); //$NON-NLS-1$
				sourcecontent = replaceTemp(sourcecontent, "menuGroupName",menuCateObj.getTitle()); //$NON-NLS-1$
				String bcp = menuCateObj.getBcp();
//				String filePath = getPortalSpecPath(projectPath,projectModuleName) + "/pml"; 
				String filePath = project.getLocation().toOSString()+"/"+bcp+"/src/portalspec/" + bcp + "/portalspec/pml";  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				String fileName = menuCateId;
				File folder = new File(filePath);
				if(!folder.exists()) 
					folder.mkdirs();
		    	File file = new File(filePath + "/" + fileName + ".pml"); //$NON-NLS-1$ //$NON-NLS-2$
		    	try {
		    		MainPlugin.getDefault().logInfo("creating pml, id:" + fileName); //$NON-NLS-1$
//		    		FileUtils.writeStringToFile(file, xml, "UTF-8");
		    		FileUtilities.saveFile(file, sourcecontent,"UTF-8");	 //$NON-NLS-1$
		    		MainPlugin.getDefault().logInfo("creating pml, id:" + fileName + ":done"); //$NON-NLS-1$ //$NON-NLS-2$
		    	} 
		    	catch (Exception e) {
		    		MainPlugin.getDefault().logError(e.getMessage());
		    	}
			}
			
			MainPlugin.getDefault().logInfo("refreshing project"); //$NON-NLS-1$
			project.refreshLocal(2, null);
			LFWAMCPersTool.refreshCurrentPorject();
			MainPlugin.getDefault().logInfo("refreshing project:done"); //$NON-NLS-1$
			
		}
		catch(Exception e){
			MainPlugin.getDefault().logError(e);
		}
		finally{
			ins.close();
		}
	}
	/*
	 * 替换模板文件中的标签
	 */
	public String replaceTemp(String content, String markersign, String replacecontent){
		markersign = "${"+markersign+"}"; //$NON-NLS-1$ //$NON-NLS-2$
		String target = content;
		while(target.indexOf(markersign)>-1){
			target = target.replace(markersign, replacecontent);
		}
		return target.trim();
	}
	
	/**
	 * 生成*.part
	 * 
	 * @param webPath
	 *            WEB-INF 路径
	 * @param module
	 *            模块名
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	protected void writePart(String webPath, String module, String mainContext)
			throws UnsupportedEncodingException, Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"); //$NON-NLS-1$
		buffer.append("\n<web-app>"); //$NON-NLS-1$
		buffer.append("\n	<context-param>"); //$NON-NLS-1$
		buffer.append("\n		<param-name>ctxPath</param-name>"); //$NON-NLS-1$
		buffer.append("\n		<param-value>/" + module + "</param-value>"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("\n	</context-param>\n"); //$NON-NLS-1$
		buffer.append("\n	<context-param>"); //$NON-NLS-1$
		buffer.append("\n		<param-name>modules</param-name>"); //$NON-NLS-1$
		buffer.append("\n		<param-value>" + module + "</param-value>"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("\n	</context-param>"); //$NON-NLS-1$
		buffer.append("\n</web-app>"); //$NON-NLS-1$
		File f = new File(webPath);
		if (!f.exists()) {
			f.mkdirs();
		}
		FileUtilities.saveFile(webPath + "/" + module + "." + mainContext //$NON-NLS-1$ //$NON-NLS-2$
				+ ".part", buffer.toString().getBytes("UTF-8")); //$NON-NLS-1$ //$NON-NLS-2$

	}
	public void importFuncNodeFromExcel(InputStream input) {
		funcNodeList = new ArrayList<FuncNodeObj>();
		funcCateList = new ArrayList<FuncCateObj>();
		funcCateMap = new HashMap<String, FuncCateObj>();
		funcNodeMap = new HashMap<String, FuncNodeObj>();
		try {
			XSSFWorkbook wb = new XSSFWorkbook(input);
			XSSFSheet sheet = wb.getSheetAt(2);
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
						case 0:
							if (cell != null && row.getCell(1)!=null) {
								funcCateObj.setBcp(cell.getStringCellValue());
							}
							else if(cell != null && row.getCell(2)!=null)
								funcNodeObj.setBcp(cell.getStringCellValue());
							break;
						case 1:
							if (cell != null && cell.getStringCellValue() != "") { //$NON-NLS-1$
								funcCateObj.setId(cell.getStringCellValue());
							}
							break;
						case 2:
							if (cell != null && cell.getStringCellValue() != "") { //$NON-NLS-1$
								funcNodeObj.setId(cell.getStringCellValue());
							}
							break;
						case 3:
							if (funcCateObj.getId() != null && cell != null ) {
								funcCateObj.setTitle(cell.getStringCellValue());
							} else if (funcNodeObj.getId() != null && cell != null) {
								funcNodeObj.setTitle(cell.getStringCellValue());
							}
							break;
						case 4:
							if (funcCateObj.getId() != null && cell != null ) {
								funcCateObj.setParentId(cell
										.getStringCellValue());
							} else if (funcNodeObj.getId() != null && cell != null) {
								funcNodeObj.setParentId(cell
										.getStringCellValue());
							}
							break;
					
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
			XSSFSheet sheet = wb.getSheetAt(3);
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
						if (k == 6 && cell != null&& menuCateObj.getMenuCode()!=null)
							 menuCateObj.setBcp(cell.getStringCellValue());
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
//	public void searchParentFunc(FuncCateObj funcCateObj) {
//		if (appsCategoryIdList.contains(funcCateObj.getId())) {
//			return;
//		} else {
//			String parentPK = null;
//			if (funcCateObj.getParentId() != null
//					&& funcCateObj.getParentId() != "") {
//				searchParentFunc(funcCateMap.get(funcCateObj.getParentId()));
//				CpAppsCategoryVO parentCateVO = LFWWfmConnector
//						.getAppsCategoryById(funcCateObj.getParentId());
//				if (parentCateVO != null) {
//					parentPK = parentCateVO.getPk_appscategory();
//				}
//			}
//
//			CpAppsCategoryVO selectedCateVO = new CpAppsCategoryVO();
//			selectedCateVO.setId(funcCateObj.getId());
//			selectedCateVO.setPk_parent(parentPK);
//			selectedCateVO.setPk_module(modulePK);
//			selectedCateVO.setTitle(funcCateObj.getTitle());
//			// 保存CpAppsCategoryVO对象
//			LFWWfmConnector.saveAppsCategory(selectedCateVO);
//			System.out.print("insert the apps cate");
//		}
//	}
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
//			menuCateId = parentCode;
//			CpMenuItemVO menuItemVO = new CpMenuItemVO();
//			String pk_menucategory = LFWWfmConnector.getMenuCategoryById(
//					menuCateId).getPk_menucategory();
//			pk_menuCate = pk_menucategory;
//			menuItemVO.setCode(code);
//			menuItemVO.setPk_menucategory(pk_menucategory);
//			menuItemVO.setName(menuItemObj.getTitle());
//			CpAppsNodeVO funcNode = LFWWfmConnector.getAppsNodeById(menuItemObj.getFuncCode());
//			if(funcNode!=null){
//				String pk_funnode = funcNode.getPk_appsnode();
//				menuItemVO.setPk_funnode(pk_funnode);
//			}			
//			LFWWfmConnector.saveMenuItem(menuItemVO);
			return;
		}
			else {
			CpMenuItemVO menuItemVO = new CpMenuItemVO();
			if (keys.contains(parentCode)) {
				searchParentMenu(menuItemMap.get(parentCode));
//				String newPk_parent = LFWWfmConnector.getMenuItemById(
//						parentCode).getPk_menuitem();
//				menuItemVO.setPk_parent(newPk_parent);
			} else
				menuItemVO.setPk_parent(null);

//			String pk_menucategory = null;
//			if(pk_menuCate==null)
//				pk_menucategory = LFWWfmConnector.getMenuCategoryById(
//					menuCateId).getPk_menucategory();
//			else pk_menucategory = pk_menuCate;
//			menuItemVO.setCode(code);
//			menuItemVO.setPk_menucategory(pk_menucategory);
//			menuItemVO.setName(menuItemObj.getTitle());
//			CpAppsNodeVO funcNode = LFWWfmConnector.getAppsNodeById(menuItemObj.getFuncCode());
//			if(funcNode!=null){
//				String pk_funnode = funcNode.getPk_appsnode();
//				menuItemVO.setPk_funnode(pk_funnode);
//			}	
//			LFWWfmConnector.saveMenuItem(menuItemVO);
//			System.out.println("insert menuItem " + menuItemObj.getTitle());
		}
	}
}
