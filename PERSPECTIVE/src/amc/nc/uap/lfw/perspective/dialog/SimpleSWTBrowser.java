/**
 * 
 */
package nc.uap.lfw.perspective.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SimpleSWTBrowser {

	// 定义浏览器的标题
	public static final String APP_TITLE = "Simple SWT Browser";
	// 定义主页的url
	public String HOME_URL = "";
	// 声明主窗口和其它控件
	private Shell parent = null;
	private Button backButton = null;// 后退按钮
	private Button forwardButton = null;// 前进按钮
	private Button stopButton = null;// 停止按钮
	private Text locationText = null;// 显示url的文本框
	private Button goButton = null;// 转向按钮
	private Browser browser = null;// 浏览器对象
	private Button homeButton = null;// 主页按钮
	private Label statusText = null;// 显示浏览器状态的文本框
	private ProgressBar progressBar = null;// 装载页面时的进度条
	private Button refreshButton = null;// 刷新按钮

	public SimpleSWTBrowser(String url){
		this.HOME_URL = url;
		this.parent = new Shell(SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.TITLE);
	}
	
	public void openBrowser(){
		createparent();
		parent.setSize(1024, 768);
		parent.open();
	}
	
	// 初始化浏览器
	private void createBrowser() {
		parent.setSize(1024, 768);
		parent.setLayout(new FillLayout());
		// 创建浏览器对象
		browser = new Browser(parent, SWT.BORDER);
		// 设置浏览器布局
		GridData layout = new GridData();
		layout.horizontalSpan = 7;
		layout.horizontalAlignment = GridData.FILL;
		layout.verticalAlignment = GridData.FILL;
		layout.grabExcessVerticalSpace = true;
		browser.setLayoutData(layout);
		// 为浏览器注册标题改变事件
//		browser.addTitleListener(new org.eclipse.swt.browser.TitleListener() {
//			public void changed(org.eclipse.swt.browser.TitleEvent e) {
//				parent.setText(APP_TITLE + " - " + e.title);
//			}
//		});
		// 为浏览器注册地址改变事件
		browser.addLocationListener(new org.eclipse.swt.browser.LocationListener() {
			public void changing(org.eclipse.swt.browser.LocationEvent e) {
				locationText.setText(e.location);
			}

			public void changed(org.eclipse.swt.browser.LocationEvent e) {
			}
		});
		// 为浏览器注册装载网页事件
		browser.addProgressListener(new org.eclipse.swt.browser.ProgressListener() {
			// 当装载时，设置装载的进度，并且设置停止按钮可用
			public void changed(org.eclipse.swt.browser.ProgressEvent e) {
				if (!stopButton.isEnabled() && e.total != e.current) {
					stopButton.setEnabled(true);
				}
				progressBar.setMaximum(e.total);
				progressBar.setSelection(e.current);
			}

			// 装载完成后设置停止按钮，后退按钮，前进按钮和进度条的状态
			public void completed(
					org.eclipse.swt.browser.ProgressEvent e) {
				stopButton.setEnabled(false);
				backButton.setEnabled(browser.isBackEnabled());
				forwardButton.setEnabled(browser.isForwardEnabled());
				progressBar.setSelection(0);
			}
		});
		// 注册浏览器状态改变事件
		browser.addStatusTextListener(new org.eclipse.swt.browser.StatusTextListener() {
			public void changed(
					org.eclipse.swt.browser.StatusTextEvent e) {
				statusText.setText(e.text);
			}
		});
		// 初始状态打开主页的url
		browser.setUrl(HOME_URL);
	}

	// 创建窗口和窗口的控件
	private void createparent() {
		//parent = new org.eclipse.swt.widgets.Shell();
		org.eclipse.swt.layout.GridLayout gridLayout1 = new GridLayout();
		org.eclipse.swt.layout.GridData gridData2 = new org.eclipse.swt.layout.GridData();
		org.eclipse.swt.layout.GridData gridData4 = new org.eclipse.swt.layout.GridData();
		org.eclipse.swt.layout.GridData gridData5 = new org.eclipse.swt.layout.GridData();
		org.eclipse.swt.layout.GridData gridData6 = new org.eclipse.swt.layout.GridData();
		org.eclipse.swt.layout.GridData gridData7 = new org.eclipse.swt.layout.GridData();
		org.eclipse.swt.layout.GridData gridData8 = new org.eclipse.swt.layout.GridData();
		backButton = new Button(parent, SWT.ARROW | SWT.LEFT);
		forwardButton = new Button(parent, SWT.ARROW | SWT.RIGHT);
		stopButton = new Button(parent, SWT.NONE);
		refreshButton = new Button(parent, SWT.NONE);
		homeButton = new Button(parent, SWT.NONE);
		locationText = new Text(parent, SWT.BORDER);
		goButton = new Button(parent, SWT.NONE);
		createBrowser();
		progressBar = new ProgressBar(parent, SWT.BORDER);
		statusText = new Label(parent, SWT.NONE);
//		parent.setText(APP_TITLE);
		parent.setLayout(gridLayout1);
		gridLayout1.numColumns = 7;
		backButton.setEnabled(false);
		backButton.setToolTipText("Navigate back to the previous page");
		backButton.setLayoutData(gridData6);
		forwardButton.setEnabled(false);
		forwardButton.setToolTipText("Navigate forward to the next page");
		forwardButton.setLayoutData(gridData5);
		stopButton.setText("Stop");
		stopButton.setEnabled(false);
		stopButton.setToolTipText("Stop the loading of the current page");
		goButton.setText("Go!");
		goButton.setLayoutData(gridData8);
		goButton.setToolTipText("Navigate to the selected web address");
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		locationText.setLayoutData(gridData2);
		locationText.setText(HOME_URL);
		locationText.setToolTipText("Enter a web address");
		homeButton.setText("Home");
		homeButton.setToolTipText("Return to home page");
		statusText.setText("Done");
		statusText.setLayoutData(gridData7);
		gridData4.horizontalSpan = 5;
		progressBar.setLayoutData(gridData4);
		progressBar.setEnabled(false);
		progressBar.setSelection(0);
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.horizontalSpan = 1;
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		refreshButton.setText("Refresh");
		refreshButton.setToolTipText("Refresh the current page");
		parent.setSize(new org.eclipse.swt.graphics.Point(553, 367));
		// 注册显示地址的文本框事件
		locationText
				.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
					public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
						locationText.selectAll();
					}
				});
		locationText.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				// Handle the press of the Enter key in the locationText.
				// This will browse to the entered text.
				if (e.character == SWT.LF || e.character == SWT.CR) {
					e.doit = false;
					browser.setUrl(locationText.getText());
				}
			}
		});
		refreshButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						browser.refresh();// 重新载入
					}
				});
		locationText
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						browser.setUrl(locationText.getText());// 设置浏览器的指向的url
					}
				});
		stopButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						browser.stop();// 停止装载网页
					}
				});
		backButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						browser.back();// 后退
					}
				});
		forwardButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						browser.forward();// 前进
					}
				});
		homeButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						browser.setUrl(HOME_URL);// 设置主页
					}
				});
		goButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						browser.setUrl(locationText.getText());// 转向地址的网页
					}
				});
	}
	
}
