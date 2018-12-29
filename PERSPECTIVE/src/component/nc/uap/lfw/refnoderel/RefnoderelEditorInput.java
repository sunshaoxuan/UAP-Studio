package nc.uap.lfw.refnoderel;


import nc.lfw.editor.common.WidgetEditorInput;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.refnode.RefNodeRelation;
import nc.uap.lfw.lang.M_refnoderel;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

public class RefnoderelEditorInput extends WidgetEditorInput{

	private RefNodeRelation refnodeRel;
	public RefNodeRelation getRefnodeRel() {
		return refnodeRel;
	}

	public void setRefnodeRel(RefNodeRelation refnodeRel) {
		this.refnodeRel = refnodeRel;
	}

	public RefnoderelEditorInput(RefNodeRelation refnodeRel, LfwView widget, LfwWindow pagemeta) {
		super(widget, pagemeta);
		this.refnodeRel = refnodeRel;
	}

	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_refnoderel.RefnoderelEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_refnoderel.RefnoderelEditorInput_0;
	}
}
