/**
 * 
 */
package nc.uap.lfw.core.base;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWVirtualDirTreeItem;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * @author chouhl
 * 2011-12-7
 */
public class CreateVirtualFolderAction extends NodeAction {

	private String itemType = null;
	
	public CreateVirtualFolderAction(String itemType){
		super(WEBPersConstants.NEW_VIRTUALDIR);
		this.itemType = itemType;
	}
	
	@Override
	public void run() {
		createVirtualFolder();
	}
	
	private void createVirtualFolder(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if (view == null){
			return;
		}
		InputDialog input = new InputDialog(null, WEBPersConstants.NEW_VIRTUALDIR, "ÊäÈë" + WEBPersConstants.VIRTUALDIR + "Ãû³Æ", "", null);
		if (input.open() == InputDialog.OK) {
			String vitualDirName = input.getValue();
			if (vitualDirName != null && vitualDirName.trim().length() > 0) {
				vitualDirName = vitualDirName.trim();
				try {
					LFWVirtualDirTreeItem folderTreeItem = (LFWVirtualDirTreeItem)view.addVirtualTreeNode(vitualDirName);
					folderTreeItem.setType(itemType);
				} 
				catch (Exception e) {
					String title = WEBPersConstants.NEW_VIRTUALDIR;
					String message = e.getMessage();
					MessageDialog.openError(null, title, message);
				}
			}
		}
	}
	
}
