package nc.uap.lfw.dataset;

import java.util.List;

import nc.uap.lfw.core.data.FieldRelation;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class ClearRefProvider extends LabelProvider implements
ITableLabelProvider, ITreeContentProvider {
	
	public ClearRefProvider(){
		super();
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List)
			return ((List) inputElement).toArray();
		else 
			return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof String)
			return true;
		else{
			return false;
		}
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		return (String)element;
	}

}
