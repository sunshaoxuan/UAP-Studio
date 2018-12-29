package nc.uap.lfw.perspective.action;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.RefreshNodeAction;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.uimodel.ViewConfig;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.listener.FileUtil;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

/**
 * 修改widget名称
 * @author guoweic
 *
 */
public class RenameWidgetAction extends NodeAction{
	
	public RenameWidgetAction() {
		setText(WEBPersConstants.RENAME_VIEW);
		setToolTipText(WEBPersConstants.RENAME_VIEW);
	} 
	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		Tree tree = LFWPersTool.getTree();
		LfwWindow pm = LFWPersTool.getCurrentPageMeta();
		if(view == null)
			return;
		LfwView widget = LFWPersTool.getCurrentWidget();
		String oldName = widget.getCaption();
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, M_perspective.RenameWidgetAction_0, M_perspective.RenameWidgetAction_1 + LFWTool.getViewText(null) + M_perspective.RenameWidgetAction_2, oldName, null);
		if(input.open() == InputDialog.OK){
			String newName = input.getValue();
			if(newName != null && newName.trim().length()>0){
				newName =newName.trim();
				try {
//					String folderPath = LFWPersTool.getCurrentFolderPath().substring(0, LFWPersTool.getCurrentFolderPath().lastIndexOf(oldName) - 1);
//					FileUtil.renameFile(folderPath, oldName, newName);
					
					widget.setCaption(newName);
//					widget.setRefId(newName);
					LFWSaveElementTool.updateView(widget);
					// 保存该Widget到pagemeta.pm文件中
//					pm.getWidget(oldName).setId(newName);
//					ViewConfig[] configs = pm.getViewConfigs();
//					for (ViewConfig widgetConf : configs) {
//						if (widgetConf.getId().equals(oldName)) {
//							widgetConf.setId(newName);
//							widgetConf.setRefId(newName);
//						}
//					}
//					savePagemeta(pm, folderPath);
					
					// 刷新节点
					RefreshNodeAction.refreshNode(view, tree);
					
					//TODO 刷新打开的Pagemeta编辑器页面
//					PagemetaEditor.refreshPagemetaEditor();
					
				} catch (Exception e) {
					String title =WEBPersConstants.RENAME_VIEW;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
		}
	}

	/**
	 * 保存Pagemeta到文件中
	 * @param widget
	 */
	public void savePagemeta(LfwWindow pagemeta, String folderPath) {
		String projectPath = LFWPersTool.getProjectPath();
		// 保存Widget到pagemeta.pm中
		LFWConnector.savePagemetaToXml(folderPath, "pagemeta.pm", projectPath, pagemeta); //$NON-NLS-1$
	}
}




