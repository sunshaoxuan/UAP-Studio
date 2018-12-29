
package nc.uap.portal.category.action;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.portal.category.dialog.CategoryDialog;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.perspective.PortalExplorerTreeView;
import nc.uap.portal.perspective.PortalProjConstants;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 编辑分类
 * @author dingrf
 */
public class EditCategoryAction extends Action {

	public EditCategoryAction() {
		super(PortalProjConstants.EDIT_CATEGORY, PaletteImage.getCreateGridImgDescriptor());
	}
	
	@Override
	public void run() {
		PortalExplorerTreeView view = PortalExplorerTreeView.getPortalExploerTreeView(null);
		if(view == null)
			return;
		Tree tree = view.getTreeView().getTree();
		TreeItem[] selTIs = tree.getSelection();
		TreeItem selTI = selTIs[0];

		CategoryDialog categoryDialog = new CategoryDialog(new Shell(), PortalProjConstants.EDIT_CATEGORY);
		PortletDisplayCategory p = (PortletDisplayCategory)selTI.getData();
		categoryDialog.create();
		categoryDialog.getIdText().setText(p.getId());
		categoryDialog.getTextText().setText(p.getText());
		categoryDialog.getI18nNameText().setText(p.getI18nName());
		
		//打开分类属性对话框
		if(categoryDialog.open() == IDialogConstants.OK_ID){
			TreeItem[] subItems= selTI.getParent().getItems();
			//校验id是否重复
			for (int i=0;i<subItems.length;i++){
				if(subItems[i].getData() instanceof PortletDisplayCategory  && !selTI.equals(subItems[i])){
					if (((PortletDisplayCategory)subItems[i].getData()).getId().equals(categoryDialog.getText())){
						MessageDialog.openError(new Shell(), M_portal.EditCategoryAction_0, M_portal.EditCategoryAction_1+ categoryDialog.getText() +M_portal.EditCategoryAction_2);
						return;
					}
				}
			}
			try {
				//保存portlet分类信息
				String projectPath = LFWPersTool.getProjectWithBcpPath();
				String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
				Display  display = PortalConnector.getDisplay(projectPath, projectModuleName);
				for (PortletDisplayCategory pdc : display.getCategory()){
					if (pdc.getId().equals(p.getId())){
						pdc.setId(categoryDialog.getId());
						pdc.setText(categoryDialog.getText());
						pdc.setI18nName(categoryDialog.getI18nName());
						p.setId(categoryDialog.getId());
						p.setText(categoryDialog.getText());
						p.setI18nName(categoryDialog.getI18nName());
						selTI.setText(p.getText());
						break;
					}
				}
				PortalConnector.saveDisplayToXml(projectPath, projectModuleName, display);
				
			} catch (Exception e) {
				String title =PortalProjConstants.EDIT_CATEGORY;
				String message = e.getMessage();
				MessageDialog.openError(new Shell(), title, message);
			}
		}
		else
			return;
	}

}
