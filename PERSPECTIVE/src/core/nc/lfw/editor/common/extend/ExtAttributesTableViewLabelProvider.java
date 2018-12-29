package nc.lfw.editor.common.extend;

import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.perspective.editor.TableViewLabelProvider;

/**
 * 扩展属性页表格内容加载器
 * 
 * @author guoweic
 *
 */
public class ExtAttributesTableViewLabelProvider extends TableViewLabelProvider {
	public String getColumnText(Object element, int columnIndex) {
		ExtAttribute attribute = (ExtAttribute)element;
		if(columnIndex == 0)
			return attribute.getKey();
		if(columnIndex == 1)
			return attribute.getValue() == null ? "" : attribute.getValue().toString();
		if(columnIndex == 2)
			return attribute.getDesc();
		return null;
	}

}
