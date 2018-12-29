package nc.uap.lfw.toolbar.commands;

import java.util.ArrayList;

import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.lang.M_toolbar;
import nc.uap.lfw.toolbar.ToolBarEditor;
import nc.uap.lfw.toolbar.ToolBarPropertiesView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;

public class UpToolBarItemAction extends Action {
	private class AttrMoveupCommand extends Command{
		private ArrayList<ToolBarItem> arraylist = null;
		private int index = -1;
		public AttrMoveupCommand(ArrayList<ToolBarItem> arraylist, int index) {
			super(M_toolbar.UpToolBarItemAction_0);
			this.arraylist = arraylist;
			this.index = index;
		}
		
		public void execute() {
			redo();
		}
		
		public void redo() {
			if(index > 0){
				TreeViewer treeView = view.getTv();
				ToolBarItem field = arraylist.get(index);
				arraylist.remove(field);
				arraylist.add(index -1, field);
				treeView.refresh(arraylist);
			}
		}
		
		public void undo() {
			if(index < arraylist.size() -1){
				TreeViewer treeView = view.getTv();
				ToolBarItem field = arraylist.remove(index-1);
				arraylist.add(index, field);
				treeView.refresh(arraylist);
			}
		}
		
	}
	private ToolBarPropertiesView view = null;
	public UpToolBarItemAction(ToolBarPropertiesView view) {
		super(M_toolbar.UpToolBarItemAction_1);
		this.view =view;
	}

	@SuppressWarnings("unchecked")
	
	public void run() {
		TreeViewer treeView = view.getTv();
		treeView.cancelEditing();
		TreeSelection sel =(TreeSelection) treeView.getSelection();
		Object o = sel.getFirstElement();
		if(o instanceof ToolBarItem){
			ArrayList<ToolBarItem> al =(ArrayList<ToolBarItem>)(ArrayList)treeView.getInput();
			int index = al.indexOf(o);
			if(index > 0){
				AttrMoveupCommand cmd = new AttrMoveupCommand(al, index);
				if(ToolBarEditor.getActiveEditor() != null)
					ToolBarEditor.getActiveEditor().executComand(cmd);
			}
		}		
		
	}
}
