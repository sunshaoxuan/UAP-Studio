package nc.uap.lfw.build.dbcreate.pdm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.common.DomUtil;
import nc.uap.lfw.build.dbcreate.pdm.Pdm.ViewInfo;
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.optlog.OperateLogger;
import nc.uap.lfw.build.optlog.OperateLogger.LogLevel;
import nc.uap.lfw.build.orm.mapping.Column;
import nc.uap.lfw.build.orm.mapping.FkConstraint;
import nc.uap.lfw.build.orm.mapping.Index;
import nc.uap.lfw.build.orm.mapping.PkConstraint;
import nc.uap.lfw.build.orm.mapping.Table;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * PDM�ļ��������ߡ�
 * 
 * @author PH
 */
public class PdmUtil {
	
	/** PDM�ļ����� */
	private static final String PD_FILE_TYPE = "PDM_DATA_MODEL_XML";
	/** PDM���ݿ����Ͱ汾 */
	private static final String DB_SQLSERVER_2005 = "Microsoft SQL Server 2005";
	/** PD�汾 */
	private static final String PD_VERSION = "12.0.0.1700";
	/** TRUE���ַ�����ʾ */
	private static final String TRUE_VALUE = "1";
	/** CaseType */
	//private static final CaseType caseType = CaseType.TOLOWER;
	
	/**
	 * У��PDM�ļ����͡����ݿ����Ͱ汾��PD�汾��Ϣ��
	 * 
	 * @param pdmFile ��У���PDM�ļ�
	 */
	public static void validatePdm(File pdmFile){
		BufferedReader br = null;
		boolean success = false;
		String fileName = pdmFile.getAbsolutePath();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(pdmFile), "UTF-8"));
			String line = null;
			while((line = br.readLine()) != null){
				line = line.trim();
				int indexOfPdStartTag = -1;
				//Current line may not only contain '<?PowerDesigner' tag and the order of attributes is random.
				if((indexOfPdStartTag = line.indexOf("<?PowerDesigner ")) != -1){
					int indexOfPdEndTag = line.indexOf(">", indexOfPdStartTag);
					String pdContent = line.substring(indexOfPdStartTag + "<?PowerDesigner ".length(), indexOfPdEndTag);
					Pattern pattern = Pattern.compile("signature=\"([^\"]*)\"");
					Matcher matcher = pattern.matcher(pdContent);
					if(!matcher.find() || !PD_FILE_TYPE.equalsIgnoreCase(matcher.group(1))){
						throw new SdpBuildRuntimeException("PDM("+ fileName +")�ļ���ʽ����, Ŀǰ֧�ֵ��ļ�����Ϊ" + PD_FILE_TYPE);
					}
					pattern = Pattern.compile("Target=\"([^\"]*)\"");
					matcher = pattern.matcher(pdContent);
					if(!matcher.find() || !DB_SQLSERVER_2005.equalsIgnoreCase(matcher.group(1))){
						throw new SdpBuildRuntimeException("PDM("+ fileName +")�ļ��е�ǰ���ݿ����Ͳ���, Ŀǰ֧�ֵ����ݿ�����Ϊ" + DB_SQLSERVER_2005);
					}
					pattern = Pattern.compile("version=\"([^\"]*)\"");
					matcher = pattern.matcher(pdContent);
					if(!matcher.find() || !PD_VERSION.equalsIgnoreCase(matcher.group(1))){
						throw new SdpBuildRuntimeException("PDM("+ fileName +")�ļ���PowerDesigner�汾����, Ŀǰ֧�ֵİ汾Ϊ" + PD_VERSION);
					}
					success = true;
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			String errorMsg = "PDM("+ fileName +")����ʧ�ܡ�";
			MainPlugin.getDefault().logError(errorMsg, e);
			throw new SdpBuildRuntimeException(errorMsg);
		} catch (FileNotFoundException e) {
			String errorMsg = "PDM("+ fileName +")�����ڡ�";
			MainPlugin.getDefault().logError(errorMsg);
			throw new SdpBuildRuntimeException(errorMsg);
		} catch (IOException e) {
			String errorMsg = "PDM("+ fileName +")��ȡʧ�ܡ�";
			MainPlugin.getDefault().logError(errorMsg, e);
			throw new SdpBuildRuntimeException(errorMsg);
		} finally{
			IOUtils.closeQuietly(br);
		}
		if(!success){
			throw new SdpBuildRuntimeException("PDM("+ fileName +")���ļ������޷����������ݿ����͡��汾����Ϣ��");
		}
	}
	
	/**
	 * ����PDM�ļ�����ȡ������(�С����������)����������ͼ��Ϣ��
	 * 
	 * @param pdmFile ��������PDM�ļ�
	 * @param parseReference �Ƿ�����������
	 * @return {@link Pdm}
	 */
	public static Pdm parsePdm(File pdmFile, boolean parseReference){
		long start = System.currentTimeMillis();
		String fileName = pdmFile.getAbsolutePath();
		Document dom = getDocument(pdmFile);
		Element rootEle = dom.getDocumentElement();
		Pdm pdm = new Pdm();
		pdm.setVersion(PD_VERSION);
		Element modelEle = (Element)DomUtil.findNestedChild(rootEle, "o:RootObject/c:Children/o:Model");
		List<Node> nameNodes = DomUtil.findNestedChilds(modelEle, "a:Name");
		if(nameNodes.size() != 1){
			throw new SdpBuildRuntimeException("PDM("+ fileName +")�ļ��а����Ƿ�������PDM");
		}else{
			pdm.setPdmDesc(nameNodes.get(0).getFirstChild().getNodeValue());
		}
		pdm.setPdmName(DomUtil.findChildContent(modelEle, "a:Code"));
		
		//1.��(������Լ��)������
		Map<String, Table> idTableMap = new LinkedHashMap<String, Table>();
		Map<String, Column> idColumnMap = new HashMap<String, Column>();
		Map<String, Index> idIndexMap = new LinkedHashMap<String, Index>();
		parseTables(modelEle, fileName, idTableMap, idColumnMap, idIndexMap);
		pdm.getTables().addAll(idTableMap.values());
		pdm.getIndexs().addAll(idIndexMap.values());
		
		//2.���Լ��
		if(parseReference){
			List<FkConstraint> fkConstraints = parseReferences(modelEle, pdmFile.getAbsolutePath(), idTableMap, idColumnMap);
			pdm.getFkConstraints().addAll(fkConstraints);
		}
		
		//3.��ͼ
		List<ViewInfo> viewInfos = parseViews(modelEle, fileName);
		pdm.getViews().addAll(viewInfos);
		
		if(MainPlugin.getDefault().isDebugging()){
			MainPlugin.getDefault().logInfo("Total millis cost at parsing pdm(" + fileName + "): " + (System.currentTimeMillis() - start));
		}
		return pdm;
	}
	
	/**
	 * ��������Ϣ��
	 * 
	 * @param modelEle
	 * @param xPath
	 * @param pdmFileName
	 * @param idTableMap
	 * @param idColumnMap
	 * @param idIndexMap
	 * @throws XPathExpressionException
	 */
	private static void parseTables(Element modelEle, String pdmFileName, 
			Map<String, Table> idTableMap, Map<String, Column> idColumnMap, 
			Map<String, Index> idIndexMap){
		long start = System.currentTimeMillis();
		//0.����
		List<Node> tableNodes = DomUtil.findNestedChilds(modelEle, "c:Tables/o:Table");
		if(tableNodes.isEmpty()){
			throw new SdpBuildRuntimeException("PDM("+ pdmFileName +")��û���ҵ�������PDM���Ƿ��ڶ��㶨����package��");
		}
		
		for(Node tableNode : tableNodes){
			//1.��
			String tableId = ((Element)tableNode).getAttribute("Id");
			Table table = new Table();
			table.setDesc(DomUtil.findChildContent(tableNode, "a:Name"));
			table.setName(DomUtil.findChildContent(tableNode, "a:Code"));
			//2.���м���
			List<Node> colNodes = DomUtil.findNestedChilds(tableNode, "c:Columns/o:Column");
			if(colNodes.isEmpty()){
				MainPlugin.getDefault().logInfo("PDM("+ pdmFileName +")�б�(" + table.getName() + ")δ�����С�");
				continue;
			}else{
				boolean success = true;
				for(Node colNode :colNodes){
					String colId = ((Element)colNode).getAttribute("Id");
					Column col = new Column();
					col.setDesc(DomUtil.findChildContent(colNode, "a:Name"));
					col.setName(DomUtil.findChildContent(colNode, "a:Code"));
					if(StringUtils.equalsIgnoreCase(col.getName(), "ts") 
							|| StringUtils.equalsIgnoreCase(col.getName(), "dr")){
						MainPlugin.getDefault().logInfo(new StringBuilder("PDM(").append(pdmFileName).append(")�б�(")
								.append(table.getName()).append(")����(").append(col.getName())
								.append(")�������á�").toString());
						continue;
					}
					col.setTypeName(DomUtil.findChildContent(colNode, "a:DataType"));
					if(StringUtils.isBlank(col.getTypeName())){
						success = false;
						String msg = new StringBuilder("PDM(").append(pdmFileName).append(")�б�(")
								.append(table.getName()).append(")����(").append(col.getName())
								.append(")δ�����������͡�").toString();
						MainPlugin.getDefault().logError(msg);
						OperateLogger.getInstance().addLog(LogLevel.ERROR, msg);
						continue;
					}
					String length = DomUtil.findChildContent(colNode, "a:Length");
					if(StringUtils.isNotBlank(length)){
						col.setLength(Integer.valueOf(length));
					}
					String precision = DomUtil.findChildContent(colNode, "a:Precision");
					if(StringUtils.isNotBlank(precision)){
						col.setPrecise(Integer.valueOf(precision));
					}
					col.setNullable(!TRUE_VALUE.equals(DomUtil.findChildContent(colNode, "a:Mandatory")));
					String defaultValue = DomUtil.findChildContent(colNode, "a:DefaultValue");
					if(StringUtils.isNotBlank(defaultValue)){
						if(col.getTypeName() != null && col.getTypeName().toLowerCase().lastIndexOf("char") != -1){
							if(defaultValue.lastIndexOf("'") == -1){
								defaultValue = "'" + defaultValue + "'";
							}
						}
						col.setDefaultValue(defaultValue);
					}
					
					String stereoType = DomUtil.findChildContent(colNode, "a:Stereotype");
					if(StringUtils.isNotBlank(stereoType)){
						col.setStereotype(stereoType);
					}
					table.getAllColumns().add(col);
					idColumnMap.put(colId, col);
				}
				if(success){
					idTableMap.put(tableId, table);
				}else{
					continue;
				}
			}
			
			//3.key����
			
			//4.��������
			List<Node> indexNodes = DomUtil.findNestedChilds(tableNode, "c:Indexes/o:Index");
			for(Node indexNode : indexNodes){
				String indexName = DomUtil.findChildContent(indexNode, "a:Code");
				List<Node> indexColNodes = DomUtil.findNestedChilds(indexNode, "c:IndexColumns/o:IndexColumn/c:Column/o:Column");
				if(indexColNodes.isEmpty()){
					MainPlugin.getDefault().logInfo(new StringBuilder("PDM(").append(pdmFileName).append(")�б�(")
							.append(table.getName()).append(")������(").append(indexName).append(")δ�����С�").toString());
					continue;
				}
				String indexId = ((Element)indexNode).getAttribute("Id");
				Index index = new Index();
				index.setTable(table);
				index.setUnique(TRUE_VALUE.equals(DomUtil.findChildContent(indexNode, "a:Unique")));
				index.setName(indexName);
				index.setDesc(DomUtil.findChildContent(indexNode, "a:Name"));
				for(Node indexColNode : indexColNodes){
					Element indexColEle = (Element)indexColNode;
					index.getColumns().add(idColumnMap.get(indexColEle.getAttribute("Ref")));
				}
				idIndexMap.put(indexId, index);
			}
			
			//5.����
			String primaryKeyRef = DomUtil.findNestedChildAttr(tableNode, "c:PrimaryKey/o:Key", "Ref");
			if(StringUtils.isBlank(primaryKeyRef)){
				MainPlugin.getDefault().logInfo("PDM("+ pdmFileName +")�б�(" + table.getName() + ")��������");
			}else{
				//��primaryKeyRef����������
				Element okeyEle = (Element)DomUtil.findNestedChild(tableNode, "c:Keys/o:Key", "Id", primaryKeyRef);
				if(okeyEle != null){
					List<Node> pkColNodes = DomUtil.findNestedChilds(okeyEle, "c:Key.Columns/o:Column");
					if(!pkColNodes.isEmpty()){
						PkConstraint pkConstraint = new PkConstraint();
						String constraintName = DomUtil.findChildContent(okeyEle, "a:ConstraintName");
						if(StringUtils.isBlank(constraintName)){
							constraintName = "pk_" + table.getName();
						}
						pkConstraint.setName(constraintName.toLowerCase());
						boolean success = true;
						for(Node pkColNode : pkColNodes){
							String colRef = ((Element)pkColNode).getAttribute("Ref");
							Column col = idColumnMap.get(colRef);
							if(col == null){
								success = false;
								break;
							}
							pkConstraint.getColumns().add(col);
						}
						if(success){
							table.setPkConstraint(pkConstraint);
						}else{
							MainPlugin.getDefault().logInfo("PDM("+ pdmFileName +")�б�(" + table.getName() + ")��������");
						}
					}
					
				}
			}
			
			//6.�ۼ����ö���(����������)
			String clusteredIndexId = DomUtil.findNestedChildAttr(tableNode, "c:ClusterObject/o:Index", "Ref");
			if(StringUtils.isNotBlank(clusteredIndexId)){
				Index index = idIndexMap.get(clusteredIndexId);
				if(index != null){
					index.setClustered(true);
				}
			}
		}
		if(MainPlugin.getDefault().isDebugging()){
			MainPlugin.getDefault().logInfo(new StringBuilder("Millis cost at parsing (").append(pdmFileName)
					.append(") with ").append(tableNodes.size()).append(" tables: ")
					.append((System.currentTimeMillis() - start)).toString());
		}
	}
	
	/**
	 * ����������á�
	 * 
	 * @param modelEle
	 * @param xPath
	 * @param pdmFileName
	 * @param idTableMap
	 * @param idColumnMap
	 * @return
	 * @throws XPathExpressionException
	 */
	private static List<FkConstraint> parseReferences(Element modelEle, String pdmFileName, 
			Map<String, Table> idTableMap, Map<String, Column> idColumnMap){
		long startMillis = System.currentTimeMillis();
		List<FkConstraint> fkConstraints = new ArrayList<FkConstraint>();
		List<Node> referenceNodes = DomUtil.findNestedChilds(modelEle, "c:References/o:Reference");
		if(referenceNodes.isEmpty()){
			if(MainPlugin.getDefault().isDebugging()){
				MainPlugin.getDefault().logInfo("PDM("+ pdmFileName +")��δ����������á�");
			}
			return fkConstraints;
		}
		
		for(Node referenceNode : referenceNodes){
			String referName = DomUtil.findChildContent(referenceNode, "a:Code"),
					referDesc = DomUtil.findChildContent(referenceNode, "a:Name");
			String mainTableId = DomUtil.findNestedChildAttr(referenceNode, "c:Object1/o:Table", "Ref"); 
			String subTableId = DomUtil.findNestedChildAttr(referenceNode, "c:Object2/o:Table", "Ref");
			Table mainTable = idTableMap.get(mainTableId);
			Table subTable = idTableMap.get(subTableId);
			if(mainTable == null || subTable == null){
				MainPlugin.getDefault().logInfo(new StringBuilder("PDM(").append(pdmFileName).append(")���������(")
						.append(referDesc).append(")���ù����ı���Ч��").toString());
				continue;
			}
			Node referenceJoinNode = DomUtil.findNestedChild(referenceNode, "c:Joins/o:ReferenceJoin");
			if(referenceJoinNode != null){
				String mainTableColId = DomUtil.findNestedChildAttr(referenceJoinNode, "c:Object1/o:Column", "Ref");
				String subTableColId = DomUtil.findNestedChildAttr(referenceJoinNode, "c:Object2/o:Column", "Ref");;
				Column mainTableCol = idColumnMap.get(mainTableColId);
				Column subTableCol = idColumnMap.get(subTableColId);
				
				if(mainTableCol != null || subTableCol != null){
					FkConstraint fkConstraint = new FkConstraint();
					fkConstraint.setTable(subTable);
					fkConstraint.setRefTable(mainTable);
					fkConstraint.getColumns().add(subTableCol);
					fkConstraint.getRefColumns().add(mainTableCol);
					fkConstraint.setName(referName);
					fkConstraints.add(fkConstraint);
					subTable.getFkConstraints().add(fkConstraint);
				}else{
					MainPlugin.getDefault().logInfo(new StringBuilder("PDM(").append(pdmFileName).append(")���������(")
							.append(referDesc).append(")���ù����ı�����Ч��").toString());
				}
			}else{
				MainPlugin.getDefault().logInfo(new StringBuilder("PDM(").append(pdmFileName).append(")���������(")
						.append(referDesc).append(")���ù����ı�����Ч��").toString());
			}
		}
		
		if(MainPlugin.getDefault().isDebugging()){
			MainPlugin.getDefault().logInfo("Millis cost at parsing " + referenceNodes.size() + " references is: " 
					+ (System.currentTimeMillis() - startMillis));
		}
		return fkConstraints;
	}
	
	/**
	 * ������ͼ��
	 * 
	 * @param modelEle
	 * @param xPath
	 * @param pdmFileName
	 * @return
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	private static List<ViewInfo> parseViews(Element modelEle, String pdmFileName) {
		long startMillis = System.currentTimeMillis();
		List<Node> viewNodes = DomUtil.findNestedChilds(modelEle, "c:Views/o:View");
		if(viewNodes.isEmpty()){
			if(MainPlugin.getDefault().isDebugging()){
				MainPlugin.getDefault().logInfo("PDM("+ pdmFileName +")��δ������ͼ��");
			}
			return Collections.EMPTY_LIST;
		}
		
		List<ViewInfo> viewInfos = new ArrayList<ViewInfo>();
		for(Node viewNode : viewNodes){
			String viewName = DomUtil.findChildContent(viewNode, "a:Code"),
					viewDesc = DomUtil.findChildContent(viewNode, "a:Name");
			List<Node> viewColNodes = DomUtil.findNestedChilds(viewNode, "c:Columns/o:ViewColumn");
			if(viewColNodes.isEmpty()){
				MainPlugin.getDefault().logInfo("PDM("+ pdmFileName +")����ͼ(" + viewDesc + ")δ���ò�ѯ�С�");
			}else{
//				boolean success = true;
//				for(Node viewColNode : viewColNodes){
//					String colName = DomUtil.findChildContent(viewColNode, "a:Code");
//					
//					List<Node> viewColInnerNodes = DomUtil.findNestedChilds(viewColNode, "c:ViewColumn.Columns/o:Column");
//					if(viewColInnerNodes.isEmpty()){
//						success = false;
//						LfwLogger.warn(new StringBuilder("PDM(").append(pdmFileName).append(")����ͼ(")
//							.append(viewDesc).append(")����(").append(colName).append(")��Ч��").toString());
//					}
//				}
//				if(!success){
//					continue;
//				}
				String sqlQuery = DomUtil.findChildContent(viewNode, "a:View.SQLQuery");
				if(StringUtils.isNotBlank(sqlQuery)){
					sqlQuery = sqlQuery.replaceAll("&#39;", "'");
//					if(LfwLogger.isDebugEnabled()){
//						LfwLogger.debug(new StringBuilder("PDM(").append(pdmFileName).append(")����ͼ(")
//								.append(viewDesc).append(")��Ч�����Ϊ��").append(sqlQuery).append("��").toString());
//					}
					ViewInfo viewInfo = new ViewInfo();
					viewInfo.setName(viewName);
					viewInfo.setDesc(viewDesc);
					viewInfo.setSql(sqlQuery);
					viewInfo.setDesc(DomUtil.findChildContent(viewNode, "Name"));
					viewInfos.add(viewInfo);
				}
			}
		}
		if(MainPlugin.getDefault().isDebugging()){
			MainPlugin.getDefault().logInfo("Millis cost at parsing " + viewNodes.size() + " view is: " 
					+ (System.currentTimeMillis() - startMillis));
		}
		return viewInfos;
	}
	
	
	private static Document getDocument(File file){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setIgnoringComments(true);
		dbf.setIgnoringElementContentWhitespace(true);
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db.parse(file);
		} catch (Exception e) {
			MainPlugin.getDefault().logError("�����ļ�" + file.getPath() + "Ϊxml����", e);
			throw new SdpBuildRuntimeException("�����ļ�" + file.getPath() + "Ϊxml����");
		}
	}
	
	static enum CaseType{
		TOLOWER, TOUPPER, RESERVE
	}

}
