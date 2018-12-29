package nc.lfw.editor.pagemeta.plug;

import java.util.List;

import nc.lfw.editor.pagemeta.PagemetaEditor;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.ObjectComboCellEditor;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.PlugoutDesc;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * ConnectorPropertiesView
 * 
 * @author dingrf
 *
 */
public class ConnectorPropertiesView extends Composite {

	
	private boolean canEdit = false;
	
	private WidgetElementObj widget = null;
	
	
	
	private ObjectComboCellEditor plugoutCellEditor = null;
	
	private ObjectComboCellEditor targetCellEditor = null;

	private ObjectComboCellEditor pluginCellEditor = null;
	
	

	public WidgetElementObj getWidget() {
		return widget;
	}

	public void setWidget(WidgetElementObj widget) {
		this.widget = widget;
	}

	public ConnectorPropertiesView(Composite parent, int style, boolean canEdit, WidgetElementObj widget) {
		super(parent, style);
		this.canEdit = canEdit;
		this.widget = widget;
		createPartControl(this);
	}
	
	private TreeViewer tv = null;
	
	public TreeViewer getTv() {
		return tv;
	}

	public void setTv(TreeViewer tv) {
		this.tv = tv;
	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		ViewForm vf = new ViewForm(parent,SWT.NONE);
		vf.setLayout(new FillLayout());
		tv = new TreeViewer(vf,SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Tree tree = tv.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		createColumn(tree, ConnectorCellModifier.colNames[0], 200, SWT.LEFT, 0);
		createColumn(tree, ConnectorCellModifier.colNames[1], 200, SWT.LEFT, 1);
		createColumn(tree, ConnectorCellModifier.colNames[2], 200, SWT.LEFT, 2);
//		createColumn(tree, ConnectorCellModifier.colNames[3], 100, SWT.LEFT, 3);
	
		ConnectorProvider provider = new ConnectorProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(ConnectorCellModifier.colNames);
		CellEditor[] cellEditors = new CellEditor[ConnectorCellModifier.colNames.length];

		this.plugoutCellEditor = new ObjectComboCellEditor(tree, getPlugout());
		cellEditors[0] = this.plugoutCellEditor;
		this.targetCellEditor = new ObjectComboCellEditor(tree, getTargetView());
		cellEditors[1] = this.targetCellEditor;
		this.pluginCellEditor = new ObjectComboCellEditor(tree, new String[]{""});
		cellEditors[2] = this.pluginCellEditor;
		
//		cellEditors[3] = new ConnectorMappingCellEditor(tree, tv);
		tv.setCellEditors(cellEditors);
		tv.setCellModifier(new ConnectorCellModifier(this));
		if (canEdit){
			Action addProp = new AddConnectorPropAction(this);
			Action delProp = new DelConnectorPropAction(this); 
			MenuManager mm = new MenuManager();
			mm.add(addProp);
			mm.add(delProp);
			Menu menu = mm.createContextMenu(tree);
			tree.setMenu(menu);
			
			ToolBar tb = new ToolBar(vf, SWT.FLAT);
			ToolBarManager tbm = new ToolBarManager(tb);
			tbm.add(addProp);
			tbm.add(delProp);
			tbm.update(true);
			vf.setTopLeft(tb);
		}
		vf.setContent(tv.getControl());
	}
	
	//ȡplugout
	private String[] getPlugout() {
		LfwView sourceView= getWidget().getWidget();
		PlugoutDesc[] plugoutCells = sourceView.getPlugoutDescs();
		if(plugoutCells.length > 0){
			String[] plugout = new String[plugoutCells.length];
			for(int i = 0; i < plugoutCells.length; i++){
				plugout[i] = plugoutCells[i].getId();
			}
			return plugout;
		}
		else return null;
	}
	//ȡtargetview
	private String[] getTargetView() {
		PagemetaEditor editor = PagemetaEditor.getActivePagemetaEditor();
		List<WidgetElementObj> widgetCells = editor.getGraph().getWidgetCells();
//		ApplicationEditor editor = ApplicationEditor.getActiveEditor();
//		Application app = editor.getGraph().getApplication();
		String sourceid = getWidget().getWidget().getId();
		String[] targetViews = new String[widgetCells.size()-1];
		int i = 0;
		for (WidgetElementObj widget : widgetCells){
			if(widget.getWidget().getId().equals(sourceid))
				continue;
			targetViews[i] = widget.getWidget().getId();
			i++;
		}
		return targetViews;
	}

	private TreeColumn createColumn(Tree tree, String colName , int width, int align, int index){
		TreeColumn col = new TreeColumn(tree, SWT.None, index);
		col.setText(colName);
		col.setWidth(width);
		col.setAlignment(align);
		return col;
	}
	
	
	public CellEditor getCellEditorByColName(String colName){
		String[] colNames =(String[]) tv.getColumnProperties();
		int count = colNames == null ? 0 : colNames.length;
		int index = -1;
		for (int i = 0; i < count; i++) {
			if(colNames[i].equals(colName)){
				index = i;
				break;
			}
		}
		CellEditor ce = null;
		if(index != -1){
			ce = tv.getCellEditors()[index];
		}
		return ce;
	}
	
	public void dispose() {
		super.dispose();
	}
	
	
	public boolean setFocus() {
		return tv.getControl().setFocus();
	}
	
	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}



	public ObjectComboCellEditor getPlugoutCellEditor() {
		return plugoutCellEditor;
	}



	public ObjectComboCellEditor getTargetCellEditor() {
		return targetCellEditor;
	}

	public ObjectComboCellEditor getPluginCellEditor() {
		return pluginCellEditor;
	}
	

}
