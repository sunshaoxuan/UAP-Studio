package nc.uap.lfw.core.combodata;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.design.view.ClassPathProvider;
import nc.uap.lfw.util.LfwClassUtil;
import uap.lfw.core.env.EnvProvider;
import uap.lfw.core.ml.LfwResBundle;

/**
 * 动态下拉列表控件的配置建模类，在配置解析时使用该类进行数据状态，
 * 另外，该类作为代理，将用户配置的动态下拉列表框类进行实例化。
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
			buffer.append(LfwResBundle.getInstance().getStrByID("model", "DynamicComboDataConf-000000")/*动态下拉数据ID不能为空!*/);
			//buffer.append(LfwResBundle.getInstance().getStrByID("bc", "DynamicComboDataConf-000000")/*动态下拉数据ID不能为空!\r\n*/);
		}
		if(this.getClassName() == null || this.getClassName().equals("")){
			buffer.append(LfwResBundle.getInstance().getStrByID("model", "DynamicComboDataConf-000001")/*动态下拉数据的实现类不能为空!*/);
			//buffer.append(LfwResBundle.getInstance().getStrByID("bc", "DynamicComboDataConf-000001")/*动态下拉数据的实现类不能为空!\r\n*/);
		}
		if(buffer.length() > 0)
			throw new  LfwRuntimeException(buffer.toString());
	}	
}
