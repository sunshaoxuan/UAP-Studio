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

public class EventEditorControl extends Composite {



//	private LfwJsEditor editor;
//	private IWorkbenchPartSite site;
	private IEditorInput editorInput;
//	// 文件路径
//	private String filePath;
	private String listenerName;
	
	Text jsText = null;


	public EventEditorControl(Composite parent, int style,
			IWorkbenchPartSite site, String listenerName, EventConf event) {
		super(parent, style);
//		this.filePath = filePath;
		if(event!=null){
			this.listenerName = listenerName;
	//		this.jsEventHandler = event;
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
		
			for (LfwParameter param : event.getParamList()) {
				params += param.getName();
				params += ",";
			}
			if (params.length() > 0) {
				params = params.substring(0, params.length() - 1);
			}
			textHead.setText(listenerName + "." + event.getName() + " = function(" + params + ")  {");
			textHead.setBackground(bg);
			textHead.setLayoutData(gridData);
			textHead.setFont(font);
			
			// 中间部分
	//		Composite center = new Composite(this, SWT.NONE);
	//		FillLayout centerLayout = new FillLayout();
	//		centerLayout.marginWidth = -12;
	//		center.setLayout(centerLayout);
	//		center.setLayoutData(gridDataCenter);
	//		center.setBackground(bg);
			
			// 加载JS编辑器（中间部分）
			Text textCenter = new Text(this, SWT.NULL);
			FillLayout centerLayout = new FillLayout();
			centerLayout.marginWidth = -12;
			textCenter.setLayoutData(gridDataCenter);
			textCenter.setBackground(bg);
			textCenter.setFont(font);
			if(event.getScript()!=null)
				textCenter.setText(event.getScript());
			
			// 方法结尾
			Text textTail = new Text(this, SWT.READ_ONLY);
			textTail.setText("}");
			textTail.setBackground(bg);
			textTail.setLayoutData(gridData);
			textTail.setFont(font);
		}
	}

	
	

	public IEditorInput getEditorInput() {
		return editorInput;
	}

//	public LfwJsEditor getEditor() {
//		return editor;
//	}

//	public String getFilePath() {
//		return filePath;
//	}

	public Text getJsText() {
		return jsText;
	}
	

	public void setJsText(Text jsText) {
		this.jsText = jsText;
	}

	public String getListenerName() {
		return listenerName;
	}



}
