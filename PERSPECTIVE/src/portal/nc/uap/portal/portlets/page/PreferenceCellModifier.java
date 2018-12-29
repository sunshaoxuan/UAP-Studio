package nc.uap.portal.portlets.page;


import java.util.ArrayList;
import java.util.List;

import nc.uap.portal.container.om.Preference;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.perspective.PortalProjConstants;
import nc.uap.portal.portlets.PortletEditor;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

/**
 * PreferenceCellModifier
 * 
 * @author dingrf
 *
 */
public class PreferenceCellModifier implements ICellModifier {
	
	private PreferencePropertiesView view = null;
	
	private class CellModifiCommand extends Command{
		private String property = ""; //$NON-NLS-1$
		private Object value = null;
		private Object oldValue = null;
		private Preference attr = null;
		public CellModifiCommand(String property, Object value, Preference attr) {
			super(M_portal.PreferenceCellModifier_0);
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
	public static final String[] colNames = {M_portal.PreferenceCellModifier_1,M_portal.PreferenceCellModifier_2,M_portal.PreferenceCellModifier_3,M_portal.PreferenceCellModifier_4};
	
	
	public PreferenceCellModifier(PreferencePropertiesView view) {
		super();
		this.view = view;
	}
	
	
	public boolean canModify(Object element, String property) {
		return true;
	}

	public Object getValue(Object element, String property) {
		if(element instanceof Preference){
			Preference prop = (Preference)element;
			if(colNames[0].equals(property)){
				return prop.getName()==null? "" : prop.getName(); //$NON-NLS-1$
			}else if(colNames[1].equals(property)){
				List<String> listValue = prop.getValues();
				String values=""; //$NON-NLS-1$
				for(int i=0;i<listValue.size();i++){
					if(i != listValue.size() -1 )
						values += listValue.get(i) + ","; //$NON-NLS-1$
					else 
						values += listValue.get(i);
				}
				return values;
			}else if(colNames[2].equals(property)){
				return prop.isReadOnly()==true? Integer.valueOf(0):Integer.valueOf(1);
			}else if(colNames[3].equals(property)){
				return prop.getDescription()==null?"":prop.getDescription(); //$NON-NLS-1$
			}
		}
		return ""; //$NON-NLS-1$
	}

	public void modify(Object element, String property, Object value) {
		if(colNames[1].equals(property) &&  PortalProjConstants.PREFERENCE_VERSION.equals((String)value)){
			MessageDialog.openError(null, M_portal.PreferenceCellModifier_5, M_portal.PreferenceCellModifier_6+PortalProjConstants.PREFERENCE_VERSION);
			return;
		}
		TreeItem item = (TreeItem)element;
		Object object = item.getData();
		if(object instanceof Preference){
			Preference prop = (Preference)object;
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
			PortletEditor editor = PortletEditor.getActiveEditor();
			if(editor != null)
				editor.executComand(cmd);
		}
	}

	
	private void modifyAttr(Preference prop, String property,Object value){
		 if(colNames[0].equals(property)){
			 prop.setName((String)value);
		 }
		else if(colNames[1].equals(property)){
			String[] values = ((String)value).split(","); //$NON-NLS-1$
			List<String> valueList = new ArrayList<String>();
			for (int i=0;i<values.length;i++){
				valueList.add(values[i]);
			}
			prop.getValues().clear();
			prop.getValues().addAll(valueList);
		}
		else if(colNames[2].equals(property)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			prop.setReadOnly(truevalue);	
		}
		else if(colNames[3].equals(property)){
			 prop.setDescription((String)value);
		}
		view.getTv().update(prop, null);
	}
}

