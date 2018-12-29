package nc.lfw.editor.widget.plug;


import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.ObjectComboCellEditor;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.uimodel.WindowConfig;

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

public class WidgetConnPropertiesView extends Composite{
	
	private boolean canEdit = false;
	
	private WidgetElementObj view = null;
	
	private ObjectComboCellEditor windowCellEditor = null;
	
	private ObjectComboCellEditor viewCellEditor = null;
	
	private ObjectComboCellEditor plugoutCellEditor = null;
	
	private ObjectComboCellEditor pluginCellEditor = null;
	
	

	public WidgetElementObj getView() {
		return view;
	}

	public void setWindow(WidgetElementObj view) {
		this.view = view;
	}

	public WidgetConnPropertiesView(Composite parent, int style,boolean canEdit, WidgetElementObj view) {
		super(parent, style);
		this.canEdit = canEdit;
		this.view = view;
		createPartControl(this);
	}
	
	private TreeViewer tv = null;
	
	public TreeViewer getTv() {
		return tv;
	}

	public void setTv(TreeViewer tv) {
		this.tv = tv;
	}

	private void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		ViewForm vf = new ViewForm(parent,SWT.NONE);
		vf.setLayout(new FillLayout());
		tv = new TreeViewer(vf,SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		Tree tree = tv.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		createColumn(tree, WidgetConnCellModifier.colNames[0], 200, SWT.LEFT, 0);
		createColumn(tree, WidgetConnCellModifier.colNames[1], 200, SWT.LEFT, 1);
		createColumn(tree, WidgetConnCellModifier.colNames[2], 200, SWT.LEFT, 2);
//		createColumn(tree, WidgetConnCellModifier.colNames[3], 200, SWT.LEFT, 3);
//		createColumn(tree, PmConnCellModifier.colNames[4], 200, SWT.LEFT, 4);
	
		WidgetConnProvider provider = new WidgetConnProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(WidgetConnCellModifier.colNames);
		CellEditor[] cellEditors = new CellEditor[WidgetConnCellModifier.colNames.length];
//		this.windowCellEditor = new ObjectComboCellEditor(tree, getSourceWindow());
//		cellEditors[0] = this.windowCellEditor;
		this.viewCellEditor = new ObjectComboCellEditor(tree, getSourceView());
		cellEditors[0] = this.viewCellEditor;
		this.plugoutCellEditor = new ObjectComboCellEditor(tree, getPlugout());
		cellEditors[1] = this.plugoutCellEditor;
		this.pluginCellEditor = new ObjectComboCellEditor(tree, getPlugin());
		cellEditors[2] = this.pluginCellEditor;
//		cellEditors[3] = new ConnectorMappingCellEditor(tree, tv);
		tv.setCellEditors(cellEditors);
		tv.setCellModifier(new WidgetConnCellModifier(this));
		if (canEdit){
			Action addProp = new AddWidgetConnPropAction(this);
			Action delProp = new DelWidgetConnPropAction(this); 
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
	
	private String[] getPlugin() {
		LfwView widget= getView().getWidget();
		PluginDesc[] pluginCells = widget.getPluginDescs();
		if(pluginCells.length > 0){
		String[] plugin = new String[pluginCells.length];
		for(int i = 0 ; i<pluginCells.length;i++){
			plugin[i] = pluginCells[i].getId();
		}
		return plugin;
		}
		else return null;
	}
	private String[] getPlugout() {
		LfwView widget= getView().getWidget();
		PlugoutDesc[] plugoutCells = widget.getPlugoutDescs();
		if(plugoutCells.length > 0){
			String[] plugout = new String[plugoutCells.length];
			for(int i = 0 ; i<plugoutCells.length;i++){
				plugout[i] = plugoutCells[i].getId();
			}
			return plugout;
		}
		else return null;
	}
	
//	private String[] getSourceWindow() {
//		PageMeta pm= getWindow().getWindow();
//		List<PageMeta> windowList = pm.getInlineWindowList();
//		if(windowList!=null&&windowList.size()>0){
//			String[] inner_windows = new String[windowList.size()];
//			int i = 0;
//			for(PageMeta innerWin:windowList){
//				inner_windows[i]=innerWin.getId();
//				i++;
//			}
//			return inner_windows;
//		}
//		return null;
//	}
	private String[] getSourceView() {
		WindowConfig[] windowList = getView().getWidget().getInlineWindows();
		if(windowList.length > 0){
			String[] windows = new String[windowList.length];
			int i = 0;
			for(WindowConfig pm : windowList){
				windows[i]=pm.getId();
				i++;
			}
			return windows;
		}
		return null;		
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

	public ObjectComboCellEditor getWindowCellEditor() {
		return windowCellEditor;
	}

	public void setWindowCellEditor(ObjectComboCellEditor windowCellEditor) {
		this.windowCellEditor = windowCellEditor;
	}

	public ObjectComboCellEditor getViewCellEditor() {
		return viewCellEditor;
	}

	public void setViewCellEditor(ObjectComboCellEditor viewCellEditor) {
		this.viewCellEditor = viewCellEditor;
	}

	public ObjectComboCellEditor getPlugoutCellEditor() {
		return plugoutCellEditor;
	}

	public void setPlugoutCellEditor(ObjectComboCellEditor plugoutCellEditor) {
		this.plugoutCellEditor = plugoutCellEditor;
	}

	public ObjectComboCellEditor getPluginCellEditor() {
		return pluginCellEditor;
	}

	public void setPluginCellEditor(ObjectComboCellEditor pluginCellEditor) {
		this.pluginCellEditor = pluginCellEditor;
	}
	
	

}

