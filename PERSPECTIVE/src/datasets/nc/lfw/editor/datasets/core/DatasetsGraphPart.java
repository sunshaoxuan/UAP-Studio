package nc.lfw.editor.datasets.core;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LFWGraphFigure;
import nc.lfw.editor.common.LfwCanvasGraphPart;
import nc.uap.lfw.perspective.model.DatasetGraph;
import nc.uap.lfw.perspective.policy.LFWGraphLayoutEditPolicy;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;

/**
 * dataset ¹ØÏµÍ¼part
 * @author zhangxya
 *
 */
public class DatasetsGraphPart extends LfwCanvasGraphPart{

	public DatasetsGraphPart(LFWBaseEditor editor) {
		super(editor);
	}
	
	protected IFigure createFigure() {
		LFWGraphFigure figure = new LFWGraphFigure();
		return figure;
	}

	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new LFWGraphLayoutEditPolicy());
	}

	public void activate() {
		super.activate();
		((DatasetsGraph)getModel()).addPropertyChangeListener(this);
	}
	
	public void deactivate() {
		super.deactivate();
		((DatasetsGraph)getModel()).removePropertyChangeListener(this);
	}
	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if(DatasetGraph.PROP_CHILD_ADD.equals(name)||DatasetGraph.PROP_CHILD_REMOVE.equals(name)){
			refreshChildren();
		}
	}
	
	protected List<LFWBasicElementObj> getModelChildren() {
		List<LFWBasicElementObj> list = new ArrayList<LFWBasicElementObj>();
		DatasetsGraph graph = (DatasetsGraph)getModel();
		if(graph.getCells().size() > 0)
			list.addAll(graph.getCells());
		return list;
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
	

    protected List<Connection> getModelSourceConnections() {
        return ((LFWBasicElementObj) this.getModel()).getSourceConnections();
    }

    protected List<Connection> getModelTargetConnections() {
        return ((LFWBasicElementObj) this.getModel()).getTargetConnections();
    }
}