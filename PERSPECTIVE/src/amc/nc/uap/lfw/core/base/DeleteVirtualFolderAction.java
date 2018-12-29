/**
 * 
 */
package nc.uap.lfw.core.base;

import nc.uap.lfw.core.WEBPersConstants;

/**
 * @author chouhl
 * 2011-12-8
 */
public class DeleteVirtualFolderAction extends DeleteAMCNodeAction {

	public DeleteVirtualFolderAction() {
		super(WEBPersConstants.DEL_VIRTUALDIR, WEBPersConstants.DEL_VIRTUALDIR);
	}

}
