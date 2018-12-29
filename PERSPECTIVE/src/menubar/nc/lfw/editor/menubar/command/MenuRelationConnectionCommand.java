package nc.lfw.editor.menubar.command;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.contextmenubar.ContextMenuEditor;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.editor.menubar.MenubarConnector;
import nc.lfw.editor.menubar.MenubarEditor;
import nc.lfw.editor.menubar.MenubarGraph;
import nc.lfw.editor.menubar.dialog.MenubarRelationDialog;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenuItemObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;

public class MenuRelationConnectionCommand extends Command {
	protected LFWBasicElementObj source;
	protected LFWBasicElementObj target;
	private MenubarConnector conn = null;
	private Constructor constructor = null;
	private Class conCls = null;

	public MenuRelationConnectionCommand(LFWBasicElementObj refdsobj, Class conCls) {
		super();
		this.source = refdsobj;
		this.conCls = conCls;
		try {
			constructor = conCls.getConstructor(new Class[] { LFWBasicElementObj.class, LFWBasicElementObj.class });
		} catch (Exception e) {
			e.printStackTrace();
		}
		setLabel("create new cell"); //$NON-NLS-1$
	}

	public boolean canExecute() {
//		if(true)
//			return true;
//		// 枚举只能和注释建立连接
//		if ((source instanceof MenuElementObj && target instanceof MenuElementObj)
//				&& Connection.class.equals(conCls)) {
//			return true;
//		}
//		else if ((source instanceof MenubarGraph && target instanceof MenuElementObj)
//				&& Connection.class.equals(conCls)) {
//			return true;
//		}
//		return false;
		return true;
	}

	void setSource(LFWBasicElementObj source) {
		this.source = source;
	}

	public void setTarget(LFWBasicElementObj target) {
		this.target = target;
	}

	public String getLabel() {
		return "Create Connection"; //$NON-NLS-1$
	}

	
	public void redo() {
		//TODO
		if(target instanceof MenuElementObj){
			MenuElementObj targetObj = (MenuElementObj) target;
//			List<MenuItem> children = targetObj.getMenuItem().getChildList();
			List<MenuItem> children = targetObj.getChildrenItems();
			if(children == null || children.size() == 0){
				MessageDialog.openInformation(null, M_menubar.MenuRelationConnectionCommand_0, M_menubar.MenuRelationConnectionCommand_1);
				return;
			}
		}
		if (source instanceof MenubarElementObj && target instanceof MenuElementObj) {
			if (1 != ((MenuElementObj)target).getLevel()) {
				MessageDialog.openInformation(null, M_menubar.MenuRelationConnectionCommand_0, M_menubar.MenuRelationConnectionCommand_2);
				return;
			} else {
				createRelation();
			}
		}else if(source instanceof ContextMenuElementObj && target instanceof MenuElementObj){
			if (1 != ((MenuElementObj)target).getLevel()) {
				MessageDialog.openInformation(null, M_menubar.MenuRelationConnectionCommand_0, M_menubar.MenuRelationConnectionCommand_2);
				return;
			} else {
				createRelation();
			}
		}else if (source instanceof MenuElementObj && target instanceof MenuElementObj) {
			if (((MenuElementObj)target).getLevel() - 1 != ((MenuElementObj)source).getLevel()) {
				MessageDialog.openInformation(null, M_menubar.MenuRelationConnectionCommand_3, M_menubar.MenuRelationConnectionCommand_4);
				return;
			} else {
				createRelation();
			}
		}else {
			MessageDialog.openInformation(null, M_menubar.MenuRelationConnectionCommand_0, M_menubar.MenuRelationConnectionCommand_5);
		}
	}
	
	/**
	 * 建立连接
	 */
	private void createRelation() {
		MenubarRelationDialog dialog = new MenubarRelationDialog(null,M_menubar.MenuRelationConnectionCommand_6);
		LfwElementObjWithGraph sourceObj = (LfwElementObjWithGraph) source;
		MenuElementObj targetObj = (MenuElementObj) target;
		dialog.setSource(sourceObj);
		dialog.setTarget(targetObj);
		int result = dialog.open();
		if (result == IDialogConstants.OK_ID) {
			// 修改关联关系后的处理方法
			
			// 新的触发项
			MenuItem newSourceItem = dialog.getSourceItem();
			if (newSourceItem != targetObj.getMenuItem()) {  // 如果发生改变
				// 重新设置Pagemeta的MenubarComp内容
				MenuItem targetMenuItem = targetObj.getMenuItem();
				//List<MenuItem> newChildList = targetObj.getMenuItem().getChildList();
//				newSourceItem.addMenuItem(targetMenuItem);
				newSourceItem.setChildList(targetObj.getChildrenItems());
//				if (null != targetObj.getMenuItem().getText() && !"".equals(targetObj.getMenuItem().getText()))  // 目标不是新建的子菜单
				if (null != targetObj.getMenuItem())
					targetObj.getMenuItem().setChildList(new ArrayList<MenuItem>());
				else {  // 目标是新建的子菜单
					targetObj.setMenuItem(newSourceItem);
				}

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

				// 获取MenubarEditor
				LFWBaseEditor editor = LFWBaseEditor.getActiveEditor();
				editor.setDirtyTrue();
			}
			
			conn.connect();
			
			((MenuElementObj) target).getFigure().refresh();
			
			LFWBaseEditor editor = LFWBaseEditor.getActiveEditor();
			if(editor instanceof MenubarEditor){
				MenubarEditor.getActiveMenubarEditor().addConnector(conn);
			}else if(editor instanceof ContextMenuEditor){
				ContextMenuEditor.getActiveMenubarEditor().addConnector(conn);
			}
		}
	}

	
	public void undo() {
		conn.disConnect();
	}

	public void execute() {
		try {
			Connection conn = (Connection) constructor.newInstance(new LFWBasicElementObj[] { source, target });
			this.conn = new MenubarConnector(conn.getSource(), conn.getTarget());
			redo();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
