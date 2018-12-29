package nc.uap.lfw.perspective.editor;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.MDField;
import nc.uap.lfw.core.data.PubField;
import nc.uap.lfw.core.data.UnmodifiableMdField;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.views.CellPropertiesView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TreeItem;

/**
 * ds Field Modifier
 * @author zhangxya
 *
 */
public class CellModifier implements ICellModifier {
	
	private CellPropertiesView view = null;
	
	private class CellModifiCommand extends Command{
		private String property = ""; //$NON-NLS-1$
		private Object value = null;
		private Object oldValue = null;
		private Field attr = null;
		public CellModifiCommand(String property, Object value, Field attr) {
			super(M_perspective.CellModifier_0);
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
	
	public static final String[] colNames = {"ID",M_perspective.CellModifier_1,M_perspective.CellModifier_2,M_perspective.CellModifier_3,M_perspective.CellModifier_4, //$NON-NLS-1$
		M_perspective.CellModifier_5,M_perspective.CellModifier_6,M_perspective.CellModifier_7,M_perspective.CellModifier_8
		,M_perspective.CellModifier_9,M_perspective.CellModifier_10,M_perspective.CellModifier_11,M_perspective.CellModifier_12,M_perspective.CellModifier_13,M_perspective.CellModifier_14,M_perspective.CellModifier_15};
	
	
	public CellModifier(CellPropertiesView view) {
		super();
		this.view = view;
	}
	
	
	public boolean canModify(Object element, String property) {
		LfwView widget = LFWPersTool.getCurrentWidget();
		if(widget != null && widget.getFrom() != null)
			return false;
		if(element instanceof PubField){
			if(property.equals(colNames[0]) || property.equals(colNames[1]) || property.equals(colNames[4]))
			return false;
		}
		else if(element instanceof MDField){
			if(property.equals(colNames[0]) || property.equals(colNames[1]) || property.equals(colNames[4]))
				return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllFieldExceptModi(String id){
		Object object = view.getTv().getInput();
		List<String> list = new ArrayList<String>();
		if(object instanceof List){
			List<Field> allFields = (List)object;
			for (int i = 0; i < allFields.size(); i++) {
				Field field = (Field) allFields.get(i);
				if(field.getId() != null && !(field.getId().equals(id)))
					list.add(field.getId());
				}
			}
		return list;
	}

	public Object getValue(Object element, String property) {
		if(element instanceof Field){
			Field prop = (Field)element;
			if(colNames[0].equals(property)){
				return prop.getId()==null? "" : prop.getId(); //$NON-NLS-1$
			}else if(colNames[1].equals(property)){
				return prop.getField()==null?"":prop.getField(); //$NON-NLS-1$
			}
			else if(colNames[2].equals(property)){
				String label = prop.getText() == null?"":prop.getText(); //$NON-NLS-1$
				return label;
			}else if(colNames[3].equals(property)){
				String i18nname = prop.getI18nName() == null?"":prop.getI18nName(); //$NON-NLS-1$
				return i18nname;
			}
			else if(colNames[4].equals(property)){
				String datatype = prop.getDataType() == null?"":prop.getDataType(); //$NON-NLS-1$
				return datatype;
			}else if(colNames[5].equals(property)){
				return prop.isNullAble() == true? Integer.valueOf(0):Integer.valueOf(1);
			}else if(colNames[6].equals(property)){
				String defaultvalue = prop.getDefaultValue() == null?"":prop.getDefaultValue().toString(); //$NON-NLS-1$
				return defaultvalue;
			}else if(colNames[7].equals(property)){
				return prop.isPrimaryKey() == true? Integer.valueOf(0):Integer.valueOf(1);
			}else if(colNames[8].equals(property)){
				String editfamular = prop.getEditFormular() == null?"":prop.getEditFormular(); //$NON-NLS-1$
				return editfamular;
			}else if(colNames[9].equals(property)){
				String validatefamular = prop.getValidateFormula() == null?"":prop.getValidateFormula(); //$NON-NLS-1$
				return validatefamular;
			}else if(colNames[10].equals(property)){
				return prop.isLock() == true? Integer.valueOf(0):Integer.valueOf(1);
			}else if(colNames[11].equals(property)){
				String validatetype = prop.getFormater() == null?"":prop.getFormater(); //$NON-NLS-1$
				return validatetype;
			}else if(colNames[12].equals(property)){
				String langdir = prop.getLangDir() == null?"":prop.getLangDir(); //$NON-NLS-1$
				return langdir;
			}
			else if(colNames[13].equals(property)){
				return prop.getMaxValue() == null?"":prop.getMaxValue(); //$NON-NLS-1$
			}
			else if(colNames[14].equals(property)){
				return prop.getMinValue() == null?"":prop.getMinValue(); //$NON-NLS-1$
			}
			else if(colNames[15].equals(property)){
				return prop.getPrecision() == null?"":prop.getPrecision(); //$NON-NLS-1$
			}
		}
		return ""; //$NON-NLS-1$
	}

	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem)element;
		Object object = item.getData();
		if(object instanceof Field){
			Field prop = null;
			if(object instanceof UnmodifiableMdField){
				prop = ((UnmodifiableMdField)object).getMDField();
			}
			else prop = (Field)object;
//			prop = (Field)object;
//			prop.getField()
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			if(property.endsWith(colNames[0])){
				List<String> list = getAllFieldExceptModi(old.toString());
				if(list.contains(value)){
					MessageDialog.openError(null, M_perspective.CellModifier_16, M_perspective.CellModifier_18 + value+ M_perspective.CellModifier_17);
					return;
				}
			}
			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
			DataSetEditor editor = DataSetEditor.getActiveEditor();
			if(editor != null){
				editor.executComand(cmd);
			}
		}
	}

	
	private void modifyAttr(Field prop, String property,Object value){
		 if(colNames[0].equals(property)){
			 prop.setId((String)value);
			 prop.setText((String)value);
		 }
		else if(colNames[1].equals(property)){
			 prop.setField((String)value);
		}else if(colNames[2].equals(property)){
			prop.setText((String)value);
		}
		else if(colNames[3].equals(property)){
			prop.setI18nName((String)value);
		}else if(colNames[4].equals(property)){
			prop.setDataType((String)value);
			prop.setDefaultValue("");
		}else if(colNames[5].equals(property)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			prop.setNullAble(truevalue);	
		}else if(colNames[6].equals(property)){
			prop.setDefaultValue((String)value);
		}else if(colNames[7].equals(property)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			prop.setPrimaryKey(truevalue);	
		}else if(colNames[8].equals(property)){
			prop.setEditFormular((String)value);
		}else if(colNames[9].equals(property)){
			prop.setValidateFormula((String)value);
		}else if(colNames[10].equals(property)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			prop.setLock(truevalue);	
		}else if(colNames[11].equals(property)){
			prop.setFormater((String)value);
		}else if(colNames[12].equals(property)){
			prop.setLangDir((String)value);
		}
		else if(colNames[13].equals(property)){
			prop.setMaxValue((String)value);
		}
		else if(colNames[14].equals(property)){
			prop.setMinValue((String)value);
		}
		else if(colNames[15].equals(property)){
			if(prop.getDataType().equals(StringDataTypeConst.Decimal) || prop.getDataType().equals(StringDataTypeConst.UFDOUBLE)
					|| prop.getDataType().equals(StringDataTypeConst.FLOATE) || 
					
					prop.getDataType().equals(StringDataTypeConst.INTEGER) || prop.getDataType().equals(StringDataTypeConst.INT)
					|| prop.getDataType().equals(StringDataTypeConst.DOUBLE) || prop.getDataType().equals(StringDataTypeConst.dOUBLE)
					||  prop.getDataType().equals(StringDataTypeConst.fLOATE) ||   prop.getDataType().equals(StringDataTypeConst.BIGDECIMAL)
					|| prop.getDataType().equals(StringDataTypeConst.LONG) ||  prop.getDataType().equals(StringDataTypeConst.lONG) 
					|| StringDataTypeConst.CUSTOM.equals(prop.getDataType()))
				prop.setPrecision((String)value);
		}
		DataSetEditor editor = DataSetEditor.getActiveEditor();
		if(editor != null&&prop.getId()!=null){
			editor.getData().getFieldSet().updateField(prop.getId(), prop);
		}
		else{
			MessageDialog.openError(null, M_perspective.CellModifier_19, M_perspective.CellModifier_20);
		}
		view.getTv().update(prop, null);
		view.getTv().refresh();
	}
}

