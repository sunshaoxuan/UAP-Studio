package nc.lfw.editor.widget.plug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import nc.lfw.editor.pagemeta.RelationEditor;
import nc.lfw.editor.widget.WidgetEditor;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.lang.M_pagemeta;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

public class AddWidgetConnPropAction extends Action{

	private WidgetConnPropertiesView view = null;
	
	private class AddAttrCommand extends Command{
		public AddAttrCommand() {
			super(M_pagemeta.AddWidgetConnPropAction_0);
		}

		public void execute() {
			redo();
		}

		public void redo() {
		}
		
		public void undo() {
		}
		
	}
	public AddWidgetConnPropAction(WidgetConnPropertiesView view) {
		super(M_pagemeta.AddWidgetConnPropAction_0);
		this.view = view;
	}
	private WidgetConnPropertiesView getPropertiesView(){
		return view;
	}
	
	public void run() {
		Connector conn = new Connector();
		insertNullProp(conn);
	}
	
	@SuppressWarnings("unchecked")
	private void insertNullProp(Connector conn){
		WidgetConnPropertiesView view =getPropertiesView();
		if(view.getView()!= null){
			Object object = view.getTv().getInput();
			UUID uuid = UUID.randomUUID();
			conn.setId(uuid.toString());
			conn.setConnType(Connector.INLINEWINDOW_VIEW);
//			HashMap<String,Connector> connectMap = (HashMap)RelationEditor.getActiveRelationEditor().getGraph().getPagemeta().getConnectorMap();
//			connectMap.put(uuid.toString(), conn);
			LfwView widget = WidgetEditor.getActiveWidgetEditor().getGraph().getWidget();
			widget.addConnector(conn);
			List<Connector> arraylist = null;
			arraylist = widget.getConnectorList();
			
			view.getTv().setInput(arraylist);
			view.getTv().refresh();
			view.getTv().expandAll();
	
			AddAttrCommand addcmd = new AddAttrCommand();
			if(RelationEditor.getActiveRelationEditor() != null)
				RelationEditor.getActiveEditor().executComand(addcmd);
			}
	}
}
