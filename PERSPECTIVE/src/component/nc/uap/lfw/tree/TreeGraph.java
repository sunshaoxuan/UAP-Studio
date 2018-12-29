package nc.uap.lfw.tree;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.tree.core.TreeLevelElementObj;

/**
 * Ê÷±à¼­Æ÷µÄµ×²ã¿Ø¼þ
 * @author zhangxya
 *
 */
public class TreeGraph extends LfwBaseGraph {
	private static final long serialVersionUID = -2154903434383093547L;
	private RefDatasetElementObj dsobj= new RefDatasetElementObj();
	

	public RefDatasetElementObj getDsobj() {
		return dsobj;
	}


	public void setDsobj(RefDatasetElementObj dsobj) {
		this.dsobj = dsobj;
	}


	private List<Connection> connections = new ArrayList<Connection>();
	
	public TreeGraph() {
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

	public List<TreeLevelElementObj> getALlTreeLevelElements (){
		List<TreeLevelElementObj> treeLevels = new ArrayList<TreeLevelElementObj>();
		List<LfwElementObjWithGraph> cells = getCells();
		for (int i = 0; i < cells.size(); i++) {
			LfwElementObjWithGraph cell = cells.get(i);
			if(cell instanceof TreeLevelElementObj)
				treeLevels.add((TreeLevelElementObj)cell);
		}
		return treeLevels;
	}


	
	public Object getPropertyValue(Object id) {
		return null;
	}

	public void setPropertyValue(Object id, Object value) {
		
	}

}
