package nc.uap.lfw.template;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.graphics.Image;

public interface ITemplatePageFactory {
	
	public String getTemplateTitle();

	public int getPageCount();
	
	public WizardPage initPage(int index);
	
	public Image getPreviewImage();
	
	public boolean finish(NewTempleteWindowWizard wizard);
	
	public void publish(String funcId,String appId);

	public void setAppId(String appid);
}
