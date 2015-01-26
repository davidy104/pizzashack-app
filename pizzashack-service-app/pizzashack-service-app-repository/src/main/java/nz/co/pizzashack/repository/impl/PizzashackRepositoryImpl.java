package nz.co.pizzashack.repository.impl;

import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.model.Page;
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

/**
 * 
 * @author Davidy
 *
 */
public class PizzashackRepositoryImpl implements PizzashackRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackRepositoryImpl.class);

	@Inject
	private Neo4jRestAPIAccessor neo4jRestAPIAccessor;

	@Inject
	@Named("pizzashackQueryNodeToModelConverter")
	private Function<AbstractCypherQueryNode, Pizzashack> pizzashackQueryNodeToModelConverter;

	@Inject
	@Named("pizzashackModelToJsonConverter")
	private Function<Pizzashack, String> pizzashackModelToJsonConverter;
	
	@Inject
	@Named("pizzashackMetaMapToModelConverter")
	private Function<Map<String, String>, Pizzashack> pizzashackMetaMapToModelConverter;
	

	@Override
	public String create(final String pizzashackId, final Pizzashack addPizzashack) throws Exception {
		return neo4jRestAPIAccessor.createUniqueNode(addPizzashack, "Pizzashack", "pizzashackId");
	}

	@Override
	public Pizzashack getById(final String pizzashackId) throws Exception {
		final String queryJson = "MATCH (p:Pizzashack{pizzashackId:{pizzashackId}}) RETURN DISTINCT p";
		final AbstractCypherQueryResult result = neo4jRestAPIAccessor.cypherQuery(queryJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put("pizzashackId", pizzashackId)
						.build()));
		if (result != null && !result.getDistinctNodes().isEmpty()) {
			AbstractCypherQueryNode pizzashackNode = (AbstractCypherQueryNode) result.getDistinctNodes().toArray()[0];
			return pizzashackQueryNodeToModelConverter.apply(pizzashackNode);
		}
		return null;
	}

	@Override
	public void updateById(final String pizzashackId, final Pizzashack updatePizzashack) throws Exception {
		final String props = pizzashackModelToJsonConverter.apply(updatePizzashack);
		LOGGER.info("props:{} ", props);
		final String updateJson = "MATCH (p:Pizzashack{pizzashackId:{pizzashackId}}) SET p = { props } RETURN p";
		neo4jRestAPIAccessor.cypherQuery(updateJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put("pizzashackId", pizzashackId)
						.put("props", updatePizzashack)
						.build()));
	}

	@Override
	public void deleteById(final String pizzashackId) throws Exception {
		final String deleteJson = "MATCH (p:Pizzashack{pizzashackId:{pizzashackId}}) DELETE p";
		neo4jRestAPIAccessor.cypherQuery(deleteJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put("pizzashackId", pizzashackId)
						.build()));
	}

	@Override
	public Set<Pizzashack> getAll() throws Exception {
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

	@Override
	public Page<Pizzashack> paginateAll(final int pageOffset,final int pageSize) throws Exception {
		final String queryStatement = "MATCH (p:Pizzashack) RETURN p";
		final Integer totalCount = neo4jRestAPIAccessor.getCountFromQueryStatement(queryStatement, null);
		final AbstractCypherQueryResult result = neo4jRestAPIAccessor.paginationThruQueryStatement(queryStatement, pageOffset, pageSize, null);
		Page<Pizzashack> page = new Page.Builder<Pizzashack>().pageOffset(pageOffset).pageSize(pageSize).totalCount(totalCount).build();
		
		Map<String,Map<String,String>> metaMap = result.getNodeColumnMap().get("p");
		if(metaMap != null){
			for (final Map.Entry<String,Map<String,String>> entry : metaMap.entrySet()) {
				Pizzashack pizzashack = pizzashackMetaMapToModelConverter.apply(entry.getValue());
				pizzashack.setNodeUri(entry.getKey());
				page.addContent(pizzashack);
			}
		}
		
		return page;
	}
	
	

}
