package nc.uap.lfw.build.pub.util.pdm;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.exception.SDPBuildException;
import nc.uap.lfw.build.exception.SDPSystemMessage;
import nc.uap.lfw.build.pub.util.pdm.itf.IParse;
import nc.uap.lfw.build.pub.util.pdm.vo.PowerDesignerVO;

/**
 * PDM工具类，仅用于校验PDM的版本等信息 TODO: mazqa 应对校验方法进行修改，以支持更多的类型，可以用XML解析的方式代替现有的字节流读取验证
 * 
 * @author fanp
 */
public class PDMUtil implements IParse {
	/**
	 * 该方法实现对指定PDM文件的校验 校验规则: A. PDM文件格式是否为"XML"格式 B. 产生PDM文件所使用的Sybase
	 * PowerDesigner的版本是否为12.0.0.1700 C. PDM文件是否以SQL Server 2005数据库版本生成
	 * 
	 * @param pdm_file
	 * @return 如校验失败则抛出SDPBuildException异常; 如校验成功则正常返回
	 */
	public static void validate(File pdm_file) throws SDPBuildException {
		/* 解析PDM文件头指令 */
		PowerDesignerVO instruction_vo = new PowerDesignerVO();
		BufferedReader _in_ = FileUtil.getFileReader(pdm_file);
		try {
			boolean pdm_name_flag = false;
			boolean pdm_db_type_flag = false;
			boolean pdm_file_type_flag = false;
			boolean pdm_version_flag = false;

			String aline = _in_.readLine();
			while (aline != null) {
				if (aline.indexOf(IParse.PDM_INSTRUCTION_TAG) > 0) {
					/*
					 * 找到"powerdesigner"指令行, 解析"Name=", "signature=",
					 * "version=", "Target="
					 */
					aline = aline.substring(aline.indexOf(IParse.PDM_INSTRUCTION_TAG)).trim();

					if (aline.indexOf(IParse.PDM_NAME_TAG) >= 0) {
						/* 解析"Name="(IParse.PDM_NAME_TAG) */
						int i = aline.indexOf(IParse.PDM_NAME_TAG);
						String temp = aline.substring(i + IParse.PDM_NAME_TAG.length() + 2);
						int j = temp.indexOf("\""); // 字符串内部: '\'需要转义为'\\',
						// '"'需要转义为'\"'
						String pdm_name = temp.substring(0, j);
						instruction_vo.setName(pdm_name);

						/* 设置标志 */
						pdm_name_flag = true;
					}

					if (aline.indexOf(IParse.PDM_FILE_TYPE_TAG) >= 0) {
						/* 解析"signature="(IParse.PDM_FILE_TYPE_TAG) */
						int i = aline.indexOf(IParse.PDM_FILE_TYPE_TAG);
						String temp = aline.substring(i + IParse.PDM_FILE_TYPE_TAG.length() + 2);
						int j = temp.indexOf("\""); // 字符串内部: '\'需要转义为'\\',
						// '"'需要转义为'\"'
						String pdm_file_type = temp.substring(0, j);
						instruction_vo.setFileType(pdm_file_type);

						/* 设置标志 */
						pdm_file_type_flag = true;
					}

					if (aline.indexOf(IParse.PDM_DB_TYPE_TAG) >= 0) {
						/* 解析"Target="(IParse.PDM_DB_TYPE_TAG) */
						int i = aline.indexOf(IParse.PDM_DB_TYPE_TAG);
						String temp = aline.substring(i + IParse.PDM_DB_TYPE_TAG.length() + 2);
						int j = temp.indexOf("\""); // 字符串内部: '\'需要转义为'\\',
						// '"'需要转义为'\"'
						String pdm_db_type = temp.substring(0, j);
						instruction_vo.setDbtype(pdm_db_type);

						/* 设置标志 */
						pdm_db_type_flag = true;
					}

					if (aline.indexOf(IParse.PDM_VERSION_TAG) >= 0) {
						/* 解析"version="(IParse.PDM_VERSION_TAG) */
						int i = aline.indexOf(IParse.PDM_VERSION_TAG);
						String temp = aline.substring(i + IParse.PDM_VERSION_TAG.length() + 2);
						int j = temp.indexOf("\""); // 字符串内部: '\'需要转义为'\\',
						// '"'需要转义为'\"'
						String pdm_version = temp.substring(0, j);
						instruction_vo.setVersion(pdm_version);

						/* 设置标志 */
						pdm_version_flag = true;
					}
				}

				if (pdm_name_flag && pdm_file_type_flag && pdm_db_type_flag && pdm_version_flag)
					break;
				else
					aline = _in_.readLine();
			}// while
		} catch (IOException ioe) {
			MainPlugin.getDefault().logError(ioe.getMessage(), ioe);
		}

		/* 1. 校验PDM商业版本 */
		String msg = "当前解析的文件：" + pdm_file.getName() + " 出现错误！";
		if (!(instruction_vo.getVersion() != null && !instruction_vo.getVersion().trim().equalsIgnoreCase("") && instruction_vo
				.getVersion().trim().equalsIgnoreCase(IParse.PDM_VERSION))) {
			MainPlugin.getDefault().logError(msg);
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2002)) ;
		}

		/* 2. 校验PDM数据库类型 */
		if (!(instruction_vo.getDbtype() != null && !instruction_vo.getDbtype().trim().equalsIgnoreCase("") && instruction_vo
				.getDbtype().trim().equalsIgnoreCase(IParse.PDM_DB_TYPE))) {
			MainPlugin.getDefault().logError(msg);
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2003));
		}

		/* 3. 校验PDM文件类型 */
		if (!(instruction_vo.getFileType() != null && !instruction_vo.getFileType().trim().equalsIgnoreCase("") && instruction_vo
				.getFileType().trim().equalsIgnoreCase(IParse.PDM_FILE_TYPE))) {
			MainPlugin.getDefault().logError(msg);
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2004));
		}

		/* 校验通过 */
		return;
	}
}
