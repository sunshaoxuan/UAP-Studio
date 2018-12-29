package nc.uap.lfw.perspective.ref;

import java.util.List;

import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.perspective.model.DSRelationField;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class DsFieldContentProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public DsFieldContentProvider() {
		super();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof DSRelationField){
			DSRelationField prop = (DSRelationField)element;
		switch (columnIndex) {
		case 0:
			if(element instanceof DSRelationField){
			return prop.getId()== null? null:prop.getId();
			}
			break;
		case 1:
			if(element instanceof DSRelationField){
			return prop.getField() == null? null:prop.getField() ;
			}
			break;
		case 2:
			if(element instanceof DSRelationField){
			return prop.getIsmatch()== null? null:prop.getIsmatch();
			}
			break;
		case 3:
			if(element instanceof DSRelationField){
			return prop.getIscontains()== null? null:prop.getIscontains();
			}
			break;
		case 4:
			if(element instanceof DSRelationField){
			return prop.getMatchfield()== null? null:prop.getMatchfield();
			}
			break;
		}
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
