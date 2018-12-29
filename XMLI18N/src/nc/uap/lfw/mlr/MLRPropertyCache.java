package nc.uap.lfw.mlr;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import nc.uap.lfw.lang.M_mlr;
import nc.uap.lfw.plugin.Activator;
import nc.uap.lfw.tool.Helper;
import nc.uap.lfw.tool.ProjConstants;
import nc.uap.lfw.tool.UTFProperties;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;

/**
 *
 * 多语资源缓存
 * @author dingrf
 *
 */
public class MLRPropertyCache{

	/**
	 * 多语资源MAP
	 * key: 语言分类+模块名称
	 * value: MAP(key:文件名,value:文件中的多语资源)
	 */
	private Map<String,Map<String,UTFProperties>> propMap;
	
	private IProject project;
	
	/**资源文件根路径*/
	private String resHomePath;

	public MLRPropertyCache(IProject project, String resHomePath){
		propMap = new HashMap<String,Map<String,UTFProperties>>();
		this.project = project;
		this.resHomePath = resHomePath;
	}

	/**
	 * 根据资源id取多语资源
	 * 
	 * @param langCode
	 * @param langClass
	 * @param resID
	 * @return
	 */
	public MLRes findLocalMLRes(String bcpName,String langCode, String langClass, String resID){
		langClass = langClass.toLowerCase();
		Map<String,UTFProperties> props = (Map<String,UTFProperties>)propMap.get((new StringBuilder(langCode)).append(langClass).toString());
		if (props == null){
			props = getMLRprop(bcpName,langCode,langClass);
			if(props.size()==0)
				return null;
			propMap.put((new StringBuilder(langCode)).append(langClass).toString(), props);
		}
		MLRes res = null;
		for (Iterator<String> iter = props.keySet().iterator(); iter.hasNext();){
			String fileName = (String)iter.next();
			UTFProperties prop = (UTFProperties)props.get(fileName);
			if (prop.containsKey(resID)){
				res = new MLRes(fileName, resID, prop.getProperty(resID));
				break;
			}
		}
		return res;
	}
	
	/**
	 * 取某一模块的多语资源
	 * 
	 * @param langCode
	 * @param langClass
	 * @return
	 */
	public List<MLRes> findLocalMLResList(String bcpName,String langCode, String langClass){
		Map<String,UTFProperties> props = new HashMap<String,UTFProperties>();
		List<MLRes> mlrList = new ArrayList<MLRes>();
		props = getMLRprop(bcpName,langCode,langClass);
		for (Iterator<String> iter = props.keySet().iterator(); iter.hasNext();){
			String fileName = (String)iter.next();
			UTFProperties prop = (UTFProperties)props.get(fileName);
			for (Iterator<String> it = prop.keySet().iterator(); it.hasNext();){
				String resID = (String)it.next();
				MLRes res = new MLRes(fileName, resID, (String)prop.get(resID));
				mlrList.add(res);
			}
		}
		return mlrList; 
		
	}
	
	/**
	 * 加载某一模块的多语资源MAP
	 * 
	 * @param bpcName 业务组件
	 * @param langCode
	 * @param langClass
	 * @return
	 */
	private Map<String,UTFProperties> getMLRprop(String bcpName,String langCode, String langClass){
		Map<String,UTFProperties> props = new HashMap<String,UTFProperties>();
		IFolder folder = null;
		if (bcpName == null || bcpName.equals("")) //$NON-NLS-1$
			folder = project.getFolder((new StringBuilder(resHomePath)).append("/" + ProjConstants.LANG + "/").append(langCode).append("/").append(langClass).toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		else
//			folder = project.getFolder((new StringBuilder(resHomePath)).append("/"+bcpName+"/" + ProjConstants.LANG + "/").append(langCode).append("/").append(langClass).toString());
			folder = project.getFolder((new StringBuilder(resHomePath)).append("/" + ProjConstants.LANG + "/").append(langCode).append("/").append(langClass).toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		if (folder.exists())
			try{
				IResource childs[] = folder.members();
				int count = childs != null ? childs.length : 0;
				for (int i = 0; i < count; i++){
					IResource res = childs[i];
					if ((res instanceof IFile) && res.getFileExtension().equalsIgnoreCase(M_mlr.MLRPropertyCache_0)){
						IFile localfile = (IFile)res;
						java.io.InputStream in = null;
						if (localfile.exists())
							try{
								in = localfile.getContents();
							}
							catch (CoreException e){
								Activator.getDefault().logError(e.getMessage(), e);
							}
						if (in != null){
							in = new BufferedInputStream(in);
							try{
								UTFProperties prop = new UTFProperties(localfile.getCharset());
								prop.load(in);
								props.put(localfile.getName(), prop);
							}
							catch (IOException e){
								Activator.getDefault().logError(e.getMessage(), e);
							}
						}
					}
				}
			}
			catch (CoreException e){
				Activator.getDefault().logError(e.getMessage(), e);
			}
		return props;
	}
	
	/**
	 * 寻找该langdir的资源文件夹是否在业务组件中存在
	 * @param bcpName
	 * @param langCode
	 * @param langClass
	 * @return
	 */
	public Boolean findLangDir(String bcpName,String langCode,String langClass){
		IFolder folder = null;
		if (bcpName != null && !bcpName.equals("")){ //$NON-NLS-1$
			folder = project.getFolder((new StringBuilder(resHomePath)).append("/" + ProjConstants.LANG + "/").append(langCode).append("/").append(langClass).toString());					
			if (folder.exists())
				return true;
		}
		return false;
	}
	
	/**
	 * 保存common多语资源
	 * 
	 * @param langCode
	 * @param resList
	 */
	public void saveCommMLRes(String bcpName,String langCode,List<MLRes> resList){
		String langClass = M_mlr.MLRPropertyCache_1;
		String fileName = M_mlr.MLRPropertyCache_2;
		IFolder folder = null;
		if (bcpName==null ||bcpName.equals("")) //$NON-NLS-1$
			folder = project.getFolder((new StringBuilder(resHomePath)).append("/").append(ProjConstants.LANG).append("/").append(langCode).append("/").append(langClass).toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		else
			folder = project.getFolder((new StringBuilder(resHomePath)).append("/").append(ProjConstants.LANG).append("/").append(langCode).append("/").append(langClass).toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
		ByteArrayInputStream stream = null;
		try {
			if (!folder.exists())
				Helper.createFolder(folder);
//				folder.create(true, true, null);
			
			IFile file = project.getFile((new StringBuilder(resHomePath)).append("/").append(ProjConstants.LANG).append("/").append(langCode).append("/").append(langClass).toString() + "/" + fileName); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			StringWriter swriter = new StringWriter();
			PrintWriter writer = new PrintWriter(swriter);
			for(MLRes m : resList){
				writer.println(m.getResID() + "=" + m.getValue()); //$NON-NLS-1$
			}
			String initContent = swriter.getBuffer().toString();
			stream = new ByteArrayInputStream(initContent.getBytes());
			if (file.exists()){
				file.setContents(stream, false, false, null);
			}
			else{
				file.create(stream, false, null);
			}
		} catch (Exception e) {
			Activator.getDefault().logError(e.getMessage(), e);
		}finally{
			if (stream!=null)
				try{
					stream.close();
				}catch(Exception e){}
		}
	}
}
