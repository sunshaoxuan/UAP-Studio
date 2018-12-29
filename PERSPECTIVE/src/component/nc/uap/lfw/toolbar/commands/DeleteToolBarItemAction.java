package nc.uap.lfw.toolbar.commands;

import java.util.ArrayList;

import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.lang.M_toolbar;
import nc.uap.lfw.toolbar.ToolBarEditor;
import nc.uap.lfw.toolbar.ToolBarElementObj;
import nc.uap.lfw.toolbar.ToolBarPropertiesView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 *É¾³ýtoolbarItem
 * @author zhangxya
 *
 */
public class DeleteToolBarItemAction extends Action {
	private ToolBarPropertiesView view = null;
	private class DelCellPropCommand extends Command{
		private ToolBarElementObj toolbarobj = null;
		private ToolBarItem field = null;
		private ArrayList<ToolBarItem> arraylist = null;
		public DelCellPropCommand(ArrayList<ToolBarItem> arraylist, ToolBarElementObj toolbarobj, ToolBarItem field) {
			super(M_toolbar.DeleteToolBarItemAction_0);
			this.arraylist = arraylist;
			this.toolbarobj = toolbarobj;
			this.field = field;
		}
		
		public void execute() {
			redo();
		}

		public void redo() {
			toolbarobj.deleteToolBarItgem(field);
			toolbarobj.getToolbarComp().deleteElement(field);
			TreeViewer tv = getPropertiesView().getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}

		
		public void undo() {
			arraylist.add(field);
			TreeViewer tv = getPropertiesView().getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}
		
	}
	
	public DeleteToolBarItemAction(ToolBarPropertiesView view) {
		setText(M_toolbar.DeleteToolBarItemAction_1);
		this.view = view;
	}

	private ToolBarPropertiesView getPropertiesView() {
		return view;
	}

	
	@SuppressWarnings("unchecked")
	public void run() {
		boolean tip = MessageDialog.openConfirm(null, M_toolbar.DeleteToolBarItemAction_2, M_toolbar.DeleteToolBarItemAction_3);
		if(tip){
			TreeViewer tv = getPropertiesView().getTv();
			Tree tree = tv.getTree();
			TreeItem[] tis = tree.getSelection();
			if (tis != null && tis.length > 0) {
				TreeItem ti = tis[0];
				Object o = ti.getData();
				Object model = getPropertiesView().getLfwElementPart().getModel();
				if (o instanceof ToolBarItem && model instanceof ToolBarElementObj) {
					ToolBarItem prop = (ToolBarItem) o;
					ToolBarElementObj vo = (ToolBarElementObj) model;
					Object object = view.getTv().getInput();
					ArrayList<ToolBarItem> arraylist = null;
					if(object instanceof ArrayList){
						arraylist = (ArrayList<ToolBarItem>)object;
					}
					arraylist.remove(prop);
					DelCellPropCommand cmd = new DelCellPropCommand(arraylist, vo, prop);
					if(ToolBarEditor.getActiveEditor() != null)
						ToolBarEditor.getActiveEditor().executComand(cmd);
				}
			}

		}
	}
}
