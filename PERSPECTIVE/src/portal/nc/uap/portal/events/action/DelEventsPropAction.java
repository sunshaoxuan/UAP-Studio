package nc.uap.portal.events.action;

import java.util.ArrayList;

import nc.uap.portal.container.om.EventDefinition;
import nc.uap.portal.events.EventsEditor;
import nc.uap.portal.events.EventsElementObj;
import nc.uap.portal.events.page.EventsPropertiesView;
import nc.uap.portal.lang.M_perspective;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * É¾³ýÃüÁî
 * @author dingrf
 *
 */
public class DelEventsPropAction extends Action {
	private class DelCellPropCommand extends Command{
		private EventsPropertiesView view = null;
		private EventsElementObj eeobj = null;
		private EventDefinition ed = null;
		private ArrayList<EventDefinition> arraylist = null;
		
		public DelCellPropCommand(ArrayList<EventDefinition> arraylist, EventsElementObj eeobj, EventDefinition ed) {
			super(M_perspective.DelEventsPropAction_0);
			this.arraylist = arraylist;
			this.eeobj = eeobj;
			this.ed = ed;
		}

		public void execute() {
			redo();
		}

		
		public void redo() {
			EventsPropertiesView view =getPropertiesView();
			eeobj.getEventDefinitions().remove(ed);
			//eeobj.getDs().getFieldSet().removeField(field);
			TreeViewer tv = view.getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}

		
		public void undo() {
			arraylist.add(ed);
			//eeobj.getDs().getFieldSet().addField(field);
			TreeViewer tv = view.getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}
		
	}
	private EventsPropertiesView view = null;
	public DelEventsPropAction(EventsPropertiesView view) {
		setText(M_perspective.DelEventsPropAction_0);
		this.view = view;
	}

	private EventsPropertiesView getPropertiesView() {
		return view;
	}

	
	@SuppressWarnings("unchecked")
	public void run() {
		boolean tip = MessageDialog.openConfirm(null, M_perspective.DelEventsPropAction_1, M_perspective.DelEventsPropAction_2);
		if(tip){
			TreeViewer tv = getPropertiesView().getTv();
			Tree tree = tv.getTree();
			TreeItem[] tis = tree.getSelection();
			if (tis != null && tis.length > 0) {
				TreeItem ti = tis[0];
				Object o = ti.getData();
				Object model = getPropertiesView().getEventsElementPart().getModel();
				if (o instanceof EventDefinition && model instanceof EventsElementObj) {
					EventDefinition prop = (EventDefinition) o;
					EventsElementObj vo = (EventsElementObj) model;
					Object object = view.getTv().getInput();
					ArrayList<EventDefinition> arraylist = null;
					if(object instanceof ArrayList){
						 arraylist = (ArrayList<EventDefinition>)object;
					}
					arraylist.remove(prop);
					DelCellPropCommand cmd = new DelCellPropCommand(arraylist, vo, prop);
					if(EventsEditor.getActiveEditor() != null)
						EventsEditor.getActiveEditor().executComand(cmd);
				}
	
			}
		}
	}
}
