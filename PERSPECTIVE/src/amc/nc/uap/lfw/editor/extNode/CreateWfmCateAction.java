package nc.uap.lfw.editor.extNode;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.perspective.webcomponent.LFWBusinessCompnentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWfmCateTreeItem;
import nc.uap.wfm.vo.WfmFlwCatVO;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;



public class CreateWfmCateAction extends NodeAction{

	public CreateWfmCateAction(){
		super(WEBPersConstants.NEW_WFMCATE);
	}
	public void run() {
		createWfmCateNode();
	}

	private void createWfmCateNode() {

		WfmFlwCateDialog wfmDialog = new WfmFlwCateDialog(
				WEBPersConstants.NEW_WFMCATE);

		if (wfmDialog.open() == IDialogConstants.OK_ID) {
			try {
				IProject project = LFWPersTool.getCurrentProject();
				WfmFlwCatVO flwCate = new WfmFlwCatVO();
				flwCate.setCatname(wfmDialog.getTypeName());
				flwCate.setCatcode(wfmDialog.getTypeCode());
				flwCate.setServerclass(null);
				String module = LfwCommonTool.getProjectModuleName(project);
				flwCate.setDevmodule(module);
				LFWBusinessCompnentTreeItem treeItem = LFWAMCPersTool.getCurrentBusiCompTreeItem();
				String bcp = null;
				if(treeItem!=null){
					String path = LFWAMCPersTool.getProjectPath();
//					String title = treeItem.getText();
//					bcp = title.substring(title.lastIndexOf("[")+1,title.length()-1);
					bcp = path.substring(path.lastIndexOf("/")+1,path.length());
					flwCate.setDevcomponent(bcp);
				}
				
				WfmFlwCatVO[] allcates = LFWWfmConnector.getWfmFlowCateQry();
				for(WfmFlwCatVO cate:allcates){
					if(cate.getCatname().equals(wfmDialog.getTypeName())){
						MessageDialog.openError(null, "Error", "Name Exsited");
						return;
					}
					if(cate.getCatcode().equals(wfmDialog.getTypeCode())){
						MessageDialog.openError(null, "Error", "Code Exsited");
						return;
					}
				}
				LFWWfmConnector.insertFlwCate(flwCate);
				
				WfmFlwCatVO[] cates = LFWWfmConnector.getWfmFlowCateByModule(module, bcp);
				String flowPk = null;
				if(cates!=null){
					for(WfmFlwCatVO cate:cates){
						if(cate.getCatname().equals(wfmDialog.getTypeName())){
							flowPk = cate.getPk_flwcat();
						}
					}
				}
				LFWDirtoryTreeItem parentItem = (LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem();
				LFWWfmCateTreeItem cateItem = new LFWWfmCateTreeItem(parentItem, parentItem.getFile(), flowPk, flwCate.getCatname());
				cateItem.setType(LFWDirtoryTreeItem.WFM_FLWCATE);
//				flwCate.setDevcomponent(newDevcomponent)
//				NCConnector.saveFlwType(flwType);
//				
//				LFWWfmFlwTypeTreeItem typeItem = new LFWWfmFlwTypeTreeItem(
//						cateItem, cateItem.getFile(), flwTypeNew.getTypename());
//				typeItem.setDef_pk(Pk_prodef);
//				typeItem.setType_pk(flwTypeNew.getPk_flwtype());
//				typeItem.setCate_Pk(flwTypeNew.getPk_flwcat());
				
//				String Pk_prodef = WfmServiceFacility.getProDefBill()
//						.insertProdef(proDef);
			} catch (Exception e) {
				WEBPersPlugin.getDefault().logError(e);
			}

		}
	}
}
