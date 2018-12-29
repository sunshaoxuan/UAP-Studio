package nc.lfw.editor.widget;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LFWGraphFigure;

import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.editor.window.WindowConfigObj;
import nc.uap.lfw.perspective.policy.LFWGraphLayoutEditPolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * @author guoweic
 *
 */
public class WidgetGraphPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener {

	private WidgetEditor editor = null;

	public WidgetEditor getEditor() {
		return editor;
	}

	public void setEditor(WidgetEditor editor) {
		this.editor = editor;
	}

	public WidgetGraphPart(WidgetEditor editor) {
		super();
		this.editor = editor;
	}

	
	protected IFigure createFigure() {
		// TODO guoweic
		LFWGraphFigure figure = new LFWGraphFigure();
		return figure;
	}

	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new LFWGraphLayoutEditPolicy());
		
	}

	public void activate() {
		super.activate();
		((WidgetGraph) getModel()).addPropertyChangeListener(this);
	}

	public void deactivate() {
		super.deactivate();
		((WidgetGraph) getModel()).removePropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if (WidgetGraph.PROP_CHILD_ADD.equals(name)
				|| WidgetGraph.PROP_CHILD_REMOVE.equals(name)
				|| WidgetGraph.PROP_WIDGETPLUG_CHANGE.equals(name)){
//				|| WidgetGraph.PROP_PLUGOUT_REMOVE.equals(name)
//				|| WidgetGraph.PROP_PLUGOUT_ADD.equals(name)
//				|| WidgetGraph.PROP_PLUGIN_REMOVE.equals(name)
//				|| WidgetGraph.PROP_PLUGIN_ADD.equals(name)) {
			refreshChildren();
		}
	}

	
	protected List getModelChildren() {
		WidgetGraph graph = (WidgetGraph) getModel();
		List<LFWBasicElementObj> list = new ArrayList<LFWBasicElementObj>();
//		if (graph.getJsListeners().size() > 0)
//			list.addAll(graph.getJsListeners());
		if (graph.getWidgetCells().size() > 0){
			list.addAll(graph.getWidgetCells());
			for (WidgetElementObj widgetObj : graph.getWidgetCells()){
				if (widgetObj.getPluginCells().size() > 0)
					list.addAll(widgetObj.getPluginCells());
				if (widgetObj.getPlugoutCells().size() > 0)
					list.addAll(widgetObj.getPlugoutCells());
			}
		}
		if(graph.getWindowCells().size()>0){
			list.addAll(graph.getWindowCells());
			for(WindowConfigObj winObj:graph.getWindowCells()){
				if(winObj.getPluginCells().size()>0)
					list.addAll(winObj.getPluginCells());
				if(winObj.getPlugoutCells().size()>0)
					list.addAll(winObj.getPlugoutCells());
			}
		}
//		
//		if (graph.getPlugoutCells().size() > 0)
//			list.addAll(graph.getPlugoutCells());
//		if (graph.getPluginCells().size() > 0)
//			list.addAll(graph.getPluginCells());
		return list;
	}

}
