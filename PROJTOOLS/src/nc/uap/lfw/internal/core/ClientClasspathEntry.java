package nc.uap.lfw.internal.core;

import java.util.ArrayList;

import nc.uap.lfw.core.WebClassPathContainerID;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.launching.DefaultProjectClasspathEntry;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.w3c.dom.Element;

public class ClientClasspathEntry extends DefaultProjectClasspathEntry
{
  public static final String TYPE_ID = "nc.uap.mde.ClientClasspathEntry";
  private DefaultProjectClasspathEntry dpce;

  public ClientClasspathEntry(DefaultProjectClasspathEntry dpce)
  {
    this.dpce = dpce;
  }

  public boolean equals(Object obj)
  {
    if (obj instanceof ClientClasspathEntry)
    {
      ClientClasspathEntry entry = (ClientClasspathEntry)obj;
      return entry.getDpce().equals(getDpce());
    }
    return false;
  }

  public int hashCode()
  {
    return this.dpce.hashCode();
  }

  public String getTypeId()
  {
    return "nc.uap.mde.ClientClasspathEntry";
  }

  public String getName()
  {
    return "Client(default classpath)";
  }

  public IRuntimeClasspathEntry[] getRuntimeClasspathEntries(ILaunchConfiguration configuration) throws CoreException
  {
    IRuntimeClasspathEntry[] arrayOfIRuntimeClasspathEntry1;
    IRuntimeClasspathEntry[] entries = getDpce().getRuntimeClasspathEntries(configuration);
    ArrayList list = new ArrayList();
    int j = (arrayOfIRuntimeClasspathEntry1 = entries).length; for (int i = 0; i < j; ++i) { IRuntimeClasspathEntry entry = arrayOfIRuntimeClasspathEntry1[i];

      IPath path = entry.getPath();
      if (entry.getType() == 1)
      {
        list.add(entry);
      }
      if ((path.equals(WebClassPathContainerID.Product_Common_Library.getPath())) || (path.equals(WebClassPathContainerID.Module_Public_Library.getPath())) || 
        (path.equals(WebClassPathContainerID.Module_Client_Library.getPath())) || 
        (path.equals(WebClassPathContainerID.Module_Lang_Library.getPath())))
      {
        list.add(entry);
      }
    }
    return ((IRuntimeClasspathEntry[])list.toArray(new IRuntimeClasspathEntry[0]));
  }

  protected DefaultProjectClasspathEntry getDpce()
  {
    return this.dpce;
  }

  public Object getAdapter(Class adapter)
  {
    return this.dpce.getAdapter(adapter);
  }

  public IClasspathEntry getClasspathEntry()
  {
    return this.dpce.getClasspathEntry();
  }

  public int getClasspathProperty()
  {
    return this.dpce.getClasspathProperty();
  }

  public IJavaProject getJavaProject()
  {
    return this.dpce.getJavaProject();
  }

  public String getLocation()
  {
    return this.dpce.getLocation();
  }

  public String getMemento() throws CoreException
  {
    return this.dpce.getMemento();
  }

  public IPath getPath()
  {
    return this.dpce.getPath();
  }

  public IResource getResource()
  {
    return this.dpce.getResource();
  }

  public IRuntimeClasspathEntry[] getRuntimeClasspathEntries() throws CoreException
  {
    return this.dpce.getRuntimeClasspathEntries();
  }

  public String getSourceAttachmentLocation()
  {
    return this.dpce.getSourceAttachmentLocation();
  }

  public IPath getSourceAttachmentPath()
  {
    return this.dpce.getSourceAttachmentPath();
  }

  public String getSourceAttachmentRootLocation()
  {
    return this.dpce.getSourceAttachmentRootLocation();
  }

  public IPath getSourceAttachmentRootPath()
  {
    return this.dpce.getSourceAttachmentRootPath();
  }

  public String getVariableName()
  {
    return this.dpce.getVariableName();
  }

  public boolean isExportedEntriesOnly()
  {
    return this.dpce.isExportedEntriesOnly();
  }

  public void setClasspathProperty(int property)
  {
    this.dpce.setClasspathProperty(property);
  }

  public void setExportedEntriesOnly(boolean exportedOnly)
  {
    this.dpce.setExportedEntriesOnly(exportedOnly);
  }

  public void setSourceAttachmentPath(IPath path)
  {
    this.dpce.setSourceAttachmentPath(path);
  }

  public void setSourceAttachmentRootPath(IPath path)
  {
    this.dpce.setSourceAttachmentRootPath(path);
  }

  public String toString()
  {
    return this.dpce.toString();
  }

  public void initializeFrom(Element memento) throws CoreException
  {
    this.dpce.initializeFrom(memento);
  }
}
