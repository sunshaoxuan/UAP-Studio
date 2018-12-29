package uap.lfw.smartmw;

import java.lang.reflect.Method;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.design.view.ClassPathProvider;
import nc.uap.lfw.util.LfwClassUtil;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public class TomcatEmbedServer implements IEmbedServer{

	private static final String PORTAL = "/portal";
	private static final String LFW = "/lfw";
//	private static TomcatEmbedServer instance = new TomcatEmbedServer();
	private Tomcat tomcat;
	
	public static boolean isStarted = false;
	//0 not started  //1 starting //2 started
	public TomcatEmbedServer(){
	}
	
	public void stop() {
		try {
			tomcat.stop();
			tomcat.destroy();
		}
		catch(Exception e){
			MainPlugin.getDefault().logError(e);
		}
	}

	
	public void start() {
		String path = System.getProperty("user.dir") + "/smartmv";
		
		//创建嵌入式Tomcat Server的实例
		tomcat = new Tomcat();
//		//设置Tomcat的工作目录
		tomcat.setBaseDir(path);
		tomcat.setPort(6688);
		try {
			ClassLoader commonLoader = Thread.currentThread().getContextClassLoader();
			
			startApp(commonLoader);
			
			String webBase = LfwCommonTool.getUapHome() + "/hotwebs";
			String lfwPath = webBase + LFW;
			String portalPath = webBase + PORTAL;
			Context lfwCtx = tomcat.addWebapp(LFW, lfwPath);
			lfwCtx.setParentClassLoader(commonLoader);
			
			Context portalCtx = tomcat.addWebapp(PORTAL, portalPath);
			portalCtx.setParentClassLoader(commonLoader);
			
			tomcat.start();
			ClassPathProvider.setClassLoader(commonLoader);
			
			afterStart(commonLoader);
			isStarted = true;
		} 
		catch (Throwable e) {
			MainPlugin.getDefault().logError(e);
			stop();
		}

	}
	



	private void afterStart(ClassLoader commonLoader)  throws Exception{
		Class c = LfwClassUtil.forName("uap.lfw.smartmw.SmartServer", commonLoader);
		Object serverStart = c.newInstance();
		Method m = c.getMethod("afterStartUp", new Class[]{});
		m.invoke(serverStart, null);
	}

	private void startApp(ClassLoader commonLoader) throws Exception{
		Class c = Class.forName("uap.lfw.smartmw.SmartMwStartup", true, commonLoader);
		Object serverStart = c.newInstance();
		Method m = c.getMethod("startup", new Class[]{});
		m.invoke(serverStart, null);
	}

//	private ClassLoader getClassLoader(ClassLoader pc) {
//		URL[] urls = LoaderHelper.getAllUrls(StartMode.SMART);
//		URLClassLoader loader = new URLClassLoader(urls, pc);
//		return loader;
//	}

}
