/**
 * 
 */
package nc.uap.lfw.application;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.DeleteAMCNodeAction;

/**
 * 
 * ɾ��Application�ڵ���
 * @author chouhl
 *
 */
public class DeleteApplicationNodeAction extends DeleteAMCNodeAction {

	public DeleteApplicationNodeAction(){
		super(WEBPersConstants.DEL_APPLICATION, WEBPersConstants.DEL_APPLICATION);
	}
	
}
