package nc.uap.lfw.perspective.editor;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TableViewContentProvider implements IStructuredContentProvider {

	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List)
			return ((List) inputElement).toArray();
		else 
			return new Object[0];
	}

	public void dispose() {
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

}
