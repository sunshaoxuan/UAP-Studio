package nc.uap.lfw.tree.core;

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
import nc.uap.lfw.core.comp.TreeLevel;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.lang.M_tree;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.views.LFWViewPage;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;
import nc.uap.lfw.tree.TreeElementObj;
import nc.uap.lfw.tree.TreeGraph;
import nc.uap.lfw.tree.TreeTopLevelConnection;
import nc.uap.lfw.tree.treelevel.TreeLevelChildConnection;

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
 * 树编辑器
 * @author zhangxya
 *
 */
public class TreeEditor extends LFWBaseEditor {
	
	private TreeGraph graph = new TreeGraph();
	private LFWWidgetTreeItem widgeTreeItem = null;
	private LfwView widget = null;
	private TreeElementObj treeElement;
	private KeyHandler shareKeyHandler = null;

	
	public TreeGraph getGraph() {
		return graph;
	}

	public void setGraph(TreeGraph graph) {
		this.graph = graph;
	}

	public TreeEditor() {
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
		if (super.isDirty())
			return true;
		return getCommandStack().isDirty();
	}

	public void  deleteNode(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		IEditorInput input = getEditorInput();
		TreeEditorInput treeEditorInput = (TreeEditorInput)input;
		TreeViewComp tree = (TreeViewComp)treeEditorInput.getCloneElement();
		WebComponent[] webcomps = widget.getViewComponents().getComponents();
		boolean isExits = false;
		for (int i = 0; i < webcomps.length; i++) {
			WebComponent web = webcomps[i];
			if(web instanceof TreeViewComp){
				if(web.getId().equals(tree.getId())){
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
		TreeEditorInput treeEditorInput = (TreeEditorInput)input;
		if(treeEditorInput.getCurrentTreeItem().isDisposed())
			return;
		LFWSeparateTreeItem lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.COMPONENTS);		
		TreeViewComp treeviewComp = (TreeViewComp) treeEditorInput.getCloneElement();
		TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
		for (int i = 0; i < childTreeItems.length; i++) {
			LFWBasicTreeItem webT = (LFWBasicTreeItem) childTreeItems[i];
			if(webT.getData() instanceof TreeViewComp){
				TreeViewComp gr = (TreeViewComp) webT.getData();
				if(treeviewComp.getId().equals(gr.getId())){
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
	public void refreshTreeItemText(TreeViewComp tree){
		TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
		if(treeItem instanceof LFWWebComponentTreeItem){
			LFWWebComponentTreeItem treeTreeItem = (LFWWebComponentTreeItem) treeItem;
			treeTreeItem.setText(WEBPersConstants.COMPONENT_TREE + tree.getId()+ "[" + tree.getCaption() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	public static TreeEditor getActiveEditor(){
		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if(page != null){
			editor = page.getActiveEditor();

		}
		if(editor != null && editor instanceof TreeEditor){
			return (TreeEditor)editor;
		}else {
			return null;
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
			String message = M_tree.TreeEditor_2+msg;
			if(!MessageDialog.openConfirm(getSite().getShell(), M_tree.TreeEditor_3, message))
				return;
		}
		save();
	}
	
	//保存
	public boolean save(){
		IEditorInput input = getEditorInput();
		TreeEditorInput treeEditorInput = (TreeEditorInput)input;
		TreeViewComp tree =  (TreeViewComp) treeEditorInput.getCloneElement();
		TreeElementObj treeobj = (TreeElementObj) this.graph.getCells().get(0);
		TreeViewComp treenew = treeobj.getTreeComp();
		treenew.setId(tree.getId());
		treeobj.setTreeComp(treenew);
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		TreeViewComp clone = (TreeViewComp)treenew.clone();
		if(widgetTreeItem != null){
			LfwView lfwwidget = widgetTreeItem.getWidget();;
			
			if(lfwwidget != null){
			Map<String, WebComponent> treemap = lfwwidget.getViewComponents().getComponentsMap();
			boolean flag = false;
			for (Iterator<String> itwd = treemap.keySet().iterator(); itwd.hasNext();) {
				String treeId = (String) itwd.next();
				if(treemap.get(treeId) instanceof TreeViewComp){
					TreeViewComp  newtree  = (TreeViewComp)treemap.get(treeId);
					if(clone.getId().equals(newtree.getId())){
						treemap.put(clone.getId(), clone);
						flag = true;
						break;
					}
				}
			}
			if(!flag)
				treemap.put(clone.getId(),clone);
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

	
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		TreeEditorInput treeEditor = (TreeEditorInput)input;
		//LFWWebComponentTreeItem lfwWebComponentTreeItem = treeEditor.getLfwWebComponentTreeItem();
		widgeTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		widget = widgeTreeItem.getWidget();
		//Map<String, Dataset> datasetMap = widget.getViewModels().getDatasetMap();
	
		treeElement = new TreeElementObj();
		TreeViewComp treecomp = (TreeViewComp) treeEditor.getCloneElement();
		treeElement.setTreeComp(treecomp);
		treeElement.setLocation(new Point(100, 100));
		graph.addCell(treeElement);
		
		// 绘制Listener图形
//		Map<String, JsListenerConf> listenerMap = treecomp.getListenerMap();
//		addListenerCellToEditor(listenerMap, graph);
		
		TreeLevel topTreeLevel = treecomp.getTopLevel();
		TreeLevelElementObj topTreeleveEle = null;
		if(topTreeLevel != null){
			String datasetId = topTreeLevel.getDataset();
			if(datasetId != null){
				Dataset ds = widget.getViewModels().getDataset(datasetId);
				if(ds != null){
					topTreeleveEle = new TreeLevelElementObj();
					topTreeleveEle.setLocation(new Point(350,100));
					topTreeleveEle.setSize(new Dimension(100,100));
					topTreeleveEle.setDs(ds);
					topTreeleveEle.setId(topTreeLevel.getId());
					graph.addCell(topTreeleveEle);
					String contextMenuId = topTreeLevel.getContextMenu();
					if(contextMenuId != null && !contextMenuId.equals("")){ //$NON-NLS-1$
						ContextMenuComp contextMenuComp = widget.getViewMenus().getContextMenu(contextMenuId);
						ContextMenuElementObj contextMenuElement = new ContextMenuElementObj();
						contextMenuElement.setMenubar(contextMenuComp);
						contextMenuElement.setLocation(new Point(350, 300));
						contextMenuElement.setSize(new Dimension(100, 100));
						graph.addCell(contextMenuElement);
					}
				}
				if(topTreeleveEle != null){
					topTreeleveEle.setParentTreeLevel(treeElement);
					TreeTopLevelConnection conn = new TreeTopLevelConnection(treeElement, topTreeleveEle);
					conn.connect();
				}
			}
			TreeLevel childTreelevel = topTreeLevel.getChildTreeLevel();
			while(childTreelevel != null){
				TreeLevelElementObj childTreelevelElement = null;
				String childds  = childTreelevel.getDataset();
				//for (Iterator itwd = datasetMap.keySet().iterator(); itwd.hasNext();) {
					Dataset ds = widget.getViewModels().getDataset(childds);
					if(ds != null){
						childTreelevelElement = new TreeLevelElementObj();
						int size = graph.getALlTreeLevelElements().size();
						TreeLevelElementObj lastTreelleveEle = (TreeLevelElementObj)graph.getALlTreeLevelElements().get(size - 1);
						int x = lastTreelleveEle.getLocation().x + lastTreelleveEle.getSize().width + WEBPersConstants.DATASETBETWEEN;
						int y = lastTreelleveEle.getLocation().y;
						childTreelevelElement.setLocation(new Point(x, y));
						childTreelevelElement.setSize(new Dimension(100,100));
						childTreelevelElement.setDs(ds);
						childTreelevelElement.setId(childTreelevel.getId());
						graph.addCell(childTreelevelElement);
						String contextMenuId = childTreelevel.getContextMenu();
						if(contextMenuId != null && !contextMenuId.equals("")){ //$NON-NLS-1$
							ContextMenuComp contextMenuComp = widget.getViewMenus().getContextMenu(contextMenuId);
							ContextMenuElementObj contextMenuElement = new ContextMenuElementObj();
							contextMenuElement.setMenubar(contextMenuComp);
							contextMenuElement.setLocation(new Point(x, y + 200));
							contextMenuElement.setSize(new Dimension(100, 100));
							graph.addCell(contextMenuElement);
						}
						
						childTreelevelElement.setParentTreeLevel(topTreeleveEle);
						TreeLevelChildConnection childcon = new TreeLevelChildConnection(topTreeleveEle, childTreelevelElement);
						childcon.connect();
						//break;
					}
				//}
				topTreeleveEle = childTreelevelElement;
				childTreelevel = childTreelevel.getChildTreeLevel();
			}
		}
		
	}

	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return true;
	}
	
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
			paleteRoot = PaletteFactory.createTreePalette();
		}
		return paleteRoot;
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
		return treeElement.getTreeComp().getAcceptEventDescs();
	}

	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

	public TreeElementObj getTreeElement() {
		return treeElement;
	}

	public void setTreeElement(TreeElementObj treeElement) {
		this.treeElement = treeElement;
	}
	
}

