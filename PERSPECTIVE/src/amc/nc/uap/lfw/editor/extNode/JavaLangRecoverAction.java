/**
 * 
 */
package nc.uap.lfw.editor.extNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;

import org.eclipse.core.resources.IProject;


/**
 * @author guomq1
 *
 */
public class JavaLangRecoverAction extends NodeAction {
	static int mount =0;
	public JavaLangRecoverAction(){
		super(WEBPersConstants.LANG_RECOVER);
	}
	public void run() {
		openLangRecoverTool();
	}
	private void openLangRecoverTool() {
		try {
			LFWDirtoryTreeItem treeItem =(LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem();
			String path = treeItem.getFile().getPath();
			String bcppath = LFWPersTool.getBcpPath(treeItem);
			IProject project = LFWPersTool.getCurrentProject();
			String bcp = LFWPersTool.getBcpProject().toString();
			String resourceName = null;
			File file = treeItem.getFile();
			String langMark = "NCLangRes4VoTransl.getNCLangRes().getStrByID";
	        TraverseFile(file,langMark);       
			project.refreshLocal(2, null);
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
	}
	
	
	//遍历BCP工程下的所有.JAVA文件	
	private void TraverseFile(File file, String langMark) {
		File[] files = file.listFiles();
		for(int i =0;i<files.length;i++){
			if(files[i].isDirectory()){
				TraverseFile(files[i],langMark);
			}else if(isJavaFile(files[i])){
				try {
					SearchFile(files[i],langMark);
				} catch (IOException e) {
					MainPlugin.getDefault().logError(e.getMessage(),e);
				}
//				Replace(files[i]);
			}
		}
	}
	/*
	private void Replace(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file.getCanonicalPath());
			try {
				byte[] buf = new byte[fis.available()];
				fis.read(buf,0,fis.available());
				String content = new String(buf);
				Pattern p = Pattern.compile("(public.*String.*=)[^;]*\\/\\*{.*}\\*\\/\\s*;");
				java.util.regex.Matcher m =p.matcher(content);
				StringBuffer sb = new StringBuffer();
				boolean result = m.find();
				while(result){
					String rel = m.group(1)+"\""+m.group(2)+"\";";
					m.appendReplacement(sb, rel);
					result = m.find();
				}
			} catch (IOException e) {
				MainPlugin.getDefault().logError(e.getMessage(),e);
			}
			
		} catch (FileNotFoundException e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		} catch (IOException e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
		finally{
			try {
				if(fis != null)
					fis.close();
			} catch (IOException e) {
				MainPlugin.getDefault().logError(e.getMessage(),e);
			}
		}
		
	}
	*/
	
	//判断是否是.java文件
	private boolean isJavaFile(File file) {
    if(file.exists()&&file.canWrite()&&file.canExecute()&&file.canRead()&&file.getName().toString().endsWith(".java"))return true;
		return false;
	}

	
	//在文件file中找字符串langMark
	private void SearchFile(File file, String langMark) throws IOException {
		int count = 0,j=0;
		String searchStr = "";
		FileReader isr = null;
//		BufferedReader reader =null;
		try {
			isr = new FileReader(file);

			int index = 0;
			while((index = isr.read())!=-1){
				searchStr+=(char)index;
			}
			if(searchStr!=null){
				while(searchStr.indexOf(langMark,j)!=-1){
					count+=1;
					searchStr = LangRecover(searchStr,langMark);
	
					j=searchStr.indexOf(langMark, j);
	
	
	
				}
			}
			if(count>0){
				mount++;
				MainPlugin.getDefault().logInfo("在"+file.getCanonicalPath()+"有"+count+"处多语需要复原");
				
				//将复原后的内容写入文件
				FileOutputStream fos = null;
				Writer out = null;
				try{
					fos = new FileOutputStream(file.getCanonicalPath());
					out = new OutputStreamWriter(fos,"GBK");
					out.write(searchStr);
					out.close();
				    fos.close();
				}
				catch(Exception e){
					MainPlugin.getDefault().logError(e.getMessage(),e);
				}
				finally{
					try{
						if(fos != null)
							fos.close();
					}catch(Exception e){
						MainPlugin.getDefault().logError(e.getMessage(),e);
					}
					try{
						if(out != null)
							out.close();
					}catch(Exception e){
						MainPlugin.getDefault().logError(e.getMessage(),e);
					}
				}
			}
		}
		catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
		finally{
			if(isr != null)
				isr.close();
		}
	}
	//多语复原操作
	private String LangRecover(String searchStr, String langMark) {
		int j =searchStr.indexOf(langMark)+langMark.length();
		int k=j;
		String beReplace ="";
		while(!beReplace.endsWith("*/")){
			k++;
			beReplace=langMark+searchStr.substring(j, k);
			}
		int bindex = beReplace.indexOf("/*");
		int eindex = beReplace.indexOf("*/");
		String Replace ="\""+beReplace.substring(bindex+2,eindex)+"\"";
		StringBuffer sb = new StringBuffer();
		searchStr =searchStr.replace(beReplace, Replace);		
		MainPlugin.getDefault().logInfo(searchStr);
		return searchStr;
	}
/*	private String trim(String str){
		StringBuffer buffer = new StringBuffer(str);
		final String compare = "NCLangRes4VoTransl";
		
		int j = buffer.indexOf(compare);
		while(j!=-1){
			
		}
		return buffer.toString();
	}*/

}