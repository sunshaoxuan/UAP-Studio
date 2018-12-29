package nc.uap.lfw.listview.core;

import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.ListViewComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.grid.DatasetToGridConnection;
import nc.uap.lfw.lang.M_listview;
import nc.uap.lfw.listview.DatasetToListViewConnection;
import nc.uap.lfw.listview.ListViewElementObj;
import nc.uap.lfw.listview.ListViewGraph;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;

public class ListViewEditor  extends LFWBaseEditor {
	
	private ListViewGraph graph = new ListViewGraph();
	public ListViewGraph getGraph() {
		return graph;
	}

	public void setGraph(ListViewGraph graph) {
		this.graph = graph;
	}

	public ListViewEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
		getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}
	
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
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
		ListViewEditorInput listviewEditorInput = (ListViewEditorInput)input;
		ListViewComp listview = (ListViewComp)listviewEditorInput.getCloneElement();
		
		WebComponent[] webcomps = widget.getViewComponents().getComponents();
		boolean isExits = false;
		for (int i = 0; i < webcomps.length; i++) {
			WebComponent web = webcomps[i];
			if(web instanceof ListViewComp){
				if(web.getId().equals(listview.getId())){
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
			String message = M_listview.ListViewEditor_0+msg;
			if(!MessageDialog.openConfirm(getSite().getShell(), M_listview.ListViewEditor_1, message))
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
		ListViewEditorInput listviewEditorInput = (ListViewEditorInput)input;
		if(listviewEditorInput.getCurrentTreeItem().isDisposed())
			return;
		LFWSeparateTreeItem lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.COMPONENTS);
		ListViewComp listviewcomp = (ListViewComp) listviewEditorInput.getCloneElement();
		TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
		for (int i = 0; i < childTreeItems.length; i++) {
			LFWBasicTreeItem webT = (LFWBasicTreeItem) childTreeItems[i];
			if(webT.getData() instanceof ListViewComp){
				ListViewComp gr = (ListViewComp) webT.getData();
				if(listviewcomp.getId().equals(gr.getId())){
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
	public void refreshTreeItemText(ListViewComp listview){
		TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
		if(treeItem instanceof LFWWebComponentTreeItem){
			LFWWebComponentTreeItem listviewTreeItem = (LFWWebComponentTreeItem) treeItem;
			listviewTreeItem.setText(WEBPersConstants.COMPONENT_LISTVIEW + listview.getId()+ "[" + listview.getCaption() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	//保存
	public boolean save(){
		IEditorInput input = getEditorInput();
		ListViewEditorInput listviewEditorInput = (ListViewEditorInput)input;
		ListViewComp listview = (ListViewComp) listviewEditorInput.getCloneElement();
		ListViewElementObj listviewobj = (ListViewElementObj) this.graph.getCells().get(0);
		ListViewComp listviewnew = listviewobj.getlistviewComp();
		listviewnew.setId(listview.getId());
//		gridnew.setListenerMap(grid.getListenerMap());
		if(listviewnew.getFrom() != null)
			listviewnew.setConfType(WebElement.CONF_REF);
		ContextMenuComp contextMenuComp = null;
		if(graph.getContextMenu() != null && graph.getContextMenu().size() > 0){
			ContextMenuElementObj contextMenuEle = graph.getContextMenu().get(0);
			if(contextMenuEle != null)
				contextMenuComp = contextMenuEle.getMenubar();
			if(contextMenuComp != null)
				listviewnew.setContextMenu(contextMenuComp.getId());
		}
		ListViewComp clone = (ListViewComp)listviewnew.clone();
		if(widget != null){
			Map<String, WebComponent> listviewmap = widget.getViewComponents().getComponentsMap();
			boolean flag = false;
			for (Iterator<String> itwd = listviewmap.keySet().iterator(); itwd.hasNext();) {
				String lvId = (String) itwd.next();
				if(listviewmap.get(lvId) instanceof ListViewComp){
					ListViewComp  newlv  = (ListViewComp)listviewmap.get(lvId);
					if(clone.getId().equals(newlv.getId())){
						listviewmap.put(clone.getId(), clone);
						flag = true;
						break;
					}
				}
			}
			if(!flag)
				listviewmap.put(clone.getId(), clone);
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
	private ListViewElementObj lvElement;
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		ListViewEditorInput lvEditor = (ListViewEditorInput)input;
		widgeTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		widget = widgeTreeItem.getWidget();
		lvElement = new ListViewElementObj();
		ListViewComp lvcomp = (ListViewComp) lvEditor.getCloneElement();
		lvElement.setlistviewComp(lvcomp);
		lvElement.setLocation(new Point(100, 100));
		graph.addCell(lvElement);
//		if(graph.getJsListeners() == null || graph.getJsListeners().size() == 0){
//			// 绘制Listener图形
////			Map<String, JsListenerConf> listenerMap = gridcomp.getListenerMap();
////			addListenerCellToEditor(listenerMap, graph);
//		}
		String dsId = lvcomp.getDataset();
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
				DatasetToListViewConnection conn = new DatasetToListViewConnection(lvElement, dsobj);
				conn.connect();
			}
		}
		//contextMenu
		String contextMenuId = lvcomp.getContextMenu();
		if(contextMenuId != null && !contextMenuId.equals("")){ //$NON-NLS-1$
			ContextMenuComp contextMenuComp = widget.getViewMenus().getContextMenu(contextMenuId);
			ContextMenuElementObj contextMenuElement = new ContextMenuElementObj();
			contextMenuElement.setMenubar(contextMenuComp);
			contextMenuElement.setLocation(new Point(100, 300));
			contextMenuElement.setSize(new Dimension(150, 150));
			graph.addCell(contextMenuElement);
		}
		
	}

	
	public static ListViewEditor getActiveEditor(){
		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if(page != null){
			editor = page.getActiveEditor();

		}
		if(editor != null && editor instanceof ListViewEditor){
			return (ListViewEditor)editor;
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
			paleteRoot = PaletteFactory.createListViewPalette();
		}
		return paleteRoot;
	}

	
//	public ListViewViewPage createViewPage() {
//		return new ListViewViewPage();
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
		return lvElement.getlistviewComp().getAcceptEventDescs();
	}

	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}
	
	public ListViewElementObj getGridElement() {
		return lvElement;
	}

	public void setListViewElement(ListViewElementObj lvElement) {
		this.lvElement = lvElement;
	}

	@Override
	public LFWAbstractViewPage createViewPage() {
		// TODO Auto-generated method stub
		return null;
	}
	
}