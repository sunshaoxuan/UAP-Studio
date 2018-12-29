package nc.uap.lfw.perspective.model;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.editor.DataSetEditor;
import nc.uap.lfw.perspective.ref.RefDatasetContent;

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

/**
 * 从同一个ds中获得引用的ds
 * @author zhangxya
 *
 */
public class RefDSFromWidget extends DialogWithTitle {

	private boolean flag;
	private Table table = null;
	private TableViewer tv = null;
	private Dataset dataset = null;
	
	public RefDSFromWidget(Shell parentShell, String title, boolean flag) {
		super(parentShell, title);
		this.flag = flag;
		
	}
	
	public Dataset getSelectedDataset(){
		return dataset;
	}
	
	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}
	
	protected void okPressed() {
		TableItem[] items = table.getSelection();
		if(items != null && items.length > 0){
			TableItem item = items[0];
			Dataset ds = (Dataset)item.getData();
			setDataset(ds);
			if(item != null){
				super.okPressed();
			}
		}else{
			MessageDialog.openConfirm(this.getShell(), M_perspective.RefDSFromWidget_0, M_perspective.RefDSFromWidget_1);
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
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		layout.addColumnData(new ColumnWeightData(100));
		new TableColumn(table,SWT.NONE).setText(M_perspective.RefDSFromWidget_2);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		RefDatasetContent refDataset = new RefDatasetContent();
		tv.setContentProvider(refDataset);
		tv.setLabelProvider(refDataset);
		List<Dataset> datasetList = null;
		if(flag){
			datasetList = LFWPersTool.getAllDatasetInWidget();
		}else
			datasetList = LFWPersTool.getAllDsExRef();
		
		if(DataSetEditor.getActiveEditor()!=null){
			//过滤已引用的数据集
			List<RefDatasetElementObj> refDataList = DataSetEditor.getActiveEditor().getGraph().getRefds();
			List<String> ids = new ArrayList<String>();
			for(RefDatasetElementObj cell:refDataList){
				ids.add(cell.getDs().getId());
			}
			ids.add(DataSetEditor.getActiveEditor().getDsElementObj().getDs().getId());
			List<Dataset> filterList = new ArrayList<Dataset>();
			for(Dataset data:datasetList){
				if(ids.contains(data.getId()))
					continue;
				filterList.add(data);
			}
			tv.setInput(filterList);
//			List<Dataset> delList = new ArrayList<Dataset>();
//			for(Dataset dataset:datasetList){
//				if(ids.contains(dataset.getId()))
//					delList.add(dataset);						
//			}
////			datasetList.removeAll(delList);
//			for(Dataset deldata:delList)
//				datasetList.remove(datasetList.indexOf(deldata));
		}		
		else
			tv.setInput(datasetList);
		return container;
	}
}
	