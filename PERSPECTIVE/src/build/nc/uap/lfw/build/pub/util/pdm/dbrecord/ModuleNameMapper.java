package nc.uap.lfw.build.pub.util.pdm.dbrecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import nc.lfw.lfwtools.perspective.MainPlugin;

/**
 * ģ������ӳ����
 * ���ڴ���װ���е�ģ������CC������ģ������ӳ��
 * # ģ������CC�Ͻű�Ŀ¼����Ӧ��ϵ:  ģ����=Ŀ¼�����ļ���Ĭ��ΪmoduleNameMap.properties
 * @author syang
 *
 */
public class ModuleNameMapper {
	private static String CONFIG_FILE_NAME = "moduleNameMap.properties";
	private Properties mapProperties;
	public ModuleNameMapper(String configFilePath){
		File file = new File(configFilePath + File.separator + CONFIG_FILE_NAME);
		if(file.exists()){
			mapProperties = new Properties();
			FileInputStream in = null;
			try{
				in = new FileInputStream(file);
				mapProperties.load(in);
			}catch(IOException e){
				MainPlugin.getDefault().logError(e.getMessage(), e);
				throw new RuntimeException(e.getMessage(), e);
			}
			finally{
				try {
					in.close();
				} catch (IOException e) {
					MainPlugin.getDefault().logError(e.getMessage(), e);
				}
			}
		}
	}
	
	public String getCCModuleName(String moduleName){
		if(moduleName != null && mapProperties != null){
			return mapProperties.getProperty(moduleName);
		}else{
			return null;
		}
	}
	
	public String getModuleName(String ccModuleName){
		if(ccModuleName != null && mapProperties != null){
			Enumeration<Object> enume = mapProperties.keys();
			if(enume != null){
				while(enume.hasMoreElements()){
					String moduleName = (String)enume.nextElement();
					String value = mapProperties.getProperty(moduleName);
					if(ccModuleName.equalsIgnoreCase(value)){
						return moduleName;
					}
				}
				return null;
			}
			return null;
		}else{
			return null;
		}
	}
}
