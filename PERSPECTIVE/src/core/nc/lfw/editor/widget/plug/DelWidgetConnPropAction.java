package nc.lfw.editor.widget.plug;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.widget.WidgetEditor;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.lang.M_parts;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class DelWidgetConnPropAction extends Action{
	
	private WidgetConnPropertiesView view = null;
	
private class DelAttrCommand extends Command{
		
		private WidgetConnPropertiesView view = null;
		private WidgetElementObj obj = null;
		private Connector conn = null;
		private ArrayList<Connector> arraylist = null;
		public DelAttrCommand(ArrayList<Connector> arraylist, WidgetElementObj obj, Connector conn) {
			super(M_parts.DelWidgetConnPropAction_0);
			this.arraylist = arraylist;
			this.obj = obj;
			this.conn = conn;
		}

		public void execute() {
			redo();
		}

		public void redo() {
			view = getPropertiesView();
//			obj.getConnectorList().remove(conn);
			TreeViewer tv = view.getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}
		
		public void undo() {
			arraylist.add(conn);
			//eeobj.getDs().getFieldSet().addField(field);
			TreeViewer tv = view.getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}
		
	}
	public DelWidgetConnPropAction(WidgetConnPropertiesView view) {
		super(M_parts.DelWidgetConnPropAction_0);
		this.view = view;
	}
	private WidgetConnPropertiesView getPropertiesView(){
		return view;
	}
	
	public void run() {
		boolean tip = MessageDialog.openConfirm(null, M_parts.DelWidgetConnPropAction_1, M_parts.DelWidgetConnPropAction_2);
		if(tip){
			TreeViewer tv = getPropertiesView().getTv();
			Tree tree = tv.getTree();
			TreeItem[] tis = tree.getSelection();
			if (tis != null && tis.length > 0) {
				TreeItem ti = tis[0];
				Object o = ti.getData();
				WidgetElementObj model = getPropertiesView().getView();
				if (o instanceof Connector){
					Connector prop = (Connector) o;
					Object object = view.getTv().getInput();
					ArrayList<Connector> arraylist = null;
					if(object instanceof ArrayList){
						 arraylist = (ArrayList<Connector>)object;
					}
					arraylist.remove(prop);
					List<Connector> connList = WidgetEditor.getActiveWidgetEditor().getGraph().getWidget().getConnectorList();
					connList.remove(prop);
					DelAttrCommand cmd = new DelAttrCommand(arraylist, model, prop);
					if(WidgetEditor.getActiveEditor()!=null){
						WidgetEditor.getActiveEditor().setDirtyTrue();
						WidgetEditor.getActiveEditor().executComand(cmd);
					}
				}
			}
		}
	}

}
