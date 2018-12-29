package nc.uap.lfw.editor.extNode;

import org.eclipse.jface.dialogs.MessageDialog;

import nc.lfw.design.view.LFWWfmConnector;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.ref.LfwRefInfoVO;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.webcomponent.LFWRefFolderTreeItem;

public class DeleteRefFolderAction extends NodeAction{
	LFWRefFolderTreeItem refInfoItem = null;
	public DeleteRefFolderAction(LFWRefFolderTreeItem item){
		super(M_editor.DeleteRefFolderAction_0);
		this.refInfoItem = item;
	}
	public void run(){
		LfwRefInfoVO refVO = refInfoItem.getRefInfo();
		if(refVO!=null){
			String pk = refVO.getPk_refinfo();
			pk = LFWWfmConnector.deleteRefInfoByPk(pk);
			if(pk==null){
				MessageDialog.openError(null, M_editor.DeleteRefFolderAction_1, M_editor.DeleteRefFolderAction_2);
			}
			else{
				refInfoItem.deleteNode();
			}
		}		
	}
}
