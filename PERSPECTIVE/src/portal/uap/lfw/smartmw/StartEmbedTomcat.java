package uap.lfw.smartmw;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.util.LfwClassUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import uap.lfw.core.factory.LfwMultiSysFactory;
import uap.lfw.core.ml.LfwResBundle;
import uap.lfw.lang.M_smartmw;


public class StartEmbedTomcat {
	public static final String DEV_LFW_PROJECTS = "dev.lfw.projects";
	private static IEmbedServer server = null;
	public static void start(){
		
		Job job = new Job(M_smartmw.StartEmbedTomcat_0000/*进度显示*/) {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(M_smartmw.StartEmbedTomcat_0001/*WEB服务正在加载中*/, IProgressMonitor.UNKNOWN);
				webServiceRun();
				monitor.done();
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
		
	}
	private static void webServiceRun(){
		MwLogger.logInfo("starting smart server ......");
		initEnv();
		ClassLoader currentCL = Thread.currentThread().getContextClassLoader();
		try{
			ClassLoader commonLoader = getClassLoader(StartEmbedTomcat.class.getClassLoader());
			Thread.currentThread().setContextClassLoader(commonLoader);
			try {
				Method m = LfwMultiSysFactory.class.getDeclaredMethod("newInstance", new Class[]{});
				m.setAccessible(true);
				m.invoke(null, null);
				
//				m = ServiceLocator.class.getDeclaredMethod("newInstance", new Class[]{});
//				m.setAccessible(true);
//				m.invoke(null, null);
				
				m = LfwResBundle.class.getDeclaredMethod("newInstance", new Class[]{});
				m.setAccessible(true);
				m.invoke(null, null);
				
				SmartServiceProvider.createInstance();
				
			}
			catch (Exception e1) {
				MwLogger.log(e1);
			}
			
			String ncHomePath = LfwCommonTool.getUapHome();
			LfwMultiSysFactory.getMultiSysFactory().getRuntimeEnv().setProperty("nc.server.location", ncHomePath);
			Object obj = LfwClassUtil.newInstance("uap.lfw.smartmw.SmartLocator", commonLoader);
			Method im = obj.getClass().getMethod("inject", new Class[]{});
			
			im.invoke(null, null);
			server = new TomcatEmbedServer();
			server.start();
		}
		catch(Exception e){
			MwLogger.log(e);
		}
		finally{
			Thread.currentThread().setContextClassLoader(currentCL);
		}
//		TomcatEmbedServer.getInstance().start();
		MwLogger.logInfo("smart server started......");
	}
	
	private static void initEnv() {
		String ncHomePath = LfwCommonTool.getUapHome();
		System.setProperty("lfw_sys_type", "SMART");
		System.setProperty("nc.runMode", "develop");
		System.setProperty("nc.lfw.location", ncHomePath);
		System.setProperty("nc.run.side", "server");
		System.setProperty("nc.server.name", "server");
		String projs = getLfwProjects();
		System.setProperty(DEV_LFW_PROJECTS, projs);
		
	}
	
	private static String getLfwProjects() {
		StringBuffer projBuf = new StringBuffer();
		IProject[] projects = LfwCommonTool.getLfwProjects();
		for (int i = 0; i < projects.length; i++) {
			IProject proj = projects[i];
			projBuf.append(proj.getLocation().toOSString());
			if(i != projects.length - 1)
				projBuf.append(",");
		}
		return projBuf.toString();
	}

	public static void restart() {		
		if(server != null)
			server.stop();
		start();

	}
	
	public static void stop() {
		if(server != null)
			server.stop();
	}
	
	private static ClassLoader getClassLoader(ClassLoader pc) {
		URL[] urls = LoaderHelper.getAllUrls(StartMode.SMART);
		URLClassLoader loader = new URLClassLoader(urls, pc);
		return loader;
	}
	
}
