package nc.uap.lfw.form.core;

import java.util.List;

import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.data.Field;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * form标签器，内容器
 * @author zhangxya
 *
 */
public class FormTabelViewProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public FormTabelViewProvider() {
		super();
	}
	


	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
	

	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
			case 0:
				if(element instanceof FormElement){
				return ((FormElement)element).getId() == null? null:((FormElement)element).getId();
				}
				break;
			case 1:
				if(element instanceof FormElement){
				return ((FormElement)element).getField() == null? null:((FormElement)element).getField();
				}
				break;
			case 2:
				if(element instanceof FormElement){
					return ((FormElement)element).getText() == null? null:((FormElement)element).getText();
					}
					break;
			
			case 3:
				if(element instanceof FormElement){
					return ((FormElement)element).getI18nName() == null? null:((FormElement)element).getI18nName();
					}
					break;
			case 4:
				if(element instanceof FormElement){
					return ((FormElement)element).isVisible() == true?"Y":"N";
				}
				break;
			case 5:
				if(element instanceof FormElement){
					return ((FormElement)element).isEnabled() == true? "Y":"N";
					}
					break;
			case 6:
				if(element instanceof FormElement){
					return ((FormElement)element).getEditorType()== null? null:((FormElement)element).getEditorType();
					}
				break;	
		
			case 7:
				if(element instanceof FormElement){
					return ((FormElement)element).getRefNode()== null? null:((FormElement)element).getRefNode();
					}
				break;
			case 8:
				if(element instanceof FormElement){
					return ((FormElement)element).getRefComboData()== null? null:((FormElement)element).getRefComboData();
					}
				break;
			case 9:
				if(element instanceof FormElement){
					return ((FormElement)element).getRelationField() == null? null:((FormElement)element).getRelationField();
					}
					break;
			
			case 10:
				if(element instanceof FormElement){
					return ((FormElement)element).getRowSpan()== null? null:((FormElement)element).getRowSpan().toString();
					}
				break;
			case 11:
				if(element instanceof FormElement){
					return ((FormElement)element).getColSpan() == null? null:((FormElement)element).getColSpan().toString();
					}
					break;
			case 12:
				if(element instanceof FormElement){
					return ((FormElement)element).getHeight() == null? null:((FormElement)element).getHeight().toString();
					}
					break;
			case 13:
				if(element instanceof FormElement){
					return ((FormElement)element).getWidth() == null? null:((FormElement)element).getWidth().toString();
					}
					break;
			case 14:
				if(element instanceof FormElement){
					return ((FormElement)element).getTip()== null? null:((FormElement)element).getTip();
				}
				break;
			case 15:
				if(element instanceof FormElement){
					return ((FormElement)element).getBindId()== null? null:((FormElement)element).getBindId();
				}
				break;
			case 16:
				if(element instanceof FormElement){
					return ((FormElement)element).getDataDivHeight()== null? null:((FormElement)element).getDataDivHeight();
				}
				break;
			case 17:
				if(element instanceof FormElement){
					return ((FormElement)element).isImageOnly()== true? "Y":"N";
				}
				break;
			case 18:
				if(element instanceof FormElement){
					return ((FormElement)element).isSelectOnly()== true? "Y":"N";
				}
				break;
			case 19:
				if(element instanceof FormElement){
					return ((FormElement)element).isNextLine()== true? "Y":"N";
				}
				break;
			case 20:
				if(element instanceof FormElement){
//					return ((FormElement)element).getToolbarType()== null? null:((FormElement)element).getToolbarType();
					if(((FormElement)element).getToolbarType()==null) return "";
					else return "Custom".equals(((FormElement)element).getToolbarType())? "常规":"完全";
				}
				break;
			
			case 21:
				if(element instanceof FormElement){
				return ((FormElement)element).getLangDir() == null? null:((FormElement)element).getLangDir() ;
				}
				break;
			case 22:
				if(element instanceof FormElement){
					return ((FormElement)element).getDescription()== null? null:((FormElement)element).getDescription();
				}
				break;
			case 23:
				if(element instanceof FormElement){
					return ((FormElement)element).getLabelColor()== null? null:((FormElement)element).getLabelColor();
				}
				break;
			case 24:
				if(element instanceof FormElement){
					return ((FormElement)element).isNullAble()== true? "Y":"N";
				}
				break;
			case 25:
				if(element instanceof FormElement){
					return ((FormElement)element).isAttachNext()== true? "Y":"N";
				}
				break;
			case 26:
				if(element instanceof FormElement){
					return ((FormElement)element).isEditable()== true? "N":"Y";
				}
				break;
			case 27:
				if(element instanceof FormElement){
					return ((FormElement)element).getInputAssistant()== null? null:((FormElement)element).getInputAssistant();
				}
				break;
			case 28:
				if(element instanceof FormElement){
					return ((FormElement)element).getPrecision()== null? null:((FormElement)element).getPrecision();
				}
				break;
			case 29:
				if(element instanceof FormElement){
					return ((FormElement)element).getSizeLimit()== null? null:((FormElement)element).getSizeLimit();
				}
				break;
			case 30:
				if(element instanceof FormElement){
					return ((FormElement)element).getMaxLength()== null? null:((FormElement)element).getMaxLength();
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
		if(element instanceof Field)
			return true;
		else{
			return false;
		}
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

