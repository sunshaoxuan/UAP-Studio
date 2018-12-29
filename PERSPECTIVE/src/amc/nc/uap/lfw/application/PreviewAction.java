package nc.uap.lfw.application;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Date;

import sun.misc.BASE64Encoder;

import nc.lfw.design.view.LFWWfmConnector;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.lfw.core.SmartModeDetector;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;

public class PreviewAction extends NodeAction{

	public PreviewAction(){
		super(WEBPersConstants.PREVIEW_APPLICATION, WEBPersConstants.PREVIEW_APPLICATION);
	}
	public void run() {
		String id = LFWAMCPersTool.getCurrentApplication().getId();
		String wid= LFWAMCPersTool.getCurrentApplication().getDefaultWindowId();
		try {
			
			CpAppsNodeVO[] funcNodes = LFWWfmConnector.getAppsNodeVOsByCondition("appid = '"+id+"'");
			String nodeCode = null;
			if(funcNodes!=null&&funcNodes.length>0){
				nodeCode = funcNodes[0].getId();
			}
			String param = md5encoder("yonyou2012"+timeStr());
			param = URLEncoder.encode(param);
//			Runtime.getRuntime().exec("explorer.exe http://localhost/portal/app/"+id);
			String preUrl = "http://localhost/portal/app/";
//			if(SmartModeDetector.isSmartMode()){
//				preUrl = "http://localhost:6688/portal/app/";
//			}
			String url = preUrl+id+"?lfw_enterParam="+param;
			if(nodeCode!=null){
				url = preUrl+id+"?nodecode="+nodeCode+"&lfw_enterParam="+param;
			}
			ProcessBuilder builder = new ProcessBuilder("C:\\Program Files\\Internet Explorer\\iexplore",url);
//			ProcessBuilder builder = new ProcessBuilder("C:\\Program Files\\Internet Explorer\\iexplore","http://localhost/portal/app/mockapp/"+wid+"?lfw_enterParam="+param);
			builder.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			WEBPersPlugin.getDefault().logError(e.getMessage());
		}
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
