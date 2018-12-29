package nc.lfw.editor.pagemeta.plug;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.page.Connector;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class PmConnProvider extends LabelProvider implements ITableLabelProvider, ITreeContentProvider{

	public PmConnProvider() {
		super();
	}
	
	@Override
	public String getColumnText(Object element, int columnIndex) {
//		LfwWindow win = RelationEditor.getActiveRelationEditor().getGraph().getPagemeta();
		
		switch (columnIndex) {
			case 0:
				if(element instanceof Connector){
	//				return ((Connector)element).getSourceWindow() == null? "":((Connector)element).getSourceWindow();
					if(((Connector)element).getConnType().equals(Connector.VIEW_WINDOW)){
						return ((Connector)element).getSource();
					}
					else if(((Connector)element).getConnType().equals(Connector.WINDOW_VIEW)){
						return ((Connector)element).getTarget();
					}
				}
				break;
			case 1:
				if(element instanceof Connector){
					return ((Connector)element).getPlugoutId() == null? "":((Connector)element).getPlugoutId();
				}
				break;
			case 2:
				if(element instanceof Connector){
					return ((Connector)element).getPluginId() == null? "":((Connector)element).getPluginId();
				}
				break;	
			case 3:
				if(element instanceof Connector){
					return "";
				}
				break;
	//		case 4:
	//			if(element instanceof Connector){
	//				return "";
	//			}
	//			break;
		}
		return null;
	}
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List)
			return ((List) inputElement).toArray();
		else 
			return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof Connector){
			return new Object[0];
		}
		else return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof ArrayList)
			return true;
		else
			return false;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}


}
