package nc.uap.lfw.editor.extNode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;

import org.apache.commons.io.IOUtils;

public class FlwTypeAssistAdjuster {
	
	private static final String[] fileNames = new String[]{"WfmFlwFormOper", "MainViewController", "ListViewController"};
	
	private Application app = null;
	
	private LfwWindow[] windows = null;
	
	private String appId = null;
	
	private String appsNodeId = null;
	
	public FlwTypeAssistAdjuster(String appId, String appsNodeId){
		this.appId = appId;
		this.appsNodeId = appsNodeId;
	}
	
	private void getWindowsByAppId(){
		if(this.appId == null || this.app != null){
			if(this.windows == null){
				this.windows = new LfwWindow[0];
			}
			return;
		}
		
		this.app = LFWAMCConnector.getApplicationById(this.appId);
		if(this.app != null){
			List<WindowConfig> winConfigs = app.getWindowList();
			int size = winConfigs != null ? winConfigs.size() : 0;
			if(size > 0){
				List<LfwWindow> list = new ArrayList<LfwWindow>(size);
				for(WindowConfig winConfig : winConfigs){
					if(winConfig == null || winConfig.getId() == null || winConfig.getId().equals("")){
						continue;
					}
					LfwWindow window = LFWAMCConnector.getWindowById(winConfig.getId());
					if(window != null){
						list.add(window);
					}
				}
				if(list.size() > 0){
					this.windows = list.toArray(new LfwWindow[0]);
				}
			}
		}
		if(this.windows == null){
			this.windows = new LfwWindow[0];
		}
	}
	
	public String getFormOperateClazzName(){
		this.getWindowsByAppId();
		
		String clazz = null;
		for(LfwWindow window : this.windows){
			Serializable value = window.getExtendAttributeValue("CARD_WINDOW");
			if((value instanceof Boolean && (Boolean)value) || (value instanceof String && value.equals("true"))){
				String controllerClazz = window.getControllerClazz();
				if(controllerClazz != null){
					int lastPointIndex = controllerClazz.lastIndexOf(".");
					if(lastPointIndex > 0){
						clazz = controllerClazz.substring(0, lastPointIndex + 1) + "WfmFlwFormOper";
						break;
					}
				}
			}
		}
		return clazz;
	}
	
	public void adjust(String flwType) {
		this.getWindowsByAppId();
		
		for(LfwWindow window : this.windows){
			for(int i = 0; i < fileNames.length; i++){
				String ctrlClazz = window.getControllerClazz();
				int lastIndex = ctrlClazz.lastIndexOf(".");
				String packName = ctrlClazz.substring(0, lastIndex);
				String clazzName = fileNames[i];
				
				String projPath = LFWAMCPersTool.getLFWProjectPath();
				
				String path = projPath + "/" + window.getSourcePackage() + "/" + packName.replaceAll("\\.", "/") + "/" + clazzName + ".java";
				
				String str = loadFileStr(path);
				
				if(str == null){
					continue;
				}
				
				str = str.replace("${AppidToReplace}", this.appId);
				str = str.replace("${WindowidToReplace}", window.getFullId());
				str = str.replace("${NodeCode}", this.appsNodeId);
				str = str.replace("${FlwCode}", flwType);
				
				File f = new File(path);
				if(f.exists() && f.isFile()){
					OutputStream output = null;
					try{
						output = new FileOutputStream(f);
						IOUtils.write(str, output, "GBK");
					}catch (Exception e) {
						MainPlugin.getDefault().logError(e);
					}finally{
						if(output != null){
							IOUtils.closeQuietly(output);
						}
					}
				}
			}
		}
		LFWPersTool.refreshCurrentPorject();
	}
	
	private String loadFileStr(String fileName) {
		InputStream input = null;
		try {
			File file = new File(fileName);
			if(file.exists() && file.isFile()){
				input = new FileInputStream(fileName);
				byte[] b = new byte[1024];
				int count = input.read(b);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				while(count > 0){
					out.write(b, 0, count);
					count = input.read(b);
				}
				return out.toString();
			}
		}catch (IOException e) {
			MainPlugin.getDefault().logError(e);
		}finally{
			if(input != null){
				IOUtils.closeQuietly(input);
			}
		}
		return null;
	}
	
}
