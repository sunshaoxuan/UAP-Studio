
package nc.uap.portal.core;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * EditorInput »ùÀà
 * 
 * @author dingrf
 *
 */
public abstract class PortalBaseEditorInput implements IEditorInput {

	
	private TreeItem currentTreeItem = null;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#exists()
	 * 
	 */
	@Override
	public boolean exists() {
		return false;
	}
	
	public TreeItem getCurrentTreeItem() {
		return currentTreeItem;
	}

	public void setCurrentTreeItem(TreeItem currentTreeItem) {
		this.currentTreeItem = currentTreeItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 * 
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 * 
	 */
	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		return null;
	}
}
