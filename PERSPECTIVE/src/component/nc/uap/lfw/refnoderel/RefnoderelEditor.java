package nc.uap.lfw.refnoderel;

import java.util.EventObject;
import java.util.List;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.refnode.BaseRefNode;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.MasterFieldInfo;
import nc.uap.lfw.core.refnode.RefNodeRelation;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.grid.core.GridViewPage;
import nc.uap.lfw.lang.M_refnoderel;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;
import nc.uap.lfw.refnode.RefNodeElementObj;

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

public class RefnoderelEditor extends LFWBaseEditor {
	private RefnoderelGraph graph = new RefnoderelGraph();
	
	public RefnoderelGraph getGraph() {
		return graph;
	}

	public void setGraph(RefnoderelGraph graph) {
		this.graph = graph;
	}

	public RefnoderelEditor() {
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
		RefnoderelEditorInput refEditorInput = (RefnoderelEditorInput)input;
		RefNodeRelation refnoderel = (RefNodeRelation)refEditorInput.getRefnodeRel();
		LfwView widget = LFWPersTool.getCurrentWidget();
		if(widget.getViewModels().getRefNodeRelations() != null){
			if(widget.getViewModels().getRefNodeRelations().getRefNodeRelation(refnoderel.getId()) == null){
				 try {
					view.deleteNewNode();
				} catch (Exception e) {
					MainPlugin.getDefault().logError(e.getMessage(), e);
				}
			 }
		}
	 }
	
	
	public void commandStackChanged(EventObject arg0) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(arg0);
	}
	public void doSave(IProgressMonitor monitor) {
		save();
	}
	
	
	public boolean save(){
		LfwView widget = LFWPersTool.getCurrentWidget();
		LFWPersTool.saveWidget(widget);
		getCommandStack().markSaveLocation();
		return true;
		
	}
	/**
	 * ¾Û½¹±à¼­Æ÷
	 */
	public void setFocus() {
		super.setFocus();
		Tree tree = LFWPersTool.getTree();
		IEditorInput input = getEditorInput();
		RefnoderelEditorInput refnodeRelEditorInput = (RefnoderelEditorInput)input;
		if(refnodeRelEditorInput.getCurrentTreeItem().isDisposed()){
			this.setDirtyFalse();
			MessageDialog.openError(null, M_refnoderel.RefnoderelEditor_1, M_refnoderel.RefnoderelEditor_2);
			return;
		}
			
		LFWSeparateTreeItem lfwSeparaTreeItem = getWebSeparateTreeItem(WEBPersConstants.REFNODE);		
		RefNodeRelation refnodeRel = (RefNodeRelation) refnodeRelEditorInput.getRefnodeRel();
		TreeItem[] childTreeItems = lfwSeparaTreeItem.getItems();
		for (int i = 0; i < childTreeItems.length; i++) {
			if(childTreeItems[i] instanceof LFWRefNodeRelTreeItem){
				LFWRefNodeRelTreeItem webT = (LFWRefNodeRelTreeItem) childTreeItems[i];
				if(webT.getData() instanceof RefNodeRelation){
					RefNodeRelation gr = (RefNodeRelation) webT.getData();
					if(refnodeRel.getId().equals(gr.getId())){
						tree.setSelection(webT);
						break;
					}
				}
			}
		}

	}
	
	
	//±£´æ
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		LfwView widget = LFWPersTool.getCurrentWidget();
		RefnoderelEditorInput refrelEditor = (RefnoderelEditorInput)input;
		RefNodeRelation refrel = refrelEditor.getRefnodeRel();
		RefNodeElementObj detailRenodeElement = null;
		List<MasterFieldInfo> refnodeInfos = refrel.getMasterFieldInfos();
		String detailrefId = refrel.getDetailRefNode();
		if(detailrefId != null){
			IRefNode refnode = widget.getViewModels().getRefNode(detailrefId);
			detailRenodeElement = new RefNodeElementObj(detailrefId);
			if(refnode instanceof BaseRefNode){
				detailRenodeElement.setRefnode((BaseRefNode)refnode);
				detailRenodeElement.setRefRelationId(refrel.getId());
				detailRenodeElement.setLocation(new Point(300, 150));
				detailRenodeElement.setSize(new Dimension(120, 120));
				graph.setDetailRefNode(detailRenodeElement);
			}else{
				throw new LfwRuntimeException(M_refnoderel.RefnoderelEditor_0);
			}
		}
		if(refnodeInfos != null){
			int parenty = 0;
			int pointy = 100;
			for (int i = 0; i < refnodeInfos.size(); i++) {
				if(i == 0)
					pointy = 100;
				else 
					pointy = parenty + 100 + WEBPersConstants.BETWEEN;
				parenty = pointy;
				MasterFieldInfo refnodeInfo = refnodeInfos.get(i);
				Dataset ds = widget.getViewModels().getDataset(refnodeInfo.getDsId());
				Field field = ds.getFieldSet().getField(refnodeInfo.getFieldId());
				String refnodeId = refnodeInfo.getFieldId();
				DatasetFieldElementObj fieldEle = new DatasetFieldElementObj();
				fieldEle.setId(refnodeId);
				fieldEle.setNullProcessor(refnodeInfo.getNullProcess());
				fieldEle.setDsId(refnodeInfo.getDsId());
				fieldEle.setFilterSql(refnodeInfo.getFilterSql());
				fieldEle.setField(field);
				fieldEle.setLocation(new Point(100, pointy));
				fieldEle.setSize(new Dimension(120, 120));
				graph.addMainRefNode(fieldEle);
				RefNodeRelConnection conn = new RefNodeRelConnection(fieldEle, detailRenodeElement);
				conn.connect();
			}
		}
	}

	public static RefnoderelEditor getActiveEditor(){
//		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
//		if(page != null){
//			editor = page.getActiveEditor();
//
//		}
		editor = LFWBaseEditor.getActiveEditor();
		if(editor != null && editor instanceof RefnoderelEditor){
			return (RefnoderelEditor)editor;
		}else {
			return null;
		}
		
	}
	
	public boolean isSaveAsAllowed() {
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
			paleteRoot = PaletteFactory.createRefNodeRelPalette();
			//createGridPalette();//reacteRefNodeRelPalette();
		}
		return paleteRoot;
	}

	
	public GridViewPage createViewPage() {
		return new GridViewPage();
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

