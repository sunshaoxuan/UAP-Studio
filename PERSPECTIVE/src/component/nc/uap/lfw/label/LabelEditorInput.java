package nc.uap.lfw.label;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_label;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * ±êÇ©±à¼­Æ÷Input
 * @author zhangxya
 *
 */
public class LabelEditorInput extends ElementEditorInput{


	public LabelEditorInput(LabelComp label, LfwView widget, LfwWindow pagemeta){
		super(label, widget, pagemeta);
	}
	
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_label.LabelEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_label.LabelEditorInput_1;
	}
}
