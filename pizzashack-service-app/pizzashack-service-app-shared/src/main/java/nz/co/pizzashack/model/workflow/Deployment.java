package nz.co.pizzashack.model.workflow;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Sets;

public class Deployment {
	private String id;
	private String name;
	//yyyy-MM-dd'T'HH:mm:ss.SSSXXX
	private Date deploymentTime;
	private String category;
	private String url;
	private String tenantId;
	private Set<DeploymentResource> deploymentResources = Collections.<DeploymentResource>emptySet();
	
	public void addDeploymentResources(final DeploymentResource deploymentResource){
		if(deploymentResources.isEmpty()){
			deploymentResources = Sets.<DeploymentResource>newHashSet();
		}
		deploymentResources.add(deploymentResource);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDeploymentTime() {
		return deploymentTime;
	}
	public void setDeploymentTime(Date deploymentTime) {
		this.deploymentTime = deploymentTime;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public Set<DeploymentResource> getDeploymentResources() {
		return deploymentResources;
	}
	public void setDeploymentResources(Set<DeploymentResource> deploymentResources) {
		this.deploymentResources = deploymentResources;
	}
	
	public static class Builder {
		private String id;
		private String name;
		private Date deploymentTime;
		private String category;
		private String url;
		private String tenantId;
		private Set<DeploymentResource> deploymentResources = Collections.<DeploymentResource>emptySet();

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder deploymentTime(Date deploymentTime) {
			this.deploymentTime = deploymentTime;
			return this;
		}
		
		public Builder category(String category) {
			this.category = category;
			return this;
		}
		
		public Builder url(String url) {
			this.url = url;
			return this;
		}
		public Builder tenantId(String tenantId) {
			this.tenantId = tenantId;
			return this;
		}
		public Builder deploymentResources(Set<DeploymentResource> deploymentResources) {
			this.deploymentResources = deploymentResources;
			return this;
		}
		
		public Deployment build() {
			Deployment deployment = new Deployment();
			deployment.setCategory(category);
			deployment.setDeploymentResources(deploymentResources);
			deployment.setDeploymentTime(deploymentTime);
			deployment.setId(id);
			deployment.setName(name);
			deployment.setTenantId(tenantId);
			deployment.setUrl(url);
			return deployment;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder
				.append(this.id, ((Deployment) obj).id)
				.append(this.name, ((Deployment) obj).name)
				.append(this.category, ((Deployment) obj).category)
				.append(this.url, ((Deployment) obj).url)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder
				.append(this.id)
				.append(this.name)
				.append(this.category)
				.append(this.url)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", id)
				.append("name", name)
				.append("category", category)
				.append("url", url)
				.append("tenantId", tenantId)
				.append("deploymentTime", deploymentTime)
				.toString();
	}
}
