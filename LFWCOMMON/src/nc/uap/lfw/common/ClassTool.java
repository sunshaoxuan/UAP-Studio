package nc.uap.lfw.common;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.plugin.common.CommonPlugin;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;

public class ClassTool {
	private static Map<String, URLClassLoader> loaderMap = new HashMap<String, URLClassLoader>();
	private static URLClassLoader loader = null;
	public static URLClassLoader getURLClassLoader(IProject project, ClassLoader parentLoader) {
		String projectName = project.getName();
		String key = projectName + "_" + parentLoader.hashCode();
		loader = loaderMap.get(key);
		if(loader == null){
			synchronized (ClassTool.class) {
				if(loader == null){
					ArrayList<URL> allUrls = new ArrayList<URL>();
					IJavaProject elementJavaProject = JavaCore.create(project);
					if (elementJavaProject != null) {
						try {
							String[] classPathArray = JavaRuntime
									.computeDefaultRuntimeClassPath(elementJavaProject);
							for (int i = 0; i < classPathArray.length; i++) {
								File file = new File(classPathArray[i]);
								System.out.println(classPathArray[i]);
								allUrls.add(file.toURL());
							}
						} catch (Exception e) {
							CommonPlugin.getPlugin().logError(e.getMessage());
						}
					}
					loader = new URLClassLoader(allUrls.toArray(new URL[0]), parentLoader);
					loaderMap.put(key, loader);
				}
			}			
		}
		return loader;
	}

	public static Class loadClass(String clsName, IProject project, ClassLoader parentLoader)
			throws ClassNotFoundException {
		return getURLClassLoader(project, parentLoader).loadClass(clsName);
	}
}

