package nc.lfw.editor.widget.plug;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.widget.WidgetEditor;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.PlugoutDescItem;
import nc.uap.lfw.core.comp.text.TextComp;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

/**
 * PlugoutDescCellModifier
 * 
 * @author dingrf
 *
 */
public class PlugoutDescItemCellModifier implements ICellModifier {
	
	private PlugoutDescItemPropertiesView view = null;
	
	private class CellModifiCommand extends Command{
		private String property = "";
		private Object value = null;
		private Object oldValue = null;
		private PlugoutDescItem attr = null;
		public CellModifiCommand(String property, Object value, PlugoutDescItem attr) {
			super("修改属性");
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
//	public static final String[] colNames = {"输出键名称","取数类型","取数来源","值","描述","输出对象类型"};
	public static final String[] colNames = {"输出键名称","取数类型","取数来源","描述","输出对象类型"};
	
	private String oldType = "";
	
	public PlugoutDescItemCellModifier(PlugoutDescItemPropertiesView view) {
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
		if(element instanceof PlugoutDescItem){
			PlugoutDescItem prop = (PlugoutDescItem)element;
			if(colNames[0].equals(property)){
				return prop.getName()==null? "" : prop.getName();
			}else if(colNames[1].equals(property)){
				return prop.getType()==null?"":prop.getType();
			}else if(colNames[2].equals(property)){
				String type = prop.getType()==null?"":prop.getType();
				type = type.split("\\.")[0];
				if (!type.equals(oldType)){
					updateSource(type);
					oldType = type;
				}
				return prop.getSource()==null?"":prop.getSource();
			}else if(colNames[3].equals(property)){
//				return prop.getValue()==null?"":prop.getValue();
				return prop.getDesc()==null?"":prop.getDesc();
			}else if(colNames[4].equals(property)){
				return prop.getClazztype()==null?"":prop.getClazztype();
//			}else if(colNames[5].equals(property)){
			}
		}
		return "";
	}
	
	private void updateSource(String type) {
		WidgetEditor editor = WidgetEditor.getActiveWidgetEditor();
		LfwView widget =  editor.getGraph().getWidget();
		String[] empty ={""};
		this.view.getTypeCellEditor().setObjectItems(empty);
		
		List<String> ids = new ArrayList<String>();
		if (type.equals("Dataset")){
			for (Dataset  ds :  widget.getViewModels().getDatasets()){
				ids.add(ds.getId());
			}
			this.view.getTypeCellEditor().setObjectItems(ids.toArray());
		}
		//控件值
		else if (type.equals("TYPE_COMPONENT_VALUE")){
			List<String> compIds = new ArrayList<String>();
			Iterator<WebComponent> it = widget.getViewComponents().getComponentsMap().values().iterator();
			while (it.hasNext()){
				WebComponent comp = it.next();
				if (comp instanceof TextComp){
					ids.add(comp.getId());
				}
			}
			this.view.getTypeCellEditor().setObjectItems(ids.toArray());
		}
	}

	/**
	 * 修改列值
	 */
	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem)element;
		Object object = item.getData();
		if(object instanceof PlugoutDescItem){
			PlugoutDescItem prop = (PlugoutDescItem)object;
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
//			WidgetEditor editor = (WidgetEditor)WidgetEditor.getActiveEditor();
			LFWBaseEditor editor = WidgetEditor.getActiveEditor();
			editor.setDirtyTrue();
			
			if(editor != null)
				editor.executComand(cmd);
		}
	}

	
	private void modifyAttr(PlugoutDescItem prop, String property,Object value){
		if (colNames[0].equals(property)) {
			prop.setName((String) value);
		} 
		else if (colNames[1].equals(property)) {
			String type = (String) value;
			prop.setType(type);
		}
		else if (colNames[2].equals(property)) {
			prop.setSource((String) value);
		}
		else if (colNames[3].equals(property)) {
//			prop.setValue((String) value);
			prop.setDesc((String) value);
		}
		else if (colNames[4].equals(property)) {
			prop.setClazztype((String) value);
		}
//		else if (colNames[5].equals(property)) {
//		}
		view.getTv().update(prop, null);
	}
}

