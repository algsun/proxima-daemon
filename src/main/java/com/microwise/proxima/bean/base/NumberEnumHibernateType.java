package com.microwise.proxima.bean.base;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.ParameterizedType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

/**
 * 自定义hibernate 类型，将 NumberEnum 的 number 映射到数据库
 * 
 * @author gaohui
 * @date 2012-07-02
 */
public class NumberEnumHibernateType implements EnhancedUserType,
		ParameterizedType {

	public static final String ENUM = "enumClass";
	public static final String SCHEMA = "schema";
	public static final String CATALOG = "catalog";
	public static final String TABLE = "table";
	public static final String COLUMN = "column";
	public static final String TYPE = "type";

	private Class<? extends Enum> enumClass;
	private transient Object[] enumValues;
	private int sqlType = Types.INTEGER; // before any guessing

	public int[] sqlTypes() {
		return new int[] { sqlType };
	}

	public Class<? extends Enum> returnedClass() {
		return enumClass;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		return x == y;
	}

	public int hashCode(Object x) throws HibernateException {
		return x == null ? 0 : x.hashCode();
	}

	private boolean isOrdinal(int paramType) {
		switch (paramType) {
		case Types.INTEGER:
		case Types.NUMERIC:
		case Types.SMALLINT:
		case Types.TINYINT:
		case Types.BIGINT:
		case Types.DECIMAL: // for Oracle Driver
		case Types.DOUBLE: // for Oracle Driver
		case Types.FLOAT: // for Oracle Driver
			return true;
		case Types.CHAR:
		case Types.LONGVARCHAR:
		case Types.VARCHAR:
			return false;
		default:
			throw new HibernateException(
					"Unable to persist an Enum in a column of SQL Type: "
							+ paramType);
		}
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean isMutable() {
		return false;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		return cached;
	}

	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}

	public void setParameterValues(Properties parameters) {
		String enumClassName = parameters.getProperty(ENUM);
		try {
			// 获取要映射的枚举类型
			Class<?> clazz = ReflectHelper.classForName(enumClassName, this
					.getClass());
			enumClass = clazz.asSubclass(Enum.class);
			if (clazz.asSubclass(NumberEnum.class) == null) {
				throw new IllegalArgumentException(
						"enumClass must be subclass of com.microwise.bean.NumberEnum.");
			}
		} catch (ClassNotFoundException exception) {
			throw new HibernateException("Enum class not found", exception);
		}

		// 数据库映射类型，默认为 INTEGER
		String type = parameters.getProperty(TYPE);
		if (type != null) {
			sqlType = Integer.decode(type);
		}
	}

	/**
	 * Lazy init of {@link #enumValues}.
	 */
	private void initEnumValues() {
		if (enumValues == null) {
			this.enumValues = enumClass.getEnumConstants();
			if (enumValues == null) {
				throw new NullPointerException("Failed to init enumValues");
			}
		}
	}

	public String objectToSQLString(Object value) {
		boolean isOrdinal = isOrdinal(sqlType);
		if (isOrdinal) {
			int ordinal = ((Enum) value).ordinal();
			return Integer.toString(ordinal);
		} else {
			return '\'' + ((Enum) value).name() + '\'';
		}
	}

	// TODO unImplements correctly
	public String toXMLString(Object value) {
		boolean isOrdinal = isOrdinal(sqlType);
		if (isOrdinal) {
			int ordinal = ((Enum) value).ordinal();
			return Integer.toString(ordinal);
		} else {
			return ((Enum) value).name();
		}
	}

	// TODO unImplements correctly
	public Object fromXMLString(String xmlValue) {
		try {
			int ordinal = Integer.parseInt(xmlValue);
			initEnumValues();
			if (ordinal < 0 || ordinal >= enumValues.length) {
				throw new IllegalArgumentException(
						"Unknown ordinal value for enum " + enumClass + ": "
								+ ordinal);
			}
			return enumValues[ordinal];
		} catch (NumberFormatException e) {
			try {
				return Enum.valueOf(enumClass, xmlValue);
			} catch (IllegalArgumentException iae) {
				throw new IllegalArgumentException(
						"Unknown name value for enum " + enumClass + ": "
								+ xmlValue, iae);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		Object object = rs.getObject(names[0]);
		if (rs.wasNull()) {
			// log().trace("Returning null as column {}", names[0]);
			return null;
		}
		// 如果是 number 则通过 NumberEnums#valueOf 得到值
		if (object instanceof Number) {
			initEnumValues();
			int number = ((Number) object).intValue();

			// log().trace("Returning '{}' as column {}", ordinal, names[0]);
			Object value = NumberEnums.valueOf(number, enumClass
					.asSubclass(NumberEnum.class));
			if (value == null) {
				throw new IllegalStateException(String.format(
						"number\"%d\" not match enum class\"%s\"", value,
						enumClass));
			}
			return value;
		} else { // 如果是 String ,则通过 Enum#valueOf 得到值
			String name = (String) object;
			// log().trace("Returning '{}' as column {}", name, names[0]);
			try {
				return Enum.valueOf(enumClass, name);
			} catch (IllegalArgumentException iae) {
				throw new IllegalArgumentException(
						"Unknown name value for enum " + enumClass + ": "
								+ name, iae);
			}
		}
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) throws HibernateException, SQLException {
		if (value == null) {
			// log().debug("Binding null to parameter: {}", index);
			st.setNull(index, sqlType);
		} else {
			boolean isOrdinal = isOrdinal(sqlType);
			if (isOrdinal) {
				int number = ((NumberEnum) value).number();
				// log().debug("Binding '{}' to parameter: {}", ordinal, index);
				st.setObject(index, Integer.valueOf(number), sqlType);
			} else {
				String enumString = ((Enum<?>) value).name();
				// log().debug("Binding '{}' to parameter: {}", enumString,
				// index);
				st.setObject(index, enumString, sqlType);
			}
		}
	}

}