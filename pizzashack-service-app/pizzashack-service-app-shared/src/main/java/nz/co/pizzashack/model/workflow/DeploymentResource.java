package nz.co.pizzashack.model.workflow;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class DeploymentResource{
	private String id;
	private String url;
	private String dataUrl;
	private String mediaType;
	private String type;
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
	public String getDataUrl() {
		return dataUrl;
	}
	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public static class Builder {
		private String id;
		private String url;
		private String dataUrl;
		private String mediaType;
		private String type;

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder dataUrl(String dataUrl) {
			this.dataUrl = dataUrl;
			return this;
		}

		public Builder mediaType(String mediaType) {
			this.mediaType = mediaType;
			return this;
		}
		
		public Builder type(String type) {
			this.type = type;
			return this;
		}
		
		public Builder url(String url) {
			this.url = url;
			return this;
		}
		
		public DeploymentResource build() {
			DeploymentResource deploymentResource = new DeploymentResource();
			deploymentResource.setId(id);
			deploymentResource.setUrl(url);
			deploymentResource.setDataUrl(dataUrl);
			deploymentResource.setMediaType(mediaType);
			deploymentResource.setType(type);
			return deploymentResource;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder
				.append(this.id, ((DeploymentResource) obj).id)
				.append(this.url, ((DeploymentResource) obj).url)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder
				.append(this.id)
				.append(this.url)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", id)
				.append("dataUrl", dataUrl)
				.append("mediaType", mediaType)
				.append("url", url)
				.append("type", type)
				.toString();
	}
}
