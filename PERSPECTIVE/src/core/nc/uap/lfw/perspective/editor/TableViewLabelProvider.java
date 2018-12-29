package nc.uap.lfw.perspective.editor;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class TableViewLabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
//		DatasetItem dataItem = (DatasetItem)element;
//		if(columnIndex == 0)
//			return dataItem.getId();
//		if(columnIndex == 1)
//			return dataItem.getFild();
//		if(columnIndex == 2)
//			return dataItem.getDatatype();
//		if(columnIndex ==3)
//			return dataItem.getI18nname();
//		if(columnIndex == 4)
//			return dataItem.getNullable() == true? "ÊÇ":"·ñ";
//		return null;
		return null;
	}

	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

}
