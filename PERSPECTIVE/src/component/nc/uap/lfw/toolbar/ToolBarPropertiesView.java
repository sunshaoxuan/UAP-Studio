package nc.uap.lfw.toolbar;

import nc.uap.lfw.core.ObjectComboCellEditor;
import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.parts.LFWElementPart;
import nc.uap.lfw.perspective.model.Constant;
import nc.uap.lfw.toolbar.commands.AddToolBarItemAction;
import nc.uap.lfw.toolbar.commands.DeleteToolBarItemAction;
import nc.uap.lfw.toolbar.commands.DownToolBarAction;
import nc.uap.lfw.toolbar.commands.UpToolBarItemAction;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
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

public class ToolBarPropertiesView extends Composite {

	private LFWElementPart lfwElementPart = null;
	public LFWElementPart getLfwElementPart() {
		return lfwElementPart;
	}

	public void setLfwElementPart(LFWElementPart lfwElementPart) {
		this.lfwElementPart = lfwElementPart;
	}

	public ToolBarPropertiesView(Composite parent, int style) {
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
		tv = new TreeViewer(vf,SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Tree tree = tv.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		for (int i = 0; i < ToolBarCellModifier.colNames.length; i++) {
			createColumn(tree, ToolBarCellModifier.colNames[i], 100, SWT.LEFT, i);
		}
		ToolBarViewProvider provider = new ToolBarViewProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(ToolBarCellModifier.colNames);
		// {"ID","显示值","多语","语言","提示","提示多语","图形1","图形2","位置"};
		CellEditor[] cellEditors = new CellEditor[ToolBarCellModifier.colNames.length];
		//"id","Field","text","i18nName","langDir",
		cellEditors[0] = new TextCellEditor(tree);;
		cellEditors[1] = new TextCellEditor(tree);
		cellEditors[2] = new TextCellEditor(tree);
		cellEditors[3] = new TextCellEditor(tree);
		cellEditors[4] = new TextCellEditor(tree);
		cellEditors[5] = new TextCellEditor(tree);;
		cellEditors[6] = new TextCellEditor(tree);
		cellEditors[7] = new TextCellEditor(tree);
		String[] types = {ToolBarItem.BUTTON_TYPE};
		cellEditors[8] = new ObjectComboCellEditor(tree, types);
		cellEditors[9] = new ComboBoxCellEditor(tree, Constant.ISVISIBLE);
//		cellEditors[10] = new TextCellEditor(tree);
		cellEditors[10] =  new ObjectComboCellEditor(tree, Constant.HOTKEYMODIFIERS);
		cellEditors[11] = new TextCellEditor(tree);
		cellEditors[12] = new TextCellEditor(tree);
			
		tv.setCellEditors(cellEditors);
		tv.setCellModifier(new ToolBarCellModifier(this));
		Action addProp = new AddToolBarItemAction(this);
		Action delProp = new DeleteToolBarItemAction(this); 
		Action moveDown = new DownToolBarAction(this);
		Action moveUp = new UpToolBarItemAction(this);
		MenuManager mm = new MenuManager();
		mm.add(addProp);
		mm.add(delProp);
		mm.add(moveDown);
		mm.add(moveUp);
		Menu menu = mm.createContextMenu(tree);
		tree.setMenu(menu);
		
		ToolBar tb = new ToolBar(vf, SWT.FLAT);
		ToolBarManager tbm = new ToolBarManager(tb);
		tbm.add(addProp);
		tbm.add(delProp);
		tbm.add(moveDown);
		tbm.add(moveUp);
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
	
		
	public void dispose() {
		super.dispose();
	}
	
	
	public boolean setFocus() {
		return tv.getControl().setFocus();
	}
	

}
