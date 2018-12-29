/**
 * 
 */
package nc.uap.lfw.editor.common.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.application.LFWApplicationTreeItem;
import nc.uap.lfw.button.ButtonEditor;
import nc.uap.lfw.button.ButtonEditorInput;
import nc.uap.lfw.common.LFWUtility;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewMenus;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.refnode.BaseRefNode;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.form.core.FormEditor;
import nc.uap.lfw.form.core.FormEditorInput;
import nc.uap.lfw.grid.core.GridEditor;
import nc.uap.lfw.grid.core.GridEditorInput;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIView;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.webcomponent.LFWBusinessCompnentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWProjectTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWVirtualDirTreeItem;
import nc.uap.lfw.textcomp.TextCompEditor;
import nc.uap.lfw.textcomp.TextCompEditorInput;
import nc.uap.lfw.tree.core.TreeEditor;
import nc.uap.lfw.tree.core.TreeEditorInput;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.WorkbenchException;

/**
 * @author chouhl
 * 
 */
public class LFWAMCPersTool extends LFWPersTool {

	public static Application getCurrentApplication() {
		Application app = null;
		TreeItem treeItem = getCurrentTreeItem();
		if (treeItem != null && treeItem instanceof LFWApplicationTreeItem) {
			LFWApplicationTreeItem appTreeItem = (LFWApplicationTreeItem) treeItem;
			app = appTreeItem.getApplication();
			if (app.getControllerClazz() == null) {
				app = LFWAMCConnector.getApplication(
						LFWAMCPersTool.getCurrentFolderPath(),
						WEBPersConstants.AMC_APPLICATION_FILENAME);
				appTreeItem.setApplication(app);
			}
		}
		return app;
	}

	public static String getCurrentWindowId() {
		String windowId = null;
		TreeItem treeItem = getCurrentTreeItem();
		if (treeItem != null && treeItem instanceof LFWPageMetaTreeItem) {
			LFWPageMetaTreeItem windowTreeItem = (LFWPageMetaTreeItem) treeItem;
			windowId = windowTreeItem.getId();
		}
		return windowId;
	}

	public static LFWApplicationTreeItem getCurrentApplicationTreeItem() {
		TreeItem treeItem = getCurrentTreeItem();
		if (treeItem != null && treeItem instanceof LFWApplicationTreeItem) {
			return (LFWApplicationTreeItem) treeItem;
		} else {
			return null;
		}
	}

	/**
	 * 保存application.app
	 * 
	 * @param application
	 */
	public static void saveApplication(Application application) {
		String folderPath = getCurrentFolderPath();
		String projectPath = getProjectPath();
		if (application.getId() != null
				&& application.getId().trim().length() > 0) {
			folderPath = projectPath + WEBPersConstants.AMC_APPLICATION_PATH
					+ File.separator + application.getId();
		}
		LFWAMCConnector.updateApplicationToXml(folderPath,
				WEBPersConstants.AMC_APPLICATION_FILENAME, projectPath,
				application);
	}

	public static LfwWindow getDefaultWindow(Application appConf) {
		LfwWindow pm = null;
		LFWPageMetaTreeItem pmTreeItem = getDefaultWindowTreeItem(appConf);
		if (pmTreeItem != null) {
			pm = pmTreeItem.getPm();
		}
		return pm;
	}

	public static LFWPageMetaTreeItem getDefaultWindowTreeItem(
			Application appConf) {
		if (appConf == null) {
			appConf = LFWAMCConnector.getApplication(
					LFWAMCPersTool.getCurrentFolderPath(),
					WEBPersConstants.AMC_APPLICATION_FILENAME);
		}
		String defaultWinId = appConf.getDefaultWindowId();
		if (defaultWinId != null && defaultWinId.trim().length() > 0) {
			List<TreeItem> children = getAllWindowTreeItems(
					getCurrentProject(), getCurrentApplicationTreeItem());
			if (children != null && children.size() > 0) {
				for (TreeItem item : children) {
					if (item instanceof LFWPageMetaTreeItem
							&& defaultWinId.equals(((LFWPageMetaTreeItem) item)
									.getId())) {
						return (LFWPageMetaTreeItem) item;
					}
				}
			}
		}
		return null;
	}

	public static String getCurrentFolderPath() {
		TreeItem treeItem = getCurrentTreeItem();
		return getCurrentFolderPath(treeItem).replaceAll("\\\\", "/");
	}

	/**
	 * 获取工程目录
	 * 
	 * @return
	 */
	public static String getProjectPath() {
		IProject project = LFWAMCPersTool.getCurrentProject();
		if (LFWAMCPersTool.isBCPProject(project)) {
			return getBCPProjectPath();
		} else {
			return getLFWProjectPath();
		}
	}

	/**
	 * 获取LFW工程目录
	 * 
	 * @return
	 */
	public static String getLFWProjectPath() {
		return LFWPersTool.getProjectPath();
	}

	/**
	 * 获取BCP工程目录(最后一级组件目录)
	 * 
	 * @return
	 */
	public static String getBCPProjectPath() {
		IProject project = LFWAMCPersTool.getCurrentProject();
		String projPath = project.getLocation().toString();
		if (LFWAMCPersTool.isBCPProject(project)) {
			LFWBusinessCompnentTreeItem busiCompnent = LFWAMCPersTool
					.getCurrentBusiCompTreeItem();
			projPath = busiCompnent.getFile().getPath().replace("\\", "/");
//			String busiComp = busiCompnent.getText().substring(
//					busiCompnent.getText().indexOf(
//							WEBProjConstants.BUSINESSCOMPONENT)
//							+ WEBProjConstants.BUSINESSCOMPONENT.length() + 1,
//					busiCompnent.getText().length() - 1);
//			projPath += "/" + busiComp;
		}
		return projPath;
	}

	/**
	 * 获取window根目录
	 * 
	 * @return
	 */
	public static String getWindowPath() {
		String projectPath = getProjectPath().replaceAll("\\\\", "/");
		if (projectPath.endsWith("/")) {
			projectPath = projectPath.substring(0, projectPath.length() - 1);
		}
		return projectPath + WEBPersConstants.AMC_WINDOW_PATH;
	}

	/**
	 * 获取application根目录
	 * 
	 * @return
	 */
	public static String getApplicationPath() {
		String projectPath = getProjectPath().replaceAll("\\\\", "/");
		if (projectPath.endsWith("/")) {
			projectPath = projectPath.substring(0, projectPath.length() - 1);
		}
		return projectPath + WEBPersConstants.AMC_APPLICATION_PATH;
	}

	/**
	 * 获取文件夹节点
	 * 
	 * @return
	 */
	public static TreeItem getTreeItemFolder(IProject project,
			TreeItem currentTreeItem, String tiFolderName) {
		LFWDirtoryTreeItem treeItemFolder = null;
		if (project == null) {
			project = getCurrentProject();
		}
		if (currentTreeItem == null) {
			currentTreeItem = getCurrentTreeItem();
		}
		TreeItem[] nodeTreeItems = null;
		if (isBCPProject(project)) {
			LFWBusinessCompnentTreeItem bcpRoot = getComponentTreeItem(currentTreeItem);
			if (bcpRoot != null) {
				nodeTreeItems = bcpRoot.getItems();
			}
		} else {
			LFWProjectTreeItem projectRoot = null;
			TreeItem[] treeItems = getTree().getItems();
			for (TreeItem treeItem : treeItems) {
				if (((LFWProjectTreeItem) treeItem).getProjectModel()
						.getJavaProject().equals(project)) {
					projectRoot = (LFWProjectTreeItem) treeItem;
					break;
				}
			}
			if (projectRoot != null) {
				nodeTreeItems = projectRoot.getItems();
			}
		}
		if (nodeTreeItems != null && nodeTreeItems.length > 0
				&& tiFolderName != null) {
			for (TreeItem treeItem : nodeTreeItems) {
				if (treeItem != null && treeItem instanceof LFWDirtoryTreeItem) {
					if (tiFolderName.equals(((LFWDirtoryTreeItem) treeItem)
							.getType())) {
						treeItemFolder = (LFWDirtoryTreeItem) treeItem;
						break;
					}
				}
			}
		}
		return treeItemFolder;
	}

	/**
	 * 获取文件夹节点下所有一级子节点
	 * 
	 * @author chouhl
	 * @return
	 */
	public static List<TreeItem> getAllChildren(IProject project,
			TreeItem currentTreeItem, String tiFolderName) {
		List<TreeItem> list = new ArrayList<TreeItem>();
		LFWDirtoryTreeItem tiFolder = (LFWDirtoryTreeItem) getTreeItemFolder(
				project, currentTreeItem, tiFolderName);
		if (tiFolder != null) {
			TreeItem[] nodeTreeItems = tiFolder.getItems();
			for (TreeItem treeItem : nodeTreeItems) {
				list.add(treeItem);
				if (treeItem instanceof LFWVirtualDirTreeItem) {
					list.addAll(getVirtualTreeItemChildren((LFWVirtualDirTreeItem) treeItem));
				}
			}
		}
		return list;
	}

	/**
	 * 获取虚拟目录下一级子节点
	 * 
	 * @param virTreeItem
	 * @return
	 */
	private static List<TreeItem> getVirtualTreeItemChildren(
			LFWVirtualDirTreeItem virTreeItem) {
		List<TreeItem> list = new ArrayList<TreeItem>();
		if (virTreeItem != null) {
			TreeItem[] children = virTreeItem.getItems();
			if (children != null && children.length > 0) {
				for (TreeItem child : children) {
					list.add(child);
					if (child instanceof LFWVirtualDirTreeItem) {
						list.addAll(getVirtualTreeItemChildren((LFWVirtualDirTreeItem) child));
					}
				}
			}
		}
		return list;
	}

	/**
	 * 获取指定上下文的所有PublicView
	 * 
	 * @return
	 */
	public static Map<String, LfwView> getPublicViewsByContext() {
		String ctx = LFWUtility.getContextFromResource(LFWPersTool
				.getCurrentProject());
//		Map<String, Map<String, LfwView>> input = LFWPersTool
//				.getAllPoolWidget();
//		return input.get("/" + ctx);
		return LFWAMCConnector.getPublicViews(ctx);
	}

	/**
	 * 根据WindowId获取Window
	 * 
	 * @param pageMetaId
	 * @return
	 */
	public static LfwWindow getPageMetaById(String pageMetaId) {
		if (pageMetaId == null) {
			MainPlugin.getDefault().logError("WindowID为空");
			return null;
		}
		LfwWindow pageMeta = null;
		List<TreeItem> winTreeItems = getAllChildren(getCurrentProject(),
				getCurrentTreeItem(), ILFWTreeNode.WINDOW_FOLDER);
		if (winTreeItems != null && winTreeItems.size() > 0) {
			for (TreeItem ti : winTreeItems) {
				if (ti instanceof LFWPageMetaTreeItem) {
					if (pageMetaId.equals(((LFWPageMetaTreeItem) ti).getPm()
							.getId())) {
						pageMeta = (LfwWindow) ((LFWPageMetaTreeItem) ti)
								.getPm();
						break;
					}
				}
			}
		}
		if (pageMeta == null) {
			List<LfwWindow> winConfList = LFWAMCConnector.getCacheWindows();
			if (winConfList != null && winConfList.size() > 0) {
				for (LfwWindow win : winConfList) {
					if (pageMetaId.equals(win.getId())) {
						pageMeta = win;
						break;
					}
				}
			}
		}
		if (pageMeta == null) {
			MainPlugin.getDefault().logError(
					"未找到ID为" + pageMetaId + "的PageMeta");
		}
		return pageMeta;
	}
	/**
	 * 根据WindowId获取 详细Window信息
	 * 
	 * @param pageMetaId
	 * @return
	 */
	public static LfwWindow getPageMetaDetailById(String pageMetaId){
		LfwWindow pm = null;
//		String projectPath = LFWAMCPersTool.getProjectPath();
//		// 获取当前Pagemeta
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put(WebConstant.PROJECT_PATH_KEY, projectPath);
//		paramMap.put(WebConstant.PAGE_ID_KEY, pageMetaId);
//		paramMap.put(WebConstant.PARENT_PAGE_ID_KEY, null);
//		try {
//			pm = LFWConnector.getPageMeta(paramMap);
//		} catch (Exception e) {
//			MainPlugin.getDefault().logError(e.getMessage(), e);
//			MessageDialog.openError(null, M_pagemeta.LFWPageMetaTreeItem_0, e.getMessage());
//		}
		pm = LFWAMCConnector.getWindowById(pageMetaId);
		return pm;
	}

	/**
	 * 获取当前UIMeta
	 * 
	 * @return
	 */
	public static UIMeta getCurrentUIMeta() {
		String folderPath = getCurrentFolderPath();
		UIMeta uimeta = null;
		if (folderPath != null) {
			uimeta = LFWAMCConnector.getUIMeta(folderPath);
		} else {
			throw new LfwRuntimeException("当前UIMeta访问路径为空");
		}
		return uimeta;
	}

	/**
	 * 从session中获取元素
	 * 
	 * @param pageMetaId
	 * @return
	 */
	public static Map<String, Object> getElementFromSession(String pageMetaId,
			String sessionId) {
		Map<String, Object> map = LFWAMCConnector.getElementFromSessionCache(
				pageMetaId, sessionId);
		LfwWindow oriMeta = (LfwWindow) map.get(LFWAMCConnector.KEY_PAGEMETA);
		LfwWindow meta = getPageMetaById(pageMetaId);
		if (oriMeta != null && meta != null) {
			LfwView[] oriWidgets = oriMeta.getViews();
			if (oriWidgets != null && oriWidgets.length > 0) {
				for (LfwView oriWidget : oriWidgets) {
					LfwView widget = meta.getWidget(oriWidget.getId());
					mergeViewEvents(widget, oriWidget);
				}
			}
		}
		return map;
	}

	/**
	 * 合并View所有事件(包括View包含的控件)
	 * 
	 * @param widget
	 * @param oriWidget
	 */
	public static void mergeViewEvents(LfwView widget, LfwView oriWidget) {
		if (widget != null && oriWidget != null) {

			ViewComponents oriVComp = oriWidget.getViewComponents();
			ViewComponents vcomp = widget.getViewComponents();
			WebComponent[] comps = vcomp.getComponents();
			if (comps != null && comps.length > 0) {
				for (WebComponent comp : comps) {
					WebComponent oriComp = oriVComp.getComponent(comp.getId());
					if (oriComp != null) {
						oriComp.removeAllEventConf();
						oriComp.setEventConfList(comp.getEventConfList());
					} else {
						oriVComp.addComponent(comp);
					}
				}
			}

			// ViewContainers oriVCon = oriWidget.getViewConinters();
			// ViewContainers vcon = widget.getgetViewConinters();
			// WebComponent[] cons = vcon.getContainers();
			// if(cons != null && cons.length > 0){
			// for(WebComponent con : cons){
			// WebComponent oriCon = oriVCon.getContainer(con.getId());
			// if(oriCon != null){
			// oriCon.removeAllEventConf();
			// oriCon.setEventConfList(con.getEventConfList());
			// }else{
			// oriVCon.addContainer(con);
			// }
			// }
			// }

			ViewMenus oriVM = oriWidget.getViewMenus();
			ViewMenus vm = widget.getViewMenus();

			MenubarComp[] mcs = vm.getMenuBars();
			if (mcs != null && mcs.length > 0) {
				for (MenubarComp mc : mcs) {
					MenubarComp oriMC = oriVM.getMenuBar(mc.getId());
					if (oriMC != null) {
						oriMC.removeAllEventConf();
						oriMC.setEventConfList(mc.getEventConfList());
						mergeChildrenMenuItemEvents(oriMC.getMenuList(),
								mc.getMenuList());
					} else {
						oriVM.addMenuBar(mc);
					}
				}
			}

			ContextMenuComp[] cmcs = vm.getContextMenus();
			if (cmcs != null && cmcs.length > 0) {
				for (ContextMenuComp cmc : cmcs) {
					ContextMenuComp oriCMC = oriVM.getContextMenu(cmc.getId());
					if (oriCMC != null) {
						oriCMC.removeAllEventConf();
						oriCMC.setEventConfList(cmc.getEventConfList());
						mergeChildrenMenuItemEvents(oriCMC.getItemList(),
								cmc.getItemList());
					} else {
						oriVM.addContextMenu(cmc);
					}
				}
			}

			ViewModels oriVms = oriWidget.getViewModels();
			ViewModels vms = widget.getViewModels();

			ComboData[] cds = vms.getComboDatas();
			if (cds != null && cds.length > 0) {
				for (ComboData cd : cds) {
					ComboData oriCD = oriVms.getComboData(cd.getId());
					if (oriCD != null) {
						oriCD.removeAllEventConf();
						oriCD.setEventConfList(cd.getEventConfList());
					} else {
						oriVms.addComboData(cd);
					}
				}
			}

			Dataset[] dss = vms.getDatasets();
			if (dss != null && dss.length > 0) {
				for (Dataset ds : dss) {
					Dataset oriDS = oriVms.getDataset(ds.getId());
					if (oriDS != null) {
						oriDS.removeAllEventConf();
						oriDS.setEventConfList(ds.getEventConfList());
					} else {
						oriVms.addDataset(ds);
					}
				}
			}

			IRefNode[] rns = vms.getRefNodes();
			if (rns != null && rns.length > 0) {
				for (IRefNode rn : rns) {
					IRefNode oriRN = oriVms.getRefNode(rn.getId());
					if (oriRN instanceof BaseRefNode
							&& rn instanceof BaseRefNode) {
						((BaseRefNode) oriRN).removeAllEventConf();
						((BaseRefNode) oriRN)
								.setEventConfList(((BaseRefNode) rn)
										.getEventConfList());
					} else {
						oriVms.addRefNode(rn);
					}
				}
			}
		}
	}

	private static void mergeChildrenMenuItemEvents(List<MenuItem> oriChildren,
			List<MenuItem> children) {
		Map<String, MenuItem> oriItemMap = null;
		if (oriChildren != null && oriChildren.size() > 0) {
			oriItemMap = new HashMap<String, MenuItem>();
			for (MenuItem item : oriChildren) {
				oriItemMap.put(item.getId(), item);
			}
			for (MenuItem item : children) {
				MenuItem oriItem = oriItemMap.get(item.getId());
				if (oriItem != null) {
					oriItem.removeAllEventConf();
					oriItem.setEventConfList(item.getEventConfList());
					if (oriItem.getChildList() != null
							&& oriItem.getChildList().size() > 0) {
						mergeChildrenMenuItemEvents(oriItem.getChildList(),
								item.getChildList());
					} else {
						oriItem.setChildList(item.getChildList());
					}
				} else {
					oriChildren.add(item);
				}
			}
		}
	}

	/**
	 * 从session中获取Window
	 * 
	 * @param pageMetaId
	 * @param sessionId
	 * @return
	 */
	public static LfwWindow getWindowFromSession(String pageMetaId,
			String sessionId, Map<String, Object> map) {
		LfwWindow meta = null;
		if (map == null) {
			map = getElementFromSession(pageMetaId, sessionId);
		}
		meta = (LfwWindow) map.get(LFWAMCConnector.KEY_PAGEMETA);
		return meta;
	}

	/**
	 * 从session中获取View
	 * 
	 * @param viewId
	 * @param pageMetaId
	 * @param sessionId
	 * @return
	 */
	public static LfwView getViewFromSession(String viewId,
			String pageMetaId, String sessionId, Map<String, Object> map) {
		LfwView view = null;
		if (map == null) {
			map = getElementFromSession(pageMetaId, sessionId);
		}
		LfwWindow meta = (LfwWindow) map.get(LFWAMCConnector.KEY_PAGEMETA);
		if (meta != null) {
			view = meta.getWidget(viewId);
		}
		return view;
	}

	/**
	 * 从session中获取View UIMeta
	 * 
	 * @param pageMetaId
	 * @param viewId
	 * @param sessionId
	 * @param map
	 * @return
	 */
	public static UIMeta getWidgetUIMetaFromSession(String pageMetaId,
			String viewId, String sessionId, Map<String, Object> map) {
		UIMeta meta = getUIMetaFromSession(pageMetaId, sessionId, map);
		UIElement uielement = null;
		if (meta != null) {
			uielement = meta.findChildById(viewId);
		}
		if (uielement instanceof UIView) {
			return ((UIView) uielement).getUimeta();
		} else {
			return null;
		}
	}

	/**
	 * 从session中获取Window UIMeta
	 * 
	 * @param pageMetaId
	 * @param sessionId
	 * @return
	 */
	public static UIMeta getUIMetaFromSession(String pageMetaId,
			String sessionId, Map<String, Object> map) {
		if (map == null) {
			map = getElementFromSession(pageMetaId, sessionId);
		}
		return (UIMeta) map.get(LFWAMCConnector.KEY_UIMETA);
	}

	/**
	 * 从session中获取UIElement
	 * 
	 * @param pageMetaId
	 * @param viewId
	 * @param elementId
	 * @param sessionId
	 * @return
	 */
	public static UIElement getUIElementFromSession(String pageMetaId,
			String viewId, String elementId, String sessionId,
			Map<String, Object> map) {
		UIElement element = null;
		UIMeta meta = getUIMetaFromSession(pageMetaId, sessionId, map);
		if (meta != null) {
			element = meta.findChildById(elementId);
		}
		return element;
	}

	/**
	 * 根据editor id 获取编辑器对象，并打开编辑器
	 */
	public static void openEditor(String moduleName,String bcpName,String pageMetaId, String widgetId,
			String elementId, String editorid) {
		IProject[] projects = LfwCommonTool.getLfwProjects();
		IProject project = null;
		for(IProject projectitem:projects){
			if(moduleName.equals(LfwCommonTool.getProjectModuleName(projectitem))) project = projectitem;
		}
		if(project == null) 
			return;
		
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put(WebConstant.PROJECT_PATH_KEY, project.getLocation().toString()+"/"+bcpName);
//		paramMap.put(WebConstant.PAGE_ID_KEY, pageMetaId);
//		paramMap.put(WebConstant.PARENT_PAGE_ID_KEY, null);
		// 获取当前Pagemeta
		LfwWindow pm = null;
		try {
			pm = LFWConnector.getPageMeta(pageMetaId);
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
			MessageDialog.openError(null, "错误提示", e.getMessage());
		}
		
		IWorkbenchPage workbenchPage;
		try {
			workbenchPage = WEBPersPlugin.getDefault().getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			LfwView wd = pm.getWidget(widgetId);
			WebComponent comp = wd.getViewComponents().getComponent(
					elementId);
			if (editorid.equals(GridEditor.class.getName())) {				
				if (comp instanceof GridComp) {
					GridComp grid = (GridComp) comp;
					GridEditorInput gridEditorInput = new GridEditorInput(grid,
							wd, pm);
					LFWPersTool.getLFWViewSheet();
					workbenchPage.openEditor(gridEditorInput, editorid);
				}
			}
			else if (editorid.equals(FormEditor.class.getName())) {				
				if (comp instanceof FormComp) {
					FormComp form = (FormComp) comp;
					FormEditorInput formEditorInput = new FormEditorInput(form,
							wd, pm);
					LFWPersTool.getLFWViewSheet();
					workbenchPage.openEditor(formEditorInput, editorid);
				}
			}
			else if (editorid.equals(TreeEditor.class.getName())) {				
				if (comp instanceof TreeViewComp) {
					TreeViewComp tree = (TreeViewComp) comp;
					TreeEditorInput treeEditorInput = new TreeEditorInput(tree,
							wd, pm);
					LFWPersTool.getLFWViewSheet();
					workbenchPage.openEditor(treeEditorInput, editorid);
				}
			}
			else if (editorid.equals(GridEditor.class.getName())) {				
				if (comp instanceof GridComp) {
					GridComp grid = (GridComp) comp;
					GridEditorInput gridEditorInput = new GridEditorInput(grid,
							wd, pm);
					LFWPersTool.getLFWViewSheet();
					workbenchPage.openEditor(gridEditorInput, editorid);
				}
			}
			else if (editorid.equals(ButtonEditor.class.getName())) {				
				if (comp instanceof ButtonComp) {
					ButtonComp button = (ButtonComp) comp;
					ButtonEditorInput buttonEditorInput = new ButtonEditorInput(button,
							wd, pm);
					LFWPersTool.getLFWViewSheet();
					workbenchPage.openEditor(buttonEditorInput, editorid);
				}
			}
			else if (editorid.equals(TextCompEditor.class.getName())) {				
				if (comp instanceof TextComp) {
					TextComp text = (TextComp) comp;
					TextCompEditorInput textEditorInput = new TextCompEditorInput(text,
							wd, pm);
					LFWPersTool.getLFWViewSheet();
					workbenchPage.openEditor(textEditorInput, editorid);
				}
			}
		} catch (WorkbenchException e) {
			WEBPersPlugin.getDefault().logError(e);
		}

	}

}
