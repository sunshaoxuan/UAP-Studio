package nc.uap.lfw.perspective.listener;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.LfwParameter;
import nc.uap.lfw.lang.M_listenr;
import nc.uap.lfw.perspective.editor.TableViewContentProvider;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * DatasetChangeEvent的参数选择对话框内容
 * 
 * @author guoweic
 *
 */
public class DataChangeParamPanel extends Canvas {
	
	private Dataset dataset = null;
	private LfwParameter oldExtendParameter;
	private LfwParameter extendParameter;
	private String selectedFields = ""; //$NON-NLS-1$
	
	private CheckboxTableViewer ctv;
	
	class CheckStateListener implements ICheckStateListener {

		public void checkStateChanged(CheckStateChangedEvent event) {
			refreshSelectedFields();
		}
		
	}
	
	public DataChangeParamPanel(Composite parent, int style, Dataset dataset, LfwParameter extendParameter) {
		super(parent, style);
		this.dataset = dataset;
		this.oldExtendParameter = extendParameter;
		initUI();
	}
	
	private void initUI() {
		this.extendParameter = (LfwParameter) oldExtendParameter.clone();
		// 总体布局
		GridLayout mainLayout = new GridLayout(4, false);
		this.setLayout(mainLayout);
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		// 原有选中列名称数组
		String[] oldFields = oldExtendParameter.getDesc().split(","); //$NON-NLS-1$
		List<Field> oldFieldList = new ArrayList<Field>();
		// 所有列数据
		Field[] fields = dataset.getFieldSet().getFields();
		List<Field> fieldList = new ArrayList<Field>();
		for (Field field : fields) {
			fieldList.add(field);
		}
		
		ViewForm vf = new ViewForm(this, SWT.NONE);
		vf.setLayout(new GridLayout(1, false));
		vf.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		TableViewer tv = new TableViewer(vf, SWT.CHECK | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		ctv = new CheckboxTableViewer(tv.getTable());
		Table table = tv.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		createColumn(table, layout, 15, SWT.NONE, M_listenr.DataChangeParamPanel_0);
		createColumn(table, layout, 60, SWT.NONE, M_listenr.DataChangeParamPanel_1);
		createColumn(table, layout, 60, SWT.NONE, M_listenr.DataChangeParamPanel_2);
		
		tv.setContentProvider(new TableViewContentProvider());
		tv.setLabelProvider(new DataChangeParamViewProvider());
		
		tv.setInput(fieldList);
		
		for (String fieldId : oldFields) {
			if (null != fields) {
				for (Field field : fields) {
					if (field.getId().equals(fieldId)) {
						oldFieldList.add(field);
					}
				}
			}
			
		}
		ctv.setCheckedElements(oldFieldList.toArray());
		
		ctv.addCheckStateListener(new CheckStateListener());

		vf.setContent(tv.getControl());
		
		this.getData();
		
	}

	public LfwParameter getExtendParameter() {
		extendParameter.setDesc(selectedFields);
		return extendParameter;
	}
	
	/**
	 * 刷新选中的列参数内容
	 * @return
	 */
	private String refreshSelectedFields() {
		selectedFields = ""; //$NON-NLS-1$
		Object[] checkedElementArray = ctv.getCheckedElements();
		for (Object field : checkedElementArray) {
			if (field instanceof Field)
				selectedFields += ((Field)field).getId() + ","; //$NON-NLS-1$
		}
		if (selectedFields.length() > 0)
			selectedFields = selectedFields.substring(0, selectedFields.length() - 1);
		return selectedFields;
	}
	
	private void createColumn(Table table, TableLayout layout, int width,
			int align, String text) {
		layout.addColumnData(new ColumnWeightData(width));
		new TableColumn(table, align).setText(text);
	}

	public CheckboxTableViewer getCtv() {
		return ctv;
	}

}
