/**
 * 
 */
package nc.lfw.editor.common;

import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.perspective.webcomponent.LFWBusinessCompnentTreeItem;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * @author guoweic
 *
 */
public abstract class LfwBaseEditorInput implements IEditorInput {
	/**
	 * 当前工程
	 */
	private IProject project = null;
	/**
	 * 当前BCP组件节点
	 */
	private LFWBusinessCompnentTreeItem busiCompTreeItem = null;
	/**
	 * 当前非BCP组件节点
	 */
	private TreeItem currentTreeItem = null;
	
	protected LfwBaseEditorInput(){
		init();
	}
	
	private void init(){
		this.project = LFWAMCPersTool.getCurrentProject();
		this.busiCompTreeItem = LFWAMCPersTool.getCurrentBusiCompTreeItem();
		this.currentTreeItem = LFWAMCPersTool.getCurrentTreeItem();
	}

//	public static final String PAGE_ID_KEY = "PAGE_ID_KEY";
//	// 项目的绝对路径
//	public static final String PROJECT_PATH_KEY = "PROJECT_PATH_KEY";
	
	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		return null;
	}
	
	public IProject getProject() {
		return project;
	}

	public LFWBusinessCompnentTreeItem getBusiCompTreeItem() {
		return busiCompTreeItem;
	}

	public TreeItem getCurrentTreeItem() {
		return currentTreeItem;
	}

	public void setCurrentTreeItem(TreeItem currentTreeItem) {
		this.currentTreeItem = currentTreeItem;
	}
	
}
