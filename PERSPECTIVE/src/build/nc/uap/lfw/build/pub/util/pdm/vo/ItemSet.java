package nc.uap.lfw.build.pub.util.pdm.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Ԥ�ýű����͵ľۺ�VO��
 * 
 * ������ʽ: �ۺ���Ԥ�ýű����������ļ�(items.xml)�ж����ȫ���ű�����, ��DBRecordConfigUtil��items.xmlת���õ���
 * 
 * @author fanp
 */
@XStreamAlias("itemset")
public class ItemSet {
	@XStreamAlias("items")
	public Item[] item;

	/**
	 * ���캯��
	 * 
	 * @return
	 */
	public ItemSet() {
	}

	public void setItem(Item[] item) {
		this.item = item;
	}

	public Item[] getItem() {
		return item;
	}
}
