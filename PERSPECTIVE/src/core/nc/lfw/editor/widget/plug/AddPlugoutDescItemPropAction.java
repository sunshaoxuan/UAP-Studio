package nc.lfw.editor.widget.plug;

import java.util.ArrayList;

import nc.lfw.editor.widget.WidgetEditor;
import nc.uap.lfw.core.page.PlugoutDescItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

/**
 * 增加PlugoutDescItem
 * @author dingrf
 *
 */
public class AddPlugoutDescItemPropAction extends Action {
	
	private PlugoutDescItemPropertiesView view = null;
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
	public AddPlugoutDescItemPropAction(PlugoutDescItemPropertiesView view) {
		super("增加");
		this.view = view;
	}
	private PlugoutDescItemPropertiesView getPropertiesView(){
		return view;
	}
	
	public void run() {
		PlugoutDescItem descItem = new PlugoutDescItem();
		insertNullProp(descItem);
	}
	
	@SuppressWarnings("unchecked")
	private void insertNullProp(PlugoutDescItem descItem){
		PlugoutDescItemPropertiesView view =getPropertiesView();
		if(view.getPlugoutDescElementPart() != null && view.getPlugoutDescElementPart().getModel() instanceof PlugoutDescElementObj){
			Object object = view.getTv().getInput();
			ArrayList<PlugoutDescItem> arraylist = null;
    		arraylist = (ArrayList<PlugoutDescItem>)object;
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

