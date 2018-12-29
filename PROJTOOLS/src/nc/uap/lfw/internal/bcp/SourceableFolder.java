package nc.uap.lfw.internal.bcp;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * @author qinjianc
 *
 */
@XStreamAlias("src")
public class SourceableFolder
  implements Serializable
{
  private static final long serialVersionUID = -6412289212751562009L;

  @XStreamAsAttribute
  @XStreamAlias("name")
  private String folderName;

  public String getFolderName()
  {
    return this.folderName;
  }

  public void setFolderName(String folderName)
  {
    this.folderName = folderName;
  }

  public SourceableFolder()
  {
  }

  public SourceableFolder(String folderName)
  {
    this.folderName = folderName;
  }
}