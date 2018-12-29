package nc.uap.lfw.form.commands;

import java.util.ArrayList;

import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.form.core.FormEditor;
import nc.uap.lfw.form.core.FormPropertiesView;
import nc.uap.lfw.lang.M_form;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * ÉÏÒÆform×Ö¶Î
 * @author zhangxya
 *
 */
public class UPFormElementAction  extends Action {
	private class AttrMoveupCommand extends Command{
		private ArrayList<FormElement> arraylist = null;
		private int index = -1;
		public AttrMoveupCommand(ArrayList<FormElement> arraylist, int index) {
			super(M_form.UPFormElementAction_0);
			this.arraylist = arraylist;
			this.index = index;
		}
		
		public void execute() {
			redo();
		}
		
		public void redo() {
			if(index > 0){
				TreeViewer treeView = view.getTv();
				FormElement field = arraylist.get(index);
				arraylist.remove(field);
				arraylist.add(index -1, field);
				treeView.refresh(arraylist);
			}
		}
		
		public void undo() {
			if(index < arraylist.size() -1){
				TreeViewer treeView = view.getTv();
				FormElement field = arraylist.remove(index-1);
				arraylist.add(index, field);
				treeView.refresh(arraylist);
			}
		}
		
	}
	private FormPropertiesView view = null;
	public UPFormElementAction(FormPropertiesView view) {
		super(M_form.UPFormElementAction_1);
		this.view =view;
	}

	@SuppressWarnings("unchecked")
	
	public void run() {
		TreeViewer treeView = view.getTv();
		treeView.cancelEditing();
		TreeSelection sel =(TreeSelection) treeView.getSelection();
		Object o = sel.getFirstElement();
		if(o instanceof FormElement){
			ArrayList<FormElement> al =(ArrayList<FormElement>)(ArrayList)treeView.getInput();
			int index = al.indexOf(o);
			if(index > 0){
				AttrMoveupCommand cmd = new AttrMoveupCommand(al, index);
				if(FormEditor.getActiveEditor() != null)
					FormEditor.getActiveEditor().executComand(cmd);
			}
		}		
		
	}
}
