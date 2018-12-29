package nc.lfw.editor.pagemeta;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.widget.WidgetElementFigure;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.editor.window.WindowFigure;
import nc.uap.lfw.editor.window.WindowObj;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class RelationElementPart extends AbstractGraphicalEditPart implements PropertyChangeListener, NodeEditPart {

//	private RelationEditor editor = null;
	
	public RelationElementPart() {
		super();
//		this.editor = editor;
	}
	
	protected IFigure createFigure() {
		Object model = getModel();
		IFigure figure = null;
		if (model instanceof LFWBasicElementObj) {
			LFWBasicElementObj cell = (LFWBasicElementObj) model;
			figure = getFigureByModel(cell);
		}
		return figure;
	}
	public void activate() {
		super.activate();
		((LFWBasicElementObj) getModel()).addPropertyChangeListener(this);
	}

	
	public void deactivate() {
		super.deactivate();
		((LFWBasicElementObj) getModel()).removePropertyChangeListener(this);
	}
	
	public static IFigure getFigureByModel(LFWBasicElementObj cell) {
		IFigure figure = null;
		
		if (cell instanceof WidgetElementObj) {
			WidgetElementObj widgetObj = (WidgetElementObj) cell;
			figure = new WidgetElementFigure(widgetObj);
		} 

		else if (cell instanceof WindowObj){
			WindowObj winObj = (WindowObj)cell;
			figure = new WindowFigure(winObj);
		}
		else if (cell instanceof Connection) {
			Connection con = (Connection) cell;
			figure = new nc.uap.lfw.perspective.figures.RelationFigure(con);
		}
		return figure;
	}

	private ConnectionAnchor anchor;

	protected ConnectionAnchor getConnectionAnchor() {
		if (anchor == null) {
			anchor = new ChopboxAnchor(getFigure());
		}
		return anchor;
	}
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		// TODO Auto-generated method stub
		return getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		// TODO Auto-generated method stub
		return getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		// TODO Auto-generated method stub
		return getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		// TODO Auto-generated method stub
		return getConnectionAnchor();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if (WidgetElementObj.PROP_CHILD_ADD.equals(name)
				|| WidgetElementObj.PROP_CHILD_REMOVE.equals(name)) {
			refreshChildren();
		} else if (LFWBasicElementObj.PROP_SOURCE_CONNECTION.equals(name)) {
			refreshSourceConnections();
		} else if (LFWBasicElementObj.PROP_TARGET_CONNECTION.equals(name)) {
			refreshTargetConnections();
		} else if (Connection.PROP_BEND_POINT.equals(name)) {
			refreshVisuals();
		}else if(LfwElementObjWithGraph.PROP_CELL_LOCATION.equals(name)){
			refreshVisuals();
		}
		
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		
	}
	protected List getModelChildren() {
		return new ArrayList();
	}

	protected List<Connection> getModelSourceConnections() {
		return ((LFWBasicElementObj) this.getModel()).getSourceConnections();
	}

	protected List<Connection> getModelTargetConnections() {
		return ((LFWBasicElementObj) this.getModel()).getTargetConnections();
	}

}
