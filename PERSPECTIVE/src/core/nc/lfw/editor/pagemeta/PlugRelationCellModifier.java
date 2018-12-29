package nc.lfw.editor.pagemeta;


import nc.lfw.editor.pagemeta.PlugRelationDialog.PlugRelation;
import nc.uap.lfw.lang.M_pagemeta;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

/**
 * PlugoutDescCellModifier
 * 
 * @author dingrf
 *
 */
public class PlugRelationCellModifier implements ICellModifier {
	
	private PlugRelationDialog dialog = null;
	
	private class CellModifiCommand extends Command{
		private String property = ""; //$NON-NLS-1$
		private Object value = null;
		private Object oldValue = null;
		private PlugRelation attr = null;
		public CellModifiCommand(String property, Object value, PlugRelation attr) {
			super(M_pagemeta.PlugRelationCellModifier_0);
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
	public static final String[] colNames = {M_pagemeta.PlugRelationCellModifier_1,M_pagemeta.PlugRelationCellModifier_2};
	
	
	public PlugRelationCellModifier(PlugRelationDialog dialog) {
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
				return prop.getOutValue() == null ? "" : prop.getOutValue(); //$NON-NLS-1$
			}else if(colNames[1].equals(property)){
				return prop.getInValue() == null ? "" : prop.getInValue(); //$NON-NLS-1$
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
		if(object instanceof PlugRelation){
			PlugRelation prop = (PlugRelation)object;
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
			PagemetaEditor editor = (PagemetaEditor)PagemetaEditor.getActiveEditor();
			editor.setDirtyTrue();
			
			if(editor != null)
				editor.executComand(cmd);
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

