package nc.uap.lfw.internal.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.common.SpaceMode;
import nc.uap.lfw.core.WEBProjConstants;
import nc.uap.lfw.core.WEBProjPlugin;
import nc.uap.lfw.core.WebClassPathContainerID;
import nc.uap.lfw.internal.project.LFWClasspathContainer;
import nc.uap.lfw.lang.M_internal;
import nc.uap.lfw.model.LFWLibraryLocation;
import nc.uap.lfw.plugin.common.CommonPlugin;
import nc.uap.lfw.wizards.NewRefCodeProjectCreationOperation;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class ClasspathComputer
{
	public static String[] sourceArr = new String[]{"src/public", "src/client", "src/private", "src/test", "src/portalspec", "resources"};
	public static void updateClasspath(IProject project, IProgressMonitor monitor) throws CoreException{
		if (project != null && project.hasNature(WEBProjConstants.MODULE_NATURE)){
			monitor.subTask(M_internal.ClasspathComputer_0);
			IJavaProject javaProject = JavaCore.create(project);
			IClasspathEntry[] entries = getClasspath(project);
			javaProject.setRawClasspath(entries, monitor);
		}
	}

	public static LFWLibraryLocation[] computeStandCP(IPath[] baseFolders) throws CoreException{
		ArrayList<LFWLibraryLocation> llist = new ArrayList<LFWLibraryLocation>();
		for (IPath folder : baseFolders){
			IPath classes = folder.append(ClasspathConstants.CLASSES);
			if (classes!=null && classes.toFile().exists() && classes.toFile().listFiles().length > 0){
				llist.add(new LFWLibraryLocation(classes));
			}
			IPath resources = folder.append(ClasspathConstants.RESOURCES);
			if (resources!=null && resources.toFile().exists() && resources.toFile().listFiles().length > 0){
				llist.add(new LFWLibraryLocation(resources));
			}
			IPath varclass = folder.append(ClasspathConstants.VAR_CLASSES);
			if (varclass!=null&& varclass.toFile().exists() && varclass.toFile().listFiles().length > 0){
				llist.add(new LFWLibraryLocation(varclass));
			}
			IPath extclass = folder.append(ClasspathConstants.EXTENSION_CLASSES);
			if (extclass!=null && extclass.toFile().exists() && extclass.toFile().listFiles().length > 0){
				llist.add(new LFWLibraryLocation(extclass));
			}
			
			llist.addAll(Arrays.asList(computeJarsInPath(new IPath[] {folder})));
			
			IPath libPath = folder.append(ClasspathConstants.LIB);
			llist.addAll(Arrays.asList(computeJarsInPath(new IPath[] {libPath})));
		}
		return llist.toArray(new LFWLibraryLocation[0]);
	}
	
	public static LFWLibraryLocation[] computeCPByList(IPath[] baseFolder, InputStream inputStream) throws CoreException {
		BufferedReader reader = null;
		List<String> list = new ArrayList<String>();
		try{
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String line = reader.readLine();
			while(line != null){
				list.add(line);
				line = reader.readLine();
			}
		} 
		catch (IOException e) {
			WEBProjPlugin.getDefault().logError(e);
		}
		finally{
			IOUtils.closeQuietly(reader);
		}
		
		LFWLibraryLocation[] libs = computeStandCP(baseFolder);
		List<LFWLibraryLocation> libsList = new ArrayList<LFWLibraryLocation>();
		if(list.size() > 0){
			for (int i = 0; i < libs.length; i++) {
				LFWLibraryLocation lib = libs[i];
				String name = lib.getLibPath().lastSegment();
				if(!name.endsWith(".jar")){
					libsList.add(lib);
				}
				else if(list.contains(name)){
						libsList.add(lib);
				}
				
			}
		}
		return libsList.toArray(new LFWLibraryLocation[0]);
	}

	private static IClasspathEntry[] getClasspath(IProject project) throws CoreException
	{
		IJavaProject javaproject = JavaCore.create(project);
		ArrayList<IClasspathEntry> result = new ArrayList<IClasspathEntry>();
		addSourceAndLibraries(javaproject, result);
		result.add(ProjCoreUtility.createJREEntry());
		WebClassPathContainerID[] values = null;
		if(LfwCommonTool.getSpaceMode().equals(SpaceMode.STANDALONG)){
			values = WebClassPathContainerID.valuesTomcat();
		}
		else{
			values = WebClassPathContainerID.valuesUap();
		}
		
		boolean isCodeRefProj = false;
		String refCodeProp = LfwCommonTool.getModuleProperty(project, NewRefCodeProjectCreationOperation.MODULE_REFCODE_PROPERTY);
		if(refCodeProp != null && refCodeProp.equals("true"))
			isCodeRefProj = true;
		for (WebClassPathContainerID id : values){
			LFWClasspathContainer container = ProjCoreUtility.getLFWClasspathContainer(id.getPath(), javaproject);
			if(isCodeRefProj)
				container.setClasspathEntries(ProjCoreUtility.getFullClasspathEntry(id));
			else
				container.setClasspathEntries(ProjCoreUtility.getClasspathEntry(id));
			result.add(ProjCoreUtility.createContainerClasspathEntry(id));
		}
//		IClasspathEntry[] entries = result.toArray(new IClasspathEntry[result.size()]);
//		IJavaModelStatus validation = JavaConventions.validateClasspath(javaproject, entries, javaproject.getOutputLocation());
//		if (!validation.isOK()){
//			throw new CoreException(validation);
//		}
		return (IClasspathEntry[]) result.toArray(new IClasspathEntry[result.size()]);
	}

	private static void addSourceAndLibraries(IJavaProject project, List<IClasspathEntry> result) throws CoreException
	{
		HashSet<IPath> paths = new HashSet<IPath>();
		scanBusiSrc(project, result);
		IClasspathEntry[] entries = project.getRawClasspath();
		for (int i = 0; i < entries.length; i++){
			IClasspathEntry entry = entries[i];
			int entryType = entry.getEntryKind();
			if (entryType == IClasspathEntry.CPE_PROJECT || entryType == IClasspathEntry.CPE_LIBRARY){
				if (paths.add(entry.getPath())){
					result.add(entry);
				}
			}
		}
	}

	private static void scanBusiSrc(IJavaProject project, List<IClasspathEntry> result) {
		try {
			//推测出业务组件列表
			Set<String> busiComps = new HashSet<String>();
			File[] files = project.getProject().getLocation().toFile().listFiles();
			for (File f : files) {
				if(f.isDirectory()){
					busiComps.add(f.getName());
				}
			}
			IPath proPath = project.getProject().getLocation();
			for (String name : busiComps) {
				String srcDir = name + "/src/public";
				//没有public目录的不认为是组件
				if(!proPath.append(srcDir).toFile().exists())
					continue;
				for (int j = 0; j < sourceArr.length; j++) {
					srcDir = name + "/" + sourceArr[j];
					if(proPath.append(srcDir).toFile().exists())
						result.add(ProjCoreUtility.createSourceEntry(project.getProject(), srcDir));
				}
			}
		} 
		catch (CoreException e) {
			CommonPlugin.getPlugin().logError(e);
		}
	}

	public static LFWLibraryLocation[] computeJarsInPath(IPath[] paths) throws CoreException
	{
		final ArrayList<LFWLibraryLocation> list = new ArrayList<LFWLibraryLocation>();
		final Pattern pattern = Pattern.compile("(?<!_(doc|src))\\.(jar|zip)$", Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE); //$NON-NLS-1$
		final String[] exps = WEBProjPlugin.getExceptJarNames().split("\r\n"); //$NON-NLS-1$
//		final IPath path;
		for (final IPath path : paths)
		{
			File file = path.toFile();
			if (file.exists() && file.isDirectory()){
				File[] subFiles = file.listFiles(new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						if (pathname.isFile()) {
			                String filename = pathname.getName();
			                Matcher matcher = pattern.matcher(filename);
			                if (matcher.find()) {
			                	for (String exp : exps)
								{
									Pattern p = Pattern.compile(exp, Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE);
									Matcher m = p.matcher(filename);
									if (m.find())
									{
										return false;
									}
								}
			                  return true;
			                }
			              }
			              return false;
					}
					
				});
				for(File pathname : subFiles)
					list.add(new LFWLibraryLocation(path.append(pathname.getName())));
//				folder.accept(new IResourceProxyVisitor()
//				{
//					public boolean visit(IResourceProxy proxy) throws CoreException
//					{
//						if (proxy.getType() == IResource.FILE)
//						{
//							IFile file = (IFile) proxy.requestResource();
//							String filename = file.getName();
//							Matcher matcher = pattern.matcher(filename);
//							if (matcher.find())
//							{
//								for (String exp : exps)
//								{
//									Pattern p = Pattern.compile(exp, Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE);
//									Matcher m = p.matcher(filename);
//									if (m.find())
//									{
//										return false;
//									}
//								}
//								list.add(new LFWLibraryLocation(file));
//							}
//							return false;
//						}
//						return true;
//					}
//				}, 0);
			}
		}
		LFWLibraryLocation[] rets = list.toArray(new LFWLibraryLocation[0]);
		Arrays.sort(rets, new Comparator<LFWLibraryLocation>()
		{
			public int compare(LFWLibraryLocation o1, LFWLibraryLocation o2)
			{
				return o1.getLibResource().lastSegment().compareToIgnoreCase(o2.getLibResource().lastSegment());
			}
		});
		return rets;
	}
}
