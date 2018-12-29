package nc.uap.lfw.grid.action;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.grid.core.GridEditor;
import nc.uap.lfw.grid.core.GridPropertisView;
import nc.uap.lfw.lang.M_grid;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

/**
 * grid中增加操作
 * @author zhangxya
 *
 */
public class AddGridFieldAction extends Action {
	private GridPropertisView view = null;
	private class AddAttrCommand extends Command{
	private GridColumn field = null;
	private ArrayList<GridColumn> arraylist = null;
	private GridElementObj gridobj = null;
	public AddAttrCommand(GridElementObj gridobj, ArrayList<GridColumn> arraylist, GridColumn field) {
		super(M_grid.AddGridFieldAction_0);
		this.gridobj = gridobj;
		this.arraylist = arraylist;
		this.field = field;
	}
	private GridPropertisView getPropertiesView(){
		return view;
	}
	
	public void execute() {
		redo();
	}
	

	public void redo() {
		GridPropertisView view =getPropertiesView(); 
		gridobj.addProp(field);
		List<IGridColumn> gridcolumns = gridobj.getGridComp().getColumnList();
		if(gridcolumns == null){
			gridcolumns = new ArrayList<IGridColumn>();
		}
		//gridcolumns.add(field);
		gridobj.getGridComp().setColumnList(gridcolumns);
		view.getTv().setInput(arraylist);
		view.getTv().refresh();
		view.getTv().expandAll();
		
	}
}
	public AddGridFieldAction(GridPropertisView view) {
		super(M_grid.AddGridFieldAction_0);
		this.view = view;
	}
	private GridPropertisView getPropertiesView(){
		return view;
	}
	
	public void run() {
		GridColumn fi = new GridColumn();
		insertNullProp(fi);
	}
	
	@SuppressWarnings("unchecked")
	private void insertNullProp(GridColumn fi){
		GridPropertisView view =getPropertiesView();
		if(view.getLfwElementPart() != null && view.getLfwElementPart().getModel() instanceof GridElementObj){
			GridElementObj vo = (GridElementObj)view.getLfwElementPart().getModel();
			Object object = view.getTv().getInput();
			ArrayList<GridColumn> arraylist = new ArrayList<GridColumn>();
			if(object instanceof ArrayList){
				 arraylist = (ArrayList<GridColumn>)object;
			}
			arraylist.add(fi);
			AddAttrCommand addcmd = new AddAttrCommand(vo, arraylist, fi);
			if(GridEditor.getActiveEditor() != null)
				GridEditor.getActiveEditor() .executComand(addcmd);
		}
	}
}

