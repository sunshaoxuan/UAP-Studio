package nc.uap.lfw.chart.core;

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
import nc.uap.lfw.chart.model.Bar2DChartModelEleObj;
import nc.uap.lfw.chart.model.Bar2DChartModelFigure;
import nc.uap.lfw.chart.model.Bar3DChartModelEleObj;
import nc.uap.lfw.chart.model.Bar3DChartModelFigure;
import nc.uap.lfw.chart.model.BaseChartModelEleObj;
import nc.uap.lfw.chart.model.BaseChartModelFigure;
import nc.uap.lfw.perspective.policy.LFWDSFieldRelationEditPolicy;
import nc.uap.lfw.perspective.policy.LFWGraphLayoutEditPolicy;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.requests.SelectionRequest;

public class ChartElementPart  extends AbstractGraphicalEditPart implements PropertyChangeListener , NodeEditPart{

	@SuppressWarnings("unused")
	private LFWBaseEditor editor = null;
	public ChartElementPart(LFWBaseEditor editor) {
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
		if (cell instanceof ChartCompEleObj) {
			figure = new ChartCompEleObjFigure((ChartCompEleObj) cell);
		} 
		else if(cell instanceof BaseChartModelEleObj){
			if(cell instanceof Bar2DChartModelEleObj)
				figure = new Bar2DChartModelFigure((Bar2DChartModelEleObj)cell);
			else if(cell instanceof Bar3DChartModelEleObj)
				figure = new Bar3DChartModelFigure((Bar3DChartModelEleObj)cell);
			else
				figure = new BaseChartModelFigure((BaseChartModelEleObj)cell);
		}
		else if(cell instanceof ChartConfigEleObj){
			figure = new ChartConfigFigure((ChartConfigEleObj)cell);
		}
		return figure;
	}


	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new LFWGraphLayoutEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new LFWDSFieldRelationEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ChartCompEditPolicy());
		
	}
	
	public void performRequest(Request request) {
		if(RequestConstants.REQ_OPEN.equals(request.getType())||request instanceof SelectionRequest){
			LFWBaseRectangleFigure fig = (LFWBaseRectangleFigure)getFigure();
			LFWPersTool.showErrDlg(fig.getErrStr());
		}
	}

	
	
	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if(ChartCompEleObj.PROP_CHART_ELEMENT.equals(name)){
			ChartCompEleObj cell = (ChartCompEleObj)getModel();
			String validate = cell.validate();
			ChartCompEleObjFigure chartFigure = (ChartCompEleObjFigure)getFigure();
			chartFigure.markError(validate);
			refreshChildren();
		}
		else if(BaseChartModelEleObj.PROP_CHARTMODEL_ELEMENT.equals(name)){
			BaseChartModelEleObj cell = (BaseChartModelEleObj)getModel();
			String validate = cell.validate();
			BaseChartModelFigure figure = null;
			if(cell instanceof Bar2DChartModelEleObj){
				figure = (Bar2DChartModelFigure)getFigure();
			}
			else if(cell instanceof Bar3DChartModelEleObj){
				figure = (Bar3DChartModelFigure)getFigure();
			}
			figure.markError(validate);
			refreshChildren();
		}
		else if(ChartConfigEleObj.PROP_CHARTCONFIG_ELEMENT.equals(name)){
			ChartConfigEleObj cell = (ChartConfigEleObj)getModel();
			String validate = cell.validate();
			ChartConfigFigure figure = null;
			figure = (ChartConfigFigure)getFigure();
			figure.markError(validate);
			refreshChildren();
		}
		else if(LfwElementObjWithGraph.PROP_CELL_LOCATION.equals(name)){
			refreshVisuals();
		}
		
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
