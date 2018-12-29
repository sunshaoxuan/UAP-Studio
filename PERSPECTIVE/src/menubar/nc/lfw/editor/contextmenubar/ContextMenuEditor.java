package nc.lfw.editor.contextmenubar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.contextmenubar.actions.DelContextMenuItemAction;
import nc.lfw.editor.menubar.MenuItemLabel;
import nc.lfw.editor.menubar.MenubarConnector;
import nc.lfw.editor.menubar.MenubarEditPartFactory;
import nc.lfw.editor.menubar.action.AddMenuItemAction;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenuItemObj;
import nc.lfw.editor.menubar.graph.MenuElementPart;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.webcomponent.LFWContextMenuTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;

/**
 * 右键菜单编辑器
 * @author zhangxya
 *
 */
public class ContextMenuEditor extends LFWBaseEditor {
	private ContextMenuGrahp graph = new ContextMenuGrahp();
	private ContextMenuElementObj menubarObj;
	/**
	 * 用于修改的临时menubar对象
	 */
	private ContextMenuComp menubarTemp;
//	private PageMeta pagemetaWithWidgets;
	
	/**
	 * 子菜单对象集合
	 */
	private Map<String, List<MenuElementObj>> childrenElementMap = new HashMap<String, List<MenuElementObj>>();
	
	/**
	 * 临时存放新建的子菜单
	 */
	private Map<String, MenuElementObj> tempChildrenElementMap = new HashMap<String, MenuElementObj>();
	
	/**
	 * 连接器集合
	 */
	private Map<String, MenubarConnector> connectorMap = new HashMap<String, MenubarConnector>();
	
	/**
	 * 当前Listener图像所代表的MenuItem对象
	 */
	private MenuItem currentListenerMenuItem;

	public ContextMenuEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
		getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}
	
	
	public void setFocus() {
		super.setFocus();
		Tree tree = LFWPersTool.getTree();
		LFWSeparateTreeItem lfwSeparaTreeItem = null;
		IEditorInput input = getEditorInput();
		ContextMenuEditorInput contextEditorInput = (ContextMenuEditorInput)input;
		if(contextEditorInput.getCurrentTreeItem().isDisposed())
			return;
		ContextMenuComp contextMenuComp = (ContextMenuComp) contextEditorInput.getCloneElement();		
		lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.COMPONENTS);		
		TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
		for (int i = 0; i < childTreeItems.length; i++) {
			if(childTreeItems[i] instanceof LFWContextMenuTreeItem){
				LFWContextMenuTreeItem webT = (LFWContextMenuTreeItem) childTreeItems[i];
				if(webT.getData() instanceof ContextMenuComp){
					ContextMenuComp gr = (ContextMenuComp) webT.getData();
					if(contextMenuComp.getId().equals(gr.getId())){
						tree.setSelection(webT);
						break;
					}
				}
			}
		}
	}
	
	

	
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		setPartName(input.getName());
	}

	protected void setInput(IEditorInput input) {
		super.setInput(input);
		ContextMenuEditorInput menubarEditorInput = (ContextMenuEditorInput) input;
//		LFWMenubarCompTreeItem menubarTreeItem = menubarEditor.getMenubarTreeItem();
//		menubarTemp = (MenubarComp) ((MenubarComp) menubarTreeItem.getData()).clone();
		menubarTemp = (ContextMenuComp) menubarEditorInput.getCloneElement();
		ContextMenuElementObj menubarElement = new ContextMenuElementObj();
		menubarElement.setMenubar(menubarTemp);
		graph.addCell(menubarElement);
		this.menubarObj = menubarElement;
		
		// 显示Menubar
		int pointX = 100;
		int pointY = 100;
		// 设置显示位置
		menubarObj.setSize(new Dimension(150, 150));
		Point point = new Point(pointX, pointY);
		menubarObj.setLocation(point);
		
		// 显示子菜单
		List<MenuItem> menuList = menubarElement.getMenubar().getItemList();
		if (null != menuList)
			showChildMenus(menuList, menubarElement, null, 1);
		
		// 显示菜单之间的连接关系
		List<MenuItemObj> menubarChildrenList = menubarObj.getChildrenList();
		if (null != menubarChildrenList)
			showRelations(menubarChildrenList, menubarObj, null);
		
		// 设置Listener显示位置
		setListenerPointX(100);
		setListenerPointY(500);
//		addListenerCellToEditor(new HashMap<String, JsListenerConf>(), graph);

//		this.pagemetaWithWidgets = ((LfwBaseEditorInput)input).getPageMeta();
		
	}
	
	/**
	 * 显示所有子菜单项
	 * @param menuList 子菜单列表
	 * @param menubarObj 主菜单显示对象
	 * @param menuObj 子菜单显示对象
	 * @param level 级别
	 */
	private void showChildMenus(List<MenuItem> menuList, ContextMenuElementObj menubarObj, MenuElementObj menuObj, int level) {
		int subLevel = level + 1;
		for (int i = 0, n = menuList.size(); i < n; i++) {
			MenuItem item = menuList.get(i);
			MenuItemObj itemObj = new MenuItemObj();
			itemObj.setMenuItem(item);
			List<MenuItem> childList = item.getChildList();
			MenuElementObj subMenuObj = new MenuElementObj();
			subMenuObj.setMenuItem(item);
			// 设置级别
			subMenuObj.setLevel(level);
			if (null != menubarObj) {
				subMenuObj.setParentElementObj(menubarObj);
			}else if (null != menuObj){
				subMenuObj.setParentElementObj(menuObj);
			}		
			if (null != childList && childList.size() > 0) {
				itemObj.setChild(subMenuObj);	
				showChildMenus(childList, null, subMenuObj, level + 1);
//				for (int j = 0; j < childList.size(); j++) {
//					MenuItem childMenu = childList.get(j);
//					// 构造子菜单对象
//					MenuElementObj subMenuObj = new MenuElementObj();
//					subMenuObj.setMenuItem(childMenu);
//					// 设置级别
//					subMenuObj.setLevel(level);
//					
//					itemObj.setChild(subMenuObj);
//					if (null != menubarObj) {
//						subMenuObj.setParentElementObj(menubarObj);
//					}else if (null != menuObj){
//						subMenuObj.setParentElementObj(menuObj);
//					}
//					
//					// 显示子菜单
//					showChildMenuItem(subMenuObj, level);
//					
//					graph.addMenu(subMenuObj);
//					
//					// 迭代显示子菜单的子菜单
//					List<MenuItem> childMenuList = childMenu.getChildList();
//					showChildMenus(childMenuList, null, subMenuObj, subLevel);
//				}
			}
			if(subMenuObj.getChildrenList().size()>0){
				// 显示子菜单
				showChildMenuItem(subMenuObj, level);
				
				graph.addMenu(subMenuObj);
			}
			if (null != menubarObj) {
				menubarObj.addChild(itemObj);
			}else if (null != menuObj){
				menuObj.addChild(itemObj);
			}
		}
	}
	
	/**
	 * 显示子菜单
	 * @param itemObj
	 * @param level
	 */
	public void showChildMenuItem(MenuElementObj itemObj, int level) {
//		graph.addCell(itemObj);
		
		// 显示子菜单
		int pointX = 120 + level * 200;
//		LfwElementObjWithGraph parentObj = itemObj.getParentElementObj();
		
		if (!childrenElementMap.containsKey(String.valueOf(level)))
			childrenElementMap.put(String.valueOf(level), new ArrayList<MenuElementObj>());
		childrenElementMap.get(String.valueOf(level)).add(itemObj);
		
//		int levelY = 1;
//		if (null != parentObj) {
//			if (parentObj instanceof MenubarElementObj)
//				levelY = ((MenubarElementObj)parentObj).getChildrenList().size();
//			else if (parentObj instanceof MenuElementObj)
//				levelY = ((MenuElementObj)parentObj).getChildrenList().size();
//		} else {
//			levelY = childrenElementMap.get(String.valueOf(level)).size();
//		}
		int	itemIndex = childrenElementMap.get(String.valueOf(level)).size() - 1;
		int pointY = 100 + (itemIndex) * 150;
		// 设置显示位置
		itemObj.setSize(new Dimension(100, 100));
		Point point = new Point(pointX, pointY);
		itemObj.setLocation(point);
		
		
	}
	
	/**
	 * 显示菜单之间的连接关系
	 * 
	 * @param menubar
	 */
	private void showRelations(List<MenuItemObj> childrenList, ContextMenuElementObj menubarObj, MenuElementObj menuObj) {
		for (int i = 0, n = childrenList.size(); i < n; i++) {
			MenuElementObj subMenuObj = childrenList.get(i).getChild();
			if(subMenuObj == null){
				continue;
			}
			// 绘制连接线
			MenubarConnector con = null;
			if (null != menubarObj) {
				con = new MenubarConnector(menubarObj, subMenuObj);
			} else if (null != menuObj) {
				con = new MenubarConnector(menuObj, subMenuObj);
			}
			con.setId(subMenuObj.getMenuItem().getId() + MenubarConnector.ID_SUFFIX);
			con.connect();
			// 迭代绘制子菜单的连接线
			showRelations(subMenuObj.getChildrenList(), null, subMenuObj);
			
			addConnector(con);
		}
	}
	
	public LfwBaseGraph getGraph() {
		return graph;
	}

	private KeyHandler shareKeyHandler = null;

	private KeyHandler getShareKeyHandler() {
		if (shareKeyHandler == null) {
			shareKeyHandler = new KeyHandler();
			shareKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), 
					getActionRegistry().getAction(ActionFactory.DELETE.getId()));
		}
		return shareKeyHandler;
	}

	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		ScalableFreeformRootEditPart rootEditpart = new ScalableFreeformRootEditPart();
		getGraphicalViewer().setRootEditPart(rootEditpart);
		getGraphicalViewer().setEditPartFactory(new MenubarEditPartFactory(this));
		getGraphicalViewer().setKeyHandler(getShareKeyHandler());
		getGraphicalViewer().setContextMenu(getMenuManager());
	}

	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(this.graph);
		getGraphicalViewer().addDropTargetListener(new nc.uap.lfw.perspective.editor
				.DiagramTemplateTransferDropTargetListener(getGraphicalViewer()));
		LFWPersTool.showView(IPageLayout.ID_PROP_SHEET);
	}

	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}

		};
	}

	private PaletteRoot paleteRoot = null;

	protected PaletteRoot getPaletteRoot() {
		if (paleteRoot == null) {
			paleteRoot = ContextMenuPalette.createPaletteRoot();
		}
		return paleteRoot;
	}

	public static ContextMenuEditor getActiveMenubarEditor() {
//		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench()
//				.getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		editor = LFWBaseEditor.getActiveEditor();
//		if (page != null) {
//			editor = page.getActiveEditor();
//
//		}
		if (editor != null && editor instanceof ContextMenuEditor) {
			return (ContextMenuEditor) editor;
		} else {
			return null;
		}

	}

	/**
	 * 保存Pagemeta到文件中
	 * 
	 * @param widget
	 */
	public void saveWidget(LfwView widget) {
		// 将修改过的menubar对象放入pagemeta中
		ContextMenuComp clone = (ContextMenuComp)menubarTemp.clone();
		widget.getViewMenus().addContextMenu(clone);
		// 获取项目路径
//		String projectPath = LFWPersTool.getProjectPath();
//		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
//		String filePath = widgetTreeItem.getFile().getPath();
//		DataProviderForDesign dataProvider = new DataProviderForDesign();
//		// 保存Widget到pagemeta.pm中
//		dataProvider.saveWidgettoXml(filePath, "widget.wd", projectPath, widget);
		LFWPersTool.saveWidget(widget);
	}

	
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		saveWidget(LFWPersTool.getCurrentWidget());
	}

	
	public LFWAbstractViewPage createViewPage() {
		return new ContextMenuViewPage();
	}

	
//	public void saveJsListener(String jsListenerId,
//			EventHandlerConf jsEventHandler, JsListenerConf listener) {
////		MenuItem item = getCurrentListenerMenuItem();
////		if(item != null){
////			if (null != jsEventHandler) {
////				if (item.getListenerMap().containsKey(jsListenerId)) {
////					JsListenerConf jsListener = item.getListenerMap().get(jsListenerId);
////					doSaveListenerEvent(jsListener, jsEventHandler);
////				}
////			} else {
////					item.addListener(listener);
////			}
////		}
////		else{
////			IEditorInput input = getEditorInput();
////			ContextMenuEditorInput menubarEditorInput = (ContextMenuEditorInput)input;
////			ContextMenuComp menubarnComp = (ContextMenuComp) menubarEditorInput.getCloneElement();
////			if (null != jsEventHandler) {
////				if (menubarnComp.getListenerMap().containsKey(jsListenerId)) {
////					JsListenerConf jsListener = menubarnComp.getListenerMap().get(jsListenerId);
////					doSaveListenerEvent(jsListener, jsEventHandler);
////			} else {
////				menubarnComp.addListener(listener);
////			}
////			setDirtyTrue();
////			}
////		}
////		setDirtyTrue();
//	}

	
	protected void editMenuManager(IMenuManager manager) {
		if (null != getCurrentSelection()) {
			StructuredSelection ss = (StructuredSelection) getCurrentSelection();
			Object sel = ss.getFirstElement();
			if (sel instanceof MenuElementPart) {
				MenuElementPart lfwEle = (MenuElementPart) sel;
				Object model = lfwEle.getModel();
				if (model instanceof ContextMenuElementObj) {
					ContextMenuElementObj menubarObj = (ContextMenuElementObj) model;
					// 增加 “设置默认菜单项” 菜单项
					//DefaultMenuItemAction defaultMenuItemAction = new DefaultMenuItemAction(menubarObj);
					
					// 设置Menubar初始化器参数
//					MenubarEditorInput input = (MenubarEditorInput) getEditorInput();
//					LFWMenubarCompTreeItem menubarTreeItem = input.getMenubarTreeItem();
					
					//manager.add(defaultMenuItemAction);
					
					// 增加 “增加自定义菜单项” 菜单项
					AddMenuItemAction addMenuItemAction = new AddMenuItemAction(menubarObj);
					
					manager.add(addMenuItemAction);
					
					// 获取当先选中的子项
					Label label = menubarObj.getFigure().getCurrentLabel();
					if (null != label) {
						
						if (label instanceof MenuItemLabel) {
							DelContextMenuItemAction delMenuItemAction = new DelContextMenuItemAction((MenuItemLabel)label,
									menubarObj, ((MenuItemLabel)label).getText());
							manager.add(delMenuItemAction);
						}
					}
				}
				if (model instanceof MenuElementObj) {
					MenuElementObj menuObj = (MenuElementObj) model;
					
					// 增加 “增加自定义菜单项” 菜单项
					AddMenuItemAction addMenuItemAction = new AddMenuItemAction(menuObj);
					manager.add(addMenuItemAction);
					
					// 获取当先选中的子项
					Label label = menuObj.getFigure().getCurrentLabel();
					if (null != label) {
						DelContextMenuItemAction delMenuItemAction = new DelContextMenuItemAction((MenuItemLabel)label,
								menuObj, ((MenuItemLabel)label).getText());
						manager.add(delMenuItemAction);
					}
				}
			} else {
				return;
			}
		} else {
			return;
		}

	}
	
	/**
	 * 刷新所有页面菜单对象的显示图像
	 */
	public void refreshAllElementObj() {
		if (null != menubarObj)
			menubarObj.getFigure().refresh();
		for (String key : childrenElementMap.keySet()) {
			List<MenuElementObj> childElementObjList = childrenElementMap.get(key);
			for (MenuElementObj menuElementObj : childElementObjList) {
				menuElementObj.getFigure().refresh();
			}
		}
	}
	
	@Override
	public List<JsEventDesc> getAcceptEventDescs() {
		if(menubarObj != null && menubarObj.getMenubar() != null){
			List<MenuItem> miList = menubarObj.getMenubar().getItemList();
			if(miList != null && miList.size() > 0){
				return miList.get(0).getAcceptEventDescs();
			}
		}
		return super.getAcceptEventDescs();
	}

	public ContextMenuElementObj getMenubarObj() {
		return menubarObj;
	}

	
	public void addTempChildMenuElement(MenuElementObj menuObj) {
		tempChildrenElementMap.put(menuObj.getId(), menuObj);
	}

	public Map<String, MenuElementObj> getTempChildrenElementMap() {
		return tempChildrenElementMap;
	}

//	public PageMeta getPagemetaWithWidgets() {
//		return pagemetaWithWidgets;
//	}

	/**
	 * 获取连接器对象
	 * @param key
	 * @return
	 */
	public MenubarConnector getConnector(String key) {
		if (connectorMap.containsKey(key))
			return connectorMap.get(key);
		return null;
	}

	/**
	 * 增加连接器
	 * @param connector
	 */
	public void addConnector(MenubarConnector connector) {
		connectorMap.put(((MenuElementObj)connector.getTarget()).getMenuItem().getId(), connector);
	}

	/**
	 * 删除连接器
	 * @param key
	 */
	public void removeConnector(String key) {
		if (connectorMap.containsKey(key))
			connectorMap.remove(key);
	}

	/**
	 * 获取当前Listener图像所代表的MenuItem对象
	 * @return
	 */
	public MenuItem getCurrentListenerMenuItem() {
		return currentListenerMenuItem;
	}

	/**
	 * 设置当前Listener图像所代表的MenuItem对象
	 * @param currentListenerMenuItem
	 */
	public void setCurrentListenerMenuItem(MenuItem currentListenerMenuItem) {
		this.currentListenerMenuItem = currentListenerMenuItem;
	}


	public String getPath() {
		LFWDirtoryTreeItem pagemetaTreeItem = LFWPersTool.getPagemetaTreeItem(LFWPersTool.getCurrentTreeItem());
		String pagemetaNodePath = pagemetaTreeItem.getFile().getPath();
		return pagemetaNodePath + "/pagemeta.pm";
	}


	public ContextMenuComp getMenubarTemp() {
		return menubarTemp;
	}

	public void setMenubarTemp(ContextMenuComp menubarTemp) {
		this.menubarTemp = menubarTemp;
	}


	protected LfwElementObjWithGraph getLeftElement() {
		return null;
	}


	protected LfwElementObjWithGraph getTopElement() {
		return getMenubarObj();
	}
	
	public LfwView getWidget() {
		return LFWAMCPersTool.getCurrentWidget();
	}

	public void setWidget(LfwView widget) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		if(widgetTreeItem != null){
			widgetTreeItem.setWidget(widget);
		}
	}

}
