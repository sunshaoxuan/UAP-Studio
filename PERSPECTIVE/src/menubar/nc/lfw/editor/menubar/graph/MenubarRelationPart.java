package nc.lfw.editor.menubar.graph;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LfwBaseConnectionPart;
import nc.lfw.editor.menubar.MenubarRelationDeletePolicy;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.uap.lfw.perspective.figures.ConnectionFigure;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.SelectionRequest;

/**
 * 
 * @author guoweic
 *
 */
public class MenubarRelationPart extends LfwBaseConnectionPart {

	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new MenubarRelationDeletePolicy());
	}

	public void performRequest(Request request) {
		if (RequestConstants.REQ_OPEN.equals(request.getType())
				|| request instanceof SelectionRequest) {
			SelectionRequest sr = (SelectionRequest) request;
			if (getFigure() instanceof ConnectionFigure) {
				ConnectionFigure fig = (ConnectionFigure) getFigure();
				Connection con = fig.getCon();
				if (con.getSource() instanceof MenubarElementObj) { // 主Menubar发出的连接
					MenubarElementObj menubarObj = (MenubarElementObj) con
							.getSource();
					MenuElementObj subMenuObj = (MenuElementObj) con
							.getTarget();
					String connectorId = con.getId();
					
//					// 获取当前Pagemeta
//					PageMetaConfig pagemeta = PagemetaEditor
//							.getActivePagemetaEditor().getGraph().getPagemeta();
//					Map<String, Connector> connectorMap = pagemeta
//							.getConnectorMap();
//					Connector connector = connectorMap.get(connectorId);
//					String signal = connector.getSignal();
//					String slot = connector.getSlot();
//					WidgetConnectorDialog dialog = new WidgetConnectorDialog(
//							null);
//					dialog.setSource(sourceWidget);
//					dialog.setTarget(targetWidget);
//					dialog.initDialogArea(connectorId, signal, slot,
//							connectorMap);
//					int result = dialog.open();
//					if (result == IDialogConstants.OK_ID) {
//						// 构造Connector
//						connector.setId(dialog.getId());
//						connector.setSignal(dialog.getSignal());
//						connector.setSlot(dialog.getSlot());
//						connector.setSource(sourceWidget.getWidget().getId());
//						connector.setTarget(targetWidget.getWidget().getId());
//						// 获取PagemetaEditor
//						PagemetaEditor editor = PagemetaEditor
//								.getActivePagemetaEditor();
//						// 构造Connector
//						if (pagemeta.getConnectorMap().containsKey(connectorId))
//							pagemeta.getConnectorMap().remove(connectorId);
//						pagemeta.addConnector(connector);
//						// 保存Connector
//						// editor.savePagemeta(pagemeta);
//						editor.setDirtyTrue();
//
//						con.setId(dialog.getId());
//					}

				}
			}
		} else {
			super.performRequest(request);
		}
	}

	protected IFigure createFigure() {
		PolylineConnection figure = null;
		Object model = getModel();
		if (model instanceof Connection) {
			Connection cell = (Connection) model;
			figure = new ConnectionFigure(cell);
		}
		return figure;
	}

}
