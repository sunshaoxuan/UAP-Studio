package nc.uap.lfw.form.core;

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
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.form.DatasetToFormConnection;
import nc.uap.lfw.form.FormElementObj;
import nc.uap.lfw.form.FormGraph;
import nc.uap.lfw.form.FormViewPage;
import nc.uap.lfw.lang.M_form;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
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

/**
 * form编辑器
 * @author zhangxya
 *
 */
public class FormEditor extends LFWBaseEditor {
	private FormGraph graph = new FormGraph();
	public FormGraph getGraph() {
		return graph;
	}

	public void setGraph(FormGraph graph) {
		this.graph = graph;
	}

	public FormEditor() {
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
			String message = M_form.FormEditor_0+msg;
			if(!MessageDialog.openConfirm(getSite().getShell(), M_form.FormEditor_1, message))
				return;
		}
		save();
	}
	
	
	public void  deleteNode(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		IEditorInput input = getEditorInput();
		FormEditorInput formEditorInput = (FormEditorInput)input;
		FormComp form = (FormComp)formEditorInput.getCloneElement();
		widget = LFWPersTool.getCurrentWidget();
		WebComponent[] webcomps = widget.getViewComponents().getComponents();
		boolean isExits = false;
		for (int i = 0; i < webcomps.length; i++) {
			WebComponent web = webcomps[i];
			if(web instanceof FormComp){
				if(web.getId().equals(form.getId())){
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
		FormEditorInput formEditorInput = (FormEditorInput)input;
		if(formEditorInput.getCurrentTreeItem().isDisposed())
			return;
		LFWSeparateTreeItem lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.COMPONENTS);		
		FormComp formcomp = (FormComp) formEditorInput.getCloneElement();
		TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
		for (int i = 0; i < childTreeItems.length; i++) {
			LFWBasicTreeItem webT = (LFWBasicTreeItem) childTreeItems[i];
			if(webT.getData() instanceof FormComp){
				FormComp gr = (FormComp) webT.getData();
				if(formcomp.getId().equals(gr.getId())){
					tree.setSelection(webT);
					break;
				}
			}
		}

	}

	
	public void refreshTreeItemText(FormComp form){
		TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
		if(treeItem instanceof LFWWebComponentTreeItem){
			LFWWebComponentTreeItem formTreeItem = (LFWWebComponentTreeItem) treeItem;
			formTreeItem.setText(WEBPersConstants.COMPONENT_FORM + form.getId()+ "[" + form.getCaption() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	//保存
	public boolean save(){
		IEditorInput input = getEditorInput();
		FormEditorInput formEditorInput = (FormEditorInput)input;
		FormComp form = (FormComp) formEditorInput.getCloneElement();
		//LFWWebComponentTreeItem formtreeItem = formEditorInput.getLfwWebComponentTreeItem();
		FormElementObj formobj = (FormElementObj) this.graph.getCells().get(0);
		//dsobj.setDs(ds);
		FormComp formnew = formobj.getFormComp();
		//formtreeItem.setData(formnew);
		//formEditorInput.setLfwWebComponentTreeItem(formtreeItem);
		formnew.setId(form.getId());
		if(formnew.getFrom() != null)
			formnew.setConfType(WebElement.CONF_REF);
		if(graph.getContextMenu() != null && graph.getContextMenu().size() > 0){
			ContextMenuComp contextMenuComp = null;
			ContextMenuElementObj contextMenuEle = graph.getContextMenu().get(0);
			if(contextMenuEle != null)
				contextMenuComp = contextMenuEle.getMenubar();
			if(contextMenuComp != null)
				formnew.setContextMenu(contextMenuComp.getId());
		}
		FormComp clone = (FormComp)formnew.clone();
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		if(widgetTreeItem != null){
			LfwView lfwwidget = widgetTreeItem.getWidget();
			
			if(lfwwidget != null){
			Map<String, WebComponent> formmap = lfwwidget.getViewComponents().getComponentsMap();
			boolean flag = false;
			for (Iterator<String> itwd = formmap.keySet().iterator(); itwd.hasNext();) {
				String formId = (String) itwd.next();
				if(formmap.get(formId) instanceof FormComp){
					FormComp  newform  = (FormComp)formmap.get(formId);
					if(clone.getId().equals(newform.getId())){
						formmap.put(clone.getId(), clone);
						flag = true;
						break;
					}
				}
			}
			if(!flag)
				formmap.put(clone.getId(),clone);
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
			refreshTreeItem(clone);
			return true;
		}
		return false;
	}

	
	private LFWWidgetTreeItem widgeTreeItem = null;
	private LfwView widget = null;
	private FormElementObj formElement;
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		FormEditorInput formEditor = (FormEditorInput)input;
		//LFWWebComponentTreeItem lfwWebComponentTreeItem = formEditor.getLfwWebComponentTreeItem();
		widgeTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		widget = widgeTreeItem.getWidget();
		formElement = new FormElementObj();
		FormComp formcomp = (FormComp) formEditor.getCloneElement();
		formElement.setFormComp(formcomp);
		formElement.setLocation(new Point(100, 100));
		graph.addCell(formElement);
		// 绘制Listener图形
//		Map<String, JsListenerConf> listenerMap = formcomp.getListenerMap();
//		addListenerCellToEditor(listenerMap, graph);
		
		String dsId = formcomp.getDataset();
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
				DatasetToFormConnection conn = new DatasetToFormConnection(formElement, dsobj);
				conn.connect();
			}
		}
		//contextMenu
		String contextMenuId = formcomp.getContextMenu();
		if(contextMenuId != null && !contextMenuId.equals("")){ //$NON-NLS-1$
			ContextMenuComp contextMenuComp = widget.getViewMenus().getContextMenu(contextMenuId);
			ContextMenuElementObj contextMenuElement = new ContextMenuElementObj();
			contextMenuElement.setMenubar(contextMenuComp);
			contextMenuElement.setLocation(new Point(400, 300));
			contextMenuElement.setSize(new Dimension(150, 150));
			graph.addCell(contextMenuElement);
		}
	}

	public static FormEditor getActiveEditor(){
//		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
//		if(page != null){
//			editor = page.getActiveEditor();
//
//		}
		editor = LFWBaseEditor.getActiveEditor();
		if(editor != null && editor instanceof FormEditor){
			return (FormEditor)editor;
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
			paleteRoot = PaletteFactory.createFormPalette();
		}
		return paleteRoot;
	}

	@Override
	public LFWAbstractViewPage createViewPage() {
		return new FormViewPage();
	}

//	@Override
//	public void saveJsListener(String jsListenerId, EventHandlerConf jsEventHandler, JsListenerConf listener) {
//		if(widget == null)
//			widget = LFWPersTool.getCurrentWidgetTreeItem().getWidget();
//		if(widget != null){
//			IEditorInput input = getEditorInput();
//			FormEditorInput formEditorInput = (FormEditorInput)input;
//			FormComp form =  (FormComp) formEditorInput.getCloneElement();
//			Map<String, WebComponent> compMap = widget.getViewComponents().getComponentsMap();
//			FormComp  addListenerform = null;
//			for (Iterator<String> itwd = compMap.keySet().iterator(); itwd.hasNext();) {
//				WebComponent webcomp = compMap.get(itwd.next());
//				if(webcomp instanceof FormComp && webcomp.getId().equals(form.getId())){
//					addListenerform = (FormComp) webcomp;
//					break;
//				}
//			}
//			if(addListenerform == null)
//				addListenerform = form;
////			if (null != jsEventHandler) {
////				if (addListenerform.getListenerMap().containsKey(jsListenerId)) {
////					JsListenerConf jsListener = addListenerform.getListenerMap().get(jsListenerId);
////					doSaveListenerEvent(jsListener, jsEventHandler);
////				}
////			} else {
////				addListenerform.addListener(listener);
////			}
//			setDirtyTrue();
//		}
//	}

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
		return formElement.getFormComp().getAcceptEventDescs();
	}

	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

	public FormElementObj getFormElement() {
		return formElement;
	}

	public void setFormElement(FormElementObj formElement) {
		this.formElement = formElement;
	}
	
}

