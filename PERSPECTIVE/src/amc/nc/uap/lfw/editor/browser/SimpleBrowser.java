package nc.uap.lfw.editor.browser;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Date;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.editor.common.tools.IEEventTranslateToolKit;
import nc.uap.lfw.editor.extNode.SimpleBrowserEditorInput;
import nc.uap.lfw.editor.view.EditorEvent;
import nc.uap.lfw.perspective.webcomponent.LFWAppsNodeTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWFuncTreeItem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;

import sun.misc.BASE64Encoder;


public class SimpleBrowser {

	public final static int WINDOW_URL = 1;
	
	public final static int VIEW_URL = 2;
	
	public final static int PUBLIC_VIEW_URL = 3;
	
	private Browser browser = null;
	
	private String type;
	
	private String url;
	
	private SimpleBrowser simple = null;

	public SimpleBrowser(Composite parent, IEditorInput input){
		
		this.type = ((SimpleBrowserEditorInput)input).getType();
//		if(type.equals("printdesign")||type.equals("wfmDesign"))
//			browser = new Browser(parent, SWT.NONE);
//		else
//			browser = BrowserProvider.getBrowser(parent);
		
		browser = new Browser(parent, SWT.NONE);
		this.url = ((SimpleBrowserEditorInput)input).getUrl();
		simple = this;
	}
	public void createBrowser(){
		browser.setUrl(createURL(type));
		browser.addStatusTextListener(new StatusTextListener(){
			@Override
			public void changed(StatusTextEvent event) {
				if(event.text != null){
					EditorEvent ent = IEEventTranslateToolKit.decode(event.text);
					if(ent != null){
						try {
							Method method = MozillaBrowser.class.getMethod(ent.getEventType(), EditorEvent.class);
							method.invoke(simple, ent);
						} catch (Exception e) {
							MainPlugin.getDefault().logError(e);
						}
					}
					execute("clearEventStatus();");
				}
			}
		});
		browser.addCloseWindowListener(new CloseWindowListener(){
			@Override
			public void close(WindowEvent event) {
//				System.out.println(event.data);
			}
		});
	}
	
	public void execute(String script){
		browser.execute(script);
	}
	
	private String createURL(String type){

		String param = md5encoder("yonyou2012"+timeStr());
		param = URLEncoder.encode(param);
		if(type.equals("node")){
			LFWFuncTreeItem item = (LFWFuncTreeItem)LFWPersTool.getCurrentTreeItem();
			String nodecode = item.getNodecode();
			url = "http://localhost:6688/portal/app/mockapp/cp_funcregister?nodecode=1111020101&lfw_enterParam="+param;
		}
		if(type.equals("menu")){
			url = "http://localhost/portal/app/mockapp/cp_menuitem?nodecode=1109020101&lfw_enterParam="+param;
		}	
		if(type.equals("type")){
			url = "http://localhost/portal/app/mockapp/cp_menucategory?nodecode=1109020101&lfw_enterParam="+param;
		}	
		if(type.equals("wfmDesign")){
			url = "http://localhost:6688"+url+"&lfw_enterParam="+param;
		}
		if(type.equals("QUERY_FOLDER")){
			if(LFWPersTool.getCurrentTreeItem() instanceof LFWAppsNodeTreeItem){
				LFWAppsNodeTreeItem item = (LFWAppsNodeTreeItem)LFWPersTool.getCurrentTreeItem();
				String nodecode = item.getNodecode();
				String metaclass = item.getMetaclass();
				String pk = item.getPk();
				url ="http://localhost:6688/portal/app/queryplanapp/cp_querycondition?nodecode="+nodecode+"&metaclass="+metaclass+"&pk_query_template="+pk+"&lfw_enterParam="+param;		
			}
			else{
				url ="http://localhost:6688/portal/app/queryplanapp?nodecode=1109020101&lfw_enterParam="+param;		
			}
		}
		if(type.equals("PRINT_FOLDER")){
			if(LFWPersTool.getCurrentTreeItem() instanceof LFWAppsNodeTreeItem){
				LFWAppsNodeTreeItem item = (LFWAppsNodeTreeItem)LFWPersTool.getCurrentTreeItem();
				String nodecode = item.getNodecode();
				String metaclass = item.getMetaclass();
				String pk = item.getPk();
				url ="http://localhost:6688/portal/app/printplanapp/cp_printcondition?nodecode="+nodecode+"&metaclass="+metaclass+"&pk_print_template="+pk+"&lfw_enterParam="+param;		
			}
			else{
				url ="http://localhost:6688/portal/app/printplanapp?nodecode=1109020103&lfw_enterParam="+param;		
			}
		}
		if(type.equals("printdesign")){
			if(LFWPersTool.getCurrentTreeItem() instanceof LFWAppsNodeTreeItem){
				LFWAppsNodeTreeItem item = (LFWAppsNodeTreeItem)LFWPersTool.getCurrentTreeItem();
				String filePk = item.getFilePk();
				String nodecode = item.getNodecode();
				String pk = item.getPk();
				String realPath = LfwCommonTool.getUapHome()+"\\";
				String openurl = LFWWfmConnector.getOnlinePrintUrl(pk, nodecode, filePk,realPath)+"&lfw_enterParam="+param;
//				url = "http://localhost:6688/portal"+openurl.substring(openurl.indexOf("/core/word.jsp"));
				url = "http://localhost:6688"+openurl.replace("null", "/portal");
//				String openurl = "/portal/pt/word/online/down?id=" + filePk + "," + pk + "," + nodecode +"&sysid=default";
//				url = "http://localhost:6688/portal/core/word.jsp?pageId=officeedit"+ "&canopen=true&noieopen=true&readonly=false&TrackRevisions=false&savepk="+filePk+"&sysid=default&openurl=" + openurl+"&lfw_enterParam="+param;
			}

		}
		MainPlugin.getDefault().logInfo(url);
		return url;
	}
	
	public String md5encoder(String str)
	{
		MessageDigest md5 = null;
		String value = null;
		try{
			md5 = MessageDigest.getInstance("MD5");
			BASE64Encoder base64Encoder = new BASE64Encoder();
			value = base64Encoder.encode(md5.digest(str.getBytes("UTF-8")));
		}catch(Exception e){
			
		}
		return value;
	}
	public String timeStr(){
		Date date = new Date();
		long time = date.getTime();
		String dateline = time+"";
		dateline = dateline.substring(0,6);
		return dateline;		
	}
}
