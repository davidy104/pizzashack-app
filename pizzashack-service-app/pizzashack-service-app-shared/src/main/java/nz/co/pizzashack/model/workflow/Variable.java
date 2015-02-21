package nz.co.pizzashack.model.workflow;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Variable {
	private String name;
	private String type;
	private String value;
	private String valueUrl;
	private String scope;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValueUrl() {
		return valueUrl;
	}
	public void setValueUrl(String valueUrl) {
		this.valueUrl = valueUrl;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder
				.append(this.valueUrl, ((Variable) obj).valueUrl)
				.append(this.name, ((Variable) obj).name)
				.append(this.type, ((Variable) obj).type)
				.append(this.scope, ((Variable) obj).scope)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder
				.append(this.valueUrl)
				.append(this.name)
				.append(this.type)
				.append(this.scope)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("valueUrl", valueUrl)
				.append("name", name)
				.append("type", type)
				.append("scope", scope)
				.toString();
	}
	
	public static class Builder {
		private String name;
		private String type;
		private String value;
		private String valueUrl;
		private String scope;

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder type(String type) {
			this.type = type;
			return this;
		}

		public Builder value(String value) {
			this.value = value;
			return this;
		}
		
		public Builder scope(String scope) {
			this.scope = scope;
			return this;
		}
		
		public Builder valueUrl(String valueUrl) {
			this.valueUrl = valueUrl;
			return this;
		}
		
		public Variable build() {
			Variable variable = new Variable();
			variable.setName(name);
			variable.setScope(scope);
			variable.setType(type);
			variable.setValue(value);
			variable.setValueUrl(valueUrl);
			return variable;
		}
	}
	
}
