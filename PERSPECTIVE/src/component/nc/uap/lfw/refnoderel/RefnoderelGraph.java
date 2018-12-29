package nc.uap.lfw.refnoderel;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.LfwBaseGraph;
import nc.uap.lfw.refnode.RefNodeElementObj;

public class RefnoderelGraph extends LfwBaseGraph {
	private static final long serialVersionUID = -2154903434383093547L;
		
	private List<DatasetFieldElementObj> mainRefNodeList = new ArrayList<DatasetFieldElementObj>();
	
	private RefNodeElementObj detailRefNode;
	
	public RefNodeElementObj getDetailRefNode() {
		return detailRefNode;
	}

	
	public void setDetailRefNode(RefNodeElementObj detailRefNode) {
		this.detailRefNode = detailRefNode;
		fireStructureChange(PROP_CHILD_ADD, detailRefNode);
	}

	public List<DatasetFieldElementObj> getMainRefNodeList() {
		return mainRefNodeList;
	}

	public void setMainRefNodeList(List<DatasetFieldElementObj> mainRefNodeList) {
		this.mainRefNodeList = mainRefNodeList;
	}

	public RefnoderelGraph() {
		super();
	}
	
	public boolean addMainRefNode(DatasetFieldElementObj cell) {
		cell.setGraph(this);
		boolean b = mainRefNodeList.add(cell);
		if (b) {
			fireStructureChange(PROP_CHILD_ADD, cell);
			elementsCount++;
		}
		return b;
	}
	
	public boolean removeMainRefNode(DatasetFieldElementObj cell) {
		boolean b = mainRefNodeList.remove(cell);
		cell.setGraph(null);
		if (b) {
			fireStructureChange(PROP_CHILD_REMOVE, cell);
			elementsCount--;
		}
		return b;
	}

	
	public Object getPropertyValue(Object id) {
		return null;
	}

	public void setPropertyValue(Object id, Object value) {
		
	}

}

