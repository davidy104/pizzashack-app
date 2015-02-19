package nz.co.pizzashack.model;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class Role extends AbstractNeo4jModel {

	private String roleName;

	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createTime;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static class Builder {
		private String roleName;
		private Date createTime;
		private String nodeUri;

		public Builder roleName(String roleName) {
			this.roleName = roleName;
			return this;
		}

		public Builder createTime(Date createTime) {
			this.createTime = createTime;
			return this;
		}

		public Builder nodeUri(String nodeUri) {
			this.nodeUri = nodeUri;
			return this;
		}

		public Role build() {
			Role role = new Role();
			role.setCreateTime(createTime);
			role.setNodeUri(nodeUri);
			role.setRoleName(roleName);
			return role;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.roleName, ((Role) obj).roleName)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.roleName)
				.toHashCode();

	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("roleName", roleName)
				.append("createTime", createTime)
				.toString();
	}
}
