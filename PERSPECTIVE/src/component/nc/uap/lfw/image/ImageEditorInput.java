package nc.uap.lfw.image;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_iframe;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * Í¼ÐÎ±à¼­Æ÷EditorInput
 * @author zhangxya
 *
 */
public class ImageEditorInput  extends ElementEditorInput{


	public ImageEditorInput(ImageComp image, LfwView widget, LfwWindow pagemeta){
		super(image, widget, pagemeta);
	}
	
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_iframe.ImageEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_iframe.ImageEditorInput_0;
	}

}
