package nc.uap.lfw.internal.bcp;

import java.util.Observable;

/**
 * 新建组建工程观察
 * @author qinjianc
 *
 */

public class BCPObservable extends Observable
{
  public synchronized void clearChanged()
  {
    super.clearChanged();
  }

  public synchronized void setChanged()
  {
    super.setChanged();
  }
}
