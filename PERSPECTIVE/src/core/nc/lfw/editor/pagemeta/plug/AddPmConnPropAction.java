package nc.lfw.editor.pagemeta.plug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import nc.lfw.editor.pagemeta.PagemetaEditor;
import nc.lfw.editor.pagemeta.RelationEditor;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.lang.M_pagemeta;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

public class AddPmConnPropAction extends Action{
	
	private PmConnPropertiesView view = null;
	
	private class AddAttrCommand extends Command{
		public AddAttrCommand() {
			super(M_pagemeta.AddPmConnPropAction_0);
		}

		public void execute() {
			redo();
		}

		public void redo() {
		}
		
		public void undo() {
		}
		
	}
	public AddPmConnPropAction(PmConnPropertiesView view) {
		super(M_pagemeta.AddPmConnPropAction_0);
		this.view = view;
	}
	private PmConnPropertiesView getPropertiesView(){
		return view;
	}
	
	public void run() {
		Connector conn = new Connector();
		insertNullProp(conn);
	}
	
	@SuppressWarnings("unchecked")
	private void insertNullProp(Connector conn){
		PmConnPropertiesView view =getPropertiesView();
		if(view.getWindow()!= null){
			Object object = view.getTv().getInput();
			UUID uuid = UUID.randomUUID();
			conn.setId(uuid.toString());
			conn.setConnType(Connector.VIEW_WINDOW);
			HashMap<String,Connector> connectMap = (HashMap)RelationEditor.getActiveRelationEditor().getGraph().getPagemeta().getConnectorMap();
			connectMap.put(uuid.toString(), conn);
			ArrayList<Connector> arraylist = null;
			arraylist = (ArrayList<Connector>)object;
			arraylist.add(conn);
			view.getTv().setInput(arraylist);
			view.getTv().refresh();
			view.getTv().expandAll();
	
			AddAttrCommand addcmd = new AddAttrCommand();
			if(RelationEditor.getActiveRelationEditor() != null)
				RelationEditor.getActiveEditor().executComand(addcmd);
			}
	}
}
