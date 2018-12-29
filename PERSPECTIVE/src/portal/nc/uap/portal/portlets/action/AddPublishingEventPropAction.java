package nc.uap.portal.portlets.action;

import java.util.ArrayList;

import nc.uap.portal.container.om.EventDefinitionReference;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.portlets.PortletEditor;
import nc.uap.portal.portlets.PortletElementObj;
import nc.uap.portal.portlets.page.PublishingEventPropertiesView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

/**
 * Ôö¼ÓPublishingEvent
 * @author dingrf
 *
 */
public class AddPublishingEventPropAction extends Action {
	
	private PublishingEventPropertiesView view = null;

	private class AddAttrCommand extends Command {
		public AddAttrCommand() {
			super(M_portal.AddPublishingEventPropAction_0);
		}

		public void execute() {
			redo();
		}

		public void redo() {
		}
	}
	public AddPublishingEventPropAction(PublishingEventPropertiesView view) {
		super(M_portal.AddPublishingEventPropAction_0);
		this.view = view;
	}
	private PublishingEventPropertiesView getPropertiesView(){
		return view;
	}
	
	public void run() {
		EventDefinitionReference event = new EventDefinitionReference();
		insertNullProp(event);
	}
	
	@SuppressWarnings("unchecked")
	private void insertNullProp(EventDefinitionReference event){
		PublishingEventPropertiesView view =getPropertiesView();
		if(view.getPortletElementPart() != null && view.getPortletElementPart().getModel() instanceof PortletElementObj){
			Object object = view.getTv().getInput();
			ArrayList<EventDefinitionReference> arraylist = null;
			if(object instanceof ArrayList){
				 arraylist = (ArrayList<EventDefinitionReference>)object;
			}
			else{
				arraylist = new ArrayList<EventDefinitionReference>();
			}
			arraylist.add(event);
			view.getTv().setInput(arraylist);
			view.getTv().refresh();
			view.getTv().expandAll();
			AddAttrCommand addcmd = new AddAttrCommand();
			if(PortletEditor.getActiveEditor() != null)
				PortletEditor.getActiveEditor().executComand(addcmd);
		}
	}
}

