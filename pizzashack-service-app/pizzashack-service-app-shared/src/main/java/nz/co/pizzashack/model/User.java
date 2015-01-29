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
public class User extends AbstractNeo4jModel {
	private String userName;
	private String password;

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

	public static class Builder {
		private String userName;
		private String password;
		private Date createTime;
		private String nodeUri;

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

		public User build() {
			User user = new User();
			user.setCreateTime(createTime);
			user.setPassword(password);
			user.setNodeUri(nodeUri);
			user.setUserName(userName);
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
