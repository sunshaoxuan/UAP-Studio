package nc.uap.lfw.internal.bcp;

import java.util.Observable;

/**
 * �½��齨���̹۲�
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
