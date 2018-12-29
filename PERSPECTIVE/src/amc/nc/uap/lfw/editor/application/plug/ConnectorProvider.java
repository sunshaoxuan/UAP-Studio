package nc.uap.lfw.editor.application.plug;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.page.Connector;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * ConnectorProvider
 * 
 * @author dingrf
 *
 */
public class ConnectorProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public ConnectorProvider() {
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
			if(element instanceof Connector){
				return ((Connector)element).getSource() == null? "":((Connector)element).getSource();
			}
			break;
		case 1:
			if(element instanceof Connector){
				return ((Connector)element).getPlugoutId() == null? "":((Connector)element).getPlugoutId();
			}
			break;
		case 2:
			if(element instanceof Connector){
				return ((Connector)element).getTarget() == null? "":((Connector)element).getTarget();
			}
			break;
		case 3:
			if(element instanceof Connector){
				return ((Connector)element).getPluginId() == null? "":((Connector)element).getPluginId();
			}
			break;
		case 4:
			if(element instanceof Connector){
				return "";
			}
			break;
		}
		return null;
	}


	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof Connector){
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
