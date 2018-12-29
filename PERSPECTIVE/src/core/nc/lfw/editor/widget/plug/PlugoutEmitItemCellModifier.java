package nc.lfw.editor.widget.plug;


import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.widget.WidgetEditor;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.PlugoutEmitItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

import uap.lfw.core.plug.PlugConstant;

/**
 * PlugoutDescEmitCellModifier
 * 
 * @author dingrf
 *
 */
public class PlugoutEmitItemCellModifier implements ICellModifier {
	
	private PlugoutEmitItemPropertiesView view = null;
	
	private class CellModifiCommand extends Command{
		private String property = "";
		private Object value = null;
		private Object oldValue = null;
		private PlugoutEmitItem attr = null;
		public CellModifiCommand(String property, Object value, PlugoutEmitItem attr) {
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
	public static final String[] colNames = {"触发器名称","触发类型","触发来源","描述"};
	
	private String oldType = "";
	
	
	public PlugoutEmitItemCellModifier(PlugoutEmitItemPropertiesView view) {
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
		if(element instanceof PlugoutEmitItem){
			PlugoutEmitItem prop = (PlugoutEmitItem)element;
			if(colNames[0].equals(property)){
				return prop.getId() == null? "" : prop.getId();
			}else if(colNames[1].equals(property)){
				return prop.getType() == null?"":prop.getType();
			}else if(colNames[2].equals(property)){
				String type = prop.getType()==null?"":prop.getType();
				type = type.split("\\.")[0];
				if (!type.equals(oldType)){
					updateSource(type);
					oldType = type;
				}
				return prop.getSource() == null?"":prop.getSource();
			}else if(colNames[3].equals(property)){
				return prop.getDesc() == null?"":prop.getDesc();
			}
		}
		return "";
	}
	
	private void updateSource(String type) {
		WidgetEditor editor = WidgetEditor.getActiveWidgetEditor();
		LfwView widget =  editor.getGraph().getWidget();
		String[] empty ={""};
		this.view.getTypeCellEditor().setObjectItems(empty);
		
		if (type.equals(PlugConstant.CONTROL_TYPE_DATASET)){
			List<String> datasetIds = new ArrayList<String>();
			for (Dataset  ds :  widget.getViewModels().getDatasets()){
				datasetIds.add(ds.getId());
			}
			this.view.getTypeCellEditor().setObjectItems(datasetIds.toArray());
		}
		else if (type.equals(PlugConstant.CONTROL_TYPE_MENU)){
			List<String> menuItemIds = new ArrayList<String>();
			for (MenubarComp mb : widget.getViewMenus().getMenuBars()){
				String menubarId = mb.getId();
				for (MenuItem item: mb.getMenuList()){
					menuItemIds.add(menubarId + "." + item.getId());
				}
			}
			this.view.getTypeCellEditor().setObjectItems(menuItemIds.toArray());
		}
		else if (type.equals(PlugConstant.CONTROL_TYPE_BUTTON)){
			List<String> compIds = new ArrayList<String>();
			for (WebComponent vc : widget.getViewComponents().getComponents()){
				if (vc instanceof ButtonComp)
					compIds.add(vc.getId());
			}
			this.view.getTypeCellEditor().setObjectItems(compIds.toArray());
		}
		else if (type.equals(PlugConstant.CONTROL_TYPE_TEXTCOMP)){
			List<String> textCompIds = new ArrayList<String>();
			for (WebComponent vc : widget.getViewComponents().getComponents()){
				if (vc instanceof TextComp)
					textCompIds.add(vc.getId());
			}
			this.view.getTypeCellEditor().setObjectItems(textCompIds.toArray());
		}
	}

	/**
	 * 修改列值
	 */
	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem)element;
		Object object = item.getData();
		if(object instanceof PlugoutEmitItem){
			PlugoutEmitItem prop = (PlugoutEmitItem)object;
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
			WidgetEditor editor = (WidgetEditor)WidgetEditor.getActiveEditor();
			editor.setDirtyTrue();
			if(editor != null)
				editor.executComand(cmd);
		}
	}

	
	private void modifyAttr(PlugoutEmitItem prop, String property,Object value){
		if (colNames[0].equals(property)) {
			prop.setId((String) value);
		} 
		else if (colNames[1].equals(property)) {
			prop.setType((String) value);
		}
		else if (colNames[2].equals(property)) {
			prop.setSource((String) value);
		}
		else if (colNames[3].equals(property)) {
			prop.setDesc((String) value);
		}
		view.getTv().update(prop, null);
	}
}

