package nz.co.pizzashack.repository.support;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static nz.co.pizzashack.util.GenericUtils.getValueByField;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.ConflictException;
import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.AbstractNeo4jModel;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryNode;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 
 * @author Davidy
 *
 * @param <T>
 * @param <PK>
 */
public abstract class RepositoryBase<T extends AbstractNeo4jModel, PK extends Serializable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryBase.class);

	protected final String label;

	protected String uniqueKey;

	public RepositoryBase(final String label) {
		super();
		this.label = label;
	}

	public RepositoryBase(final String label, final String uniqueKey) {
		super();
		this.label = label;
		this.uniqueKey = uniqueKey;
	}

	public String create(final T addModel, final Function<T, String> objectFieldsCreateStatementConverter) throws Exception {
		checkState(getNeo4jRestAPIAccessor() != null, "Neo4jRestAPIAccessor can not be null");
		checkArgument(addModel != null, "addModel can not be null");
		checkState(objectFieldsCreateStatementConverter != null, "objectFieldsCreateStatementConverter can not be null");
		return getNeo4jRestAPIAccessor().createNode(objectFieldsCreateStatementConverter.apply(addModel), label);
	}

	public String createUnique(final T addModel) throws Exception {
		checkState(getNeo4jRestAPIAccessor() != null, "Neo4jRestAPIAccessor can not be null");
		if (StringUtils.isEmpty(uniqueKey)) {
			return null;
		}
		checkArgument(addModel != null && getValueByField(addModel, uniqueKey) != null, "addModel and its unique value can not be null");
		return getNeo4jRestAPIAccessor().createUniqueNode(addModel, label, uniqueKey);
	}

	public String createUnique(final T addModel, final Function<T, String> objectFieldsCreateStatementConverter) throws Exception {
		checkState(getNeo4jRestAPIAccessor() != null, "Neo4jRestAPIAccessor can not be null");
		checkArgument(addModel != null && getValueByField(addModel, uniqueKey) != null, "addModel and its unique value can not be null");
		if (StringUtils.isEmpty(uniqueKey)) {
			return null;
		}
		if (objectFieldsCreateStatementConverter != null) {
			final String createStatement = objectFieldsCreateStatementConverter.apply(addModel);
			LOGGER.info("createStatement:{} ", createStatement);
			return getNeo4jRestAPIAccessor().createUniqueNode(createStatement, addModel, label, uniqueKey);
		}
		return getNeo4jRestAPIAccessor().createUniqueNode(addModel, label, uniqueKey);
	}

	@SuppressWarnings("unchecked")
	protected T getBasicById(PK id, Function<Map<String, String>, T> converter) throws NotFoundException {
		checkArgument(converter != null, "converter can not be null");
		checkState(getNeo4jRestAPIAccessor() != null, "Neo4jRestAPIAccessor can not be null");
		if (StringUtils.isEmpty(uniqueKey)) {
			return null;
		}
		T returnModel = null;
		final AbstractCypherQueryResult result = this.doGetBasicById(id);

		if (result == null) {
			throw new NotFoundException("Entity not found by id[" + id + "].");
		}

		Map<String, Map<String, String>> metaMap = result.getNodeColumnMap().get("p");
		if (metaMap != null && !metaMap.isEmpty()) {
			final String nodeUri = (String) metaMap.keySet().toArray()[0];
			final Map<String, String> fieldValueMap = (Map<String, String>) metaMap.values().toArray()[0];
			returnModel = converter.apply(fieldValueMap);
			returnModel.setNodeUri(nodeUri);
		}
		return returnModel;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	protected AbstractCypherQueryResult doGetBasicById(final PK id) {
		final String queryJson = "MATCH (p:" + label + "{" + uniqueKey + ":{" + uniqueKey + "}}) RETURN p";
		try {
			return this.getNeo4jRestAPIAccessor().cypherQuery(queryJson,
					Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
							.put(uniqueKey, id)
							.build()));
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Set<T> getBasicAll(final Function<Map<String, String>, T> metaMapToModelConverter) throws Exception {
		checkState(getNeo4jRestAPIAccessor() != null, "Neo4jRestAPIAccessor can not be null");
		checkArgument(metaMapToModelConverter != null, "metaMapToModelConverter can not be null");
		Set<T> resultSet = Sets.<T> newHashSet();
		final String queryJson = "MATCH (p:" + label + ") RETURN p";
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

	protected void updateBasicById(final T updatedModel) throws Exception {
		checkState(getNeo4jRestAPIAccessor() != null, "Neo4jRestAPIAccessor can not be null");
		checkArgument(updatedModel != null && getValueByField(updatedModel, uniqueKey) != null, "updatedModel and its unique value can not be null");
		if (!StringUtils.isEmpty(uniqueKey)) {
			this.doUpdateBasicById(getValueByField(updatedModel, uniqueKey), updatedModel);
		}
	}

	protected void updateBasicById(final T updatedModel, final Function<T, Map<String, String>> toUpdateStatementConverter) throws Exception {
		checkState(getNeo4jRestAPIAccessor() != null, "Neo4jRestAPIAccessor can not be null");
		checkArgument(updatedModel != null && getValueByField(updatedModel, uniqueKey) != null, "updatedModel and its unique value can not be null");
		checkArgument(toUpdateStatementConverter != null, "toUpdateStatementConverter can not be null");
		if (!StringUtils.isEmpty(uniqueKey)) {
			this.doUpdateBasicById(getValueByField(updatedModel, uniqueKey), toUpdateStatementConverter.apply(updatedModel));
		}
	}

	@SuppressWarnings("unchecked")
	private void doUpdateBasicById(final Object uniqueKeyValue, final Object props) throws Exception {
		if (this.doGetBasicById((PK) uniqueKeyValue) == null) {
			throw new NotFoundException("Entity not found by id[" + uniqueKeyValue + "].");
		}
		final String updateJson = "MATCH (p:" + label + "{" + uniqueKey + ":{" + uniqueKey + "}}) SET p = { props }";
		this.getNeo4jRestAPIAccessor().cypherQuery(updateJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put(uniqueKey, uniqueKeyValue)
						.put("props", props)
						.build()));
	}

	public void deleteAllById(final PK id, final boolean forceDeleteIfHasRelationships) throws Exception {
		if (!StringUtils.isEmpty(uniqueKey)) {
			if (this.doGetBasicById(id) == null) {
				throw new NotFoundException("Entity not found by id[" + id + "].");
			}
			String deleteJson = "MATCH (p:" + label + "{" + uniqueKey + ":{" + uniqueKey + "}}) DELETE p";
			final Set<AbstractCypherQueryNode> relationshipsNodes = this.getAllRelationshipsNodesById(id);

			if (relationshipsNodes != null) {
				if (forceDeleteIfHasRelationships) {
					deleteJson = "MATCH (p:" + label + "{" + uniqueKey + ":{" + uniqueKey + "}})-[r]-() DELETE p,r";
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
	}

	protected Set<AbstractCypherQueryNode> getAllRelationshipsNodesById(final PK id) throws Exception {
		if (StringUtils.isEmpty(uniqueKey)) {
			return null;
		}
		final String getAllRelationshipsJson = "MATCH (p:" + label + "{" + uniqueKey + ":{" + uniqueKey + "}})-[r]-(a) RETURN DISTINCT a, r";
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
		final String queryStatement = "MATCH (p:" + label + ") RETURN p";
		return this.doPagination(queryStatement, pageOffset, pageSize, modelConverter);
	}

	protected Page<T> doPagination(final String queryStatement, final int pageOffset, final int pageSize, final Function<Map<String, String>, T> modelConverter) throws Exception {
		final Integer totalCount = this.getNeo4jRestAPIAccessor().getCountFromQueryStatement(queryStatement);
		final AbstractCypherQueryResult result = this.getNeo4jRestAPIAccessor().paginationThruQueryStatement(queryStatement, pageOffset, pageSize);
		Page<T> page = new Page.Builder<T>().pageSize(pageSize).totalCount(totalCount).build();
		
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
