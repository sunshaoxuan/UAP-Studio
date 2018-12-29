package nc.uap.lfw.editor.application.plug;

import java.util.ArrayList;
import java.util.UUID;

import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.editor.application.ApplicationEditor;
import nc.uap.lfw.editor.application.ApplicationObj;
import nc.uap.lfw.lang.M_application;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

/**
 * Ôö¼ÓConnector
 * @author dingrf
 *
 */
public class AddConnectorPropAction extends Action {
	
	private ConnectorPropertiesView view = null;
	
	private class AddAttrCommand extends Command{
		public AddAttrCommand() {
			super(M_application.AddConnectorPropAction_0);
		}

		public void execute() {
			redo();
		}

		public void redo() {
		}
		
		public void undo() {
		}
		
	}
	public AddConnectorPropAction(ConnectorPropertiesView view) {
		super(M_application.AddConnectorPropAction_0);
		this.view = view;
	}
	private ConnectorPropertiesView getPropertiesView(){
		return view;
	}
	
	public void run() {
		Connector conn = new Connector();
		insertNullProp(conn);
	}
	
	@SuppressWarnings("unchecked")
	private void insertNullProp(Connector conn){
		ConnectorPropertiesView view =getPropertiesView();
		if(view.getApplicationPart() != null && view.getApplicationPart().getModel() instanceof ApplicationObj){
			Object object = view.getTv().getInput();
			UUID uuid = UUID.randomUUID();
			conn.setId(uuid.toString());
			conn.setConnType(Connector.WINDOW_WINDOW);
			ApplicationEditor.getActiveEditor().getGraph().getApplication().addConnector(conn);
//			connectMap.put(uuid.toString(), conn);
			ArrayList<Connector> arraylist = null;
			arraylist = (ArrayList<Connector>)object;
			arraylist.add(conn);
			view.getTv().setInput(arraylist);
			view.getTv().refresh();
			view.getTv().expandAll();
	
			AddAttrCommand addcmd = new AddAttrCommand();
			if(ApplicationEditor.getActiveEditor() != null)
				ApplicationEditor.getActiveEditor().executComand(addcmd);
			}
	}
}

