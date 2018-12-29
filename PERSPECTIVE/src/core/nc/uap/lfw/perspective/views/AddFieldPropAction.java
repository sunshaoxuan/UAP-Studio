package nc.uap.lfw.perspective.views;

import java.util.ArrayList;

import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.editor.DataSetEditor;
import nc.uap.lfw.perspective.model.DatasetElementObj;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

/**
 * Ôö¼Ó²Ù×÷
 * @author zhangxya
 *
 */
public class AddFieldPropAction extends Action {
	
	private CellPropertiesView view = null;
	private class AddAttrCommand extends Command{
	private Field field = null;
	private ArrayList<Field> arraylist = null;
	private DatasetElementObj dsobj = null;
	public AddAttrCommand(DatasetElementObj dsobj, ArrayList<Field> arraylist, Field field) {
		super(M_perspective.AddFieldPropAction_0);
		this.dsobj = dsobj;
		this.arraylist = arraylist;
		this.field = field;
	}
	private CellPropertiesView getPropertiesView(){
		return view;
	}
	
	public void execute() {
		redo();
	}
	public void redo() {
		CellPropertiesView view =getPropertiesView(); 
		dsobj.addProp(field);
		if(dsobj.getDs().getFieldSet().getFieldCount() < arraylist.size()){
			dsobj.getDs().getFieldSet().addField(field);
		}
		view.getTv().setInput(arraylist);
		view.getTv().refresh();
		view.getTv().expandAll();
		
	}
}
	public AddFieldPropAction(CellPropertiesView view) {
		super(M_perspective.AddFieldPropAction_0);
		this.view = view;
	}
	private CellPropertiesView getPropertiesView(){
		return view;
	}
	
	public void run() {
		Field fi = new Field();
		fi.setDataType("String"); //$NON-NLS-1$
		insertNullProp(fi);
	}
	
	@SuppressWarnings("unchecked")
	private void insertNullProp(Field fi){
		//LFWDSField prop =new LFWDSField();
		CellPropertiesView view =getPropertiesView();
		if(view.getLfwElementPart() != null && view.getLfwElementPart().getModel() instanceof DatasetElementObj){
			DatasetElementObj vo = (DatasetElementObj)view.getLfwElementPart().getModel();
			Object object = view.getTv().getInput();
			ArrayList<Field> arraylist = null;
			if(object instanceof ArrayList){
				 arraylist = (ArrayList<Field>)object;
			}
			arraylist.add(fi);
			AddAttrCommand addcmd = new AddAttrCommand(vo, arraylist, fi);
			if(DataSetEditor.getActiveEditor() != null)
				DataSetEditor.getActiveEditor().executComand(addcmd);
		}
	}
}

