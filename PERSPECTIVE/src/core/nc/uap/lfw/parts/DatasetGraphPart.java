package nc.uap.lfw.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LFWGraphFigure;
import nc.lfw.editor.common.LfwCanvasGraphPart;
import nc.uap.lfw.perspective.model.DatasetGraph;
import nc.uap.lfw.perspective.policy.LFWGraphLayoutEditPolicy;
import nc.uap.lfw.perspective.policy.RefElementEditPolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

/**
 * »­²¼Part
 * @author zhangxya
 *
 */
public class DatasetGraphPart extends LfwCanvasGraphPart{

	public DatasetGraphPart(LFWBaseEditor editor) {
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
		((DatasetGraph)getModel()).addPropertyChangeListener(this);
	}
	
	public void deactivate() {
		super.deactivate();
		((DatasetGraph)getModel()).removePropertyChangeListener(this);
	}
	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if(DatasetGraph.PROP_CHILD_ADD.equals(name)||DatasetGraph.PROP_CHILD_REMOVE.equals(name)){
			refreshChildren();
		}
			//MDPExplorerTreeView.getMDPExploerTreeView(null).fireJGraphEditor(name, editor,(Cell) event.getNewValue());
		}
	
	protected List<LFWBasicElementObj> getModelChildren() {
		List<LFWBasicElementObj> list = super.getModelChildren();
		DatasetGraph graph = (DatasetGraph)getModel();
		if(graph.getRefds().size() > 0)
			list.addAll(graph.getRefds());
		return list;
	}
}
