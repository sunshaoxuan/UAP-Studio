package nc.lfw.editor.common;

import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_pagemeta;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

public class PagemetaEditorInput extends LfwBaseEditorInput {

	private LfwWindow pagemeta;
	
	private LfwWindow cloneElement;
	
	private LFWPageMetaTreeItem pmTreeItem;
	
	public PagemetaEditorInput(LfwWindow pagemeta){
		super();
		this.pagemeta = (LfwWindow) pagemeta;
	}
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_pagemeta.PagemetaEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return M_pagemeta.PagemetaEditorInput_0;
	}


	public LfwWindow getPagemeta() {
		return pagemeta;
	}
	
	public WebElement getCloneElement() {
		if(cloneElement == null)
			cloneElement = (LfwWindow) pagemeta.clone();
		return cloneElement;
	}
	
	public LFWPageMetaTreeItem getPmTreeItem() {
		return pmTreeItem;
	}
	public void setPmTreeItem(LFWPageMetaTreeItem pmTreeItem) {
		this.pmTreeItem = pmTreeItem;
	}

}
