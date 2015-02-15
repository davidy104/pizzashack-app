package nz.co.pizzashack.model;

import java.math.BigDecimal;
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
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_EMPTY)
public class Pizzashack extends AbstractNeo4jModel {
	@JsonProperty
	private String pizzashackId;
	@JsonProperty
	private String pizzaName;
	@JsonProperty
	private String description;
	@JsonProperty
	private BigDecimal price;
	@JsonProperty
	private String icon;
	@JsonProperty
	private Integer amount;
	@JsonProperty
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createTime;
	@JsonProperty
	private Long viewed = new Long(0);
	
	@JsonProperty
	private Set<PizzashackComment> comments = Collections.<PizzashackComment>emptySet();

	public Pizzashack() {
		super();
	}

	public Pizzashack(String pizzashackId, String pizzaName, String description, BigDecimal price, String icon, String nodeUri,Integer amount,Date createTime,Set<PizzashackComment> comments) {
		this.pizzashackId = pizzashackId;
		this.pizzaName = pizzaName;
		this.description = description;
		this.price = price;
		this.icon = icon;
		this.nodeUri = nodeUri;
		this.amount = amount;
		this.createTime = createTime;
	}

	public String getPizzashackId() {
		return pizzashackId;
	}

	public void setPizzashackId(String pizzashackId) {
		this.pizzashackId = pizzashackId;
	}

	public String getPizzaName() {
		return pizzaName;
	}

	public void setPizzaName(String pizzaName) {
		this.pizzaName = pizzaName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getNodeUri() {
		return nodeUri;
	}

	public void setNodeUri(String nodeUri) {
		this.nodeUri = nodeUri;
	}
	
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Set<PizzashackComment> getComments() {
		return comments;
	}

	public void setComments(Set<PizzashackComment> comments) {
		this.comments = comments;
	}
	
	public Long getViewed() {
		return viewed;
	}

	public void setViewed(Long viewed) {
		this.viewed = viewed;
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.pizzashackId, ((Pizzashack) obj).pizzashackId)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.pizzashackId)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("pizzashackId", pizzashackId)
				.append("pizzaName", pizzaName)
				.append("description", description)
				.append("price", price)
				.append("icon", icon)
				.append("nodeUri", nodeUri)
				.append("amount", amount)
				.append("createTime", createTime)
				.append("viewed", viewed)
				.toString();
	}

	public static class Builder {
		private String pizzashackId;
		private String pizzaName;
		private String description;
		private BigDecimal price;
		private String icon;
		private String nodeUri;
		private Integer amount;
		private Date createTime;
		private Long viewed = new Long(0);
		private Set<PizzashackComment> comments = Collections.<PizzashackComment>emptySet();

		public Builder(String pizzashackId, String pizzaName, String description, BigDecimal price, String icon, String nodeUri,Integer amount,Date creatTime) {
			this.pizzashackId = pizzashackId;
			this.pizzaName = pizzaName;
			this.description = description;
			this.price = price;
			this.icon = icon;
			this.nodeUri = nodeUri;
		}

		public Builder() {
		}

		public Builder pizzashackId(String pizzashackId) {
			this.pizzashackId = pizzashackId;
			return this;
		}

		public Builder pizzaName(String pizzaName) {
			this.pizzaName = pizzaName;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder price(BigDecimal price) {
			this.price = price;
			return this;
		}

		public Builder icon(String icon) {
			this.icon = icon;
			return this;
		}

		public Builder nodeUri(String nodeUri) {
			this.nodeUri = nodeUri;
			return this;
		}
		
		public Builder amount(Integer amount) {
			this.amount = amount;
			return this;
		}
		
		public Builder createTime(Date createTime) {
			this.createTime = createTime;
			return this;
		}
		public Builder comments(Set<PizzashackComment> comments) {
			this.comments = comments;
			return this;
		}
		public Builder viewed(Long viewed) {
			this.viewed = viewed;
			return this;
		}
		
		public Pizzashack build() {
			Pizzashack pizzashack = new Pizzashack(pizzashackId, pizzaName, description, price, icon, nodeUri,amount,createTime,comments);
			pizzashack.setViewed(viewed);
			return pizzashack;
		}
	}
}
