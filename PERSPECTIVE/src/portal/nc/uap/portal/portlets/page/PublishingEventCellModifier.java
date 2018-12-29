package nc.uap.portal.portlets.page;


import nc.uap.portal.container.om.EventDefinitionReference;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.portlets.PortletEditor;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

/**
 * PublishingEventCellModifier
 * 
 * @author dingrf
 *
 */
public class PublishingEventCellModifier implements ICellModifier {
	
	private PublishingEventPropertiesView view = null;
	
	private class CellModifiCommand extends Command{
		private String property = ""; //$NON-NLS-1$
		private Object value = null;
		private Object oldValue = null;
		private EventDefinitionReference attr = null;
		public CellModifiCommand(String property, Object value, EventDefinitionReference attr) {
			super(M_portal.PublishingEventCellModifier_0);
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
	
	/**列名称*/
	public static final String[] colNames = {M_portal.PublishingEventCellModifier_1};
	
	
	public PublishingEventCellModifier(PublishingEventPropertiesView view) {
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
	 * 取列值
	 */
	public Object getValue(Object element, String property) {
		if(element instanceof EventDefinitionReference){
			EventDefinitionReference prop = (EventDefinitionReference)element;
			if(colNames[0].equals(property)){
				return prop.getName()==null? "" : prop.getName(); //$NON-NLS-1$
			}
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * 修改列值
	 */
	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem)element;
		Object object = item.getData();
		if(object instanceof EventDefinitionReference){
			EventDefinitionReference prop = (EventDefinitionReference)object;
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
			PortletEditor editor = PortletEditor.getActiveEditor();
			if(editor != null)
				editor.executComand(cmd);
		}
	}

	
	private void modifyAttr(EventDefinitionReference prop, String property,Object value){
		if(colNames[0].equals(property)){
			 prop.setName((String)value);
		}
		view.getTv().update(prop, null);
	}
}

