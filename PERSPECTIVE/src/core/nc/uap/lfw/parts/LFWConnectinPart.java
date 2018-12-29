package nc.uap.lfw.parts;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LfwBaseConnectionPart;
import nc.lfw.editor.datasets.core.DsRelationConnection;
import nc.lfw.editor.datasets.core.DsRelationConnectionFigure;
import nc.lfw.editor.pagemeta.PlugRelationDialog;
import nc.lfw.editor.widget.plug.PluginDescElementObj;
import nc.lfw.editor.widget.plug.PlugoutDescElementObj;
import nc.uap.lfw.grid.DatasetToGridConnection;
import nc.uap.lfw.grid.DsToGridConnectionFigure;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.grid.GridToContextMenuConnFigure;
import nc.uap.lfw.grid.GridToContextMenuConnection;
import nc.uap.lfw.grid.core.GridLevelElementObj;
import nc.uap.lfw.grid.gridlevel.GridLevelWizard;
import nc.uap.lfw.lang.M_parts;
import nc.uap.lfw.listview.DatasetToListViewConnection;
import nc.uap.lfw.listview.DsToListViewConnectionFigure;
import nc.uap.lfw.pagination.DatasetToPaginationConnection;
import nc.uap.lfw.pagination.DsToPaginationConnectionFigure;
import nc.uap.lfw.palette.ChildConnection;
import nc.uap.lfw.perspective.figures.ChildConnectionFigure;
import nc.uap.lfw.perspective.figures.ConnectionFigure;
import nc.uap.lfw.perspective.model.DatasetElementObj;
import nc.uap.lfw.perspective.model.DatasetRelationDialog;
import nc.uap.lfw.perspective.model.DsRelationSetDialog;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.perspective.policy.LFWElementReEditPolicy;
import nc.uap.lfw.refnode.RefNodeElementObj;
import nc.uap.lfw.refnoderel.DatasetFieldElementObj;
import nc.uap.lfw.refnoderel.RefNodeRelationDialog;
import nc.uap.lfw.tree.TreeElementObj;
import nc.uap.lfw.tree.TreeTopLevelConnection;
import nc.uap.lfw.tree.TreeTopLevelConnectionFigure;
import nc.uap.lfw.tree.core.TreeLevelElementObj;
import nc.uap.lfw.tree.treelevel.TreeLevelChildConnection;
import nc.uap.lfw.tree.treelevel.TreeLevelChildConnectionFigure;
import nc.uap.lfw.tree.treelevel.TreeLevelWizard;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.jface.wizard.WizardDialog;

/**
 * lfw connection Part
 * @author zhangxya
 *
 */
public class LFWConnectinPart extends LfwBaseConnectionPart {

	
	protected void createEditPolicies() {
//		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new LFWDSFieldRelationEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new LFWElementReEditPolicy());
	}
	
	public void performRequest(Request request) {
		if(RequestConstants.REQ_OPEN.equals(request.getType())||request instanceof SelectionRequest){
			if(getFigure() instanceof ConnectionFigure){
				ConnectionFigure fig = (ConnectionFigure)getFigure();
				Connection con = fig.getCon();
				if (con.getSource() instanceof DatasetElementObj) {
					DatasetElementObj dsElement = (DatasetElementObj)con.getSource();
					RefDatasetElementObj refDsElement = (RefDatasetElementObj)con.getTarget();
					DsRelationSetDialog dsRelation = new DsRelationSetDialog(null, M_parts.LFWConnectinPart_0, dsElement, refDsElement);
					dsRelation.open();
				} else if(con.getSource() instanceof TreeElementObj ){
					TreeElementObj treeobj = (TreeElementObj)con.getSource();
					TreeLevelElementObj treelevel = (TreeLevelElementObj)con.getTarget();
					WizardDialog treeLevelDialog = new WizardDialog(null, new TreeLevelWizard(treeobj, treelevel, "Y")); //$NON-NLS-1$
		    		//TreeLevelSetDialog treeLevelDialog = new TreeLevelSetDialog(null, source, targetnnew);
		    		treeLevelDialog.open();
				}else if(con.getSource() instanceof TreeLevelElementObj){
					TreeLevelElementObj treeobj = (TreeLevelElementObj)con.getSource();
					TreeLevelElementObj treelevel = (TreeLevelElementObj)con.getTarget();
					WizardDialog treeLevelDialog = new WizardDialog(null, new TreeLevelWizard(treeobj, treelevel, null));
					treeLevelDialog.open();
				}
				else if(con.getSource() instanceof GridElementObj ){
					GridElementObj gridobj = (GridElementObj)con.getSource();
					GridLevelElementObj gridlevel = (GridLevelElementObj)con.getTarget();
					WizardDialog gridLevelDialog = new WizardDialog(null, new GridLevelWizard(gridobj, gridlevel, "Y")); //$NON-NLS-1$
		    		//TreeLevelSetDialog treeLevelDialog = new TreeLevelSetDialog(null, source, targetnnew);
		    		gridLevelDialog.open();
				}
				else if(con.getSource() instanceof DatasetFieldElementObj){
					DatasetFieldElementObj source = (DatasetFieldElementObj) con.getSource();
					RefNodeElementObj target = (RefNodeElementObj) con.getTarget();
					RefNodeRelationDialog dialog = new RefNodeRelationDialog(null, M_parts.LFWConnectinPart_1, source, target);
					dialog.open();
				}
//				else if (con.getSource() instanceof PlugoutDescElementObj){
//					PlugoutDescElementObj source = (PlugoutDescElementObj) con.getSource();
//					PluginDescElementObj target = (PluginDescElementObj) con.getTarget();
//					PlugRelationDialog dialog = new PlugRelationDialog(null, M_parts.LFWConnectinPart_2, source, target, con.getId());
//					dialog.open();
//				}
			}
			if(getFigure() instanceof ChildConnectionFigure){
				ChildConnectionFigure fig = (ChildConnectionFigure)getFigure();
				Connection con = fig.getCon();
				if(con.getSource() instanceof RefDatasetElementObj && con.getTarget() instanceof RefDatasetElementObj){
					RefDatasetElementObj dsElement = (RefDatasetElementObj)con.getSource();
					RefDatasetElementObj refDsElement = (RefDatasetElementObj)con.getTarget();
					DsRelationSetDialog dsRelation = new DsRelationSetDialog(null, M_parts.LFWConnectinPart_0, dsElement, refDsElement);
					dsRelation.open();
				}
			}
			if(getFigure() instanceof DsRelationConnectionFigure){
				DsRelationConnectionFigure fig = (DsRelationConnectionFigure)getFigure();
				DsRelationConnection con = fig.getCon();
				DatasetElementObj source = (DatasetElementObj)con.getSource();
				DatasetElementObj target = (DatasetElementObj)con.getTarget();
				DatasetRelationDialog dialog = new DatasetRelationDialog(null, source, target);
				dialog.open();
			}
		}else{
			super.performRequest(request);
		}
	}
	

	 protected IFigure createFigure() {
		PolylineConnection figure = null;
		Object model = getModel();
		if(model instanceof ChildConnection){
			ChildConnection cell = (ChildConnection)model;
			figure = new ChildConnectionFigure(cell); 
		}else if(model instanceof DsRelationConnection){
			DsRelationConnection cell = (DsRelationConnection)model;
			figure = new DsRelationConnectionFigure(cell);
		}
		else if (model instanceof DatasetToGridConnection){
			DatasetToGridConnection cell = (DatasetToGridConnection)model;
			figure = new DsToGridConnectionFigure(cell);
		}else if (model instanceof DatasetToListViewConnection){
			DatasetToListViewConnection cell = (DatasetToListViewConnection)model;
			figure = new DsToListViewConnectionFigure(cell);
		}else if (model instanceof DatasetToPaginationConnection){
			DatasetToPaginationConnection cell = (DatasetToPaginationConnection)model;
			figure = new DsToPaginationConnectionFigure(cell);
		}else if(model instanceof TreeTopLevelConnection){
			TreeTopLevelConnection cell = (TreeTopLevelConnection)model;
			figure = new TreeTopLevelConnectionFigure(cell);
		}else if(model instanceof TreeLevelChildConnection){
			TreeLevelChildConnection cell = (TreeLevelChildConnection)model;
			figure = new TreeLevelChildConnectionFigure(cell);
		}else if(model instanceof GridToContextMenuConnection){
			GridToContextMenuConnection cell = (GridToContextMenuConnection) model;
			figure = new GridToContextMenuConnFigure(cell);
		}
		
		else if(model instanceof Connection){
			Connection cell = (Connection)model;
			figure = new ConnectionFigure(cell);
		}
		return figure;
	  }

	
}
