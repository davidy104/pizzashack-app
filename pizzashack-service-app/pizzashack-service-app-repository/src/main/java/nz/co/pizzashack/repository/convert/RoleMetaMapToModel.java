package nz.co.pizzashack.repository.convert;

import static nz.co.pizzashack.util.GenericUtils.parseToDate;

import java.util.Map;

import nz.co.pizzashack.model.Role;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;

public class RoleMetaMapToModel implements Function<Map<String, String>, Role> {

	@Override
	public Role apply(Map<String, String> inputMap) {
		Role role = null;
		if (inputMap != null) {
			role = new Role.Builder().roleName(inputMap.get("roleName")).build();
			final String createTimeStr = inputMap.get("createTime");
			if (!StringUtils.isEmpty(createTimeStr)) {
				role.setCreateTime(parseToDate("yyyy-MM-dd hh:mm:ss", createTimeStr));
			}
		}
		return role;
	}

}
