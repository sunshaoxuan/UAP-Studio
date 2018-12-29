package nc.uap.lfw.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import nc.uap.lfw.plugin.common.CommonPlugin;

import org.eclipse.core.resources.IProject;

public class LFWUtility {
	public static String getContextFromResource(IProject project) {
		String module = null;
		File f = project.getLocation().toFile();
		File moduleFile = new File(f, ".module_prj");
		if (moduleFile.exists()) {
			InputStream is = null;
			try {
				is = new FileInputStream(moduleFile);
				Properties prop = new Properties();
				prop.load(is);
				module = prop.getProperty("module.webContext");
			} 
			catch (Exception e) {
				CommonPlugin.getPlugin().logError(e.getMessage(), e);
			}
			finally{
				if(is != null)
					try {
						is.close();
					} catch (IOException e) {
						CommonPlugin.getPlugin().logError(e.getMessage(), e);
					}
			}
			return module;
		}
		return "lfw";
	}
}