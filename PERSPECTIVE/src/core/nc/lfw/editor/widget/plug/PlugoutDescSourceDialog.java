package nc.lfw.editor.widget.plug;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.DialogWithTitle;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * plugout数据来源选择对话框
 * 
 * @author dingrf
 *
 */

public class PlugoutDescSourceDialog extends DialogWithTitle{
	
	public class PlugoutDescSource{
		/*来源类型*/
		private String sourceType;
		/*来源类型显示*/
		private String sourceName;
		
		public PlugoutDescSource(String sourceType, String sourceName){
			this.sourceType = sourceType;
			this.sourceName = sourceName;
		}
		
		public String getSourceType() {
			return sourceType;
		}
		public void setSourceType(String sourceType) {
			this.sourceType = sourceType;
		}

		public String getSourceName() {
			return sourceName;
		}

		public void setSourceName(String sourceName) {
			this.sourceName = sourceName;
		}
	}

	private TreeViewer tv = null;
	
	private List<PlugoutDescSource> plugoutDescSources = new ArrayList<PlugoutDescSource>();
	
	private PlugoutDescSource currentSource = null;
	
	protected Point getInitialSize() {
		return new Point(500,250); 
	}
	
	public PlugoutDescSourceDialog(Shell parentShell) {
		super(parentShell, "取数来源");
//		WidgetEditor editor = WidgetEditor.getActiveWidgetEditor();
//		LfwWidget widget =  editor.getGraph().getWidget();
//		for (Dataset  ds : widget.getViewModels().getDatasets()){
//			PlugoutDescSource ps = new PlugoutDescSource("Dataset", ds.getId());
//			plugoutDescSources.add(ps);
//		}
		buildAllTypes(plugoutDescSources);
	}

	
	private void buildAllTypes(List<PlugoutDescSource> plugoutDescSources) {
		plugoutDescSources.add(new PlugoutDescSource("Dataset.TYPE_DATASET_SEL_ROW", "数据集当前选中行"));
		plugoutDescSources.add(new PlugoutDescSource("Dataset.TYPE_DATASET_MUTL_SEL_ROW", "数据集所有选中行"));
		plugoutDescSources.add(new PlugoutDescSource("Dataset.TYPE_DATASET_ALL_ROW", "数据集所有行"));
		plugoutDescSources.add(new PlugoutDescSource("TYPE_COMPONENT_VALUE", "控件值"));
		
	}

	public TreeViewer getTv() {
		return tv;
	}

	public void setTv(TreeViewer tv) {
		this.tv = tv;
	}

	public String getResult(){
			return currentSource.getSourceType(); 
	}
	
	protected void okPressed() {
		if(currentSource == null){
			MessageDialog.openInformation(null, "提示", "请选择取数来源");
			return;
		}
		super.okPressed();
	}
	 
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		container.setLayoutData(gd);
		container.setLayout(new GridLayout(1, false));

		ViewForm vf = new ViewForm(container, SWT.NONE);
		vf.setLayoutData(gd);
		tv = new TreeViewer(vf,SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Tree tree = tv.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		createColumn(tree, PlugoutDescSourceCellModifier.colNames[0], 450, SWT.LEFT, 0);
//		createColumn(tree, PlugoutDescSourceCellModifier.colNames[1], 100, SWT.LEFT, 1);
		PlugoutDescSourceProvider provider = new PlugoutDescSourceProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(PlugoutDescSourceCellModifier.colNames);
		CellEditor[] cellEditors = new CellEditor[PlugoutDescSourceCellModifier.colNames.length];
		cellEditors[0] = new TextCellEditor(tree);
//		cellEditors[1] = new TextCellEditor(tree);
		tv.setCellEditors(cellEditors);
		tv.setCellModifier(new PlugoutDescSourceCellModifier(this));
		vf.setContent(tv.getControl());
		tree.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				setCurrentSource((PlugoutDescSource)e.item.getData());
			}}  );
		
		tv.setInput(this.plugoutDescSources);
		return container;
	}
	
	private TreeColumn createColumn(Tree tree, String colName , int width, int align, int index){
		TreeColumn col = new TreeColumn(tree, SWT.None, index);
		col.setText(colName);
		col.setWidth(width);
		col.setAlignment(align);
		return col;
	}

	public List<PlugoutDescSource> getPlugoutDescSources() {
		return plugoutDescSources;
	}

	public void setPlugoutDescSources(List<PlugoutDescSource> plugoutDescSources) {
		this.plugoutDescSources = plugoutDescSources;
	}

	public PlugoutDescSource getCurrentSource() {
		return currentSource;
	}

	public void setCurrentSource(PlugoutDescSource currentSource) {
		this.currentSource = currentSource;
	}

}
