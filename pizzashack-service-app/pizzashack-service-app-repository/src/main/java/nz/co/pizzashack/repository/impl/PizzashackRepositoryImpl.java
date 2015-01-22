package nz.co.pizzashack.repository.impl;

import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;
import nz.co.pizzashack.repository.convert.PizzashackNeo4jRestConverter;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class PizzashackRepositoryImpl implements PizzashackRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackRepositoryImpl.class);

	@Inject
	private PizzashackNeo4jRestConverter pizzashackNeo4jRestConverter;

	@Inject
	private Neo4jRestAPIAccessor neo4jRestAPIAccessor;

	@Override
	public String createPizzashack(final String pizzashackId, final Pizzashack addPizzashack) throws Exception {
		final String createStatementJson = pizzashackNeo4jRestConverter.pizzashackModelToCreateStatementJson(addPizzashack);
		return neo4jRestAPIAccessor.createUniqueNode(createStatementJson, "pizzashackId", pizzashackId);
	}

	@Override
	public void updatePizzashack(final String pizzashackId, final Pizzashack updatePizzashack) throws Exception {

	}

}
