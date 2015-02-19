package nz.co.pizzashack.repository.convert;

import static nz.co.pizzashack.util.GenericUtils.formatDate;

import java.util.Map;

import nz.co.pizzashack.model.Role;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class RoleModelToMap implements Function<Role, Map<String, String>> {

	@Override
	public Map<String, String> apply(Role role) {
		Map<String, String> resultMap = Maps.<String, String> newHashMap();
		if (role != null) {
			resultMap.put("roleName", role.getRoleName());
			if (role.getCreateTime() != null) {
				resultMap.put("createTime", formatDate("yyyy-MM-dd hh:mm:ss", role.getCreateTime()));
			}
		}
		return resultMap;
	}

}
