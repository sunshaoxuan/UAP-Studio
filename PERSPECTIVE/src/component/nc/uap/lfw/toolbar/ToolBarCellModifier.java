package nc.uap.lfw.toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.lang.M_toolbar;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;

public class ToolBarCellModifier implements ICellModifier{

	//private TreeViewer treeViewer;
	private ToolBarPropertiesView view = null;
	
	private Map<String, String> hotKeyModifierMap;
	
	public static final String[] colNames = {"ID",M_toolbar.ToolBarCellModifier_0,M_toolbar.ToolBarCellModifier_1,M_toolbar.ToolBarCellModifier_2,M_toolbar.ToolBarCellModifier_3,M_toolbar.ToolBarCellModifier_4,M_toolbar.ToolBarCellModifier_5,M_toolbar.ToolBarCellModifier_6,M_toolbar.ToolBarCellModifier_7,M_toolbar.ToolBarCellModifier_8, //$NON-NLS-1$
	M_toolbar.ToolBarCellModifier_9, M_toolbar.ToolBarCellModifier_10, M_toolbar.ToolBarCellModifier_11};
	
	
	private void initHotKeyModifierMap() {
		if (hotKeyModifierMap == null) {
			hotKeyModifierMap = new HashMap<String, String>();
			hotKeyModifierMap.put("1", "SHIFT"); //$NON-NLS-1$ //$NON-NLS-2$
			hotKeyModifierMap.put("2", "CTRL"); //$NON-NLS-1$ //$NON-NLS-2$
			hotKeyModifierMap.put("8", "ALT"); //$NON-NLS-1$ //$NON-NLS-2$
			hotKeyModifierMap.put("3", "CTRL+SHIFT"); //$NON-NLS-1$ //$NON-NLS-2$
			hotKeyModifierMap.put("10", "CTRL+ALT"); //$NON-NLS-1$ //$NON-NLS-2$
			hotKeyModifierMap.put("9", "ALT+SHIFT"); //$NON-NLS-1$ //$NON-NLS-2$
			hotKeyModifierMap.put("11", "CTRL+SHIFT+ALT"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
		
	private class CellModifiCommand extends Command{
		private String property = ""; //$NON-NLS-1$
		private Object value = null;
		private Object oldValue = null;
		private ToolBarItem attr = null;
		public CellModifiCommand(String property, Object value, ToolBarItem attr) {
			super(M_toolbar.ToolBarCellModifier_12);
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
	
	public ToolBarCellModifier(ToolBarPropertiesView view) {
		super();
		this.view = view;
		initHotKeyModifierMap();
	}
	private  TreeViewer getTreeViewer(){
		return getPropertiesView().getTv();
	}
	private ToolBarPropertiesView getPropertiesView(){
		return view;
	}
	
	private void modifyAttr(ToolBarItem prop, String property,Object value){
		if(colNames[0].equals(property)){
			prop.setId((String)value);
		}else if(colNames[1].equals(property)){
			prop.setText((String)value);
		}else if(colNames[2].equals(property)){
			 prop.setI18nName((String)value);
		}else if(colNames[3].equals(property)){
			prop.setLangDir((String)value);
		}
		else if(colNames[4].equals(property)){
			 prop.setTip((String)value);
		}
		else if(colNames[5].equals(property)){
			 prop.setTipI18nName((String)value);
		}else if(colNames[6].equals(property)){
			prop.setRefImg((String)value);
		}
		else if(colNames[7].equals(property)){
			 prop.setAlign((String)value);
		}
		else if(colNames[8].equals(property)){
			 prop.setType((String)value);
		}
		else if(colNames[9].equals(property)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			 prop.setWithSep(truevalue);
		}
//		else if(colNames[10].equals(property)){
//			 prop.setWidth((String)value);
//		}
		else if(colNames[10].equals(property)){
//			if(value != null)
//				prop.setModifiers(Integer.valueOf(hotKeyModifierMap.get((String)value)));
//			else
//				prop.setModifiers(2);
			if(value.equals("SHIFT")) //$NON-NLS-1$
				prop.setModifiers(1);
			else if(value.equals("CTRL")) //$NON-NLS-1$
				prop.setModifiers(2);
			else if(value.equals("ALT")) //$NON-NLS-1$
				prop.setModifiers(8);
			else if(value.equals("CTRL+SHIFT")) //$NON-NLS-1$
				prop.setModifiers(3);
			else if(value.equals("CTRL+ALT")) //$NON-NLS-1$
				prop.setModifiers(10);
			else if(value.equals("ALT+SHIFT")) //$NON-NLS-1$
				prop.setModifiers(9);
			else if(value.equals("CTRL+SHIFT+ALT")) //$NON-NLS-1$
				prop.setModifiers(11);
			//prop.setModifiers((String)value);
//			String value = "";
//			if(prop.getModifiers() == 1)
//				value = "SHIFT";
//			else if(prop.getModifiers() == 2)
//				value = "CTRL";
//			else if(prop.getModifiers() == 8)
//				value = "ALT";
//			else if(prop.getModifiers() == 3)
//				value = "CTRL+SHIFT";
//			else if(prop.getModifiers() == 10)
//				value = "CTRL+ALT";
//			else if(prop.getModifiers() == 9)
//				value = "ALT+SHIFT";
//			else if(prop.getModifiers() == 3)
//				value = "CTRL+SHIFT+ALT";
		}
		
		else if(colNames[11].equals(property)){
			 prop.setHotKey((String)value);
		}
		else if(colNames[12].equals(property)){
			 prop.setDisplayHotKey((String)value);
		}
		
		getTreeViewer().refresh(prop);
		view.getTv().update(prop, null);
		((ToolBarElementObj)getPropertiesView().getLfwElementPart().getModel()).updateToolBarItgem(prop);
	}
	
	public boolean canModify(Object element, String property) {
		ToolBarItem toolbarItem = (ToolBarItem) element;
		if(toolbarItem.getFrom() != null)
			return false;
		return true;
	}
	
	public Object getValue(Object element, String property) {
		if(element instanceof ToolBarItem){
			ToolBarItem prop = (ToolBarItem)element;
			if(colNames[0].equals(property)){
				return prop.getId()==null? "" : prop.getId(); //$NON-NLS-1$
			}else if(colNames[1].equals(property)){
				return prop.getText()==null?"":prop.getText(); //$NON-NLS-1$
			}
			else if(colNames[2].equals(property)){
				return  prop.getI18nName() == null?"":prop.getI18nName(); //$NON-NLS-1$
			}
			else if(colNames[3].equals(property)){
				return prop.getLangDir()== null?"":prop.getLangDir(); //$NON-NLS-1$
			}else if(colNames[4].equals(property)){
				return  prop.getTip()==null?"":prop.getTip(); //$NON-NLS-1$
			}
			else if(colNames[5].equals(property)){
				return prop.getTipI18nName()== null?"":prop.getTipI18nName(); //$NON-NLS-1$
			}else if(colNames[6].equals(property)){
				return  prop.getRefImg()==null?"":prop.getRefImg(); //$NON-NLS-1$
			}
			else if(colNames[7].equals(property)){
				return prop.getAlign()== null?"":prop.getAlign(); //$NON-NLS-1$
			}else if(colNames[8].equals(property)){
				return prop.getType() == null?"":prop.getType(); //$NON-NLS-1$
			}
			else if(colNames[9].equals(property)){
				return prop.isWithSep() == true? Integer.valueOf(0):Integer.valueOf(1);
			}
//			else if(colNames[10].equals(property)){
//				return prop.getWidth() == null?"":prop.getWidth();
//			}
			else if(colNames[10].equals(property)){
				//return String.valueOf(prop.getModifiers());
				String value = ""; //$NON-NLS-1$
				if(prop.getModifiers() == 1)
					value = "SHIFT"; //$NON-NLS-1$
				else if(prop.getModifiers() == 2)
					value = "CTRL"; //$NON-NLS-1$
				else if(prop.getModifiers() == 8)
					value = "ALT"; //$NON-NLS-1$
				else if(prop.getModifiers() == 3)
					value = "CTRL+SHIFT"; //$NON-NLS-1$
				else if(prop.getModifiers() == 10)
					value = "CTRL+ALT"; //$NON-NLS-1$
				else if(prop.getModifiers() == 9)
					value = "ALT+SHIFT"; //$NON-NLS-1$
				else if(prop.getModifiers() == 11)
					value = "CTRL+SHIFT+ALT"; //$NON-NLS-1$
				return value;
			}
			else if(colNames[11].equals(property)){
				return prop.getHotKey() == null?"":prop.getHotKey(); //$NON-NLS-1$
			}
			else if(colNames[12].equals(property)){
				return prop.getDisplayHotKey() == null?"":prop.getDisplayHotKey(); //$NON-NLS-1$
			}
		}
		return ""; //$NON-NLS-1$
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getAllFieldExceptModi(String id){
		Object object = view.getTv().getInput();
		List<String> list = new ArrayList<String>();
		if(object instanceof List){
			List<ToolBarItem> allElements = (List<ToolBarItem>)object;
			for (int i = 0; i < allElements.size(); i++) {
				ToolBarItem toolbarItem = allElements.get(i);
				if(toolbarItem.getId() != null && !(toolbarItem.getId().equals(id)))
					list.add(toolbarItem.getId());
				}
			}
		return list;
	}


	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem)element;
		Object o = item.getData();
		if(o instanceof ToolBarItem){
			ToolBarItem prop = (ToolBarItem)o;
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			if(property.endsWith(colNames[0])){
				List<String> list = getAllFieldExceptModi(old.toString());
				if(list.contains(value)){
					MessageDialog.openError(null, M_toolbar.ToolBarCellModifier_13, M_toolbar.ToolBarCellModifier_14 + value+ M_toolbar.ToolBarCellModifier_15);
					return;
				}
			}
			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
			ToolBarEditor editor = ToolBarEditor.getActiveEditor();
			if(editor != null)
				editor.executComand(cmd);
		}
	}
}


