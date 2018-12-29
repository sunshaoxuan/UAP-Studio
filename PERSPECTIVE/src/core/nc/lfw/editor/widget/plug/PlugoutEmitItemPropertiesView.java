package nc.lfw.editor.widget.plug;


import nc.uap.lfw.core.ObjectComboCellEditor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * PlugoutDescEmitPropertiesView
 * 
 * @author dingrf
 *
 */
public class PlugoutEmitItemPropertiesView extends Composite {

	class SourceCellEditor extends DialogCellEditor {

		public SourceCellEditor(Composite parent,TreeViewer tv) {
			this(parent, SWT.NONE);
		}

		public SourceCellEditor(Composite parent, int style) {
			super(parent, style);
		}

		protected Object openDialogBox(Control cellEditorWindow) {
			PlugoutEmitSourceDialog dialog = new PlugoutEmitSourceDialog(cellEditorWindow.getShell());
			if (dialog.open() == Dialog.OK) {
				return dialog.getResult();
			}
			return null;
		}

	}
	
	/**PlugoutDescElementObj 对象*/
	private PlugoutDescElementPart plugoutDescElementPart = null;
	
	private boolean canEdit = false;
	
	private ObjectComboCellEditor typeCellEditor = null;
	
	public PlugoutDescElementPart getPlugoutDescElementPart() {
		return plugoutDescElementPart;
	}

	public void setPlugoutDescElementPart(PlugoutDescElementPart plugoutDescElementPart) {
		this.plugoutDescElementPart = plugoutDescElementPart;
	}

	public PlugoutEmitItemPropertiesView(Composite parent, int style, boolean canEdit) {
		super(parent, style);
		this.canEdit = canEdit;
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
		createColumn(tree, PlugoutEmitItemCellModifier.colNames[0], 100, SWT.LEFT, 0);
		createColumn(tree, PlugoutEmitItemCellModifier.colNames[1], 150, SWT.LEFT, 1);
		createColumn(tree, PlugoutEmitItemCellModifier.colNames[2], 150, SWT.LEFT, 2);
		createColumn(tree, PlugoutEmitItemCellModifier.colNames[3], 100, SWT.LEFT, 3);
		PlugoutEmitItemProvider provider = new PlugoutEmitItemProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(PlugoutEmitItemCellModifier.colNames);
		//每个Cell的编辑Editor，todo
		CellEditor[] cellEditors = new CellEditor[PlugoutEmitItemCellModifier.colNames.length];
		//id,field,text,i18nname
		cellEditors[0] = new TextCellEditor(tree);
		cellEditors[1] = new SourceCellEditor(tree, tv);
//		cellEditors[2] = new TextCellEditor(tree);
		String[] type = {""};
		this.typeCellEditor = new ObjectComboCellEditor(tree, type);
		cellEditors[2] = this.typeCellEditor;
		cellEditors[3] = new TextCellEditor(tree);
		tv.setCellEditors(cellEditors);
		tv.setCellModifier(new PlugoutEmitItemCellModifier(this));
		if (canEdit){
			Action addProp = new AddPlugoutEmitItemPropAction(this);
			Action delProp = new DelPlugoutEmitItemPropAction(this); 
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

	public ObjectComboCellEditor getTypeCellEditor() {
		return typeCellEditor;
	}

	public void setTypeCellEditor(ObjectComboCellEditor typeCellEditor) {
		this.typeCellEditor = typeCellEditor;
	}
	
}
