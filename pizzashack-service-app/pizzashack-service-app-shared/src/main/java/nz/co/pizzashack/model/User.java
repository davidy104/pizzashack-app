package nz.co.pizzashack.model;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Sets;

@JsonInclude(Include.NON_EMPTY)
public class User extends AbstractNeo4jModel {
	private String userName;
	private String password;
	private Set<Role> roles = Collections.<Role> emptySet();

	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createTime;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void addRole(final Role role) {
		if (roles.isEmpty()) {
			roles = Sets.<Role> newHashSet();
		}
		roles.add(role);
	}

	public static class Builder {
		private String userName;
		private String password;
		private Date createTime;
		private String nodeUri;
		private Set<Role> roles = Collections.<Role> emptySet();

		public Builder userName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
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

		public Builder roles(Set<Role> roles) {
			this.roles = roles;
			return this;
		}

		public User build() {
			User user = new User();
			user.setCreateTime(createTime);
			user.setPassword(password);
			user.setNodeUri(nodeUri);
			user.setUserName(userName);
			user.setRoles(roles);
			return user;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.userName, ((User) obj).userName)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.userName)
				.toHashCode();

	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("userName", userName)
				.append("createTime", createTime)
				.append("password", password)
				.toString();
	}
}
