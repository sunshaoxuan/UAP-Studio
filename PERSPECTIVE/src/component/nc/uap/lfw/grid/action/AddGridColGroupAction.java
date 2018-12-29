package nc.uap.lfw.grid.action;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.GridColumnGroup;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.grid.core.GridEditor;
import nc.uap.lfw.grid.core.GridPropertisView;
import nc.uap.lfw.lang.M_grid;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TreeSelection;

public class AddGridColGroupAction extends Action {
	private GridPropertisView view = null;
	private class AddAttrCommand extends Command{
	private GridColumnGroup field = null;
	private ArrayList<IGridColumn> arraylist = null;
	private GridElementObj gridobj = null;
	public AddAttrCommand(GridElementObj gridobj, ArrayList<IGridColumn> arraylist, GridColumnGroup field) {
		super(M_grid.AddGridColGroupAction_0);
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
	public AddGridColGroupAction(GridPropertisView view) {
		super(M_grid.AddGridColGroupAction_1);
		this.view = view;
	}
	private GridPropertisView getPropertiesView(){
		return view;
	}
	
	public void run() {
		GridColumnGroup fi = new GridColumnGroup();
		insertNullProp(fi);
	}
	
	@SuppressWarnings("unchecked")
	private void insertNullProp(GridColumnGroup fi){
		GridPropertisView view =getPropertiesView();
		if(view.getLfwElementPart() != null && view.getLfwElementPart().getModel() instanceof GridElementObj){
			GridElementObj vo = (GridElementObj)view.getLfwElementPart().getModel();
			Object object = view.getTv().getInput();
			ArrayList<IGridColumn> arraylist = new ArrayList<IGridColumn>();
			if(object instanceof ArrayList){
				 arraylist = (ArrayList<IGridColumn>)object;
			}
			arraylist.add(fi);
			AddAttrCommand addcmd = new AddAttrCommand(vo, arraylist, fi);
			if(GridEditor.getActiveEditor() != null)
				GridEditor.getActiveEditor() .executComand(addcmd);
		}
	}
}

