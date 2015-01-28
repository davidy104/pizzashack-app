package nz.co.pizzashack.model;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class Customer extends Person {
	private String customerNo;
	private Date createTime;
	private Set<Order> orders = Collections.<Order> emptySet();

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public static class Builder {
		private String lastName;
		private String firstName;
		private Date birthDate;
		private String email;
		private User user;
		private String customerNo;
		private Date createTime;
		private Set<Order> orders = Collections.<Order> emptySet();
		private String nodeUri;

		public Builder nodeUri(String nodeUri) {
			this.nodeUri = nodeUri;
			return this;
		}

		public Builder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Builder birthDate(Date birthDate) {
			this.birthDate = birthDate;
			return this;
		}

		public Builder user(User user) {
			this.user = user;
			return this;
		}

		public Builder customerNo(String customerNo) {
			this.customerNo = customerNo;
			return this;
		}

		public Builder createTime(Date createTime) {
			this.createTime = createTime;
			return this;
		}

		public Builder orders(Set<Order> orders) {
			this.orders = orders;
			return this;
		}

		public Customer build() {
			Customer customer = new Customer();
			customer.setBirthDate(birthDate);
			customer.setCreateTime(createTime);
			customer.setCustomerNo(customerNo);
			customer.setEmail(email);
			customer.setFirstName(firstName);
			customer.setLastName(lastName);
			customer.setNodeUri(nodeUri);
			customer.setOrders(orders);
			customer.setUser(user);
			return customer;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.customerNo, ((Customer) obj).customerNo)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.customerNo)
				.toHashCode();

	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("nodeUri", nodeUri)
				.append("customerNo", customerNo)
				.append("createTime", createTime)
				.append("lastName", lastName)
				.append("firstName", firstName)
				.append("birthDate", birthDate)
				.append("email", email)
				.toString();
	}

}
