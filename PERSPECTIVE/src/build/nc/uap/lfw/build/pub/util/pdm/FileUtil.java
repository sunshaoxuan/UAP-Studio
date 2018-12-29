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
 * �ļ�������
 * 
 * �ɷ��ʷ���: forceCopy, forceDelete, modifyFileContent, getLastLine,
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
			return; // �ݹ鷵��

		for (int i = 0; i < subFiles.length; i++) {
			File subFile = subFiles[i];
			if (subFile.isFile()) {
				// ĩ���ļ�, �ƶ���Ŀ�����Ŀ¼��
				File newFile = new File(des.getAbsolutePath() + File.separator + subFile.getName());
				subFile.renameTo(newFile); /* ע: renameTo()����ʵ��'cut/paste' */
				// newFile.createNewFile(); /* ע:
				// createNewFile()����ʵ��'copy/paste' */
				MainPlugin.getDefault().logInfo("���ƶ��ļ�: " + newFile.getName() + "�ļ���С: " + newFile.length() + "�ֽ�");
			} else {
				// ��Ŀ¼, ��Ŀ�����Ŀ¼�´����Ե���Ŀ¼
				File newDir = new File(des.getAbsolutePath() + File.separator + subFile.getName());
				newDir.mkdir();
				MainPlugin.getDefault().logInfo("�Ѹ�����Ŀ¼: " + newDir.getAbsolutePath());

				// �ݹ鴦���¼�Files
				forceCopy(subFile, newDir);
			}
		}/* for */
	}

	/**
	 * �Եݹ鷽ʽʵ�ֶ�ָ��Ŀ¼�ĳ���ɾ��
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
				/* ĩ����Ŀ¼, �ݹ鷵�� */
				if (!_file.delete())
					throw new IOException("�޷�ɾ��Ŀ¼: " + msg);
				MainPlugin.getDefault().logInfo("��ɾ��Ŀ¼: " + msg);
				return;
			}

			/* ��һ���ֽ�Ϊ�¼�Ԫ�غ�ݹ鴦�� */
			for (int i = 0; i < files.length; i++) {
				File afile = files[i];
				forceDelete(afile);
			}
			_file.delete();
			MainPlugin.getDefault().logInfo("��ɾ��Ŀ¼: " + msg);
		} else {
			/* ĩ���ļ�, �ݹ鷵�� */
			if (!_file.delete())
				throw new IOException("�޷�ɾ���ļ�: " + msg);
			MainPlugin.getDefault().logInfo("��ɾ���ļ�: " + msg);
			return;
		}
	}

	/**
	 * ��д�ļ�����
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
	 * ����ָ���ļ�����ִ���ַ�����ͷ����
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
			MainPlugin.getDefault().logError("��ȡָ���ļ�: " + src_file.getName() + "����ʱ����",ioe);
//			ioe.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException ioe) {
				MainPlugin.getDefault().logError("��ȡ�����ر�ָ���ļ�: " + src_file.getName() + "ʱ����", ioe);
//				ioe.printStackTrace();
			}
		}

		String[] lines = new String[v.size()];
		v.copyInto(lines);
		return lines;
	}

	/**
	 * ����ָ���ļ������һ�����ݲ��滻����%nc_date%, %nc_time%��ֵ
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
		String cacheLine = ""; // "��ǰ��"����
		try {
			String aline = in.readLine();
			while (aline != null) {
				// ������"��ǰ��"����
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
			MainPlugin.getDefault().logError("��ȡָ���ļ�: " + src_file.getName() + "����ʱ����");
//			ioe.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException ioe) {
				MainPlugin.getDefault().logError("��ȡ�����ر�ָ���ļ�: " + src_file.getName() + "ʱ����");
//				ioe.printStackTrace();
			}
		}

		// ��nc_date, nc_time�ĵ�ǰֵ�滻���һ���е�%nc_date%, %nc_time%
		cacheLine = cacheLine.replaceAll("%nc_date%", nc_date);
		cacheLine = cacheLine.replaceAll("%nc_time%", nc_time);
		return cacheLine;
	}

	/**
	 * ����ָ���ļ���ȫ������
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
			MainPlugin.getDefault().logError("��ȡָ���ļ�: " + src_file.getName() + "ʱ����");
//			ioe.printStackTrace();
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException ioe) {
				MainPlugin.getDefault().logError("��ȡ�����ر�ָ���ļ�: " + src_file.getName() + "ʱ����");
//				ioe.printStackTrace();
			}
		}
		return sb;
	}

	/**
	 * ��ָ���ļ��������������
	 * 
	 * @param fSrc
	 * @return
	 */
	public static BufferedReader getFileReader(File fSrc) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(fSrc))));
		} catch (IOException ioe) {
			MainPlugin.getDefault().logError("��ָ���ļ�����, �����˳�");
//			ioe.printStackTrace();
			return null;
		}
		return in;
	}

	/**
	 * ��ָ���ļ��������������
	 * 
	 * @param src
	 * @return
	 */
	public static BufferedReader getFileReader(FileInputStream src) {
		return new BufferedReader(new InputStreamReader(new BufferedInputStream(src)));
	}

	/**
	 * ��ָ���ļ���������������
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
			MainPlugin.getDefault().logError("���ļ�����, �����˳�");
//			ioe.printStackTrace();
			return null;
		}
		return out;
	}

	/**
	 * ��ָ���ļ���������������
	 * 
	 * @param des
	 * @return
	 */
	public static BufferedWriter getFileWriter(FileOutputStream des) {
		return new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(des)));
	}

	/**
	 * �Եݹ鷽ʽʵ�ֽ�ָ��Ŀ¼�����jar��
	 * 
	 * @param archiveFile
	 * @param tobeJared
	 * @return
	 */
	public static void createJarForDir(File archiveFile, File tobeJared) {
	}

	/**
	 * �÷���������ָ���ļ���jar��
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

			// ���"jar entry"
			JarEntry jarAdd = new JarEntry(tobeJared.getName());
			jarAdd.setTime(tobeJared.lastModified());
			out.putNextEntry(jarAdd);

			// ��Դ�ļ�����д��jar�ļ�
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
			MainPlugin.getDefault().logError("��[" + tobeJared.getName() + "]jar��ʱ����");
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
	 * �Եݹ鷽ʽʵ�ֽ�ָ��Ŀ¼�����zip�� ע: ʹ��JDK1.5���Դ����ļ�/Ŀ¼ѹ���಻֧�����ĵ�����/Ŀ¼��, ���������������ͻ�ʧ��;
	 * Ant.jar�����ṩ��zip���ܹ������֧�������ļ�/Ŀ¼������
	 * 
	 * @param archiveFilePath
	 * @param inputFileName
	 * @param base
	 * @return
	 */
	public static void createZipForDir(String archiveFilePath, String inputFileName, String base) throws Exception {
		MainPlugin.getDefault().logInfo("��ʼ���� " + archiveFilePath + "...");

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(archiveFilePath));
		File f = new File(inputFileName);
		out.putNextEntry(new ZipEntry(base + "/"));

		/* �ֽ�ָ������Ŀ¼, �����¼���Ŀ¼����"base"��ѹ��ָ�����jar�� */
		File[] fl = f.listFiles();
		for (int i = 0; i < fl.length; i++) {
			zip(out, fl[i], base);
		}
		out.close();

		MainPlugin.getDefault().logInfo("�������");
	}

	private static void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) {
			// ����ǰĿ¼����jar��
			MainPlugin.getDefault().logInfo("ѹ��Ŀ¼: " + f.getPath());
			String cur_base = base + "/" + f.getName();
			out.putNextEntry(new ZipEntry(cur_base + "/"));

			File[] fl = f.listFiles();
			if (fl == null || fl.length == 0) {
				// ��ǰĿ¼Ϊ��Ŀ¼, �ݹ鷵��
				return;
			} else {
				// ��ǰĿ¼Ϊ��Ŀ¼, ��һ���ݹ鴦���¼�Ŀ¼
				for (int i = 0; i < fl.length; i++) {
					zip(out, fl[i], cur_base);
				}
			}
		} else {
			// ����ǰ�ļ�����jar��
			MainPlugin.getDefault().logInfo("ѹ���ļ�: " + f.getPath());
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
			// �ݹ鷵��
			return;
		}
	}

	/**
	 * ��ָ��"�ַ�������"�ϲ���"��ϣ��"��, �ϲ�ʱ: (1)�ϲ�ʱÿһ������Ԫ�ؾ�ͬʱ��Ϊkey��value�����ϣ�� (2)�ظ��Ĺ��˵�
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
	 * �ϲ�Vector
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
	 * ����ָ�������е��ظ��ַ�����
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
	 * properties�����ļ���"����"��Ϣ��ת����ȡ
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
			// MainPlugin.getDefault().logInfo("Ĭ���ַ���: " + Charset.defaultCharset().name());
			str = new String(b, Charset.defaultCharset().name());
		} catch (UnsupportedEncodingException uee) {
			MainPlugin.getDefault().logInfo("��ȡproperties�����ļ�ʱת���ַ���ʧ��");
			System.exit(-1);
		}
		return str;
	}

	/**
	 * ��ָ���ļ������һ�в���ָ������
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
	 * ��ָ����Ŀ¼����������ָ����Ŀ¼������������Ŀ¼����
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
	 * ��ָ����Ŀ¼��������ָ����Ŀ¼�����������Ŀ¼����
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
		// /* ��⵱ǰfChild�Ƿ�Ӧ���ų� */
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
	 * �÷�����ָ����ʽ�ݹ鴴��ָ��·��, ��isDirֵΪ'true'ʱ��'Ŀ¼'��ʽ����; ��isDirֵΪ'false'ʱ��'�ļ�'��ʽ����
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
			 * st.countTokens(); int i = 0; //token������ while(st.hasMoreTokens())
			 * { sbDir.append(st.nextToken()); i++; File fCurDir = new
			 * File(sbDir.toString()); if(fCurDir.isDirectory()) { // �����м�Ŀ¼,
			 * ���в������򴴽� if(!fCurDir.exists()) fCurDir.createNewFile();
			 * 
			 * if(i < size) sbDir.append(File.separator); } else { // ����ĩ���ļ�,
			 * ���в������򴴽�, ��ɺ��˳� if(!fCurDir.exists()) fCurDir.createNewFile();
			 * break; } }//while
			 */
			File f = new File(path);
			if (isDir) {
				/* ��path��"Ŀ¼"��ʽ�ݹ鴴�� */
				f.mkdirs();
			} else {
				/* ��path��"�ļ�"��ʽ�ݹ鴴�� */
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
		} catch (IOException ioe) {
//			ioe.printStackTrace();
			MainPlugin.getDefault().logError("FileUtil::forceCreate���������ļ�ϵͳ����");
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
	 * �÷������ָ���ļ���Ŀ¼��ǰ�Ƿ��Ѵ���
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
	 * �÷������ָ���ļ����Ƿ���Ҫ����
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
	 * ʵ���ļ����ƹ���
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
