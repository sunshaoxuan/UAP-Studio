package uap.lfw.smartmw.mw;

import java.util.Vector;

import nc.vo.framework.rsa.Encode;
import nc.vo.jcom.xml.XMLToObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 管理服务器的配置
 *  
 */
public class MiddleProperty implements IMiddleProperty {
	private static Log log = LogFactory.getLog("SMART");

	private MiddleParameter mp = null;

	public MiddleProperty(String fileName) throws Exception {
		super();
		loadAll(fileName);
	}

	public ServiceProp[] getAllExternService() {
		Vector<ServiceProp> v = new Vector<ServiceProp>();
		if (mp.internalServiceArray != null)
			for (int i = 0; i < mp.internalServiceArray.length; i++) {
				v.addElement(mp.internalServiceArray[i]);
			}
		if (mp.externServiceArray != null)
			for (int i = 0; i < mp.externServiceArray.length; i++) {
				v.addElement(mp.externServiceArray[i]);
			}
		return (ServiceProp[]) Convertor.convertVectorToArray(v,
				ServiceProp.class);
	}

	public MiddleParameter getMiddleParameter() {
		return mp;
	}

	private void loadAll(String fileName) throws Exception {
		mp = (MiddleParameter) XMLToObject.getJavaObjectFromFile(fileName,
				MiddleParameter.class, true);
		if (mp.isEncode) {
			decode();
		}

	}

	private void decode() {
		Encode en = new Encode();
		if (mp != null) {
			if (mp.dataSource != null && mp.dataSource.length > 0) {
				for (int i = 0; i < mp.dataSource.length; i++) {
					String de = en.decode(mp.dataSource[i].password);
					if (de.equals("ufnull")) {
						mp.dataSource[i].password = "";
					} else
						mp.dataSource[i].password = de;
				}
			}
		}
		log.debug("加载数据源密码配置的密文信息.");
	}

}