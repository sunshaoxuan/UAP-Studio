
package nc.uap.portal.skin.action;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.om.Skin;
import nc.uap.portal.om.SkinDescription;
import nc.uap.portal.om.Theme;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;
import nc.uap.portal.skin.dialog.SkinDialog;
import nc.uap.portal.theme.ThemeTypeTreeItem;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * ±‡º≠—˘ Ω Ù–‘
 * 
 * @author dingrf
 */
public class EditSkinAction extends Action {

	public EditSkinAction() {
		super(PortalProjConstants.EDIT_SKIN, PaletteImage.getCreateGridImgDescriptor());
	}
	
	@Override
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		if(view == null)
			return;
		Tree tree = view.getTreeView().getTree();
		TreeItem[] selTIs = tree.getSelection();
		TreeItem selTI = selTIs[0];

		SkinDialog skinDialog = new SkinDialog(new Shell(), PortalProjConstants.EDIT_SKIN);
		Skin skin = (Skin)selTI.getData();
		skinDialog.create();
		
		skinDialog.getIdText().setText(skin.getId());
		skinDialog.getIdText().setEditable(false);
		skinDialog.getNameText().setText(skin.getName());
		skinDialog.getI18nnameText().setText(skin.getI18nname());
		skinDialog.getLangdirText().setText(skin.getLangdir());
		skinDialog.getIconText().setText(skin.getIcon());
		
		if(skinDialog.open() == IDialogConstants.OK_ID){
			
			skin.setName(skinDialog.getName());
			skin.setI18nname(skinDialog.getI18nname());
			skin.setLangdir(skinDialog.getLangdir());
			skin.setIcon(skinDialog.getIcon());
			selTI.setText(skin.getName()+"[" + skin.getId() + "]");
			TreeItem parentItem = selTI.getParentItem();
			String type = ((ThemeTypeTreeItem) parentItem).getType();
			String themeId = ((Theme) parentItem.getData()).getId();
			
			String projectPath = LFWPersTool.getProjectWithBcpPath();
			String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
			
//			SkinDescription skinDescription  = PortalConnector.getSkinDescription(projectPath, projectModuleName, type, themeId);
//			if (skinDescription == null){
//				skinDescription = new SkinDescription();
//			}
//			for (Skin s: skinDescription.getSkin()){
//				if(s.getId().equals(skin.getId())){
//					s.setName(skin.getName());
//					break;
//				}
//			}
//			PortalConnector.saveSkinDescription(projectPath, projectModuleName, type, themeId, skinDescription);
			PortalConnector.updateSkinDescription(projectPath, skin, 2);
		}
		else
			return;
	}

}
