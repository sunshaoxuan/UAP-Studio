package nc.uap.lfw.tree.core;

import java.beans.PropertyChangeEvent;
import java.util.List;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LFWGraphFigure;
import nc.lfw.editor.common.LfwCanvasGraphPart;
import nc.uap.lfw.perspective.policy.LFWGraphLayoutEditPolicy;
import nc.uap.lfw.perspective.policy.RefElementEditPolicy;
import nc.uap.lfw.tree.TreeGraph;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

/**
 * tree graph part
 * @author zhangxya
 *
 */
public class TreeGraphPart  extends LfwCanvasGraphPart{

	public TreeGraphPart(LFWBaseEditor editor) {
		super(editor);
	}
	
	protected IFigure createFigure() {
		LFWGraphFigure figure = new LFWGraphFigure();
		return figure;
	}

	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new LFWGraphLayoutEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RefElementEditPolicy());
	}

	public void activate() {
		super.activate();
		((TreeGraph)getModel()).addPropertyChangeListener(this);
	}
	
	public void deactivate() {
		super.deactivate();
		((TreeGraph)getModel()).removePropertyChangeListener(this);
	}
	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if(TreeGraph.PROP_CHILD_ADD.equals(name)||TreeGraph.PROP_CHILD_REMOVE.equals(name)){
			refreshChildren();
		}
			//MDPExplorerTreeView.getMDPExploerTreeView(null).fireJGraphEditor(name, editor,(Cell) event.getNewValue());
		}
	
	protected List<LFWBasicElementObj> getModelChildren() {
		List<LFWBasicElementObj> list = super.getModelChildren();
		return list;
	}
	
}
