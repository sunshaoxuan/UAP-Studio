/**
 * 
 */
package nc.uap.lfw.grid.commands;

import java.util.List;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.grid.GridGraph;
import nc.uap.lfw.grid.core.GridLevelElementObj;
import nc.uap.lfw.lang.M_grid;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * @author chouhl
 * 2011-12-15
 */
public class GridLevelCreateCommand extends Command {

	private GridLevelElementObj gridlevelobj = null;
	private boolean canUndo = true;
	private GridGraph graph = null;
	private Rectangle rect = null;

	public GridLevelCreateCommand(GridLevelElementObj gridlevelobj,GridGraph graph, Rectangle rect) {
		super();
		this.gridlevelobj = gridlevelobj;
		this.graph = graph;
		this.rect = rect;
		setLabel(M_grid.GridLevelCreateCommand_0);
	}

	
	public boolean canExecute() {
		return gridlevelobj != null && graph != null && rect != null;
	}

	public void execute() {
		List<LfwElementObjWithGraph> cells = graph.getAllCellsExceptListener();
		int size = cells.size();
		Dataset ds = null;
		for(LfwElementObjWithGraph cell : cells){
			if(cell instanceof RefDatasetElementObj){
				ds = ((RefDatasetElementObj)cell).getDs();
				break;
			}
		}
		if(ds == null){
			MessageDialog.openConfirm(null, M_grid.GridLevelCreateCommand_1, M_grid.GridLevelCreateCommand_2);
			return;
		}
		gridlevelobj.setId(ds.getId());
		gridlevelobj.setDs(ds);
		int x = 350;
		int y = 200;
		GridLevelElementObj lastrefds = null;
		for(int i=size-1; i>=0; i--){
			if(cells.get(i) instanceof GridLevelElementObj){
				lastrefds = (GridLevelElementObj)cells.get(i);
				break;
			}
		}
		if(lastrefds != null){
			x = lastrefds.getLocation().x + lastrefds.getSize().width + WEBPersConstants.BETWEEN;
			y = lastrefds.getLocation().y;
		}
		gridlevelobj.setLocation(new Point(x, y));
		redo();
	}

	public void redo() {
		graph.addCell(gridlevelobj);
	}

	public void undo() {
		graph.removeCell(gridlevelobj);
	}

	public boolean isCanUndo() {
		return canUndo;
	}

	public void setCanUndo(boolean canUndo) {
		this.canUndo = canUndo;
	}

	public boolean canUndo() {
		return isCanUndo();
	}
	
}
