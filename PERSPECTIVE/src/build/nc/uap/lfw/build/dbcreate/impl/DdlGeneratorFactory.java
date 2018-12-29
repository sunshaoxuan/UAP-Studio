package nc.uap.lfw.build.dbcreate.impl;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.dbcreate.itf.IDbCreateService.DatabaseType;
import nc.uap.lfw.build.dbcreate.pdm.Pdm.ViewInfo;
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.orm.mapping.IFkConstraint;
import nc.uap.lfw.build.orm.mapping.IIndex;
import nc.uap.lfw.build.orm.mapping.ITable;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * ����ű�������Factory��
 * 
 * @author PH
 */
public class DdlGeneratorFactory {
	
	private static final String CREATE_TABLE_TEMPL_NAME = "createTable.templ";
	private static final String CREATE_INDEX_TEMPL_NAME = "createIndex.templ";
	private static final String CREATE_VIEW_TEMPL_NAME = "createView.templ";
	private static final String CREATE_REFERENCE_TEMPL_NAME = "reference.templ";
	private static final String TEMPL_PATH_SQLSERVER = "nc/uap/lfw/build/dbcreate/vm/sqlserver/";
	private static final String TEMPL_PATH_ORACEL = "nc/uap/lfw/build/dbcreate/vm/oracle/";
	private static final String TEMPL_PATH_DB2 = "nc/uap/lfw/build/dbcreate/vm/db2/";
	
	/** key��dbType; value: IDdlGenerator */
	private static Map<DatabaseType, IDdlGenerator> ddlGeneratorMap = new HashMap<DatabaseType, IDdlGenerator>();
	
	/**
	 * Default constructor.
	 */
	private DdlGeneratorFactory(){
		
	}
	
	/**
	 * ��ȡDDL������ʵ����
	 * 
	 * @param dbType ���ݿ�����
	 * @return DDL������ʵ��
	 */
	public static IDdlGenerator getInstance(DatabaseType dbType){
		IDdlGenerator generator = ddlGeneratorMap.get(dbType);
		if(generator == null){
			synchronized (ddlGeneratorMap) {
				if((generator = ddlGeneratorMap.get(dbType)) == null){
					DdlGeneratorVelocityImpl temp = new DdlGeneratorVelocityImpl();
					if(DatabaseType.SQLSERVER == dbType){
						temp.createTableTempl = TEMPL_PATH_SQLSERVER + CREATE_TABLE_TEMPL_NAME;
						temp.createIndexTempl = TEMPL_PATH_SQLSERVER + CREATE_INDEX_TEMPL_NAME;
						temp.referenceTempl = TEMPL_PATH_SQLSERVER + CREATE_REFERENCE_TEMPL_NAME;
						temp.createViewTempl = TEMPL_PATH_SQLSERVER + CREATE_VIEW_TEMPL_NAME;
					}else if(DatabaseType.ORACLE == dbType){
						temp.createTableTempl = TEMPL_PATH_ORACEL + CREATE_TABLE_TEMPL_NAME;
						temp.createIndexTempl = TEMPL_PATH_ORACEL + CREATE_INDEX_TEMPL_NAME;
						temp.referenceTempl = TEMPL_PATH_ORACEL + CREATE_REFERENCE_TEMPL_NAME;
						temp.createViewTempl = TEMPL_PATH_ORACEL + CREATE_VIEW_TEMPL_NAME;
					}else if(DatabaseType.DB2 == dbType){
						temp.createTableTempl = TEMPL_PATH_DB2 + CREATE_TABLE_TEMPL_NAME;
						temp.createIndexTempl = TEMPL_PATH_DB2 + CREATE_INDEX_TEMPL_NAME;
						temp.referenceTempl = TEMPL_PATH_DB2 + CREATE_REFERENCE_TEMPL_NAME;
						temp.createViewTempl = TEMPL_PATH_DB2 + CREATE_VIEW_TEMPL_NAME;
					}else{
						throw new IllegalArgumentException("Unsupported dbType: " + dbType);
					}
					generator = temp;
					ddlGeneratorMap.put(dbType, generator);
				}
			}
		}
		return generator;
	}
	
	/**
	 * ����Velocity��DDL����ʵ�֡�
	 * 
	 * @author PH
	 */
	static class DdlGeneratorVelocityImpl implements IDdlGenerator{
		
		private String createTableTempl;
		
		private String createIndexTempl;
		
		private String createViewTempl;
		
		private String referenceTempl;
		
		private VelocityEngine ve;
		
		public DdlGeneratorVelocityImpl() {
			initVelocityEngine();
		}

		public DdlGeneratorVelocityImpl(String createTableTempl, String createIndexTempl, String createViewTempl, String referenceTempl) {
			this.createTableTempl = createTableTempl;
			this.createIndexTempl = createIndexTempl;
			this.createViewTempl = createViewTempl;
			this.referenceTempl = referenceTempl;
			initVelocityEngine();
		}

		public void geneCreateIndexDdl(List<IIndex> indexs, Writer writer) {
			try {
				ClassLoader ccl = Thread.currentThread().getContextClassLoader();
				Template template = null;
				try {
				    Thread.currentThread().setContextClassLoader(
				      this.getClass().getClassLoader());
				    template = ve.getTemplate(createIndexTempl, "UTF-8");
				 } finally {
				    Thread.currentThread().setContextClassLoader(ccl);
				 }
				VelocityContext vc = new VelocityContext();
				vc.put("indexs", indexs);
				template.merge(vc, writer);
			} catch (ResourceNotFoundException e) {
				MainPlugin.getDefault().logError("��������sqlʧ�ܡ�", e);
				throw new SdpBuildRuntimeException("ģ���ļ�" + createIndexTempl + "�����ڡ�");
			} catch (ParseErrorException e) {
				MainPlugin.getDefault().logError("��������sqlʧ�ܡ�", e);
				throw new SdpBuildRuntimeException("����ģ���ļ�" + createIndexTempl + "ʧ�ܡ�");
			} catch (Exception e) {
				MainPlugin.getDefault().logError("��������sqlʧ�ܡ�", e);
				throw new SdpBuildRuntimeException("����ģ���ļ�" + createIndexTempl + "δ֪����");
			}
		}

		public void geneCreateTableDdl(List<ITable> tables, Writer writer) {
			try {
				ClassLoader ccl = Thread.currentThread().getContextClassLoader();
				Template template;
				try {
				    Thread.currentThread().setContextClassLoader(
				      this.getClass().getClassLoader());
				     template = ve.getTemplate(createTableTempl, "UTF-8");
				   } finally {
				    Thread.currentThread().setContextClassLoader(ccl);
				   }
				VelocityContext vc = new VelocityContext();
				vc.put("tables", tables);
				template.merge(vc, writer);
			} catch (ResourceNotFoundException e) {
				MainPlugin.getDefault().logError("���ɽ���sqlʧ�ܡ�", e);
				throw new SdpBuildRuntimeException("ģ���ļ�" + createTableTempl + "�����ڡ�");
			} catch (ParseErrorException e) {
				MainPlugin.getDefault().logError("���ɽ���sqlʧ�ܡ�", e);
				throw new SdpBuildRuntimeException("����ģ���ļ�" + createTableTempl + "ʧ�ܡ�");
			} catch (Exception e) {
				MainPlugin.getDefault().logError("���ɽ���sqlʧ�ܡ�", e);
				throw new SdpBuildRuntimeException("����ģ���ļ�" + createTableTempl + "δ֪����");
			}
		}

		public void geneCreateViewDdl(List<ViewInfo> views, Writer writer) {
			try {
				ClassLoader ccl = Thread.currentThread().getContextClassLoader();
				Template template = null;
				try {
				    Thread.currentThread().setContextClassLoader(
				      this.getClass().getClassLoader());
				    template = ve.getTemplate(createViewTempl, "UTF-8");
				 } finally {
				    Thread.currentThread().setContextClassLoader(ccl);
				 }
				VelocityContext vc = new VelocityContext();
				vc.put("views", views);
				template.merge(vc, writer);
			} catch (ResourceNotFoundException e) {
				MainPlugin.getDefault().logError("������ͼsqlʧ�ܡ�", e);
				throw new SdpBuildRuntimeException("ģ���ļ�" + createViewTempl + "�����ڡ�");
			} catch (ParseErrorException e) {
				MainPlugin.getDefault().logError("������ͼsqlʧ�ܡ�", e);
				throw new SdpBuildRuntimeException("����ģ���ļ�" + createViewTempl + "ʧ�ܡ�");
			} catch (Exception e) {
				MainPlugin.getDefault().logError("������ͼsqlʧ�ܡ�", e);
				throw new SdpBuildRuntimeException("����ģ���ļ�" + createViewTempl + "δ֪����");
			}
			
		}
		
		public void geneAddConstraintDdl(List<IFkConstraint> constraints, Writer writer) {
			try {
				ClassLoader ccl = Thread.currentThread().getContextClassLoader();
				Template template = null;
				try {
				    Thread.currentThread().setContextClassLoader(
				      this.getClass().getClassLoader());
				    template = ve.getTemplate(referenceTempl, "UTF-8");
				 } finally {
				    Thread.currentThread().setContextClassLoader(ccl);
				 }
				VelocityContext vc = new VelocityContext();
				vc.put("fkConstraints", constraints);
				template.merge(vc, writer);
			} catch (ResourceNotFoundException e) {
				MainPlugin.getDefault().logError("�������Լ��sqlʧ�ܡ�", e);
				throw new SdpBuildRuntimeException("ģ���ļ�" + referenceTempl + "�����ڡ�");
			} catch (ParseErrorException e) {
				MainPlugin.getDefault().logError("�������Լ��sqlʧ�ܡ�", e);
				throw new SdpBuildRuntimeException("����ģ���ļ�" + referenceTempl + "ʧ�ܡ�");
			} catch (Exception e) {
				MainPlugin.getDefault().logError("�������Լ��sqlʧ�ܡ�", e);
				throw new SdpBuildRuntimeException("����ģ���ļ�" + referenceTempl + "δ֪����");
			}
		}
		
		private void initVelocityEngine(){	
			ve = new VelocityEngine();			
//			Properties prop = new Properties();
			ve.setProperty("file.resource.loader.class", 
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//			ve.setProperty("resource.loader", "class");
//			ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

			try {
				ve.init();
			} catch (Exception e) {
				MainPlugin.getDefault().logError("", e);
				throw new SdpBuildRuntimeException("��ʼ��VelocityEngineʧ�ܡ�");
			}
		}
	}

}
