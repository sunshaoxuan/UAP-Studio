package nc.uap.lfw.ref.page;

import java.util.Map;

import nc.lfw.lfwtools.perspective.MainPlugin;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public abstract class AbstractNewRefWizardPage extends WizardPage
		implements INewRefWizardPage {

	protected Listener modifyListener = new Listener() {

		@Override
		public void handleEvent(Event event) {
			validatePage();
		}
	};

	protected Map<String, Object> context;

	protected AbstractNewRefWizardPage(Map<String, Object> context,
			String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		this.context = context;
	}

	protected AbstractNewRefWizardPage(Map<String, Object> context,
			String pageName) {
		super(pageName);
		this.context = context;
	}

	/**
	 * 添加了界面验证逻辑。
	 */
	@Override
	public void createControl(Composite parent) {
		createUI(parent);
		validatePage();
		for (Control control : getModifyListenerControls()) {
			control.addListener(SWT.Modify | SWT.FocusOut, modifyListener);
		}
		initValueAfterUICreated(getPreferenceStore());
	}

	/**
	 * 创建界面后的初始值设置。
	 */
	protected void initValueAfterUICreated(IPreferenceStore store) {

	}

	/**
	 * 创建UI界面。
	 * 
	 * @param parent
	 */
	protected abstract void createUI(Composite parent);

	@Override
	public void updateModel() {
		beforeUpdateModel(getPreferenceStore());
		doUpdateModel();
		afterUpdateModel(getPreferenceStore());

	}

	/**
	 * 执行更新模型的操作。
	 */
	protected abstract void doUpdateModel();

	/**
	 * updateModel后调用 ，一般用于缓存一些值。
	 * 
	 * @param store
	 */
	public void beforeUpdateModel(IPreferenceStore store) {

	}

	/**
	 * updateModel后调用 ，一般用于缓存一些值。
	 * 
	 * @param store
	 */
	public void afterUpdateModel(IPreferenceStore store) {

	}


	/**
	 * 校验页面；包含设置错误信息以及Page Complete信息。
	 */
	protected void validatePage() {
		setErrorMessage(null);
		setPageComplete(true);
	}

	/**
	 * 获取需要注册校验的控件。
	 * 
	 * @return controls
	 */
	protected Control[] getModifyListenerControls() {
		return new Control[0];
	}

	/**
	 * 为StringBuilder提供统一的追加信息的方法，一般用于方法
	 * 
	 * @param sb
	 * @param info
	 */
	protected void appendMsg(StringBuilder sb, String info) {
		if (sb.toString().length() > 0) {
			sb.append("\r\n");
		}
		sb.append(info);
	}

	protected IPreferenceStore getPreferenceStore() {
		if (MainPlugin.getDefault() != null) {
			return MainPlugin.getDefault().getPreferenceStore();
		}
		return null;
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			IWizard wizard = this.getWizard();
			IWizardPage[] pages = wizard.getPages();
			for(IWizardPage page:pages){
				if(page instanceof INewRefWizardPage){
					INewRefWizardPage p = (INewRefWizardPage) page;
					p.updateModel();
				}
			}
			enterPage();
		} else {
			leavePage();
		}
		super.setVisible(visible);
	}

	/**
	 * 页面设置为不显示时调用。
	 */
	protected void leavePage() {

	}

	/**
	 * 页面设置为显示时调用。
	 */
	protected void enterPage() {

	}
}
