package nc.uap.lfw.perspective.listener;

import java.util.List;

import nc.uap.lfw.core.data.Field;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * @author guoweic
 *
 */
public class DataChangeParamViewProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public DataChangeParamViewProvider() {
		super();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		Field field = (Field) element;
		switch (columnIndex) {
			case 0:
				return "";
			case 1:
				return field.getId();
			case 2:
				return field.getText();
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
