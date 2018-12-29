package nc.lfw.editor.datasets.core;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.uap.lfw.perspective.model.DatasetElementObj;

/**
 * datasets µ×²ãÍ¼ÐÎ
 * @author zhangxya
 *
 */
public class DatasetsGraph extends LfwBaseGraph {
	private static final long serialVersionUID = -2154903434383093547L;
	private List<DatasetElementObj> dss = new ArrayList<DatasetElementObj>();
	public List<DatasetElementObj> getDss() {
		return dss;
	}


	public void setDss(List<DatasetElementObj> dss) {
		this.dss = dss;
	}



	private List<Connection> connections = new ArrayList<Connection>();
	
	public DatasetsGraph() {
		super();
	}
	

	public boolean addConns(Connection cell) {
		cell.setGrahp(this);
		boolean b = connections.add(cell);
		if (b) {
			fireStructureChange(PROP_CHILD_ADD, cell);
		}
		return b;
	}

	public boolean addRefDs(DatasetElementObj cell) {
		cell.setGraph(this);
		boolean b = dss.add(cell);
		if (b) {
			fireStructureChange(PROP_CHILD_ADD, cell);
		}
		return b;
	}
	
	public boolean removeRefDs(DatasetElementObj cell) {
		boolean b = dss.remove(cell);
		cell.setGraph(null);
		if (b) {
			fireStructureChange(PROP_CHILD_REMOVE, cell);
		}
		return b;
	}


	
	public Object getPropertyValue(Object id) {
		return null;
	}



	public void setPropertyValue(Object id, Object value) {
		
	}

}

