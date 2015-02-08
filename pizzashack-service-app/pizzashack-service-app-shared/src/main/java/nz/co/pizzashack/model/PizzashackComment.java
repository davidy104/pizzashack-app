package nz.co.pizzashack.model;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class PizzashackComment extends AbstractNeo4jModel{
	private Pizzashack pizzashack;
	private PizzashackCommentType commentType = PizzashackCommentType.LIKE;
	private String message;
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
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder
				.append(this.commentType, ((PizzashackComment) obj).commentType)
				.append(this.createTime, ((PizzashackComment) obj).createTime)
				.append(this.user, ((PizzashackComment) obj).user)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder
				.append(this.commentType)
				.append(this.createTime)
				.append(this.user)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("commentType", commentType)
				.append("createTime", createTime)
				.append("user", user)
				.append("message", message)
				.toString();
	}
	
}
