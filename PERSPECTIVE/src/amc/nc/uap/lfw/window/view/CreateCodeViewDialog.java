/**
 * 
 */
package nc.uap.lfw.window.view;

import java.util.Map;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.lang.M_window;
import nc.uap.lfw.perspective.project.ILFWTreeNode;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
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
 */
public class CreateCodeViewDialog extends DialogWithTitle {
	
	/**
	 * ID输入框
	 */
	private Text idText;
	/**
	 * 名称输入框
	 */
	private Text nameText;
	/**
	 * Provider类全路径输入框
	 */
	private Text providerText;
	
	/**
	 * UI Provider类全路径输入框
	 */
	private Text uiProviderText;
	
	/**
	 * WindowID
	 */
	private String id;
	/**
	 * Window名称
	 */
	private String name;
	
	/**
	 * 个性化
	 */
	private Button flowlayoutRadio;
	/**
	 * Controller类全路径 
	 */
	private String providerClazz;
	private String uiProviderClazz;
	private boolean isCreateView;
	private boolean isNotRefPublicView;
	private boolean canFreeDesign = false;
	
	public CreateCodeViewDialog(String title) {
		super(null, title);
	}
	
	public CreateCodeViewDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		//id
		Label idLabel = new Label(container, SWT.NONE);
		idLabel.setText(M_window.CreateWindowDialog_0);
		idText = new Text(container, SWT.NONE);
		idText.setLayoutData(new GridData(220,20));
		idText.setText(""); //$NON-NLS-1$
		//名称
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText(M_window.CreateCodeViewDialog_0);
		nameText = new Text(container, SWT.NONE);
		nameText.setLayoutData(new GridData(220,20));
		nameText.setText(""); //$NON-NLS-1$
		//Controller类全路径
		Label controllerLabel = new Label(container, SWT.NONE);
		controllerLabel.setText(M_window.CreateCodeViewDialog_1);
		providerText = new Text(container, SWT.NONE);
		providerText.setLayoutData(new GridData(220,20));
		providerText.setText(""); //$NON-NLS-1$
		
		//Controller类全路径
		Label uiProviderLabel = new Label(container, SWT.NONE);
		uiProviderLabel.setText(M_window.CreateCodeViewDialog_2);
		uiProviderText = new Text(container, SWT.NONE);
		uiProviderText.setLayoutData(new GridData(220,20));
		uiProviderText.setText(""); //$NON-NLS-1$
		//个性化
		new Label(container,SWT.NONE).setText(M_window.CreateNormalViewDialog_6);
		Composite radioContainer = new Composite(container, SWT.NONE);
		radioContainer.setLayout(new GridLayout(2, false));
		radioContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		flowlayoutRadio = new Button(radioContainer, SWT.RADIO);
		flowlayoutRadio.setSelection(false);
		flowlayoutRadio.setText(M_window.CreateNormalViewDialog_4);
		Button flowlayoutRadio2 = new Button(radioContainer, SWT.RADIO);
		flowlayoutRadio2.setSelection(true);
		flowlayoutRadio2.setText(M_window.CreateNormalViewDialog_5);
		return container;
	}
	
	protected void okPressed() {
		if(idText.getText() == null || idText.getText().trim().length() == 0){
			MessageDialog.openError(null, "错误提示", "Id不能为空");
			return;
		}
		setId(idText.getText().trim());
		
		//Name校验
		if(nameText.getText() == null || nameText.getText().trim().length() == 0){
			MessageDialog.openError(null, M_window.CreateCodeViewDialog_3, M_window.CreateCodeViewDialog_4);
			return;
		}
		setName(nameText.getText().trim());
		if(isCreateView){//创建View
			try{
				LFWTool.createNodeCheck(name, ILFWTreeNode.VIEW);
			}
			catch(Exception e){
				MainPlugin.getDefault().logError(e.getMessage(), e);
				MessageDialog.openError(null, M_window.CreateCodeViewDialog_3, e.getMessage());
				return;
			}
		}
		else{//创建PublicView
			Map<String, LfwView> publicViews = LFWAMCPersTool.getPublicViewsByContext();
			if(publicViews != null && publicViews.get(name) != null){
				MessageDialog.openError(null, WEBPersConstants.NEW_PUBLIC_VIEW, M_window.CreateCodeViewDialog_5 + name + M_window.CreateCodeViewDialog_6 + WEBPersConstants.PUBLIC_VIEW_SUB + "!"); //$NON-NLS-3$
				return;
			}
		}
		if(isNotRefPublicView){
			//Controller类校验
			if(providerText.getText().trim().length() == 0){
				MessageDialog.openError(null, M_window.CreateCodeViewDialog_3, M_window.CreateCodeViewDialog_7);
				return;
			}
			providerText.setText(LFWTool.upperClassName(providerText.getText().trim()));
			setProviderClazz(providerText.getText().trim());
			
			//Controller类校验
			if(uiProviderText.getText().trim().length() == 0){
				MessageDialog.openError(null, M_window.CreateCodeViewDialog_3, M_window.CreateCodeViewDialog_8);
				return;
			}
			uiProviderText.setText(LFWTool.upperClassName(uiProviderText.getText().trim()));
			setUiProviderClazz(uiProviderText.getText().trim());
			try{
				LFWTool.clazzCheck(providerClazz);
			}catch(Exception e){
				MessageDialog.openError(null, M_window.CreateCodeViewDialog_3, e.getMessage());
				return;
			}
			setCanFreeDesign(flowlayoutRadio.getSelection());
		}
		else{
			setProviderClazz(""); //$NON-NLS-1$
			setUiProviderClazz(""); //$NON-NLS-1$
		}
		super.okPressed();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProviderClazz() {
		return providerClazz;
	}

	public void setProviderClazz(String controllerClazz) {
		this.providerClazz = controllerClazz;
	}


	public void setNotRefPublicView(boolean isNotRefPublicView) {
		this.isNotRefPublicView = isNotRefPublicView;
	}

	public void setCreateView(boolean isCreateView) {
		this.isCreateView = isCreateView;
	}

	public String getUiProviderClazz() {
		return uiProviderClazz;
	}

	public void setUiProviderClazz(String uiProviderClazz) {
		this.uiProviderClazz = uiProviderClazz;
	}
	public boolean isCanFreeDesign() {
		return canFreeDesign;
	}

	public void setCanFreeDesign(boolean canFreeDesign) {
		this.canFreeDesign = canFreeDesign;
	}

}
