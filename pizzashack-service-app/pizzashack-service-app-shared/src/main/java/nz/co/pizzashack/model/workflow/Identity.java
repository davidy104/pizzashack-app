package nz.co.pizzashack.model.workflow;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class Identity {
	public String url;
	public String user;
	public String group;
	IdentityType type = IdentityType.candidate;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public IdentityType getType() {
		return type;
	}
	public void setType(IdentityType type) {
		this.type = type;
	}
	
	public static class Builder {
		public String url;
		public String user;
		public String group;
		IdentityType type = IdentityType.candidate;

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public Builder type(IdentityType type) {
			this.type = type;
			return this;
		}

		public Builder user(String user) {
			this.user = user;
			return this;
		}
		
		public Builder group(String group) {
			this.group = group;
			return this;
		}
		
		public Identity build() {
			Identity identity = new Identity();
			identity.setGroup(group);
			identity.setType(type);
			identity.setUrl(url);
			identity.setUser(user);
			return identity;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder
				.append(this.url, ((Identity) obj).url)
				.append(this.user, ((Identity) obj).user)
				.append(this.type, ((Identity) obj).type)
				.append(this.group, ((Identity) obj).group)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder
				.append(this.url)
				.append(this.user)
				.append(this.type)
				.append(this.group)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("url", url)
				.append("user", user)
				.append("type", type)
				.append("group", group)
				.toString();
	}
	
}
