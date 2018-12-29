/*
 * Created on 2005-8-10
 * @author ºÎ¹ÚÓî
 */
package nc.uap.lfw.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * @author ºÎ¹ÚÓî
 *  
 */
public class LibraryLocation
{
	
	public String toString()
	{
		return getLibResource().getName();
	}

	private IResource	libPath;

	public LibraryLocation()
	{
	}

	public LibraryLocation(IResource libPath)
	{
		this.libPath = libPath;
	}

	public String getDocLocation()
	{
		IResource res = getLibResource();
		if (res instanceof IFile)
		{
			IFile file = (IFile) res;
			IFile docjar = file.getParent().getFile(new Path(file.getName().substring(0, file.getName().lastIndexOf('.')) + "_doc." + file.getFileExtension()));
			if (docjar.exists())
			{
				return "jar:file:/" + docjar.getLocation().toOSString() + "!/";
			}
		}
		return null;
	}

	public IPath getSrcPath()
	{
		IResource res = getLibResource();
		if (res instanceof IFile)
		{
			IFile file = (IFile) res;
			IFile srcjar = file.getParent().getFile(new Path(file.getName().substring(0, file.getName().lastIndexOf('.')) + "_src." + file.getFileExtension()));
			if (srcjar.exists())
			{
				return srcjar.getFullPath();
			}
		}
		else if (res instanceof IFolder)
		{
			IFolder file = (IFolder) res;
			IFolder src = file.getParent().getFolder(new Path("source"));
			if (src.exists())
			{
				return src.getFullPath();
			}
			src = file.getParent().getFolder(new Path("sources"));
			if (src.exists())
			{
				return src.getFullPath();
			}
			src = file.getParent().getFolder(new Path("src"));
			if (src.exists())
			{
				return src.getFullPath();
			}
		}
		return null;
	}

	public IPath getLibPath()
	{
		return getLibResource().getFullPath();
	}

	public IResource getLibResource()
	{
		return libPath;
	}

	public void setLibResource(IResource libPath)
	{
		this.libPath = libPath;
	}
}