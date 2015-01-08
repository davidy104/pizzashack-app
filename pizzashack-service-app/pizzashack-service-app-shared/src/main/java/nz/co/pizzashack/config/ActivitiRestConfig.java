package nz.co.pizzashack.config;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ActivitiRestConfig {
	private String authUserId;
	private String authPassword;
	private String restHostUri;

	public String getAuthUserId() {
		return authUserId;
	}

	public void setAuthUserId(String authUserId) {
		this.authUserId = authUserId;
	}

	public String getAuthPassword() {
		return authPassword;
	}

	public void setAuthPassword(String authPassword) {
		this.authPassword = authPassword;
	}

	public String getRestHostUri() {
		return restHostUri;
	}

	public void setRestHostUri(String restHostUri) {
		this.restHostUri = restHostUri;
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.authUserId, ((ActivitiRestConfig) obj).authUserId)
				.append(this.authPassword, ((ActivitiRestConfig) obj).authPassword)
				.append(this.restHostUri, ((ActivitiRestConfig) obj).restHostUri)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.authUserId)
				.append(this.authPassword)
				.append(this.restHostUri)
				.toHashCode();

	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("authUserId", authUserId).append("authPassword", authPassword).append("restHostUri", restHostUri).toString();
	}

	public static class Builder {
		private String authUserId;
		private String authPassword;
		private String restHostUri;

		public Builder(String authUserId, String authPassword, String restHostUri) {
			this.authUserId = authUserId;
			this.authPassword = authPassword;
			this.restHostUri = restHostUri;
		}

		public Builder() {
		}

		public Builder user(String authUserId) {
			this.authUserId = authUserId;
			return this;
		}

		public Builder password(String authPassword) {
			this.authPassword = authPassword;
			return this;
		}

		public Builder hostUri(String restHostUri) {
			this.restHostUri = restHostUri;
			return this;
		}

		public ActivitiRestConfig build() {
			ActivitiRestConfig result = new ActivitiRestConfig();
			result.setAuthPassword(authPassword);
			result.setAuthUserId(authUserId);
			result.setRestHostUri(restHostUri);
			return result;
		}
	}
}
