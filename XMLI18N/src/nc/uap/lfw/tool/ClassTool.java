package nc.uap.lfw.tool;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import nc.uap.lfw.plugin.Activator;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * 获取类加载器
 * 
 * @author dingrf
 * 
 */
public class ClassTool {

	@SuppressWarnings("deprecation")
	public static URLClassLoader getURLClassLoader(IProject project) {
		URLClassLoader loader = null;
		ArrayList<URL> allUrls = new ArrayList<URL>();
		IJavaProject elementJavaProject = JavaCore.create(project);
		if (elementJavaProject != null) {
			try {
				String[] classPathArray = JavaRuntime
						.computeDefaultRuntimeClassPath(elementJavaProject);
				for (int i = 0; i < classPathArray.length; i++) {
					File file = new File(classPathArray[i]);
					allUrls.add(file.toURL());
				}
			} catch (Exception e) {
				Activator.getDefault().logError(e.getMessage(), e);
			}
		}
		loader = new URLClassLoader((URL[]) allUrls.toArray(new URL[0]),
				ClassTool.class.getClassLoader());

		return loader;
	}
}