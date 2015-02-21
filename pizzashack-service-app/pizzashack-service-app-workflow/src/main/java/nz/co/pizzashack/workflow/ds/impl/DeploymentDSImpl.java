package nz.co.pizzashack.workflow.ds.impl;

import java.io.File;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.workflow.Deployment;
import nz.co.pizzashack.model.workflow.DeploymentResource;
import nz.co.pizzashack.util.GeneralJsonRestClientAccessor;
import nz.co.pizzashack.workflow.ds.DeploymentDS;
import nz.co.pizzashack.workflow.ds.DeploymentQueryParameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DeploymentDSImpl implements DeploymentDS{

	private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentDSImpl.class);

	@Inject
	@Named("activitiGeneralJsonRestClientAccessor")
	private GeneralJsonRestClientAccessor activitiGeneralJsonRestClientAccessor;

	@Inject
	@Named("jacksonObjectMapper")
	private ObjectMapper jacksonObjectMapper;
	
	private final static String DEPLOYMENT_PATH="/repository/deployments/";
	
	@Override
	public Deployment deployment(String name, String category, File uploadFile) throws Exception {
		return null;
	}

	@Override
	public Deployment getDeployment(String name, String category) throws Exception {
		return null;
	}

	@Override
	public Deployment getDeployment(String deploymentId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unDeployment(String deploymentId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<DeploymentResource> getDeploymentResource(String deploymentId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Deployment> paginateDeployment(
			Map<DeploymentQueryParameter, String> deploymentQueryParameters,
			Integer pageOffset, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

}
