package nc.uap.portal.theme.action;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.core.PortalDirtoryTreeItem;
import nc.uap.portal.om.LookAndFeel;
import nc.uap.portal.om.Theme;
import nc.uap.portal.perspective.PortalProjConstants;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalTreeBuilder;
import nc.uap.portal.theme.ThemeTreeItem;
import nc.uap.portal.theme.ThemeTypeTreeItem;
import nc.uap.portal.theme.dialog.ThemeDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 新建主题
 * 
 * @author dingrf
 */
public class NewThemeAction extends Action {
	
	public NewThemeAction() {
		super(PortalProjConstants.NEW_THEME, PaletteImage.getCreateGridImgDescriptor());
	}

	
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		if(view == null)
			return;

		Tree tree = view.getTreeView().getTree();
		TreeItem[] selTIs = tree.getSelection();
		TreeItem selTI = selTIs[0];
		if (!(selTI instanceof PortalDirtoryTreeItem && ((PortalDirtoryTreeItem)selTI).getType().equals(PortalDirtoryTreeItem.THEMES))){
			MessageDialog.openError(new Shell(), "错误提示", "必须在主题节点下新建主题!");
			return;
		}
		
		ThemeDialog themeDialog = new ThemeDialog(new Shell(), PortalProjConstants.NEW_THEME);
		if(themeDialog.open() == IDialogConstants.OK_ID){
			
			if (selTI.getItemCount() ==0){
				PortalTreeBuilder.getInstance().initTheme((PortalDirtoryTreeItem)selTI, LFWPersTool.getCurrentProjectModuleName());
			}
			TreeItem[] subItems= selTI.getItems();
			for (int i=0;i<subItems.length;i++){
				if(subItems[i].getData() instanceof Theme){
					if (((Theme)subItems[i].getData()).getId().equals(themeDialog.getId())){
						MessageDialog.openError(new Shell(), "错误提示", "已经存在ID为"+ themeDialog.getId() +"的主题!");
						return;
					}
				}
			}
			try {
				Theme theme = new Theme();
				theme.setId(themeDialog.getId());
				theme.setTitle(themeDialog.getTitle());
				theme.setI18nName(themeDialog.getI18nName());
				theme.setLfwThemeId(themeDialog.getThemeId());
				
				ThemeTreeItem themeTreeItem =  (ThemeTreeItem)view.addThemeTreeNode(theme);
				/* 创建主题分类*/
				new ThemeTypeTreeItem(themeTreeItem,theme,PortalProjConstants.TYPE_LAYOUT);
				new ThemeTypeTreeItem(themeTreeItem,theme,PortalProjConstants.TYPE_PAGE);
				new ThemeTypeTreeItem(themeTreeItem,theme,PortalProjConstants.TYPE_PORTLET);
				

				String projectPath = LFWPersTool.getProjectWithBcpPath();
				String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
//				LookAndFeel  lookAndFeel = PortalConnector.getLookAndFeel(projectPath, projectModuleName);
//				if (lookAndFeel == null){
//					lookAndFeel = new LookAndFeel();
//				}
//				lookAndFeel.addTheme(theme);
				PortalConnector.createThemeFolder(projectPath, projectModuleName, theme);
				PortalConnector.updateLookAndFeelToXml(projectPath, theme, 1);
//				PortalConnector.saveLookAndFeelToXml(projectPath, projectModuleName, lookAndFeel);
				
			} catch (Exception e) {
				String title =PortalProjConstants.NEW_THEME;
				String message = e.getMessage();
				MessageDialog.openError(new Shell(), title, message);
			}
		}
		else
			return;
	}
}
