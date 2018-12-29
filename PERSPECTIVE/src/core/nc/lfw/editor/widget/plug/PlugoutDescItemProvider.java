package nc.lfw.editor.widget.plug;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.page.PlugoutDescItem;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * PlugoutDescItemProvider
 * 
 * @author dingrf
 *
 */
public class PlugoutDescItemProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public PlugoutDescItemProvider() {
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
			if(element instanceof PlugoutDescItem){
				return ((PlugoutDescItem)element).getName() == null? "":((PlugoutDescItem)element).getName();
			}
			break;
		case 1:
			if(element instanceof PlugoutDescItem){
				String type = ((PlugoutDescItem)element).getType();
				return type == null ? "" : type;
			}
			break;
		case 2:
			if(element instanceof PlugoutDescItem){
				String source =((PlugoutDescItem)element).getSource();
				return source == null ? "" : source ;
			}
			break;
		case 3:
			if(element instanceof PlugoutDescItem){
//				return ((PlugoutDescItem)element).getValue() == null? "":((PlugoutDescItem)element).getValue() ;
				return ((PlugoutDescItem)element).getDesc() == null? "":((PlugoutDescItem)element).getDesc() ;
			}
			break;
		case 4:
			if(element instanceof PlugoutDescItem){
				return ((PlugoutDescItem)element).getClazztype() == null? "":((PlugoutDescItem)element).getClazztype();
			}
			break;
//		case 5:
//			if(element instanceof PlugoutDescItem){
//			}
//			break;
		}
		return null;
	}

	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof PlugoutDescItem){
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
