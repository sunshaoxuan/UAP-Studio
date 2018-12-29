
package nc.uap.portal.category.action;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.portal.category.CategoryTreeItem;
import nc.uap.portal.category.dialog.CategoryDialog;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.core.PortalDirtoryTreeItem;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;
import nc.uap.portal.perspective.PortalTreeBuilder;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 新建分类
 * @author dingrf
 */
public class NewCategoryAction extends Action {

	public NewCategoryAction() {
		super(PortalProjConstants.NEW_CATEGORY, PaletteImage.getCreateGridImgDescriptor());
	}
	
	@Override
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		if(view == null)
			return;
		CategoryDialog categoryDialog = new CategoryDialog(new Shell(), PortalProjConstants.NEW_CATEGORY);
		//打开新建对话框
		if(categoryDialog.open() == IDialogConstants.OK_ID){
			
			Tree tree = view.getTreeView().getTree();
			TreeItem[] selTIs = tree.getSelection();
			TreeItem selTI = selTIs[0];
			if (selTI.getItemCount() ==0){
				PortalTreeBuilder.getInstance().initCategory((PortalDirtoryTreeItem)selTI, LFWPersTool.getCurrentProjectModuleName());
			}
			TreeItem[] subItems= selTI.getItems();
			//校验id是否重复
			for (int i=0;i<subItems.length;i++){
				if(subItems[i].getData() instanceof PortletDisplayCategory){
					if (((PortletDisplayCategory)subItems[i].getData()).getId().equals(categoryDialog.getText())){
						MessageDialog.openError(new Shell(), M_portal.NewCategoryAction_0, M_portal.NewCategoryAction_1+ categoryDialog.getText() +M_portal.NewCategoryAction_2);
						return;
					}
				}
			}
			try {
				//保存
				CategoryTreeItem cti = (CategoryTreeItem)view.addCategoryTreeNode(categoryDialog.getId(),
						categoryDialog.getText(),categoryDialog.getI18nName());

				String projectPath = LFWPersTool.getProjectWithBcpPath();
				String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
				Display  display = PortalConnector.getDisplay(projectPath, projectModuleName);
				if (display == null){
					display = new Display();
				}
				display.addPortletDisplayList((PortletDisplayCategory)cti.getData());
				PortalConnector.saveDisplayToXml(projectPath, projectModuleName, display);
				
			} catch (Exception e) {
				String title =PortalProjConstants.NEW_CATEGORY;
				String message = e.getMessage();
				MessageDialog.openError(new Shell(), title, message);
			}
		}
		else
			return;
	}

}
