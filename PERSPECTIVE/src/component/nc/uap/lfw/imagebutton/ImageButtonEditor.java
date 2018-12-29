package nc.uap.lfw.imagebutton;

import java.util.EventObject;
import java.util.Iterator;
import java.util.Map;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.comp.ImageButtonComp;
import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.image.ImageEditor;
import nc.uap.lfw.image.ImageEditorInput;
import nc.uap.lfw.image.ImageElementObj;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.views.LFWViewPage;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;
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

public class ImageButtonEditor extends LFWBaseEditor {


	private ImageButtonGraph graph = new ImageButtonGraph();
	

	public ImageButtonGraph getGraph() {
		return graph;
	}


	public void setGraph(ImageButtonGraph graph) {
		this.graph = graph;
	}


	public ImageButtonEditor() {
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

	
	public void deleteNode(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		IEditorInput input = getEditorInput();
		ImageButtonEditorInput imageButtonEditorInput = (ImageButtonEditorInput)input;
		ImageButtonComp imageButton = (ImageButtonComp)imageButtonEditorInput.getCloneElement();
		widget = LFWPersTool.getCurrentWidget();
		WebComponent[] webcomps = widget.getViewComponents().getComponents();
		boolean isExits = false;
		for (int i = 0; i < webcomps.length; i++) {
			WebComponent web = webcomps[i];
			if(web instanceof ImageButtonComp){
				if(web.getId().equals(imageButton.getId())){
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
		ImageButtonEditorInput imageButtonEdiInput = (ImageButtonEditorInput)input;
		if(imageButtonEdiInput.getCurrentTreeItem().isDisposed())
			return;
		LFWSeparateTreeItem lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.COMPONENTS);		
		ImageButtonComp imageButton = (ImageButtonComp) imageButtonEdiInput.getCloneElement();
		TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
		for (int i = 0; i < childTreeItems.length; i++) {
			LFWWebComponentTreeItem webT = (LFWWebComponentTreeItem) childTreeItems[i];
			if(webT.getData() instanceof ImageButtonComp){
				ImageComp lab = (ImageComp) webT.getData();
				if(imageButton.getId().equals(lab.getId())){
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
			String message = "图片按钮输入信息有错误，是否还要保存：\r\n\r\n\r\n"+msg;
			if(!MessageDialog.openConfirm(getSite().getShell(), "提示", message))
				return;
		}
		save();
	}

	//保存
	public boolean save(){
		IEditorInput input = getEditorInput();
		ImageButtonEditorInput imageEditorInput = (ImageButtonEditorInput)input;
		ImageComp image = (ImageComp) imageEditorInput.getCloneElement();
		ImageElementObj imageobj = (ImageElementObj) this.graph.getCells().get(0);
		ImageComp imagenew = imageobj.getImageComp();
		imagenew.setId(image.getId());
		imageobj.setImageComp(imagenew);
	
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		if(widgetTreeItem != null){
			LfwView lfwwidget = widgetTreeItem.getWidget();
			if(lfwwidget != null){
			Map<String, WebComponent> map = lfwwidget.getViewComponents().getComponentsMap();
			boolean flag = false;
			for (Iterator<String> itwd = map.keySet().iterator(); itwd.hasNext();) {
				String buttonId = (String) itwd.next();
				if(map.get(buttonId) instanceof ImageComp){
					ImageComp newimage  = (ImageComp)map.get(buttonId);
					if(image.getId().equals(newimage.getId())){
						map.put(image.getId(), imagenew);
						flag = true;
						break;
					}
				}
			}
			if(!flag)
				map.put(imagenew.getId(), imagenew);
			}
			LFWPersTool.saveWidget(lfwwidget);
			getCommandStack().markSaveLocation();
			//更新左边的树节点
			refreshTreeItem(imagenew);
			return true;
		}
		return false;
	}

	
	private LfwView widget = null;
	private ImageElementObj imageElement;
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		ImageEditorInput imageEditor = (ImageEditorInput)input;
		//LFWWebComponentTreeItem lfwWebComponentTreeItem = buttonEditor.getLfwWebComponentTreeItem();
		//widgeTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		imageElement = new ImageElementObj();
		ImageComp imagecomp = (ImageComp)imageEditor.getCloneElement();
		imageElement.setImageComp(imagecomp);
		imageElement.setLocation(new Point(100, 100));
		graph.addCell(imageElement);
		// 绘制Listener图形
//		Map<String, JsListenerConf> listenerMap = imagecomp.getListenerMap();
//		addListenerCellToEditor(listenerMap, graph);
	}

	
	public static ImageEditor getActiveEditor(){
		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if(page != null){
			editor = page.getActiveEditor();
		}
		if(editor != null && editor instanceof ImageEditor){
			return (ImageEditor)editor;
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
//			ImageEditorInput labelEditorInput = (ImageEditorInput)input;
//			ImageComp label =  (ImageComp) labelEditorInput.getCloneElement();
//			Map<String, WebComponent> compMap = widget.getViewComponents().getComponentsMap();
//			ImageComp addListenerlabel = null;
//			for (Iterator<String> itwd = compMap.keySet().iterator(); itwd.hasNext();) {
//				WebComponent webcomp = compMap.get(itwd.next());
//				if(webcomp instanceof ImageComp && webcomp.getId().equals(label.getId())){
//					addListenerlabel = (ImageComp) webcomp;
//					break;
//				}
//			}
//			if(addListenerlabel == null)
//				addListenerlabel = label;
////			if (null != jsEventHandler) {
////				if (addListenerlabel.getListenerMap().containsKey(jsListenerId)) {
////					JsListenerConf jsListener = addListenerlabel.getListenerMap().get(jsListenerId);
////					doSaveListenerEvent(jsListener, jsEventHandler);
////				}
////			} else {
////				addListenerlabel.addListener(listener);
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
	
}

