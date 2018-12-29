package nc.uap.lfw.grid.action;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridColumnGroup;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.grid.core.GridEditor;
import nc.uap.lfw.grid.core.GridPropertisView;
import nc.uap.lfw.lang.M_grid;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * ÏÂÒÆgrid ×Ö¶Î²Ù×÷
 * @author zhangxya
 *
 */
public class DownGridFieldAction extends Action {
	private class AttrMoveDownCommand extends Command{
		private ArrayList<IGridColumn> arraylist = null;
		private int index = -1;
		private int groupindex = -1;
		private List<IGridColumn> groupList = null;
		public AttrMoveDownCommand(ArrayList<IGridColumn> arraylist, List<IGridColumn> groupList, int index, int groupindex) {
			super(M_grid.DownGridFieldAction_0);
			this.arraylist = arraylist;
			this.index = index;
			this.groupindex = groupindex;
			this.groupList = groupList;
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			TreeViewer treeView = view.getTv();
			if(index != -1  && index < arraylist.size()-1){
				IGridColumn field = arraylist.get(index);
				arraylist.remove(index);
				arraylist.add(index+1, field);
				treeView.refresh(arraylist);
			}
			else if(groupindex != -1 && groupindex < groupList.size() -1){
				IGridColumn field = groupList.get(groupindex);
				groupList.remove(groupindex);
				groupList.add(groupindex + 1, field);
				treeView.refresh(groupList);
			}
		}

		
		public void undo() {
			if(index > 0){
				TreeViewer treeView = view.getTv();
				IGridColumn field = arraylist.get(index + 1);
				arraylist.remove(field);
				arraylist.add(index, field);
				treeView.refresh(arraylist);
			}
		}
		
	}
	private GridPropertisView view = null;
	public DownGridFieldAction(GridPropertisView view) {
		super(M_grid.DownGridFieldAction_1);
		this.view = view;
	}

	@SuppressWarnings("unchecked")
	
	public void run() {
		TreeViewer treeView = view.getTv();
		treeView.cancelEditing();
		TreeSelection sel =(TreeSelection) treeView.getSelection();
		Object o = sel.getFirstElement();
		if(o instanceof GridColumn){
			ArrayList<IGridColumn> al =(ArrayList<IGridColumn>)(ArrayList)treeView.getInput();
			List<IGridColumn> groupal = new ArrayList<IGridColumn>();
			int index = al.indexOf(o);
			int groupindex = -1;
			if(index < 0){
				if(o instanceof GridColumn){
					GridColumn gridc = (GridColumn) o;
					if(gridc.getColmngroup() != null && !gridc.getColmngroup().equals("")){ //$NON-NLS-1$
						String groupcolumn = gridc.getColmngroup();
						for (int i = 0; i < al.size(); i++) {
							if(al.get(i) instanceof GridColumnGroup){
								GridColumnGroup col = (GridColumnGroup) al.get(i);
								groupal = col.getChildColumnList();
								if(col.getId().equals(groupcolumn)){
									groupindex = col.getChildColumnList().indexOf(o);
									break;
								}
							}
						}
					}
				}
			}

			if(index < al.size()-1 || groupindex < groupal.size() - 1){
				AttrMoveDownCommand cmd = new AttrMoveDownCommand(al, groupal, index, groupindex);
				if(GridEditor.getActiveEditor() != null)
					GridEditor.getActiveEditor().executComand(cmd);
			}
		}		
		
	}

}
