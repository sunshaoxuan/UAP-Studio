/**
 * 
 */
package nc.uap.lfw.editor.application;


//import nc.uap.lfw.perspective.listener.ListenerElementObj;

import nc.uap.lfw.editor.window.WindowConfigObj;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * @author chouhl
 *
 */
public class ApplicationEditPartFactory implements EditPartFactory {

	private ApplicationEditor editor = null;
	
	public ApplicationEditPartFactory(ApplicationEditor editor) {
		this.editor = editor;
	}

	public EditPart createEditPart(EditPart context, Object model) {
//		if(model instanceof WindowObj || model instanceof ApplicationObj || model instanceof ListenerElementObj){
		if(model instanceof WindowConfigObj || model instanceof ApplicationObj){
			context = new ApplicationPart();
		}
//		else if(model instanceof Connection) {
//			context = new WidgetConnectionPart();
//		}
		else if(model instanceof ApplicationGraph) {
			context = new ApplicationGraphPart(editor);
		}else{
			throw new RuntimeException("illegal param");
		}
		context.setModel(model);
		return context;
	}
	
}
