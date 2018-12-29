package nc.uap.lfw.build.pub.util.pdm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.pub.util.pdm.dbrecord.UnitDataSource;
import nc.uap.lfw.build.pub.util.pdm.itf.ICast;
import nc.uap.lfw.build.pub.util.pdm.itf.ISDP;
import nc.uap.lfw.build.pub.util.pdm.itf.ITableHierarchy;
import nc.uap.lfw.build.pub.util.pdm.vo.Item;
import nc.uap.lfw.build.pub.util.pdm.vo.MainTable;
import nc.uap.lfw.build.pub.util.pdm.vo.SubTable;
import nc.uap.lfw.build.pub.util.pdm.vo.Table;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XML文件解析工具类
 * 
 * 可访问静态方法: getElementsByURL, getChildElementByName, getChildrenElementsByName,
 * getAttribute, getContent
 * 
 * @author fanp
 */
public class XMLUtil {
	/**
	 * 该方法解析指定XML文件并返回得到的Document对象
	 * 
	 * @param xml_file
	 * @return
	 */
	public static Document parseDOMObject(File xml_file) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		XPathFactory xpfactory = XPathFactory.newInstance();
		XPath path = xpfactory.newXPath();

		DocumentBuilder parser = null; // DOM Parser
		Document doc = null;

		/* xml文件校验 */
		if (!valid(xml_file)) {
//			System.out.println("[XMLWorker]: 校验" + xml_file.getPath() + "文件失败, 系统退出！");
//			System.exit(-10);
			MainPlugin.getDefault().logError(xml_file.getPath() + "不存在或不是xml文件。");
			throw new SdpBuildRuntimeException(xml_file.getPath() + "不存在或不是xml文件。");
		}

		/* 解析DOM对象 */
		factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false); // 关闭"XML语法检查"
		try {
			parser = factory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
//			ex.printStackTrace();
//			System.exit(-9);
			MainPlugin.getDefault().logError("解析" + xml_file.getPath() + "失败。",ex);
			throw new SdpBuildRuntimeException("解析" + xml_file.getPath() + "失败。");
		}

		FileInputStream in = null;
		try {
			in = new FileInputStream(xml_file);
			doc = parser.parse(in);
		} catch (IOException e) {
//			System.out.println("[XMLWorker]: 读入" + xml_file.getPath() + "文件错误");
//			ioe.printStackTrace();
//			System.exit(-8);
			MainPlugin.getDefault().logError("解析" + xml_file.getPath() + "失败。",e);
			throw new SdpBuildRuntimeException("解析" + xml_file.getPath() + "失败。");
		} catch (SAXException e) {
//			System.out.println("[XMLWorker]: 解析" + xml_file.getPath() + "文件错误");
//			e.printStackTrace();
//			System.exit(-11);
			MainPlugin.getDefault().logError("解析" + xml_file.getPath() + "失败。",e);
			throw new SdpBuildRuntimeException("解析" + xml_file.getPath() + "失败。");
		} finally {
			try {
				in.close();
			} catch (IOException e) {
//				System.out.println("[XMLWorker]: 关闭" + xml_file.getPath() + "文件错误");
//				ioe.printStackTrace();
				MainPlugin.getDefault().logError("关闭" + xml_file.getPath() + "失败。", e);
			}
		}
		return doc;
	}

	private static boolean valid(File xml_file) {
		if (xml_file == null)
			return false;

		if (!xml_file.exists())
			return false;

		String path = xml_file.getPath();
		int i = path.lastIndexOf(".");
		if (!path.substring(i + 1).equalsIgnoreCase("xml"))
			return false;

		return true;
	}

	/**
	 * 该方法指定XML文件中复数元素的url, 返回对应的Element对象数组
	 * 
	 * @param doc
	 * @param url
	 * @return
	 */
	public static Element[] getElementsByURL(Document doc, String url) {
		XPathFactory xpfactory = XPathFactory.newInstance();
		XPath path = xpfactory.newXPath();

		Element[] elements = null;
		try {
			int count = ((Number) path.evaluate("count(" + ISDP._VERSION_ + ")", doc, XPathConstants.NUMBER))
					.intValue();
			if (count == 0)
				return null;

			Vector v = new Vector(count);
			for (int i = 0; i < count; i++) {
				String element_url = url + "[" + (i + 1) + "]";
				Element element = (Element) path.evaluate(element_url, doc, XPathConstants.NODE);
				v.addElement(element);
			}// for
			elements = new Element[count];
			v.copyInto(elements);
		} catch (XPathExpressionException ex) {
			MainPlugin.getDefault().logError(ex);
			System.exit(-11);
		}
		return elements;
	}

	/**
	 * 该方法指定父元素对象及子元素名称, 返回唯一对应的子元素对象
	 * 
	 * @param e
	 * @param element_name
	 * @return
	 */
	public static Element getChildElementByName(Element e, String element_name) {
		if (e == null)
			return null;

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

	/**
	 * 该方法返回指定父元素的符合指定元素名称的子元素集合
	 * 
	 * @param e
	 * @param element_name
	 * @return
	 */
	public static Element[] getChildrenElementsByName(Element e, String element_name) {
		if (e == null)
			return null;

		NodeList children = e.getChildNodes();
		if (children == null || children.getLength() == 0)
			return null;

		Vector v = new Vector();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeType() == 1 && children.item(i).getNodeName().equalsIgnoreCase(element_name)) {
				v.addElement((Element) children.item(i));
			}
		}/* for */
		if (v.size() == 0)
			return null;

		Element[] elements = new Element[v.size()];
		v.copyInto(elements);

		return elements;
	}

	/**
	 * 该方法根据指定元素及属性名称, 返回元素的对应属性值
	 * 
	 * @param element
	 * @param attribute_name
	 * @return
	 */
	public static String getAttribute(Element element, String attribute_name) {
		if (element == null)
			return null;

		NamedNodeMap attributes = element.getAttributes();
		if (attributes != null && attributes.getLength() > 0) {
			for (int k = 0; k < attributes.getLength(); k++) {
				if (attributes.item(k).getNodeName().equalsIgnoreCase(attribute_name))
					return attributes.item(k).getNodeValue();
			}// for
		}
		return null;
	}

	/**
	 * 该方法返回指定元素的正文
	 * 
	 * @param e
	 * @return
	 */
	public static String getContent(Element e) {
		if (e == null)
			return null;

		NodeList children = e.getChildNodes();
		if (children == null || children.getLength() == 0)
			return null;

		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeType() == 3)
				return node.getNodeValue();
		}// for
		return null;
	}

	/**
	 * 该方法返回指定预置脚本的主-子表拓补结构
	 * 
	 * @param hierarchy_path
	 * @param item_vo
	 * @return
	 */
	public static MainTable getMultiTableHierarchyStructure(String hierarchy_file_path, Item item_vo) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false); // 关闭"XML语法检查"
			DocumentBuilder parser = factory.newDocumentBuilder();
			FileInputStream in = new FileInputStream(hierarchy_file_path);
			Document doc = parser.parse(in);
			Node root = doc.getDocumentElement();
			String doc_type = ((Element) root).getAttribute("docType");
			if (!ICast.CAST_XML_DOC_TYPE_TABLE_HIERARCHY.equalsIgnoreCase(doc_type))
				return null;

			/* 一. 构造主表VO对象 */
			MainTable mainTable = new MainTable(item_vo);
			// tableName
			Element elmtTableName = getChildElementByName((Element) root, ICast.CAST_TABLE_HIERARCHY_TABLENAME);
			if (elmtTableName != null) {
				String mainTableName = getContent(elmtTableName);
				mainTable.setTableName(mainTableName);
			}
			// sqlNo
			Element elmtSqlNo = getChildElementByName((Element) root, ICast.CAST_TABLE_HIERARCHY_SQLNO);
			if (elmtSqlNo != null) {
				String sqlNo = getContent(elmtSqlNo);
				mainTable.setSqlNo(sqlNo);
			}

			/* 二. 构造下级子表VO对象 */
			Element subTableGroup = getChildElementByName((Element) root, ICast.CAST_TABLE_HIERARCHY_SUBTABLEGROUP);
			Element[] subTables = getChildrenElementsByName(subTableGroup, ICast.CAST_TABLE_HIERARCHY_SUBTABLE);
			if (subTables != null && subTables.length > 0) {
				Vector subTableContainer = new Vector(subTables.length);

				for (int i = 0; i < subTables.length; i++) {
					/* 解析当前子表元素 */
					Element elmtSubTable = subTables[i];

					/* 转换为子表VO对象 */
					SubTable subTable = buildSubTable(elmtSubTable);

					/* 加入子表容器 */
					subTableContainer.addElement(subTable);
				}// for

				mainTable.setSubTableSet(subTableContainer);
			}
			return mainTable;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 该方法递归构造指定子表元素的对应子表VO对象
	 * 
	 * @param sub_table_element
	 * @return
	 */
	private static SubTable buildSubTable(Element sub_table_element) {
		SubTable subTable = new SubTable();

		// tableName
		Element elmtSubTableName = getChildElementByName(sub_table_element, ICast.CAST_TABLE_HIERARCHY_TABLENAME);
		if (elmtSubTableName != null) {
			String subTableName = getContent(elmtSubTableName);
			subTable.setTableName(subTableName);
		}
		// foreignKeyColumn
		Element elmtSubTableForeignKeyColumn = getChildElementByName(sub_table_element,
				ICast.CAST_TABLE_HIERARCHY_FOREIGNKEYCOLUMN);
		if (elmtSubTableForeignKeyColumn != null) {
			String subTableForeignKeyColumn = getContent(elmtSubTableForeignKeyColumn);
			subTable.setForeignKeyColumn(subTableForeignKeyColumn);
		}
		// whereCondition
		Element elmtSubTableWhereCondition = getChildElementByName(sub_table_element,
				ICast.CAST_TABLE_HIERARCHY_WHERECONDITION);
		if (elmtSubTableWhereCondition != null) {
			String subTableWhereCondition = getContent(elmtSubTableWhereCondition);
			subTable.setWhereCondition(subTableWhereCondition);
		}
		// sqlNo
		Element elmtSubTableSqlNo = getChildElementByName(sub_table_element, ICast.CAST_TABLE_HIERARCHY_SQLNO);
		if (elmtSubTableSqlNo != null) {
			String subTableSqlNo = getContent(elmtSubTableSqlNo);
			subTable.setSqlNo(subTableSqlNo);
		}
		// subTable(s)
		Element subTableGroup = getChildElementByName(sub_table_element, ICast.CAST_TABLE_HIERARCHY_SUBTABLEGROUP);
		Element[] subTables = getChildrenElementsByName(subTableGroup, ICast.CAST_TABLE_HIERARCHY_SUBTABLE);
		if (subTables != null && subTables.length > 0) {
			Vector subTableContainer = new Vector(subTables.length);

			for (int i = 0; i < subTables.length; i++) {
				/* 解析当前子表元素 */
				Element elmtSubTable = subTables[i];

				/* 转换为子表VO对象 */
				SubTable moreSubTable = buildSubTable(elmtSubTable);

				/* 加入子表容器 */
				subTableContainer.addElement(moreSubTable);
			}// for

			subTable.setSubTableSet(subTableContainer);
		}

		return subTable;
	}

	/**
	 * 该方法在指定"主-子表拓补结构"中递归查找指定表的对应SubTable对象
	 * 
	 * @param hierarchy
	 * @param table_name
	 * @return
	 */
	public static SubTable getSubTableByTableName(ITableHierarchy hierarchy, String table_name) {
		if (hierarchy == null)
			return null;

		List<SubTable> subTableSet = hierarchy.getSubTableSet();

		/* 如果当前hierarchy中已经没有下级子表了, 则直接返回null */
		if (subTableSet == null)
			return null;

		/* 如果当前hierarchy中存在下级子表, 则首先在当前表的下级子表中查找, 找到了即直接返回 */
		for (SubTable subTable : subTableSet) {
			if (subTable.getTableName().equalsIgnoreCase(table_name))
				return subTable;
		}// for

		/* 接下来在每一个子表的下级子表中查找 */
		for (SubTable sub_table : subTableSet) {
			SubTable st = getSubTableByTableName((ITableHierarchy) sub_table, table_name);
			if (st != null)
				return st;
		}// for
		return null;
	}

	/**
	 * 该方法返回一个指定元素是否不再还有下级元素
	 * 
	 * @param cur_node
	 * @return
	 */
	private static boolean isNullElementNode(Node cur_node) {
		/* Element Node */
		NodeList nl = ((Element) cur_node).getChildNodes();
		if (nl == null || nl.getLength() == 0)
			return true; // 无"子元素"

		int element_number = 0;
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeType() == 1)
				element_number += 1;
		}

		if (element_number > 0)
			return false;
		else
			return true;
	}

	/**
	 * 该方法返回指定预置脚本的主-子表拓补结构, 同时生成各级表的元数据
	 * 
	 * @param hierarchy_file_path
	 * @param item_vo
	 * @param con
	 * @return
	 * @throws SDPBuildException
	 */
	public static MainTable getMultiTableHierarchyStructure(String hierarchy_file_path, Item item_vo, SDPConnection con)
			throws SDPBuildException {
		UnitDataSource uds = con.getDataSource();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false); // 关闭"XML语法检查"
		Document doc;
		try {
			DocumentBuilder parser = factory.newDocumentBuilder();
			FileInputStream in = new FileInputStream(hierarchy_file_path);
			doc = parser.parse(in);
		} catch (FileNotFoundException e) {
			throw new SDPBuildException("文件:"+ hierarchy_file_path +"未找到!", e);
		} catch (ParserConfigurationException e) {
			throw new SDPBuildException("解析文件"+ hierarchy_file_path +"错误!", e);
		} catch (SAXException e) {
			throw new SDPBuildException("解析文件"+ hierarchy_file_path +"错误!", e);
		} catch (IOException e) {
			throw new SDPBuildException("文件:"+ hierarchy_file_path +" io错误!", e);
		}
		Node root = doc.getDocumentElement();
		String doc_type = ((Element) root).getAttribute("docType");
		if (!ICast.CAST_XML_DOC_TYPE_TABLE_HIERARCHY.equalsIgnoreCase(doc_type))
			return null;

		/* 一. 构造主表VO对象 */
		MainTable mainTable = new MainTable(item_vo);
		// tableName
		Element elmtTableName = getChildElementByName((Element) root, ICast.CAST_TABLE_HIERARCHY_TABLENAME);
		if (elmtTableName != null) {
			String mainTableName = getContent(elmtTableName);
			mainTable.setTableName(mainTableName);
		}
		// sqlNo
		Element elmtSqlNo = getChildElementByName((Element) root, ICast.CAST_TABLE_HIERARCHY_SQLNO);
		if (elmtSqlNo != null) {
			String sqlNo = getContent(elmtSqlNo);
			mainTable.setSqlNo(sqlNo);
		}
		// isAssociatedTables
		mainTable.setAssociated(true);
		// metaData
		try {
			Table tableVO = SqlBuildRuleUtil.getTable(uds.getDatabaseType(), uds.getUser(), item_vo.getItemRule(), con);
			mainTable.setTableMetaData(tableVO);
		} catch (SQLException sdp_exception) {
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4014));
		}

		/* 二. 构造下级子表VO对象 */
		Element subTableGroup = getChildElementByName((Element) root, ICast.CAST_TABLE_HIERARCHY_SUBTABLEGROUP);
		Element[] subTables = getChildrenElementsByName(subTableGroup, ICast.CAST_TABLE_HIERARCHY_SUBTABLE);
		if (subTables != null && subTables.length > 0) {
			List<SubTable> subTableSet = new ArrayList<SubTable>(subTables.length);

			for (int i = 0; i < subTables.length; i++) {
				/* 解析当前子表元素 */
				Element elmtSubTable = subTables[i];

				/* 转换为子表VO对象 */
				SubTable subTable = buildSubTable(elmtSubTable, con);

				/* 加入子表容器 */
				subTableSet.add(subTable);
			}// for

			mainTable.setSubTableSet(subTableSet);
		}
		return mainTable;
	}

	/**
	 * 该方法递归构造指定子表元素的对应VO对象, 同时生成子表的元数据
	 * 
	 * @param sub_table_element
	 * @param con
	 * @return
	 * @throws SDPBuildException
	 */
	private static SubTable buildSubTable(Element sub_table_element, SDPConnection con) throws SDPBuildException {
		UnitDataSource uds = con.getDataSource();

		SubTable subTable = new SubTable();
		// tableName
		Element elmtSubTableName = getChildElementByName(sub_table_element, ICast.CAST_TABLE_HIERARCHY_TABLENAME);
		if (elmtSubTableName != null) {
			String subTableName = getContent(elmtSubTableName);
			subTable.setTableName(subTableName);
		}
		// foreignKeyColumn
		Element elmtSubTableForeignKeyColumn = getChildElementByName(sub_table_element,
				ICast.CAST_TABLE_HIERARCHY_FOREIGNKEYCOLUMN);
		if (elmtSubTableForeignKeyColumn != null) {
			String subTableForeignKeyColumn = getContent(elmtSubTableForeignKeyColumn);
			subTable.setForeignKeyColumn(subTableForeignKeyColumn);
		}
		// whereCondition
		Element elmtSubTableWhereCondition = getChildElementByName(sub_table_element,
				ICast.CAST_TABLE_HIERARCHY_WHERECONDITION);
		if (elmtSubTableWhereCondition != null) {
			String subTableWhereCondition = getContent(elmtSubTableWhereCondition);
			subTable.setWhereCondition(subTableWhereCondition);
		}
		// sqlNo
		Element elmtSubTableSqlNo = getChildElementByName(sub_table_element, ICast.CAST_TABLE_HIERARCHY_SQLNO);
		if (elmtSubTableSqlNo != null) {
			String subTableSqlNo = getContent(elmtSubTableSqlNo);
			subTable.setSqlNo(subTableSqlNo);
		}
		// metaData
		Table tableVO = null;
		try {
			tableVO = SqlBuildRuleUtil.getTable(uds.getDatabaseType(), uds.getUser(), subTable.getTableName(),
					con);
			subTable.setTableMetaData(tableVO);
		} catch (SQLException sdp_exception) {
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4014));
		}
		
		if(tableVO != null && tableVO.getTableFields() != null && tableVO.getTableFields().length > 0){
			// subTable(s)
			Element subTableGroup = getChildElementByName(sub_table_element, ICast.CAST_TABLE_HIERARCHY_SUBTABLEGROUP);
			Element[] subTables = getChildrenElementsByName(subTableGroup, ICast.CAST_TABLE_HIERARCHY_SUBTABLE);
			if (subTables != null && subTables.length > 0) {
				Vector subTableContainer = new Vector(subTables.length);
				
				for (int i = 0; i < subTables.length; i++) {
					/* 解析当前子表元素 */
					Element elmtSubTable = subTables[i];
					
					/* 转换为子表VO对象 */
					SubTable moreSubTable = buildSubTable(elmtSubTable, con);
					
					/* 加入子表容器 */
					subTableContainer.addElement(moreSubTable);
				}// for
				
				subTable.setSubTableSet(subTableContainer);
			}
		}

		return subTable;
	}

	public static String escape(String dangerous) {
		// TODO this still slightly inefficient - improve
		// TODO maybe: in some usages, Strings may be passed in
		// that already contain &amp; and such. respect those?
		if (dangerous == null) {
			return null;
		}
		if (dangerous.indexOf("&") == -1 && dangerous.indexOf("\"") == -1 && dangerous.indexOf("'") == -1
				&& dangerous.indexOf("<") == -1 && dangerous.indexOf(">") == -1) {
			return dangerous;
		} else {
			dangerous = dangerous.replaceAll("&", "&amp;");
			dangerous = dangerous.replaceAll("\"", "&quot;");
			dangerous = dangerous.replaceAll("'", "&apos;");
			dangerous = dangerous.replaceAll("<", "&lt;");
			dangerous = dangerous.replaceAll(">", "&gt;");
			return dangerous;
		}
	}
}
