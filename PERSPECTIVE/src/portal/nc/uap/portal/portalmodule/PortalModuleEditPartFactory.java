package nc.uap.portal.portalmodule;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * PortalModuleEditPartFactory
 * 
 * @author dingrf
 * 
 */
public class PortalModuleEditPartFactory implements EditPartFactory {

	/**
	 * ´´½¨EditPart
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if (model instanceof PortalModuleElementObj){
			editPart = new PortalModuleElementPart();
		}
		else
			throw new RuntimeException("illegal param");
		editPart.setModel(model);
		return editPart;
	}

}
