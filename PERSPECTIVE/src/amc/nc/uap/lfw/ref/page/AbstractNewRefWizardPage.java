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
	 * ����˽�����֤�߼���
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
	 * ���������ĳ�ʼֵ���á�
	 */
	protected void initValueAfterUICreated(IPreferenceStore store) {

	}

	/**
	 * ����UI���档
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
	 * ִ�и���ģ�͵Ĳ�����
	 */
	protected abstract void doUpdateModel();

	/**
	 * updateModel����� ��һ�����ڻ���һЩֵ��
	 * 
	 * @param store
	 */
	public void beforeUpdateModel(IPreferenceStore store) {

	}

	/**
	 * updateModel����� ��һ�����ڻ���һЩֵ��
	 * 
	 * @param store
	 */
	public void afterUpdateModel(IPreferenceStore store) {

	}


	/**
	 * У��ҳ�棻�������ô�����Ϣ�Լ�Page Complete��Ϣ��
	 */
	protected void validatePage() {
		setErrorMessage(null);
		setPageComplete(true);
	}

	/**
	 * ��ȡ��Ҫע��У��Ŀؼ���
	 * 
	 * @return controls
	 */
	protected Control[] getModifyListenerControls() {
		return new Control[0];
	}

	/**
	 * ΪStringBuilder�ṩͳһ��׷����Ϣ�ķ�����һ�����ڷ���
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
	 * ҳ������Ϊ����ʾʱ���á�
	 */
	protected void leavePage() {

	}

	/**
	 * ҳ������Ϊ��ʾʱ���á�
	 */
	protected void enterPage() {

	}
}
