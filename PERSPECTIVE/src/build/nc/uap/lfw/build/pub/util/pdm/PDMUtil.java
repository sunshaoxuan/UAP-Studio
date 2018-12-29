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
 * PDM�����࣬������У��PDM�İ汾����Ϣ TODO: mazqa Ӧ��У�鷽�������޸ģ���֧�ָ�������ͣ�������XML�����ķ�ʽ�������е��ֽ�����ȡ��֤
 * 
 * @author fanp
 */
public class PDMUtil implements IParse {
	/**
	 * �÷���ʵ�ֶ�ָ��PDM�ļ���У�� У�����: A. PDM�ļ���ʽ�Ƿ�Ϊ"XML"��ʽ B. ����PDM�ļ���ʹ�õ�Sybase
	 * PowerDesigner�İ汾�Ƿ�Ϊ12.0.0.1700 C. PDM�ļ��Ƿ���SQL Server 2005���ݿ�汾����
	 * 
	 * @param pdm_file
	 * @return ��У��ʧ�����׳�SDPBuildException�쳣; ��У��ɹ�����������
	 */
	public static void validate(File pdm_file) throws SDPBuildException {
		/* ����PDM�ļ�ͷָ�� */
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
					 * �ҵ�"powerdesigner"ָ����, ����"Name=", "signature=",
					 * "version=", "Target="
					 */
					aline = aline.substring(aline.indexOf(IParse.PDM_INSTRUCTION_TAG)).trim();

					if (aline.indexOf(IParse.PDM_NAME_TAG) >= 0) {
						/* ����"Name="(IParse.PDM_NAME_TAG) */
						int i = aline.indexOf(IParse.PDM_NAME_TAG);
						String temp = aline.substring(i + IParse.PDM_NAME_TAG.length() + 2);
						int j = temp.indexOf("\""); // �ַ����ڲ�: '\'��Ҫת��Ϊ'\\',
						// '"'��Ҫת��Ϊ'\"'
						String pdm_name = temp.substring(0, j);
						instruction_vo.setName(pdm_name);

						/* ���ñ�־ */
						pdm_name_flag = true;
					}

					if (aline.indexOf(IParse.PDM_FILE_TYPE_TAG) >= 0) {
						/* ����"signature="(IParse.PDM_FILE_TYPE_TAG) */
						int i = aline.indexOf(IParse.PDM_FILE_TYPE_TAG);
						String temp = aline.substring(i + IParse.PDM_FILE_TYPE_TAG.length() + 2);
						int j = temp.indexOf("\""); // �ַ����ڲ�: '\'��Ҫת��Ϊ'\\',
						// '"'��Ҫת��Ϊ'\"'
						String pdm_file_type = temp.substring(0, j);
						instruction_vo.setFileType(pdm_file_type);

						/* ���ñ�־ */
						pdm_file_type_flag = true;
					}

					if (aline.indexOf(IParse.PDM_DB_TYPE_TAG) >= 0) {
						/* ����"Target="(IParse.PDM_DB_TYPE_TAG) */
						int i = aline.indexOf(IParse.PDM_DB_TYPE_TAG);
						String temp = aline.substring(i + IParse.PDM_DB_TYPE_TAG.length() + 2);
						int j = temp.indexOf("\""); // �ַ����ڲ�: '\'��Ҫת��Ϊ'\\',
						// '"'��Ҫת��Ϊ'\"'
						String pdm_db_type = temp.substring(0, j);
						instruction_vo.setDbtype(pdm_db_type);

						/* ���ñ�־ */
						pdm_db_type_flag = true;
					}

					if (aline.indexOf(IParse.PDM_VERSION_TAG) >= 0) {
						/* ����"version="(IParse.PDM_VERSION_TAG) */
						int i = aline.indexOf(IParse.PDM_VERSION_TAG);
						String temp = aline.substring(i + IParse.PDM_VERSION_TAG.length() + 2);
						int j = temp.indexOf("\""); // �ַ����ڲ�: '\'��Ҫת��Ϊ'\\',
						// '"'��Ҫת��Ϊ'\"'
						String pdm_version = temp.substring(0, j);
						instruction_vo.setVersion(pdm_version);

						/* ���ñ�־ */
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

		/* 1. У��PDM��ҵ�汾 */
		String msg = "��ǰ�������ļ���" + pdm_file.getName() + " ���ִ���";
		if (!(instruction_vo.getVersion() != null && !instruction_vo.getVersion().trim().equalsIgnoreCase("") && instruction_vo
				.getVersion().trim().equalsIgnoreCase(IParse.PDM_VERSION))) {
			MainPlugin.getDefault().logError(msg);
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2002)) ;
		}

		/* 2. У��PDM���ݿ����� */
		if (!(instruction_vo.getDbtype() != null && !instruction_vo.getDbtype().trim().equalsIgnoreCase("") && instruction_vo
				.getDbtype().trim().equalsIgnoreCase(IParse.PDM_DB_TYPE))) {
			MainPlugin.getDefault().logError(msg);
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2003));
		}

		/* 3. У��PDM�ļ����� */
		if (!(instruction_vo.getFileType() != null && !instruction_vo.getFileType().trim().equalsIgnoreCase("") && instruction_vo
				.getFileType().trim().equalsIgnoreCase(IParse.PDM_FILE_TYPE))) {
			MainPlugin.getDefault().logError(msg);
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2004));
		}

		/* У��ͨ�� */
		return;
	}
}
