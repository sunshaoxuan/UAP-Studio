package nc.lfw.editor.widget.plug;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.uap.lfw.perspective.policy.LFWPlugRelationEditPolicy;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * @author dingrf
 *
 */
public class PlugoutDescElementPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener, NodeEditPart {

//	private WidgetEditor editor = null;

	public PlugoutDescElementPart() {
		super();
//		this.editor = editor;
	}

	protected IFigure createFigure() {
		Object model = getModel();
		IFigure figure = null;
		if (model instanceof PlugoutDescElementObj) {
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
		if (cell instanceof PlugoutDescElementObj) {
			PlugoutDescElementObj obj = (PlugoutDescElementObj) cell;
			figure = new PlugoutDescElementFigure(obj);
		}
		return figure;
	}

	
	protected void createEditPolicies() {
		
		// Widget建立连接规则
//		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new WidgetRelationEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new LFWPlugRelationEditPolicy());

		
	}

	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
//		if (PlugoutDescElementObj.PROP_PLUGOUT_CHANGE.equals(name)) {
//			refreshChildren();
//		}	
//		if (WidgetElementObj.PROP_CHILD_ADD.equals(name)
//				|| WidgetElementObj.PROP_CHILD_REMOVE.equals(name)) {
//			refreshChildren();
		if (LFWBasicElementObj.PROP_SOURCE_CONNECTION.equals(name)) {
			refreshSourceConnections();
		} 
//		else if (LFWBasicElementObj.PROP_TARGET_CONNECTION.equals(name)) {
//			refreshTargetConnections();
//		}
	}

	private ConnectionAnchor anchor;

	protected ConnectionAnchor getConnectionAnchor() {
		if (anchor == null) {
			anchor = new ChopboxAnchor(getFigure());
		}
		return anchor;
	}

	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart arg0) {
		return getConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request arg0) {
		return getConnectionAnchor();
	}

	
	protected List getModelChildren() {
		PlugoutDescElementObj graph = (PlugoutDescElementObj) getModel();
		List<LFWBasicElementObj> list = new ArrayList<LFWBasicElementObj>();
//		if (graph != null)
//			list.add(graph);
		return list;
	}

	protected List<Connection> getModelSourceConnections() {
		return ((LFWBasicElementObj) this.getModel()).getSourceConnections();
	}

	protected List<Connection> getModelTargetConnections() {
		return ((LFWBasicElementObj) this.getModel()).getTargetConnections();
	}

}
