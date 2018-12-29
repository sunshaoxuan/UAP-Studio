package nc.lfw.editor.widget.plug;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.page.Connector;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class WidgetConnProvider extends LabelProvider implements
		ITableLabelProvider, ITreeContentProvider {

	public WidgetConnProvider() {
		super();
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof Connector
				&& ((((Connector) element).getConnType()
						.equals(Connector.INLINEWINDOW_VIEW)) || (((Connector) element)
						.getConnType().equals(Connector.INLINEWINDOW_VIEW)))) {
			switch (columnIndex) {
			case 0:
				if (((Connector) element).getConnType().equals(
						Connector.INLINEWINDOW_VIEW)) {
					return ((Connector) element).getSource();
				} else if (((Connector) element).getConnType().equals(
						Connector.VIEW_INLINEWINDOW)) {
					return ((Connector) element).getTarget();
				}
			case 1:
				return ((Connector) element).getPlugoutId() == null ? ""
						: ((Connector) element).getPlugoutId();
			case 2:
				return ((Connector) element).getPluginId() == null ? ""
						: ((Connector) element).getPluginId();
			}
		}
		return null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List)
			return ((List) inputElement).toArray();
		else
			return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Connector) {
			return new Object[0];
		} else
			return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ArrayList)
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
