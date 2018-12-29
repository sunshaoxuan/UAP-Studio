package nc.uap.lfw.build.transport;

import nc.uap.lfw.build.dbscript.itf.ISqlFile;

/**
 * 文件输送接口。对源文件进行处理，如：
 * <ul>
 * 	<li>上传到版本控制工具</li>
 * 	<li>输送到文件系统其它路径或网络</li>
 * </ul>
 * 
 * @author PH
 */
public interface ITransporter {
	
	/**
	 * 对源文件进行处理。
	 * 
	 * @param srcFile
	 */
	public void handle(ISqlFile[] srcFile);
	
	/**
	 * 获取具体操作描述。
	 * 
	 * @return
	 */
	public String getOperateDesc();

}
