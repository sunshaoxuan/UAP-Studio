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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * É¾³ýForm×Ö¶Î
 * @author zhangxya
 *
 */
public class DelFormElementAction extends Action {
	
	private FormPropertiesView view = null;
	
	private class DelCellPropCommand extends Command{
		private FormElementObj formobj = null;
		private List<FormElement> fields = null;
		private ArrayList<FormElement> arraylist = null;
		public DelCellPropCommand(ArrayList<FormElement> arraylist, FormElementObj formobj, List<FormElement> fields) {
			super(M_form.DelFormElementAction_0);
			this.arraylist = arraylist;
			this.formobj = formobj;
			this.fields = fields;
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			if(fields != null){
				for (int i = 0; i < fields.size(); i++) {
					formobj.getFormComp().getElementList().remove(fields.get(i));
				}
			}
			TreeViewer tv = getPropertiesView().getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}

		
		public void undo() {
			if(fields != null){
				for (int i = 0; i < fields.size(); i++) {
					arraylist.add(fields.get(i));
					formobj.getFormComp().getElementList().add(fields.get(i));
				}
			}
			TreeViewer tv = getPropertiesView().getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}
		
	}
	
	public DelFormElementAction(FormPropertiesView view) {
		setText(M_form.DelFormElementAction_1);
		this.view = view;
	}

	private FormPropertiesView getPropertiesView() {
		return view;
	}

	
	@SuppressWarnings("unchecked")
	public void run() {
		boolean tip = MessageDialog.openConfirm(null, M_form.DelFormElementAction_2, M_form.DelFormElementAction_3);
		if(tip){
			
			
			Object model = getPropertiesView().getLfwElementPart().getModel();
			CheckboxTreeViewer ctx = getPropertiesView().getCtv();
			Object[] objects = ctx.getCheckedElements();
			if(objects != null && model instanceof FormElementObj){
				FormElementObj vo = (FormElementObj) model;
				List allRemoveList = new ArrayList<FormElement>();
				Object object = view.getTv().getInput();
				ArrayList<FormElement> arraylist = null;
				if(object instanceof ArrayList){
					 arraylist = (ArrayList<FormElement>)object;
				}
				for (int i = 0; i < objects.length; i++) {
					if (objects[i] instanceof FormElement) {
						FormElement prop = (FormElement) objects[i];
						arraylist.remove(prop);
						allRemoveList.add(prop);
					}
				}
				DelCellPropCommand cmd = new DelCellPropCommand(arraylist, vo, allRemoveList);
				if(FormEditor.getActiveEditor() != null)
					FormEditor.getActiveEditor().executComand(cmd);
			}
			
			
//			TreeViewer tv = getPropertiesView().getTv();
//			Tree tree = tv.getTree();
//			TreeItem[] tis = tree.getSelection();
//			if (tis != null && tis.length > 0) {
//				TreeItem ti = tis[0];
//				Object o = ti.getData();
//				Object model = getPropertiesView().getLfwElementPart().getModel();
//				if (o instanceof FormElement && model instanceof FormElementObj) {
//					FormElement prop = (FormElement) o;
//					FormElementObj vo = (FormElementObj) model;
//					Object object = view.getTv().getInput();
//					ArrayList<FormElement> arraylist = null;
//					if(object instanceof ArrayList){
//						 arraylist = (ArrayList<FormElement>)object;
//					}
//					arraylist.remove(prop);
//					DelCellPropCommand cmd = new DelCellPropCommand(arraylist, vo, prop);
//					if(FormEditor.getActiveEditor() != null)
//						FormEditor.getActiveEditor().executComand(cmd);
//				}
		//	}

		}
	}
}
