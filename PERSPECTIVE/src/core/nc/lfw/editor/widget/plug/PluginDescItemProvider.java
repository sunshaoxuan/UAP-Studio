package nc.lfw.editor.widget.plug;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.page.PluginDescItem;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * PluginDescItemProvider
 * 
 * @author dingrf
 *
 */
public class PluginDescItemProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public PluginDescItemProvider() {
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
			if(element instanceof PluginDescItem){
				return ((PluginDescItem)element).getId() == null? "":((PluginDescItem)element).getId();
			}
			break;
		case 1:
			if(element instanceof PluginDescItem){
				return ((PluginDescItem)element).getClazztype() == null? "":((PluginDescItem)element).getClazztype() ;
			}
			break;
//		case 2:
//			if(element instanceof PluginDescItem){
//				return ((PluginDescItem)element).getClazztype() == null? "":((PluginDescItem)element).getClazztype() ;
//			}
//			break;
		}
		return null;
	}


	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof PluginDescItem){
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
