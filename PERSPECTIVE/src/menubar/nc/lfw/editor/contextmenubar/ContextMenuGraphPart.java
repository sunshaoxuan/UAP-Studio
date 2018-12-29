package nc.lfw.editor.contextmenubar;

import java.util.List;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwCanvasGraphPart;
import nc.lfw.editor.menubar.MenuRelationEditPolicy;

import org.eclipse.gef.EditPolicy;

public class ContextMenuGraphPart extends LfwCanvasGraphPart {

	public ContextMenuGraphPart(LFWBaseEditor editor) {
		super(editor);
	}

	
	@SuppressWarnings("unchecked")
	public List getModelChildren() {
		List list = super.getModelChildren();
		ContextMenuGrahp graph = (ContextMenuGrahp)getModel();
		list.addAll(graph.getAllChildren());
		return list;
	}

	
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new MenuRelationEditPolicy());
	}

}
