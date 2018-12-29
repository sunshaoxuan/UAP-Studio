package nc.uap.lfw.pagination;

import java.util.EventObject;
import java.util.Iterator;
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
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.PaginationComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;

public class PaginationEditor extends LFWBaseEditor{

	private PaginationGraph graph = new PaginationGraph();
	
	public PaginationEditor(){
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
	public void commandStackChanged(EventObject arg0) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(arg0);
	}
	
	/**
	 * 聚焦编辑器
	 */
	public void setFocus() {
		super.setFocus();
		Tree tree = LFWPersTool.getTree();
		IEditorInput input = getEditorInput();
		PaginationEditorInput paginationEditorInput= (PaginationEditorInput)input;
		if(paginationEditorInput.getCurrentTreeItem().isDisposed())
			return;
		LFWSeparateTreeItem lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.COMPONENTS);		
		PaginationComp paginationComp = (PaginationComp) paginationEditorInput.getCloneElement();
		TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
		for (int i = 0; i < childTreeItems.length; i++) {
			LFWBasicTreeItem webT = (LFWBasicTreeItem) childTreeItems[i];
			if(webT.getData() instanceof PaginationComp){
				PaginationComp gr = (PaginationComp) webT.getData();
				if(paginationComp.getId().equals(gr.getId())){
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
	public void refreshTreeItemText(PaginationComp pagination){
		TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
		if(treeItem instanceof LFWWebComponentTreeItem){
			LFWWebComponentTreeItem paginationItem = (LFWWebComponentTreeItem) treeItem;
			paginationItem.setText("[分页条]" + pagination.getId()); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	public void doSave(IProgressMonitor monitor) {
		// 保存Event代码
		super.doSave(monitor);
//		String msg = graph.getCells().get(0).validate();
//		if(msg != null){
//			String message = "列表输入信息有错误，是否还要保存："+msg;
//			if(!MessageDialog.openConfirm(getSite().getShell(), "提示", message))
//				return;
//		}
		//TODO 其他保存操作
		save();
	}
	//保存
	public boolean save(){
		IEditorInput input = getEditorInput();
		PaginationEditorInput paginationEditorInput = (PaginationEditorInput)input;
		PaginationComp pagination = (PaginationComp) paginationEditorInput.getCloneElement();
		PaginationElementObj paginationElementObj = (PaginationElementObj) this.graph.getCells().get(0);
		PaginationComp pagenew = paginationElementObj.getPaginationComp();
		pagenew.setId(pagination.getId());
//		gridnew.setListenerMap(grid.getListenerMap());
		if(pagenew.getFrom() != null)
			pagenew.setConfType(WebElement.CONF_REF);
		ContextMenuComp contextMenuComp = null;
		if(graph.getContextMenu() != null && graph.getContextMenu().size() > 0){
			ContextMenuElementObj contextMenuEle = graph.getContextMenu().get(0);
			if(contextMenuEle != null)
				contextMenuComp = contextMenuEle.getMenubar();
			if(contextMenuComp != null)
				pagenew.setContextMenu(contextMenuComp.getId());
		}
		PaginationComp clone = (PaginationComp)pagenew.clone();
		if(widget != null){
			Map<String, WebComponent> paginationMap = widget.getViewComponents().getComponentsMap();
			boolean flag = false;
			for (Iterator<String> itwd = paginationMap.keySet().iterator(); itwd.hasNext();) {
				String lvId = (String) itwd.next();
				if(paginationMap.get(lvId) instanceof PaginationComp){
					PaginationComp  newlv  = (PaginationComp)paginationMap.get(lvId);
					if(clone.getId().equals(newlv.getId())){
						paginationMap.put(clone.getId(), clone);
						flag = true;
						break;
					}
				}
			}
			if(!flag)
				paginationMap.put(clone.getId(), clone);
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
	private PaginationElementObj pageElement;
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		PaginationEditorInput pageInput = (PaginationEditorInput)input;
		widgeTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		widget = widgeTreeItem.getWidget();
		pageElement = new PaginationElementObj();
		PaginationComp paginationComp = (PaginationComp) pageInput.getCloneElement();
		pageElement.setPaginationComp(paginationComp);
		pageElement.setLocation(new Point(100, 100));
		graph.addCell(pageElement);
//		if(graph.getJsListeners() == null || graph.getJsListeners().size() == 0){
//			// 绘制Listener图形
////			Map<String, JsListenerConf> listenerMap = gridcomp.getListenerMap();
////			addListenerCellToEditor(listenerMap, graph);
//		}
		String dsId = paginationComp.getDataset();
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
				DatasetToPaginationConnection conn = new DatasetToPaginationConnection(pageElement, dsobj);
				conn.connect();
			}
		}
		//contextMenu
		String contextMenuId = paginationComp.getContextMenu();
		if(contextMenuId != null && !contextMenuId.equals("")){ //$NON-NLS-1$
			ContextMenuComp contextMenuComp = widget.getViewMenus().getContextMenu(contextMenuId);
			ContextMenuElementObj contextMenuElement = new ContextMenuElementObj();
			contextMenuElement.setMenubar(contextMenuComp);
			contextMenuElement.setLocation(new Point(100, 300));
			contextMenuElement.setSize(new Dimension(150, 150));
			graph.addCell(contextMenuElement);
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
	
	public static PaginationEditor getActiveEditor(){
		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if(page != null){
			editor = page.getActiveEditor();

		}
		if(editor != null && editor instanceof PaginationEditor){
			return (PaginationEditor)editor;
		}else {
			return null;
		}
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
	private PaletteRoot paleteRoot = null;
	protected PaletteRoot getPaletteRoot() {
		if(paleteRoot == null){
			paleteRoot = PaletteFactory.createPaginationPalette();
		}
		return paleteRoot;
	}
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()){
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}
			
		};
	}
	public PaginationGraph getGraph() {
		return graph;
	}

	public void setGraph(PaginationGraph graph) {
		this.graph = graph;
	}

	@Override
	protected LfwElementObjWithGraph getTopElement() {
		return graph.getCells().get(0);
	}

	@Override
	protected LfwElementObjWithGraph getLeftElement() {
		return null;
	}

	@Override
	public LFWAbstractViewPage createViewPage() {
		return null;
	}

	@Override
	protected void editMenuManager(IMenuManager manager) {
	}

}
