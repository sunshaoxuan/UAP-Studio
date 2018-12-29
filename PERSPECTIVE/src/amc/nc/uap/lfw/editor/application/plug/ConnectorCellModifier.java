package nc.uap.lfw.editor.application.plug;


import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.IPluginDesc;
import nc.uap.lfw.core.page.IPlugoutDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.uimodel.ViewConfig;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.application.ApplicationEditor;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_application;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

/**
 * ConnectorCellModifier
 * 
 * @author dingrf
 *
 */
public class ConnectorCellModifier implements ICellModifier {
	
	private ConnectorPropertiesView view = null;
	
	private class CellModifiCommand extends Command{
		private String property = ""; //$NON-NLS-1$
		private Object value = null;
		private Object oldValue = null;
		private Connector attr = null;
		public CellModifiCommand(String property, Object value, Connector attr) {
			super(M_application.ConnectorCellModifier_0);
			this.property = property;
			this.value = value;
			this.attr = attr;
		}
		
		public void execute() {
			oldValue = getValue(attr, property);
			redo();
		}
		
		public void redo() {
			modifyAttr(attr, property, value);
		}
		
		public void undo() {
			modifyAttr(attr, property, oldValue);
		}
	}
	
	/**列名称*/
//	public static final String[] colNames = {"输出Window", "输出View", "plugout", "输入Window", "输入View", "plugin", "关系映射 "};
//	public static final String[] colNames = {"输出Window", "plugout", "输入Window", "plugin", "关系映射 "};
	public static final String[] colNames = {M_application.ConnectorCellModifier_1, M_application.ConnectorCellModifier_2, M_application.ConnectorCellModifier_3, M_application.ConnectorCellModifier_4};
	
	
	public ConnectorCellModifier(ConnectorPropertiesView view) {
		super();
		this.view = view;
	}
	
	/**
	 * 是否可修改
	 */
	public boolean canModify(Object element, String property) {
		if (view.isCanEdit())
			return true;
		else
			return false;
	}

	/**
	 * 取列值
	 */
	public Object getValue(Object element, String property) {
		if(element instanceof Connector){
			Connector prop = (Connector)element;
			if(colNames[0].equals(property)){
				return prop.getSource() == null? "" : prop.getSource(); //$NON-NLS-1$
			}
			else if(colNames[1].equals(property)){
				getPlugout(prop);
				return prop.getPlugoutId() == null?"":prop.getPlugoutId(); //$NON-NLS-1$
			}	
			else if(colNames[2].equals(property)){
				return prop.getTarget() == null?"":prop.getTarget(); //$NON-NLS-1$
			}	
			else if(colNames[3].equals(property)){
				getPlugin(prop);
				return prop.getPluginId() == null?"":prop.getPluginId(); //$NON-NLS-1$
			}	
			else if(colNames[4].equals(property)){
				return ""; //$NON-NLS-1$
			}	
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * 修改列值
	 */
	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem)element;
		Object object = item.getData();
		if(object instanceof Connector){
			Connector prop = (Connector)object;
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
			ApplicationEditor editor = (ApplicationEditor)ApplicationEditor.getActiveEditor();
			editor.setDirtyTrue();
			if(editor != null)
				editor.executComand(cmd);
		}
	}

	
	private void modifyAttr(Connector prop, String property,Object value){
		if (colNames[0].equals(property)) {
			prop.setSource((String) value);
			getPlugout(prop);
		} 
		else if (colNames[1].equals(property)) {
			prop.setPlugoutId((String) value);
		}
		else if (colNames[2].equals(property)) {
			prop.setTarget((String) value);
			getPlugin(prop);
		}
		else if (colNames[3].equals(property)) {
			prop.setPluginId((String) value);
		}
		view.getTv().update(prop, null);
	}
/*
	private void getSourceView(Connector prop) {

		String windowId = prop.getSourceWindow();
//		ApplicationEditor editor = ApplicationEditor.getActiveEditor();
//		ApplicationConf app = editor.getGraph().getApplication();
//		WindowConf win = app.getWindowConf(windowId);
		LfwWindow win = LFWAMCPersTool.getPageMetaById(windowId);
//		PageMeta win = LFWPersTool.getPagemetaById(windowId);
		if(win == null){
			return;
		}
//		String[] views = new String[win.getWidgets().length];
		String[] views = new String[win.getViewList().size()];
		int i = 0;
//		for (LfwWidget widget : win.getWidgets()){
//			views[i] = widget.getId();
//			i ++;
//		}
		for (ViewConfig widget : win.getViewList()){
			views[i] = widget.getId();
			i ++;
		}
		view.getSourceCellEditor().setObjectItems(views);
	}
	
	private void getTargetView(Connector prop) {
		String windowId = prop.getTargetWindow();
//		ApplicationEditor editor = ApplicationEditor.getActiveEditor();
//		ApplicationConf app = editor.getGraph().getApplication();
//		WindowConf win = app.getWindowConf(windowId);
		LfwWindow win = LFWAMCPersTool.getPageMetaById(windowId);
//		String[] views = new String[win.getWidgets().length];
		String[] views = new String[win.getViewList().size()];
		int i = 0;
//		for (LfwWidget widget : win.getWidgets()){
//			views[i] = widget.getId();
//			i ++;
//		}
		for (ViewConfig widget : win.getViewList()){
			views[i] = widget.getId();
			i ++;
		}
		view.getTargetCellEditor().setObjectItems(views);
	}
	*/
	
	private void getPlugout(Connector prop) {
		String windowId = prop.getSource();
		if (windowId == null){
			view.getPlugoutCellEditor().setObjectItems(new String[]{""}); //$NON-NLS-1$
			return;
		}
			
//		ApplicationEditor editor = ApplicationEditor.getActiveEditor();
//		ApplicationConf app = editor.getGraph().getApplication();
//		PageMeta win = LFWAMCPersTool.getPageMetaById(windowId);
		LfwWindow win = LFWAMCConnector.getWindowById(windowId);
		String[] plugouts = new String[win.getPlugoutDescs().length];
		if (plugouts.length > 0){
			int i = 0;
			for (IPlugoutDesc plugout : win.getPlugoutDescs()){
				plugouts[i] = plugout.getId();
				i ++;
			}
		} 
		
		view.getPlugoutCellEditor().setObjectItems(plugouts);
	}
	
	private void getPlugin(Connector prop) {
		String windowId = prop.getTarget();
		if (windowId == null){
			view.getPluginCellEditor().setObjectItems(new String[]{""}); //$NON-NLS-1$
			return;
		}		
//		ApplicationEditor editor = ApplicationEditor.getActiveEditor();
//		ApplicationConf app = editor.getGraph().getApplication();
//		WindowConf win = app.getWindowConf(windowId);
		LfwWindow win = LFWAMCConnector.getWindowById(windowId);
		String[] plugins = new String[win.getPluginDescs().length];
		if (plugins.length > 0){
			int i = 0;
			for (IPluginDesc plugin : win.getPluginDescs()){
				plugins[i] = plugin.getId();
				i ++;
			}
		}
		view.getPluginCellEditor().setObjectItems(plugins);
	}
}

