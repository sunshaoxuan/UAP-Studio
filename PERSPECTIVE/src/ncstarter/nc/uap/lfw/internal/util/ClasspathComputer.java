/*
 * Created on 2005-8-17
 * @author ºÎ¹ÚÓî
 */
package nc.uap.lfw.internal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.model.LibraryLocation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;

/*
 * @author ºÎ¹ÚÓî
 */
public class ClasspathComputer
{
//	public static void updateClasspath(IProject project, IProgressMonitor monitor) throws CoreException
//	{
//		if (project != null && project.hasNature(WEBProjConstants.MODULE_NATURE))
//		{
//			monitor.subTask("Update classpath");
//			IJavaProject javaProject = JavaCore.create(project);
//			IClasspathEntry[] entries = getClasspath(project, false);
//			javaProject.setRawClasspath(entries, monitor);
//		}
//	}

	public static LibraryLocation[] computeStandCP(IFolder... baseFolders) throws CoreException
	{
		ArrayList<LibraryLocation> llist = new ArrayList<LibraryLocation>();
		for (IFolder folder : baseFolders)
		{
			IFolder classes = folder.getFolder(ClasspathConstants.CLASSES);
			IFolder resources = folder.getFolder(ClasspathConstants.RESOURCES);
			if (classes.exists())
			{
				llist.add(new LibraryLocation(classes));
			}
			if (resources.exists())
			{
				//llist.addAll(Arrays.asList(computeJarsInPath(resources)));
				llist.add(new LibraryLocation(resources));
			}
			//
			IFolder varclass = folder.getFolder(ClasspathConstants.VAR_CLASSES);
			if (varclass.exists())
			{
				llist.add(new LibraryLocation(varclass));
			}
			//
			IFolder extclass = folder.getFolder(ClasspathConstants.EXTENSION_CLASSES);
			if (extclass.exists())
			{
				llist.add(new LibraryLocation(extclass));
			}
			llist.addAll(Arrays.asList(computeJarsInPath(folder.getFolder(ClasspathConstants.LIB))));
		}
		return llist.toArray(new LibraryLocation[0]);
	}

//	private static IClasspathEntry[] getClasspath(IProject project, boolean clear) throws CoreException
//	{
//		IJavaProject javaproject = JavaCore.create(project);
//		ArrayList<IClasspathEntry> result = new ArrayList<IClasspathEntry>();
//		addSourceAndLibraries(javaproject, clear, result);
//		result.add(ProjCoreUtility.createJREEntry());
//		for (WEBClassPathContainerID id : WEBClassPathContainerID.values())
//		{
//			LFWClasspathContainer container = ProjCoreUtility.getLFWClasspathContainer(id.getPath(), javaproject);
//			container.setClasspathEntries(ProjCoreUtility.getClasspathEntry(project, id));
//			result.add(ProjCoreUtility.createContainerClasspathEntry(id));
//		}
//		IClasspathEntry[] entries = result.toArray(new IClasspathEntry[result.size()]);
//		IJavaModelStatus validation = JavaConventions.validateClasspath(javaproject, entries, javaproject.getOutputLocation());
//		if (!validation.isOK())
//		{
//			throw new CoreException(validation);
//		}
//		return (IClasspathEntry[]) result.toArray(new IClasspathEntry[result.size()]);
//	}
//
//	private static void addSourceAndLibraries(IJavaProject project, boolean clear, ArrayList<IClasspathEntry> result) throws CoreException
//	{
//		HashSet<IPath> paths = new HashSet<IPath>();
//		IClasspathEntry resEntry = null;
//		if (!clear)
//		{
//			IClasspathEntry[] entries = project.getRawClasspath();
//			for (int i = 0; i < entries.length; i++)
//			{
//				IClasspathEntry entry = entries[i];
//				int entryType = entry.getEntryKind();
//				if (entryType == IClasspathEntry.CPE_SOURCE || entryType == IClasspathEntry.CPE_PROJECT || entryType == IClasspathEntry.CPE_LIBRARY)
//				{
//					if (entry.getPath().toPortableString().endsWith("resources"))
//					{
//						resEntry = entry;
//						continue;
//					}
//					// avoid duplicate entry
//					if (paths.add(entry.getPath()))
//					{
//						result.add(entry);
//					}
//				}
//				else if (entryType == IClasspathEntry.CPE_CONTAINER)
//				{
//					entry = null;
//				}
//			}
//		}
//		if (resEntry == null)
//			resEntry = ProjCoreUtility.createSourceEntry(project.getProject(), "resources", "out/resources");
//		result.add(resEntry);
//	}

	public static LibraryLocation[] computeJarsInPath(IFolder... folders) throws CoreException
	{
		final ArrayList<LibraryLocation> list = new ArrayList<LibraryLocation>();
		final Pattern pattern = Pattern.compile("(?<!_(doc|src))\\.(jar|zip)$", Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE);
		final String[] exps = WEBPersPlugin.getExceptJarNames().split("\r\n");
		for (IFolder folder : folders)
		{
			if (folder.exists())
			{
				folder.accept(new IResourceProxyVisitor()
				{
					public boolean visit(IResourceProxy proxy) throws CoreException
					{
						if (proxy.getType() == IResource.FILE)
						{
							IFile file = (IFile) proxy.requestResource();
							String filename = file.getName();
							Matcher matcher = pattern.matcher(filename);
							if (matcher.find())
							{
								for (String exp : exps)
								{
									Pattern p = Pattern.compile(exp, Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE);
									Matcher m = p.matcher(filename);
									if (m.find())
									{
										return false;
									}
								}
								list.add(new LibraryLocation(file));
							}
							return false;
						}
						return true;
					}
				}, 0);
			}
		}
		LibraryLocation[] rets = list.toArray(new LibraryLocation[0]);
		Arrays.sort(rets, new Comparator<LibraryLocation>()
		{
			public int compare(LibraryLocation o1, LibraryLocation o2)
			{
				return o1.getLibResource().getName().compareToIgnoreCase(o2.getLibResource().getName());
			}
		});
		return rets;
	}
}
