package nz.co.pizzashack.repository.support;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import nz.co.pizzashack.ConflictException;
import nz.co.pizzashack.repository.convert.template.Neo4jRestGenericConverter;
import nz.co.pizzashack.util.GeneralJsonRestClientAccessor;
import nz.co.pizzashack.util.RestClientCustomErrorHandler;
import nz.co.pizzashack.util.RestClientExecuteCallback;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Neo4jRestAPIAccessor {

	@Inject
	private GeneralJsonRestClientAccessor generalJsonRestClientAccessor;

	@Inject
	@Named("NEO4J.HOST_URI")
	private String neo4jHostUri;

	@Inject
	@Named("jacksonObjectMapper")
	private ObjectMapper jacksonObjectMapper;

	@Inject
	private Neo4jRestGenericConverter neo4jRestGenericConverter;

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

		final String responseString = generalJsonRestClientAccessor.get(neo4jHostUri + "/relationships");
		return jacksonObjectMapper.readValue(responseString, Map.class);
	}

	/**
	 * 
	 * @param createStatement
	 * @param key
	 * @param value
	 * @return NodeUrl
	 */
	public String createUniqueNode(final String createStatement, final String key, final String value) throws Exception {
		final Map<String, Object> jsonMap = this.createNode(createStatement);
		String nodeUri = null;
		Map<String, String> columnAndUriMap = neo4jRestGenericConverter.transCypherRestFormatResponseConvert(jsonMap);
		if (!columnAndUriMap.isEmpty()) {
			nodeUri = (String) columnAndUriMap.values().toArray()[0];
			final String uniqueNodeReqBody = neo4jRestGenericConverter.buildUniqueNodeRequest(nodeUri, key, value);
			try {
				generalJsonRestClientAccessor.process(neo4jHostUri + "/index/node/favorites", ClientResponse.Status.CREATED.getStatusCode(), new RestClientExecuteCallback() {
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

	public Map<String, Object> cypherQuery(final String cypherQueryStatement, final Map<String, String> queryParameters) throws Exception {
		generalJsonRestClientAccessor.process(neo4jHostUri + "/cypher", ClientResponse.Status.OK.getStatusCode(), restClientCallback, customErrorHandlers);
		
		
		generalJsonRestClientAccessor.create(path, jsonBody)
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
	public Map<String, Object> createNode(final String createStatement) throws Exception {
		final String responseJson = generalJsonRestClientAccessor.create(neo4jHostUri + "/transaction/commit", createStatement);
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

}
