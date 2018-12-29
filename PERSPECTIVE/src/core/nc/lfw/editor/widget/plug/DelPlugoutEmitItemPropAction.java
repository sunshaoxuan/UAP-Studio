package nc.lfw.editor.widget.plug;

import java.util.ArrayList;

import nc.lfw.editor.widget.WidgetEditor;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutEmitItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 删除PlugoutEmitItem
 * @author dingrf
 *
 */
public class DelPlugoutEmitItemPropAction extends Action {
	private class DelCellPropCommand extends Command{
		private PlugoutEmitItemPropertiesView view = null;
		private PlugoutDescElementObj obj = null;
		private PlugoutEmitItem emitItem = null;
		private ArrayList<PlugoutEmitItem> arraylist = null;
		
		public DelCellPropCommand(ArrayList<PlugoutEmitItem> arraylist, PlugoutDescElementObj obj, PlugoutEmitItem emitItem) {
			super("删除");
			this.arraylist = arraylist;
			this.obj = obj;
			this.emitItem = emitItem;
		}

		public void execute() {
			redo();
		}
		
		public void redo() {
			PlugoutEmitItemPropertiesView view =getPropertiesView();
			((PlugoutDesc)obj.getPlugout()).removeEmitItem(emitItem.getId());
			//eeobj.getDs().getFieldSet().removeField(field);
			TreeViewer tv = view.getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}

		
		public void undo() {
			arraylist.add(emitItem);
			//eeobj.getDs().getFieldSet().addField(field);
			TreeViewer tv = view.getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}
		
	}
	private PlugoutEmitItemPropertiesView view = null;
	public DelPlugoutEmitItemPropAction(PlugoutEmitItemPropertiesView view) {
		setText("删除");
		this.view = view;
	}

	private PlugoutEmitItemPropertiesView getPropertiesView() {
		return view;
	}

	
	@SuppressWarnings("unchecked")
	public void run() {
		boolean tip = MessageDialog.openConfirm(null, "提示", "确定要删除数据吗?");
		if(tip){
			TreeViewer tv = getPropertiesView().getTv();
			Tree tree = tv.getTree();
			TreeItem[] tis = tree.getSelection();
			if (tis != null && tis.length > 0) {
				TreeItem ti = tis[0];
				Object o = ti.getData();
				Object model = getPropertiesView().getPlugoutDescElementPart().getModel();
				if (o instanceof PlugoutEmitItem && model instanceof PlugoutDescElementObj) {
					PlugoutEmitItem prop = (PlugoutEmitItem) o;
					PlugoutDescElementObj vo = (PlugoutDescElementObj) model;
					Object object = view.getTv().getInput();
					ArrayList<PlugoutEmitItem> arraylist = null;
					if(object instanceof ArrayList){
						 arraylist = (ArrayList<PlugoutEmitItem>)object;
					}
					arraylist.remove(prop);
					DelCellPropCommand cmd = new DelCellPropCommand(arraylist, vo, prop);
					WidgetEditor editor = (WidgetEditor)WidgetEditor.getActiveEditor();
					editor.setDirtyTrue();
					if(editor != null)
						editor.executComand(cmd);
				}
	
			}
		}
	}
}
