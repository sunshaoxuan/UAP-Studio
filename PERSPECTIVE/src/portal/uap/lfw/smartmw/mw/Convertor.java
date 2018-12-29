package uap.lfw.smartmw.mw;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Convertor {

	private static Log log = LogFactory.getLog("SMART");

	/**
	 * Convertor 构造子注解。
	 */
	public Convertor() {
		super();
	}

	public static final Object addObjectToArray(Object[] array, Object newObject) {
		Vector<Object> v = new Vector<Object>();
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				v.add(array[i]);
			}
		}
		v.add(newObject);
		return convertVectorToArray(v);
	}

	public static final Object addOrReplaceObjectToArray(Object[] array,
			String fieldNamed, Object newObject) {
		Vector<Object> v = new Vector<Object>();
		if (array == null) {
			v.addElement(newObject);
		} else {
			try {
				Field keyField = newObject.getClass().getField(fieldNamed);
				Object newKey = keyField.get(newObject);
				for (int i = 0; i < array.length; i++) {
					try {
						Object key = keyField.get(array[i]);
						if (!newKey.equals(key))
							v.addElement(array[i]);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			v.addElement(newObject);
		}
		return convertVectorToArray(v);
	}

	public final static Vector<Object> convertArrayToVector(Object[] obj) {
		Vector<Object> v = new Vector<Object>();
		if (obj == null)
			return v;
		for (int i = 0; i < obj.length; i++) {
			v.addElement(obj[i]);
		}
		return v;
	}

	public final static Object convertVectorToArray(Vector<?> v) {
		if (v.size() == 0)
			return null;
		Class<?> classType = v.elementAt(0).getClass();
		Object o = java.lang.reflect.Array.newInstance(classType, v.size());
		for (int i = 0; i < v.size(); i++) {
			java.lang.reflect.Array.set(o, i, v.elementAt(i));
		}
		return o;
	}

	public final static Object convertVectorToArray(Vector<?> v,
			Class<?> classType) {
		Object o = Array.newInstance(classType, v.size());
		for (int i = 0; i < v.size(); i++) {
			java.lang.reflect.Array.set(o, i, v.elementAt(i));
		}
		return o;
	}

	public static final Hashtable<Object, Object> getHashtable(Object[] obj,
			Field keyField) {
		Hashtable<Object, Object> ha = new Hashtable<Object, Object>();
		if (obj == null)
			return ha;
		for (int i = 0; i < obj.length; i++) {
			try {
				Object key = keyField.get(obj[i]);
				ha.put(key, obj[i]);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return ha;
	}

	public static final Hashtable<?, ?> getHashtable(Object[] obj, String named) {
		Hashtable<Object, Object> ha = new Hashtable<Object, Object>();
		if (obj == null)
			return ha;
		Field keyField = null;
		for (int i = 0; i < obj.length; i++) {
			try {
				if (keyField == null)
					keyField = obj[0].getClass().getField(named);
				Object key = keyField.get(obj[i]);
				ha.put(key, obj[i]);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return ha;
	}

	public static final Object getObjtFrmArry(Object[] array,
			String fieldNamed, Object targetKey) {
		if (array == null)
			return null;
		try {
			Field keyField = null;
			for (int i = 0; i < array.length; i++) {
				if (i == 0)
					keyField = array[0].getClass().getField(fieldNamed);
				try {
					Object key = keyField.get(array[i]);
					if (targetKey.equals(key))
						return array[i];
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static final Object removeObjectFromArray(Object[] array,
			String fieldNamed, Object keyObject) {
		Vector<Object> v = new Vector<Object>();
		if (array == null)
			return null;
		Field keyField = null;
		for (int i = 0; i < array.length; i++) {
			try {
				if (keyField == null)
					keyField = array[0].getClass().getField(fieldNamed);
				Object key = keyField.get(array[i]);
				/** 如果等于就被删除了 */
				if (!key.equals(keyObject))
					v.addElement(array[i]);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return convertVectorToArray(v);
	}

	public static final boolean replaceObjectInArray(Object[] array,
			String fieldNamed, Object newObject) {
		if (array == null) {
			return false;
		} else {
			try {
				Field keyField = newObject.getClass().getField(fieldNamed);
				Object newKey = keyField.get(newObject);
				for (int i = 0; i < array.length; i++) {
					try {
						Object key = keyField.get(array[i]);
						/** 替换元素 */
						if (newKey.equals(key)) {
							array[i] = newObject;
							return true;
						}
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return false;

	}
}