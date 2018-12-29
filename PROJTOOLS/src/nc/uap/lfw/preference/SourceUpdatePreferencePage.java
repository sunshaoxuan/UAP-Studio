package nc.uap.lfw.preference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBProjPlugin;
import nc.uap.lfw.lang.M_wizards;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.http.HttpHost;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class SourceUpdatePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private StringFieldEditor sourceField = null;
	
	private Button autoCheckbuttun = null;

	public SourceUpdatePreferencePage() {
		setTitle(M_wizards.SourceUpdatePreferencePage_0);
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO 自动生成的方法存根

	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, 0);
		composite.setLayout(new GridLayout());
		createSettingContents(composite);
		return composite;
	}

	private void createSettingContents(Composite parent) {
		Group group = new Group(parent, 128);
		group.setLayout(new GridLayout(1, false));
		group.setLayoutData(new GridData(768));
		group.setText(M_wizards.SourceUpdatePreferencePage_1);
		Composite composite = new Composite(group, 0);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(768));
		sourceField = new StringFieldEditor("dataSource", "http://", composite); //$NON-NLS-1$ //$NON-NLS-2$
		sourceField.setPreferenceStore(WEBProjPlugin.getDefault().getPreferenceStore());
		String host = null;
		String port = null;
		String autoCheck = null;
		File file = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation()+"/updateSource.txt"); //$NON-NLS-1$
		if(!file.exists()){
			sourceField.setStringValue("172.16.50.240/web_release/"); //$NON-NLS-1$
			host = "172.16.50.240/web_release/"; //$NON-NLS-1$
//			port = "6687"; //$NON-NLS-1$
		}
		else{
			InputStream is = null;
			try{
				Properties setting = new Properties();
				is = new FileInputStream(file);
				setting.load(is);
				host = setting.getProperty("smart_host"); //$NON-NLS-1$
//				port = setting.getProperty("smart_port"); //$NON-NLS-1$
//				autoCheck = setting.getProperty("autoCheck"); //$NON-NLS-1$
			}
			catch(Exception e){
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
			finally{
				try {
					if(is!=null)
						is.close();
				} catch (IOException e1) {
					WEBProjPlugin.getDefault().logError(e1.getMessage(),e1);
				}
			}
			sourceField.setStringValue(host); //$NON-NLS-1$
		}
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(3, true));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
//		Button updatePlugin = createButton(container, M_wizards.SourceUpdatePreferencePage_2);
//		updatePlugin.addSelectionListener(new SelectionListener() {
//
//			@Override
//			public void widgetSelected(SelectionEvent e) {								
////				WEBProjPlugin.getDefault().logInfo(WEBProjPlugin.getDefault().getBundle().getLocation());			
////				if (MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "提示", "更新完成后需重启studio,是否确定更新？")) {
//					String url = sourceField.getStringValue();
//					String[] value = url.split(":"); //$NON-NLS-1$
//					if(value.length!=2){
//						MessageDialog.openError(null, M_wizards.SourceUpdatePreferencePage_3, M_wizards.SourceUpdatePreferencePage_4);
//						return;
//					}
//					updatePlugins(value[0],value[1]);
////				}
//
//			}
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//
//			}
//		});
		Button updateDev = createButton(container, M_wizards.SourceUpdatePreferencePage_5);
		updateDev.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
						
//				if (MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "提示", "更新完成后需重启中间件,是否确定更新？")) {
					String url = sourceField.getStringValue();
//					String[] value = url.split(":"); //$NON-NLS-1$
//					if(value.length!=2){
//						MessageDialog.openError(null, M_wizards.SourceUpdatePreferencePage_3, M_wizards.SourceUpdatePreferencePage_4);
//						return;
//					}
					updateDevPack(url);
//				}
				
			}			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO 自动生成的方法存根
				
			}
		});
		
		Button clearPatch = createButton(container, M_wizards.SourceUpdatePreferencePage_6);
		clearPatch.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] modules = {"webad","webbd","webdbl","webimp","webrt","websm","uapportal"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
				String ncPath = LfwCommonTool.getNCHomePath().toString();	
				String pubPath = ncPath+"/modules/modulename/classes"; //$NON-NLS-1$
				String praPath = ncPath+"/modules/modulename/META-INF/classes"; //$NON-NLS-1$
				String uiPath = ncPath+"/modules/modulename/client/classes"; //$NON-NLS-1$
				try{
					for(String module:modules){
						FileUtilities.deleteFiles(pubPath.replace("modulename", module)); //$NON-NLS-1$
						FileUtilities.deleteFiles(praPath.replace("modulename", module)); //$NON-NLS-1$
						FileUtilities.deleteFiles(uiPath.replace("modulename", module));					 //$NON-NLS-1$
					}
					FileUtilities.deleteFile(new File(ncPath+"/patchVersion.txt")); //$NON-NLS-1$
					JOptionPane.showMessageDialog(null,M_wizards.SourceUpdatePreferencePage_7);
				}
				catch(Exception e1){
					WEBProjPlugin.getDefault().logError(e1);
				}
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO 自动生成的方法存根
				
			}
		}
		);
		
//		Button updateDetail = createButton(container, M_wizards.SourceUpdatePreferencePage_8);
//		updateDetail.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				String url = sourceField.getStringValue();
//				String[] value = url.split(":"); //$NON-NLS-1$
//				if(value.length!=2){
//					MessageDialog.openError(null, M_wizards.SourceUpdatePreferencePage_3, M_wizards.SourceUpdatePreferencePage_4);
//					return;
//				}
//				updateDetail(value[0],value[1]);
//			}
//		});
		
//		autoCheckbuttun = new Button(container, SWT.CHECK);
//		autoCheckbuttun.setText(M_wizards.SourceUpdatePreferencePage_9);
//		if(autoCheck!=null&&autoCheck.equals("0")){ //$NON-NLS-1$
//			autoCheckbuttun.setSelection(false);
//		}
//		else
//			autoCheckbuttun.setSelection(true);
	}
	
	/**
	 * 更新最新插件
	 */
	public void updatePlugins(String host,String port) {
		String idePath = Platform.getInstallLocation().getURL().getPath();
		final String pluginsPath = idePath.substring(1, idePath.indexOf("Platform")) + "WEB/plugins"; //$NON-NLS-1$ //$NON-NLS-2$
		InputStream inputStream = null;
		try {
//			HttpClient client = new DefaultHttpClient();
//			HttpHost httpHost = new HttpHost(host, Integer.parseInt(port));
//			HttpGet httpGet = new HttpGet("/v63/插件/version.txt");
//			HttpResponse httpResponse = client.execute(httpHost, httpGet);
//			if(!httpResponse.getStatusLine().getReasonPhrase().equals("OK"))
//			{
//				MessageDialog.openError(null, "错误", "找不到版本信息");
//				throw new IOException("找不到版本信息");
//			}
//			inputStream = httpResponse.getEntity().getContent();
			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod("http://"+host+":"+port+"/v63/devplugin/version.txt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int httpResponse = client.executeMethod(method);
			if(httpResponse != 200){
				JOptionPane.showMessageDialog(null,M_wizards.SourceUpdatePreferencePage_10);
				throw new IOException(M_wizards.SourceUpdatePreferencePage_10);
			}
			inputStream = method.getResponseBodyAsStream();
			String content = FileUtilities.fetchFileContent(inputStream, "GBK"); //$NON-NLS-1$
			String newVersion = content.split("\n")[0].split("=")[1].trim(); //$NON-NLS-1$ //$NON-NLS-2$

			String currentVersion = null;
			File file = new File(pluginsPath+"/version.txt"); //$NON-NLS-1$
			if(file.exists()){
				content = FileUtilities.fetchFileContent(file, "GBK"); //$NON-NLS-1$
				currentVersion = content.split("\n")[0].split("=")[1].trim(); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if(currentVersion == null)
				currentVersion = "0000"; //$NON-NLS-1$
//			int sel = JOptionPane.showConfirmDialog(null, "当前版本为"+currentVersion+"，最新版本为"+newVersion+"，是否更新最新插件");
//			if(sel != 0){
//				return;
//			}
			if(!MessageDialog.openConfirm(null, M_wizards.SourceUpdatePreferencePage_11, M_wizards.SourceUpdatePreferencePage_12+currentVersion+M_wizards.SourceUpdatePreferencePage_13+newVersion+M_wizards.SourceUpdatePreferencePage_14)){
				return;
			}
//			httpGet = new HttpGet("/v63/插件/" + newVersion + "/plugins.zip");
//			final HttpResponse httpResponse2 = client.execute(httpHost, httpGet);
			final HttpMethod method2 = new GetMethod("http://"+host+":"+port+"/v63/devplugin/" + newVersion + "/plugins.zip"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			httpResponse = client.executeMethod(method2);
			if(httpResponse != 200){
				JOptionPane.showMessageDialog(null,M_wizards.SourceUpdatePreferencePage_15);
				throw new IOException(M_wizards.SourceUpdatePreferencePage_15);
			}
			// pluginStream = httpResponse.getEntity().getContent();
			// final InputStream zipInput = pluginStream;
			// String idePath =
			// Platform.getInstallLocation().getURL().getPath();
			// String pluginsPath =
			// idePath.substring(0,idePath.lastIndexOf("/"));
			// FileLocator.resolve(WEBProjPlugin.getDefault().getBundle().getEntry("/"))
			Job job = new Job(M_wizards.SourceUpdatePreferencePage_16) {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask(M_wizards.SourceUpdatePreferencePage_17, 2);
					InputStream zipInput = null;
					try {
						zipInput = method2.getResponseBodyAsStream();
						String zipPath = "C:/WebPlugins/plugins.zip"; //$NON-NLS-1$
						FileUtilities.saveFile(zipPath, zipInput);
						monitor.worked(1);
						FileUtilities.unzip(zipPath, pluginsPath);
					} catch (Exception e) {
						WEBProjPlugin.getDefault().logError(e.getMessage(), e);
					} finally {
						try {
							if (zipInput != null)
								zipInput.close();
						} catch (IOException e) {
							WEBProjPlugin.getDefault().logError(e.getMessage(), e);
						}
					}
					monitor.worked(1);
					monitor.done();
					JOptionPane.showMessageDialog(null, M_wizards.SourceUpdatePreferencePage_18);
					WEBProjPlugin.getDefault().logInfo(M_wizards.SourceUpdatePreferencePage_19);
					// MessageDialog.openInformation(WEBProjPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
					// "成功", "更新完成，请重启studio");
					return Status.OK_STATUS;
				}

			};
			job.schedule();
			// FileUtilities.inflate(inputStream, pluginsPath);
			// MessageDialog.openInformation(null, "成功",
			// "更新完成，请重启studio");
		} catch (Exception e1) {
			MessageDialog.openError(null, M_wizards.SourceUpdatePreferencePage_3, M_wizards.SourceUpdatePreferencePage_20);
			WEBProjPlugin.getDefault().logError(e1.getMessage(), e1);
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e1) {
				WEBProjPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}


	/**
	 * 更新开发包
	 */
	public void updateDevPack(String url) {
		InputStream inputStream = null;
		final String ncPath = LfwCommonTool.getNCHomePath().toString();	
		try {
//			HttpClient client = new DefaultHttpClient();
//			HttpHost httpHost = new HttpHost(host, Integer.parseInt(port));
//			HttpGet httpGet = new HttpGet("/v63/开发包/version.txt");
//			HttpResponse httpResponse = client.execute(httpHost, httpGet);
//			if(!httpResponse.getStatusLine().getReasonPhrase().equals("OK"))
//			{
//				MessageDialog.openError(null, "错误", "找不到版本信息");
//				throw new IOException("找不到版本信息");
//			}
//			inputStream = httpResponse.getEntity().getContent();
			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod("http://"+url+"/v63/devpack/version.txt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int httpResponse = client.executeMethod(method);
			if(httpResponse != 200){
				JOptionPane.showMessageDialog(null,M_wizards.SourceUpdatePreferencePage_10);
				throw new IOException(M_wizards.SourceUpdatePreferencePage_10);
			}
			inputStream = method.getResponseBodyAsStream();
			String content = FileUtilities.fetchFileContent(inputStream, "GBK"); //$NON-NLS-1$
			String newVersion = content.split("\n")[0].split("=")[1].trim(); //$NON-NLS-1$ //$NON-NLS-2$
			String uapHome = LfwCommonTool.getUapHome();
			String currentVersion = null;
			File file = new File(uapHome+"/version.txt"); //$NON-NLS-1$
			if(file.exists()){
				content = FileUtilities.fetchFileContent(file, "GBK"); //$NON-NLS-1$
				currentVersion = content.split("\n")[0].split("=")[1].trim(); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if(currentVersion == null)
				currentVersion = M_wizards.SourceUpdatePreferencePage_25; 
//			int sel = JOptionPane.showConfirmDialog(null, "当前版本为"+currentVersion+"，最新版本为"+newVersion+"，是否更新开发包");
//			if(sel != 0){
//				return;
//			}
			if(!MessageDialog.openConfirm(null, M_wizards.SourceUpdatePreferencePage_11, M_wizards.SourceUpdatePreferencePage_12+currentVersion+M_wizards.SourceUpdatePreferencePage_13+newVersion+M_wizards.SourceUpdatePreferencePage_21)){
				return;
			}
//			httpGet = new HttpGet("/v63/开发包/" + newVersion + "/lfw.zip");
//			final HttpResponse httpResponse2 = client.execute(httpHost, httpGet);
//			httpGet = new HttpGet("/v63/开发包/" + newVersion + "_patch/patch.zip");
//			HttpClient client2 =new DefaultHttpClient();
//			final HttpResponse httpResponse3 = client2.execute(httpHost, httpGet);
			final HttpMethod method2 = new GetMethod("http://"+url+"/v63/devpack/" + newVersion + "/lfw.zip"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//			final HttpMethod method3 = new GetMethod("http://"+host+":"+port+"/v63/devpack/" + newVersion + "_patch/patch.zip");
			int httpResponse2 = client.executeMethod(method2);			
			if(httpResponse2 != 200){
				JOptionPane.showMessageDialog(null,M_wizards.SourceUpdatePreferencePage_22);
				throw new IOException(M_wizards.SourceUpdatePreferencePage_10);
			}
//			int httpResponse3 = client.executeMethod(method3);
//			if(httpResponse3 != 200){
//				JOptionPane.showMessageDialog(null,"找不到补丁更新文件");
//				throw new IOException("找不到版本信息");
//			}
			Job job = new Job(M_wizards.SourceUpdatePreferencePage_23) {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask(M_wizards.SourceUpdatePreferencePage_17, 2);
					InputStream zipInput = null;
//					InputStream zipInput2 = null;
					try {
//						zipInput = httpResponse2.getEntity().getContent();
//						zipInput2 = httpResponse3.getEntity().getContent();
						zipInput = method2.getResponseBodyAsStream();
//						zipInput2 = method3.getResponseBodyAsStream();
						String lfwPath = "C:/devPackage/lfw.zip"; //$NON-NLS-1$
//						String patchPath = "C:/devPackage/patch.zip";
						FileUtilities.saveFile(lfwPath, zipInput);
//						FileUtilities.saveFile(patchPath, zipInput2);
						monitor.worked(1);

						FileUtilities.unzip(lfwPath, ncPath);
//						FileUtilities.unzip(patchPath, ncPath);
					} catch (Exception e) {
						WEBProjPlugin.getDefault().logError(e.getMessage(), e);
					} finally {
						try {
							if (zipInput != null)
								zipInput.close();
						} catch (IOException e) {
							WEBProjPlugin.getDefault().logError(e.getMessage(), e);
						}
//						try {
//							if (zipInput2 != null)
//								zipInput2.close();
//						} catch (IOException e) {
//							WEBProjPlugin.getDefault().logError(e.getMessage(), e);
//						}
					}
					monitor.worked(1);
					monitor.done();
					JOptionPane.showMessageDialog(null, M_wizards.SourceUpdatePreferencePage_24);
					WEBProjPlugin.getDefault().logInfo(M_wizards.SourceUpdatePreferencePage_19);
					// MessageDialog.openInformation(WEBProjPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
					// "成功", "更新完成，请重启studio");
					return Status.OK_STATUS;
				}

			};
			job.schedule();
		}
		catch (Exception e1) {
			WEBProjPlugin.getDefault().logError(e1.getMessage(),e1);
		}
		finally{
			try {
				if(inputStream != null)
					inputStream.close();
			} catch (IOException e1) {
				WEBProjPlugin.getDefault().logError(e1.getMessage(),e1);
			}
		}
	}
	private void updateDetail(String host,String port){
		InputStream inputStream = null;
		final String ncPath = LfwCommonTool.getNCHomePath().toString();	
		try {
			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod("http://"+host+":"+port+"/v63/devplugin/readme.txt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int httpResponse = client.executeMethod(method);
			if(httpResponse != 200){
				JOptionPane.showMessageDialog(null,M_wizards.SourceUpdatePreferencePage_10);
				throw new IOException(M_wizards.SourceUpdatePreferencePage_10);
			}
			inputStream = method.getResponseBodyAsStream();
			String content = FileUtilities.fetchFileContent(inputStream, "GBK"); //$NON-NLS-1$
			JOptionPane.showMessageDialog(null, content);
		}
		catch(Exception e){
			WEBProjPlugin.getDefault().logError(e.getMessage(),e);
		}
		finally{
			try {
				if(inputStream != null)
					inputStream.close();
			} catch (IOException e1) {
				WEBProjPlugin.getDefault().logError(e1.getMessage(),e1);
			}
		}
	}

	private Button createButton(Composite parent, String value) {
		Button button = new Button(parent, 0);
		button.setText(value);
		return button;
	}
	public boolean performOk() {
		String url = sourceField.getStringValue();
//		String[] value = url.split(":"); //$NON-NLS-1$
//		if(value.length!=2){
//			MessageDialog.openError(null, M_wizards.SourceUpdatePreferencePage_3, M_wizards.SourceUpdatePreferencePage_4);
//			return false;
//		}
//		boolean autoCheck = autoCheckbuttun.getSelection();
//		String autoflag = autoCheck==true?"1":"0"; //$NON-NLS-1$ //$NON-NLS-2$
		OutputStream fos = null;
		try {
			Properties setting = new Properties();
			File file = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation()+"/updateSource.txt"); //$NON-NLS-1$
			if(!file.exists()){
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			setting.setProperty("smart_host",url); //$NON-NLS-1$
//			setting.setProperty("smart_port", value[1]); //$NON-NLS-1$
//			setting.setProperty("autoCheck", autoflag); //$NON-NLS-1$
			setting.store(fos, null);
		}
		catch(Exception e){
			
		}
		finally{
			try {
				if(fos!= null)
					fos.close();
			} catch (IOException e) {
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
		}
        return true;
    }
	protected void performApply() {
	    performOk();
	}

}
