package nc.uap.lfw.internal.bcp;

import nc.uap.lfw.lang.M_internal;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author qinjianc
 *
 */
public class BusinessComponentComposite extends Composite
  implements KeyListener
{
  private Label bcpNameLabel;
  private Text bcpNameText;
  private Label bcpDisplayName;
  private Text bcpDisplayText;
  private BCPObservable observable;

  public BusinessComponentComposite(Composite parent, int style)
  {
    super(parent, style);
    initUI();
  }

  private void initUI()
  {
    setLayout(new GridLayout(2, false));
    this.bcpNameLabel = new Label(this, 0);
    this.bcpNameLabel.setText(M_internal.BusinessComponentComposite_0);
    this.bcpNameText = new Text(this, 2048);
    this.bcpNameText.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
    this.bcpDisplayName = new Label(this, 0);
    this.bcpDisplayName.setText(M_internal.BusinessComponentComposite_1);
    this.bcpNameText.addKeyListener(this);
    this.bcpDisplayText = new Text(this, 2048);
    this.bcpDisplayText.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
  }

  public Text getBcpDisplayText()
  {
    return this.bcpDisplayText;
  }

  public Text getBcpNameText()
  {
    return this.bcpNameText;
  }

  public void keyPressed(KeyEvent e)
  {
  }

  public void keyReleased(KeyEvent e)
  {
    if (this.observable != null)
    {
      this.observable.setChanged();
      this.observable.notifyObservers();
    }
  }

  public void setObservable(BCPObservable observable)
  {
    this.observable = observable;
  }
}