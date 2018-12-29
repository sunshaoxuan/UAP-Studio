package nc.uap.portal.portlets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBasicElementObj;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * PortletElementPart
 * 
 * @author dingrf
 *
 */
public class PortletElementPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener, NodeEditPart {

	public PortletElementPart() {
		super();
	}

	/**
	 * ´´½¨Í¼ÐÎ
	 */
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
		if (cell instanceof PortletElementObj) {
			PortletElementObj portlet = (PortletElementObj) cell;
			figure = new PortletEleObjFigure(portlet);
		}
		
		return figure;
	}

	
	protected void createEditPolicies() {
		
	}

	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if (PortletElementObj.PROP_PORTLET_ELEMENT.equals(name)) {
			refreshChildren();
		}
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

	protected List<Connection> getModelSourceConnections() {
		return ((LFWBasicElementObj) this.getModel()).getSourceConnections();
	}

	protected List<Connection> getModelTargetConnections() {
		return ((LFWBasicElementObj) this.getModel()).getTargetConnections();
	}

}
