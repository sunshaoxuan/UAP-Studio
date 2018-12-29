package nc.lfw.editor.common;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.perspective.policy.LFWGraphLayoutEditPolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * »­²¼Part
 * 
 * @author zhangxya
 * 
 */
public class LfwCanvasGraphPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener {

	private LFWBaseEditor editor = null;

	public LFWBaseEditor getEditor() {
		return editor;
	}

	public void setEditor(LFWBaseEditor editor) {
		this.editor = editor;
	}

	public LfwCanvasGraphPart(LFWBaseEditor editor) {
		super();
		this.editor = editor;
	}

	
	protected IFigure createFigure() {
		LFWGraphFigure figure = new LFWGraphFigure();
		return figure;
	}

	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new LFWGraphLayoutEditPolicy());
	}

	public void activate() {
		super.activate();
		((LfwBaseGraph) getModel()).addPropertyChangeListener(this);
	}

	public void deactivate() {
		super.deactivate();
		((LfwBaseGraph) getModel()).removePropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if (LfwBaseGraph.PROP_CHILD_ADD.equals(name)
				|| LfwBaseGraph.PROP_CHILD_REMOVE.equals(name)) {
			refreshChildren();
		}
	}

	
	protected List<LFWBasicElementObj> getModelChildren() {
		LfwBaseGraph graph = (LfwBaseGraph) getModel();
		List<LFWBasicElementObj> list = new ArrayList<LFWBasicElementObj>();
//		if (graph.getJsListeners().size() > 0)
//			list.addAll(graph.getJsListeners());
		if (graph.getCells().size() > 0)
			list.addAll(graph.getCells());
		return list;
	}
}
