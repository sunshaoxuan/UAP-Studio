
package nc.uap.portal.page.action;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.om.Page;
import nc.uap.portal.page.dialog.PageDialog;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * ±‡º≠page Ù–‘
 * 
 * @author dingrf
 */
public class EditPageAction extends Action {

	public EditPageAction() {
		super(PortalProjConstants.EDIT_PAGE, PaletteImage.getCreateGridImgDescriptor());
	}
	
	@Override
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		if(view == null)
			return;
		Tree tree = view.getTreeView().getTree();
		TreeItem[] selTIs = tree.getSelection();
		TreeItem selTI = selTIs[0];

		PageDialog pageDialog = new PageDialog(new Shell(), PortalProjConstants.EDIT_PAGE);
		Page page = (Page)selTI.getData();
		pageDialog.create();
		
		pageDialog.getIdText().setText(page.getPagename());
		pageDialog.getIdText().setEditable(false);
		pageDialog.getTitleText().setText(page.getTitle());
		
		if(pageDialog.open() == IDialogConstants.OK_ID){
			
			page.setTitle(pageDialog.getTitle());
			selTI.setText(page.getTitle());

			String projectPath = LFWPersTool.getProjectWithBcpPath();
			String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
			
			PortalConnector.savePageToXml(projectPath, projectModuleName, page);
		}
		else
			return;
	}

}
