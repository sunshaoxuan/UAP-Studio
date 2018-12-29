package nc.lfw.editor.pagemeta.plug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import nc.lfw.editor.pagemeta.PagemetaEditor;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.lang.M_pagemeta;

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
			super(M_pagemeta.AddConnectorPropAction_0);
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
		super(M_pagemeta.AddConnectorPropAction_0);
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
		if(view.getWidget()!= null){
			Object object = view.getTv().getInput();
			UUID uuid = UUID.randomUUID();
			conn.setId(uuid.toString());
			HashMap<String,Connector> connectMap = (HashMap)PagemetaEditor.getActivePagemetaEditor().getGraph().getPagemeta().getConnectorMap();
			connectMap.put(uuid.toString(), conn);
			ArrayList<Connector> arraylist = null;
			arraylist = (ArrayList<Connector>)object;
			arraylist.add(conn);
			view.getTv().setInput(arraylist);
			view.getTv().refresh();
			view.getTv().expandAll();
	
			AddAttrCommand addcmd = new AddAttrCommand();
			if(PagemetaEditor.getActivePagemetaEditor() != null)
				PagemetaEditor.getActiveEditor().executComand(addcmd);
			}
	}
}

