package nc.uap.lfw.chart.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LFWGraphFigure;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.pagemeta.PagemetaGraph;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class ChartGraphPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener {

	private LFWBaseEditor editor = null;

	public LFWBaseEditor getEditor() {
		return editor;
	}

	public void setEditor(LFWBaseEditor editor) {
		this.editor = editor;
	}

	public ChartGraphPart(LFWBaseEditor editor2) {
		super();
		this.editor = editor2;
	}

	
	protected IFigure createFigure() {
		LFWGraphFigure figure = new LFWGraphFigure();
		return figure;
	}

	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ChartLayoutEditorPolicy());
		
	}

	public void activate() {
		super.activate();
		((ChartGraph) getModel()).addPropertyChangeListener(this);
	}

	public void deactivate() {
		super.deactivate();
		((ChartGraph) getModel()).removePropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if (LfwBaseGraph.PROP_CHILD_ADD.equals(name)
				|| LfwBaseGraph.PROP_CHILD_REMOVE.equals(name)) {
			refreshChildren();
		}
	}

	
	protected List getModelChildren() {
		ChartGraph graph = (ChartGraph) getModel();
		List<LFWBasicElementObj> list = new ArrayList<LFWBasicElementObj>();
		if(graph.getCells().size() > 0)
			list.addAll(graph.getCells());
		return list;
	}

}
