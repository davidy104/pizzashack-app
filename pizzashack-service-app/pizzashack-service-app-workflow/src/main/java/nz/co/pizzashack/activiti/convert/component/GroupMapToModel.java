package nz.co.pizzashack.activiti.convert.component;

import java.util.Map;

import nz.co.pizzashack.model.workflow.Group;

import com.google.common.base.Function;

public class GroupMapToModel implements Function<Map<String, String>, Group> {

	@Override
	public Group apply(final Map<String, String> inputMap) {
		Group group = null;
		if (inputMap != null) {
			group = new Group.Builder()
					.id(inputMap.get("id"))
					.url(inputMap.get("url"))
					.name(inputMap.get("name"))
					.type(inputMap.get("type"))
					.build();
		}
		return group;
	}
}
