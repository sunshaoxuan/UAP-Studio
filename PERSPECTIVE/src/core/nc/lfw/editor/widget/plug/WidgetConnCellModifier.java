package nc.lfw.editor.widget.plug;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.widget.WidgetEditor;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.IPluginDesc;
import nc.uap.lfw.core.page.IPlugoutDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_pagemeta;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

public class WidgetConnCellModifier implements ICellModifier{

	private WidgetConnPropertiesView view = null;
	
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
	public static final String[] colNames = {M_pagemeta.WidgetConnCellModifier_0, M_pagemeta.WidgetConnCellModifier_1, M_pagemeta.WidgetConnCellModifier_2/*, "关系映射"*/ };
	
	public WidgetConnCellModifier(WidgetConnPropertiesView view){
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
	private String inlineWindowId = null;
	
	private void modifyAttr(Connector prop, String property, Object value) {
		if (colNames[0].equals(property)) {
//			prop.setSource((String) value);
			inlineWindowId = (String)value;
			prop.setSource(inlineWindowId);  //暂存在这个属性
			getPlugout(prop);
			getPlugin(prop);
		} else if (colNames[1].equals(property)) {
			prop.setPlugoutId((String) value);
		} else if (colNames[2].equals(property)) {
			prop.setPluginId((String) value);
		}
		LfwView widget = WidgetEditor.getActiveWidgetEditor().getGraph().getWidget();
		List<String> pluginIdList = new ArrayList<String>();
		List<String> plugoutIdList = new ArrayList<String>();
		if(widget.getPlugoutDescs().length>0){
			for(PlugoutDesc plugout:widget.getPlugoutDescs()){
				plugoutIdList.add(plugout.getId());
			}
		}
		if(widget.getPluginDescs().length>0){
			for(PluginDesc plugin:widget.getPluginDescs()){
				pluginIdList.add(plugin.getId());
			}
		}
		if (pluginIdList.contains(prop.getPluginId())) {
			if(inlineWindowId!=null){
				prop.setSource(inlineWindowId);
				prop.setTarget(""); //$NON-NLS-1$
				prop.setConnType(Connector.INLINEWINDOW_VIEW);
			}	
		}
		else if (plugoutIdList.contains(prop.getPlugoutId())) {
			if(inlineWindowId!=null){
				prop.setTarget(inlineWindowId);
				prop.setSource(""); //$NON-NLS-1$
				prop.setConnType(Connector.VIEW_INLINEWINDOW);
			}	
		}
		
		view.getTv().update(prop, null);
	}

	
	@Override
	public Object getValue(Object element, String property) {
		if (element instanceof Connector && ((((Connector) element).getConnType()
				.equals(Connector.INLINEWINDOW_VIEW)) || (((Connector) element)
				.getConnType().equals(Connector.INLINEWINDOW_VIEW)))) {
			Connector prop = (Connector) element;
			if((!"".equals(prop.getSource())||!"".equals(prop.getTarget()))){ //$NON-NLS-1$ //$NON-NLS-2$
				inlineWindowId = "".equals(prop.getSource())?prop.getTarget():prop.getSource(); //$NON-NLS-1$
				getPlugout(prop);
				getPlugin(prop);
			}
			if (colNames[0].equals(property)) {
				return inlineWindowId == null?"":inlineWindowId; //$NON-NLS-1$
			} else if (colNames[1].equals(property)) {
				return prop.getPlugoutId() == null ? "" : prop.getPlugoutId(); //$NON-NLS-1$
			} else if (colNames[2].equals(property)) {
				return prop.getPluginId() == null ? "" : prop.getPluginId(); //$NON-NLS-1$
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
			WidgetEditor editor = WidgetEditor.getActiveWidgetEditor();
			editor.setDirtyTrue();
			if (editor != null)
				editor.executComand(cmd);
		}
	}

	private void getPlugout(Connector prop) {
		LfwView widget = WidgetEditor.getActiveWidgetEditor().getGraph().getWidget();
		
		if(inlineWindowId!=null){
			String windowId = inlineWindowId;
			LfwWindow pm = LFWAMCPersTool.getPageMetaDetailById(windowId);
			List<String> plugIdList = new ArrayList<String>();
			if(widget.getPlugoutDescs().length>0){
				for(PlugoutDesc plugout:widget.getPlugoutDescs()){
					plugIdList.add(plugout.getId());
				}
			}
			if(pm.getPlugoutDescs().length > 0){
				for(IPlugoutDesc plugout : pm.getPlugoutDescs()){
					plugIdList.add(plugout.getId());
				}
			}
			String[] plugouts = plugIdList.toArray(new String[0]);
			view.getPlugoutCellEditor().setObjectItems(plugouts);
		}
	}
	
	private void getPlugin(Connector prop) {
		
		LfwView widget = WidgetEditor.getActiveWidgetEditor().getGraph().getWidget();
		
		if(inlineWindowId!=null){
			String windowId = inlineWindowId;
			LfwWindow pm = LFWAMCPersTool.getPageMetaDetailById(windowId);
			List<String> plugIdList = new ArrayList<String>();
			if(widget.getPluginDescs().length>0){
				for(PluginDesc plugin:widget.getPluginDescs()){
					plugIdList.add(plugin.getId());
				}
			}
			if(pm.getPluginDescs().length>0){
				for(IPluginDesc plugin:pm.getPluginDescs()){
					plugIdList.add(plugin.getId());
				}
			}
			String[] plugins = plugIdList.toArray(new String[0]);
			view.getPluginCellEditor().setObjectItems(plugins);
		}
	}
	
}
