package nc.uap.lfw.perspective.model;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.LfwBaseGraph;


/**
 * ×îµ×²ãÍ¼ÐÎ
 * @author zhangxya
 *
 */
public class DatasetGraph extends LfwBaseGraph {
	private static final long serialVersionUID = -2154903434383093547L;
	private List<RefDatasetElementObj> refds = new ArrayList<RefDatasetElementObj>();
	
	public DatasetGraph() {
		super();
	}
	
	
	public List<RefDatasetElementObj> getRefds() {
		return refds;
	}

	public void setRefds(List<RefDatasetElementObj> refds) {
		this.refds = refds;
	}
	
	public boolean addRefDs(RefDatasetElementObj cell) {
		cell.setGraph(this);
		boolean b = refds.add(cell);
		if (b) {
			fireStructureChange(PROP_CHILD_ADD, cell);
			elementsCount++;
		}
		return b;
	}
	
	public boolean removeRefDs(RefDatasetElementObj cell) {
		boolean b = refds.remove(cell);
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
