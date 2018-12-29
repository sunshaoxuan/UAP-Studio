package nc.uap.lfw.tool;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nc.uap.lfw.plugin.Activator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.Region;

/**
 * Wizard辅助类
 * 
 * @author dingrf
 *
 */
public class Helper{
	
	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmptyString(String str) {
		return (str == null) || (str.trim().length() == 0);
	}
  
	/**
	 * 取换行符
	 * 
	 * @return
	 */
	public static String getLineDelimiter() {
		String lineDelimiter = System.getProperty("line.separator");
		if (isEmptyString(lineDelimiter)) {
			lineDelimiter = "\r\n";
		}
		return lineDelimiter;
	}

	/**
	 * 转换字符串中的双引号
	 * 
	 * @param value
	 * @return
	 */
	public static String dealWithQuote(String value) {
		if (value == null)
			return value;
		StringBuilder sb = new StringBuilder();
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char ch = chars[i];
			if ((ch == '"') && ((i == 0) || (chars[(i - 1)] != '\\'))) {
				sb.append('\\');
			}
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * 取资源文件
	 * 
	 * @param project
	 * @param resourceHomePath
	 * @param langCode
	 * @param moduleName
	 * @param resFileName
	 * @return
	 */
	public static IFile getResBoundleFile(IProject project,
			String resourceHomePath, String langCode, String moduleName,
			String resFileName) {
		resFileName = getResFileNameWithSuffix(resFileName);

		IFolder resRootFolder = project.getFolder(resourceHomePath);
		try {
			resRootFolder.refreshLocal(2, null);
		} catch (CoreException e1) {
			Activator.getDefault().logError(e1.getMessage(), e1);
		}
		IFolder moduleFolder = resRootFolder.getFolder(ProjConstants.LANG + "/"
				+ langCode + "/" + moduleName);

		IFile resFile = moduleFolder.getFile(resFileName);
		resFile.exists();

		return resFile;
	}

	/**
	 * 获取资源文件全名称
	 * 
	 * @param resFileName
	 * @return
	 */
	private static String getResFileNameWithSuffix(String resFileName) {
		if (resFileName.toLowerCase().endsWith(".properties")) {
			return resFileName;
		}
		return resFileName + ".properties";
	}

  /**
   * 创建资源文件
   * 
   * @param res
   * @throws CoreException
   */
	public static void createResource(IResource res) throws CoreException {
		IContainer parent = res.getParent();
		if (!parent.exists()) {
			createResource(parent);
		}
		if ((res instanceof IFolder)) {
			IFolder folder = (IFolder) res;
			folder.create(true, true, null);
		} else if ((res instanceof IFile)) {
			IFile file = (IFile) res;
			file.create(new ByteArrayInputStream(new byte[0]), true, null);
		}
	}

	public static String getLineDelimiterPreference(IProject project) {
		if (project != null) {
			IScopeContext[] scopeContext = { new ProjectScope(project) };
			String lineDelimiter = Platform.getPreferencesService().getString(
					"org.eclipse.core.runtime", "line.separator", null,
					scopeContext);
			if (lineDelimiter != null) {
				return lineDelimiter;
			}
		}
		IScopeContext[] scopeContext = { new InstanceScope() };
		String platformDefault = System.getProperty("line.separator", "\n");
		return Platform.getPreferencesService().getString(
				"org.eclipse.core.runtime", "line.separator", platformDefault,
				scopeContext);
	}

	/**
	 * 去除字符串两端的双引号
	 * 
	 * @param str
	 * @return
	 */
	public static String stripQuotes(String str) {
		if (str == null)
			return str;
		if ((str.startsWith("\"")) && (str.endsWith("\"")) && (str.length() >= 2)) {
			return str.substring(1, str.length() - 1);
		}
		return str;
	}

	public static HashMap<String, UTFProperties> getSimpchnMLResPropsHM(
			IProject project, String resourceHomePath, List<String> moduleList) {
		return getMLResPropsHM(project, "simpchn", resourceHomePath, moduleList);
	}

	public static HashMap<String, UTFProperties> getMLResPropsHM(
			IProject project, String langCode, String resourceHomePath,
			List<String> moduleList) {
		HashMap<String, UTFProperties> hm = new HashMap<String, UTFProperties>();
		LangResStructScaner scaner = new LangResStructScaner(project);
		URLClassLoader loader = ClassTool.getURLClassLoader(project);
		String charsetName = "GBK";
		if ("tradchn".equals(langCode)) {
			charsetName = "UTF-16";
		}
		for (int i = 0; i < moduleList.size(); i++) {
			String moduleName = (String) moduleList.get(i);
			moduleName = moduleName.toLowerCase();
			String[] fileNames = scaner.findPropFilesByLangClass(
					resourceHomePath, langCode, moduleName);

			UTFProperties properties = new UTFProperties(null);
			int count = fileNames == null ? 0 : fileNames.length;
			for (int j = 0; j < count; j++) {
				String path = ProjConstants.LANG + "/" + langCode + "/"
						+ moduleName + "/" + fileNames[j];
				IFile localfile = project
						.getFile(resourceHomePath + "/" + path);
				InputStream in = null;
				if (localfile.exists())
					try {
						in = localfile.getContents();
						charsetName = localfile.getCharset();
					} catch (CoreException e) {
						Activator.getDefault().logError(e.getMessage(), e);
					}
				else {
					in = loader.getResourceAsStream(path);
				}

				if (in != null) {
					in = new BufferedInputStream(in);
					try {
						properties.load(in, charsetName);
					} catch (IOException e) {
						Activator.getDefault().logError(e.getMessage(), e);
					}
				}
			}
			hm.put(moduleName, properties);
		}
		return hm;
	}

	/**
	 * 根据值在UTFProperties中取key
	 * 
	 * @param prop
	 * @param value
	 * @return
	 */
	public static String getKeyByValue(UTFProperties prop, String value) {
		if ((prop != null) && (prop.containsValue(value))) {
			Iterator<String> iter = prop.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				String v = prop.getProperty(key);
				if (v.equals(value)) {
					return key;
				}
			}
		}
		return null;
	}

	/***
	 * 两个set内容比较
	 * 
	 * @param set1
	 * @param set2
	 * @return
	 */
	public static boolean isEquals(Set<String> set1, Set<String> set2) {
		if (set1.size() != set2.size()) {
			return false;
		}
		String[] strs1 = (String[]) set1.toArray(new String[0]);
		String[] strs2 = (String[]) set2.toArray(new String[1]);
		Comparator<Object> comp = new Comparator<Object>() {
			public int compare(String arg0, String arg1) {
				return arg0.compareTo(arg1);
			}

			public int compare(Object obj, Object obj1) {
				return compare((String) obj, (String) obj1);
			}

		};
		Arrays.sort(strs1, comp);
		Arrays.sort(strs2, comp);
		for (int i = 0; i < strs1.length; i++) {
			if (!strs1[i].equals(strs2[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 字符串特殊字符处理
	 * 
	 * @param s
	 * @return
	 */
	public static String unwindEscapeChars(String s) {
		if (s != null) {
			StringBuffer sb = new StringBuffer(s.length());
			int length = s.length();
			for (int i = 0; i < length; i++) {
				char c = s.charAt(i);
				sb.append(getUnwoundString(c));
			}
			return sb.toString();
		}
		return null;
	}

	private static String getUnwoundString(char c) {
		switch (c) {
		case '\b':
			return "\\b";
		case '\t':
			return "\\t";
		case '\n':
			return "\\n";
		case '\f':
			return "\\f";
		case '\r':
			return "\\r";
		case '\\':
			return "\\\\";
		}
		return String.valueOf(c);
	}

	public static String windEscapeChars(String s) {
		if (s != null) {
			StringBuffer sb = new StringBuffer(s.length());
			int length = s.length();
			for (int i = 0; i < length; i++) {
				char c = s.charAt(i);
				if (c == '\\' && i < length - 1) {
					char next = s.charAt(i + 1);
					switch (next) {
					case 98: // 'b'
						sb.append("\b");
						i++;
						break;

					case 116: // 't'
						sb.append("\t");
						i++;
						break;

					case 110: // 'n'
						sb.append("\n");
						i++;
						break;

					case 102: // 'f'
						sb.append("\f");
						i++;
						break;

					case 114: // 'r'
						sb.append("\r");
						i++;
						break;

					case 92: // '\\'
						sb.append("\\");
						i++;
						break;
					}
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		} else {
			return s;
		}
	}
	
	/**
	 * 包含字母
	 * 
	 * @param str
	 * @return
	 */
	public static boolean containsLetters(String str) {
		if (str == null) {
			return false;
		}
		boolean b = false;
		char[] chars = str.toCharArray();
		int count = chars == null ? 0 : chars.length;
		for (int i = 0; i < count; i++) {
			char ch = chars[i];
			if (Character.toUpperCase(ch) != Character.toLowerCase(ch)) {
				b = true;
				break;
			}
		}
		return b;
	}

	/**
	 * 包含数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean containsDigist(String str) {
		if (str == null) {
			return false;
		}
		boolean b = false;
		char[] chars = str.toCharArray();
		int count = chars == null ? 0 : chars.length;
		for (int i = 0; i < count; i++) {
			char ch = chars[i];
			if (Character.isDigit(ch)) {
				b = true;
				break;
			}
		}
		return b;
	}

	/**
	 * 包含其它字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean containsOtherSign(String str) {
		if (str == null) {
			return false;
		}
		boolean b = false;
		char[] chars = str.toCharArray();
		int count = chars == null ? 0 : chars.length;
		for (int i = 0; i < count; i++) {
			char ch = chars[i];
			if ((!Character.isDigit(ch))
					&& (Character.toUpperCase(ch) == Character.toLowerCase(ch))) {
				b = true;
				break;
			}
		}
		return b;
	}

	/**
	 * 字符串数组排序
	 * 
	 * @param strs
	 */
	public static void sortStrings(String strs[]) {
		Arrays.sort(strs, new Comparator<Object>() {

			public int compare(String o1, String o2) {
				if (o1 == null)
					return o2 != null ? -1 : 0;
				else
					return o1.compareTo(o2);
			}

			public int compare(Object obj, Object obj1) {
				return compare((String) obj, (String) obj1);
			}

		});
	}

	/**
	 * 两个Region是否有交叉
	 * 
	 * @param reg1
	 * @param reg2
	 * @return
	 */
	public static boolean isIntersectRegion(Region reg1, Region reg2) {
		int pos11 = reg1.getOffset();
		int pos12 = reg1.getOffset() + reg1.getLength();
		int pos21 = reg2.getOffset();
		int pos22 = reg2.getOffset() + reg2.getLength();
		boolean intersect = true;
		if ((pos12 < pos21) || (pos11 > pos22)) {
			intersect = false;
		}
		return intersect;
	}

	/**
	 * 取项目源码路径列表
	 * 
	 * @param project
	 * @return
	 */
	public static List<IPackageFragmentRoot> getProjectAllSourceRoot(
			IJavaProject project) {
		List<IPackageFragmentRoot> list = new ArrayList<IPackageFragmentRoot>();
		try {
			IPackageFragmentRoot[] roots = project.getPackageFragmentRoots();
			int count = roots == null ? 0 : roots.length;
			for (int i = 0; i < count; i++) {
				IPackageFragmentRoot root = roots[i];
				if (root.getRawClasspathEntry().getEntryKind() == 3)
					list.add(root);
			}
		} catch (JavaModelException e) {
			Activator.getDefault().logError(e.getMessage(), e);
		}

		return list;
	}
  
  /**
   * 创建文件夹
   * 
   * @param folder
   * @throws CoreException
   */
  public static void createFolder(IFolder folder) throws CoreException{
	if (!folder.exists()){
		IContainer parent = folder.getParent();
		if (parent instanceof IFolder){
			createFolder((IFolder) parent);
		}
		folder.create(true, true, null);
	}
  }

}