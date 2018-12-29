package nc.uap.portal.events;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * EventsEditPartFactory
 * 
 * @author dingrf
 * 
 */
public class EventsEditPartFactory implements EditPartFactory {

	/**
	 * ´´½¨EditPart
	 */
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if (model instanceof EventsElementObj){
			editPart = new EventsElementPart();
		}
		else
			throw new RuntimeException("illegal param");
		editPart.setModel(model);
		return editPart;
	}

}
