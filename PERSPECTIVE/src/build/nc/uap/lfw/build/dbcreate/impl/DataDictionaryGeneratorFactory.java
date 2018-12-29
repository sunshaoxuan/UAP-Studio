package nc.uap.lfw.build.dbcreate.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.common.PropertyComparator;
import nc.uap.lfw.build.dbcreate.pdm.Pdm;
import nc.uap.lfw.build.dbcreate.pdm.PdmUtil;
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.orm.mapping.IColumn;
import nc.uap.lfw.build.orm.mapping.IIndex;
import nc.uap.lfw.build.orm.mapping.ITable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * 数据字典生成器工厂。
 * 
 * @author PH
 */
public class DataDictionaryGeneratorFactory {
	
	private static IDataDictionaryGenerator ddGenerator = new DataDictionaryGenerator();
	
	public static IDataDictionaryGenerator getInstance(){
		return ddGenerator;
	}
	
	static class DataDictionaryGenerator implements IDataDictionaryGenerator{
		static final String TEMPL_ENCODING = "UTF-8";
		static final String DD_TEMPL_PATH = "nc/sdp/build/dbcreate/dd/vm/";
		static final String INDEX_TEMPL_NAME = "index.templ";
		static final String HEADER_TEMPL_NAME = "header.templ";
		static final String HOME_TEMPL_NAME = "home.templ";
		static final String LEFT_NAVIGATION_TEMPL_NAME = "leftNavigation.templ";
		static final String TABLE_CONTENT_TEMPL_NAME = "tableContent.templ";
		private VelocityEngine ve;
		public DataDictionaryGenerator() {
			initVelocityEngine();
		}

		public void generate(File pdmFile, boolean geneReference, File ddRoot) {
			PdmUtil.validatePdm(pdmFile);
			Pdm pdm = PdmUtil.parsePdm(pdmFile, geneReference);
			String pdmFileName = pdmFile.getName();
			int indexOfLastDot = -1;
			if((indexOfLastDot = pdmFileName.lastIndexOf(".")) != -1){
				pdmFileName = pdmFileName.substring(0, indexOfLastDot).toLowerCase();
			}
			if(ddRoot.exists()){
				try {
					FileUtils.forceDelete(ddRoot);
				} catch (IOException e) {
					throw new SdpBuildRuntimeException("删除目录" + ddRoot.getPath() + "失败。");
				}
			}
			if(!ddRoot.mkdirs()){
				throw new SdpBuildRuntimeException("创建目录" + ddRoot.getPath() + "失败。");
			}
			Writer writer = null;
			Map<String, List<IIndex>> tableMapIndexs = new HashMap<String, List<IIndex>>();
			for(IIndex index : pdm.getIndexs()){
				List<IIndex> tableIndexs = null;
				if((tableIndexs = tableMapIndexs.get(index.getTable().getName())) == null){
					tableIndexs = new ArrayList<IIndex>();
					tableMapIndexs.put(index.getTable().getName(), tableIndexs);
				}
				tableIndexs.add(index);
			}
			try{
				try {
					Template indexTempl = ve.getTemplate(DD_TEMPL_PATH + INDEX_TEMPL_NAME, TEMPL_ENCODING);
					VelocityContext vc = new VelocityContext();
					vc.put("pdmName", pdmFileName);
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ddRoot, "nc_dd_index.html")), TEMPL_ENCODING));
					indexTempl.merge(vc, writer);
				} finally{
					IOUtils.closeQuietly(writer);
				}
				writer = null;
				try {
					Template headerTempl = ve.getTemplate(DD_TEMPL_PATH + HEADER_TEMPL_NAME, TEMPL_ENCODING);
					VelocityContext vc = new VelocityContext();
					vc.put("pdmName", pdmFileName);
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ddRoot, "nc_dd_header.html")), TEMPL_ENCODING));
					headerTempl.merge(vc, writer);
				} finally{
					IOUtils.closeQuietly(writer);
				}
				
				writer = null;
				try {
					Template headerTempl = ve.getTemplate(DD_TEMPL_PATH + HOME_TEMPL_NAME, TEMPL_ENCODING);
					VelocityContext vc = new VelocityContext();
					vc.put("pdmName", pdmFileName);
					vc.put("reportName", pdmFileName);
					vc.put("moduleName", pdmFileName);
					vc.put("geneDatetime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ddRoot, "nc_dd_doc_home.html")), TEMPL_ENCODING));
					headerTempl.merge(vc, writer);
				} finally{
					IOUtils.closeQuietly(writer);
				}
				
				writer = null;
				try {
					Template indexTempl = ve.getTemplate(DD_TEMPL_PATH + LEFT_NAVIGATION_TEMPL_NAME, TEMPL_ENCODING);
					VelocityContext vc = new VelocityContext();
					vc.put("pdmName", pdmFileName);
					List<ITable> tables = new ArrayList<ITable>(pdm.getTables());
					Collections.sort(tables, new PropertyComparator<ITable>("name"));
					vc.put("tables", tables);
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ddRoot, "nc_dd_navigator.html")), TEMPL_ENCODING));
					indexTempl.merge(vc, writer);
				} finally{
					IOUtils.closeQuietly(writer);
				}
				
				
				Template tableContentTempl = ve.getTemplate(DD_TEMPL_PATH + TABLE_CONTENT_TEMPL_NAME, TEMPL_ENCODING);
				for(ITable table : pdm.getTables()){
					List<String> pkColNames = new ArrayList<String>();
					if(table.getPkConstraint() != null && table.getPkConstraint().getColumns() != null){
						for(IColumn pkCol : table.getPkConstraint().getColumns()){
							pkColNames.add(pkCol.getName());
						}
					}
					VelocityContext vc = new VelocityContext();
					vc.put("table", table);
					vc.put("indexs", tableMapIndexs.get(table.getName()));
					vc.put("pkColNames", pkColNames);
					writer = null;
					try{
						writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ddRoot, table.getName() +".html")), TEMPL_ENCODING));
						tableContentTempl.merge(vc, writer);
					}finally{
						IOUtils.closeQuietly(writer);
					}
				}
			} catch (Exception e) {
				MainPlugin.getDefault().logError("生成PDM(" + pdmFile.getPath() + ")的数据字典失败。",e);
				throw new SdpBuildRuntimeException("生成PDM(" + pdmFile.getPath() + ")的数据字典失败。");
			} 
			
			String resourceDir = "D:/work/cc_workspace/huping_NC_DSGN_SDP5.6_dev_2/NC5_DSGN5.0_VOB/NC_DSGN_SDP/dbscript/nc/sdp/build/dbcreate/dd/resource";
			try {
				FileUtils.copyDirectory(new File(resourceDir), ddRoot);
			} catch (IOException e) {
				MainPlugin.getDefault().logError("拷贝资源文件(" + resourceDir + ")到(" + ddRoot +")失败。",e);
				throw new SdpBuildRuntimeException("");
			}
		}
		
		private void initVelocityEngine(){
			ve = new VelocityEngine();
			Properties prop = new Properties();
			prop.setProperty("file.resource.loader.class", 
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			try {
				ve.init(prop);
			} catch (Exception e) {
				MainPlugin.getDefault().logError("", e);
				throw new SdpBuildRuntimeException("初始化VelocityEngine失败。");
			}
		}
	}

}
