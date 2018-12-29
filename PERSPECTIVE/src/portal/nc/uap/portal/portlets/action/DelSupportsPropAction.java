package nc.uap.portal.portlets.action;

import java.util.ArrayList;

import nc.uap.portal.container.om.Supports;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.portlets.PortletEditor;
import nc.uap.portal.portlets.PortletElementObj;
import nc.uap.portal.portlets.page.InitParamPropertiesView;
import nc.uap.portal.portlets.page.SupportsPropertiesView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * É¾³ýSupportsProp
 * @author dingrf
 *
 */
public class DelSupportsPropAction extends Action {
	private class DelCellPropCommand extends Command{
		private InitParamPropertiesView view = null;
		private PortletElementObj ptobj = null;
		private Supports supports = null;
		private ArrayList<Supports> arraylist = null;
		
		public DelCellPropCommand(ArrayList<Supports> arraylist, PortletElementObj ptobj, Supports supports) {
			super(M_portal.DelSupportsPropAction_0);
			this.arraylist = arraylist;
			this.ptobj = ptobj;
			this.supports = supports;
		}

		public void execute() {
			redo();
		}

		
		public void redo() {
			SupportsPropertiesView view =getPropertiesView();
			ptobj.getSupports().remove(supports);
			//eeobj.getDs().getFieldSet().removeField(field);
			TreeViewer tv = view.getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}

		
		public void undo() {
			arraylist.add(supports);
			//eeobj.getDs().getFieldSet().addField(field);
			TreeViewer tv = view.getTv();
			tv.setInput(arraylist);
			tv.cancelEditing();
			tv.refresh();
			tv.expandAll();
		}
		
	}
	private SupportsPropertiesView view = null;
	public DelSupportsPropAction(SupportsPropertiesView view) {
		setText(M_portal.DelSupportsPropAction_0);
		this.view = view;
	}

	private SupportsPropertiesView getPropertiesView() {
		return view;
	}

	
	@SuppressWarnings("unchecked")
	public void run() {
		boolean tip = MessageDialog.openConfirm(null, M_portal.DelSupportsPropAction_1, M_portal.DelSupportsPropAction_2);
		if(tip){
			TreeViewer tv = getPropertiesView().getTv();
			Tree tree = tv.getTree();
			TreeItem[] tis = tree.getSelection();
			if (tis != null && tis.length > 0) {
				TreeItem ti = tis[0];
				Object o = ti.getData();
				Object model = getPropertiesView().getPortletElementPart().getModel();
				if (o instanceof Supports && model instanceof PortletElementObj) {
					Supports prop = (Supports) o;
					PortletElementObj vo = (PortletElementObj) model;
					Object object = view.getTv().getInput();
					ArrayList<Supports> arraylist = null;
					if(object instanceof ArrayList){
						 arraylist = (ArrayList<Supports>)object;
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
