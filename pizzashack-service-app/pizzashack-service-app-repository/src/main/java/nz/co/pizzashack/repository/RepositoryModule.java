package nz.co.pizzashack.repository;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.convert.PizzashackNeo4jRestConverter;
import nz.co.pizzashack.repository.convert.component.PizzashackModelToStatement;
import nz.co.pizzashack.repository.convert.component.PizzashackModelValueToString;
import nz.co.pizzashack.repository.convert.template.Neo4jRestGenericConverter;
import nz.co.pizzashack.repository.impl.PizzashackRepositoryImpl;

import com.google.common.base.Function;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * 
 * @author Davidy
 *
 */
public class RepositoryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PizzashackNeo4jRestConverter.class).asEagerSingleton();
		bind(PizzashackRepository.class).to(PizzashackRepositoryImpl.class).asEagerSingleton();
	}

	@Provides
	@Singleton
	@Named("pizzashackModelValueToString")
	public Function<Pizzashack, String> pizzashackModelValueToString() {
		return new PizzashackModelValueToString();
	}

	@Provides
	@Singleton
	@Named("pizzashackModelToStatement")
	public Function<Pizzashack, String> pizzashackModelToStatement(final @Named("pizzashackModelValueToString") Function<Pizzashack, String> pizzashackModelValueToString) {
		return new PizzashackModelToStatement(pizzashackModelValueToString);
	}

	@Provides
	@Singleton
	public Neo4jRestGenericConverter neo4jRestGenericConverter(final @Named("jsonBuilder") JsonBuilder jsonBuilder, final @Named("jsonSlurper") JsonSlurper jsonSlurper) {
		return new Neo4jRestGenericConverter(jsonBuilder, jsonSlurper);
	}
}
