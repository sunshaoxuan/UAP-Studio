/**
 * 
 */
package nc.uap.lfw.editor.application;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LFWGraphFigure;
import nc.lfw.editor.pagemeta.PagemetaGraph;
import nc.uap.lfw.perspective.policy.AMCGraphLayoutEditPolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * @author chouhl
 *
 */
public class ApplicationGraphPart extends AbstractGraphicalEditPart implements PropertyChangeListener {

	private ApplicationEditor editor = null;
	
	public ApplicationGraphPart(ApplicationEditor editor){
		super();
		this.editor = editor;
	}

	protected IFigure createFigure() {
		LFWGraphFigure figure = new LFWGraphFigure();
		return figure;
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new AMCGraphLayoutEditPolicy());
	}

	public void activate() {
		super.activate();
		((ApplicationGraph) getModel()).addPropertyChangeListener(this);
	}

	public void deactivate() {
		super.deactivate();
		((ApplicationGraph) getModel()).removePropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if (PagemetaGraph.PROP_CHILD_ADD.equals(name) || PagemetaGraph.PROP_CHILD_REMOVE.equals(name)) {
			refreshChildren();
		}
	}
	
	/**
	 * 获取Graph下所有显示图形
	 */
	protected List<LFWBasicElementObj> getModelChildren() {
		ApplicationGraph graph = (ApplicationGraph)getModel();
		List<LFWBasicElementObj> list = new ArrayList<LFWBasicElementObj>();
//		if(graph.getJsListeners().size() > 0){
//			list.addAll(graph.getJsListeners());
//		}
		if(graph.getApplicationCells().size() > 0){
			list.addAll(graph.getApplicationCells());
		}
		if(graph.getWindowCells().size() > 0){
			list.addAll(graph.getWindowCells());
		}
		return list;
	}

	public ApplicationEditor getEditor() {
		return editor;
	}

	public void setEditor(ApplicationEditor editor) {
		this.editor = editor;
	}

}
