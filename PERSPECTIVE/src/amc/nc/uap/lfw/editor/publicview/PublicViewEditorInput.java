/**
 * 
 */
package nc.uap.lfw.editor.publicview;

import nc.lfw.editor.common.LfwBaseEditorInput;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;

/**
 * @author chouhl
 *
 */
public class PublicViewEditorInput extends LfwBaseEditorInput {

	private String folderPath;
	private String pmPath;
	
	private LFWDirtoryTreeItem dirTreeItem;
	
	private LfwView widget;
	private LfwView cloneElement;
	
	public PublicViewEditorInput(){
		super();
	}
	
	public LfwView getWidget() {
		return widget;
	}
	
	public WebElement getCloneElement() {
		if(cloneElement == null)
			cloneElement = (LfwView) widget.clone();
		return cloneElement;
	}
	
	public PublicViewEditorInput(LfwView widget){
		this.widget = widget;
	}
	
	public String getPmPath() {
		return pmPath;
	}

	public void setPmPath(String pmPath) {
		this.pmPath = pmPath;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getName() {
		return M_editor.PublicViewEditorInput_0;
	}

	public String getToolTipText() {
		return M_editor.PublicViewEditorInput_0;
	}

	public LFWDirtoryTreeItem getDirTreeItem() {
		return dirTreeItem;
	}

	public void setDirTreeItem(LFWDirtoryTreeItem dirTreeItem) {
		this.dirTreeItem = dirTreeItem;
	}

}
