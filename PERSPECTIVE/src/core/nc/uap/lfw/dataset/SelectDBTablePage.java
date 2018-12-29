package nc.uap.lfw.dataset;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.HashMap;

import nc.lfw.lfwtools.perspective.MainPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SelectDBTablePage extends WizardPage implements IImportTablesService{
	private CCombo driverCombo = null;

	private Text userText = null;

	private Text pwdText = null;

	private Text urlText = null;

	private HashMap<String, String> driverMap = new HashMap<String, String>();


	public SelectDBTablePage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		driverMap.put("SQLServer", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
	}

	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		new Label(container, SWT.NONE).setText("数据库类型");
		driverCombo = new CCombo(container, SWT.BORDER);
		driverCombo.setEditable(false);
		driverCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		driverCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				doDriverComboSelction();
			}

		});
		String[] items = new String[] { "SQLServer" };
		driverCombo.setItems(items);
		driverCombo.setText(items[0]);
		new Label(container, SWT.NONE).setText("URL");
		urlText = new Text(container, SWT.BORDER);
		urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		urlText.setText("jdbc:sqlserver://127.0.0.1:1433;database=dbName;sendStringParametersAsUnicode=false");
		new Label(container, SWT.NONE).setText("用户");
		userText = new Text(container, SWT.BORDER);
		userText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		userText.setText("sa");
		new Label(container, SWT.NONE).setText("密码");
		pwdText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		pwdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		doDriverComboSelction();
		setControl(container);
	}

	private void doDriverComboSelction() {
		String text = driverCombo.getText();
		if ("SQLServer".equalsIgnoreCase(text)) {
			String url = getDefaultStr("defaultURL","jdbc:sqlserver://127.0.0.1:1433;database=dbName;sendStringParametersAsUnicode=false");
			urlText.setText(url);
			userText.setText(getDefaultStr("defaultuserstr","sa"));
			pwdText.setText(getDefaultStr("defaultuserpwdstr","sa"));
		}
	}
	private String getDefaultStr(String strType, String defaultStr){
		IStringVariableManager vvManager = VariablesPlugin.getDefault().getStringVariableManager();
		IValueVariable var = vvManager.getValueVariable(strType);
		String value =  var == null ? "" : var.getValue();
		if(value == null || value.trim().length() == 0)
			value = defaultStr;
		return value;
	}
	private void putStr(String strType, String str){
		IStringVariableManager vvManager = VariablesPlugin.getDefault().getStringVariableManager();
		IValueVariable var = vvManager.getValueVariable(strType);
		if(var == null){
			var = vvManager.newValueVariable(strType, "");
			try {
				vvManager.addVariables(new IValueVariable[]{var});
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		var.setValue(str);
	}
	@Override
	public IWizardPage getNextPage() {
		putStr("defaultURL", urlText.getText());
		putStr("defaultuserstr", userText.getText());
		putStr("defaultuserpwdstr", pwdText.getText());
		return getWizard().getPage(SelectTableColumnPage.class.getCanonicalName());
	}

	public Connection getConnection() throws Exception {
		String driverCls = driverMap.get(driverCombo.getText());
		String url = urlText.getText();
		String user = userText.getText();
		String pwd = pwdText.getText();
		Driver driver = (Driver) Class.forName(driverCls).newInstance();
		Connection con = DataBaseTool.getConnection(driver, url, user, pwd);
		return con;

	}

	public DBTable[] getAllTables() {
		DBTable[] tables = new DBTable[0];
		Connection con = null;
		try {
			con = getConnection();
			tables = DataBaseTool.getAllTables(con);
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(), e);
			MessageDialog.openError(null, "错误提示", "获取数据库表出现错误");
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return tables;

	}
}
