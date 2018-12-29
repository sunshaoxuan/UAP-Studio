package nc.uap.portal.portlets.action;

import java.util.ArrayList;

import nc.uap.portal.container.om.InitParam;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.portlets.PortletEditor;
import nc.uap.portal.portlets.PortletElementObj;
import nc.uap.portal.portlets.page.InitParamPropertiesView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * É¾³ýInitParam
 * @author dingrf
 *
 */
public class DelInitParamPropAction extends Action {
	private class DelCellPropCommand extends Command{
		private InitParamPropertiesView view = null;
		private PortletElementObj ptobj = null;
		private InitParam initParam = null;
		private ArrayList<InitParam> arraylist = null;
		
		public DelCellPropCommand(ArrayList<InitParam> arraylist, PortletElementObj ptobj, InitParam initParam) {
			super(M_portal.DelInitParamPropAction_0);
			this.arraylist = arraylist;
			this.ptobj = ptobj;
			this.initParam = initParam;
		}

		public void execute() {
			redo();
		}

		
		public void redo() {
			InitParamPropertiesView view =getPropertiesView();
			ptobj.getInitParams().remove(initParam);
			//eeobj.getDs().getFieldSet().removeField(field);
			TreeViewer tv = view.getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}

		
		public void undo() {
			arraylist.add(initParam);
			//eeobj.getDs().getFieldSet().addField(field);
			TreeViewer tv = view.getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}
		
	}
	private InitParamPropertiesView view = null;
	public DelInitParamPropAction(InitParamPropertiesView view) {
		setText(M_portal.DelInitParamPropAction_0);
		this.view = view;
	}

	private InitParamPropertiesView getPropertiesView() {
		return view;
	}

	
	@SuppressWarnings("unchecked")
	public void run() {
		boolean tip = MessageDialog.openConfirm(null, M_portal.DelInitParamPropAction_1, M_portal.DelInitParamPropAction_2);
		if(tip){
			TreeViewer tv = getPropertiesView().getTv();
			Tree tree = tv.getTree();
			TreeItem[] tis = tree.getSelection();
			if (tis != null && tis.length > 0) {
				TreeItem ti = tis[0];
				Object o = ti.getData();
				Object model = getPropertiesView().getPortletElementPart().getModel();
				if (o instanceof InitParam && model instanceof PortletElementObj) {
					InitParam prop = (InitParam) o;
					PortletElementObj vo = (PortletElementObj) model;
					Object object = view.getTv().getInput();
					ArrayList<InitParam> arraylist = null;
					if(object instanceof ArrayList){
						 arraylist = (ArrayList<InitParam>)object;
					}
					arraylist.remove(prop);
					DelCellPropCommand cmd = new DelCellPropCommand(arraylist, vo, prop);
					if(PortletEditor.getActiveEditor() != null)
						PortletEditor.getActiveEditor().executComand(cmd);
				}
	
			}
		}
	}
}
