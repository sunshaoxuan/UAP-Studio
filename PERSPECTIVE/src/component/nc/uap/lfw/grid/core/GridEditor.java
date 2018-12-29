package nc.uap.lfw.grid.core;

import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.GridTreeLevel;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.grid.DatasetToGridConnection;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.grid.GridGraph;
import nc.uap.lfw.lang.M_grid;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;
import nc.uap.lfw.tree.TreeTopLevelConnection;
import nc.uap.lfw.tree.treelevel.TreeLevelChildConnection;

import org.eclipse.core.runtime.IProgressMonitor;
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
 * grid editor
 * @author zhangxya
 *
 */
public class GridEditor extends LFWBaseEditor {
	private GridGraph graph = new GridGraph();
	public GridGraph getGraph() {
		return graph;
	}

	public void setGraph(GridGraph graph) {
		this.graph = graph;
	}

	public GridEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
		getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}
	
	
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		setPartName(input.getName());
	}
	public boolean isDirty() {
		if (super.isDirty())
			return true;
		return getCommandStack().isDirty();
	}

	
	public void  deleteNode(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		IEditorInput input = getEditorInput();
		GridEditorInput gridEditorInput = (GridEditorInput)input;
		GridComp grid = (GridComp)gridEditorInput.getCloneElement();
		Tree tree = LFWPersTool.getTree();
//		tree.getSelection()grid.getpa
//		tree.setSelection(gridEditorInput.getCurrentTreeItem().getItems());
		WebComponent[] webcomps = widget.getViewComponents().getComponents();
		boolean isExits = false;
		for (int i = 0; i < webcomps.length; i++) {
			WebComponent web = webcomps[i];
			if(web instanceof GridComp){
				if(web.getId().equals(grid.getId())){
					isExits = true;
					break;
				}
			}
		}
		if(!isExits){
			 try {
				view.deleteNewNode();
			} catch (Exception e) {
				MainPlugin.getDefault().logError(e.getMessage(), e);
			}
		 }
	}
	
	
	public void commandStackChanged(EventObject arg0) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(arg0);
	}
	public void doSave(IProgressMonitor monitor) {
		// 保存Event代码
		super.doSave(monitor);
		String msg = graph.getCells().get(0).validate();
		if(msg != null){
			String message = M_grid.GridEditor_0+msg;
			if(!MessageDialog.openConfirm(getSite().getShell(), M_grid.GridEditor_1, message))
				return;
		}
		//TODO 其他保存操作
		save();
	}
	
	/**
	 * 聚焦编辑器
	 */
	public void setFocus() {
		super.setFocus();
		Tree tree = LFWPersTool.getTree();
		IEditorInput input = getEditorInput();
		GridEditorInput gridEditorInput = (GridEditorInput)input;
		if(gridEditorInput.getCurrentTreeItem().isDisposed())
			return;
		LFWSeparateTreeItem lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.COMPONENTS);		
		GridComp gridcomp = (GridComp) gridEditorInput.getCloneElement();
		TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
		for (int i = 0; i < childTreeItems.length; i++) {
			LFWBasicTreeItem webT = (LFWBasicTreeItem) childTreeItems[i];
			if(webT.getData() instanceof GridComp){
				GridComp gr = (GridComp) webT.getData();
				if(gridcomp.getId().equals(gr.getId())){
					tree.setSelection(webT);
					break;
				}
			}
		}
	}
	
	/**
	 * 更新左边树的显示信息
	 * @param grid
	 */
	public void refreshTreeItemText(GridComp grid){
		TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
		if(treeItem instanceof LFWWebComponentTreeItem){
			LFWWebComponentTreeItem gridTreeItem = (LFWWebComponentTreeItem) treeItem;
			gridTreeItem.setText(WEBPersConstants.COMPONENT_GRID + grid.getId()+ "[" + grid.getCaption() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	//保存
	public boolean save(){
		IEditorInput input = getEditorInput();
		GridEditorInput gridEditorInput = (GridEditorInput)input;
		GridComp grid = (GridComp) gridEditorInput.getCloneElement();
		GridElementObj gridobj = (GridElementObj) this.graph.getCells().get(0);
		GridComp gridnew = gridobj.getGridComp();
		gridnew.setId(grid.getId());
//		gridnew.setListenerMap(grid.getListenerMap());
		if(gridnew.getFrom() != null)
			gridnew.setConfType(WebElement.CONF_REF);
		ContextMenuComp contextMenuComp = null;
		if(graph.getContextMenu() != null && graph.getContextMenu().size() > 0){
			ContextMenuElementObj contextMenuEle = graph.getContextMenu().get(0);
			if(contextMenuEle != null)
				contextMenuComp = contextMenuEle.getMenubar();
			if(contextMenuComp != null)
				gridnew.setContextMenu(contextMenuComp.getId());
		}
		GridComp clone = (GridComp)gridnew.clone();
		if(widget != null){
			Map<String, WebComponent> gridmap = widget.getViewComponents().getComponentsMap();
			boolean flag = false;
			for (Iterator<String> itwd = gridmap.keySet().iterator(); itwd.hasNext();) {
				String gridId = (String) itwd.next();
				if(gridmap.get(gridId) instanceof GridComp){
					GridComp  newgrid  = (GridComp)gridmap.get(gridId);
					if(clone.getId().equals(newgrid.getId())){
						gridmap.put(clone.getId(), clone);
						flag = true;
						break;
					}
				}
			}
			if(!flag)
				gridmap.put(clone.getId(), clone);
			LFWPersTool.saveWidget(widget);
			getCommandStack().markSaveLocation();
			//更新左边树节点
			refreshTreeItem(clone);
			return true;
		}
		return false;

	}

	
	private LFWWidgetTreeItem widgeTreeItem = null;
	private LfwView widget = null;
	private GridElementObj gridElement;
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		GridEditorInput gridEditor = (GridEditorInput)input;
		widgeTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		widget = widgeTreeItem.getWidget();
		gridElement = new GridElementObj();
		GridComp gridcomp = (GridComp) gridEditor.getCloneElement();
		gridElement.setGridComp(gridcomp);
		gridElement.setLocation(new Point(100, 100));
		graph.addCell(gridElement);
//		if(graph.getJsListeners() == null || graph.getJsListeners().size() == 0){
//			// 绘制Listener图形
////			Map<String, JsListenerConf> listenerMap = gridcomp.getListenerMap();
////			addListenerCellToEditor(listenerMap, graph);
//		}
		String dsId = gridcomp.getDataset();
		if(dsId != null){
			RefDatasetElementObj dsobj = null;
			Dataset ds = widget.getViewModels().getDataset(dsId);
			if(ds != null){
				dsobj = new RefDatasetElementObj();
				dsobj.setLocation(new Point(400,100));
				dsobj.setSize(new Dimension(150,150));
				dsobj.setDs(ds);
				graph.addCell(dsobj);
			}
			if(dsobj != null){
				DatasetToGridConnection conn = new DatasetToGridConnection(gridElement, dsobj);
				conn.connect();
			}
		}
		//contextMenu
		String contextMenuId = gridcomp.getContextMenu();
		if(contextMenuId != null && !contextMenuId.equals("")){ //$NON-NLS-1$
			ContextMenuComp contextMenuComp = widget.getViewMenus().getContextMenu(contextMenuId);
			ContextMenuElementObj contextMenuElement = new ContextMenuElementObj();
			contextMenuElement.setMenubar(contextMenuComp);
			contextMenuElement.setLocation(new Point(100, 300));
			contextMenuElement.setSize(new Dimension(150, 150));
			graph.addCell(contextMenuElement);
		}
		
		GridTreeLevel topTreeLevel = gridcomp.getTopLevel();
		GridLevelElementObj topGridleveEle = null;
		if(topTreeLevel != null){
			String datasetId = topTreeLevel.getDataset();
			if(datasetId != null){
				Dataset ds = widget.getViewModels().getDataset(datasetId);
				if(ds != null){
					topGridleveEle = new GridLevelElementObj();
					topGridleveEle.setLocation(new Point(350,100));
					topGridleveEle.setSize(new Dimension(100,100));
					topGridleveEle.setDs(ds);
					topGridleveEle.setId(topTreeLevel.getId());
					graph.addCell(topGridleveEle);
//					String levelContextMenuId = topTreeLevel.getContextMenu();
//					if(levelContextMenuId != null && !levelContextMenuId.equals("")){
//						ContextMenuComp contextMenuComp = widget.getViewMenus().getContextMenu(levelContextMenuId);
//						ContextMenuElementObj contextMenuElement = new ContextMenuElementObj();
//						contextMenuElement.setMenubar(contextMenuComp);
//						contextMenuElement.setLocation(new Point(350, 300));
//						contextMenuElement.setSize(new Dimension(100, 100));
//						graph.addCell(contextMenuElement);
//					}
				}
				if(topGridleveEle != null){
					topGridleveEle.setParentTreeLevel(gridElement);
					TreeTopLevelConnection conn = new TreeTopLevelConnection(gridElement, topGridleveEle);
					conn.connect();
				}
			}
			GridTreeLevel childTreelevel = topTreeLevel.getChildTreeLevel();
			while(childTreelevel != null){
				GridLevelElementObj childTreelevelElement = null;
				String childds  = childTreelevel.getDataset();
				Dataset ds = widget.getViewModels().getDataset(childds);
				if(ds != null){
					childTreelevelElement = new GridLevelElementObj();
					int size = graph.getALlGridLevelElements().size();
					GridLevelElementObj lastTreelleveEle = (GridLevelElementObj)graph.getALlGridLevelElements().get(size - 1);
					int x = lastTreelleveEle.getLocation().x + lastTreelleveEle.getSize().width + WEBPersConstants.DATASETBETWEEN;
					int y = lastTreelleveEle.getLocation().y;
					childTreelevelElement.setLocation(new Point(x, y));
					childTreelevelElement.setSize(new Dimension(100,100));
					childTreelevelElement.setDs(ds);
					childTreelevelElement.setId(childTreelevel.getId());
					graph.addCell(childTreelevelElement);
//					String levelContextMenuId = childTreelevel.getContextMenu();
//					if(levelContextMenuId != null && !levelContextMenuId.equals("")){
//						ContextMenuComp contextMenuComp = widget.getViewMenus().getContextMenu(levelContextMenuId);
//						ContextMenuElementObj contextMenuElement = new ContextMenuElementObj();
//						contextMenuElement.setMenubar(contextMenuComp);
//						contextMenuElement.setLocation(new Point(x, y + 200));
//						contextMenuElement.setSize(new Dimension(100, 100));
//						graph.addCell(contextMenuElement);
//					}
					
					childTreelevelElement.setParentTreeLevel(topGridleveEle);
					TreeLevelChildConnection childcon = new TreeLevelChildConnection(topGridleveEle, childTreelevelElement);
					childcon.connect();
					//break;
				}
				topGridleveEle = childTreelevelElement;
				childTreelevel = childTreelevel.getChildTreeLevel();
			}
		}
	}

	
	public static GridEditor getActiveEditor(){
//		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
//		if(page != null){
//			editor = page.getActiveEditor();
//
//		}
		editor = LFWBaseEditor.getActiveEditor();
		if(editor != null && editor instanceof GridEditor){
			return (GridEditor)editor;
		}else {
			return null;
		}
		
	}
	
	
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return true;
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
		getGraphicalViewer().setEditPartFactory(new ElementEidtPartFactory(this));
		getGraphicalViewer().setKeyHandler(getShareKeyHandler());
		getGraphicalViewer().setContextMenu(getMenuManager());
	}
	
	
	
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
	    getGraphicalViewer().setContents(this.graph);
	    getGraphicalViewer().addDropTargetListener(new nc.uap.lfw.perspective.editor.DiagramTemplateTransferDropTargetListener(getGraphicalViewer()));
        LFWPersTool.showView(IPageLayout.ID_PROP_SHEET);
	}
	
	
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()){
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}
			
		};
	}
	
	private PaletteRoot paleteRoot = null;
	protected PaletteRoot getPaletteRoot() {
		if(paleteRoot == null){
			paleteRoot = PaletteFactory.createGridPalette();
		}
		return paleteRoot;
	}

	
	public GridViewPage createViewPage() {
		return new GridViewPage();
	}

	
//	public void saveJsListener(String jsListenerId, EventHandlerConf jsEventHandler, JsListenerConf listener) {
//		if(widget == null)
//			widget = LFWPersTool.getCurrentWidgetTreeItem().getWidget();
//		if(widget != null){
//			IEditorInput input = getEditorInput();
//			GridEditorInput gridEditorInput = (GridEditorInput)input;
//			GridComp grid =  (GridComp) gridEditorInput.getCloneElement();
//			Map<String, WebComponent> gridMap = widget.getViewComponents().getComponentsMap();
//			GridComp addListenergrid = null;
//			for (Iterator<String> itwd = gridMap.keySet().iterator(); itwd.hasNext();) {
//				WebComponent webcomp = gridMap.get(itwd.next());
//				if(webcomp instanceof GridComp && webcomp.getId().equals(grid.getId())){
//					addListenergrid = (GridComp) webcomp;
//					break;
//				}
//			}
//			if(addListenergrid == null)
//				addListenergrid = grid;
////			if (null != jsEventHandler) {
////				if (addListenergrid.getListenerMap().containsKey(jsListenerId)) {
////					JsListenerConf jsListener = addListenergrid.getListenerMap().get(jsListenerId);
////					doSaveListenerEvent(jsListener, jsEventHandler);
////				}
////			} else {
////				addListenergrid.addListener(listener);
////			}
//			setDirtyTrue();
//		}
//	}

	
	protected void editMenuManager(IMenuManager manager) {
	}


	
	@Override
	protected LfwElementObjWithGraph getLeftElement() {
		return null;
	}

	@Override
	protected LfwElementObjWithGraph getTopElement() {
		return graph.getCells().get(0);
	}

	@Override
	public List<JsEventDesc> getAcceptEventDescs() {
		return gridElement.getGridComp().getAcceptEventDescs();
	}

	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}
	
	public GridElementObj getGridElement() {
		return gridElement;
	}

	public void setGridElement(GridElementObj gridElement) {
		this.gridElement = gridElement;
	}
	
}

