/**
 * 
 */
package nc.uap.lfw.perspective.listener;

import nc.uap.lfw.core.data.LfwParameter;
import nc.uap.lfw.core.event.conf.EventConf;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * @author chouhl
 *
 */
public class EventEditorControlComp extends EventEditorControl {

	private IEditorInput editorInput;

	private Text jsText = null;
	
	private EventConf eventConf = null;
	
	public EventEditorControlComp(Composite parent, int style, IWorkbenchPartSite site, EventConf eventConf) {
		super(parent, style, site, eventConf.getMethodName(), null);
		this.eventConf = eventConf;
		// 背景色
		Color bg = new Color(null, 255, 255, 255);
		// 总体布局
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		this.setLayout(layout);
		this.setBackground(bg);
		GridData gridDataCenter = new GridData(GridData.FILL_BOTH);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		
		Font font = new Font(Display.getDefault(), "Arial", 10, SWT.NONE);
		// 方法开头
		Text textHead = new Text(this, SWT.READ_ONLY);
		// 参数
		String params = "";
		for (LfwParameter param : eventConf.getParamList()) {
			params += param.getName();
			params += ",";
		}
		if (params.length() > 0) {
			params = params.substring(0, params.length() - 1);
		}
		textHead.setText(eventConf.getMethodName() + "." + eventConf.getName() + " = function(" + params + ")  {");
		textHead.setBackground(bg);
		textHead.setLayoutData(gridData);
		textHead.setFont(font);
		
		// 加载JS编辑器（中间部分）
		jsText = new Text(this, SWT.MULTI);
		FillLayout centerLayout = new FillLayout();
		centerLayout.marginWidth = -12;
		jsText.setLayoutData(gridDataCenter);
		jsText.setBackground(bg);
		jsText.setFont(font);
		jsText.setText(eventConf.getScript() == null ? "" : eventConf.getScript());
		
//		setDirtyTrue();
		
		// 方法结尾
		Text textTail = new Text(this, SWT.READ_ONLY);
		textTail.setText("}");
		textTail.setBackground(bg);
		textTail.setLayoutData(gridData);
		textTail.setFont(font);
	}

	public IEditorInput getEditorInput() {
		return editorInput;
	}

	public Text getJsText() {
		return jsText;
	}

	public EventConf getEventConf() {
		return eventConf;
	}
	
}
