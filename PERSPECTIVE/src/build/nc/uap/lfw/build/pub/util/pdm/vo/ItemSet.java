package nc.uap.lfw.build.pub.util.pdm.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 预置脚本类型的聚合VO类
 * 
 * 工作方式: 聚合了预置脚本类型配置文件(items.xml)中定义的全部脚本类型, 由DBRecordConfigUtil对items.xml转换得到。
 * 
 * @author fanp
 */
@XStreamAlias("itemset")
public class ItemSet {
	@XStreamAlias("items")
	public Item[] item;

	/**
	 * 构造函数
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
