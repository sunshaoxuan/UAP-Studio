package nc.uap.lfw.grid.action;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.GridColumnGroup;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.grid.core.GridEditor;
import nc.uap.lfw.grid.core.GridPropertisView;
import nc.uap.lfw.lang.M_grid;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * gridÖÐÉ¾³ý²Ù×÷
 * @author zhangxya
 *
 */
public class DeleteGridFieldAction extends Action {
	private class DelCellPropCommand extends Command{
		private GridElementObj gridobj = null;
		private List<IGridColumn> fields = null;
		private ArrayList<IGridColumn> arraylist = null;
		public DelCellPropCommand(ArrayList<IGridColumn> arraylist, GridElementObj gridobj, List<IGridColumn> fields) {
			super(M_grid.DeleteGridFieldAction_0);
			this.arraylist = arraylist;
			this.gridobj = gridobj;
			this.fields = fields;
		}
		
		public void execute() {
			redo();
		}
		
		public void redo() {
			if(fields != null){
				GridComp grid = gridobj.getGridComp();
				for (int i = 0; i < fields.size(); i++) {
					if(fields.get(i) instanceof GridComp){
						grid.getColumnList().remove(fields.get(i));
					}
					else if(fields.get(i) instanceof GridColumnGroup){
						GridColumnGroup columngroup = (GridColumnGroup) fields.get(i);
						if(columngroup.getChildColumnList() != null){
							MessageDialog.openError(null, M_grid.DeleteGridFieldAction_1, M_grid.DeleteGridFieldAction_2);
							return;
						}
						grid.getColumnList().remove(fields.get(i));
					}
				}
			}
			TreeViewer tv = getPropertiesView().getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}

		
		public void undo() {
			if(fields != null){
				for (int i = 0; i < fields.size(); i++) {
					arraylist.remove(fields.get(i));
					gridobj.getGridComp().getColumnList().add(fields.get(i));
				}
			}
			TreeViewer tv = getPropertiesView().getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}
		
	}
	private GridPropertisView view = null;
	public DeleteGridFieldAction(GridPropertisView view) {
		setText(M_grid.DeleteGridFieldAction_3);
		this.view = view;
	}

	private GridPropertisView getPropertiesView() {
		return view;
	}

	
	@SuppressWarnings("unchecked")
	public void run() {
		boolean tip = MessageDialog.openConfirm(null, M_grid.DeleteGridFieldAction_1, M_grid.DeleteGridFieldAction_5);
		if(tip){
			Object model = getPropertiesView().getLfwElementPart().getModel();
			CheckboxTreeViewer ctx = getPropertiesView().getCtv();
			Object[] objects = ctx.getCheckedElements();
			if(objects != null && model instanceof GridElementObj){
				GridElementObj vo = (GridElementObj) model;
				List allRemoveList = new ArrayList<IGridColumn>();
				Object object = view.getTv().getInput();
				ArrayList<IGridColumn> arraylist = null;
				if(object instanceof ArrayList){
					 arraylist = (ArrayList<IGridColumn>)object;
				}
				for (int i = 0; i < objects.length; i++) {
					if (objects[i] instanceof IGridColumn) {
						IGridColumn prop = (IGridColumn) objects[i];
						arraylist.remove(prop);
						allRemoveList.add(prop);
					}
				}
				DelCellPropCommand cmd = new DelCellPropCommand(arraylist, vo, allRemoveList);
				if(GridEditor.getActiveEditor() != null)
					GridEditor.getActiveEditor().executComand(cmd);
			}
		}
	}
}
