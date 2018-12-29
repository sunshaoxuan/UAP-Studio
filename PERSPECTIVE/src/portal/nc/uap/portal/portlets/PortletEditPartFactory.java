package nc.uap.portal.portlets;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * PortletEditPartFactory
 * 
 * @author dingrf
 *
 */
public class PortletEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if (model instanceof PortletElementObj){
			editPart = new PortletElementPart();
		} 
		else
			throw new RuntimeException("illegal param");
		editPart.setModel(model);
		return editPart;
	}

}
