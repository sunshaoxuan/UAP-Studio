package nc.uap.lfw.editor.extNode;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.lang.M_editor;
import nc.uap.wfm.vo.WfmFlwTypeVO;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author qinjianc
 *
 */

public class WfmFlwTypeDialog extends DialogWithTitle{
	
	/**
	 * 
	 * 类型名称输入框
	 */
	private Text typeNameText;
	
	/**
	 * 
	 * 类型编码输入框
	 */
	private Text typeCodeText;
	
//	/**
//	 * 
//	 * 所属组织下拉框
//	 */
//	private Combo orgCombo;
	
	/**
	 * 
	 * 所属组织下拉框
	 */
	private Text clazzText;
	
	private String typeName;
	
	private String typeCode;
	
	private String org;
	
	private String clazzName;
	
	private WfmFlwTypeVO flwType;
	
	/**
	 * app应用id
	 */
	private String appId;
	
	/**
	 * 功能节点id
	 */
	private String appsNodeId;
	
	private FlwTypeAssistAdjuster adjuster;
	
	public WfmFlwTypeDialog(String title) {
		super(null, title);
	}
	public WfmFlwTypeDialog(String title,WfmFlwTypeVO flwType) {
		super(null, title);
		this.flwType = flwType;
	}

	public WfmFlwTypeDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label codeLabel = new Label(container, SWT.NONE);
		codeLabel.setText(M_editor.WfmFlwTypeDialog_1);
		typeCodeText = new Text(container, SWT.NONE);
		typeCodeText.setLayoutData(new GridData(180,20));
		
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText(M_editor.WfmFlwTypeDialog_0);
		typeNameText = new Text(container, SWT.NONE);
		typeNameText.setLayoutData(new GridData(180,20));

		Composite clazzContainer = new Composite(container, SWT.NONE);
		clazzContainer.setLayout(new GridLayout(3, false));
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		clazzContainer.setLayoutData(gd);
		
		Label clazzLabel = new Label(clazzContainer, SWT.NONE);
		clazzLabel.setText(M_editor.WfmFlwTypeDialog_2);
		clazzText = new Text(clazzContainer, SWT.NONE);
		clazzText.setLayoutData(new GridData(250,20));
		
		Button assBtn = new Button(clazzContainer, SWT.NONE);
		assBtn.setText(M_editor.WfmFlwTypeDialog_5);
		assBtn.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(typeNameText.getText() == null || typeNameText.getText().trim().length() == 0){
					MessageDialog.openError(null, M_editor.WfmFlwTypeDialog_3, M_editor.WfmFlwTypeDialog_4);
					return;
				}
				
				if(typeCodeText.getText() == null || typeCodeText.getText().trim().length() == 0){
					MessageDialog.openError(null, M_editor.WfmFlwTypeDialog_3, M_editor.WfmFlwTypeDialog_4);
					return;
				}
				WfmFlwTypeAppsAssistDialog appAssDialog = new WfmFlwTypeAppsAssistDialog(M_editor.WfmFlwTypeDialog_6);
				if(appAssDialog.open() == Dialog.OK){
					appId = appAssDialog.getAppId();
					appsNodeId = appAssDialog.getId();
					adjuster = new FlwTypeAssistAdjuster(appId, appsNodeId);
					String clazz = adjuster.getFormOperateClazzName();
					if(clazz != null && !clazz.equals("")){ //$NON-NLS-1$
						clazzText.setText(clazz);
					}
				}
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}
		});
		
		if(flwType!=null){
			typeCodeText.setText(flwType.getTypecode());
			typeNameText.setText(flwType.getTypename());
			clazzText.setText(flwType.getServerclass());
		}
		else{
			typeCodeText.setText(""); //$NON-NLS-1$
			typeNameText.setText(""); //$NON-NLS-1$
			clazzText.setText("nc.uap.wfm.dftimpl.DefaultFormOper"); //$NON-NLS-1$
		}
		
		
		return container;
	}
	
	protected void okPressed() {
		
		if(typeNameText.getText() == null || typeNameText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_editor.WfmFlwTypeDialog_3, M_editor.WfmFlwTypeDialog_4);
			return;
		}
		setTypeName(typeNameText.getText().trim());
		
		if(typeCodeText.getText() == null || typeCodeText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_editor.WfmFlwTypeDialog_3, M_editor.WfmFlwTypeDialog_4);
			return;
		}
		setTypeCode(typeCodeText.getText().trim());
		
		if(clazzText.getText() == null || clazzText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_editor.WfmFlwTypeDialog_3, M_editor.WfmFlwTypeDialog_4);
			return;
		}
		setClazzName(clazzText.getText().trim());
		
		super.okPressed();
		
		if(this.adjuster == null){
			this.adjuster = new FlwTypeAssistAdjuster(this.appId, this.appsNodeId);
		}
		this.adjuster.adjust(this.getTypeCode());
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppsNodeId() {
		return appsNodeId;
	}
	public void setAppsNodeId(String appsNodeId) {
		this.appsNodeId = appsNodeId;
	}
	
}
