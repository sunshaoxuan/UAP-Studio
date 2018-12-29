package nc.uap.lfw.combodata.commands;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.combodata.ComboDataElementObj;
import nc.uap.lfw.combodata.core.ComboDataEditor;
import nc.uap.lfw.combodata.core.ComboDataPropertiesView;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.lang.M_combodata;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

/**
 * 增加静态下拉框ComItem
 * @author zhangxya
 *
 */
public class AddComboFieldAction extends Action {
	
	private ComboDataPropertiesView view = null;
	private class AddAttrCommand extends Command{
	private CombItem field = null;
	private List<CombItem> arraylist = null;
	private ComboDataElementObj combobj = null;
	public AddAttrCommand(ComboDataElementObj combobj, List<CombItem> arraylist, CombItem field) {
		super(M_combodata.AddComboFieldAction_0);
		this.combobj = combobj;
		this.arraylist = arraylist;
		this.field = field;
	}
	private ComboDataPropertiesView getPropertiesView(){
		return view;
	}
	
	public void execute() {
		redo();
	}
	
	public void redo() {
		ComboDataPropertiesView view =getPropertiesView(); 
		combobj.addProp(field);
		StaticComboData combo = (StaticComboData) combobj.getCombodata();
		combo.addCombItem(field);
		view.getTv().setInput(arraylist);
		view.getTv().refresh();
		view.getTv().expandAll();
		
	}
}
	public AddComboFieldAction(ComboDataPropertiesView view) {
		super(M_combodata.AddComboFieldAction_1);
		this.view = view;
	}
	private ComboDataPropertiesView getPropertiesView(){
		return view;
	}
	
	public void run() {
		CombItem fi = new CombItem();
		insertNullProp(fi);
	}
	
	@SuppressWarnings("unchecked")
	private void insertNullProp(CombItem fi){
		ComboDataPropertiesView view =getPropertiesView();
		if(view.getLfwElementPart() != null && view.getLfwElementPart().getModel() instanceof ComboDataElementObj){
			ComboDataElementObj vo = (ComboDataElementObj)view.getLfwElementPart().getModel();
			Object object = view.getTv().getInput();
			List<CombItem> arraylist =new ArrayList<CombItem>();
			if(object instanceof ArrayList){
				arraylist = (ArrayList<CombItem>)object;
			}
			arraylist.add(fi);
			AddAttrCommand addcmd = new AddAttrCommand(vo, arraylist, fi);
			if(ComboDataEditor.getActiveEditor() != null)
				ComboDataEditor.getActiveEditor().executComand(addcmd);
		}
	}
}

