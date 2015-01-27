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
public class OrderReview extends AbstractNeo4jModel {
	private String orderNo;
	private String content;
	private ReviewStatus reviewStatus = ReviewStatus.PENDING;

	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createTime;
	private User reviewer;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ReviewStatus getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(ReviewStatus reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public User getReviewer() {
		return reviewer;
	}

	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}

	public static class Builder {
		private String orderNo;
		private String content;
		private ReviewStatus reviewStatus = ReviewStatus.PENDING;
		private Date createTime;
		private User reviewer;
		private String nodeUri;

		public Builder orderNo(String orderNo) {
			this.orderNo = orderNo;
			return this;
		}

		public Builder content(String content) {
			this.content = content;
			return this;
		}

		public Builder reviewStatus(ReviewStatus reviewStatus) {
			this.reviewStatus = reviewStatus;
			return this;
		}

		public Builder createTime(Date createTime) {
			this.createTime = createTime;
			return this;
		}

		public Builder reviewer(User reviewer) {
			this.reviewer = reviewer;
			return this;
		}

		public Builder nodeUri(String nodeUri) {
			this.nodeUri = nodeUri;
			return this;
		}

		public OrderReview build() {
			OrderReview orderReview = new OrderReview();
			orderReview.setContent(content);
			orderReview.setCreateTime(createTime);
			orderReview.setNodeUri(nodeUri);
			orderReview.setOrderNo(orderNo);
			orderReview.setReviewer(reviewer);
			orderReview.setReviewStatus(reviewStatus);
			return orderReview;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.orderNo, ((OrderReview) obj).orderNo)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.orderNo)
				.toHashCode();

	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("orderNo", orderNo)
				.append("content", content)
				.append("reviewStatus", reviewStatus)
				.append("createTime", createTime)
				.toString();
	}
}
