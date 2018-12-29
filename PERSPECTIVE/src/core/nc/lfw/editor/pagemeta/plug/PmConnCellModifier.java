package nc.lfw.editor.pagemeta.plug;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.pagemeta.RelationEditor;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.IPluginDesc;
import nc.uap.lfw.core.page.IPlugoutDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PluginProxy;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutProxy;
import nc.uap.lfw.lang.M_pagemeta;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

public class PmConnCellModifier implements ICellModifier {

	private PmConnPropertiesView view = null;

	private class CellModifiCommand extends Command {
		private String property = ""; //$NON-NLS-1$
		private Object value = null;
		private Object oldValue = null;
		private Connector attr = null;

		public CellModifiCommand(String property, Object value, Connector attr) {
			super(M_pagemeta.ConnectorCellModifier_0);
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

	public static final String[] colNames = {M_pagemeta.PmConnCellModifier_0, M_pagemeta.PmConnCellModifier_1,M_pagemeta.PmConnCellModifier_2, M_pagemeta.PmConnCellModifier_3 };

	public PmConnCellModifier(PmConnPropertiesView view) {
		super();
		this.view = view;
	}

	@Override
	/**
	 * 是否可修改
	 */
	public boolean canModify(Object element, String property) {
		if (view.isCanEdit())
			return true;
		else
			return false;
	}

	@Override
	public Object getValue(Object element, String property) {
		if (element instanceof Connector) {
			Connector prop = (Connector) element;
			if((!"".equals(prop.getSource())||!"".equals(prop.getTarget()))){ //$NON-NLS-1$ //$NON-NLS-2$
				viewId = "".equals(prop.getSource())?prop.getTarget():prop.getSource(); //$NON-NLS-1$
				getPlugout(prop);
				getPlugin(prop);
			}
			if (colNames[0].equals(property)) {
				return prop.getPlugoutId() == null ? "" : prop.getPlugoutId(); //$NON-NLS-1$
			} else if (colNames[1].equals(property)) {
				return viewId == null?"":viewId; //$NON-NLS-1$
			} else if (colNames[2].equals(property)) {
				return prop.getPluginId() == null ? "" : prop.getPluginId(); //$NON-NLS-1$
			} else if (colNames[3].equals(property)) {
				return ""; //$NON-NLS-1$
			}
		}
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem) element;
		Object object = item.getData();
		if (object instanceof Connector) {
			Connector prop = (Connector) object;
			Object old = getValue(prop, property);
			if (old != null && old.equals(value))
				return;
			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
			RelationEditor editor = RelationEditor.getActiveRelationEditor();
			editor.setDirtyTrue();
			if (editor != null)
				editor.executComand(cmd);
		}
	}
//	private String windowId = null;
	private String viewId = null;

	private void modifyAttr(Connector prop, String property, Object value) {
//		if (colNames[0].equals(property)) {
////			prop.setSourceWindow((String) value);
//			windowId = (String)value;
//			getPlugout(prop);
//			getPlugin(prop);
//		} else 
		if (colNames[0].equals(property)) {
//			prop.setSource((String) value);
			viewId = (String)value;
			prop.setSource(viewId);  //暂存在这个属性
			getPlugout(prop);
			getPlugin(prop);
		} else if (colNames[1].equals(property)) {
			prop.setPlugoutId((String) value);
		} else if (colNames[2].equals(property)) {
			prop.setPluginId((String) value);
		}
		LfwWindow win = RelationEditor.getActiveRelationEditor().getGraph().getPagemeta();
		List<String> pluginIdList = new ArrayList<String>();
		List<String> plugoutIdList = new ArrayList<String>();
		if(win.getPlugoutDescs().length > 0){
			for(IPlugoutDesc plugout:win.getPlugoutDescs()){
				plugoutIdList.add(plugout.getId());
			}
		}
		if(win.getPluginDescs().length>0){
			for(IPluginDesc plugin:win.getPluginDescs()){
				pluginIdList.add(plugin.getId());
			}
		}
		if (pluginIdList.contains(prop.getPluginId())) {
//			prop.setTargetWindow(win.getId());
//			if(windowId!=null)
//				prop.setSourceWindow(windowId);
			if(viewId!=null){
				prop.setSource(viewId);
				prop.setTarget("");
				prop.setConnType(Connector.VIEW_WINDOW);
			}				
		}
		if (plugoutIdList.contains(prop.getPlugoutId())) {
//			prop.setSourceWindow(win.getId());
//			if(windowId!=null)
//				prop.setTargetWindow(windowId);
			if(viewId!=null){
				prop.setTarget(viewId);
				prop.setSource("");
				prop.setConnType(Connector.WINDOW_VIEW);
			}
				
		}
//		if(windowId!=null){
//			prop.setConnType(Connector.WINDOW_INLINEWINDOW);
//		}
//		else if(viewId!=null){
//			prop.setConnType(Connector.WINDOW_VIEW);
//		}
		view.getTv().update(prop, null);
	}

	private void getPlugout(Connector prop) {
		LfwWindow win = RelationEditor.getActiveRelationEditor().getGraph().getPagemeta();
		List<String> plugIdList = new ArrayList<String>();
		if (viewId != null) {
			String widgetId = viewId;
			LfwView widget = win.getView(widgetId);
			
			if(win.getPlugoutDescs().length>0){
				for(IPlugoutDesc plugout:win.getPlugoutDescs()){
					if(plugout instanceof PlugoutProxy)
						continue;
					plugIdList.add(plugout.getId());
				}
			}
			if(widget.getPlugoutDescs().length > 0){
				for(PlugoutDesc plugout:widget.getPlugoutDescs()){
					plugIdList.add(plugout.getId());
				}
			}
			String[] plugouts = plugIdList.toArray(new String[0]);
			view.getPlugoutCellEditor().setObjectItems(plugouts);
		}
	}
	private void getPlugin(Connector prop) {
		LfwWindow win = RelationEditor.getActiveRelationEditor().getGraph().getPagemeta();
		List<String> plugIdList = new ArrayList<String>();
//		if (windowId != null) {
//			String winId = windowId;
//			PageMeta pm = LFWAMCPersTool.getPageMetaDetailById(winId);
//			if (pm != null) {
//				if(win.getPluginDescs()!=null&&win.getPluginDescs().size()>0){
//					for(PluginDesc plugin:win.getPluginDescs()){
//						plugIdList.add(plugin.getId());
//					}
//				}
//				if(pm.getPluginDescs()!=null&&pm.getPluginDescs().size()>0){
//					for(PluginDesc plugin:pm.getPluginDescs()){
//						plugIdList.add(plugin.getId());
//					}
//				}
//				String[] plugins = plugIdList.toArray(new String[0]);
//
//				view.getPluginCellEditor().setObjectItems(plugins);
//				
//			}
//
//		}
		if (viewId != null) {
			String widgetId = viewId;
			LfwView widget = win.getView(widgetId);

			if(win.getPluginDescs().length>0){
				for(IPluginDesc plugin:win.getPluginDescs()){
					if(plugin instanceof PluginProxy)
						continue;
					plugIdList.add(plugin.getId());
				}
			}
			if(widget.getPluginDescs().length>0){
				for(PluginDesc plugin:widget.getPluginDescs()){
					plugIdList.add(plugin.getId());
				}
			}
			String[] plugins =  plugIdList.toArray(new String[0]);
			view.getPluginCellEditor().setObjectItems(plugins);
		}
	}

}
