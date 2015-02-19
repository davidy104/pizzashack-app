package nz.co.pizzashack.repository.convert;

import static nz.co.pizzashack.util.GenericUtils.formatDate;

import java.util.List;

import nz.co.pizzashack.model.Role;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class RoleModelToCreateStatement implements Function<Role, String> {

	@Override
	public String apply(Role role) {
		if (role != null) {
			List<String> fieldValueList = Lists.<String> newArrayList();
			fieldValueList.add("roleName:'" + role.getRoleName() + "'");

			if (role.getCreateTime() != null) {
				fieldValueList.add("createTime:'" + formatDate("yyyy-MM-dd hh:mm:ss", role.getCreateTime()) + "'");
			}
			return Joiner.on(",").join(fieldValueList);
		}
		return null;
	}

}
