package nc.uap.lfw.core;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ObjectComboPropertyDescriptor extends PropertyDescriptor {
	private Object[] items = null;
	public ObjectComboPropertyDescriptor(Object id, String displayName,Object[] items) {
		super(id, displayName);
		this.items = items;
	}
	
	public CellEditor createPropertyEditor(Composite parent) {
		return new ObjectComboCellEditor(parent, items);
	}

}
