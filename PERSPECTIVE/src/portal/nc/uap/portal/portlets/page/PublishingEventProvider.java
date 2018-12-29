package nc.uap.portal.portlets.page;

import java.util.ArrayList;
import java.util.List;

import nc.uap.portal.container.om.EventDefinitionReference;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * PublishingEventProvider
 * 
 * @author dingrf
 *
 */
public class PublishingEventProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public PublishingEventProvider() {
		super();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}


	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if(element instanceof EventDefinitionReference){
				return ((EventDefinitionReference)element).getName() == null? "":((EventDefinitionReference)element).getName() ;
			}
			break;
		}
		return null;
	}


	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof EventDefinitionReference){
			return new Object[0];
		}
		else return new Object[0];
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if(element instanceof ArrayList)
			return true;
		else
			return false;
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
