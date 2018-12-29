package nc.lfw.editor.menubar.page;

import java.util.List;

import nc.lfw.editor.menubar.DefaultItem;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * @author guoweic
 *
 */
public class DefaultMenuItemViewProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public DefaultMenuItemViewProvider() {
		super();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		DefaultItem item = (DefaultItem) element;
		switch (columnIndex) {
			case 0:
				return "";
			case 1:
				return item.getText();
//			case 2:
//				return item.getOperatorStatusArray();
//			case 3:
//				return item.getBusinessStatusArray();
//			case 4:
//				return item.getI18nName();
//			case 5: //TODO
//				return null;
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
