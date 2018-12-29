package nc.uap.portal.events.action;

import java.util.ArrayList;

import nc.uap.portal.container.om.EventDefinition;
import nc.uap.portal.events.EventsEditor;
import nc.uap.portal.events.EventsElementObj;
import nc.uap.portal.events.page.EventsPropertiesView;
import nc.uap.portal.lang.M_portal;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

/**
 * 事件增加操作
 * 
 * @author dingrf
 */
public class AddEventsPropAction extends Action {
	
	/**
	 * 模型属性视图
	 */
	private EventsPropertiesView view = null;
	
	@SuppressWarnings("unused")
	private class AddAttrCommand extends Command {
		private EventDefinition ed = null;
		
		private ArrayList<EventDefinition> arraylist = null;
		
		private EventsElementObj eeobj = null;

		public AddAttrCommand(EventsElementObj eeobj,
				ArrayList<EventDefinition> arraylist, EventDefinition ed) {
			super(M_portal.AddEventsPropAction_0);
			this.eeobj = eeobj;
			this.arraylist = arraylist;
			this.ed = ed;
		}

		private EventsPropertiesView getPropertiesView() {
			return view;
		}

		public void execute() {
			redo();
		}

		public void redo() {
			EventsPropertiesView view = getPropertiesView();
			view.getTv().setInput(arraylist);
			view.getTv().refresh();
			view.getTv().expandAll();

		}
	}
	
	public AddEventsPropAction(EventsPropertiesView view) {
		super(M_portal.AddEventsPropAction_0);
		this.view = view;
	}
	
	private EventsPropertiesView getPropertiesView(){
		return view;
	}
	
	public void run() {
		EventDefinition ed = new EventDefinition();
		insertNullProp(ed);
	}
	
	/**
	 *新增属性 
	 */
	@SuppressWarnings("unchecked")
	private void insertNullProp(EventDefinition ed){
		EventsPropertiesView view =getPropertiesView();
		if(view.getEventsElementPart() != null && view.getEventsElementPart().getModel() instanceof EventsElementObj){
			EventsElementObj vo = (EventsElementObj)view.getEventsElementPart().getModel();
			Object object = view.getTv().getInput();
			ArrayList<EventDefinition> arraylist = null;
			if(object instanceof ArrayList){
				 arraylist = (ArrayList<EventDefinition>)object;
			}
			else{
				arraylist = new ArrayList<EventDefinition>();
			}
			arraylist.add(ed);
			AddAttrCommand addcmd = new AddAttrCommand(vo, arraylist, ed);
			if(EventsEditor.getActiveEditor() != null)
				EventsEditor.getActiveEditor().executComand(addcmd);

		}
	}
}

