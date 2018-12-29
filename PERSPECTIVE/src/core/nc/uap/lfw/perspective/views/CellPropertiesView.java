package nc.uap.lfw.perspective.views;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.ObjectComboCellEditor;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.parts.LFWElementPart;
import nc.uap.lfw.perspective.editor.CellModifier;
import nc.uap.lfw.perspective.editor.CellPropertiesViewProvider;
import nc.uap.lfw.perspective.editor.EditableComboBoxCellEditor;
import nc.uap.lfw.perspective.model.Constant;

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

/**
 * 视图下单元格属性视图
 * @author zhangxya
 *
 */
public class CellPropertiesView extends Composite {

	private LFWElementPart lfwElementPart = null;
	public LFWElementPart getLfwElementPart() {
		return lfwElementPart;
	}

	public void setLfwElementPart(LFWElementPart lfwElementPart) {
		this.lfwElementPart = lfwElementPart;
	}

	public CellPropertiesView(Composite parent, int style) {
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
//		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		for (int i = 0; i < CellModifier.colNames.length; i++) {
			createColumn(tree, CellModifier.colNames[i], 80, SWT.LEFT, i);
		}
		CellPropertiesViewProvider provider = new CellPropertiesViewProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(CellModifier.colNames);
		//每个Cell的编辑Editor，todo
		CellEditor[] cellEditors = new CellEditor[CellModifier.colNames.length];
		//id,field,text,i18nname
		cellEditors[0] = new TextCellEditor(tree);;
		cellEditors[1] = new TextCellEditor(tree);
		cellEditors[2] = new TextCellEditor(tree);
		cellEditors[3] = new TextCellEditor(tree);
		//dataType
		cellEditors[4] = new ObjectComboCellEditor(tree, Constant.DATATYPE);
		//isnullable
		cellEditors[5] = new ComboBoxCellEditor(tree, Constant.ISNULLBALE);
		//defaultvalue
		cellEditors[6] = new TextCellEditor(tree);
		//isprimarykey
		cellEditors[7] = new ComboBoxCellEditor(tree, Constant.ISPRIMARYKEY);
		//编辑公式
		cellEditors[8] = new TextCellEditor(tree);
		//验证公式
		cellEditors[9] = new TextCellEditor(tree);
		//islock
		cellEditors[10] = new ComboBoxCellEditor(tree, Constant.ISLOCK);
		//字段校验类型，email、number、chn、variable、date、phone
		//cellEditors[11] = new TextCellEditor(tree);
		cellEditors[11] = new EditableComboBoxCellEditor(tree, Constant.FORMATERTYPE);
		//多语目录
		cellEditors[12] = new TextCellEditor(tree);
		cellEditors[13] = new TextCellEditor(tree);
		cellEditors[14] = new TextCellEditor(tree);
		//精度
		cellEditors[15] = new TextCellEditor(tree);
		tv.setCellEditors(cellEditors);
		LfwView widget = LFWPersTool.getCurrentWidget();
		if((widget != null && widget.getFrom() == null) || (widget == null)){
			tv.setCellModifier(new CellModifier(this));
			Action addProp = new AddFieldPropAction(this);
			Action delProp = new DelFieldPropAction(this); 
			Action moveDown = new DownFieldPropAction(this);
			Action moveUp = new UpFieldPropAction(this);
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
