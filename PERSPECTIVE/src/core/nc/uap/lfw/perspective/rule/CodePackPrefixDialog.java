package nc.uap.lfw.perspective.rule;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.lang.M_perspective;

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

public class CodePackPrefixDialog extends DialogWithTitle{
	private Text codePrefixText;
	private String codePrefix;
	public CodePackPrefixDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		//ID
		Label idLabel = new Label(container, SWT.NONE);
		idLabel.setText(M_perspective.CodePackPrefixDialog_0);
		codePrefixText = new Text(container, SWT.NONE);
		codePrefixText.setLayoutData(new GridData(220,20));
		IProject proj = LFWPersTool.getCurrentProject();
		String prefix = LfwCommonTool.getModuleProperty(proj, "pack.prefix"); //$NON-NLS-1$
		if(prefix == null){
			codePrefixText.setText("com."); //$NON-NLS-1$
			codePrefixText.setSelection(4);
		}
		else{
			codePrefixText.setText(prefix);
			codePrefixText.setSelection(prefix.length());
		}
		return container;
	}
	
	protected void okPressed() {
		//IDÐ£Ñé
		if(codePrefixText.getText() == null || codePrefixText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_perspective.CodePackPrefixDialog_1, M_perspective.CodePackPrefixDialog_2);
			return;
		}
		if(codePrefixText.getText().startsWith(".")||codePrefixText.getText().endsWith(".")||codePrefixText.getText().indexOf("..")>-1){
			MessageDialog.openError(null, "error", M_perspective.CodeRuleChecker_0);
			return;
			
		}
		setCodePrefix(codePrefixText.getText());
		super.okPressed();
	}

	public String getCodePrefix() {
		return codePrefix;
	}

	public void setCodePrefix(String codePrefix) {
		this.codePrefix = codePrefix;
	}
}
