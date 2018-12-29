package nc.lfw.editor.pagemeta.plug;


import java.util.List;

import nc.lfw.editor.pagemeta.PagemetaEditor;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.lang.M_pagemeta;

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
	
	/**列名称*/
	public static final String[] colNames = {M_pagemeta.ConnectorCellModifier_1, M_pagemeta.ConnectorCellModifier_2, M_pagemeta.ConnectorCellModifier_3/*, M_pagemeta.ConnectorCellModifier_4*/};
	
	
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
				return prop.getPlugoutId() == null? "" : prop.getPlugoutId();
			}
			else if(colNames[1].equals(property)){
				return prop.getTarget() == null? "" : prop.getTarget();
			}
			else if(colNames[2].equals(property)){
				getPlugin(prop);
				return prop.getPluginId() == null? "" : prop.getPluginId();
			}
			else if(colNames[3].equals(property)){
				return "";
			}
//			if(colNames[0].equals(property)){
//				return prop.getSourceWindow() == null? "" : prop.getSourceWindow(); //$NON-NLS-1$
//			}
//			else if(colNames[1].equals(property)){
//				return prop.getSource() == null?"":prop.getSource(); //$NON-NLS-1$
//			}	
//			else if(colNames[2].equals(property)){
//				return prop.getPlugoutId() == null?"":prop.getPlugoutId(); //$NON-NLS-1$
//			}	
//			else if(colNames[3].equals(property)){
//				return prop.getTargetWindow() == null?"":prop.getTargetWindow(); //$NON-NLS-1$
//			}	
//			else if(colNames[4].equals(property)){
//				return prop.getTarget() == null?"":prop.getTarget(); //$NON-NLS-1$
//			}	
//			else if(colNames[5].equals(property)){
//				return prop.getPluginId() == null?"":prop.getPluginId(); //$NON-NLS-1$
//			}	
//			else if(colNames[6].equals(property)){
//				return ""; //$NON-NLS-1$
//			}	
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
			PagemetaEditor editor = PagemetaEditor.getActivePagemetaEditor();
			editor.setDirtyTrue();
			if(editor != null)
				editor.executComand(cmd);
		}
	}

	
	private void modifyAttr(Connector prop, String property,Object value){
		if (colNames[0].equals(property)) {
			prop.setPlugoutId((String) value);
//			getSourceView(prop);
		} 
		else if (colNames[1].equals(property)) {
			prop.setTarget((String) value);
			getPlugin(prop);
		}
		else if (colNames[2].equals(property)) {
			prop.setPluginId((String) value);
		}
		if(prop.getSource()==null){
			prop.setSource(view.getWidget().getWidget().getId());
		}
		view.getTv().update(prop, null);
	}

//	private void getSourceView(Connector prop) {
//
//		String windowId = prop.getSourceWindow();
////		ApplicationEditor editor = ApplicationEditor.getActiveEditor();
////		ApplicationConf app = editor.getGraph().getApplication();
////		WindowConf win = app.getWindowConf(windowId);
//		PageMeta win = LFWAMCPersTool.getPageMetaById(windowId);
//		if(win == null){
//			return;
//		}
//		String[] views = new String[win.getWidgets().length];
//		int i = 0;
//		for (LfwWidget widget : win.getWidgets()){
//			views[i] = widget.getId();
//			i ++;
//		}
//		view.getSourceCellEditor().setObjectItems(views);
//	}
	
//	private void getTargetView(Connector prop) {
//		String windowId = prop.getTargetWindow();
////		ApplicationEditor editor = ApplicationEditor.getActiveEditor();
////		ApplicationConf app = editor.getGraph().getApplication();
////		WindowConf win = app.getWindowConf(windowId);
//		PageMeta win = LFWAMCPersTool.getPageMetaById(windowId);
//		String[] views = new String[win.getWidgets().length];
//		int i = 0;
//		for (LfwWidget widget : win.getWidgets()){
//			views[i] = widget.getId();
//			i ++;
//		}
//		view.getTargetCellEditor().setObjectItems(views);
//	}
	
//	private void getPlugout(Connector prop) {
//		String windowId = prop.getSourceWindow();
//		String viewId = prop.getSource();
//		if (windowId == null || viewId == null){
//			view.getPlugoutCellEditor().setObjectItems(new String[]{""});
//			return;
//		}
//			
////		ApplicationEditor editor = ApplicationEditor.getActiveEditor();
////		ApplicationConf app = editor.getGraph().getApplication();
//		PageMeta win = LFWAMCPersTool.getPageMetaById(windowId);
//		LfwWidget widget = win.getWidget(viewId);
//		String[] plugouts = new String[widget.getPlugoutDescs() == null ? 0 : widget.getPlugoutDescs().size()];
//		if (plugouts.length > 0){
//			int i = 0;
//			for (PlugoutDesc plugout : widget.getPlugoutDescs()){
//				plugouts[i] = plugout.getId();
//				i ++;
//			}
//		} 
//		view.getPlugoutCellEditor().setObjectItems(plugouts);
//	}
	
	private void getPlugin(Connector prop) {
		String viewId = prop.getTarget();
		PagemetaEditor editor = PagemetaEditor.getActivePagemetaEditor();
		List<WidgetElementObj> widgetCells = editor.getGraph().getWidgetCells();
		if (viewId == null){
			view.getPluginCellEditor().setObjectItems(new String[]{""}); //$NON-NLS-1$
			return;
		}
		
//		ApplicationEditor editor = ApplicationEditor.getActiveEditor();
//		ApplicationConf app = editor.getGraph().getApplication();
//		WindowConf win = app.getWindowConf(windowId);
		LfwWindow win = PagemetaEditor.getActivePagemetaEditor().getGraph().getPagemeta();
		LfwView widget = win.getView(viewId);
		String[] plugins = new String[widget.getPluginDescs().length];
		if (plugins.length > 0){
			int i = 0;
			for (PluginDesc plugin : widget.getPluginDescs()){
				plugins[i] = plugin.getId();
				i ++;
			}
		}
		view.getPluginCellEditor().setObjectItems(plugins);
	}
}

