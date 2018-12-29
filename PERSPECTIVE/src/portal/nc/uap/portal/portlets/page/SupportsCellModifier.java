package nc.uap.portal.portlets.page;


import java.util.List;

import nc.uap.portal.container.om.Supports;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.portlets.PortletEditor;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

/**
 * SupportsCellModifier
 * 
 * @author dingrf
 *
 */
public class SupportsCellModifier implements ICellModifier {
	
	private SupportsPropertiesView view = null;
	
	private class CellModifiCommand extends Command{
		private String property = ""; //$NON-NLS-1$
		private Object value = null;
		private Object oldValue = null;
		private Supports attr = null;
		public CellModifiCommand(String property, Object value, Supports attr) {
			super(M_portal.SupportsCellModifier_0);
			this.property = property;
			this.value = value;
			this.attr = attr;
		}
		
		public void execute() {
			oldValue = getValue(attr, property);
			redo();
		}
		
		public void redo() {
			modifyAttr(attr, property, value);
		}
		
		public void undo() {
			modifyAttr(attr, property, oldValue);
		}
	}
	
	/**ÁÐÃû³Æ*/
	public static final String[] colNames = {M_portal.SupportsCellModifier_1,M_portal.SupportsCellModifier_2,M_portal.SupportsCellModifier_3};
	
	
	public SupportsCellModifier(SupportsPropertiesView view) {
		super();
		this.view = view;
	}
	
	
	public boolean canModify(Object element, String property) {
		return true;
	}

	public Object getValue(Object element, String property) {
		if(element instanceof Supports){
			Supports prop = (Supports)element;
			if(colNames[0].equals(property)){
				return prop.getMimeType()==null? "" : prop.getMimeType(); //$NON-NLS-1$
			}else if(colNames[1].equals(property)){
				List<String> listValue = prop.getPortletModes();
				String values=""; //$NON-NLS-1$
				for(int i=0;i<listValue.size();i++){
					if(i != listValue.size() -1 )
						values += listValue.get(i) + ","; //$NON-NLS-1$
					else 
						values += listValue.get(i);
				}
				return values;
			}else if(colNames[2].equals(property)){
				List<String> listValue = prop.getWindowStates();
				String values=""; //$NON-NLS-1$
				for(int i=0;i<listValue.size();i++){
					if(i != listValue.size() -1 )
						values += listValue.get(i) + ","; //$NON-NLS-1$
					else 
						values += listValue.get(i);
				}
				return values;
			}
		}
		return ""; //$NON-NLS-1$
	}

	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem)element;
		Object object = item.getData();
		if(object instanceof Supports){
			Supports prop = (Supports)object;
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
			PortletEditor editor = PortletEditor.getActiveEditor();
			if(editor != null)
				editor.executComand(cmd);
		}
	}

	
	private void modifyAttr(Supports prop, String property,Object value){
		 if(colNames[0].equals(property)){
			 prop.setMimeType((String)value);
		 }
		else if(colNames[1].equals(property)){
			prop.getPortletModes().clear();
			String[] values = ((String)value).split(","); //$NON-NLS-1$
			for (String v :values){
				prop.getPortletModes().add(v);
			}
		}
		else if(colNames[2].equals(property)){
			prop.getWindowStates().clear();
			String[] values = ((String)value).split(","); //$NON-NLS-1$
			for (String v :values){
				prop.getWindowStates().add(v);
			}
		}
		view.getTv().update(prop, null);
	}
}

