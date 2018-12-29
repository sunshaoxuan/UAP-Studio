package nc.uap.lfw.perspective.listener;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import nc.lfw.lfwtools.perspective.MainPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * 文件操作
 * @author guoweic
 *
 */
public class FileUtil {

	/**
	 * 删除事件临时文件
	 * @param ctrl
	public static void deleteEventFile(EventEditorControl eventCtrl) {
		deleteFile(eventCtrl.getFilePath());
	}
	 */

	/**
	 * 保存文件
	 * @param folder
	 * @param fileName
	 * @param text
	 */
	public static void saveToFile(String folder, String fileName, String text) {
		File f = new File(folder);
		if (!f.exists())
			f.mkdirs();
		f = new File(folder + "\\" + fileName);
		try {
			if (!f.exists())
				f.createNewFile();
			saveToFile(f, text);
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
	}

	/**
	 * 保存文件
	 * @param f
	 * @param text
	 */
	public static void saveToFile(File f, String text) {
		BufferedWriter bf = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(f);
			osw = new OutputStreamWriter(fos, ResourcesPlugin.getEncoding());
			bf = new BufferedWriter(osw);
			bf.write(null == text ? "" : text);
			bf.flush();
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		} finally {
			try {
				bf.close();
			} catch (IOException e) {
				MainPlugin.getDefault().logError(e.getMessage(),e);
			}
		}
		return;
	}
	
	/**
	 * 保存文件
	 * @param fileName
	 * @param text
	 */
	public static void saveToFile(String fileName, String text)
    {
        File f = new File(fileName);
        if(!f.exists())
            try
            {
                f.createNewFile();
            }
            catch(IOException e)
            {
            	MainPlugin.getDefault().logError(e.getMessage(),e);
                return;
            }
        saveToFile(f, text);
    }
	
	/**
	 * 删除文件
	 * @param fileName
	 */
	public static void deleteFile(String fileName) {
		File f = new File(fileName);
		if (f.list() != null && f.list().length > 0) {
			for (String child : f.list()) {
				deleteFile(fileName + "\\" + child);
			}
		}
		if (f.exists())
        	f.delete();
	}
	
	// 缓冲区默认大小
	private static final int SIZE = 4096;
	
	private static final String CHARSET_NAME  = "UTF-8";
	
	/**
	 * 读取文件内容
	 * @param file
	 * @return
	 */
	public static String readFile(IFile file) {
		if (file.exists()) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			FileInputStream in = null;
			try {
				in = (FileInputStream) file.getContents();
				// 创建字节数组缓冲区
				byte[] buff = new byte[SIZE];
				int len = in.read(buff);
				while (len != -1) {
					out.write(buff, 0, len);
					len = in.read(buff);
				}
				return new String(out.toByteArray(), CHARSET_NAME);
			} catch (Exception e) {
				MainPlugin.getDefault().logError(e.getMessage(),e);
			} finally {
				try {
					out.close();
					in.close();
				} catch (IOException e) {
					MainPlugin.getDefault().logError(e.getMessage(),e);
				}
			}
		}
		return "";
	}
	
	/**
	 * 保存文件
	 * @param folder
	 * @param fileName
	 * @param text
	 */
	public static void renameFile(String folder, String oldName, String newName) {
		File oldFile = new File(folder + "\\" + oldName);
		if (!oldFile.exists())
			return;
		File newFile = new File(folder + "\\" + newName);
		try {
			oldFile.renameTo(newFile);
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
	}
}
