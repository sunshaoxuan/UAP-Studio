package nc.uap.lfw.refnoderel;

import java.beans.PropertyChangeEvent;
import java.util.List;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.LfwCanvasGraphPart;
import nc.uap.lfw.perspective.policy.LFWDSFieldRelationEditPolicy;

import org.eclipse.gef.EditPolicy;

public class RefNodeRelGraphPart extends LfwCanvasGraphPart {

	public RefNodeRelGraphPart(LFWBaseEditor editor) {
		super(editor);
	}

	
	@SuppressWarnings("unchecked")
	public List getModelChildren() {
		List list = super.getModelChildren();
		RefnoderelGraph graph = (RefnoderelGraph) getModel();
		if(graph.getDetailRefNode() != null)
			list.add(graph.getDetailRefNode());
		if(graph.getMainRefNodeList().size() > 0)
			list.addAll(graph.getMainRefNodeList());
		return list;
	}

	
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new LFWDSFieldRelationEditPolicy());
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if(LfwBaseGraph.PROP_CHILD_ADD.equals(name)||LfwBaseGraph.PROP_CHILD_REMOVE.equals(name)){
			refreshChildren();
		}
	}

}
