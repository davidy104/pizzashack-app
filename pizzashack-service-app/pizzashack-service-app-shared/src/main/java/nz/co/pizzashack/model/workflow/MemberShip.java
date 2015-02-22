package nz.co.pizzashack.model.workflow;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MemberShip {
	private String userId;
	private String groupId;
	private String url;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static class Builder {
		private String userId;
		private String groupId;
		private String url;

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder groupId(String groupId) {
			this.groupId = groupId;
			return this;
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public MemberShip build() {
			MemberShip memberShip = new MemberShip();
			memberShip.setUrl(url);
			memberShip.setGroupId(groupId);
			memberShip.setUserId(userId);
			return memberShip;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("userId", userId)
				.append("groupId", groupId)
				.append("url", url)
				.toString();
	}
}
