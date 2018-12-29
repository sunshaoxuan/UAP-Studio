package nc.uap.lfw.button;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_button;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * button editor input
 * @author zhangxya
 *
 */
public class ButtonEditorInput extends ElementEditorInput{


	public ButtonEditorInput(ButtonComp button, LfwView widget, LfwWindow pagemeta){
		super(button, widget, pagemeta);
	}
	
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_button.ButtonEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_button.ButtonEditorInput_0;
	}

}
