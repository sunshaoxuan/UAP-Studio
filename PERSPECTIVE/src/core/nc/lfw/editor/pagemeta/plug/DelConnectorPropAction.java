package nc.lfw.editor.pagemeta.plug;

import java.util.ArrayList;
import java.util.HashMap;

import nc.lfw.editor.pagemeta.PagemetaEditor;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.lang.M_parts;


import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * É¾³ýConnector
 * @author dingrf
 *
 */
public class DelConnectorPropAction extends Action {
	private class DelCellPropCommand extends Command{
		private ConnectorPropertiesView view = null;
		private WidgetElementObj obj = null;
		private Connector conn = null;
		private ArrayList<Connector> arraylist = null;
		
		public DelCellPropCommand(ArrayList<Connector> arraylist, WidgetElementObj obj, Connector conn) {
			super(M_parts.DelConnectorPropAction_0);
			this.arraylist = arraylist;
			this.obj = obj;
			this.conn = conn;
		}

		public void execute() {
			redo();
		}
		
		public void redo() {
			ConnectorPropertiesView view = getPropertiesView();
			obj.getConnectorList().remove(conn);
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
	private ConnectorPropertiesView view = null;
	public DelConnectorPropAction(ConnectorPropertiesView view) {
		setText(M_parts.DelConnectorPropAction_0);
		this.view = view;
	}

	private ConnectorPropertiesView getPropertiesView() {
		return view;
	}

	
	@SuppressWarnings("unchecked")
	public void run() {
		boolean tip = MessageDialog.openConfirm(null, M_parts.DelConnectorPropAction_1, M_parts.DelConnectorPropAction_2);
		if(tip){
			TreeViewer tv = getPropertiesView().getTv();
			Tree tree = tv.getTree();
			TreeItem[] tis = tree.getSelection();
			if (tis != null && tis.length > 0) {
				TreeItem ti = tis[0];
				Object o = ti.getData();
				Object model = getPropertiesView().getWidget();
				if (o instanceof Connector && model instanceof WidgetElementObj) {
					Connector prop = (Connector) o;
					WidgetElementObj vo = (WidgetElementObj) model;
					Object object = view.getTv().getInput();
					ArrayList<Connector> arraylist = null;
					if(object instanceof ArrayList){
						 arraylist = (ArrayList<Connector>)object;
					}
					arraylist.remove(prop);
					HashMap<String,Connector> connectMap = (HashMap)PagemetaEditor.getActivePagemetaEditor().getGraph().getPagemeta().getConnectorMap();
					connectMap.remove(prop.getId());
					DelCellPropCommand cmd = new DelCellPropCommand(arraylist, vo, prop);
					if(PagemetaEditor.getActiveEditor() != null)
						PagemetaEditor.getActiveEditor().setDirtyTrue();
						PagemetaEditor.getActiveEditor().executComand(cmd);
				}
	
			}
		}
	}
}
