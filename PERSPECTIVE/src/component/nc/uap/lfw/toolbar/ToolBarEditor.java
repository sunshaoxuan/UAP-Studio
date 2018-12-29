package nc.uap.lfw.toolbar;

import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.ToolBarComp;
import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.lang.M_toolbar;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;

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

public class ToolBarEditor  extends LFWBaseEditor {
	
	private ToolBarItem currentListenerToolItem;
	private PaletteRoot paleteRoot = null;
	private ToolBarGraph graph = new ToolBarGraph();
	
	protected PaletteRoot getPaletteRoot() {
		if(paleteRoot == null){
			paleteRoot = PaletteFactory.createToolbarPalette();
		}
		return paleteRoot;
	}
	
	public ToolBarItem getCurrentListenerToolItem() {
		return currentListenerToolItem;
	}

	public void setCurrentListenerToolItem(ToolBarItem currentListenerToolItem) {
		this.currentListenerToolItem = currentListenerToolItem;
	}

	public ToolBarGraph getGraph() {
		return graph;
	}

	public void setGraph(ToolBarGraph graph) {
		this.graph = graph;
	}

	public ToolBarEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
		getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		setPartName(input.getName());
	}
	public boolean isDirty() {
		if(super.isDirty())
			return true;
		return getCommandStack().isDirty();
	}

	@Override
	public void commandStackChanged(EventObject arg0) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(arg0);
	}
	
	public void doSave(IProgressMonitor monitor) {
		//TODO 其他保存操作
		super.doSave(monitor);
		String msg = graph.getCells().get(0).validate();
		if(msg != null){
			String message = M_toolbar.ToolBarEditor_0+msg;
			if(!MessageDialog.openConfirm(getSite().getShell(), M_toolbar.ToolBarEditor_1, message))
				return;
		}
		save();
	}
	
	
	public void  deleteNode(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		IEditorInput input = getEditorInput();
		ToolBarEditorInput toolbarEditorInput = (ToolBarEditorInput)input;
		ToolBarComp toolbar = (ToolBarComp)toolbarEditorInput.getCloneElement();
		widget = LFWPersTool.getCurrentWidget();
		WebComponent[] webcomps = widget.getViewComponents().getComponents();
		boolean isExits = false;
		for (int i = 0; i < webcomps.length; i++) {
			WebComponent web = webcomps[i];
			if(web instanceof ToolBarComp){
				if(web.getId().equals(toolbar.getId())){
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
	
	public void setFocus() {
		super.setFocus();
		Tree tree = LFWPersTool.getTree();
		IEditorInput input = getEditorInput();
		ToolBarEditorInput toolbarEditorInput = (ToolBarEditorInput)input;
		if(toolbarEditorInput.getCurrentTreeItem().isDisposed())
			return;
		LFWSeparateTreeItem lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.COMPONENTS);		
		ToolBarComp toolbar = (ToolBarComp) toolbarEditorInput.getCloneElement();
		TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
		for (int i = 0; i < childTreeItems.length; i++) {
			LFWWebComponentTreeItem webT = (LFWWebComponentTreeItem) childTreeItems[i];
			if(webT.getData() instanceof ToolBarComp){
				ToolBarComp toolb = (ToolBarComp) webT.getData();
				if(toolbar.getId().equals(toolb.getId())){
					tree.setSelection(webT);
					break;
				}
			}
		}

	}
	
	//保存
	public boolean save(){
		IEditorInput input = getEditorInput();
		ToolBarEditorInput toolbarEditorInput = (ToolBarEditorInput)input;
		ToolBarComp toolbar = (ToolBarComp) toolbarEditorInput.getCloneElement();
		ToolBarElementObj toolbarobj = (ToolBarElementObj) this.graph.getCells().get(0);
		ToolBarComp toolbarnew = toolbarobj.getToolbarComp();
		toolbarnew.setId(toolbar.getId());
		toolbarobj.setToolbarComp(toolbarnew);
		ContextMenuComp contextMenuComp = null;
		if(graph.getContextMenu() != null && graph.getContextMenu().size() > 0){
			ContextMenuElementObj contextMenuEle = graph.getContextMenu().get(0);
			if(contextMenuEle != null)
				contextMenuComp = contextMenuEle.getMenubar();
			if(contextMenuComp != null)
				toolbarnew.setContextMenu(contextMenuComp.getId());
		}
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		if(widgetTreeItem != null){
			LfwView lfwwidget = widgetTreeItem.getWidget();
			
			if(lfwwidget != null){
			Map<String, WebComponent> toolbarmap = lfwwidget.getViewComponents().getComponentsMap();
			boolean flag = false;
			for (Iterator<String> itwd = toolbarmap.keySet().iterator(); itwd.hasNext();) {
				String toolbarId = (String) itwd.next();
				if(toolbarmap.get(toolbarId) instanceof ToolBarComp){
					ToolBarComp newtoolbar  = (ToolBarComp)toolbarmap.get(toolbarId);
					if(toolbar.getId().equals(newtoolbar.getId())){
						toolbarmap.put(toolbar.getId(), toolbarnew);
						flag = true;
						break;
					}
				}
			}
			if(!flag)
				toolbarmap.put(toolbarnew.getId(),toolbarnew);
			}
			
//			String projectPath = LFWPersTool.getProjectPath();
//			LFWDirtoryTreeItem pagemetaTreeItem = (LFWDirtoryTreeItem)widgeTreeItem.getParentItem();
//			String pagemetaNode = pagemetaTreeItem.getFile().getPath();
//			String filePath = pagemetaNode + "/" + widgeTreeItem.getText().substring(5).trim();
//			String fileName = "widget.wd";
//			DataProviderForDesign dataProvider = new DataProviderForDesign();
//			dataProvider.saveWidgettoXml(filePath, fileName, projectPath, lfwwidget);
			LFWPersTool.saveWidget(lfwwidget);
			getCommandStack().markSaveLocation();
			//更新左边树节点
			refreshTreeItem(toolbarnew);
			super.setDirtyFalse();
			return true;
		}
		return false;
	}

	
	private LFWWidgetTreeItem widgeTreeItem = null;
	private LfwView widget = null;
	private ToolBarElementObj toolbarElement;
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		ToolBarEditorInput toolbarEditor = (ToolBarEditorInput)input;
		widgeTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		widget = widgeTreeItem.getWidget();
		toolbarElement = new ToolBarElementObj();
		ToolBarComp toolbarcomp = (ToolBarComp) toolbarEditor.getCloneElement();
		toolbarElement.setToolbarComp(toolbarcomp);
		toolbarElement.setLocation(new Point(100, 100));
		graph.addCell(toolbarElement);
		// 绘制Listener图形
//		Map<String, JsListenerConf> listenerMap = toolbarcomp.getListenerMap();
//		addListenerCellToEditor(listenerMap, graph);
		
		//contextMenu
		String contextMenuId = toolbarcomp.getContextMenu();
		if(contextMenuId != null && !contextMenuId.equals("")){ //$NON-NLS-1$
			ContextMenuComp contextMenuComp = widget.getViewMenus().getContextMenu(contextMenuId);
			ContextMenuElementObj contextMenuElement = new ContextMenuElementObj();
			contextMenuElement.setMenubar(contextMenuComp);
			contextMenuElement.setLocation(new Point(400, 100));
			contextMenuElement.setSize(new Dimension(150, 150));
			graph.addCell(contextMenuElement);
		}
	}

	
	public static ToolBarEditor getActiveEditor(){
		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if(page != null){
			editor = page.getActiveEditor();

		}
		if(editor != null && editor instanceof ToolBarEditor){
			return (ToolBarEditor)editor;
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
	
	
	@Override
	public LFWAbstractViewPage createViewPage() {
		return new ToolBarViewPage();
	}

	@Override
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
		return toolbarElement.getToolbarComp().getAcceptEventDescs();
	}

	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

	public ToolBarElementObj getToolbarElement() {
		return toolbarElement;
	}

	public void setToolbarElement(ToolBarElementObj toolbarElement) {
		this.toolbarElement = toolbarElement;
	}
	
}

