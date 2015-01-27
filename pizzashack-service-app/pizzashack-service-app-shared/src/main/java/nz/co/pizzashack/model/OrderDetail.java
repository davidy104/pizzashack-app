package nz.co.pizzashack.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
class OrderDetail extends AbstractNeo4jModel {
	private String orderNo;
	private Integer qty = 0;
	private BigDecimal totalPrice = BigDecimal.ZERO;
	private Pizzashack pizzashack;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Pizzashack getPizzashack() {
		return pizzashack;
	}

	public void setPizzashack(Pizzashack pizzashack) {
		this.pizzashack = pizzashack;
	}

	public static class Builder {
		private String orderNo;
		private Integer qty = 0;
		private BigDecimal totalPrice = BigDecimal.ZERO;
		private Pizzashack pizzashack;
		private String nodeUri;

		public Builder orderNo(String orderNo) {
			this.orderNo = orderNo;
			return this;
		}

		public Builder qty(Integer qty) {
			this.qty = qty;
			return this;
		}

		public Builder totalPrice(BigDecimal totalPrice) {
			this.totalPrice = totalPrice;
			return this;
		}

		public Builder pizzashack(Pizzashack pizzashack) {
			this.pizzashack = pizzashack;
			return this;
		}

		public Builder nodeUri(String nodeUri) {
			this.nodeUri = nodeUri;
			return this;
		}

		public OrderDetail build() {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setNodeUri(nodeUri);
			orderDetail.setOrderNo(orderNo);
			orderDetail.setPizzashack(pizzashack);
			orderDetail.setQty(qty);
			orderDetail.setTotalPrice(totalPrice);
			return orderDetail;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.orderNo, ((OrderDetail) obj).orderNo)
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
				.append("qty", qty)
				.append("totalPrice", totalPrice)
				.toString();
	}
}
