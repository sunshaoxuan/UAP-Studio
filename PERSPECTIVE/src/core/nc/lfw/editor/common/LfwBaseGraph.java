package nc.lfw.editor.common;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.TreeItem;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.uap.lfw.perspective.listener.LFWListenerGraph;
//import nc.uap.lfw.perspective.listener.ListenerElementObj;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;

/**
 * 底层图形基类
 * @author zhangxya
 *
 */
public abstract class LfwBaseGraph extends LFWListenerGraph {

	private static final long serialVersionUID = 7613644080324443105L;
	public static final String PROP_CHILD_ADD = "prop_child_add";
	public static final String PROP_CHILD_REMOVE = "prop_child_remove";
	private List<LfwElementObjWithGraph> cells = new ArrayList<LfwElementObjWithGraph>();
	private List<Connection> connections = new ArrayList<Connection>();
	
	private IProject project = null;
	private TreeItem currentTreeItem = null;
	
	public LfwBaseGraph() {
		super();
		// 设置JsListener父对象类型
		//setTargetType(JsListenerConstant.TARGET_DATASET);
		this.project = LFWPersTool.getCurrentProject();
		this.currentTreeItem = LFWPersTool.getCurrentTreeItem();
	}
	
	public List<Connection> getConnections() {
		return connections;
	}

	public void setConnections(List<Connection> connections) {
		this.connections = connections;
	}

	public boolean addConns(Connection cell) {
		cell.setGrahp(this);
		boolean b = connections.add(cell);
		if (b) {
			fireStructureChange(PROP_CHILD_ADD, cell);
		}
		return b;
	}
	
	public boolean removeConn(Connection cell) {
		boolean b = connections.remove(cell);
		cell.disConnect();
		if (b) {
			fireStructureChange(PROP_CHILD_REMOVE, cell);
		}
		return b;
	}

		
	public boolean addCell(LfwElementObjWithGraph cell) {
		cell.setGraph(this);
		boolean b = cells.add(cell);
		if (b) {
			fireStructureChange(PROP_CHILD_ADD, cell);
			elementsCount++;
		}
		return b;
	}
	
	public boolean removeCell(LfwElementObjWithGraph cell) {
		boolean b = cells.remove(cell);
		cell.setGraph(null);
		if (b) {
			fireStructureChange(PROP_CHILD_REMOVE, cell);
			elementsCount--;
		}
		return b;
	}

	public List<LfwElementObjWithGraph> getCells() {
		return cells;
	}
	
	
	public List<LfwElementObjWithGraph> getCellsExceptListener(){
		List<LfwElementObjWithGraph> list = new ArrayList<LfwElementObjWithGraph>();
		for (int i = 0; i < cells.size(); i++) {
			if((cells.get(i) instanceof RefDatasetElementObj))
				list.add(cells.get(i));
		}
		return list;
	}
	
	public List<LfwElementObjWithGraph> getAllCellsExceptListener(){
		List<LfwElementObjWithGraph> list = new ArrayList<LfwElementObjWithGraph>();
		for (int i = 0; i < cells.size(); i++) {
				list.add(cells.get(i));
		}
		return list;
	}
	
	public List<ContextMenuElementObj> getContextMenu(){
		List<ContextMenuElementObj> list = new ArrayList<ContextMenuElementObj>();
		for (int i = 0; i < cells.size(); i++) {
			if((cells.get(i) instanceof ContextMenuElementObj))
				list.add((ContextMenuElementObj)cells.get(i));
		}
		return list;
	}
	/**
	 * 取消所有图形的所有子项选中状态
	 */
	public void unSelectAllLabels() {
		// Listener图形
		List<LfwElementObjWithGraph> listenerCells = getCells();
//		for (int i = 0, n = listenerCells.size(); i < n; i++) {
//			if (listenerCells.get(i) instanceof ListenerElementObj) {
//				ListenerElementObj listenerObj = (ListenerElementObj) listenerCells.get(i);
//				listenerObj.getFigure().unSelectAllLabels();
//				listenerObj.setCurrentListener(null);
//			}
//		}
	}

	public IProject getProject() {
		return project;
	}

	public TreeItem getCurrentTreeItem() {
		return currentTreeItem;
	}
	
}
