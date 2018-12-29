package nc.lfw.editor.widget.plug;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.page.PlugoutEmitItem;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * PlugoutDescEmitProvider
 * 
 * @author dingrf
 *
 */
public class PlugoutEmitItemProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public PlugoutEmitItemProvider() {
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
			if(element instanceof PlugoutEmitItem){
				return ((PlugoutEmitItem)element).getId() == null? "":((PlugoutEmitItem)element).getId();
			}
			break;
		case 1:
			if(element instanceof PlugoutEmitItem){
				return ((PlugoutEmitItem)element).getType() == null? "":((PlugoutEmitItem)element).getType() ;
			}
			break;
		case 2:
			if(element instanceof PlugoutEmitItem){
				return ((PlugoutEmitItem)element).getSource() == null? "":((PlugoutEmitItem)element).getSource() ;
			}
			break;
		case 3:
			if(element instanceof PlugoutEmitItem){
				return ((PlugoutEmitItem)element).getDesc() == null? "":((PlugoutEmitItem)element).getDesc() ;
			}
			break;
		}
		return null;
	}


	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof PlugoutEmitItem){
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
