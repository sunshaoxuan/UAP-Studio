package nc.uap.portal.page;

import nc.uap.portal.core.PortalBaseEditorInput;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.om.Page;

/**
 * PortalPageEditorInput
 * 
 * @author dingrf
 *
 */
public class PortalPageEditorInput extends PortalBaseEditorInput {
	
	/**pageÊ÷½Úµã*/
	private PortalPageTreeItem pageTreeItem;

	/**pageÒ³Ãæ*/
	private Page page;
	
	public PortalPageEditorInput(PortalPageTreeItem item){
		this.pageTreeItem = item;
		this.page = (Page)item.getData();
	}
	
	public Page getPage() {
		return page;
	}

	public PortalPageTreeItem getpageTreeItem() {
		return pageTreeItem;
	}

	public void setpageTreeItem(PortalPageTreeItem pageTreeItem) {
		this.pageTreeItem = pageTreeItem;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getName() {
		return M_portal.PortalPageEditorInput_0;
	}

	public String getToolTipText() {
		return M_portal.PortalPageEditorInput_0;
	}

}
