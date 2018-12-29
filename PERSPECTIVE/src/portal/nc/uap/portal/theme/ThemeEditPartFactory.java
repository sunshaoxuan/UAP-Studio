package nc.uap.portal.theme;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * PluginEditPartFactory
 * 
 * @author dingrf
 * 
 */
public class ThemeEditPartFactory implements EditPartFactory {

	/**
	 * ´´½¨EditPart
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if (model instanceof ThemeElementObj){
			editPart = new ThemeElementPart();
		}
		else
			throw new RuntimeException("illegal param");
		editPart.setModel(model);
		return editPart;
	}

}
