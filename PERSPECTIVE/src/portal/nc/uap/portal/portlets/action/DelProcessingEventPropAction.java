package nc.uap.portal.portlets.action;

import java.util.ArrayList;

import nc.uap.portal.container.om.EventDefinitionReference;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.portlets.PortletEditor;
import nc.uap.portal.portlets.PortletElementObj;
import nc.uap.portal.portlets.page.ProcessingEventPropertiesView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * É¾³ýProcessingProp
 * @author dingrf
 *
 */
public class DelProcessingEventPropAction extends Action {
	private class DelCellPropCommand extends Command{
		private ProcessingEventPropertiesView view = null;
		private PortletElementObj ptobj = null;
		private EventDefinitionReference event = null;
		private ArrayList<EventDefinitionReference> arraylist = null;
		
		public DelCellPropCommand(ArrayList<EventDefinitionReference> arraylist, PortletElementObj ptobj, EventDefinitionReference event) {
			super(M_portal.DelProcessingEventPropAction_0);
			this.arraylist = arraylist;
			this.ptobj = ptobj;
			this.event = event;
		}

		public void execute() {
			redo();
		}

		
		public void redo() {
			ProcessingEventPropertiesView view =getPropertiesView();
			ptobj.getSupportedProcessingEvents().remove(event);
			//eeobj.getDs().getFieldSet().removeField(field);
			TreeViewer tv = view.getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}

		
		public void undo() {
			arraylist.add(event);
			//eeobj.getDs().getFieldSet().addField(field);
			TreeViewer tv = view.getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}
		
	}
	private ProcessingEventPropertiesView view = null;
	public DelProcessingEventPropAction(ProcessingEventPropertiesView view) {
		setText(M_portal.DelProcessingEventPropAction_0);
		this.view = view;
	}

	private ProcessingEventPropertiesView getPropertiesView() {
		return view;
	}

	
	@SuppressWarnings("unchecked")
	public void run() {
		boolean tip = MessageDialog.openConfirm(null, M_portal.DelProcessingEventPropAction_1, M_portal.DelProcessingEventPropAction_2);
		if(tip){
			TreeViewer tv = getPropertiesView().getTv();
			Tree tree = tv.getTree();
			TreeItem[] tis = tree.getSelection();
			if (tis != null && tis.length > 0) {
				TreeItem ti = tis[0];
				Object o = ti.getData();
				Object model = getPropertiesView().getPortletElementPart().getModel();
				if (o instanceof EventDefinitionReference && model instanceof PortletElementObj) {
					EventDefinitionReference prop = (EventDefinitionReference) o;
					PortletElementObj vo = (PortletElementObj) model;
					Object object = view.getTv().getInput();
					ArrayList<EventDefinitionReference> arraylist = null;
					if(object instanceof ArrayList){
						 arraylist = (ArrayList<EventDefinitionReference>)object;
					}
					arraylist.remove(prop);
					DelCellPropCommand cmd = new DelCellPropCommand(arraylist, vo, prop);
					if(PortletEditor.getActiveEditor() != null)
						PortletEditor.getActiveEditor().executComand(cmd);
				}
	
			}
		}
	}
}
