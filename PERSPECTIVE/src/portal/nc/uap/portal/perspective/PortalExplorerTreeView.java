package nc.uap.portal.perspective;

import java.util.List;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
//import nc.uap.portal.category.CategoryEditor;
//import nc.uap.portal.category.CategoryEditorInput;
import nc.uap.portal.category.CategoryTreeItem;
import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.core.PortalBasicTreeItem;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.core.PortalDirtoryTreeItem;
import nc.uap.portal.deploy.vo.PortalModule;
import nc.uap.portal.events.EventsEditor;
import nc.uap.portal.events.EventsEditorInput;
import nc.uap.portal.lang.M_perspective;
import nc.uap.portal.om.LookAndFeel;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.om.Skin;
import nc.uap.portal.om.Theme;
import nc.uap.portal.page.PortalPageEditor;
import nc.uap.portal.page.PortalPageEditorInput;
import nc.uap.portal.page.PortalPageTreeItem;
import nc.uap.portal.perspective.action.RefreshPortalExplorerTreeAction;

import nc.uap.portal.portalmodule.PortalModuleEditor;
import nc.uap.portal.portalmodule.PortalModuleEditorInput;
import nc.uap.portal.portlets.PortletEditor;
import nc.uap.portal.portlets.PortletEditorInput;
import nc.uap.portal.portlets.PortletTreeItem;
import nc.uap.portal.portlets.action.ExportPageToPortletAction;
import nc.uap.portal.skin.SkinEditor;
import nc.uap.portal.skin.SkinEditorInput;
import nc.uap.portal.skin.SkinTreeItem;
import nc.uap.portal.theme.ThemeEditor;
import nc.uap.portal.theme.ThemeEditorInput;
import nc.uap.portal.theme.ThemeTreeItem;
import nc.uap.portal.theme.ThemeTypeTreeItem;
import nc.uap.portal.webconfig.WebConfigEditor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import uap.lfw.smartmw.StartEmbedTomcat;

/**
 * Portal 浏览器
 * 
 * @since 1.6
 *
 */

public class PortalExplorerTreeView extends LFWExplorerTreeView{
	
	public PortalExplorerTreeView() {
		super();
		LFWExplorerTreeView.EXTEND_VIEWID=PortalExplorerTreeView.class.getCanonicalName();
	}
	
	/**
	 * 初始化portal树
	 * 
	 */
	@Override
	protected void initTree() {
		PortalTreeBuilder.getInstance().buildTree(treeView.getTree());
	}
	
	/**
	 * 扩展lfw树节点右键菜单
	 * 
	 */
	@Override
	protected void treeItemMenuListenerExtends (TreeItem ti, IMenuManager manager) {
		/*对page节点扩展，增加生成portlet功能*/
		final ExportPageToPortletAction exportPageToPortletAction = new ExportPageToPortletAction();
		if (ti instanceof LFWPageMetaTreeItem) {
			IContributionItem[] cb =  manager.getItems();
			for (int i = 0 ; i<cb.length ; i ++){
				if (cb[i] instanceof MenuManager && ((MenuManager)cb[i]).getMenuText().equals(WEBPersConstants.PUBLISH_NODE)){
					((MenuManager)cb[i]).addMenuListener(new IMenuListener() {
						public void menuAboutToShow(IMenuManager manager) {
							manager.add(exportPageToPortletAction);
						}
					});
					break;
				} 
			}
		}
	}
	
	/**
	 * 树节点增加右键菜单
	 * 
	 */
	@Override
	protected void othersTreeItemMenuListener(TreeItem ti, IMenuManager manager) {
		if (ti instanceof PortalBasicTreeItem){
			((PortalBasicTreeItem)ti).addMenuListener(manager);
		}
	}
	
	/**
	 * 处理Portal定义中TreeItem鼠标双击事件
	 */
	@Override
	protected void otherMouseDoubleClick(TreeItem ti) {
		if (ti instanceof PortalBasicTreeItem){
			((PortalBasicTreeItem)ti).mouseDoubleClick();
		}
	}
	
	/**
	 * 取Portal浏览器,如果不存在，则创建一个
	 * 
	 * @param page
	 * @return
	 */
	public static PortalExplorerTreeView getPortalExploerTreeView(IWorkbenchPage page) {
		if (page == null) {
			if (PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage() != null) {
				page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage();
			}
		}
		if (page != null) {
			PortalExplorerTreeView view = (PortalExplorerTreeView) page
					.findView(PortalExplorerTreeView.class.getCanonicalName());
			if (view == null) {
				try {
					view = (PortalExplorerTreeView) page
							.showView(PortalExplorerTreeView.class
									.getCanonicalName());
				} catch (PartInitException e) {
					PortalPlugin.getDefault().logError(e.getMessage(), e);
				}
			}
			return view;
		} else
			return null;
	}
	
	/**
	 * 设置Portal Explorer 的 ToolBar
	 * 
	 */
	@Override
	protected void dealOthersToolBar(ToolBarManager tbm){
		RefreshPortalExplorerTreeAction refresh = new RefreshPortalExplorerTreeAction();
		tbm.removeAll();
		tbm.add(refresh);
	}
	
	/**
	 * 关闭Editor
	 * 
	 */
	@Override
	public void closeOpenedEidtor(TreeItem treeItem){
		//删除节点后关闭编辑器
		if (treeItem instanceof PortalBasicTreeItem){
			IWorkbenchPage page = getViewSite().getPage();
			IEditorInput input = ((PortalBasicTreeItem) treeItem).getEditorInput();
			if (null != input) {
				IEditorPart editor = page.findEditor(input);
				if (null != editor) {
					page.closeEditor(editor, false);
				}
			}
		}
		else
			super.closeOpenedEidtor(treeItem);
	}
	
	/**
	 * 打开事件编辑器
	 */
	@SuppressWarnings("deprecation")
	public void openEventsEditor(PortalDirtoryTreeItem dir){
		String editorid  = EventsEditor.class.getName();
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		PortletApplicationDefinition  portletApp = PortalConnector.getPortletApp(projectPath,projectModuleName);
		if (portletApp==null){
			portletApp = new PortletApplicationDefinition(); 
		}
		EventsEditorInput editorinput = new EventsEditorInput(portletApp);
		if (editorinput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		IEditorPart[] parts = workbenchPage.getEditors();
		for (int i = 0; i < parts.length; i++) {
			IEditorInput editorInput = (IEditorInput)parts[i].getEditorInput();
			if(editorInput instanceof EventsEditorInput){
				editor =  parts[i];
				break;
//				EventsEditorInput evEditorInput = (EventsEditorInput)editorInput;
//				PortletApplicationDefinition pa = (PortletApplicationDefinition) evEditorInput.getPortletApp();
//				String name = pa.getName();
//				if(portletApp.getName().equals(name)){
//					editor =  parts[i];
//					break;
//				}
			}
		}
		if (editor != null)
			workbenchPage.bringToTop(editor);
		else {
			try {
				editorinput.setCurrentTreeItem(dir);
				dir.setEditorInput(editorinput);
				workbenchPage.openEditor(editorinput, editorid);
			} catch (PartInitException e1) {
				PortalPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	/**
	 * 打开web配置
	 */
	@SuppressWarnings("deprecation")
	public void openWebConfigEditor(PortalDirtoryTreeItem dir){
		String editorid  = WebConfigEditor.class.getName();
		IProject project = LFWPersTool.getCurrentProject();
		IFile file = project.getFile("/web/WEB-INF/portal.part"); //$NON-NLS-1$

		FileEditorInput editorinput = new FileEditorInput(file);
		if (editorinput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		IEditorPart[] parts = workbenchPage.getEditors();
		for (int i = 0; i < parts.length; i++) {
			IEditorInput input = (IEditorInput)parts[i].getEditorInput();
			if(input instanceof FileEditorInput){
				if (((FileEditorInput)input).getFile().getFullPath().equals(file.getFullPath())){
					editor =  parts[i];
					break;
				}
			}
		}
		if (editor != null)
			workbenchPage.bringToTop(editor);
		else {
			try {
				workbenchPage.openEditor(editorinput, editorid);
			} catch (PartInitException e1) {
				PortalPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}
	
	/**
	 * 打开 PortalModuleEditor
	 */
	@SuppressWarnings("deprecation")
	public void openPortalModuleEditor(PortalDirtoryTreeItem dir){
		String editorid  = PortalModuleEditor.class.getName();
		String projectPath = LFWPersTool.getProjectWithBcpPath();
//		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		PortalModule  portalModule = PortalConnector.getPortal(projectPath, projectModuleName);
		if (portalModule ==null){
			portalModule = new PortalModule(); 
			portalModule.setModule(projectModuleName);
		}
		PortalModuleEditorInput editorinput = new PortalModuleEditorInput(portalModule,projectModuleName);
		if (editorinput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		IEditorPart[] parts = workbenchPage.getEditors();
		for (int i = 0; i < parts.length; i++) {
			IEditorInput editorInput = (IEditorInput)parts[i].getEditorInput();
			if(editorInput instanceof ThemeEditorInput){
				PortalModuleEditorInput pEditorInput = (PortalModuleEditorInput)editorInput;
				String moduleName = pEditorInput.getModuleName();
				if(projectModuleName.equals(moduleName)){
					editor =  parts[i];
					break;
				}
			}
		}
		if (editor != null)
			workbenchPage.bringToTop(editor);
		else {
			try {
				workbenchPage.openEditor(editorinput, editorid);
			} catch (PartInitException e1) {
				PortalPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	/**
	 * 增加主题
	 */	
	public TreeItem addThemeTreeNode(Theme theme) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception(M_perspective.PortalExplorerTreeView_0);
		TreeItem selTI = selTIs[0];
		TreeItem plTreeItem = new ThemeTreeItem(selTI, theme);
		selTI.setExpanded(true);
		return plTreeItem;
	}
	
	/**
	 * 删除选中主题
	 */
	public void deleteSelectThemeTreeNode()throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception(M_perspective.PortalExplorerTreeView_0);
		TreeItem selTI = selTIs[0];
		if (selTI instanceof ThemeTreeItem) {
			Shell shell = new Shell(Display.getCurrent());
			if (!MessageDialog.openConfirm(shell, M_perspective.PortalExplorerTreeView_1, M_perspective.PortalExplorerTreeView_2
					+ selTI.getText() + M_perspective.PortalExplorerTreeView_3))
				return;
			else{
				((ThemeTreeItem) selTI).deleteNode((ThemeTreeItem)selTI);
			}
		}	
	}
	
	/**
	 * 打开 ThemeEditor
	 */
	@SuppressWarnings("deprecation")
	public void openThemeEditor(PortalDirtoryTreeItem dir){
		String editorid  = ThemeEditor.class.getName();
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		LookAndFeel  lookAndFeel = PortalConnector.getLookAndFeel(projectPath, projectModuleName);
		if (lookAndFeel ==null){
			lookAndFeel = new LookAndFeel(); 
		}
		ThemeEditorInput editorinput = new ThemeEditorInput(lookAndFeel,projectModuleName);
		if (editorinput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		IEditorPart[] parts = workbenchPage.getEditors();
		for (int i = 0; i < parts.length; i++) {
			IEditorInput editorInput = (IEditorInput)parts[i].getEditorInput();
			if(editorInput instanceof ThemeEditorInput){
				ThemeEditorInput pEditorInput = (ThemeEditorInput)editorInput;
				String moduleName = pEditorInput.getModuleName();
				if(projectModuleName.equals(moduleName)){
					editor =  parts[i];
					break;
				}
			}
		}
		if (editor != null)
			workbenchPage.bringToTop(editor);
		else {
			try {
				workbenchPage.openEditor(editorinput, editorid);
			} catch (PartInitException e1) {
				PortalPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}
	
	/**
	 * 新建样式
	 * 
	 */	
	public TreeItem addSkinTreeNode(Skin skin) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception(M_perspective.PortalExplorerTreeView_0);
		TreeItem selTI = selTIs[0];
		TreeItem plTreeItem = new SkinTreeItem(selTI, skin);
		selTI.setExpanded(true);
		return plTreeItem;
	}
	
	/**
	 * 删除选中样式
	 */
	public void deleteSelectSkinTreeNode()throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception(M_perspective.PortalExplorerTreeView_0);
		TreeItem selTI = selTIs[0];
		if (selTI instanceof SkinTreeItem) {
			Shell shell = new Shell(Display.getCurrent());
			if (!MessageDialog.openConfirm(shell, M_perspective.PortalExplorerTreeView_1, M_perspective.PortalExplorerTreeView_2
					+ selTI.getText() + M_perspective.PortalExplorerTreeView_3))
				return;
			else{
				((SkinTreeItem) selTI).deleteNode();
			}
		}	
	}
	
	/**
	 * 打开样式编辑器
	 */
	@SuppressWarnings("deprecation")
	public void openSkinEditor(SkinTreeItem treeItem){
		String editorid  = SkinEditor.class.getName();

		ThemeTypeTreeItem typeItem =  (ThemeTypeTreeItem)treeItem.getParentItem();
		String type = typeItem.getType();
		String themeId = ((Theme)typeItem.getData()).getId();
		SkinEditorInput editorinput = new SkinEditorInput(themeId,type,((Skin)treeItem.getData()).getId());
		if (editorinput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		IEditorPart[] parts = workbenchPage.getEditors();
		for (int i = 0; i < parts.length; i++) {
			IEditorInput editorInput = (IEditorInput)parts[i].getEditorInput();
			if(editorInput instanceof SkinEditorInput){
				SkinEditorInput pEditorInput = (SkinEditorInput)editorInput;
				String id = pEditorInput.getId();
				if(id.equals(((Skin)treeItem.getData()).getId())){
					editor =  parts[i];
					break;
				}
			}
		}
		if (editor != null)
			workbenchPage.bringToTop(editor);
		else {
			try {
				workbenchPage.openEditor(editorinput, editorid);
			} catch (PartInitException e1) {
				PortalPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}
	


	/**
	 * 增加Portlet分类
	 */	
	public TreeItem addCategoryTreeNode(String id,String text,String i18nName) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception(M_perspective.PortalExplorerTreeView_0);
		TreeItem selTI = selTIs[0];
		PortletDisplayCategory p = new PortletDisplayCategory();
		p.setId(id);
		p.setText(text);
		p.setI18nName(i18nName);
		TreeItem[] subItems = selTI.getItems();
		int i;
		for (i = 0 ; i< subItems.length ; i++){
			if (subItems[i] instanceof PortletTreeItem){
				break;
			}
		}
		TreeItem plTreeItem = new CategoryTreeItem(selTI, p,i);
		selTI.setExpanded(true);
		return plTreeItem;
	}

	/**
	 * 删除选中Portlet分类
	 */
	public void deleteSelectCategoryTreeNode()throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception(M_perspective.PortalExplorerTreeView_0);
		TreeItem selTI = selTIs[0];
		if (selTI instanceof CategoryTreeItem) {
			Shell shell = new Shell(Display.getCurrent());
			if (!MessageDialog.openConfirm(shell, M_perspective.PortalExplorerTreeView_1, M_perspective.PortalExplorerTreeView_2
					+ selTI.getText() + M_perspective.PortalExplorerTreeView_3))
				return;
			else{
				closeOpenedEidtor(selTI);
				((CategoryTreeItem) selTI).deleteNode();
			}
		}	
	}
	
	/**
	 * 打开portlet编辑器
	 */
	@SuppressWarnings("deprecation")
	public void openPortletEditor(PortletTreeItem treeItem){
		PortletDefinition portlet = (PortletDefinition) treeItem.getData();
		String categoryId = null;
		TreeItem parent = treeItem.getParentItem();
		if (parent instanceof CategoryTreeItem){
			PortletDisplayCategory p = (PortletDisplayCategory)parent.getData();
			categoryId = p.getId();
		}
		PortletEditorInput editorinput = new PortletEditorInput(portlet,categoryId);
		editorinput.setCurrentTreeItem(treeItem);
		treeItem.setEditorInput(editorinput);
		String editorid  = PortletEditor.class.getName();
		if (editorinput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		String editorinputid = editoridMap.get("portlet" + portlet.getPortletName()); //$NON-NLS-1$
		if (editorinputid != null){
			IEditorPart[] parts = workbenchPage.getEditors();
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput)parts[i].getEditorInput();
				if(editorInput instanceof PortletEditorInput){
					PortletEditorInput pfEditorInput = (PortletEditorInput)editorInput;
					PortletDefinition pfnew = (PortletDefinition) pfEditorInput.getPortlet();
					String name = pfnew.getPortletName();
					if(portlet.getPortletName().equals(name)){
						editor =  parts[i];
						break;
					}
				}
			}
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(editorinput, editorid);
					} catch (PartInitException e1) {
				}
			}
		}
		else {
			editoridMap.put("portlet" + portlet.getPortletName(), "portlet" + portlet.getPortletName()); //$NON-NLS-1$ //$NON-NLS-2$
			try {
				workbenchPage.openEditor(editorinput, editorid);
				} catch (PartInitException e1) {
					PortalPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}
	
	/**
	 * 增加Portlet节点
	 */	
	public TreeItem addPortletTreeNode(PortletDefinition portlet) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception(M_perspective.PortalExplorerTreeView_0);
		TreeItem selTI = selTIs[0];
		
		TreeItem plTreeItem = new PortletTreeItem(selTI, portlet);
		selTI.setExpanded(true);
		return plTreeItem;
	}

	/**
	 * 删除选中Portlet
	 */
	public void deleteSelectPortletTreeNode()throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception(M_perspective.PortalExplorerTreeView_0);
		TreeItem selTI = selTIs[0];
		if (selTI instanceof PortletTreeItem) {
			Shell shell = new Shell(Display.getCurrent());
			if (!MessageDialog.openConfirm(shell, M_perspective.PortalExplorerTreeView_1, M_perspective.PortalExplorerTreeView_2
					+ selTI.getText() + M_perspective.PortalExplorerTreeView_3))
				return;
			 closeOpenedEidtor(selTI);
			((PortletTreeItem) selTI).deleteNode();
		}
	}
	
	/**
	 * 增加 PageNode
	 */	
	public TreeItem addPageTreeNode(Page page) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception(M_perspective.PortalExplorerTreeView_0);
		TreeItem selTI = selTIs[0];
		
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		PortalConnector.savePageToXml(projectPath, projectModuleName, page);

		TreeItem plTreeItem = new PortalPageTreeItem(selTI, page);
		selTI.setExpanded(true);
		return plTreeItem;
	}
	
	/**
	 * 删除 Page
	 * @throws Exception
	 */
	public void deleteSelectPageTreeNode()throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception(M_perspective.PortalExplorerTreeView_0);
		TreeItem selTI = selTIs[0];
		if (selTI instanceof PortalPageTreeItem) {
			Shell shell = new Shell(Display.getCurrent());
			if (!MessageDialog.openConfirm(shell, M_perspective.PortalExplorerTreeView_1, M_perspective.PortalExplorerTreeView_2
					+ selTI.getText() + M_perspective.PortalExplorerTreeView_3))
				return;
			 closeOpenedEidtor(selTI);
			((PortalPageTreeItem) selTI).deleteNode();
		}
	}
	
	
	/**
	 * 打开 PageEditor
	 */
	@SuppressWarnings("deprecation")
	public void openPortalPageEditor(PortalPageTreeItem portalpage) {
		Page page = (Page) portalpage.getData();
		PortalPageEditorInput editorinput = new PortalPageEditorInput(portalpage);
		String editorid = PortalPageEditor.class.getName(); 
		if (editorinput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		String editorinputid = editoridMap.get("page" + page.getPagename()); //$NON-NLS-1$
		if (editorinputid != null){
			IEditorPart[] parts = workbenchPage.getEditors();
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput)parts[i].getEditorInput();
				if(editorInput instanceof PortalPageEditorInput){
					PortalPageEditorInput pfEditorInput = (PortalPageEditorInput)editorInput;
					Page pfnew = (Page) pfEditorInput.getPage();
					String name = pfnew.getPagename();
					if(page.getPagename().equals(name)){
						editor =  parts[i];
						break;
					}
				}
			}
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(editorinput, editorid);
					} catch (PartInitException e1) {
						PortalPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		}
		else {
			editoridMap.put("page" + page.getPagename(), "page" + page.getPagename()); //$NON-NLS-1$ //$NON-NLS-2$
			try {
				workbenchPage.openEditor(editorinput, editorid);
				} catch (PartInitException e1) {
					PortalPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}
	
}
