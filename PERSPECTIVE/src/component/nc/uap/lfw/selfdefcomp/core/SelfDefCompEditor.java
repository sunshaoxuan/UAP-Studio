package nc.uap.lfw.selfdefcomp.core;

import java.util.EventObject;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.views.LFWViewPage;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
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

public class SelfDefCompEditor extends LFWBaseEditor {
	private SelfDefCompGraph graph = new SelfDefCompGraph();


	public SelfDefCompGraph getGraph() {
		return graph;
	}


	public void setGraph(SelfDefCompGraph graph) {
		this.graph = graph;
	}


	public SelfDefCompEditor() {
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
		if(super.isDirty())
			return true;
		return getCommandStack().isDirty();
	}

	
	public void  deleteNode(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		IEditorInput input = getEditorInput();
		SelfDefCompEditorInput selfEditorInput = (SelfDefCompEditorInput)input;
		SelfDefComp selfdef = (SelfDefComp)selfEditorInput.getCloneElement();
		widget = LFWPersTool.getCurrentWidget();
		WebComponent[] webcomps = widget.getViewComponents().getComponents();
		boolean isExits = false;
		for (int i = 0; i < webcomps.length; i++) {
			WebComponent web = webcomps[i];
			if(web instanceof SelfDefComp){
				if(web.getId().equals(selfdef.getId())){
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
		SelfDefCompEditorInput selfEditorInput = (SelfDefCompEditorInput)input;
		if(selfEditorInput.getCurrentTreeItem().isDisposed())
			return;
		LFWSeparateTreeItem lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.COMPONENTS);		
		SelfDefComp selfdefComp = (SelfDefComp) selfEditorInput.getCloneElement();
		TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
		for (int i = 0; i < childTreeItems.length; i++) {
			LFWBasicTreeItem webT = (LFWBasicTreeItem) childTreeItems[i];
			if(webT.getData() instanceof SelfDefComp){
				SelfDefComp gr = (SelfDefComp) webT.getData();
				if(selfdefComp.getId().equals(gr.getId())){
					tree.setSelection(webT);
					break;
				}
			}
		}

	}
	
	
	public void commandStackChanged(EventObject arg0) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(arg0);
	}
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		String msg = graph.getCells().get(0).validate();
		if(msg != null){
			String message = "按钮输入信息有错误，是否还要保存：\r\n\r\n\r\n"+msg;
			if(!MessageDialog.openConfirm(getSite().getShell(), "提示", message))
				return;
		}
		save();
	}
	
	
	//保存
	public boolean save(){
		IEditorInput input = getEditorInput();
		SelfDefCompEditorInput selfEditorInput = (SelfDefCompEditorInput)input;
		SelfDefComp selfdefComp = (SelfDefComp) selfEditorInput.getCloneElement();
		SelfDefEleObj selfdefobj = (SelfDefEleObj) this.graph.getCells().get(0);
		SelfDefComp selfdefnew = selfdefobj.getSelfDefComp();
		selfdefnew.setId(selfdefComp.getId());
		SelfDefComp clone = (SelfDefComp)selfdefnew.clone();
		if(widgeTreeItem != null){
			LfwView lfwwidget = widgeTreeItem.getWidget();
			if(lfwwidget != null){
			Map<String, WebComponent> map = lfwwidget.getViewComponents().getComponentsMap();
			map.put(clone.getId(), clone);
			LFWPersTool.saveWidget(lfwwidget);
			getCommandStack().markSaveLocation();
			//更新左边的树节点
			refreshTreeItem(clone);
			return true;
			}
		}
		return false;
	}

	
	private LFWWidgetTreeItem widgeTreeItem = null;
	private LfwView widget = null;
	private SelfDefEleObj selfdefElement;
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		SelfDefCompEditorInput selfdefEditor = (SelfDefCompEditorInput)input;
		widgeTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		widget = widgeTreeItem.getWidget();
		selfdefElement = new SelfDefEleObj();
		SelfDefComp selfdefcomp = (SelfDefComp)selfdefEditor.getCloneElement();
		selfdefElement.setSelfDefComp(selfdefcomp);
		selfdefElement.setLocation(new Point(100, 100));
		graph.addCell(selfdefElement);
		// 绘制Listener图形
//		Map<String, JsListenerConf> listenerMap = selfdefcomp.getListenerMap();
//		addListenerCellToEditor(listenerMap, graph);
	}

	
	public static SelfDefCompEditor getActiveEditor(){
		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if(page != null){
			editor = page.getActiveEditor();
		}
		if(editor != null && editor instanceof SelfDefCompEditor){
			return (SelfDefCompEditor)editor;
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
	
	
	
	public LFWAbstractViewPage createViewPage() {
		// TODO Auto-generated method stub
		return new LFWViewPage();
	}

	
//	public void saveJsListener(String jsListenerId, EventHandlerConf jsEventHandler, JsListenerConf listener) {
//		if(widget == null)
//			widget = LFWPersTool.getCurrentWidgetTreeItem().getWidget();
//			
//		IEditorInput input = getEditorInput();
//		SelfDefCompEditorInput selfdefEditorInput = (SelfDefCompEditorInput)input;
//		SelfDefComp selfdef =  (SelfDefComp) selfdefEditorInput.getCloneElement();
//		Map<String, WebComponent> compMap = widget.getViewComponents().getComponentsMap();
//		SelfDefComp addListenerself = null;
//		for (Iterator<String> itwd = compMap.keySet().iterator(); itwd.hasNext();) {
//			WebComponent webcomp = compMap.get(itwd.next());
//			if(webcomp instanceof SelfDefComp && webcomp.getId().equals(selfdef.getId())){
//				addListenerself = (SelfDefComp) webcomp;
//				break;
//			}
//		}
//		if(addListenerself == null)
//			addListenerself = selfdef;
////		if (null != jsEventHandler) {
////			if (addListenerself.getListenerMap().containsKey(jsListenerId)) {
////				JsListenerConf jsListener = addListenerself.getListenerMap().get(jsListenerId);
////				doSaveListenerEvent(jsListener, jsEventHandler);
////			}
////		} else {
////			addListenerself.addListener(listener);
////		}
//		setDirtyTrue();
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
		return selfdefElement.getSelfDefComp().getAcceptEventDescs();
	}

	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

	public SelfDefEleObj getSelfdefElement() {
		return selfdefElement;
	}

	public void setSelfdefElement(SelfDefEleObj selfdefElement) {
		this.selfdefElement = selfdefElement;
	}
	
}

