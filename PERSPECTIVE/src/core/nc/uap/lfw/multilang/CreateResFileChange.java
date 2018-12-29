package nc.uap.lfw.multilang;

import nc.uap.lfw.tool.Helper;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.internal.corext.refactoring.nls.changes.CreateTextFileChange;
import org.eclipse.ltk.core.refactoring.Change;

public class CreateResFileChange extends CreateTextFileChange
{
  private String encodeing = null;

  public CreateResFileChange(IPath path, String source, String encoding)
  {
    super(path, source, encoding, "properties");
    this.encodeing = encoding;
  }

  public Change perform(IProgressMonitor pm) throws CoreException, OperationCanceledException
  {
    IFile file = getOldFile(pm);
    IContainer parent = file.getParent();
    if (!(parent.exists()))
      Helper.createResource(parent);

    return super.perform(pm);
  }
}