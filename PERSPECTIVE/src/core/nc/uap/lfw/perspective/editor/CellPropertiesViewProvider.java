package nc.uap.lfw.perspective.editor;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.data.Field;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * ds编辑器的标签、内容提供类
 * @author zhangxya
 *
 */
public class CellPropertiesViewProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public CellPropertiesViewProvider() {
		super();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}


	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if(element instanceof Field){
			return ((Field)element).getId() == null? "":((Field)element).getId();
			}
			break;
		case 1:
			if(element instanceof Field){
			return ((Field)element).getField() == null? "":((Field)element).getField() ;
			}
			break;
		case 2:
			if(element instanceof Field){
				return ((Field)element).getText() == null? "":((Field)element).getText();
				}
				break;
		case 3:
			if(element instanceof Field){
				return ((Field)element).getI18nName() == null? "":((Field)element).getI18nName();
				}
				break;
		case 4:
			if(element instanceof Field){
				return ((Field)element).getDataType() == null? "":((Field)element).getDataType();
				}
				break;
		case 5:
			if(element instanceof Field){
				return ((Field)element).isNullAble() == true? "Y":"N";
				}
				break;
		case 6:
			if(element instanceof Field){
				return ((Field)element).getDefaultValue() == null? "":((Field)element).getDefaultValue().toString();
				}
				break;
		case 7:
			if(element instanceof Field){
				return ((Field)element).isPrimaryKey() == true?"Y":"N";
				}
				break;
		case 8:
			if(element instanceof Field){
					return ((Field)element).getEditFormular() == null? "":((Field)element).getEditFormular();
			}
				break;
		case 9:
			if(element instanceof Field){
				return ((Field)element).getValidateFormula() == null? "":((Field)element).getValidateFormula();
			}
				break;
		case 10:
			if(element instanceof Field){
				return ((Field)element).isLock() == true ? "Y":"N";
				}
			break;
		case 11:
			if(element instanceof Field){
				return ((Field)element).getFormater() == null? "":((Field)element).getFormater();
				}
				break;
		case 12:
			if(element instanceof Field){
				return ((Field)element).getLangDir() == null? "":((Field)element).getLangDir();
				}
			break;
		case 13:
			if(element instanceof Field){
				return ((Field)element).getMaxValue() == null? "":((Field)element).getMaxValue();
				}
			break;
		case 14:
			if(element instanceof Field){
				return ((Field)element).getMinValue() == null? "":((Field)element).getMinValue();
				}
			break;
		case 15:
			if(element instanceof Field){
				return ((Field)element).getPrecision() == null? "":((Field)element).getPrecision();
				}
			break;
		}
		return null;
	}


	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof Field){
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
