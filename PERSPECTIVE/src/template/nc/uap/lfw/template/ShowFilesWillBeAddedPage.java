/**
 * 
 */
package nc.uap.lfw.template;

import java.awt.TextArea;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextPane;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.lang.M_template;
import nc.uap.lfw.template.mastersecondly.TempGeneResultPage;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 显示依模板创建window时将要添加的文件的信息
 * @author guomq1
 * 2012-8-9
 */
public class ShowFilesWillBeAddedPage extends TempNextUsedWizardPage {

    private JTextPane filesMessagePanel = null;
    private String filesmessage = null;
	private Application app = LFWAMCPersTool.getCurrentApplication();
	private String appid = app.getId();
	private Text text = null;
	protected Composite showfilecomposite;


	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		showfilecomposite = new Composite(parent, SWT.NULL);
		showfilecomposite.setFont(parent.getFont());
//		showfilecomposite.setLayout(new GridLayout(3, true));
		showfilecomposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		showfilecomposite.setLayout(layout);
		
		Label label = new Label(showfilecomposite,SWT.NONE);
		label.setText(M_template.ShowFilesWillBeAddedPage_0);
   	 	text = new Text(showfilecomposite, SWT.BORDER | SWT.WRAP | SWT.MULTI);
   	 	text.setEditable(false);
//   	 	text.setBounds(10, 10, 600, 100);
   	 	text.setLayoutData(new GridData(GridData.FILL_BOTH));
//   	 	text.setText("asdfasdffffff  \n\r fffffff");
		
//		filemessagelabelstest = new Label[10];
//	     for(Label label: filemessagelabels){
//	    	 label = new Label(showfilecomposite,SWT.NONE);
//	    	 label.setText("abc");
//	     } 
//		new Label(showfilecomposite, SWT.NONE).setText("PROJECT");
//		new Label(showfilecomposite, SWT.NONE).setText("PROJECT");
//		new Label(showfilecomposite, SWT.NONE).setText("PROJECT");
//		new Label(showfilecomposite, SWT.NONE).setText("PROJECT");
//		new Label(showfilecomposite, SWT.NONE).setText("PROJECT");
/*		filesmessage = "SIHEITHI"+appid+"wiehithie";
		filesMessagePanel = new JTextPane();
		filesMessagePanel.setText(filesmessage);
		filesMessagePanel.enable(true);
		filesMessagePanel.setVisible(true);
		parent.setData(filesMessagePanel);*/
		
		
        setControl(showfilecomposite);
        
	}
	
	protected ShowFilesWillBeAddedPage(String pageName) {
		super(pageName);
		setTitle(WEBPersPlugin
				.getResourceString(WEBPersConstants.KEY_TEMP_WINDOW_MAINPAGE_TITLE));
		setDescription(WEBPersPlugin
				.getResourceString(WEBPersConstants.KEY_TEMP_FILESCONFIG_DESC)); 
	}

	public Text getText() {
		return text;
	}

	public IWizardPage getNextPage() {
		if (getWizard() == null) {
    		return null;
    	}
		return getWizard().getPage(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_RESULT_DESC));	
	}
	@Override
	protected boolean nextButtonClick() {
		((TempGeneResultPage)getNextPage()).setAllResList();
		new Thread(((TempGeneResultPage)getNextPage())).start();
//		((TempGeneResultPage)getNextPage()).run();
		return true;
	}

	@Override
	protected boolean previousButtonClick() {
		// TODO 自动生成的方法存根
		return true;
	}
	
	
/*		
		protected Control createDetailsArea(Composite parent){
			Composite panel = new Composite(parent, SWT.NONE);
			panel.setLayoutData(new GridData(GridData.FILL_BOTH));
			GridLayout layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			panel.setLayout(layout);
			
			
			return panel;
		}*/

}
