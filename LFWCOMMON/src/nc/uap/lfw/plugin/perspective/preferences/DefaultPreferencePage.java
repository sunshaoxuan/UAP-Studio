package nc.uap.lfw.plugin.perspective.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * LFW 工具 首选项配置页首页
 * @author guoweic
 *
 */
public class DefaultPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	protected Control createContents(Composite parent) {
		Composite topComp = new Composite(parent, SWT.NONE);
		topComp.setLayout(new RowLayout());
		new Label(topComp, SWT.NONE).setText("欢迎使用 LFW 工具");
		return topComp;
	}

	public void init(IWorkbench workbench) {
		
	}

}
