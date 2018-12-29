package nc.uap.lfw.toolbar.commands;

import java.util.ArrayList;

import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.lang.M_toolbar;
import nc.uap.lfw.toolbar.ToolBarEditor;
import nc.uap.lfw.toolbar.ToolBarElementObj;
import nc.uap.lfw.toolbar.ToolBarPropertiesView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

public class AddToolBarItemAction extends Action {
	private ToolBarPropertiesView view = null;
	private ToolBarPropertiesView getPropertiesView(){
		return view;
	}
	private class AddAttrCommand extends Command{
	private ToolBarItem field = null;
	private ArrayList<ToolBarItem> arraylist = null;
	private ToolBarElementObj toolbarobj = null;
	public AddAttrCommand(ToolBarElementObj toolbarobj, ArrayList<ToolBarItem> arraylist, ToolBarItem field) {
		super(M_toolbar.AddToolBarItemAction_0);
		this.toolbarobj = toolbarobj;
		this.arraylist = arraylist;
		this.field = field;
	}
	private ToolBarPropertiesView getPropertiesView(){
		return view;
	}
	
	public void execute() {
		redo();
	}
	

	public void redo() {
		ToolBarPropertiesView view =getPropertiesView(); 
		toolbarobj.addToolBarItem(field);
		toolbarobj.getToolbarComp().addElement(field);
		view.getTv().setInput(arraylist);
		view.getTv().refresh();
		view.getTv().expandAll();
		
	}
}
	public AddToolBarItemAction(ToolBarPropertiesView view) {
		super(M_toolbar.AddToolBarItemAction_0);
		this.view = view;
	}
	
	
	public void run() {
		ToolBarItem fi = new ToolBarItem();
		insertNullProp(fi);
	}
	
	@SuppressWarnings("unchecked")
	private void insertNullProp(ToolBarItem fi){
		ToolBarPropertiesView view =getPropertiesView();
		if(view.getLfwElementPart() != null && view.getLfwElementPart().getModel() instanceof ToolBarElementObj){
			ToolBarElementObj vo = (ToolBarElementObj)view.getLfwElementPart().getModel();
			Object object = view.getTv().getInput();
			ArrayList<ToolBarItem> arraylist = new ArrayList<ToolBarItem>();
			if(object instanceof ArrayList){
				 arraylist = (ArrayList<ToolBarItem>)object;
			}
			arraylist.add(fi);
			AddAttrCommand addcmd = new AddAttrCommand(vo, arraylist, fi);
			if(ToolBarEditor.getActiveEditor() != null)
				ToolBarEditor.getActiveEditor().executComand(addcmd);
		}
	}
}

