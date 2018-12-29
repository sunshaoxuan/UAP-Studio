package nc.lfw.editor.pagemeta.plug;

import java.util.List;

import nc.uap.lfw.core.ObjectComboCellEditor;
import nc.uap.lfw.core.page.IPluginDesc;
import nc.uap.lfw.core.page.IPlugoutDesc;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginProxy;
import nc.uap.lfw.core.page.PlugoutProxy;
import nc.uap.lfw.core.uimodel.ViewConfig;
import nc.uap.lfw.editor.window.WindowObj;

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

public class PmConnPropertiesView extends Composite{
	
	private boolean canEdit = false;
	
	private WindowObj window = null;
	
	private ObjectComboCellEditor windowCellEditor = null;
	
	private ObjectComboCellEditor viewCellEditor = null;
	
	private ObjectComboCellEditor plugoutCellEditor = null;
	
	private ObjectComboCellEditor pluginCellEditor = null;
	
	

	public WindowObj getWindow() {
		return window;
	}

	public void setWindow(WindowObj window) {
		this.window = window;
	}

	public PmConnPropertiesView(Composite parent, int style,boolean canEdit, WindowObj window) {
		super(parent, style);
		this.canEdit = canEdit;
		this.window = window;
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
		createColumn(tree, PmConnCellModifier.colNames[0], 200, SWT.LEFT, 0);
		createColumn(tree, PmConnCellModifier.colNames[1], 200, SWT.LEFT, 1);
		createColumn(tree, PmConnCellModifier.colNames[2], 200, SWT.LEFT, 2);
//		createColumn(tree, PmConnCellModifier.colNames[3], 200, SWT.LEFT, 3);
//		createColumn(tree, PmConnCellModifier.colNames[4], 200, SWT.LEFT, 4);
	
		PmConnProvider provider = new PmConnProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(PmConnCellModifier.colNames);
		CellEditor[] cellEditors = new CellEditor[PmConnCellModifier.colNames.length];
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
		tv.setCellModifier(new PmConnCellModifier(this));
		if (canEdit){
			Action addProp = new AddPmConnPropAction(this);
			Action delProp = new DelPmConnPropAction(this); 
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
		LfwWindow pm= getWindow().getWindow();
		IPluginDesc[] pluginCells = pm.getPluginDescs();
		if(pluginCells.length > 0){
			String[] plugin = new String[pluginCells.length];
			for(int i = 0; i < pluginCells.length; i++){
				if(pluginCells[i] instanceof PluginProxy)
					continue;
				plugin[i] = pluginCells[i].getId();
			}
			return plugin;
		}
		else return null;
	}
	private String[] getPlugout() {
		LfwWindow pm= getWindow().getWindow();
		IPlugoutDesc[] plugoutCells = pm.getPlugoutDescs();
		if(plugoutCells.length > 0){
			String[] plugout = new String[plugoutCells.length];
			for(int i = 0; i< plugoutCells.length; i++){
				if(plugoutCells[i] instanceof PlugoutProxy)
					continue;
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
		List<ViewConfig> widgetList = getWindow().getWindow().getViewList();
		if(widgetList!=null&&widgetList.size()>0){
			String[] widgets = new String[widgetList.size()];
			int i = 0;
			for(ViewConfig widgetConf:widgetList){
				widgets[i]=widgetConf.getId();
				i++;
			}
			return widgets;
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
