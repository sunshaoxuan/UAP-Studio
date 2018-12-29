package nc.lfw.editor.common.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;

/**
 * 编辑器临时信息
 * 
 * @author guoweic
 * 
 */
public class LfwGlobalEditorInfo implements Serializable {

	private static final long serialVersionUID = 8339476671146844449L;

	// 信息集合
	private static Map<String, Serializable> attrMap;

	// 保存的文件对象
	private static File file;
	
	// 保存的文件夹基本路径
	private static final String BASE_FOLDER = "\\temp";
	
	// 保存的文件名
	private static final String FILE_NAME = "GlobalEditorInfo.temp";
	
	/**
	 * 新建文件
	 */
	private static void createFile() {
		try {
			String folder = LFWPersTool.getProjectPath() + BASE_FOLDER;
			String fileName = FILE_NAME;
			
			File f = new File(folder);
			if (!f.exists())
				f.mkdirs();
			file = new File(folder + "\\" + fileName);
			if (!f.exists())
				file.createNewFile();
		} catch (IOException e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
	}
	
	/**
	 * 增加信息
	 * @param key
	 * @param obj
	 */
	public static void addAttribute(String key, Serializable obj) {
		if (null == attrMap)
			attrMap = getAttrMap();
		attrMap.put(key, obj);
		writeToFile();
	}

	/**
	 * 将对象写入文件
	 */
	private static void writeToFile() {
		try {
			if (null == file || !file.exists())
				createFile();
			LFWAMCPersTool.checkOutFile(file.getAbsolutePath());
			FileOutputStream fo = new FileOutputStream(file);
			ObjectOutputStream oo = new ObjectOutputStream(fo);
			oo.writeObject(attrMap);
			
		} catch (IOException e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
	}

	/**
	 * 获取对象
	 * @param key
	 * @return
	 */
	public static Serializable getAttr(String key) {
		Map<String, Serializable> map = getAttrMap();
		if (map.containsKey(key))
			return map.get(key);
		return null;
	}
	
	/**
	 * 从文件中获取Map对象
	 * 
	 * @return
	 */
	private static Map<String, Serializable> getAttrMap() {
		if (null == file || !file.exists()) {
			createFile();
		}
		try {
			FileInputStream fi = new FileInputStream(file);
			ObjectInputStream oi = new ObjectInputStream(fi);
			attrMap = (Map<String, Serializable>) oi.readObject();
		} catch (Exception e) {
			// 重新生成文件
			if (file.exists())
				file.delete();
			attrMap = new HashMap<String, Serializable>();
			writeToFile();
		}
		return attrMap;
	}

}
