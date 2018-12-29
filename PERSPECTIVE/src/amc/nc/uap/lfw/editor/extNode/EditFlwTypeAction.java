package nc.uap.lfw.editor.extNode;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.perspective.webcomponent.LFWWfmFlwTypeTreeItem;
import nc.uap.wfm.vo.WfmFlwTypeVO;

import org.eclipse.jface.dialogs.IDialogConstants;

/**
 * 
 * @author qinjianc
 * 
 */
public class EditFlwTypeAction extends NodeAction {
	public EditFlwTypeAction() {
		super(WEBPersConstants.EDIT_WFMTYPE);
	}

	public void run() {
		editFlwType();
	}

	private void editFlwType() {		
		LFWWfmFlwTypeTreeItem selTI = (LFWWfmFlwTypeTreeItem)LFWPersTool.getCurrentTreeItem();
		WfmFlwTypeVO flwType = LFWWfmConnector.getFlwType(selTI.getType_pk());
		
		WfmFlwTypeDialog wfmDialog = new WfmFlwTypeDialog(
				WEBPersConstants.EDIT_WFMTYPE, flwType);

		if (wfmDialog.open() == IDialogConstants.OK_ID) {
			try {
				
				flwType.setTypename(wfmDialog.getTypeName());
				flwType.setTypecode(wfmDialog.getTypeCode());
				flwType.setServerclass(wfmDialog.getClazzName());
				LFWWfmConnector.editFlwType(flwType);
				selTI.setText(wfmDialog.getTypeName());
			} catch (Exception e) {
				WEBPersPlugin.getDefault().logError(e);
			}
		}
	}
}
