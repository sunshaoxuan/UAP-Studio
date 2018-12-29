package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.aciton.NCConnector;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.dataset.NewPoolDsAction;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.extNode.DelFlwCateAction;
import nc.uap.lfw.editor.extNode.EditFlwCateAction;
import nc.uap.lfw.editor.extNode.WfmCateNodeAction;
import nc.uap.wfm.vo.WfmFlwTypeVO;
import nc.uap.wfm.vo.WfmProdefVO;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

public class LFWWfmCateTreeItem extends LFWDirtoryTreeItem {

	String cateName = null;
	String pk = null;

	public LFWWfmCateTreeItem(TreeItem parentItem, File file, String flowPk,
			String text) {
		super(parentItem, file, text);
		this.cateName = text;
		this.pk = flowPk;
		setImage(getDirImage());
	}

	protected Image getDirImage() {
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(
//				WEBProjPlugin.ICONS_PATH, "groups.gif");
		return ImageProvider.groups;
	}

	public void addMenuListener(IMenuManager manager) {
		String type = this.getType();
		if (type.equals(LFWDirtoryTreeItem.WFM_FLWCATE)) {
			WfmCateNodeAction newWfmTypeAction = new WfmCateNodeAction();
			manager.add(newWfmTypeAction);
			EditFlwCateAction editFlwCateAction = new EditFlwCateAction();
			manager.add(editFlwCateAction);
			DelFlwCateAction delFlwCateAction = new DelFlwCateAction();
			manager.add(delFlwCateAction);
		}
	}
	public void deleteNode() {
		LFWAMCPersTool.refreshCurrentPorject();
		dispose();
	}

	public void mouseDoubleClick() {
		if (getItemCount() == 0) {
			WfmFlwTypeVO[] flwTypes = NCConnector.getFlwType(pk);
			if(flwTypes!=null&&flwTypes.length>0){
				for (WfmFlwTypeVO flwType : flwTypes) {
					if(flwType.getPk_parent()!=null && !"".equals(flwType.getPk_parent())) continue;
					LFWWfmFlwTypeTreeItem typeItem = new LFWWfmFlwTypeTreeItem(
							this, this.getFile(), flwType.getTypename());
					WfmProdefVO[] vos = LFWWfmConnector.getProDef(flwType.getPk_flwtype());
					if(vos!=null&&vos.length>0){
						WfmProdefVO vo = null;
						boolean flag = false;
						for(WfmProdefVO def:vos){
							if(def.getIsnotstartup().booleanValue()){
								vo = def;
								flag = true;
								break;
							}
						}
						if(!flag)
							vo = vos[0];
						typeItem.setDef_pk(vo.getPk_prodef());
					}
					typeItem.setType_pk(flwType.getPk_flwtype());
					typeItem.setCate_Pk(pk);
				}
			}
		}
	}

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	

}
