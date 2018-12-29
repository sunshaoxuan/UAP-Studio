package nc.uap.lfw.build.pub.util.pdm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.exception.SDPBuildException;
import nc.uap.lfw.build.exception.SDPSystemMessage;
import nc.uap.lfw.build.pub.util.pdm.itf.IParse;
import nc.uap.lfw.build.pub.util.pdm.vo.ColumnVO;
import nc.uap.lfw.build.pub.util.pdm.vo.IndexVO;
import nc.uap.lfw.build.pub.util.pdm.vo.KeyVO;
import nc.uap.lfw.build.pub.util.pdm.vo.PDM;
import nc.uap.lfw.build.pub.util.pdm.vo.ReferenceVO;
import nc.uap.lfw.build.pub.util.pdm.vo.TableVO;
import nc.uap.lfw.build.pub.util.pdm.vo.ViewVO;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * PDM解析器
 * 
 * 工作方式: 多实例, 线程安全 可访问方法: parse
 * 
 * @author fanp
 */
public class PDMParser implements IParse {
	private FileInputStream in = null;
	private boolean referenceFlag = false;

	private DocumentBuilderFactory factory = null;
	private DocumentBuilder parser = null;
	XPathFactory xpfactory = null;
	XPath path = null;

	private void setRefFlag(boolean flag) {
		this.referenceFlag = flag;
	}

	private boolean getRefFlag() {
		return this.referenceFlag;
	}

	/**
	 * 构造函数
	 * 
	 * @param pdm_path pdm文件的路径
	 * @param is_ref_needed 是否需要导出外键
	 * @return
	 */
	public PDMParser(String pdm_path, boolean is_ref_needed) throws SDPBuildException {
		/* 打开XML格式的PDM源文件 */
		try {
			in = new FileInputStream(pdm_path);
		} catch (IOException ioe) {
			MainPlugin.getDefault().logError("读入外部PDM文件错误", ioe);
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2005));
		}

		/* 设置是否导出外键标识 */
		setRefFlag(is_ref_needed);

		/* 创建DOM Parser单例, xpath单例 */
		factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false); // 关闭"XML语法检查"
		try {
			parser = factory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			MainPlugin.getDefault().logError("初始化DOM Parser错误", ex);
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2006));
		}
		xpfactory = XPathFactory.newInstance();
		path = xpfactory.newXPath();
	}

	/**
	 * 该方法解析PDM文件, 并生成结果VO对象
	 * 此部分使用DOM来解析pdm文件
	 * 
	 * @return 解析后的PDM文件
	 */
	public PDM parse() throws SDPBuildException {
		/* 解析PDM */
		Document doc = null;
		Element root = null;
		String pdmName = null;
		String pdmCode = null;
		PDM pdm = null;
		List<TableVO> tableDefs = new ArrayList<TableVO>();

		pdm = new PDM();

		// 设置PDMVO对象中是否包含解析后的reference结构之标志
//		if (getRefFlag())
//			pdm.setIsReferenceNeeded(true);
//		else
//			pdm.setIsReferenceNeeded(false);
		pdm.setIsReferenceNeeded(getRefFlag());

		try {
			doc = parser.parse(in);
			root = doc.getDocumentElement();
			MainPlugin.getDefault().logInfo("root element: " + root.getNodeName());

			// 根据XPath标识的元素URL解析各PDM元素
			// Model/RootObject/Children/Model/Name元素是否只存在一个，否则的话就抛出错误
			int pdmCount = ((Number) path.evaluate("count(" + IParse.PDM_NAME + ")", doc, XPathConstants.NUMBER))
					.intValue();
			if (pdmCount != 1) {
				MainPlugin.getDefault().logError("PDM文件中包含非法数量的PDM，数量为" + pdmCount);
				throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2007));
			}

			// 2008.11.14 效率优化
			// pdmName = path.evaluate(IParse.PDM_NAME, doc);
			// pdmCode = path.evaluate(IParse.PDM_CODE, doc);
			//使用XPath来定位pdmName以及pdmCode
			Element model = (Element) path.evaluate("/Model/RootObject/Children/Model", doc, XPathConstants.NODE);
			Element pdmNameElement = XMLUtil.getChildElementByName(model, "a:Name");
			pdmName = XMLUtil.getContent(pdmNameElement);
			MainPlugin.getDefault().logInfo("pdm 名称: " + pdmName);
			Element pdmCodeElement = XMLUtil.getChildElementByName(model, "a:Code");
			pdmCode = XMLUtil.getContent(pdmCodeElement);
			MainPlugin.getDefault().logInfo("pdm 编码: " + pdmCode);
			pdm.setName(pdmName);
			pdm.setCode(pdmCode);
			pdm.setVersion("12.0.0.1700");
			// 2008.11.14 效率优化

			/***************************************************************
			 * Table 解析: name, code, comment, column, primary key
			 **************************************************************/
			// 2008.11.14 效率优化
			// int tableCount = ((Number) path.evaluate("count(" + IParse.TABLE
			// + ")", doc, XPathConstants.NUMBER)).intValue();
			Element tables = XMLUtil.getChildElementByName(model, "c:Tables");
			Element[] tableElements = XMLUtil.getChildrenElementsByName(tables, "o:Table");
			if (tableElements == null || tableElements.length == 0) {
				throw new SDPBuildException("pdm中没有找到表，请检查pdm中是否在顶层定义了package");
			}
			int tableCount = tableElements.length;
			// 2008.11.14 效率优化
			MainPlugin.getDefault().logInfo("table 数量: " + tableCount);
//			tableDefs = new Vector(tableCount);

			for (int i = 0; i < tableCount; i++) {
				// 2008.11.14 效率优化
				TableVO tableVO = new TableVO();
				
				Element table = tableElements[i];
				//table Id
				tableVO.setId(XMLUtil.getAttribute(table, IParse.TABLE_ID_ATTRIBUTE));
				//table code
				Element tableCodeElement = XMLUtil.getChildElementByName(table, "a:Code");
				String tableCode = XMLUtil.getContent(tableCodeElement);
				tableVO.setCode(tableCode);
				//table name
				Element tableNameElement = XMLUtil.getChildElementByName(table, "a:Name");
				String tableName = XMLUtil.getContent(tableNameElement);
				tableVO.setName(tableName);

				MainPlugin.getDefault().logInfo("开始处理table: " + tableCode + " " + tableName);
				//table comment
				Element tableCommentElement = XMLUtil.getChildElementByName(table, "a:Comment");
				String tableComment = XMLUtil.getContent(tableCommentElement);
				tableVO.setComment(tableComment);
				MainPlugin.getDefault().logInfo("table comment: " + tableComment);

				// table columns : 解析出所有的列
				Map<String, ColumnVO> htColumns = parseColumnsFromTable(table);
				tableVO.setColumns(htColumns);

				// table primary key : 解析出所有的主鍵信息
				KeyVO keyVO = parsePK(table, tableVO);
				tableVO.setPrimaryKey(keyVO);

				// table indexes ： 解析出所有的索引信息
				List<IndexVO> indexVos = parseIndexes(table, tableVO);
				tableVO.setIndexs(indexVos);
				// 2008.11.14 效率优化

				tableDefs.add(tableVO);
				MainPlugin.getDefault().logInfo("处理结束");
			}/* for */
			pdm.setTableSet(tableDefs);

			/***************************************************************
			 * Reference 解析: id, column, unique
			 **************************************************************/
			pdm.setReferenceSet(parseReferences(pdm, doc));

			/***************************************************************
			 * View 解析: id, code, name, SQLQuery
			 **************************************************************/
			// 2008.11.14 效率优化
			// pdm.setViewSet(parseViews(doc));
			pdm.setViewSet(parseViews(model));
			// 2008.11.14 效率优化

			/* PDM装配(压栈顺序为:(3)Table Set->(2)Reference Set->(1)View Set) */
//			pdm.assembly();

			return pdm;
		} catch (IOException ioe) {
			MainPlugin.getDefault().logError("PDM解析错误", ioe);
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2008));
		} catch (SAXException sax_exception) {
			MainPlugin.getDefault().logError("PDM解析错误", sax_exception);
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2008));
		} catch (XPathExpressionException xpath_expression_exception) {
			MainPlugin.getDefault().logError("PDM解析错误", xpath_expression_exception);
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_2008));
		}
	}

	/**
	 * 该方法以非XPath方式为指定的Model元素生成View集合
	 * 
	 * @param model_element
	 * @return
	 */
	private Vector parseViews(Element model_element) {
		if (model_element == null)
			return null;

		Element views = XMLUtil.getChildElementByName(model_element, "c:Views");
		if (views == null)
			return null;

		Element[] viewElements = XMLUtil.getChildrenElementsByName(views, "o:View");
		MainPlugin.getDefault().logInfo("view 数量: " + viewElements.length);
		Vector v = new Vector(viewElements.length);
		for (int i = 0; i < viewElements.length; i++) {
			Element view = viewElements[i];

			ViewVO viewVO = new ViewVO();
			// id
			String id = XMLUtil.getAttribute(view, IParse.VIEW_ID_ATTRIBUTE);
			viewVO.setId(id);

			// name
			Element viewNameElement = XMLUtil.getChildElementByName(view, "a:Name");
			String name = XMLUtil.getContent(viewNameElement);
			viewVO.setName(name);

			// code
			Element viewCodeElement = XMLUtil.getChildElementByName(view, "a:Code");
			String code = XMLUtil.getContent(viewCodeElement);
			viewVO.setCode(code);
			MainPlugin.getDefault().logInfo("view id: " + viewVO.getId() + ", view code: " + viewVO.getCode() + ", view name: "
					+ viewVO.getName());

			// query sql
			Element viewQuerySqlElement = XMLUtil.getChildElementByName(view, "a:View.SQLQuery");
			String querySQl = XMLUtil.getContent(viewQuerySqlElement);
			viewVO.setQuery(querySQl);
			MainPlugin.getDefault().logInfo("view query: " + viewVO.getQuery());

			v.addElement(viewVO);
		}// for
		return v;
	}

	/**
	 * 该方法以XPath方式为指定Document对象生成View集合
	 * 
	 * @param _doc
	 * @return
	 */
	private Vector parseViews(Document doc) {
		Vector v = null;
		Element view = null;
		String id = null;
		String code = null;
		String name = null;
		String querySQl = null;
		ViewVO viewVO = null;

		try {
			int viewCount = ((Number) path.evaluate("count(" + IParse.VIEW + ")", doc, XPathConstants.NUMBER))
					.intValue();

			if (viewCount <= 0)
				return null;

			MainPlugin.getDefault().logInfo("view 数量: " + viewCount);
			v = new Vector(viewCount);
			for (int i = 0; i < viewCount; i++) {
				viewVO = new ViewVO();
				view = (Element) path.evaluate(IParse.VIEW + "[" + (i + 1) + "]", doc, XPathConstants.NODE);
				// id
				id = getAttribute(view, IParse.VIEW_ID_ATTRIBUTE);
				viewVO.setId(id);

				// name
				String urlName = IParse.VIEW + "[" + (i + 1) + "]" + "/Name";
				name = path.evaluate(urlName, doc);
				viewVO.setName(name);

				// code
				String urlCode = IParse.VIEW + "[" + (i + 1) + "]" + "/Code";
				code = path.evaluate(urlCode, doc);
				viewVO.setCode(code);
				MainPlugin.getDefault().logInfo("view id: " + viewVO.getId() + ", view code: " + viewVO.getCode() + ", view name: "
						+ viewVO.getName());

				// query sql
				String urlQuerySQL = IParse.VIEW + "[" + (i + 1) + "]" + IParse.VIEW_QUERY_SQL;
				querySQl = path.evaluate(urlQuerySQL, doc);
				viewVO.setQuery(querySQl);
				MainPlugin.getDefault().logInfo("view query: " + viewVO.getQuery());

				v.addElement(viewVO);
			}// for
			return v;
		} catch (XPathExpressionException e) {
			return null;
		}
	}

	/**
	 * 该方法以非XPath方式为指定Document对象生成Reference集合
	 * 
	 * @param pdm
	 * @param model_element
	 * @return
	 */
	private Vector parseReferences(PDM pdm, Element model_element) {
		if (model_element == null)
			return null;

		Element references = XMLUtil.getChildElementByName(model_element, "c:References");
		if (references == null)
			return null;

		Element[] referenceElements = XMLUtil.getChildrenElementsByName(references, "o:Reference");
		MainPlugin.getDefault().logInfo("reference 数量: " + referenceElements.length);
		Vector v = new Vector(referenceElements.length);
		for (int i = 0; i < referenceElements.length; i++) {
			Element reference = referenceElements[i];

			ReferenceVO referenceVO = new ReferenceVO();
			// id, code, name
			String referenceId = XMLUtil.getAttribute(reference, IParse.REFERENCE_ID_ATTRIBUTE);
			referenceVO.setId(referenceId);
			MainPlugin.getDefault().logInfo("reference id: " + referenceId);

			Element referenceCodeElement = XMLUtil.getChildElementByName(reference, "a:Code");
			String referenceCode = XMLUtil.getContent(referenceCodeElement);
			referenceVO.setCode(referenceCode);
			MainPlugin.getDefault().logInfo("reference code: " + referenceCode);

			Element referenceNameElement = XMLUtil.getChildElementByName(reference, "a:Name");
			String referenceName = XMLUtil.getContent(referenceNameElement);
			referenceVO.setName(referenceName);
			MainPlugin.getDefault().logInfo("reference name: " + referenceName);

			// parent table
			Element Object1 = XMLUtil.getChildElementByName(reference, "c:Object1");
			Element parentTable = XMLUtil.getChildElementByName(Object1, "o:Table");
			String parentTableId = XMLUtil.getAttribute(parentTable, IParse.REFERENCE_TABLE_REF_ATTRIBUTE);
			TableVO parentTableVO = getTableFromPDMById(pdm, parentTableId);
			referenceVO.setParentTable(parentTableVO);
			MainPlugin.getDefault().logInfo("reference parent table code: " + referenceVO.getParentTable().getCode());

			// children table
			Element Object2 = XMLUtil.getChildElementByName(reference, "c:Object2");
			Element childrenTable = XMLUtil.getChildElementByName(Object2, "o:Table");
			String childrenTableId = XMLUtil.getAttribute(childrenTable, IParse.REFERENCE_TABLE_REF_ATTRIBUTE);
			TableVO childrenTableVO = getTableFromPDMById(pdm, childrenTableId);
			referenceVO.setChildrenTable(childrenTableVO);
			MainPlugin.getDefault().logInfo("reference children table code: " + referenceVO.getChildrenTable().getCode());

			// parent column, children column
			Element referenceJoins = XMLUtil.getChildElementByName(reference, "c:Joins");
			Element[] referenceJoinElements = XMLUtil.getChildrenElementsByName(referenceJoins, "o:ReferenceJoin");
			Vector parentTableColumns = new Vector(referenceJoinElements.length);
			Vector childrenTableColumns = new Vector(referenceJoinElements.length);
			for (int j = 0; j < referenceJoinElements.length; j++) {
				Element referenceJoin = referenceJoinElements[j];

				// parent table column
				Element Obj1 = XMLUtil.getChildElementByName(referenceJoin, "c:Object1");
				Element parentTableColumn = XMLUtil.getChildElementByName(Obj1, "o:Column");
				String parentTableColumnId = XMLUtil.getAttribute(parentTableColumn,
						IParse.INDEXES_COLUMN_REF_ATTRIBUTE);
				ColumnVO parentTableColumnVO = getColumnFromTableByColumnId(parentTableColumnId, referenceVO
						.getParentTable());
				parentTableColumns.addElement(parentTableColumnVO);

				// children table column
				Element obj2 = XMLUtil.getChildElementByName(referenceJoin, "c:Object2");
				Element childrenTableColumn = XMLUtil.getChildElementByName(obj2, "o:Column");
				String childrenTableColumnId = XMLUtil.getAttribute(childrenTableColumn,
						IParse.INDEXES_COLUMN_REF_ATTRIBUTE);
				ColumnVO childrenTableColumnVO = getColumnFromTableByColumnId(childrenTableColumnId, referenceVO
						.getChildrenTable());
				childrenTableColumns.addElement(childrenTableColumnVO);
				MainPlugin.getDefault().logInfo("column pair: " + referenceVO.getChildrenTable().getCode() + "."
						+ childrenTableColumnVO.getCode() + "->" + referenceVO.getParentTable().getCode() + "."
						+ parentTableColumnVO.getCode());
			}
			referenceVO.setParentColumns(parentTableColumns);
			referenceVO.setChildrenColumns(childrenTableColumns);

			v.addElement(referenceVO);
		}// for
		return v;
	}

	/**
	 * 该方法以XPath方式为指定Document对象生成Reference集合
	 * 
	 * @param pdm
	 * @param _doc
	 * @return
	 */
	private Vector parseReferences(PDM pdm, Document _doc) {
		ReferenceVO referenceVO = null;

		Element reference = null;
		String referenceId = null;
		String referenceCode = null;
		String referenceName = null;

		Element parentTable = null;
		String parentTableId = null;
		TableVO parentTableVO = null;

		Element childrenTable = null;
		String childrenTableId = null;
		TableVO childrenTableVO = null;

		Element joins = null;
		String urlRefercejoins = null;
		Vector joinColumns = null;
		Element parentTableColumn = null;
		String parentTableColumnId = null;
		ColumnVO parentTableColumnVO = null;
		Vector parentTableColumns = null;

		Element childrenTableColumn = null;
		String childrenTableColumnId = null;
		ColumnVO childrenTableColumnVO = null;
		Vector childrenTableColumns = null;

		Vector v = null;

		try {
			int referenceCount = ((Number) path
					.evaluate("count(" + IParse.REFERENCE + ")", _doc, XPathConstants.NUMBER)).intValue();

			if (referenceCount <= 0)
				return null;

			MainPlugin.getDefault().logInfo("reference 数量: " + referenceCount);
			v = new Vector(referenceCount);
			for (int i = 0; i < referenceCount; i++) {
				referenceVO = new ReferenceVO();
				// id, code, name
				reference = (Element) path.evaluate(IParse.REFERENCE + "[" + (i + 1) + "]", _doc, XPathConstants.NODE);
				referenceId = getAttribute(reference, IParse.REFERENCE_ID_ATTRIBUTE);
				referenceVO.setId(referenceId);
				MainPlugin.getDefault().logInfo("reference id: " + referenceId);
				referenceCode = path.evaluate(IParse.REFERENCE + "[" + (i + 1) + "]/Code", _doc);
				referenceVO.setCode(referenceCode);
				MainPlugin.getDefault().logInfo("reference code: " + referenceCode);
				referenceName = path.evaluate(IParse.REFERENCE + "[" + (i + 1) + "]/Name", _doc);
				referenceVO.setName(referenceName);
				MainPlugin.getDefault().logInfo("reference name: " + referenceName);

				// parent table
				parentTable = (Element) path.evaluate(IParse.REFERENCE + "[" + (i + 1) + "]"
						+ IParse.REFERENCE_PARENT_TABLE, _doc, XPathConstants.NODE);
				parentTableId = getAttribute(parentTable, IParse.REFERENCE_TABLE_REF_ATTRIBUTE);
				parentTableVO = getTableFromPDMById(pdm, parentTableId);
				referenceVO.setParentTable(parentTableVO);
				MainPlugin.getDefault().logInfo("reference parent table code: " + referenceVO.getParentTable().getCode());

				// children table
				childrenTable = (Element) path.evaluate(IParse.REFERENCE + "[" + (i + 1) + "]"
						+ IParse.REFERENCE_CHILDREN_TABLE, _doc, XPathConstants.NODE);
				childrenTableId = getAttribute(childrenTable, IParse.REFERENCE_TABLE_REF_ATTRIBUTE);
				childrenTableVO = getTableFromPDMById(pdm, childrenTableId);
				referenceVO.setChildrenTable(childrenTableVO);
				MainPlugin.getDefault().logInfo("reference children table code: " + referenceVO.getChildrenTable().getCode());

				// parent column, children column
				urlRefercejoins = IParse.REFERENCE + "[" + (i + 1) + "]" + IParse.REFERENCE_JOINS;
				int joinCount = ((Number) path.evaluate("count(" + urlRefercejoins + ")", _doc, XPathConstants.NUMBER))
						.intValue();
				joinColumns = new Vector(joinCount);
				parentTableColumns = new Vector();
				childrenTableColumns = new Vector();
				for (int j = 0; j < joinCount; j++) {
					// parent table column
					parentTableColumn = (Element) path.evaluate(urlRefercejoins + "[" + (j + 1) + "]"
							+ IParse.REFERENCE_PARENT_COLUMN, _doc, XPathConstants.NODE);
					parentTableColumnId = getAttribute(parentTableColumn, IParse.INDEXES_COLUMN_REF_ATTRIBUTE);
					parentTableColumnVO = getColumnFromTableByColumnId(parentTableColumnId, referenceVO
							.getParentTable());
					parentTableColumns.addElement(parentTableColumnVO);

					// children table column
					childrenTableColumn = (Element) path.evaluate(urlRefercejoins + "[" + (j + 1) + "]"
							+ IParse.REFERENCE_CHILDREN_COLUMN, _doc, XPathConstants.NODE);
					childrenTableColumnId = getAttribute(childrenTableColumn, IParse.INDEXES_COLUMN_REF_ATTRIBUTE);
					childrenTableColumnVO = getColumnFromTableByColumnId(childrenTableColumnId, referenceVO
							.getChildrenTable());
					childrenTableColumns.addElement(childrenTableColumnVO);
					MainPlugin.getDefault().logInfo("column pair: " + referenceVO.getChildrenTable().getCode() + "."
							+ childrenTableColumnVO.getCode() + "->" + referenceVO.getParentTable().getCode() + "."
							+ parentTableColumnVO.getCode());
				}
				referenceVO.setParentColumns(parentTableColumns);
				referenceVO.setChildrenColumns(childrenTableColumns);

				v.addElement(referenceVO);
			}/* for */
			return v;
		} catch (XPathExpressionException ex) {
			return null;
		}
	}

	private ColumnVO getColumnFromTableByColumnId(String column_id, TableVO table_vo) {
		Map<String, ColumnVO> columns = table_vo.getColumns();

		if (columns != null && columns.containsKey(column_id))
			return (ColumnVO) columns.get(column_id);
		else
			return null;
	}

	private TableVO getTableFromPDMById(PDM _pdm, String table_id) {
		// 弹出TableVO结构
		List<TableVO> tableSet = _pdm.getTableSet();

		// 遍历TableVO,寻找并返回指定的TableVO
		for (TableVO table : tableSet) {
			if (table_id.equalsIgnoreCase(table.getId()))
				return table;
		}
		return null;
	}

	/**
	 * 该方法以非XPath方式为指定url的表元素生成索引VO集合
	 * 解析table element中包含的索引
	 * 
	 * @param table_element
	 * @param table_vo
	 * @return
	 */
	private List<IndexVO> parseIndexes(Element table_element, TableVO table_vo) {
		if (table_element == null)
			return null;

		Element indexes = XMLUtil.getChildElementByName(table_element, "c:Indexes");
		if (indexes == null)
			return null;

		Element[] indexElements = XMLUtil.getChildrenElementsByName(indexes, "o:Index");
		MainPlugin.getDefault().logInfo("index 数量: " + indexElements.length);
		List<IndexVO> result = new ArrayList<IndexVO>();
		for (int i = 0; i < indexElements.length; i++) {
			Element index = indexElements[i];

			IndexVO indexVO = new IndexVO();
			// id, code, name
			String indexId = XMLUtil.getAttribute(index, IParse.INDEXES_ID_ATTRIBUTE);
			indexVO.setId(indexId);
			Element indexCodeElement = XMLUtil.getChildElementByName(index, "a:Code");
			String indexCode = XMLUtil.getContent(indexCodeElement);
			indexVO.setCode(indexCode);
			Element indexNameElement = XMLUtil.getChildElementByName(index, "a:Name");
			String indexName = XMLUtil.getContent(indexNameElement);
			indexVO.setName(indexName);

			// unique
			Element uniqueIndexElement = XMLUtil.getChildElementByName(index, "a:Unique");
			String uniqueIndex = XMLUtil.getContent(uniqueIndexElement);
			if (uniqueIndex != null && uniqueIndex.equalsIgnoreCase("1"))
				indexVO.setUnique(true);
			else
				indexVO.setUnique(false);

			// columns
			Element indexColumns = XMLUtil.getChildElementByName(index, "c:IndexColumns");
			if (indexColumns != null) {
				Element[] indexColumnElements = XMLUtil.getChildrenElementsByName(indexColumns, "o:IndexColumn");
				if (indexColumnElements == null || indexColumnElements.length == 0)
					indexVO.setColumnDefs(null);

//				Vector columns = new Vector(indexColumnElements.length);
				List<ColumnVO> columns = new ArrayList<ColumnVO>();
				for (int j = 0; j < indexColumnElements.length; j++) {
					Element indexColumn = indexColumnElements[j];
					Element column = XMLUtil.getChildElementByName(XMLUtil.getChildElementByName(indexColumn,
							"c:Column"), "o:Column");
					String columnRef = XMLUtil.getAttribute(column, IParse.INDEXES_COLUMN_REF_ATTRIBUTE);
					ColumnVO columnVO = getColumnVOByColumnID(columnRef, table_vo);
					MainPlugin.getDefault().logInfo("index id: " + indexId + ", column id: " + columnRef + ", column name: "
							+ columnVO.getName() + ", column code: " + columnVO.getCode());
					columns.add(columnVO);
				}// for
				indexVO.setColumnDefs(columns);
			} else {
				indexVO.setColumnDefs(null);
			}
			result.add(indexVO);
		}// for
		return result;
	}

	/**
	 * 该方法以XPath方式为指定url的表元素生成索引VO集合
	 * 
	 * @param url_table
	 * @param _doc
	 * @param table_vo
	 * @return
	 */
	private Vector parseIndexes(String url_table, Document _doc, TableVO table_vo) {
		Element indexDef = null;
		String indexId = null;
		String indexCode = null;
		String indexName = null;
		Element column = null;
		ColumnVO columnVO = null;
		IndexVO indexVO = null;

		String urlIndexes = url_table + IParse.INDEXES;
		try {
			int indexCount = ((Number) path.evaluate("count(" + urlIndexes + ")", _doc, XPathConstants.NUMBER))
					.intValue();

			if (indexCount <= 0)
				return null;

			MainPlugin.getDefault().logInfo("index 数量: " + indexCount);
			Vector v = new Vector(indexCount);
			for (int i = 0; i < indexCount; i++) {
				// id, code, name
				indexDef = (Element) path.evaluate(urlIndexes + "[" + (i + 1) + "]", _doc, XPathConstants.NODE);
				indexVO = new IndexVO();
				indexId = getAttribute(indexDef, IParse.INDEXES_ID_ATTRIBUTE);
				indexVO.setId(indexId);
				indexCode = path.evaluate(urlIndexes + "[" + (i + 1) + "]/Code", _doc);
				indexVO.setCode(indexCode);
				indexName = path.evaluate(urlIndexes + "[" + (i + 1) + "]/Name", _doc);
				indexVO.setName(indexName);

				// unique
				String unique = path.evaluate(urlIndexes + "[" + (i + 1) + "]/Unique", _doc);
				if (unique != null && unique.equalsIgnoreCase("1"))
					indexVO.setUnique(true);
				else
					indexVO.setUnique(false);

				// columns
				String urlColumn = urlIndexes + "[" + (i + 1) + "]" + IParse.INDEXES_COLUMN;
				int columnCount = ((Number) path.evaluate("count(" + urlColumn + ")", _doc, XPathConstants.NUMBER))
						.intValue();
				if (columnCount > 0) {
					Vector indexColumns = new Vector(columnCount);
					for (int j = 0; j < columnCount; j++) {
						String urlColumnRef = urlColumn + "[" + (j + 1) + "]" + IParse.INDEXES_COLUMN_REF;
						column = (Element) path.evaluate(urlColumnRef, _doc, XPathConstants.NODE);
						String columnRef = getAttribute(column, IParse.INDEXES_COLUMN_REF_ATTRIBUTE);
						columnVO = getColumnVOByColumnID(columnRef, table_vo);
						MainPlugin.getDefault().logInfo("index id: " + indexId + ", column id: " + columnRef + ", column name: "
								+ columnVO.getName() + ", column code: " + columnVO.getCode());
						indexColumns.addElement(columnVO);
					}
					indexVO.setColumnDefs(indexColumns);
				} else
					indexVO.setColumnDefs(null);

				v.addElement(indexVO);
			}/* for */
			return v;
		} catch (XPathExpressionException ex) {
			return null;
		}
	}

	/**
	 * 该方法通过非XPath方式为指定table元素生成主键VO
	 * 
	 * @param table_element
	 * @param tableVO
	 * @return
	 */
	private KeyVO parsePK(Element table_element, TableVO tableVO) {
		if (table_element == null)
			return null;

		// 获得 primary key reference
		Element primaryKey = XMLUtil.getChildElementByName(table_element, "c:PrimaryKey");
		if (primaryKey == null) {
			MainPlugin.getDefault().logInfo("该表未定义Primary Key");
			return null;
		}

		Element key = XMLUtil.getChildElementByName(primaryKey, IParse.PK_KEY);
		String keyRef = XMLUtil.getAttribute(key, IParse.PK_REF_ATTRIBUTE);
		MainPlugin.getDefault().logInfo("PK reference: " + keyRef);

		// key code
		String keyCode = getKeyCodeByKeyRef(table_element, keyRef);

		// key columns
		Vector keyColumns = getKeyColumnsByKeyRef(table_element, keyRef, tableVO);
		if (keyColumns == null || keyColumns.size() == 0) {
			MainPlugin.getDefault().logInfo("primary key定义在空列上");
			return null;
		}

		// key constraint name(仅在"当前Key为"Primary
		// Key"且PK constraint name已被手工修改过的情况下", constraint name才非空)
		String constraintName = getConstraintNameByKeyRef(table_element, keyRef);

		KeyVO keyVO = new KeyVO(IParse.KEY_PRIMARY);
		keyVO.setKeyId(keyRef);
		keyVO.setCode(keyCode);
		keyVO.setConstraintName(constraintName);
		if (isClusterKey(table_element, keyVO.getKeyId()))
			keyVO.setClustered(true);
		else
			keyVO.setClustered(false);
		keyVO.setColumnDefs(keyColumns);

		return keyVO;
	}

	/**
	 * 该方法通过XPath方法为指定url的表生成主键VO
	 * 
	 * @param url_table
	 * @param _doc
	 * @param tableVO
	 * @return
	 */
	/*
	private KeyVO parsePK(String url_table, Document _doc, TableVO tableVO) {
		KeyVO keyVO = null;

		String urlPK = url_table + "/PrimaryKey";
		try {
			// 获得 primary key reference
			Element primaryKey = (Element) path.evaluate(urlPK, _doc, XPathConstants.NODE);

			if (primaryKey == null) {
				MainPlugin.getDefault().logInfo("该表未定义Primary Key");
				return null;
			}

			Element key = getChildElementByName(primaryKey, IParse.PK_KEY);
			String refPK = getAttribute(key, IParse.PK_REF_ATTRIBUTE);
			MainPlugin.getDefault().logInfo("PK reference: " + refPK);

			// key code
			String code = getKeyCodeByKeyRef(url_table, refPK, _doc);

			// key columns
			List<ColumnVO> pkColumns = getKeyColumnsByKeyRef(url_table, refPK, _doc, tableVO);
			if (pkColumns == null || pkColumns.size() == 0)
				MainPlugin.getDefault().logInfo("primary key定义在空列上");

			// key constraint name
			String constraintName = getConstraintNameByKeyRef(url_table, refPK, _doc);

			keyVO = new KeyVO(IParse.KEY_PRIMARY);
			keyVO.setKeyId(refPK);
			keyVO.setCode(code);
			keyVO.setConstraintName(constraintName);
			if (isClusterKey(url_table, _doc, keyVO.getKeyId()))
				keyVO.setClustered(true);
			else
				keyVO.setClustered(false);
			keyVO.setColumnDefs(pkColumns);

			return keyVO;
		} catch (XPathExpressionException e) {
			return null;
		}
	}
*/
	private boolean isClusterKey(Element table_element, String key_id) {
		if (table_element == null)
			return false;

		String[] sb = getClusteredObjectIDs(table_element, IParse.IS_CLUSTER_KEY);
		if (sb == null || sb.length == 0)
			return false;

		for (int i = 0; i < sb.length; i++) {
			if (key_id.trim().equalsIgnoreCase(sb[i]))
				return true;
		}
		return false;
	}

	private boolean isClusterIndex(Element table_element, String index_id) {
		if (table_element == null)
			return false;

		String[] sb = getClusteredObjectIDs(table_element, IParse.IS_CLUSTER_INDEX);
		if (sb == null || sb.length == 0)
			return false;

		for (int i = 0; i < sb.length; i++) {
			if (index_id.trim().equalsIgnoreCase(sb[i]))
				return true;
		}
		return false;
	}

	/**
	 * 该方法判断指定key是否为cluster key(XPath方式)
	 * 
	 * @param url_table
	 * @param _doc
	 * @param key_id
	 * @return
	 * @throws XPathExpressionException
	 */
	private boolean isClusterKey(String url_table, Document _doc, String key_id) throws XPathExpressionException {
		try {
			String[] sb = getClusteredKeyIDs(url_table, _doc);
			if (sb == null || sb.length == 0)
				return false;

			for (int i = 0; i < sb.length; i++) {
				if (key_id.trim().equalsIgnoreCase(sb[i]))
					return true;
			}
		} catch (XPathExpressionException e) {
			MainPlugin.getDefault().logError("isClusterKey()执行错误", e);
			throw e;
		}
		return false;
	}

	/**
	 * 该方法以非XPath方式返回指定table元素的clustered key id集合
	 * 
	 * @param table_element
	 * @return
	 */
	private String[] getClusteredObjectIDs(Element table_element, String cluster_object_type) {
		if (table_element == null)
			return null;

		Element clusterObjs = XMLUtil.getChildElementByName(table_element, "c:ClusterObject");
		if (clusterObjs == null)
			return null;

		if (cluster_object_type.equalsIgnoreCase(IParse.IS_CLUSTER_KEY)) {
			Element[] clusterKeyElements = XMLUtil.getChildrenElementsByName(clusterObjs, "o:Key");
			if (clusterKeyElements != null && clusterKeyElements.length > 0) {
				/* cluster key */
				String[] sb = new String[clusterKeyElements.length];
				for (int i = 0; i < clusterKeyElements.length; i++) {
					Element clusterKey = clusterKeyElements[i];
					String clusterKeyRef = XMLUtil.getAttribute(clusterKey, IParse.CLUSTER_KEY_ATTRIBUTE);
					MainPlugin.getDefault().logInfo("cluster key id: " + clusterKeyRef);
					sb[i] = clusterKeyRef;
				}// for
				return sb;
			} else
				return null;
		}

		if (cluster_object_type.equalsIgnoreCase(IParse.IS_CLUSTER_INDEX)) {
			Element[] clusterIndexElements = XMLUtil.getChildrenElementsByName(clusterObjs, "o:Index");
			if (clusterIndexElements != null && clusterIndexElements.length > 0) {
				/* cluster index */
				String[] sb = new String[clusterIndexElements.length];
				for (int i = 0; i < clusterIndexElements.length; i++) {
					Element clusterIndex = clusterIndexElements[i];
					String clusterIndexRef = XMLUtil.getAttribute(clusterIndex, IParse.CLUSTER_INDEX_ATTRIBUTE);
					MainPlugin.getDefault().logInfo("cluster index id: " + clusterIndexRef);
					sb[i] = clusterIndexRef;
				}// for
				return sb;
			} else
				return null;
		}

		return null;
	}

	/**
	 * 该方法以XPath方式返回指定table元素的clustered key id集合
	 * 
	 * @param url_table
	 * @param _doc
	 * @return
	 * @throws XPathExpressionException
	 */
	private String[] getClusteredKeyIDs(String url_table, Document _doc) throws XPathExpressionException {
		Element clusterKey = null;
		String clusterKeyID = null;
		String[] sb = null;

		String urlClusterKey = url_table + IParse.CLUSTER_KEY;
		try {
			int clusterKeyCount = ((Number) path.evaluate("count(" + urlClusterKey + ")", _doc, XPathConstants.NUMBER))
					.intValue();
			sb = new String[clusterKeyCount];
			for (int i = 0; i < clusterKeyCount; i++) {
				clusterKey = (Element) path.evaluate(urlClusterKey + "[" + (i + 1) + "]", _doc, XPathConstants.NODE);
				clusterKeyID = getAttribute(clusterKey, IParse.CLUSTER_KEY_ATTRIBUTE);
				MainPlugin.getDefault().logInfo("cluster key id: " + clusterKeyID);
				sb[i] = clusterKeyID;
			}
		} catch (XPathExpressionException e) {
			MainPlugin.getDefault().logError("getClusteredKeyIDs()执行错误", e);
			throw e;
		}
		return sb;
	}

	/**
	 * 该方法通过非XPath方式为指定key参照返回对应的key编码(该编码在整个PDM文件中唯一)
	 * 
	 * @param table_element
	 * @param ref_key
	 * @return
	 */
	private String getKeyCodeByKeyRef(Element table_element, String ref_key) {
		Element keys = XMLUtil.getChildElementByName(table_element, "c:Keys");
		Element[] keyElements = XMLUtil.getChildrenElementsByName(keys, "o:Key");
		if (keyElements == null || keyElements.length == 0)
			return null;

		for (int i = 0; i < keyElements.length; i++) {
			Element key = keyElements[i];
			String keyId = XMLUtil.getAttribute(key, IParse.KEY_ID_ATTRIBUTE);
			if (keyId != null && keyId.equalsIgnoreCase(ref_key)) {
				/* 找到匹配的Key元素, 返回其编码 */
				Element keyCode = XMLUtil.getChildElementByName(key, "a:Code");
				return XMLUtil.getContent(keyCode);
			}
		}// for
		return null;
	}

	/**
	 * 该方法通过XPath方式为指定key参照返回对应的key编码(该编码在整个PDM文件中唯一)
	 * 
	 * @param url_table
	 * @param ref_key
	 * @param _doc
	 * @return
	 */
	private String getKeyCodeByKeyRef(String url_table, String ref_key, Document _doc) {
		String urlKey = url_table + IParse.KEY;
		String code = null;

		try {
			int keyCount = ((Number) path.evaluate("count(" + urlKey + ")", _doc, XPathConstants.NUMBER)).intValue();
			for (int i = 0; i < keyCount; i++) {
				Element key = (Element) path.evaluate(urlKey + "[" + (i + 1) + "]", _doc, XPathConstants.NODE);
				if (key != null && getAttribute(key, IParse.KEY_ID_ATTRIBUTE) != null
						&& getAttribute(key, IParse.KEY_ID_ATTRIBUTE).equalsIgnoreCase(ref_key)) {
					// 找到指定Key, 解析其code
					String urlKeyCode = urlKey + "[" + (i + 1) + "]" + "/Code";
					code = path.evaluate(urlKeyCode, _doc);
					break;
				}
			}/* for */
			return code;
		} catch (XPathExpressionException e) {
			return null;
		}
	}

	private String getConstraintNameByKeyRef(Element table_element, String ref_key) {
		Element keys = XMLUtil.getChildElementByName(table_element, "c:Keys");
		Element[] keyElements = XMLUtil.getChildrenElementsByName(keys, "o:Key");
		if (keyElements == null || keyElements.length == 0)
			return null;

		for (int i = 0; i < keyElements.length; i++) {
			Element key = keyElements[i];
			String keyId = XMLUtil.getAttribute(key, IParse.KEY_ID_ATTRIBUTE);
			if (keyId != null && keyId.equalsIgnoreCase(ref_key)) {
				/* 找到匹配的Key元素, 解析其约束名称 */
				// Element constraint = getChildElementByName(key,
				// IParse.KEY_CONSTRAINT);
				Element constraint = XMLUtil.getChildElementByName(key, IParse.KEY_CONSTRAINT);
				if (constraint != null)
					// return getChildTextByName(constraint);
					return XMLUtil.getContent(constraint);
				else
					return null;
			}
		}// for
		return null;
	}

	/**
	 * 对于Primary Key, 该方法返回其约束名称
	 * 
	 * @param url_table
	 * @param ref_key
	 * @param _doc
	 * @return
	 */
	private String getConstraintNameByKeyRef(String url_table, String ref_key, Document _doc) {
		String urlKey = url_table + IParse.KEY;
		try {
			int keyCount = ((Number) path.evaluate("count(" + urlKey + ")", _doc, XPathConstants.NUMBER)).intValue();
			for (int i = 0; i < keyCount; i++) {
				Element key = (Element) path.evaluate(urlKey + "[" + (i + 1) + "]", _doc, XPathConstants.NODE);
				if (key != null && getAttribute(key, IParse.KEY_ID_ATTRIBUTE) != null
						&& getAttribute(key, IParse.KEY_ID_ATTRIBUTE).equalsIgnoreCase(ref_key)) {
					// 找到指定key, 解析其constraint name
					Element constraint = getChildElementByName(key, IParse.KEY_CONSTRAINT);
					if (constraint != null)
						return getChildTextByName(constraint);
					else
						return null;
				}
			}
		} catch (XPathExpressionException ex) {
			MainPlugin.getDefault().logError(ex.getMessage(),ex);
		}
		return null;
	}

	private Vector getKeyColumnsByKeyRef(Element table_element, String ref_key, TableVO table_vo) {
		Element keys = XMLUtil.getChildElementByName(table_element, "c:Keys");
		Element[] keyElements = XMLUtil.getChildrenElementsByName(keys, "o:Key");
		if (keyElements == null || keyElements.length == 0)
			return null;

		for (int i = 0; i < keyElements.length; i++) {
			Element key = keyElements[i];
			String keyId = XMLUtil.getAttribute(key, IParse.KEY_ID_ATTRIBUTE);
			if (keyId != null && keyId.equalsIgnoreCase(ref_key)) {
				/* 找到匹配的Key元素, 解析其所在列集合 */
				Element keyColumns = XMLUtil.getChildElementByName(key, "c:Key.Columns");
				Element[] keyColumnElements = XMLUtil.getChildrenElementsByName(keyColumns, "o:Column");
				if (keyColumnElements == null || keyColumnElements.length == 0)
					return null;

				Vector columns = new Vector(keyColumnElements.length);
				for (int j = 0; j < keyColumnElements.length; j++) {
					Element keyColumnElement = keyColumnElements[j];
					String columnId = XMLUtil.getAttribute(keyColumnElement, IParse.KEY_COLUMN_REF_ATTRIBUTE);
					ColumnVO columnVO = getColumnVOByColumnID(columnId, table_vo);
					MainPlugin.getDefault().logInfo("primary key column reference: " + columnId + ", column name: " + columnVO.getName()
							+ ", column code: " + columnVO.getCode());
					columns.addElement(columnVO);
				}// for
				return columns;
			}
		}// for
		return null;
	}

	/**
	 * 该方法通过XPath方式返回所参照Key所在的列VO集合
	 * 
	 * @param url_table
	 * @param ref_key
	 * @param _doc
	 * @param table_vo
	 * @return
	 */
	private List<ColumnVO> getKeyColumnsByKeyRef(String url_table, String ref_key, Document _doc, TableVO table_vo) {
		String urlKey = url_table + IParse.KEY;
		Element key = null;
		Element keyColumnRef = null;
		ColumnVO columnVO = null;
		List<ColumnVO> columns = null;

		try {
			int keyCount = ((Number) path.evaluate("count(" + urlKey + ")", _doc, XPathConstants.NUMBER)).intValue();
			for (int i = 0; i < keyCount; i++) {
				key = (Element) path.evaluate(urlKey + "[" + (i + 1) + "]", _doc, XPathConstants.NODE);
				if (key != null && getAttribute(key, IParse.KEY_ID_ATTRIBUTE) != null
						&& getAttribute(key, IParse.KEY_ID_ATTRIBUTE).equalsIgnoreCase(ref_key)) {
					// 找到指定Key, 解析其columns
					String urlKeyColumn = urlKey + "[" + (i + 1) + "]" + IParse.KEY_COLUMN;
					int columnCount = ((Number) path.evaluate("count(" + urlKeyColumn + ")", _doc,
							XPathConstants.NUMBER)).intValue();
					if (columnCount > 0) {
						columns = new ArrayList<ColumnVO>(columnCount);
						for (int j = 0; j < columnCount; j++) {
							keyColumnRef = (Element) path.evaluate(urlKeyColumn + "[" + (j + 1) + "]", _doc,
									XPathConstants.NODE);
							String columnId = getAttribute(keyColumnRef, IParse.KEY_COLUMN_REF_ATTRIBUTE);
							columnVO = getColumnVOByColumnID(columnId, table_vo);
							MainPlugin.getDefault().logInfo("primary key column reference: " + columnId + ", column name: "
									+ columnVO.getName() + ", column code: " + columnVO.getCode());
							columns.add(columnVO);
						}
					}
					break;
				}
			}/* for */
			return columns;
		} catch (XPathExpressionException e) {
			return null;
		}
	}

	private ColumnVO getColumnVOByColumnID(String column_id, TableVO table_vo) {
		if (table_vo == null || table_vo.getColumns() == null)
			return null;

		Map<String, ColumnVO> ht = table_vo.getColumns();
		return ht.get(column_id);
	}

	private String getChildTextByName(Element e) {
		NodeList children = e.getChildNodes();
		if (children == null || children.getLength() == 0)
			return null;

		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeType() == 3) {
				return children.item(i).getNodeValue();
			}
		}/* for */
		return null;
	}

	private Element getChildElementByName(Element e, String element_name) {
		NodeList children = e.getChildNodes();
		if (children == null || children.getLength() == 0)
			return null;

		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeType() == 1 && children.item(i).getNodeName().equalsIgnoreCase(element_name)) {
				return (Element) children.item(i);
			}
		}/* for */
		return null;
	}

	private String getAttribute(Element child, String attribute_name) {
		NamedNodeMap attributes = child.getAttributes();

		for (int k = 0; k < attributes.getLength(); k++) {
			if (attributes.item(k).getNodeName().equalsIgnoreCase(attribute_name)) {
				return attributes.item(k).getNodeValue();
			}
		}
		return null;
	}

	/**
	 * 该方法通过非XPath方式为指定table元素生成ColumnVO哈希表
	 * 
	 * @param table_element table的Element
	 * @return
	 */
	private Map<String, ColumnVO> parseColumnsFromTable(Element table_element) {
		if (table_element == null)
			return null;

		Element columns = XMLUtil.getChildElementByName(table_element, "c:Columns");
		if (columns == null) {
			/* 指定'表'未定义'列' */
			MainPlugin.getDefault().logInfo("该表未定义列");
			return null;
		}

		Element[] columnElements = XMLUtil.getChildrenElementsByName(columns, "o:Column");
		MainPlugin.getDefault().logInfo("column 数量: " + columnElements.length);
//		Hashtable htColumnDef = new Hashtable(columnElements.length);
		Map<String, ColumnVO> htColumnDef = new LinkedHashMap<String, ColumnVO>();
		for (int j = 0; j < columnElements.length; j++) {
			Element column = columnElements[j];

			ColumnVO columnVO = new ColumnVO();
			// column Id
			String columnId = XMLUtil.getAttribute(column, "Id");
			columnVO.setId(columnId);
			MainPlugin.getDefault().logInfo("column Id: " + columnVO.getId());

			// name
			Element columnNameElement = XMLUtil.getChildElementByName(column, "a:Name");
			String columnName = XMLUtil.getContent(columnNameElement);
			MainPlugin.getDefault().logInfo("column name: " + columnName);
			columnVO.setName(columnName);

			// code
			Element columnCodeElement = XMLUtil.getChildElementByName(column, "a:Code");
			String columnCode = XMLUtil.getContent(columnCodeElement);
			MainPlugin.getDefault().logInfo("column code: " + columnCode);
			columnVO.setCode(columnCode);

			// comment
			Element columnCommentElement = XMLUtil.getChildElementByName(column, "a:Comment");
			String columnComment = XMLUtil.getContent(columnCommentElement);
			columnVO.setComment(columnComment);

			// description
			Element columnDescriptionElement = XMLUtil.getChildElementByName(column, "a:Comment");
			String columnDescription = XMLUtil.getContent(columnDescriptionElement);
			MainPlugin.getDefault().logInfo("column description: " + columnDescription);
			columnVO.setDescription(columnDescription);

			// data type
			Element columnDataTypeElement = XMLUtil.getChildElementByName(column, "a:DataType");
			String columnDataType = XMLUtil.getContent(columnDataTypeElement);
			if (columnDataType == null || columnDataType.trim().equalsIgnoreCase(""))
				columnDataType = IParse.COLUMN_DATATYPE_UNDEFINED;
			MainPlugin.getDefault().logInfo("column data type: " + columnDataType);
			columnVO.setDataType(columnDataType);

			// length
			Element columnLengthElement = XMLUtil.getChildElementByName(column, "a:Length");
			String clumnLength = XMLUtil.getContent(columnLengthElement);
			MainPlugin.getDefault().logInfo("column length: " + clumnLength);
			columnVO.setLength(clumnLength);

			// default value
			Element columnDefaultValueElement = XMLUtil.getChildElementByName(column, "a:DefaultValue");
			String columnDefaultValue = XMLUtil.getContent(columnDefaultValueElement);
			if (columnDefaultValue != null && !columnDefaultValue.equalsIgnoreCase("")) {
				// columnDefaultValue = columnDefaultValue.trim();
				if (columnDataType.toLowerCase().lastIndexOf("char") != -1) {
					if (columnDefaultValue.lastIndexOf("'") < 0)
						columnDefaultValue = "'" + columnDefaultValue + "'";
				}
			} else
				columnDefaultValue = null;
			MainPlugin.getDefault().logInfo("column default value: " + columnDefaultValue);
			columnVO.setDefaultValue(columnDefaultValue);

			// mandatory
			Element columnMandatoryElement = XMLUtil.getChildElementByName(column, "a:Mandatory");
			String columnMandatory = XMLUtil.getContent(columnMandatoryElement);
			MainPlugin.getDefault().logInfo("column mandatory: " + columnMandatory);
			columnVO.setMandatory(columnMandatory);

			htColumnDef.put(columnVO.getId(), columnVO);
		}// for
		return htColumnDef;
	}

	/**
	 * 该方法通过XPath方式为指定url所标识的table元素生成ColumnVO哈希表
	 * 
	 * @param url_table
	 * @param _doc
	 * @return
	 */
	private Map<String, ColumnVO> parseColumns(String url_table, Document _doc) {
		String urlColumn = url_table + IParse.COLUMN;
		try {
			int columnCount = ((Number) path.evaluate("count(" + urlColumn + ")", _doc, XPathConstants.NUMBER))
					.intValue();
			MainPlugin.getDefault().logInfo("column 数量: " + columnCount);
			Map<String, ColumnVO> htColumnDef = new HashMap<String, ColumnVO>(columnCount);

			Element column = null;
			NamedNodeMap columnAttrs = null;
			Node curAttr = null;
			String attrName = null;
			String attrValue = null;
			ColumnVO columnVO = null;

			for (int j = 0; j < columnCount; j++) {
				columnVO = new ColumnVO();

				// column Id
				column = (Element) path.evaluate(urlColumn + "[" + (j + 1) + "]", _doc, XPathConstants.NODE);
				columnAttrs = column.getAttributes();
				for (int k = 0; k < columnAttrs.getLength(); k++) {
					curAttr = columnAttrs.item(k);
					if (curAttr != null) {
						attrName = curAttr.getNodeName();
						attrValue = curAttr.getNodeValue();
						if (attrName != null && attrName.equalsIgnoreCase(IParse.COLUMN_ID_ATTRIBUTE)) {
							columnVO.setId(attrValue);
							MainPlugin.getDefault().logInfo("column Id: " + columnVO.getId());
							break;
						}
					}
				}

				// name
				String columnName = path.evaluate(urlColumn + "[" + (j + 1) + "]/Name", _doc);
				MainPlugin.getDefault().logInfo("column name: " + columnName);
				columnVO.setName(columnName);

				// code
				String columnCode = path.evaluate(urlColumn + "[" + (j + 1) + "]/Code", _doc);
				MainPlugin.getDefault().logInfo("column code: " + columnCode);
				columnVO.setCode(columnCode);

				// comment
				String columnComment = path.evaluate(urlColumn + "[" + (j + 1) + "]/Comment", _doc);
				columnVO.setComment(columnComment);

				// description
				String columnDescription = path.evaluate(urlColumn + "[" + (j + 1) + "]/Comment", _doc);
				MainPlugin.getDefault().logInfo("column description: " + columnDescription);
				columnVO.setDescription(columnDescription);

				// data type
				String columnDataType = path.evaluate(urlColumn + "[" + (j + 1) + "]/DataType", _doc);
				if (columnDataType == null || columnDataType.trim().equalsIgnoreCase(""))
					columnDataType = IParse.COLUMN_DATATYPE_UNDEFINED;
				MainPlugin.getDefault().logInfo("column data type: " + columnDataType);
				columnVO.setDataType(columnDataType);

				// length
				String clumnLength = path.evaluate(urlColumn + "[" + (j + 1) + "]/Length", _doc);
				MainPlugin.getDefault().logInfo("column length: " + clumnLength);
				columnVO.setLength(clumnLength);

				// default value
				String columnDefaultValue = path.evaluate(urlColumn + "[" + (j + 1) + "]/DefaultValue", _doc);
				if (columnDefaultValue != null)
					columnDefaultValue = columnDefaultValue.trim();
				if (columnDataType.toLowerCase().lastIndexOf("char") != -1 && columnDefaultValue != null
						&& !columnDefaultValue.equalsIgnoreCase("")) {
					if (columnDefaultValue.lastIndexOf("'") < 0)
						columnDefaultValue = "'" + columnDefaultValue + "'";
				}
				MainPlugin.getDefault().logInfo("column default value: " + columnDefaultValue);
				columnVO.setDefaultValue(columnDefaultValue);

				// mandatory
				String columnMandatory = path.evaluate(urlColumn + "[" + (j + 1) + "]/Mandatory", _doc);
				MainPlugin.getDefault().logInfo("column mandatory: " + columnMandatory);
				columnVO.setMandatory(columnMandatory);

				htColumnDef.put(columnVO.getId(), columnVO);
			}/* for */

			return htColumnDef;
		} catch (XPathExpressionException ex) {
			return null;
		}
	}
}
