package nc.lfw.editor.menubar.page;

import java.util.List;

import nc.uap.lfw.core.comp.MenuItem;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * 
 * @author guoweic
 *
 */
public class MenuItemPropertiesViewProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public MenuItemPropertiesViewProvider() {
		super();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		MenuItem item = (MenuItem) element;
		switch (columnIndex) {
			case 0:
				return item.getId();
			case 1:
				return item.getText();
//			case 2:
//				return item.getOperatorStatusArray();
//			case 3:
//				return item.getBusinessStatusArray();
//			case 4:
//				return item.getOperatorVisibleStatusArray();
//			case 5:
//				return item.getBusinessVisibleStatusArray();
			case 2:
				return item.getI18nName();
			case 3: //TODO
				return null;
			case 4:
				return item.getImgIcon();
			case 5:
				return item.getImgIconOn();
			case 6:
				return item.getImgIconDisable();
			case 7:
				return String.valueOf(item.getModifiers());
			case 8:
				return item.getHotKey();
			case 9:
				return item.getDisplayHotKey();
			case 10:
				return item.isSep()?"ÊÇ":"·ñ";
			default:
				return null;
		}
	}


	public Object[] getChildren(Object parentElement) {
		return null;
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return false;
	}

	public Object[] getElements(Object inputElement) {
	if(inputElement instanceof List)
		return ((List) inputElement).toArray();
	else 
		return new Object[0];
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
