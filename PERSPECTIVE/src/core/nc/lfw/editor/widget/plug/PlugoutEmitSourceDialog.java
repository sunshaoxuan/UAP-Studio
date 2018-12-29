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

public class PlugoutEmitSourceDialog extends DialogWithTitle{
	
	public class PlugoutEmitSource{
		/*来源类型*/
		private String sourceType;
		/*来源ID*/
//		private String sourceId;
		
		public PlugoutEmitSource(String sourceType){
			this.sourceType = sourceType;
//			this.sourceId = sourceId;
		}
		
		public String getSourceType() {
			return sourceType;
		}
		public void setSourceType(String sourceType) {
			this.sourceType = sourceType;
		}
//		public String getSourceId() {
//			return sourceId;
//		}
//		public void setSourceId(String sourceId) {
//			this.sourceId = sourceId;
//		}
	}

	private TreeViewer tv = null;
	
	private List<PlugoutEmitSource> plugoutEmitSources = new ArrayList<PlugoutEmitSource>();
	
	private PlugoutEmitSource currentSource = null;
	
	protected Point getInitialSize() {
		return new Point(500,600); 
	}
	
	public PlugoutEmitSourceDialog(Shell parentShell) {
		super(parentShell, "触发来源");
//		WidgetEditor editor = WidgetEditor.getActiveWidgetEditor();
//		LfwWidget widget =  editor.getGraph().getWidget();
//		for (Dataset  ds : widget.getViewModels().getDatasets()){
//			PlugoutEmitSource ps = new PlugoutEmitSource("Dataset", ds.getId());
//			plugoutEmitSources.add(ps);
//		}
		buildAllTypes(plugoutEmitSources);
		
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
			MessageDialog.openInformation(null, "提示", "请选择触发器来源");
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
		createColumn(tree, PlugoutEmitSourceCellModifier.colNames[0], 450, SWT.LEFT, 0);
//		createColumn(tree, PlugoutEmitSourceCellModifier.colNames[1], 100, SWT.LEFT, 1);
		PlugoutEmitSourceProvider provider = new PlugoutEmitSourceProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(PlugoutEmitSourceCellModifier.colNames);
		CellEditor[] cellEditors = new CellEditor[PlugoutEmitSourceCellModifier.colNames.length];
		cellEditors[0] = new TextCellEditor(tree);
//		cellEditors[1] = new TextCellEditor(tree);
		tv.setCellEditors(cellEditors);
		tv.setCellModifier(new PlugoutEmitSourceCellModifier());
		vf.setContent(tv.getControl());
		tree.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				setCurrentSource((PlugoutEmitSource)e.item.getData());
			}}  );
		
		tv.setInput(this.plugoutEmitSources);
		return container;
	}
	
	private TreeColumn createColumn(Tree tree, String colName , int width, int align, int index){
		TreeColumn col = new TreeColumn(tree, SWT.None, index);
		col.setText(colName);
		col.setWidth(width);
		col.setAlignment(align);
		return col;
	}

	public List<PlugoutEmitSource> getPlugoutEmitSources() {
		return plugoutEmitSources;
	}

	public void setPlugoutEmitSources(List<PlugoutEmitSource> plugoutEmitSources) {
		this.plugoutEmitSources = plugoutEmitSources;
	}

	public PlugoutEmitSource getCurrentSource() {
		return currentSource;
	}

	public void setCurrentSource(PlugoutEmitSource currentSource) {
		this.currentSource = currentSource;
	}

	/**
	 * 生成触发器类型
	 * 
	 * @param plugoutEmitSources
	 */
	

	private void buildAllTypes(List<PlugoutEmitSource> plugoutEmitSources) {
		//dataset事件
		plugoutEmitSources.add(new PlugoutEmitSource("Dataset.onBeforeRowSelect"));
		plugoutEmitSources.add(new PlugoutEmitSource("Dataset.onAfterRowSelect"));
		plugoutEmitSources.add(new PlugoutEmitSource("Dataset.onAfterRowUnSelect"));
		plugoutEmitSources.add(new PlugoutEmitSource("Dataset.onBeforeRowInsert"));
		plugoutEmitSources.add(new PlugoutEmitSource("Dataset.onAfterRowInsert"));
		plugoutEmitSources.add(new PlugoutEmitSource("Dataset.onBeforeDataChange"));
		plugoutEmitSources.add(new PlugoutEmitSource("Dataset.onAfterDataChange"));
		plugoutEmitSources.add(new PlugoutEmitSource("Dataset.onBeforeRowDelete"));
		plugoutEmitSources.add(new PlugoutEmitSource("Dataset.onAfterRowDelete"));
		plugoutEmitSources.add(new PlugoutEmitSource("Dataset.onBeforePageChange"));
		plugoutEmitSources.add(new PlugoutEmitSource("Dataset.onAfterPageChange"));
		plugoutEmitSources.add(new PlugoutEmitSource("Dataset.onDataLoad"));

		//menu事件
		plugoutEmitSources.add(new PlugoutEmitSource("Menu.onmouseout"));
		plugoutEmitSources.add(new PlugoutEmitSource("Menu.onmouseover"));
		plugoutEmitSources.add(new PlugoutEmitSource("Menu.ondbclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Menu.onclick"));
		
		//button事件
		plugoutEmitSources.add(new PlugoutEmitSource("Button.onmouseout"));
		plugoutEmitSources.add(new PlugoutEmitSource("Button.onmouseover"));
		plugoutEmitSources.add(new PlugoutEmitSource("Button.ondbclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Button.onclick"));
		
		//grid事件
		plugoutEmitSources.add(new PlugoutEmitSource("Grid.onmouseout"));
		plugoutEmitSources.add(new PlugoutEmitSource("Grid.onmouseover"));
		plugoutEmitSources.add(new PlugoutEmitSource("Grid.ondbclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Grid.onclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Grid.onDataOuterDivContextMenu"));
		plugoutEmitSources.add(new PlugoutEmitSource("Grid.onRowDbClick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Grid.onRowSelected"));
		plugoutEmitSources.add(new PlugoutEmitSource("Grid.beforeEdit"));
		plugoutEmitSources.add(new PlugoutEmitSource("Grid.afterEdit"));
		plugoutEmitSources.add(new PlugoutEmitSource("Grid.cellEdit"));
		plugoutEmitSources.add(new PlugoutEmitSource("Grid.onCellClick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Grid.cellValueChanged"));
		
		//excel事件
		plugoutEmitSources.add(new PlugoutEmitSource("Excel.onmouseout"));
		plugoutEmitSources.add(new PlugoutEmitSource("Excel.onmouseover"));
		plugoutEmitSources.add(new PlugoutEmitSource("Excel.ondbclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Excel.onclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Excel.onDataOuterDivContextMenu"));
		plugoutEmitSources.add(new PlugoutEmitSource("Excel.onRowDbClick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Excel.onRowSelected"));
		plugoutEmitSources.add(new PlugoutEmitSource("Excel.beforeEdit"));
		plugoutEmitSources.add(new PlugoutEmitSource("Excel.afterEdit"));
		plugoutEmitSources.add(new PlugoutEmitSource("Excel.cellEdit"));
		plugoutEmitSources.add(new PlugoutEmitSource("Excel.onCellClick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Excel.cellValueChanged"));
		
		//tree事件
		plugoutEmitSources.add(new PlugoutEmitSource("Tree.onmouseout"));
		plugoutEmitSources.add(new PlugoutEmitSource("Tree.onmouseover"));
		plugoutEmitSources.add(new PlugoutEmitSource("Tree.ondbclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Tree.onclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Tree.beforeSelNodeChange"));
		plugoutEmitSources.add(new PlugoutEmitSource("Tree.afterSelNodeChange"));
		plugoutEmitSources.add(new PlugoutEmitSource("Tree.rootNodeCreated"));
		plugoutEmitSources.add(new PlugoutEmitSource("Tree.nodeCreated"));
		plugoutEmitSources.add(new PlugoutEmitSource("Tree.beforeNodeCaptionChange"));
		plugoutEmitSources.add(new PlugoutEmitSource("Tree.onDragEnd"));
		plugoutEmitSources.add(new PlugoutEmitSource("Tree.onDragStart"));
		plugoutEmitSources.add(new PlugoutEmitSource("Tree.beforeNodeCreated"));
		plugoutEmitSources.add(new PlugoutEmitSource("Tree.beforeContextMenu"));
		
		//form事件
		plugoutEmitSources.add(new PlugoutEmitSource("Form.onmouseout"));
		plugoutEmitSources.add(new PlugoutEmitSource("Form.onmouseover"));
		plugoutEmitSources.add(new PlugoutEmitSource("Form.ondbclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Form.onclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Form.inActive"));
		plugoutEmitSources.add(new PlugoutEmitSource("Form.getValue"));
		plugoutEmitSources.add(new PlugoutEmitSource("Form.active"));
		plugoutEmitSources.add(new PlugoutEmitSource("Form.setValue"));
		plugoutEmitSources.add(new PlugoutEmitSource("Form.onBlur"));
		plugoutEmitSources.add(new PlugoutEmitSource("Form.onFocus"));
		
		//TextComp事件
		plugoutEmitSources.add(new PlugoutEmitSource("TextComp.onmouseout"));
		plugoutEmitSources.add(new PlugoutEmitSource("TextComp.onmouseover"));
		plugoutEmitSources.add(new PlugoutEmitSource("TextComp.ondbclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("TextComp.onclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("TextComp.onKeyUp"));
		plugoutEmitSources.add(new PlugoutEmitSource("TextComp.onKeyDown"));
		plugoutEmitSources.add(new PlugoutEmitSource("TextComp.onEnter"));
		plugoutEmitSources.add(new PlugoutEmitSource("TextComp.onBlur"));
		plugoutEmitSources.add(new PlugoutEmitSource("TextComp.onFocus"));
		plugoutEmitSources.add(new PlugoutEmitSource("TextComp.onselect"));
		plugoutEmitSources.add(new PlugoutEmitSource("TextComp.valuechanged"));
		
		//IFrame事件
		plugoutEmitSources.add(new PlugoutEmitSource("IFrameComp.onmouseout"));
		plugoutEmitSources.add(new PlugoutEmitSource("IFrameComp.onmouseover"));
		plugoutEmitSources.add(new PlugoutEmitSource("IFrameComp.ondbclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("IFrameComp.onclick"));
		
		//Label事件
		plugoutEmitSources.add(new PlugoutEmitSource("Label.onmouseout"));
		plugoutEmitSources.add(new PlugoutEmitSource("Label.onmouseover"));
		plugoutEmitSources.add(new PlugoutEmitSource("Label.ondbclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Label.onclick"));
		
		//Label事件
		plugoutEmitSources.add(new PlugoutEmitSource("Image.onmouseout"));
		plugoutEmitSources.add(new PlugoutEmitSource("Image.onmouseover"));
		plugoutEmitSources.add(new PlugoutEmitSource("Image.ondbclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("Image.onclick"));
		
		//弹出菜单事件
		plugoutEmitSources.add(new PlugoutEmitSource("PopupMenu.onmouseout"));
		plugoutEmitSources.add(new PlugoutEmitSource("PopupMenu.onmouseover"));
		plugoutEmitSources.add(new PlugoutEmitSource("PopupMenu.ondbclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("PopupMenu.onclick"));
		plugoutEmitSources.add(new PlugoutEmitSource("PopupMenu.onContainerCreate"));
		
		//LinkComp事件
		plugoutEmitSources.add(new PlugoutEmitSource("LinkComp.onclick"));
		
		//自定义控件事件
		plugoutEmitSources.add(new PlugoutEmitSource("CustomConizol.onSelfDefEvent"));
		plugoutEmitSources.add(new PlugoutEmitSource("CustomConizol.onclick"));
		
	}
}
