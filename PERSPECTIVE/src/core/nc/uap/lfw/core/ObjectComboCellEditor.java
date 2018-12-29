package nc.uap.lfw.core;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class ObjectComboCellEditor extends ComboBoxCellEditor {
	private Object[] items = null;

	public ObjectComboCellEditor(Composite parent, Object[] items) {
		this(parent, items, SWT.READ_ONLY);
	}

	public ObjectComboCellEditor(Composite parent, Object[] items, int style) {
		super(parent, new String[]{}, style);
		setObjectItems(items);
	}
	public void setObjectItems(Object[] items){
		this.items = items;
		int count = items == null ? 0 : items.length;
		String[] strs = new String[count];
		for (int i = 0; i < count; i++) {
			if (items[i] != null){
				strs[i] = items[i].toString();
			}else
				strs[i] = "";
		}
		setItems(strs);
		
	}
	
	protected Object doGetValue() {
		Object o = super.doGetValue();
		if (o instanceof Integer) {
			int index = ((Integer) o).intValue();
			if(index > -1)
				return items[index];
			else
				return null;
		}
		return o;
	}

	private boolean isEqual(Object o1, Object o2) {
		if (o1 == null) {
			if (o2 == null) {
				return true;
			} else {
				return false;
			}
		} else {
			return o1.equals(o2);
		}
	}

	
	protected void doSetValue(Object value) {
		int count = this.items == null ? 0 : items.length;
		Integer integer = Integer.valueOf(-1);
		for (int i = 0; i < count; i++) {
			if (isEqual(value, items[i])) {
				integer = Integer.valueOf(i);
				break;
			}
		}
		super.doSetValue(integer);
	}

}
