package nc.uap.lfw.dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.ref.DsFieldContentProvider;

public class ChooseFrDialog extends DialogWithTitle{
	
	private TableViewer tvright = null;
	
	private CheckboxTableViewer ctv = null;
	
	private Table tableright = null;
	
	private MdDataset ds = null;
	
	private List<FieldRelation> selectFr = null;
	
	
	
	public MdDataset getDs() {
		return ds;
	}


	public void setDs(MdDataset ds) {
		this.ds = ds;
	}


	public ChooseFrDialog(Shell parentShell, String title){
		super(parentShell, title);
	}
	
	
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1,false));
		container.setLayoutData(new GridData(600,200));
		tvright = new TableViewer(container, SWT.CHECK|SWT.BORDER|SWT.MULTI|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		ctv = new CheckboxTableViewer(tvright.getTable());
		tableright = tvright.getTable();
		tableright.setLinesVisible(true);
		tableright.setHeaderVisible(true);
		TableLayout layoutright = new TableLayout();
		tableright.setLayout(layoutright);
		
		//ID字段
		layoutright.addColumnData(new ColumnWeightData(200));
		new TableColumn(tableright,SWT.NONE).setText("ID"); //$NON-NLS-1$
		//readField字段
		layoutright.addColumnData(new ColumnWeightData(300));		
		new TableColumn(tableright, SWT.NONE).setText("读取字段");
		//writeField
		layoutright.addColumnData(new ColumnWeightData(300));		
		new TableColumn(tableright, SWT.NONE).setText("写入字段");
		//refDataset
		layoutright.addColumnData(new ColumnWeightData(300));		
		new TableColumn(tableright, SWT.NONE).setText("关联数据集");
		
		tableright.setLayoutData(new GridData(GridData.FILL_BOTH));
		tvright.setContentProvider(new FieldRelationContentProvider());
		tvright.setLabelProvider(new FieldRelationContentProvider());
		
		List<FieldRelation> fieldRelations = LFWConnector.getNCFieldRelations(ds);
		List<FieldRelation> rightinput = new ArrayList<FieldRelation>();
		for (int i = 0; i < fieldRelations.size(); i++) {
			FieldRelation fr = (FieldRelation) fieldRelations.get(i);
			rightinput.add(fr);
		}
		tvright.setInput(rightinput);
		
		CellEditor[] cellEditors = new CellEditor[4];
		cellEditors[0] = new TextCellEditor(tableright);
		cellEditors[1] = new TextCellEditor(tableright);
		cellEditors[2] = new TextCellEditor(tableright);
		cellEditors[3] = new TextCellEditor(tableright);
		tvright.setCellEditors(cellEditors);
		tvright.setColumnProperties(new String[]{"ID", "读取字段","写入字段","关联数据集"});
		ctv.setAllChecked(true);
		return container;
	}
	
	protected void okPressed() {
		Object[] itemsright = ctv.getCheckedElements();
		if(itemsright!=null&&itemsright.length > 0 ){
//			selectFr = new ArrayList<FieldRelation>();
			for(Object fr:itemsright)
				ds.getFieldRelations().addFieldRelation((FieldRelation)fr);
		}
		super.okPressed();
	}

}
