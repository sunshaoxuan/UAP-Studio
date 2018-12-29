package nc.uap.lfw.md.component;

import nc.uap.lfw.lang.M_lfw_component;
import nc.uap.studio.common.core.developer.DevelopPubServiceFactory;
import nc.uap.studio.common.core.developer.IDevelopPubService;
import nc.uap.studio.common.core.developer.vo.DevelopOrg;
import nc.uap.studio.common.core.developer.vo.Developer;
import ncmdp.tool.basic.StringUtil;
import ncmdp.ui.industry.Industry;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewMdFileDialog extends TitleAreaDialog
{
  private Text fileNameText;
  private Text IndustryText;
  private Text partnerCodeText;
  private Text versionCodeText;
  private IProject project = null;
  private Industry industry = null;
  private String versiontype = ""; //$NON-NLS-1$
  private String fileName = ""; //$NON-NLS-1$
  private String programCode = ""; //$NON-NLS-1$

  private void setFileName(String fileName)
  {
    this.fileName = fileName;
  }

  public String getFileName() {
    return this.fileName;
  }

  public String getVersiontype() {
    return this.versiontype;
  }

  public String getProgramCode() {
    return this.programCode;
  }

  public Industry getIndustry() {
    return this.industry;
  }

  public NewMdFileDialog(Shell parentShell, IProject project) {
    super(parentShell);

    this.project = project;
  }

  protected void okPressed()
  {
    if (StringUtil.isEmptyWithTrim(getFileName())) {
      MessageDialog.openError(getShell(), M_lfw_component.NewMdFileDialog_0, M_lfw_component.NewMdFileDialog_1);
      return;
    }

    if (!("0".equalsIgnoreCase(getVersiontype()))) { //$NON-NLS-1$
      String tag = ("1".equalsIgnoreCase(getVersiontype())) ? getIndustry().getCode //$NON-NLS-1$
        () : getProgramCode();
      String suffix = "_" + getVersiontype() + tag; //$NON-NLS-1$
      if (!(getFileName().endsWith(suffix)))
        setFileName(getFileName() + suffix);

    }

    if ((!("0".equalsIgnoreCase(getVersiontype()))) &&  //$NON-NLS-1$
      (!("1".equalsIgnoreCase //$NON-NLS-1$
      (getVersiontype()))) && ((
      (StringUtil.isEmptyWithTrim(getProgramCode())) || 
      (getProgramCode().length() > 2)))) {
      MessageDialog.openError(getShell(), M_lfw_component.NewMdFileDialog_2, M_lfw_component.NewMdFileDialog_3);
      return;
    }

    super.okPressed();
  }

  protected Control createDialogArea(Composite parent)
  {
    setTitle(M_lfw_component.NewMdFileDialog_4);
    setMessage(M_lfw_component.NewMdFileDialog_5);

    Composite topComp = new Composite(parent, 0);
    topComp.setLayout(new GridLayout(3, false));

    GridData textData = new GridData(200, -1);
    textData.horizontalSpan = 2;
    Label fileNameLable = new Label(topComp, 0);
    fileNameLable.setText(M_lfw_component.NewMdFileDialog_6);
    this.fileNameText = new Text(topComp, 2048);
    this.fileNameText.setLayoutData(textData);
    this.fileNameText.addModifyListener(new ModifyListener() {
		
		@Override
		public void modifyText(ModifyEvent e) {
			setFileName(fileNameText.getText());
		}
	});
    
    Label industryLable = new Label(topComp, 0);
    industryLable.setText(M_lfw_component.NewMdFileDialog_7);
    this.IndustryText = new Text(topComp, 2048);
    GridData textData3 = new GridData(200, -1);
    textData3.horizontalSpan = 2;
    this.IndustryText.setLayoutData(textData3);

    String curIndustry = getCurIndustry();
    this.industry = new Industry(curIndustry, curIndustry);
    this.IndustryText.setText(curIndustry);
    this.IndustryText.setEditable(false);
    this.IndustryText.setEnabled(false);

    Label partnerLable = new Label(topComp, 0);
    partnerLable.setText(M_lfw_component.NewMdFileDialog_8);

    this.versionCodeText = new Text(topComp, 2048);
    initVersionCodeText();
    GridData textData4 = new GridData(200, -1);
    textData4.horizontalSpan = 2;
    this.versionCodeText.setLayoutData(textData4);

    Label partnerCodeLable = new Label(topComp, 0);
    partnerCodeLable.setText(M_lfw_component.NewMdFileDialog_9);

    this.partnerCodeText = new Text(topComp, 2048);
    initPartnerCodeText();

    return topComp;
  }

  private void initVersionCodeText() {
    GridData textData2 = new GridData(50, -1);
    this.versionCodeText.setLayoutData(textData2);
    this.versiontype = getDevInfo();
    this.versionCodeText.setText(this.versiontype);
    this.versionCodeText.setEditable(false);
    this.versionCodeText.setEnabled(false);
  }

  private void initPartnerCodeText() {
    GridData textData2 = new GridData(50, -1);
    this.partnerCodeText.setLayoutData(textData2);
    this.programCode = getDevCode();
    this.partnerCodeText.setText(this.programCode);
    this.partnerCodeText.setEditable(false);
    this.partnerCodeText.setEnabled(false);
  }
  public static String getCurIndustry()
  {
    try
    {
      return getCurIndustryPri();
    } catch (Exception e) {
      MessageDialog.openError(null, M_lfw_component.NewMdFileDialog_10, M_lfw_component.NewMdFileDialog_11 + e); }
    return "0"; //$NON-NLS-1$
  }

  private static String getCurIndustryPri()
  {
    Developer dev = DevelopPubServiceFactory.getService().getDeveloper();
    if (dev == null)
      return "0"; //$NON-NLS-1$

    String curIndustryPK = dev.getPk_industry();
    String curIndustry = DevelopPubServiceFactory.getService().getIndustryCode
      (curIndustryPK);
//    if (StringUtil.isEmptyWithTrim(curIndustry))
    if(curIndustry==null||curIndustry.equals("")) //$NON-NLS-1$
      curIndustry = "0"; //$NON-NLS-1$

    return curIndustry;
  }
  public static String getDevInfo()
  {
    try
    {
      return getDevInfoPri();
    } catch (Exception e) {
      MessageDialog.openError(null, M_lfw_component.NewMdFileDialog_10, M_lfw_component.NewMdFileDialog_12 + e); }
    return "0"; //$NON-NLS-1$
  }

  private static String getDevInfoPri()
  {
    Developer dev = DevelopPubServiceFactory.getService().getDeveloper();
    if (dev == null) {
      return "0"; //$NON-NLS-1$
    }

    String developerLay = dev.getAssetlayer();
    if (StringUtil.isEmptyWithTrim(developerLay))
      developerLay = "0"; //$NON-NLS-1$

    return developerLay;
  }
  public static String getDevCode()
  {
    try
    {
      return getDevCodePri();
    } catch (Exception e) {
      e.printStackTrace();
      MessageDialog.openError(null, M_lfw_component.NewMdFileDialog_10, M_lfw_component.NewMdFileDialog_13 + e); }
    return ""; //$NON-NLS-1$
  }

  private static String getDevCodePri()
  {
    IDevelopPubService service = DevelopPubServiceFactory.getService();
    DevelopOrg devOrg = service.getDevelopOrg();
    if (devOrg == null)
      return ""; //$NON-NLS-1$

    String devCode = service.getExtSuffix();
    if ((!(StringUtil.isEmptyWithTrim(devCode))) && (devCode.length() > 2)){
      devCode = devCode.substring(devCode.length() - 2, devCode.length());
      return devCode;
    }
    devCode = "0";

    return devCode;
  }
}
