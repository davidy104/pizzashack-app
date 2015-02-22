package nz.co.pizzashack.workflow;

import nz.co.pizzashack.util.GeneralJsonRestClientAccessor;
import nz.co.pizzashack.workflow.ds.GroupDS;
import nz.co.pizzashack.workflow.ds.UserDS;
import nz.co.pizzashack.workflow.ds.impl.GroupDSImpl;
import nz.co.pizzashack.workflow.ds.impl.UserDSImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class WorkflowModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GeneralJsonRestClientAccessor.class).annotatedWith(Names.named("activitiGeneralJsonRestClientAccessor")).toProvider(ActivitiGeneralJsonRestClientAccessorProvider.class).asEagerSingleton();
		bind(GroupDS.class).to(GroupDSImpl.class).asEagerSingleton();
		bind(UserDS.class).to(UserDSImpl.class).asEagerSingleton();
	}

	public static class ActivitiGeneralJsonRestClientAccessorProvider implements Provider<GeneralJsonRestClientAccessor> {
		@Inject
		private Client jerseyClient;

		@Inject
		@Named("ACTIVITI.AUTH.USERID")
		String activitiAuthUserId;

		@Inject
		@Named("ACTIVITI.AUTH.PASSWORD")
		String activitiAuthPassword;

		@Inject
		@Named("ACTIVITI.API.BASE_URL")
		String activitiRestHostUri;

		@Override
		public GeneralJsonRestClientAccessor get() {
			jerseyClient.addFilter(new HTTPBasicAuthFilter(activitiAuthUserId, activitiAuthPassword));
			return new GeneralJsonRestClientAccessor(jerseyClient, activitiRestHostUri);
		}
	}
}
