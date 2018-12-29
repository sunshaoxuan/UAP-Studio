package nc.uap.lfw.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.editor.contextmenubar.ContextMenuElementObjFigure;
import nc.lfw.editor.menubar.MenubarElementFigure;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.lfw.editor.menubar.figure.MenuItemElementFigure;
import nc.lfw.editor.widget.WidgetElementFigure;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.button.ButtonElementObj;
import nc.uap.lfw.button.ButtonElementObjFigure;
import nc.uap.lfw.combodata.ComboDataElementFigure;
import nc.uap.lfw.combodata.ComboDataElementObj;
import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.editor.application.ApplicationFigure;
import nc.uap.lfw.editor.application.ApplicationObj;
//import nc.uap.lfw.excel.ExcelElementFigure;
//import nc.uap.lfw.excel.ExcelElementObj;
import nc.uap.lfw.form.FormElementObj;
import nc.uap.lfw.form.FormElementObjFigure;
import nc.uap.lfw.grid.GridElementFigure;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.grid.core.GridLevelElementObj;
import nc.uap.lfw.grid.core.GridLevelElementObjFigure;
import nc.uap.lfw.iframe.IFrameElementObj;
import nc.uap.lfw.iframe.IFrameElementObjFigure;
import nc.uap.lfw.image.ImageElementObj;
import nc.uap.lfw.image.ImageElementObjFigure;
import nc.uap.lfw.label.LabelElementObj;
import nc.uap.lfw.label.LabelElementObjFigure;
import nc.uap.lfw.linkcomp.LinkCompElementObj;
import nc.uap.lfw.linkcomp.LinkCompElementObjFigure;
import nc.uap.lfw.listview.ListViewElementFigure;
import nc.uap.lfw.listview.ListViewElementObj;
import nc.uap.lfw.pagination.PaginationElementFigure;
import nc.uap.lfw.pagination.PaginationElementObj;
import nc.uap.lfw.palette.ChildConnection;
import nc.uap.lfw.perspective.figures.ChildConnectionFigure;
import nc.uap.lfw.perspective.figures.ConnectionFigure;
//import nc.uap.lfw.perspective.listener.ListenerElementFigure;
//import nc.uap.lfw.perspective.listener.ListenerElementObj;
import nc.uap.lfw.perspective.model.DatasetElementFigure;
import nc.uap.lfw.perspective.model.DatasetElementObj;
import nc.uap.lfw.perspective.model.RefDatasetElementFigure;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.perspective.policy.LFWDSFieldRelationEditPolicy;
import nc.uap.lfw.perspective.policy.LFWGraphLayoutEditPolicy;
import nc.uap.lfw.perspective.policy.RefElementEditPolicy;
import nc.uap.lfw.progressbar.ProgressBarEleObj;
import nc.uap.lfw.progressbar.ProgressBarEleObjFigure;
import nc.uap.lfw.refnode.RefNodeElementObj;
import nc.uap.lfw.refnode.RefNodeElementObjFigure;
import nc.uap.lfw.refnoderel.DatasetFieldEleObjFigure;
import nc.uap.lfw.refnoderel.DatasetFieldElementObj;
import nc.uap.lfw.selfdefcomp.core.SelfDefEleObj;
import nc.uap.lfw.selfdefcomp.core.SelfDefEleObjFigure;
import nc.uap.lfw.textcomp.TextCompElementObj;
import nc.uap.lfw.textcomp.TextCompElementObjFigure;
import nc.uap.lfw.toolbar.ToolBarElementObj;
import nc.uap.lfw.toolbar.ToolBarElementObjFigure;
import nc.uap.lfw.tree.TreeElementObj;
import nc.uap.lfw.tree.TreeElementObjFigure;
import nc.uap.lfw.tree.core.TreeLevelElementObj;
import nc.uap.lfw.tree.core.TreeLevelElementObjFigure;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.requests.SelectionRequest;


/**
 * ×é¼þ Part
 * @author zhangxya
 *
 */
public class LFWElementPart extends AbstractGraphicalEditPart implements PropertyChangeListener , NodeEditPart{

	@SuppressWarnings("unused")
	private LFWBaseEditor editor = null;
	public LFWElementPart(LFWBaseEditor editor) {
		super();
		this.editor = editor;
	}
	protected IFigure createFigure() {
		Object model = getModel();
		IFigure figure = null;
		if(model instanceof LFWBasicElementObj){
			LFWBasicElementObj cell = (LFWBasicElementObj)model;
			figure = getFigureByModel(cell);
		}
		return figure;
	}
	
	public void activate() {
		super.activate();
		((LFWBasicElementObj)getModel()).addPropertyChangeListener(this);
	}

	
	public void deactivate() {
		super.deactivate();
		((LFWBasicElementObj)getModel()).removePropertyChangeListener(this);
	}
	
	
	public static IFigure getFigureByModel(LFWBasicElementObj cell){
		IFigure figure = null;
//		if (cell instanceof ListenerElementObj) {
//			figure = new ListenerElementFigure((ListenerElementObj) cell);
//		} 
//		else 
		if(cell instanceof FormElementObj){
			figure = new FormElementObjFigure((FormElementObj)cell);
		}
		else if (cell instanceof DatasetElementObj) {
			figure = new DatasetElementFigure((DatasetElementObj) cell);
		} 
		else if(cell instanceof TreeLevelElementObj){
			figure = new TreeLevelElementObjFigure((TreeLevelElementObj)cell);
		}
		else if (cell instanceof RefDatasetElementObj) {
			figure = new RefDatasetElementFigure((RefDatasetElementObj) cell);
		} 
		else if(cell instanceof DatasetFieldElementObj){
			figure = new DatasetFieldEleObjFigure((DatasetFieldElementObj)cell);
		}
		else if (cell instanceof GridElementObj){
			figure = new GridElementFigure((GridElementObj)cell);
		} 
		else if(cell instanceof GridLevelElementObj){
			figure = new GridLevelElementObjFigure((GridLevelElementObj)cell);
		}
		else if (cell instanceof ListViewElementObj){
			figure = new ListViewElementFigure((ListViewElementObj)cell);
		} 
		else if (cell instanceof PaginationElementObj){
			figure = new PaginationElementFigure((PaginationElementObj)cell);
		} 
		else if (cell instanceof TreeElementObj){
			figure = new TreeElementObjFigure((TreeElementObj)cell);
		}
		else if(cell instanceof ChildConnection){
			figure = new ChildConnectionFigure((ChildConnection)cell);
		}
		else if (cell instanceof Connection) {
			figure = new ConnectionFigure((Connection) cell);
		}
		else if(cell instanceof MenubarElementObj){
			figure = new MenubarElementFigure((MenubarElementObj) cell);
		}
		else if(cell instanceof MenuElementObj){
			figure = new MenuItemElementFigure((LfwElementObjWithGraph) cell);
		}
		else if(cell instanceof ComboDataElementObj){
			figure = new ComboDataElementFigure((LfwElementObjWithGraph) cell);
		}
		else if(cell instanceof ProgressBarEleObj)
			figure = new ProgressBarEleObjFigure((LfwElementObjWithGraph)cell);
		else if(cell instanceof RefNodeElementObj){
			figure = new RefNodeElementObjFigure((LfwElementObjWithGraph) cell);
		}
		else if(cell instanceof ButtonElementObj){
			figure = new ButtonElementObjFigure((LfwElementObjWithGraph)cell);
		}
//		else if(cell instanceof ExcelElementObj){
//			figure = new ExcelElementFigure((LfwElementObjWithGraph)cell);
//		}
		else if(cell instanceof TextCompElementObj){
			figure = new TextCompElementObjFigure((LfwElementObjWithGraph)cell);
		}
		else if(cell instanceof IFrameElementObj){
			figure = new IFrameElementObjFigure((LfwElementObjWithGraph)cell);
		}
		else if(cell instanceof LabelElementObj){
			figure = new LabelElementObjFigure((LfwElementObjWithGraph)cell);
		}
		else if(cell instanceof ImageElementObj){
			figure = new ImageElementObjFigure((LfwElementObjWithGraph)cell);
		}
		else if(cell instanceof ToolBarElementObj){
			figure = new ToolBarElementObjFigure((LfwElementObjWithGraph)cell);
		}
		else if(cell instanceof LinkCompElementObj){
			figure = new LinkCompElementObjFigure((LfwElementObjWithGraph)cell);
		}
		else if(cell instanceof WidgetElementObj){
			figure = new WidgetElementFigure((WidgetElementObj)cell);
		}
//		else if(cell instanceof PageStateElementObj){
//			figure = new PageStateElementFigure((PageStateElementObj)cell);
//		}
		else if(cell instanceof ContextMenuElementObj){
			figure = new ContextMenuElementObjFigure((ContextMenuElementObj)cell);
		}
		else if(cell instanceof SelfDefEleObj){
			figure = new SelfDefEleObjFigure((SelfDefEleObj)cell);
		}
		else if(cell instanceof ApplicationObj){
			figure = new ApplicationFigure((ApplicationObj)cell);
		}
		return figure;
	}


	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new LFWGraphLayoutEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new LFWDSFieldRelationEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RefElementEditPolicy());
		
	}
	
	public void performRequest(Request request) {
		if(RequestConstants.REQ_OPEN.equals(request.getType())||request instanceof SelectionRequest){
			LFWBaseRectangleFigure fig = (LFWBaseRectangleFigure)getFigure();
			LFWPersTool.showErrDlg(fig.getErrStr());
		}
	}

	
	
	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if(DatasetElementObj.PROP_DATASET_ELEMENT.equals(name)){
			DatasetElementObj cell = (DatasetElementObj)getModel();
			String validate = cell.validate();
			DatasetElementFigure dsFigure = (DatasetElementFigure)getFigure();
			dsFigure.markError(validate);
			dsFigure.deleteAllAttris();
			for(Field field:cell.getDs().getFieldSet().getFields()){
				dsFigure.showField(field);
			}
			refreshChildren();
		}else if(RefNodeElementObj.PROP_REFNODE_ELEMENT.equals(name)){
			RefNodeElementObj refnode = (RefNodeElementObj) getModel();
			String validata = refnode.validate();
			RefNodeElementObjFigure refnodeFigure = (RefNodeElementObjFigure) getFigure();
			refnodeFigure.markError(validata);
		}
		else if(ComboDataElementObj.PROP_COMBODATA_ELEMENT.equals(name)){
			ComboDataElementObj comboData = (ComboDataElementObj) getModel();
			String validata = comboData.validate();
			ComboDataElementFigure comboDataFigure = (ComboDataElementFigure) getFigure();
			comboDataFigure.markError(validata);
		}
		else if(GridElementObj.PROP_GRID_ELEMENT.equals(name)){
			GridElementObj grid = (GridElementObj) getModel();
			String validata = grid.validate();
			GridElementFigure gridFigure = (GridElementFigure) getFigure();
			gridFigure.markError(validata);
		}
		else if(ListViewElementObj.PROP_LISTVIEW_ELEMENT.equals(name)){
			ListViewElementObj lv = (ListViewElementObj) getModel();
			String validata = lv.validate();
			ListViewElementFigure lvFigure = (ListViewElementFigure) getFigure();
			lvFigure.markError(validata);
		}
		else if(PaginationElementObj.PROP_PAGINATION_ELEMENT.equals(name)){
			PaginationElementObj lv = (PaginationElementObj) getModel();
			String validata = lv.validate();
			PaginationElementFigure lvFigure = (PaginationElementFigure) getFigure();
			lvFigure.markError(validata);
		}
//		else if(ExcelElementObj.PROP_EXCEL_ELEMENT.equals(name)){
//			ExcelElementObj excel = (ExcelElementObj) getModel();
//			String validata = excel.validate();
//			ExcelElementFigure excelFigure = (ExcelElementFigure) getFigure();
//			excelFigure.markError(validata);
//		}
		else if(TreeElementObj.PROP_TREE_ELEMENT.equals(name)){
			TreeElementObj tree = (TreeElementObj) getModel();
			String validata = tree.validate();
			TreeElementObjFigure treeFigure = (TreeElementObjFigure) getFigure();
			treeFigure.markError(validata);
		}
		else if(FormElementObj.PROP_FORM_ELEMENT.equals(name)){
			FormElementObj form = (FormElementObj) getModel();
			String validata = form.validate();
			FormElementObjFigure formFigure = (FormElementObjFigure) getFigure();
			formFigure.markError(validata);
		}
		else if(ButtonElementObj.PROP_BUTTON_ELEMENT.equals(name)){
			ButtonElementObj button = (ButtonElementObj) getModel();
			String validata = button.validate();
			ButtonElementObjFigure buttonFigure = (ButtonElementObjFigure) getFigure();
			buttonFigure.markError(validata);
		}
		else if(SelfDefEleObj.PROP_SELFDEFCOMP_ELEMENT.equals(name)){
			SelfDefEleObj selfdef = (SelfDefEleObj) getModel();
			String validata = selfdef.validate();
			SelfDefEleObjFigure selfdefFigure = (SelfDefEleObjFigure) getFigure();
			selfdefFigure.markError(validata);
		}
		else if(TextCompElementObj.PROP_TEXTCOMP_ELEMENT.equals(name)){
			TextCompElementObj textcomp = (TextCompElementObj) getModel();
			String validata = textcomp.validate();
			TextCompElementObjFigure textFigure = (TextCompElementObjFigure) getFigure();
			textFigure.markError(validata);
		}
		else if(IFrameElementObj.PROP_IFRAME_ELEMENT.equals(name)){
			IFrameElementObj iframeComp = (IFrameElementObj) getModel();
			String validata = iframeComp.validate();
			IFrameElementObjFigure iframeFigure = (IFrameElementObjFigure) getFigure();
			iframeFigure.markError(validata);
		}
		else if(LinkCompElementObj.PROP_LINKCOMP_ELEMENT.equals(name)){
			LinkCompElementObj linkComp = (LinkCompElementObj) getModel();
			String validata = linkComp.validate();
			LinkCompElementObjFigure linkFigure = (LinkCompElementObjFigure) getFigure();
			linkFigure.markError(validata);
		}
		else if(LabelElementObj.PROP_LABEL_ELEMENT.equals(name)){
			LabelElementObj labelcomp = (LabelElementObj) getModel();
			String validata = labelcomp.validate();
			LabelElementObjFigure labelFigure = (LabelElementObjFigure) getFigure();
			labelFigure.markError(validata);
		}
		else if(ImageElementObj.PROP_IMAGE_ELEMENT.equals(name)){
			ImageElementObj imagecomp = (ImageElementObj) getModel();
			String validata = imagecomp.validate();
			ImageElementObjFigure imageFigure = (ImageElementObjFigure) getFigure();
			imageFigure.markError(validata);
		}
		else if(ToolBarElementObj.PROP_TOOLBAR_ELEMENT.equals(name)){
			ToolBarElementObj toolbarcomp = (ToolBarElementObj) getModel();
			String validata = toolbarcomp.validate();
			ToolBarElementObjFigure toolbarFigure = (ToolBarElementObjFigure) getFigure();
			toolbarFigure.markError(validata);
		}
		else if(RefDatasetElementObj.PROP_CELL_SIZE.equals(name)
				||RefDatasetElementObj.PROP_CELL_LOCATION.equals(name)){
			refreshVisuals();
		}else if(DatasetElementObj.PROP_ADD_FIELDRELATION_PROPS.equals(name)){
			FieldRelation fr = (FieldRelation)event.getNewValue();
			((DatasetElementFigure)getFigure()).addAttribute(fr);
		}else if(DatasetElementObj.PROP_DEL_ALLFIELDRELATION_PROPS.equals(name)){
			((DatasetElementFigure)getFigure()).deleteAllAttris();
		}
		else if(DatasetElementObj.PROP_DEL_FIELDRELATION_PROPS.equals(name)){
			FieldRelation fr = (FieldRelation)event.getNewValue();
			((DatasetElementFigure)getFigure()).deleteAttribute(fr);
		}
		else if(ToolBarElementObj.PROP_ADD_TOOLBARITEM.equals(name)){
			ToolBarItem toolbarItem = (ToolBarItem) event.getNewValue();
			((ToolBarElementObjFigure)getFigure()).addItem(toolbarItem);
		}
		else if(ToolBarElementObj.PROP_DEL_TOOLBARITEM.equals(name)){
			ToolBarItem toolbarItem = (ToolBarItem) event.getNewValue();
			((ToolBarElementObjFigure)getFigure()).deleteToolBarItem(toolbarItem);
		}
		else if(ToolBarElementObj.PROP_UPDATE_TOOLBARITEM.equals(name)){
			ToolBarItem toolbarItem = (ToolBarItem) event.getNewValue();
			((ToolBarElementObjFigure)getFigure()).updatItem(toolbarItem);
		}
		else if(RefDatasetElementObj.PROP_FIELDRELATION_PROPS.equals(name)){
			FieldRelation fr = (FieldRelation)event.getNewValue();
			((RefDatasetElementFigure)getFigure()).addAttribute(fr);
		}
		else if(RefDatasetElementObj.PROP_DELETE_FIELDRELATION_PROPS.equals(name)){
			FieldRelation fr = (FieldRelation)event.getNewValue();
			((RefDatasetElementFigure)getFigure()).deleteAttribute(fr);
		}
		else if(DatasetElementObj.PROP_ADD_CELL_PROPS.equals(name)){
			//Field attri = (Field)event.getNewValue();
			//((DatasetElementFigure)getFigure()).addAttribute(attri);
		}
		else if(ComboDataElementObj.PROP_ADD_COMBO_CELL_PROPS.equals(name)){
			//CombItem combItem = (CombItem) event.getNewValue();
		}
		else if(GridElementObj.Grid_ADD_CELL_PROPS.equals(name)){
			//GridColumn attri = (GridColumn)event.getNewValue();
		}
		else if(DatasetElementObj.PROP_UPDATE_CELL_PROPS.equals(name)){
//			Field oldValue= (Field)event.getOldValue();
//			Field attri = (Field)event.getNewValue();
//			((DatasetElementFigure)getFigure()).updateAttribute(attri);
		}
		else if (DatasetElementObj.PROP_CHILD_ADD.equals(name)
				|| DatasetElementObj.PROP_CHILD_REMOVE.equals(name)) {
			refreshChildren();
		} else if (LFWBasicElementObj.PROP_SOURCE_CONNECTION.equals(name)) {
			refreshSourceConnections();
		} else if (LFWBasicElementObj.PROP_TARGET_CONNECTION.equals(name)) {
			refreshTargetConnections();
		} else if (Connection.PROP_BEND_POINT.equals(name)) {
			refreshVisuals();
		}
	}
	
	protected void refreshVisuals() {
		if(getModel() instanceof DatasetElementObj ){
			//DatasetElementObj cell = (DatasetElementObj)getModel();
		}
		if(getModel() instanceof RefDatasetElementObj ){
			RefDatasetElementObj cell = (RefDatasetElementObj)getModel();
			Rectangle rect = new Rectangle(cell.getLocation(), cell.getSize());
			((GraphicalEditPart)getParent()).setLayoutConstraint(this, getFigure(), rect);
		}
//		else if(getModel() instanceof ListenerElementObj){
//			ListenerElementObj cell = (ListenerElementObj)getModel();
//			Rectangle rect = new Rectangle(cell.getLocation(), cell.getSize());
//			((GraphicalEditPart)getParent()).setLayoutConstraint(this, getFigure(), rect);
//		}
	}
	
	private ConnectionAnchor anchor;
	protected ConnectionAnchor getConnectionAnchor(){
	       if (anchor == null) {
	           anchor = new ChopboxAnchor(getFigure());
	       }
	       return anchor;
	    }
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		// TODO Auto-generated method stub
		return getConnectionAnchor();
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		// TODO Auto-generated method stub
		return getConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart arg0) {
		return getConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request arg0) {
		return getConnectionAnchor();
	}
	
	protected List<LFWBasicElementObj> getModelChildren() {
		return new ArrayList<LFWBasicElementObj>();	
	}
    protected List<Connection> getModelSourceConnections() {
        return ((LFWBasicElementObj) this.getModel()).getSourceConnections();
    }

    protected List<Connection> getModelTargetConnections() {
        return ((LFWBasicElementObj) this.getModel()).getTargetConnections();
    }
}
