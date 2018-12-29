package nc.uap.lfw.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.lang.M_core;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class ScanNewVersionTask implements Runnable{

	private int flag = 0;
	private boolean patchFlag = true;
	@Override
	public void run() {
		
		try {
			Thread.sleep(10000);
		} catch (Exception e1) {
			WEBProjPlugin.getDefault().logError(e1.getMessage(),e1);
		}
		int count = 0;
		String uapHome = LfwCommonTool.getUapHome();
		String idePath = Platform.getInstallLocation().getURL().getPath();
		
		Properties setting = new Properties();
		InputStream is = null;
		try{
			File file = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation()+"/updateSource.txt"); //$NON-NLS-1$
			if(!file.exists()){
				return;
			}
			is = new FileInputStream(file);
			setting.load(is);
		}
		catch (Exception e) {
			WEBProjPlugin.getDefault().logError(e.getMessage(),e);
		}
		finally{
			try {
				if(is != null)
					is.close();
			} catch (IOException e) {
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
		}
		String host = setting.getProperty("smart_host"); //$NON-NLS-1$
		String port = setting.getProperty("smart_port"); //$NON-NLS-1$
		String autoflag = setting.getProperty("autoCheck"); //$NON-NLS-1$
		if(host == null||(autoflag!=null&&autoflag.equals("0"))){ //$NON-NLS-1$
			return;
		}
		do{
			try {
				if(count == 0){
					checkPlugin(idePath,host,port);
					checkDevPack(uapHome,host,port);
				}
				
				checkPatch(uapHome,host,port);
				count++;
				if(count==60){
					count = 0;
				}
			}catch(Exception e){
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
			finally{
				try {
					Thread.sleep(60000);
				} 
				catch (InterruptedException e) {
					WEBProjPlugin.getDefault().logError(e.getMessage(), e);
				}
			}
		}while(true);
	}
	
	public void checkPatch(String uapHome,String host,String port){
		InputStream inputStream = null;
		String content = null;
		String currentVersion = null;
		try{
			File patchFile = new File(uapHome+"/patchVersion.txt"); //$NON-NLS-1$
			if(patchFile.exists()){
				content = FileUtilities.fetchFileContent(patchFile, "GBK"); //$NON-NLS-1$
				currentVersion = content.split("\n")[0].split("=")[1].trim(); //$NON-NLS-1$ //$NON-NLS-2$
			}
			HttpClient client = new HttpClient();
			HttpMethod method = null;
//			HttpHost httpHost = new HttpHost(host, Integer.parseInt(port));
			method = new GetMethod("http://"+host+":"+port+"/v63/devpack/patchVersion.txt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//			method = new GetMethod(location)("/v63/开发包/patchVersion.txt");
//			HttpResponse httpResponse = client.executeMethod(method);
			int httpResponse = client.executeMethod(method);
//			if(!httpResponse.getStatusLine().getReasonPhrase().equals("OK"))
//			{
//				return;
//			}
			if(httpResponse != 200){
				return;
			}
			inputStream = method.getResponseBodyAsStream();
//			inputStream = httpResponse.getEntity().getContent();
			content = FileUtilities.fetchFileContent(inputStream, "GBK"); //$NON-NLS-1$
			String newVersion = content.split("\n")[0].split("=")[1].trim(); //$NON-NLS-1$ //$NON-NLS-2$
			if(!patchFile.exists()||!newVersion.equals(currentVersion)){
				updatePatch(host,port);
			}
			flag = 0;
		}
		catch(Exception e){
			if(flag == 0){
//				JOptionPane.showMessageDialog(null, "更新出错，请检查数据源");
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
			flag++;
		}
		finally{
			try {
				if(inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
		}
	}
	public void checkDevPack(String uapHome,String host,String port){
		InputStream inputStream = null;
		String content = null;
		String currentVersion = null;
		try{
			File file = new File(uapHome+"/version.txt"); //$NON-NLS-1$
			if(file.exists()){
				content = FileUtilities.fetchFileContent(file, "GBK"); //$NON-NLS-1$
				currentVersion = content.split("\n")[0].split("=")[1].trim(); //$NON-NLS-1$ //$NON-NLS-2$
			}
			HttpClient client = new HttpClient();
			
//			HttpHost httpHost = new HttpHost(host, Integer.parseInt(port));
//			HttpGet httpGet = new HttpGet("/v63/开发包/version.txt");
//			HttpResponse httpResponse = client.execute(httpHost, httpGet);
//			if(!httpResponse.getStatusLine().getReasonPhrase().equals("OK"))
//			{
//				return;
//			}
			HttpMethod method = new GetMethod("http://"+host+":"+port+"/v63/devpack/version.txt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int httpResponse = client.executeMethod(method);
			inputStream = method.getResponseBodyAsStream();
			content = FileUtilities.fetchFileContent(inputStream, "GBK"); //$NON-NLS-1$
			String newVersion = content.split("\n")[0].split("=")[1].trim(); //$NON-NLS-1$ //$NON-NLS-2$
			if(!file.exists()||!newVersion.equals(currentVersion)){
				int type = JOptionPane.showConfirmDialog(null, M_core.ScanNewVersionTask_0+newVersion+M_core.ScanNewVersionTask_1);
				if(type == 0){
					updateDevPack(host,port);
				}				
			}
			flag = 0;
		}
		catch(Exception e){
			if(flag == 0){
//				JOptionPane.showMessageDialog(null, "更新出错，请检查数据源");
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
			flag++;
		}
		finally{
			try {
				if(inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
		}
	}
	public void checkPlugin(String idePath,String host,String port){
		InputStream inputStream = null;
		String content = null;
		String currentVersion = null;
		try{
			int idebase = idePath.indexOf("Platform"); //$NON-NLS-1$
			String pluginsPath = null;
			if(idebase != -1)
				pluginsPath = idePath.substring(1, idePath.indexOf("Platform")) + "WEB/plugins"; //$NON-NLS-1$ //$NON-NLS-2$
			else
				pluginsPath = idePath.substring(1) + "WEB/plugins"; //$NON-NLS-1$
			File file = new File(pluginsPath+"/version.txt"); //$NON-NLS-1$
			if(file.exists()){
				content = FileUtilities.fetchFileContent(file, "GBK"); //$NON-NLS-1$
				currentVersion = content.split("\n")[0].split("=")[1].trim(); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
//			HttpClient client = new DefaultHttpClient();
//			HttpHost httpHost = new HttpHost(host, Integer.parseInt(port));
//			HttpGet httpGet = new HttpGet("/v63/插件/version.txt");
//			HttpResponse httpResponse = client.execute(httpHost, httpGet);
//			if(!httpResponse.getStatusLine().getReasonPhrase().equals("OK"))
//			{
//				return;
//			}
//			inputStream = httpResponse.getEntity().getContent();
			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod("http://"+host+":"+port+"/v63/devplugin/version.txt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int httpResponse = client.executeMethod(method);
			inputStream = method.getResponseBodyAsStream();
			content = FileUtilities.fetchFileContent(inputStream, "GBK"); //$NON-NLS-1$
			String newVersion = content.split("\n")[0].split("=")[1].trim(); //$NON-NLS-1$ //$NON-NLS-2$
			
			if(!file.exists()||!newVersion.equals(currentVersion)){
				int type = JOptionPane.showConfirmDialog(null, M_core.ScanNewVersionTask_0+newVersion+M_core.ScanNewVersionTask_2);
				if(type == 0){
					updatePlugins(host,port);
				}
	//					MessageDialog.openConfirm(null, "提示", "是否更新新插件");
			}
			flag = 0;;
		}
		catch(Exception e){
			if(flag == 0){
//				JOptionPane.showMessageDialog(null, "更新出错，请检查数据源");
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
			flag++;
		}
		finally{
			try {
				if(inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
		}
	}
	
	public void updatePatch(String host,String port){
		InputStream inputStream = null;
		final String ncPath = LfwCommonTool.getNCHomePath().toString();	
		try {
//			HttpClient client = new DefaultHttpClient();
//			HttpHost httpHost = new HttpHost(host, Integer.parseInt(port));
//			HttpGet httpGet = new HttpGet("/v63/开发包/patchVersion.txt");
//			HttpResponse httpResponse = client.execute(httpHost, httpGet);
//			if(!httpResponse.getStatusLine().getReasonPhrase().equals("OK"))
//			{
//				MessageDialog.openError(null, "错误", "找不到版本信息");
//				throw new IOException("找不到版本信息");
//			}
//			inputStream = httpResponse.getEntity().getContent();
			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod("http://"+host+":"+port+"/v63/devpack/version.txt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int httpResponse = client.executeMethod(method);
			if(httpResponse != 200){
				JOptionPane.showMessageDialog(null,M_core.ScanNewVersionTask_3);
				throw new IOException(M_core.ScanNewVersionTask_3);
			}
			inputStream = method.getResponseBodyAsStream();
			String content = FileUtilities.fetchFileContent(inputStream, "GBK"); //$NON-NLS-1$
			String newVersion = content.split("\n")[0].split("=")[1].trim(); //$NON-NLS-1$ //$NON-NLS-2$
			final HttpMethod method2 = new GetMethod("http://"+host+":"+port+"/v63/devpack/" + newVersion + "_patch/patch.zip"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			httpResponse = client.executeMethod(method2);
			if(httpResponse != 200){
//				if(flag == 0){
//					JOptionPane.showMessageDialog(null,"没有需要更新的补丁");
//					flag++;
//				}
				throw new IOException(M_core.ScanNewVersionTask_4);
			}
//			final HttpResponse httpResponse2 = client.execute(httpHost, httpGet);
			
			Job job = new Job(M_core.ScanNewVersionTask_5) {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask(M_core.ScanNewVersionTask_6, 2);
					InputStream zipInput = null;
					InputStream zipInput2 = null;
					try {
						zipInput = method2.getResponseBodyAsStream();
						String patchPath = "C:/devPackage/patch.zip"; //$NON-NLS-1$
						FileUtilities.saveFile(patchPath, zipInput);
						monitor.worked(1);

						FileUtilities.unzip(patchPath, ncPath);
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
					JOptionPane.showMessageDialog(null, M_core.ScanNewVersionTask_7);
					WEBProjPlugin.getDefault().logInfo(M_core.ScanNewVersionTask_8);
					patchFlag = true;
					// MessageDialog.openInformation(WEBProjPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
					// "成功", "更新完成，请重启studio");
					return Status.OK_STATUS;
				}

			};
			job.schedule();
		}
		catch (Exception e1) {
			if(patchFlag){
//				JOptionPane.showMessageDialog(null, "更新出错，请检查数据源");
				WEBProjPlugin.getDefault().logError(e1.getMessage(),e1);
				patchFlag = false;
			}
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
			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod("http://"+host+":"+port+"/v63/devplugin/version.txt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int httpResponse = client.executeMethod(method);
			if(httpResponse != 200){
//				JOptionPane.showMessageDialog(null, "找不到版本信息");
				throw new IOException(M_core.ScanNewVersionTask_3);
			}
			inputStream = method.getResponseBodyAsStream();
//			inputStream = httpResponse.getEntity().getContent();
			String content = FileUtilities.fetchFileContent(inputStream, "GBK"); //$NON-NLS-1$
			String newVersion = content.split("\n")[0].split("=")[1].trim(); //$NON-NLS-1$ //$NON-NLS-2$
			final HttpMethod method2 = new GetMethod("http://"+host+":"+port+"/v63/devplugin/" + newVersion + "/plugins.zip"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			httpResponse = client.executeMethod(method2);
			if(httpResponse != 200){
//				JOptionPane.showMessageDialog(null, "找不到更新文件");
				throw new IOException(M_core.ScanNewVersionTask_9);
			}
//			final HttpResponse httpResponse2 = client.execute(httpHost, httpGet);
			// pluginStream = httpResponse.getEntity().getContent();
			// final InputStream zipInput = pluginStream;
			// String idePath =
			// Platform.getInstallLocation().getURL().getPath();
			// String pluginsPath =
			// idePath.substring(0,idePath.lastIndexOf("/"));
			// FileLocator.resolve(WEBProjPlugin.getDefault().getBundle().getEntry("/"))
			Job job = new Job(M_core.ScanNewVersionTask_10) {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask(M_core.ScanNewVersionTask_6, 2);
					InputStream zipInput = null;
					try {
//						zipInput = httpResponse2.getEntity().getContent();
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
					JOptionPane.showMessageDialog(null, M_core.ScanNewVersionTask_11);
					WEBProjPlugin.getDefault().logInfo(M_core.ScanNewVersionTask_8);
					// MessageDialog.openInformation(WEBProjPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
					// "成功", "更新完成，请重启studio");
					return Status.OK_STATUS;
				}

			};
			job.schedule();
		} catch (Exception e1) {
//			JOptionPane.showMessageDialog(null, "更新出错，请检查数据源");
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
	public void updateDevPack(String host,String port) {
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
			HttpMethod method = new GetMethod("http://"+host+":"+port+"/v63/devpack/version.txt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int httpResponse = client.executeMethod(method);
			if(httpResponse != 200){
//				JOptionPane.showMessageDialog(null,"找不到版本信息");
				throw new IOException(M_core.ScanNewVersionTask_3);
			}
			inputStream = method.getResponseBodyAsStream();
			String content = FileUtilities.fetchFileContent(inputStream, "GBK"); //$NON-NLS-1$
			String newVersion = content.split("\n")[0].split("=")[1].trim(); //$NON-NLS-1$ //$NON-NLS-2$
//			httpGet = new HttpGet("/v63/开发包/" + newVersion + "/lfw.zip");
//			final HttpResponse httpResponse2 = client.execute(httpHost, httpGet);
			final HttpMethod method2 = new GetMethod("http://"+host+":"+port+"/v63/devpack/" + newVersion + "/lfw.zip"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			httpResponse = client.executeMethod(method2);
			if(httpResponse != 200){
//				JOptionPane.showMessageDialog(null,"找不到更新文件");
				throw new IOException(M_core.ScanNewVersionTask_9);
			}
			Job job = new Job(M_core.ScanNewVersionTask_5) {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask(M_core.ScanNewVersionTask_6, 2);
					InputStream zipInput = null;
					try {
						zipInput = method2.getResponseBodyAsStream();
						String lfwPath = "C:/devPackage/lfw.zip"; //$NON-NLS-1$
						FileUtilities.saveFile(lfwPath, zipInput);
						monitor.worked(1);

						FileUtilities.unzip(lfwPath, ncPath);
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
					JOptionPane.showMessageDialog(null, M_core.ScanNewVersionTask_12);
					WEBProjPlugin.getDefault().logInfo(M_core.ScanNewVersionTask_8);
					// MessageDialog.openInformation(WEBProjPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
					// "成功", "更新完成，请重启studio");
					return Status.OK_STATUS;
				}

			};
			job.schedule();
		}
		catch (Exception e1) {
//			JOptionPane.showMessageDialog(null, "更新出错，请检查数据源");
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
	

}
