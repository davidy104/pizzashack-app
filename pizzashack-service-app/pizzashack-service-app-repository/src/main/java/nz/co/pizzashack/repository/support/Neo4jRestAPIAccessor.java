package nz.co.pizzashack.repository.support;

import static com.google.common.base.Preconditions.checkArgument;
import static nz.co.pizzashack.util.GenericUtils.getValueByField;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import nz.co.pizzashack.ConflictException;
import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryResult;
import nz.co.pizzashack.repository.convert.template.Neo4jRestGenericConverter;
import nz.co.pizzashack.util.GeneralJsonRestClientAccessor;
import nz.co.pizzashack.util.RestClientCustomErrorHandler;
import nz.co.pizzashack.util.RestClientExecuteCallback;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * 
 * @author Davidy
 *
 */
public class Neo4jRestAPIAccessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(Neo4jRestAPIAccessor.class);

	@Inject
	private GeneralJsonRestClientAccessor generalJsonRestClientAccessor;

	@Inject
	@Named("jacksonObjectMapper")
	private ObjectMapper jacksonObjectMapper;

	@Inject
	private Neo4jRestGenericConverter neo4jRestGenericConverter;

	private static final String CYPHER_DISTINCT_KEYWORD = "DISTINCT";

	/**
	 * 
	 * @param nodeUri
	 * @param direction
	 * @param types
	 * @return <relationshipid,<field,value>>
	 * @throws Exception
	 */
	public Map<String, Map<String, String>> getRelationsByNodeId(final String nodeUri, final RelationshipDirection direction, final String... types) throws Exception {
		checkArgument(direction != null, "direction can not be null");
		if (direction == RelationshipDirection.NONE && ArrayUtils.isEmpty(types)) {
			throw new IllegalArgumentException("types can not be empty when relationship direction is none");
		}

		StringBuilder builder = new StringBuilder("/");
		if (direction != RelationshipDirection.NONE) {
			builder = new StringBuilder("/").append(direction.name().toLowerCase()).append("/");
		}
		if (!ArrayUtils.isEmpty(types)) {
			builder = Joiner.on("&").appendTo(builder, types);
		}

		final String responseString = generalJsonRestClientAccessor.doProcess(nodeUri, "/relationships", ClientResponse.Status.OK.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(WebResource webResource) {
				return webResource.accept(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
			}
		});
		return neo4jRestGenericConverter.relationshipsQueryResponseToMap(responseString);
	}

	public Map<String, Map<String, String>> buildRelationshipBetween2Nodes(final String fromNodeUri, final String toNodeUri, final String relationshipsLabel) throws Exception {
		return this.buildRelationshipBetween2Nodes(fromNodeUri, toNodeUri, relationshipsLabel, null);
	}

	/**
	 * 
	 * @param fromNodeUri
	 * @param toNodeUri
	 * @param relationshipsLabel
	 * @param propertyMap
	 * @return
	 */
	public Map<String, Map<String, String>> buildRelationshipBetween2Nodes(final String fromNodeUri, final String toNodeUri, final String relationshipsLabel, final Map<String, String> propertyMap) throws Exception {
		final String createRelationshipsJsonReq = neo4jRestGenericConverter.createRelationshipsConvert(toNodeUri, relationshipsLabel, propertyMap);
		final String jsonResponse = generalJsonRestClientAccessor.doProcess(fromNodeUri, "/relationships", ClientResponse.Status.CREATED.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(final WebResource webResource) {
				return webResource.accept(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, createRelationshipsJsonReq);
			}
		});
		return neo4jRestGenericConverter.relationshipsQueryResponseToMap(jsonResponse);
	}

	public void deleteRelationship(final String nodeUri, final String relationshipNodeUri, final String type, RelationshipDirection relationshipDirection) throws Exception {
		String relationshipUri = null;
		relationshipDirection = relationshipDirection == null ? RelationshipDirection.ALL : relationshipDirection;
		final Map<String, Map<String, String>> resultMap = this.getRelationsByNodeId(nodeUri, relationshipDirection, type);

		for (final Map.Entry<String, Map<String, String>> entry : resultMap.entrySet()) {
			final Map<String, String> dataMap = entry.getValue();
			if (dataMap.get("start") == nodeUri && dataMap.get("end") == relationshipNodeUri && ((dataMap.get("type")).toLowerCase()).equals(type.toLowerCase())) {
				relationshipUri = entry.getKey();
				break;
			}
		}

		if (StringUtils.isEmpty(relationshipUri)) {
			throw new NotFoundException("Relationship not found.");
		}

		this.deleteNodeByUri(relationshipUri);
	}

	public String createUniqueNode(final String objFieldsCreateStatement, final Object obj, final String label, final String key) throws Exception {
		final String createStatement = neo4jRestGenericConverter.doCreateStatement(objFieldsCreateStatement, label, "p");
		final Map<String, Object> jsonMap = this.doCreateNode(createStatement);
		return this.doCreateUniqueNode(jsonMap, obj, key);
	}

	/**
	 * 
	 * @param createStatement
	 * @param key
	 * @param value
	 * @return NodeUrl
	 */
	public String createUniqueNode(final Object obj, final String label, final String key) throws Exception {
		final Map<String, Object> jsonMap = this.createNodeByObject(obj, label, "p");
		return this.doCreateUniqueNode(jsonMap, obj, key);
	}

	private String doCreateUniqueNode(final Map<String, Object> jsonMap, final Object obj, final String key) throws Exception {
		final Object keyValue = getValueByField(obj, key);
		LOGGER.info("key:{} ", String.valueOf(keyValue));
		checkArgument(keyValue != null, "uniqueNodeKey can not be null");

		String nodeUri = null;
		Map<String, String> columnAndUriMap = neo4jRestGenericConverter.transCypherRestFormatResponseConvert(jsonMap);
		if (!columnAndUriMap.isEmpty()) {
			nodeUri = (String) columnAndUriMap.values().toArray()[0];
			final String uniqueNodeReqBody = neo4jRestGenericConverter.buildUniqueNodeRequest(nodeUri, key, String.valueOf(keyValue));
			try {
				generalJsonRestClientAccessor.process("/index/node/favorites", ClientResponse.Status.CREATED.getStatusCode(), new RestClientExecuteCallback() {
					@Override
					public ClientResponse execute(WebResource webResource) {
						return webResource.queryParam("uniqueness", "create_or_fail").accept(MediaType.APPLICATION_JSON)
								.type(MediaType.APPLICATION_JSON)
								.post(ClientResponse.class, uniqueNodeReqBody);
					}
				}, new RestClientCustomErrorHandler() {
					@SuppressWarnings("unchecked")
					@Override
					public void handle(final int statusCode, final String responseString) throws Exception {
						if (statusCode == Status.CONFLICT.getStatusCode()) {
							Map<String, Object> jsonMap = jacksonObjectMapper.readValue(responseString, Map.class);
							if (jsonMap != null) {
								final String conflictNodeUri = (String) jsonMap.get("indexed");
								throw new ConflictException(conflictNodeUri);
							}
						} else {
							throw new IllegalStateException(responseString);
						}
					}
				});
			} catch (final Exception e) {
				if (!StringUtils.isEmpty(nodeUri)) {
					this.deleteNodeByUri(nodeUri);
				}
				throw e;
			}
		}
		return nodeUri;
	}

	/**
	 * Delete node by nodeUri
	 * 
	 * @param nodeUri
	 * @throws Exception
	 */
	public void deleteNodeByUri(final String nodeUri) throws Exception {
		generalJsonRestClientAccessor.doProcess(nodeUri, null, ClientResponse.Status.NO_CONTENT.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(WebResource webResource) {
				return webResource.accept(MediaType.APPLICATION_JSON)
						.delete(ClientResponse.class);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getNodeByUri(final String nodeUri) throws Exception {
		final String jsonResponse = generalJsonRestClientAccessor.doProcess(nodeUri, null, ClientResponse.Status.OK.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(final WebResource webResource) {
				return webResource.accept(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
			}
		});
		return jacksonObjectMapper.readValue(jsonResponse, Map.class);
	}

	/**
	 * Cypher Query statement
	 * 
	 * @param cypherQueryStatement
	 * @return
	 * @throws Exception
	 */
	public AbstractCypherQueryResult cypherQuery(final String cypherQueryStatement) throws Exception {
		return this.cypherQuery(cypherQueryStatement, null);
	}

	public AbstractCypherQueryResult cypherQuery(final String cypherQueryStatement, final Map<String, Object> queryParameters) throws Exception {
		final String distinctColumn = this.getDistinctPrefixFromCypherQueryStatement(cypherQueryStatement);
		final String queryReqJson = neo4jRestGenericConverter.cypherQueryRequestConvert(cypherQueryStatement, queryParameters);
		final String responseJson = generalJsonRestClientAccessor.process("/cypher", ClientResponse.Status.OK.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(WebResource webResource) {
				return webResource.accept(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_JSON)
						.post(ClientResponse.class, queryReqJson);
			}
		});
		return neo4jRestGenericConverter.cypherQueryRespConvert(responseJson, distinctColumn);
	}

	/**
	 * Create Node via Model
	 * 
	 * @param obj
	 * @param label
	 * @param returnPrefix
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> createNodeByObject(final Object obj, final String label, final String returnPrefix) throws Exception {
		final String createStatement = neo4jRestGenericConverter.modelToCreateStatement(obj, label, returnPrefix);
		return this.doCreateNode(createStatement);
	}

	public String createNode(final String objFieldsCreateStatement, final String label) throws Exception {
		final String createStatement = neo4jRestGenericConverter.doCreateStatement(objFieldsCreateStatement, label, "p");
		final Map<String, String> columnAndUriMap = neo4jRestGenericConverter.transCypherRestFormatResponseConvert(this.doCreateNode(createStatement));
		if (!columnAndUriMap.isEmpty()) {
			return (String) columnAndUriMap.values().toArray()[0];
		}
		return null;
	}

	/**
	 * Cypher Transactional Create Statement
	 * 
	 * @param createStatement
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> doCreateNode(final String createStatement) throws Exception {
		final String responseJson = generalJsonRestClientAccessor.process("/transaction/commit", ClientResponse.Status.OK.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(WebResource webResource) {
				return webResource.accept(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, createStatement);
			}
		});
		final Map<String, Object> jsonMap = jacksonObjectMapper.readValue(responseJson, Map.class);
		this.handleErrors((List) jsonMap.get("errors"));
		return jsonMap;
	}

	private void handleErrors(final List<Map<String, String>> errorList) {
		if (errorList != null && !errorList.isEmpty()) {
			Map<String, String> errorMap = errorList.get(0);
			if (errorMap != null) {
				final String errorCode = errorMap.get("code");
				final String errorMessage = errorMap.get("message");
				throw new IllegalStateException("errorCode[" + errorCode + "]-errorMessage[" + errorMessage + "]");
			}
		}
	}

	private String getDistinctPrefixFromCypherQueryStatement(final String cypherQueryStatement) {
		int distinctKeyLen = CYPHER_DISTINCT_KEYWORD.length();
		int disPos = cypherQueryStatement.toUpperCase().indexOf(CYPHER_DISTINCT_KEYWORD);
		if (disPos != -1) {
			if (cypherQueryStatement.indexOf(",") != -1 && cypherQueryStatement.indexOf(",") > disPos) {
				return cypherQueryStatement.substring(disPos + distinctKeyLen, cypherQueryStatement.indexOf(",")).trim();
			} else {
				return cypherQueryStatement.substring(disPos + distinctKeyLen, cypherQueryStatement.length()).trim();
			}
		}
		return null;
	}

	public Integer getCountFromQueryStatement(final String cypherQueryStatement) throws Exception {
		return this.getCountFromQueryStatement(cypherQueryStatement, null);
	}

	/**
	 * 
	 * @param cypherQueryStatement
	 * @param queryParameters
	 * @return
	 * @throws Exception
	 */
	public Integer getCountFromQueryStatement(final String cypherQueryStatement, final Map<String, Object> queryParameters) throws Exception {
		Integer count = 0;
		String countQueryStatement = null;
		int returnIndex = cypherQueryStatement.indexOf("RETURN");
		if (returnIndex != -1) {
			countQueryStatement = cypherQueryStatement.substring(0, returnIndex) + " RETURN COUNT(*) as total";
		} else {
			countQueryStatement = cypherQueryStatement + " RETURN COUNT(*) as total";
		}

		final AbstractCypherQueryResult result = this.cypherQuery(countQueryStatement, queryParameters);
		Set<String> countResultSet = result.getDataColumnMap().get("total");
		if (countResultSet != null && !countResultSet.isEmpty()) {
			count = Integer.valueOf((String) countResultSet.toArray()[0]);
		}
		return count;
	}

	public AbstractCypherQueryResult paginationThruQueryStatement(final String cypherQueryStatement, final int begin, final int pageSize) throws Exception {
		return this.paginationThruQueryStatement(cypherQueryStatement, begin, pageSize, null);
	}

	/**
	 * 
	 * @param cypherQueryStatement
	 * @param pageOffset
	 * @param pageSize
	 * @param queryParameters
	 * @return
	 * @throws Exception
	 */
	public AbstractCypherQueryResult paginationThruQueryStatement(final String cypherQueryStatement, final int begin, final int pageSize, final Map<String, Object> queryParameters) throws Exception {
		final String paginationQueryStatement = cypherQueryStatement + " SKIP " + begin + " LIMIT " + pageSize;
		return this.cypherQuery(paginationQueryStatement, queryParameters);
	}

}
