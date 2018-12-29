package nc.uap.lfw.internal.bcp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * @author qinjianc
 *
 */
public class SourceableFolders
  implements Serializable
{
  private static final long serialVersionUID = -6412289212751562009L;

  @XStreamImplicit(itemFieldName="src")
  private List<SourceableFolder> folderList;

  public List<SourceableFolder> getFolderList()
  {
    return this.folderList;
  }

  public void setFolderList(List<SourceableFolder> folderList)
  {
    this.folderList = folderList;
  }

  public void addSourceableFolder(SourceableFolder folder)
  {
    if (this.folderList == null)
    {
      this.folderList = new ArrayList();
    }
    this.folderList.add(folder);
  }

  public void removeSourceableFolder(SourceableFolder folder)
  {
    if (this.folderList == null)
    {
      this.folderList = new ArrayList();
    }
    this.folderList.remove(folder);
  }
}