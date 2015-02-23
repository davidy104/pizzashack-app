package nz.co.pizzashack.activiti;

import nz.co.pizzashack.activiti.ds.GroupActivitiDS;
import nz.co.pizzashack.activiti.ds.UserActivitiDS;
import nz.co.pizzashack.activiti.ds.impl.GroupActivitiDSImpl;
import nz.co.pizzashack.activiti.ds.impl.UserActivitiDSImpl;
import nz.co.pizzashack.util.GeneralJsonRestClientAccessor;

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
		this.install(new WorkflowConverterModule());
		bind(GeneralJsonRestClientAccessor.class).annotatedWith(Names.named("activitiGeneralJsonRestClientAccessor")).toProvider(ActivitiGeneralJsonRestClientAccessorProvider.class).asEagerSingleton();
		bind(GroupActivitiDS.class).to(GroupActivitiDSImpl.class).asEagerSingleton();
		bind(UserActivitiDS.class).to(UserActivitiDSImpl.class).asEagerSingleton();
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
