package nc.uap.lfw.component;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.lang.M_application;
import nc.uap.lfw.perspective.rule.CodeRuleChecker;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CreateComponentDialog extends DialogWithTitle {
	public static final String COMP_PRE = "com.prod.comp."; //$NON-NLS-1$
	/**
	 * ID输入框
	 */
	private Text idText;
	/**
	 * 名称输入框
	 */
	private Text nameText;
	
	private Text packageText;
	
	private String id;
	
	private String name;
	
	private String packName;
	
	public CreateComponentDialog(Shell parentShell, String title) {
		super(parentShell, title);
		// TODO Auto-generated constructor stub
	}
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		Label packLabel = new Label(container, SWT.NONE);
		packLabel.setText(M_application.CreateComponentDialog_0);
		packageText = new Text(container, SWT.NONE);
		packageText.setLayoutData(new GridData(220,20));
		IProject proj = LFWPersTool.getCurrentProject();
		String prefix = LfwCommonTool.getModuleProperty(proj, CodeRuleChecker.PACK_PREFIX);	
		String bcpId = LFWPersTool.getBcpId(LFWPersTool.getCurrentTreeItem());
		packageText.setText(prefix+"."+bcpId+".uiComps");  //$NON-NLS-1$ //$NON-NLS-2$
//		Label idLabel = new Label(container, SWT.NONE);
//		idLabel.setText("ID");
//		idText = new Text(container, SWT.NONE);
//		idText.setLayoutData(new GridData(220,20));
//		idText.setText("uiComps"); 
		
		//名称
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText(M_application.CreateComponentDialog_1);
		nameText = new Text(container, SWT.NONE);
		nameText.setLayoutData(new GridData(220,20));
		nameText.setText("");  //$NON-NLS-1$
		nameText.setFocus();
		
		return container;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	protected void okPressed() {
		if(packageText.getText().trim().length()==0){
			MessageDialog.openError(null, M_application.CreateApplicationDialog_12, M_application.CreateApplicationDialog_13);
			return;
		}
		String prefixId = packageText.getText().trim();
		String id = prefixId.substring(prefixId.lastIndexOf(".")+1); //$NON-NLS-1$
		if(!id.equals(prefixId)){
			setPackName(prefixId.substring(0,prefixId.lastIndexOf("."))); //$NON-NLS-1$
		}
		else
			setPackName(""); //$NON-NLS-1$
		
		setId(id);
		if(nameText.getText().trim().length()==0){
			MessageDialog.openError(null, M_application.CreateApplicationDialog_15, M_application.CreateApplicationDialog_16);
			return;
		}
		setName(nameText.getText().trim());

//		setPackName(packageText.getText().trim());
		super.okPressed();
	}
	

}
