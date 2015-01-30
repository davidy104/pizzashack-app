package nz.co.pizzashack.repository.convert;

import static nz.co.pizzashack.util.GenericUtils.parseToDate;

import java.util.Map;

import nz.co.pizzashack.model.User;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;

public class UserMetaMapToModel implements Function<Map<String, String>, User> {

	@Override
	public User apply(Map<String, String> inputMap) {
		User result = null;
		if (inputMap != null) {
			result = new User.Builder()
					.userName(inputMap.get("userName"))
					.password(inputMap.get("password"))
					.build();
			final String createTimeStr = inputMap.get("createTime");
			if (!StringUtils.isEmpty(createTimeStr)) {
				result.setCreateTime(parseToDate("yyyy-MM-dd hh:mm:ss", createTimeStr));
			}
		}
		return result;
	}

}
