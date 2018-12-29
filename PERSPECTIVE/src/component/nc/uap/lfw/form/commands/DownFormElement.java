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
 * ÏÂÒÆform×Ö¶Î
 * @author zhangxya
 *
 */
public class DownFormElement extends Action {
	private class AttrMoveDownCommand extends Command{
		private ArrayList<FormElement> arraylist = null;
		private int index = -1;
		public AttrMoveDownCommand(ArrayList<FormElement> arraylist, int index) {
			super(M_form.DownFormElement_0);
			this.arraylist = arraylist;
			this.index = index;
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			if(index < arraylist.size()-1){
				TreeViewer treeView = view.getTv();
				FormElement field = arraylist.get(index);
				arraylist.remove(index);
				arraylist.add(index+1, field);
				treeView.refresh(arraylist);
			}
		}

		
		public void undo() {
			if(index > 0){
				TreeViewer treeView = view.getTv();
				FormElement field = arraylist.get(index + 1);
				arraylist.remove(field);
				arraylist.add(index, field);
				treeView.refresh(arraylist);
			}
		}
		
	}
	private FormPropertiesView view = null;
	public DownFormElement(FormPropertiesView view) {
		super(M_form.DownFormElement_1);
		this.view = view;
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
			if(index < al.size()-1){
				AttrMoveDownCommand cmd = new AttrMoveDownCommand(al, index);
				if(FormEditor.getActiveEditor() != null)
					FormEditor.getActiveEditor().executComand(cmd);
			}
		}		
		
	}

}
