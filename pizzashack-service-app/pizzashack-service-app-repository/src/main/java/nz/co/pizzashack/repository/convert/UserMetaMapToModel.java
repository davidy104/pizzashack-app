package nz.co.pizzashack.repository.convert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import nz.co.pizzashack.model.User;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;

public class UserMetaMapToModel implements Function<Map<String, String>, User> {
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	@Override
	public User apply(Map<String, String> inputMap) {
		User result = null;
		if (inputMap != null) {
			result = new User.Builder()
					.userName(inputMap.get("userName"))
					.password(inputMap.get("password"))
					.build();
			try {
				final String createTimeStr = inputMap.get("createTime");
				if (!StringUtils.isEmpty(createTimeStr)) {
					result.setCreateTime(FORMAT.parse(createTimeStr));
				}
			} catch (final ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

}
