package nc.uap.lfw.form.commands;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.form.FormElementObj;
import nc.uap.lfw.form.core.FormEditor;
import nc.uap.lfw.form.core.FormPropertiesView;
import nc.uap.lfw.lang.M_form;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

/**
 * Ôö¼Óform×Ö¶Î
 * @author zhangxya
 *
 */
public class AddFormElementAction  extends Action {
	private FormPropertiesView view = null;
	private class AddAttrCommand extends Command{
	private FormElement field = null;
	private ArrayList<FormElement> arraylist = null;
	private FormElementObj formobj = null;
	public AddAttrCommand(FormElementObj formobj, ArrayList<FormElement> arraylist, FormElement field) {
		super(M_form.AddFormElementAction_0);
		this.formobj = formobj;
		this.arraylist = arraylist;
		this.field = field;
	}
	private FormPropertiesView getPropertiesView(){
		return view;
	}
	
	public void execute() {
		redo();
	}
	

	public void redo() {
		FormPropertiesView view =getPropertiesView(); 
		formobj.addProp(field);
		List<FormElement> formelements = formobj.getFormComp().getElementList();
		if(formelements == null){
			formelements = new ArrayList<FormElement>();
			formelements.add(field);
		}
		formobj.getFormComp().setElementList(formelements);
		view.getTv().setInput(arraylist);
		view.getTv().refresh();
		view.getTv().expandAll();
		
	}
}
	public AddFormElementAction(FormPropertiesView view) {
		super(M_form.AddFormElementAction_1);
		this.view = view;
	}
	private FormPropertiesView getPropertiesView(){
		return view;
	}
	
	public void run() {
		FormElement fi = new FormElement();
		insertNullProp(fi);
	}
	
	@SuppressWarnings("unchecked")
	private void insertNullProp(FormElement fi){
		//LFWDSField prop =new LFWDSField();
		FormPropertiesView view =getPropertiesView();
		if(view.getLfwElementPart() != null && view.getLfwElementPart().getModel() instanceof FormElementObj){
			FormElementObj vo = (FormElementObj)view.getLfwElementPart().getModel();
			Object object = view.getTv().getInput();
			ArrayList<FormElement> arraylist = new ArrayList<FormElement>();
			if(object instanceof ArrayList){
				 arraylist = (ArrayList<FormElement>)object;
			}
			arraylist.add(fi);
			AddAttrCommand addcmd = new AddAttrCommand(vo, arraylist, fi);
			if(FormEditor.getActiveEditor() != null)
				FormEditor.getActiveEditor().executComand(addcmd);
		}
	}
}

