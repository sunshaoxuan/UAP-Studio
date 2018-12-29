package nc.uap.lfw.perspective.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.cpb.org.vos.CpAppsCategoryVO;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.ctrl.tpl.print.base.CpPrintTemplateVO;
import nc.uap.ctrl.tpl.qry.base.CpQueryTemplateVO;
import nc.uap.lfw.aciton.NCConnector;
import nc.uap.lfw.application.LFWApplicationTreeItem;
import nc.uap.lfw.common.LFWUtility;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.dev.LfwComponent;
import nc.uap.lfw.core.dev.LfwDevBusiComponent;
import nc.uap.lfw.core.dev.LfwDevModel;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.ref.LfwRefInfoVO;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.design.itf.BusinessComponent;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.editor.LFWDomainTreeItem;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.webcomponent.LFWAppsCategoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWAppsNodeTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWBuildTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWBusinessCompnentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWComponentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDocTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWFuncTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMdDirTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMdFileTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMetadataTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWProjectTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWRefFolderTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWRefInfoTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWVirtualDirTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWfmCateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;
import nc.uap.lfw.publicview.LFWPublicViewTreeItem;
import nc.uap.lfw.ref.model.IConst;
import nc.uap.lfw.window.RefreshWindowNodeAction;
import nc.uap.wfm.vo.WfmFlwCatVO;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * 建立树的builder
 * 
 * @author zhangxya
 * 
 */
public class LFWExplorerTreeBuilder {

	private static LFWExplorerTreeBuilder instance = null;

	// private Map<String, Map<String, LfwWidget>> ctxWidgetMap = new
	// HashMap<String, Map<String, LfwWidget>>();

	public LFWExplorerTreeBuilder() {
		super();
		// instance = this;
	}

	public static LFWExplorerTreeBuilder getInstance() {
		synchronized (LFWExplorerTreeBuilder.class) {
			if (instance == null)
				instance = new LFWExplorerTreeBuilder();
		}
		return instance;
	}

	/**
	 * 构造树结构
	 * 
	 * @param tree
	 */
	public void buildTree(Tree tree) {
		tree.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {

			}

			public void mouseDown(MouseEvent e) {
				Tree t = (Tree) e.getSource();
				TreeItem[] items = t.getSelection();
				if (items != null && items.length > 0) {
					if (items[0] instanceof LFWProjectTreeItem) {
						LFWProjectTreeItem lfwItem = (LFWProjectTreeItem) items[0];
						IProject project = lfwItem.getProjectModel()
								.getJavaProject();
						LFWPersTool.setCurrentProject(project);
						String projectPath = project.getLocation().toString();
						LFWPersTool.setProjectPath(projectPath);
						LFWPersTool.setProjectWithBcpPath(LFWPersTool
								.getBcpPath(items[0]));
					} else if (items[0] instanceof LFWDirtoryTreeItem) {
						LFWDirtoryTreeItem lfwItem = (LFWDirtoryTreeItem) items[0];
						IProject project = getFileOwnProject(t,
								lfwItem.getFile());
						if(project != null){
							LFWPersTool.setCurrentProject(project);
							String projectPath = project.getLocation().toString();
							LFWPersTool.setProjectPath(projectPath);
							LFWPersTool.setProjectWithBcpPath(LFWPersTool
									.getBcpPath(items[0]));
						}
					}

					else if (items[0] instanceof LFWBusinessCompnentTreeItem) {
						LFWBusinessCompnentTreeItem lfwItem = (LFWBusinessCompnentTreeItem) items[0];
						IProject project = getFileOwnProject(t,
								lfwItem.getFile());
						LFWPersTool.setCurrentProject(project);
						LFWPersTool.setProjectWithBcpPath(LFWPersTool
								.getBcpPath(items[0]));

					} 
					else if (items[0] instanceof LFWBasicTreeItem) {
						
					}
					else {
						otherMouseDown(t, items[0]);
					}
				}
			}

			public void mouseUp(MouseEvent e) {

			}

		});
		new AMCThread().start();
		// initAllPublicViews();
		buildLFWProTree(tree);
		buildBcpProjTree(tree);
//		buildDomainDiskTree(tree);
		buildRefProjectTree(tree);
	}

	// private void initAllPublicViews() {
	// ctxWidgetMap = RefDatasetData.getPoolWidgets(null);
	// }

	protected void otherMouseDown(Tree tree, TreeItem item) {

	}
	
	public void buildDomainDiskTree(Tree tree){
		LFWDomainTreeItem domainTreeItem = new LFWDomainTreeItem(tree, SWT.NONE);
		domainTreeItem.setId("DomainView"); //$NON-NLS-1$
		domainTreeItem.setItemName("DomainView"); //$NON-NLS-1$
		domainTreeItem.setText(M_perspective.LFWExplorerTreeBuilder_5);
		domainTreeItem.setData("DomainView"); //$NON-NLS-1$
		IPath workpath = ResourcesPlugin.getWorkspace().getRoot().getLocation().append("domain"); //$NON-NLS-1$
		File domainFolder = workpath.toFile();
		if(domainFolder.exists()&&domainFolder.listFiles().length>0){
			for(File domain:domainFolder.listFiles()){
				File buildFile = new File(domain, "setup.ini"); //$NON-NLS-1$
				if(buildFile.exists()){
					LFWBuildTreeItem buildItem = new LFWBuildTreeItem(domainTreeItem,buildFile, domain.getName());
					buildItem.setType(LFWDirtoryTreeItem.DOMAIN_BUILD_CONFIG);
				}
			}
		}
//		File buildFile = new File(workpath.toFile(), "setup.ini");
//		if(buildFile.exists()){
//			LFWBuildTreeItem buildItem = new LFWBuildTreeItem(domainTreeItem,buildFile, buildFile.getName());
//			buildItem.setType(LFWDirtoryTreeItem.DOMAIN_BUILD_CONFIG);
//		}
	}
	public void buildRefProjectTree(Tree tree){
		String nchome = LfwCommonTool.getUapHome();
		File ncForder = new File(nchome);
		LFWDirtoryTreeItem refProjectItem = new LFWDirtoryTreeItem(tree, ncForder);
		refProjectItem.setType(LFWDirtoryTreeItem.PROJECTS_FOLDER);
		refProjectItem.setText(M_perspective.LFWExplorerTreeBuilder_6);
		initRefProjTree(refProjectItem);
	}
	
	public void initRefProjTree(LFWBasicTreeItem parent) {
		File tempFolder = parent.getFile();
		if (parent.getItemCount() == 0) {			
			LFWDirtoryTreeItem initNode = new LFWDirtoryTreeItem(parent,
					tempFolder, M_perspective.LFWExplorerTreeBuilder_0);
			initNode.setType("LOAD"); //$NON-NLS-1$
			return;
		}
		parent.removeAll();
		LFWDirtoryTreeItem lfwItem = new LFWDirtoryTreeItem(parent, parent.getData(),"lfw"); //$NON-NLS-1$
		Map<String, LfwDevModel> lfwDevModelMap = LFWAMCConnector.getLfwDevModelCache("/lfw"); //$NON-NLS-1$
//		lfwDevModelMap
		IProject[] projects = LfwCommonTool.getAllOpenedJavaProjects();
		List<String> moduleList = new ArrayList<String>();
		for(IProject proj: projects){
			String moduleName = LfwCommonTool.getProjectModuleName(proj);
			moduleList.add(moduleName);
		}
		if(lfwDevModelMap!=null){
			Iterator<LfwDevModel> devModel = lfwDevModelMap.values().iterator();
			while(devModel.hasNext()){
				LfwDevModel model = devModel.next();
				String moduleId = model.getModuleId();
				if(moduleList.contains(moduleId))
					continue;
				LFWDirtoryTreeItem moduleItem = new LFWDirtoryTreeItem(lfwItem, lfwItem.getData(),M_perspective.LFWExplorerTreeBuilder_7+ moduleId);
				Map<String, LfwDevBusiComponent> bcpMap = model.getLfwdevBusinessComMap();
				Iterator<LfwDevBusiComponent> bcpIter = bcpMap.values().iterator();
				
				while(bcpIter.hasNext()){
					LfwDevBusiComponent bcp = bcpIter.next();
					String bcpId = bcp.getBusiComponentId();
					LFWDirtoryTreeItem bcpItem = new LFWDirtoryTreeItem(moduleItem, moduleItem.getData(),M_perspective.LFWExplorerTreeBuilder_8+ bcpId);
					LFWDirtoryTreeItem windowItem = new LFWDirtoryTreeItem(bcpItem, moduleItem.getData(),"Window"); //$NON-NLS-1$
					LFWDirtoryTreeItem viewItem = new LFWDirtoryTreeItem(bcpItem, moduleItem.getData(),M_perspective.LFWExplorerTreeBuilder_9);
					initComponentNodeTree(windowItem, moduleId, bcpId,"/lfw"); //$NON-NLS-1$
					initComponentViewTree(viewItem, moduleId, bcpId, "/lfw"); //$NON-NLS-1$
				}
				
			}
		}
		LFWDirtoryTreeItem portalItem = new LFWDirtoryTreeItem(parent, parent.getData(),"portal"); //$NON-NLS-1$
		Map<String, LfwDevModel> portalDevModelMap = LFWAMCConnector.getLfwDevModelCache("/portal"); //$NON-NLS-1$
		if(portalDevModelMap!=null){
			Iterator<LfwDevModel> devModel = portalDevModelMap.values().iterator();
			while(devModel.hasNext()){
				LfwDevModel model = devModel.next();
				String moduleId = model.getModuleId();
				if(moduleList.contains(moduleId))
					continue;
				LFWDirtoryTreeItem moduleItem = new LFWDirtoryTreeItem(portalItem, lfwItem.getData(),M_perspective.LFWExplorerTreeBuilder_7+ moduleId);
				Map<String, LfwDevBusiComponent> bcpMap = model.getLfwdevBusinessComMap();
				Iterator<LfwDevBusiComponent> bcpIter = bcpMap.values().iterator();				
				while(bcpIter.hasNext()){
					LfwDevBusiComponent bcp = bcpIter.next();
					String bcpId = bcp.getBusiComponentId();
					File preFile = new File(parent.getFile(),"/hotwebs/portal/sync/"+moduleId+"/"+bcpId+"/html/");
					LFWDirtoryTreeItem bcpItem = new LFWDirtoryTreeItem(moduleItem, preFile,M_perspective.LFWExplorerTreeBuilder_8+ bcpId);
					LFWDirtoryTreeItem windowItem = new LFWDirtoryTreeItem(bcpItem, new File(preFile,"nodes"),"Window"); //$NON-NLS-1$
					LFWDirtoryTreeItem viewItem = new LFWDirtoryTreeItem(bcpItem, new File(preFile,"views"),M_perspective.LFWExplorerTreeBuilder_9);
					initComponentNodeTree(windowItem, moduleId, bcpId,"/portal"); //$NON-NLS-1$
					initComponentViewTree(viewItem, moduleId, bcpId, "/portal"); //$NON-NLS-1$
				}
				
			}
		}
	}


	/**
	 * 创建lfw工程的树
	 * 
	 * @param tree
	 */
	private void buildLFWProTree(Tree tree) {
		IProject[] projects = getOpenedLFWNotBCPJavaProjects();
		// 将项目按照名称排序
		for (int i = 0; i < projects.length; i++) {
			for (int j = projects.length - 1; j > i; j--) {
				if (projects[i].getName().compareTo(projects[j].getName()) > 0) {
					IProject temp = projects[i];
					projects[i] = projects[j];
					projects[j] = temp;
				}
			}
		}
		String[] projPaths = new String[projects.length];
		for (int i = 0; i < projPaths.length; i++) {
			projPaths[i] = projects[i].getLocation().toString();
		}

		int count = projects == null ? 0 : projects.length;
		for (int i = 0; i < count; i++) {

			IProject project = projects[i];
			String projPath = projPaths[i];

			Map<String, String> pageName = null;
			Map<String, String> appName = null;
			Map<String, String> windowName = null;
			Map<String, String> publicViewName = null;

			LFWProjectModel model = new LFWProjectModel(project);
			LFWProjectTreeItem ti = new LFWProjectTreeItem(tree, model);
//			buildAppendTree(ti, project);

			initDocItem(ti);
//			initCommonProjectTree(project, projPath, pageName, ti, appName,
//					windowName, publicViewName, null);
			// initSRCTtree(project);
//			initBuildConfigItem(ti);
			
		}

	}

	protected IProject[] getOpenedLFWNotBCPJavaProjects() {
		return LFWPersTool.getOpenedLFWNotBCPJavaProjects();
	}

	/**
	 * 创建bcp工程的树结构
	 * 
	 * @param tree
	 */

	private void buildBcpProjTree(Tree tree) {
		try {
			// 得到所有的bcp工程
			IProject[] projects = getOpenedBcpJavaProjects();
			if (projects == null || projects.length == 0)
				return;
			// 将项目按照名称排序
			for (int i = 0; i < projects.length; i++) {
				for (int j = projects.length - 1; j > i; j--) {
					if (projects[i].getName().compareTo(projects[j].getName()) > 0) {
						IProject temp = projects[i];
						projects[i] = projects[j];
						projects[j] = temp;
					}
				}
			}

			List<String> projectPathList = new ArrayList<String>();
			for (int i = 0; i < projects.length; i++) {
				String projPath = projects[i].getLocation().toString();
				MainPlugin.getDefault().logInfo("从" + projPath + "下查询bcp配置"); //$NON-NLS-1$ //$NON-NLS-2$
				// BCPManifest manifest = NCConnector.getBCPManifest(projPath
				// + "/manifest.xml");
				String[] bcpNames = LfwCommonTool.getBCPNames(projects[i]);
				if (bcpNames == null) {
					MainPlugin.getDefault().logError("BCP解析异常,路径:" + projPath); //$NON-NLS-1$
					continue;
				}
				// List componentsList = manifest.getBusinessComponentList();
				for (int j = 0; j < bcpNames.length; j++) {
					BusinessComponent businessC = new BusinessComponent();
					businessC.setDispname(bcpNames[j]);
					businessC.setName(bcpNames[j]);
					String busiComName = businessC.getName();
					projectPathList.add(projPath + "/" + busiComName); //$NON-NLS-1$
				}
			}

			if (projectPathList.size() == 0)
				return;

			String[] projPaths = (String[]) projectPathList
					.toArray(new String[0]);
			Set<String> ctxSet = new HashSet<String>();
			
			for (int i = 0; i < projects.length; i++) {
				IProject project = projects[i];
				String ctx = LFWUtility.getContextFromResource(project);
				ctxSet.add("/" + ctx); //$NON-NLS-1$
				LFWProjectModel model = new LFWProjectModel(project);
				LFWProjectTreeItem ti = new LFWProjectTreeItem(tree, model);
				String projPath = project.getLocation().toString();
				MainPlugin.getDefault().logInfo("从" + projPath + "下查询bcp配置"); //$NON-NLS-1$ //$NON-NLS-2$
				// BCPManifest manifest = NCConnector.getBCPManifest(projPath
				// + "/manifest.xml");
				String[] bcpNames = LfwCommonTool.getBCPNames(project);
				Map<String,String> displayMap = LfwCommonTool.getBCPDisplayNames(project);
				if (bcpNames == null) {
					MainPlugin.getDefault().logError("BCP解析异常,路径:" + projPath); //$NON-NLS-1$
					continue;
				}
				// List componentsList = manifest.getBusinessComponentList();
				initDocItem(ti);
				for (int j = 0; j < bcpNames.length; j++) {
					// for (int j = 0; j < componentsList.size(); j++) {
					BusinessComponent businessC = new BusinessComponent();
					businessC.setDispname(displayMap.get(bcpNames[j]));
					businessC.setName(bcpNames[j]);
					String bcpTitle = businessC.getDispname();
					String busiComName = businessC.getName();
					Map<String, String> pageName = null;

					// PageFlow[] pageflow = null;
					// 组件的项目
					File file = ti.getFile();
					File busiFile = new File(file, busiComName);
					LFWBusinessCompnentTreeItem busiComTreeItem = new LFWBusinessCompnentTreeItem(
							ti, busiFile, bcpTitle+"["+busiComName+"]"); //$NON-NLS-1$ //$NON-NLS-2$

					String busiCompPath = projPath + "/" + busiComName; //$NON-NLS-1$

//					buildAppendTree(busiComTreeItem, project);

					Map<String, String> appName = null;

					Map<String, String> windowName = null;

					Map<String, String> publicViewName = null;

					initCommonProjectTree(project, busiCompPath, pageName,
							busiComTreeItem, appName, windowName,
							publicViewName, businessC);

				}
//				initBuildConfigItem(ti);
				
			}

		} catch (Throwable e) {
			MainPlugin.getDefault().logError(e);
		}
	}

	private void initCommonProjectTree(IProject project, String projPath,
			Map<String, String> pageName, LFWBasicTreeItem busiComTreeItem,
			Map<String, String> appName, Map<String, String> windowName,
			Map<String, String> publicViewName, BusinessComponent bcp) {
		File rootFile = busiComTreeItem.getFile();
		
		File file = new File(rootFile, WEBPersConstants.AMC_DOCNODE_PATH);
		LFWDirtoryTreeItem docNode = new LFWDirtoryTreeItem(busiComTreeItem,
				file, WEBPersConstants.DOC);
		docNode.setType(LFWDirtoryTreeItem.DOC_FOLDER);
		
		refreshDocFolder(docNode);
		
		
		file = new File(rootFile, WEBPersConstants.AMC_MDNODE_PATH);
		LFWMdDirTreeItem mdNode = new LFWMdDirTreeItem(busiComTreeItem,
				file, WEBPersConstants.METADATAS);
		mdNode.setType(LFWDirtoryTreeItem.METADATA_FOLDER);
		
		file = new File(rootFile, WEBPersConstants.AMC_SRC_PATH);
		LFWDirtoryTreeItem srcNode = new LFWDirtoryTreeItem(busiComTreeItem,
				file, WEBPersConstants.SRC);
		srcNode.setType(LFWDirtoryTreeItem.SRC_FOLDER);
		srcNode.setBcp(bcp);

		file = new File(rootFile, WEBPersConstants.AMC_BASE_NODEGROUP_PATH);
		LFWDirtoryTreeItem uiNode = new LFWDirtoryTreeItem(busiComTreeItem,
				file, WEBPersConstants.UIS);
		uiNode.setType(LFWDirtoryTreeItem.UIS_FOLDER);
		uiNode.setBcp(bcp);


		file = new File(rootFile, WEBPersConstants.AMC_FUNCNODE_PATH);
		LFWDirtoryTreeItem funcNode = new LFWDirtoryTreeItem(busiComTreeItem,
				file, WEBPersConstants.FUNC);
		funcNode.setType(LFWDirtoryTreeItem.FUNC_FOLDER);
		funcNode.setBcp(bcp);
		
		file = new File(rootFile, WEBPersConstants.AMC_WFMNODE_PATH);
		LFWDirtoryTreeItem wfmNode = new LFWDirtoryTreeItem(busiComTreeItem,
				file, WEBPersConstants.WFM);
		wfmNode.setType(LFWDirtoryTreeItem.WFM_FOLDER);
		wfmNode.setBcp(bcp);

		
		LFWDirtoryTreeItem templateNode = new LFWDirtoryTreeItem(busiComTreeItem,
				file, WEBPersConstants.TEMPLATE);
		templateNode.setType(LFWDirtoryTreeItem.TEMPLATE_FOLDER);
		
		file = new File(rootFile, WEBPersConstants.AMC_QUERYNODE_PATH);
		LFWDirtoryTreeItem queryNode = new LFWDirtoryTreeItem(templateNode,
				file, WEBPersConstants.QUERY);
		queryNode.setType(LFWDirtoryTreeItem.QUERY_FOLDER);
		queryNode.setBcp(bcp);

		file = new File(rootFile, WEBPersConstants.AMC_PRINTNODE_PATH);
		LFWDirtoryTreeItem printNode = new LFWDirtoryTreeItem(templateNode,
				file, WEBPersConstants.PRINT);
		printNode.setType(LFWDirtoryTreeItem.PRINT_FOLDER);
		printNode.setBcp(bcp);

		
				
		LFWDirtoryTreeItem langNode = new LFWDirtoryTreeItem(busiComTreeItem,
				rootFile, WEBPersConstants.LANG);
		langNode.setType(LFWDirtoryTreeItem.MLR_FOLDER);
		
		file = new File(rootFile, "/web/html");  //$NON-NLS-1$
		LFWDirtoryTreeItem frontlangNode = new LFWDirtoryTreeItem(langNode,
				file, WEBPersConstants.FRONTLANG);
		frontlangNode.setType(LFWDirtoryTreeItem.LANG_FOLDER);
		frontlangNode.setBcp(bcp);
		
		file = new File(rootFile, "/src"); //$NON-NLS-1$
		LFWDirtoryTreeItem javalangNode = new LFWDirtoryTreeItem(langNode,
				file, WEBPersConstants.JAVALANG);
		javalangNode.setType(LFWDirtoryTreeItem.JAVALANG_FOLDER);
		javalangNode.setBcp(bcp);
		
//		String bcpName = (bcp==null)?"":bcp.getName();
//		file = new File(rootFile, "/src");
//		LFWDirtoryTreeItem javalangNode = new LFWDirtoryTreeItem(busiComTreeItem,
//				file, WEBProjConstants.JAVALANG);
//		javalangNode.setType(LFWDirtoryTreeItem.JAVALANG_FOLDER);
//		javalangNode.setBcp(bcp);
		LFWDirtoryTreeItem configNode = new LFWDirtoryTreeItem(busiComTreeItem, rootFile,WEBPersConstants.CONFIG);
		configNode.setType(LFWDirtoryTreeItem.CONFIG_FOLDER);
		file = new File(rootFile, WEBPersConstants.AMC_SERVICENODE_PATH);
		LFWDirtoryTreeItem serviceNode = new LFWDirtoryTreeItem(
				configNode, file, WEBPersConstants.SERVICE);
		serviceNode.setType(LFWDirtoryTreeItem.SERVICE_FOLDER);

		buildAppendTree(busiComTreeItem, project);
		
		file = new File(rootFile, WEBPersConstants.AMC_BUILDNODE_PATH);
		LFWDirtoryTreeItem buildNode = new LFWDirtoryTreeItem(configNode,
				file, WEBPersConstants.BUILD);
		buildNode.setType(LFWDirtoryTreeItem.BUILD_FOLDER);
		
		initAMCProjectTree(uiNode, projPath, pageName, appName, windowName,
				publicViewName, project, bcp);
		initMdTree(mdNode, projPath, pageName, appName, windowName,
				publicViewName, project, bcp);

		initFuncTree(funcNode, project, bcp);
		initWfmTree(wfmNode, project, bcp);
		initQryTempTree(queryNode, project, bcp);
		initPrintTempTree(printNode, project, bcp);
		initLangTree(frontlangNode, projPath, pageName, appName, windowName,
				publicViewName, project, bcp);
		initServConfTree(serviceNode, projPath, pageName, appName, windowName,
				publicViewName, project, bcp);
		initBuildTree(buildNode, projPath, pageName, appName, windowName,
				publicViewName, project, bcp);
	}

	private void initBuildConfigItem(LFWBasicTreeItem projectItem) {
		File file = projectItem.getFile();
		LFWDirtoryTreeItem buildNode = new LFWDirtoryTreeItem(projectItem,
				file, M_perspective.LFWExplorerTreeBuilder_1);
		buildNode.setType(LFWDirtoryTreeItem.BUILDDISK_FOLDER);
		refreshBuilderFolder(buildNode);
	}
	private void initDocItem(LFWBasicTreeItem projectItem){
		File file = new File(projectItem.getFile(),"doc"); //$NON-NLS-1$
		LFWDirtoryTreeItem docNode = new LFWDirtoryTreeItem(projectItem,
				file, M_perspective.LFWExplorerTreeBuilder_2);
		docNode.setType(LFWDirtoryTreeItem.DOC_FOLDER);
		File subFolder = new File(file, "/design"); //$NON-NLS-1$
		LFWDirtoryTreeItem disigngNode = new LFWDirtoryTreeItem(docNode,
				subFolder, M_perspective.LFWExplorerTreeBuilder_3);
		disigngNode.setType(LFWDirtoryTreeItem.DOC_FOLDER);
		subFolder = new File(file, "/other"); //$NON-NLS-1$
		LFWDirtoryTreeItem otherDocNode = new LFWDirtoryTreeItem(docNode,
				subFolder, M_perspective.LFWExplorerTreeBuilder_4);
		otherDocNode.setType(LFWDirtoryTreeItem.DOC_FOLDER);
		refreshDocFolder(docNode);
	}

	protected IProject[] getOpenedBcpJavaProjects() {
		return LFWPersTool.getOpenedBcpJavaProjects();
	}

	private void initBuildTree(LFWBasicTreeItem projectRoot,
			String projectPath, Map<String, String> pageNames,
			Map<String, String> appNames, Map<String, String> windowNames,
			Map<String, String> publicViewName, IProject project,
			BusinessComponent bcp) {
		File file = projectRoot.getFile();
		// 初始化PDM节点
		File pdmFile = new File(file, WEBPersConstants.AMC_PDM_PATH);
		LFWDirtoryTreeItem pdmNode = new LFWDirtoryTreeItem(projectRoot,
				pdmFile, WEBPersConstants.PDM);
		pdmNode.setType(LFWDirtoryTreeItem.PDM_FOLDER);
		getFilesFromItem(pdmNode, project);

		// 初始化script节点
		File scriptFile = new File(file, WEBPersConstants.AMC_SCRIPT_PATH);
		LFWDirtoryTreeItem scriptNode = new LFWDirtoryTreeItem(projectRoot,
				scriptFile, WEBPersConstants.SCRIPT);
		scriptNode.setType(LFWDirtoryTreeItem.SCRIPT_FOLDER);
		getFilesFromItem(scriptNode, project);
		
//		File scriptFolder = new File(file, "/build/script"); //$NON-NLS-1$
//		LFWDirtoryTreeItem scriptfileNode = new LFWDirtoryTreeItem(scriptNode,scriptFolder, "script");
//		scriptfileNode.setType(LFWDirtoryTreeItem.SCRIPT_FOLDER);
		
		// 初始化目录映射节点
		// File mapFile = new File(file,
		// WEBProjConstants.AMC_PDM_PATH);
		File mapFile = file;
		LFWDirtoryTreeItem mapNode = new LFWDirtoryTreeItem(projectRoot,
				mapFile, WEBPersConstants.MODULEMAPPING);
		mapNode.setType(LFWDirtoryTreeItem.MAPPING_FOLDER);
		getFilesFromItem(mapNode, project);
//		File buildFolder = new File(file, "build"); //$NON-NLS-1$
//		LFWDirtoryTreeItem mapfileNode = new LFWDirtoryTreeItem(mapNode,buildFolder, "");
//		mapfileNode.setType(LFWDirtoryTreeItem.MAPPING_FOLDER);

		// 构造安装盘
//		File buildFolder = new File(file, WEBProjConstants.AMC_CONF_PATH);
//		LFWDirtoryTreeItem buildNode = new LFWDirtoryTreeItem(projectRoot,
//				buildFolder, WEBProjConstants.BUILDDISK);
//		buildNode.setType(LFWDirtoryTreeItem.BUILDDISK_FOLDER);
//		refreshBuilderFolder(buildNode, project);

	}

	private void refreshBuilderFolder(LFWBasicTreeItem parentNode) {
		File buildFolder = parentNode.getFile();
		if (buildFolder.exists()) {
			File buildFile = new File(buildFolder,"setup.ini"); //$NON-NLS-1$
			if (buildFile != null&&buildFile.exists()) {
				LFWBuildTreeItem buildItem = new LFWBuildTreeItem(parentNode,
						buildFile, buildFile.getName());
			}
		}
	}
	private void refreshDocFolder(LFWBasicTreeItem parentNode){
		File docFolder = parentNode.getFile();
		File designFolder = new File(docFolder,"design"); //$NON-NLS-1$
		File otherFolder = new File(docFolder,"other"); //$NON-NLS-1$
		if(designFolder.exists()){
			for(File file:designFolder.listFiles()){
				LFWDocTreeItem docItem = new LFWDocTreeItem(parentNode.getItem(0),
						file, file.getName());
			}
		}
		if(otherFolder.exists()){
			for(File file:otherFolder.listFiles()){
				LFWDocTreeItem docItem = new LFWDocTreeItem(parentNode.getItem(1),
						file, file.getName());
			}
		}
		if(docFolder.exists()&&(!designFolder.exists())){
			for(File file:docFolder.listFiles()){
				if(file.isFile()){
					LFWDocTreeItem docItem = new LFWDocTreeItem(parentNode,
							file, file.getName());
				}
			}
		}
	}

	private void getFilesFromItem(LFWBasicTreeItem parentNode, IProject project) {
		String module = LfwCommonTool.getProjectModuleName(project);
		File folder = parentNode.getFile();
		File[] files = folder.listFiles();
		if (files != null) {
			for (File ifile : files) {
				if (ifile.isFile()) {
					LFWMetadataTreeItem servdata = new LFWMetadataTreeItem(
							parentNode, ifile, ifile.getName());
					IFile file = project.getFile(ifile.getPath().substring(
							project.getLocation().toString().length()));
					servdata.setMdFile(file);

				}
			}
		}
	}

	private void initServConfTree(LFWBasicTreeItem projectRoot,
			String projectPath, Map<String, String> pageNames,
			Map<String, String> appNames, Map<String, String> windowNames,
			Map<String, String> publicViewName, IProject project,
			BusinessComponent bcp) {
		// 服务配置节点
		File folder = projectRoot.getFile();

		if (folder.exists()) {
			File[] files = folder.listFiles();
			if (files != null && files.length!=0) {
				for (File ifile : files) {
					if (ifile.isFile()
							&& ifile.getName()
									.substring(
											ifile.getName().lastIndexOf(".") + 1) //$NON-NLS-1$
									.equalsIgnoreCase(
											WEBPersConstants.AMC_SERVICE_FILENAME)) {
						LFWMetadataTreeItem servdata = new LFWMetadataTreeItem(
								projectRoot, ifile, ifile.getName());
						IFile mdfile = project.getFile(ifile.getPath()
								.substring(
										project.getLocation().toString()
										.length()));
						servdata.setMdFile(mdfile);
					}
				}
			}
		}
	}

	private Map<String, CpAppsCategoryVO> appsMap = new HashMap();
	private Map<String, LFWAppsCategoryTreeItem> existMap = new HashMap();
	private LFWBasicTreeItem tempRoot = null;

	public void initQryTempTree(LFWBasicTreeItem projectRoot, IProject project,
			BusinessComponent bcp) {
		File tempFolder = projectRoot.getFile();
		if (projectRoot.getItemCount() == 0) {
			
			LFWDirtoryTreeItem initNode = new LFWDirtoryTreeItem(projectRoot,
					tempFolder, M_perspective.LFWExplorerTreeBuilder_0);
			initNode.setType("LOAD"); //$NON-NLS-1$
			return;
		}
		projectRoot.removeAll();
		CpAppsNodeVO[] funcNodes = LFWWfmConnector.getAppsNodeVOsByCondition("devcomponent = '"+bcp.getName()+"'"); //$NON-NLS-1$ //$NON-NLS-2$
		if(funcNodes!=null){
			for(CpAppsNodeVO funcNode:funcNodes){
				LFWFuncTreeItem funcItem = new LFWFuncTreeItem(projectRoot, tempFolder, bcp,
						funcNode.getTitle(), "query"); //$NON-NLS-1$
				String nodeCode = funcNode.getId();
				funcItem.setNodecode(nodeCode);
				CpQueryTemplateVO[] queryTpls = LFWWfmConnector.getQueryTplByCondition("nodecode = '"+nodeCode+"'"); //$NON-NLS-1$ //$NON-NLS-2$
				if(queryTpls!=null){
					for(CpQueryTemplateVO qryTemp:queryTpls){
						LFWAppsNodeTreeItem appsNode = new LFWAppsNodeTreeItem(funcItem, tempFolder, qryTemp.getModelname(),qryTemp.getPk_query_template());
						appsNode.setNodecode(nodeCode);
						appsNode.setMetaclass(qryTemp.getMetaclass());
						appsNode.setType(((LFWDirtoryTreeItem) projectRoot).getType());
					}
				}
			}
		}
//		if(tempFolder.exists()){
//			File[] apps = tempFolder.listFiles();
//			if(apps.length>0){
//				for(File app:apps){
//					LFWAppsNodeTreeItem appsNode = new LFWAppsNodeTreeItem(projectRoot, app, app.getName(),null);
//					appsNode.setType(((LFWDirtoryTreeItem) projectRoot).getType());
//				}
//			}
//		}
//		else{
//			LFWDirtoryTreeItem initNode = new LFWDirtoryTreeItem(projectRoot,
//					tempFolder, "暂无可用模板");
//			initNode.setType("LOAD");
//		}
		
//		getBcpAppNodes(projectRoot, project, bcp);
	}
	
	public void initPrintTempTree(LFWBasicTreeItem projectRoot, IProject project,
			BusinessComponent bcp) {
		File tempFolder = projectRoot.getFile();
		if (projectRoot.getItemCount() == 0) {
			
			LFWDirtoryTreeItem initNode = new LFWDirtoryTreeItem(projectRoot,
					tempFolder, M_perspective.LFWExplorerTreeBuilder_0);
			initNode.setType("LOAD"); //$NON-NLS-1$
			return;
		}
		projectRoot.removeAll();
		CpAppsNodeVO[] funcNodes = LFWWfmConnector.getAppsNodeVOsByCondition("devcomponent = '"+bcp.getName()+"'"); //$NON-NLS-1$ //$NON-NLS-2$
		if(funcNodes!=null){
			for(CpAppsNodeVO funcNode:funcNodes){
				LFWFuncTreeItem funcItem = new LFWFuncTreeItem(projectRoot, tempFolder, bcp,
						funcNode.getTitle(), "print"); //$NON-NLS-1$
				String nodeCode = funcNode.getId();
				funcItem.setNodecode(nodeCode);
				CpPrintTemplateVO[] printTpls = LFWWfmConnector.getPrintTplByCondition("nodecode = '"+nodeCode+"'"); //$NON-NLS-1$ //$NON-NLS-2$
				if(printTpls!=null){
					for(CpPrintTemplateVO printTemp:printTpls){
						LFWAppsNodeTreeItem appsNode = new LFWAppsNodeTreeItem(funcItem, tempFolder, printTemp.getModelname(),printTemp.getPk_print_template());
						appsNode.setNodecode(nodeCode);
						appsNode.setMetaclass(printTemp.getMetaclass());
						appsNode.setType(((LFWDirtoryTreeItem) projectRoot).getType());
					}
				}
			}
		}
	}

	/**
	 * @param projectRoot
	 * @param project
	 * @param bcp
	 */
	private void getBcpAppNodes(LFWBasicTreeItem projectRoot, IProject project,
			BusinessComponent bcp) {
		tempRoot = projectRoot;
		appsMap.clear();
		existMap.clear();

		File file = projectRoot.getFile();

		String module = LfwCommonTool.getProjectModuleName(project);

		// CpAppsCategoryVO[] categorys = LFWWfmConnector.getAppsCategory();
		CpAppsCategoryVO[] categorys = LFWWfmConnector
				.getAppsCategoryByDevModule(module);

		if (categorys != null) {
			for (CpAppsCategoryVO category : categorys) {
				appsMap.put(category.getPk_appscategory(), category);
			}
			for (CpAppsCategoryVO category : categorys) {
				if (category.getPk_parent() == null
						|| category.getPk_parent() == ""||appsMap.get(category.getPk_parent())==null) { //$NON-NLS-1$
					if (!existMap.containsKey(category.getPk_appscategory())) {
						LFWAppsCategoryTreeItem appsCate = new LFWAppsCategoryTreeItem(
								projectRoot, file, category.getTitle(),
								category.getPk_appscategory());
						existMap.put(category.getPk_appscategory(), appsCate);
					}
				} else {
					if (!existMap.containsKey(category.getPk_appscategory())) {
						LFWAppsCategoryTreeItem parentItem = searchParent(category);
						LFWAppsCategoryTreeItem appsCate = new LFWAppsCategoryTreeItem(
								parentItem, parentItem.getFile(),
								category.getTitle(),
								category.getPk_appscategory());
						existMap.put(category.getPk_appscategory(), appsCate);
					}
				}
			}
		}
		// CpAppsNodeVO[] nodes = LFWWfmConnector.getAllAppsNodes();
		CpAppsNodeVO[] nodes = null;
		if (bcp != null)
			nodes = LFWWfmConnector.getNodeByModuleandComponent(module,
					bcp.getName());
		else
			nodes = LFWWfmConnector.getNodeByModuleandComponent(module, null);
		if (nodes != null) {
			String type = ((LFWDirtoryTreeItem) projectRoot).getType();
			for (CpAppsNodeVO node : nodes) {
				String parent_pk = node.getPk_appscategory();
				if (existMap.get(parent_pk) != null) {
					LFWAppsNodeTreeItem appsNode = new LFWAppsNodeTreeItem(
							existMap.get(parent_pk), existMap.get(parent_pk)
									.getFile(), node.getTitle(),
							node.getPk_appsnode());
					appsNode.setType(type);
				} else {
					LFWAppsNodeTreeItem appsNode = new LFWAppsNodeTreeItem(
							projectRoot, file, node.getTitle(),
							node.getPk_appsnode());
					appsNode.setType(type);
				}

			}
		}
	}

	private LFWAppsCategoryTreeItem searchParent(CpAppsCategoryVO categoryVO) {
		if (categoryVO.getPk_parent() == null
				|| categoryVO.getPk_parent() == "" //$NON-NLS-1$
				|| existMap.containsKey(categoryVO.getPk_parent())) {
			if (categoryVO.getPk_parent() == null
					|| categoryVO.getPk_parent() == "") { //$NON-NLS-1$
				return null;
			} else
				return existMap.get(categoryVO.getPk_parent());
		} else {
			CpAppsCategoryVO parentVO = appsMap.get(categoryVO.getPk_parent());
			LFWAppsCategoryTreeItem parent = searchParent(parentVO);
			if (parent == null) {
				LFWAppsCategoryTreeItem appsCate = new LFWAppsCategoryTreeItem(
						tempRoot, tempRoot.getFile(), parentVO.getTitle(),
						parentVO.getPk_appscategory());
				existMap.put(parentVO.getPk_appscategory(), appsCate);
				return appsCate;
			} else {
				LFWAppsCategoryTreeItem appsCate = new LFWAppsCategoryTreeItem(
						parent, parent.getFile(), parentVO.getTitle(),
						parentVO.getPk_appscategory());
				existMap.put(parentVO.getPk_appscategory(), appsCate);
				return appsCate;
			}
		}
	}

	public void initMdTree(LFWBasicTreeItem projectRoot, String projectPath,
			Map<String, String> pageNames, Map<String, String> appNames,
			Map<String, String> windowNames,
			Map<String, String> publicViewName, IProject project,
			BusinessComponent bcp) {
//		File file = projectRoot.getFile();
		// 初始化元数据节点
//		File mdFolder = new File(file, WEBProjConstants.AMC_MDNODE_PATH);
		File mdFolder = projectRoot.getFile();
		// LFWDirtoryTreeItem applicationNode = new LFWDirtoryTreeItem(
		// projectRoot, mdFolder, WEBProjConstants.METADATA);
		// applicationNode.setType(LFWDirtoryTreeItem.METADATA_FOLDER);
		File[] children = mdFolder.listFiles();
		if (children != null) {
			for (int j = 0; j < children.length; j++) {
				if (children[j].isDirectory()) {
					LFWMdDirTreeItem mdNode = new LFWMdDirTreeItem(
							projectRoot, children[j], children[j].getName());
					mdNode.setType(LFWDirtoryTreeItem.METADATA_FOLDER);
					scanMdDir(mdNode, children[j], LFWDirtoryTreeItem.METADATA,
							WEBPersConstants.AMC_MD_FILENAME, project, bcp);
				} else if (children[j].isFile()) {
					if (children[j]
							.getName()
							.substring(
									children[j].getName().lastIndexOf(".") + 1) //$NON-NLS-1$
							.equalsIgnoreCase(WEBPersConstants.AMC_MD_FILENAME)) {
						LFWMdFileTreeItem metadata = new LFWMdFileTreeItem(
								projectRoot, children[j], children[j].getName());
						IFile mdfile = project.getFile(children[j].getPath()
								.substring(
										project.getLocation().toString()
												.length()));
						metadata.setMdFile(mdfile);
						metadata.setType(WEBPersConstants.AMC_MD_FILENAME);
					}
					if (children[j]
									.getName()
									.substring(
											children[j].getName().lastIndexOf(".") + 1) //$NON-NLS-1$
									.equalsIgnoreCase(WEBPersConstants.AMC_BPF_FILENAME)) {
						LFWMdFileTreeItem metadata = new LFWMdFileTreeItem(
								projectRoot, children[j], children[j].getName());
						IFile mdfile = project.getFile(children[j].getPath()
								.substring(
										project.getLocation().toString()
												.length()));
						metadata.setMdFile(mdfile);
						metadata.setType(WEBPersConstants.AMC_BPF_FILENAME);
					}
				}
			}
		}
		// refreshAMCNodeSubTree(applicationNode, appNames,
		// LFWDirtoryTreeItem.APPLICATION,
		// WEBProjConstants.AMC_APPLICATION_FILENAME, project);
	}

	private void scanMdDir(TreeItem parent, File dir, String itemType,
			String fileName, IProject project, BusinessComponent bcp) {
		File[] files = dir.listFiles();
		if (files == null)
			return;
		else {
			for (File file : files) {
				if (file.isDirectory()) {
					LFWMdDirTreeItem mdNode = new LFWMdDirTreeItem(parent,
							file, file.getName());
					mdNode.setType(LFWDirtoryTreeItem.METADATA_FOLDER);
					scanMdDir(mdNode, file, LFWDirtoryTreeItem.METADATA,
							WEBPersConstants.AMC_MD_FILENAME, project, bcp);
				} else if (file.isFile()) {
					if (file.getName()
							.substring(file.getName().lastIndexOf(".") + 1) //$NON-NLS-1$
							.equalsIgnoreCase(WEBPersConstants.AMC_MD_FILENAME)) {
						LFWMdFileTreeItem metadata = new LFWMdFileTreeItem(
								parent, file, file.getName());
						
						IFile mdfile = project.getFile(file.getPath()
								.substring(
										project.getLocation().toString()
												.length()));
						metadata.setMdFile(mdfile);
						metadata.setType(WEBPersConstants.AMC_MD_FILENAME);
					}
					else if(file.getName()
							.substring(file.getName().lastIndexOf(".") + 1) //$NON-NLS-1$
							.equalsIgnoreCase(WEBPersConstants.AMC_BPF_FILENAME)){
						LFWMdFileTreeItem metadata = new LFWMdFileTreeItem(
								parent, file, file.getName());
						IFile mdfile = project.getFile(file.getPath()
								.substring(
										project.getLocation().toString()
												.length()));
						metadata.setMdFile(mdfile);
						metadata.setType(WEBPersConstants.AMC_BPF_FILENAME);
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void initSRCTtree(LFWBasicTreeItem projectRoot, String projectPath,
			Map<String, String> pageNames, Map<String, String> appNames,
			Map<String, String> windowNames,
			Map<String, String> publicViewName, IProject project,
			BusinessComponent bcp) {
		IJavaProject javaProject = JavaCore.create(project);
		ArrayList list = new ArrayList();
		String bcpName = bcp == null ? "/" : bcp.getName(); //$NON-NLS-1$
		try {
			IPackageFragmentRoot[] packageFragmentRoots = javaProject
					.getPackageFragmentRoots();
			for (IPackageFragmentRoot sourceFolder : packageFragmentRoots) {
				if (sourceFolder.getKind() == IPackageFragmentRoot.K_SOURCE) {
					// list.add(sourceFolder);
					LFWDirtoryTreeItem srcNode = new LFWDirtoryTreeItem(
							projectRoot, sourceFolder.getResource(),
							sourceFolder.getResource().getFullPath().toString()
									.replace("/" + project.getName() + "/", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					// TreeItem srcNoItem = new TreeItem(projectRoot,SWT.None);
					srcNode.setType(LFWDirtoryTreeItem.BUILD_FOLDER);
				}
			}
		} catch (JavaModelException e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
	}

	public void initFuncTree(LFWBasicTreeItem projectRoot, IProject project,
			BusinessComponent bcp) {
//		new AMCThread().start();
		if (projectRoot.getItemCount() == 0) {
			File file = projectRoot.getFile();
			LFWDirtoryTreeItem initNode = new LFWDirtoryTreeItem(projectRoot,
					file, M_perspective.LFWExplorerTreeBuilder_0);
			initNode.setType("LOAD"); //$NON-NLS-1$
			return;
		}
		projectRoot.removeAll();
		
		File file = projectRoot.getFile();
		CpAppsNodeVO[] funcNodes = LFWWfmConnector.getAppsNodeVOsByCondition("devcomponent='"+bcp.getName()+"'"); //$NON-NLS-1$ //$NON-NLS-2$
		if(funcNodes!=null){
			for(CpAppsNodeVO func:funcNodes){
				LFWFuncTreeItem funcNode = new LFWFuncTreeItem(projectRoot, file, bcp,
						func.getTitle(), "node");  //$NON-NLS-1$
				funcNode.setFuncPk(func.getPk_appsnode());
				funcNode.setNodecode(func.getId());
				funcNode.setExsitFunc(func);
			}
		}
//		LFWFuncTreeItem funcNode = new LFWFuncTreeItem(projectRoot, file, bcp,
//				"注册功能节点", "node");
//		LFWFuncTreeItem typeNode = new LFWFuncTreeItem(projectRoot, file, bcp,
//				"注册菜单分类", "type");
//		LFWFuncTreeItem menuNode = new LFWFuncTreeItem(projectRoot, file, bcp,
//				"注册菜单", "menu");

	}

	// private WfmFlwCatVO[] wfmFlowCateList = null;

	public void initWfmTree(LFWBasicTreeItem projectRoot, IProject project,
			BusinessComponent bcp) {
//		new AMCThread().start();
		if (projectRoot.getItemCount() == 0) {
			File file = projectRoot.getFile();
			LFWDirtoryTreeItem initNode = new LFWDirtoryTreeItem(projectRoot,
					file, M_perspective.LFWExplorerTreeBuilder_0);
			initNode.setType("LOAD"); //$NON-NLS-1$
			return;
		}
		projectRoot.removeAll();
		String module = LfwCommonTool.getProjectModuleName(project);

		WfmFlwCatVO[] wfmFlowCateList = null;
		// wfmFlowCateList = NCConnector.getWfmFLowQry();
		if (bcp != null)
			wfmFlowCateList = LFWWfmConnector.getWfmFlowCateByModule(module,
					bcp.getName());
		else
			wfmFlowCateList = LFWWfmConnector.getWfmFlowCateByModule(module,
					null);

		File file = projectRoot.getFile();
		for (WfmFlwCatVO wfmFlowCate : wfmFlowCateList) {
			LFWWfmCateTreeItem cateNode = new LFWWfmCateTreeItem(projectRoot,
					file, wfmFlowCate.getPk_flwcat(), wfmFlowCate.getCatname());
			cateNode.setType(LFWDirtoryTreeItem.WFM_FLWCATE);
		}
	}

	public void initLangTree(LFWBasicTreeItem projectRoot, String projectPath,
			Map<String, String> pageNames, Map<String, String> appNames,
			Map<String, String> windowNames,
			Map<String, String> publicViewName, IProject project,
			BusinessComponent bcp) {
		// new AMCThread().start();
		if (projectRoot.getItemCount() == 0) {
			File file = projectRoot.getFile();
			LFWDirtoryTreeItem initNode = new LFWDirtoryTreeItem(projectRoot,
					file, M_perspective.LFWExplorerTreeBuilder_0);
			initNode.setType("LOAD"); //$NON-NLS-1$
			return;
		}
		projectRoot.removeAll();

		File file = projectRoot.getFile();
		// 初始化Applications节点
		File applicationFile = new File(file,
				WEBPersConstants.AMC_APPLICATION_PATH); //$NON-NLS-1$
		LFWDirtoryTreeItem applicationNode = new LFWDirtoryTreeItem(
				projectRoot, applicationFile, WEBPersConstants.APPLICATION);
		applicationNode.setType(LFWDirtoryTreeItem.APPLICATION_FOLDER);
//		refreshAMCNodeSubTree(applicationNode, appNames,
//				LFWDirtoryTreeItem.APPLICATION,
//				WEBPersConstants.AMC_APPLICATION_FILENAME, project, bcp);
//		refreshAppsNodeSubTree(applicationNode, project, bcp);

		
		//初始化windowcomponent节点
		File windowFile = new File(file, WEBPersConstants.AMC_WINDOW_PATH);
		LFWDirtoryTreeItem windowComponentNode = new LFWDirtoryTreeItem(projectRoot, windowFile,WEBPersConstants.WINDOWCOMPONENT);
		windowComponentNode.setBcp(bcp);
		windowComponentNode.setType(LFWDirtoryTreeItem.COMPONENT);
//		refreshCompNodeSubTree(windowComponentNode,project,bcp);
		
		//初始化viewcomponent节点
		File publicViewFile = new File(file, WEBPersConstants.AMC_PUBVIEW_PATH);
		LFWDirtoryTreeItem viewComponentNode = new LFWDirtoryTreeItem(projectRoot, publicViewFile,WEBPersConstants.VIEWCOMPONENT);
		viewComponentNode.setBcp(bcp);
		viewComponentNode.setType(LFWDirtoryTreeItem.COMPONENT);
//		refreshCompViewSubTree(viewComponentNode,project,bcp);
	}

	public void initAMCProjectTree(LFWBasicTreeItem projectRoot,
			String projectPath, Map<String, String> pageNames,
			Map<String, String> appNames, Map<String, String> windowNames,
			Map<String, String> publicViewName, IProject project,
			BusinessComponent bcp) {

		if (projectRoot.getItemCount() == 0) {
			File file = projectRoot.getFile();
			LFWDirtoryTreeItem initNode = new LFWDirtoryTreeItem(projectRoot,
					file, M_perspective.LFWExplorerTreeBuilder_0);
			initNode.setType("LOAD"); //$NON-NLS-1$
			return;
		}
		projectRoot.removeAll();

//		new AMCThread().start();

		File file = projectRoot.getFile();
		// 初始化Applications节点
		File applicationFile = new File(file,
				WEBPersConstants.AMC_APPLICATION_PATH);
		LFWDirtoryTreeItem applicationNode = new LFWDirtoryTreeItem(
				projectRoot, applicationFile, WEBPersConstants.APPLICATION);
		applicationNode.setType(LFWDirtoryTreeItem.APPLICATION_FOLDER);
		applicationNode.setBcp(bcp);
//		refreshAMCNodeSubTree(applicationNode, appNames,
//				LFWDirtoryTreeItem.APPLICATION,
//				WEBPersConstants.AMC_APPLICATION_FILENAME, project, bcp);
		
		refreshAppsNodeSubTree(applicationNode,project,bcp);

		// 初始化Modules节点
		// File modelFile = new File(file, WEBProjConstants.AMC_MODEL_PATH);
		// LFWDirtoryTreeItem modelNode = new LFWDirtoryTreeItem(projectRoot,
		// modelFile, WEBProjConstants.MODEL);
		// modelNode.setType(LFWDirtoryTreeItem.MODEL_FOLDER);
		// modelNode.setLfwVersion(LFWTool.NEW_VERSION);
		// refreshAMCNodeSubTree(modelNode, projectPath, modelNames,
		// WEBProjConstants.AMC_MODEL_PATH, LFWDirtoryTreeItem.MODEL,
		// WEBProjConstants.AMC_MODEL_FILENAME, project);

//		// 初始化Windows节点
//		File windowFile = new File(file, WEBPersConstants.AMC_WINDOW_PATH);
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put(WebConstant.PROJECT_PATH_KEY, projectPath);
//		paramMap.put(WebConstant.PARENT_PAGE_ID_KEY, null);
//
//		LFWDirtoryTreeItem windowNode = new LFWDirtoryTreeItem(projectRoot,
//				windowFile, WEBPersConstants.WINDOW);
//		windowNode.setType(LFWDirtoryTreeItem.WINDOW_FOLDER);
//		windowNode.setBcp(bcp);
//		refreshAMCNodeSubTree(windowNode, pageNames, LFWDirtoryTreeItem.WINDOW,
//				WEBPersConstants.AMC_WINDOW_FILENAME, project, bcp);
//		// initNodeSubTree(windowNode, projectPath, pageNames);
//
//		// 初始化PublicViews节点
//		File publicViewFile = new File(
//				((LFWBasicTreeItem) projectRoot.getParentItem()).getFile(),
//				WEBPersConstants.AMC_PUBLIC_VIEW_PATH);
//		LFWDirtoryTreeItem publicViewNode = new LFWDirtoryTreeItem(projectRoot,
//				publicViewFile, WEBPersConstants.PUBLIC_VIEW);
//		publicViewNode.setType(LFWDirtoryTreeItem.PUBLIC_VIEW_FOLDER);
//
//		initPublicViewSubTree(publicViewNode, publicViewFile, project, bcp);
		
		//初始化windowcomponent节点
		File windowFile = new File(file, WEBPersConstants.AMC_WINDOW_PATH);
		LFWDirtoryTreeItem windowComponentNode = new LFWDirtoryTreeItem(projectRoot, windowFile,WEBPersConstants.WINDOWCOMPONENT);
		windowComponentNode.setBcp(bcp);
		windowComponentNode.setType(LFWDirtoryTreeItem.WINDOW_COMPONENT);
		refreshCompNodeSubTree(windowComponentNode,project,bcp);
		
		//初始化viewcomponent节点
		File publicViewFile = new File(file, WEBPersConstants.AMC_PUBVIEW_PATH);
		LFWDirtoryTreeItem viewComponentNode = new LFWDirtoryTreeItem(projectRoot, publicViewFile,WEBPersConstants.VIEWCOMPONENT);
		viewComponentNode.setBcp(bcp);
		viewComponentNode.setType(LFWDirtoryTreeItem.PUBVIEW_COMPONENT);
		refreshCompViewSubTree(viewComponentNode,project,bcp);
		
		//初始化参照节点
		LFWDirtoryTreeItem refInfoNode = new LFWDirtoryTreeItem(projectRoot,
				file, WEBPersConstants.REFINFO);
		refInfoNode.setBcp(bcp);
		refInfoNode.setType(LFWDirtoryTreeItem.REFINFO);
		showRefInfoTree(refInfoNode,project,bcp);
	}
	public void refreshAppsNodeSubTree(LFWDirtoryTreeItem parent,IProject project, BusinessComponent bcp) {
		String moduleName = LfwCommonTool.getProjectModuleName(project);
		String bcpName = bcp.getName();
		Map<String,Application> applicationMap = new HashMap<String, Application>();		
		applicationMap = LFWAMCConnector.getApplications(moduleName, bcpName);
		if(applicationMap!=null&&applicationMap.size()>0){
			List<Application> appList = sortMap(applicationMap);
//			Iterator<Application> iter = applicationMap.values().iterator();
//			while(iter.hasNext()){
			for(Application app:appList){
//				Application app = iter.next();
				if(app==null||app.getId()==null)
					continue;
				LFWApplicationTreeItem item = null;
				String id = app.getId();
				String name = app.getCaption();
				File appFolder = new File(parent.getFile(),app.getId());
				if (name != null && name.trim().length() > 0) {
					item = new LFWApplicationTreeItem(parent, appFolder, name + "[" + id //$NON-NLS-1$
							+ "]"); //$NON-NLS-1$
				} else {
					item = new LFWApplicationTreeItem(parent, appFolder, id);
				}
				((LFWApplicationTreeItem) item).setApplication(app);
				((LFWApplicationTreeItem) item).setId(id);
				LFWDirtoryTreeItem direc = (LFWDirtoryTreeItem) item;
				direc.setType(LFWDirtoryTreeItem.APPLICATION);
				direc.setBcp(bcp);
			}
		}
	}
	public void refreshCompViewSubTree(LFWDirtoryTreeItem parent,IProject project, BusinessComponent bcp) {
		String moduleName = LfwCommonTool.getProjectModuleName(project);
		String bcpName = bcp.getName();
		initComponentViewTree(parent, moduleName, bcpName,null);
	}

	/**
	 * @param parent
	 * @param moduleName
	 * @param bcpName
	 */
	public void initComponentViewTree(LFWDirtoryTreeItem parent, String moduleName, String bcpName,String ctx) {
		Map<String, LfwComponent> componentMap = new HashMap<String, LfwComponent>();
		if(ctx == null)
			componentMap = LFWAMCConnector.getCacheViewCompMap(moduleName, bcpName);
		else componentMap = LFWAMCConnector.getCacheViewCompMap(ctx, moduleName, bcpName);
		Boolean defaultComp = false;
		BusinessComponent bcp = new BusinessComponent();
		bcp.setName(bcpName);
		if(componentMap!=null&&componentMap.size()>0){			
			Iterator<String> iter = componentMap.keySet().iterator();
			while(iter.hasNext()){
				String id = iter.next();
				LfwComponent component = componentMap.get(id);
				String displayName = null;
				if(component.getId().equals(LfwUIComponent.ANNOYUICOMPONENT)){
					displayName = M_perspective.LFWExplorerTreeBuilder_10;
					defaultComp = true;
					LfwUIComponent uiComponent = new LfwUIComponent();
					uiComponent.setId(component.getId());
					uiComponent.setName(displayName);
					uiComponent.setPack(""); //$NON-NLS-1$
					component.setUiComponent(uiComponent);
				}
				else displayName = component.getUiComponent().getName();
				if(displayName==null){
					displayName = component.getUiComponent().getId();
					component.getUiComponent().setName(displayName);
				}
				String pack = component.getUiComponent().getPack();
				displayName = displayName+"["+id+"]"; //$NON-NLS-1$ //$NON-NLS-2$

				
				String packPath = ""; //$NON-NLS-1$
				if(!component.getId().equals(LfwUIComponent.ANNOYUICOMPONENT)){
					if(pack==null||pack.length()==0){
						packPath = "/"+id; //$NON-NLS-1$
					}
					else
						packPath = "/"+id.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				
				File pubviewFile = new File(parent.getFile(),packPath);
				
				LFWComponentTreeItem componentItem = new LFWComponentTreeItem(parent, pubviewFile, component.getUiComponent(), displayName);
				componentItem.setBcp(bcp);
				componentItem.setType(ILFWTreeNode.PUBLIC_VIEW);
				
				initPublicViewsByComponent(componentItem, component);
			}			
		}
		if(!defaultComp){
			LfwUIComponent defaultComponent = new LfwUIComponent();
			defaultComponent.setId(LfwUIComponent.ANNOYUICOMPONENT);
			String displayName = M_perspective.LFWExplorerTreeBuilder_10;
			defaultComponent.setName(displayName);
			defaultComponent.setPack(""); //$NON-NLS-1$
			File pubviewFile = parent.getFile();
			LFWComponentTreeItem componentItem = new LFWComponentTreeItem(parent, pubviewFile, defaultComponent, displayName);
			componentItem.setBcp(bcp);
			componentItem.setType(ILFWTreeNode.PUBLIC_VIEW);
		}
	}
	public void refreshCompNodeSubTree(LFWDirtoryTreeItem parent,IProject project, BusinessComponent bcp) {
		String moduleName = LfwCommonTool.getProjectModuleName(project);
		String bcpName = bcp.getName();		
		initComponentNodeTree(parent, moduleName, bcpName,null);
	}

	/**
	 * @param parent
	 * @param moduleName
	 * @param bcpName
	 */
	public void initComponentNodeTree(LFWDirtoryTreeItem parent, String moduleName, String bcpName,String ctx) {
		Map<String, LfwComponent> componentMap = new HashMap<String, LfwComponent>();
		if(ctx == null)
			componentMap = LFWAMCConnector.getCacheComponentMap(moduleName, bcpName);
		else componentMap = LFWAMCConnector.getCacheComponentMap(ctx, moduleName, bcpName);
		Boolean defaultComp = false;
		BusinessComponent bcp = new BusinessComponent();
		bcp.setName(bcpName);
		if(componentMap!=null&&componentMap.size()>0){			
			Iterator<String> iter = componentMap.keySet().iterator();
			while(iter.hasNext()){
				String id = iter.next();
				LfwComponent component = componentMap.get(id);
				String displayName = null;
				if(component.getId().equals(LfwUIComponent.ANNOYUICOMPONENT)){
					displayName = M_perspective.LFWExplorerTreeBuilder_10;
					defaultComp = true;
					LfwUIComponent uiComponent = new LfwUIComponent();
					uiComponent.setId(component.getId());
					uiComponent.setName(displayName);
					uiComponent.setPack(""); //$NON-NLS-1$
					component.setUiComponent(uiComponent);
				}
				else displayName = component.getUiComponent().getName();
				if(displayName==null){
					displayName = component.getUiComponent().getId();
					component.getUiComponent().setName(displayName);
				}
				String pack = component.getUiComponent().getPack();
				displayName = displayName+"["+id+"]"; //$NON-NLS-1$ //$NON-NLS-2$
				
//				LFWComponentTreeItem componentItem = new LFWComponentTreeItem(parent, parent.getFile(), component.getUiComponent(), displayName);
//				componentItem.setBcp(bcp);
//				componentItem.setType(ILFWTreeNode.WINDOW);
				
				String packPath = ""; //$NON-NLS-1$
				if(!component.getId().equals(LfwUIComponent.ANNOYUICOMPONENT)){
					if(pack==null||pack.length()==0){
						packPath = "/"+id; //$NON-NLS-1$
					}
					else
						packPath = "/"+id.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				File windowFile = new File(parent.getFile(), packPath);

				
				LFWComponentTreeItem componentItem = new LFWComponentTreeItem(parent, windowFile, component.getUiComponent(), displayName);				
				componentItem.setBcp(bcp);
				componentItem.setType(ILFWTreeNode.WINDOW);
				
				initWindowsByComponent(componentItem,component);
			}			
		}
		if(!defaultComp){
			LfwUIComponent defaultComponent = new LfwUIComponent();
			defaultComponent.setId(LfwUIComponent.ANNOYUICOMPONENT);
			String displayName = M_perspective.LFWExplorerTreeBuilder_10;
			defaultComponent.setName(displayName);
			defaultComponent.setPack(""); //$NON-NLS-1$
			File windowFile = parent.getFile();
			LFWComponentTreeItem componentItem = new LFWComponentTreeItem(parent, windowFile, defaultComponent, displayName);
			componentItem.setBcp(bcp);
			componentItem.setType(ILFWTreeNode.WINDOW);
		}
	}

	/**
	 * @param pubviewNode
	 * @param component
	 */
	public void initPublicViewsByComponent(LFWDirtoryTreeItem pubviewNode, LfwComponent component) {
		Map<String,LfwView> pubviewMap = component.getViewMap();
		if(pubviewMap!=null){
//			Iterator<LfwView> pubIter = pubviewMap.values().iterator();
			List<LfwView> viewList = sortMap(pubviewMap);
//			while(pubIter.hasNext()){
//				LfwView pubview = pubIter.next();
			for(LfwView pubview:viewList){
				File file = new File(pubviewNode.getFile(),pubview.getId());
				String caption = pubview.getCaption()==null?pubview.getId():pubview.getCaption();
				LFWWidgetTreeItem pubviewTreeItem = new LFWWidgetTreeItem(pubviewNode,file, pubview, caption+"["+ pubview.getId()+ "]");  //$NON-NLS-1$ //$NON-NLS-2$
				LFWDirtoryTreeItem direc = (LFWDirtoryTreeItem) pubviewTreeItem;
				direc.setId(pubview.getId());
				direc.setItemName(pubview.getCaption());
				pubviewTreeItem.setType(LFWDirtoryTreeItem.POOLWIDGETFOLDER);
				LFWExplorerTreeView.getLFWExploerTreeView(null).detalWidgetTreeItem(pubviewTreeItem, file, pubview);
			}
		}
	}

	/**
	 * @param windowNode
	 * @param windowFile
	 * @param component
	 */
	public void initWindowsByComponent(LFWDirtoryTreeItem windowNode, LfwComponent component) {
		Map<String, LfwWindow> windowMap = component.getWindowMap();
		if(windowMap!=null){
			List<LfwWindow> windowList = sortMap(windowMap);
//			Iterator<LfwWindow> winIter = windowMap.values().iterator();			
//			while(winIter.hasNext()){
			for(LfwWindow win:windowList){
//				LfwWindow win = winIter.next();
				if(win.getId()==null) continue;
				File file = new File(windowNode.getFile(),win.getId());
				LFWPageMetaTreeItem pmItem = new LFWPageMetaTreeItem(windowNode, file, win.getCaption() + "[" + win.getId() +"]"); //$NON-NLS-1$ //$NON-NLS-2$
				pmItem.setPm(win);
				LFWDirtoryTreeItem direc = (LFWDirtoryTreeItem) pmItem;
				direc.setId(win.getId());
				direc.setItemName(win.getCaption());
				direc.setType(ILFWTreeNode.WINDOW);
				initViewsByWindowNode(pmItem);
			}
		}
	}
	public void initViewsByWindowNode(LFWDirtoryTreeItem windowNode){
		if (windowNode.getItemCount() == 0) {
			File file = windowNode.getFile();
			LFWDirtoryTreeItem initNode = new LFWDirtoryTreeItem(windowNode,
					file, M_perspective.LFWExplorerTreeBuilder_0);
			initNode.setType("LOAD"); //$NON-NLS-1$
			return;
		}
		windowNode.removeAll();
		RefreshWindowNodeAction refreshNodeAction = new RefreshWindowNodeAction();
		refreshNodeAction.run();
	}
	
	public void showRefInfoTree(LFWDirtoryTreeItem parent,IProject project, BusinessComponent bcp) {
		String moduleName = LfwCommonTool.getProjectModuleName(project);
		String path = project.getLocation().toOSString()+"/"+bcp.getName()+"/src/public/"; //$NON-NLS-1$ //$NON-NLS-2$
		File fileFolder = new File(path);
		LfwRefInfoVO[] refInfoVOs = LFWWfmConnector.getRefInfoByCondition("reserv2='"+moduleName+"' and reserv3='"+bcp.getName()+"'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if(refInfoVOs!=null && refInfoVOs.length>0){
			for(LfwRefInfoVO refinfo:refInfoVOs){
//				LFWRefInfoTreeItem refInfoItem = new LFWRefInfoTreeItem(parent, project, fileFolder,refinfo, refinfo.getName());
				LFWRefFolderTreeItem refFolder = new LFWRefFolderTreeItem(parent, fileFolder, refinfo, refinfo.getName());
				LFWRefInfoTreeItem refModelNode = new LFWRefInfoTreeItem(refFolder, IConst.MODEL_CLASSTYPE, project, fileFolder, refinfo, 
						refinfo.getRefclass().substring(refinfo.getRefclass().lastIndexOf(".")+1)+".java"); //$NON-NLS-1$ //$NON-NLS-2$
				String refClassName = refinfo.getRefclass().substring(refinfo.getRefclass().indexOf(".model.")+7); //$NON-NLS-1$
				String refCtrlClass = refinfo.getRefclass().substring(0,refinfo.getRefclass().indexOf(".model."))+".control."+refClassName.replace("RefModel", "RefController"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				File classFile = new File(fileFolder,refCtrlClass.replace(".", "/")+".java"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				if(!classFile.exists())
					continue;
				LFWRefInfoTreeItem refControlNode = new LFWRefInfoTreeItem(refFolder, "tree", project, fileFolder, refinfo,  //$NON-NLS-1$
						refCtrlClass.substring(refCtrlClass.lastIndexOf(".")+1)+".java"); //$NON-NLS-1$ //$NON-NLS-2$
				refControlNode.setRefClass(classFile);
//				refInfoItem.setRefClass(refinfo.getRefclass());
			}
		}
	}

	/**
	 * 初始化PublicView
	 * 
	 * @param parent
	 * @param file
	 * @param project
	 */
//	public void initPublicViewSubTree(LFWDirtoryTreeItem parent, File file,
//			IProject project, BusinessComponent bcp) {
//		try {
//			String ctx = LFWUtility.getContextFromResource(project);
//			// Map<String, Map<String, LfwWidget>> allWidget =
//			// ctxWidgetMap.get("/" + ctx);
//			Map<String, Map<String, LfwView>> ctxWidgetMap = RefDatasetData
//					.getPoolWidgets(null);
//			Map<String, LfwView> widgetMap = ctxWidgetMap.get("/" + ctx); //$NON-NLS-1$
//			LFWWidgetTreeItem pubWidgetTreeItem = null;
//			if (widgetMap != null) {
//				String parentFilePath = file.getPath();
//				Iterator<LfwView> it = widgetMap.values().iterator();
//				while (it.hasNext()) {
//					LfwView widget = it.next();
//					File widgetFile = new File(parentFilePath + File.separator
//							+ widget.getId());
//					if (widgetFile.exists()) {
//						String caption = widget.getCaption()==null?widget.getId():widget.getCaption();
//						pubWidgetTreeItem = new LFWWidgetTreeItem(parent,
//								widgetFile, widget, caption+"[" //$NON-NLS-1$
//										+ widget.getId()
//										+ "]"); //$NON-NLS-1$
//						pubWidgetTreeItem
//								.setType(LFWDirtoryTreeItem.POOLWIDGETFOLDER);
//					}
//				}
//			}
//		} catch (Throwable e) {
//			MainPlugin.getDefault().logError(e);
//		}
//	}

	/**
	 * 初始化公共片段下的节点
	 * 
	 * @param parent
	 * @param file
	 * @param projectPath
	 */
//	public void initPubWidgetSubTree(LFWDirtoryTreeItem parent, File file,
//			IProject project) {
//		try {
//			String ctx = LFWUtility.getContextFromResource(project);
//			// Map<String, Map<String, LfwWidget>> allWidget = RefDatasetData
//			// .getPoolWidgets("/" + ctx);
//			Map<String, Map<String, LfwView>> ctxWidgetMap = RefDatasetData
//					.getPoolWidgets(null);
//			Map<String, LfwView> widgetMap = ctxWidgetMap.get("/" + ctx); //$NON-NLS-1$
//			LFWWidgetTreeItem pubWidgetTreeItem = null;
//			if (widgetMap != null) {
//				String msg = WEBPersConstants.PUBLIC_VIEW_SUB;
//				String parentFilePath = file.getPath().replace(
//						WEBPersConstants.PUBLIC_WIDGET,
//						"web\\pagemeta\\public\\widgetpool"); //$NON-NLS-1$
//				Iterator<LfwView> it = widgetMap.values().iterator();
//				while (it.hasNext()) {
//					LfwView widget = it.next();
//					File widgetFile = new File(parentFilePath + "/" //$NON-NLS-1$
//							+ widget.getId());
//					pubWidgetTreeItem = new LFWWidgetTreeItem(parent,
//							widgetFile, widget, "[" + msg + "] " //$NON-NLS-1$ //$NON-NLS-2$
//									+ widgetFile.getName());
//					pubWidgetTreeItem
//							.setType(LFWDirtoryTreeItem.POOLWIDGETFOLDER);
//				}
//			}
//		} catch (Throwable e) {
//			MainPlugin.getDefault().logError(e);
//		}
//	}

	/**
	 * 初始化公共数据集下的节点
	 * 
	 * @param parent
	 * @param project
	 */
//	public void initDsSubTree(LFWDirtoryTreeItem parent, File file,
//			String projectPath) {
//		Map<File, LFWDirtoryTreeItem> foldermap = new HashMap<File, LFWDirtoryTreeItem>();
//		try {
//			File rootFile = file;
//			Map<String, Dataset> dsMap = LFWConnector.getDataset(projectPath);
//			if (dsMap != null) {
//				Iterator<Dataset> it = dsMap.values().iterator();
//				LFWDirtoryTreeItem orinalparent = parent;
//				while (it.hasNext()) {
//					Dataset ds = it.next();
//					String id = ds.getId();
//					if (id.indexOf(".") == -1) {
//						new LFWDSTreeItem(parent, ds, ds.getId());
//					} else {
//						String foldername = id.substring(0, id.indexOf("."));
//						String leftfolder = id.substring(id.indexOf(".") + 1);
//						while (leftfolder != null) {
//							File newfile = new File(file, foldername);
//							LFWDirtoryTreeItem treeItem = (LFWDirtoryTreeItem) foldermap
//									.get(newfile);
//							file = newfile;
//							if (treeItem == null) {
//								LFWDirtoryTreeItem newTreeItem = new LFWDirtoryTreeItem(
//										parent, newfile);
//								newTreeItem
//										.setType(LFWDirtoryTreeItem.POOLDSFOLDER);
//								parent = newTreeItem;
//								foldermap.put(newfile, newTreeItem);
//								if (leftfolder.indexOf(".") != -1) {
//									foldername = leftfolder.substring(0,
//											leftfolder.indexOf("."));
//									leftfolder = leftfolder
//											.substring(leftfolder.indexOf(".") + 1);
//								} else {
//									new LFWDSTreeItem(parent, ds, leftfolder);
//									leftfolder = null;
//								}
//							} else {
//								parent = treeItem;
//								if (leftfolder.indexOf(".") != -1) {
//									foldername = leftfolder.substring(0,
//											leftfolder.indexOf("."));
//									leftfolder = leftfolder
//											.substring(leftfolder.indexOf(".") + 1);
//
//								} else {
//									new LFWDSTreeItem(parent, ds, leftfolder);
//									leftfolder = null;
//								}
//							}
//						}
//						parent = orinalparent;
//					}
//					file = rootFile;
//				}
//			}
//		} catch (Throwable e) {
//			MainPlugin.getDefault().logError(e);
//		}
//	}

	//
	// // 初始化pageflow节点
	// public void initPageFlowSubTree(TreeItem parent, String projectPath) {
	// if (pageFlows != null) {
	// for (int i = 0; i < pageFlows.length; i++) {
	// PageFlow pageflow = pageFlows[i];
	// new LFWPageFlowTreeItem(parent, pageflow);
	// }
	// }
	// }

	/**
	 * 刷新某个page下的节点
	 * 
	 * @param nodeItem
	 * @param project
	 */
	public void initSubNodeTree(TreeItem nodeItem, IProject project) {
		String[] projPaths = new String[1];
		projPaths[0] = project.getLocation().toString();
		try {
			Map<String, String> pageNames = NCConnector.getPageNames(projPaths)[0];
			LFWDirtoryTreeItem dir = (LFWDirtoryTreeItem) nodeItem;
			File fileFolder = dir.getFile();
			File[] children = fileFolder.listFiles();
			if (children != null) {
				for (int j = 0; j < children.length; j++) {
					if (children[j].isDirectory()) {
						scanDir(nodeItem, children[j], pageNames);
					}
				}
			}
		} catch (Throwable e) {
			MainPlugin.getDefault().logError(e);
		}
	}

	/**
	 * 刷新某个applicaton下的节点
	 * 
	 * @param nodeItem
	 * @param project
	 */
	public void initSubAppTree(TreeItem nodeItem, IProject project) {

	}

	/**
	 * 刷新某个Window下的节点
	 * 
	 * @param nodeItem
	 * @param project
	 */
	public void initWindowSubNodeTree(TreeItem nodeItem, IProject project) {
		String[] projPaths = new String[1];
		projPaths[0] = project.getLocation().toString();
		try {
			Map<String, String> pageNames = NCConnector.getPageNames(projPaths)[0];
			LFWDirtoryTreeItem dir = (LFWDirtoryTreeItem) nodeItem;
			File fileFolder = dir.getFile();
			File[] children = fileFolder.listFiles();
			if (children != null) {
				for (int j = 0; j < children.length; j++) {
					if (children[j].isDirectory()) {
						scanWindowDir(nodeItem, children[j], pageNames,
								ILFWTreeNode.VIEW,
								WEBPersConstants.AMC_VIEW_FILENAME, project);
					}
				}
			}
		} catch (Throwable e) {
			MainPlugin.getDefault().logError(e);
		}
	}

	/**
	 * 刷新pages下的节点
	 * 
	 * @param parent
	 * @param project
	 * @param pageNames
	 */
	// public void initNodeSubTree(TreeItem parent, String projectPath,
	// Map<String, String> pageNames) {
	// if (pageNames != null) {
	// File fileFolder = new File(projectPath + "/web/html/nodes");
	// File[] children = fileFolder.listFiles();
	// if (children != null) {
	// for (int j = 0; j < children.length; j++) {
	// if (children[j].isDirectory()) {
	// scanDir(parent, children[j], pageNames);
	// }
	// }
	// }
	// }
	// }

	private void scanDir(TreeItem parent, File dir,
			Map<String, String> pageNames) {
		TreeItem item = null;
		if (judgeIsPMFolder(dir)) {
			String id = dir.getName();
			String parentId = null;
			String name = null;
			if (parent instanceof LFWPageMetaTreeItem) {
				LFWPageMetaTreeItem pmParent = (LFWPageMetaTreeItem) parent;
				parentId = pmParent.getPageId();
			}
			if (parentId == null)
				name = pageNames.get(id);
			else
				name = pageNames.get(parentId + "." + id); //$NON-NLS-1$
			if (name != null && !name.equals("")) //$NON-NLS-1$
				item = new LFWPageMetaTreeItem(parent, dir, name + "[" + id //$NON-NLS-1$
						+ "]"); //$NON-NLS-1$
			else
				item = new LFWPageMetaTreeItem(parent, dir, dir.getName());
			if (item != null) {
				((LFWPageMetaTreeItem) item).setId(id);
				LFWDirtoryTreeItem direc = (LFWDirtoryTreeItem) item;
				direc.setType(LFWDirtoryTreeItem.NODE_FOLDER);
			}
		} else {
			if ((parent instanceof LFWPageMetaTreeItem))
				return;
			item = new LFWVirtualDirTreeItem(parent, dir);
		}

		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			if (fs[i].isDirectory())
				scanDir(item, fs[i], pageNames);
		}
	}

	/**
	 * 刷新AMC下的节点
	 * 
	 * @param parent
	 * @param project
	 * @param pageNames
	 * @param path
	 */
	/*
	public void refreshAMCNodeSubTree(LFWBasicTreeItem parent,
			Map<String, String> amcNames, String itemType, String fileName,
			IProject project, BusinessComponent bcp) {

		String projPath = project.getLocation().toString();
		if (bcp != null)
			projPath = projPath + "/" + bcp.getName(); //$NON-NLS-1$
		String[] projPaths = new String[1];
		projPaths[0] = projPath;

		if (parent.getText().equals(WEBPersConstants.APPLICATION)) {
			amcNames = LFWAMCConnector.getTreeNodeNames(projPaths,
					ILFWTreeNode.APPLICATION,
					WEBPersConstants.AMC_BASE_NODEGROUP_PATH
							+ WEBPersConstants.AMC_APPLICATION_PATH)[0];
		} else if (parent.getText().equals(WEBPersConstants.WINDOW)) {
			amcNames = NCConnector.getPageNames(projPaths)[0];
		} else if (parent.getText().equals(
				WEBPersConstants.AMC_PUBLIC_VIEW_FILENAME)) {
			amcNames = LFWAMCConnector.getTreeNodeNames(projPaths,
					ILFWTreeNode.PUBLIC_VIEW,
					WEBPersConstants.AMC_PUBLIC_VIEW_PATH)[0];
		}
		if (amcNames != null) {
			String projectPath = parent.getFile().getAbsolutePath();
			if (!projectPath.endsWith("/") && !projectPath.endsWith("\\")) { //$NON-NLS-1$ //$NON-NLS-2$
				projectPath += "/"; //$NON-NLS-1$
			}
			File fileFolder = new File(projectPath);
			File[] children = fileFolder.listFiles();
			if (children != null) {
				for (int j = 0; j < children.length; j++) {
					if (children[j].isDirectory()) {
						if(children[j].getName().equals("includecss")||children[j].getName().equals("includejs")) 
							continue;
						scanAMCDir(parent, children[j], amcNames, itemType,
								fileName, project);
					}
				}
			}
		}
	}

	 */
	
	private void scanAMCDir(TreeItem parent, File dir,
			Map<String, String> amcNames, String itemType, String fileName,
			IProject project) {
		if (ILFWTreeNode.APPLICATION.equals(itemType)) {
			scanAppDir(parent, dir, amcNames, itemType, fileName, project);
		}
		// else if (ILFWTreeNode.MODEL.equals(itemType)) {
		// scanModelDir(parent, dir, amcNames, itemType, fileName, project);
		// }
		else if (ILFWTreeNode.WINDOW.equals(itemType)) {
			scanWindowDir(parent, dir, amcNames, itemType, fileName, project);
		} else if (ILFWTreeNode.PUBLIC_VIEW.equals(itemType)) {
			scanPublicViewDir(parent, dir, amcNames, itemType, fileName,
					project);
		}
	}

	private void scanAppDir(TreeItem parent, File dir,
			Map<String, String> amcNames, String itemType, String fileName,
			IProject project) {
		TreeItem item = null;
		if (judgeIsNeedFolder(dir, fileName)) {
			String id = dir.getName();
			String name = amcNames.get(id);
			if (name != null && name.trim().length() > 0) {
				item = new LFWApplicationTreeItem(parent, dir, name + "[" + id //$NON-NLS-1$
						+ "]"); //$NON-NLS-1$
			} else {
				item = new LFWApplicationTreeItem(parent, dir, dir.getName());
			}
			Application application = new Application();
			application.setId(id);
			application.setCaption(name);
			((LFWApplicationTreeItem) item).setApplication(application);
			((LFWApplicationTreeItem) item).setId(id);
			LFWDirtoryTreeItem direc = (LFWDirtoryTreeItem) item;
			direc.setType(itemType);
		} else {
			if (parent instanceof LFWApplicationTreeItem) {
				return;
			}
			item = new LFWVirtualDirTreeItem(parent, dir);
			((LFWVirtualDirTreeItem) item)
					.setType(ILFWTreeNode.APPLICATION_FOLDER);
		}
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			if (fs[i].isDirectory()) {
				scanAppDir(item, fs[i], amcNames, itemType, fileName, project);
			}
		}
	}

	public void scanWindowDir(TreeItem parent, File dir,
			Map<String, String> pageNames, String itemType, String fileName,
			IProject project) {
		TreeItem item = null;
		if (judgeIsNeedFolder(dir, fileName)) {
			String id = dir.getName();
			String parentId = null;
			String name = null;
			if (parent instanceof LFWPageMetaTreeItem) {
				LFWPageMetaTreeItem pmParent = (LFWPageMetaTreeItem) parent;
				parentId = pmParent.getPageId();
			}
			if (parentId == null) {
				name = pageNames.get(id);
			} else {
				name = pageNames.get(parentId + "." + id); //$NON-NLS-1$
			}
			if (name != null && name.trim().length() > 0) {
				item = new LFWPageMetaTreeItem(parent, dir, name + "[" + id //$NON-NLS-1$
						+ "]"); //$NON-NLS-1$
			} else {
				item = new LFWPageMetaTreeItem(parent, dir, dir.getName());
				name = dir.getName();
			}
			LFWDirtoryTreeItem direc = (LFWDirtoryTreeItem) item;
			direc.setId(id);
			direc.setItemName(name);
			direc.setType(itemType);
		} else {
			if (parent instanceof LFWPageMetaTreeItem) {
				return;
			}
			item = new LFWVirtualDirTreeItem(parent, dir);
			((LFWVirtualDirTreeItem) item).setType(ILFWTreeNode.WINDOW_FOLDER);
		}
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			if (fs[i].isDirectory()) {
				scanWindowDir(item, fs[i], pageNames, itemType, fileName,
						project);
			}
		}
	}

	private void scanPublicViewDir(TreeItem parent, File dir,
			Map<String, String> amcNames, String itemType, String fileName,
			IProject project) {
		TreeItem item = null;
		if (judgeIsNeedFolder(dir, fileName)) {
			String id = dir.getName();
			String name = amcNames.get(id);
			if (name != null && name.trim().length() > 0) {
				item = new LFWPublicViewTreeItem(parent, dir, "[View] " //$NON-NLS-1$
						+ dir.getName());
			} else {
				item = new LFWPublicViewTreeItem(parent, dir, dir.getName());
			}
			if (item != null) {
				LFWDirtoryTreeItem direc = (LFWDirtoryTreeItem) item;
				direc.setType(itemType);
			}
		} else {
			if (parent instanceof LFWPublicViewTreeItem) {
				return;
			}
			item = new LFWVirtualDirTreeItem(parent, dir);
			((LFWVirtualDirTreeItem) item)
					.setType(ILFWTreeNode.PUBLIC_VIEW_FOLDER);
		}
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			if (fs[i].isDirectory()) {
				scanPublicViewDir(item, fs[i], amcNames, itemType, fileName,
						project);
			}
		}
	}

	private boolean judgeIsNeedFolder(File fold, String fileName) {
		File[] childChildren = fold.listFiles();
		if (childChildren == null)
			return false;
		for (int i = 0; i < childChildren.length; i++) {
			if (childChildren[i].getName().equals(fileName))
				return true;
		}
		return false;

	}

	private boolean judgeIsPMFolder(File fold) {
		File[] childChildren = fold.listFiles();
		if (childChildren == null)
			return false;
		for (int i = 0; i < childChildren.length; i++) {
			if (childChildren[i].getName().equals("pagemeta.pm")) //$NON-NLS-1$
				return true;
		}
		return false;

	}

//	public void initRefNodeSubTree(LFWDirtoryTreeItem parent, File file,
//			String projectPath) {
//		Map<Object, Object> foldermap = new HashMap<Object, Object>();
//		LFWDirtoryTreeItem orinalparent = parent;
//		File rootFile = file;
//		try {
//			Map<String, IRefNode> refnodeMap = LFWConnector
//					.getRefNode(projectPath);
//			if (refnodeMap != null) {
//				Iterator<IRefNode> it = refnodeMap.values().iterator();
//				while (it.hasNext()) {
//					IRefNode refnode = it.next();
//					String id = refnode.getId();
//					if (id.indexOf(".") == -1)
//						new LFWRefNodeTreeItem(parent, refnode, "[参照]");
//					else {
//						String foldername = id.substring(0, id.indexOf("."));
//						String leftfolder = id.substring(id.indexOf(".") + 1);
//						while (leftfolder != null) {
//							File newfile = new File(file, foldername);
//							LFWDirtoryTreeItem treeItem = (LFWDirtoryTreeItem) foldermap
//									.get(newfile);
//							file = newfile;
//							if (treeItem == null) {
//								LFWDirtoryTreeItem newTreeItem = new LFWDirtoryTreeItem(
//										parent, newfile);
//								newTreeItem
//										.setType(LFWDirtoryTreeItem.POOLREFNODEFOLDER);
//								parent = newTreeItem;
//								foldermap.put(newfile, newTreeItem);
//								if (leftfolder.indexOf(".") != -1) {
//									foldername = leftfolder.substring(0,
//											leftfolder.indexOf("."));
//									leftfolder = leftfolder
//											.substring(leftfolder.indexOf(".") + 1);
//								} else {
//									new LFWRefNodeTreeItem(parent, refnode,
//											"[参照]");
//									leftfolder = null;
//								}
//							} else {
//								parent = treeItem;
//								if (leftfolder.indexOf(".") != -1) {
//									foldername = leftfolder.substring(0,
//											leftfolder.indexOf("."));
//									leftfolder = leftfolder
//											.substring(leftfolder.indexOf(".") + 1);
//								} else {
//									new LFWRefNodeTreeItem(parent, refnode,
//											"[参照]");
//									leftfolder = null;
//								}
//							}
//						}
//						parent = orinalparent;
//					}
//					file = rootFile;
//				}
//			}
//		} catch (Throwable e) {
//			MainPlugin.getDefault().logError(e);
//		}
//	}

	/**
	 * 初始化nodes下节点下的子节点
	 * 
	 * @param parent
	 * @param file
	 * @param project
	 */
	public void initNodeSubTree(TreeItem parent, File file, IProject project) {
		File[] childs = file.listFiles();
		int count = childs == null ? 0 : childs.length;
		for (int i = 0; i < count; i++) {
			File child = childs[i];
			if (child.isDirectory()) {
				File[] children = child.listFiles();
				TreeItem tichild = null;
				if (children != null) {
					for (int j = 0; j < children.length; j++) {
						if (children[j].getName().equals("pagemeta.pm")) { //$NON-NLS-1$
							if (tichild == null)
								tichild = new LFWDirtoryTreeItem(parent,
										children[j]);
							continue;
						}
						if (children[j].isDirectory()) {
							if (tichild == null)
								tichild = new LFWDirtoryTreeItem(parent,
										children[j]);
							initNodeSubTree(tichild, children[j], project);
						}
					}
				}
			}
		}
	}

	public IProject getFileOwnProject(Tree tree, File file) {
		IProject project = null;
		TreeItem[] items = tree.getItems();
		int count = items == null ? 0 : items.length;
		for (int i = 0; i < count; i++) {
			if (items[i] instanceof LFWProjectTreeItem) {
				LFWProjectTreeItem item = (LFWProjectTreeItem) items[i];
				if (containsFile(item, file)) {
					project = item.getProjectModel().getJavaProject();
					break;
				}
			}
		}
		return project;
	}

	private boolean containsFile(TreeItem item, File file) {
		Object o = item.getData();
		if (o != null && o instanceof File
				&& ((File) o).getAbsolutePath().equals(file.getAbsolutePath())) {
			return true;
		} else {
			TreeItem[] items = item.getItems();
			int count = items == null ? 0 : items.length;
			for (int i = 0; i < count; i++) {
				if (containsFile(items[i], file)) {
					return true;
				}
			}
			return false;
		}
	}

	class AMCThread extends Thread {

		public AMCThread() {
		}

		@Override
		public void run() {
			LFWTool.checkFirefoxEnvironment();
		}
	}

	protected void buildAppendTree(LFWBasicTreeItem projectRoot,
			IProject project) {
		return;
	}
	
	private List sortMap(Map map){
		List resultList = new ArrayList();
		Object[] ids = map.keySet().toArray();
		Arrays.sort(ids);
		for(Object id:ids){
			resultList.add(map.get(id));
		}
		return resultList;
	}

}
