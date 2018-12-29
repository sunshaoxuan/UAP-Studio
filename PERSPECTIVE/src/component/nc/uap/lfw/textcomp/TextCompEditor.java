package nc.uap.lfw.textcomp;

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
import nc.uap.lfw.core.comp.CheckBoxComp;
import nc.uap.lfw.core.comp.CheckboxGroupComp;
import nc.uap.lfw.core.comp.RadioComp;
import nc.uap.lfw.core.comp.RadioGroupComp;
import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.comp.SelfDefElementComp;
import nc.uap.lfw.core.comp.TextAreaComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.lang.M_textcomp;
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
 * 文本框编辑器
 * @author zhangxya
 *
 */
public class TextCompEditor extends LFWBaseEditor {
	public TextCompGraph getGraph() {
		return graph;
	}

	public void setGraph(TextCompGraph graph) {
		this.graph = graph;
	}


	private TextCompGraph graph = new TextCompGraph();
	public TextCompEditor() {
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
		TextCompEditorInput textEditorInput = (TextCompEditorInput)input;
		TextComp text = (TextComp)textEditorInput.getCloneElement();
		widget = (LfwView) widgeTreeItem.getWidget();
		WebComponent[] webcomps = widget.getViewComponents().getComponents();
		boolean isExits = false;
		for (int i = 0; i < webcomps.length; i++) {
			WebComponent web = webcomps[i];
			if(web instanceof TextComp){
				if(web.getId().equals(text.getId())){
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
		TextCompEditorInput textEditorInput = (TextCompEditorInput)input;
		if(textEditorInput.getCurrentTreeItem().isDisposed())
			return;
		LFWSeparateTreeItem lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.COMPONENTS);		
		TextComp textComp = (TextComp) textEditorInput.getCloneElement();
		TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
		for (int i = 0; i < childTreeItems.length; i++) {
			LFWBasicTreeItem webT = (LFWBasicTreeItem) childTreeItems[i];
			if(webT.getData() instanceof TextComp){
				TextComp gr = (TextComp) webT.getData();
				if(textComp.getId().equals(gr.getId())){
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
			String message = M_textcomp.TextCompEditor_0+msg;
			if(!MessageDialog.openConfirm(getSite().getShell(), M_textcomp.TextCompEditor_1, message))
				return;
		}
		save();
	}
	

	//保存
	public boolean save(){
		IEditorInput input = getEditorInput();
		TextCompEditorInput textcompEditorInput = (TextCompEditorInput)input;
		TextComp textcomp = (TextComp) textcompEditorInput.getCloneElement();
		//LFWWebComponentTreeItem textreeItem = textcompEditorInput.getLfwWebComponentTreeItem();
		TextCompElementObj textobj = (TextCompElementObj) this.graph.getCells().get(0);
		//dsobj.setDs(ds);
		TextComp textnew = textobj.getTextComp();
		//textreeItem.setData(textnew);
		//textcompEditorInput.setTextcomp(textnew);
		textnew.setId(textcomp.getId());
//		textnew.setListenerMap(textcomp.getListenerMap());
		TextComp clone = (TextComp)textnew.clone();
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		//LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(widgetTreeItem != null){
			LfwView lfwwidget = widgetTreeItem.getWidget();
			if(lfwwidget != null){
			Map<String, WebComponent> map = lfwwidget.getViewComponents().getComponentsMap();
			boolean flag = false;
			for (Iterator<String> itwd = map.keySet().iterator(); itwd.hasNext();) {
				String textId = (String) itwd.next();
				if(map.get(textId) instanceof TextComp){
					TextComp  newtext  = (TextComp)map.get(textId);
					if(clone.getId().equals(newtext.getId())){
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
	private TextCompElementObj textElement;
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		TextCompEditorInput textCompEditor = (TextCompEditorInput)input;
		//LFWWebComponentTreeItem lfwWebComponentTreeItem = textCompEditor.getLfwWebComponentTreeItem();
		widgeTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		widget = widgeTreeItem.getWidget();
		textElement = new TextCompElementObj();
		TextComp textcomp = (TextComp)textCompEditor.getCloneElement();
		textElement.setTextComp(textcomp);
		textElement.setLocation(new Point(100, 100));
		graph.addCell(textElement);
		
		// 绘制Listener图形
//		Map<String, JsListenerConf> listenerMap = textcomp.getListenerMap();
//		addListenerCellToEditor(listenerMap, graph);
	}

	
	public static TextCompEditor getActiveEditor(){
		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if(page != null){
			editor = page.getActiveEditor();

		}
		if(editor != null && editor instanceof TextCompEditor){
			return (TextCompEditor)editor;
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
		if(textElement.getTextComp() instanceof CheckBoxComp){
			return ((CheckBoxComp)textElement.getTextComp()).getAcceptEventDescs();
		}else if(textElement.getTextComp() instanceof CheckboxGroupComp){
			return ((CheckboxGroupComp)textElement.getTextComp()).getAcceptEventDescs();
		}else if(textElement.getTextComp() instanceof ComboBoxComp){
			return ((ComboBoxComp)textElement.getTextComp()).getAcceptEventDescs();
		}else if(textElement.getTextComp() instanceof RadioComp){
			return ((RadioComp)textElement.getTextComp()).getAcceptEventDescs();
		}else if(textElement.getTextComp() instanceof RadioGroupComp){
			return ((RadioGroupComp)textElement.getTextComp()).getAcceptEventDescs();
		}else if(textElement.getTextComp() instanceof ReferenceComp){
			return ((ReferenceComp)textElement.getTextComp()).getAcceptEventDescs();
		}else if(textElement.getTextComp() instanceof SelfDefElementComp){
			return ((SelfDefElementComp)textElement.getTextComp()).getAcceptEventDescs();
		}else if(textElement.getTextComp() instanceof TextAreaComp){
			return ((TextAreaComp)textElement.getTextComp()).getAcceptEventDescs();
		}else{
			return textElement.getTextComp().getAcceptEventDescs();
		}
	}

	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

	public TextCompElementObj getTextElement() {
		return textElement;
	}

	public void setTextElement(TextCompElementObj textElement) {
		this.textElement = textElement;
	}
	
}

