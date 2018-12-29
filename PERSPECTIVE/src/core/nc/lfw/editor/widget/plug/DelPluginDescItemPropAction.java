package nc.lfw.editor.widget.plug;

import java.util.ArrayList;

import nc.lfw.editor.widget.WidgetEditor;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PluginDescItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 删除PluginDescItem
 * @author dingrf
 *
 */
public class DelPluginDescItemPropAction extends Action {
	private class DelCellPropCommand extends Command{
		private PluginDescItemPropertiesView view = null;
		private PluginDescElementObj obj = null;
		private PluginDescItem descItem = null;
		private ArrayList<PluginDescItem> arraylist = null;
		
		public DelCellPropCommand(ArrayList<PluginDescItem> arraylist, PluginDescElementObj obj, PluginDescItem descItem) {
			super("删除");
			this.arraylist = arraylist;
			this.obj = obj;
			this.descItem = descItem;
		}

		public void execute() {
			redo();
		}
		
		public void redo() {
			PluginDescItemPropertiesView view =getPropertiesView();
			((PluginDesc)obj.getPlugin()).removeDescItem(descItem.getId());
			//eeobj.getDs().getFieldSet().removeField(field);
			TreeViewer tv = view.getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}

		
		public void undo() {
			arraylist.add(descItem);
			//eeobj.getDs().getFieldSet().addField(field);
			TreeViewer tv = view.getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}
		
	}
	private PluginDescItemPropertiesView view = null;
	public DelPluginDescItemPropAction(PluginDescItemPropertiesView view) {
		setText("删除");
		this.view = view;
	}

	private PluginDescItemPropertiesView getPropertiesView() {
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
				Object model = getPropertiesView().getPluginDescElementPart().getModel();
				if (o instanceof PluginDescItem && model instanceof PluginDescElementObj) {
					PluginDescItem prop = (PluginDescItem) o;
					if(prop.getId()==null){
						MessageDialog.openError(null, "错误", "木有ID，请输入");
						return;
					}
					PluginDescElementObj vo = (PluginDescElementObj) model;
					Object object = view.getTv().getInput();
					ArrayList<PluginDescItem> arraylist = null;
					if(object instanceof ArrayList){
						 arraylist = (ArrayList<PluginDescItem>)object;
					}
					arraylist.remove(prop);
					DelCellPropCommand cmd = new DelCellPropCommand(arraylist, vo, prop);
//					if(WidgetEditor.getActiveEditor() != null)
//						WidgetEditor.getActiveEditor().executComand(cmd);
					WidgetEditor editor = (WidgetEditor)WidgetEditor.getActiveEditor();
					editor.setDirtyTrue();
					if(editor != null)
						editor.executComand(cmd);
				}
	
			}
		}
	}
}
