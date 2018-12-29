package nc.uap.lfw.internal.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.internal.launching.DefaultProjectClasspathEntry;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.StandardClasspathProvider;

public class MDEClientClasspathProvider extends StandardClasspathProvider
{
  public IRuntimeClasspathEntry[] computeUnresolvedClasspath(ILaunchConfiguration configuration)
    throws CoreException
  {
    IRuntimeClasspathEntry[] defEntries = super.computeUnresolvedClasspath(configuration);
    for (int i = 0; i < defEntries.length; ++i)
    {
      if (defEntries[i] instanceof DefaultProjectClasspathEntry)
      {
        defEntries[i] = new ClientClasspathEntry((DefaultProjectClasspathEntry)defEntries[i]);
      }
    }
    return defEntries;
  }

  protected IRuntimeClasspathEntry[] recoverRuntimePath(ILaunchConfiguration configuration, String attribute)
    throws CoreException
  {
    IRuntimeClasspathEntry[] entries = super.recoverRuntimePath(configuration, attribute);
    return entries;
  }

  public IRuntimeClasspathEntry[] resolveClasspath(IRuntimeClasspathEntry[] entries, ILaunchConfiguration configuration)
    throws CoreException
  {
    IRuntimeClasspathEntry[] entries2 = super.resolveClasspath(entries, configuration);
    return entries2;
  }
}
