package nc.uap.lfw.perspective.views;

import java.util.ArrayList;

import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.MDField;
import nc.uap.lfw.core.data.PubField;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.editor.DataSetEditor;
import nc.uap.lfw.perspective.model.DatasetElementObj;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * …æ≥˝Field√¸¡Ó
 * @author zhangxya
 *
 */
public class DelFieldPropAction extends Action {
	private class DelCellPropCommand extends Command{
		private DatasetElementObj dsobj = null;
		private Field field = null;
		private ArrayList<Field> arraylist = null;
		
		public DelCellPropCommand(ArrayList<Field> arraylist, DatasetElementObj dsobj, Field field) {
			super(M_perspective.DelFieldPropAction_0);
			this.arraylist = arraylist;
			this.dsobj = dsobj;
			this.field = field;
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			if(dsobj.getDs().getFieldSet().getFieldCount() > arraylist.size()){
				dsobj.getDs().getFieldSet().removeField(field);
			}
			TreeViewer tv = getPropertiesView().getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}

		
		public void undo() {
			arraylist.add(field);
			if(dsobj.getDs().getFieldSet().getFieldCount() < arraylist.size()){
				dsobj.getDs().getFieldSet().addField(field);
			}
			TreeViewer tv = getPropertiesView().getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}
		
	}
	private CellPropertiesView view = null;
	public DelFieldPropAction(CellPropertiesView view) {
		setText(M_perspective.DelFieldPropAction_1);
		this.view = view;
	}

	private CellPropertiesView getPropertiesView() {
		return view;
	}

	
	@SuppressWarnings("unchecked")
	public void run() {
		boolean tip = MessageDialog.openConfirm(null, M_perspective.DelFieldPropAction_2, M_perspective.DelFieldPropAction_3);
		if(tip){
			TreeViewer tv = getPropertiesView().getTv();
			Tree tree = tv.getTree();
			TreeItem[] tis = tree.getSelection();
			if (tis != null && tis.length > 0) {
				TreeItem ti = tis[0];
				Object o = ti.getData();
				Object model = getPropertiesView().getLfwElementPart().getModel();
				if (!(o instanceof PubField) && !(o instanceof MDField) && o instanceof Field && model instanceof DatasetElementObj) {
					Field prop = (Field) o;
					DatasetElementObj vo = (DatasetElementObj) model;
					Object object = view.getTv().getInput();
					ArrayList<Field> arraylist = null;
					if(object instanceof ArrayList){
						 arraylist = (ArrayList<Field>)object;
					}
					arraylist.remove(prop);
					DelCellPropCommand cmd = new DelCellPropCommand(arraylist, vo, prop);
					if(DataSetEditor.getActiveEditor() != null)
						DataSetEditor.getActiveEditor().executComand(cmd);
				}
	
			}
		}
	}
}
