package nc.uap.portal.portlets.page;

import java.util.ArrayList;
import java.util.List;


import nc.uap.portal.container.om.InitParam;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * InitParamProvider
 * 
 * @author dingrf
 *
 */
public class InitParamProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public InitParamProvider() {
		super();
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if(element instanceof InitParam){
				return ((InitParam)element).getParamName() == null? "":((InitParam)element).getParamName();
			}
			break;
		case 1:
			if(element instanceof InitParam){
				return ((InitParam)element).getParamValue() == null? "":((InitParam)element).getParamValue() ;
			}
			break;
		case 2:
			if(element instanceof InitParam){
				if (((InitParam)element).getDescriptions().size()<=0){
					((InitParam)element).addDescription(null);
					return "";
				}else{
					return ((InitParam)element).getDescriptions().get(0).getDescription()==null?"":((InitParam)element).getDescriptions().get(0).getDescription();
				}
			}
			break;
		}
		return null;
	}


	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof InitParam){
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
