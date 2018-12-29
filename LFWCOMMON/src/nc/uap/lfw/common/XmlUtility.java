package nc.uap.lfw.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;


/**
 * XML DOM 方式操作基础类
 * 
 */
public class XmlUtility {
    private static class XmlPrinter {
        private static boolean lastIsAString=false;
    /**
     * 填充缩进空格，以四个空格为单位
     *  
     * @param space
     * @return
     * 创建时间：2004-10-16 16:09:21
     */
    private static String getSpace(int space) {
        char ca[] = new char[space * 4];
        for(int i = 0; i< ca.length; i += 4)
        {
            ca[i]  = ' ';
            ca[i+1]= ' ';
            ca[i+2]= ' ';
            ca[i+3]= ' ';
        }
        return new String(ca);
    }
    
    public static void printDOMTree(PrintWriter pw,Node node, int deepSet) {
        XmlPrinter.printDOMTree( pw,  node, deepSet, "gb2312");
        
    }
    /**
     * 将xml节点按照xml的文件格式输出的方法
     *  
     * @param pw 输出流
     * @param node  xml节点
     * @param deepSet 节点级次，一般为0；
     * 创建时间：2004-10-16 16:10:07
     */
	public static void printDOMTree(Writer writer, Node node, int deepSet,String encoding) 
	{
		int type = node.getNodeType();
		PrintWriter pw = null;
		if (writer instanceof OutputStreamWriter) 
		{
			pw = new PrintWriter(writer);
		} else if (writer instanceof PrintWriter) {
			pw = (PrintWriter) writer;
		} else {
			throw new IllegalArgumentException("Illegal writer to print dom tree.");
		}
		switch (type) {
			// print the document element
			case Node.DOCUMENT_NODE :
				{
					pw.print("<?xml version=\"1.0\" encoding='" + encoding + "'?>");
					/**root节点前有可能有comment,只考虑一个-BY CCH 2006-01-17*/
					Node preComment = ((Document) node).getDocumentElement().getPreviousSibling();
					if(preComment!=null)
					{
						printDOMTree(pw, preComment, deepSet);
					}
					printDOMTree(pw, ((Document) node).getDocumentElement(), deepSet);
					pw.println();
					break;
				}
				// print element with attributes
			case Node.ELEMENT_NODE :
				{
					pw.println();
					pw.print(getSpace(deepSet) + "<");
					pw.print(node.getNodeName());
					NamedNodeMap attrs = node.getAttributes();
					for (int i = 0; i < attrs.getLength(); i++) {
						Node attr = attrs.item(i);
						pw.print(" " + attr.getNodeName() + "=\"" + XmlUtility.getXMLString(attr.getNodeValue()) + "\"");
						if (null == attr.getNodeValue()
							|| attr.getNodeValue().equals("null"))
							lastIsAString = true;
					}
					pw.print(">");
					NodeList children = node.getChildNodes();
					if (children != null) {
						int len = children.getLength();
						for (int i = 0; i < len; i++)
							printDOMTree(pw, children.item(i), deepSet+1);
					}
					break;
				}
				// handle entity reference nodes
			case Node.ENTITY_REFERENCE_NODE :
				{
					pw.print("&");
					pw.print(node.getNodeName());
					pw.print(";");
					break;
				}
				// print cdata sections
			case Node.CDATA_SECTION_NODE :
				{
					pw.print(getSpace(deepSet) + "<![CDATA[");
					pw.print(node.getNodeValue());
					pw.print("]]>");
					break;
				}
				// print text
			case Node.TEXT_NODE :
				{//
				 String value = node.getNodeValue();
				 //.trim();
				 if(value != null){
				 	value = value.trim();
				 	value = getXMLString(value);
				    pw.print(value);
				 }
			    lastIsAString = !"".equals(value);
				//
				    
//					pw.print(node.getNodeValue());
//					lastIsAString = true;
					break;
				}
				// print processing instruction
			case Node.PROCESSING_INSTRUCTION_NODE :
				{
					pw.print(getSpace(deepSet) + "<?");
					pw.print(node.getNodeName());
					String data = node.getNodeValue();
					{
						pw.print("");
						pw.print(data);
					}
					pw.print("?>");
					break;
				}
			case Node.COMMENT_NODE:
				{
				   pw.println();	
				   pw.print(getSpace(deepSet)+"<!--");
				   pw.print(node.getNodeValue()+"-->");
				   break;
				}
		}
		if (type == Node.ELEMENT_NODE) {
			if (!lastIsAString) {
				pw.println();
				pw.print(getSpace(deepSet) + "</");
			} else
				pw.print("</");
			pw.print(node.getNodeName());
			pw.print('>');
			lastIsAString = false;
		}
	}
    }
    
    private XmlUtility() {

    }

    /**
     * 以XML格式输出DOM树
     *
     * @param pw 输出类
     * @param node 给定的 node
     * @param deepSet 缩进深度，0 则不缩进
     */
    public static void printDOMTree(PrintWriter pw, Node node, int deepSet) {
        XmlPrinter.printDOMTree(pw, node, deepSet);
    }
    
    /**
     * 按照XML格式将结点写到输出流中去
     * 创建日期:(2001-4-14 20:09:18)
     * @param fileBuffer StringBuffer
     * @param node org.w3c.dom.Node
     * @param depth int 用来计算层次
     */
    public static  void writeXMLFormatString(
    		StringBuffer fileBuffer,
    		Node node,
    		int depth) {
    	OutputStream out = new ByteArrayOutputStream();
    	Writer writer = new OutputStreamWriter(out);
    	printDOMTree(writer,node,depth,"UTF-8");
    	try{
			writer.close();
		} 
    	catch (IOException e){
		}
		fileBuffer.append(out.toString());
    }
    
    
    public static DocumentBuilder getDocumentBuilder() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(true);
        try {
            return dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("XML解析器构造失败!");
        }
    }
    
    
    /**
     * 创建一个新文档
     * @return
     */
    public static Document getNewDocument() {
        return getDocumentBuilder().newDocument();
    }

    /**
     * 得到给定node的子node
     *
     * @param node 给定的node
     * @param tagName 要取得的子node名称
     * @return 子node
     */
    public static Node getChildNodeOf(Node node, String tagName) {
        for(Node temp = node.getFirstChild(); temp != null; temp = temp.getNextSibling())
            if(temp.getNodeType() == Node.ELEMENT_NODE
                    && tagName.equals(temp.getNodeName())) {
                return temp;
            }
        return null;
    }

    
    /**
     * 取得指定子节点的值
     * @param node
     * @param tagName
     * @return
     */
    public static String getChildNodeValueOf(Node node, String tagName) {
        if(tagName.equals(node.getNodeName())) {
            return getValueOf(node);
        }
        for(Node temp = node.getFirstChild(); temp != null; temp = temp.getNextSibling()) {
            if(temp.getNodeType() == Node.ELEMENT_NODE && tagName.equals(temp.getNodeName())) {
                return getValueOf(temp);
            }
        }
        return null;
    }

    public static final String getValueOf(Node node) {
        if(node == null) {
            return null;
        }
        else if(node instanceof Text) {
            return node.getNodeValue().trim();
        }
        else if(node instanceof Element) {
            ((Element) node).normalize();
            Node temp = node.getFirstChild();
            if(temp != null && (temp instanceof Text))
                return temp.getNodeValue().trim();
            else
                return "";
        }
        else {
            return node.getNodeValue().trim();
        }
    }

    public static final String getAtrributeValueOf(Node node, String attribute) {
        Node _node = node.getAttributes().getNamedItem(attribute);
        return getValueOf(_node);
    }

    @SuppressWarnings("unchecked")
	public static Iterator getElementsByTagName(Element element, String tag) {
        ArrayList<Element> children = new ArrayList<Element>();
        if(element != null && tag != null) {
            NodeList nodes = element.getElementsByTagName(tag);
            for(int i = 0; i < nodes.getLength(); i++) {
                Node child = nodes.item(i);
//      System.out.println("Name: " + child.getNodeName() + ", Value: " + child.getFirstChild().getNodeValue());
                children.add((Element) child);
            }
        }
        return children.iterator();
    }

    @SuppressWarnings("unchecked")
	public static Iterator getElementsByTagNames(Element element, String[] tags) {
        List<Element> children = new ArrayList<Element>();
        if(element != null && tags != null) {
            List tagList = Arrays.asList(tags);
            NodeList nodes = element.getChildNodes();
            for(int i = 0; i < nodes.getLength(); i++) {
                Node child = nodes.item(i);
                if(child.getNodeType() == Node.ELEMENT_NODE
                        && tagList.contains(((Element) child).getTagName())) {
                    children.add((Element) child);
                }
            }
        }
        return children.iterator();
    }

    /**
     * get the xml root document of the xml descriptor
     *
     * @param url the xml descriptor url
     * @return
     */
    public static Document getDocument(URL url) throws Exception {
        InputStream is = null;
        try {
            is = new BufferedInputStream(url.openStream());
            return getDocumentBuilder().parse(is);
        }
        finally {
            if(is != null) {
                try {
                    is.close();
                }
                catch(IOException e) {

                }
            }
        }
    }

    public static Document getDocument(File file) throws Exception {
    	return getDocumentBuilder().parse(file);
    }

    public static Document getDocument(String file) throws Exception {
    	return getDocumentBuilder().parse(file);
    }

    /**
     * 注意源节点及其子节点是作为子节点挂在目标节点上，所以src和dest不是同级的<br>
     * 使用此方法时务必注意(By cch)
     * Copies the source tree into the specified place in a destination
     * tree. The source node and its children are appended as children
     * of the destination node.
     * <p>
     * <em>Note:</em> This is an iterative implementation.
     */
    @SuppressWarnings("null")
	public static void copyInto(Node src, Node dest) throws DOMException {

        Document factory = dest.getOwnerDocument();

        //Node start = src;
        Node parent = null;
        Node place = src;

        // traverse source tree
        while (place != null) {

            // copy this node
            Node node = null;
            int type = place.getNodeType();
            switch (type) {
            case Node.CDATA_SECTION_NODE: {
                node = factory.createCDATASection(place.getNodeValue());
                break;
            }
            case Node.COMMENT_NODE: {
                node = factory.createComment(place.getNodeValue());
                break;
            }
            case Node.ELEMENT_NODE: {
                Element element = factory.createElement(place.getNodeName());
                node = element;
                NamedNodeMap attrs = place.getAttributes();
                int attrCount = attrs.getLength();
                for (int i = 0; i < attrCount; i++) {
                    Attr attr = (Attr) attrs.item(i);
                    String attrName = attr.getNodeName();
                    String attrValue = attr.getNodeValue();
                    element.setAttribute(attrName, attrValue);
/*
                    if (domimpl && !attr.getSpecified()) {
                        ((Attr) element.getAttributeNode(attrName)).setSpecified(false);
                    }
*/
                }
                break;
            }
            case Node.ENTITY_REFERENCE_NODE: {
                node = factory.createEntityReference(place.getNodeName());
                break;
            }
            case Node.PROCESSING_INSTRUCTION_NODE: {
                node = factory.createProcessingInstruction(place.getNodeName(),
                        place.getNodeValue());
                break;
            }
            case Node.TEXT_NODE: {
                node = factory.createTextNode(place.getNodeValue());
                break;
            }
            default: {
                throw new IllegalArgumentException("can't copy node type, " +
                        type + " (" +
                        place.getNodeName() + ')');
            }
            }
            dest.appendChild(node);
            
            // iterate over children
            if (place.hasChildNodes()) {
            	parent = place;
            	place = place.getFirstChild();
            	dest = node;
            }
            else if(parent == null)
            {
            	place = null;
            }
            else
            {
	            // advance
            	place = place.getNextSibling();
                while (place == null && parent!=null && dest!=null) {
                    place = parent.getNextSibling();
                    parent = parent.getParentNode();
                    dest = dest.getParentNode();
                }
            }

        }

    } // copyInto(Node,Node)
    /** Finds and returns the first child element node. */
    public static Element getFirstChildElement(Node parent) {

    	if(parent == null) return null;
        // search for node
        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return (Element) child;
            }
            child = child.getNextSibling();
        }

        // not found
        return null;

    } // getFirstChildElement(Node):Element

    /** Finds and returns the last child element node. */
    public static Element getLastChildElement(Node parent) {

    	if(parent == null) return null;
        // search for node
        Node child = parent.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return (Element) child;
            }
            child = child.getPreviousSibling();
        }

        // not found
        return null;

    } // getLastChildElement(Node):Element

    /** Finds and returns the next sibling element node. */
    public static Element getNextSiblingElement(Node node) {

    	if(node == null) return null;
        // search for node
        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                return (Element) sibling;
            }
            sibling = sibling.getNextSibling();
        }

        // not found
        return null;

    } // getNextSiblingElement(Node):Element


    /** Finds and returns the first child node with the given name. */
    public static Element getFirstChildElement(Node parent, String elemName) {

    	if(parent == null) return null;
        // search for node
        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getNodeName().equals(elemName)) {
                    return (Element) child;
                }
            }
            child = child.getNextSibling();
        }

        // not found
        return null;

    } // getFirstChildElement(Node,String):Element

    /** Finds and returns the last child node with the given name. */
    public static Element getLastChildElement(Node parent, String elemName) {

    	if(parent == null) return null;
        // search for node
        Node child = parent.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getNodeName().equals(elemName)) {
                    return (Element) child;
                }
            }
            child = child.getPreviousSibling();
        }

        // not found
        return null;

    } // getLastChildElement(Node,String):Element

    /** Finds and returns the next sibling node with the given name. */
    public static Element getNextSiblingElement(Node node, String elemName) {

    	if(node == null) return null;
        // search for node
        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                if (sibling.getNodeName().equals(elemName)) {
                    return (Element) sibling;
                }
            }
            sibling = sibling.getNextSibling();
        }

        // not found
        return null;

    } // getNextSiblingdElement(Node,String):Element

    /**
     * Returns the concatenated child text of the specified node.
     * This method only looks at the immediate children of type
     * <code>Node.TEXT_NODE</code> or the children of any child
     * node that is of type <code>Node.CDATA_SECTION_NODE</code>
     * for the concatenation.
     *
     * @param node The node to look at.
     */
    public static String getChildText(Node node) {

        // is there anything to do?
        if (node == null) {
            return null;
        }

        // concatenate children text
        StringBuffer str = new StringBuffer();
        Node child = node.getFirstChild();
        while (child != null) {
            short type = child.getNodeType();
            if (type == Node.TEXT_NODE) {
                str.append(child.getNodeValue());
            } else if (type == Node.CDATA_SECTION_NODE) {
                str.append(getChildText(child));
            }
            child = child.getNextSibling();
        }

        // return text value
        return str.toString();

    } // getChildText(Node):String

    /**
     * 获取简单元素Element的文本值,如果没有文本节点,返回null
     *(注意和getChildText方法的区别)
     * @param ele The node to look at.
     */
    public static String getElementText(Element ele) {

        // is there anything to do?
        if (ele == null) {
            return null;
        }
        // get children text
        Node child = ele.getFirstChild();
        if (child != null) {
            short type = child.getNodeType();
            if (type == Node.TEXT_NODE) {
                return child.getNodeValue();
            }
        }
        // return text value
        return null;
    } // getChildText(Node):String


    public static String getFirstChildElementText(Node node) {
        return getElementText(getFirstChildElement(node));
    }

    public static String getLastChildElementText(Node node) {
        return getElementText(getLastChildElement(node));
    }

    public static String getNextSiblingElementText(Node node) {
        return getElementText(getNextSiblingElement(node));
    }

    public static String getFirstChildElementText(Node node, String elemName) {
        return getElementText(getFirstChildElement(node, elemName));
    }

    public static String getLastChildElementText(Node node, String elemName) {
        return getElementText(getLastChildElement(node, elemName));
    }

    public static String getNextSiblingElementText(Node node, String elemName) {
        return getElementText(getNextSiblingElement(node, elemName));
    }

    /**
     * 创建叶子元素，注意创建的元素不会自动添加到doc
     * @param doc 所属文档
     * @param eleName 元素名
     * @param text 元素值
     * @return
     */
    public static Element createLeafElement(Document doc, String eleName, String text) {
        Element ele = doc.createElement(eleName);
        if(text!=null){
           ele.appendChild(doc.createTextNode(text));
        }
        return ele;
    }

    /**
     * 增加子元素
     * @param element
     * @param child_ele_name
     * @param text
     */
    public static void addChildElement(Element element, String child_ele_name, String text) {
        Document doc = element.getOwnerDocument();
        Element sub_element = createLeafElement(doc, child_ele_name, text);
        element.appendChild(sub_element);
    }
    
    /**
     * 返回指定文档中某个给定结点名中的第 index 个元素。
     * 创建日期：(01-4-23 14:27:11)
     * @return org.w3c.dom.Element
     * @param doc org.w3c.dom.Document
     * @param tagName java.lang.String
     * @param index int
     */
    public static Element getElement(Document doc, String tagName, int index) {
     NodeList rows = doc.getDocumentElement().getElementsByTagName(tagName);
     return (Element) rows.item(index);
    }

    /**
     * 创建新的空白XML文档
     * @return
     * @throws ParserConfigurationException
     */
    public static Document newDocument() throws ParserConfigurationException {  
        return  getDocumentBuilder().newDocument();
    }

    /**
     * 将XML文件写到writer指定的目的地  <br>
     * 例如：<br><code>
     * 	File c = new File("c:/aa.xml");
     *  Writer wr = null;
     *  try
     *  {
     *      wr = new OutputStreamWriter(new FileOutputStream(c),"UTF-8");
     *      XMLUtil.printDOMTree(wr,doc,0,"UTF-8");
     *  }
     *  catch (IOException e)
	 *  {
	 *  	e.printStackTrace();
	 *  }
	 *  finally
	 *  {
	 *    	try
	 *  	{
	 *  		wr.flush();
	 *  		wr.close();
	 *  	} catch (IOException e){}
	 *  } 
	 *  </code><br>
     * <b>请注意<b>上面有两处需要指定编码，为保持对各种字符的支持，建议使用UTF-8. <br>
     * 
     * @param pw
     * @param node  
     * @param deepSet 一般为0即可
     * @param encoding XML文件头的编码,如<?xml version="1.0" encoding='UTF-8'?>
     */
    public static void printDOMTree(Writer pw, Node node, int deepSet, String encoding) {
        XmlUtility.XmlPrinter.printDOMTree(new PrintWriter(pw), node, deepSet,encoding);
    }
    
    /**
     * 转换XML文件中的特殊字符串到正常字符<br>
     * 比如：将 "a&ltb" 转换为 "a<b"
     * @since V1.00
     * @return java.lang.String 转换完毕的字符串
     * @param value java.lang.String 待转换的字符串
     */
    public static String getRegularString(String value) {
    	if (value != null) {
    		value = value.toString().trim();
    		//因为XML中属性值不能包含字符五种固定的实体引用的字符
    		value = StringUtility.replaceAllString(value,  "&lt;","<");
    		value = StringUtility.replaceAllString(value, "&gt;",">");
    		value = StringUtility.replaceAllString(value,  "&quot;","\"");
    		value = StringUtility.replaceAllString(value,  "&apos;","\'");
    		value = StringUtility.replaceAllString(value, "&amp;","&");		
    	}
    	return value;
    }
    
    /**
     * 将给定字符串中的特殊字符转换为相应的转义字符<br>
     * 比如：将"a<b" 转换为 "a&ltb"
     * @since V1.00
     * @return java.lang.String
     * @param value java.lang.String
     */
    public static String getXMLString(String value) {
    	if (value != null) {
    		value = value.toString().trim();
    		//因为XML中属性值不能包含字符五种固定的实体引用的字符
    		value = StringUtility.replaceAllString(value, "&", "&amp;");
    		value = StringUtility.replaceAllString(value, "<", "&lt;");
    		value = StringUtility.replaceAllString(value, "<", "&lt;");
    		value = StringUtility.replaceAllString(value, ">", "&gt;");
    		value = StringUtility.replaceAllString(value, "\"", "&quot;");
    		value = StringUtility.replaceAllString(value, "\'", "&apos;");
    	}
    	return value;
    }
    
    /**
     * 取得节点node下所有的子元素(嵌套查找，包括所有子孙,非Text元素)
     * @param node
     * @return
     */
    public static List<Node> getAllChildNodes(Node node)
    {
    	if(node == null) return null;
    	List<Node> result = new ArrayList<Node>();
    	NodeList nodelist = node.getChildNodes();
    	for(int i=0;i<nodelist.getLength();i++)
    	{
    		Node curnode = nodelist.item(i);
    		int type = curnode.getNodeType();
    		if(type != Node.TEXT_NODE)
    			result.add(nodelist.item(i));
    		List<Node> childlist = getAllChildNodes(curnode);
    		if(childlist!=null)
    			result.addAll(childlist);
    	}
    	return result;
    }
	/**
	 * 将parentNode下的所有子node全部附加到destNode
	 * 会清空destNode下的TextContent
	 * @author Larrylau
	 * @param destNode
	 * @param parentNode
	 */
	public static Node appendChildNodes(Node destNode, Node parentNode)
	{
		if(destNode != null && parentNode != null)
		{
			destNode.setTextContent("");
			NodeList list = parentNode.getChildNodes();
			if (list != null)
			{
				for (int i = 0; i < list.getLength(); i++)
				{
					Node node = list.item(i);
					if (node != null)
					{
						copyInto(node, destNode);
					}
				}
			}
		}
		return destNode;
	}
	/**
	 * 根据xml片断，构造一个新的document
	 * @author Larrylau
	 * @param xmlSnippet
	 * @return
	 * @throws PfxxException
	 * @throws SAXException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static Document rebuildDocument(String xmlSnippet) throws SAXException, IOException, UnsupportedEncodingException
	{
		StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\'UTF-8\'?><root>");
		buffer.append(xmlSnippet);
		buffer.append("</root>");
		Document tmpDoc = getDocumentBuilder().parse(new ByteArrayInputStream(buffer.toString().getBytes("UTF-8")));
		return tmpDoc;
	}
	/**
	 * 删除Node下的所有子Node
	 * @author Larrylau
	 * @param parentNode
	 */
	public static Node removeChildren(Node parentNode)
	{
		if(parentNode != null)
		{
			NodeList list = parentNode.getChildNodes();
			if (list != null)
			{
				for (int i = 0; i < list.getLength(); i++)
				{
					Node node = list.item(i);
					if (node != null)
					{
						parentNode.removeChild(node);
					}
				}
			}
		}
		return parentNode;
	}
	/**
	 * 将当前Node下所有Contents输出到一个StringBuffer
	 * @author Larrylau
	 * @param parentNode
	 * @return
	 */
	public static StringBuffer getContentsOfNode(Node parentNode)
	{
		StringBuffer stringBuffer = new StringBuffer();
		if(parentNode != null)
		{
			NodeList list = parentNode.getChildNodes();
			if (list != null)
			{
				for (int i = 0; i < list.getLength(); i++)
				{
					Node node = list.item(i);
					if (node != null)
					{
						writeXMLFormatString(stringBuffer, node, 0);
					}
				}
			}
		}
		return stringBuffer;
	}
}

