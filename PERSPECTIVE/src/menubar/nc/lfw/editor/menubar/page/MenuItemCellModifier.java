package nc.lfw.editor.menubar.page;

import nc.lfw.editor.menubar.MenubarEditor;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 目前已不用此类
 * 
 * 单元格Modifier
 *
 */
public class MenuItemCellModifier implements ICellModifier {
	public static final String[] colNames = {"ID",M_menubar.MenuItemCellModifier_1,M_menubar.MenuItemCellModifier_2,M_menubar.MenuItemCellModifier_3,M_menubar.MenuItemCellModifier_4,M_menubar.MenuItemCellModifier_5, M_menubar.MenuItemCellModifier_6,M_menubar.MenuItemCellModifier_7,M_menubar.MenuItemCellModifier_8,M_menubar.MenuItemCellModifier_9,M_menubar.MenuItemCellModifier_10}; //$NON-NLS-1$
	
	private MenuItemPropertiesView view = null;
	private class CellModifiCommand extends Command{
		private String property = ""; //$NON-NLS-1$
		private Object value = null;
		private Object oldValue = null;
		private MenuItem attr = null;
		public CellModifiCommand(String property, Object value, MenuItem attr) {
			super(M_menubar.MenuItemCellModifier_12);
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
	
	public MenuItemCellModifier(MenuItemPropertiesView view) {
		super();
		this.view = view;
	}
	private  TreeViewer getTreeViewer(){
		//TODO
//		return getPropertiesView().getTv();
		return null;
	}
	private MenuItemPropertiesView getPropertiesView(){
		return view;
	}
	public boolean canModify(Object element, String property) {
		return true;
	}

	public Object getValue(Object element, String property) {
		MenuItem item = (MenuItem)element;
		if(colNames[0].equals(property)){
			return item.getId();
		}
		else if(colNames[1].equals(property)){
			return item.getText() == null ? "" : item.getText(); //$NON-NLS-1$
		}
		else if(colNames[2].equals(property)){
		}
		return null;
	}

	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem)element;
		Object object = item.getData();
		if(object instanceof MenuItem){
			MenuItem prop = (MenuItem)object;
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
			MenubarEditor editor = MenubarEditor.getActiveMenubarEditor();
			if(editor != null)
				editor.executComand(cmd);
		}
	}

	
	private void modifyAttr(MenuItem prop, String property,Object value){
		if(colNames[0].equals(property)){
			prop.setId((String)value);
		}
		else if(colNames[1].equals(property)){
			 prop.setText((String)value);
		}
//		else if(colNames[2].equals(property)){
//			prop.setIdColName((String)value);
//		}else if(colNames[3].equals(property)){
//			prop.setText((String)value);
//		}else if(colNames[4].equals(property)){
//			prop.setDataType((String)value);
//		}else if(colNames[5].equals(property)){
//			prop.setNullAble((Boolean)value);	
//		}else if(colNames[6].equals(property)){
//			prop.setDefaultValue((String)value);
//		}else if(colNames[7].equals(property)){
//			prop.setPrimaryKey((Boolean)value);	
//		}else if(colNames[8].equals(property)){
//			prop.setDefEditFormular((String)value);
//		}else if(colNames[9].equals(property)){
//			prop.setValidateFormula((String)value);
//		}else if(colNames[10].equals(property)){
//			prop.setLock((Boolean)value);	
//		}else if(colNames[11].equals(property)){
//			prop.setFormater((String)value);
//		}else if(colNames[12].equals(property)){
//			prop.setLangDir((String)value);
//		}
		getTreeViewer().refresh(prop);
		// ((DatasetElementObj)getPropertiesView().getLfwElementPart().getModel())..firePropUpdate(prop);
	}
}

