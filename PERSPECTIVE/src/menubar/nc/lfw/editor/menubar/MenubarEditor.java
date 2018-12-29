package nc.lfw.editor.menubar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.contextmenubar.ContextMenuEditor;
import nc.lfw.editor.menubar.action.AddMenuItemAction;
import nc.lfw.editor.menubar.action.DelMenuItemAction;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenuItemObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.lfw.editor.menubar.graph.MenuElementPart;
import nc.lfw.editor.menubar.page.MenubarViewPage;
import nc.lfw.editor.menubar.palette.MenubarPalette;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_menubar;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.listener.EventEditorControl;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMenubarCompTreeItem;
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
import org.eclipse.jface.dialogs.MessageDialog;
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

public class MenubarEditor extends LFWBaseEditor {
	private MenubarGraph graph = new MenubarGraph();
	private MenubarElementObj menubarObj;
	private String gridId;
	/**
	 * 用于修改的临时menubar对象
	 */
	private MenubarComp menubarTemp;
	
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

	public MenubarEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
		getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}

	
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		setPartName(input.getName());
	}

	protected void setInput(IEditorInput input) {
		super.setInput(input);
		MenubarEditorInput menubarEditorInput = (MenubarEditorInput) input;
//		LFWMenubarCompTreeItem menubarTreeItem = menubarEditor.getMenubarTreeItem();
//		menubarTemp = (MenubarComp) ((MenubarComp) menubarTreeItem.getData()).clone();
		gridId = menubarEditorInput.getGridId();
		menubarTemp = (MenubarComp) menubarEditorInput.getCloneElement();
		MenubarElementObj menubarElement = new MenubarElementObj();
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
		List<MenuItem> menuList = menubarElement.getMenubar().getMenuList();
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
//		getJsListenerObj().setSize(new Dimension(150, 150));
//		this.pagemetaWithWidgets = ((LfwBaseEditorInput)input).getPageMeta();
		
	}
	
	/**
	 * 显示所有子菜单项
	 * @param menuList 子菜单列表
	 * @param menubarObj 主菜单显示对象
	 * @param menuObj 子菜单显示对象
	 * @param level 级别
	 */
	private void showChildMenus(List<MenuItem> menuList, MenubarElementObj menubarObj, MenuElementObj menuObj, int level) {
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
//				for (int j = 0; j < childList.size(); j++) {
//					MenuItem childMenu = childList.get(j);
//					// 构造子菜单对象
//					MenuItemObj subItemObj = new MenuItemObj();
//					subItemObj.setMenuItem(childMenu);				    
////					subMenuObj.setMenuItem(childMenu);					
//					subMenuObj.addChild(subItemObj);
//									
////					// 构造子菜单内容
////					MenuItemObj itemObj = new MenuItemObj();
////					itemObj.setMenuItem(childMenu);
////					itemObj.setChild(subMenuObj);
////					
////					// 增加子菜单内容
////					if (null != menubarObj) {
////						menubarObj.addChild(itemObj);
////						subMenuObj.setParentElementObj(menubarObj);
////					} else if (null != menuObj) {
////						menuObj.addChild(itemObj);
////						subMenuObj.setParentElementObj(menuObj);
////					}
//					
//					
//					
//					// 迭代显示子菜单的子菜单
////					List<MenuItem> childMenuList = childMenu.getChildList();
//					
//				}
				showChildMenus(childList, null, subMenuObj, level + 1);
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
		int pointX = 100 + level * 200;
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
	private void showRelations(List<MenuItemObj> childrenList, MenubarElementObj menubarObj, MenuElementObj menuObj) {
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
			paleteRoot = MenubarPalette.createPaletteRoot();
		}
		return paleteRoot;
	}

	public static MenubarEditor getActiveMenubarEditor() {
//		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench()
//				.getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		editor = LFWBaseEditor.getActiveEditor();
//		if (page != null) {
//			editor = page.getActiveEditor();
//
//		}
		if (editor != null && editor instanceof MenubarEditor) {
			return (MenubarEditor) editor;
		} 
		else {
			return null;
		}

	}

	
	public void setFocus() {
		super.setFocus();
		Tree tree = LFWPersTool.getTree();
		LFWSeparateTreeItem lfwSeparaTreeItem = null;
		IEditorInput input = getEditorInput();
		MenubarEditorInput menubarEditorInput = (MenubarEditorInput)input;
		MenubarComp menubarnComp = (MenubarComp) menubarEditorInput.getCloneElement();
		LFWWidgetTreeItem widgetTreeItem = getWidgetTreeItem();
		if(widgetTreeItem != null&&!widgetTreeItem.isDisposed()){
				lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.COMPONENTS);			
				TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
				for (int i = 0; i < childTreeItems.length; i++) {
					if(childTreeItems[i] instanceof LFWMenubarCompTreeItem){
						LFWMenubarCompTreeItem webT = (LFWMenubarCompTreeItem) childTreeItems[i];
						if(webT.getData() instanceof MenubarComp){
							MenubarComp gr = (MenubarComp) webT.getData();
							if(menubarnComp.getId().equals(gr.getId())){
								tree.setSelection(webT);
								break;
							}
						}
					}
				}
		}
		else{
			this.setDirtyFalse();
			MessageDialog.openError(null, M_menubar.MenubarEditor_0, M_menubar.MenubarEditor_1);
		}
	}
	
	
	/**
	 * 保存Pagemeta到文件中
	 * 
	 * @param widget
	 */
	public void savePagemeta(LfwWindow pagemeta) {
		// 将修改过的menubar对象放入pagemeta中
//		pagemeta.getViewMenus().addMenuBar(menubarTemp);
		
		// 获取项目路径
//		String projectPath = LFWPersTool.getProjectPath();
////		LFWMenubarCompTreeItem menubarTreeItem = input.getMenubarTreeItem();
////		LFWDirtoryTreeItem pagemetaTreeItem = LFWPersTool.getPagemetaTreeItem(menubarTreeItem);
//		LFWDirtoryTreeItem pagemetaTreeItem = LFWPersTool.getPagemetaTreeItem(LFWPersTool.getCurrentTreeItem());
//		String pagemetaNodePath = pagemetaTreeItem.getFile().getPath();
//		DataProviderForDesign dataProvider = new DataProviderForDesign();
//		// 保存Widget到pagemeta.pm中
//		dataProvider.savePagemetaToXml(pagemetaNodePath, "pagemeta.pm",
//				projectPath, pagemeta);
		LFWPersTool.savePagemeta(pagemeta);
	}

	
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		
		/*if(isExistRepeatedMethod()){
			MessageDialog.openError(null, "提示", "事件中存在重复方法，请检查");
			return;
		}*/
		
		IEditorInput input = getEditorInput();
		MenubarEditorInput menubarEditorInput = (MenubarEditorInput)input;
		if(menubarEditorInput.getWidget() != null)
			saveWidget();
		else
			savePagemeta(LFWPersTool.getCurrentPageMeta());
		
	}

	private boolean isExistRepeatedMethod() {
		List<EventConf> confList = new ArrayList<EventConf>();
		MenubarComp comp = this.getMenubarObj().getMenubar();
		List<MenuItem> itemList = comp.getMenuList();
		for(MenuItem item:itemList){
			confList.addAll(item.getEventConfList());
		}
		int size = confList.size();
		String[] events = new String[size];
		for(int i=0;i<size;i++){
			EventConf conf = confList.get(i);
			events[i] = conf.getMethodName();
		}
		if(events.length!=array_unique(events).length){
			return true;
		}
		return false;
	}


	public void saveWidget() {
		// 将修改过的menubar对象放入pagemeta中
		MenubarComp clone = (MenubarComp)menubarTemp.clone();
		LfwView widget = LFWPersTool.getCurrentWidget();
		if(gridId==null)
			widget.getViewMenus().addMenuBar(clone);
		else{
			WebComponent comp=widget.getViewComponents().getComponent(gridId);
			if(comp instanceof GridComp){
				((GridComp) comp).setMenuBar(clone);
			}
		}
		// 获取项目路径
//		String projectPath = LFWPersTool.getProjectPath();
//		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
//		//LFWDirtoryTreeItem pagemetaTreeItem = LFWPersTool.getPagemetaTreeItem(LFWPersTool.getCurrentTreeItem());
//		String filePath = widgetTreeItem.getFile().getPath();
//		DataProviderForDesign dataProvider = new DataProviderForDesign();
//		// 保存Widget到pagemeta.pm中
//		dataProvider.saveWidgettoXml(filePath, "widget.wd", projectPath, widget);
		LFWPersTool.saveWidget(widget);
	}
	
	public LFWAbstractViewPage createViewPage() {
		return new MenubarViewPage();
	}
	
	//数组去重复值
	private String[] array_unique(String[] a) {
        // array_unique
        List<String> list = new LinkedList<String>();
        for(int i = 0; i < a.length; i++) {
            if(!list.contains(a[i])) {
                list.add(a[i]);
            }
        }
        return (String[])list.toArray(new String[list.size()]);
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
////				item.addListener(listener);
////			}
////			setDirtyTrue();
////		}
////		else{
////			IEditorInput input = getEditorInput();
////			MenubarEditorInput menubarEditorInput = (MenubarEditorInput)input;
////			MenubarComp menubarnComp = (MenubarComp) menubarEditorInput.getCloneElement();
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
//	}

	
	protected void editMenuManager(IMenuManager manager) {
		if (null != getCurrentSelection()) {
			StructuredSelection ss = (StructuredSelection) getCurrentSelection();
			Object sel = ss.getFirstElement();
			if (sel instanceof MenuElementPart) {
				MenuElementPart lfwEle = (MenuElementPart) sel;
				Object model = lfwEle.getModel();
				if (model instanceof MenubarElementObj) {
					MenubarElementObj menubarObj = (MenubarElementObj) model;
					// 增加 “增加自定义菜单项” 菜单项
					AddMenuItemAction addMenuItemAction = new AddMenuItemAction(menubarObj);
					
					manager.add(addMenuItemAction);
					
					// 获取当先选中的子项
					Label label = menubarObj.getFigure().getCurrentLabel();
					if (null != label) {
						
						if (label instanceof MenuItemLabel) {
							DelMenuItemAction delMenuItemAction = new DelMenuItemAction((MenuItemLabel)label,
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
						DelMenuItemAction delMenuItemAction = new DelMenuItemAction((MenuItemLabel)label,
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


	public MenubarElementObj getMenubarObj() {
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
		return pagemetaNodePath + "/pagemeta.pm"; //$NON-NLS-1$
	}


	public MenubarComp getMenubarTemp() {
		return menubarTemp;
	}


	protected LfwElementObjWithGraph getLeftElement() {
		return null;
	}


	protected LfwElementObjWithGraph getTopElement() {
		return getMenubarObj();
	}
	
	@Override
	public List<JsEventDesc> getAcceptEventDescs() {
		if(menubarTemp != null){
			return menubarTemp.getAcceptEventDescs();
		}else{
			return menubarObj.getMenubar().getAcceptEventDescs();
		}
	}

	public void setMenubarObj(MenubarElementObj menubarObj) {
		this.menubarObj = menubarObj;
	}
	
	public LfwView getWidget() {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		if(widgetTreeItem != null){
			return widgetTreeItem.getWidget();
		}
		return null;
	}

	public void setWidget(LfwView widget) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		if(widgetTreeItem != null){
			widgetTreeItem.setWidget(widget);
		}
	}

	public void setMenubarTemp(MenubarComp menubarTemp) {
		this.menubarTemp = menubarTemp;
	}

}
