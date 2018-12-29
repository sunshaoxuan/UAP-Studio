package nc.uap.portal.events;

import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.core.PortalBaseEditorInput;
import nc.uap.portal.lang.M_portal;

/**
 * EventsEditorInput
 * 
 * @author dingrf
 * 
 */
public class EventsEditorInput extends PortalBaseEditorInput {

	/**portlet-app∂‘œÛ */
	private PortletApplicationDefinition portletApp;
	
	public PortletApplicationDefinition getPortletApp() {
		return portletApp;
	}
	
	public void setPortletApp(PortletApplicationDefinition portletApp) {
		this.portletApp = portletApp;
	}
	
	public EventsEditorInput(PortletApplicationDefinition portletApp){
		this.portletApp = portletApp;
	}
	
	public String getName() {
		return M_portal.EventsEditorInput_0;
	}

	public String getToolTipText() {
		return M_portal.EventsEditorInput_0;
	}

}
