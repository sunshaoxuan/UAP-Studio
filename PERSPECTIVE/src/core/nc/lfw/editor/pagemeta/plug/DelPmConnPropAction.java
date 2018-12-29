package nc.lfw.editor.pagemeta.plug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.lfw.editor.pagemeta.RelationEditor;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.editor.window.WindowObj;
import nc.uap.lfw.lang.M_pagemeta;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class DelPmConnPropAction extends Action{

	private PmConnPropertiesView view = null;
	private class DelAttrCommand extends Command{
		
		private PmConnPropertiesView view = null;
		private WindowObj obj = null;
		private Connector conn = null;
		private ArrayList<Connector> arraylist = null;
		public DelAttrCommand(ArrayList<Connector> arraylist, WindowObj obj, Connector conn) {
			super(M_pagemeta.DelPmConnPropAction_0);
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
	public DelPmConnPropAction(PmConnPropertiesView view) {
		super(M_pagemeta.DelPmConnPropAction_0);
		this.view = view;
	}
	private PmConnPropertiesView getPropertiesView() {
		return view;
	}
	@SuppressWarnings("unchecked")
	public void run() {
		boolean tip = MessageDialog.openConfirm(null, M_pagemeta.DelPmConnPropAction_1, M_pagemeta.DelPmConnPropAction_2);
		if(tip){
			TreeViewer tv = getPropertiesView().getTv();
			Tree tree = tv.getTree();
			TreeItem[] tis = tree.getSelection();
			if (tis != null && tis.length > 0) {
				TreeItem ti = tis[0];
				Object o = ti.getData();
				WindowObj model = getPropertiesView().getWindow();
				if (o instanceof Connector){
					Connector prop = (Connector) o;
					Object object = view.getTv().getInput();
					ArrayList<Connector> arraylist = null;
					if(object instanceof ArrayList){
						 arraylist = (ArrayList<Connector>)object;
					}
					arraylist.remove(prop);
					Map<String, Connector> connectMap = RelationEditor.getActiveRelationEditor().getGraph().getPagemeta().getConnectorMap();
					connectMap.remove(prop.getId());
					DelAttrCommand cmd = new DelAttrCommand(arraylist, model, prop);
					if(RelationEditor.getActiveEditor()!=null){
						RelationEditor.getActiveEditor().setDirtyTrue();
						RelationEditor.getActiveEditor().executComand(cmd);
					}
				}
			}
				
		}
	}
}
