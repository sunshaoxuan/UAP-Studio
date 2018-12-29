package nc.uap.lfw.selfdefcomp.core;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_iframe;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

public class SelfDefCompEditorInput extends ElementEditorInput{


	public SelfDefCompEditorInput(SelfDefComp seldefComp, LfwView widget, LfwWindow pagemeta){
		super(seldefComp, widget, pagemeta);
	}
	
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_iframe.SelfDefCompEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_iframe.SelfDefCompEditorInput_0;
	}

}
