
package nc.uap.portal.theme.action;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.om.LookAndFeel;
import nc.uap.portal.om.Theme;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;
import nc.uap.portal.theme.dialog.ThemeDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * ±‡º≠÷˜Ã‚ Ù–‘
 * 
 * @author dingrf
 */
public class EditThemeAction extends Action {

	public EditThemeAction() {
		super(PortalProjConstants.EDIT_THEME, PaletteImage.getCreateGridImgDescriptor());
	}
	
	@Override
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		if(view == null)
			return;
		Tree tree = view.getTreeView().getTree();
		TreeItem[] selTIs = tree.getSelection();
		TreeItem selTI = selTIs[0];

		ThemeDialog themeDialog = new ThemeDialog(new Shell(), PortalProjConstants.EDIT_THEME);
		Theme theme = (Theme)selTI.getData();
		themeDialog.create();
		
		themeDialog.getIdText().setText(theme.getId());
		themeDialog.getIdText().setEditable(false);
		themeDialog.getTitleText().setText(theme.getTitle());
		themeDialog.getI18nNameText().setText(theme.getI18nName());
		themeDialog.getThemeIdText().setText(theme.getLfwThemeId());
		
		if(themeDialog.open() == IDialogConstants.OK_ID){
			
			theme.setTitle(themeDialog.getTitle());
			theme.setI18nName(themeDialog.getI18nName());
			theme.setLfwThemeId(themeDialog.getThemeId());
			
			selTI.setText(theme.getTitle());

			
			String projectPath = LFWPersTool.getProjectWithBcpPath();
			String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
			
//			LookAndFeel lookAndFeel  = PortalConnector.getLookAndFeel(projectPath, projectModuleName);
//			if (lookAndFeel  == null){
//				lookAndFeel  = new LookAndFeel ();
//			}
//			int i = 0;
//			for (Theme t: lookAndFeel.getTheme()){
//				if(t.getId().equals(theme.getId())){
//					lookAndFeel.getTheme().set(i, theme);
//					break;
//				}
//				i++;
//			}
//			PortalConnector.saveLookAndFeelToXml(projectPath, projectModuleName, lookAndFeel);
			PortalConnector.updateLookAndFeelToXml(projectPath, theme, 2);
		}
		else
			return;
	}

}
