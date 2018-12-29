package nc.uap.portal.wizards;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import nc.uap.lfw.wizards.NewModuleWebContextPage;

public class PortalModuleWebContextPage extends NewModuleWebContextPage {

	protected PortalModuleWebContextPage(String pageName) {
		super(pageName);
	}
	public Group PartGroup(Composite composite) {
		return PartGroup(composite, false, "portal");
	}
}
