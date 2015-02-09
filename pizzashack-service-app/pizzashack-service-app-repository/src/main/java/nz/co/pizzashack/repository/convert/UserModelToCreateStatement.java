package nz.co.pizzashack.repository.convert;

import static nz.co.pizzashack.util.GenericUtils.formatDate;

import java.util.List;

import nz.co.pizzashack.model.User;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class UserModelToCreateStatement implements Function<User, String> {

	@Override
	public String apply(final User user) {
		if (user != null) {
			List<String> fieldValueList = Lists.<String> newArrayList();
			fieldValueList.add("userName:'" + user.getUserName() + "'");
			fieldValueList.add("password:'" + user.getPassword() + "'");

			if (user.getCreateTime() != null) {
				fieldValueList.add("createTime:'" + formatDate("yyyy-MM-dd hh:mm:ss", user.getCreateTime()) + "'");
			}
			return Joiner.on(",").join(fieldValueList);
		}
		return null;
	}

}
