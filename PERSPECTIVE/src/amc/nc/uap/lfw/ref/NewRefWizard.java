package nc.uap.lfw.ref;

import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.ref.LfwRefInfoVO;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.ref.model.IConst;
import nc.uap.lfw.ref.page.INewRefWizardPage;
import nc.uap.lfw.ref.page.RefBasicInfoWizardPage;
import nc.uap.lfw.ref.page.RefSQLInfoWizardPage;
import nc.uap.lfw.ref.page.RefTableInfoWizardPage;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewRefWizard extends Wizard implements INewWizard{

	private Map<String, Object> context = new HashMap<String, Object>();
	private LFWDirtoryTreeItem parentItem = null;
	
	public NewRefWizard(){};
	public NewRefWizard(LFWDirtoryTreeItem treeItem){
		this.parentItem = treeItem;
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		
		
	}

	
	@Override
	public void addPages() {
		this.addPage(new RefBasicInfoWizardPage(context, "1")); //$NON-NLS-1$
		this.addPage(new RefTableInfoWizardPage(context, "2")); //$NON-NLS-1$
		this.addPage(new RefSQLInfoWizardPage(context, "3")); //$NON-NLS-1$
	}


	@Override
	public boolean performFinish() {
		for (IWizardPage page : this.getPages()) {
			if (page instanceof INewRefWizardPage) {
				INewRefWizardPage p = (INewRefWizardPage) page;
				p.updateModel();
			}
		}

		if (context != null && context.containsKey(IConst.REF_INFO)
				&& context.get(IConst.REF_INFO) instanceof LfwRefInfoVO) {
			RefClassGenerator generator = new RefClassGenerator(context, parentItem);
			if(generator.generate()){
				MessageDialog.openInformation(null, M_editor.NewRefWizard_0, M_editor.NewRefWizard_1);
			}else{
				return false;
			}
		}
		return true;
		
	}

}
