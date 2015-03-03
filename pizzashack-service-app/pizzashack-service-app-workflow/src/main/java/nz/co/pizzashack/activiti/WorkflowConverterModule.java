package nz.co.pizzashack.activiti;

import java.util.Map;

import nz.co.pizzashack.activiti.convert.GroupConverter;
import nz.co.pizzashack.activiti.convert.ProcessDefinitionConverter;
import nz.co.pizzashack.activiti.convert.UserConverter;
import nz.co.pizzashack.activiti.convert.component.GroupMapToModel;
import nz.co.pizzashack.activiti.convert.component.MembershipMapToModel;
import nz.co.pizzashack.activiti.convert.component.ProcessDefinitionMapToModel;
import nz.co.pizzashack.activiti.convert.component.UserMapToModel;
import nz.co.pizzashack.model.workflow.Group;
import nz.co.pizzashack.model.workflow.MemberShip;
import nz.co.pizzashack.model.workflow.ProcessDefinition;
import nz.co.pizzashack.model.workflow.User;

import com.google.common.base.Function;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

public class WorkflowConverterModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GroupConverter.class).asEagerSingleton();
		bind(UserConverter.class).asEagerSingleton();
		bind(ProcessDefinitionConverter.class).asEagerSingleton();
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

	@Provides
	@Singleton
	@Named("processDefinitionMapToModelConverter")
	public Function<Map<String, String>, ProcessDefinition> processDefinitionMapToModelConverter() {
		return new ProcessDefinitionMapToModel();
	}
}
