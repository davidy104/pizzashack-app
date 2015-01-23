package nz.co.pizzashack.repository.impl;

import java.util.Set;

import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryNode;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryResult;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PizzashackRepositoryImpl implements PizzashackRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackRepositoryImpl.class);

	@Inject
	private Neo4jRestAPIAccessor neo4jRestAPIAccessor;

	@Inject
	@Named("pizzashackQueryNodeToModelConverter")
	private Function<AbstractCypherQueryNode, Pizzashack> pizzashackQueryNodeToModelConverter;

	@Override
	public String createPizzashack(final String pizzashackId, final Pizzashack addPizzashack) throws Exception {
		return neo4jRestAPIAccessor.createUniqueNode(addPizzashack, "Pizzashack", "pizzashackId");
	}

	@Override
	public Pizzashack getPizzashackById(final String pizzashackId) throws Exception {
		final String queryJson = "MATCH (p:Pizzashack{pizzashackId:{pizzashackId}}) RETURN DISTINCT p";
		final AbstractCypherQueryResult result = neo4jRestAPIAccessor.cypherQuery(queryJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, String>()
						.put("pizzashackId", pizzashackId)
						.build()));
		if (!result.getDistinctNodes().isEmpty()) {
			AbstractCypherQueryNode pizzashackNode = (AbstractCypherQueryNode) result.getDistinctNodes().toArray()[0];
			return pizzashackQueryNodeToModelConverter.apply(pizzashackNode);
		}
		return null;
	}

	@Override
	public void updatePizzashack(final String pizzashackId, final Pizzashack updatePizzashack) throws Exception {

	}

	@Override
	public void deletePizzashack(final String pizzashackId) throws Exception {
		final String deleteJson = "MATCH (p:Pizzashack{pizzashackId:{pizzashackId}})-[r]-() DELETE p,r";
		neo4jRestAPIAccessor.cypherQuery(deleteJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, String>()
						.put("pizzashackId", pizzashackId)
						.build()));
	}

	@Override
	public Set<Pizzashack> getAllPizzashack() throws Exception {
		LOGGER.info("getAllPizzashack start..");
		Set<Pizzashack> resultSet = Sets.<Pizzashack> newHashSet();
		final String queryJson = "MATCH (p:Pizzashack) RETURN p";
		final AbstractCypherQueryResult result = neo4jRestAPIAccessor.cypherQuery(queryJson, null);
		if (result != null) {
			for (final AbstractCypherQueryNode node : result.getDistinctNodes()) {
				resultSet.add(pizzashackQueryNodeToModelConverter.apply(node));
			}
		}
		return resultSet;
	}

}
