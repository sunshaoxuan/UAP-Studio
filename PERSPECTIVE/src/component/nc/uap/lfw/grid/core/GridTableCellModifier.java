package nc.uap.lfw.grid.core;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.ObjectComboCellEditor;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridColumnGroup;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.lang.M_grid;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;

/**
 *grid GridTableCellModifier 
 * @author zhangxya
 *
 */
public class GridTableCellModifier implements ICellModifier{
	
	/**
	 * {"id","field","text","i18nName","langdir","width","visible","editable","columBgColor"
		,"textAlign","textColor","fixedHeader","editorType","renderType","refNode","imageOnly",
		"sumCol","autoExpand","sortable"};
	 */
	public static final String[] colNames ={M_grid.GridTableCellModifier_0,M_grid.GridTableCellModifier_1,M_grid.GridTableCellModifier_2,M_grid.GridTableCellModifier_3,M_grid.GridTableCellModifier_4,M_grid.GridTableCellModifier_5,M_grid.GridTableCellModifier_6,M_grid.GridTableCellModifier_7,
		M_grid.GridTableCellModifier_8,M_grid.GridTableCellModifier_9,M_grid.GridTableCellModifier_10,M_grid.GridTableCellModifier_11,M_grid.GridTableCellModifier_12,M_grid.GridTableCellModifier_13,M_grid.GridTableCellModifier_14,M_grid.GridTableCellModifier_15,M_grid.GridTableCellModifier_16,M_grid.GridTableCellModifier_17,M_grid.GridTableCellModifier_18,
		M_grid.GridTableCellModifier_19,M_grid.GridTableCellModifier_20,M_grid.GridTableCellModifier_21,M_grid.GridTableCellModifier_22,M_grid.GridTableCellModifier_23,M_grid.GridTableCellModifier_24};

	private GridPropertisView view = null;
	
	private class GridModifiCommand extends Command{
		
		private String property = ""; //$NON-NLS-1$
		private Object value = null;
		private Object oldValue = null;
		private IGridColumn attr = null;
		
		public GridModifiCommand(String property, Object value, IGridColumn attr) {
			super(M_grid.GridTableCellModifier_25);
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
	
	
	public GridTableCellModifier(GridPropertisView view) {
		super();
		this.view = view;
	}
	
	private  TreeViewer getTreeViewer(){
		return getPropertiesView().getTv();
	}
	
	private GridPropertisView getPropertiesView(){
		return view;
	}
	

	private void modifyAttr(IGridColumn prop, String property,Object value){
		if(prop instanceof GridColumn){
			GridColumn propCol = (GridColumn) prop;
			if(colNames[0].equals(property)){
				propCol.setId((String)value);
			}else if(colNames[1].equals(property)){
				propCol.setField((String)value);
			}else if(colNames[2].equals(property)){
				propCol.setText((String)value);
			}else if(colNames[3].equals(property)){
				propCol.setI18nName((String)value);
			}
			else if(colNames[4].equals(property)){
				propCol.setLangDir((String)value);
			}
			else if(colNames[5].equals(property)){
				if(value == null || value.equals("")) //$NON-NLS-1$
					propCol.setWidth(GridColumn.DEFAULT_WIDTH);
				else
					propCol.setWidth((Integer.valueOf((String)value)));
			}
			else if(colNames[6].equals(property)){
				boolean truevalue = false;
				if((Integer)value == 0)
					truevalue = true;
				propCol.setVisible(truevalue);
			}else if(colNames[7].equals(property)){
				boolean truevalue = false;
				if((Integer)value == 0)
					truevalue = true;
				propCol.setEditable(truevalue);
			}else if(colNames[8].equals(property)){
				propCol.setColumBgColor((String)value);
			}else if(colNames[9].equals(property)){
				propCol.setTextAlign((String)value);
			}
			else if(colNames[10].equals(property)){
				propCol.setTextColor((String)value);
			}else if(colNames[11].equals(property)){
				boolean truevalue = false;
				if((Integer)value == 0)
					truevalue = true;
				propCol.setFixedHeader(truevalue);
			}else if(colNames[12].equals(property)){
				propCol.setEditorType((String)value);
				if(value != null && !(value.toString().equals(EditorTypeConst.REFERENCE))){
					propCol.setRefNode(null);
				}
				if(value != null && !(value.toString().equals(EditorTypeConst.COMBODATA))){
					propCol.setRefComboData(null);
				}
			}
			else if(colNames[13].equals(property)){
				propCol.setRenderType((String)value);
			}else if(colNames[14].equals(property)){
				propCol.setRefNode((String)value);
			}else if(colNames[15].equals(property)){
				propCol.setRefComboData((String)value);
			}
			else if(colNames[16].equals(property)){
				boolean truevalue = false;
				if((Integer)value == 0)
					truevalue = true;
				propCol.setImageOnly(truevalue);
			}
			else if(colNames[17].equals(property)){
				boolean truevalue = false;
				if((Integer)value == 0)
					truevalue = true;
				propCol.setSumCol(truevalue);
			}else if(colNames[18].equals(property)){
				boolean truevalue = false;
				if((Integer)value == 0)
					truevalue = true;
				propCol.setAutoExpand(truevalue);
			}
			else if(colNames[19].equals(property)){
				boolean truevalue = false;
				if((Integer)value == 0)
					truevalue = true;
				propCol.setSortable(truevalue);
			}
			else if(colNames[20].equals(property)){
				if(value != null && !value.equals("")){ //$NON-NLS-1$
					if(view.getLfwElementPart() != null && view.getLfwElementPart().getModel() instanceof GridElementObj){
						GridElementObj vo = (GridElementObj)view.getLfwElementPart().getModel();
						GridComp grid = vo.getGridComp();
						IGridColumn column = grid.getColumnById((String)value);
						if(column != null && column instanceof GridColumnGroup){
							GridColumnGroup columngroup = (GridColumnGroup) column;
							if(columngroup.getChildColumnList() == null || (columngroup.getChildColumnList() != null && !columngroup.getChildColumnList().contains(propCol)))
							// if(!columngroup.getChildColumnList().contains(propCol))
								 columngroup.addColumn(propCol);
							 grid.removeColumnByField(propCol.getId());
						}
						if(propCol != null && propCol.getColmngroup() != null){ //$NON-NLS-1$
							IGridColumn columnold = grid.getColumnById(propCol.getColmngroup());
							if(columnold != null && columnold instanceof GridColumnGroup){
								GridColumnGroup columgroupold = (GridColumnGroup) columnold;
								columgroupold.removeColumn(propCol.getId());
							}
						}
						
					}
				}
				else{
					GridElementObj vo = (GridElementObj)view.getLfwElementPart().getModel();
					GridComp grid = vo.getGridComp();
					if(propCol != null && propCol.getColmngroup() != null){ //$NON-NLS-1$
						IGridColumn columnold = grid.getColumnById(propCol.getColmngroup());
						if(columnold != null && columnold instanceof GridColumnGroup){
							GridColumnGroup columgroupold = (GridColumnGroup) columnold;
							columgroupold.removeColumn(propCol.getId());
							grid.addColumn(propCol);
						}
					}
				}
				propCol.setColmngroup((String)value);
			}
			else if(colNames[21].equals(property)){
				boolean truevalue = false;
				if((Integer)value == 0)
					truevalue = true;
				propCol.setNullAble(truevalue);
			}
			else if (colNames[22].equals(property)){
				boolean showCheckBox = true;
				if ((Integer)value == 1)
					showCheckBox = false;
				propCol.setShowCheckBox(showCheckBox);
			}
			else if (colNames[23].equals(property)){
				propCol.setMaxLength((String)value);
			}
			else if (colNames[24].equals(property)){
				propCol.setSumColRenderFunc((String)value);
			}
		}
		else if(prop instanceof GridColumnGroup){
			GridColumnGroup propGroup = (GridColumnGroup)prop;
			if(colNames[0].equals(property)){
				propGroup.setId((String)value);
			}else if(colNames[2].equals(property)){
				propGroup.setText((String)value);
			}
			else if(colNames[3].equals(property)){
				propGroup.setI18nName((String)value);
			}
			else if(colNames[6].equals(property)){
				boolean truevalue = false;
				if((Integer)value == 0)
					truevalue = true;
				propGroup.setVisible(truevalue);
			}
			
			CellEditor editor = view.getCellEditorByColName(M_grid.GridTableCellModifier_26);
			String[] valuegroup = view.getColumnGroup();
			if(editor instanceof ObjectComboCellEditor){
				ObjectComboCellEditor objEditor = (ObjectComboCellEditor) editor;
				objEditor.setObjectItems(valuegroup);
			}
		}
		
		getTreeViewer().refresh(prop);
		view.getTv().update(prop, null);
	}
	
	public boolean canModify(Object element, String property) {
		if(element instanceof GridColumn)
			return true;
		else if(element instanceof GridColumnGroup){
			if(colNames[0].equals(property) || colNames[2].equals(property)
					|| colNames[3].equals(property) || colNames[6].equals(property)){
				return true;
			}
			else
				return false;
		}
		return false;
	}

	public Object getValue(Object element, String property) {
		if(element instanceof GridColumn){
			GridColumn prop = (GridColumn)element;
			if(colNames[0].equals(property)){
				return prop.getId()==null? "" : prop.getId(); //$NON-NLS-1$
			}else if(colNames[1].equals(property)){
				return prop.getField()==null?"":prop.getField(); //$NON-NLS-1$
			}else if(colNames[2].equals(property)){
				return prop.getText() == null?"":prop.getText(); //$NON-NLS-1$
			}else if(colNames[3].equals(property)){
				return prop.getI18nName()== null?"":prop.getI18nName(); //$NON-NLS-1$
			}else if(colNames[4].equals(property)){
				return  prop.getLangDir()==null?"":prop.getLangDir(); //$NON-NLS-1$
			}else if(colNames[5].equals(property)){
				 Integer.valueOf(prop.getWidth()).toString();
			}
			
			else if(colNames[6].equals(property)){
				return prop.isVisible() == true? Integer.valueOf(0):Integer.valueOf(1);
			}
			else if(colNames[7].equals(property)){
				return prop.isEditable() == true? Integer.valueOf(0):Integer.valueOf(1);
			}
			else if(colNames[8].equals(property)){
				return  prop.getColumBgColor() == null?"":prop.getColumBgColor(); //$NON-NLS-1$
			}
			else if(colNames[9].equals(property)){
				return  prop.getTextAlign() == null?"":prop.getTextAlign(); //$NON-NLS-1$
			}
			else if(colNames[10].equals(property)){
				return  prop.getTextColor() == null?"":prop.getTextColor(); //$NON-NLS-1$
			}
			else if(colNames[11].equals(property)){
				return prop.isFixedHeader() == true? Integer.valueOf(0):Integer.valueOf(1);
			}
			
			else if(colNames[12].equals(property)){
				return  prop.getEditorType() == null?"":prop.getEditorType(); //$NON-NLS-1$
			}
			else if(colNames[13].equals(property)){
				return prop.getRenderType() == null?"":prop.getRenderType(); //$NON-NLS-1$
			}
			else if(colNames[14].equals(property)){
				return  prop.getRefNode() == null?"":prop.getRefNode(); //$NON-NLS-1$
			}
			else if(colNames[15].equals(property)){
				return  prop.getRefComboData() == null?"":prop.getRefComboData(); //$NON-NLS-1$
			}
			else if(colNames[16].equals(property)){
				return prop.isImageOnly() == true? Integer.valueOf(0):Integer.valueOf(1);
			}
			
			else if(colNames[17].equals(property)){
				 return prop.isSumCol() == true? Integer.valueOf(0):Integer.valueOf(1);
			}else if(colNames[18].equals(property)){
				return prop.isAutoExpand() == true? Integer.valueOf(0):Integer.valueOf(1);
			}
			else if(colNames[19].equals(property)){
				return prop.isSortable() == true? Integer.valueOf(0):Integer.valueOf(1);
			}
			else if(colNames[20].equals(property)){
				return prop.getColmngroup() == null? "":prop.getColmngroup(); //$NON-NLS-1$
			}
			else if(colNames[21].equals(property)){
				return prop.isNullAble() == true? Integer.valueOf(0):Integer.valueOf(1);
			}
			else if(colNames[22].equals(property)){
				return prop.isShowCheckBox() == true? Integer.valueOf(0):Integer.valueOf(1);
			}
			else if(colNames[23].equals(property)){
				return prop.getMaxLength() ==  null? "":prop.getMaxLength(); //$NON-NLS-1$
			}
			else if(colNames[24].equals(property)){
				return prop.getSumColRenderFunc() ==  null? "":prop.getSumColRenderFunc(); //$NON-NLS-1$
			}
		}
		else if(element instanceof GridColumnGroup){
			GridColumnGroup prop = (GridColumnGroup)element;
			if(colNames[0].equals(property)){
				return prop.getId()==null? "" : prop.getId(); //$NON-NLS-1$
			}
			else if(colNames[2].equals(property)){
				return prop.getText() == null?"":prop.getText(); //$NON-NLS-1$
			}
			else if(colNames[3].equals(property)){
				return prop.getI18nName()== null?"":prop.getI18nName(); //$NON-NLS-1$
			}
			else if(colNames[6].equals(property)){
				return prop.isVisible() == true? Integer.valueOf(0):Integer.valueOf(1);
			}
		}
			
		return ""; //$NON-NLS-1$
	}
	
	/**
	 * 查询除了要修改的字段的其余字段的id
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllFieldExceptModi(String id){
		Object object = view.getTv().getInput();
		List<String> list = new ArrayList<String>();
		if(object instanceof List){
			List<IGridColumn> allColumns = (List<IGridColumn>)object;
			for (int i = 0; i < allColumns.size(); i++) {
				IGridColumn col = allColumns.get(i);
				if(col instanceof GridColumn){
					GridColumn column = (GridColumn) allColumns.get(i);
					if(column.getId() != null && !(column.getId().equals(id)))
						list.add(column.getId());
				}
				else if(col instanceof GridColumnGroup){
					GridColumnGroup column = (GridColumnGroup) allColumns.get(i);
					if(column.getId() != null && !(column.getId().equals(id)))
						list.add(column.getId());
					}
				}
			}
		return list;
	}

	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem)element;
		Object o = item.getData();
		if(o instanceof IGridColumn){
			IGridColumn prop = (IGridColumn)o;
			Object old = getValue(prop, property);
			if(old != null && old.equals(value))
				return;
			if(property.endsWith(colNames[0])){
				List<String> list = getAllFieldExceptModi(old.toString());
				if(list.contains(value)){
					MessageDialog.openError(null, "错误提示", "此表格已经存在ID为" + value+ "的列!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					return;
				}
			}
			GridModifiCommand cmd = new GridModifiCommand(property, value, prop );
			GridEditor editor = GridEditor.getActiveEditor();
			if(editor != null)
				editor.executComand(cmd);
		}
	
	}
}


