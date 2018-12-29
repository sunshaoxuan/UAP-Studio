package nc.lfw.editor.datasets.core;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import nc.lfw.editor.common.LFWAbstractViewPage;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwBaseEditorInput;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.IRefDataset;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.factory.ElementEidtPartFactory;
import nc.uap.lfw.palette.PaletteFactory;
import nc.uap.lfw.perspective.model.DatasetElementObj;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;

import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Datasets Eidtor
 * @author zhangxya
 *
 */
public class DatasetsEditor extends LFWBaseEditor {
	private DatasetsGraph graph = new DatasetsGraph();
	public DatasetsEditor() {
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

	
	
	
	public void commandStackChanged(EventObject arg0) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(arg0);
	}

	public void doSave(IProgressMonitor monitor) {
		//TODO 其他保存操作
		save();
	}
	
	//保存
	public boolean save(){
		LfwView lfwwidget = LFWPersTool.getLfwWidget();
		if(lfwwidget != null){
			LFWPersTool.saveWidget(lfwwidget);
			getCommandStack().markSaveLocation();
			return true;
		}
		return false;
	}

	
	private LFWWidgetTreeItem widgeTreeItem = null;
	private LfwView widget = null;
	
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		DatasetsEditorInput datasetsEditor = (DatasetsEditorInput)input;
		LFWSeparateTreeItem lfwseparateTreeItem = datasetsEditor.getSeparateTreeItem();
		widgeTreeItem = LFWPersTool.getWidgetTreeItem(lfwseparateTreeItem);
		widget = (LfwView)widgeTreeItem.getWidget();
		Dataset[]  datasetMap = widget.getViewModels().getDatasets();
		int i = 0;
		int parenty = 0;
		int parentx = 0;
		List<DatasetElementObj> dslist = new ArrayList<DatasetElementObj>();
		List<String> dsRelationsList = new ArrayList<String>();
		for (int k = 0; k < datasetMap.length; k++) {
			Dataset ds = datasetMap[k];
			if(!(ds instanceof IRefDataset)){
				DatasetElementObj dsobj = new DatasetElementObj(ds.getId());
				dsobj.setDatasets(true);
				dsobj.setDs(ds);
				dslist.add(dsobj);
				int pointy = 100;
				int pointx = 150;
				//dsobj.setSize(new Dimension(100,100));
				if(i == 0){
					pointx = 100;
					pointy = 50;
				}
				else{
					//单数
					if(i%2 != 0){
						pointx = parentx + 150 + WEBPersConstants.DATASETBETWEEN;
						pointy = parenty;
					}
					else{
						pointx = parentx - 150 - WEBPersConstants.DATASETBETWEEN;
						pointy = parenty + 150 + WEBPersConstants.DATASETBETWEEN;
					}
				}
			
				parenty = pointy;
				parentx = pointx;
				Point point = new Point(pointx, pointy);
				dsobj.setLocation(point);
				dsobj.deleteAllFR(dsobj);
				graph.addCell(dsobj);
				i++;
			}
			DatasetRelations dsRelations = widget.getViewModels().getDsrelations();
			if(dsRelations != null){
				for (int j = 0; j < dslist.size(); j++) {
					for (int j2 = 0; j2 < dslist.size(); j2++) {
						DatasetRelation dsRelation = dsRelations.getDsRelation(dslist.get(j).getDs().getId(), dslist.get(j2).getDs().getId());
						if(dsRelation != null){
							if(!dsRelationsList.contains(dsRelation.getId())){
								dsRelationsList.add(dsRelation.getId());
								DsRelationConnection con = new DsRelationConnection(dslist.get(j), dslist.get(j2));
								con.connect();
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	protected TreeItem getSelectedTreeItem(LFWDirtoryTreeItem dirTreeItem,
			LfwBaseEditorInput editorInput) {
		return null;
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
			paleteRoot = PaletteFactory.createDatasetsPalette();
		}
		return paleteRoot;
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
		return null;
	}

}

