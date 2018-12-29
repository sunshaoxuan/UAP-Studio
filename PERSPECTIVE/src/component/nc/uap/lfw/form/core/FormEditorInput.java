package nc.uap.lfw.form.core;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_form;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * formEditor input
 * @author zhangxya
 *
 */
public class FormEditorInput extends ElementEditorInput{


	public FormEditorInput(FormComp formcomp, LfwView widget, LfwWindow pagemeta){
		super(formcomp, widget, pagemeta);
	}
	
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_form.FormEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_form.FormEditorInput_0;
	}
}