package nc.uap.lfw.tree.core;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.core.comp.TreeLevel;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * tree element
 * @author zhangxya
 *
 */
public class TreeLevelElementObj  extends LfwElementObjWithGraph{
	private static final long serialVersionUID = 1L;
	private TreeLevel treelevel = null;
	public TreeLevel getTreelevel() {
		return treelevel;
	}
	private LFWBasicElementObj parentTreeLevel = null;
	
	public LFWBasicElementObj getParentTreeLevel() {
		return parentTreeLevel;
	}
	public void setParentTreeLevel(LFWBasicElementObj parentTreeLevel) {
		this.parentTreeLevel = parentTreeLevel;
	}
	public void setTreelevel(TreeLevel treelevel) {
		this.treelevel = treelevel;
	}

	private Dataset ds = null;

	public Dataset getDs() {
		return ds;
	}
	public void setDs(Dataset ds) {
		this.ds = ds;
	}

	public static final String PROP_ISLAZY = "element_ISLAZY";
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return super.getPropertyDescriptors();
	}
	
	public void setPropertyValue(Object id, Object value) {
	}
	public Object getPropertyValue(Object id) {
		return null;
	}
	
	
	public WebElement getWebElement() {
		return treelevel;
	}
	
}
