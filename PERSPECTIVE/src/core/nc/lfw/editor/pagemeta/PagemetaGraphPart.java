package nc.lfw.editor.pagemeta;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LFWGraphFigure;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.editor.window.WindowObj;
import nc.uap.lfw.perspective.policy.LFWGraphLayoutEditPolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class PagemetaGraphPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener {

	private PagemetaEditor editor = null;

	public PagemetaEditor getEditor() {
		return editor;
	}

	public void setEditor(PagemetaEditor editor) {
		this.editor = editor;
	}

	public PagemetaGraphPart(PagemetaEditor editor) {
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
		((PagemetaGraph) getModel()).addPropertyChangeListener(this);
	}

	public void deactivate() {
		super.deactivate();
		((PagemetaGraph) getModel()).removePropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if (PagemetaGraph.PROP_CHILD_ADD.equals(name)
				|| PagemetaGraph.PROP_CHILD_REMOVE.equals(name)||PagemetaGraph.PROP_WINDOWPLUG_CHANGE.equals(name)) {
			refreshChildren();
		}
	}

	
	protected List getModelChildren() {
		PagemetaGraph graph = (PagemetaGraph) getModel();
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
			WindowObj selfObj = graph.getWindowCells().get(0);
			if(selfObj.getPluginCells().size()>0)
				list.addAll(selfObj.getPluginCells());
			if(selfObj.getPlugoutCells().size()>0)
				list.addAll(selfObj.getPlugoutCells());
		}
//		if (graph.getPagemeta().getConnectorMap().size() >0){
//			Map<String, Connector> connectorMap = graph.getPagemeta().getConnectorMap();
//			for (Iterator<String> i = connectorMap.keySet().iterator() ; i.hasNext(); ) {
//				String id = i.next();
//				list.add(connectorMap.get(id));
//			}	
//
//		}
//		if (graph.getPageStateCells().size() > 0)
//			list.addAll(graph.getPageStateCells());
		return list;
	}

}
