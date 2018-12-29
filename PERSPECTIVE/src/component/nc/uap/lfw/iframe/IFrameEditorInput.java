package nc.uap.lfw.iframe;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_iframe;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * Iframe editor input
 * @author zhangxya
 *
 */
public class IFrameEditorInput extends ElementEditorInput{


	public IFrameEditorInput(IFrameComp iFrame, LfwView widget, LfwWindow pagemeta){
		super(iFrame, widget, pagemeta);
	}
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_iframe.IFrameEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_iframe.IFrameEditorInput_1;
	}
}
