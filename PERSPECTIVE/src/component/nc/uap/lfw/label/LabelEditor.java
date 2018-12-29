package nc.uap.lfw.label;

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
import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.lang.M_label;
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
 * Label Editor
 * @author zhangxya
 *
 */
public class LabelEditor extends LFWBaseEditor {
	public LabelGraph getGraph() {
		return graph;
	}

	public void setGraph(LabelGraph graph) {
		this.graph = graph;
	}


	private LabelGraph graph = new LabelGraph();
	

	public LabelEditor() {
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
		LabelEditorInput labelEditorInput = (LabelEditorInput)input;
		LabelComp label = (LabelComp)labelEditorInput.getCloneElement();
		widget = LFWPersTool.getCurrentWidget();
		WebComponent[] webcomps = widget.getViewComponents().getComponents();
		boolean isExits = false;
		for (int i = 0; i < webcomps.length; i++) {
			WebComponent web = webcomps[i];
			if(web instanceof LabelComp){
				if(web.getId().equals(label.getId())){
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
		LabelEditorInput labelEditorInput = (LabelEditorInput)input;
		if(labelEditorInput.getCurrentTreeItem().isDisposed())
			return;
		LFWSeparateTreeItem lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.COMPONENTS);		
		LabelComp labelComp = (LabelComp) labelEditorInput.getCloneElement();
		TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
		for (int i = 0; i < childTreeItems.length; i++) {
			LFWBasicTreeItem webT = (LFWBasicTreeItem) childTreeItems[i];
			if(webT.getData() instanceof LabelComp){
				LabelComp lab = (LabelComp) webT.getData();
				if(labelComp.getId().equals(lab.getId())){
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
			String message = M_label.LabelEditor_0+msg;
			if(!MessageDialog.openConfirm(getSite().getShell(), M_label.LabelEditor_1, message))
				return;
		}
		save();
	}
	
	//保存
	public boolean save(){
		IEditorInput input = getEditorInput();
		LabelEditorInput labelEditorInput = (LabelEditorInput)input;
		LabelComp label = (LabelComp) labelEditorInput.getCloneElement();
		//LFWWebComponentTreeItem buttontreeItem = buttonEditorInput.getLfwWebComponentTreeItem();
		LabelElementObj labelobj = (LabelElementObj) this.graph.getCells().get(0);
		//dsobj.setDs(ds);
		LabelComp labelnew = labelobj.getLabelComp();
		//buttontreeItem.setData(buttonnew);
		//buttonEditorInput.setButton(buttonnew);
		labelnew.setId(label.getId());
		LabelComp clone = (LabelComp)labelnew.clone();
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		//LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(widgetTreeItem != null){
			LfwView lfwwidget = widgetTreeItem.getWidget();
			if(lfwwidget != null){
			Map<String, WebComponent> map = lfwwidget.getViewComponents().getComponentsMap();
			boolean flag = false;
			for (Iterator<String> itwd = map.keySet().iterator(); itwd.hasNext();) {
				String buttonId = (String) itwd.next();
				if(map.get(buttonId) instanceof LabelComp){
					LabelComp  newlabel  = (LabelComp)map.get(buttonId);
					if(clone.getId().equals(newlabel.getId())){
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
	private LabelElementObj labelElement;
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		LabelEditorInput labelEditor = (LabelEditorInput)input;
		//LFWWebComponentTreeItem lfwWebComponentTreeItem = buttonEditor.getLfwWebComponentTreeItem();
		widgeTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		widget = widgeTreeItem.getWidget();
		labelElement = new LabelElementObj();
		LabelComp labelcomp = (LabelComp)labelEditor.getCloneElement();
		labelElement.setLabelComp(labelcomp);
		labelElement.setLocation(new Point(100, 100));
		graph.addCell(labelElement);
		// 绘制Listener图形
//		Map<String, JsListenerConf> listenerMap = labelcomp.getListenerMap();
//		addListenerCellToEditor(listenerMap, graph);
	}

	
	public static LabelEditor getActiveEditor(){
		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if(page != null){
			editor = page.getActiveEditor();
		}
		if(editor != null && editor instanceof LabelEditor){
			return (LabelEditor)editor;
		}else {
			return null;
		}
		
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
		return labelElement.getLabelComp().getAcceptEventDescs();
	}

	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

	public LabelElementObj getLabelElement() {
		return labelElement;
	}

	public void setLabelElement(LabelElementObj labelElement) {
		this.labelElement = labelElement;
	}
	
}

