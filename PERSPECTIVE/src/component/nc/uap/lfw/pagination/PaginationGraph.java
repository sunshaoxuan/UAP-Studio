package nc.uap.lfw.pagination;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;

public class PaginationGraph extends LfwBaseGraph {

	private RefDatasetElementObj dsobj= new RefDatasetElementObj();
	
	public RefDatasetElementObj getDsobj() {
		return dsobj;
	}

	public void setDsobj(RefDatasetElementObj dsobj) {
		this.dsobj = dsobj;
	}

	private List<Connection> connections = new ArrayList<Connection>();
	
	public PaginationGraph(){
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
	@Override
	public Object getPropertyValue(Object id) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		// TODO 自动生成的方法存根
		
	}

}
