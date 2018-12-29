package nc.uap.lfw.perspective.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 提交规则 变量对话框内容
 * 
 * @author guoweic
 *
 */
public class SubmitRuleParamPanel extends Canvas {
	
	/**
	 * 参数名输入框
	 */
	private Text nameText;

	/**
	 * 参数描述输入框
	 */
	private Text valueText;
	
	public SubmitRuleParamPanel(Composite parent, int style) {
		super(parent, style);
		initUI();
	}

	private void initUI() {
		this.setLayout(new GridLayout(2, false));
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		new Label(this, SWT.NONE).setText("名称:");
		nameText = new Text(this, SWT.BORDER);
		nameText.setLayoutData(createGridData(200, 1));

		new Label(this, SWT.NONE).setText("值:");
		valueText = new Text(this, SWT.BORDER);
		valueText.setLayoutData(createGridData(200, 1));
		
	}
	
	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 15);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}

	public Text getNameText() {
		return nameText;
	}

	public Text getValueText() {
		return valueText;
	}

}
