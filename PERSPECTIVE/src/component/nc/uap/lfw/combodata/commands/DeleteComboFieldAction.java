package nc.uap.lfw.combodata.commands;

import java.util.ArrayList;

import nc.uap.lfw.combodata.ComboDataElementObj;
import nc.uap.lfw.combodata.core.ComboDataEditor;
import nc.uap.lfw.combodata.core.ComboDataPropertiesView;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.lang.M_combodata;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * É¾³ý¾²Ì¬ÏÂÀ­¿òItem
 * @author zhangxya
 *
 */
public class DeleteComboFieldAction  extends Action {
	private class DelCellPropCommand extends Command{
		private ComboDataElementObj combobj = null;
		private CombItem field = null;
		private ArrayList<CombItem> arraylist = null;
		public DelCellPropCommand(ArrayList<CombItem> arraylist, ComboDataElementObj combobj, CombItem field) {
			super(M_combodata.DeleteComboFieldAction_0);
			this.arraylist = arraylist;
			this.combobj = combobj;
			this.field = field;
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			StaticComboData combo = (StaticComboData) combobj.getCombodata();
			combo.removeComboItem(field.getValue());
			TreeViewer tv = getPropertiesView().getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}

		
		public void undo() {
			arraylist.add(field);
			combobj.getCombodata().getAllCombItems();
			TreeViewer tv = getPropertiesView().getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}
		
	}
	private ComboDataPropertiesView view = null;
	public DeleteComboFieldAction(ComboDataPropertiesView view) {
		setText(M_combodata.DeleteComboFieldAction_1);
		this.view = view;
	}

	private ComboDataPropertiesView getPropertiesView() {
		return view;
	}
	
	
	@SuppressWarnings("unchecked")
	public void run() {
		boolean tip = MessageDialog.openConfirm(null, M_combodata.DeleteComboFieldAction_2, M_combodata.DeleteComboFieldAction_3);
		if(tip){
			TreeViewer tv = getPropertiesView().getTv();
			Tree tree = tv.getTree();
			TreeItem[] tis = tree.getSelection();
			if (tis != null && tis.length > 0) {
				TreeItem ti = tis[0];
				Object o = ti.getData();
				Object model = getPropertiesView().getLfwElementPart().getModel();
				if (o instanceof CombItem && model instanceof ComboDataElementObj) {
					CombItem prop = (CombItem) o;
					ComboDataElementObj vo = (ComboDataElementObj) model;
					Object object = view.getTv().getInput();
					ArrayList<CombItem> arraylist = new ArrayList<CombItem>();
					if(object instanceof ArrayList){
						arraylist = (ArrayList<CombItem>)object;
					}
					arraylist.remove(prop);
					DelCellPropCommand cmd = new DelCellPropCommand(arraylist, vo, prop);
					if(ComboDataEditor.getActiveEditor() != null)
						ComboDataEditor.getActiveEditor().executComand(cmd);
				}
	
			}
		}
	}
}
