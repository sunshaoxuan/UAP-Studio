package nc.uap.lfw.perspective.project;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
/**
 * 内容器，决定那些对象应该输出在treeview里显示
 * @author zhangxya
 *
 */
public class TreeViewerContentProvider implements ITreeContentProvider{

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

	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List){
			return ((List) inputElement).toArray();
		}
		return new Object[0];
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

}
