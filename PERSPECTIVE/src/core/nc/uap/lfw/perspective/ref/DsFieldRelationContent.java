package nc.uap.lfw.perspective.ref;

import java.util.List;

import nc.uap.lfw.core.data.Field;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * DsFiledRelation的内容提供器，标签器
 * @author zhangxya
 *
 */
public class DsFieldRelationContent extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public DsFieldRelationContent() {
		super();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if(element instanceof Field){
			return ((Field)element).getId() == null? null:((Field)element).getId();
			}
			break;
		case 1:
			if(element instanceof Field){
			return ((Field)element).getField() == null? null:((Field)element).getField() ;
			}
			break;
		}
		return null;
	}


	public Object[] getChildren(Object parentElement) {
//		if(parentElement instanceof Field){
//			return new Object[0];
//		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if(element instanceof Field)
			return true;
		else{
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List)
			return ((List) inputElement).toArray();
		else 
			return new Object[0];
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
