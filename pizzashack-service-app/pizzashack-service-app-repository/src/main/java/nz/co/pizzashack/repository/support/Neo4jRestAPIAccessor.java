package nz.co.pizzashack.repository.support;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;

import nz.co.pizzashack.util.GeneralJsonRestClientAccessor;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class Neo4jRestAPIAccessor {

	@Inject
	private GeneralJsonRestClientAccessor generalJsonRestClientAccessor;

	@Inject
	@Named("NEO4J.HOST_URI")
	private String neo4jHostUri;

	public Map<String, Map<String, String>> getRelationsByNodeId(final String nodeUri, final RelationshipDirection direction, final String... types) {
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

		return null;
	}
}
