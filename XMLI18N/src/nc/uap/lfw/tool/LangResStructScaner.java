package nc.uap.lfw.tool;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import nc.uap.lfw.plugin.Activator;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;

/**
 * 扫描xml多语资源文件
 * 
 * @author dingrf
 *
 */
public class LangResStructScaner{

	private IProject project;
	private IFolder ncHome;
	private static final String LANGRES_JAR_SUFFIX = "_langres.jar";

	public LangResStructScaner(IProject project){
		this.project = project;
		ncHome = project.getFolder("NC_HOME");
	}

	private String[] findLangResJarFile(){
		HashSet<String> set = new HashSet<String>();
		if (ncHome != null && ncHome.exists()){
			IFolder langlib = ncHome.getFolder("langlib");
			IResource childs[] = (IResource[])null;
			try{
				childs = langlib.members();
			}
			catch (CoreException e){
				Activator.getDefault().logError(e.getMessage(), e);
			}
			int count = childs != null ? childs.length : 0;
			for (int i = 0; i < count; i++){
				IResource res = childs[i];
				if ((res instanceof IFile) && res.getName().endsWith(LANGRES_JAR_SUFFIX))
					set.add(res.getLocation().toFile().getAbsolutePath());
			}
		}
		return (String[])set.toArray(new String[0]);
	}

	private HashSet<String> findLangResFileInJarFile0(String matchModel, boolean onlyFileName){
		HashSet<String> set = new HashSet<String>();
		String jarFilePaths[] = findLangResJarFile();
		for (int i = 0; i < jarFilePaths.length; i++)
			try{
				ZipFile zip = new ZipFile(jarFilePaths[i]);
				for (Enumeration<? extends ZipEntry> enumer = zip.entries(); enumer.hasMoreElements();){
					ZipEntry entry = (ZipEntry)enumer.nextElement();
					if (!entry.isDirectory()){
						String name = entry.getName();
						if (name.matches(matchModel) && name.toLowerCase().endsWith(".properties"))
							if (onlyFileName){
								int index = name.lastIndexOf('/');
								set.add(name.substring(index + 1));
							} else{
								set.add(name);
							}
					}
				}
			}
			catch (Exception e){
				Activator.getDefault().logError(e.getMessage(), e);
			}
		return set;
	}

	/**
	 * 通过多语模块查找资源文件
	 * 
	 * @param resourceHomePath
	 * @param langCode
	 * @param langClass
	 * @return
	 */
	public String[] findPropFilesByLangClass(String resourceHomePath, String langCode, String langClass){
		HashSet<String> set = findLangResFileInJarFile0((new StringBuilder(".*lang/")).append(langCode).append("/").append(langClass).append("/.*").toString(), true);
		IFolder dir = project.getFolder((new StringBuilder(resourceHomePath)).append("/").append(ProjConstants.LANG).append("/").append(langCode).append("/").append(langClass).toString());
		if (dir.exists()){
			IResource childs[] = (IResource[])null;
			try{
				childs = dir.members();
			}
			catch (CoreException e){
				Activator.getDefault().logError(e.getMessage(), e);
			}
			int count = childs != null ? childs.length : 0;
			for (int i = 0; i < count; i++){
				IResource res = childs[i];
				if ((res instanceof IFile) && res.getFileExtension().equalsIgnoreCase("properties"))
					set.add(res.getName());
			}
		}
		String strs[] = (String[])set.toArray(new String[0]);
		return strs;
	}

	private HashSet<String> findLangModulesInJarFile0(String langCode){
		String matchModel = (new StringBuilder(".*lang/")).append(langCode).append(".*").toString();
		String prefix = (new StringBuilder(ProjConstants.LANG + "/")).append(langCode).append("/").toString();
		HashSet<String> set = new HashSet<String>();
		String jarFilePaths[] = findLangResJarFile();
		for (int i = 0; i < jarFilePaths.length; i++)
			try{
				ZipFile zip = new ZipFile(jarFilePaths[i]);
				addZipFile(matchModel,zip,prefix,set);
			}
			catch (Exception e){
				Activator.getDefault().logError(e.getMessage(), e);
			}

		return set;
	}
	
	private void addZipFile(String matchModel,ZipFile zip,String prefix,HashSet<String> set){
		for (Enumeration<? extends ZipEntry> enumer = zip.entries(); enumer.hasMoreElements();){
			ZipEntry entry = (ZipEntry)enumer.nextElement();
			if (entry.isDirectory()){
				String name = entry.getName();
				if (name.matches(matchModel)){
					if (name.endsWith("/"))
						name = name.substring(0, name.length() - 1);
					int index = name.lastIndexOf(prefix);
					if (index != -1 && name.length() > index + prefix.length()){
						String str = name.substring(index + prefix.length());
						set.add(str);
					}
				}
			}
		}
	}

	public String[] findLangMoudles(String resourceHomePath, String langCode){
		HashSet<String> set = findLangModulesInJarFile0(langCode);
		IFolder dir = project.getFolder((new StringBuilder(String.valueOf(resourceHomePath))).append("/"+ProjConstants.LANG+"/").append(langCode).toString());
		if (dir.exists()){
			IResource childs[] = (IResource[])null;
			try{
				childs = dir.members();
			}
			catch (CoreException e){
				Activator.getDefault().logError(e.getMessage(), e);
			}
			int count = childs != null ? childs.length : 0;
			for (int i = 0; i < count; i++){
				IResource res = childs[i];
				if (res instanceof IFolder)
					set.add(res.getName());
			}

		}
		String strs[] = (String[])set.toArray(new String[0]);
		Helper.sortStrings(strs);
		return strs;
	}

	/**
	 * 查找多语资源类别
	 * 
	 * @param resourceHomePath	项目路径
	 * @param langCode	语言类型
	 * @param includeJar 是否包含jar文件
	 * @return
	 */
	public String[] findLangMoudles(String resourceHomePath, String langCode, boolean includeJar){
		HashSet<String> set = null;
		if (includeJar)
			set = findLangModulesInJarFile0(langCode);
		else
			set = new HashSet<String>();
		IFolder dir = project.getFolder((new StringBuilder(resourceHomePath)).append("/"+ProjConstants.LANG+"/").append(langCode).toString());
		if (dir.exists()){
			IResource childs[] = (IResource[])null;
			try{
				childs = dir.members();
			}
			catch (CoreException e){
				Activator.getDefault().logError(e.getMessage(), e);
			}
			int count = childs != null ? childs.length : 0;
			for (int i = 0; i < count; i++){
				IResource res = childs[i];
				if (res instanceof IFolder)
					set.add(res.getName());
			}

		}
		String strs[] = (String[])set.toArray(new String[0]);
		Helper.sortStrings(strs);
		return strs;
	}
}
