package nz.co.pizzashack.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractNeo4jModel {
	@JsonIgnore
	public String nodeUri;

	public String getNodeUri() {
		return nodeUri;
	}

	public void setNodeUri(String nodeUri) {
		this.nodeUri = nodeUri;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
