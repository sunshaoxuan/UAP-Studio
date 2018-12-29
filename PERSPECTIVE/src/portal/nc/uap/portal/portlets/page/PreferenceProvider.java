package nc.uap.portal.portlets.page;

import java.util.ArrayList;
import java.util.List;



import nc.uap.portal.container.om.Preference;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * PreferenceProvider
 * 
 * @author dingrf
 *
 */
public class PreferenceProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public PreferenceProvider() {
		super();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/**
	 * 取列显示值
	 */
	public String getColumnText(Object element, int columnIndex) { 
		switch (columnIndex) {
		case 0:
			if(element instanceof Preference){
				return ((Preference)element).getName() == null? "":((Preference)element).getName();
			}
			break;
		case 1:
			if(element instanceof Preference){
				List<String> listValue = ((Preference)element).getValues();
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
			if(element instanceof Preference){
				return ((Preference)element).isReadOnly() == true? "Y":"N";
			}
			break;
		case 3:
			if(element instanceof Preference){
				return ((Preference)element).getDescription() == null? "":((Preference)element).getDescription();
			}
			break;
		}
		return null;
	}


	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof Preference){
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
