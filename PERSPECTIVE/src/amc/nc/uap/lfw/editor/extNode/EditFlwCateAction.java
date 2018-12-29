package nc.uap.lfw.editor.extNode;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.perspective.webcomponent.LFWWfmCateTreeItem;
import nc.uap.wfm.vo.WfmFlwCatVO;

import org.eclipse.jface.dialogs.IDialogConstants;

public class EditFlwCateAction extends NodeAction{
	public EditFlwCateAction() {
		super(WEBPersConstants.EDIT_WFMCATE);
	}

	public void run() {
		editFlwCate();
	}
	public void editFlwCate(){
		LFWWfmCateTreeItem selTI = (LFWWfmCateTreeItem)LFWPersTool.getCurrentTreeItem();
		WfmFlwCatVO[] flwCates = LFWWfmConnector.getWfmFlowCateQry();
		WfmFlwCatVO flwCatVO = null;
		for(WfmFlwCatVO flwCate:flwCates){
			if(flwCate.getPk_flwcat().equals(selTI.getPk())) flwCatVO = flwCate;
		}
			
		
		WfmFlwCateDialog wfmDialog = new WfmFlwCateDialog(
				WEBPersConstants.EDIT_WFMCATE, flwCatVO);

		if (wfmDialog.open() == IDialogConstants.OK_ID) {
			try {
				
				flwCatVO.setCatname(wfmDialog.getTypeName());
				flwCatVO.setCatcode(wfmDialog.getTypeCode());
				LFWWfmConnector.editFlwCate(flwCatVO);
				selTI.setText(wfmDialog.getTypeName());
			} catch (Exception e) {
				WEBPersPlugin.getDefault().logError(e);
			}
		}
	
	}
}
