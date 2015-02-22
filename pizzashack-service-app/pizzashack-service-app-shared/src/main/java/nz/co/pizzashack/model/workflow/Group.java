package nz.co.pizzashack.model.workflow;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Group {
	private String id;
	private String url;
	private String name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		private String name;
		private String type;

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
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

		public Group build() {
			Group group = new Group();
			group.setId(id);
			group.setName(name);
			group.setUrl(url);
			group.setType(type);
			return group;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder
				.append(this.id, ((Group) obj).id)
				.append(this.name, ((Group) obj).name)
				.append(this.type, ((Group) obj).type)
				.append(this.url, ((Group) obj).url)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder
				.append(this.id)
				.append(this.name)
				.append(this.type)
				.append(this.url)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", id)
				.append("name", name)
				.append("type", type)
				.append("url", url)
				.toString();
	}
}
