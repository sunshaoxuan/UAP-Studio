package nc.uap.lfw.build.pub.util.pdm.dbrecord;

import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.exception.SDPBuildException;
import nc.uap.lfw.build.exception.SDPSystemMessage;
import nc.uap.lfw.build.pub.util.pdm.itf.ICast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * �ⲿ����Դ����������
 * 
 * ������ʽ: ���� �ɷ��ʷ���: getDataSourceDef
 * 
 * @author fanp
 */
public class DBRecordConfigUtil implements ICast {
	private static final Class m_PrimitiveClasses[] = { int.class, char.class, boolean.class, long.class, double.class,
			float.class, Boolean.class, Character.class, Integer.class, Long.class, Double.class, Float.class,
			String.class, BigDecimal.class };

	/** Cast����ʱ�Ƿ�����VO���Աȱ�ٶ�ӦElementԪ�� */
	private boolean m_IsNullAllowed = false;

	/**
	 * ���캯��
	 * 
	 * @param is_null_allowed
	 * @return
	 */
	public DBRecordConfigUtil(boolean is_null_allowed) {
		this.m_IsNullAllowed = is_null_allowed;
	}

	/**
	 * �÷�����ָ���ⲿ����Դ������Ѱ������ָ��product,version������UnitDataSource
	 * 
	 * @param version
	 *            �汾
	 * @param product_name
	 *            ��Ʒ����
	 * @param db_record_ds
	 *            ���е��ⲿ����Դ����
	 * @return
	 */
	public static UnitDataSource getSpecifiedUnitDataSource(String version, String product_name,
			DBRecordDataSource db_record_ds) {
		if (db_record_ds == null || version == null || product_name == null)
			return null;

		UnitDataSource[] unitDatasources = db_record_ds.getUnitDataSource();
		if (unitDatasources == null || unitDatasources.length == 0)
			return null;

		for (int i = 0; i < unitDatasources.length; i++) {
			UnitDataSource unitDataSource = unitDatasources[i];

			/* ��鵱ǰUnidataSource������product,version�Ƿ���ָ������ƥ�� */
			String unitDataSourceName = unitDataSource.getDataSourceName();
			String s = unitDataSourceName.substring(ICast.CAST_DATASOURCE_PREFIX.length());
			int p = s.indexOf("_");
			String productName = s.substring(p + 1).trim();
			String ver = s.substring(0, p);
			if (version.equalsIgnoreCase("NC" + ver) && product_name.equalsIgnoreCase(productName))
				return unitDataSource;
		}
		return null;
	}

	/**
	 * ʹ��dom����fileName������DBRecordDataSource����
	 * @param fileName
	 * @return
	 * @throws SDPBuildException
	 */
	public Object getDataSourceDef(String fileName) throws SDPBuildException {
		try {
			/*
			 * ��������XML�ļ�(��ʽһ) ����IBM DOM Parser com.ibm.xml.parsers.DOMParser
			 * parser = new DOMParser(); parser.parse(fileName); Document doc =
			 * parser.getDocument();
			 */

			/* (��ʽ��) ����Xerces DOM Parser */
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false); // �ر�"XML�﷨���"
			DocumentBuilder parser = factory.newDocumentBuilder();
			FileInputStream in = new FileInputStream(fileName);
			Document doc = parser.parse(in);

			/* ��root�ڵ���Ϊת����ʼ��, ���ȫƪ�����ļ�ת�� */
			Node root = doc.getDocumentElement();
			String doc_type = ((Element) root).getAttribute("docType");
			if (ICast.CAST_XML_DOC_TYPE_DATASOURCE.equalsIgnoreCase(doc_type)) {
				return cast(root, nc.uap.lfw.build.pub.util.pdm.dbrecord.DBRecordDataSource.class);
			} else
				return null;
		} catch (Exception e) {
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4000));
		}
	}

	public Object getDBRecordItemDef(String fileName) throws SDPBuildException {
		try {
			/*
			 * ��������XML�ļ�(��ʽһ) ����IBM DOM Parser com.ibm.xml.parsers.DOMParser
			 * parser = new DOMParser(); parser.parse(fileName); Document doc =
			 * parser.getDocument();
			 */

			/* (��ʽ��) ����Xerces DOM Parser */
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false); // �ر�"XML�﷨���"
			DocumentBuilder parser = factory.newDocumentBuilder();
			FileInputStream in = new FileInputStream(fileName);
			Document doc = parser.parse(in);

			/* ��root�ڵ���Ϊת����ʼ��, ���ȫƪ�����ļ�ת�� */
			Node root = doc.getDocumentElement();
			String doc_type = ((Element) root).getAttribute("docType");
			if (ICast.CAST_XML_DOC_TYPE_SCRIPT_ITEM.equalsIgnoreCase(doc_type)) {
				Class objClass = nc.uap.lfw.build.pub.util.pdm.vo.ItemSet.class;
				return cast(root, objClass);
			} else
				return null;
		} catch (Exception e) {
			MainPlugin.getDefault().logError("���������ļ���������!" + fileName, e);
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4001) + fileName, e);
		}
	}

	private boolean isNullElementNode(Node cur_node) {
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
	 * ��ָ��XML�ĵ��ݹ�ת��ΪĿ�������
	 * @param item ָ����item
	 * @param defaultClass ָ�������������������ʵ��������item�е����Ը�ֵ��ȥ
	 */
	private Object cast(Node item, Class defaultClass) throws Exception {
		NodeList nl = item.getChildNodes();

		String className = defaultClass.getName();
		Class classType = Class.forName(className);
		if (isPrimitiveType(classType))
			throw new Exception("Node[" + item.getNodeName() + "] �޷�ת��ΪJavaԭʼ��������");
		Object obj = classType.newInstance();
		Field[] fields = obj.getClass().getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			// ���˵�"private", "protected"����field, ������"public"����field
			if ((fields[i].getModifiers() & Modifier.PUBLIC) == 0)
				continue;

			Class fieldType = fields[i].getType();
			if (fieldType.isArray()) {
				// �����Ա
				try {
					// �õ�ƥ���Ԫ�ؼ���
					Element[] elems = getMatchedElements(fields[i], nl);
					if (elems != null) {
						// �õ������Ա��Ԫ����
						Class array_item_type = null;
						try {
							array_item_type = getArrayItemClass(fieldType.getName());
							MainPlugin.getDefault().logInfo("�����Ա��Ԫ����: " + array_item_type.getName());
						} catch (Exception e) {
							throw e;
						}

						// �õ������Ա�Ķ�������
						Object arrayObject = Array.newInstance(array_item_type, elems.length);
						if (isPrimitiveType(array_item_type)) {
							// �����Ա��Ԫ����Ϊ'Javaԭʼ��������'
							for (int j = 0; j < elems.length; j++) {
								NodeList nlc = elems[j].getChildNodes();
								if (nlc.item(0) == null)
									setArrayPrimitiveValue(arrayObject, j, array_item_type, null);
								else
									setArrayPrimitiveValue(arrayObject, j, array_item_type, nlc.item(0).getNodeValue()
											.trim());
							}
						} else {
							// �����Ա��Ԫ����Ϊ'�Զ�����������'
							for (int k = 0; k < elems.length; k++) {
								Array.set(arrayObject, k, cast((Node) elems[k], array_item_type));
							}
						}
						fields[i].set(obj, arrayObject);
					} else{
						if(isNullAllowed()){
							fields[i].set(obj, null);
						}else{
							throw new Exception("XML����Ŀ\""+ item.getNodeName()+"\"ȱ������Ϊ\"" + fields[i].getName() + "\"����Ŀ");
						}
					}
				} catch (Exception e) {
					throw e;
				}
			} else {
				// �������Ա
				Element elem = getMatchedElement(fields[i], nl);
				if(elem == null && !isNullAllowed()){
					throw new Exception("XML��ȱ������Ϊ\"" + fields[i].getName() + "\"����Ŀ");
				}

				if (isPrimitiveType(fieldType)) {
					// Javaԭʼ��������
					NodeList nlc = elem.getChildNodes();
					if (nlc.item(0) == null) // ע: 'elem'Ԫ�صĵ�һ���ӽڵ���'����'�ڵ�
						fillFieldValue(fields[i], obj, null);
					else
						fillFieldValue(fields[i], obj, nlc.item(0).getNodeValue().trim());
				} else {
					// Java��չ��������, �ݹ�ת��
					Object value_obj = cast((Node) elem, fieldType);
					fields[i].set(obj, value_obj);
				}
			}
		}// for
		return obj;
	}

	/**
	 * ����ָ�������Ա��XML�е�ƥ��Ԫ�ؼ���,��ѡƥ��Ԫ�ظ���: "field name == element tag name"
	 * @param field
	 * @param nl
	 */
	private Element[] getMatchedElements(Field field, NodeList nl) throws Exception {
		List<Element> v = new ArrayList<Element>();

		// Ѱ���뵱ǰfieldƥ���Element����
		Node curNode = null;
		for (int j = 0; j < nl.getLength(); j++) {
			if (nl.item(j).getNodeType() == 1) {
				/* ��ѡƥ��Ԫ�ظ���: "field name == element tag name" ԭ�� */
				if (field.getName().equals(nl.item(j).getNodeName())) {
					curNode = nl.item(j);
					v.add((Element) curNode);
				}
			}
		}// for

		if (v.size() == 0) {
			return null;
		}else{
			return v.toArray(new Element[0]);
		}
	}

	/**
	 * ����ָ���������Ա��XML�е�ƥ��Ԫ��
	 */
	private Element getMatchedElement(Field field, NodeList nl) throws Exception {
		// Ѱ���뵱ǰfieldƥ���Element
		Element elem = null;
		for (int j = 0; j < nl.getLength(); j++) {
			if (nl.item(j).getNodeType() == 1) {
				/* ��ѡƥ��Ԫ�ظ���: "field name == element tag name" ԭ�� */
				if (field.getName().equals(nl.item(j).getNodeName())) {
					elem = (Element) nl.item(j);
					MainPlugin.getDefault().logInfo("field[" + field.getName() + "]ƥ��: Element[" + elem.getNodeName() + "]");
					break;
				}
			}
		}// for

		return elem;
	}

	/**
	 * ���������Ա�ķ���������(CLASS name), �õ��������Ա��(CLASS)
	 */
	private Class getArrayItemClass(String className) throws Exception {
		/***********************************************************************
		 * Java�������ͳ�Ա�����Class����: int[] [I char[] [C byte[] [B long[] [J String[]
		 * [Ljava.lang.String;
		 * --------------------------------------------------
		 * -------------------- �Զ����������Ա�����Class����, ��: UnitDataSource[]
		 * [Lnc.bs.sdp.utility.caster.UnitDataSource;
		 ***********************************************************************/
		int key = className.indexOf("[L");
		if (key >= 0) {
			int lastLoc = className.indexOf(";");
			String classPureName = className.substring(key + 2, lastLoc);
			Class pureClass = Class.forName(classPureName);
			if (key == 0)
				return pureClass;

			int arrayList[] = new int[key];
			for (int i = 0; i < arrayList.length; i++) {
				arrayList[i] = 1;
			}
			return Array.newInstance(pureClass, arrayList).getClass();
		}

		String[] id = { "[B", "[C", "[I", "[J" };
		Class[] type = { byte.class, char.class, int.class, long.class };
		for (int i = 0; i < id.length; i++) {
			key = className.indexOf(id[i]);
			if (key >= 0) {
				int lastLoc = className.indexOf(";");
				Class pureClass = type[i];
				if (key == 0)
					return pureClass;

				int arrayList[] = new int[key];
				for (int j = 0; j < arrayList.length; j++) {
					arrayList[j] = 1;
				}
				return Array.newInstance(pureClass, arrayList).getClass();
			}
		}
		return null;
	}

	private void setArrayPrimitiveValue(Object arrayObject, int location, Class itemClass, String value)
			throws Exception {
		boolean isObjectPrimitiveClassA = false;
		Class classA[] = { Boolean.class, Character.class, Integer.class, Long.class, String.class, Double.class,
				Float.class, BigDecimal.class };

		for (int i = 0; i < classA.length; i++) {
			if (classA[i] == itemClass)
				isObjectPrimitiveClassA = true;
		}

		if (value != null && isObjectPrimitiveClassA) {
			// Javaԭʼ��������
			Object itemValue = null;

			if (itemClass == String.class)
				itemValue = value;
			else if (itemClass == Integer.class)
				itemValue = Integer.valueOf(value);
			else if (itemClass == Boolean.class)
				itemValue = Boolean.valueOf(value);
			else if (itemClass == Character.class)
				itemValue = Character.valueOf(value.charAt(0));
			else if (itemClass == Long.class)
				itemValue = Long.valueOf(value);
			else if (itemClass == Double.class)
				itemValue = Double.valueOf(value);
			else if (itemClass == Float.class)
				itemValue = Float.valueOf(value);
			else if (itemClass == BigDecimal.class)
				itemValue = new BigDecimal(value);

			Array.set(arrayObject, location, itemValue);
			return;
		}

		boolean isObjectPrimitiveClassB = false;
		Class classB[] = { int.class, boolean.class, char.class, long.class, double.class, float.class };

		for (int j = 0; j < classB.length; j++) {
			if (classB[j] == itemClass)
				isObjectPrimitiveClassB = true;
		}

		if (value != null && isObjectPrimitiveClassB) {
			// Javaԭʼ��������
			if (itemClass == int.class)
				Array.setInt(arrayObject, location, Integer.parseInt(value));
			else if (itemClass == boolean.class)
				Array.setBoolean(arrayObject, location, Boolean.valueOf(value).booleanValue());
			else if (itemClass == char.class)
				Array.setChar(arrayObject, location, value.charAt(0));
			else if (itemClass == long.class)
				Array.setLong(arrayObject, location, Long.parseLong(value));
			else if (itemClass == double.class)
				Array.setDouble(arrayObject, location, Double.valueOf(value).doubleValue());
			else if (itemClass == float.class)
				Array.setDouble(arrayObject, location, Float.valueOf(value).floatValue());
			return;
		}
	}

	private void fillFieldValue(Field f, Object o, String value) throws Exception {
		/* �ֶ����� */
		Class fieldType = f.getType();

		/* �Ƿ�Javaԭʼ�������� */
		boolean isObjectPrimitiveClassA = false;

		/* Javaԭʼ���������б� */
		Class classA[] = { Boolean.class, Character.class, Integer.class, Long.class, String.class, Double.class,
				Float.class, BigDecimal.class };

		for (int i = 0; i < classA.length; i++) {
			if (classA[i] == fieldType)
				isObjectPrimitiveClassA = true; // Javaԭʼ��������
		}

		if (value != null && isObjectPrimitiveClassA) {
			// Javaԭʼ��������
			Object itemValue = null;

			if (fieldType == String.class)
				itemValue = value;
			else if (fieldType == Integer.class)
				itemValue = Integer.valueOf(value);
			else if (fieldType == Boolean.class)
				itemValue = Boolean.valueOf(value);
			else if (fieldType == Character.class)
				itemValue = Character.valueOf(value.charAt(0));
			else if (fieldType == Long.class)
				itemValue = Long.valueOf(value);
			else if (fieldType == Double.class)
				itemValue = Double.valueOf(value);
			else if (fieldType == Float.class)
				itemValue = Float.valueOf(value);
			else if (fieldType == java.math.BigDecimal.class)
				itemValue = new java.math.BigDecimal(value);

			f.set(o, itemValue);
			return;
		}

		boolean isObjectPrimitiveClassB = false;

		Class classB[] = { int.class, boolean.class, char.class, long.class, double.class, float.class };

		for (int j = 0; j < classB.length; j++) {
			if (classB[j] == fieldType)
				isObjectPrimitiveClassB = true; // Javaԭʼ��������
		}

		// Javaԭʼ��������
		if (value != null && isObjectPrimitiveClassB) {
			if (fieldType == int.class)
				f.setInt(o, Integer.parseInt(value));
			else if (fieldType == boolean.class)
				f.setBoolean(o, Boolean.valueOf(value).booleanValue());
			else if (fieldType == char.class)
				f.setChar(o, value.charAt(0));
			else if (fieldType == long.class)
				f.setLong(o, Long.parseLong(value));
			else if (fieldType == double.class)
				f.setDouble(o, Double.valueOf(value).doubleValue());
			else if (fieldType == float.class)
				f.setDouble(o, Float.valueOf(value).floatValue());
			return;
		}
	}

	private boolean isNullAllowed() {
		return this.m_IsNullAllowed;
	}

	private boolean isPrimitiveType(Class cl) {
		for (int i = 0; i < m_PrimitiveClasses.length; i++) {
			if (m_PrimitiveClasses[i] == cl)
				return true;
		}
		return false;
	}
}
