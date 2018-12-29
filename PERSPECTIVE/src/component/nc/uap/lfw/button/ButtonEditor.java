package nc.uap.lfw.button;

import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.lang.M_button;
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

/**
 * button 编辑器
 * @author zhangxya
 *
 */
public class ButtonEditor extends LFWBaseEditor {
	private ButtonGraph graph = new ButtonGraph();
	public ButtonGraph getGraph() {
		return graph;
	}

	public void setGraph(ButtonGraph graph) {
		this.graph = graph;
	}

	public ButtonEditor() {
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
		ButtonEditorInput buttonEditorInput = (ButtonEditorInput)input;
		ButtonComp button = (ButtonComp)buttonEditorInput.getCloneElement();
		widget = LFWPersTool.getCurrentWidget();
		WebComponent[] webcomps = widget.getViewComponents().getComponents();
		boolean isExits = false;
		for (int i = 0; i < webcomps.length; i++) {
			WebComponent web = webcomps[i];
			if(web instanceof ButtonComp){
				if(web.getId().equals(button.getId())){
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
		ButtonEditorInput buttonEditorInput = (ButtonEditorInput)input;
		if(buttonEditorInput.getCurrentTreeItem().isDisposed())
			return;
		LFWSeparateTreeItem lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.COMPONENTS);		
		ButtonComp buttonComp = (ButtonComp) buttonEditorInput.getCloneElement();
		TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
		for (int i = 0; i < childTreeItems.length; i++) {
			LFWBasicTreeItem webT = (LFWBasicTreeItem) childTreeItems[i];
			if(webT.getData() instanceof ButtonComp){
				ButtonComp gr = (ButtonComp) webT.getData();
				if(buttonComp.getId().equals(gr.getId())){
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
			String message = M_button.ButtonEditor_0+msg;
			if(!MessageDialog.openConfirm(getSite().getShell(), M_button.ButtonEditor_1, message))
				return;
		}
		save();
	}
	
	
	//保存
	public boolean save(){
		IEditorInput input = getEditorInput();
		ButtonEditorInput buttonEditorInput = (ButtonEditorInput)input;
		ButtonComp button = (ButtonComp) buttonEditorInput.getCloneElement();
		ButtonElementObj buttonobj = (ButtonElementObj) this.graph.getCells().get(0);
		ButtonComp buttonnew = buttonobj.getButtonComp();
		buttonnew.setId(button.getId());
		ButtonComp clone = (ButtonComp)buttonnew.clone();
		if(widgeTreeItem != null){
			LfwView lfwwidget = widgeTreeItem.getWidget();
			if(lfwwidget != null){
			Map<String, WebComponent> map = lfwwidget.getViewComponents().getComponentsMap();
			boolean flag = false;
			for (Iterator<String> itwd = map.keySet().iterator(); itwd.hasNext();) {
				String buttonId = (String) itwd.next();
				if(map.get(buttonId) instanceof ButtonComp){
					ButtonComp  newbutton  = (ButtonComp)map.get(buttonId);
					if(clone.getId().equals(newbutton.getId())){
						map.put(clone.getId(), clone);
						flag = true;
						break;
					}
				}
			}
			if(!flag)
				map.put(clone.getId(), clone);
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
			//更新左边的树节点
			refreshTreeItem(clone);
			return true;
		}
		return false;
	}

	
	private LFWWidgetTreeItem widgeTreeItem = null;
	private LfwView widget = null;
	private ButtonElementObj buttonElement;
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		ButtonEditorInput buttonEditor = (ButtonEditorInput)input;
		widgeTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		buttonElement = new ButtonElementObj();
		ButtonComp buttoncomp = (ButtonComp)buttonEditor.getCloneElement();
		buttonElement.setButtonComp(buttoncomp);
		buttonElement.setLocation(new Point(100, 100));
		graph.addCell(buttonElement);
		// 绘制Listener图形
//		Map<String, JsListenerConf> listenerMap = buttoncomp.getListenerMap();
//		addListenerCellToEditor(listenerMap, graph);
	}

	
	public static ButtonEditor getActiveEditor(){
//		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
//		if(page != null){
//			editor = page.getActiveEditor();
//		}
		editor = LFWBaseEditor.getActiveEditor();
		
		if(editor != null && editor instanceof ButtonEditor){
			return (ButtonEditor)editor;
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
//		ButtonEditorInput buttonEditorInput = (ButtonEditorInput)input;
//		ButtonComp button =  (ButtonComp) buttonEditorInput.getCloneElement();
//		Map<String, WebComponent> compMap = widget.getViewComponents().getComponentsMap();
//		ButtonComp addListenerbutton = null;
//		for (Iterator<String> itwd = compMap.keySet().iterator(); itwd.hasNext();) {
//			WebComponent webcomp = compMap.get(itwd.next());
//			if(webcomp instanceof ButtonComp && webcomp.getId().equals(button.getId())){
//				addListenerbutton = (ButtonComp) webcomp;
//				break;
//			}
//		}
//		if(addListenerbutton == null)
//			addListenerbutton = button;
////		if (null != jsEventHandler) {
////			if (addListenerbutton.getListenerMap().containsKey(jsListenerId)) {
////				JsListenerConf jsListener = addListenerbutton.getListenerMap().get(jsListenerId);
////				doSaveListenerEvent(jsListener, jsEventHandler);
////			}
////		} else {
////			addListenerbutton.addListener(listener);
////		}
//		setDirtyTrue();
//	}
	
	
	
	protected void editMenuManager(IMenuManager manager) {
		// TODO Auto-generated method stub
		
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
		return buttonElement.getButtonComp().getAcceptEventDescs();
	}

	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

	public ButtonElementObj getButtonElement() {
		return buttonElement;
	}

	public void setButtonElement(ButtonElementObj buttonElement) {
		this.buttonElement = buttonElement;
	}
	
}

