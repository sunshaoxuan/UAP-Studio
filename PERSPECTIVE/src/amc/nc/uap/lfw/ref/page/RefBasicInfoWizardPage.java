package nc.uap.lfw.ref.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.uap.lfw.common.StringUtility;
import nc.uap.lfw.core.ref.LfwRefInfoVO;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.ref.model.IConst;
import nc.uap.lfw.ref.model.RefTypeEnum;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class RefBasicInfoWizardPage extends AbstractNewRefWizardPage{

	private Text refCodeText = null;
	
	private Text refNameText = null;
	
	private Combo refTypeText = null;
	
	private Text refControllerText = null;
	private Text refModelText = null;

	private Text packageText;
	
	private Text package2Text;
	
	public RefBasicInfoWizardPage(Map<String, Object> context, String pageName) {
		super(context, pageName);
		this.setTitle(M_editor.RefBasicInfoWizardPage_0);
		this.setDescription(M_editor.RefBasicInfoWizardPage_1);
	}

	@Override
	protected void createUI(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label codeLabel = new Label(container, SWT.NONE);
		codeLabel.setText(M_editor.RefBasicInfoWizardPage_2);
		refCodeText = new Text(container, SWT.NONE);
		GridData gd_refCodeText = new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 2, 1);
		gd_refCodeText.heightHint = 20;
		gd_refCodeText.widthHint = 250;
		refCodeText.setLayoutData(gd_refCodeText);
		
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText(M_editor.RefBasicInfoWizardPage_3);
		refNameText = new Text(container, SWT.NONE);
		GridData gd_refNameText = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 2, 1);
		gd_refNameText.heightHint = 20;
		gd_refNameText.widthHint = 250;
		refNameText.setLayoutData(gd_refNameText);
		
		Label typeLabel = new Label(container, SWT.NONE);
		typeLabel.setText(M_editor.RefBasicInfoWizardPage_4);
		refTypeText = new Combo(container, SWT.READ_ONLY);
		refTypeText.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 2, 1));
		refTypeText.setItems(getTypeItems());
		refTypeText.select(0);
		
		Label modelLabel = new Label(container, SWT.NONE);
		modelLabel.setText(M_editor.RefBasicInfoWizardPage_5);
		package2Text = new Text(container, SWT.BORDER | SWT.RIGHT);
		GridData gd_package2Text = new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1);
		gd_package2Text.heightHint = 15;
		gd_package2Text.widthHint = 100;
		package2Text.setLayoutData(gd_package2Text);
		package2Text.setText(IConst.PACKAGE+"."); //$NON-NLS-1$
		package2Text.setEditable(true);
		refModelText = new Text(container, SWT.NONE);
		GridData gd_refModelText = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_refModelText.heightHint = 20;
		gd_refModelText.widthHint = 220;
		refModelText.setLayoutData(gd_refModelText);
		refModelText.setText("model."+getfixbytype(refTypeText.getSelectionIndex())+"RefModel"); //$NON-NLS-1$ //$NON-NLS-2$
		
		Label classLabel = new Label(container, SWT.NONE);
		classLabel.setText(M_editor.RefBasicInfoWizardPage_6);
		packageText = new Text(container, SWT.BORDER | SWT.RIGHT);
		GridData gd_packageText = new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1);
		gd_packageText.heightHint = 15;
		gd_packageText.widthHint = 100;
		packageText.setLayoutData(gd_packageText);
		packageText.setText(IConst.PACKAGE+"."); //$NON-NLS-1$
		packageText.setEditable(true);
		refControllerText = new Text(container, SWT.NONE);
		GridData gd_refControllerText = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_refControllerText.heightHint = 20;
		gd_refControllerText.widthHint = 220;
		refControllerText.setLayoutData(gd_refControllerText);
		refControllerText.setText("control."+getfixbytype(refTypeText.getSelectionIndex())+"RefController"); //$NON-NLS-1$ //$NON-NLS-2$
		
		refCodeText.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(refCodeText.getText().trim().length()>0){
					String upper = toUpperFirst(refCodeText.getText());
					refNameText.setText(refCodeText.getText());
					packageText.setText(IConst.PACKAGE+"."+refCodeText.getText()+"."); //$NON-NLS-1$ //$NON-NLS-2$
					package2Text.setText(IConst.PACKAGE+"."+refCodeText.getText()+"."); //$NON-NLS-1$ //$NON-NLS-2$
					refControllerText.setText("control."+upper+getfixbytype(refTypeText.getSelectionIndex())+"RefController"); //$NON-NLS-1$ //$NON-NLS-2$
					refModelText.setText("model."+upper+getfixbytype(refTypeText.getSelectionIndex())+"RefModel"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		refTypeText.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				String upper = toUpperFirst(refCodeText.getText());
				refControllerText.setText("control."+upper+getfixbytype(refTypeText.getSelectionIndex())+"RefController"); //$NON-NLS-1$ //$NON-NLS-2$
				refModelText.setText("model."+upper+getfixbytype(refTypeText.getSelectionIndex())+"RefModel"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private String getfixbytype(int type){
		String fix = null;
		for(RefTypeEnum reftype:RefTypeEnum.values()){
			if(type==reftype.ordinal()){
				fix = reftype.name(); 
			}
		}
		return fix;
	}
	
	private String[] getTypeItems(){
		List<String> list = new ArrayList<String>();
		for(RefTypeEnum reftype:RefTypeEnum.values()){
			list.add(reftype.getName());
		}
		return (String[]) list.toArray(new String[0]);
	}
	
	private String toUpperFirst(String str){
		if(checkCode(refCodeText.getText().trim())){
			String c = str.substring(0, 1);
			String o = str.substring(1, str.length());
			String codePat = "[a-z]"; //$NON-NLS-1$
			Pattern pattern = Pattern.compile(codePat);
			Matcher matcher = pattern.matcher(c);
			if(matcher.matches()){
				c = c.toUpperCase();
				return c+o;
			}
			return str;
		}
		return str;
	}
	
	private boolean checkCode(String str){
		String codePat = "[a-zA-Z]+"; //$NON-NLS-1$
		Pattern pattern = Pattern.compile(codePat);
		Matcher matcher = pattern.matcher(str);
		if(!matcher.matches()){
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isPageComplete() {
		if (refCodeText.getText().trim().equals("")||refNameText.getText().trim().equals("") //$NON-NLS-1$ //$NON-NLS-2$
				||refModelText.getText().trim().equals("")||refControllerText.getText().trim().equals("")) {  //$NON-NLS-1$ //$NON-NLS-2$
			return false;
		}
		if(!checkCode(refCodeText.getText().trim())){
			return false;
		}
		return true;
	}

	@Override
	public void doUpdateModel() {
		Object object = context.get(IConst.REF_INFO);
		if (object == null || !(object instanceof LfwRefInfoVO)) {
			object = new LfwRefInfoVO();
			context.put(IConst.REF_INFO, object);
		}
		LfwRefInfoVO refinfo = (LfwRefInfoVO) object;
		refinfo.setCode(refCodeText.getText());
		refinfo.setName(refNameText.getText());
		refinfo.setResid(refCodeText.getText());
		refinfo.setResidPath("ref"); //$NON-NLS-1$
		refinfo.setRefType(refTypeText.getSelectionIndex());
		refinfo.setRefclass(package2Text.getText()+refModelText.getText());
		refinfo.setRefControlClass(packageText.getText()+refControllerText.getText());
	}

	@Override
	protected void validatePage() {
		String code = refCodeText.getText();
		if (code.trim().length() == 0) {
			setErrorMessage(M_editor.RefBasicInfoWizardPage_7);
			setPageComplete(false);
			return;
		}
		if(!checkCode(refCodeText.getText().trim())){
			setErrorMessage(M_editor.RefBasicInfoWizardPage_8);
			setPageComplete(false);
			return;
		}
		String name = refNameText.getText();
		if (name.trim().length() == 0) {
			setErrorMessage(M_editor.RefBasicInfoWizardPage_9);
			setPageComplete(false);
			return;
		}
		String modelText = refModelText.getText();
		if (modelText.trim().length() == 0) {
			setErrorMessage(M_editor.RefBasicInfoWizardPage_10);
			setPageComplete(false);
			return;
		}
		String controlText = refControllerText.getText();
		if (controlText.trim().length() == 0) {
			setErrorMessage(M_editor.RefBasicInfoWizardPage_11);
			setPageComplete(false);
			return;
		}
		super.validatePage();
	}

	@Override
	protected Control[] getModifyListenerControls() {
		return new Control[] { refCodeText,refNameText,refModelText,refControllerText };
	}

}
