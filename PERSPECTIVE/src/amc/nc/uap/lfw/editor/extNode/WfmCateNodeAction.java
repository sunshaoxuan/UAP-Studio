package nc.uap.lfw.editor.extNode;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.aciton.NCConnector;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.perspective.webcomponent.LFWWfmCateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWfmFlwTypeTreeItem;
import nc.uap.wfm.vo.WfmFlwTypeVO;
import nc.uap.wfm.vo.WfmProdefVO;
import nc.vo.pub.lang.UFBoolean;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 * @author qinjianc
 * 
 */
public class WfmCateNodeAction extends NodeAction {

	public WfmCateNodeAction() {
		super(WEBPersConstants.NEW_WFMTYPE);
	}

	public void run() {
		createWfmCateNode();
	}

	private void createWfmCateNode() {
		TreeItem item = LFWPersTool.getCurrentTreeItem();
		LFWWfmCateTreeItem cateItem = (LFWWfmCateTreeItem) item;

		WfmFlwTypeDialog wfmDialog = new WfmFlwTypeDialog(
				WEBPersConstants.NEW_WFMTYPE);

		if (wfmDialog.open() == IDialogConstants.OK_ID) {
			try {

				WfmFlwTypeVO flwType = new WfmFlwTypeVO();
				flwType.setTypename(wfmDialog.getTypeName());
				flwType.setTypecode(wfmDialog.getTypeCode());
				String groupPk = LFWWfmConnector.getGroupPk();
				flwType.setPk_group(groupPk);
				flwType.setPk_org(groupPk);
				flwType.setServerclass(wfmDialog.getClazzName());
				flwType.setPk_flwcat(cateItem.getPk());
				NCConnector.saveFlwType(flwType);

				WfmFlwTypeVO[] types = NCConnector.getFlwType(cateItem.getPk());
				WfmFlwTypeVO flwTypeNew = types[types.length-1];
				
				WfmProdefVO proDef = new WfmProdefVO();
				proDef.setId("DefId");
				proDef.setName("wfmProDef");
				proDef.setDr(Integer.valueOf(0));
				String PK = LFWWfmConnector.generatePK();
				proDef.setPk_original(PK);
				proDef.setPk_group(groupPk);
				proDef.setPk_org(groupPk);
				proDef.setEnginename("1");
				proDef.setPk_prodef(PK);
				proDef.setVersion("1.0");
				proDef.setFlwtype(flwTypeNew.getPk_flwtype());
				proDef.setIsnotstartup(UFBoolean.TRUE);
				String Pk_prodef = LFWWfmConnector.insertProdef(proDef);
				
				LFWWfmFlwTypeTreeItem typeItem = new LFWWfmFlwTypeTreeItem(
						cateItem, cateItem.getFile(), flwTypeNew.getTypename());
//				typeItem.setDef_pk(Pk_prodef);
				typeItem.setType_pk(flwTypeNew.getPk_flwtype());
				typeItem.setCate_Pk(flwTypeNew.getPk_flwcat());
				typeItem.setDef_pk(PK);
				
//				String Pk_prodef = WfmServiceFacility.getProDefBill()
//						.insertProdef(proDef);
			} catch (Exception e) {
				WEBPersPlugin.getDefault().logError(e);
			}

		}
	}
}
