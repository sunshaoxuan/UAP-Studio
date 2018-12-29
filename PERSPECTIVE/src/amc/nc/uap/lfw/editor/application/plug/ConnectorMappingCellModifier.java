package nc.uap.lfw.editor.application.plug;


import nc.uap.lfw.editor.application.plug.ConnectorMappingDialog.PlugRelation;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 * @author dingrf
 *
 */
public class ConnectorMappingCellModifier implements ICellModifier {
	
	private ConnectorMappingDialog dialog = null;
	
//	private class CellModifiCommand extends Command{
//		private String property = "";
//		private Object value = null;
//		private Object oldValue = null;
//		private PlugRelation attr = null;
//		public CellModifiCommand(String property, Object value, PlugRelation attr) {
//			super("修改属性");
//			this.property = property;
//			this.value = value;
//			this.attr = attr;
//		}
//		
//		public void execute() {
//			oldValue = getValue(attr, property);
//			redo();
//		}
//		
//		public void redo() {
//			modifyAttr(attr, property, value);
//		}
//		
//		public void undo() {
//			modifyAttr(attr, property, oldValue);
//		}
//	}
	
	/**列名称*/
	public static final String[] colNames = {"输出键值","输入键值"};
	
	
	public ConnectorMappingCellModifier(ConnectorMappingDialog dialog) {
		super();
		this.dialog = dialog;
	}
	
	/**
	 * 是否可修改
	 */
	public boolean canModify(Object element, String property) {
		if(colNames[1].equals(property)){
			return true;
		}else
			return false;
	}

	/**
	 * 取列值
	 */
	public Object getValue(Object element, String property) {
		if(element instanceof PlugRelation){
			PlugRelation prop = (PlugRelation)element;
			if(colNames[0].equals(property)){
				return prop.getOutValue() == null ? "" : prop.getOutValue();
			}else if(colNames[1].equals(property)){
				return prop.getInValue() == null ? "" : prop.getInValue();
			}
		}
		return "";
	}

	/**
	 * 修改列值
	 */
	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem)element;
		Object object = item.getData();
		if(object instanceof PlugRelation){
			PlugRelation prop = (PlugRelation)object;
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			modifyAttr(prop, property, value);
//			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
//			PagemetaEditor editor = (PagemetaEditor)PagemetaEditor.getActiveEditor();
//			editor.setDirtyTrue();
//			
//			if(editor != null)
//				editor.executComand(cmd);
		}
	}

	
	private void modifyAttr(PlugRelation prop, String property,Object value){
		if (colNames[0].equals(property)) {
			prop.setOutValue((String) value);
		} 
		else if (colNames[1].equals(property)) {
			prop.setInValue((String) value);
		}
		dialog.getTv().update(prop, null);
	}
}

