package cn.succy.geccospider.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class DBObjectToJavaBean {

	public static <T> List<T> cursorToList(DBCursor cursor, Class<T> clazz)
			throws Exception {
		List<T> list = new ArrayList<T>();
		while (cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			T t = propertySetter(dbObj, clazz);
			list.add(t);
		}
		return list;
	}

	public static <T> T propertySetter(DBObject dbObj, Class<T> clazz)
			throws Exception {
		if (dbObj == null)
			return null;
		T t = clazz.newInstance();
		recyleSetter(dbObj, t);
		return t;
	}

	/***
	 * 递归所有属性
	 * 
	 * @param dbObj
	 * @param bean
	 * @throws Exception
	 */
	private static void recyleSetter(DBObject dbObj, Object bean) throws Exception {
		Iterator<String> it = dbObj.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Object value = dbObj.get(key);
			recyleValueJutisy(key, value, bean);
		}
	}

	@SuppressWarnings("rawtypes")
	private static void recyleValueJutisy(String key, Object value, Object bean)
			throws Exception {
		if (value instanceof BasicDBList) {
			BasicDBList dblist = (BasicDBList) value;
			Iterator<Object> it = dblist.iterator();
			Class<?> type = null;
			try {
				type = getPropertyType(bean, key);
				List list = new ArrayList();
				while (it.hasNext()) {
					Object object = it.next();
					if (object instanceof DBObject) {
						DBObject dbItem = (DBObject) object;
						Object o = type.newInstance();
						recyleSetter(dbItem, o);
						list.add(o);
					} else if (object instanceof String) {
						list.add(object);
					}
				}
				BeanUtils.setProperty(bean, key, list);
			} catch (NoSuchFieldException e) {
			}

		} else if (value instanceof DBObject) {
			DBObject dbItem = (DBObject) value;
			Class<?> type = getPropertyType(bean, key);
			Class tclazz = Timestamp.class;
			if (type == tclazz) {
				// 时间类型
				Object otime = dbItem.get("time");
				if (otime != null) {
					long time = Long.parseLong(String.valueOf(otime));
					Timestamp st = new Timestamp(time);
					BeanUtils.setProperty(bean, key, st);
				}
			} else {
				Object o = type.newInstance();
				recyleSetter(dbItem, o);
				BeanUtils.setProperty(bean, key, o);
			}
		} else {
			Class<?> clazz = bean.getClass();
			Field field;
			try {
				field = clazz.getDeclaredField(key);
				if (field != null) {
					if (value != null) {
						if ("".equals(value)) {
							if (field.getType() == String.class)
								BeanUtils.setProperty(bean, key, value);
						} else {
								BeanUtils.setProperty(bean, key, value);
						}
					}

				}
			} catch (NoSuchFieldException e) {
			}

		}
	}

	/**
	 * @param bean
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Class getPropertyType(Object bean, String key) throws Exception {
		Class<?> clazz = bean.getClass();
		Field f = clazz.getDeclaredField(key);
		Class t = f.getType();
		if (t.getName().startsWith("java.lang")) {
			return t;
		}
		if (t.isAssignableFrom(List.class) || t.isAssignableFrom(Set.class)
				|| t.isAssignableFrom(Vector.class)) {
			Type gt = f.getGenericType();
			if (gt == null) {
				return t;
			}
			if (gt instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) gt;
				Class genericClazz = (Class) pt.getActualTypeArguments()[0];
				if (genericClazz != null) {
					return genericClazz;
				}
			}
		}
		return t;
	}
}

   //使用说明：

//   例如：想把一个DBOBject对象转换为UserBean：

//UserBean user = DBObjectToJavaBean .propertySetter(dbObj,UserBean.class);
//该方法支持多层Mongo对象的转化