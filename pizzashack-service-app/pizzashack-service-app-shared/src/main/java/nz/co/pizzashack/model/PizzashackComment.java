package nz.co.pizzashack.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_EMPTY)
public class PizzashackComment extends AbstractNeo4jModel {
	private Pizzashack pizzashack;
	@JsonProperty
	private PizzashackCommentType commentType = PizzashackCommentType.LIKE;
	@JsonProperty
	private String message;
	@JsonProperty
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createTime;
	private User user;

	public PizzashackCommentType getCommentType() {
		return commentType;
	}

	public void setCommentType(PizzashackCommentType commentType) {
		this.commentType = commentType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Pizzashack getPizzashack() {
		return pizzashack;
	}

	public void setPizzashack(Pizzashack pizzashack) {
		this.pizzashack = pizzashack;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("commentType", commentType)
				.append("createTime", createTime)
				.append("message", message)
				.toString();
	}

	public static class Builder {
		private Pizzashack pizzashack;
		private PizzashackCommentType commentType = PizzashackCommentType.LIKE;
		private String message;
		private Date createTime;
		private User user;
		private String nodeUri;

		public Builder pizzashack(Pizzashack pizzashack) {
			this.pizzashack = pizzashack;
			return this;
		}

		public Builder commentType(PizzashackCommentType commentType) {
			this.commentType = commentType;
			return this;
		}

		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public Builder createTime(Date createTime) {
			this.createTime = createTime;
			return this;
		}

		public Builder user(User user) {
			this.user = user;
			return this;
		}

		public Builder nodeUri(String nodeUri) {
			this.nodeUri = nodeUri;
			return this;
		}

		public PizzashackComment build() {
			PizzashackComment pizzashackComment = new PizzashackComment();
			pizzashackComment.setCommentType(commentType);
			pizzashackComment.setCreateTime(createTime);
			pizzashackComment.setMessage(message);
			pizzashackComment.setNodeUri(nodeUri);
			pizzashackComment.setPizzashack(pizzashack);
			pizzashackComment.setUser(user);
			return pizzashackComment;
		}
	}

}
