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
import com.google.common.collect.Sets;

@JsonInclude(Include.NON_EMPTY)
public class Order extends AbstractNeo4jModel {
	private String orderNo;
	private Integer qty = 0;
	private BigDecimal totalPrice = BigDecimal.ZERO;
	private String address;
	private OrderStatus status;
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date orderTime;
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date deliverTime;
	private Set<OrderDetail> orderDetails = Collections.<OrderDetail> emptySet();
	private Customer customer;

	public void addOrderDetail(final OrderDetail detail) {
		if (orderDetails.isEmpty()) {
			orderDetails = Sets.<OrderDetail> newHashSet();
		}
		orderDetails.add(detail);
	}

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public static class Builder {
		private String orderNo;
		private Integer qty = 0;
		private BigDecimal totalPrice = BigDecimal.ZERO;
		private String address;
		private OrderStatus status;
		private Date orderTime;
		private Date deliverTime;
		private Set<OrderDetail> orderDetails = Collections.<OrderDetail> emptySet();
		private Customer customer;
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

		public Builder address(String address) {
			this.address = address;
			return this;
		}

		public Builder status(OrderStatus status) {
			this.status = status;
			return this;
		}

		public Builder orderTime(Date orderTime) {
			this.orderTime = orderTime;
			return this;
		}

		public Builder deliverTime(Date deliverTime) {
			this.deliverTime = deliverTime;
			return this;
		}

		public Builder orderDetails(Set<OrderDetail> orderDetails) {
			this.orderDetails = orderDetails;
			return this;
		}

		public Builder customer(Customer customer) {
			this.customer = customer;
			return this;
		}

		public Builder nodeUri(String nodeUri) {
			this.nodeUri = nodeUri;
			return this;
		}

		public Order build() {
			Order order = new Order();
			order.setAddress(address);
			order.setCustomer(customer);
			order.setDeliverTime(deliverTime);
			order.setNodeUri(nodeUri);
			order.setOrderDetails(orderDetails);
			order.setOrderNo(orderNo);
			order.setOrderTime(orderTime);
			order.setQty(qty);
			order.setStatus(status);
			order.setTotalPrice(totalPrice);
			return order;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.orderNo, ((Order) obj).orderNo)
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
				.append("nodeUri", nodeUri)
				.append("orderNo", orderNo)
				.append("qty", qty)
				.append("totalPrice", totalPrice)
				.append("address", address)
				.append("status", status)
				.append("orderTime", orderTime)
				.append("deliverTime", deliverTime)
				.toString();
	}

}
