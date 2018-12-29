package nc.uap.lfw.internal.bcp;

import org.eclipse.core.resources.IFolder;

/**
 * 
 * @author qinjianc
 *
 */

public class BCPSourceFolder
{
  private IFolder outFolder;
  private IFolder srcFolder;

  public IFolder getOutFolder()
  {
    return this.outFolder;
  }

  public void setOutFolder(IFolder outFolder)
  {
    this.outFolder = outFolder;
  }

  public IFolder getSrcFolder()
  {
    return this.srcFolder;
  }

  public void setSrcFolder(IFolder srcFolder)
  {
    this.srcFolder = srcFolder;
  }

  public BCPSourceFolder()
  {
  }

  public BCPSourceFolder(IFolder srcFolder, IFolder outFolder)
  {
    this.outFolder = outFolder;
    this.srcFolder = srcFolder;
  }
}