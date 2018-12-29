package nc.lfw.editor.widget.plug;

import java.util.ArrayList;

import nc.lfw.editor.widget.WidgetEditor;
import nc.uap.lfw.core.page.PluginDescItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

/**
 * 增加PluginDescItem
 * @author dingrf
 *
 */
public class AddPluginDescItemPropAction extends Action {
	
	private PluginDescItemPropertiesView view = null;
	private class AddAttrCommand extends Command{
		public AddAttrCommand() {
			super("增加");
		}

		public void execute() {
			redo();
		}

		public void redo() {
		}
		
		public void undo() {
		}
		
	}
	public AddPluginDescItemPropAction(PluginDescItemPropertiesView view) {
		super("增加");
		this.view = view;
	}
	private PluginDescItemPropertiesView getPropertiesView(){
		return view;
	}
	
	public void run() {
		PluginDescItem descItem = new PluginDescItem();
		insertNullProp(descItem);
	}
	
	@SuppressWarnings("unchecked")
	private void insertNullProp(PluginDescItem descItem){
		PluginDescItemPropertiesView view =getPropertiesView();
		if(view.getPluginDescElementPart() != null && view.getPluginDescElementPart().getModel() instanceof PluginDescElementObj){
			Object object = view.getTv().getInput();
			ArrayList<PluginDescItem> arraylist = null;
    		arraylist = (ArrayList<PluginDescItem>)object;
			arraylist.add(descItem);
			view.getTv().setInput(arraylist);
			view.getTv().refresh();
			view.getTv().expandAll();

			AddAttrCommand addcmd = new AddAttrCommand();
			if(WidgetEditor.getActiveEditor() != null)
				WidgetEditor.getActiveEditor().executComand(addcmd);
		}
	}
}

