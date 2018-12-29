package nc.uap.portal.portlets;

import nc.lfw.editor.common.LfwBaseEditorInput;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.core.PortalBaseEditorInput;
import nc.uap.portal.lang.M_portal;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IPersistableElement;

public class PortletEditorInput extends PortalBaseEditorInput {

	private PortletDefinition portlet;
	
	/**portlet∑÷¿‡id*/
	private String categoryId;
	
	
	public PortletDefinition getPortlet() {
		return portlet;
	}
	
	public void setPortletApp(PortletDefinition portlet) {
		this.portlet = portlet;
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
 
	public PortletEditorInput(PortletDefinition portlet,String categoryId){
		this.portlet = portlet;
		this.categoryId = categoryId; 
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_portal.PortletEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return M_portal.PortletEditorInput_0;
	}

	


}
