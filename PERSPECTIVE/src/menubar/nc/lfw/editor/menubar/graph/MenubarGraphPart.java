package nc.lfw.editor.menubar.graph;

import java.util.List;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwCanvasGraphPart;
import nc.lfw.editor.menubar.MenuRelationEditPolicy;
import nc.lfw.editor.menubar.MenubarGraph;

import org.eclipse.gef.EditPolicy;

public class MenubarGraphPart extends LfwCanvasGraphPart {

	public MenubarGraphPart(LFWBaseEditor editor) {
		super(editor);
	}

	
	public List getModelChildren() {
		List list = super.getModelChildren();
		MenubarGraph graph = (MenubarGraph) getModel();
		list.addAll(graph.getAllChildren());
		return list;
	}

	
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new MenuRelationEditPolicy());
	}

}
