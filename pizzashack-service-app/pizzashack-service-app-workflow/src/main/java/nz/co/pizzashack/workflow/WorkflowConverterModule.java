package nz.co.pizzashack.workflow;

import java.util.Map;

import nz.co.pizzashack.model.workflow.Group;
import nz.co.pizzashack.model.workflow.MemberShip;
import nz.co.pizzashack.model.workflow.User;
import nz.co.pizzashack.workflow.convert.GroupConverter;
import nz.co.pizzashack.workflow.convert.component.GroupMapToModel;
import nz.co.pizzashack.workflow.convert.component.MembershipMapToModel;
import nz.co.pizzashack.workflow.convert.component.UserMapToModel;

import com.google.common.base.Function;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

public class WorkflowConverterModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GroupConverter.class).asEagerSingleton();
	}

	@Provides
	@Singleton
	@Named("membershipMapToModelConverter")
	public Function<Map<String, String>, MemberShip> membershipMapToModelConverter() {
		return new MembershipMapToModel();
	}

	@Provides
	@Singleton
	@Named("groupMapToModelConverter")
	public Function<Map<String, String>, Group> groupMapToModelConverter() {
		return new GroupMapToModel();
	}

	@Provides
	@Singleton
	@Named("userMapToModelConverter")
	public Function<Map<String, String>, User> userMapToModelConverter() {
		return new UserMapToModel();
	}
}
