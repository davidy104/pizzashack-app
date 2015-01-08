package nz.co.pizzashack.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Pizzashack {
	private String pizzashackId;
	private String pizzaName;
	private String description;
	private BigDecimal price;
	private String icon;

	private Pizzashack(String pizzashackId, String pizzaName, String description, BigDecimal price, String icon) {
		this.pizzashackId = pizzashackId;
		this.pizzaName = pizzaName;
		this.description = description;
		this.price = price;
		this.icon = icon;
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
				.toString();
	}

	public static class Builder {
		private String pizzashackId;
		private String pizzaName;
		private String description;
		private BigDecimal price;
		private String icon;

		public Builder(String pizzashackId, String pizzaName, String description, BigDecimal price, String icon) {
			this.pizzashackId = pizzashackId;
			this.pizzaName = pizzaName;
			this.description = description;
			this.price = price;
			this.icon = icon;
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

		public Pizzashack build() {
			return new Pizzashack(pizzashackId, pizzaName, description, price, icon);
		}
	}
}
