package nz.co.pizzashack.repository.support;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import nz.co.pizzashack.ConflictException;
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

	@SuppressWarnings("unchecked")
	public Map<String, Object> getRelationsByNodeId(final String nodeUri, final RelationshipDirection direction, final String... types) throws Exception {
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

		final String responseString = generalJsonRestClientAccessor.get("/relationships");
		return jacksonObjectMapper.readValue(responseString, Map.class);
	}

	/**
	 * 
	 * @param createStatement
	 * @param key
	 * @param value
	 * @return NodeUrl
	 */
	public String createUniqueNode(final Object obj, final String label, final String key) throws Exception {
		Field field = obj.getClass().getDeclaredField(key);
		field.setAccessible(true);
		final Object keyValue = field.get(obj);
		LOGGER.info("key:{} ", String.valueOf(keyValue));
		checkArgument(keyValue != null, "uniqueNodeKey can not be null");
		final Map<String, Object> jsonMap = this.createNodeByObject(obj, label, "p");
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
					this.deleteNode(nodeUri);
				}
				throw e;
			}
		}
		return nodeUri;
	}

	public void deleteNode(final String nodeUri) throws Exception {
		generalJsonRestClientAccessor.delete(nodeUri);
	}

	public AbstractCypherQueryResult cypherQuery(final String cypherQueryStatement, final Map<String, String> queryParameters) throws Exception {
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

	public Map<String, Object> createNodeByObject(final Object obj, final String label, final String returnPrefix) throws Exception {
		final String createStatement = neo4jRestGenericConverter.modelToCreateStatement(obj, label, returnPrefix);
		return this.createNode(createStatement);
	}

	/**
	 * Cypher Transactional Create Statement
	 * 
	 * @param createStatement
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> createNode(final String createStatement) throws Exception {
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

}
