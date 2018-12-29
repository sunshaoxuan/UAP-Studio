package nc.uap.lfw.dataset;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.editor.DataSetEditor;
import nc.uap.lfw.perspective.model.DatasetElementObj;
import nc.uap.lfw.perspective.views.CellPropertiesView;
import nc.uap.lfw.perspective.views.LFWViewPage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;

/**
 * 导入属性action
 * @author zhangxya
 *
 */
public class ImportDsFromDBAction extends Action {

	private DatasetElementObj dsobj;
	public ImportDsFromDBAction(DatasetElementObj dsobj) {
		super("导入属性",PaletteImage.getCreateDsImgDescriptor());
		this.dsobj = dsobj;
	}

	public void run() {
		DataSetEditor editor = DataSetEditor.getActiveEditor();
		if (editor != null) {
			Dataset ds = dsobj.getDs();
			if (ds != null) {
				LFWViewPage lfwviewpage = LFWPersTool.getLFWViewPage();
				CellPropertiesView view = ((CellPropertiesView) lfwviewpage.getCellPropertiesView());
				WizardDialog wd = new WizardDialog(editor.getSite().getShell(), new ImportDsAttrWizard(ds, view)) {
						protected void nextPressed() {
						IWizardPage page = getCurrentPage().getNextPage();
						if (page instanceof SelectTableColumnPage) {
							((SelectTableColumnPage)page).initTableListData();
						}
						super.nextPressed();
					}

				};
				String title = "为" + ds.getId() + "导入字段对话框";
				wd.setTitle(title);
				wd.setPageSize(-1, 300);
				wd.open();
			} else {
				MessageDialog.openConfirm(editor.getSite().getShell(), "提示", "请选中一个数据集!");
			}

		}
	}

}
