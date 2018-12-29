package nc.uap.portal.events.page;


import nc.uap.portal.container.om.EventDefinition;
import nc.uap.portal.events.EventsEditor;
import nc.uap.portal.lang.M_portal;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;
/**
 * EventsPropertiesView用CellModifier
 * 
 * @author dingrf
 *
 */

public class EventDefinitionCellModifier implements ICellModifier {
	
	/**事件属性模型视图*/
	private EventsPropertiesView view = null;
	
	private class CellModifiCommand extends Command{
		private String property = ""; //$NON-NLS-1$
		private Object value = null;
		private Object oldValue = null;
		private EventDefinition attr = null;
		public CellModifiCommand(String property, Object value, EventDefinition attr) {
			super(M_portal.EventDefinitionCellModifier_0);
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
	
	/**列定义*/
	public static final String[] colNames = {M_portal.EventDefinitionCellModifier_1,M_portal.EventDefinitionCellModifier_2};
	
	
	public EventDefinitionCellModifier(EventsPropertiesView view) {
		super();
		this.view = view;
	}
	
	/**
	 * 是否可修改
	 */
	public boolean canModify(Object element, String property) {
		return true;
	}

	/**
	 * 取值 
	 */
	public Object getValue(Object element, String property) {
		if(element instanceof EventDefinition){
			EventDefinition prop = (EventDefinition)element;
			if(colNames[0].equals(property)){
				return prop.getName()==null? "" : prop.getName(); //$NON-NLS-1$
			}else if(colNames[1].equals(property)){
				return prop.getValueType()==null?"":prop.getValueType(); //$NON-NLS-1$
			}
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * 修改操作
	 */
	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem)element;
		Object object = item.getData();
		if(object instanceof EventDefinition){
			EventDefinition prop = (EventDefinition)object;
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
			EventsEditor editor = EventsEditor.getActiveEditor();
			if(editor != null)
				editor.executComand(cmd);
		}
	}

	
	private void modifyAttr(EventDefinition prop, String property,Object value){
		 if(colNames[0].equals(property)){
			 prop.setName((String)value);
		 }
		else if(colNames[1].equals(property)){
			 prop.setValueType((String)value);
		}
		view.getTv().update(prop, null);
	}
}

