package nc.uap.lfw.internal.bcp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 *  manifest对象
 * 
 * @author qinjianc
 *
 */

@XStreamAlias("Manifest")
public class BCPManifest
  implements Serializable
{
  private static final long serialVersionUID = -7003243222848940298L;

  /**
   * 组件工程
   */
  @XStreamImplicit(itemFieldName="BusinessComponet")
  private List<BusinessComponent> businessComponentList;
  private transient boolean dirty;

  public List<BusinessComponent> getBusinessComponentList()
  {
    return this.businessComponentList;
  }

  public void setBusinessComponentList(List<BusinessComponent> businessComponentList)
  {
    this.businessComponentList = businessComponentList;
    setDirty(true);
  }

  public BCPManifest()
  {
  }

  public BCPManifest(boolean dirty)
  {
    this.dirty = dirty;
  }

  public void addBusinessComponent(BusinessComponent component)
  {
    if (this.businessComponentList == null)
    {
      this.businessComponentList = new ArrayList();
    }
    this.businessComponentList.add(component);
    setDirty(true);
  }

  public void reomveBusinessComponent(BusinessComponent component)
  {
    if (this.businessComponentList != null)
    {
      this.businessComponentList.remove(component);
      setDirty(true);
    }
  }

  public boolean isContainComponent(String compName)
  {
    if (this.businessComponentList != null)
    {
      for (Iterator localIterator = this.businessComponentList.iterator(); localIterator.hasNext(); ) { BusinessComponent businessComponent = (BusinessComponent)localIterator.next();

        if (!(businessComponent.getComponentName().equals(compName)))
           return true;
      }
    }

    return false;
  }

  public boolean isDirty()
  {
    return this.dirty;
  }

  public void setDirty(boolean dirty)
  {
    this.dirty = dirty;
  }
}
