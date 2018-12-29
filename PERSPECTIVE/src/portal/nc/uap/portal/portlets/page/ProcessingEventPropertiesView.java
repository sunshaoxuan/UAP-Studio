package nc.uap.portal.portlets.page;

import java.util.List;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.ObjectComboCellEditor;
import nc.uap.portal.container.om.EventDefinition;
import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.portlets.PortletElementPart;
import nc.uap.portal.portlets.action.AddProcessingEventPropAction;
import nc.uap.portal.portlets.action.DelProcessingEventPropAction;

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
 * ProcessingEvent 模型视图
 * 
 * @author dingrf
 *
 */
public class ProcessingEventPropertiesView extends Composite {

	private PortletElementPart portletElementPart = null;
	
	public PortletElementPart getPortletElementPart() {
		return portletElementPart;
	}

	public void setPortletElementPart(PortletElementPart portletElementPart) {
		this.portletElementPart = portletElementPart;
	}

	public ProcessingEventPropertiesView(Composite parent, int style) {
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
		createColumn(tree, ProcessingEventCellModifier.colNames[0], 200, SWT.LEFT, 0);
		ProcessingEventProvider provider = new ProcessingEventProvider();
		tv.setLabelProvider(provider);
		tv.setContentProvider(provider);
		tv.setColumnProperties(ProcessingEventCellModifier.colNames);
		//每个Cell的编辑Editor，todo
		CellEditor[] cellEditors = new CellEditor[ProcessingEventCellModifier.colNames.length];
		cellEditors[0] = new ObjectComboCellEditor(tree, getPorcessingEvents());
		tv.setCellEditors(cellEditors);
		tv.setCellModifier(new ProcessingEventCellModifier(this));
		Action addProp = new AddProcessingEventPropAction(this);
		Action delProp = new DelProcessingEventPropAction(this); 
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
	
	private String[] getPorcessingEvents() {
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		PortletApplicationDefinition  portletApp = PortalConnector.getPortletApp(projectPath,projectModuleName);
		List<EventDefinition> events = (List<EventDefinition>)portletApp.getEventDefinitions();
		String[] eventsArray = new String[events.size()];
		for (int i=0; i<events.size();i++){
			eventsArray[i]=events.get(i).getName();
		}
		return eventsArray;
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
