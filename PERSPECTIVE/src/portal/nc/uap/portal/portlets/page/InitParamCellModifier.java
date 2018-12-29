package nc.uap.portal.portlets.page;


import nc.uap.portal.container.om.InitParam;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.portlets.PortletEditor;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

/**
 * InitParamCellModifier
 * 
 * @author dingrf
 *
 */
public class InitParamCellModifier implements ICellModifier {
	
	private InitParamPropertiesView view = null;
	
	private class CellModifiCommand extends Command{
		private String property = ""; //$NON-NLS-1$
		private Object value = null;
		private Object oldValue = null;
		private InitParam attr = null;
		public CellModifiCommand(String property, Object value, InitParam attr) {
			super(M_portal.InitParamCellModifier_0);
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
	public static final String[] colNames = {M_portal.InitParamCellModifier_1,M_portal.InitParamCellModifier_2,M_portal.InitParamCellModifier_3};
	
	
	public InitParamCellModifier(InitParamPropertiesView view) {
		super();
		this.view = view;
	}
	
	/**
	 * 是否可修改
	 */
	public boolean canModify(Object element, String property) {
		return true;
	}

	/**
	 * 取列值
	 */
	public Object getValue(Object element, String property) {
		if(element instanceof InitParam){
			InitParam prop = (InitParam)element;
			if(colNames[0].equals(property)){
				return prop.getParamName()==null? "" : prop.getParamName(); //$NON-NLS-1$
			}else if(colNames[1].equals(property)){
				return prop.getParamValue()==null?"":prop.getParamValue(); //$NON-NLS-1$
			}else if(colNames[2].equals(property)){
				return prop.getDescriptions().size()<=0?"": //$NON-NLS-1$
					(prop.getDescriptions().get(0).getDescription()==null?"":prop.getDescriptions().get(0).getDescription()); //$NON-NLS-1$
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
		if(object instanceof InitParam){
			InitParam prop = (InitParam)object;
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
			PortletEditor editor = PortletEditor.getActiveEditor();
			if(editor != null)
				editor.executComand(cmd);
		}
	}

	
	private void modifyAttr(InitParam prop, String property,Object value){
		 if(colNames[0].equals(property)){
			 prop.setParamName((String)value);
		 }
		else if(colNames[1].equals(property)){
			 prop.setParamValue((String)value);
		}
		else if(colNames[2].equals(property)){
			if (prop.getDescriptions().size()<=0){
				prop.addDescription((String)value);
			}else{
				prop.getDescriptions().get(0).setDescription((String)value);
			}
		}
		view.getTv().update(prop, null);
	}
}

