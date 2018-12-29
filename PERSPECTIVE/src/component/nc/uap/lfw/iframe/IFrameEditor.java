package nc.uap.lfw.iframe;

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
import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.lang.M_iframe;
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
 * Iframe 编辑器
 * @author zhangxya
 *
 */
public class IFrameEditor extends LFWBaseEditor {
	public IFrameGraph getGraph() {
		return graph;
	}

	public void setGraph(IFrameGraph graph) {
		this.graph = graph;
	}


	private IFrameGraph graph = new IFrameGraph();
	public IFrameEditor() {
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
		IFrameEditorInput iFrameEditorInput = (IFrameEditorInput)input;
		IFrameComp iFrame = (IFrameComp)iFrameEditorInput.getCloneElement();
		widget = widgeTreeItem.getWidget();
		WebComponent[] webcomps = widget.getViewComponents().getComponents();
		boolean isExits = false;
		for (int i = 0; i < webcomps.length; i++) {
			WebComponent web = webcomps[i];
			if(web instanceof IFrameComp){
				if(web.getId().equals(iFrame.getId())){
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
		IFrameEditorInput iframeEditorInput = (IFrameEditorInput)input;
		if(iframeEditorInput.getCurrentTreeItem().isDisposed())
			return;
		LFWSeparateTreeItem lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.COMPONENTS);		
		IFrameComp iFraemComp = (IFrameComp) iframeEditorInput.getCloneElement();
		TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
		for (int i = 0; i < childTreeItems.length; i++) {
			LFWBasicTreeItem webT = (LFWBasicTreeItem) childTreeItems[i];
			if(webT.getData() instanceof IFrameComp){
				IFrameComp gr = (IFrameComp) webT.getData();
				if(iFraemComp.getId().equals(gr.getId())){
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
		//TODO 其他保存操作
		super.doSave(monitor);
		String msg = graph.getCells().get(0).validate();
		if(msg != null){
			String message = M_iframe.IFrameEditor_0+msg;
			if(!MessageDialog.openConfirm(getSite().getShell(), M_iframe.IFrameEditor_1, message))
				return;
		}
		save();
	}

	//保存
	public boolean save(){
		IEditorInput input = getEditorInput();
		IFrameEditorInput iframeEditorInput = (IFrameEditorInput)input;
		IFrameComp iframe = (IFrameComp) iframeEditorInput.getCloneElement();
	//	LFWWebComponentTreeItem iframeItem = iframeEditorInput.getLfwWebComponentTreeItem();
		IFrameElementObj iframeobj = (IFrameElementObj) this.graph.getCells().get(0);
		//dsobj.setDs(ds);
		IFrameComp iframenew = iframeobj.getIframecomp();
		//iframeItem.setData(iframenew);
		//iframeEditorInput.setIFrameComp(iframenew);
		iframenew.setId(iframe.getId());
		IFrameComp clone = (IFrameComp)iframenew.clone();
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		//LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(widgetTreeItem != null){
			LfwView lfwwidget = widgetTreeItem.getWidget();
			if(lfwwidget != null){
			Map<String, WebComponent> map = lfwwidget.getViewComponents().getComponentsMap();
			boolean flag = false;
			for (Iterator<String> itwd = map.keySet().iterator(); itwd.hasNext();) {
				String iframeid = (String) itwd.next();
				if(map.get(iframeid) instanceof IFrameComp){
					IFrameComp newiframe  = (IFrameComp)map.get(iframeid);
					if(clone.getId().equals(newiframe.getId())){
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
	private IFrameElementObj iframeElement;
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		IFrameEditorInput iframeEditor = (IFrameEditorInput)input;
		//LFWWebComponentTreeItem lfwWebComponentTreeItem = iframeEditor..getLfwWebComponentTreeItem();
		widgeTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		iframeElement = new IFrameElementObj();
		IFrameComp iframecomp = (IFrameComp)iframeEditor.getCloneElement();
		iframeElement.setIframecomp(iframecomp);
		iframeElement.setLocation(new Point(100, 100));
		graph.addCell(iframeElement);
		
		// 绘制Listener图形
//		Map<String, JsListenerConf> listenerMap = iframecomp.getListenerMap();
//		addListenerCellToEditor(listenerMap, graph);
	}

	
	public static IFrameEditor getActiveEditor(){
		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if(page != null){
			editor = page.getActiveEditor();
		}
		if(editor != null && editor instanceof IFrameEditor){
			return (IFrameEditor)editor;
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
//		if(widget != null){
//			IEditorInput input = getEditorInput();
//			IFrameEditorInput iframeEditorInput = (IFrameEditorInput)input;
//			IFrameComp iframe =  (IFrameComp) iframeEditorInput.getCloneElement();
//			Map<String, WebComponent> compdMap = widget.getViewComponents().getComponentsMap();
//			IFrameComp addListeneriframe = null;
//			for (Iterator<String> itwd = compdMap.keySet().iterator(); itwd.hasNext();) {
//				WebComponent webcomp = compdMap.get(itwd.next());
//				if(webcomp instanceof IFrameComp && webcomp.getId().equals(iframe.getId())){
//					addListeneriframe = (IFrameComp) webcomp;
//					break;
//				}
//			}
//			if(addListeneriframe == null)
//				addListeneriframe = iframe;
////			if (null != jsEventHandler) {
////				if (addListeneriframe.getListenerMap().containsKey(jsListenerId)) {
////					JsListenerConf jsListener = addListeneriframe.getListenerMap().get(jsListenerId);
////					doSaveListenerEvent(jsListener, jsEventHandler);
////				}
////			} else {
////				addListeneriframe.addListener(listener);
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
		return iframeElement.getIframecomp().getAcceptEventDescs();
	}

	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

	public IFrameElementObj getIframeElement() {
		return iframeElement;
	}

	public void setIframeElement(IFrameElementObj iframeElement) {
		this.iframeElement = iframeElement;
	}
	
}

