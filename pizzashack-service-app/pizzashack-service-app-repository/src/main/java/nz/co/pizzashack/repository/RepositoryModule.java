package nz.co.pizzashack.repository;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import nz.co.pizzashack.repository.convert.template.Neo4jRestGenericConverter;
import nz.co.pizzashack.repository.impl.CustomerRepositoryImpl;
import nz.co.pizzashack.repository.impl.PizzashackCommentRepositoryImpl;
import nz.co.pizzashack.repository.impl.PizzashackRepositoryImpl;
import nz.co.pizzashack.repository.impl.RoleRepositoryImpl;
import nz.co.pizzashack.repository.impl.UserRepositoryImpl;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.util.GeneralJsonRestClientAccessor;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.sun.jersey.api.client.Client;

/**
 * 
 * @author Davidy
 *
 */
public class RepositoryModule extends AbstractModule {

	@Override
	protected void configure() {
		this.install(new ConverterModule());
		bind(GeneralJsonRestClientAccessor.class).annotatedWith(Names.named("neo4jGeneralJsonRestClientAccessor")).toProvider(Neo4jGeneralJsonRestClientAccessorProvider.class).asEagerSingleton();
		bind(Neo4jRestAPIAccessor.class).asEagerSingleton();
		bind(PizzashackRepository.class).to(PizzashackRepositoryImpl.class).asEagerSingleton();
		bind(UserRepository.class).to(UserRepositoryImpl.class).asEagerSingleton();
		bind(CustomerRepository.class).to(CustomerRepositoryImpl.class).asEagerSingleton();
		bind(PizzashackCommentRepository.class).to(PizzashackCommentRepositoryImpl.class).asEagerSingleton();
		bind(RoleRepository.class).to(RoleRepositoryImpl.class).asEagerSingleton();
	}

	@Provides
	@Singleton
	public Neo4jRestGenericConverter neo4jRestGenericConverter(final @Named("jsonBuilder") JsonBuilder jsonBuilder, final @Named("jsonSlurper") JsonSlurper jsonSlurper) {
		return new Neo4jRestGenericConverter(jsonBuilder, jsonSlurper);
	}

	public static class Neo4jGeneralJsonRestClientAccessorProvider implements Provider<GeneralJsonRestClientAccessor> {

		@Inject
		private Client jerseyClient;

		@Inject
		@Named("NEO4J.HOST_URI")
		private String neo4jHostUri;

		@Override
		public GeneralJsonRestClientAccessor get() {
			return new GeneralJsonRestClientAccessor(jerseyClient, neo4jHostUri);
		}

	}
}
