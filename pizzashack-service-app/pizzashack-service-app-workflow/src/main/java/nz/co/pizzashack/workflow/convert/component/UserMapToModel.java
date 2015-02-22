package nz.co.pizzashack.workflow.convert.component;

import groovy.util.logging.Slf4j;

import java.util.Map;

import nz.co.pizzashack.model.workflow.User;

import com.google.common.base.Function;

@Slf4j
public class UserMapToModel implements Function<Map<String, String>, User> {

	@Override
	public User apply(final Map<String, String> inputMap) {
		User user = null;
		if (inputMap != null) {
			user = new User.Builder()
					.id(inputMap.get("id"))
					.url(inputMap.get("url"))
					.firstName(inputMap.get("firstName"))
					.lastName(inputMap.get("lastName"))
					.email(inputMap.get("email"))
					.build();
		}
		return user;
	}
}
