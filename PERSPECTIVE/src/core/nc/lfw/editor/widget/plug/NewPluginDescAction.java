package nc.lfw.editor.widget.plug;


import java.util.List;

import nc.lfw.editor.pagemeta.RelationEditor;
import nc.lfw.editor.widget.WidgetEditor;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.editor.window.WindowObj;
import nc.uap.lfw.lang.M_pagemeta;
import nc.uap.lfw.palette.ChildConnection;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 新建plugin
 * @author dingrf
 *
 */
public class NewPluginDescAction extends Action {
	
	private class NewPluginCommand extends Command{
		private WidgetElementObj widgetObj;
		private WindowObj windowObj;
		
		private String id;
		private String method;
		public NewPluginCommand(WidgetElementObj obj, String id,String method) {
			super(WEBPersConstants.NEW_PLUGINDESC);
			this.widgetObj = obj;
			this.id = id;
			this.method = method;
		}
		public NewPluginCommand(WindowObj obj, String id,String method) {
			super(WEBPersConstants.NEW_PLUGINDESC);
			this.windowObj = obj;
			this.id = id;
			this.method = method;
		}

		public void execute() {
			redo();
		}

		
		public void redo() {
			PluginDescElementObj pluginObj = new PluginDescElementObj();
			pluginObj.setPlugin(new PluginDesc());
			pluginObj.getPlugin().setId(this.id);
			((PluginDesc)pluginObj.getPlugin()).setMethodName(this.method);
//			pluginObj.getPlugin().setSubmitRule(new EventSubmitRule());
			if(widgetObj != null){
//				List<PluginDesc> pluginDescs = widgetObj.getWidget().getPluginDescList();
//				pluginDescs.add((PluginDesc) pluginObj.getPlugin());
//				((PluginDesc)pluginObj.getPlugin()).setControllerClazz(widgetObj.getWidget().getControllerClazz());
				widgetObj.getWidget().addPluginDescs((PluginDesc)pluginObj.getPlugin());
				Point point = new Point();
				int count =getCount(widgetObj.getPluginCells())-2;
				point.x = widgetObj.getLocation().x -100 - 50;
				point.y = widgetObj.getLocation().y + count * 40;
				pluginObj.setLocation(point);
				ChildConnection conn = new ChildConnection(widgetObj,pluginObj);
				conn.connect();
				pluginObj.setConn(conn);
				pluginObj.setSize(new Dimension(100, 30));
				pluginObj.setWidgetObj(widgetObj);
				widgetObj.addPluginCell(pluginObj);
			}
			else if(windowObj!=null){
//				List<IPluginDesc> pluginDescs = windowObj.getWindow().getPluginDescList();
//
//				pluginDescs.add(pluginObj.getPlugin());
				((PluginDesc)pluginObj.getPlugin()).setControllerClazz(windowObj.getWindow().getControllerClazz());
				windowObj.getWindow().addPluginDesc(pluginObj.getPlugin());
				Point point = new Point();
				int count =getCount(windowObj.getPluginCells());
				point.x = windowObj.getLocation().x +100 + 50;
				point.y = windowObj.getLocation().y + (count+1) * 40;
				pluginObj.setLocation(point);
				ChildConnection conn = new ChildConnection(windowObj,pluginObj);
				conn.connect();
				pluginObj.setConn(conn);
				pluginObj.setSize(new Dimension(100, 30));
				pluginObj.setWindowObj(windowObj);
				windowObj.addPluginCell(pluginObj);
			}

		}

		public void undo() {
		}
		
	}	
	
	private int getCount(List<PluginDescElementObj> ins){
		int count = 0;
		if(ins.size()==0){
			return 0;
		}
		for(PluginDescElementObj pluginDesc:ins){
			if(pluginDesc.getPlugin() instanceof PluginDesc){
				count++;
			}
		}
		return count;
	}
	
	private WidgetElementObj widgetObj;
	
	private WindowObj windowObj;
	
//	private String controllerClazz = null;
	
	public NewPluginDescAction(WidgetElementObj obj) {
		super(WEBPersConstants.NEW_PLUGINDESC);
		this.widgetObj = obj;
//		this.controllerClazz = widgetObj.getWidget().getControllerClazz(); 
	}
	public NewPluginDescAction(WindowObj obj){
		super(WEBPersConstants.NEW_PLUGINDESC);
		this.windowObj = obj;
//		this.controllerClazz = windowObj.getWindow().getControllerClazz();		
	}

	public void run() {
		Shell shell = new Shell(Display.getCurrent());
		
		PluginDescDialog pluginDescDialog = new PluginDescDialog(new Shell(), WEBPersConstants.NEW_PLUGINDESC);
		if(pluginDescDialog.open() == InputDialog.OK){
			String id = pluginDescDialog.getId(); 
			String method = pluginDescDialog.getMethod();
			
			if(widgetObj!=null){
				/*判断ID重复*/
				for (PluginDescElementObj obj : widgetObj.getPluginCells()){
					if (obj.getPlugin().getId().equals(id)){
						MessageDialog.openError(shell, M_pagemeta.NewPluginDescAction_0, M_pagemeta.NewPluginDescAction_1+ id +M_pagemeta.NewPluginDescAction_2);
						return;
					}
					if ((obj.getPlugin() instanceof PluginDesc) && method.equals(((PluginDesc)obj.getPlugin()).getMethodName())){
						MessageDialog.openError(shell, M_pagemeta.NewPluginDescAction_0, M_pagemeta.NewPluginDescAction_3+ id +M_pagemeta.NewPluginDescAction_2);
						return;
					}
				}
				
				
				NewPluginCommand cmd = new NewPluginCommand(widgetObj, id, method);
				if(WidgetEditor.getActiveEditor() != null)
					WidgetEditor.getActiveEditor().executComand(cmd);
				
//				int index = controllerClazz.lastIndexOf(".");
//				String packageName = null;
//				if(index > 0){
//					packageName = controllerClazz.substring(0, index);
//				}else{
//					packageName = "";
//				}
//				String projectPath = LFWAMCPersTool.getLFWProjectPath();
				
//				String className = controllerClazz.substring(index + 1);
//				String classFilePath = projectPath + File.separator + widgetObj.getWidget().getSourcePackage() + packageName.replaceAll("\\.", "/");
//				String classFileName = className + ".java";
//				EventConf eventConf = new EventConf();
//				eventConf.setMethodName("plugin" + id);
//				eventConf.setName("");
//				LfwParameter param = new LfwParameter();
//				param.setDesc("java.util.Map");
//				param.setName("keys");
//				eventConf.addParam(param);
//				eventConf.setEventStatus(EventConf.ADD_STATUS);
//				eventConf.setClassFileName(classFileName);
//				eventConf.setClassFilePath(classFilePath);
//				widgetObj.getWidget().addEventConf(eventConf);
				
				WidgetEditor.getActiveWidgetEditor().setDirtyTrue();
			}
			else if(windowObj!=null){
				/*判断ID重复*/
				for (PluginDescElementObj obj : windowObj.getPluginCells()){
					if (obj.getPlugin().getId().equals(id)){
						MessageDialog.openError(shell, M_pagemeta.NewPluginDescAction_0, M_pagemeta.NewPluginDescAction_1+ id +M_pagemeta.NewPluginDescAction_2);
						return;
					}
				}
				NewPluginCommand cmd = new NewPluginCommand(windowObj, id, method);
				
				if(RelationEditor.getActiveEditor() != null)
					RelationEditor.getActiveEditor().executComand(cmd);
				
				RelationEditor relationEditor = RelationEditor.getActiveRelationEditor();
				relationEditor.setDirtyTrue();
				
			}
			
//			LFWAMCConnector.operateViewEventMethod(actionType, packageName, className, classFilePath, classFileName, filePath, WEBProjConstants.AMC_VIEW_FILENAME, projectPath, widgetObj.getWidget(), eventConf);
			
//			JsEventDesc desc = new JsEventDesc(eventConf.getMethodName(), "keys");
//			desc.setEventClazz("Map");			
//			eventConf
//			LFWAMCConnector.operateViewEventMethod(actionType, packageName, className, classFilePath, classFileName, filePath, WEBProjConstants.AMC_VIEW_FILENAME, projectPath, widgetObj.getWidget(), eventConf);
				
				
				
//				PortletDefinition p = new PortletDefinition();
//				p.setPortletName(id);
//				DisplayName displayName = new DisplayName();
//				displayName.setDisplayName(name);
//				((List<DisplayName>)p.getDisplayNames()).add(displayName);
//				p.getPortletInfo().setTitle(name);
//				Supports supports = new Supports();
//				supports.setMimeType("text/html");
//				((List<Supports>)p.getSupports()).add(supports);
//				
//				PortletTreeItem pl = (PortletTreeItem)view.addPortletTreeNode(p);
//
//				//保存portlet
//				String categoryId = null;
//				if (selTI instanceof CategoryTreeItem){
//					categoryId = ((PortletDisplayCategory)selTI.getData()).getId();
//				}
//				PortalConnector.savePortletToXml(projectPath, projectModuleName, p, categoryId);
//				
//				//打开ds编辑器
//				view.openPortletEditor(pl);
//				} catch (Exception e) {
//				String title =PortalProjConstants.NEW_PORTLET;
//				String message = e.getMessage();
//				MessageDialog.openError(shell, title, message);
		}
		else return;
	}
}
