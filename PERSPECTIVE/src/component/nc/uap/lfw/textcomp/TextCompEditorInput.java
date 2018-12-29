package nc.uap.lfw.textcomp;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_textcomp;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * textcomp ±à¼­Æ÷input
 * @author zhangxya
 *
 */

public class TextCompEditorInput extends ElementEditorInput{


	public TextCompEditorInput(TextComp textcomp, LfwView widget, LfwWindow pagemeta){
		super(textcomp, widget, pagemeta);
	}
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_textcomp.TextCompEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_textcomp.TextCompEditorInput_0;
	}
}
