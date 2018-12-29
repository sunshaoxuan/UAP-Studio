package nc.uap.lfw.grid;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.grid.core.GridLevelElementObj;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;

/**
 * grid µ×²ãÍ¼ÐÎ
 * @author zhangxya
 *
 */
public class GridGraph extends LfwBaseGraph {
	private static final long serialVersionUID = -2154903434383093547L;
	private RefDatasetElementObj dsobj= new RefDatasetElementObj();
	

	public RefDatasetElementObj getDsobj() {
		return dsobj;
	}


	public void setDsobj(RefDatasetElementObj dsobj) {
		this.dsobj = dsobj;
	}


	private List<Connection> connections = new ArrayList<Connection>();
	
	public GridGraph() {
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

	public List<GridLevelElementObj> getALlGridLevelElements (){
		List<GridLevelElementObj> gridLevels = new ArrayList<GridLevelElementObj>();
		List<LfwElementObjWithGraph> cells = getCells();
		for (int i = 0; i < cells.size(); i++) {
			LfwElementObjWithGraph cell = cells.get(i);
			if(cell instanceof GridLevelElementObj)
				gridLevels.add((GridLevelElementObj)cell);
		}
		return gridLevels;
	}
	
	public Object getPropertyValue(Object id) {
		return null;
	}

	public void setPropertyValue(Object id, Object value) {
		
	}

}

