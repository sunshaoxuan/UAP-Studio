package nc.uap.portal.portlets.action;

import java.util.ArrayList;

import nc.uap.portal.container.om.Supports;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.portlets.PortletEditor;
import nc.uap.portal.portlets.PortletElementObj;
import nc.uap.portal.portlets.page.SupportsPropertiesView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

/**
 * Ôö¼ÓSupportsProp
 * @author dingrf
 *
 */
public class AddSupportsPropAction extends Action {
	
	private SupportsPropertiesView view = null;
	
	private class AddAttrCommand extends Command {
		public AddAttrCommand() {
			super(M_portal.AddSupportsPropAction_0);
		}

		public void execute() {
			redo();
		}

		public void redo() {

		}
	}
	public AddSupportsPropAction(SupportsPropertiesView view) {
		super(M_portal.AddSupportsPropAction_0);
		this.view = view;
	}
	private SupportsPropertiesView getPropertiesView(){
		return view;
	}
	
	public void run() {
		Supports su = new Supports();
		su.setMimeType("text/html"); //$NON-NLS-1$
		insertNullProp(su);
	}
	
	@SuppressWarnings("unchecked")
	private void insertNullProp(Supports supports){
		SupportsPropertiesView view =getPropertiesView();
		if(view.getPortletElementPart() != null && view.getPortletElementPart().getModel() instanceof PortletElementObj){
			Object object = view.getTv().getInput();
			ArrayList<Supports> arraylist = null;
			if(object instanceof ArrayList){
				 arraylist = (ArrayList<Supports>)object;
			}
			else{
				arraylist = new ArrayList<Supports>();
			}
			arraylist.add(supports);
			view.getTv().setInput(arraylist);
			view.getTv().refresh();
			view.getTv().expandAll();
			AddAttrCommand addcmd = new AddAttrCommand();
			if(PortletEditor.getActiveEditor() != null)
				PortletEditor.getActiveEditor().executComand(addcmd);
		}
	}
}

