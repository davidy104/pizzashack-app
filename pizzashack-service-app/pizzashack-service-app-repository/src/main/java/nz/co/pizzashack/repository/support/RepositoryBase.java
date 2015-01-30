package nz.co.pizzashack.repository.support;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static nz.co.pizzashack.util.GenericUtils.getValueByField;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.ConflictException;
import nz.co.pizzashack.model.AbstractNeo4jModel;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryNode;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryResult;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public abstract class RepositoryBase<T extends AbstractNeo4jModel, PK extends Serializable> {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(RepositoryBase.class);

	protected final String modelName;

	protected final String uniqueKey;

	public RepositoryBase(final String uniqueKey, final Class<T> modelClz) {
		super();
		modelName = modelClz.getSimpleName();
		this.uniqueKey = uniqueKey;
	}

	public String create(final T addModel) throws Exception {
		checkState(getNeo4jRestAPIAccessor() != null, "Neo4jRestAPIAccessor can not be null");
		checkArgument(addModel != null && getValueByField(addModel, uniqueKey) != null, "addModel and its unique value can not be null");
		return getNeo4jRestAPIAccessor().createUniqueNode(addModel, modelName, uniqueKey);
	}

	@SuppressWarnings("unchecked")
	protected T getBasicById(PK id, Function<Map<String, String>, T> converter) throws Exception {
		checkArgument(converter != null, "converter can not be null");
		checkState(getNeo4jRestAPIAccessor() != null, "Neo4jRestAPIAccessor can not be null");
		T returnModel = null;
		final String queryJson = "MATCH (p:" + modelName + "{" + uniqueKey + ":{" + uniqueKey + "}}) RETURN p";
		final AbstractCypherQueryResult result = this.getNeo4jRestAPIAccessor().cypherQuery(queryJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put(uniqueKey, id)
						.build()));

		if (result != null) {
			Map<String, Map<String, String>> metaMap = result.getNodeColumnMap().get("p");
			if (metaMap != null && !metaMap.isEmpty()) {
				final String nodeUri = (String) metaMap.keySet().toArray()[0];
				final Map<String, String> fieldValueMap = (Map<String, String>) metaMap.values().toArray()[0];
				returnModel = converter.apply(fieldValueMap);
				returnModel.setNodeUri(nodeUri);
			}
		}
		return returnModel;
	}

	protected Set<T> getBasicAll(final Function<Map<String, String>, T> metaMapToModelConverter) throws Exception {
		checkState(getNeo4jRestAPIAccessor() != null, "Neo4jRestAPIAccessor can not be null");
		checkArgument(metaMapToModelConverter != null, "metaMapToModelConverter can not be null");
		Set<T> resultSet = Sets.<T> newHashSet();
		final String queryJson = "MATCH (p:" + modelName + ") RETURN p";
		final AbstractCypherQueryResult result = this.getNeo4jRestAPIAccessor().cypherQuery(queryJson);
		if (result != null) {
			Map<String, Map<String, String>> metaMap = result.getNodeColumnMap().get("p");
			if (metaMap != null) {
				for (final Map.Entry<String, Map<String, String>> entry : metaMap.entrySet()) {
					T model = metaMapToModelConverter.apply(entry.getValue());
					model.setNodeUri(entry.getKey());
					resultSet.add(model);
				}
			}
		}
		return resultSet;
	}

	protected void updateBasic(final T updatedModel) throws Exception {
		checkState(getNeo4jRestAPIAccessor() != null, "Neo4jRestAPIAccessor can not be null");
		checkArgument(updatedModel != null && getValueByField(updatedModel, uniqueKey) != null, "updatedModel and its unique value can not be null");
		final String updateJson = "MATCH (p:" + modelName + "{" + uniqueKey + ":{" + uniqueKey + "}}) SET p = { props }";
		this.getNeo4jRestAPIAccessor().cypherQuery(updateJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put(uniqueKey, getValueByField(updatedModel, uniqueKey))
						.put("props", updatedModel)
						.build()));
	}

	public void deleteAllById(final PK id, final boolean forceDeleteIfHasRelationships) throws Exception {
		String deleteJson = "MATCH (p:" + modelName + "{" + uniqueKey + ":{" + uniqueKey + "}}) DELETE p";
		final Set<AbstractCypherQueryNode> relationshipsNodes = this.getAllRelationshipsNodesById(id);

		if (relationshipsNodes != null) {
			if (forceDeleteIfHasRelationships) {
				deleteJson = "MATCH (p:" + modelName + "{" + uniqueKey + ":{" + uniqueKey + "}})-[r]-() DELETE p,r";
			} else {
				Set<String> relationshipNodeUris = Sets.<String> newHashSet();
				for (final AbstractCypherQueryNode relationNode : relationshipsNodes) {
					relationshipNodeUris.add(relationNode.getUri());
				}
				String conflictNodesStr = Joiner.on(",").join(relationshipNodeUris);
				throw new ConflictException("Current Node has been refered by Other Nodes:" + conflictNodesStr);
			}
		}

		getNeo4jRestAPIAccessor().cypherQuery(deleteJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put(uniqueKey, id)
						.build()));
	}

	protected Set<AbstractCypherQueryNode> getAllRelationshipsNodesById(final PK id) throws Exception {
		final String getAllRelationshipsJson = "MATCH (p:" + modelName + "{" + uniqueKey + ":{" + uniqueKey + "}})-[r]-(a) RETURN DISTINCT a, r";
		final AbstractCypherQueryResult result = this.getNeo4jRestAPIAccessor().cypherQuery(getAllRelationshipsJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put(uniqueKey, id)
						.build()));
		if (result != null) {
			return result.getDistinctNodes();
		}
		return null;
	}

	protected Page<T> paginationBasic(final int pageOffset, final int pageSize, final Function<Map<String, String>, T> modelConverter) throws Exception {
		final String queryStatement = "MATCH (p:" + modelName + ") RETURN p";
		final Integer totalCount = this.getNeo4jRestAPIAccessor().getCountFromQueryStatement(queryStatement);
		final AbstractCypherQueryResult result = this.getNeo4jRestAPIAccessor().paginationThruQueryStatement(queryStatement, pageOffset, pageSize);
		Page<T> page = new Page.Builder<T>().pageOffset(pageOffset).pageSize(pageSize).totalCount(totalCount).build();

		Map<String, Map<String, String>> metaMap = result.getNodeColumnMap().get("p");
		if (metaMap != null) {
			for (final Map.Entry<String, Map<String, String>> entry : metaMap.entrySet()) {
				T model = modelConverter.apply(entry.getValue());
				model.setNodeUri(entry.getKey());
				page.addContent(model);
			}
		}
		return page;
	}

	protected abstract Neo4jRestAPIAccessor getNeo4jRestAPIAccessor();

}
