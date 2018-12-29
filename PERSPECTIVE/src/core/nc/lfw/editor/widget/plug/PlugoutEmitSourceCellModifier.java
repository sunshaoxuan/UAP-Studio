package nc.lfw.editor.widget.plug;


import nc.lfw.editor.widget.plug.PlugoutEmitSourceDialog.PlugoutEmitSource;

import org.eclipse.jface.viewers.ICellModifier;

/**
 * PlugoutEmitCellModifier
 * 
 * @author dingrf
 *
 */
public class PlugoutEmitSourceCellModifier implements ICellModifier {
	
	/**列名称*/
	public static final String[] colNames = {"触发器类型"};
	
	
	public PlugoutEmitSourceCellModifier() {
		super();
	}
	
	/**
	 * 是否可修改
	 */
	public boolean canModify(Object element, String property) {
		return false;
	}

	/**
	 * 取列值
	 */
	public Object getValue(Object element, String property) {
		if(element instanceof PlugoutEmitSource){
			PlugoutEmitSource prop = (PlugoutEmitSource)element;
			if(colNames[0].equals(property)){
				return prop.getSourceType() == null ? "" : prop.getSourceType();
			}
//			else if(colNames[1].equals(property)){
//				return prop.getSourceId() == null ? "" : prop.getSourceId();
//			}
		}
		return "";
	}

	@Override
	public void modify(Object element, String property, Object value) {
	}
}

