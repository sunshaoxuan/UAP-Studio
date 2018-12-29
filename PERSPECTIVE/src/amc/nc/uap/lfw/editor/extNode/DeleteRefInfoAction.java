package nc.uap.lfw.editor.extNode;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.ref.LfwRefInfoVO;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.webcomponent.LFWRefInfoTreeItem;

public class DeleteRefInfoAction extends NodeAction{

	LFWRefInfoTreeItem refInfoItem = null;
	public DeleteRefInfoAction(LFWRefInfoTreeItem item){
		super(M_editor.DeleteRefInfoAction_0);
		this.refInfoItem = item;
	}
	public void run(){
		LfwRefInfoVO refVO = refInfoItem.getRefInfo();
		if(refVO!=null){
			String pk = refVO.getPk_refinfo();
			pk = LFWWfmConnector.deleteRefInfoByPk(pk);
			if(pk==null){
				MessageDialog.openError(null, M_editor.DeleteRefInfoAction_1, M_editor.DeleteRefInfoAction_2);
			}
			else{
				refInfoItem.deleteNode();
			}
		}		
	}
}
