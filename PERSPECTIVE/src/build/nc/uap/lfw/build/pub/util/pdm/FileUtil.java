package nc.uap.lfw.build.pub.util.pdm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import nc.lfw.lfwtools.perspective.MainPlugin;

/**
 * 文件工具类
 * 
 * 可访问方法: forceCopy, forceDelete, modifyFileContent, getLastLine,
 * getFileContent, getFileReader, getFileWriter, createJarForDir,
 * createJarForSingleFile, createZipForDir, mergeToHashtable, mergeVector,
 * strTranslate, appendLine
 * 
 * @author fanp
 */
public class FileUtil implements FileFilter {
	private static FileUtil instance;
	private String suffixToBeFiltered;

	public FileUtil(String suffix_to_be_filtered) {
		this.suffixToBeFiltered = suffix_to_be_filtered;
	}

	public static FileUtil getInstance(String suffix_to_be_filtered) {
		if (instance == null){
			synchronized(FileUtil.class) {
				if(instance == null)
					instance = new FileUtil(suffix_to_be_filtered);					
			}
		}
		return instance;
	}

	public static void forceCopy(File src, File des) {
		File[] subFiles = src.listFiles();
		if (subFiles == null || subFiles.length == 0)
			return; // 递归返回

		for (int i = 0; i < subFiles.length; i++) {
			File subFile = subFiles[i];
			if (subFile.isFile()) {
				// 末级文件, 移动到目标输出目录下
				File newFile = new File(des.getAbsolutePath() + File.separator + subFile.getName());
				subFile.renameTo(newFile); /* 注: renameTo()方法实现'cut/paste' */
				// newFile.createNewFile(); /* 注:
				// createNewFile()方法实现'copy/paste' */
				MainPlugin.getDefault().logInfo("已移动文件: " + newFile.getName() + "文件大小: " + newFile.length() + "字节");
			} else {
				// 子目录, 在目标输出目录下创建对等子目录
				File newDir = new File(des.getAbsolutePath() + File.separator + subFile.getName());
				newDir.mkdir();
				MainPlugin.getDefault().logInfo("已复制子目录: " + newDir.getAbsolutePath());

				// 递归处理下级Files
				forceCopy(subFile, newDir);
			}
		}/* for */
	}

	/**
	 * 以递归方式实现对指定目录的彻底删除
	 * 
	 * @param _file
	 * @return
	 */
	public static void forceDelete(File _file) throws IOException {
		String msg = _file.getName();

		if (_file == null)
			return;

		if (_file.isDirectory()) {
			File[] files = _file.listFiles();

			if (files == null || files.length == 0) {
				/* 末级空目录, 递归返回 */
				if (!_file.delete())
					throw new IOException("无法删除目录: " + msg);
				MainPlugin.getDefault().logInfo("已删除目录: " + msg);
				return;
			}

			/* 进一步分解为下级元素后递归处理 */
			for (int i = 0; i < files.length; i++) {
				File afile = files[i];
				forceDelete(afile);
			}
			_file.delete();
			MainPlugin.getDefault().logInfo("已删除目录: " + msg);
		} else {
			/* 末级文件, 递归返回 */
			if (!_file.delete())
				throw new IOException("无法删除文件: " + msg);
			MainPlugin.getDefault().logInfo("已删除文件: " + msg);
			return;
		}
	}

	/**
	 * 改写文件内容
	 * 
	 * @param src_file
	 * @param new_content
	 * @return
	 */
	public static boolean modifyFileContent(File src_file, StringBuffer new_content) {
		if (src_file.exists()) {
			BufferedWriter bs = null;
			try {
				src_file.delete();
				src_file.createNewFile();
				bs = getFileWriter(src_file);
				bs.write(new_content.toString());
			} catch (IOException ioe) {
				MainPlugin.getDefault().logError(ioe.getMessage(),ioe);
			} finally {
				try {
					bs.close();
					return true;
				} catch (IOException ioe) {
					MainPlugin.getDefault().logError(ioe.getMessage(),ioe);
					return false;
				}
			}
		} else
			return false;
	}

	/**
	 * 返回指定文件中以执行字符串开头的行
	 * 
	 * @param src_file
	 * @param str
	 * @return
	 */
	public static String[] getLinesWithSpecifiedBeginningStr(File src_file, String str) {
		if (src_file.isDirectory() || !src_file.exists())
			return null;

		if (str == null || str.trim().length() == 0)
			return null;

		Vector v = new Vector();
		String objectiveStr = str.trim();

		BufferedReader in = getFileReader(src_file);
		try {
			String aline = in.readLine();
			while (aline != null) {
				if (aline.contains(objectiveStr) && aline.indexOf(objectiveStr) == 0) {
					String validLineContent = aline.substring(objectiveStr.length());
					v.addElement(validLineContent);
				}
				aline = in.readLine();
			}// while
		} catch (IOException ioe) {
			MainPlugin.getDefault().logError("读取指定文件: " + src_file.getName() + "内容时出错",ioe);
//			ioe.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException ioe) {
				MainPlugin.getDefault().logError("读取结束关闭指定文件: " + src_file.getName() + "时出错", ioe);
//				ioe.printStackTrace();
			}
		}

		String[] lines = new String[v.size()];
		v.copyInto(lines);
		return lines;
	}

	/**
	 * 返回指定文件的最后一行内容并替换其中%nc_date%, %nc_time%的值
	 * 
	 * @param src_file
	 * @return
	 */
	public static String getLastLine(File src_file) {
		BufferedReader in = getFileReader(src_file);
		if (in == null)
			return null;

		String nc_date = "";
		String nc_time = "";
		String cacheLine = ""; // "当前行"缓存
		try {
			String aline = in.readLine();
			while (aline != null) {
				// 保存至"当前行"缓存
				cacheLine = aline;

				if (cacheLine.indexOf("set nc_date=") >= 0) {
					int i = cacheLine.indexOf("set nc_date=");
					nc_date = cacheLine.substring(i + 12).trim();
				}

				if (cacheLine.indexOf("set nc_time=") >= 0) {
					int j = cacheLine.indexOf("set nc_time=");
					nc_time = cacheLine.substring(j + 12).trim();
				}

				aline = in.readLine();
			}// while
		} catch (IOException ioe) {
			MainPlugin.getDefault().logError("读取指定文件: " + src_file.getName() + "内容时出错");
//			ioe.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException ioe) {
				MainPlugin.getDefault().logError("读取结束关闭指定文件: " + src_file.getName() + "时出错");
//				ioe.printStackTrace();
			}
		}

		// 用nc_date, nc_time的当前值替换最后一行中的%nc_date%, %nc_time%
		cacheLine = cacheLine.replaceAll("%nc_date%", nc_date);
		cacheLine = cacheLine.replaceAll("%nc_time%", nc_time);
		return cacheLine;
	}

	/**
	 * 返回指定文件的全部内容
	 * 
	 * @param src_file
	 * @return
	 */
	public static StringBuffer getFileContent(File src_file) {
		BufferedReader in = getFileReader(src_file);
		if (in == null)
			return null;

		StringBuffer sb = new StringBuffer();
		try {
			String aline = in.readLine();
			while (aline != null) {
				sb.append(aline);
				sb.append("\r\n");
				aline = in.readLine();
			}
		} catch (IOException ioe) {
			MainPlugin.getDefault().logError("读取指定文件: " + src_file.getName() + "时出错");
//			ioe.printStackTrace();
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException ioe) {
				MainPlugin.getDefault().logError("读取结束关闭指定文件: " + src_file.getName() + "时出错");
//				ioe.printStackTrace();
			}
		}
		return sb;
	}

	/**
	 * 打开指定文件并返回其输出流
	 * 
	 * @param fSrc
	 * @return
	 */
	public static BufferedReader getFileReader(File fSrc) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(fSrc))));
		} catch (IOException ioe) {
			MainPlugin.getDefault().logError("打开指定文件错误, 程序退出");
//			ioe.printStackTrace();
			return null;
		}
		return in;
	}

	/**
	 * 打开指定文件并返回其输出流
	 * 
	 * @param src
	 * @return
	 */
	public static BufferedReader getFileReader(FileInputStream src) {
		return new BufferedReader(new InputStreamReader(new BufferedInputStream(src)));
	}

	/**
	 * 打开指定文件并返回其输入流
	 * 
	 * @param fdes
	 * @return
	 */
	public static BufferedWriter getFileWriter(File fdes) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(
					new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(fdes)), "gbk"));
		} catch (IOException ioe) {
			MainPlugin.getDefault().logError("打开文件错误, 程序退出");
//			ioe.printStackTrace();
			return null;
		}
		return out;
	}

	/**
	 * 打开指定文件并返回其输入流
	 * 
	 * @param des
	 * @return
	 */
	public static BufferedWriter getFileWriter(FileOutputStream des) {
		return new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(des)));
	}

	/**
	 * 以递归方式实现将指定目录整体打jar包
	 * 
	 * @param archiveFile
	 * @param tobeJared
	 * @return
	 */
	public static void createJarForDir(File archiveFile, File tobeJared) {
	}

	/**
	 * 该方法将单个指定文件打jar包
	 * 
	 * @param archiveFile
	 * @param tobeJared
	 * @return
	 */
	public static void createJarForSingleFile(File archiveFile, File tobeJared) {
		FileInputStream in = null;
		JarOutputStream out = null;
		FileOutputStream fileOut = null;
		if (!tobeJared.exists() || tobeJared.isDirectory())
			return;

		int BUFFER_SIZE = 10240;

		try {
			fileOut = new FileOutputStream(archiveFile); 
			out = new JarOutputStream(fileOut, new Manifest());

			// 添加"jar entry"
			JarEntry jarAdd = new JarEntry(tobeJared.getName());
			jarAdd.setTime(tobeJared.lastModified());
			out.putNextEntry(jarAdd);

			// 将源文件内容写入jar文件
			in = new FileInputStream(tobeJared);
			byte buffer[] = new byte[BUFFER_SIZE];
			while (true) {
				int nRead = in.read(buffer, 0, buffer.length);
				if (nRead <= 0)
					break;
				out.write(buffer, 0, nRead);
			}// while
			out.closeEntry();
		} catch (IOException ioe) {
			MainPlugin.getDefault().logError("打[" + tobeJared.getName() + "]jar包时出错");
//			ioe.printStackTrace();
		} finally {
			try {
				if(fileOut != null)
					fileOut.close();
			} catch (IOException ioe) {
				MainPlugin.getDefault().logError(ioe);
			}
			try {
				if(in != null)
					in.close();
			} catch (IOException ioe) {
				MainPlugin.getDefault().logError(ioe);
			}
			try {
				if(out != null)
					out.close();
			} catch (IOException ioe) {
				MainPlugin.getDefault().logError(ioe);
			}
		}
	}

	/**
	 * 以递归方式实现将指定目录整体打zip包 注: 使用JDK1.5中自带的文件/目录压缩类不支持中文的名件/目录名, 如果有中文名打包就会失败;
	 * Ant.jar包中提供的zip类能够解决不支持中文文件/目录的问题
	 * 
	 * @param archiveFilePath
	 * @param inputFileName
	 * @param base
	 * @return
	 */
	public static void createZipForDir(String archiveFilePath, String inputFileName, String base) throws Exception {
		MainPlugin.getDefault().logInfo("开始创建 " + archiveFilePath + "...");

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(archiveFilePath));
		File f = new File(inputFileName);
		out.putNextEntry(new ZipEntry(base + "/"));

		/* 分解指定输入目录, 将其下级子目录挂在"base"下压入指定输出jar包 */
		File[] fl = f.listFiles();
		for (int i = 0; i < fl.length; i++) {
			zip(out, fl[i], base);
		}
		out.close();

		MainPlugin.getDefault().logInfo("创建完成");
	}

	private static void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) {
			// 将当前目录加入jar包
			MainPlugin.getDefault().logInfo("压缩目录: " + f.getPath());
			String cur_base = base + "/" + f.getName();
			out.putNextEntry(new ZipEntry(cur_base + "/"));

			File[] fl = f.listFiles();
			if (fl == null || fl.length == 0) {
				// 当前目录为空目录, 递归返回
				return;
			} else {
				// 当前目录为满目录, 进一步递归处理下级目录
				for (int i = 0; i < fl.length; i++) {
					zip(out, fl[i], cur_base);
				}
			}
		} else {
			// 将当前文件加入jar包
			MainPlugin.getDefault().logInfo("压缩文件: " + f.getPath());
			out.putNextEntry(new ZipEntry(base + "/" + f.getName()));
			FileInputStream in = null;
			try{
				in = new FileInputStream(f);
				byte[] b = new byte[512];
				int len = 0;
				while ((len = in.read(b)) != -1) {
					out.write(b, 0, len);
				}
			}
			catch(Exception e){
				MainPlugin.getDefault().logError(e.getMessage(),e);
			}finally{
				if(in != null)
					in.close();
			}
			// 递归返回
			return;
		}
	}

	/**
	 * 将指定"字符串数组"合并至"哈希表"中, 合并时: (1)合并时每一个数组元素均同时作为key和value存入哈希表 (2)重复的过滤掉
	 * 
	 * @param src
	 * @param ht
	 * @return
	 */
	public static void mergeToHashtable(Object[] src, Hashtable ht) {
		if (src == null || src.length == 0)
			return;

		for (int i = 0; i < src.length; i++) {
			if (!ht.containsKey(src[i]))
				ht.put(src[i], src[i]);
		}
	}

	/**
	 * 合并Vector
	 * 
	 * @param base
	 * @param v
	 * @return
	 */
	public static void mergeVector(Vector base, Vector v) {
		if (base == null || v == null || v.size() == 0)
			return;

		for (int i = 0; i < v.size(); i++)
			base.addElement(v.elementAt(i));
	}

	/**
	 * 过滤指定集合中的重复字符串项
	 * 
	 * @param src
	 * @return
	 */
	public static Vector filtRedundentStringItems(Vector src) {
		if (src == null || src.size() == 0)
			return null;

		Vector v = new Vector();
		Hashtable cache = new Hashtable();

		for (int i = 0; i < src.size(); i++) {
			String item = (String) src.elementAt(i);
			if (!cache.containsKey(item))
				cache.put(item, item);
		}

		Enumeration e = cache.keys();
		while (e.hasMoreElements()) {
			v.addElement(e.nextElement());
		}
		return v;
	}

	/**
	 * properties属性文件中"中文"信息的转换读取
	 * 
	 * @param src
	 * @return
	 */
	public static String strTranslate(String src) {
		if (src == null)
			return null;

		char[] c = src.toCharArray();
		byte[] b = new byte[c.length];
		for (int i = 0; i < c.length; i++) {
			b[i] = (byte) c[i];
		}

		String str = null;
		try {
			// MainPlugin.getDefault().logInfo("默认字符集: " + Charset.defaultCharset().name());
			str = new String(b, Charset.defaultCharset().name());
		} catch (UnsupportedEncodingException uee) {
			MainPlugin.getDefault().logInfo("读取properties属性文件时转换字符集失败");
			System.exit(-1);
		}
		return str;
	}

	/**
	 * 在指定文件的最后一行插入指定内容
	 * 
	 * @param file
	 * @param aline
	 * @return
	 */
	public static void appendLine(File file, String aline) {
		if (file == null || !file.exists() || !file.isFile() || !file.canWrite())
			return;

		BufferedWriter bs = getFileWriter(file);
		try {
			bs.write("\r\n");
			bs.write(aline);
			bs.write("\r\n");
		} catch (IOException ioe) {
			MainPlugin.getDefault().logError(ioe.getMessage(),ioe);
		} finally {
			try {
				bs.close();
			} catch (IOException ioe) {
				MainPlugin.getDefault().logError(ioe.getMessage(),ioe);
			}
		}
	}

	/**
	 * 在指定父目录下搜索满足指定子目录名称条件的子目录集合
	 * 
	 * @param parent_dir
	 * @param children_dir_name
	 * @return
	 */
	public static File[] findChildrenDirIncludedByName(File parent_dir, String included_children_dir_name) {
		if (parent_dir == null || !parent_dir.isDirectory())
			return null;

		final String filteredDirName = included_children_dir_name;
		File[] children = parent_dir.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				if (pathname.isDirectory() && pathname.getName().equals(filteredDirName)) {
					return true;
				} else {
					return false;
				}
			}

		});

		return children;

		// File[] children = parent_dir.listFiles();
		// if(children == null || children.length == 0)
		// return null;
		//
		// Vector v = new Vector();
		// for(int i=0; i<children.length; i++)
		// {
		// File fChild = children[i];
		// if(fChild.isDirectory() &&
		// fChild.getName().equals(included_children_dir_name))
		// v.addElement(fChild);
		// }
		//    	
		// File[] result = new File[v.size()];
		// v.copyInto(result);
		//    		
		// return result;
	}

	/**
	 * 在指定父目录下搜索除指定子目录名称以外的子目录集合
	 * 
	 * @param parent_dir
	 * @param children_dir_name
	 * @return
	 */
	public static File[] findChildrenDirExcludedByName(File parent_dir, String[] excluded_children_dir_names) {
		if (parent_dir == null || !parent_dir.isDirectory())
			return null;

		final String[] filteredDirNames = excluded_children_dir_names;
		File[] children = parent_dir.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					for (int j = 0; j < filteredDirNames.length; j++) {
						if (pathname.getName().equals(filteredDirNames[j])) {
							return false;
						}
					}
					return true;
				} else {
					return false;
				}
			}

		});

		return children;
		// File[] children = parent_dir.listFiles();
		// if(children == null || children.length == 0)
		// return null;
		//
		// Vector v = new Vector();
		// for(int i=0; i<children.length; i++)
		// {
		// File fChild = children[i];
		//
		// /* 检测当前fChild是否应被排除 */
		// boolean isBeExcluded = false;
		// for(int j=0; j<excluded_children_dir_names.length; j++)
		// {
		// if(fChild.isDirectory() &&
		// fChild.getName().equals(excluded_children_dir_names[j]))
		// {
		// isBeExcluded = true;
		// break;
		// }
		// }
		// if(!isBeExcluded)
		// v.addElement(fChild);
		// }
		//    	
		// File[] result = new File[v.size()];
		// v.copyInto(result);
		//    		
		// return result;
	}

	/**
	 * 该方法以指定方式递归创建指定路径, 当isDir值为'true'时以'目录'方式创建; 当isDir值为'false'时以'文件'方式创建
	 * 
	 * @param path
	 * @param isDir
	 * @return
	 */
	public static void forceCreate(String path, boolean isDir) {
		try {
			/*
			 * StringBuffer sbDir = new StringBuffer(); StringTokenizer st = new
			 * StringTokenizer(path, File.separator); int size =
			 * st.countTokens(); int i = 0; //token计数器 while(st.hasMoreTokens())
			 * { sbDir.append(st.nextToken()); i++; File fCurDir = new
			 * File(sbDir.toString()); if(fCurDir.isDirectory()) { // 遇到中间目录,
			 * 如尚不存在则创建 if(!fCurDir.exists()) fCurDir.createNewFile();
			 * 
			 * if(i < size) sbDir.append(File.separator); } else { // 遇到末级文件,
			 * 如尚不存在则创建, 完成后退出 if(!fCurDir.exists()) fCurDir.createNewFile();
			 * break; } }//while
			 */
			File f = new File(path);
			if (isDir) {
				/* 将path以"目录"方式递归创建 */
				f.mkdirs();
			} else {
				/* 将path以"文件"方式递归创建 */
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
		} catch (IOException ioe) {
//			ioe.printStackTrace();
			MainPlugin.getDefault().logError("FileUtil::forceCreate方法出现文件系统错误");
		}
	}

	/**
	 * Tests whether or not the specified abstract pathname should be included
	 * in a pathname list.
	 * 
	 * @param pathname
	 *            The abstract pathname to be tested
	 * @return <code>true</code> if and only if <code>pathname</code> should be
	 *         included
	 * @todo Implement this java.io.FileFilter method
	 */
	public boolean accept(File pathname) {
		if (pathname.isDirectory() || pathname.getAbsolutePath().lastIndexOf(suffixToBeFiltered) > 0)
			return true;
		else
			return false;
	}

	/**
	 * 该方法检查指定文件或目录当前是否已存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean checkExist(String path) {
		File f = new File(path);
		if (f.exists())
			return true;
		else
			return false;
	}

	/**
	 * 该方法检查指定文件名是否需要过滤
	 * 
	 * @param suffix
	 * @param file
	 * @return
	 */
	public static boolean filter(String suffix, File file) {
		if (file == null || file.isDirectory() || !file.exists())
			return true;

		String fileName = file.getName();
		if (fileName.contains("." + suffix))
			return true;
		else
			return false;
	}

	/**
	 * 实现文件复制功能
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFile(String src, String dest) throws IOException {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(src);
			File file = new File(dest);
			if (!file.exists()) {
				file.createNewFile();
			}

			out = new FileOutputStream(file);
			int c;
			byte[] buffer = new byte[1024];
			while ((c = in.read(buffer)) != -1) {
				for (int i = 0; i < c; i++) {
					out.write(buffer[i]);
				}
			}

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
