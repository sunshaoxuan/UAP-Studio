package nc.uap.lfw.editor.extNode;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.lang.M_editor;
import nc.uap.wfm.vo.WfmFlwCatVO;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class WfmFlwCateDialog extends DialogWithTitle{
	
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
	
	private String typeName;
	
	private String typeCode;
	
	private WfmFlwCatVO flwCate;
	
	public WfmFlwCateDialog(String title) {
		super(null, title);
	}
	public WfmFlwCateDialog(String title,WfmFlwCatVO flwCate) {
		super(null, title);
		this.flwCate = flwCate;
	}

	public WfmFlwCateDialog(Shell parentShell, String title) {
		super(parentShell, title);
		// TODO Auto-generated constructor stub
	}
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label codeLabel = new Label(container, SWT.NONE);
		codeLabel.setText(M_editor.WfmFlwCateDialog_1);
		typeCodeText = new Text(container, SWT.NONE);
		typeCodeText.setLayoutData(new GridData(220,20));
		
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText(M_editor.WfmFlwCateDialog_0);
		typeNameText = new Text(container, SWT.NONE);
		typeNameText.setLayoutData(new GridData(220,20));

		if(flwCate!=null){
			typeCodeText.setText(flwCate.getCatcode());
			typeNameText.setText(flwCate.getCatname());
		}
		else{
			typeCodeText.setText(""); //$NON-NLS-1$
			typeNameText.setText(""); //$NON-NLS-1$
		}
		return container;
	}
	protected void okPressed() {
		
		if(typeNameText.getText() == null || typeNameText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_editor.WfmFlwCateDialog_2, M_editor.WfmFlwCateDialog_3);
			return;
		}
		setTypeName(typeNameText.getText().trim());
		
		if(typeCodeText.getText() == null || typeCodeText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_editor.WfmFlwCateDialog_2, M_editor.WfmFlwCateDialog_3);
			return;
		}
		setTypeCode(typeCodeText.getText().trim());		
		
		super.okPressed();
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
	public WfmFlwCatVO getFlwCate() {
		return flwCate;
	}
	public void setFlwCate(WfmFlwCatVO flwCate) {
		this.flwCate = flwCate;
	}
	

	
}
