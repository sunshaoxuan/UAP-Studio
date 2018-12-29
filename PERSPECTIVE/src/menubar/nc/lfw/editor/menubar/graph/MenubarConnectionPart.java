package nc.lfw.editor.menubar.graph;


import java.util.ArrayList;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LfwBaseConnectionPart;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.contextmenubar.ContextMenuEditor;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.editor.menubar.MenubarConnector;
import nc.lfw.editor.menubar.MenubarEditor;
import nc.lfw.editor.menubar.MenubarRelationDeletePolicy;
import nc.lfw.editor.menubar.dialog.MenubarRelationDialog;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenuItemObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.lang.M_menubar;
import nc.uap.lfw.perspective.figures.ConnectionFigure;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.jface.dialogs.IDialogConstants;

/**
 * @author guoweic
 *
 */
public class MenubarConnectionPart extends LfwBaseConnectionPart {

	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new MenubarRelationDeletePolicy());
	}

	public void performRequest(Request request) {
		if (RequestConstants.REQ_OPEN.equals(request.getType())
				|| request instanceof SelectionRequest) {
			SelectionRequest sr = (SelectionRequest) request;
			if (getFigure() instanceof ConnectionFigure) {
				ConnectionFigure fig = (ConnectionFigure) getFigure();
				MenubarConnector con = (MenubarConnector) fig.getCon();
				if (con.getSource() instanceof MenubarElementObj || con.getSource() instanceof MenuElementObj) { // Menubar的连接
					LfwElementObjWithGraph sourceObj = (LfwElementObjWithGraph) con.getSource();
					MenuElementObj targetObj = (MenuElementObj) con.getTarget();
					
					//MenubarComp menubar = MenubarEditor.getActiveMenubarEditor().getMenubarObj().getMenubar();
					
					MenubarRelationDialog dialog = new MenubarRelationDialog(null, M_menubar.MenubarConnectionPart_0);
					dialog.setSource(sourceObj);
					dialog.setTarget(targetObj);
					int result = dialog.open();
					if (result == IDialogConstants.OK_ID) {
						// 修改关联关系后的处理方法
						
						// 新的触发项
						MenuItem newSourceItem = dialog.getSourceItem();
						if (newSourceItem != targetObj.getMenuItem()) {  // 如果发生改变
							// 获取MenubarEditor
							MenubarEditor editor = MenubarEditor.getActiveMenubarEditor();
							
							editor.removeConnector(targetObj.getMenuItem().getId());
							
							// 重新设置Pagemeta的MenubarComp内容
							newSourceItem.setChildList(targetObj.getMenuItem().getChildList());
							targetObj.getMenuItem().setChildList(new ArrayList<MenuItem>());

							// 重新设置父菜单的被触发项
							if (sourceObj instanceof MenubarElementObj) {
								for (MenuItemObj childObj : ((MenubarElementObj)sourceObj).getChildrenList()) {
									if (childObj.getMenuItem() == targetObj.getMenuItem())
										childObj.setMenuItem(newSourceItem);
								}
							} else if (sourceObj instanceof MenuElementObj) {
								for (MenuItemObj childObj : ((MenuElementObj)sourceObj).getChildrenList()) {
									if (childObj.getMenuItem() == targetObj.getMenuItem())
										childObj.setMenuItem(newSourceItem);
								}
							}
							// 重新设置子菜单的触发项
							targetObj.setMenuItem(newSourceItem);
							
							editor.addConnector(con);
							
							targetObj.getFigure().refresh();
							
							// 保存Connector
							// editor.savePagemeta(pagemeta);
							editor.setDirtyTrue();
						}

					}

				}
				
				else if (con.getSource() instanceof ContextMenuElementObj || con.getSource() instanceof MenuElementObj) { // Menubar的连接
					LfwElementObjWithGraph sourceObj = (LfwElementObjWithGraph) con.getSource();
					MenuElementObj targetObj = (MenuElementObj) con.getTarget();
					
					ContextMenuComp menubar = ContextMenuEditor.getActiveMenubarEditor().getMenubarObj().getMenubar();
					
					MenubarRelationDialog dialog = new MenubarRelationDialog(null, M_menubar.MenubarConnectionPart_0);
					dialog.setSource(sourceObj);
					dialog.setTarget(targetObj);
					int result = dialog.open();
					if (result == IDialogConstants.OK_ID) {
						// 修改关联关系后的处理方法
						
						// 新的触发项
						MenuItem newSourceItem = dialog.getSourceItem();
						if (newSourceItem != targetObj.getMenuItem()) {  // 如果发生改变
							// 获取MenubarEditor
							ContextMenuEditor editor = ContextMenuEditor.getActiveMenubarEditor();
							
							editor.removeConnector(targetObj.getMenuItem().getId());
							
							// 重新设置Pagemeta的MenubarComp内容
							newSourceItem.setChildList(targetObj.getMenuItem().getChildList());
							targetObj.getMenuItem().setChildList(new ArrayList<MenuItem>());

							// 重新设置父菜单的被触发项
							if (sourceObj instanceof ContextMenuElementObj) {
								for (MenuItemObj childObj : ((ContextMenuElementObj)sourceObj).getChildrenList()) {
									if (childObj.getMenuItem() == targetObj.getMenuItem())
										childObj.setMenuItem(newSourceItem);
								}
							} else if (sourceObj instanceof MenuElementObj) {
								for (MenuItemObj childObj : ((MenuElementObj)sourceObj).getChildrenList()) {
									if (childObj.getMenuItem() == targetObj.getMenuItem())
										childObj.setMenuItem(newSourceItem);
								}
							}
							// 重新设置子菜单的触发项
							targetObj.setMenuItem(newSourceItem);
							
							editor.addConnector(con);
							
							targetObj.getFigure().refresh();
							
							// 保存Connector
							// editor.savePagemeta(pagemeta);
							editor.setDirtyTrue();
						}

					}

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
