package nc.lfw.editor.datasets.core;

import nc.lfw.editor.common.LfwBaseEditorInput;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * datasetEditorInput
 * @author zhangxya
 *
 */
public class DatasetsEditorInput extends LfwBaseEditorInput{

	private LFWSeparateTreeItem separateTreeItem = null;
	
	public DatasetsEditorInput(){
		super();
	}
	
	public LFWSeparateTreeItem getSeparateTreeItem() {
		return separateTreeItem;
	}

	public void setSeparateTreeItem(LFWSeparateTreeItem separateTreeItem) {
		this.separateTreeItem = separateTreeItem;
	}

	public DatasetsEditorInput(LFWSeparateTreeItem separateTreeItem){
		this.separateTreeItem = separateTreeItem;
	}
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_editor.DatasetsEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return  M_editor.DatasetsEditorInput_0;
	}

}
