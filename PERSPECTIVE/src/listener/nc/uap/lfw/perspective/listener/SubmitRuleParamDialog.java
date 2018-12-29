package nc.uap.lfw.perspective.listener;

import nc.uap.lfw.core.event.conf.EventSubmitRule;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * 提交规则 变量对话框
 * @author guoweic
 *
 */
public class SubmitRuleParamDialog extends Dialog {
	
	private SubmitRuleParamPanel mainContainer;
	
	private String name;
	
	private String value;
	
	private EventSubmitRule submitRule = null;

	protected SubmitRuleParamDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected Point getInitialSize() {
		return new Point(300, 150); 
	}
	
	
	public Control createDialogArea(Composite parent) {

		mainContainer = new SubmitRuleParamPanel(parent, SWT.NONE);
		
		return mainContainer;
	}
	
	protected void okPressed() {
		name = mainContainer.getNameText().getText().trim();
		value = mainContainer.getValueText().getText().trim();
		if (!"".equals(name)) {
			super.okPressed();
		} else if (submitRule.getParam(name) != null) {
			MessageDialog.openInformation(null, "提示", "名称不能重复");
			mainContainer.getNameText().setFocus();
		} else {
			MessageDialog.openInformation(null, "提示", "名称不能为空");
			mainContainer.getNameText().setFocus();
		}
	}

	public SubmitRuleParamPanel getMainContainer() {
		return mainContainer;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setSubmitRule(EventSubmitRule submitRule) {
		this.submitRule = submitRule;
	}

}
