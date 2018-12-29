package nc.lfw.editor.menubar.graph;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.editor.contextmenubar.ContextMenuElementObjFigure;
import nc.lfw.editor.menubar.MenuRelationEditPolicy;
import nc.lfw.editor.menubar.MenubarElementFigure;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.lfw.editor.menubar.figure.MenuItemElementFigure;
//import nc.uap.lfw.perspective.listener.ListenerElementFigure;
//import nc.uap.lfw.perspective.listener.ListenerElementObj;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * ×é¼þ Part
 *
 */
public class MenuElementPart extends AbstractGraphicalEditPart implements PropertyChangeListener , NodeEditPart{

	private LFWBaseEditor editor = null;
	public MenuElementPart(LFWBaseEditor editor) {
		super();
		this.editor = editor;
	}
	protected IFigure createFigure() {
		Object model = getModel();
		IFigure figure = null;
		if(model instanceof LFWBasicElementObj){
			LFWBasicElementObj cell = (LFWBasicElementObj)model;
			figure = getFigureByModel(cell);
		}
		return figure;
	}
	
	public void activate() {
		super.activate();
		((LFWBasicElementObj)getModel()).addPropertyChangeListener(this);
	}

	
	public void deactivate() {
		super.deactivate();
		((LFWBasicElementObj)getModel()).removePropertyChangeListener(this);
	}
	
	
	public static IFigure getFigureByModel(LFWBasicElementObj cell){
		IFigure figure = null;
		if (cell instanceof MenuElementObj) {
			MenuElementObj obj = (MenuElementObj) cell;
			figure = new MenuItemElementFigure(obj);
		} else if (cell instanceof MenubarElementObj) {
			MenubarElementObj obj = (MenubarElementObj) cell;
			figure = new MenubarElementFigure(obj);
//		} else if (cell instanceof ListenerElementObj) {
//			ListenerElementObj obj = (ListenerElementObj) cell;
//			figure = new ListenerElementFigure(obj);
		}
		else if(cell instanceof ContextMenuElementObj){
			figure = new ContextMenuElementObjFigure((ContextMenuElementObj)cell);
		}
		return figure;
	}


	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new MenuRelationEditPolicy());
	}

	
	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if (LFWBasicElementObj.PROP_SOURCE_CONNECTION.equals(name)) {
			refreshSourceConnections();
		} else if (LFWBasicElementObj.PROP_TARGET_CONNECTION.equals(name)) {
			refreshTargetConnections();
		}
		else if(MenuElementObj.MENU_ITEM_ADD.equals(name)){
			//TODO
//			MenuItemElementFigure figure = (MenuItemElementFigure) getFigure();
//			figure.refresh();
		}
	}
	
	private ConnectionAnchor anchor;
	protected ConnectionAnchor getConnectionAnchor(){
	       if (anchor == null) {
	           anchor = new ChopboxAnchor(getFigure());
	       }
	       return anchor;
	    }
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		// TODO Auto-generated method stub
		return getConnectionAnchor();
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		// TODO Auto-generated method stub
		return getConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart arg0) {
		return getConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request arg0) {
		return getConnectionAnchor();
	}
	
	protected List getModelChildren() {
//		LFWBasicElementObj obj = (LFWBasicElementObj)getModel();
////		List list = new ArrayList();
//		if(obj instanceof DatasetElementObj){
//			DatasetElementObj ds = (DatasetElementObj)obj;
//			return ds.getCells();
////			list.add(ds.getDs());
////			list.add(ds.get)
//		}
		return new ArrayList();	
	}
//    protected List<Connection> getModelSourceConnections() {
//        return ((Cell) this.getModel()).getSourceConnections();
//    }
//
//    protected List<Connection> getModelTargetConnections() {
//        return ((Cell) this.getModel()).getTargetConnections();
//    }


    protected List<Connection> getModelSourceConnections() {
        return ((LFWBasicElementObj) this.getModel()).getSourceConnections();
    }

    protected List<Connection> getModelTargetConnections() {
        return ((LFWBasicElementObj) this.getModel()).getTargetConnections();
    }
}
