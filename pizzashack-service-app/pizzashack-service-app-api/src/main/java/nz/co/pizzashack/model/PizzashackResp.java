package nz.co.pizzashack.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_EMPTY)
public class PizzashackResp {

	@JsonProperty
	private String uri;

	@JsonProperty
	private Long dislike;

	@JsonProperty
	private Long like;

	@JsonProperty("data")
	private Pizzashack pizzashack;

	public Long getDislike() {
		return dislike;
	}

	public void setDislike(Long dislike) {
		this.dislike = dislike;
	}

	public Long getLike() {
		return like;
	}

	public void setLike(Long like) {
		this.like = like;
	}

	public Pizzashack getPizzashack() {
		return pizzashack;
	}

	public void setPizzashack(Pizzashack pizzashack) {
		this.pizzashack = pizzashack;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.uri, ((PizzashackResp) obj).uri)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.uri)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("uri", uri)
				.append("dislike", dislike)
				.append("like", like)
				.append("pizzashack", pizzashack)
				.toString();
	}

	public static class Builder {
		private String uri;
		private Long dislike;
		private Long like;
		private Pizzashack pizzashack;

		public Builder uri(String uri) {
			this.uri = uri;
			return this;
		}

		public Builder dislike(Long dislike) {
			this.dislike = dislike;
			return this;
		}

		public Builder like(Long like) {
			this.like = like;
			return this;
		}

		public Builder pizzashack(Pizzashack pizzashack) {
			this.pizzashack = pizzashack;
			return this;
		}

		public PizzashackResp build() {
			PizzashackResp pizzashackResp = new PizzashackResp();
			pizzashackResp.setDislike(dislike);
			pizzashackResp.setLike(like);
			pizzashackResp.setUri(uri);
			pizzashackResp.setPizzashack(pizzashack);
			return pizzashackResp;
		}
	}
}
