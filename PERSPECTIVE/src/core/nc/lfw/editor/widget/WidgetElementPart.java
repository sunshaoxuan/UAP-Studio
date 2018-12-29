package nc.lfw.editor.widget;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.editor.window.WindowConfigFigure;
import nc.uap.lfw.editor.window.WindowConfigObj;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.requests.SelectionRequest;

/**
 * @author guoweic
 *
 */
public class WidgetElementPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener, NodeEditPart {

	private WidgetEditor editor = null;

	public WidgetElementPart(WidgetEditor editor) {
		super();
		this.editor = editor;
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
//		if (cell instanceof ListenerElementObj) {
//			ListenerElementObj obj = (ListenerElementObj) cell;
//			figure = new ListenerElementFigure(obj);
//		} else 
		if (cell instanceof WidgetElementObj) {
			WidgetElementObj widgetObj = (WidgetElementObj) cell;
			figure = new WidgetElementFigure(widgetObj);
		}
		if (cell instanceof WindowConfigObj) {
			WindowConfigObj windowObj = (WindowConfigObj) cell;
			figure = new WindowConfigFigure(windowObj);
		}
		return figure;
	}

	
	protected void createEditPolicies() {
		
		// Widget建立连接规则
		//installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new WidgetRelationEditPolicy());
		
	}
	public void performRequest(Request request) {
		if(RequestConstants.REQ_OPEN.equals(request.getType())||request instanceof SelectionRequest){
			LFWBaseRectangleFigure fig = (LFWBaseRectangleFigure)getFigure();
			LFWPersTool.showErrDlg(fig.getErrStr());
		}
	}

	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if (WidgetElementObj.PROP_CHILD_ADD.equals(name)
				|| WidgetElementObj.PROP_CHILD_REMOVE.equals(name)){
			refreshChildren();
		} else if (LFWBasicElementObj.PROP_SOURCE_CONNECTION.equals(name)) {
			refreshSourceConnections();
		} else if (LFWBasicElementObj.PROP_TARGET_CONNECTION.equals(name)) {
			refreshTargetConnections();
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

	
	protected List getModelChildren() {
//		if (getModel() instanceof WidgetElementObj){
//			WidgetElementObj graph = (WidgetElementObj) getModel();
//			List<PlugoutDescElementObj> list = new ArrayList<PlugoutDescElementObj>();
//			if (graph.getPlugoutCells().size() > 0)
//				list.addAll(graph.getPlugoutCells());
//	//		if (graph.getWidgetCells().size() > 0)
//	//			list.addAll(graph.getWidgetCells());
//			return list;
//		}else
			return new ArrayList();
	}

	protected List<Connection> getModelSourceConnections() {
		return ((LFWBasicElementObj) this.getModel()).getSourceConnections();
	}

	protected List<Connection> getModelTargetConnections() {
		return ((LFWBasicElementObj) this.getModel()).getTargetConnections();
	}

}
