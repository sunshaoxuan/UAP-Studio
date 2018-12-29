package nc.uap.portal.events.page;

import nc.uap.portal.events.EventsElementPart;
import nc.uap.portal.events.action.AddEventsPropAction;
import nc.uap.portal.events.action.DelEventsPropAction;

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
 * 事件属性模型视图
 * 
 * @author dingrf
 *
 */
public class EventsPropertiesView extends Composite {

	/**事件元素*/
	private EventsElementPart eventsElementPart = null;
	
	public EventsElementPart getEventsElementPart() {
		return eventsElementPart;
	}

	public void setEventsElementPart(EventsElementPart eventsElementPart) {
		this.eventsElementPart = eventsElementPart;
	}

	public EventsPropertiesView(Composite parent, int style) {
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
		createColumn(tree, EventDefinitionCellModifier.colNames[0], 200, SWT.LEFT, 0);
		createColumn(tree, EventDefinitionCellModifier.colNames[1], 200, SWT.LEFT, 1);
		EventDefinitionProvider provider = new EventDefinitionProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(EventDefinitionCellModifier.colNames);
		CellEditor[] cellEditors = new CellEditor[EventDefinitionCellModifier.colNames.length];
		cellEditors[0] = new TextCellEditor(tree);;
		cellEditors[1] = new TextCellEditor(tree);
		tv.setCellEditors(cellEditors);
		tv.setCellModifier(new EventDefinitionCellModifier(this));
		Action addProp = new AddEventsPropAction(this);
		Action delProp = new DelEventsPropAction(this); 
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
