package nc.uap.portal.events.page;

import java.util.ArrayList;
import java.util.List;

import nc.uap.portal.container.om.EventDefinition;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * EventsPropertiesView用LabelProvider
 * 
 * @author dingrf
 *
 */
public class EventDefinitionProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	
	public EventDefinitionProvider() {
		super();
	}

	/**
	 * 获取图片
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/**
	 * 获取列值
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if(element instanceof EventDefinition){
			return ((EventDefinition)element).getName() == null? "":((EventDefinition)element).getName();
			}
			break;
		case 1:
			if(element instanceof EventDefinition){
			return ((EventDefinition)element).getValueType() == null? "":((EventDefinition)element).getValueType() ;
			}
			break;
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof EventDefinition){
			return new Object[0];
		}
		else return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof ArrayList)
			return true;
		else
			return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List)
			return ((List) inputElement).toArray();
		else 
			return new Object[0];
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
