package nc.uap.lfw.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.Enumeration;

import nc.uap.lfw.plugin.common.CommonPlugin;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author luoyf
 * 
 */
public class FileUtilities {

	/**
	 * 复制目录下的文件（不包括此目录）到指定目录，连同子目录一起复制。
	 * 
	 * @param toPath
	 * @param fromPath
	 */
	public static void copyFileFromDir(String toPath, String fromPath) throws Exception {
		File file = new File(fromPath);
		if (file.isDirectory()) {
			copyFileToDir(toPath, listFile(file));
		}
	}

	/**
	 * 复制控制文件模板
	 * 
	 * @param toPath
	 * @param fromPath
	 */
	public static void copyCtrlFileFromDir(String toPath, String fromPath) throws Exception {
		File file = new File(fromPath);
		File[] files = file.listFiles();
		if (file.isDirectory()) {
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				String fromfilename = f.getCanonicalPath();
				int index1 = fromfilename.lastIndexOf("\\");

				String fn = fromfilename.substring(index1 + 1);
				String name = fn.substring(0, fn.indexOf('.'));
				String tofilename = name + ".java";

				copyFileToDir(toPath, f, tofilename);
			}
		}
	}

	/**
	 * 复制文件到指定目录，toDir是目标目录，filePath是被复制的文件路径
	 * 
	 * @param toDir
	 * @param filePath
	 * @throws IOException
	 */

	public static void copyFileToDir(String toDir, String[] filePath) throws IOException {
		if (toDir == null || toDir.equals("")) {
			return;
		}
		File targetFile = new File(toDir);
		if (!targetFile.exists()) {
			targetFile.mkdir();
		} else if (!targetFile.isDirectory()) {
			return;
		}
		for (String path : filePath) {
			File file = new File(path);
			if (file.isDirectory()) {
				copyFileToDir(toDir + "/" + file.getName(), listFile(file));
			} else {
				copyFileToDir(toDir, file, "");
			}
		}
	}

	/**
	 * 复制文件到指定目录。toDir是目标目录，file是源文件名，newName是重命名的名字。
	 * 
	 * @param toDir
	 * @param from
	 * @param newName
	 * @throws IOException
	 */
	public static void copyFileToDir(String toDir, File from, String newName) throws IOException {
		String newFilePath = "";
		if (newName != null && !newName.equals("")) {
			newFilePath = toDir + "/" + newName;
		} else {
			newFilePath = toDir + "/" + from.getName();
		}
		copyFile(from.getPath(), newFilePath);
	}

	/**
	 * 保存文件
	 * 
	 * @param fileName
	 * @param content
	 * @throws Exception
	 */
	public static void saveFile(String fileName, byte[] content) throws IOException {
		FileOutputStream out = null;
		File file = new File(fileName);
		try {
			if (!file.exists()) {
				File dir = new File(file.getParent());
				if (!dir.exists())
					dir.mkdirs();
				file.createNewFile();
			}
			out = new FileOutputStream(file);
			out.write(content);
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				CommonPlugin.getPlugin().logError(e.getMessage(), e);
			}
		}
	}
	/**
	 * 保存文件
	 * 
	 * @param fileName
	 * @param input
	 * @throws IOException
	 */
	public static void saveFile(String fileName, InputStream input) throws IOException {
		FileOutputStream outputStream = null;
		File file = new File(fileName);
		try {
			if (!file.exists()) {
				File dir = new File(file.getParent());
				if (!dir.exists())
					dir.mkdirs();
				file.createNewFile();
			}
			outputStream = new FileOutputStream(file);
			byte b[] = new byte[1024];
			int j = 0;
			while((j = input.read(b))!= -1){
				outputStream.write(b, 0, j);
			}
			outputStream.flush();
			outputStream.close();
		}
		catch(Exception e){
			CommonPlugin.getPlugin().logError(e.getMessage(), e);
		}
		finally{
			try{
				if(outputStream != null)
					outputStream.close();
				}
			catch(Exception e){
				CommonPlugin.getPlugin().logError(e.getMessage(), e);
			}
		}
	}

	/**
	 * 保存文件
	 * 
	 * @param fileName
	 *            文件路径
	 * @param text
	 *            文件内容
	 * @throws IOException
	 */
	public static void saveFile(String fileName, String text) throws IOException {
		File f = new File(fileName);
		if (!f.exists())
			f.createNewFile();
		saveFile(f, text);
	}

	/**
	 * 保存文件
	 * 
	 * @param f
	 *            文件
	 * @param text
	 *            文件内容
	 * @throws IOException
	 */
	public static void saveFile(File f, String text) throws IOException {
		BufferedWriter bf = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(f);
			osw = new OutputStreamWriter(fos, ResourcesPlugin.getEncoding());
			bf = new BufferedWriter(osw);
			bf.write(text);
			bf.flush();
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			try {
				if (bf != null)
					bf.close();
				if (fos != null)
					fos.close();
				if (osw != null)
					osw.close();
			} catch (IOException e) {
				CommonPlugin.getPlugin().logError(e.getMessage(), e);
			}
		}
	}

	public static void saveFile(File f, String text, String encode) throws IOException {
		BufferedWriter bf = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(f);
			osw = new OutputStreamWriter(fos, encode);
			bf = new BufferedWriter(osw);
			bf.write(text);
			bf.flush();
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			try {
				if (bf != null)
					bf.close();
				if (fos != null)
					fos.close();
				if (osw != null)
					osw.close();
			} catch (IOException e) {
				CommonPlugin.getPlugin().logError(e.getMessage(), e);
			}
		}
	}

	/**
	 * 复制文件to为目标文件，from为源文件
	 * 
	 * @param from
	 * @param to
	 * @throws Exception
	 */
	public static void copyFile(String from, String to) throws IOException {
		File sour = new File(from);
		if (!sour.exists()) {
			CommonPlugin.getPlugin().logInfo("文件" + from + "不存在");
			return;
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		File destFile = new File(to);
		File parentFile = destFile.getParentFile();
		if (!parentFile.exists())
			parentFile.mkdirs();
		if (destFile.exists())
			destFile.delete();
		byte[] buf = new byte[2048];
		int len = -1;
		try {
			fis = new FileInputStream(from);
			fos = new FileOutputStream(to);
			while ((len = fis.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				CommonPlugin.getPlugin().logError(e.getMessage(), e);
			}
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				CommonPlugin.getPlugin().logError(e.getMessage(), e);
			}
		}
		destFile.setLastModified(sour.lastModified());
	}

	/**
	 * 获取文件路径
	 * 
	 * @param dir
	 * @return
	 */
	public static String[] listFile(File dir) {
		String absolutPath = dir.getAbsolutePath();
		String[] paths = dir.list();
		String[] files = new String[paths.length];
		for (int i = 0; i < paths.length; i++) {
			files[i] = absolutPath + "/" + paths[i];
		}
		return files;
	}

	/**
	 * 生成文件
	 * 
	 * @param path
	 * @param isFile
	 * @throws IOException
	 */
	public static void createFile(String path, boolean isFile) throws IOException {
		createFile(new File(path), isFile);
	}

	public static void createFile(File file, boolean isFile) throws IOException {
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				createFile(file.getParentFile(), false);
			} else {
				if (isFile)
					file.createNewFile();
				else
					file.mkdir();

			}
		}
	}
	
	public static void createFolder(IFolder folder){
		try {
			folder.create(true, true, null);
		} catch (CoreException e) {
			CommonPlugin.getPlugin().logError(e.getMessage(), e);
		}
	}

	/**
	 * 删除指定文件或文件夹(包括文件夹中所有文件)
	 * 
	 * @param folder
	 */
	public static void deleteFile(File file) {
		if (!file.exists())
			return;
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			int count = childFiles == null ? 0 : childFiles.length;
			for (int i = 0; i < count; i++) {
				deleteFile(childFiles[i]);
			}
			file.delete();
		}
		else{
			if (!file.delete()) {
				CommonPlugin.getPlugin().logError("删除文件失败：" + file.getAbsolutePath());
			}
		}

	}

	/**
	 * 删除指定文件夹下所有文件和子文件夹
	 * 
	 * @param folder
	 */
	public static void deleteFiles(String folder) {
		File f = new File(folder);
		if (f.exists()) {
			File allFiles[] = f.listFiles();
			for (int i = 0, n = allFiles.length; i < n; i++) {
				if (allFiles[i].isFile()) {
					allFiles[i].delete();
				} else {
					FileUtilities.deleteFiles(allFiles[i].toString());
					allFiles[i].delete();
				}
			}
		}
	}

	/**
	 * 获取文件内容
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	// public static String fetchFileContent(String fileName) throws Exception{
	// File file = new File(fileName);
	// if (file.exists()) {
	// ByteArrayOutputStream out = new ByteArrayOutputStream();
	// FileInputStream in = null;
	// try {
	// in = new FileInputStream(file);
	// // 创建字节数组缓冲区
	// byte[] buff = new byte[4096];
	// int len = in.read(buff);
	// while (len != -1) {
	// out.write(buff, 0, len);
	// len = in.read(buff);
	// }
	// return out.toString();
	// } catch (IOException e) {
	// throw new Exception(e);
	// } finally {
	// try {
	// out.close();
	// in.close();
	// }
	// catch (IOException e) {
	// CommonPlugin.getPlugin().logError("获取文件内容出错：" + e.getMessage(), e);
	// }
	// }
	// }
	// return null;
	// }

	/**
	 * 获取文件内容
	 * 
	 * @param fileName
	 *            文件
	 * @param encoding
	 *            编码格式
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public static String fetchFileContent(String fileName, String encoding) throws Exception {
		return fetchFileContent(new File(fileName), encoding);
	}

	/**
	 * 获取文件内容
	 * 
	 * @param f
	 *            目标文件
	 * @param encoding
	 *            编码格式
	 * @return
	 * @throws Exception
	 */
	public static String fetchFileContent(File f, String encoding) throws Exception {
		String text;
		if (!f.exists())
			return null;
		BufferedReader in = null;
		text = null;
		try {
			StringBuffer sb = new StringBuffer();
			in = new BufferedReader(new InputStreamReader(new FileInputStream(f), encoding));
			// in = new BufferedReader(new FileReader(f));
			for (String line = null; (line = in.readLine()) != null;)
				sb.append(line).append("\r\n");
			text = sb.toString();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				CommonPlugin.getPlugin().logError(e.getMessage(), e);
			}
		}
		return text;
	}

	/**
	 * 获取文件内容
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String fetchFileContent(IFile file, String encoding) throws Exception {
		String text;
		InputStream io;
		if (!file.exists())
			return null;
		text = null;
		io = null;
		try {
			io = file.getContents();
			if (io == null)
				return null;
			text = fetchFileContent(io, encoding);
			return text;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (io != null)
					io.close();
			} catch (IOException e) {
				CommonPlugin.getPlugin().logError(e.getMessage(), e);
			}
		}
	}

	/**
	 * 获取文件内容
	 * 
	 * @param io
	 * @return
	 * @throws IOException
	 */
	public static String fetchFileContent(InputStream io, String encoding) throws IOException {
		String text = null;
		if (io == null)
			return null;
		BufferedReader br = new BufferedReader(new InputStreamReader(io, encoding));
		StringBuffer sb = new StringBuffer();
		for (String line = br.readLine(); line != null; line = br.readLine())
			sb.append(line).append("\r\n");

		text = sb.toString();
		return text;
	}
	
	public static void unzip(String fileName, String toFolder) throws Exception{
//		FileInputStream inputStream = null;
		try{
//			File file = new File(fileName);
//			inputStream = new FileInputStream(file);
			inflate(fileName, toFolder);
		}
		catch(Exception e){
			CommonPlugin.getPlugin().logError(e.getMessage(), e);
		}
	}

	/**
	 * 压缩文件输入流解压到到指定目录
	 * 
	 * @param iszip
	 * @param toFolder
	 * @throws Exception
	 */
	private static void inflate(String fileName, String toFolder) throws Exception {
		File folder = new File(toFolder);
		if(!folder.exists()){
			folder.mkdirs();
		}
		if (folder.exists()) {
			try {
				ZipFile zip = new ZipFile(fileName);
				Enumeration entries = zip.getEntries();
				while(entries.hasMoreElements()){
					ZipEntry entry = (ZipEntry) entries.nextElement();
					OutputStream out = null;
					InputStream in = null;
					try{
						String outFilename = entry.getName();
						File zipFile = new File(toFolder + "/" + outFilename);
						if(entry.isDirectory()){
							if(!zipFile.exists())
								zipFile.mkdirs();
							continue;
						}
						if(!zipFile.getParentFile().exists())
							zipFile.getParentFile().mkdirs();
						File targetFile = new File(toFolder + "/" + outFilename);
						if(targetFile.exists()){
							targetFile.setWritable(true);
						}
						out = new FileOutputStream(toFolder + "/" + outFilename);
						in = zip.getInputStream(entry);
						
						if(in != null){
							byte buf[] = new byte[1024];
							int len;
							
							while ((len = in.read(buf)) > 0)
								out.write(buf, 0, len);
						}
					}
					finally{
						try{
							if(out != null)
								out.close();
						}
						catch(Exception e){
							CommonPlugin.getPlugin().logError(e);
						}
						try{
							if(in != null)
								in.close();
						}
						catch(Exception e){
							CommonPlugin.getPlugin().logError(e);
						}
					}
				}
			} catch (Exception e) {
				throw new Exception(e);
			} 
		}
	}
	
	public static Document parseText(String text) throws DocumentException {
        Document result = null;

        SAXReader reader = new SAXReader();
        String encoding = reader.getEncoding();

        InputSource source = new InputSource(new StringReader(text));
        source.setEncoding(encoding);
        try {
			reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		} catch (SAXException e) {
			CommonPlugin.getPlugin().logError(e.getMessage(),e);
		}
        result = reader.read(source);

        // if the XML parser doesn't provide a way to retrieve the encoding,
        // specify it manually
        if (result.getXMLEncoding() == null) {
            result.setXMLEncoding(encoding);
        }

        return result;
    }

	 public static void main(String[] args) {
		try {
			unzip("C:/devPackage/lfw.zip", "C:/devPackage/dd");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	 }
}
