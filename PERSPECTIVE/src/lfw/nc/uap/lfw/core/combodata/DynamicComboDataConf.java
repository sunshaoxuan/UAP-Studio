package nc.uap.lfw.core.combodata;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.design.view.ClassPathProvider;
import nc.uap.lfw.util.LfwClassUtil;
import uap.lfw.core.env.EnvProvider;
import uap.lfw.core.ml.LfwResBundle;

/**
 * ��̬�����б�ؼ������ý�ģ�࣬�����ý���ʱʹ�ø����������״̬��
 * ���⣬������Ϊ�������û����õĶ�̬�����б�������ʵ������
 *
 */
public class DynamicComboDataConf extends DynamicComboData {

	private static final long serialVersionUID = -1023276292907278981L;
	
	private String className = null;
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public CombItem[] createCombItems() {
		if(this.className == null)
			return null;
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(ClassPathProvider.getClassLoader());
			DynamicComboData dynamic = (DynamicComboData)Class.forName(className, true, ClassPathProvider.getClassLoader()).newInstance();			
//			DynamicComboData dynamic = (DynamicComboData)LfwClassUtil.newInstance(className);
			return dynamic.getAllCombItems();
			
		} catch (Exception e) {
			throw new LfwRuntimeException(e.getMessage(), e);
		}
		finally{
			Thread.currentThread().setContextClassLoader(currentLoader);
		}
//		return null;
	}
	
	public void validate(){
		StringBuffer buffer = new StringBuffer();
		if(this.getId() == null || this.getId().equals("")){
			buffer.append(LfwResBundle.getInstance().getStrByID("model", "DynamicComboDataConf-000000")/*��̬��������ID����Ϊ��!*/);
			//buffer.append(LfwResBundle.getInstance().getStrByID("bc", "DynamicComboDataConf-000000")/*��̬��������ID����Ϊ��!\r\n*/);
		}
		if(this.getClassName() == null || this.getClassName().equals("")){
			buffer.append(LfwResBundle.getInstance().getStrByID("model", "DynamicComboDataConf-000001")/*��̬�������ݵ�ʵ���಻��Ϊ��!*/);
			//buffer.append(LfwResBundle.getInstance().getStrByID("bc", "DynamicComboDataConf-000001")/*��̬�������ݵ�ʵ���಻��Ϊ��!\r\n*/);
		}
		if(buffer.length() > 0)
			throw new  LfwRuntimeException(buffer.toString());
	}	
}
