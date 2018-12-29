/*
 * Created on 2005-8-10
 * @author ºÎ¹ÚÓî
 */
package nc.uap.lfw.model;

import java.io.File;

import nc.uap.lfw.internal.util.ProjCoreUtility;

import org.eclipse.core.runtime.IPath;

/**
 * 
 * @author qinjianc
 * 
 */
public class LFWLibraryLocation {
	@Override
	public String toString() {
		// return getLibResource().getName();
		return this.libPath.makeRelativeTo(
				ProjCoreUtility.getNcHomeFolderPath()).toOSString();
	}

	private IPath libPath;
	private File file;

	public LFWLibraryLocation() {
	}

	public LFWLibraryLocation(IPath libPath) {
		this.libPath = libPath;
	}

	private String getPathName() {
		return this.libPath.lastSegment();
	}

	public String getDocLocation() {
		if (getFile().isFile())
	    {
	      String srcjar = getPathName().substring(0, getPathName().lastIndexOf(46)) + "_doc." + this.libPath.getFileExtension();
	      File javaDoc = new File(getFile().getParent(), srcjar);
	      if (javaDoc.exists())
	      {
	        return "jar:file:/" + javaDoc.getAbsolutePath() + "!/";
	      }
	    }
		return null;
	}

	public IPath getSrcPath() {
		
		IPath parentPath = this.libPath.removeLastSegments(1);
		if (getFile().isFile()) {
			String srcjar = getPathName().substring(0, getPathName().lastIndexOf(46)) + "_src." + this.libPath.getFileExtension();
		      IPath append = parentPath.append(srcjar);
			if (append!=null && append.toFile().exists()) {
				return append;
			}
		} else if (getFile().isDirectory()) {
			File src = new File(getFile().getParent(), "source");
			if (src.exists()) {
				return parentPath.append("source");
			}
			src = new File(getFile().getParent(), "sources");
			if (src.exists()) {
				return parentPath.append("sources");
			}
			src = new File(getFile().getParent(), "src");
			if (src.exists()) {
				 return parentPath.append("src");
			}
		}
		return null;
	}

	public IPath getLibPath() {
		return getLibResource();
	}

	public IPath getLibResource() {
		return libPath;
	}

	public void setLibResource(IPath libPath) {
		this.libPath = libPath;
	}

	public File getFile() {
		if (this.file == null) {
			this.file = this.libPath.toFile();
		}
		return this.file;
	}
}