/**
 * 
 */
package nc.uap.lfw.application;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.DeleteAMCNodeAction;

/**
 * 
 * É¾³ýApplication½ÚµãÀà
 * @author chouhl
 *
 */
public class DeleteApplicationNodeAction extends DeleteAMCNodeAction {

	public DeleteApplicationNodeAction(){
		super(WEBPersConstants.DEL_APPLICATION, WEBPersConstants.DEL_APPLICATION);
	}
	
}
