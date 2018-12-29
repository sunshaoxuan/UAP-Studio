package nc.uap.lfw.internal.project;

import nc.uap.lfw.core.WEBClassPathContainerID;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;

public class LFWClasspathContainer implements IClasspathContainer
{
	protected IClasspathEntry[]		entries;
	private WEBClassPathContainerID	id;

	public LFWClasspathContainer(WEBClassPathContainerID id, IClasspathEntry[] entries)
	{
		this.id = id;
		this.entries = entries;
	}

	public int getKind()
	{
		return K_APPLICATION;
	}

	public IClasspathEntry[] getClasspathEntries()
	{
		return entries;
	}

	public String getDescription()
	{
		return id.name();
	}

	public IPath getPath()
	{
		return id.getPath();
	}

	public void setClasspathEntries(IClasspathEntry[] entries)
	{
		this.entries = entries;
	}
}
