package nz.co.pizzashack.model.workflow;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProcessDefinition {
	private String id;
	private String url;
	private String key;
	private int version;
	private String name;
	private String description;

	private String deploymentId;
	private String deploymentUrl;
	private String resource;

	private String diagramResource;
	private String category;
	private boolean graphicalNotationDefined;
	private boolean suspended;
	private boolean startFormDefined;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getDeploymentUrl() {
		return deploymentUrl;
	}

	public void setDeploymentUrl(String deploymentUrl) {
		this.deploymentUrl = deploymentUrl;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getDiagramResource() {
		return diagramResource;
	}

	public void setDiagramResource(String diagramResource) {
		this.diagramResource = diagramResource;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isGraphicalNotationDefined() {
		return graphicalNotationDefined;
	}

	public void setGraphicalNotationDefined(boolean graphicalNotationDefined) {
		this.graphicalNotationDefined = graphicalNotationDefined;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public boolean isStartFormDefined() {
		return startFormDefined;
	}

	public void setStartFormDefined(boolean startFormDefined) {
		this.startFormDefined = startFormDefined;
	}

	public static class Builder {
		private String id;
		private String url;
		private String name;
		private String category;

		private String key;
		private int version;
		private String description;
		private String deploymentId;
		private String deploymentUrl;
		private String resource;
		private String diagramResource;
		private boolean graphicalNotationDefined;
		private boolean suspended;
		private boolean startFormDefined;

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder resource(String resource) {
			this.resource = resource;
			return this;
		}

		public Builder suspended(boolean suspended) {
			this.suspended = suspended;
			return this;
		}

		public Builder startFormDefined(boolean startFormDefined) {
			this.startFormDefined = startFormDefined;
			return this;
		}

		public Builder graphicalNotationDefined(boolean graphicalNotationDefined) {
			this.graphicalNotationDefined = graphicalNotationDefined;
			return this;
		}

		public Builder diagramResource(String diagramResource) {
			this.diagramResource = diagramResource;
			return this;
		}

		public Builder deploymentUrl(String deploymentUrl) {
			this.deploymentUrl = deploymentUrl;
			return this;
		}

		public Builder deploymentId(String deploymentId) {
			this.deploymentId = deploymentId;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder key(String key) {
			this.key = key;
			return this;
		}

		public Builder version(int version) {
			this.version = version;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder category(String category) {
			this.category = category;
			return this;
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public ProcessDefinition build() {
			ProcessDefinition processDefinition = new ProcessDefinition();
			processDefinition.setCategory(category);
			processDefinition.setId(id);
			processDefinition.setName(name);
			processDefinition.setUrl(url);
			processDefinition.setVersion(version);
			processDefinition.setDescription(description);
			processDefinition.setDeploymentId(deploymentId);
			processDefinition.setDeploymentUrl(deploymentUrl);
			processDefinition.setResource(resource);
			processDefinition.setDiagramResource(diagramResource);
			processDefinition.setGraphicalNotationDefined(graphicalNotationDefined);
			processDefinition.setSuspended(suspended);
			processDefinition.setStartFormDefined(startFormDefined);
			return processDefinition;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder
				.append(this.id, ((ProcessDefinition) obj).id)
				.append(this.url, ((ProcessDefinition) obj).url)
				.append(this.key, ((ProcessDefinition) obj).key)
				.append(this.name, ((ProcessDefinition) obj).name)
				.append(this.version, ((ProcessDefinition) obj).version)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder
				.append(this.id)
				.append(this.name)
				.append(this.key)
				.append(this.url)
				.append(this.version)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", id)
				.append("name", name)
				.append("category", category)
				.append("url", url)
				.append("key", key)
				.append("version", version)
				.append("deploymentId", deploymentId)
				.append("deploymentUrl", deploymentUrl)
				.append("resource", resource)
				.append("diagramResource", diagramResource)
				.append("graphicalNotationDefined", graphicalNotationDefined)
				.append("suspended", suspended)
				.append("startFormDefined", startFormDefined)
				.toString();
	}

}
