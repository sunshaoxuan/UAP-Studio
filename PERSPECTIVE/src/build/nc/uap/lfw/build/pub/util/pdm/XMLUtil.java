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
 * XML�ļ�����������
 * 
 * �ɷ��ʾ�̬����: getElementsByURL, getChildElementByName, getChildrenElementsByName,
 * getAttribute, getContent
 * 
 * @author fanp
 */
public class XMLUtil {
	/**
	 * �÷�������ָ��XML�ļ������صõ���Document����
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

		/* xml�ļ�У�� */
		if (!valid(xml_file)) {
//			System.out.println("[XMLWorker]: У��" + xml_file.getPath() + "�ļ�ʧ��, ϵͳ�˳���");
//			System.exit(-10);
			MainPlugin.getDefault().logError(xml_file.getPath() + "�����ڻ���xml�ļ���");
			throw new SdpBuildRuntimeException(xml_file.getPath() + "�����ڻ���xml�ļ���");
		}

		/* ����DOM���� */
		factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false); // �ر�"XML�﷨���"
		try {
			parser = factory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
//			ex.printStackTrace();
//			System.exit(-9);
			MainPlugin.getDefault().logError("����" + xml_file.getPath() + "ʧ�ܡ�",ex);
			throw new SdpBuildRuntimeException("����" + xml_file.getPath() + "ʧ�ܡ�");
		}

		FileInputStream in = null;
		try {
			in = new FileInputStream(xml_file);
			doc = parser.parse(in);
		} catch (IOException e) {
//			System.out.println("[XMLWorker]: ����" + xml_file.getPath() + "�ļ�����");
//			ioe.printStackTrace();
//			System.exit(-8);
			MainPlugin.getDefault().logError("����" + xml_file.getPath() + "ʧ�ܡ�",e);
			throw new SdpBuildRuntimeException("����" + xml_file.getPath() + "ʧ�ܡ�");
		} catch (SAXException e) {
//			System.out.println("[XMLWorker]: ����" + xml_file.getPath() + "�ļ�����");
//			e.printStackTrace();
//			System.exit(-11);
			MainPlugin.getDefault().logError("����" + xml_file.getPath() + "ʧ�ܡ�",e);
			throw new SdpBuildRuntimeException("����" + xml_file.getPath() + "ʧ�ܡ�");
		} finally {
			try {
				in.close();
			} catch (IOException e) {
//				System.out.println("[XMLWorker]: �ر�" + xml_file.getPath() + "�ļ�����");
//				ioe.printStackTrace();
				MainPlugin.getDefault().logError("�ر�" + xml_file.getPath() + "ʧ�ܡ�", e);
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
	 * �÷���ָ��XML�ļ��и���Ԫ�ص�url, ���ض�Ӧ��Element��������
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
	 * �÷���ָ����Ԫ�ض�����Ԫ������, ����Ψһ��Ӧ����Ԫ�ض���
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
	 * �÷�������ָ����Ԫ�صķ���ָ��Ԫ�����Ƶ���Ԫ�ؼ���
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
	 * �÷�������ָ��Ԫ�ؼ���������, ����Ԫ�صĶ�Ӧ����ֵ
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
	 * �÷�������ָ��Ԫ�ص�����
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
	 * �÷�������ָ��Ԥ�ýű�����-�ӱ��ز��ṹ
	 * 
	 * @param hierarchy_path
	 * @param item_vo
	 * @return
	 */
	public static MainTable getMultiTableHierarchyStructure(String hierarchy_file_path, Item item_vo) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false); // �ر�"XML�﷨���"
			DocumentBuilder parser = factory.newDocumentBuilder();
			FileInputStream in = new FileInputStream(hierarchy_file_path);
			Document doc = parser.parse(in);
			Node root = doc.getDocumentElement();
			String doc_type = ((Element) root).getAttribute("docType");
			if (!ICast.CAST_XML_DOC_TYPE_TABLE_HIERARCHY.equalsIgnoreCase(doc_type))
				return null;

			/* һ. ��������VO���� */
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

			/* ��. �����¼��ӱ�VO���� */
			Element subTableGroup = getChildElementByName((Element) root, ICast.CAST_TABLE_HIERARCHY_SUBTABLEGROUP);
			Element[] subTables = getChildrenElementsByName(subTableGroup, ICast.CAST_TABLE_HIERARCHY_SUBTABLE);
			if (subTables != null && subTables.length > 0) {
				Vector subTableContainer = new Vector(subTables.length);

				for (int i = 0; i < subTables.length; i++) {
					/* ������ǰ�ӱ�Ԫ�� */
					Element elmtSubTable = subTables[i];

					/* ת��Ϊ�ӱ�VO���� */
					SubTable subTable = buildSubTable(elmtSubTable);

					/* �����ӱ����� */
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
	 * �÷����ݹ鹹��ָ���ӱ�Ԫ�صĶ�Ӧ�ӱ�VO����
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
				/* ������ǰ�ӱ�Ԫ�� */
				Element elmtSubTable = subTables[i];

				/* ת��Ϊ�ӱ�VO���� */
				SubTable moreSubTable = buildSubTable(elmtSubTable);

				/* �����ӱ����� */
				subTableContainer.addElement(moreSubTable);
			}// for

			subTable.setSubTableSet(subTableContainer);
		}

		return subTable;
	}

	/**
	 * �÷�����ָ��"��-�ӱ��ز��ṹ"�еݹ����ָ����Ķ�ӦSubTable����
	 * 
	 * @param hierarchy
	 * @param table_name
	 * @return
	 */
	public static SubTable getSubTableByTableName(ITableHierarchy hierarchy, String table_name) {
		if (hierarchy == null)
			return null;

		List<SubTable> subTableSet = hierarchy.getSubTableSet();

		/* �����ǰhierarchy���Ѿ�û���¼��ӱ���, ��ֱ�ӷ���null */
		if (subTableSet == null)
			return null;

		/* �����ǰhierarchy�д����¼��ӱ�, �������ڵ�ǰ����¼��ӱ��в���, �ҵ��˼�ֱ�ӷ��� */
		for (SubTable subTable : subTableSet) {
			if (subTable.getTableName().equalsIgnoreCase(table_name))
				return subTable;
		}// for

		/* ��������ÿһ���ӱ���¼��ӱ��в��� */
		for (SubTable sub_table : subTableSet) {
			SubTable st = getSubTableByTableName((ITableHierarchy) sub_table, table_name);
			if (st != null)
				return st;
		}// for
		return null;
	}

	/**
	 * �÷�������һ��ָ��Ԫ���Ƿ��ٻ����¼�Ԫ��
	 * 
	 * @param cur_node
	 * @return
	 */
	private static boolean isNullElementNode(Node cur_node) {
		/* Element Node */
		NodeList nl = ((Element) cur_node).getChildNodes();
		if (nl == null || nl.getLength() == 0)
			return true; // ��"��Ԫ��"

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
	 * �÷�������ָ��Ԥ�ýű�����-�ӱ��ز��ṹ, ͬʱ���ɸ������Ԫ����
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
		factory.setValidating(false); // �ر�"XML�﷨���"
		Document doc;
		try {
			DocumentBuilder parser = factory.newDocumentBuilder();
			FileInputStream in = new FileInputStream(hierarchy_file_path);
			doc = parser.parse(in);
		} catch (FileNotFoundException e) {
			throw new SDPBuildException("�ļ�:"+ hierarchy_file_path +"δ�ҵ�!", e);
		} catch (ParserConfigurationException e) {
			throw new SDPBuildException("�����ļ�"+ hierarchy_file_path +"����!", e);
		} catch (SAXException e) {
			throw new SDPBuildException("�����ļ�"+ hierarchy_file_path +"����!", e);
		} catch (IOException e) {
			throw new SDPBuildException("�ļ�:"+ hierarchy_file_path +" io����!", e);
		}
		Node root = doc.getDocumentElement();
		String doc_type = ((Element) root).getAttribute("docType");
		if (!ICast.CAST_XML_DOC_TYPE_TABLE_HIERARCHY.equalsIgnoreCase(doc_type))
			return null;

		/* һ. ��������VO���� */
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

		/* ��. �����¼��ӱ�VO���� */
		Element subTableGroup = getChildElementByName((Element) root, ICast.CAST_TABLE_HIERARCHY_SUBTABLEGROUP);
		Element[] subTables = getChildrenElementsByName(subTableGroup, ICast.CAST_TABLE_HIERARCHY_SUBTABLE);
		if (subTables != null && subTables.length > 0) {
			List<SubTable> subTableSet = new ArrayList<SubTable>(subTables.length);

			for (int i = 0; i < subTables.length; i++) {
				/* ������ǰ�ӱ�Ԫ�� */
				Element elmtSubTable = subTables[i];

				/* ת��Ϊ�ӱ�VO���� */
				SubTable subTable = buildSubTable(elmtSubTable, con);

				/* �����ӱ����� */
				subTableSet.add(subTable);
			}// for

			mainTable.setSubTableSet(subTableSet);
		}
		return mainTable;
	}

	/**
	 * �÷����ݹ鹹��ָ���ӱ�Ԫ�صĶ�ӦVO����, ͬʱ�����ӱ��Ԫ����
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
					/* ������ǰ�ӱ�Ԫ�� */
					Element elmtSubTable = subTables[i];
					
					/* ת��Ϊ�ӱ�VO���� */
					SubTable moreSubTable = buildSubTable(elmtSubTable, con);
					
					/* �����ӱ����� */
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
