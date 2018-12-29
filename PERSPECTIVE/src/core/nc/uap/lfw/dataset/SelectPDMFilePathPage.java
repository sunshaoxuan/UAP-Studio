package nc.uap.lfw.dataset;

import java.io.File;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

public class SelectPDMFilePathPage extends WizardPage implements IImportTablesService{
	private Text fileNameText = null;
	private Button openBtn = null;
	public SelectPDMFilePathPage(String pageName) {
		super(pageName);
	}

	public SelectPDMFilePathPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent,SWT.NONE);
		container.setLayout(new GridLayout(2,false));
		fileNameText = new Text(container, SWT.BORDER);
		fileNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		openBtn = new Button(container, SWT.BORDER);
		openBtn.setText("...");
		openBtn.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleBtnClicked(e);
			}
			
		});
		setControl(container);

	}

	protected void handleBtnClicked(SelectionEvent e) {
		FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
		fd.setFilterNames(new String[]{"PDM(XML格式)文件(.pdm)"});
		fd.setFilterExtensions(new String[]{"*.pdm"});
		String text = fd.open();
		if(text != null){
			fileNameText.setText(text);
		}
	}
	


	@Override
	public IWizardPage getNextPage() {
		return getWizard().getPage(SelectTableColumnPage.class.getCanonicalName());
	}
	private String getPDMFilePath(){
		return fileNameText.getText();
	}

	public DBTable[] getAllTables() {
		DBTable[] tables = null;
		String pdmFilePath = getPDMFilePath();
		if (pdmFilePath != null && pdmFilePath.trim().length() > 0) {
			PDMFileParser parser = new PDMFileParser(new File(pdmFilePath));
			tables = parser.parse();
		}
		return tables;
	}
}
