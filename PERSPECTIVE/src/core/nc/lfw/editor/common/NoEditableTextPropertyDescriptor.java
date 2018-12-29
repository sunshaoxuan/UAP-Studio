package nc.lfw.editor.common;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * ≤ªø…±‡º≠ Ù–‘
 * @author zhangxya
 *
 */
public class NoEditableTextPropertyDescriptor extends TextPropertyDescriptor {
	public NoEditableTextPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}
	public CellEditor createPropertyEditor(Composite parent) {
		return null;
	}
}