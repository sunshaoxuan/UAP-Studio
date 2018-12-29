package nc.uap.lfw.refnode.core;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.refnode.BaseRefNode;
import nc.uap.lfw.lang.M_refnode;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
/**
 * ≤Œ’’editorInput
 * @author zhangxya
 *
 */

public class RefNodeEditorInput  extends ElementEditorInput{

	private boolean isFromPool;
	
	public boolean isFromPool() {
		return isFromPool;
	}

	public void setFromPool(boolean isFromPool) {
		this.isFromPool = isFromPool;
	}

	public RefNodeEditorInput(BaseRefNode refnode, LfwView widget, LfwWindow pagemeta){
		super(refnode, widget, pagemeta);
	}
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_refnode.RefNodeEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_refnode.RefNodeEditorInput_0;
	}

}
