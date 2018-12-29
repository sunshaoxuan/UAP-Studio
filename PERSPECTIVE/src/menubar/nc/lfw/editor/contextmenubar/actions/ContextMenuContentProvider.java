package nc.lfw.editor.contextmenubar.actions;

import java.util.List;

import nc.uap.lfw.core.comp.ContextMenuComp;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * contextMenuCompÄÚÈÝÆ÷¡¢±êÇ©Æ÷
 * @author zhangxya
 *
 */
public class ContextMenuContentProvider implements IStructuredContentProvider,ITableLabelProvider {

	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List){
			return ((List)inputElement).toArray();
		}
			
		else
			return new Object[0];
	}
	

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}


	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}


	public String getColumnText(Object element, int columnIndex) {
		ContextMenuComp contextMenu = (ContextMenuComp)element;
		if(columnIndex == 0)
			return contextMenu.getId();
		return null;
	}


	public void addListener(ILabelProviderListener listener) {
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
