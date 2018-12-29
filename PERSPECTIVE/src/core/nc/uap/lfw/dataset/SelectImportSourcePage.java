package nc.uap.lfw.dataset;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
public class SelectImportSourcePage extends WizardPage {
	private Button fromPDMXML = null;
	private Button fromDB = null;
	public SelectImportSourcePage(String pageName) {
		super(pageName);
	}

	public SelectImportSourcePage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent,SWT.NONE);
		container.setLayout(new FillLayout());
		Group group = new Group(container, SWT.NONE);
		group.setText("导入源");
		group.setLayout(new FillLayout(SWT.VERTICAL));
		fromPDMXML = new Button(group, SWT.RADIO);
		fromPDMXML.setText("从pdm（.xml）文件导入");
		fromDB = new Button(group,SWT.RADIO);
		fromDB.setText("从数据库表导入");
		setControl(container);
		
	}

	@Override
	public IWizardPage getNextPage() {
		SelectTableColumnPage selColPage = (SelectTableColumnPage) getWizard().getPage(SelectTableColumnPage.class.getCanonicalName());
		if(fromPDMXML.getSelection()){
			SelectPDMFilePathPage page = (SelectPDMFilePathPage)getWizard().getPage(SelectPDMFilePathPage.class.getCanonicalName());
			selColPage.setImportTables(page);
			return page;
		}else if(fromDB.getSelection()){
			SelectDBTablePage page = (SelectDBTablePage)getWizard().getPage(SelectDBTablePage.class.getCanonicalName());
			selColPage.setImportTables(page);
			return page;
		}
		else
			return super.getNextPage();
	}
	

}
