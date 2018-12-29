package nc.uap.lfw.dataset;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.core.page.LfwView;

public class ClearRefDialog extends DialogWithTitle{
	
	List<String> refDatasetList = new ArrayList<String>();
	
	List<String> refNodeList = new ArrayList<String>();
	
	List<String> comboList = new ArrayList<String>();
	
	LfwView widget = null;
	
	
	private CheckboxTableViewer ctv = null;
	
	private CheckboxTableViewer ctv2 = null;
	
	private CheckboxTableViewer ctv3 = null;
	

	public List<String> getRefDatasetList() {
		return refDatasetList;
	}

	public void setRefDatasetList(List<String> refDatasetList) {
		this.refDatasetList = refDatasetList;
	}

	public List<String> getRefNodeList() {
		return refNodeList;
	}

	public void setRefNodeList(List<String> refNodeList) {
		this.refNodeList = refNodeList;
	}

	public List<String> getComboList() {
		return comboList;
	}

	public void setComboList(List<String> comboList) {
		this.comboList = comboList;
	}
	
	

	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

	public ClearRefDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}
	
	protected Control createDialogArea(Composite parent){
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(3,true));
		container.setLayoutData(new GridData(600,200));
//		container.setLayoutData((new GridData(GridData.FILL_BOTH)));
		Group refdataGroup = new Group(container, SWT.NONE);
		refdataGroup.setText("冗余引用数据集");
		refdataGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		refdataGroup.setLayout(new GridLayout(1,true));
		TableViewer refdataView = new TableViewer(refdataGroup, SWT.CHECK|SWT.BORDER|SWT.MULTI|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Table refdataTable = refdataView.getTable();
		ctv = new CheckboxTableViewer(refdataTable);
		refdataTable.setLinesVisible(true);
		refdataTable.setHeaderVisible(true);
		TableLayout layoutright = new TableLayout();
		refdataTable.setLayout(layoutright);
		
		layoutright.addColumnData(new ColumnWeightData(300));
		new TableColumn(refdataTable,SWT.NONE).setText("ID"); 
		
		refdataTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		refdataView.setContentProvider(new ClearRefProvider());
		refdataView.setLabelProvider(new ClearRefProvider());
		refdataView.setInput(refDatasetList);
		ctv.setAllChecked(true);
//		CellEditor[] cellEditors = new CellEditor[1];
//		cellEditors[0] = new TextCellEditor(refdataTable);
//		refdataView.setCellEditors(cellEditors);
		
		Group refnodeGroup = new Group(container, SWT.NONE);
		refnodeGroup.setText("冗余参照");
		refnodeGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		refnodeGroup.setLayout(new GridLayout(1,true));
		TableViewer refnodeView = new TableViewer(refnodeGroup, SWT.CHECK|SWT.BORDER|SWT.MULTI|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Table refnodeTable = refnodeView.getTable();
		ctv2 = new CheckboxTableViewer(refnodeTable);
		refnodeTable.setLinesVisible(true);
		refnodeTable.setHeaderVisible(true);
		TableLayout layoutright2 = new TableLayout();
		refnodeTable.setLayout(layoutright2);
		
		layoutright2.addColumnData(new ColumnWeightData(300));
		new TableColumn(refnodeTable,SWT.NONE).setText("ID"); 
		
		refnodeTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		refnodeView.setContentProvider(new ClearRefProvider());
		refnodeView.setLabelProvider(new ClearRefProvider());
		refnodeView.setInput(refNodeList);
		ctv2.setAllChecked(true);
		
		Group comboGroup = new Group(container, SWT.NONE);
		comboGroup.setText("冗余下拉数据");
		comboGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		comboGroup.setLayout(new GridLayout(1,true));
		TableViewer comboView = new TableViewer(comboGroup, SWT.CHECK|SWT.BORDER|SWT.MULTI|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Table comboTable = comboView.getTable();
		ctv3 = new CheckboxTableViewer(comboTable);
		comboTable.setLinesVisible(true);
		comboTable.setHeaderVisible(true);
		TableLayout layoutright3 = new TableLayout();
		comboTable.setLayout(layoutright3);
		
		layoutright3.addColumnData(new ColumnWeightData(300));
		new TableColumn(comboTable,SWT.NONE).setText("ID"); 
		
		comboTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		comboView.setContentProvider(new ClearRefProvider());
		comboView.setLabelProvider(new ClearRefProvider());
		comboView.setInput(comboList);
		ctv3.setAllChecked(true);
		
		
		
		return container;
				
	}
	
	protected void okPressed() {
		Object[] items = ctv.getCheckedElements();
		Object[] items2 = ctv2.getCheckedElements();
		Object[] items3 = ctv3.getCheckedElements();
		if(items!=null){
			for(Object obj1:items){
				widget.getViewModels().removeDataset((String)obj1);
			}
		}
		if(items2!=null){
			for(Object obj2:items2){
				widget.getViewModels().removeRefNode((String)obj2);
			}
		}
		if(items3!=null){
			for(Object obj3:items3){
				widget.getViewModels().removeComboData((String)obj3);
			}
		}
		super.okPressed();
	}

}
