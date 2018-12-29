package nc.uap.portal.perspective;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.perspective.project.LFWExplorerTreeBuilder;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
import nc.uap.portal.category.CategoryTreeItem;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.core.PortalDirtoryTreeItem;
import nc.uap.portal.core.PortalTools;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.LookAndFeel;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.om.Skin;
import nc.uap.portal.om.SkinDescription;
import nc.uap.portal.om.Theme;
import nc.uap.portal.page.PortalPageTreeItem;
import nc.uap.portal.perspective.dnd.PortalDragSourceListener;
import nc.uap.portal.perspective.dnd.PortalDropTargetListener;
import nc.uap.portal.portlets.PortletTreeItem;
import nc.uap.portal.skin.SkinTreeItem;
import nc.uap.portal.theme.ThemeTreeItem;
import nc.uap.portal.theme.ThemeTypeTreeItem;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 创建Portal树
 * 
 */

public class PortalTreeBuilder extends LFWExplorerTreeBuilder{
	
	private static PortalTreeBuilder instance = null;
	
	/*拖拽源*/
	private DragSource source = null;
	
	public PortalTreeBuilder() {
		super();
	}
	
	public static PortalTreeBuilder getInstance() {
		synchronized (PortalTreeBuilder.class) {
			if (instance == null)
				instance = new PortalTreeBuilder();
		}
		return instance;
	}
	
	@Override
	protected void buildAppendTree(LFWBasicTreeItem projectRoot, IProject project) {
		File file = projectRoot.getFile();
		try {
			if(project.hasNature(WEBPersConstants.PORTAL_MODULE_NATURE))
			{
				initPortalSubTree(projectRoot, project, file);
			}
		} 
		catch (CoreException e) {
			PortalPlugin.getDefault().logError(e);			
		}
		
//		initPortalSubTree(projectRoot, project, file);
		final Tree tree = projectRoot.getParent();
		/*增加拖放事件*/
	    Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
	    int operations = DND.DROP_MOVE;
	    if (source == null){
	    	source = new DragSource(tree, operations);
	    	source.setTransfer(types);
	    	final TreeItem[] dragSourceItem = new TreeItem[1];
	    	PortalDragSourceListener portalDragSourceListener = new PortalDragSourceListener();
	    	portalDragSourceListener.setTree(tree);
	    	portalDragSourceListener.setDragSourceItem(dragSourceItem);
	    	source.addDragListener(portalDragSourceListener);
	    	
	    	DropTarget target = new DropTarget(tree, operations);
	    	target.setTransfer(types);
	    	PortalDropTargetListener portalDropTargetListener = new PortalDropTargetListener();
	    	portalDropTargetListener.setDragSourceItem(dragSourceItem);
	    	target.addDropListener(portalDropTargetListener);
	    }
	}
	
//	/**
//	 * 初始化portal树
//	 * 
//	 */
//	@Override
//	protected void initProjectTree(LFWBasicTreeItem projectRoot,String projectPath, Map<String, String> pageNames, IProject project) {
//		File file = projectRoot.getFile();
//		initPortalSubTree(projectRoot, project, file);
//
//		final Tree tree = projectRoot.getParent();
//		/*增加拖放事件*/
//	    Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
//	    int operations = DND.DROP_MOVE;
//	    if (source == null){
//	    	source = new DragSource(tree, operations);
//	    	source.setTransfer(types);
//	    	final TreeItem[] dragSourceItem = new TreeItem[1];
//	    	PortalDragSourceListener portalDragSourceListener = new PortalDragSourceListener();
//	    	portalDragSourceListener.setTree(tree);
//	    	portalDragSourceListener.setDragSourceItem(dragSourceItem);
//	    	source.addDragListener(portalDragSourceListener);
//	    	
//	    	DropTarget target = new DropTarget(tree, operations);
//	    	target.setTransfer(types);
//	    	PortalDropTargetListener portalDropTargetListener = new PortalDropTargetListener();
//	    	portalDropTargetListener.setDragSourceItem(dragSourceItem);
//	    	target.addDropListener(portalDropTargetListener);
//	    }
//		super.initProjectTree(projectRoot, projectPath, pageNames, project);	
//	}
//	
	/**
	 * 鼠标按下事件
	 * 
	 */
	protected void otherMouseDown(Tree tree,TreeItem item){
		while(!(item instanceof PortalDirtoryTreeItem)){
			item = item.getParentItem();
		}
		PortalDirtoryTreeItem portalItem = (PortalDirtoryTreeItem) item;
		IProject project = getFileOwnProject(tree, portalItem.getFile());
		LFWPersTool.setCurrentProject(project);
		String projectPath = project.getLocation().toString();
		LFWPersTool.setProjectPath(projectPath);
		LFWPersTool.setProjectWithBcpPath(LFWPersTool.getBcpPath(item));
	}
	
	/**
	 * 取所有打开的Portal非组件项目
	 */
	@Override
	protected IProject[] getOpenedLFWNotBCPJavaProjects(){
		return PortalTools.getPortalNotBCPJavaProjects();
	}
	
	/**
	 * 取所有打开的Portal组件项目
	 */
	@Override
	protected IProject[] getOpenedBcpJavaProjects(){
		return PortalTools.getPortalBcpProject();
	}
	
	/**
	 * 初始化portal树
	 * 
	 * @param projectRoot
	 * @param project
	 * @param file
	 */
	public void initPortalSubTree(LFWBasicTreeItem projectRoot, IProject project, File file){
		File portalDefineFile = new File(file, PortalProjConstants.PORTALDEFINE);
		PortalDirtoryTreeItem portalDefineNode = new PortalDirtoryTreeItem(projectRoot, portalDefineFile);
		portalDefineNode.setType(PortalDirtoryTreeItem.PORTAL_DEFINE);
		
		//Portal 页面
		File portalPageFile = new File(file, PortalProjConstants.PORTALPAGE);
		PortalDirtoryTreeItem portaPageNode = new PortalDirtoryTreeItem(portalDefineNode, portalPageFile);
		portaPageNode.setType(PortalDirtoryTreeItem.PORTALPAGE);
		//initPageNodeTree(portaPageNode, project);
			
		//portlet分类节点
		File portletsFile = new File(file, PortalProjConstants.PORTLETS);
		PortalDirtoryTreeItem portletNode = new PortalDirtoryTreeItem(portalDefineNode, portletsFile);
		portletNode.setType(PortalDirtoryTreeItem.PORTLETS);
		//initCategory(portletNode, project);
		
		//功能节点
//		File managerAppFile = new File(file, PortalProjConstants.MANAGERAPPS);
//		PortalDirtoryTreeItem managerAppNode = new PortalDirtoryTreeItem(portalDefineNode, managerAppFile);
//		managerAppNode.setType(PortalDirtoryTreeItem.MANAGERAPPS);
//		initManagerApps(managerAppNode,project);

		//主题
		File themesFile = new File(file, PortalProjConstants.THEMES);
		PortalDirtoryTreeItem themesNode = new PortalDirtoryTreeItem(portalDefineNode, themesFile);
		themesNode.setType(PortalDirtoryTreeItem.THEMES);	
		
		//插件
//		File pluginFile = new File(file, PortalProjConstants.PLUGIN);
//		PortalDirtoryTreeItem pluginNode = new PortalDirtoryTreeItem(portalDefineNode, pluginFile);
//		pluginNode.setType(PortalDirtoryTreeItem.PLUGIN);
//		pluginNode.setImage(MainPlugin.loadImage(MainPlugin.ICONS_PATH, "page.gif").createImage());

		//事件
		File eventsFile = new File(file, PortalProjConstants.EVENTS);
		PortalDirtoryTreeItem eventsNode = new PortalDirtoryTreeItem(portalDefineNode, eventsFile);
		eventsNode.setType(PortalDirtoryTreeItem.EVENTS);
		eventsNode.setImage(ImageProvider.portalpage);

		//PORTAL
//		File portalFile = new File(file, PortalProjConstants.PORTAL);
//		PortalDirtoryTreeItem portalNode = new PortalDirtoryTreeItem(portalDefineNode, portalFile);
//		portalNode.setType(PortalDirtoryTreeItem.PORTAL);	
//		portalNode.setImage(MainPlugin.loadImage(MainPlugin.ICONS_PATH, "page.gif").createImage());

		//WEBCONFIG
//		File webFile = new File(file, WEBCONFIG);
//		PortalDirtoryTreeItem webNode = new PortalDirtoryTreeItem(portalDefineNode, webFile);
//		webNode.setType(PortalDirtoryTreeItem.WEBCONFIG);	
//		webNode.setImage(MainPlugin.loadImage(MainPlugin.ICONS_PATH, "page.gif").createImage());
	}
	
	/**
	 * 初始化功能节点
	 * 
	 * @param managerAppNode
	 * @param project
	 */
	public void initManagerApps(PortalDirtoryTreeItem parent, String moduleName) {
//		List<ManagerApps> managerApps = PortalConnector.getAllManagerApps(LFWPersTool.getProjectWithBcpPath(), moduleName);
//		if (managerApps == null)
//			return;
//		for (ManagerApps m : managerApps){
//			ManagerAppsTreeItem maTreeItem =  new ManagerAppsTreeItem(parent,m); 
//			for (ManagerCategory mc : m.getCategory()){
//				ManagerCategoryTreeItem mcTreeItem = new ManagerCategoryTreeItem(maTreeItem,m.getId(),mc);
//				initManagerCategory(mcTreeItem, m.getId(),mc);
//			}
//		}
	}

	/**
	 * 初始化主题
	 * 
	 * @param parent
	 * @param project
	 */
	public void initTheme(PortalDirtoryTreeItem parent, String moduleName) {
		//moduleName为null或""时读取NCHOME下的主题列表，否则读取当前业务组件下的主题列表
		LookAndFeel curLookAndFeel = PortalConnector.getLookAndFeel(LFWPersTool.getProjectWithBcpPath(), moduleName);
		LookAndFeel lookAndFeel = PortalConnector.getLookAndFeel(LFWPersTool.getProjectWithBcpPath(), null);
		if (lookAndFeel == null||lookAndFeel.getTheme()==null)
			return;
		for (Theme thm : lookAndFeel.getTheme()){
			ThemeTreeItem  themeTreeItem  = new ThemeTreeItem(parent,thm,isCurrentBCPTheme(curLookAndFeel, thm));
			initSkins(themeTreeItem, moduleName);
		}
	}
	
	/**
	 * 判断当前主题是否存在于给定的LookAndFeel中
	 * 
	 * @param cur
	 * @param theme
	 * @return 
	 */
	private boolean isCurrentBCPTheme(LookAndFeel cur,Theme theme){
		boolean iscurrent = false;
		if(cur == null || cur.getTheme() == null){
			return false;
		}
		for(Theme thm:cur.getTheme()){
			if(theme.getId().equals(thm.getId())){
				iscurrent = true;
				break;
			}
		}
		return iscurrent;
	}
	
	/**
	 * 初始化样式
	 * 
	 * @param parent
	 * @param project
	 */
	private void initSkins(ThemeTreeItem parent, String moduleName) {
		String themeId = ((Theme)parent.getData()).getId();
		
		ThemeTypeTreeItem layoutItem = new ThemeTypeTreeItem(parent,(Theme)parent.getData(),PortalProjConstants.TYPE_LAYOUT);
		SkinDescription layoutDesc =  PortalConnector.getSkinDescription(LFWPersTool.getProjectWithBcpPath(), moduleName, PortalProjConstants.TYPE_LAYOUT, themeId);
		List<Skin> layoutSkin = layoutDesc==null?null:layoutDesc.getSkin();
		if (layoutSkin != null){
			for (Skin s : layoutSkin){
				new SkinTreeItem(layoutItem,s);
			}
		}
		
		ThemeTypeTreeItem pageItem = new ThemeTypeTreeItem(parent,(Theme)parent.getData(),PortalProjConstants.TYPE_PAGE);  
		SkinDescription pageDesc = PortalConnector.getSkinDescription(LFWPersTool.getProjectWithBcpPath(), moduleName, PortalProjConstants.TYPE_PAGE, themeId); 
		List<Skin> pageSkin = pageDesc==null?null:pageDesc.getSkin();
		if (pageSkin != null){
			for (Skin s : pageSkin){
				new SkinTreeItem(pageItem,s); 
			}
		}

		ThemeTypeTreeItem portletItem = new ThemeTypeTreeItem(parent,(Theme)parent.getData(),PortalProjConstants.TYPE_PORTLET);  
		SkinDescription portletDesc = PortalConnector.getSkinDescription(LFWPersTool.getProjectWithBcpPath(), moduleName, PortalProjConstants.TYPE_PORTLET, themeId); 
		List<Skin> portletSkin = portletDesc==null?null:portletDesc.getSkin();
		if (portletSkin != null){
			for (Skin s : portletSkin){
				new SkinTreeItem(portletItem,s); 
			}
		}
	}
	
	/**
	 * 初始化功能管理
	 * 
	 * @param parent
	 * @param id
	 * @param managerCategory
	 */
//	private void initManagerCategory(ManagerCategoryTreeItem parent,
//			String id, ManagerCategory managerCategory) {
//		for (ManagerCategory mc : managerCategory.getCategory()){
//			ManagerCategoryTreeItem mcTreeItem = new ManagerCategoryTreeItem(parent,id,mc);
//			initManagerCategory(mcTreeItem, id, mc);
//		}
//	}


	/**
	 * 初始化Page
	 * 
	 * @param parent
	 * @param project
	 */
	public void initPageNodeTree(TreeItem parent, String moduleName) {
		Page[] pages = PortalConnector.getAllPages(LFWPersTool.getProjectWithBcpPath(), moduleName);
		if (pages != null){
			for (int i=0;i<pages.length;i++){
				new PortalPageTreeItem(parent, pages[i]);
			}
		}
	}
	
	/**
	 *初始化portlet分类
	 * 
	 * @param parent
	 * @param project
	 */
	public void initCategory(PortalDirtoryTreeItem parent, String moduleName) {
		
		/*是否需要保存*/
		Boolean needSave = false;

		Display display = PortalConnector.getDisplay(LFWPersTool.getProjectWithBcpPath(),moduleName);
		List<PortletDefinition> portlets = PortalConnector.getAllPortlet(LFWPersTool.getProjectWithBcpPath(), moduleName);

		if (display == null && portlets == null)
			return;
		//如果没有portlet分类,所有portlet放到根节点下
		if(display == null || display.getCategory().size()==0){
			for (PortletDefinition p: portlets){
				new PortletTreeItem(parent, p);	
			}
		}
		else{
			List<PortletDisplayCategory> category = display.getCategory();
			Map<String, PortletDefinition> portletMap = new HashMap<String, PortletDefinition>();
			if(portlets!=null){
				for (PortletDefinition p: portlets){
					portletMap.put(p.getPortletName(), p);
				}
			}
			for (PortletDisplayCategory portletDisplayCategory : category){
				CategoryTreeItem cti = new CategoryTreeItem(parent, portletDisplayCategory);
				for (int i=portletDisplayCategory.getPortletDisplayList().size()-1 ; i>-1; i--){
					if (portletMap.containsKey(portletDisplayCategory.getPortletDisplayList().get(i).getId())){
						new PortletTreeItem(cti, portletMap.get(portletDisplayCategory.getPortletDisplayList().get(i).getId()));
						portletMap.remove(portletDisplayCategory.getPortletDisplayList().get(i).getId());
					}
					else{
						portletDisplayCategory.getPortletDisplayList().remove(portletDisplayCategory.getPortletDisplayList().get(i));
						needSave = true;
					}
				}
			}
			for (PortletDefinition p : portletMap.values()){
				new PortletTreeItem(parent, p);
			}
			if (needSave){
	    		PortalConnector.saveDisplayToXml(LFWPersTool.getProjectWithBcpPath(), moduleName, display);
			}
		}
	}

}
