package nc.uap.lfw.toolbar;

import java.util.List;

import nc.uap.lfw.core.comp.ToolBarItem;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class ToolBarViewProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	public ToolBarViewProvider() {
		super();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
	
	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if(element instanceof ToolBarItem){
			return ((ToolBarItem)element).getId() == null? null:((ToolBarItem)element).getId();
			}
			break;
		case 1:
			if(element instanceof ToolBarItem){
			return ((ToolBarItem)element).getText() == null? null:((ToolBarItem)element).getText();
			}
			break;
		case 2:
			if(element instanceof ToolBarItem){
			return ((ToolBarItem)element).getI18nName()== null? null:((ToolBarItem)element).getI18nName();
			}
			break;
		case 3:
			if(element instanceof ToolBarItem){
			return ((ToolBarItem)element).getLangDir() == null? null:((ToolBarItem)element).getLangDir();
			}
			break;
		case 4:
			if(element instanceof ToolBarItem){
			return ((ToolBarItem)element).getTip() == null? null:((ToolBarItem)element).getTip();
			}
			break;
		case 5:
			if(element instanceof ToolBarItem){
			return ((ToolBarItem)element).getTipI18nName() == null? null:((ToolBarItem)element).getTipI18nName();
			}
			break;
		case 6:
			if(element instanceof ToolBarItem){
				return ((ToolBarItem)element).getRefImg() == null? null:((ToolBarItem)element).getRefImg();
				}
				break;
		case 7:
			if(element instanceof ToolBarItem){
				return ((ToolBarItem)element).getAlign() == null? null:((ToolBarItem)element).getAlign();
				}
				break;
		case 8:
			if(element instanceof ToolBarItem){
			return ((ToolBarItem)element).getType() == null? null:((ToolBarItem)element).getType();
			}
			break;
			
		case 9:
			if(element instanceof ToolBarItem){
				return ((ToolBarItem)element).isWithSep() == true?"Y":"N";
			}
			break;
			
//		case 10:
//			if(element instanceof ToolBarItem){
//			return ((ToolBarItem)element).getWidth() == null? null:((ToolBarItem)element).getWidth();
//			}
//			break;
		case 10:
			if(element instanceof ToolBarItem){
				ToolBarItem toolbarItem = (ToolBarItem) element;
				String value = "";
				if(toolbarItem.getModifiers() == 1)
					value = "SHIFT";
				else if(toolbarItem.getModifiers() == 2)
					value = "CTRL";
				else if(toolbarItem.getModifiers() == 8)
					value = "ALT";
				else if(toolbarItem.getModifiers() == 3)
					value = "CTRL+SHIFT";
				else if(toolbarItem.getModifiers() == 10)
					value = "CTRL+ALT";
				else if(toolbarItem.getModifiers() == 9)
					value = "ALT+SHIFT";
				else if(toolbarItem.getModifiers() == 11)
					value = "CTRL+SHIFT+ALT";
				return value;
			}
			break;
		case 11:
			if(element instanceof ToolBarItem){
			return ((ToolBarItem)element).getHotKey() == null? null:((ToolBarItem)element).getHotKey();
			}
			break;
		case 12:
			if(element instanceof ToolBarItem){
			return ((ToolBarItem)element).getDisplayHotKey() == null? null:((ToolBarItem)element).getDisplayHotKey();
			}
			break;
		}
		return null;
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

	public Object[] getChildren(Object parentElement) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

}

