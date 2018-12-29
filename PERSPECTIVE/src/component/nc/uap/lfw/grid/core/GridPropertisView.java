package nc.uap.lfw.grid.core;

import java.util.ArrayList;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.ObjectComboCellEditor;
import nc.uap.lfw.core.comp.GridColumnGroup;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.grid.action.AddGridColGroupAction;
import nc.uap.lfw.grid.action.AddGridFieldAction;
import nc.uap.lfw.grid.action.DeleteGridFieldAction;
import nc.uap.lfw.grid.action.DownGridFieldAction;
import nc.uap.lfw.grid.action.GridSelectedAllAction;
import nc.uap.lfw.grid.action.GridUnSelectedAllAction;
import nc.uap.lfw.grid.action.UPGridFieldAction;
import nc.uap.lfw.parts.LFWElementPart;
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

/**
 * grid 属性视图
 * @author zhangxya
 *
 */
public class GridPropertisView extends Composite {

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

	public GridPropertisView(Composite parent, int style) {
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

	public String[] getColumnGroup(){
		Object object = tv.getInput();
		ArrayList<IGridColumn> arraylist = new ArrayList<IGridColumn>();
		ArrayList<String> groupList = new ArrayList<String>();
		groupList.add("");
		if(object instanceof ArrayList){
			 arraylist = (ArrayList<IGridColumn>)object;
			 for (int i = 0; i < arraylist.size(); i++) {
				IGridColumn gridColumn = arraylist.get(i);
				if(gridColumn instanceof GridColumnGroup){
					GridColumnGroup group = (GridColumnGroup) gridColumn;
					groupList.add(group.getId());
				}
			}
		}
		return groupList.toArray(new String[0]);
	}
	
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		ViewForm vf = new ViewForm(parent,SWT.NONE);
		vf.setLayout(new FillLayout());
		tv = new TreeViewer(vf,SWT.CHECK|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Tree tree = tv.getTree();
		ctv =  new CheckboxTreeViewer(tree);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		for (int i = 0; i < GridTableCellModifier.colNames.length; i++) {
			createColumn(tree, GridTableCellModifier.colNames[i], 120, SWT.LEFT, i);
		}
		GridTableViewProvider provider = new GridTableViewProvider(this);
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(GridTableCellModifier.colNames);
		//每个Cell的编辑Editor，todo
		CellEditor[] cellEditors = new CellEditor[GridTableCellModifier.colNames.length];
		cellEditors[0] = new TextCellEditor(tree);;
		cellEditors[1] = new TextCellEditor(tree);
		cellEditors[2] = new TextCellEditor(tree);
		cellEditors[3] = new TextCellEditor(tree);
		cellEditors[4] = new TextCellEditor(tree);
		//width
		cellEditors[5] = new TextCellEditor(tree);
		//visble
		cellEditors[6] = new ComboBoxCellEditor(tree, Constant.ISVISIBLE);
		//editable
		cellEditors[7] = new ComboBoxCellEditor(tree, Constant.ISEDITABLE);
		//columbgcolor
		cellEditors[8] = new TextCellEditor(tree);
		//textalign
		cellEditors[9] = new ObjectComboCellEditor(tree, Constant.TEXTALIGN);
		//textcolor
		cellEditors[10] = new TextCellEditor(tree);
		//fixedHeader
		cellEditors[11] =  new ComboBoxCellEditor(tree, Constant.ISFIXHEADER);
		//editorType
		cellEditors[12] = new ObjectComboCellEditor(tree, Constant.TEXTTYPE);
		//rendertype,todo
		cellEditors[13] = new TextCellEditor(tree);
		//refnode
		cellEditors[14] = new ObjectComboCellEditor(tree, LFWPersTool.getRefNodes());
		//refcombodata
		cellEditors[15] = new ObjectComboCellEditor(tree, LFWPersTool.getRefCombdata());
		//imageonly
		cellEditors[16] = new ComboBoxCellEditor(tree, Constant.ISIMAGEONLY);
		//sumcol
		cellEditors[17] = new ComboBoxCellEditor(tree, Constant.ISSUMCOL);
		//autoexpend
		cellEditors[18] =  new ComboBoxCellEditor(tree, Constant.ISAUTOEXPEND);
		//sortable
		cellEditors[19] =  new ComboBoxCellEditor(tree, Constant.ISSORTALBE);
		
		cellEditors[20] =  new ObjectComboCellEditor(tree, getColumnGroup());
		
		cellEditors[21] =  new ComboBoxCellEditor(tree, Constant.ISSORTALBE);
		
		cellEditors[22] =  new ComboBoxCellEditor(tree, Constant.ISSORTALBE);
		
		cellEditors[23] = new TextCellEditor(tree);
		
		//sumColRenderFunc
		cellEditors[24] = new TextCellEditor(tree);
		
		tv.setCellEditors(cellEditors);
		tv.setCellModifier(new GridTableCellModifier(this));
		Action addProp = new AddGridFieldAction(this);
		Action delProp = new DeleteGridFieldAction(this); 
		Action moveDown = new DownGridFieldAction(this);
		Action moveUp = new UPGridFieldAction(this);
		Action addGridColAction = new AddGridColGroupAction(this);
		Action gridSelectedAll = new GridSelectedAllAction(this);
		Action gridUnSelAll = new GridUnSelectedAllAction(this);
		MenuManager mm = new MenuManager();
		LfwView widget = LFWPersTool.getCurrentWidget();
		if(widget != null && widget.getFrom() == null){
			mm.add(addProp);
			mm.add(delProp);
			mm.add(moveDown);
			mm.add(moveUp);
			mm.add(addGridColAction);
			mm.add(gridSelectedAll);
			mm.add(gridUnSelAll);
			Menu menu = mm.createContextMenu(tree);
			tree.setMenu(menu);
			
			ToolBar tb = new ToolBar(vf, SWT.FLAT);
			ToolBarManager tbm = new ToolBarManager(tb);
			tbm.add(addProp);
			tbm.add(delProp);
			tbm.add(moveDown);
			tbm.add(moveUp);
			tbm.add(addGridColAction);
			tbm.add(gridSelectedAll);
			tbm.add(gridUnSelAll);
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
	

}
