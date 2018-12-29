package nc.lfw.editor.widget.plug;

import java.util.ArrayList;

import nc.lfw.editor.widget.WidgetEditor;
import nc.uap.lfw.core.page.PlugoutEmitItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

/**
 * 增加PlugoutEmitItem
 * @author dingrf
 *
 */
public class AddPlugoutEmitItemPropAction extends Action {
	
	private PlugoutEmitItemPropertiesView view = null;
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
	public AddPlugoutEmitItemPropAction(PlugoutEmitItemPropertiesView view) {
		super("增加");
		this.view = view;
	}
	private PlugoutEmitItemPropertiesView getPropertiesView(){
		return view;
	}
	
	public void run() {
		PlugoutEmitItem emitItem = new PlugoutEmitItem();
		insertNullProp(emitItem);
	}
	
	@SuppressWarnings("unchecked")
	private void insertNullProp(PlugoutEmitItem emitItem){
		PlugoutEmitItemPropertiesView view =getPropertiesView();
		if(view.getPlugoutDescElementPart() != null && view.getPlugoutDescElementPart().getModel() instanceof PlugoutDescElementObj){
			Object object = view.getTv().getInput();
			ArrayList<PlugoutEmitItem> arraylist = null;
    		arraylist = (ArrayList<PlugoutEmitItem>)object;
			arraylist.add(emitItem);
			view.getTv().setInput(arraylist);
			view.getTv().refresh();
			view.getTv().expandAll();

			AddAttrCommand addcmd = new AddAttrCommand();
			if(WidgetEditor.getActiveEditor() != null)
				WidgetEditor.getActiveEditor().executComand(addcmd);
		}
	}
}

