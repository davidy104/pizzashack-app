package nz.co.pizzashack.workflow.ds;

import java.io.File;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.workflow.Deployment;
import nz.co.pizzashack.model.workflow.DeploymentResource;
import nz.co.pizzashack.model.workflow.query.DeploymentQueryParameter;

public interface DeploymentDS {
	Deployment deployment(String name, String category, File uploadFile) throws Exception;
	Deployment getDeployment(String name,String category) throws Exception;
	Deployment getDeployment(String deploymentId) throws Exception;
	void unDeployment(String deploymentId) throws Exception;
	Set<DeploymentResource> getDeploymentResource(String deploymentId) throws Exception;
	Page<Deployment> paginateDeployment(Map<DeploymentQueryParameter, String> deploymentQueryParameters,Integer pageOffset,Integer pageSize);
}
