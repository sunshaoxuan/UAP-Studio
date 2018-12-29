package nc.uap.lfw.build.common;

import java.util.Comparator;

import nc.lfw.lfwtools.perspective.MainPlugin;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 属性比较器。
 * 
 * @author PH
 */
public class PropertyComparator<T>  implements Comparator<T> {
	/** 比较属性 */
	private String property;
	/** 是否升序 */
	private boolean ascending;
	
	public PropertyComparator(String property) {
		this(property, true);
	}

	public PropertyComparator(String property, boolean ascending) {
		this.property = property;
		this.ascending = ascending;
	}

	@SuppressWarnings("unchecked")
	public int compare(T o1, T o2) {
		if(o1 == null){
			return o2 == null ? 0 : ascending ? -1 : 1;
		}else{
			if(o2 == null){
				return ascending ? 1 : -1;
			}
			Object value1 = null;
			Object value2 = null;
			try {
				value1 = PropertyUtils.getProperty(o1, property);
				value2 = PropertyUtils.getProperty(o2, property);
			} catch (Exception e) {
				MainPlugin.getDefault().logError("Failed to get property( " + property + ") of Class: "+ o1.getClass().getName(),e);
			}
//			if(value1 instanceof Comparable || value)
			//make sure negative won't be the max.
			int temp = ((Comparable)value1).compareTo((Comparable)value2);
			return ascending ? temp : 0 - temp ;
		}
	}

}
