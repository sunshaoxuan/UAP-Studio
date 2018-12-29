package nc.uap.lfw.combodata.core;

import nc.uap.lfw.combodata.commands.AddComboFieldAction;
import nc.uap.lfw.combodata.commands.DeleteComboFieldAction;
import nc.uap.lfw.lang.M_combodata;
import nc.uap.lfw.parts.LFWElementPart;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
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
 * 下拉数据视图
 * @author zhangxya
 *
 */
public class ComboDataPropertiesView extends Composite {

	private LFWElementPart lfwElementPart = null;
	public LFWElementPart getLfwElementPart() {
		return lfwElementPart;
	}

	public void setLfwElementPart(LFWElementPart lfwElementPart) {
		this.lfwElementPart = lfwElementPart;
	}

	public ComboDataPropertiesView(Composite parent, int style) {
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

	public static final String[] colNames = {M_combodata.ComboDataPropertiesView_0,M_combodata.ComboDataPropertiesView_1, M_combodata.ComboDataPropertiesView_2, M_combodata.ComboDataPropertiesView_3};
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		ViewForm vf = new ViewForm(parent,SWT.NONE);
		vf.setLayout(new FillLayout());
		tv = new TreeViewer(vf,SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Tree tree = tv.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		for(int i=0; i<colNames.length; i++){
			createColumn(tree, colNames[i], 180, SWT.NONE, i);
		}
		ComboDataProvider provider = new ComboDataProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(colNames);
		//每个Cell的编辑Editor，todo
		CellEditor[] cellEditors = new CellEditor[colNames.length];
		for(int i=0; i<colNames.length; i++){
			cellEditors[i] = new TextCellEditor(tree);
		}
		tv.setCellEditors(cellEditors);
		tv.setCellModifier(new ComboDataCellModifier(this));
		Action addProp = new AddComboFieldAction(this);
		Action delProp = new DeleteComboFieldAction(this); 
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
		vf.setContent(tv.getControl());
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
