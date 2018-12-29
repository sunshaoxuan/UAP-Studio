package nc.uap.lfw.dataset;

import java.util.ArrayList;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.perspective.editor.DataSetEditor;
import nc.uap.lfw.perspective.views.CellPropertiesView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

/**
 * 导入属性
 * @author zhangxya
 *
 */
public class ImportDsAttrWizard extends Wizard {
	private Dataset ds  = null;
	private CellPropertiesView view = null;
	private class ImportAttrCommand extends Command{
		private Field[] attrs = null;
		private Field keyAttr = null;
		private Field oldKeyAttr = null;
		public ImportAttrCommand(Field[] attrs, Field keyAttr) {
			super("导入属性");
			this.attrs = attrs;
			this.keyAttr = keyAttr;
		}

		public void execute() {
			redo();
		}

		public void redo() {
			if(ds.getFieldSet().getFields() != null)
				ds.setFieldSet(new FieldSet());
			int count = attrs == null ? 0 : attrs.length;
			for (int i = 0; i < count; i++) {
				ds.getFieldSet().addField(attrs[i]);
			}
			view.getTv().refresh();
		}

		public void undo() {
			int count = attrs == null ? 0 : attrs.length;
			for (int i = 0; i < count; i++) {
				ds.getFieldSet().removeField(attrs[i]);
			}
			view.getTv().refresh();
		}
		
	}
	private SelectImportSourcePage selImportSourcePage = new SelectImportSourcePage(SelectImportSourcePage.class.getCanonicalName(), "选择导入源", null);

	private SelectPDMFilePathPage selPdmFilePathPage = new SelectPDMFilePathPage(SelectPDMFilePathPage.class.getCanonicalName(), "选择pdm（xml）文件", null);

	private SelectDBTablePage selDBTableWizardPage = new SelectDBTablePage(SelectDBTablePage.class.getCanonicalName(), "选择数据库表", null);
	
	private SelectTableColumnPage selTableColumnsPage = new SelectTableColumnPage(SelectTableColumnPage.class.getCanonicalName(), "选择表和导入的字段", null);
	
	
	public ImportDsAttrWizard(Dataset ds, CellPropertiesView view) {
		super();
		this.ds = ds;
		this.view = view;
	}

	@Override
	public boolean performFinish() {
		IWizardPage currPage = this.getContainer().getCurrentPage();
		if (currPage instanceof SelectTableColumnPage) {
			SelectTableColumnPage page = (SelectTableColumnPage)currPage;
			DBField[] fields = page.getSelectedFields();
			ArrayList<Field> al = new ArrayList<Field>();
			Field keyAttr = null;
			for (int i = 0; i < fields.length; i++) {
				DBField field = fields[i];
				Field dsField = new Field();
				dsField.setId(field.getName());
				dsField.setField(field.getName());
				dsField.setText(field.getDisplayName());
				if(field.getModuleType() != null){
					if(field.getModuleType().getName().equals("UFID"))
						dsField.setDataType("String");
					else if(field.getModuleType().getName().equals("UFDateTime"))
						dsField.setDataType("UFTime");
					else if(field.getModuleType().getName().equals("CUSTOM")){
						dsField.setDataType("SelfDefine");
					}
					else if(field.getModuleType().getName().equals("BLOB") || field.getModuleType().getName().equals("CLOB") || 
							field.getModuleType().getName().equals("IMAGE") || field.getModuleType().getName().equals("Text"))
						dsField.setDataType("Object");
					else
						dsField.setDataType(field.getModuleType().getName());
				}
				else{
					if(field.getType() != null){
						if(field.getType().indexOf("smallint") != -1)
							dsField.setDataType("Integer");
					}
				}
				dsField.setPrecision(field.getPrecision());
				dsField.setPrimaryKey(field.isKey());
				al.add(dsField);
			}
			if(al.size() > 0){
				ImportAttrCommand cmd = new ImportAttrCommand(al.toArray(new Field[0]), keyAttr);
				if(DataSetEditor.getActiveEditor() != null)
					DataSetEditor.getActiveEditor().executComand(cmd);
			}
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void addPages() {
		addPage(selImportSourcePage);
		addPage(selPdmFilePathPage);
		addPage(selDBTableWizardPage);
		addPage(selTableColumnsPage);
	}

	@Override
	public boolean canFinish() {
		IWizardPage currPage = this.getContainer().getCurrentPage();
		if (currPage instanceof SelectTableColumnPage) {
			return true;
		}

		return false;
	}

}
