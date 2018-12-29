package nc.uap.portal.portlets.page;

import java.util.ArrayList;
import java.util.List;

import nc.uap.portal.container.om.Supports;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * SupportsProvider
 * 
 * @author dingrf
 *
 */
public class SupportsProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public SupportsProvider() {
		super();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}


	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if(element instanceof Supports){
				return ((Supports)element).getMimeType() == null? "":((Supports)element).getMimeType();
			}
			break;
		case 1:
			if(element instanceof Supports){
				//return ((Supports)element).getPortletModes().toString();
				List<String> listValue = ((Supports)element).getPortletModes();
				String values="";
				for(int i=0;i<listValue.size();i++){
					if(i != listValue.size() -1 )
						values += listValue.get(i) + ",";
					else 
						values += listValue.get(i);
				}
				return values;
			}
			break;
		case 2:
			if(element instanceof Supports){
				List<String> listValue = ((Supports)element).getWindowStates();
				String values="";
				for(int i=0;i<listValue.size();i++){
					if(i != listValue.size() -1 )
						values += listValue.get(i) + ",";
					else 
						values += listValue.get(i);
				}
				return values;
			}
			break;
		}
		return null;
	}


	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof Supports){
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
