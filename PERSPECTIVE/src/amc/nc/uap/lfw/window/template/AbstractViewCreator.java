package nc.uap.lfw.window.template;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.page.LfwView;

import org.apache.commons.io.IOUtils;


public abstract class AbstractViewCreator implements IViewCreator, ICreatorWithChildren {
	
	protected static final String VIEW_MAIN = "main";
	protected static final String MENUBAR = "menubar";
	protected static final String DATASET_RELATIONS = "ds_relations";
	protected static final String LEFT_RIGHT_PADDING = "20";
	
	private static final String FORM_TEMPLATE = "form.txt";
	private static final String FORM_OPERATOR_TEMPLATE = "formoperator.txt";
	private static final String WFM_FLW_FORM_VO = "WfmFlwFormVO";
	private static final String WFM_FLW_FORM_OPER = "WfmFlwFormOper";
	
	private Map<String, String> extInfo;
	
	@Override
	public void replaceCode(String projPath, LfwView view) {
		String[] fileNames = getTemplateFileName();
		if(fileNames == null || fileNames.length == 0)
			return;
		for (int i = 0; i < fileNames.length; i++) {
			String fileName = fileNames[i];
			doReplace(projPath, view, fileName);
		}
	}
	
	private void doReplace(String projPath, LfwView view, String fileName){
		String str = loadFileStr(fileName);
		if(str == null || str.equals(""))
			return;
		String ctrlClazz = view.getControllerClazz();
		int lastIndex = ctrlClazz.lastIndexOf(".");
		
		String packName = ctrlClazz.substring(0, lastIndex);
		
		String clazzName = ctrlClazz.substring(lastIndex + 1);
		if(FORM_TEMPLATE.equals(fileName)){
			clazzName = WFM_FLW_FORM_VO;
			str = str.replace("${CtxClassToReplace}", clazzName);
		}else if(FORM_OPERATOR_TEMPLATE.equals(fileName)){
			clazzName = WFM_FLW_FORM_OPER;
		}else{
			str = str.replace("${CtxClassToReplace}", WFM_FLW_FORM_VO);
		}
		
		str = str.replace("${ClassToReplace}", clazzName);
		str = str.replace("${PackageToReplace}", packName);
		
		String fullId = this.getExtInfo("FULLID");
		if(fullId != null){
			int lastPointIndex = fullId.lastIndexOf(".");
			if(lastPointIndex >= 0){
				str = str.replace("${CARD_WIN_ID}", fullId.substring(0, lastPointIndex + 1) + "cardwin");
			}else{
				str = str.replace("${CARD_WIN_ID}", "cardwin");
			}
		}
		
		String path = projPath + "/" + view.getSourcePackage() + "/" + packName.replaceAll("\\.", "/") + "/" + clazzName + ".java";
		File f = new File(path);
		OutputStream output = null;
		try{
			output = new FileOutputStream(f);
			IOUtils.write(str, output, "GBK");
			LFWPersTool.refreshCurrentPorject();
		} 
		catch (Exception e) {
			MainPlugin.getDefault().logError(e);
		}
		finally{
			if(output != null){
				IOUtils.closeQuietly(output);
			}
		}
	}

	private String loadFileStr(String fileName) {
		InputStream input = null;
		try {
			input = this.getClass().getResourceAsStream(fileName);
			byte[] b = new byte[1024];
			int count = input.read(b);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while(count > 0){
				out.write(b, 0, count);
				count = input.read(b);
			}
			return out.toString();
		} 
		catch (IOException e) {
			MainPlugin.getDefault().logError(e);
		}
		finally{
			if(input != null)
				IOUtils.closeQuietly(input);
		}
		return null;
	}

	abstract protected String[] getTemplateFileName();
	
	public void addExtInfo(String key, String value){
		if(extInfo == null)
			extInfo = new HashMap<String, String>(2);
		extInfo.put(key, value);
	}
	
	public void setExtInfo(Map<String, String> extInfo){
		this.extInfo = extInfo;
	}
	
	public String getExtInfo(String key){
		if(extInfo == null)
			return null;
		return extInfo.get(key);
	}
	
	protected Integer getChildCount() {
		String countStr = getExtInfo(CHILD_COUNT_KEY);
		if(countStr==null)
			return 0;
		Integer count = Integer.parseInt(countStr);
		return count;
	}
	
}
