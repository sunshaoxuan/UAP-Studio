package nc.uap.lfw.grid.core;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.ObjectComboCellEditor;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridColumnGroup;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * grid table内容器，标签器
 * @author zhangxya
 *
 */
public class GridTableViewProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {
	GridPropertisView viewer;
	public GridTableViewProvider(GridPropertisView viewer) {
		super();
		this.viewer = viewer;
	}
	


	public Image getColumnImage(Object element, int columnIndex) {
		if(element instanceof GridColumnGroup){
		switch (columnIndex) {
			case 0:
				return ImageProvider.create;
			}
		}
		return null;
	}
	
	

	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if(element instanceof GridColumn){
			return ((GridColumn)element).getId() == null? null:((GridColumn)element).getId();
			}
			else if(element instanceof GridColumnGroup){
				return ((GridColumnGroup)element).getId() == null? null:((GridColumnGroup)element).getId();
			}
			break;
		case 1:
			if(element instanceof GridColumn){
			return ((GridColumn)element).getField() == null? null:((GridColumn)element).getField();
			}
			break;
		case 2:
			if(element instanceof GridColumn){
				return ((GridColumn)element).getText() == null? null:((GridColumn)element).getText();
				}
			else if(element instanceof GridColumnGroup){
				return ((GridColumnGroup)element).getText() == null? null:((GridColumnGroup)element).getText();
			}
			break;
		case 3:
			if(element instanceof GridColumn){
				return ((GridColumn)element).getI18nName() == null? null:((GridColumn)element).getI18nName();
				}
			else if(element instanceof GridColumnGroup){
				return ((GridColumnGroup)element).getI18nName() == null? null:((GridColumnGroup)element).getI18nName();
			}
				break;
		case 4:
			if(element instanceof GridColumn){
			return ((GridColumn)element).getLangDir() == null? null:((GridColumn)element).getLangDir() ;
			}
			break;
		
		
		case 5:
			if(element instanceof GridColumn){
				return String.valueOf(((GridColumn)element).getWidth());
				}
			
				break;
		case 6:
			if(element instanceof GridColumn){
				return ((GridColumn)element).isVisible() == true?"Y":"N";
				}
			else if(element instanceof GridColumnGroup){
				return ((GridColumnGroup)element).isVisible() == true? "Y":"N";
			}
				break;
		case 7:
			if(element instanceof GridColumn){
				return ((GridColumn)element).isEditable() == true? "Y":"N";
				}
				break;
		case 8:
			if(element instanceof GridColumn){
				return ((GridColumn)element).getColumBgColor() == null? null:((GridColumn)element).getColumBgColor();
				}
				break;
		case 9:
			if(element instanceof GridColumn){
				return ((GridColumn)element).getTextAlign()== null? null:((GridColumn)element).getTextAlign();
				}
			break;
		case 10:
			if(element instanceof GridColumn){
				return ((GridColumn)element).getTextColor() == null? null:((GridColumn)element).getTextColor();
				}
				break;
		case 11:
			if(element instanceof GridColumn){
				return ((GridColumn)element).isFixedHeader() == true? "Y": "N";
				}
				break;
		case 12:
			if(element instanceof GridColumn){
				return ((GridColumn)element).getEditorType()== null? null:((GridColumn)element).getEditorType();
				}
			break;
		case 13:
			if(element instanceof GridColumn){
				return ((GridColumn)element).getRenderType()== null? null:((GridColumn)element).getRenderType();
			}
			break;
		case 14:
			if(element instanceof GridColumn){
				return ((GridColumn)element).getRefNode()== null? null:((GridColumn)element).getRefNode();
				}
			break;
		case 15:
			if(element instanceof GridColumn){
				return ((GridColumn)element).getRefComboData()== null? null:((GridColumn)element).getRefComboData();
				}
			break;
		case 16:
		if(element instanceof GridColumn){
			return ((GridColumn)element).isImageOnly() == true? "Y":"N";
			}
			break;
		case 17:
			if(element instanceof GridColumn){
				return ((GridColumn)element).isSumCol() == true? "Y":"N";
				}
				break;
		case 18:
			if(element instanceof GridColumn){
				return ((GridColumn)element).isAutoExpand() == true? "Y":"N";
				}
				break;
		case 19:
			if(element instanceof GridColumn){
				return ((GridColumn)element).isSortable() == true? "Y":"N";
				}
				break;
		case 20:
			if(element instanceof GridColumn){
				return ((GridColumn)element).getColmngroup() == null? "":((GridColumn)element).getColmngroup();
				}
				break;
		case 21:
			if(element instanceof GridColumn){
				return ((GridColumn)element).isNullAble() == true? "Y":"N";
				}
				break;
		case 22:
			if(element instanceof GridColumn){
				return ((GridColumn)element).isShowCheckBox() == true? "Y":"N";
			}
			break;
		case 23:
			if(element instanceof GridColumn){
				return ((GridColumn)element).getMaxLength() == null?"":((GridColumn)element).getMaxLength();
			}
		case 24:
			if(element instanceof GridColumn){
				return ((GridColumn)element).getSumColRenderFunc() == null?"":((GridColumn)element).getSumColRenderFunc();
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
		if(inputElement instanceof List){
			List list = (List) inputElement;
			ArrayList<IGridColumn> gridList = new ArrayList<IGridColumn>();
			if(list != null && list.size() > 0){
				for (int i = 0; i < list.size(); i++) {
					IGridColumn column = (IGridColumn) list.get(i);
					gridList.add(column);
					if(column instanceof GridColumnGroup){
						GridColumnGroup columngroup = (GridColumnGroup) column;
						List child = columngroup.getChildColumnList();
						if(child != null)
							gridList.addAll(child);
					}
				
				}
			}
			
			CellEditor editor = viewer.getCellEditorByColName("所属组");
			String[] valuegroup = viewer.getColumnGroup();
			if(editor instanceof ObjectComboCellEditor){
				ObjectComboCellEditor objEditor = (ObjectComboCellEditor) editor;
				objEditor.setObjectItems(valuegroup);
			}
			return ((List) gridList).toArray();
		}
		else 
			return new Object[0];
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}

