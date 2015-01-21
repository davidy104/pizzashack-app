package nz.co.pizzashack.repository.support;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;

import nz.co.pizzashack.util.GeneralJsonRestClientAccessor;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class Neo4jRestAPIAccessor {

	@Inject
	private GeneralJsonRestClientAccessor generalJsonRestClientAccessor;

	@Inject
	@Named("NEO4J.HOST_URI")
	private String neo4jHostUri;

	@Inject
	@Named("jacksonObjectMapper")
	private ObjectMapper jacksonObjectMapper;

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
}
