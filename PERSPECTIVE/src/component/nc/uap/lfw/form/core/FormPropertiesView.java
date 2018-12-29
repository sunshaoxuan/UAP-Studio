package nc.uap.lfw.form.core;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.ObjectComboCellEditor;
import nc.uap.lfw.form.commands.AddFormElementAction;
import nc.uap.lfw.form.commands.DelFormElementAction;
import nc.uap.lfw.form.commands.DownFormElement;
import nc.uap.lfw.form.commands.FormSelectedAllAction;
import nc.uap.lfw.form.commands.FormUnSelectedAllAction;
import nc.uap.lfw.form.commands.UPFormElementAction;
import nc.uap.lfw.parts.LFWElementPart;
import nc.uap.lfw.perspective.editor.EditableComboBoxCellEditor;
import nc.uap.lfw.perspective.model.Constant;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class FormPropertiesView  extends Composite {

	private LFWElementPart lfwElementPart = null;
	private CheckboxTreeViewer ctv;
	public CheckboxTreeViewer getCtv() {
		return ctv;
	}

	public void setCtv(CheckboxTreeViewer ctv) {
		this.ctv = ctv;
	}

	public LFWElementPart getLfwElementPart() {
		return lfwElementPart;
	}

	public void setLfwElementPart(LFWElementPart lfwElementPart) {
		this.lfwElementPart = lfwElementPart;
	}

	public FormPropertiesView(Composite parent, int style) {
		super(parent, style);
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
		tv = new TreeViewer(vf,SWT.CHECK|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Tree tree = tv.getTree();
		ctv =  new CheckboxTreeViewer(tree);;
//		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		for (int i = 0; i < FormTableCellModifier.colNames.length; i++) {
			createColumn(tree, FormTableCellModifier.colNames[i], 80, SWT.LEFT, i);
		}
		FormTabelViewProvider provider = new FormTabelViewProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(FormTableCellModifier.colNames);
		//Ã¿¸öCellµÄ±à¼­Editor£¬todo
		CellEditor[] cellEditors = new CellEditor[FormTableCellModifier.colNames.length];
		//"id","Field","text","i18nName","langDir",
		cellEditors[0] = new TextCellEditor(tree);;
		cellEditors[1] = new TextCellEditor(tree);
		cellEditors[2] = new TextCellEditor(tree);
		cellEditors[3] = new TextCellEditor(tree);
		//visible
		cellEditors[4] = new ComboBoxCellEditor(tree, Constant.ISVISIBLE);
	
		//enable
		cellEditors[5] = new ComboBoxCellEditor(tree, Constant.ISEDITABLE);
		////editorType
		cellEditors[6] = new EditableComboBoxCellEditor(tree, Constant.TEXTTYPE);
		//refnode
		//refnode
		cellEditors[7] = new EditableComboBoxCellEditor(tree, LFWPersTool.getRefNodes());
		//refcombodata
		cellEditors[8] = new EditableComboBoxCellEditor(tree, LFWPersTool.getRefCombdata());
		//relatinid
		cellEditors[9] = new TextCellEditor(tree);
		//rowspan
		cellEditors[10] = new TextCellEditor(tree);
		//colspan
		cellEditors[11] = new TextCellEditor(tree);
		//height
		cellEditors[12] = new TextCellEditor(tree);
		//width
		cellEditors[13] = new TextCellEditor(tree);
		
		cellEditors[14] = new TextCellEditor(tree);
		
		cellEditors[15] = new EditableComboBoxCellEditor(tree, LFWPersTool.getAllWidgetComonents());
		
		//dataDivHeight
		cellEditors[16] = new TextCellEditor(tree);
		//defaultValue
		//cellEditors[15] = new TextCellEditor(tree);
		//imageOnly
		cellEditors[17] = new ComboBoxCellEditor(tree, Constant.ISIMAGEONLY);
		//selectonly
		cellEditors[18] = new ComboBoxCellEditor(tree, Constant.SELECTONLY);
		//nextline
		cellEditors[19] = new ComboBoxCellEditor(tree, Constant.ISNEXTLINE);
		//toorbar
		cellEditors[20] = new ComboBoxCellEditor(tree, Constant.TOOLBARTYPE);

		cellEditors[21] = new TextCellEditor(tree);
		
		//description
		cellEditors[22] = new TextCellEditor(tree);
		//labelColor
		cellEditors[23] = new TextCellEditor(tree);
		//isnullable
		cellEditors[24] = new ComboBoxCellEditor(tree, Constant.ISNEXTLINE);
		//attchNext
		cellEditors[25] = new ComboBoxCellEditor(tree, Constant.ISNEXTLINE);
		//editable
		cellEditors[26] = new ComboBoxCellEditor(tree, Constant.ISNEXTLINE);
		//inputAssistent
		cellEditors[27] = new TextCellEditor(tree);
		//presion
		cellEditors[28] = new TextCellEditor(tree);
		//sizeLimit
		cellEditors[29] = new TextCellEditor(tree);
		//MaxLength
		cellEditors[30] = new TextCellEditor(tree);
		tv.setCellEditors(cellEditors);
		tv.setCellModifier(new FormTableCellModifier(this));
		Action addProp = new AddFormElementAction(this);
		Action delProp = new DelFormElementAction(this); 
		Action moveDown = new DownFormElement(this);
		Action moveUp = new UPFormElementAction(this);
		Action selectAll = new FormSelectedAllAction(this);
		Action unselectAll = new FormUnSelectedAllAction(this);
		MenuManager mm = new MenuManager();
		mm.add(addProp);
		mm.add(delProp);
		mm.add(moveDown);
		mm.add(moveUp);
		mm.add(selectAll);
		mm.add(unselectAll);
		Menu menu = mm.createContextMenu(tree);
		tree.setMenu(menu);
		
		ToolBar tb = new ToolBar(vf, SWT.FLAT);
		ToolBarManager tbm = new ToolBarManager(tb);
		tbm.add(addProp);
		tbm.add(delProp);
		tbm.add(moveDown);
		tbm.add(moveUp);
		tbm.add(selectAll);
		tbm.add(unselectAll);
		tbm.update(true);
		vf.setContent(tv.getControl());
		vf.setTopLeft(tb);
		
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
	

}
