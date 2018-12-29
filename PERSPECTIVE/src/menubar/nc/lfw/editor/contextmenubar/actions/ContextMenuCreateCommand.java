package nc.lfw.editor.contextmenubar.actions;

import java.util.List;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.TreeLevel;
import nc.uap.lfw.form.FormGraph;
import nc.uap.lfw.grid.GridGraph;
import nc.uap.lfw.lang.M_menubar;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.toolbar.ToolBarGraph;
import nc.uap.lfw.tree.TreeElementObj;
import nc.uap.lfw.tree.TreeGraph;
import nc.uap.lfw.tree.core.TreeLevelElementObj;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;


/**
 * 关联右键菜单命令
 * @author zhangxya
 *
 */
public class ContextMenuCreateCommand extends Command{
	
	private LFWBasicElementObj refdsobj = null;
	private boolean canUndo = true;
	private LfwBaseGraph graph = null;
	private Rectangle rect = null;

	public ContextMenuCreateCommand(LFWBasicElementObj refdsobj,LfwBaseGraph graph, Rectangle rect) {
		super();
		this.refdsobj = refdsobj;
		this.graph = graph;
		this.rect = rect;
		setLabel("create new refds"); //$NON-NLS-1$
	}

	
	public boolean canExecute() {
		return refdsobj != null && graph != null && rect != null;
	}

	public void execute() {
	 if(graph instanceof GridGraph && refdsobj instanceof ContextMenuElementObj){
			GridGraph gridgraph = (GridGraph)graph;
			ContextMenuElementObj refdsobjnew = (ContextMenuElementObj)refdsobj;
			List<ContextMenuElementObj> cells = gridgraph.getContextMenu();
			if(cells.size() >= 1){
				MessageDialog.openConfirm(null, M_menubar.ContextMenuCreateCommand_0, M_menubar.ContextMenuCreateCommand_1);
				return;
			}
			//从本widget查找右键菜单
			ContextMenuFromWidget dialog = new ContextMenuFromWidget(new Shell());
			int result = dialog.open();
			if (result == IDialogConstants.OK_ID) {
				ContextMenuComp contextMenu = dialog.getContextMenuComp();
				if (contextMenu != null) {
					refdsobjnew.setMenubar(contextMenu);
					refdsobjnew.setId(contextMenu.getId());
				}
			}
			else if(result == IDialogConstants.CANCEL_ID){
				return;
			}
			refdsobjnew.setLocation(new Point(100, 300));
			refdsobjnew.setSize(new Dimension(120, 120));
			redo();
		} else if(graph instanceof FormGraph && refdsobj instanceof ContextMenuElementObj){
			FormGraph formgraph = (FormGraph)graph;
			ContextMenuElementObj refdsobjnew = (ContextMenuElementObj)refdsobj;
			List<ContextMenuElementObj> cells = formgraph.getContextMenu();
			if(cells.size() >= 1){
				MessageDialog.openConfirm(null, M_menubar.ContextMenuCreateCommand_0, M_menubar.ContextMenuCreateCommand_2);
				return;
			}
			//从本widget查找右键菜单
			ContextMenuFromWidget dialog = new ContextMenuFromWidget(new Shell());
			int result = dialog.open();
			if (result == IDialogConstants.OK_ID) {
				ContextMenuComp contextMenu = dialog.getContextMenuComp();
				if (contextMenu != null) {
					refdsobjnew.setMenubar(contextMenu);
					refdsobjnew.setId(contextMenu.getId());
				}
			}
			else if(result == IDialogConstants.CANCEL_ID){
				return;
			}
			refdsobjnew.setLocation(new Point(350, 300));
			refdsobjnew.setSize(new Dimension(120, 120));
			redo();
		} else if(graph instanceof ToolBarGraph && refdsobj instanceof ContextMenuElementObj){
			ToolBarGraph toolbargraph = (ToolBarGraph)graph;
			ContextMenuElementObj refdsobjnew = (ContextMenuElementObj)refdsobj;
			List<ContextMenuElementObj> cells = toolbargraph.getContextMenu();
			if(cells.size() >= 1){
				MessageDialog.openConfirm(null, M_menubar.ContextMenuCreateCommand_0, M_menubar.ContextMenuCreateCommand_3);
				return;
			}
			//从本widget查找右键菜单
			ContextMenuFromWidget dialog = new ContextMenuFromWidget(new Shell());
			int result = dialog.open();
			if (result == IDialogConstants.OK_ID) {
				ContextMenuComp contextMenu = dialog.getContextMenuComp();
				if (contextMenu != null) {
					refdsobjnew.setMenubar(contextMenu);
					refdsobjnew.setId(contextMenu.getId());
				}
			}
			else if(result == IDialogConstants.CANCEL_ID){
				return;
			}
			refdsobjnew.setLocation(new Point(350, 100));
			refdsobjnew.setSize(new Dimension(120, 120));
			redo();
		}else if(graph instanceof TreeGraph && refdsobj instanceof ContextMenuElementObj){
			TreeGraph treegraph = (TreeGraph)graph;
			ContextMenuElementObj refdsobjnew = (ContextMenuElementObj)refdsobj;
			//从本widget查找右键菜单
			ContextMenuFromWidget dialog = new ContextMenuFromWidget(new Shell());
			int result = dialog.open();
			if (result == IDialogConstants.OK_ID) {
				ContextMenuComp contextMenu = dialog.getContextMenuComp();
				refdsobjnew.setId(contextMenu.getId());
				refdsobjnew.setMenubar(contextMenu);
				if (contextMenu != null) {
					TreeLevelSelDialog treelevelDialog = new TreeLevelSelDialog(null, M_menubar.ContextMenuCreateCommand_4, treegraph);
					int treeLevelFlag = treelevelDialog.open();
					if(treeLevelFlag == IDialogConstants.OK_ID){
						TreeLevelElementObj treeLevelElementObj = treelevelDialog.getTreeLevelObj();
//						TreeLevel treeLevel = treeLevelElementObj.getTreelevel();
						TreeElementObj treeobjnew = null;
						if(treeLevelElementObj != null){
							TreeLevelElementObj treelevelEle = (TreeLevelElementObj)treeLevelElementObj;
							LFWBasicElementObj parent = treelevelEle.getParentTreeLevel();
							while(parent != null && !(parent instanceof TreeElementObj)){
								 TreeLevelElementObj parentnew = (TreeLevelElementObj)parent;
								 parent = parentnew.getParentTreeLevel();
							}
							treeobjnew = (TreeElementObj)parent;
						}
						if(treeobjnew == null){
							MessageDialog.openError(null, M_menubar.ContextMenuCreateCommand_5, M_menubar.ContextMenuCreateCommand_6);
							return;
						}
						TreeLevel topLevel = treeobjnew.getTreeComp().getTopLevel();
						if(topLevel.getDataset().equals(treeLevelElementObj.getDs().getId())){
							if(topLevel.getContextMenu() != null){
								MessageDialog.openConfirm(null, M_menubar.ContextMenuCreateCommand_0, M_menubar.ContextMenuCreateCommand_7);
								return;
							}
							else{
								topLevel.setContextMenu(contextMenu.getId());
							}
						}else{
							TreeLevel childLevel = topLevel.getChildTreeLevel();
							while(childLevel != null){
								if(childLevel.getDataset().equals(treeLevelElementObj.getDs().getId())){
									if(childLevel.getContextMenu() != null){
										MessageDialog.openConfirm(null, M_menubar.ContextMenuCreateCommand_0, M_menubar.ContextMenuCreateCommand_7);
										return;
									}else{
										childLevel.setContextMenu(contextMenu.getId());
										break;
									}
								}else{
									childLevel = childLevel.getChildTreeLevel();
								}
							}
						}
						int x = treeLevelElementObj.getLocation().x;
						int y = treeLevelElementObj.getLocation().y + treeLevelElementObj.getSize().height + WEBPersConstants.BETWEEN;
						refdsobjnew.setLocation(new Point(x, y));
						refdsobjnew.setSize(new Dimension(100, 100));
						redo();
					}
				}
			}
			else if(result == IDialogConstants.CANCEL_ID){
				return;
			}
			
		}
	}

	
	public void redo() {
		 if(graph instanceof GridGraph && refdsobj instanceof ContextMenuElementObj){
			GridGraph graphnew = (GridGraph)graph;
			ContextMenuElementObj refdsobjnew = (ContextMenuElementObj)refdsobj;
			graphnew.addCell(refdsobjnew);
		}else if(graph instanceof FormGraph && refdsobj instanceof ContextMenuElementObj){
				FormGraph  graphnew = (FormGraph)graph;
				ContextMenuElementObj refdsobjnew = (ContextMenuElementObj)refdsobj;
				graphnew.addCell(refdsobjnew);
		}
		else if(graph instanceof ToolBarGraph && refdsobj instanceof ContextMenuElementObj){
			ToolBarGraph  graphnew = (ToolBarGraph)graph;
			ContextMenuElementObj refdsobjnew = (ContextMenuElementObj)refdsobj;
			graphnew.addCell(refdsobjnew);
		}else if(graph instanceof TreeGraph && refdsobj instanceof ContextMenuElementObj){
			TreeGraph graphnew = (TreeGraph)graph;
			ContextMenuElementObj refdsobjnew = (ContextMenuElementObj)refdsobj;
			graphnew.addCell(refdsobjnew);
		}
	}

	
	public void undo() {
		if(graph instanceof GridGraph && refdsobj instanceof RefDatasetElementObj){
			GridGraph graphnew = (GridGraph)graph;
			ContextMenuElementObj refdsobjnew = (ContextMenuElementObj)refdsobj;
			graphnew.removeCell(refdsobjnew);
		}
	}

	public boolean isCanUndo() {
		return canUndo;
	}

	public void setCanUndo(boolean canUndo) {
		this.canUndo = canUndo;
	}

	
	public boolean canUndo() {
		return isCanUndo();
	}

}
