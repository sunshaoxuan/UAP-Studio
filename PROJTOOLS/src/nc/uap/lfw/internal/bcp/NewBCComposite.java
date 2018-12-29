package nc.uap.lfw.internal.bcp;

import nc.uap.lfw.lang.M_internal;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author qinjianc
 *
 */
public class NewBCComposite extends Composite
{
  private BusinessComponentComposite businessComponentComposite;
  private Button createDefaultFolderButton;

  public NewBCComposite(Composite parent, int style)
  {
    super(parent, 0);
    initUI();
  }

  private void initUI()
  {
    setLayout(new GridLayout(1, false));
    setLayoutData(new GridData(1808));
    this.businessComponentComposite = new BusinessComponentComposite(this, 0);
    this.businessComponentComposite.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
    this.createDefaultFolderButton = new Button(this, 32);
    this.createDefaultFolderButton.setSelection(true);
    this.createDefaultFolderButton.setText(M_internal.NewBCComposite_0);
  }

  public BusinessComponentComposite getBusinessComponentComposite()
  {
    return this.businessComponentComposite;
  }

  public String getBcpName()
  {
    return this.businessComponentComposite.getBcpNameText().getText();
  }

  public boolean getSelection()
  {
    return this.createDefaultFolderButton.getSelection();
  }

  public String getBcpDisplay()
  {
    return this.businessComponentComposite.getBcpDisplayText().getText();
  }

  public void setObservable(BCPObservable observable)
  {
    this.businessComponentComposite.setObservable(observable);
  }
}