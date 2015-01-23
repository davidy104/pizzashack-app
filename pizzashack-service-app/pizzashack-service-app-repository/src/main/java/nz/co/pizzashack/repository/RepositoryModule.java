package nz.co.pizzashack.repository;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.convert.component.PizzashackQueryNodeToModel;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryNode;
import nz.co.pizzashack.repository.convert.template.Neo4jRestGenericConverter;
import nz.co.pizzashack.repository.impl.PizzashackRepositoryImpl;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.util.GeneralJsonRestClientAccessor;

import com.google.common.base.Function;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.Client;

/**
 * 
 * @author Davidy
 *
 */
public class RepositoryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GeneralJsonRestClientAccessor.class).toProvider(GeneralJsonRestClientAccessorProvider.class)
				.asEagerSingleton();
		bind(Neo4jRestAPIAccessor.class).asEagerSingleton();
		bind(PizzashackRepository.class).to(PizzashackRepositoryImpl.class).asEagerSingleton();
	}

	@Provides
	@Singleton
	public Neo4jRestGenericConverter neo4jRestGenericConverter(final @Named("jsonBuilder") JsonBuilder jsonBuilder, final @Named("jsonSlurper") JsonSlurper jsonSlurper) {
		return new Neo4jRestGenericConverter(jsonBuilder, jsonSlurper);
	}

	@Provides
	@Singleton
	@Named("pizzashackQueryNodeToModelConverter")
	public Function<AbstractCypherQueryNode, Pizzashack> pizzashackQueryNodeToModelConvert() {
		return new PizzashackQueryNodeToModel();
	}

	public static class GeneralJsonRestClientAccessorProvider implements Provider<GeneralJsonRestClientAccessor> {
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
