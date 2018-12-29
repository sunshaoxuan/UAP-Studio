package nc.lfw.editor.widget.plug;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.widget.plug.PlugoutDescSourceDialog.PlugoutDescSource;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * PlugRelationProvider
 * 
 * @author dingrf
 *
 */
public class PlugoutDescSourceProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public PlugoutDescSourceProvider() {
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
			if(element instanceof PlugoutDescSource){
				return ((PlugoutDescSource)element).getSourceName() == null? "":((PlugoutDescSource)element).getSourceName();
			}
			break;
//		case 1:
//			if(element instanceof PlugoutDescSource){
//				return ((PlugoutDescSource)element).getSourceId() == null? "":((PlugoutDescSource)element).getSourceId();
//			}
//			break;
		}	
		return null;
	}


	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof PlugoutDescSource){
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
