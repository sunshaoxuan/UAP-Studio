package nc.uap.lfw.linkcomp;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_iframe;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

public class LinkCompEditroInput extends ElementEditorInput{

	
	public LinkCompEditroInput(LinkComp linkcomp, LfwView widget, LfwWindow pagemeta) {
		super(linkcomp, widget, pagemeta);
	}
	
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_iframe.LinkCompEditroInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_iframe.LinkCompEditroInput_0;
	}
}
