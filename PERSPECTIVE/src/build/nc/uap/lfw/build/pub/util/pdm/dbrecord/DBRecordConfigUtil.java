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
 * 外部数据源解析工具类
 * 
 * 工作方式: 多例 可访问方法: getDataSourceDef
 * 
 * @author fanp
 */
public class DBRecordConfigUtil implements ICast {
	private static final Class m_PrimitiveClasses[] = { int.class, char.class, boolean.class, long.class, double.class,
			float.class, Boolean.class, Character.class, Integer.class, Long.class, Double.class, Float.class,
			String.class, BigDecimal.class };

	/** Cast操作时是否允许VO类成员缺少对应Element元素 */
	private boolean m_IsNullAllowed = false;

	/**
	 * 构造函数
	 * 
	 * @param is_null_allowed
	 * @return
	 */
	public DBRecordConfigUtil(boolean is_null_allowed) {
		this.m_IsNullAllowed = is_null_allowed;
	}

	/**
	 * 该方法在指定外部数据源定义中寻找满足指定product,version条件的UnitDataSource
	 * 
	 * @param version
	 *            版本
	 * @param product_name
	 *            产品名称
	 * @param db_record_ds
	 *            所有的外部数据源定义
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

			/* 检查当前UnidataSource所属的product,version是否与指定条件匹配 */
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
	 * 使用dom解析fileName，生成DBRecordDataSource对象
	 * @param fileName
	 * @return
	 * @throws SDPBuildException
	 */
	public Object getDataSourceDef(String fileName) throws SDPBuildException {
		try {
			/*
			 * 解析输入XML文件(方式一) 采用IBM DOM Parser com.ibm.xml.parsers.DOMParser
			 * parser = new DOMParser(); parser.parse(fileName); Document doc =
			 * parser.getDocument();
			 */

			/* (方式二) 采用Xerces DOM Parser */
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false); // 关闭"XML语法检查"
			DocumentBuilder parser = factory.newDocumentBuilder();
			FileInputStream in = new FileInputStream(fileName);
			Document doc = parser.parse(in);

			/* 以root节点做为转换起始点, 完成全篇配置文件转换 */
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
			 * 解析输入XML文件(方式一) 采用IBM DOM Parser com.ibm.xml.parsers.DOMParser
			 * parser = new DOMParser(); parser.parse(fileName); Document doc =
			 * parser.getDocument();
			 */

			/* (方式二) 采用Xerces DOM Parser */
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false); // 关闭"XML语法检查"
			DocumentBuilder parser = factory.newDocumentBuilder();
			FileInputStream in = new FileInputStream(fileName);
			Document doc = parser.parse(in);

			/* 以root节点做为转换起始点, 完成全篇配置文件转换 */
			Node root = doc.getDocumentElement();
			String doc_type = ((Element) root).getAttribute("docType");
			if (ICast.CAST_XML_DOC_TYPE_SCRIPT_ITEM.equalsIgnoreCase(doc_type)) {
				Class objClass = nc.uap.lfw.build.pub.util.pdm.vo.ItemSet.class;
				return cast(root, objClass);
			} else
				return null;
		} catch (Exception e) {
			MainPlugin.getDefault().logError("解析配置文件发生错误!" + fileName, e);
			throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4001) + fileName, e);
		}
	}

	private boolean isNullElementNode(Node cur_node) {
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
	 * 将指定XML文档递归转换为目标类对象
	 * @param item 指定的item
	 * @param defaultClass 指定的类名，创建该类的实例，并将item中的属性赋值上去
	 */
	private Object cast(Node item, Class defaultClass) throws Exception {
		NodeList nl = item.getChildNodes();

		String className = defaultClass.getName();
		Class classType = Class.forName(className);
		if (isPrimitiveType(classType))
			throw new Exception("Node[" + item.getNodeName() + "] 无法转换为Java原始数据类型");
		Object obj = classType.newInstance();
		Field[] fields = obj.getClass().getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			// 过滤掉"private", "protected"类型field, 仅处理"public"类型field
			if ((fields[i].getModifiers() & Modifier.PUBLIC) == 0)
				continue;

			Class fieldType = fields[i].getType();
			if (fieldType.isArray()) {
				// 数组成员
				try {
					// 得到匹配的元素集合
					Element[] elems = getMatchedElements(fields[i], nl);
					if (elems != null) {
						// 得到数组成员的元素类
						Class array_item_type = null;
						try {
							array_item_type = getArrayItemClass(fieldType.getName());
							MainPlugin.getDefault().logInfo("数组成员的元素类: " + array_item_type.getName());
						} catch (Exception e) {
							throw e;
						}

						// 得到数组成员的对象数组
						Object arrayObject = Array.newInstance(array_item_type, elems.length);
						if (isPrimitiveType(array_item_type)) {
							// 数组成员的元素类为'Java原始数据类型'
							for (int j = 0; j < elems.length; j++) {
								NodeList nlc = elems[j].getChildNodes();
								if (nlc.item(0) == null)
									setArrayPrimitiveValue(arrayObject, j, array_item_type, null);
								else
									setArrayPrimitiveValue(arrayObject, j, array_item_type, nlc.item(0).getNodeValue()
											.trim());
							}
						} else {
							// 数组成员的元素类为'自定义数据类型'
							for (int k = 0; k < elems.length; k++) {
								Array.set(arrayObject, k, cast((Node) elems[k], array_item_type));
							}
						}
						fields[i].set(obj, arrayObject);
					} else{
						if(isNullAllowed()){
							fields[i].set(obj, null);
						}else{
							throw new Exception("XML中条目\""+ item.getNodeName()+"\"缺少名称为\"" + fields[i].getName() + "\"的条目");
						}
					}
				} catch (Exception e) {
					throw e;
				}
			} else {
				// 非数组成员
				Element elem = getMatchedElement(fields[i], nl);
				if(elem == null && !isNullAllowed()){
					throw new Exception("XML中缺少名称为\"" + fields[i].getName() + "\"的条目");
				}

				if (isPrimitiveType(fieldType)) {
					// Java原始数据类型
					NodeList nlc = elem.getChildNodes();
					if (nlc.item(0) == null) // 注: 'elem'元素的第一个子节点是'正文'节点
						fillFieldValue(fields[i], obj, null);
					else
						fillFieldValue(fields[i], obj, nlc.item(0).getNodeValue().trim());
				} else {
					// Java扩展数据类型, 递归转换
					Object value_obj = cast((Node) elem, fieldType);
					fields[i].set(obj, value_obj);
				}
			}
		}// for
		return obj;
	}

	/**
	 * 返回指定数组成员在XML中的匹配元素集合,挑选匹配元素根据: "field name == element tag name"
	 * @param field
	 * @param nl
	 */
	private Element[] getMatchedElements(Field field, NodeList nl) throws Exception {
		List<Element> v = new ArrayList<Element>();

		// 寻找与当前field匹配的Element集合
		Node curNode = null;
		for (int j = 0; j < nl.getLength(); j++) {
			if (nl.item(j).getNodeType() == 1) {
				/* 挑选匹配元素根据: "field name == element tag name" 原则 */
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
	 * 返回指定非数组成员在XML中的匹配元素
	 */
	private Element getMatchedElement(Field field, NodeList nl) throws Exception {
		// 寻找与当前field匹配的Element
		Element elem = null;
		for (int j = 0; j < nl.getLength(); j++) {
			if (nl.item(j).getNodeType() == 1) {
				/* 挑选匹配元素根据: "field name == element tag name" 原则 */
				if (field.getName().equals(nl.item(j).getNodeName())) {
					elem = (Element) nl.item(j);
					MainPlugin.getDefault().logInfo("field[" + field.getName() + "]匹配: Element[" + elem.getNodeName() + "]");
					break;
				}
			}
		}// for

		return elem;
	}

	/**
	 * 给定数组成员的反射类名称(CLASS name), 得到其数组成员类(CLASS)
	 */
	private Class getArrayItemClass(String className) throws Exception {
		/***********************************************************************
		 * Java数组类型成员反射的Class名称: int[] [I char[] [C byte[] [B long[] [J String[]
		 * [Ljava.lang.String;
		 * --------------------------------------------------
		 * -------------------- 自定义类数组成员反射的Class名称, 如: UnitDataSource[]
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
			// Java原始对象类型
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
			// Java原始对象类型
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
		/* 字段类型 */
		Class fieldType = f.getType();

		/* 是否Java原始对象类型 */
		boolean isObjectPrimitiveClassA = false;

		/* Java原始对象类型列表 */
		Class classA[] = { Boolean.class, Character.class, Integer.class, Long.class, String.class, Double.class,
				Float.class, BigDecimal.class };

		for (int i = 0; i < classA.length; i++) {
			if (classA[i] == fieldType)
				isObjectPrimitiveClassA = true; // Java原始对象类型
		}

		if (value != null && isObjectPrimitiveClassA) {
			// Java原始对象类型
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
				isObjectPrimitiveClassB = true; // Java原始对象类型
		}

		// Java原始数据类型
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
