package nc.uap.lfw.editor.application.plug;

import nc.uap.lfw.core.ObjectComboCellEditor;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.editor.application.ApplicationEditor;
import nc.uap.lfw.editor.application.ApplicationPart;

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
	
	private ApplicationPart applicationPart = null;
	
	private ObjectComboCellEditor sourceWindowCellEditor = null;
	
//	private ObjectComboCellEditor sourceCellEditor = null;
	
	private ObjectComboCellEditor plugoutCellEditor = null;
	
	private ObjectComboCellEditor targetWindowCellEditor = null;
	
//	private ObjectComboCellEditor targetCellEditor = null;

	private ObjectComboCellEditor pluginCellEditor = null;
	
	public ApplicationPart getApplicationPart() {
		return applicationPart;
	}

	public void setApplicationPart(ApplicationPart applicationPart) {
		this.applicationPart = applicationPart;
	}

	public ConnectorPropertiesView(Composite parent, int style, boolean canEdit, ApplicationPart applicationPart) {
		super(parent, style);
		this.canEdit = canEdit;
		this.applicationPart = applicationPart;
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
		createColumn(tree, ConnectorCellModifier.colNames[3], 200, SWT.LEFT, 3);
//		createColumn(tree, ConnectorCellModifier.colNames[4], 100, SWT.LEFT, 4);
		ConnectorProvider provider = new ConnectorProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(ConnectorCellModifier.colNames);
		CellEditor[] cellEditors = new CellEditor[ConnectorCellModifier.colNames.length];
		this.sourceWindowCellEditor = new ObjectComboCellEditor(tree, getWindows());
		cellEditors[0] = this.sourceWindowCellEditor;
		this.plugoutCellEditor = new ObjectComboCellEditor(tree, new String[]{""});
		cellEditors[1] = this.plugoutCellEditor;
		this.targetWindowCellEditor = new ObjectComboCellEditor(tree, getWindows());
		cellEditors[2] = this.targetWindowCellEditor;
		this.pluginCellEditor = new ObjectComboCellEditor(tree, new String[]{""});
		cellEditors[3] = this.pluginCellEditor;
//		cellEditors[4] = new ConnectorMappingCellEditor(tree, tv);
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
	
	//ȡsource
	private String[] getWindows() {
		ApplicationEditor editor = ApplicationEditor.getActiveEditor();
		Application app = editor.getGraph().getApplication();
		String[] sourceWindows = new String[app.getWindowList().size()];
		int i = 0;
		for (WindowConfig win : app.getWindowList()){
			sourceWindows[i] = win.getId();
			i++;
		}
		return sourceWindows;
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

	public ObjectComboCellEditor getSourceWindowCellEditor() {
		return sourceWindowCellEditor;
	}

//	public ObjectComboCellEditor getSourceCellEditor() {
//		return sourceCellEditor;
//	}

	public ObjectComboCellEditor getPlugoutCellEditor() {
		return plugoutCellEditor;
	}

	public ObjectComboCellEditor getTargetWindowCellEditor() {
		return targetWindowCellEditor;
	}

//	public ObjectComboCellEditor getTargetCellEditor() {
//		return targetCellEditor;
//	}

	public ObjectComboCellEditor getPluginCellEditor() {
		return pluginCellEditor;
	}
	

}
