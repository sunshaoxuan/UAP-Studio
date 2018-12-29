package nc.uap.lfw.perspective.model;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

/**
 * 设置Ds FieldRelation时的单元格编辑
 * @author zhangxya
 *
 */
public class DsRelationCellModifier implements ICellModifier{

	private TableViewer tv;
	public DsRelationCellModifier(TableViewer tv){
		this.tv = tv;
	}
	public boolean canModify(Object element, String property) {
		if(element instanceof DSRelationField){
			if("ID".equals(property) || "Field".equals(property))
				return false;
			else
				return true;
		}
		return false;
	}

	public Object getValue(Object element, String property) {
		if(element instanceof DSRelationField){
			DSRelationField prop = (DSRelationField)element;
			if("ID".equals(property)){
				return prop.getId()==null? "" : prop.getId();
			}else if("Field".equals(property)){
				return prop.getField()==null?"":prop.getField();
			}else if("ismatch".equals(property)){
				if(prop.getIsmatch() == null)
					return 1;
				else if(prop.getIsmatch().equals("Y"))
					return 0;
					else
						return 1;
			}else if("isinput".equals(property)){
				if(prop.getIscontains() == null)
					return 1;
				else if (prop.getIscontains().equals("Y"))
					return 0;
				else
					return 1;
			}
			else if("matchField".equals(property))
				return prop.getMatchfield() == null?"":prop.getMatchfield();
		}
		return null;
	}

	public void modify(Object element, String property, Object value) {
		TableItem item = (TableItem)element;
		DSRelationField dsrf = (DSRelationField)item.getData();
		if(property.equals("ID")){
			dsrf.setId((String)value);
		}else if(property.equals("Field")){
			dsrf.setField((String)value);
		}else if(property.endsWith("ismatch")){
			Integer comboindex = (Integer)value;
			if(comboindex == 0)
				dsrf.setIsmatch("Y");
			else
				dsrf.setIsmatch("N");
		}
		else if(property.endsWith("isinput")){
			Integer comboindex = (Integer)value;
			if(comboindex == 0)
				dsrf.setIscontains("Y");
			else
				dsrf.setIscontains("N");
		}
		else if(property.endsWith("matchField"))
			dsrf.setMatchfield((String)value);
		tv.update(dsrf, null);
	}
	

}
