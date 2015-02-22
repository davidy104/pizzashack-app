package nz.co.pizzashack.workflow.convert.component;

import java.util.Map;

import nz.co.pizzashack.model.workflow.MemberShip;

import com.google.common.base.Function;

public class MembershipMapToModel implements Function<Map<String, String>, MemberShip> {

	@Override
	public MemberShip apply(final Map<String, String> inputMap) {
		MemberShip memberShip = null;
		if (inputMap != null) {
			memberShip = new MemberShip.Builder()
					.userId(inputMap.get("userId"))
					.groupId(inputMap.get("groupId"))
					.url(inputMap.get("url"))
					.build();
		}
		return memberShip;
	}
}
