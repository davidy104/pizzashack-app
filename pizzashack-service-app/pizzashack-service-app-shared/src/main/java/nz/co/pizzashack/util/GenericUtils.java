package nz.co.pizzashack.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericUtils.class);

	public static Object getValueByField(final Object instance, final String fieldName) {
		try {
			Field field = instance.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(instance);
		} catch (final Exception e) {
			LOGGER.error("getValueByField error.", e);
		}
		return null;
	}

	public static Date parseToDate(final String format, final String dateTimeStr) {
		try {
			final SimpleDateFormat sf = new SimpleDateFormat(format);
			return sf.parse(dateTimeStr);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String formatDate(final String format, final Date date) {
		try {
			final SimpleDateFormat sf = new SimpleDateFormat(format);
			return sf.format(date);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
