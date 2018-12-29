package nc.uap.lfw.combodata.core;

import java.util.List;

import nc.uap.lfw.core.combodata.CombItem;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * 下拉数据标签，内容提供器
 * @author zhangxya
 *
 */
public class ComboDataProvider  extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public ComboDataProvider() {
		super();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if(element instanceof CombItem){
			return ((CombItem)element).getText() == null? "":((CombItem)element).getText();
			}
			break;
		case 1:
			if(element instanceof CombItem){
			return ((CombItem)element).getValue() == null? "":((CombItem)element).getValue() ;
			}
			break;
		case 2:
			if(element instanceof CombItem){
			return ((CombItem)element).getI18nName() == null? "":((CombItem)element).getI18nName() ;
			}
			break;
		case 3:
			if(element instanceof CombItem){
			return ((CombItem)element).getImage() == null? "":((CombItem)element).getImage() ;
			}
			break;
		}
		return null;
	}


	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof CombItem){
			return new Object[0];
		}
		else return new Object[0];
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if(element instanceof List)
			return true;
		else
			return false;
	}

	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
	if(inputElement instanceof List)
		return ((List<Object>) inputElement).toArray();
	else 
		return new Object[0];
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
