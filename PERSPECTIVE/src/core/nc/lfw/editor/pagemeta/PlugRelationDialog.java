package nc.lfw.editor.pagemeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.widget.plug.PluginDescElementObj;
import nc.lfw.editor.widget.plug.PlugoutDescElementObj;
import nc.uap.lfw.core.ObjectComboCellEditor;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginDescItem;
import nc.uap.lfw.core.page.PlugoutDescItem;
import nc.uap.lfw.lang.M_pagemeta;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * plugin plugout 关联窗口
 * 
 * @author dingrf
 *
 */

public class PlugRelationDialog extends DialogWithTitle{
	
	 class AddDSCommand extends Command{
		public AddDSCommand(){
			super(M_pagemeta.PlugRelationDialog_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	
	public class PlugRelation{
		private String outValue;
		private String inValue;
		
		public String getOutValue() {
			return outValue;
		}
		public void setOutValue(String outValue) {
			this.outValue = outValue;
		}
		public String getInValue() {
			return inValue;
		}
		public void setInValue(String inValue) {
			this.inValue = inValue;
		}
	}

	private TreeViewer tv = null;
	private PlugoutDescElementObj source;
	private PluginDescElementObj target;
	private Connector conn;
	private List<PlugRelation> plugRelation = new ArrayList<PlugRelation>();
	private String[] inValues;
	/**
	 * conn ID
	 */
	private Text idText;
	/**
	 * conn ID
	 */
//	private String id;
	
	
	protected Point getInitialSize() {
		return new Point(500,250); 
	}
	
	public PlugRelationDialog(Shell parentShell, String title, PlugoutDescElementObj source, PluginDescElementObj target, String connId) {
		super(parentShell, title);
		this.source = source;
		this.target = target;
		
		//新建连接的时候
		if (connId == null || connId.equals("")){ //$NON-NLS-1$
			this.conn = new Connector();
			this.conn.setPluginId(target.getPlugin().getId());
			this.conn.setPlugoutId(source.getPlugout().getId());
			this.conn.setSource(source.getWidgetObj().getWidget().getId());
			this.conn.setTarget(target.getWidgetObj().getWidget().getId());
//			this.conn.setMapping(new HashMap<String, String>());
			for (PlugoutDescItem item : source.getPlugout().getDescItems()){
				PlugRelation r = new PlugRelation();
				r.setOutValue(item.getName());
				plugRelation.add(r);
			}
			PluginDescItem[] pluginDescItem = this.target.getPlugin().getDescItems();
			this.inValues = new String[pluginDescItem.length];
			for (int i = 0; i< pluginDescItem.length; i++){
				this.inValues[i] = pluginDescItem[i].getId();
			}
		}
		//修改连接
		else{
			PagemetaEditor editor = PagemetaEditor.getActivePagemetaEditor();
//			List<WidgetElementObj> widgetObjs = editor.getGraph().getWidgetCells();
			LfwWindow pagemeta = editor.getGraph().getPagemeta();
			this.conn = pagemeta.getConnectorMap().get(connId);
			
//			Map<String, String > plugMap = this.conn.getMapping();
//			if (plugMap == null)
//				this.conn.setMapping(new HashMap<String, String>());
			for (PlugoutDescItem item : this.source.getPlugout().getDescItems()){
				PlugRelation r = new PlugRelation();
				r.setOutValue(item.getName());
//				if (plugMap.containsKey(item.getName()))
//					r.setInValue(plugMap.get(item.getName()));
				plugRelation.add(r);
			}
			PluginDescItem[] pluginDescItem = this.target.getPlugin().getDescItems();
			this.inValues = new String[pluginDescItem.length];
			for (int i = 0; i < pluginDescItem.length; i++){
				this.inValues[i] = pluginDescItem[i].getId();
			}
			
		}
	}

	
	public TreeViewer getTv() {
		return tv;
	}

	public void setTv(TreeViewer tv) {
		this.tv = tv;
	}

	
	public Connector getConn() {
		return conn;
	}

	protected void okPressed() {
		if (idText.getText().equals("")){ //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_pagemeta.PlugRelationDialog_1, M_pagemeta.PlugRelationDialog_2);
			idText.setFocus();
			return;
		}
		String connId = idText.getText();
		PagemetaEditor editor = PagemetaEditor.getActivePagemetaEditor();
//		List<WidgetElementObj> widgetObjs = editor.getGraph().getWidgetCells();
		LfwWindow pagemeta = editor.getGraph().getPagemeta();
		Map<String, Connector> map = pagemeta.getConnectorMap();
		if (map.containsKey(connId)){
			Connector c = map.get(connId);
			if (!(c.getPluginId().equals(this.conn.getPluginId()) && c.getPlugoutId().equals(this.conn.getPlugoutId()))){
				MessageDialog.openInformation(this.getShell(), M_pagemeta.PlugRelationDialog_1, M_pagemeta.PlugRelationDialog_3);
				return;
			}
		}
		
			
		conn.setId(idText.getText());
//		this.conn.getMapping().clear();
//		for (PlugRelation r : plugRelation){
//			if (r.getInValue() != null && !r.getInValue().equals("")) //$NON-NLS-1$
//				this.conn.getMapping().put(r.getOutValue(), r.getInValue());
//		}
		
//		if (this.conn.getMapping().size() <1){
//			MessageDialog.openInformation(this.getShell(), "提示", "请选择关联关系");
//			return;
//		}
		
//		PagemetaEditor editor = PagemetaEditor.getActivePagemetaEditor();
//		PageMeta pagemeta = editor.getGraph().getPagemeta();
		pagemeta.getConnectorMap().put(conn.getId(), conn);
		PagemetaEditor.getActivePagemetaEditor().setDirtyTrue();
		super.okPressed();
	}
	 
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		container.setLayoutData(gd);
		container.setLayout(new GridLayout(1, false));

		
		Composite container2 = new Composite(container, SWT.NONE);
		
		container2.setLayout(new GridLayout(2, false));
//		container2.setLayoutData(new GridData(GridData.FILL_BOTH));
		Label nameLabel = new Label(container2, SWT.NONE);
		nameLabel.setText(M_pagemeta.PlugRelationDialog_4);
		idText = new Text(container2, SWT.NONE);
		idText.setLayoutData(new GridData(220,20));
		idText.setText(conn.getId() == null ? "" : conn.getId()); //$NON-NLS-1$

		
		ViewForm vf = new ViewForm(container, SWT.NONE);
		vf.setLayoutData(gd);
//		TableViewer tv = new TableViewer(vf, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		tv = new TreeViewer(vf,SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Tree tree = tv.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		createColumn(tree, PlugRelationCellModifier.colNames[0], 100, SWT.LEFT, 0);
		createColumn(tree, PlugRelationCellModifier.colNames[1], 100, SWT.LEFT, 1);
		PlugRelationProvider provider = new PlugRelationProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(PlugRelationCellModifier.colNames);
		//每个Cell的编辑Editor，todo
		CellEditor[] cellEditors = new CellEditor[PlugRelationCellModifier.colNames.length];
		//id,field,text,i18nname
		cellEditors[0] = new TextCellEditor(tree);;
//		cellEditors[1] = new TextCellEditor(tree);
		cellEditors[1] = new ObjectComboCellEditor(tree, this.inValues);
		tv.setCellEditors(cellEditors);
		tv.setCellModifier(new PlugRelationCellModifier(this));
		vf.setContent(tv.getControl());
		
		tv.setInput(plugRelation);
		return container;
	}
	
	private TreeColumn createColumn(Tree tree, String colName , int width, int align, int index){
		TreeColumn col = new TreeColumn(tree, SWT.None, index);
		col.setText(colName);
		col.setWidth(width);
		col.setAlignment(align);
		return col;
	}

	public Text getIdText() {
		return idText;
	}

	public void setIdText(Text idText) {
		this.idText = idText;
	}
	
	
}
