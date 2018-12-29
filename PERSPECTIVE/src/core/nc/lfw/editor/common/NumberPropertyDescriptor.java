package nc.lfw.editor.common;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class NumberPropertyDescriptor  extends PropertyDescriptor {

	public NumberPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		// TODO Auto-generated constructor stub
	}
	
	public CellEditor createPropertyEditor(Composite parent) {
		return new NumberTextCellEditor(parent);
	}
}