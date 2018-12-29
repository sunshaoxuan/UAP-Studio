package nc.uap.lfw.refnode.core;

import java.util.EventObject;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.combodata.core.ComboDataEditor;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.refnode.BaseRefNode;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.lang.M_refnode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWBusinessCompnentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWProjectTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWRefNodeTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;
import nc.uap.lfw.refnode.RefNodeElementObj;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
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
 * 参照编辑器 
 * @author zhangxya
 *
 */
public class RefNodeEditor extends LFWBaseEditor {
	private RefNodeGraph graph = new RefNodeGraph();
	public RefNodeEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
		//getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
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

	public void setFocus() {
		super.setFocus();
		Tree tree = LFWPersTool.getTree();
		IEditorInput input = getEditorInput();
		RefNodeEditorInput refEditorInput = (RefNodeEditorInput)input;
		if(refEditorInput.getCurrentTreeItem().isDisposed())
			return;
		BaseRefNode refnode = (BaseRefNode) refEditorInput.getCloneElement();
		LFWWidgetTreeItem widgetTreeItem = getWidgetTreeItem();
		if(widgetTreeItem  != null){
			LFWSeparateTreeItem lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.REFNODE);
			//widget中参照
			TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
			for (int i = 0; i < childTreeItems.length; i++) {
				if(!(childTreeItems[i] instanceof LFWRefNodeTreeItem))
					continue;
				LFWRefNodeTreeItem webT = (LFWRefNodeTreeItem) childTreeItems[i];
				if(webT.getData() instanceof IRefNode){
					IRefNode gr = (IRefNode) webT.getData();
					if(refnode.getId().equals(gr.getId())){
						tree.setSelection(webT);
						break;
					}
				}
			}
		}
		
		else{
			//公共参照中refnode
			TreeItem[] treeItems = tree.getItems();
			IProject project = LFWPersTool.getCurrentProject();
			LFWProjectTreeItem projectTreeItem = null;
			LFWDirtoryTreeItem pubRefNode = null;
			for (int i = 0; i < treeItems.length; i++) {
				LFWProjectTreeItem projectItem = (LFWProjectTreeItem) treeItems[i];
				if(projectItem.getProjectModel().getJavaProject().equals(project)){
					projectTreeItem = projectItem;
					break;
				}
			}
			LFWDirtoryTreeItem nodeTreeItem = null;
			LFWBusinessCompnentTreeItem busiCompTreeItem = null;
			if(LFWPersTool.isBCPProject(project)){
				TreeItem currComp = LFWPersTool.getCurrentBusiCompTreeItem();
				TreeItem[] compTreeItems = projectTreeItem.getItems();
				for (int i = 0; i < compTreeItems.length; i++) {
					TreeItem treeItem = compTreeItems[i];
					if(treeItem.getText().equals(currComp.getText())){
						busiCompTreeItem = (LFWBusinessCompnentTreeItem) treeItem;
						break;
					}
				}
			}
			TreeItem[] nodesTreeItems = null;
			if(busiCompTreeItem != null)
				nodesTreeItems = busiCompTreeItem.getItems();
			else
				nodesTreeItems = projectTreeItem.getItems();
			for (int i = 0; i < nodesTreeItems.length; i++) {
				TreeItem treeItem = nodesTreeItems[i];
				if(treeItem.getText().equals(WEBPersConstants.PUBLIC_REFNODE)){
					nodeTreeItem = (LFWDirtoryTreeItem) treeItem;
					pubRefNode = nodeTreeItem;
					break;
				}
			}	
			TreeItem selRefNodeTreeItem = null;
			String refnodeId = refnode.getId();
			if(refnodeId.indexOf(".") != -1){ //$NON-NLS-1$
				String firstFolder = refnodeId.substring(0, refnodeId.indexOf(".")); //$NON-NLS-1$
				String leftfolder = refnodeId.substring(refnodeId.indexOf(".") + 1); //$NON-NLS-1$
				
				while(nodeTreeItem != null){
					TreeItem[] childTreeItems = nodeTreeItem.getItems();
					for (int i = 0; i < childTreeItems.length; i++) {
						if(nodeTreeItem == null)
							break;
						TreeItem childTreeItem = childTreeItems[i];
						if(childTreeItem instanceof LFWDirtoryTreeItem && childTreeItem.getText().equals(firstFolder)){
							if(leftfolder.indexOf(".") != -1){ //$NON-NLS-1$
								firstFolder = leftfolder.substring(0, leftfolder.indexOf(".")); //$NON-NLS-1$
								leftfolder = leftfolder.substring(leftfolder.indexOf(".")+ 1); //$NON-NLS-1$
							}
							nodeTreeItem = (LFWDirtoryTreeItem) childTreeItem;
							break;
						}
						else {
							TreeItem[] dsTreeItems = nodeTreeItem.getItems();
							for (int j = 0; j < dsTreeItems.length; j++) {
								TreeItem refNodeTreeItem = dsTreeItems[j];
								if(refNodeTreeItem instanceof LFWRefNodeTreeItem){
									if(refNodeTreeItem.getText().substring(4).equals(leftfolder)){
										selRefNodeTreeItem =  refNodeTreeItem;
										//tree.setSelection(refNodeTreeItem);
										nodeTreeItem = null;
										break;
									}
								}
							}
						}
					}
				}
			}
			else{
				TreeItem[] dsTreeItems = nodeTreeItem.getItems();
				for (int j = 0; j < dsTreeItems.length; j++) {
					TreeItem refNodeTreeItem = dsTreeItems[j];
					if(refNodeTreeItem instanceof LFWRefNodeTreeItem){
						String text = refNodeTreeItem.getText();
						if(text.substring(text.indexOf("]") + 1).equals(refnodeId)){ //$NON-NLS-1$
							selRefNodeTreeItem = refNodeTreeItem;
						//	tree.setSelection(refNodeTreeItem);
							nodeTreeItem = null;
							break;
						}
					}
				}
			}
			if(selRefNodeTreeItem != null){
				tree.setSelection(selRefNodeTreeItem);
			}
			else
				tree.setSelection(pubRefNode);
		}
	
	}
	
	public void refreshTreeItemText(BaseRefNode refnode){
		TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
		if(treeItem instanceof LFWRefNodeTreeItem){
			LFWRefNodeTreeItem refnodeTreeItem = (LFWRefNodeTreeItem) treeItem;
			refnodeTreeItem.setText(WEBPersConstants.COMPONENT_REFNODE + refnode.getId()+ "[" + refnode.getText() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	public void  deleteNode(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		IEditorInput input = getEditorInput();
		RefNodeEditorInput refEditorInput = (RefNodeEditorInput)input;
		BaseRefNode refnode = (BaseRefNode)refEditorInput.getCloneElement();
		if(widget.getViewModels().getRefNode(refnode.getId()) == null){
			 try {
				view.deleteNewNode();
			} catch (Exception e) {
				MainPlugin.getDefault().logError(e.getMessage(), e);
			}
		 }
	 }
	
	public static RefNodeEditor getActiveEditor(){
		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if(page != null){
			editor = page.getActiveEditor();

		}
		if(editor != null && editor instanceof RefNodeEditor){
			return (RefNodeEditor)editor;
		}else {
			return null;
		}
	}
	
	public void commandStackChanged(EventObject arg0) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(arg0);
	}
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		//TODO 其他保存操作
		String msg = graph.getCells().get(0).validate();
		if(msg != null){
			String message = M_refnode.RefNodeEditor_0+msg;
			if(!MessageDialog.openConfirm(getSite().getShell(), M_refnode.RefNodeEditor_1, message))
				return;
		}
		save();
	}
	
	
	//保存
	public boolean save(){
		IEditorInput input = getEditorInput();
		RefNodeEditorInput refnodeEditorInput = (RefNodeEditorInput)input;
		BaseRefNode refnode = (BaseRefNode) refnodeEditorInput.getCloneElement();
		RefNodeElementObj refnodeElement = (RefNodeElementObj) this.graph.getCells().get(0);
		BaseRefNode refnodenew = refnodeElement.getRefnode();
		refnodenew.setId(refnode.getId());
		refnodeElement.setRefnode(refnodenew);
		//根据当前节点获取widgetItem
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
		if(widgetTreeItem != null){
			LfwView lfwwidget = widgetTreeItem.getWidget();
			if(lfwwidget != null){
				ViewModels viewModel =  lfwwidget.getViewModels();
				IRefNode oldrefnode = viewModel.getRefNode(refnodeElement.getOriginalId());
				if(oldrefnode != null)
					viewModel.removeRefNode(refnodeElement.getOriginalId());
				viewModel.addRefNode(refnodenew);
				refnodeElement.setOriginalId(refnodenew.getId());
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
			refreshTreeItem(refnodenew);
			return true;
		}
//		else{
//			String projectPath = LFWPersTool.getProjectPath();
//			TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
//			String directory = treeItem.getText();
//			TreeItem parent = treeItem.getParentItem();
//			while (parent != null && (parent instanceof LFWDirtoryTreeItem)) {
//				//LFWDirtoryTreeItem parent = (LFWDirtoryTreeItem)treeItem.getParentItem();
//				if (parent.getText().equals("公共参照"))
//					break;
//				directory = ((LFWDirtoryTreeItem)parent).getFile().getName() + "/" + directory;
//				parent = parent.getParentItem();
//			}
//			
//			if(LFWPersTool.isBCPProject(LFWPersTool.getCurrentProject())){
//				LFWBusinessCompnentTreeItem busiCompnent = LFWPersTool.getCurrentBusiCompTreeItem();
//				projectPath += "/" + busiCompnent.getText().substring(busiCompnent.getText().indexOf(WEBProjConstants.BUSINESSCOMPONENT) + 
//						WEBProjConstants.BUSINESSCOMPONENT.length() + 1, busiCompnent.getText().length() -1 );
//			}
//			String filePath = projectPath + "/web/pagemeta/public/refnodes";
//			if(directory.lastIndexOf("/") != -1){
//				String folder = directory.substring(0, directory.lastIndexOf("/"));
//				filePath = projectPath + "/web/pagemeta/public/refnodes/" + folder;
//			}
//			
//			String fileName = treeItem.getText().substring(treeItem.getText().indexOf("]") + 1) + ".xml";
//			LFWPersTool.getCurrentProject().getLocation();
//			//.getWorkspace().getRoot().getLocation();
//			
//			//String rootPath = LFWPersTool.getProjectModuleName(LFWPersTool.getCurrentProject());
//			String rootPath = LFWUtility.getContextFromResource(LFWPersTool.getCurrentProject());
//			LFWConnector.saveRefNodetoXml("/" + rootPath, filePath, fileName, refnodenew);
//			refreshTreeItem(refnode);
//			getCommandStack().markSaveLocation();
//			return true;
//		}
		return true;
	}

	
	private LFWWidgetTreeItem widgeTreeItem = null;
	private LfwView widget = null;
	private RefNodeElementObj refnodeElement;
	
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		RefNodeEditorInput refnodeEditorInput = (RefNodeEditorInput)input;
		BaseRefNode refnode = (BaseRefNode)refnodeEditorInput.getCloneElement();
		refnodeElement = new RefNodeElementObj(refnode.getId());
		refnodeElement.setFromPool(refnodeEditorInput.isFromPool());
		refnodeElement.setRefnode(refnode);
		//refnodeElement.setRefnode(refnode);
		refnodeElement.setLocation(new Point(100, 100));
		refnodeElement.setSize(new Dimension(150,150));
		graph.addCell(refnodeElement);
		if(LFWPersTool.getCurrentWidgetTreeItem() != null){
			widgeTreeItem = LFWPersTool.getCurrentWidgetTreeItem();
			widget = widgeTreeItem.getWidget();
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
	
	public static ComboDataEditor getActiveComboEditor(){
		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if(page != null){
			editor = page.getActiveEditor();

		}
		if(editor != null && editor instanceof ComboDataEditor){
			return (ComboDataEditor)editor;
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
		return null;
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
	
}

