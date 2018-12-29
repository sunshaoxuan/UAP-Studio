package nc.uap.lfw.refnoderel;

import java.util.Arrays;
import java.util.List;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.lang.M_refnoderel;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class DsFieldSelDialog extends DialogWithTitle{
	
	private List<Field> fieldsList;
	public List<Field> getFieldsList() {
		return fieldsList;
	}
	public void setFieldsList(List<Field> fieldsList) {
		this.fieldsList = fieldsList;
	}
	
	private Field field;

	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}

	private Dataset ds;
	private Table table = null;
	private TableViewer tv = null;
	//private  CheckboxTableViewer ctv;

	public DsFieldSelDialog(Shell parentShell, String title, Dataset ds) {
		super(parentShell, title);
		this.ds = ds;
	}
	protected void okPressed() {
		//Object[] object = ctv.getCheckedElements();
		TableItem[] treeItem = table.getSelection();
		if(treeItem != null && treeItem.length > 0){
			Field field = (Field) treeItem[0].getData();
			setField(field);
//			if(fieldsList == null)
//				fieldsList = new ArrayList<Field>();
//			for (int i = 0; i < object.length; i++) {
//				Object obj = object[i];
//				if(obj instanceof Field){
//					Field fi = (Field) obj;
//					fieldsList.add(fi);
//				}
//			}
//			setFieldsList(fieldsList);
			super.okPressed();
		}else{
			MessageDialog.openConfirm(this.getShell(), M_refnoderel.DsFieldSelDialog_0, M_refnoderel.DsFieldSelDialog_1);
			table.setFocus();
		}
	}
	
	protected Point getInitialSize() {
		return new Point(350,500); 
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent , SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		tv = new TableViewer(container, SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		table = tv.getTable();
		//ctv =  new CheckboxTableViewer(table);;
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		layout.addColumnData(new ColumnWeightData(100));
		new TableColumn(table,SWT.NONE).setText(M_refnoderel.DsFieldSelDialog_2);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		DsFieldSelProvider dsFieldProvider = new DsFieldSelProvider();
		tv.setContentProvider(dsFieldProvider);
		tv.setLabelProvider(dsFieldProvider);
		List<Field> fieldsList = null;
		Field[] fields = ds.getFieldSet().getFields();
		if(fields != null)
			fieldsList = Arrays.asList(fields);
		tv.setInput(fieldsList);
		return container;
	}
}