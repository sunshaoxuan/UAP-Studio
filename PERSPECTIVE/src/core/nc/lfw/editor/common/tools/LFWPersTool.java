package nc.lfw.editor.common.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.application.LFWApplicationTreeItem;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.common.XmlCommonTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.IRefDataset;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.lang.M_lfw_core;
import nc.uap.lfw.perspective.LFWViewSheet;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWProjectModel;
import nc.uap.lfw.perspective.views.LFWViewPage;
import nc.uap.lfw.perspective.webcomponent.LFWBusinessCompnentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWComponentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWProjectTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWVirtualDirTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.core.filesystem.provider.FileInfo;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.ExternalPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Perspective公用方法
 * @author zhangxya
 *
 */
public class LFWPersTool  {
	
	private static String version = null;
	
	private static Dataset[] refdatasets = null;
	
	private static LfwWindow pageMeta;	
	
	private static Boolean supportGDI = false;

	private static LfwView lfwWidget;

	/**当前项目*/
	private static IProject currentProject;

	/**当前项目路径*/
	private static String projectPath = null;
	
	/**当前项目业务组件路径*/
	private static String projectWithBcpPath = null;
	
	private static ComboData[] refcomdatas = null;
	
	private static IRefNode[]  refnodes = null;
	
//	
//	public static String getVersion(){
//		if(version == null)
//			version = NCConnector.getVersion();
//		return version;
//	}
	
	private static Tree tree = null;
	
	private static Map<String, Tree> treeMap = new HashMap<String, Tree>();
	
	/**
	 * 获取所有的根路径
	 * @return
	 */
	public static  String[] getAllRootPackage(){
		IProject proj = getCurrentProject();
		String projName = proj.getName();
		JavaProject javaProj = (JavaProject) JavaCore.create(proj);
		IPackageFragmentRoot[] pfrs = null;;
		try {
			pfrs = javaProj.getAllPackageFragmentRoots();
		} catch (JavaModelException e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
		}
		List<String> rootPackage = new ArrayList<String>(); 
		if(pfrs != null){
			for (int i = 0; i < pfrs.length; i++) {
				if(pfrs[i] instanceof JarPackageFragmentRoot || pfrs[i] instanceof ExternalPackageFragmentRoot)
					continue;
				else if(pfrs[i] instanceof PackageFragmentRoot){
					PackageFragmentRoot root = (PackageFragmentRoot) pfrs[i];
					String rootPath = root.getPath().toString();
					if(rootPath.indexOf(projName) != -1){
						String absPath = root.getPath().removeFirstSegments(1).makeRelative().toString();
						if(!absPath.startsWith("NC_HOME") && absPath.startsWith("src")) //$NON-NLS-1$ //$NON-NLS-2$
							rootPackage.add(absPath);
					}
				}
				
			}
		}
		return (String[])rootPackage.toArray(new String[rootPackage.size()]);
		
	}
//	
//	/**
//	 * 获取关联nc用户信息Map
//	 * @return
//	 */
//	public static Map<String, String> getUserInfoMap(){
//		Map<String, String> userInfoMap = new HashMap<String, String>();
////		String userInfo = LFWPersTool.getUserMessage();
////		if(userInfo != null){
////			String[] userInfos = userInfo.split(":", -1);
////			userInfoMap.put(ExtAttrConstants.ACCOUNTCODE, userInfos[0]);
////			userInfoMap.put(ExtAttrConstants.USERNAME, userInfos[1]);
////			userInfoMap.put(ExtAttrConstants.PASSWORD, userInfos[2]);
////		}
//		return userInfoMap;
//	}
	
	/**
	 * checkout文件
	 */
	 public static void checkOutFile(String path){
			IPath ph = new Path(path);
			IFile ifile =  ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(ph);
			File filea = new File(path);
			//如果文件不可写，checkout,若果未连cc，变为可写
			IWorkbenchPart part = null;
			Shell shell = null;
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if (page != null)
				part = page.getActivePart();
			if(part != null)
				shell = part.getSite().getShell();
			IStatus statu = ResourcesPlugin.getWorkspace().validateEdit(new IFile[]{ifile}, shell);
				if(!filea.canWrite() && !statu.isOK()){
				boolean isWritable = MessageDialog.openConfirm(null, M_lfw_core.LFWPersTool_0, M_lfw_core.LFWPersTool_1);
				if(isWritable){
					try {
						LFWPersTool.silentSetWriterable(path);
					} catch (Exception e) {
						MainPlugin.getDefault().logError(e.getMessage(), e);
						MessageDialog.openInformation(null, M_lfw_core.LFWPersTool_0, e.getMessage());
					}
				}
			}
		 }
	 
//	public static Map<String, Map<String, Dataset>> getAllPoolDs(){
//		String ctx = LFWUtility.getContextFromResource(LFWPersTool.getCurrentProject());
//		return RefDatasetData.getDatasets("/" + ctx);
//	}
//	
//	public static List<String> getAllPoolDsId(){
//		List<String> pooldsIds = new ArrayList<String>();
//		 Map<String, Map<String, Dataset>> input = getAllPoolDs();
//		 for (Iterator<String> it = input.keySet().iterator();it.hasNext();) {
//			String itnext = it.next();
//			Map<String, Dataset> dsMap = input.get(itnext);
//			for (Iterator<String> itch =  dsMap.keySet().iterator(); itch.hasNext();) {
//				pooldsIds.add(itch.next());
//				
//			}
//		}
//		return pooldsIds;
//	}
	
	
	/**
	 * 得到所有的公共池中的片段Id
	 * @return
	 */
//	public static List<String> getAllPoolWidgetId(){
//		List<String> poolWidgetIds = new ArrayList<String>();
//		 Map<String, Map<String, LfwView>> input = getAllPoolWidget();
//		 for (Iterator<String> it = input.keySet().iterator();it.hasNext();) {
//			String itnext = it.next();
//			Map<String, LfwView> widgetMap = input.get(itnext);
//			for (Iterator<String> itch =  widgetMap.keySet().iterator(); itch.hasNext();) {
//				poolWidgetIds.add(itch.next());
//				
//			}
//		}
//		return poolWidgetIds;
//	}
	
	
	/**
	 * 获取公共池里的所有片段
	 * @return
	 */
//	public static Map<String, Map<String, LfwView>> getAllPoolWidget(){
//		String ctx = LFWUtility.getContextFromResource(LFWPersTool.getCurrentProject());
//		return RefDatasetData.getPoolWidgets("/" + ctx);
//	}
	
//	public static Map<String, Map<String, IRefNode>> getAllRefNodeDs(){
//		String ctx = LFWUtility.getContextFromResource(LFWPersTool.getCurrentProject());
//		return LFWConnector.getAllPoolRefNodes("/" + ctx);
//	}
	
	
	public static List<String> getAllPoolRefNodeId(){
		return null;
//		List<String> poolrefnodeIds = new ArrayList<String>();
//		 Map<String, Map<String, IRefNode>> input = getAllRefNodeDs();
//		 for (Iterator<String> it = input.keySet().iterator();it.hasNext();) {
//			String itnext = it.next();
//			Map<String, IRefNode> refnodeMap = input.get(itnext);
//			for (Iterator<String> itch =  refnodeMap.keySet().iterator(); itch.hasNext();) {
//				poolrefnodeIds.add(itch.next());
//				
//			}
//		}
//		return poolrefnodeIds;
	}
	 
	 
	public static List<String> getAllVirtualTreeItems(){
		List<String> virtualList = new ArrayList<String>();
		 Tree tree = getTree();
		 if(tree != null){
			 TreeItem[] treeItems = tree.getItems();
			 for (int i = 0; i < treeItems.length; i++) {
				 getAllChildTreeItems(treeItems[i], virtualList);
			}
		 }
		 return virtualList;
	 }
	
	
	private static void getAllChildTreeItems(TreeItem parent, List<String> virtualList){
		if(parent instanceof LFWVirtualDirTreeItem){
			LFWVirtualDirTreeItem virtual = (LFWVirtualDirTreeItem) parent;
			virtualList.add(virtual.getText());
		}
		TreeItem[] treeItems = parent.getItems();
		for (int i = 0; i < treeItems.length; i++) {
			getAllChildTreeItems(treeItems[i], virtualList);
		}
	}
	
//	/**
//	 * 获取关联nc用户信息
//	 * @return
//	 */
//	public static String getUserMessage(){
// 		IProject project = LFWPersTool.getCurrentProject();
// 		String userInfo = null;
// 		String accountOpn = null;
// 		String userName = null;
// 		String passWord = null;
// 		if(project != null){
//	 		IFile jfile = project.getFile(".module_prj");
//	 		File file = new File(jfile.getLocation().toString());
//	 		InputStream input = null;
//			if (file.exists()) {
//				try {
//					input = new FileInputStream(file);
//					Properties prop = new Properties();
//					prop.load(input);
//					accountOpn = prop.getProperty(NCUserRelatedDialog.ACCOUTN);
//					if(accountOpn == null)
//						return null;
//					userName = prop.getProperty(NCUserRelatedDialog.USERNAME);
//					if(userName == null)
//						return null;
//					passWord = prop.getProperty(NCUserRelatedDialog.PASSWORD);
//					if(passWord == null)
//						return null;
//				} catch (Exception e) {
//					MainPlugin.getDefault().logError("读取node.properties文件出错!", e);
//				}
//				finally{
//					try {
//						if(input != null)
//							input.close();
//					} catch (IOException e) {
//						LfwLogger.error(e.getMessage(), e);
//					}
//				}
//			}
// 		}
// 		userInfo = accountOpn + ":" + userName + ":" + passWord;
// 		return userInfo;
//  	}
	
	public static String getPageModel(){
		String model = ""; //$NON-NLS-1$
		TreeItem currItem = getCurrentTreeItem();
		String cuttentPath = getCurrentFolderPath(currItem);
		File file = new File(cuttentPath + "/" + "node.properties"); //$NON-NLS-1$ //$NON-NLS-2$
		InputStream input = null;
		if (file.exists()) {
			try {
				input = new FileInputStream(file);
				Properties prop = new Properties();
				prop.load(input);
				model = prop.getProperty("model"); //$NON-NLS-1$
			} catch (Exception e) {
				MainPlugin.getDefault().logError(M_lfw_core.LFWPersTool_2, e);
			}
			finally{
				try {
					if(input != null)
						input.close();
				} catch (IOException e) {
					WEBPersPlugin.getDefault().logError(e.getMessage(), e);
				}
			}
		}
		return model;
	}


	public static Dataset[] getRefdatasets() {
		return refdatasets;
	}

	public static void setRefdatasets(Dataset[] refdatasets) {
		LFWPersTool.refdatasets = refdatasets;
	}
	
	
	public static LfwWindow getPageMeta() {
		return pageMeta;
	}

	public static void setPageMeta(LfwWindow pageMeta) {
		LFWPersTool.pageMeta = pageMeta;
	}

	public static boolean isSupportGDI() {
		if (supportGDI == false) {
			try {
				System.loadLibrary("gdiplus"); //$NON-NLS-1$
				supportGDI = Boolean.TRUE;
			} catch (Throwable e) {
				supportGDI = Boolean.FALSE;
			}
		}
		return supportGDI.booleanValue();
	}

	public static LfwView getLfwWidget() {
		return lfwWidget;
	}
	
	public static void setLfwWidget(LfwView lfwWidget) {
		LFWPersTool.lfwWidget = lfwWidget;
	}

	public static IProject getCurrentProject() {
		return currentProject;
	}
	
	public static void setCurrentProject(IProject currentProject) {
		LFWPersTool.currentProject = currentProject;
	}

	public static String getProjectPath() {
		return projectPath;
	}
	
	public static void setProjectPath(String projectPath) {
		LFWPersTool.projectPath = projectPath;
	}

	public static String getProjectWithBcpPath() {
		if (projectWithBcpPath == null || projectWithBcpPath.equals("")) //$NON-NLS-1$
			return projectPath;
		else
			return projectWithBcpPath;
			
	}
	
	
	public static void setProjectWithBcpPath(String projectWithBcpPath) {
		LFWPersTool.projectWithBcpPath = projectWithBcpPath;
	}

	public static List<Dataset> getAllDsExRef(){
		java.util.List<Dataset> list = new ArrayList<Dataset>();
		LfwView widget = getCurrentWidget();
		Dataset[] datasets = widget.getViewModels().getDatasets();
		for (int i = 0; i < datasets.length; i++) {
			Dataset dataset = datasets[i];
			if(!(dataset instanceof IRefDataset))
				list.add(dataset);
		}
		return list;
	}
	
	public static List<Dataset> getAllDatasetInWidget(){
		java.util.List<Dataset> list = new ArrayList<Dataset>();
		LfwView widget = getCurrentWidget();
		Dataset[] datasets = widget.getViewModels().getDatasets();
		list = Arrays.asList(datasets);
		return list;
	}
	
	public static ComboData[]getRefcomdatas() {
		return refcomdatas;
	}
	
	public static void setRefcomdatas(ComboData[] refcomdatas) {
		LFWPersTool.refcomdatas = refcomdatas;
	}
	
	public static IRefNode[] getRefnodes() {
		return refnodes;
	}
	
	public static void setRefnodes(IRefNode[] refnodes) {
		LFWPersTool.refnodes = refnodes;
	}
	
	public static String[] getRefCombdata(){
		java.util.List<String> list = new ArrayList<String>();
		LfwView widget = getCurrentWidget();
		ComboData[]  combodataMap = widget.getViewModels().getComboDatas();
		for (int i = 0; i < combodataMap.length; i++) {
			ComboData compdata = combodataMap[i];
			list.add(compdata.getId());
		}
		return list.toArray(new String[list.size()]);
	}
	
	public static String[] getRefNodes(){
		java.util.List<String> list = new ArrayList<String>();
		LfwView widget = getCurrentWidget();
		IRefNode[]  refNodeMap = widget.getViewModels().getRefNodes();
		for (int i = 0; i < refNodeMap.length; i++) {
			IRefNode refnodeValue = refNodeMap[i];
			list.add(refnodeValue.getId());
		}
		return list.toArray(new String[list.size()]);
	}
	
	
	public static String[] getAllWidgetComonents(){
		java.util.List<String> list = new ArrayList<String>();
		LfwView widget = getCurrentWidget();
		WebComponent[] components = widget.getViewComponents().getComponents();
		for (int i = 0; i < components.length; i++) {
			list.add(components[i].getId());
		}
		return list.toArray(new String[list.size()]);
	}
	
	public static List<LFWProjectTreeItem> getProjectTreeItem(TreeViewer tree){
		List<LFWProjectTreeItem> projectNode = new ArrayList<LFWProjectTreeItem>();
		IProject[] projects = LfwCommonTool.getOpenedLfwProjects();
		int count = projects == null ? 0 : projects.length;
		for (int i = 0; i < count; i++) {
			LFWProjectModel model = new LFWProjectModel(projects[i]);
			LFWProjectTreeItem ti = new LFWProjectTreeItem(tree.getTree(), model);
			projectNode.add(ti);
		}
		return projectNode;
	}
	
	/**
	 * 当前为Pagemeta节点，找子节点
	 * @param widgetId
	 * @return
	 */
	public static LFWWidgetTreeItem getWidgetTreeItemById(String widgetId) {
		TreeItem treeItem = getCurrentTreeItem();
		TreeItem[] childItems = treeItem.getItems();
		for (int i = 0, n = childItems.length; i < n; i++) {
			if (childItems[i] instanceof LFWWidgetTreeItem) {
				if (widgetId.equals(((LFWWidgetTreeItem)childItems[i]).getWidget().getId())) {
					return (LFWWidgetTreeItem)childItems[i];
				}
			}
		}
		return null;
	}
	
	/**
	 * 当前为Widget节点下的子节点，找父节点
	 * @return
	 */
	public static LFWWidgetTreeItem getCurrentWidgetTreeItem() {
		TreeItem treeItem = getCurrentTreeItem();
		return getWidgetTreeItem(treeItem);
	}
	
	public static TreeItem getCurrentTreeItem() {
		Tree tree = getTree();
		TreeItem treeItem = null;
		if(!tree.isDisposed()&&tree.getSelection() != null && tree.getSelection().length > 0){
			treeItem = tree.getSelection()[0];
		}
		return treeItem;
	}
	
	public static String getCurrentFolderPath() {
		TreeItem treeItem = getCurrentTreeItem();
		if(treeItem instanceof LFWApplicationTreeItem){
			treeItem = LFWAMCPersTool.getDefaultWindowTreeItem(null);
		}
		return getCurrentFolderPath(treeItem);
	}
	
	public static String getCurrentFolderPath(TreeItem currItem) {
		TreeItem treeItem = currItem;
		LFWDirtoryTreeItem dirItem = getDirectoryTreeItem(treeItem);
		if(dirItem == null)
			return null;
		File file = dirItem.getFile();
		return file.getPath();
	}
	
	public static LFWDirtoryTreeItem getDirectoryTreeItem(TreeItem item) {
		while(!(item instanceof LFWDirtoryTreeItem)){
			item = item.getParentItem();
		}
		return (LFWDirtoryTreeItem) item;
	}
	
	public static String getCurrentSimpleFolderPath() {
		TreeItem treeItem = getCurrentTreeItem();
		LFWDirtoryTreeItem dirItem = getDirectoryTreeItem(treeItem);
		if(dirItem == null)
			return null;
		File file = dirItem.getFile();
		return file.getName();
	}
	
	public static String getCurrentPagemetaSimpleFolderPath() {
		TreeItem treeItem = getCurrentPageMetaTreeItem();
		LFWDirtoryTreeItem dirItem = getDirectoryTreeItem(treeItem);
		if(dirItem == null)
			return null;
		File file = dirItem.getFile();
		return file.getName();
	}
	
	
	public static Tree getTree() {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow wbw = wb.getActiveWorkbenchWindow();
		IWorkbenchPage wbp = wbw.getActivePage();
		if(wbp != null){
			Tree curTree = treeMap.get(wbp.getPerspective().getId());
			if(curTree != null){
				tree = curTree;
			}
		}
		return tree;
	}
	public static void setTree(Tree tree) {
		LFWPersTool.tree = tree;
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow wbw = wb.getActiveWorkbenchWindow();
		IWorkbenchPage wbp = wbw.getActivePage();
		if(wbp != null){
			treeMap.put(wbp.getPerspective().getId(), tree);
		}
	}
	public static LfwView getCurrentWidget() {
		LFWWidgetTreeItem treeItem = getCurrentWidgetTreeItem();
		if(treeItem == null)
			return null;
		return treeItem.getWidget();
	}
	
	
	public static LfwWindow getCurrentPageMeta() {
		LFWPageMetaTreeItem pmTreeItem = getCurrentPageMetaTreeItem();
		if(pmTreeItem == null){
			pmTreeItem = LFWAMCPersTool.getDefaultWindowTreeItem(null);
		}
		return pmTreeItem.getPm();
	}
	
	public static LFWPageMetaTreeItem getCurrentPageMetaTreeItem() {
		Tree tree = getTree();
		TreeItem treeItem = tree.getSelection()[0];
		if(treeItem instanceof LFWApplicationTreeItem){
			treeItem = LFWAMCPersTool.getDefaultWindowTreeItem(null);
		}
		return getPagemetaTreeItem(treeItem);
	}
	
	public static LFWWidgetTreeItem getWidgetTreeItem(TreeItem treeItem){
		TreeItem parent = treeItem;
		while(!(parent instanceof LFWWidgetTreeItem) && parent != null){
			parent = parent.getParentItem();
		}
		return (LFWWidgetTreeItem)parent;
	}
	
	/**
	 * 查找某树节点父组件
	 * @param treeItem
	 * @return
	 */
	public static LFWBusinessCompnentTreeItem getComponentTreeItem(TreeItem treeItem){
		TreeItem parent = treeItem;
		while(!(parent instanceof LFWBusinessCompnentTreeItem) && parent != null){
			parent = parent.getParentItem();
		}
		return (LFWBusinessCompnentTreeItem)parent;
	}
	
	/**
	 * 查找当前节点父组件
	 * @param treeItem
	 * @return
	 */
	public static LFWBusinessCompnentTreeItem getCurrentBusiCompTreeItem() {
		TreeItem treeItem = getCurrentTreeItem();
		return getComponentTreeItem(treeItem);
	}
	
	/**
	 * 将文件变为可写
	 * @param filename
	 * @throws CoreException
	 */
	 public static void silentSetWriterable(String filename) throws CoreException {
        IFileInfo fileinfo = new FileInfo(filename);
        fileinfo.setAttribute(EFS.ATTRIBUTE_READ_ONLY, false);
        IFileSystem fs = EFS.getLocalFileSystem();
        IFileStore store = fs.fromLocalFile(new File(filename));
        store.putInfo(fileinfo, EFS.SET_ATTRIBUTES, null);
	 }
	
	/**
	 * 获取子项的Pagemeta所在项
	 * @param treeItem
	 * @return
	 */
	public static LFWPageMetaTreeItem getPagemetaTreeItem(TreeItem treeItem){
		TreeItem parent = treeItem;
		
		while(!(parent instanceof LFWPageMetaTreeItem) && parent != null){
			parent = parent.getParentItem();
		}
		return (LFWPageMetaTreeItem)parent;
	}
	
	/**
	 * 获取当前的组件项
	 * @param treeItem
	 * @return
	 */
	public static LFWBusinessCompnentTreeItem getBusiCompTreeItem(TreeItem treeItem){
		TreeItem parent = treeItem;
		while(!(parent instanceof LFWBusinessCompnentTreeItem) && parent != null){
			parent = parent.getParentItem();
		}
		return (LFWBusinessCompnentTreeItem)parent;
	}
	
	/**
	 * 获取Windows节点下所有Window子节点
	 * @author chouhl
	 * @return
	 */
	public static List<TreeItem> getAllWindowTreeItems(IProject project, TreeItem currentTreeItem){
		List<TreeItem> list = new ArrayList<TreeItem>();
		LFWDirtoryTreeItem windowFolder = (LFWDirtoryTreeItem)getWindowDirectoryTreeItem(project, currentTreeItem);
		if(windowFolder != null){
			TreeItem[] nodeTreeItems = windowFolder.getItems();
			for(TreeItem treeItem : nodeTreeItems){
				list.add(treeItem);
			}
		}
		return list;
	}

	/**
	 * 获取Windows（文件夹）节点
	 * @return
	 */
	public static TreeItem getWindowDirectoryTreeItem(IProject project, TreeItem currentTreeItem){
		LFWDirtoryTreeItem windowFolder = null;
		if(project == null){
			project = getCurrentProject();
		}
		if(currentTreeItem == null){
			currentTreeItem = getCurrentTreeItem();
		}
		TreeItem[] nodeTreeItems = null;
		if(isBCPProject(project)){
			LFWBusinessCompnentTreeItem bcpRoot = getComponentTreeItem(currentTreeItem);
			if(bcpRoot != null){
				nodeTreeItems = bcpRoot.getItems();
			}
		}else{
			LFWProjectTreeItem projectRoot = null;
			TreeItem[] treeItems = tree.getItems();
			for (TreeItem treeItem : treeItems) {
				if(((LFWProjectTreeItem) treeItem).getProjectModel().getJavaProject().equals(project)){
					projectRoot = (LFWProjectTreeItem) treeItem;
					break;
				}
			}
			if(projectRoot != null){
				nodeTreeItems = projectRoot.getItems();
			}
		}
		if(nodeTreeItems != null && nodeTreeItems.length > 0){
			for (TreeItem treeItem : nodeTreeItems) {
				if(treeItem != null && treeItem instanceof LFWDirtoryTreeItem){
					if(ILFWTreeNode.WINDOW_FOLDER.equals(((LFWDirtoryTreeItem)treeItem).getType())){
						windowFolder = (LFWDirtoryTreeItem) treeItem;
						break;
					}
				}
			}
		}
		return windowFolder;
	}
	
	public static IViewPart showView(String viewId) {
		IViewPart part = null;
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if (page != null)
				part = page.showView(viewId, null, IWorkbenchPage.VIEW_VISIBLE);
		} catch (PartInitException e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
		}
		return part;
	}
	
	public static void hideView(String viewId){
		IViewPart part = null;
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (page != null){
			part = page.findView(viewId);
		}
		if(part != null){
			page.hideView(part);
		}
	}
	
	public static LFWViewSheet getLFWViewSheet(){
		LFWViewSheet sheet = null;
			try {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				if(page != null){
					sheet = (LFWViewSheet)page.showView(LFWViewSheet.class.getCanonicalName(),null,IWorkbenchPage.VIEW_VISIBLE);
				}
			} catch (PartInitException e) {
				MainPlugin.getDefault().logError(e.getMessage(), e);
			}
		
		return sheet;
	
	}
	
	public static LFWViewPage getLFWViewPage(){
		IPage page = getLFWViewSheet().getCurrentPage();
		if(page instanceof LFWViewPage)
			return (LFWViewPage)page;
		else
			return null;
		
	}

	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$

	public static String formatDateString(Date date) {
		if (date == null) {
			return ""; //$NON-NLS-1$
		}
		String dateStr = sdf.format(date);
		return dateStr;
	}
	
	public static Date parseStringToDate(String dateStr) {
		if (dateStr == null || dateStr.trim().length() == 0)
			return null;
		Date d = null;
		try {
			d = sdf.parse(dateStr);
		} catch (ParseException e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
		}
		return d;
	}

	public static String getWrokspaceDirPath() {
		IPath path = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		String strPath = path.toOSString();
		return strPath;
	}

	public static IProject[] getJavaProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		return projects;
	}
	
//	public static IProject[] getLFwProjects(){
//		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
//		List<IProject> lfwProjects = new ArrayList<IProject>();
//		for (int i = 0; i < projects.length; i++) {
//			try {
//				if(projects[i].hasNature(WEBPersConstants.MODULE_NATURE)){
//					lfwProjects.add(projects[i]);
//				}
//			} 
//			catch (CoreException e) {
//				MainPlugin.getDefault().logError(e);
//				
//			}
//		}
//		return lfwProjects.toArray(new IProject[lfwProjects.size()]);
//	}
	
	/**
	 * 获得所有不包含BCP组件工程的lfw工程
	 * @return
	 */
	public static IProject[] getOpenedLFWNotBCPJavaProjects(){
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<IProject> lfwProjects = new ArrayList<IProject>();
		for (int i = 0; i < projects.length; i++) {
			try {
				if (projects[i].isOpen() && projects[i].hasNature(WEBPersConstants.MODULE_NATURE) && !projects[i].hasNature(WEBPersConstants.BCPMODULE_NATURE))
					if(projects[i].getName().equals("CODE_PROJECT"))
						continue;
					lfwProjects.add(projects[i]);
			} 
			catch (CoreException e) {
				MainPlugin.getDefault().logError(e);
				
			}
		}
		return lfwProjects.toArray(new IProject[lfwProjects.size()]);
	}
	
	/**
	 * 获得所有组件工程
	 * @return
	 */
	public static IProject[] getBcpProject(){
		IProject[] projects = LfwCommonTool.getOpenedLfwProjects();
		List<IProject> bcpProjects = new ArrayList<IProject>();
		for (int i = 0; i < projects.length; i++) {
			try {
				if(projects[i].hasNature(WEBPersConstants.BCPMODULE_NATURE))
					bcpProjects.add(projects[i]);
			} 
			catch (CoreException e) {
				MainPlugin.getDefault().logError(e);
				
			}
		}
		return bcpProjects.toArray(new IProject[bcpProjects.size()]);
	}
	
	/**
	 * 得到打开的所有bcp工程
	 * @return
	 */
	public static IProject[] getOpenedBcpJavaProjects() {
		IProject[] allProjects = getBcpProject();
		ArrayList<IProject> al = new ArrayList<IProject>();
		int count = allProjects == null ? 0 : allProjects.length;
		for (int i = 0; i < count; i++) {
			if (allProjects[i].isOpen()) {
				al.add(allProjects[i]);
			}
		}
		return al.toArray(new IProject[0]);
	}
	
	public static IProject getProjectByName(IProject[] projects,String name){
		for(IProject project:projects){
			if(name.equals(project.getName()))
				return project;
		}
		return null;
	}
	
	
	
	
	public static boolean isPortalProject(IProject project){
		try {
			if(project.hasNature(WEBPersConstants.PORTAL_MODULE_NATURE))
				return true;
		} catch (CoreException e) {
			MainPlugin.getDefault().logError(e);
		}
		return false;
	}
	
	/**
	 * 是否是bcp工程
	 * @param project
	 * @return
	 */
	public static boolean isBCPProject(IProject project){
		try {
			if(project.hasNature(WEBPersConstants.BCPMODULE_NATURE))
				return true;
		} catch (CoreException e) {
			MainPlugin.getDefault().logError(e);
		}
		return false;
	}
	

//	public static IProject[] getOpenedLFWJavaProjects() {
//		IProject[] allProjects = LfwCommonTool.getLfwProjects();
//			//getJavaProjects();
//		ArrayList<IProject> al = new ArrayList<IProject>();
//		int count = allProjects == null ? 0 : allProjects.length;
//		for (int i = 0; i < count; i++) {
//			if (allProjects[i].isOpen()) {
//				al.add(allProjects[i]);
//			}
//		}
//		return al.toArray(new IProject[0]);
//	}
	
	public static IProject[] getAllOpenedJavaProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			//getJavaProjects();
		ArrayList<IProject> al = new ArrayList<IProject>();
		int count = projects == null ? 0 : projects.length;
		for (int i = 0; i < count; i++) {
			if (projects[i].isOpen()) {
				al.add(projects[i]);
			}
		}
		return al.toArray(new IProject[0]);
	}
	
	/**
	 * 错误提示
	 * @param errMsg
	 */
	public static void showErrDlg(String errMsg) {
		if (errMsg != null) {
			Shell parent = new Shell(Display.getCurrent());
			MessageDialog.openError(parent, M_lfw_core.LFWPersTool_3, errMsg);
		}
	}
	
	/**
	 * 获取某项目的moduleName
	 * @param project
	 * @return
	 */
	public static String getProjectModuleName(IProject project) {
		String module = null;
		File f = project.getLocation().toFile();
		File moduleFile = new File(f, ".module_prj"); //$NON-NLS-1$
		if (moduleFile.exists()) {
			InputStream is = null;
			try {
				is = new FileInputStream(moduleFile);
				Properties prop = new Properties();
				prop.load(is);
				module = prop.getProperty("module.name"); //$NON-NLS-1$
			} catch (Exception e) {
				MainPlugin.getDefault().logError(e.getMessage(), e);
			}
			finally{
				try {
					if(is != null)
						is.close();
				} catch (IOException e) {
					MainPlugin.getDefault().logError(e.getMessage(), e);
				}
			}
		}
		return module;
	}
	
	public static String getCurrentProjectModuleName() {
		if (LFWPersTool.projectPath == null)
			return null;
		//当前选中非bcp中的树节点
		if (LFWPersTool.projectWithBcpPath == null ||  LFWPersTool.projectPath.equals(LFWPersTool.projectWithBcpPath)){
			return LFWPersTool.getProjectModuleName(LFWPersTool.currentProject);
			
		}
		//当前选 中树节点在bcp业务组件中
		else{
			//业务组件module名必须与目录名一致
			String module = null;
			module =  LFWPersTool.projectWithBcpPath.substring(LFWPersTool.projectPath.length() + 1);
			return module;
		}
	}

	/**
	 * 保存Pagemeta到文件中
	 * @param widget
	 */
	public static void savePagemeta(LfwWindow pagemeta) {
		// 保存Widget到pagemeta.pm中
		LFWConnector.savePagemetaToXml(getCurrentFolderPath(), "pagemeta.pm", projectPath, pagemeta); //$NON-NLS-1$
	}
	
	/**
	 * 保存widget
	 * @param widget
	 */
	public static void saveWidget(LfwView widget) {
		LFWSaveElementTool.updateView(widget);
	}

	/**
	 * 根据ID获取Pagemeta
	 * @param pmId
	 * @return
	 */
	public static LfwWindow getPagemetaById(String pmId) {
		LfwWindow pm = null;
//		String projectPath = LFWAMCPersTool.getProjectPath();
		// 获取当前Pagemeta
//		if(LFWPersTool.isBCPProject(getCurrentProject())){
//			LFWBusinessCompnentTreeItem busiCompnent = LFWPersTool.getCurrentBusiCompTreeItem();
//			projectPath += "/" + busiCompnent.getText().substring(busiCompnent.getText().indexOf(WEBProjConstants.BUSINESSCOMPONENT) + 
//					WEBProjConstants.BUSINESSCOMPONENT.length() + 1, busiCompnent.getText().length() -1 );
//		}
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put(WebConstant.PROJECT_PATH_KEY, projectPath);
//		paramMap.put(WebConstant.PAGE_ID_KEY, pmId);
		
		try {
			pm = LFWConnector.getPageMeta(pmId);
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
			MessageDialog.openError(null, M_lfw_core.LFWPersTool_3, e.getMessage());
		}
		return pm;
	}
	public static List<LfwWindow> getAllPagemeta(){
		LfwWindow pm = null;
		List<LfwWindow> pmList = new ArrayList<LfwWindow>();
		IProject[] projects = LfwCommonTool.getLfwProjects();
		try{
			for(IProject project:projects){
				String[] bcpNames = LfwCommonTool.getBCPNames(project);
				String projectPath = project.getLocation().toOSString();
				if(bcpNames!=null&&bcpNames.length>0){
					for(String bcpName:bcpNames){
						String bcpPath = projectPath+"/"+bcpName; //$NON-NLS-1$
						String nodePath = bcpPath+"/web/html/nodes"; //$NON-NLS-1$
						File nodeFolders = new File(nodePath);
						if(nodeFolders.exists()){
						for(File subFile:nodeFolders.listFiles()){
							String pmId= subFile.getName();
//							Map<String, Object> paramMap = new HashMap<String, Object>();
//							paramMap.put(WebConstant.PROJECT_PATH_KEY, bcpPath);
//							paramMap.put(WebConstant.PAGE_ID_KEY, pmId);
							pm = LFWConnector.getPageMeta(pmId);
							if(pm!=null) pmList.add(pm);
						}					
						}
					}
				}
				else{
					String nodePath = projectPath+"/web/html/nodes"; //$NON-NLS-1$
					File nodeFolders = new File(nodePath);
					if(nodeFolders.exists()){
						for(File subFile:nodeFolders.listFiles()){
							String pmId= subFile.getName();
//							Map<String, Object> paramMap = new HashMap<String, Object>();
//							paramMap.put(WebConstant.PROJECT_PATH_KEY, projectPath);
//							paramMap.put(WebConstant.PAGE_ID_KEY, pmId);
							pm = LFWConnector.getPageMeta(pmId);
							if(pm!=null) pmList.add(pm);
						}					
					}
				}
			}
		}
		catch (Exception e) {
			WEBPersPlugin.getDefault().logError(e.getMessage());
		}
		return pmList;
	}
	/**
	 * 从所有工程中取得指定id的pagemeta
	 * @param pmId
	 * @return
	 */
	public static LfwWindow getPmByIdFromAll(String pmId){
		LfwWindow pm = null;
		IProject[] projects = LfwCommonTool.getLfwProjects();
		try{
		for(IProject project:projects){
			String[] bcpNames = LfwCommonTool.getBCPNames(project);
			String projectPath = project.getLocation().toOSString();
			if(bcpNames!=null&&bcpNames.length>0){
				for(String bcpName:bcpNames){
					String bcpPath = projectPath+"/"+bcpName; //$NON-NLS-1$
					String nodePath = bcpPath+"/web/html/nodes"; //$NON-NLS-1$
					File nodeFolders = new File(nodePath);
					File node = searchForPageFolder(nodeFolders, pmId);
					if(node!=null){
						pmId = node.getPath().toString().substring(nodePath.length());
					}
					else 
						continue;
//					Map<String, Object> paramMap = new HashMap<String, Object>();
//					paramMap.put(WebConstant.PROJECT_PATH_KEY, bcpPath);
//					paramMap.put(WebConstant.PAGE_ID_KEY, pmId);
					pm = LFWConnector.getPageMeta(pmId);
					if(pm!=null) return pm;
				}
			}
			else{
//				Map<String, Object> paramMap = new HashMap<String, Object>();
//				paramMap.put(WebConstant.PROJECT_PATH_KEY, projectPath);
//				paramMap.put(WebConstant.PAGE_ID_KEY, pmId);
				pm = LFWConnector.getPageMeta(pmId);
				if(pm!=null) return pm;
			}
		}
		}
		catch (Exception e) {
			WEBPersPlugin.getDefault().logError(e.getMessage());
		}
		
		return pm;
	}
	/**
	 * 取得遍历node文件夹取得指定id的pagemeta文件夹
	 * @param file
	 * @param pmId
	 * @return
	 */
	public static File searchForPageFolder(File file,String pmId){
		File pageFile = null;
		if(!file.exists()) return null;
		for(File subFolder:file.listFiles()){
			if(subFolder.isDirectory()){
				pageFile = searchForPageFolder(subFolder,pmId);
				if(pageFile==null) continue;
				else return pageFile;
			}
			else 
				if(subFolder.getName().equals("pagemeta.pm")&&file.getName().equals(pmId)) return file; //$NON-NLS-1$
				else continue;
		}
		return null;
	}
	
	/**
	 * 刷新当前porject
	 * @throws CoreException 
	 */
	public static void refreshCurrentPorject(){
		IProject project = LFWPersTool.getCurrentProject();
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		} 
	}
	
	/**
	 * 取当前树节点所在业务组件path
	 * 
	 * @param treeItem
	 * @return
	 */
	public static String getBcpId(TreeItem treeItem) {
		String bcpPath = getBcpPath(treeItem);
		String bcpId = bcpPath.substring(bcpPath.lastIndexOf("\\")+1,bcpPath.length()); //$NON-NLS-1$
		return bcpId;
	}

	/**
	 * 取当前树节点所在业务组件path
	 * 
	 * @param treeItem
	 * @return
	 */
	public static String getBcpPath(TreeItem treeItem) {
		if (treeItem instanceof LFWBusinessCompnentTreeItem){
			LFWBusinessCompnentTreeItem lfwItem = (LFWBusinessCompnentTreeItem) treeItem;
			return lfwItem.getFile().getPath();
		}
		else{
			TreeItem item =  treeItem.getParentItem();
			while (item != null){
				if (item instanceof LFWBusinessCompnentTreeItem){
					LFWBusinessCompnentTreeItem lfwItem = (LFWBusinessCompnentTreeItem) item;
					return lfwItem.getFile().getPath();
				}
				item = item.getParentItem();
			}
			return null;
		}
	}
	/**
	 * 取得最上层的功能列表节点
	 * @param treeItem
	 * @return
	 */
	public static TreeItem getTopList(TreeItem treeItem){
//		IProject[] projects = getBcpProject();
		while(treeItem.getParentItem() instanceof LFWDirtoryTreeItem){
			treeItem = treeItem.getParentItem();
		}			
		return treeItem;
	}
	/**
	 * 取得最上层的功能列表节点
	 * @param treeItem
	 * @return
	 */
	public static LFWComponentTreeItem getComponentItem(TreeItem treeItem){		
		while(!(treeItem instanceof LFWComponentTreeItem)){
			treeItem = treeItem.getParentItem();
			if(treeItem instanceof LFWProjectTreeItem)
				return null;
		}			
		return (LFWComponentTreeItem)treeItem;
	}
	
	public static void createComponent(String compid){
		Document doc = XmlCommonTool.createDocument();
		Element rootNode = doc.createElement("Component"); //$NON-NLS-1$
		doc.appendChild(rootNode);
		if(compid.indexOf(".")>-1){ //$NON-NLS-1$
			int index = compid.lastIndexOf("."); //$NON-NLS-1$
			rootNode.setAttribute("id", compid.substring(index+1)); //$NON-NLS-1$
			rootNode.setAttribute("name",  compid.substring(index+1)); //$NON-NLS-1$
			rootNode.setAttribute("pack", compid.substring(0,index)); //$NON-NLS-1$
		}
		else{
			rootNode.setAttribute("id", compid); //$NON-NLS-1$
			rootNode.setAttribute("name", compid); //$NON-NLS-1$
			rootNode.setAttribute("pack", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		String path = LFWPersTool.getProjectWithBcpPath()+"/web/html/nodes/"+compid.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		File folder = new File(path);
		File file = new File(folder,"component.cp"); //$NON-NLS-1$
		if(!folder.exists()){
			folder.mkdirs();
		}
    	XmlCommonTool.documentToXml(doc, file);
	}
	
	
}
